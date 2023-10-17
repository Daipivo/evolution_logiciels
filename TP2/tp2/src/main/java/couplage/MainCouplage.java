package couplage;


import graph.CallGraph;
import graph.Pair;

import java.io.IOException;
import java.util.*;

public class MainCouplage {

    public static void main(String[] args) throws IOException {

        CallGraph graph = new CallGraph("/home/e20180003955/Bureau/evolution_logiciels/TP2/tp2");
        graph.start();

        Couplage couplage = new Couplage(graph);

        System.out.println(couplage.getCouplageGraph());

        // Si vous souhaitez afficher le graphe pondéré à l'aide de DisplayWeightedGraph
        // décommentez les lignes suivantes:
        // DisplayWeightedGraph display = new DisplayWeightedGraph();
        // display.displayGraph(couplage.getWeightedGraph());
        Map<Pair<String, String>, Double> weightedGraph = couplage.getWeightedGraph();
        weightedGraph.keySet().stream().forEach(paire -> System.out.println(paire.getFirst() + " - " + paire.getSecond() + " -> " + weightedGraph.get(paire)));

        ClusteringHierarchique clustering = new ClusteringHierarchique(weightedGraph);

        System.out.println(clustering.clusteringHierarchique());

    }


}
