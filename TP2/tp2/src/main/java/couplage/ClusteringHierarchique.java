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
        clusters.add(new Cluster(vertex)); // Ajoute un nouveau cluster si aucun match n'a été trouvé
    }

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

    public Set<Cluster> clusteringHierarchique() {
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

            Double couplageCluster = clusterMax1.getWeight() + clusterMax2.getWeight() + couplageMax;
            Cluster clusterMax = new Cluster(clusterMax1, clusterMax2, couplageCluster);
            clusters.remove(clusterMax1);
            clusters.remove(clusterMax2);
            clusters.add(clusterMax);
            dendro.add(clusterMax);
        }

        return dendro;
    }
}
