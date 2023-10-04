package org.example.couplage;

import org.example.graph.CallGraph;

public class Couplage {

    private CallGraph graph;
//    private Map<String, >

    public Couplage(CallGraph graph){
        this.graph = graph;
    }

    public double getCouplage(String class1, String class2){

        double result = 0;


        return result;
    }


//    public double calculateCouplage(String classA, String classB) {
//
//        Map<String, List<MethodInvocation>> methodsInClassA = graph.get(classA);
//        Map<String, List<MethodInvocation>> methodsInClassB = graph.get(classB);
//
//        int numberOfRelationsAB = 0;
//        int totalNumberOfRelations = 0;
//
//        // Calcul du nombre de relations entre les méthodes de classe A et classe B
//        for (String methodA : methodsInClassA.keySet()) {
//            for (String methodB : methodsInClassB.keySet()) {
//                numberOfRelationsAB += countMethodInvocations(methodsInClassA.get(methodA), methodsInClassB.get(methodB));
//            }
//        }
//
//        // Calcul du nombre total de relations entre toutes les classes
//        for (String className : graph.keySet()) {
//            if (!className.equals(classA) && !className.equals(classB)) {
//                totalNumberOfRelations += countMethodInvocationsBetweenClasses(methodsInClassA, graph.get(className));
//                totalNumberOfRelations += countMethodInvocationsBetweenClasses(methodsInClassB, graph.get(className));
//            }
//        }
//
//        // Calcul de la métrique de couplage
//        double couplage = (double) numberOfRelationsAB / totalNumberOfRelations;
//
//        return couplage;
//    }

    // Compte le nombre de relations entre deux listes de MethodInvocations
//    private int countMethodInvocations(List<MethodInvocation> listA, List<MethodInvocation> listB) {
//        int count = 0;
//        for (MethodInvocation invocationA : listA) {
//            for (MethodInvocation invocationB : listB) {
//                if (invocationA.equals(invocationB)) {
//                    count++;
//                }
//            }
//        }
//        return count;
//    }

    // Compte le nombre de relations entre les méthodes d'une classe et d'une autre classe
//    private int countMethodInvocationsBetweenClasses(Map<String, List<MethodInvocation>> methodsInClassA, Map<String, List<MethodInvocation>> methodsInClassB) {
//        int count = 0;
//        for (String methodA : methodsInClassA.keySet()) {
//            for (String methodB : methodsInClassB.keySet()) {
//                count += countMethodInvocations(methodsInClassA.get(methodA), methodsInClassB.get(methodB));
//            }
//        }
//        return count;
//    }


}
