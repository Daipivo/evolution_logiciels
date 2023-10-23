package spoon.main;

import spoon.parser.SpoonParser;
import spoon.processing.AbstractProcessor;
import spoon.processing.Processor;
import spoon.processor.SpoonProcessor;
import spoon.reflect.declaration.CtClass;

public class SpoonMain {
    public static void main(String[] args) {
        // Remplacez le chemin du projet par le chemin réel de votre projet
        String projectPath = "C:\\Users\\Sandratra\\Desktop\\Projet M1 UM\\HAI712I - Ingénierie Logicielle\\TP4-EauDuBidon";

        // Créez une instance de votre SpoonParser en passant le chemin du projet
        SpoonParser spoonParser = new SpoonParser(projectPath);

        // Créez une instance de votre SpoonProcessor en passant le chemin du projet
        SpoonProcessor spoonProcessor = new SpoonProcessor(projectPath);

        spoonParser.configure();
        // Ajoutez le processeur personnalisé au parser
        //spoonParser.addProcessor((Processor<CtClass>) spoonProcessor);

        // Exécutez le parser pour générer l'AST
        spoonParser.run();
    }
}
