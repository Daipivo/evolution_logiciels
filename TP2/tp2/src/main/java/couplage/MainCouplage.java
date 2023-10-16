package couplage;


import graph.CallGraph;
import graph.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainCouplage {

    public static void main(String[] args) throws IOException {

        CallGraph graph = new CallGraph("/home/e20180003955/Bureau/evolution_logiciels/TP2/tp2");
        graph.start();

        System.out.println(graph.getNbrAretesIntern());

        Couplage couplage = new Couplage(graph);

        System.out.println(couplage.getCouplageGraph());

        DisplayWeightedGraph display = new DisplayWeightedGraph();
        display.displayGraph(couplage.getWeightedGraph());

//        couplage.getWeightedGraph().forEach((pair, value) -> {
//            String class1 = pair.getFirst();
//            String class2 = pair.getSecond();
//            double couplingValue = value;
//
//            // Votre logique ici.
//            System.out.println("Couplage entre " + class1 + " et " + class2 + " est : " + couplingValue);
//        });

    }


}
