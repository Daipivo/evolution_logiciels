package facade;

import couplage.*;
import graph.CallGraph;

public class Facade {

    // CALL GRAPH

    public void createAndDisplayCallGraph(String path){
        CallGraph graph = new CallGraph(path);
        Couplage couplage = new Couplage(graph);
        DisplayWeightedGraph displayWeightedGraph = new DisplayWeightedGraph(couplage);
        displayWeightedGraph.displayGraph();
    }
    public void createAndDisplayCallGraph(Instance instance){
        DisplayWeightedGraph displayWeightedGraph = new DisplayWeightedGraph(instance.getWeightedGraph());
        displayWeightedGraph.displayGraph();
    }

    // CLUSTERING HIERARCHIQUE
    public ClusteringHierarchique createClusteringHierarchique(String path){
        CallGraph graph = new CallGraph(path);
        Couplage couplage = new Couplage(graph);
        return new ClusteringHierarchique(couplage.getWeightedGraph());
    }
    public void createAndDisplayClusteringHierarchique(String path){
        CallGraph graph = new CallGraph(path);
        Couplage couplage = new Couplage(graph);
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
        CallGraph graph = new CallGraph(path);
        Couplage couplage = new Couplage(graph);
        ModuleIdentifier moduleIdentifierTP = new ModuleIdentifier(clusteringHierarchique, couplage.getWeightedGraph(), CP, graph.getClasses().size());
        System.out.println(moduleIdentifierTP);
    }
    public void createAndDisplayModuleIdentifier(Instance instance, float CP){
        ClusteringHierarchique clusteringHierarchique = createClusteringHierarchique(instance);
        ModuleIdentifier moduleIdentifierTP = new ModuleIdentifier(clusteringHierarchique, instance.getWeightedGraph(), CP, instance.getNbrClasses());
        System.out.println(moduleIdentifierTP);
    }


}
