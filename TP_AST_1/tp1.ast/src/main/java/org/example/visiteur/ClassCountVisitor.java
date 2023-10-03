package org.example.visiteur;

import org.eclipse.jdt.core.dom.*;

import java.lang.reflect.Array;
import java.util.*;

public class ClassCountVisitor extends ASTVisitor {
    private int classCount = 0;

    private List<TypeDeclaration> classes = new ArrayList<>();

    private Map<String, List<MethodDeclaration>> classesAndMethods = new HashMap<>();

    private TypeDeclaration currentClasse;

    @Override
    public boolean visit(TypeDeclaration node) {

        if(!node.isInterface()) {
            classCount++;
            classes.add(node);
            currentClasse = node;
            if (!classesAndMethods.containsKey(String.valueOf(node.getName()))) {
                classesAndMethods.put(String.valueOf(node.getName()), Arrays.asList(node.getMethods()));
            }
        }

        return super.visit(node);
    }

    public int getClassCount() {
        return classCount;
    }

    public List<TypeDeclaration> getClasses(){
        return classes;
    }

    public List<String> get10PercentMostMethods() {
        List<TypeDeclaration> allClasses = getClasses();

        // Triez toutes les classes en fonction du nombre de méthodes dans l'ordre décroissant
        allClasses.sort((class1, class2) -> {
            int methods1 = class1.getMethods().length;
            int methods2 = class2.getMethods().length;
            return Integer.compare(methods2, methods1); // Tri décroissant
        });

        // Calculez le nombre de classes à inclure (10% des classes)
        int numberOfClassesToInclude = (int) Math.ceil(0.1 * allClasses.size());

        // Limitez la liste aux 10% supérieurs

        List<TypeDeclaration> top10PercentClasses = allClasses.subList(0, numberOfClassesToInclude);

        // Créez une liste de noms de classes à partir des objets TypeDeclaration
        List<String> classNames = new ArrayList<>();
        for (TypeDeclaration typeDeclaration : top10PercentClasses) {
            classNames.add(String.valueOf(typeDeclaration.getName())); // Supposons que getName() renvoie le nom de la classe
        }

        return classNames;
    }

    public List<String> get10PercentMostAttributes() {
        List<TypeDeclaration> allClasses = getClasses();

        // Triez toutes les classes en fonction du nombre d'attributs dans l'ordre décroissant
        allClasses.sort((class1, class2) -> {
            int attributes1 = class1.getFields().length;
            int attributes2 = class2.getFields().length;
            return Integer.compare(attributes2,attributes1); // Tri décroissant
        });

        // Calculez le nombre de classes à inclure (10% des classes)
        int numberOfClassesToInclude = (int) Math.ceil(0.1 * allClasses.size());

        // Limitez la liste aux 10% supérieurs

        List<TypeDeclaration> top10PercentClasses = allClasses.subList(0, numberOfClassesToInclude);


        // Créez une liste de noms de classes à partir des objets TypeDeclaration
        List<String> classNames = new ArrayList<>();
        for (TypeDeclaration typeDeclaration : top10PercentClasses) {
            classNames.add(String.valueOf(typeDeclaration.getName())); // Supposons que getName() renvoie le nom de la classe
        }

        return classNames;
    }

    public List<String> getClassesWithMoreThanMethods(int nbMethodsMin){

        List<TypeDeclaration> allClasses = getClasses();

        List<String> result = new ArrayList<>();

        for (TypeDeclaration cl : allClasses){
            if(cl.getMethods().length >= nbMethodsMin){
                result.add(String.valueOf(cl.getName()));
            }
        }

        return result;
    }

    public Map<String, List<String>> getMethodsMostCodeLinesPerClasses(){

        Map<String, List<String>> result = new HashMap<>();

        for ( String cs : classesAndMethods.keySet()){
            if(!classesAndMethods.get(cs).isEmpty()) {
                System.out.println(classesAndMethods.get(cs).get(0).getName() + " " + getMethodLineCount(classesAndMethods.get(cs).get(0)));
            }
        }

        return result;
    }

    public int getMethodLineCount(MethodDeclaration methodDeclaration) {
        Block body = methodDeclaration.getBody();
        if (body != null) {
            int methodStartLine = body.getStartPosition();
            int methodEndLine = methodStartLine + body.getLength();
            CompilationUnit compilationUnit = (CompilationUnit) methodDeclaration.getRoot();
            int startLine = compilationUnit.getLineNumber(methodStartLine);
            int endLine = compilationUnit.getLineNumber(methodEndLine);
            return endLine - startLine + 1;
        }
        return 0; // Method has no body
    }

    public TypeDeclaration getCurrentClasse(){
        return this.currentClasse;
    }

    public TypeDeclaration getClasse(CompilationUnit cUnit) {
        cUnit.accept(this);
        return this.getCurrentClasse();
    }

}
