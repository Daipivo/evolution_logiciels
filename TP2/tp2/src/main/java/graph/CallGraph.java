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

    private List<String> classes;
    private Map<String, Map<String, List<MethodInvocation>>> graphAll = new HashMap<>();
    private Map<String, Map<String, List<MethodInvocation>>> graphIntern = new HashMap<>();
    private Set<Pair<String, String>> aretesAll;
    private Set<Pair<String, String>> aretesIntern;

    public CallGraph(String projectPath){
        this.parser = new Parser(projectPath);
        classes = new ArrayList<>();
        this.aretesAll = new HashSet<>();
        this.aretesIntern = new HashSet<>();
    }

    // Parcours d'un fichier java
    public void parcours(CompilationUnit cUnit){

        ClassCountVisitor classVisitor = new ClassCountVisitor();

        Map<String, List<MethodInvocation>> callMethods = new HashMap<>();
        Map<String, List<MethodInvocation>> callMethodsIntern = new HashMap<>();

            TypeDeclaration cls = classVisitor.getClasse(cUnit);

            if(cls != null) {


                MethodDeclarationVisitor methodVisitor = new MethodDeclarationVisitor();

                // Pour chaque méthode de la classe
                for (MethodDeclaration method : methodVisitor.getMethods(cls)) {

                // Récupération de la liste des appels de méthode présent dans la méthode "method"
                List<MethodInvocation> invocationMethods = getMethodInvocation(method);

                List<MethodInvocation> invocationMethodsIntern = new ArrayList<>();

                invocationMethods.stream()
                        .forEach(methodInvocation -> {
                            if (methodInvocation.getExpression() != null) {
                                if (methodInvocation.getExpression().resolveTypeBinding() != null) {
                                    String secondValue = methodInvocation.getExpression().resolveTypeBinding().getName() + ":" + methodInvocation.getName().toString();
                                    if(classes.contains(cls.getName().toString()) && classes.contains(methodInvocation.getExpression().resolveTypeBinding().getName().toString()))
                                    {
                                        aretesIntern.add(new Pair<>(cls.getName() + ":" + method.getName().toString(), secondValue));
                                        invocationMethodsIntern.add(methodInvocation);
                                    }
                                    aretesAll.add(new Pair<>(cls.getName() + ":" + method.getName().toString(), secondValue));
                                } else {
                                    String secondValue = methodInvocation.getExpression() + ":" + methodInvocation.getName().toString();
                                    aretesAll.add(new Pair<>(cls.getName() + ":" + method.getName().toString(), secondValue));
                                }

                            } else {
                                IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
                                if (methodBinding != null) {
                                    ITypeBinding declaringClass = methodBinding.getDeclaringClass();
                                    aretesAll.add(new Pair<>(cls.getName() + ":" + method.getName().toString(), declaringClass.getName() + ":" + methodInvocation.getName().toString()));
                                    if(classes.contains(cls.getName().toString()) && classes.contains(declaringClass.getName().toString()))
                                    {
                                        aretesIntern.add(new Pair<>(cls.getName() + ":" + method.getName().toString(), declaringClass.getName() + ":" + methodInvocation.getName().toString()));
                                        invocationMethodsIntern.add(methodInvocation);

                                    }
                                } else {
                                    aretesAll.add(new Pair<>(cls.getName() + ":" + method.getName().toString(), methodInvocation.getName().toString()));
                                }
                            }
                        });
                // Ajout dans le hashmap => NomMethode : [ methodeAppelee1, methodeAppelee2 ..]
                callMethods.put(method.getName().toString(), invocationMethods);
                callMethodsIntern.put(method.getName().toString(), invocationMethodsIntern);
            }

            // Ajout dans le graph => Classe : hashmap
            graphAll.put(String.valueOf(cls.getName()), callMethods);
            graphIntern.put(String.valueOf(cls.getName()), callMethodsIntern);
        }
    }

    public List<String> getClasses() {
        return classes;
    }

    public boolean start(){

        // Parcours du dossier
        parser.ParseFolder();
        classes = parser.getClasses();

        // Chaque cUnit => AST d'un fichier .java
        for(CompilationUnit cUnit : parser.getcUnits()){
            parcours(cUnit);
        }
        return true;
    }

    public Map<String, Map<String, List<MethodInvocation>>> getGraphAll(){
        return this.graphAll;
    }

    public Map<String, Map<String, List<MethodInvocation>>> getGraphIntern(){
        return this.graphIntern;
    }

    // Renvoie la liste des appels de méthodes présent dans une méthode
    public List<MethodInvocation> getMethodInvocation(MethodDeclaration methode){

        MethodInvocationVisitor visitor = new MethodInvocationVisitor();

        return visitor.getMethodsInvocation(methode);

    }

    public Map<String, List<String>> getCalledMethodsInClasse(String nameClasse) {

        Map<String, List<String>> result = new HashMap<>();

        for (String methodName : graphIntern.get(nameClasse).keySet()) {
            List<String> methodNamesList = graphIntern.get(nameClasse).get(methodName)
                    .stream()
                    .map(m -> m.getName().toString())
                    .collect(Collectors.toList());

            result.put(methodName, methodNamesList);

        }
        return result;
    }

    public Set<Pair<String, String>> getAretes(){
        return aretesAll;
    }

    public Set<Pair<String, String>> getAretesIntern(){

        return aretesIntern;

    }

    public int getNbrAretes(){
        return aretesAll.size();
    }

    public int getNbrAretesIntern(){
        return aretesIntern.size();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        aretesAll
                .stream()
                .forEach(arete -> System.out.println(arete.getFirst() + " ---> " + arete.getSecond()));


        return sb.toString();
    }


}
