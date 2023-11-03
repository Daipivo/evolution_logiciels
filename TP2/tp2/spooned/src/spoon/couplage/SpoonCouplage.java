package spoon.couplage;
import graph.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;
public class SpoonCouplage {
    private CtModel model;

    private List<CtClass<?>> classes;

    public SpoonCouplage(CtModel model) {
        this.model = model;
        this.classes = getClasses();
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
                    if ((methodA.getBody() != null) && methodA.getBody().toString().contains(methodB.getSimpleName())) {
                        couplingMetric++;
                    }
                }
            }
        }
        return couplingMetric;
    }

    public boolean existeInversePair(Pair<String, String> pairToFind, List<Pair<String, String>> listeDePairs) {
        for (Pair<String, String> pair : listeDePairs) {
            if (pair.getFirst().equals(pairToFind.getSecond()) && pair.getFirst().equals(pairToFind.getSecond())) {
                return true;
            }
        }
        return false;
    }

    public Map<Pair<String, String>, Double> calculateCouplingMetricsForAllClasses() {
        Map<Pair<String, String>, Double> results = new HashMap<>();
        List<Pair<String, String>> listeDePairs = new ArrayList<>();
        for (CtClass<?> classA : classes) {
            for (CtClass<?> classB : classes) {
                if (classA != classB) {
                    Pair<String, String> pairAB = new Pair<>(classA.getQualifiedName(), classB.getQualifiedName());
                    Pair<String, String> pairBA = new Pair<>(classB.getQualifiedName(), classA.getQualifiedName());
                    // Vérifiez si l'entrée existe déjà pour A vers B
                    if (!results.containsKey(pairAB)) {
                        double newMetric = calculateCouplingMetric(classA, classB);
                        if (existeInversePair(pairAB, listeDePairs)) {
                            double existingMetric = results.get(pairBA);
                            results.put(pairBA, existingMetric + ((double) (newMetric)));
                        } else {
                            results.put(pairAB, ((double) (newMetric)));
                        }
                        listeDePairs.add(pairAB);
                    }
                }
            }
        }
        return results;
    }
}