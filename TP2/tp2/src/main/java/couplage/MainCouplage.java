package couplage;


import graph.CallGraph;
import graph.Pair;
import scala.util.parsing.combinator.testing.Str;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

public class MainCouplage {

    public static void main(String[] args) throws IOException {

        CallGraph graph = new CallGraph("/home/reyne/Bureau/evolution_logiciels/TP2/tp2");

        List<String> classes = graph.getClasses();

        Couplage couplage = new Couplage(graph);

        // Graphe de couplage
        Map<Pair<String, String>, Double> weightedGraph = couplage.getWeightedGraph();

        // Affichage du graphe de couplage
        DisplayWeightedGraph displayWeightedGraph = new DisplayWeightedGraph(couplage);
        displayWeightedGraph.displayGraph();


//        ClusteringHierarchique clustering = new ClusteringHierarchique(weightedGraph);
//        System.out.println(clustering);

        // DENDROGRAMME PRESENT SUR LE TP
        Map<Pair<String, String>, Double> weightedGraphtest = new HashMap<>();

        weightedGraphtest.put(new Pair<>("A", "C"), 0.3913);
        weightedGraphtest.put(new Pair<>("B", "D"), 0.3043);
        weightedGraphtest.put(new Pair<>("E", "C"), 0.1304);
        weightedGraphtest.put(new Pair<>("E", "A"), 0.0435);
        weightedGraphtest.put(new Pair<>("E", "D"), 0.1304);


        ClusteringHierarchique clusteringtest = new ClusteringHierarchique(weightedGraphtest);

        System.out.println(clusteringtest);

        // Identification de modules sur le graphe du TP
        ModuleIdentifier moduleIdentifier = new ModuleIdentifier(clusteringtest, weightedGraphtest, 0.2, 5);
        System.out.println(moduleIdentifier);



    }


}
