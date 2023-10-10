package graph;

import java.io.IOException;


public class MainGraph {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Projet à tester
        CallGraph graph = new CallGraph("/home/reyne/Bureau/evolution_logiciels/TP1/tp1.ast");

        // Lancement du graph
        graph.start();

        DisplayGraph displayGraph = new DisplayGraph(graph);

        displayGraph.displayGraph();

    }

}
