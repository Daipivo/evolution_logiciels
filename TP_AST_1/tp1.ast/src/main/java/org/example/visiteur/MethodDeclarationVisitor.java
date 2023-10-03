package org.example.visiteur;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.*;

public class MethodDeclarationVisitor extends ASTVisitor {
    List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
    private int methodCount = 0;
    private int maxParameters = 0;
    private int nbrLigneMethodes = 0;
    private Map<TypeDeclaration, List<MethodDeclaration>> map = new HashMap<>();
    private Map<MethodDeclaration, Integer> methodsLineCount = new HashMap<>();
    private Map<MethodDeclaration, TypeDeclaration> methodsClasses = new HashMap<>();

    public boolean visit(MethodDeclaration node) {

        methodCount++;
        methods.add(node);

        if (node != null && node.getBody() != null) {
            int methodLines = node.getBody().toString().split("\n").length;
            nbrLigneMethodes += methodLines;
            methodsLineCount.put(node, methodLines);
        }
        else{
            nbrLigneMethodes += 1;
            methodsLineCount.put(node, 1);
        }

        int parameterCount = node.parameters().size();
        if (parameterCount > maxParameters) {
            maxParameters = parameterCount;
        }
        return super.visit(node);
    }

    public boolean visit(TypeDeclaration type) {

        if (!type.isInterface() && !map.containsKey(type)) {

            map.put(type, Arrays.asList(type.getMethods()));

            Arrays.asList(type.getMethods())
                    .stream()
                    .forEach(m -> methodsClasses.put(m, type));
        }

        return super.visit(type);
    }

    public int getMethodCount() {
        return methodCount;
    }

    public List<MethodDeclaration> getMethods(TypeDeclaration cls) {
        cls.accept(this);
        return map.get(cls);
    }

    public List<MethodDeclaration> getMethods() {
        return methods;
    }

    public int getMaxParameters() {
        return maxParameters;
    }

    public int getNbrLigneMethodes() {
        return nbrLigneMethodes;
    }

    public Map<String, List<String>> get10PercentMostMethodsPerClasse() {

        Map<String, List<String>> result = new HashMap<>();

        for (TypeDeclaration classe : map.keySet()) {

            map.get(classe).sort((method1, method2) -> {
                int countLineMethod1 = methodsLineCount.get(method1);
                int countLineMethod2 = methodsLineCount.get(method2);
                return Integer.compare(countLineMethod2, countLineMethod1);
            });

            int numberOfMethodsToInclude = (int) Math.ceil(0.1 * map.get(classe).size());

            // Limitez la liste aux 10% supérieurs
            List<MethodDeclaration> top10PercentMethod = map.get(classe).subList(0, numberOfMethodsToInclude);
            List<String> methodsString = new ArrayList<>();

            top10PercentMethod
                    .stream()
                    .forEach(m -> methodsString.add(m.getName().toString()));

            result.put(classe.getName().toString(), methodsString);
        }

        return result;


    }
}
