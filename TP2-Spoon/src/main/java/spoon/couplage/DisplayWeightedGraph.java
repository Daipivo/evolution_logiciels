package spoon.couplage;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.DefaultView;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.j2dviewer.J2DGraphRenderer;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class DisplayWeightedGraph {

    private Graph graph;
    private SpoonCouplage couplage;
    public DisplayWeightedGraph(SpoonCouplage couplage) {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph = new SingleGraph("Weighted Graph");
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        this.couplage = couplage;
    }

    public void displayGraph() {


        couplage.getWeightedGraph().forEach((pair, weight) -> {
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
                    edge.addAttribute("layout.weight", 0.5);
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
                    "   text-size: 20px;" +   // Réduit la taille du texte pour le poids
                    "}" +
                    "node {" +
                    "   size: 8px;" +         // Réduit la taille des nœuds
                    "   shape: circle;" +
                    "   fill-color: white;" +
                    "   stroke-mode: plain;" +
                    "   stroke-color: black;" +
                    "   text-offset: 10px, 10px;" +  // Décale le texte de
                    "   text-size: 16px;" +   // Augmente la taille du texte pour les noms de classes
                    "}";


}
