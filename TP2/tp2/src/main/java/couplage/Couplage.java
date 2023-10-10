package couplage;

import graph.CallGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Couplage {

    private CallGraph graph;
    private int cpt = 0;

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

                numberOfRelationsBtwAllClasses += getCouplage(clsList.get(i), clsList.get(j), false);
//                System.out.println(numberOfRelationsBtwAllClasses);
            }
        }


        return numberOfRelationsBtwAllClasses;

    }


    public double getCouplage(String class1, String class2, boolean isRefl){

        Map<String, List<String>> class1Methods = graph.getCalledMethodsInClasse(class1);
        Map<String, List<String>> class2Methods = graph.getCalledMethodsInClasse(class2);

//        System.out.println(class1 + " " + class1Methods);
//
//        System.out.println(" ======== ");
//
//        System.out.println(class2 + " " + class2Methods);

        double numberOfRelationsBtwClasses = 0;

        for ( String cls1m : class1Methods.keySet()){

//            System.out.println("Methode 1 ==> " + cls1m);

            for ( String methodscls1 : class1Methods.get(cls1m)){

//                System.out.println("Methode 1 bis ==> " + methodscls1);


                if(class2Methods.keySet().contains(methodscls1)){

                    numberOfRelationsBtwClasses++;
                    cpt++;
                }
            }
        }

        if(!isRefl) {
            for (String cls2m : class2Methods.keySet()) {

//            System.out.println("Methode 2 ==> " + cls2m);

                for (String methodscls2 : class2Methods.get(cls2m)) {

//                System.out.println("Methode 2 bis ==> " + methodscls2);
                    if (class1Methods.keySet().contains(methodscls2)){
                        if(methodscls2.equals(cls2m)){
                            System.out.println(cls2m + " " + methodscls2);
                        }
                    }


                    if (class1Methods.keySet().contains(methodscls2) && !methodscls2.equals(cls2m))  {

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
