package couplage;


import graph.CallGraph;
import graph.Pair;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

public class MainCouplage {

    public static void main(String[] args) throws IOException {

        CallGraph graph = new CallGraph("/home/reyne/Bureau/evolution_logiciels/TP2/tp2");
        graph.start();

        Couplage couplage = new Couplage(graph);

        Map<Pair<String, String>, Double> weightedGraph = couplage.computeWeightedGraph();

        ClusteringHierarchique clustering = new ClusteringHierarchique(weightedGraph);
        Set<Cluster> dendro = clustering.clusteringHierarchique();

//
//        double totalWeight = weightedGraph
//                .values()
//                .stream()
//                .mapToDouble(Double::doubleValue).sum();
//
//        System.out.println("Total Weight: " + totalWeight);


//        System.out.println(couplage.getTotalCoupling());

        dendro
                .stream()
                .forEach(cluster -> System.out.println(cluster));

    }


}
