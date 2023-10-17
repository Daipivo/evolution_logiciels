package couplage;

import graph.CallGraph;
import graph.Pair;

import java.util.*;

public class Couplage {

    private CallGraph graph;
    private double totalCoupling;  // Ajouté pour stocker le couplage total

    private Map<Pair<String, String>, Double> weightedGraph;

    public Couplage(CallGraph graph){
        this.graph = graph;
        // Initialisation du couplage total à l'instantiation de la classe
        this.totalCoupling = computeTotalCoupling();
        this.weightedGraph = computeWeightedGraph(); // Construction du graphe pondéré lors de l'initialisation

    }

    private Map<Pair<String, String>, Double> computeWeightedGraph() {
        Map<Pair<String, String>, Double> graph = new HashMap<>();

        List<String> classes = new ArrayList<>(this.graph.getGraphIntern().keySet());

        for(int i = 0; i < classes.size(); i++){
            double coupling = getCouplage(classes.get(i), classes.get(i));
            graph.put(new Pair<>(classes.get(i), classes.get(i)), coupling);
        }

        for (int i = 0; i < classes.size(); i++) {
            for (int j = i + 1; j < classes.size(); j++) {
                String cls1 = classes.get(i);
                String cls2 = classes.get(j);
                double coupling = getCouplage(cls1, cls2);
                graph.put(new Pair<>(cls1, cls2), coupling);
            }
        }

        return graph;
    }



    private double computeTotalCoupling() {
        return graph.getGraphIntern().keySet()
                .stream()
                .flatMap(cls1 -> graph.getGraphIntern().keySet()
                        .stream()
                        .map(cls2 -> {
                            double couplingValue = getRawCouplage(cls1, cls2);
                            return couplingValue;
                        }))
                .reduce(0.0, Double::sum);
    }


    public double getCouplageGraph() {
        Set<String> classes = graph.getGraphIntern().keySet();
        List<String> classList = new ArrayList<>(classes);
        double sum = 0.0;

        for (int i = 0; i < classList.size(); i++) {
            for (int j = i; j < classList.size(); j++) {  // Commencer à i pour éviter les redondances
                sum += getCouplage(classList.get(i), classList.get(j));

                if(i != j) { // Si ce n'est pas un couplage réflexif, ajoutez le couplage pour l'ordre inverse
                    sum += getCouplage(classList.get(i), classList.get(j));
                }
            }
        }

        return sum;
    }


    private double getRawCouplage(String class1, String class2) {

        Map<String, List<String>> class1Methods = graph.getCalledMethodsInClasse(class1);
        Map<String, List<String>> class2Methods = graph.getCalledMethodsInClasse(class2);

//        System.out.println(class1 + "  " +  class2);
//        System.out.println(class1Methods);

        long couplingsFromCls1ToCls2 = class1Methods.values().stream()
                .flatMap(List::stream)
                .filter(method -> method.startsWith(class2 + ":"))
                .count();

        long couplingsFromCls2ToCls1 = 0;

        if (!class1.equals(class2)) {  // Avoid calculating reflexive coupling twice
            couplingsFromCls2ToCls1 = class2Methods.values().stream()
                    .flatMap(List::stream)
                    .filter(method -> method.startsWith(class1 + ":"))
                    .count();
        }

        return (double)(couplingsFromCls1ToCls2 + couplingsFromCls2ToCls1) / (double)graph.getNbrAretesIntern();
    }

    public double getCouplage(String class1, String class2) {
        double rawCouplingValue = getRawCouplage(class1, class2);
        return rawCouplingValue / totalCoupling;
    }

    public Map<Pair<String, String>, Double> getWeightedGraph() {
        return this.weightedGraph;
    }
}



