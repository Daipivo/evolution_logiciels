package couplage;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

    private List<String> classes;
    private double weight;

    public Cluster() {
        this.classes = new ArrayList<>();
    }

    public Cluster(String classe) {
        this.classes = new ArrayList<>();
        this.classes.add(classe);
    }

    public Cluster(String classe, double weight) {
        this.classes = new ArrayList<>();
        this.classes.add(classe);
        this.weight = weight;
    }

    public Cluster(List<String> classes, double weight) {
        this.classes = new ArrayList<>(classes);
        this.weight = weight;
    }
    public List<String> getClasses() {
        return classes;
    }

    public void setClasses(List<String> classes) {
        this.classes = classes;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getWeight(){
        return weight;
    }

}
