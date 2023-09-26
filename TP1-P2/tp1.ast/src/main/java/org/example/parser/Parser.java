package org.example.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.example.visiteur.*;

public class Parser {
    //recuperer le fichier a parser
    private String projectPath;
    private String projectSourcePath;

    public Parser(){

    }
    
    public Parser(String projectPath){
        this.projectPath=projectPath;
        this.projectSourcePath = this.projectPath + "/src";
    }



    public void ParseFolder(){
        final File folder = new File(projectSourcePath);
        ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
        int nbrClasse=0;
        int nbrLigne=0;
        int nbrMethode=0;
        int nbrPackage=0;
        int nbrAttribute=0;
        //

        processFolder(folder);


        for (File fileEntry : javaFiles) {
            String content = null;

            System.out.println(fileEntry.getName());

            nbrLigne += (int) getNumberOfLines(fileEntry.getAbsolutePath());

            System.out.println(getNumberOfLines(fileEntry.getAbsolutePath()));

            try {
                content = FileUtils.readFileToString(fileEntry);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //printMethodInfo(parse);
            CompilationUnit parse = parse(content.toCharArray());
            //System.out.println(content);
            nbrClasse+=getNbrClassInFiles(parse);
            //nbrLigne+=getNbrLineInFile(parse);
            nbrMethode+=getNbrMethodeInFile(parse);
            nbrPackage += getNbrPackageInFolder(parse);
            nbrAttribute+=getnbrAttribut(parse);
        }
        // Affichez le nombre de classes

        System.out.println("Nombre total de classes dans le projet : " + nbrClasse);
        System.out.println("Nombre total de Ligne de code dans le projet : " + nbrLigne);
        System.out.println("Nombre total de Methode dans le projet : " + nbrMethode);
        //Nombre	moyen	de	méthodes	par	classe.
        System.out.println("Nombre moyen de méthodes par classe : " + moyenne(nbrMethode,nbrClasse));
        //Nombre	moyen	de	lignes	de	code	par	méthode
        System.out.println("Nombre moyen de lignes de code par méthode : "+moyenne(nbrLigne,nbrMethode));
        System.out.println("Nombre de packages : " + nbrPackage);
        System.out.println("Nombre d'attribut dans le projet :"+nbrAttribute);
        System.out.println("Nombre moyenne d'attribut par classe :"+moyenne(nbrAttribute,nbrClasse));
    }

    // read all java files from specific folder
    public static ArrayList<File> listJavaFilesForFolder(final File folder) {
        ArrayList<File> javaFiles = new ArrayList<File>();
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                javaFiles.addAll(listJavaFilesForFolder(fileEntry));
            } else if (fileEntry.getName().contains(".java")) {
                System.out.println(fileEntry.getName());
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

    //Methode declaration
    public void printMethodInfo(CompilationUnit parse) {
        MethodDeclarationVisitor visitor = new MethodDeclarationVisitor();
        parse.accept(visitor);

        for (MethodDeclaration method : visitor.getMethods()) {
            System.out.println("Method name: " + method.getName()
                    + " Return type: " + method.getReturnType2());
        }

    }

    public int getNbrClassInFiles(CompilationUnit parse){
        ClassCountVisitor visitor=new ClassCountVisitor();
        parse.accept(visitor);
        return visitor.getClassCount();
    }

    public int getNbrLineInFile(CompilationUnit parse){
        LineCountVisitor visitor=new LineCountVisitor();
        parse.accept(visitor);
        return visitor.getLineCount();
    }

    public int getNbrMethodeInFile(CompilationUnit parse){
        MethodDeclarationVisitor visitor=new MethodDeclarationVisitor();
        parse.accept(visitor);
        return visitor.getMethodCount();
    }

    public int getNbrPackageInFolder(CompilationUnit parse){
        PackageDeclarationVisitor visitor = new PackageDeclarationVisitor();
        parse.accept(visitor);
        return visitor.getPackageCount();
    }

    public long getNumberOfLines(String fileName) {
        Path path = Paths.get(fileName);
        long lines = 0;
        try {
            lines = Files.lines(path).count();
        } catch (IOException e) { e.printStackTrace(); }
        return lines;
    }

    public double moyenne(int nbr,int nbrClasse){
        return nbr/nbrClasse;
    }

    public int getnbrAttribut(CompilationUnit parse){
        AttributDeclarationVisitor visitor = new AttributDeclarationVisitor();
        parse.accept(visitor);

        // Calculez le nombre moyen d'attributs par classe
        int totalAttributes = 0;
        int classCount = visitor.getClassCount();

        for (Integer attributeCount : visitor.getAttributeCounts().values()) {
            totalAttributes += attributeCount;
        }
        return totalAttributes;
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
}