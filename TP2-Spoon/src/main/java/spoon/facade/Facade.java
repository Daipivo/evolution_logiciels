package spoon.facade;


import spoon.couplage.*;
import spoon.parser.SpoonParser;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

public class Facade {

    // CALL GRAPH

    public void createAndDisplayCallGraph(String path){
        SpoonParser spoonParser = new SpoonParser(path);
        spoonParser.configure();
        spoonParser.run();
        CtModel graph=spoonParser.getParser().getModel();
        SpoonCouplage couplage = new SpoonCouplage(graph);
        DisplayWeightedGraph displayWeightedGraph = new DisplayWeightedGraph(couplage);
        displayWeightedGraph.displayGraph();
    }
    public void createAndDisplayCallGraph(Instance instance){
        DisplayWeightedGraph displayWeightedGraph = new DisplayWeightedGraph(instance.getWeightedGraph());
        displayWeightedGraph.displayGraph();
    }

    // CLUSTERING HIERARCHIQUE
    public ClusteringHierarchique createClusteringHierarchique(String path){
        SpoonParser spoonParser = new SpoonParser(path);
        spoonParser.configure();
        spoonParser.run();
        CtModel graph=spoonParser.getParser().getModel();
        SpoonCouplage couplage = new SpoonCouplage(graph);
        return new ClusteringHierarchique(couplage.getWeightedGraph());
    }
    public void createAndDisplayClusteringHierarchique(String path){
        SpoonParser spoonParser = new SpoonParser(path);
        spoonParser.configure();
        spoonParser.run();
        CtModel graph=spoonParser.getParser().getModel();
        SpoonCouplage couplage = new SpoonCouplage(graph);
        ClusteringHierarchique clusteringHierarchique = new ClusteringHierarchique(couplage.getWeightedGraph());
        System.out.println(clusteringHierarchique);
    }
    public ClusteringHierarchique createClusteringHierarchique(Instance instance){
        ClusteringHierarchique clusteringExempleRapport = new ClusteringHierarchique(instance.getWeightedGraph());
        return clusteringExempleRapport;
    }
    public void createAndDisplayClusteringHierarchique(Instance instance){
        ClusteringHierarchique clusteringExempleRapport = new ClusteringHierarchique(instance.getWeightedGraph());
        System.out.println(clusteringExempleRapport);
    }

    // MODULE IDENTIFIER
    public void createAndDisplayModuleIdentifier(String path, float CP){
        ClusteringHierarchique clusteringHierarchique = createClusteringHierarchique(path);
        SpoonParser spoonParser = new SpoonParser(path);
        spoonParser.configure();
        spoonParser.run();
        CtModel graph=spoonParser.getParser().getModel();
        SpoonCouplage couplage = new SpoonCouplage(graph);

        List<CtClass<?>> classes = graph.filterChildren(new TypeFilter<>(CtClass.class)).list();
        int classCount = classes.size();
        System.out.println("class count "+classCount);

        ModuleIdentifier moduleIdentifierTP = new ModuleIdentifier(clusteringHierarchique, couplage.getWeightedGraph(), CP, classCount);
        System.out.println(moduleIdentifierTP);
    }
    public void createAndDisplayModuleIdentifier(Instance instance, float CP){
        ClusteringHierarchique clusteringHierarchique = createClusteringHierarchique(instance);
        ModuleIdentifier moduleIdentifierTP = new ModuleIdentifier(clusteringHierarchique, instance.getWeightedGraph(), CP, instance.getNbrClasses());
        System.out.println(moduleIdentifierTP);
    }


}
