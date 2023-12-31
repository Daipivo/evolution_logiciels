package org.example;

import org.example.Spoon.Processor.LoggingProcessor;

public class Main {
    public static void main(String[] args) {
        String projectPath = "C:\\Users\\Sandratra\\Desktop\\Projet M2 UM\\evolution_logiciels\\Software Logging\\TPLogging";
        System.out.println("------- Go spoon ---------");
        LoggingProcessor loggingProcessor = new LoggingProcessor(projectPath);
        loggingProcessor.setParser(projectPath); // Configure et ajoute le Processor
        loggingProcessor.getParser().run(); // Exécute le parsing et la transformation
    }
}