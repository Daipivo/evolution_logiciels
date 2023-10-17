package couplage;

import graph.Pair;

import java.util.*;

public class ClusteringHierarchique {

    private Map<Pair<String, String>, Double> weightedGraph;

    public ClusteringHierarchique(Map<Pair<String, String>, Double> weightedGraph) {
        this.weightedGraph = weightedGraph;
    }

    public Set<Cluster> clusteringHierarchique() {

        Set<Cluster> dendro = new HashSet<>();
        Set<Cluster> clusters = new HashSet<>();

        for (Pair<String, String> edge : weightedGraph.keySet()) {
            clusters.add(new Cluster(edge.getFirst()));
            clusters.add(new Cluster(edge.getSecond()));
            dendro.add(new Cluster(edge.getFirst()));
            dendro.add(new Cluster(edge.getSecond()));

        }

        while (clusters.size() > 1){


            double degreMax = -1;
            Pair pairMax = new Pair();

            for (Pair<String, String> pair : weightedGraph.keySet()) {

                if(weightedGraph.get(pair) > degreMax){
                    pairMax = pair;
                    degreMax = weightedGraph.get(pair);
                }

            }

            ArrayList<String> classes = new ArrayList<>();
            classes.add(pairMax.getFirst().toString());
            classes.add(pairMax.getSecond().toString());

            Cluster cluster = new Cluster(classes, degreMax);

            System.out.println(clusters);

            for(Cluster c : clusters){

                System.out.println(c.getClasses());

                if(c.getClasses().equals(cluster.getClasses())){

                    clusters.remove(c);

                }

            }

            dendro.add(cluster);

        }



        return dendro;
    }
}
