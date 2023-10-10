package couplage;


import graph.CallGraph;

import java.io.IOException;

public class MainCouplage {

    public static void main(String[] args) throws IOException {

        CallGraph graph = new CallGraph("/home/e20180003955/Bureau/evolution_logiciels/TP2/tp2");
        graph.start();

        System.out.println(graph.getNbrAretesIntern());
        graph.getAretesIntern().stream().forEach(p -> System.out.println(p.getFirst() + " ==> " + p.getSecond()));

        Couplage couplage = new Couplage(graph);

//        System.out.println(couplage.getCouplage("clsTest1", "clsTest2"));
//
        System.out.println(couplage.getCouplageGraph());

        System.out.println(couplage.getCpt());

    }
}
