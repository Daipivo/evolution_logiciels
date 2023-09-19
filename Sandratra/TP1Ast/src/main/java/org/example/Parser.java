package org.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Parser {
    //recuperer le fichier a parser
    private String projectPath;
    public Parser(){

    }
    public Parser(String projectPath){
        this.projectPath=projectPath;
    }



    public void ParseFolder(){
        String projectSourcePath = this.projectPath + "/src";
        final File folder = new File(projectSourcePath);
        ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

        //
        for (File fileEntry : javaFiles) {
            String content = null;
            try {
                content = FileUtils.readFileToString(fileEntry);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(content);
        }
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
//    private static CompilationUnit parse(char[] classSource) {
//        ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
//        parser.setResolveBindings(true);
//        parser.setKind(ASTParser.K_COMPILATION_UNIT);
//
//        parser.setBindingsRecovery(true);
//
//        Map options = JavaCore.getOptions();
//        parser.setCompilerOptions(options);
//
//        parser.setUnitName("");
//
//        String[] sources = { projectSourcePath };
//        String[] classpath = {jrePath};
//
//        parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
//        parser.setSource(classSource);
//
//        return (CompilationUnit) parser.createAST(null); // create and parse
//    }

}
