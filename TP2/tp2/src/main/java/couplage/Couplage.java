package couplage;

import graph.CallGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Couplage {

    private CallGraph graph;
    public Couplage(CallGraph graph){
        this.graph = graph;
    }

    public int getCouplageGraph(){

        int numberOfRelationsBtwAllClasses = 0;

        Set<String> cls = graph.getGraph().keySet();
        List<String> clsList = new ArrayList<>(cls);

            for (int i = 0; i < clsList.size() - 1; i++) {
                for (int j = i + 1; j < clsList.size(); j++) {
                    numberOfRelationsBtwAllClasses += getCouplage(clsList.get(i), clsList.get(j));
                }
            }

        return numberOfRelationsBtwAllClasses;

    }

    public int getCouplage(String class1, String class2){

        Map<String, List<String>> class1Methods = graph.getCalledMethodsInClasse(class1);
        Map<String, List<String>> class2Methods = graph.getCalledMethodsInClasse(class2);

        int numberOfRelationsBtwClasses = 0;

        for ( String cls1m : class1Methods.keySet()){

            for ( String methodscls1 : class1Methods.get(cls1m)){

                if(class2Methods.keySet().contains(methodscls1)){

                    numberOfRelationsBtwClasses++;
                }
            }
        }

        for ( String cls2m : class2Methods.keySet()){

            for ( String methodscls2 : class2Methods.get(cls2m)){

                if(class1Methods.keySet().contains(methodscls2)){

                    numberOfRelationsBtwClasses++;

                }
            }
        }

        return numberOfRelationsBtwClasses;
    }
}
