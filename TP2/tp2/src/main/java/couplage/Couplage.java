package couplage;

import graph.CallGraph;
import graph.Pair;

import java.util.*;

public class Couplage {

    private CallGraph graph;
    private Map<Pair<String, String>, Double> weightedGraph;
    private double totalCoupling;

    /**
     * Constructeur de la classe Couplage.
     *
     * @param graph Le graphe des appels à initialiser.
     */
    public Couplage(CallGraph graph){
        this.graph = graph;
        this.totalCoupling = graph.getNbrAretesIntern();
        this.weightedGraph = computeWeightedGraph();
    }

    /**
     * Calcule le graphe pondéré.
     *
     * @return Retourne un Map contenant des paires de classes et leur couplage pondéré.
     */
    public Map<Pair<String, String>, Double> computeWeightedGraph() {

        Map<Pair<String, String>, Double> graphResult = new HashMap<>();
        List<String> classes = new ArrayList<>(this.graph.getGraphIntern().keySet());

        for(int i = 0; i<classes.size();i++){
            double coupling = calculateCouplingBtwClasses(classes.get(i), classes.get(i));
            graphResult.put(new Pair<>(classes.get(i), classes.get(i)), coupling);
        }


        for (int i = 0; i < classes.size() - 1; i++) {
            for (int j = i + 1; j < classes.size(); j++) {

                String cls1 = classes.get(i);
                String cls2 = classes.get(j);

                double coupling = calculateCouplingBtwClasses(cls1, cls2);

                graphResult.put(new Pair<>(cls1, cls2), coupling);
            }
        }

        return graphResult;
    }

    /**
     * Calcule le couplage entre deux classes.
     *
     * @param class1 La première classe.
     * @param class2 La deuxième classe.
     * @return Retourne le couplage entre class1 et class2.
     */
    private double calculateCouplingBtwClasses(String class1, String class2) {

        Map<String, List<String>> class1Methods = graph.getCalledMethodsInClasse(class1);
        Map<String, List<String>> class2Methods = graph.getCalledMethodsInClasse(class2);


        long couplingsFromCls1ToCls2 = 0;
        for (Map.Entry<String, List<String>> entry : class1Methods.entrySet()) {
            List<String> methods = entry.getValue();

            for (String method : methods) {
                if (method.startsWith(class2 + ":")) {
                    couplingsFromCls1ToCls2++;
                }
            }
        }

        if(class1.equals(class2)) {
            return couplingsFromCls1ToCls2 / totalCoupling;
        }

        long couplingsFromCls2ToCls1 = 0;
        for (Map.Entry<String, List<String>> entry : class2Methods.entrySet()) {
            List<String> methods = entry.getValue();

            for (String method : methods) {
                if (method.startsWith(class1 + ":")) {
                    couplingsFromCls2ToCls1++;
                }
            }
        }



        return ( couplingsFromCls1ToCls2 + couplingsFromCls2ToCls1 ) / totalCoupling;
    }

    /**
     * Obtient le graphe pondéré.
     *
     * @return Retourne le graphe pondéré.
     */
    public Map<Pair<String, String>, Double> getWeightedGraph() {
        return this.weightedGraph;
    }

    /**
     * Retourne une représentation sous forme de chaîne de la classe Couplage.
     *
     * @return une chaîne de caractères représentant l'objet Couplage.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Couplage {\n");
        sb.append("\tTotal Coupling: ").append(totalCoupling).append("\n");
        sb.append("\tWeighted Graph: \n");
        for (Map.Entry<Pair<String, String>, Double> entry : weightedGraph.entrySet()) {
            sb.append("\t\t").append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        sb.append("}");

        return sb.toString();
    }
}


