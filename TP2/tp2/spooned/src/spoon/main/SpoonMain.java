package spoon.main;
import graph.Pair;
import java.util.List;
import java.util.Map;
import spoon.couplage.SpoonCouplage;
import spoon.parser.SpoonParser;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtElement;
public class SpoonMain {
    public static void main(String[] args) {
        // Remplacez le chemin du projet par le chemin réel de votre projet
        String projectPath = "/home/e20220012486/M2/Projet/evolution_logiciels/TP2/tp2";
        // Créez une instance de votre SpoonParser en passant le chemin du projet
        SpoonParser spoonParser = new SpoonParser(projectPath);
        // Configurez le parser en spécifiant les répertoires de sortie et les options
        spoonParser.configure();
        // Exécutez le parser pour générer l'AST
        spoonParser.run();
        // L'AST est maintenant généré et prêt à être utilisé pour l'analyse ou la transformation.
        CtModel model = spoonParser.getParser().getModel();
        // Recupperer toute les classes de l'application
        SpoonCouplage cpl = new SpoonCouplage(model);
        List<String> classes = cpl.getStringClasses();
        // Calculez le métrique de couplage entre toutes les classes de l'application
        Map<Pair<String, String>, Double> couplingResults = cpl.calculateCouplingMetricsForAllClasses();
        // Affichez les résultats
        for (Map.Entry<Pair<String, String>, Double> entry : couplingResults.entrySet()) {
            Pair<String, String> classPair = entry.getKey();
            double couplingMetric = entry.getValue();
            String classA = classPair.getFirst();
            String classB = classPair.getSecond();
            System.out.println((((("Couplage entre " + classA) + " et ") + classB) + ": ") + couplingMetric);
        }
    }

    // Fonction récursive pour afficher l'AST
    public static void displayAST(CtElement element, int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");// Indentation pour montrer la profondeur dans l'arbre

        }
        System.out.println((element.getClass().getSimpleName() + " ") + element.getShortRepresentation());
        // Parcourez les sous-éléments de cet élément
        for (CtElement subElement : element.getElements(null)) {
            displayAST(subElement, depth + 1);
        }
    }
}