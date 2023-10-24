package visiteur;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
public class AttributDeclarationVisitor extends ASTVisitor {
    private Map<String, Integer> attributeCounts = new HashMap<>();

    private String currentClass = null;

    /* Visite un nœud de déclaration de type (TypeDeclaration)
    @param node Le nœud de déclaration de type à visiter.
    @return true si la visite est réussie, sinon false.
     */
    @Override
    public boolean visit(TypeDeclaration node) {
        // Nouvelle classe trouvée
        currentClass = node.getName().getIdentifier();
        return super.visit(node);
    }

    /* Visite un nœud de déclaration d'attribut (FieldDeclaration)
    @param node Le nœud de déclaration d'attribut à visiter.
    @return true si la visite est réussie, sinon false.
     */
    @Override
    public boolean visit(FieldDeclaration node) {
        // Attribut trouvé dans la classe en cours
        if (currentClass != null) {
            int count = attributeCounts.getOrDefault(currentClass, 0);
            count++;
            attributeCounts.put(currentClass, count);
        }
        return super.visit(node);
    }

    public Map<String, Integer> getAttributeCounts() {
        return attributeCounts;
    }

    public int getClassCount() {
        return attributeCounts.size();
    }
}