package couplage;


import graph.CallGraph;
import graph.Pair;
import scala.util.parsing.combinator.testing.Str;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

public class MainCouplage {

    public static void main(String[] args) throws IOException {

        /*
        Instance du projet courant
         */

        CallGraph graph = new CallGraph("/home/reyne/Bureau/org.anonbnr.design_patterns-main");

        Couplage couplage = new Couplage(graph);

        // Affichage du graphe de couplage
//        DisplayWeightedGraph displayWeightedGraph = new DisplayWeightedGraph(couplage);
//        displayWeightedGraph.displayGraph();

        /*
        Instance utilisé pour le rapport
         */
        Map<Pair<String, String>, Double> weightedGraphExempleRapport = Instance.EXEMPLE_RAPPORT.getWeightedGraph();

        ClusteringHierarchique clusteringExempleRapport = new ClusteringHierarchique(couplage.getWeightedGraph());
        System.out.println(clusteringExempleRapport);

        ModuleIdentifier moduleIdentifier = new ModuleIdentifier(clusteringExempleRapport, couplage.getWeightedGraph(), 0.2, 3);
        System.out.println(moduleIdentifier);

        /*
        Instance présent sur le TP
         */
        Map<Pair<String, String>, Double> weightedGraphTP = Instance.TP_INSTANCE.getWeightedGraph();

        ClusteringHierarchique clusteringTP = new ClusteringHierarchique(weightedGraphTP);
        // Affichage du dendrogramme de l'instance du TP
        System.out.println(clusteringTP);

        // Identification de modules sur le graphe du TP
        ModuleIdentifier moduleIdentifierTP = new ModuleIdentifier(clusteringTP, weightedGraphTP, 0.2, 5);
        System.out.println(moduleIdentifierTP);



    }


}
