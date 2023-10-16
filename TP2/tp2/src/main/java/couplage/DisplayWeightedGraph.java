package couplage;

import graph.Pair;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import java.util.Map;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.swingViewer.DefaultView;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class DisplayWeightedGraph {

    private Graph graph;
    public DisplayWeightedGraph() {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph = new SingleGraph("Weighted Graph");
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
    }

    public void displayGraph(Map<Pair<String, String>, Double> weightedGraph) {
        weightedGraph.forEach((pair, weight) -> {
                String class1 = pair.getFirst();
                String class2 = pair.getSecond();

                Node node1 = graph.getNode(class1);
                if(node1 == null) {
                    node1 = graph.addNode(class1);
                    node1.setAttribute("ui.label", class1);
                }

                Node node2 = graph.getNode(class2);
                if(node2 == null) {
                    node2 = graph.addNode(class2);
                    node2.setAttribute("ui.label", class2);
                }

                String edgeId = class1 + "-" + class2;
                if (node1 != null && node2 != null) {
                    Edge edge = graph.addEdge(edgeId, class1, class2, false);
                    if (edge != null) {
                        edge.setAttribute("ui.label", String.format("%.3f", weight));
                    }
                }

        });

        Viewer viewer = graph.display();
        ViewPanel view = viewer.getDefaultView();

        if (view instanceof DefaultView) {
            ((DefaultView) view).addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    e.consume();
                    int i = e.getWheelRotation();
                    double factor = Math.pow(1.25, i);
                    DefaultView view = (DefaultView) e.getSource();
                    view.getCamera().setViewPercent(view.getCamera().getViewPercent() * factor);
                }
            });
        }

        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);

        // Ajuste la vue pour qu'elle s'adapte au graphe
        Toolkit.computeLayout(graph);
    }

    protected String styleSheet =
            "edge {" +
                    "   text-alignment: above;" +
                    "   text-size: 12px;" +
                    "}" +
                    "node {" +
                    "   size: 10px;" +
                    "   shape: circle;" +
                    "   fill-color: white;" +
                    "   stroke-mode: plain;" +
                    "   stroke-color: black;" +
                    "   text-size: 10px;" +
                    "}";

}
