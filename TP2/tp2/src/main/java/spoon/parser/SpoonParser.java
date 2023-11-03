package spoon.parser;

import spoon.Launcher;
import spoon.processing.Processor;
import spoon.reflect.declaration.CtClass;
import Abstract.AbstractParser;

public class SpoonParser extends AbstractParser<Launcher> {

    public SpoonParser(String projectPath) {
        super(projectPath);
    }

    public void setLauncher(String sourceOutputPath, String binaryOutputPath,
                            boolean autoImports, boolean commentsEnabled) {
        parser = new Launcher(); // Créer le lanceur (Launcher)
        parser.addInputResource(getProjectSrcPath()); // Définir le chemin des sources du projet
//        parser.getEnvironment().setSourceClasspath(new String[] {getProjectBinPath()}); // Définir le chemin de classe du projet
        parser.setSourceOutputDirectory(sourceOutputPath); // Définir le chemin du répertoire pour le code source généré
        parser.setBinaryOutputDirectory(binaryOutputPath); // Définir le chemin du répertoire pour le code binaire généré
        parser.getEnvironment().setAutoImports(autoImports); // Définir l'importation automatique
        parser.getEnvironment().setCommentEnabled(commentsEnabled); // Activer/désactiver les commentaires
    }

    public void configure() {
        setLauncher(projectPath + "/spooned/src/", projectPath + "/spooned/bin/", true, true);
    }

    public void addProcessor(Processor<CtClass> processor) {
        parser.addProcessor(processor);
    }

    public void run() {
        parser.run();
    }
}
