package visiteur;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.*;

public class MethodDeclarationVisitor extends ASTVisitor {
    List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
    private int methodCount = 0;
    private int maxParameters = 0;
    private int nbrLigneMethodes = 0;
    private Map<TypeDeclaration, List<MethodDeclaration>> classToMethodsMap = new HashMap<>();
    private Map<MethodDeclaration, Integer> methodToLineCountMap = new HashMap<>();

    /*
   Visite un nœud de déclaration de méthode (MethodDeclaration)
   @param node Le nœud de déclaration de méthode à visiter.
   @return true si la visite est réussie, sinon false.
    */
    public boolean visit(MethodDeclaration node) {

        methodCount++;
        methods.add(node);

        if (node != null && node.getBody() != null) {
            int methodLines = node.getBody().toString().split("\n").length;
            nbrLigneMethodes += methodLines;
            methodToLineCountMap.put(node, methodLines);
        }
        else{
            nbrLigneMethodes += 1;
            methodToLineCountMap.put(node, 1);
        }

        int parameterCount = node.parameters().size();
        if (parameterCount > maxParameters) {
            maxParameters = parameterCount;
        }
        return super.visit(node);
    }

    public boolean visit(TypeDeclaration type) {

        if (!type.isInterface() && !classToMethodsMap.containsKey(type)) {

            classToMethodsMap.put(type, Arrays.asList(type.getMethods()));
        }

        return super.visit(type);
    }

    public int getMethodCount() {
        return methodCount;
    }

    public List<MethodDeclaration> getMethods(TypeDeclaration cls) {
        cls.accept(this);
        return classToMethodsMap.get(cls);
    }
    public int getMaxParameters() {
        return maxParameters;
    }

    public int getNbrLigneMethodes() {
        return nbrLigneMethodes;
    }

    public Map<String, List<String>> get10PercentMostMethodsPerClasse() {

        Map<String, List<String>> result = new HashMap<>();

        for (TypeDeclaration classe : classToMethodsMap.keySet()) {

            classToMethodsMap.get(classe).sort((method1, method2) -> {
                int countLineMethod1 = methodToLineCountMap.get(method1);
                int countLineMethod2 = methodToLineCountMap.get(method2);
                return Integer.compare(countLineMethod2, countLineMethod1);
            });

            int numberOfMethodsToInclude = (int) Math.ceil(0.1 * classToMethodsMap.get(classe).size());

            // Limitez la liste aux 10% supérieurs
            List<MethodDeclaration> top10PercentMethod = classToMethodsMap.get(classe).subList(0, numberOfMethodsToInclude);
            List<String> methodsString = new ArrayList<>();

            top10PercentMethod
                    .stream()
                    .forEach(m -> methodsString.add(m.getName().toString()));

            result.put(classe.getName().toString(), methodsString);
        }

        return result;


    }
}
