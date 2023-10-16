package couplage;

import graph.CallGraph;
import graph.Pair;

import java.util.*;

public class Couplage {

    private CallGraph graph;
    private int cpt = 0;

    public Set<Pair<String, String>> getTest() {
        return test;
    }

    private Set<Pair<String, String>> test = new HashSet<>();

    public Couplage(CallGraph graph){
        this.graph = graph;
    }

    public double getCouplageGraph(){

        double numberOfRelationsBtwAllClasses = 0.00;

        Set<String> cls = graph.getGraphIntern().keySet();
        List<String> clsList = new ArrayList<>(cls);

        System.out.println(clsList);

        numberOfRelationsBtwAllClasses = cls.stream()
                .mapToDouble(c -> getCouplage(c, c, true))
                .sum();

        for (int i = 0; i < clsList.size() - 1; i++) {

            for (int j = i + 1; j < clsList.size(); j++) {

//                System.out.println(clsList.get(i) + " " + clsList.get(j));
//
//                if(clsList.get(i).equals("CallGraph")){
//                    System.out.println(clsList.get(j));
//                }
//
//                if(clsList.get(j).equals("CallGraph")){
//                    System.out.println(clsList.get(i));
//                }

                numberOfRelationsBtwAllClasses += getCouplage(clsList.get(i), clsList.get(j), false);
//                System.out.println(numberOfRelationsBtwAllClasses);
            }
        }


        return numberOfRelationsBtwAllClasses;

    }


    public double getCouplage(String class1, String class2, boolean isRefl){

        Map<String, List<String>> class1Methods = graph.getCalledMethodsInClasse(class1);
        Map<String, List<String>> class2Methods = graph.getCalledMethodsInClasse(class2);

        double numberOfRelationsBtwClasses = 0;

        for ( String cls1m : class1Methods.keySet()){

            for ( String methodscls1 : class1Methods.get(cls1m)){

                if(class2Methods.keySet().contains(methodscls1)){
                    test.add(new Pair<>(class2 + ":" + cls1m, class1 + ":" + methodscls1));
                    if(class1.equals("CallGraph")){
                        System.out.println(class2);
                    }
                    if(class2.equals("CallGraph")){
                        System.out.println(class1);
                    }
                    numberOfRelationsBtwClasses++;
                    cpt++;
                }
            }
        }

        if(!isRefl) {
            for (String cls2m : class2Methods.keySet()) {


                for (String methodscls2 : class2Methods.get(cls2m)) {

                    if (class1Methods.keySet().contains(methodscls2) && !methodscls2.equals(cls2m))  {
//                        System.out.println(class1 + ":" + cls2m);
                        test.add(new Pair<>(class1 + ":" + cls2m, class2 + ":" + methodscls2));
                        if(class1.equals("CallGraph")){
                            System.out.println(class2);
                        }
                        if(class2.equals("CallGraph")){
                            System.out.println(class1);
                        }
                        numberOfRelationsBtwClasses++;
                        cpt++;

                    }
                }
            }
        }

        return (double)numberOfRelationsBtwClasses/(double)graph.getNbrAretesIntern();
    }

    public int getCpt(){
        return cpt;
    }
}
