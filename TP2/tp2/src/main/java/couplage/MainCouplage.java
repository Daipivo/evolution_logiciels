package couplage;


import graph.CallGraph;
import graph.Pair;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

public class MainCouplage {

    public static void main(String[] args) throws IOException {

        CallGraph graph = new CallGraph("/home/reyne/Bureau/org.anonbnr.design_patterns-main");
        graph.start();

        Couplage couplage = new Couplage(graph);

        Map<Pair<String, String>, Double> weightedGraph = couplage.computeWeightedGraph();

        ClusteringHierarchique clustering = new ClusteringHierarchique(weightedGraph);
        Set<Cluster> dendro = clustering.clusteringHierarchique();




        double totalWeight = weightedGraph
                .values()
                .stream()
                .mapToDouble(Double::doubleValue).sum();

        System.out.println("Total Weight: " + totalWeight);

        System.out.println(graph.getNbrAretesIntern());
        System.out.println(couplage.test.size());

        Set<Pair<String, String>> diff = new HashSet<>();

        Map<Pair<String, String>, Integer> occurrenceInGraphIntern = new HashMap<>();
        Map<Pair<String, String>, Integer> occurrenceInCouplageTest = new HashMap<>();

// Initialiser le comptage pour graph.getAretesIntern()
        for (Pair<String, String> pairGraphe : graph.getAretesIntern()) {
            occurrenceInGraphIntern.put(pairGraphe, occurrenceInGraphIntern.getOrDefault(pairGraphe, 0) + 1);
        }

// Initialiser le comptage pour couplage.test
        for (Pair<String, String> testPair : couplage.test) {
            occurrenceInCouplageTest.put(testPair, occurrenceInCouplageTest.getOrDefault(testPair, 0) + 1);
        }

// Vérifier et imprimer les divergences
        for (Pair<String, String> pair : occurrenceInGraphIntern.keySet()) {
            Integer countInGraphIntern = occurrenceInGraphIntern.get(pair);
            Integer countInCouplageTest = occurrenceInCouplageTest.getOrDefault(pair, 0); // Utilisez getOrDefault pour éviter null

            if (!countInGraphIntern.equals(countInCouplageTest)) {
                System.out.println("Divergence for " + pair.getFirst() + " " + pair.getSecond() + ": " +
                        "Occurs " + countInGraphIntern + " times in graph.getAretesIntern() and " +
                        countInCouplageTest + " times in couplage.test.");
            }
        }

        diff.stream().forEach(stringStringPair -> System.out.println(stringStringPair.getFirst() + " -- " + stringStringPair.getSecond()));
//        System.out.println(couplage.getTotalCoupling());

//        dendro
//                .stream()
//                .forEach(cluster -> System.out.println(cluster));

    }


}
