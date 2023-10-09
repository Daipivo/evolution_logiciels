package org.example.graph;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.geom.Point2;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.view.Camera;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Set;

public class DisplayGraph {
    private CallGraph callGraph;

    public DisplayGraph(CallGraph graph){
        this.callGraph = graph;
    }
    
    public Viewer displayGraph(){

        System.setProperty("org.graphstream.ui", "swing");

        Set<Pair<String,String>> aretes = callGraph.getAretes();

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

        Viewer viewer = graphDisplay.display();
        viewer.enableAutoLayout();
        final View view = viewer.addDefaultView(true);
        view.getCamera().setViewPercent(1);
        ((Component) view).addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                e.consume();
                int i = e.getWheelRotation();
                double factor = Math.pow(1.25, i);
                Camera cam = view.getCamera();
                double zoom = cam.getViewPercent() * factor;
                Point2 pxCenter  = cam.transformGuToPx(cam.getViewCenter().x, cam.getViewCenter().y, 0);
                Point3 guClicked = cam.transformPxToGu(e.getX(), e.getY());
                double newRatioPx2Gu = cam.getMetrics().ratioPx2Gu/factor;
                double x = guClicked.x + (pxCenter.x - e.getX())/newRatioPx2Gu;
                double y = guClicked.y - (pxCenter.y - e.getY())/newRatioPx2Gu;
                cam.setViewCenter(x, y, 0);
                cam.setViewPercent(zoom);
            }
        });

        return viewer;

    }

}
