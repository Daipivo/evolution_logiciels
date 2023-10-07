package org.example.graph;

import java.io.IOException;

public class MainGraph {

    public static void main(String[] args) throws IOException{


        // Projet à tester
        CallGraph graph = new CallGraph("C:\\Users\\Sandratra\\Desktop\\Projet M1 UM\\HAI712I - Ingénierie Logicielle\\TP4-EauDuBidon");

        // Lancement du graph
        graph.start();

        System.out.println(graph);

    }

}
