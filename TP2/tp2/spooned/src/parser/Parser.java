package parser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import utils.ApplicationStatistics;
import visiteur.AttributDeclarationVisitor;
import visiteur.ClassCountVisitor;
import visiteur.MethodDeclarationVisitor;
import visiteur.PackageDeclarationVisitor;
import org.eclipse.jdt.core.JavaCore;
public class Parser {
    // recuperer le fichier a parser
    private String projectPath;

    private String projectSourcePath;

    private int x;

    ArrayList<CompilationUnit> cUnits = new ArrayList<>();

    public Parser() {
    }

    public Parser(String projectPath, int x) {
        this.x = x;
        this.projectPath = projectPath;
        this.projectSourcePath = this.projectPath + "/src";
    }

    public Parser(String projectPath) {
        this.projectPath = projectPath;
        this.projectSourcePath = this.projectPath + "/src";
    }

    public ApplicationStatistics ParseFolder() {
        final File folder = new File(projectSourcePath);
        ApplicationStatistics stats = new ApplicationStatistics();
        ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
        int nbrClasses = 0;
        int nbrLignes = 0;
        int nbrMethodes = 0;
        int nbrLignesMethode = 0;
        int nbrPackage = 0;
        int nbrAttribute = 0;
        int nbrMaxParameters = 0;
        processFolder(folder);
        for (File fileEntry : javaFiles) {
            String content = null;
            nbrLignes += getNumberOfLines(fileEntry.getAbsolutePath());
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
        nbrLignesMethode = getNbLigneMethods();
        nbrPackage = getNbPackages();
        nbrAttribute = getnbrAttribut();
        nbrMaxParameters = getNbrParameterMax();
        // String results = "Nombre total de classes : \n" + nbrClasses + "\n"
        // + "Nombre total de lignes de code : \n" + nbrLignes + "\n"
        // + "Nombre total de méthodes : \n" + nbrMethodes + "\n"
        // + "Nombre total de packages : \n" + nbrPackage + "\n"
        // + "Nombre moyen de méthodes par classe : \n" + moyenne(nbrMethodes, nbrClasses) + "\n"
        // + "Nombre moyen de lignes de code par méthode : \n" + moyenne(nbrLignesMethode, nbrMethodes) + "\n"
        // + "Nombre d'attributs : " + nbrAttribute + "\n"
        // + "Nombre moyen d'attributs par classe : \n" + (double) moyenne(nbrAttribute, nbrClasses) + "\n"
        // + "Classes avec au moins 10% des méthodes : \n" + classes10percentMethods() + "\n"
        // + "Classes avec au moins 10% des attributs : \n" + classes10percentAttributes() + "\n"
        // + "Classes avec le plus d'attributs et de méthodes : \n" + classesMostAttributesAndMethods() + "\n"
        // + "Classes avec plus de " + x + " méthodes : \n" + classesWithMoreThanMethods(x) + "\n"
        // + "Nombre maximal de paramètres dans une méthode : " + nbrMaxParameters;
        stats.setNumberOfClasses(getNbClasses());
        stats.setNumberOfLinesOfCode(nbrLignes);
        stats.setTotalNumberOfMethods(getNbMethods());
        stats.setTotalNumberOfPackages(getNbPackages());
        stats.setAverageMethodsPerClass(moyenne(getNbMethods(), getNbClasses()));
        stats.setAverageLinesOfCodePerMethod(moyenne(getNbLigneMethods(), getNbMethods()));
        stats.setAverageAttributesPerClass(((double) (moyenne(getnbrAttribut(), getNbClasses()))));
        stats.setClassesWithMostMethods(classes10percentMethods());
        stats.setClassesWithMostAttributes(classes10percentAttributes());
        stats.setClassesWithBothAttributesAndMethods(classesMostAttributesAndMethods());
        stats.setClassesWithMoreThanXMethods(classesWithMoreThanMethods(x));
        stats.setMaxParametersInMethods(getNbrParameterMax());
        stats.setMethodsWithMostLinesOfCode(classesMostAttributesAndMethods());
        return stats;
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

    // create AST
    private CompilationUnit parse(char[] classSource) {
        String javaHome = System.getProperty("java.home");
        ASTParser parser = ASTParser.newParser(AST.JLS4);// Java +1.6

        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setBindingsRecovery(true);
        Map<String, String> options = JavaCore.getOptions();
        parser.setCompilerOptions(options);
        parser.setUnitName("");
        String[] sources = new String[]{ projectSourcePath };
        String[] classpath = new String[]{ javaHome };// Utilisez JAVA_HOME ici

        parser.setEnvironment(classpath, sources, new String[]{ "UTF-8" }, true);
        parser.setSource(classSource);
        return ((CompilationUnit) (parser.createAST(null)));// Crée et analyse

    }

    public int getNbClasses() {
        ClassCountVisitor visitor = new ClassCountVisitor();
        for (CompilationUnit cUnit : cUnits)
            cUnit.accept(visitor);

        return visitor.getClassCount();
    }

    public List<String> classes10percentMethods() {
        ClassCountVisitor ClassVisitor = new ClassCountVisitor();
        for (CompilationUnit cUnit : cUnits)
            cUnit.accept(ClassVisitor);

        return ClassVisitor.get10PercentMostMethods();
    }

    public List<String> getClasses() {
        ClassCountVisitor ClassVisitor = new ClassCountVisitor();
        for (CompilationUnit cUnit : cUnits)
            cUnit.accept(ClassVisitor);

        return // Transformation des éléments en noms de classe
        ClassVisitor.getClasses().stream().map(c -> c.getName().toString()).collect(Collectors.toList());
    }

    public List<String> classes10percentAttributes() {
        ClassCountVisitor ClassVisitor = new ClassCountVisitor();
        for (CompilationUnit cUnit : cUnits)
            cUnit.accept(ClassVisitor);

        return ClassVisitor.get10PercentMostAttributes();
    }

    public List<String> classesMostAttributesAndMethods() {
        List<String> classesMostAttributes = classes10percentAttributes();
        List<String> classesMostMethods = classes10percentMethods();
        List<String> result = new ArrayList<>();
        for (String cA : classesMostAttributes) {
            if (classesMostMethods.contains(cA)) {
                result.add(cA);
            }
        }
        return result;
    }

    public List<String> classesWithMoreThanMethods(int nbMethods) {
        ClassCountVisitor ClassVisitor = new ClassCountVisitor();
        for (CompilationUnit cUnit : cUnits)
            cUnit.accept(ClassVisitor);

        return ClassVisitor.getClassesWithMoreThanMethods(nbMethods);
    }

    public Map<String, List<String>> methodsWithMostCodeLinesPerClasses() {
        MethodDeclarationVisitor MethodVisitor = new MethodDeclarationVisitor();
        for (CompilationUnit cUnit : cUnits)
            cUnit.accept(MethodVisitor);

        return MethodVisitor.get10PercentMostMethodsPerClasse();
    }

    public int getNbMethods() {
        MethodDeclarationVisitor MethodVisitor = new MethodDeclarationVisitor();
        for (CompilationUnit cUnit : cUnits)
            cUnit.accept(MethodVisitor);

        return MethodVisitor.getMethodCount();
    }

    public int getNbLigneMethods() {
        MethodDeclarationVisitor MethodVisitor = new MethodDeclarationVisitor();
        for (CompilationUnit cUnit : cUnits)
            cUnit.accept(MethodVisitor);

        return MethodVisitor.getNbrLigneMethodes();
    }

    public int getNbPackages() {
        PackageDeclarationVisitor Packagevisitor = new PackageDeclarationVisitor();
        for (CompilationUnit cUnit : cUnits)
            cUnit.accept(Packagevisitor);

        return Packagevisitor.getPackageCount();
    }

    public int getNumberOfLines(String fileName) {
        Path path = Paths.get(fileName);
        long lines = 0;
        try {
            lines = Files.lines(path).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ((int) (lines));
    }

    public int getNbrParameterMax() {
        MethodDeclarationVisitor parametersvisitor = new MethodDeclarationVisitor();
        for (CompilationUnit cUnit : cUnits)
            cUnit.accept(parametersvisitor);

        return parametersvisitor.getMaxParameters();
    }

    public double moyenne(int nbr, int nbrClasse) {
        double resultat = ((double) (nbr)) / nbrClasse;
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

    public int getnbrAttribut() {
        AttributDeclarationVisitor Attributevisitor = new AttributDeclarationVisitor();
        for (CompilationUnit cUnit : cUnits)
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