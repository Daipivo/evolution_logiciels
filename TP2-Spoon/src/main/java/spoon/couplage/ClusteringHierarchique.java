package spoon.couplage;


import graph.Pair;

import java.util.*;

public class ClusteringHierarchique {

    private Map<Pair<String, String>, Double> weightedGraph;

    private Set<Cluster> dendrogramme = new LinkedHashSet<>();

    /**
     * Constructeur de la classe ClusteringHierarchique.
     *
     * @param weightedGraph Le graphe pondéré de l'application.
     */
    public ClusteringHierarchique(Map<Pair<String, String>, Double> weightedGraph) {
        this.weightedGraph = weightedGraph;
        this.dendrogramme = clusteringHierarchique();
    }

    /**
     * Ajoute un cluster s'il n'existe pas déjà.
     *
     * @param clusters Ensemble de clusters existants.
     * @param vertex Le sommet pour vérifier / ajouter.
     */
    private void addClusterIfNotExists(Set<Cluster> clusters, String vertex) {
        for (Cluster existingCluster : clusters) {
            if (existingCluster.getClasses().contains(vertex)) {
                return; // Le cluster avec ce sommet existe déjà
            }
        }
        clusters.add(new Cluster(vertex)); // Ajoute un nouveau cluster si aucun match n'a été trouvé
    }

    /**
     * Calcule le couplage entre deux clusters.
     *
     * @param c1 Le premier cluster.
     * @param c2 Le deuxième cluster.
     * @return Le couplage entre les clusters c1 et c2.
     */
    public double couplageBtwClusters(Cluster c1, Cluster c2){
        Set<String> classesC1 = c1.getClasses();
        Set<String> classesC2 = c2.getClasses();
        double result = 0.00;

        for (String class1 : classesC1) {
            for (String class2 : classesC2) {
                Pair<String, String> pair1 = new Pair<>(class1, class2);
                Pair<String, String> pair2 = new Pair<>(class2, class1);
                if(weightedGraph.containsKey(pair1)) {
                    result += weightedGraph.get(pair1);
                }
                else if(weightedGraph.containsKey(pair2)) {
                    result += weightedGraph.get(pair2);
                }
            }
        }
        return result;
    }

    /**
     * Effectue un clustering hiérarchique et retourne un ensemble de clusters.
     *
     * @return Un ensemble représentant la dendrogramme de clusters.
     */
    private Set<Cluster> clusteringHierarchique() {
        Set<Cluster> dendro = new LinkedHashSet<>();
        Set<Cluster> clusters = new HashSet<>();

        // Initialisez les clusters individuels
        for (Pair<String, String> edge : weightedGraph.keySet()) {
            addClusterIfNotExists(clusters, edge.getFirst());
            addClusterIfNotExists(clusters, edge.getSecond());
        }

        dendro.addAll(clusters);

        while (clusters.size() > 1) {
            double couplageMax = -1;
            Cluster clusterMax1 = null;
            Cluster clusterMax2 = null;

            List<Cluster> clusterList = new ArrayList<>(clusters);

            for (int i = 0; i < clusterList.size() - 1; i++) {
                for (int j = i + 1; j < clusterList.size(); j++) {
                    double currentCouplage = couplageBtwClusters(clusterList.get(i), clusterList.get(j));
                    if (currentCouplage > couplageMax) {
                        couplageMax = currentCouplage;
                        clusterMax1 = clusterList.get(i);
                        clusterMax2 = clusterList.get(j);
                    }
                }
            }

            Cluster clusterMax = new Cluster(clusterMax1, clusterMax2);
            clusters.remove(clusterMax1);
            clusters.remove(clusterMax2);
            clusters.add(clusterMax);
            dendro.add(clusterMax);
        }

        return dendro;
    }

    public Set<Cluster> getDendrogramme(){
        return dendrogramme;
    }

    /**
     * Retourne une représentation sous forme de chaîne de la classe ClusteringHierarchique.
     *
     * @return une chaîne de caractères représentant l'objet ClusteringHierarchique.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Clustering Hierarchique {\n\tDendrogramme :\n");

        // Convertir le Set en List pour conserver l'ordre d'insertion
        List<Cluster> clustersList = new ArrayList<>(dendrogramme);

        // Parcourir la liste en sens inverse
        for (int i = clustersList.size() - 1; i >= 0; i--) {
            builder.append("\t\t").append(clustersList.get(i)).append("\n");
        }

        builder.append("}");
        return builder.toString();
    }
}
