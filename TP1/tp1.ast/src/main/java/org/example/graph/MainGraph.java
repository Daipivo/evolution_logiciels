package org.example.graph;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;


public class MainGraph {

    public static void main(String[] args) throws IOException, InterruptedException {
        // Projet à tester
        CallGraph graph = new CallGraph("/home/reyne/Bureau/evolution_logiciels/TP1/tp1.ast");

        // Lancement du graph
        graph.start();

        DisplayGraph displayGraph = new DisplayGraph(graph);

        displayGraph.displayGraph();

    }

}
