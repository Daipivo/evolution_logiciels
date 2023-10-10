package graph;

import org.eclipse.jdt.core.dom.*;
import parser.Parser;
import visiteur.ClassCountVisitor;
import visiteur.MethodDeclarationVisitor;
import visiteur.MethodInvocationVisitor;

import java.util.*;
import java.util.stream.Collectors;

public class CallGraph {

    private Parser parser;

    private Map<String, Map<String, List<MethodInvocation>>> graph = new HashMap<>();

    private Set<Pair<String, String>> aretes;

    public CallGraph(String projectPath){
        this.parser = new Parser(projectPath);
        this.aretes = new HashSet<>();
    }

    // Parcours d'un fichier java
    public Map<String, Map<String, List<MethodInvocation>>> parcours(CompilationUnit cUnit){

        ClassCountVisitor classVisitor = new ClassCountVisitor();

        Map<String, List<MethodInvocation>> callMethods = new HashMap<>();

            TypeDeclaration cls = classVisitor.getClasse(cUnit);

            if(cls != null) {


                MethodDeclarationVisitor methodVisitor = new MethodDeclarationVisitor();

                // Pour chaque méthode de la classe
                for (MethodDeclaration method : methodVisitor.getMethods(cls)) {

                // Récupération de la liste des appels de méthode présent dans la méthode "method"
                List<MethodInvocation> invocationMethods = getMethodInvocation(method);

                invocationMethods.stream()
                        .forEach(methodInvocation -> {
                            if (methodInvocation.getExpression() != null) {
                                if (methodInvocation.getExpression().resolveTypeBinding() != null) {
                                    String secondValue = methodInvocation.getExpression().resolveTypeBinding().getName() + ":" + methodInvocation.getName().toString();
                                    aretes.add(new Pair<>(cls.getName() + ":" + method.getName().toString(), secondValue));
                                } else {
                                    String secondValue = methodInvocation.getExpression() + ":" + methodInvocation.getName().toString();
                                    aretes.add(new Pair<>(cls.getName() + ":" + method.getName().toString(), secondValue));
                                }

                            } else {
                                IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
                                if (methodBinding != null) {
                                    ITypeBinding declaringClass = methodBinding.getDeclaringClass();
                                    aretes.add(new Pair<>(cls.getName() + ":" + method.getName().toString(), declaringClass.getName() + ":" + methodInvocation.getName().toString()));
                                } else {
                                    aretes.add(new Pair<>(cls.getName() + ":" + method.getName().toString(), methodInvocation.getName().toString()));
                                }
                            }
                        });
                // Ajout dans le hashmap => NomMethode : [ methodeAppelee1, methodeAppelee2 ..]
                callMethods.put(method.getName().toString(), invocationMethods);

            }

            // Ajout dans le graph => Classe : hashmap
            graph.put(String.valueOf(cls.getName()), callMethods);
        }

        return graph;
    }


    public boolean start(){

        // Parcours du dossier
        parser.ParseFolder();

        // Chaque cUnit => AST d'un fichier .java
        for(CompilationUnit cUnit : parser.getcUnits()){
            parcours(cUnit);
        }
        return true;
    }


    // Renvoie la liste des appels de méthodes présent dans une méthode
    public List<MethodInvocation> getMethodInvocation(MethodDeclaration methode){

        MethodInvocationVisitor visitor = new MethodInvocationVisitor();

        return visitor.getMethodsInvocation(methode);

    }

    public Map<String, List<String>> getCalledMethodsInClasse(String nameClasse) {

        Map<String, List<String>> result = new HashMap<>();

        for (String methodName : graph.get(nameClasse).keySet()) {
            List<String> methodNamesList = graph.get(nameClasse).get(methodName)
                    .stream()
                    .map(m -> m.getName().toString())
                    .collect(Collectors.toList());

            result.put(methodName, methodNamesList);

        }
        return result;
    }

    public Set<Pair<String, String>> getAretes(){

        return aretes;

    }

    public Map<String, Map<String, List<MethodInvocation>>> getGraph() {
        return graph;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        aretes
                .stream()
                .forEach(arete -> System.out.println(arete.getFirst() + " ---> " + arete.getSecond()));


        return sb.toString();
    }


}
