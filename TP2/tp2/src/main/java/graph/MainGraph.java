package graph;

import java.io.IOException;


public class MainGraph {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Projet à tester
        CallGraph graph = new CallGraph("C:\\Users\\Sandratra\\Desktop\\Projet M2 UM\\evolution_logiciels\\TP2\\tp2");

        // Lancement du graph
        graph.start();

        DisplayGraph displayGraph = new DisplayGraph(graph);

        displayGraph.displayGraph();

    }

}
