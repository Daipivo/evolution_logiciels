package org.example.visiteur;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.HashMap;
import java.util.Map;

public class AttributDeclarationVisitor extends ASTVisitor {
    private Map<String, Integer> attributeCounts = new HashMap<>();
    private String currentClass = null;

    @Override
    public boolean visit(TypeDeclaration node) {
        // Nouvelle classe trouvée
        currentClass = node.getName().getIdentifier();
        return super.visit(node);
    }

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
