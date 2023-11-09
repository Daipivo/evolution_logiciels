package graph;

import java.io.IOException;


public class MainGraph {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Projet à tester
        CallGraph graph = new CallGraph("home/e20220012486/Semestre1/ingenierie Logiciel/EauDuBidons/");

        // Lancement du graph
        graph.start();

        DisplayGraph displayGraph = new DisplayGraph(graph);

        displayGraph.displayGraph();

    }

}
