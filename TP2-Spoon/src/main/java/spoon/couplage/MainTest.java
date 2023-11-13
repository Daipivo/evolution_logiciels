package spoon.couplage;

import graph.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainTest {
    public static void main(String[] args) {
        Map<Pair<String,String>,Double> liste=new HashMap<>();

        Pair<String,String> a=new Pair<>("A","B");
        Pair<String,String> b=new Pair<>("A","C");
        Pair<String,String> c=new Pair<>("C","B");

        liste.put(a,15.2);
        liste.put(c,18.2);
        liste.put(b,25.2);

        System.out.println(liste.get(new Pair<>("A","C")));


    }
}
