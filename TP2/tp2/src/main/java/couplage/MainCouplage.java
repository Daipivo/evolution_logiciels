package couplage;


import graph.CallGraph;

import java.io.IOException;

public class MainCouplage {

    public static void main(String[] args) throws IOException {

        CallGraph graph = new CallGraph("/home/e20180003955/Bureau/evolution_logiciels/TP2/tp2");
        graph.start();

        Couplage couplage = new Couplage(graph);

        System.out.println(couplage.getCouplage("ClassCountVisitor", "Parser"));

        System.out.println(couplage.getCouplageGraph());

    }
}
