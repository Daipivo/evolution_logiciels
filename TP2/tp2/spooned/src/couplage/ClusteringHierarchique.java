package couplage;
import graph.Pair;
import java.util.*;
public class ClusteringHierarchique {
    private Map<Pair<String, String>, Double> weightedGraph;

    private Set<Cluster> dendrogramme = new HashSet<>();

    /**
     * Constructeur de la classe ClusteringHierarchique.
     *
     * @param weightedGraph
     * 		Le graphe pondéré de l'application.
     */
    public ClusteringHierarchique(Map<Pair<String, String>, Double> weightedGraph) {
        this.weightedGraph = weightedGraph;
        this.dendrogramme = clusteringHierarchique();
    }

    /**
     * Ajoute un cluster s'il n'existe pas déjà.
     *
     * @param clusters
     * 		Ensemble de clusters existants.
     * @param vertex
     * 		Le sommet pour vérifier / ajouter.
     */
    private void addClusterIfNotExists(Set<Cluster> clusters, String vertex) {
        for (Cluster existingCluster : clusters) {
            if (existingCluster.getClasses().contains(vertex)) {
                return;// Le cluster avec ce sommet existe déjà

            }
        }
        clusters.add(new Cluster(vertex));// Ajoute un nouveau cluster si aucun match n'a été trouvé

    }

    /**
     * Calcule le couplage entre deux clusters.
     *
     * @param c1
     * 		Le premier cluster.
     * @param c2
     * 		Le deuxième cluster.
     * @return Le couplage entre les clusters c1 et c2.
     */
    public double couplageBtwClusters(Cluster c1, Cluster c2) {
        Set<String> classesC1 = c1.getClasses();
        Set<String> classesC2 = c2.getClasses();
        double result = 0.0;
        for (String class1 : classesC1) {
            for (String class2 : classesC2) {
                Pair<String, String> pair1 = new Pair<>(class1, class2);
                Pair<String, String> pair2 = new Pair<>(class2, class1);
                if (weightedGraph.containsKey(pair1)) {
                    result += weightedGraph.get(pair1);
                } else if (weightedGraph.containsKey(pair2)) {
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
        Set<Cluster> dendro = new HashSet<>();
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
            for (int i = 0; i < (clusterList.size() - 1); i++) {
                for (int j = i + 1; j < clusterList.size(); j++) {
                    double currentCouplage = couplageBtwClusters(clusterList.get(i), clusterList.get(j));
                    if (currentCouplage > couplageMax) {
                        couplageMax = currentCouplage;
                        clusterMax1 = clusterList.get(i);
                        clusterMax2 = clusterList.get(j);
                    }
                }
            }
            Double couplageCluster = (clusterMax1.getWeight() + clusterMax2.getWeight()) + couplageMax;
            Cluster clusterMax = new Cluster(clusterMax1, clusterMax2, couplageCluster);
            clusters.remove(clusterMax1);
            clusters.remove(clusterMax2);
            clusters.add(clusterMax);
            dendro.add(clusterMax);
        } 
        return dendro;
    }

    public Set<Cluster> getDendrogramme() {
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
        // Convertir le dendrogramme en liste pour le tri
        List<Cluster> sortedClusters = new ArrayList<>(clusteringHierarchique());
        // Trier la liste en fonction du poids, de manière décroissante
        sortedClusters.sort((c1, c2) -> Double.compare(c2.getClasses().size(), c1.getClasses().size()));// Si Cluster a une méthode getWeight()

        builder.append("ClusteringHierarchique {\n\tDendrogram:\n");
        for (Cluster cluster : sortedClusters) {
            builder.append("\t\tClasses: ").append(cluster.getClasses()).append(", Weight: ").append(cluster.getWeight()).append("\n");
        }
        builder.append("}");
        return builder.toString();
    }
}