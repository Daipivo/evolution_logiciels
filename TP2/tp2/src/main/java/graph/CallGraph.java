package graph;

import org.eclipse.jdt.core.dom.*;
import parser.Parser;
import scala.util.parsing.combinator.testing.Str;
import visiteur.ClassCountVisitor;
import visiteur.MethodDeclarationVisitor;
import visiteur.MethodInvocationVisitor;

import java.util.*;
import java.util.stream.Collectors;

public class CallGraph {

    private Parser parser;
    private List<String> classes;
    private Map<String, Map<String, List<MethodInvocation>>> graphAll = new HashMap<>();
    private Map<String, Map<String, List<String>>> graphIntern = new HashMap<>();
    private List<Pair<String, String>> aretesAll;
    private List<Pair<String, String>> aretesIntern;

    public CallGraph(String projectPath){
        this.parser = new Parser(projectPath);
        this.classes = new ArrayList<>();
        this.aretesAll = new ArrayList<>();
        this.aretesIntern = new ArrayList<>();
        start();
    }

    // Parcours d'un fichier java
    public void parcours(CompilationUnit cUnit){

        ClassCountVisitor classVisitor = new ClassCountVisitor();

        Map<String, List<MethodInvocation>> callMethods = new HashMap<>();
        Map<String, List<String>> callMethodsIntern = new HashMap<>();

            TypeDeclaration cls = classVisitor.getClasse(cUnit);

            if(cls != null) {


                MethodDeclarationVisitor methodVisitor = new MethodDeclarationVisitor();

                // Pour chaque méthode de la classe
                for (MethodDeclaration method : methodVisitor.getMethods(cls)) {

                    // Récupération de la liste des appels de méthode présent dans la méthode "method"
                    List<MethodInvocation> invocationMethods = getMethodInvocation(method);

                    List<String> invocationMethodsIntern = new ArrayList<>();

                    invocationMethods.forEach(methodInvocation -> {
                        String className = null;
                        String methodName = methodInvocation.getName().toString();

                        // Si l'invocation de la méthode a une expression (par exemple, `obj.method()`), utilisez celle-ci.
                        if (methodInvocation.getExpression() != null && methodInvocation.getExpression().resolveTypeBinding() != null) {
                            className = methodInvocation.getExpression().resolveTypeBinding().getName();
                        }
                        // Sinon, utilisez la classe déclarante de la méthode invoquée.
                        else {
                            IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
                            if (methodBinding != null && methodBinding.getDeclaringClass() != null) {
                                className = methodBinding.getDeclaringClass().getName();
                            }
                        }

                        // Si nous avons le nom de la classe, construisez la chaîne souhaitée.
                        if (className != null) {
                            String fullMethodInvocation = className + ":" + methodName;

                            // Ajout à aretesAll
                            aretesAll.add(new Pair<>(cls.getName() + ":" + method.getName().toString(), fullMethodInvocation));

                            // Si la classe source et la classe cible sont toutes deux dans 'classes', ajoutez à aretesIntern et invocationMethodsIntern
                            if (classes.contains(cls.getName().toString()) && classes.contains(className)) {
                                aretesIntern.add(new Pair<>(cls.getName() + ":" + method.getName().toString(), fullMethodInvocation));
                                invocationMethodsIntern.add(fullMethodInvocation);
                            }
                        } else {
                            // Ajoutez seulement le nom de la méthode si le nom de la classe n'est pas disponible.
                            aretesAll.add(new Pair<>(cls.getName() + ":" + method.getName().toString(), methodName));
                        }
                    });

                    // Ajout dans le hashmap => NomMethode : [ methodeAppelee1, methodeAppelee2 ..]
                    callMethods.put(method.getName().toString(), invocationMethods);
                    callMethodsIntern.put(method.getName().toString(), invocationMethodsIntern);
                }

            String key = String.valueOf(cls.getName());


            // Ajout dans le graph => Classe : hashmap
            graphAll.put(String.valueOf(cls.getName()), callMethods);


                // Vérifier si la clé existe déjà dans graphIntern
            if (graphIntern.containsKey(key)) {
                // Récupérer la valeur existante
                Map<String, List<String>> existingCallMethodsIntern = graphIntern.get(key);

                // Parcourir toutes les clés de callMethodsIntern et les fusionner avec existingCallMethodsIntern
                for (Map.Entry<String, List<String>> entry : callMethodsIntern.entrySet()) {
                        existingCallMethodsIntern.merge(entry.getKey(), entry.getValue(), (existingList, newList) -> {
                        existingList.addAll(newList);
                        return existingList;
                    });
                }
            } else {
                // Si la clé n'existe pas dans graphIntern, ajoutez simplement callMethodsIntern
                graphIntern.put(key, callMethodsIntern);
            }

            }
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

    public Map<String, Map<String, List<String>>> getGraphIntern(){
        return this.graphIntern;
    }

    // Renvoie la liste des appels de méthodes présent dans une méthode
    public List<MethodInvocation> getMethodInvocation(MethodDeclaration methode){

        MethodInvocationVisitor visitor = new MethodInvocationVisitor();

        return visitor.getMethodsInvocation(methode);
    }
    public Map<String, List<String>> getCalledMethodsInClasse(String nameClasse) {

        if (graphIntern.containsKey(nameClasse)) {
            return new HashMap<>(graphIntern.get(nameClasse));
        }
        return new HashMap<>();
    }

    public List<Pair<String, String>> getAretes(){
        return aretesAll;
    }

    public List<Pair<String, String>> getAretesIntern(){

        return aretesIntern;

    }

    public int getNbrAretes(){
        return aretesAll.size();
    }

    public int getNbrAretesIntern(){
        return aretesIntern.size();
    }

    public List<String> getClasses(){return classes;}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        aretesAll
                .stream()
                .forEach(arete -> System.out.println(arete.getFirst() + " ---> " + arete.getSecond()));


        return sb.toString();
    }


}
