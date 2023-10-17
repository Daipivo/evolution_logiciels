package couplage;

import scala.util.parsing.combinator.testing.Str;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

    List<String> classes;
    double weight;

    public Cluster(){

    }

    public Cluster(String classe){
        classes = classes;
    }

    public Cluster(ArrayList<String> classes, double weight){
        classes = classes;
        weight = weight;
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
