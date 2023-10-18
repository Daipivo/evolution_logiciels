package couplage;

import graph.CallGraph;
import graph.Pair;

import java.util.*;

public class Couplage {

    private CallGraph graph;
    private Map<Pair<String, String>, Double> weightedGraph;
    private double totalCoupling = 0.00;

    public List<Pair<String, String>> test = new ArrayList<>();

    public Couplage(CallGraph graph){
        this.graph = graph;
        this.totalCoupling = graph.getNbrAretesIntern();
    }

    public Map<Pair<String, String>, Double> computeWeightedGraph() {

        Map<Pair<String, String>, Double> graphResult = new HashMap<>();
        List<String> classes = new ArrayList<>(this.graph.getGraphIntern().keySet());

        for(int i = 0; i<classes.size();i++){
            double coupling = calculateCouplingBtwClasses(classes.get(i), classes.get(i));
            graphResult.put(new Pair<>(classes.get(i), classes.get(i)), coupling);
        }


        for (int i = 0; i < classes.size() - 1; i++) { // Notez le changement ici
            for (int j = i + 1; j < classes.size(); j++) { // Et ici

                String cls1 = classes.get(i);
                String cls2 = classes.get(j);

                double coupling = calculateCouplingBtwClasses(cls1, cls2);

                graphResult.put(new Pair<>(cls1, cls2), coupling);
            }
        }

        return graphResult;
    }
    private double calculateCouplingBtwClasses(String class1, String class2) {

        Map<String, List<String>> class1Methods = graph.getCalledMethodsInClasse(class1);
        Map<String, List<String>> class2Methods = graph.getCalledMethodsInClasse(class2);


        long couplingsFromCls1ToCls2 = 0;
        for (Map.Entry<String, List<String>> entry : class1Methods.entrySet()) {
            String keyMethodName = entry.getKey();
            List<String> methods = entry.getValue();

            for (String method : methods) {
                if (method.startsWith(class2 + ":")) {
                    couplingsFromCls1ToCls2++;
                    test.add(new Pair<>(class1 + ":" + keyMethodName, method));
                }
            }
        }

        if(class1.equals(class2)) {
            return couplingsFromCls1ToCls2 / totalCoupling; // Retourner uniquement le nombre d'appels internes si les deux classes sont identiques.
        }

        long couplingsFromCls2ToCls1 = 0;
        for (Map.Entry<String, List<String>> entry : class2Methods.entrySet()) {
            String keyMethodName = entry.getKey();
            List<String> methods = entry.getValue();

            for (String method : methods) {
                if (method.startsWith(class1 + ":")) {
                    couplingsFromCls2ToCls1++;


                    test.add(new Pair<>(class2 + ":" + keyMethodName, method));
                }
            }
        }



        return ( couplingsFromCls1ToCls2 + couplingsFromCls2ToCls1 ) / totalCoupling;
    }

    public Map<Pair<String, String>, Double> getWeightedGraph() {
        return this.weightedGraph;
    }
}


