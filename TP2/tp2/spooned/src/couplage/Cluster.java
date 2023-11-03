package couplage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
public class Cluster {
    private Set<String> classes;

    private Set<Cluster> clusters;

    private double weight;

    public Cluster(String classe) {
        this.classes = new HashSet<>();
        this.clusters = new HashSet<>();
        this.classes.add(classe);
    }

    public Cluster(Cluster c1, Cluster c2, double weight) {
        this.classes = new HashSet<>();
        this.classes.addAll(c1.getClasses());
        this.classes.addAll(c2.getClasses());
        this.clusters = new HashSet<>();
        this.clusters.addAll(c1.getClusters());
        this.clusters.addAll(c2.getClusters());
        this.weight = weight;
    }

    public Set<String> getClasses() {
        return classes;
    }

    public Set<Cluster> getClusters() {
        return clusters;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        // Ajout des classes
        builder.append("Classes: ");
        builder.append(classes);
        // Ajout des clusters enfants si existants
        if (!clusters.isEmpty()) {
            builder.append(", Child Clusters: [");
            List<String> clusterStrings = clusters.stream().map(Cluster::toString).collect(Collectors.toList());
            builder.append(String.join(", ", clusterStrings));
            builder.append("]");
        }
        // Ajout du poids
        builder.append(", Weight: ");
        builder.append(weight);
        return builder.toString();
    }
}