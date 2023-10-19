package couplage;

import java.util.*;
import graph.Pair;

import static java.lang.Math.floor;

public class ModuleIdentifier {

    private Set<Cluster> clusteringHierarchique;
    private Map<Pair<String, String>, Double> weightedGraph;

    private Set<Set<String>> moduleIdentfiers = new HashSet<>();
    private double CP;
    private double M;

    /**
     * Constructeur de la classe ModuleIdentifier.
     *
     * @param clusteringHierarchique L'ensemble des clusters de l'application.
     * @param weightedGraph Le graphe pondéré de l'application.
     * @param CP La valeur CP correspond au seuil de couplage entre deux classes.
     * @param M La valeur M correspond au nombre de classes de l'application.
     */
    public ModuleIdentifier(ClusteringHierarchique clusteringHierarchique, Map<Pair<String, String>, Double> weightedGraph, double CP, double M) {
        this.clusteringHierarchique = clusteringHierarchique.getDendrogramme();
        this.weightedGraph = weightedGraph;
        this.CP = CP;
        this.M = M;
        this.moduleIdentfiers = identifyModules();
    }

    /**
     * Identifie les modules basés sur le clustering hiérarchique, le graphe pondéré, CP et M.
     *
     * @return Un ensemble de modules identifiés.
     */
    private Set<Set<String>> identifyModules() {

        Set<Set<String>> modules = new HashSet<>();

        for (Cluster cluster : clusteringHierarchique) {

            Set<String> classes = cluster.getClasses();

            double totalCoupling = 0.0;
            int totalPairs = 0;

            for (String class1 : classes) {
                for (String class2 : classes) {
                    Pair<String, String> pair1 = new Pair<>(class1, class2);
                    Pair<String, String> pair2 = new Pair<>(class2, class1);
                    if (weightedGraph.containsKey(pair1)) {
                        totalCoupling += weightedGraph.get(pair1);
                        totalPairs++;
                    }
                    else if (weightedGraph.containsKey(pair2)) {
                        totalCoupling += weightedGraph.get(pair2);
                        totalPairs++;
                    }
                }
            }

            if (totalPairs == 0 || totalCoupling/totalPairs <= CP) continue;  // Si la moyenne de couplage est inférieure à CP, passez au cluster suivant

            modules.add(classes);
            if (modules.size() >= floor(M/2.00)) break;  // Ne pas dépasser M/2 modules

        }
        return modules;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Modules identifiés basés sur le dendrogramme, CP : " + this.CP + " et M : " + this.M +"\n");

        int moduleCounter = 1;
        for (Set<String> module : moduleIdentfiers) {
            builder.append("Module ").append(moduleCounter).append(":\n");
            for (String className : module) {
                builder.append("\t").append(className).append("\n");
            }
            moduleCounter++;
        }

        return builder.toString();
    }
}
