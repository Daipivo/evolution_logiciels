package graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.util.List;
import java.util.Set;

public class DisplayGraph {
    private CallGraph callGraph;

    public DisplayGraph(CallGraph graph){
        this.callGraph = graph;
    }
    
    public Viewer displayGraph(){

        System.setProperty("org.graphstream.ui", "swing");

        List<Pair<String,String>> aretes = callGraph.getAretes();

        Graph graphDisplay = new SingleGraph("Mon graphe");

        aretes.forEach(paire -> {

            String node1Name = paire.getFirst();
            Node node1 = graphDisplay.getNode(node1Name);

            String node2Name = paire.getSecond();
            Node node2 = graphDisplay.getNode(node2Name);

            if(node1Name.equals(node2Name)){
                return;
            }

            if (node1 == null) {
                // Le nœud n'existe pas encore, créez-le
                node1 = graphDisplay.addNode(node1Name);
                node1.setAttribute("label", node1Name);
            }

            if (node2 == null) {
                // Le nœud n'existe pas encore, créez-le
                node2 = graphDisplay.addNode(node2Name);
                node2.setAttribute("label", node2Name);
            }


            Edge edgeAB = graphDisplay.getEdge(node1Name+node2Name);
            if(edgeAB == null){
                Edge e = graphDisplay.addEdge(node1Name+node2Name, node1, node2, true);
                e.addAttribute("layout.weight", 10);
            }
        });
        // Affichez le graphe (facultatif)
        graphDisplay.addAttribute("ui.stylesheet", "node {\n" +
                "\tsize: 2px;\n" +
                "\tfill-color: #777;\n" +
                "\tz-index: 0;\n" +
                "}\n" +
                "\n" +
                "edge {\n" +
                "\tshape: line;\n" +
                "\tfill-mode: dyn-plain;\n" +
                "\tfill-color: #222;\n" +
                "\tarrow-size: 2px, 4px;\n" +
                "}\n");

        graphDisplay.addAttribute("ui.quality");
        graphDisplay.addAttribute("ui.antialias");
        return graphDisplay.display();

    }

}
