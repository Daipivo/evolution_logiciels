package spoon.couplage;

import graph.Pair;

import java.util.HashMap;
import java.util.Map;

public enum Instance {
    EXEMPLE_RAPPORT(new HashMap<>() {{
        put(new Pair<>("ClassA", "ClassB"), 0.375);
        put(new Pair<>("ClassA", "ClassC"), 0.25);
        put(new Pair<>("ClassB", "ClassC"), 0.25);
        put(new Pair<>("ClassC", "ClassC"), 0.125);
    }}, 3),
    TP_INSTANCE(new HashMap<>() {{
        put(new Pair<>("A", "C"), 0.3913);
        put(new Pair<>("B", "D"), 0.3043);
        put(new Pair<>("E", "C"), 0.1304);
        put(new Pair<>("E", "A"), 0.0435);
        put(new Pair<>("E", "D"), 0.1304);
    }}, 5);

    private final Map<Pair<String, String>, Double> weightedGraph;
    private final int nbrClasses;

    Instance(Map<Pair<String, String>, Double> weightedGraph, int nbrClasses) {
        this.weightedGraph = weightedGraph;
        this.nbrClasses = nbrClasses;
    }

    public Map<Pair<String, String>, Double> getWeightedGraph() {
        return weightedGraph;
    }

    public int getNbrClasses(){
        return nbrClasses;
    }

}
