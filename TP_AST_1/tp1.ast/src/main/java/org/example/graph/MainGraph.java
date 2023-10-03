package org.example.graph;

import java.io.IOException;

public class MainGraph {

    public static void main(String[] args) throws IOException{


        // Projet à tester
        CallGraph graph = new CallGraph("/home/e20180003955/Bureau/evolution_logiciels/Yoann/tp1.ast");

        // Lancement du graph
        graph.start();

        System.out.println(graph);

    }

}
