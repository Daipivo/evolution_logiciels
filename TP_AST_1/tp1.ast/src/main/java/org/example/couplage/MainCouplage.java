package org.example.couplage;

import org.example.graph.CallGraph;

import java.io.IOException;

public class MainCouplage {

    public static void main(String[] args) throws IOException {

        CallGraph graph = new CallGraph("/home/reyne/Bureau/evolution_logiciels/Yoann/tp1.ast");
        graph.start();

        Couplage couplage = new Couplage(graph);

        System.out.println(couplage.getCouplage("ClassCountVisitor", "Parser"));

        System.out.println(couplage.getCouplageGraph());

    }
}
