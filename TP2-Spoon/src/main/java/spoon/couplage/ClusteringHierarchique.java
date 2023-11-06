package spoon.couplage;

import graph.Pair;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;
import java.util.*;

public class ClusteringHierarchique {

    private Set<Cluster> dendrogramme = new LinkedHashSet<>();
    private Map<Pair<String, String>, Double> weightedGraph;

    public ClusteringHierarchique(CtModel model,Map<Pair<String,String> ,Double> weightedGraph) {
        this.dendrogramme = clusteringHierarchique(model);
        this.weightedGraph = weightedGraph;
    }

    private Set<Cluster> clusteringHierarchique(CtModel model) {
        Set<Cluster> dendro = new LinkedHashSet<>();
        Set<Cluster> clusters = new HashSet<>();

        for (CtClass<?> ctClass : model.getElements(new TypeFilter<>(CtClass.class))) {
            addClusterIfNotExists(clusters, ctClass.getQualifiedName());
        }

        dendro.addAll(clusters);

        // Le reste de votre implémentation pour le clustering hiérarchique ici

        return dendro;
    }

    private void addClusterIfNotExists(Set<Cluster> clusters, String vertex) {
        for (Cluster existingCluster : clusters) {
            if (existingCluster.getClasses().contains(vertex)) {
                return; // Le cluster avec ce sommet existe déjà
            }
        }
        clusters.add(new Cluster(vertex)); // Ajoute un nouveau cluster si aucun match n'a été trouvé
    }

    /**
     * Calcule le couplage entre deux clusters.
     *
     * @param c1 Le premier cluster.
     * @param c2 Le deuxième cluster.
     * @return Le couplage entre les clusters c1 et c2.
     */
    public double couplageBtwClusters(Cluster c1, Cluster c2){
        Set<String> classesC1 = c1.getClasses();
        Set<String> classesC2 = c2.getClasses();
        double result = 0.00;

        for (String class1 : classesC1) {
            for (String class2 : classesC2) {
                Pair<String, String> pair1 = new Pair<>(class1, class2);
                Pair<String, String> pair2 = new Pair<>(class2, class1);
                if(weightedGraph.containsKey(pair1)) {
                    result += weightedGraph.get(pair1);
                }
                else if(weightedGraph.containsKey(pair2)) {
                    result += weightedGraph.get(pair2);
                }
            }
        }
        return result;
    }

    /**
     * Effectue un clustering hiérarchique et retourne un ensemble de clusters.
     *
     * @return Un ensemble représentant la dendrogramme de clusters.
     */
    public Set<Cluster> getDendrogramme(){
        return dendrogramme;
    }

    /**
     * Retourne une représentation sous forme de chaîne de la classe ClusteringHierarchique.
     *
     * @return une chaîne de caractères représentant l'objet ClusteringHierarchique.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Clustering Hierarchique {\n\tDendrogramme :\n");

        // Convertir le Set en List pour conserver l'ordre d'insertion
        List<Cluster> clustersList = new ArrayList<>(dendrogramme);

        // Parcourir la liste en sens inverse
        for (int i = clustersList.size() - 1; i >= 0; i--) {
            builder.append("\t\t").append(clustersList.get(i)).append("\n");
        }

        builder.append("}");
        return builder.toString();
    }



    // Reste du code inchangé
}
