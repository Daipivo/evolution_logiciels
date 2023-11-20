package spoon.couplage;
import graph.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleFunction;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;
public class SpoonCouplage {
    private CtModel model;

    private List<CtClass<?>> classes;

    private double TotalCoupling = 0;

    Map<Pair<String, String>, Double> couplingMetrics;

    Map<Pair<String, String>, Double> couplage;

    public Map<Pair<String, String>, Double> getWeightedGraph() {
        return couplage;
    }

    public double getTotalCoupling() {
        return TotalCoupling;
    }

    public void setTotalCoupling(double totalCoupling) {
        TotalCoupling = totalCoupling;
    }

    public SpoonCouplage(CtModel model) {
        this.model = model;
        this.classes = getClasses();
        couplingMetrics = calculateCouplingMetricsForAllClasses();
        this.couplage = calculateCouplingMetric(couplingMetrics);
    }

    public List<String> getStringClasses() {
        List<String> classes = new ArrayList<>();
        for (CtClass<?> ctClass : this.model.getElements(new TypeFilter<>(CtClass.class))) {
            String className = ctClass.getQualifiedName();
            classes.add(className);
        }
        return classes;
    }

    public List<CtClass<?>> getClasses() {
        List<CtClass<?>> classes = new ArrayList<>();
        for (CtClass<?> ctClass : this.model.getElements(new TypeFilter<>(CtClass.class))) {
            classes.add(ctClass);
        }
        return classes;
    }

    public int calculateCouplingMetric(CtClass<?> classA, CtClass<?> classB) {
        int couplingMetric = 0;
        if ((classA != null) && (classB != null)) {
            for (CtMethod<?> methodA : classA.getMethods()) {
                for (CtMethod<?> methodB : classB.getMethods()) {
                    if (methodA.getBody() != null) {
                        // Rechercher des invocations de méthode qui impliquent classB
                        List<CtInvocation<?>> invocations = methodA.getBody().getElements(new TypeFilter<>(CtInvocation.class));
                        for (CtInvocation<?> invocation : invocations) {
                            // Vérifier si l'invocation est une méthode de classB
                            if (invocation.getExecutable().getDeclaringType().equals(classB.getReference()) && invocation.getExecutable().getSimpleName().equals(methodB.getSimpleName())) {
                                couplingMetric++;
                            }
                        }
                    }
                }
            }
        }
        return couplingMetric;
    }

    public boolean existePair(Pair<String, String> pairToFind, List<Pair<String, String>> listeDePairs) {
        for (Pair<String, String> pair : listeDePairs) {
            if (pair.getFirst().equals(pairToFind.getFirst()) && pair.getSecond().equals(pairToFind.getSecond())) {
                return true;
            }
        }
        return false;
    }

    public Pair<String, String> getCurrentPair(Pair<String, String> pairToFind, List<Pair<String, String>> listeDePairs) {
        for (Pair<String, String> pair : listeDePairs) {
            if (pair.getFirst().equals(pairToFind.getSecond()) && pair.getFirst().equals(pairToFind.getSecond())) {
                return pair;
            }
        }
        return null;
    }

    public Map<Pair<String, String>, Double> calculateCouplingMetric(Map<Pair<String, String>, Double> relations) {
        Map<Pair<String, String>, Double> couplingMetrics = new HashMap<>();
        for (Map.Entry<Pair<String, String>, Double> entry : relations.entrySet()) {
            Pair<String, String> classPair = entry.getKey();
            double relationsCountAB = entry.getValue();
            double couplingMetric = relationsCountAB / getTotalCoupling();
            couplingMetrics.put(classPair, couplingMetric);
        }
        return couplingMetrics;
    }

    public Map<Pair<String, String>, Double> calculateCouplingMetricsForAllClasses() {
        Map<Pair<String, String>, Double> results = new HashMap<>();
        List<Pair<String, String>> listeDePairs = new ArrayList<>();
        for (CtClass<?> classA : classes) {
            for (CtClass<?> classB : classes) {
                Pair<String, String> pairAB = new Pair<>(classA.getQualifiedName(), classB.getQualifiedName());
                Pair<String, String> pairBA = new Pair<>(classB.getQualifiedName(), classA.getQualifiedName());
                // Vérifiez si l'entrée existe déjà pour A vers B
                if (!existePair(pairAB, listeDePairs)) {
                    double newMetric = calculateCouplingMetric(classA, classB);
                    setTotalCoupling(getTotalCoupling() + newMetric);
                    if (existePair(pairBA, listeDePairs)) {
                        // System.out.println("paire :" +pairAB.getFirst() +" -" +pairAB.getSecond() +" : "+ results.get(pairBA));
                        double existingMetric = results.get(pairBA);
                        results.put(pairBA, existingMetric + ((double) (newMetric)));
                    } else {
                        results.put(pairAB, ((double) (newMetric)));
                    }
                    listeDePairs.add(pairAB);
                }
            }
        }
        return results;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Couplage {\n");
        sb.append("\tTotal Coupling: ").append(getTotalCoupling()).append("\n");
        sb.append("\tWeighted Graph: \n");
        for (Map.Entry<Pair<String, String>, Double> entry : getWeightedGraph().entrySet()) {
            sb.append("\t\t").append(entry.getKey().getFirst()).append("-").append(entry.getKey().getSecond()).append(" -> ").append(entry.getValue()).append("\n");
        }
        double somme = couplage.values().stream().mapToDouble(Double::doubleValue).sum();
        sb.append("somme total des couple : " + somme);
        sb.append("}");
        return sb.toString();
    }
}