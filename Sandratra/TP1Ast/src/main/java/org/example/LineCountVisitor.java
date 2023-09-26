package org.example;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class LineCountVisitor extends ASTVisitor {
    private int lineCount = 0;

    @Override
    public boolean visit(MethodDeclaration node) {
        // Obtenir le numéro de ligne de début et de fin du nœud
        int startLine = ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition());
        int endLine = ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition() + node.getLength());

        // Ajouter le nombre de lignes du nœud
        lineCount += endLine - startLine + 1;

        return super.visit(node);
    }

    public int getLineCount() {
        return lineCount;
    }
}
