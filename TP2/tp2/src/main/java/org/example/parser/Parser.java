package org.example.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.example.visiteur.*;

import javax.xml.crypto.dsig.spec.HMACParameterSpec;

public class Parser {
    //recuperer le fichier a parser
    private String projectPath;
    private String projectSourcePath;
    private int x;

    ArrayList<CompilationUnit> cUnits = new ArrayList<>();


    public Parser(){

    }
    
    public Parser(String projectPath,int x){
        this.x=x;
        this.projectPath=projectPath;
        this.projectSourcePath = this.projectPath + "/src";
    }

    public Parser(String projectPath){
        this.projectPath=projectPath;
        this.projectSourcePath = this.projectPath + "/src";
    }



    public String ParseFolder(){
        final File folder = new File(projectSourcePath);
        ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
        int nbrClasses = 0;
        int nbrLignes = 0;
        int nbrMethodes = 0;
        int nbrLignesMethode=0;
        int nbrPackage = 0;
        int nbrAttribute=0;
        int nbrMaxParameters=0;

        processFolder(folder);


        for (File fileEntry : javaFiles) {
            String content = null;

            nbrLignes += getNumberOfLines(fileEntry.getAbsolutePath()) ;

            try {
                content = FileUtils.readFileToString(fileEntry);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            CompilationUnit parse = parse(content.toCharArray());
            cUnits.add(parse);
        }

        nbrClasses = getNbClasses();
        nbrMethodes = getNbMethods();
        nbrLignesMethode=getNbLigneMethods();
        nbrPackage = getNbPackages();
        nbrAttribute=getnbrAttribut();
        nbrMaxParameters=getNbrParameterMax();

//        System.out.println("nombre de ligne de methodes: "+nbrLignesMethode);
//        System.out.println("nombre de methodes: "+nbrMethodes);
//        System.out.println(methodsWithMostCodeLinesPerClasses());


        String results = "Nombre total de classes dans le projet ==> " + nbrClasses + "\n"
                + "Nombre total de lignes de code dans le projet ==> " + nbrLignes + "\n"
                + "Nombre total de méthodes dans le projet ==> " + nbrMethodes + "\n"
                + "Nombre total de packages dans le projet ==> " + nbrPackage + "\n"
                + "Nombre moyen de méthodes par classe ==> " + moyenne(nbrMethodes, nbrClasses) + "\n"
                + "Nombre moyen de lignes de code par méthode ==> " + moyenne(nbrLignesMethode, nbrMethodes) + "\n"
                + "Nombre d'attribut: " + nbrAttribute + "\n"
                + "Nombre moyenne d'attribut par classe :" + (double) moyenne(nbrAttribute, nbrClasses) + "\n"
                + classes10percentMethods() + "\n"
                + classes10percentAttributes() + "\n"
                + classesMostAttributesAndMethods() + "\n"
                + classesWithMoreThanMethods(this.x) + "\n"
                + "Le nombre maximal de paramètres par rapport à toutes les méthodes de l’application: " + nbrMaxParameters;

        return results;
    }

    // read all java files from specific folder
    public static ArrayList<File> listJavaFilesForFolder(final File folder) {
        ArrayList<File> javaFiles = new ArrayList<File>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                javaFiles.addAll(listJavaFilesForFolder(fileEntry));
            } else if (fileEntry.getName().contains(".java")) {
                javaFiles.add(fileEntry);
            }
        }

