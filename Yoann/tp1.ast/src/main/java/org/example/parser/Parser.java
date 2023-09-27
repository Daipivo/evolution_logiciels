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
import org.example.visiteur.ClassCountVisitor;
import org.example.visiteur.MethodDeclarationVisitor;
import org.example.visiteur.PackageDeclarationVisitor;

public class Parser {
    //recuperer le fichier a parser
    private String projectPath;
    private String projectSourcePath;

    ArrayList<CompilationUnit> cUnits = new ArrayList<>();


    public Parser(){

    }
    
    public Parser(String projectPath){
        this.projectPath=projectPath;
        this.projectSourcePath = this.projectPath + "/src";
    }



    public void ParseFolder(){
        final File folder = new File(projectSourcePath);
        ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
        int nbrClasses = 0;
        int nbrLignes = 0;
        int nbrMethodes = 0;
        int nbrPackage = 0;


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
        nbrPackage = getNbPackages();


        System.out.println("Nombre total de classes dans le projet ==> " + nbrClasses);
        System.out.println("Nombre total de lignes de code dans le projet ==> " + nbrLignes);
        System.out.println("Nombre total de méthodes dans le projet ==> " + nbrMethodes);
        System.out.println("Nombre total de packages dans le projet ==> " + nbrPackage);
        System.out.println("Nombre moyen de méthodes par classe ==> " + moyenne(nbrMethodes,nbrClasses));
        System.out.println("Nombre moyen de lignes de code par méthode ==> "+moyenne(nbrLignes,nbrMethodes));
        System.out.println(classes10percentMethods());
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

    public int getNbMethods(){

        MethodDeclarationVisitor MethodVisitor=new MethodDeclarationVisitor();

        for (CompilationUnit cUnit: cUnits)
            cUnit.accept(MethodVisitor);

        return MethodVisitor.getMethodCount();
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

    public double moyenne(int nbr,int nbrClasse){
        return nbr/nbrClasse;
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
