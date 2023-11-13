package couplage;
import graph.CallGraph;
import graph.Pair;
import java.io.IOException;
import java.util.*;
import scala.util.parsing.combinator.testing.Str;
public class MainCouplage {
    public static void main(String[] args) throws IOException {
        /* Instance du projet courant */
        CallGraph graph = new CallGraph("C:\\Users\\Sandratra\\Desktop\\Projet M2 UM\\evolution_logiciels\\TP2\\tp2");
        Couplage couplage = new Couplage(graph);
        // Affichage du graphe de couplage
        DisplayWeightedGraph displayWeightedGraph = new DisplayWeightedGraph(couplage);
        displayWeightedGraph.displayGraph();
        /* Instance utilisé pour le rapport */
        Map<Pair<String, String>, Double> weightedGraphExempleRapport = new HashMap<>();
        double totalRelations = 8.0;
        weightedGraphExempleRapport.put(new Pair<>("ClassA", "ClassB"), 3.0 / totalRelations);
        weightedGraphExempleRapport.put(new Pair<>("ClassA", "ClassC"), 2.0 / totalRelations);
        weightedGraphExempleRapport.put(new Pair<>("ClassB", "ClassC"), 2.0 / totalRelations);
        weightedGraphExempleRapport.put(new Pair<>("ClassC", "ClassC"), 1.0 / totalRelations);
        ClusteringHierarchique clusteringExempleRapport = new ClusteringHierarchique(weightedGraphExempleRapport);
        System.out.println(clusteringExempleRapport);
        ModuleIdentifier moduleIdentifier = new ModuleIdentifier(clusteringExempleRapport, weightedGraphExempleRapport, 0.2, 3);
        System.out.println(moduleIdentifier);
        /* Instance présent sur le TP */
        Map<Pair<String, String>, Double> weightedGraphTP = new HashMap<>();
        weightedGraphTP.put(new Pair<>("A", "C"), 0.3913);
        weightedGraphTP.put(new Pair<>("B", "D"), 0.3043);
        weightedGraphTP.put(new Pair<>("E", "C"), 0.1304);
        weightedGraphTP.put(new Pair<>("E", "A"), 0.0435);
        weightedGraphTP.put(new Pair<>("E", "D"), 0.1304);
        ClusteringHierarchique clusteringTP = new ClusteringHierarchique(weightedGraphTP);
        // Affichage du dendrogramme de l'instance du TP
        System.out.println(clusteringTP);
        // Identification de modules sur le graphe du TP
        ModuleIdentifier moduleIdentifierTP = new ModuleIdentifier(clusteringTP, weightedGraphTP, 0.2, 5);
        System.out.println(moduleIdentifierTP);
    }
}