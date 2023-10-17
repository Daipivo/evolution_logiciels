package couplage;

import graph.Pair;

import java.util.*;

public class ClusteringHierarchique {

    private Map<Pair<String, String>, Double> weightedGraph;

    public ClusteringHierarchique(Map<Pair<String, String>, Double> weightedGraph) {
        this.weightedGraph = weightedGraph;
    }

    private void addClusterIfNotExists(Set<Cluster> clusters, String vertex) {
        for (Cluster existingCluster : clusters) {
            if (existingCluster.getClasses().contains(vertex)) {
                return; // Le cluster avec ce sommet existe déjà
            }
        }
        clusters.add(new Cluster(vertex, 0)); // Ajoute un nouveau cluster si aucun match n'a été trouvé
    }


    public Set<Cluster> clusteringHierarchique() {

        Set<Cluster> dendro = new HashSet<>();
        Set<Cluster> clusters = new HashSet<>();

        // Initialisez les clusters individuels
        for (Pair<String, String> edge : weightedGraph.keySet()) {
            addClusterIfNotExists(clusters, edge.getFirst());
            addClusterIfNotExists(clusters, edge.getSecond());
        }

        System.out.println("=====");
        clusters.stream().forEach(cluster -> System.out.println(cluster.getClasses()));
        System.out.println("=====");



        while (clusters.size() > 1) {
            double degreMax = Double.NEGATIVE_INFINITY;
            Pair<String, String> pairMax = null;
            Cluster clusterMax = null;

            for (Pair<String, String> pair : weightedGraph.keySet()) {
                if (weightedGraph.get(pair) > degreMax) {
                    pairMax = pair;
                    degreMax = weightedGraph.get(pair);
                }
            }

            for (Cluster cluster : clusters){
                if(cluster.getWeight() > degreMax){
                    clusterMax = cluster;
                    degreMax = cluster.getWeight();
                }
            }

            if (pairMax == null) {
                break;  // Toutes les distances sont probablement négatives, ce qui est inattendu. Sortez de la boucle.
            }
            /*
            List<String> classes = new ArrayList<>();
            classes.add(pairMax.getFirst());
            classes.add(pairMax.getSecond());

            final String firstClass = pairMax.getFirst();
            final String secondClass = pairMax.getSecond();

            Cluster mergedCluster = new Cluster(classes, degreMax);

            // Supprimez les anciens clusters
            clusters.removeIf(cluster -> cluster.getClasses().contains(firstClass) || cluster.getClasses().contains(secondClass));

            // Supprimez la paire maximale de weightedGraph pour éviter de la retrouver à la prochaine itération
            weightedGraph.remove(pairMax);

            // Ajoutez le nouveau cluster fusionné
            clusters.add(mergedCluster);

            // (Optionnel) Mettez à jour le weightedGraph ici si nécessaire

            // Ajoutez le nouveau cluster au dendrogramme
            dendro.add(mergedCluster);
        }
    */
        return dendro;
    }
}