        return javaFiles;
    }
    //create AST
    private CompilationUnit parse(char[] classSource) {
        String javaHome = System.getProperty("java.home");

        ASTParser parser = ASTParser.newParser(AST.JLS4); // Java +1.6
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);

        Map<String, String> options = JavaCore.getOptions();
        parser.setCompilerOptions(options);

        parser.setUnitName("");

        String[] sources = {projectSourcePath};
        String[] classpath = {javaHome}; // Utilisez JAVA_HOME ici

        parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);
        parser.setSource(classSource);

        return (CompilationUnit) parser.createAST(null); // Crée et analyse
    }

    public int getNbClasses(){

        ClassCountVisitor visitor = new ClassCountVisitor();

        for (CompilationUnit cUnit: cUnits)
            cUnit.accept(visitor);

        return visitor.getClassCount();
    }

    public List<String> classes10percentMethods()
    {
        ClassCountVisitor ClassVisitor = new ClassCountVisitor();

        for (CompilationUnit cUnit: cUnits)
            cUnit.accept(ClassVisitor);

        return ClassVisitor.get10PercentMostMethods();
    }

    public List<String> classes10percentAttributes()
    {
        ClassCountVisitor ClassVisitor = new ClassCountVisitor();

        for (CompilationUnit cUnit: cUnits)
            cUnit.accept(ClassVisitor);

        return ClassVisitor.get10PercentMostAttributes();
    }

    public List<String> classesMostAttributesAndMethods() {

        List<String> classesMostAttributes = classes10percentAttributes();
        List<String> classesMostMethods = classes10percentMethods();
        List<String> result = new ArrayList<>();

        for (String cA : classesMostAttributes){
                if(classesMostMethods.contains(cA)){
                    result.add(cA);
                }
        }

        return result;

    }

    public List<String> classesWithMoreThanMethods(int nbMethods)
    {
        ClassCountVisitor ClassVisitor = new ClassCountVisitor();

        for (CompilationUnit cUnit: cUnits)
            cUnit.accept(ClassVisitor);

        return ClassVisitor.getClassesWithMoreThanMethods(nbMethods);
    }

    public Map<String,List<String>> methodsWithMostCodeLinesPerClasses()
    {
        MethodDeclarationVisitor MethodVisitor = new MethodDeclarationVisitor();

        for (CompilationUnit cUnit: cUnits)
            cUnit.accept(MethodVisitor);

        return MethodVisitor.get10PercentMostMethodsPerClasse();
    }


    public int getNbMethods(){

        MethodDeclarationVisitor MethodVisitor=new MethodDeclarationVisitor();

        for (CompilationUnit cUnit: cUnits)
            cUnit.accept(MethodVisitor);

        return MethodVisitor.getMethodCount();
    }

    public int getNbLigneMethods(){

        MethodDeclarationVisitor MethodVisitor=new MethodDeclarationVisitor();

        for (CompilationUnit cUnit: cUnits)
            cUnit.accept(MethodVisitor);

        return MethodVisitor.getNbrLigneMethodes();
    }

    public int getNbPackages(){

        PackageDeclarationVisitor Packagevisitor = new PackageDeclarationVisitor();

        for (CompilationUnit cUnit: cUnits)
            cUnit.accept(Packagevisitor);

        return Packagevisitor.getPackageCount();
    }

    public int getNumberOfLines(String fileName) {
        Path path = Paths.get(fileName);
        long lines = 0;
        try {
            lines = Files.lines(path).count();
        } catch (IOException e) { e.printStackTrace(); }
        return (int)lines;
    }

    public int getNbrParameterMax() {
        MethodDeclarationVisitor parametersvisitor = new MethodDeclarationVisitor();
        for (CompilationUnit cUnit: cUnits)
            cUnit.accept(parametersvisitor);

        return parametersvisitor.getMaxParameters();
    }


    public double moyenne(int nbr,int nbrClasse){
        double resultat = (double) nbr / nbrClasse;
        double arrondi = Math.round(resultat * 1000.0) / 1000.0;
        return arrondi;
    }


    public void processFolder(File folder) {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    processFolder(file);
                }
            }
        }
    }

    public ArrayList<CompilationUnit> getcUnits() {
        return cUnits;
    }

    public int getnbrAttribut(){
        AttributDeclarationVisitor Attributevisitor = new AttributDeclarationVisitor();
        for (CompilationUnit cUnit: cUnits)
            cUnit.accept(Attributevisitor);

        // Calculez le nombre moyen d'attributs par classe
        int totalAttributes = 0;
        int classCount = Attributevisitor.getClassCount();

        for (Integer attributeCount : Attributevisitor.getAttributeCounts().values()) {
            totalAttributes += attributeCount;
        }
        return totalAttributes;
    }
}
