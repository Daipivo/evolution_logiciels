package spoon.couplage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Cluster {

    private Set<String> classes;
    private Set<Cluster> clusters;
    public Cluster(String classe) {
        this.classes = new HashSet<>();
        this.clusters = new HashSet<>();
        this.classes.add(classe);
    }
    public Cluster(Cluster c1, Cluster c2) {

        this.classes = new HashSet<>();
        this.classes.addAll(c1.getClasses());
        this.classes.addAll(c2.getClasses());

        this.clusters = new HashSet<>();
        this.clusters.add(c1);
        this.clusters.add(c2);
        this.clusters.addAll(c1.getClusters());
        this.clusters.addAll(c2.getClusters());

    }

    public Set<String> getClasses() {
        return classes;
    }

    public Set<Cluster> getClusters() {
        return clusters;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        // Ajout des classes
        builder.append("Classes du cluster : ");
        builder.append(this.classes);

        // Ajout du nombre de clusters
        builder.append(" : ce cluster contient ");
        builder.append(this.clusters.size());
        builder.append(" cluster(s) enfant(s)");

        return builder.toString();
    }

}

