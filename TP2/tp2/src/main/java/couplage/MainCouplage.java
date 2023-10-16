package couplage;


import graph.CallGraph;
import graph.Pair;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class MainCouplage {

    public static void main(String[] args) throws IOException {

        CallGraph graph = new CallGraph("/home/reyne/Bureau/evolution_logiciels/TP2/tp2");
        graph.start();

        System.out.println(graph.getNbrAretesIntern());
//        graph.getAretesIntern().stream().forEach(p -> System.out.println(p.getFirst() + " ==> " + p.getSecond()));

        Couplage couplage = new Couplage(graph);

//        System.out.println(couplage.getCouplage("clsTest1", "clsTest2"));
//
        System.out.println(couplage.getCouplageGraph());

//        System.out.println(couplage.getCpt());

        System.out.println(graph.getAretesIntern().size());
        System.out.println(couplage.getTest().size());
//
//
//        Set<Pair<String, String>> difference = new HashSet<>();
//
//        for(Pair<String, String> pTest : couplage.getTest()) {
//            boolean isFound = false;
//            for(Pair<String, String> pIntern : graph.getAretesIntern()) {
//                if((pTest.getFirst().equals(pIntern.getFirst()) && pTest.getSecond().equals(pIntern.getSecond())) ||
//                        (pTest.getFirst().equals(pIntern.getSecond()) && pTest.getSecond().equals(pIntern.getFirst()))) {
//                    isFound = true;
//                    break;
//                }
//            }
//            if(!isFound) {
//                difference.add(pTest);
//            }
//        }

        System.out.println("================>1");
        graph.getAretesIntern()
                .stream().forEach(p-> System.out.println(p.getFirst() + " " + p.getSecond()));
//
//
//        System.out.println("================>2");
//        couplage.getTest()
//                .stream().forEach(p-> System.out.println(p.getFirst() + " " + p.getSecond()));

//
//        System.out.println("================>3");
//        difference
//                .stream().forEach(p-> System.out.println(p.getFirst() + " " + p.getSecond()));
//

    }


}
