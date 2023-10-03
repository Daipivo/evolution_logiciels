package org.example.graph;

import org.eclipse.jdt.core.dom.*;
import org.example.parser.Parser;
import org.example.visiteur.ClassCountVisitor;
import org.example.visiteur.MethodDeclarationVisitor;
import org.example.visiteur.MethodInvocationVisitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CallGraph {

    private Parser parser;

    private Map<String, Map<String, List<MethodInvocation>>> graph = new HashMap<>();

    public CallGraph(String projectPath){

        this.parser = new Parser(projectPath,5);

    }

    // Parcours d'un fichier java
    public Map<String, Map<String, List<MethodInvocation>>> parcours(CompilationUnit cUnit){

        ClassCountVisitor classVisitor = new ClassCountVisitor();

        Map<String, List<MethodInvocation>> callMethods = new HashMap<>();

        TypeDeclaration cls = classVisitor.getClasse(cUnit);

        MethodDeclarationVisitor methodVisitor = new MethodDeclarationVisitor();

        // Pour chaque méthode de la classe
        for(MethodDeclaration method: methodVisitor.getMethods(cls)){

            // Récupération de la liste des appels de méthode présent dans la méthode "method"
            List<MethodInvocation> invocationMethods = getMethodInvocation(method);

            // Ajout dans le hashmap => NomMethode : [ methodeAppelee1, methodeAppelee2 ..]
            callMethods.put(method.getName().toString(), invocationMethods);

        }

        // Ajout dans le graph => Classe : hashmap
        graph.put(String.valueOf(cls.getName()),callMethods);

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

        MethodInvocationVisitor  visitor = new MethodInvocationVisitor();

        return visitor.getMethodsInvocation(methode);

    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Map<String, List<MethodInvocation>>> entry1 : graph.entrySet()) {
            String classe = entry1.getKey();
            Map<String, List<MethodInvocation>> methodes = entry1.getValue();

            sb.append(classe).append(System.lineSeparator());

            for (Map.Entry<String, List<MethodInvocation>> entry2 : methodes.entrySet()) {
                String methode = entry2.getKey();
                List<MethodInvocation> appels = entry2.getValue();

                sb.append("==> ").append(methode).append(System.lineSeparator());

                for (MethodInvocation appel : appels) {
                    sb.append("    ==> ").append(appel.toString()).append(System.lineSeparator());
                }
            }
        }

        return sb.toString();
    }


}
