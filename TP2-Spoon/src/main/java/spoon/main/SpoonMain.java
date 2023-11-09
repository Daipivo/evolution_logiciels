package spoon.main;

import graph.Pair;
import spoon.couplage.ClusteringHierarchique;
import spoon.couplage.DisplayWeightedGraph;
import spoon.couplage.ModuleIdentifier;
import spoon.couplage.SpoonCouplage;
import spoon.parser.SpoonParser;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpoonMain {
    public static void main(String[] args) {
        // Remplacez le chemin du projet par le chemin réel de votre projet
        String projectPath = "/home/e20220012486/Semestre1/ingenierie Logiciel/EauDuBidons";

        // Créez une instance de votre SpoonParser en passant le chemin du projet
        SpoonParser spoonParser = new SpoonParser(projectPath);

        // Configurez le parser en spécifiant les répertoires de sortie et les options
        spoonParser.configure();

        // Exécutez le parser pour générer l'AST
        spoonParser.run();

        // L'AST est maintenant généré et prêt à être utilisé pour l'analyse ou la transformation.
        CtModel model=spoonParser.getParser().getModel();

        //Recupperer toute les classes de l'application
        SpoonCouplage cpl=new SpoonCouplage(model);
        List<String> classes=cpl.getStringClasses();

        // Affichez les détails de l'objet SpoonCouplage en utilisant la méthode toString
        System.out.println(cpl.toString());
        // Affichage du graphe de couplage
        DisplayWeightedGraph displayWeightedGraph = new DisplayWeightedGraph(cpl);
        displayWeightedGraph.displayGraph();

        Map<Pair<String, String>, Double> weightedGraphExempleRapport = new HashMap<>();
        double totalRelations = 8.00;

        weightedGraphExempleRapport.put(new Pair<>("ClassA", "ClassB"), 3.0/totalRelations);
        weightedGraphExempleRapport.put(new Pair<>("ClassA", "ClassC"), 2.0/totalRelations);
        weightedGraphExempleRapport.put(new Pair<>("ClassB", "ClassC"), 2.0/totalRelations);
        weightedGraphExempleRapport.put(new Pair<>("ClassC", "ClassC"), 1.0/totalRelations);

        ClusteringHierarchique clusteringExempleRapport = new ClusteringHierarchique(weightedGraphExempleRapport);
        System.out.println(clusteringExempleRapport);

        ModuleIdentifier moduleIdentifier = new ModuleIdentifier(clusteringExempleRapport, weightedGraphExempleRapport, 0.2, 3);
        System.out.println(moduleIdentifier);


    }

    // Fonction récursive pour afficher l'AST
    public static void displayAST(CtElement element, int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("  "); // Indentation pour montrer la profondeur dans l'arbre
        }
        System.out.println(element.getClass().getSimpleName() + " " + element.getShortRepresentation());

        // Parcourez les sous-éléments de cet élément
        for (CtElement subElement : element.getElements(null)) {
            displayAST(subElement, depth + 1);
        }
    }
}
