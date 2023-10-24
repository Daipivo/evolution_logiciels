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
                    // Vous pouvez personnaliser votre métrique ici
                    if ((methodA.getBody() != null) && methodA.getBody().toString().contains(methodB.getSimpleName())) {
                        couplingMetric++;
                    }
                }
            }
        }
        return couplingMetric;
    }

    public Map<Pair<String, String>, Double> calculateCouplingMetricsForAllClasses() {
        Map<Pair<String, String>, Double> results = new HashMap<>();
        for (CtClass<?> classA : classes) {
            for (CtClass<?> classB : classes) {
                if (classA != classB) {
                    Pair<String, String> pairAB = new Pair<>(classA.getQualifiedName(), classB.getQualifiedName());
                    Pair<String, String> pairBA = new Pair<>(classB.getQualifiedName(), classA.getQualifiedName());
                    // Vérifiez si l'entrée existe déjà pour A vers B
                    if (!results.containsKey(pairAB)) {
                        if (results.containsKey(pairBA)) {
                            double existingMetric = results.get(pairBA);
                            int newMetric = calculateCouplingMetric(classA, classB);
                            results.put(pairAB, existingMetric + ((double) (newMetric)));
                        } else {
                            int couplingAB = calculateCouplingMetric(classA, classB);
                            results.put(pairAB, ((double) (couplingAB)));
                        }
                    }
                }
            }
        }
        return results;
    }
}