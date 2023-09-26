package org.example.visiteur;

import org.eclipse.jdt.core.dom.*;

public class LineCountVisitor extends ASTVisitor {
    private int lineCount = 0;

    @Override
    public boolean visit(TypeDeclaration node) {
        // Obtenir le numéro de ligne de début et de fin du nœud
        int startLine = ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition());
        int endLine = ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition() + node.getLength());

        // Ajouter le nombre de lignes du nœud
        lineCount += endLine - startLine + 1;

        return super.visit(node);
    }

    @Override
    public boolean visit(ImportDeclaration node) {
        // Traitez ici les déclarations d'importation (import statements)
        // Vous pouvez compter ces lignes ou effectuer d'autres opérations

        lineCount++; // Vous pouvez personnaliser cette logique en fonction de vos besoins

        return super.visit(node);
    }

    @Override
    public boolean visit(PackageDeclaration node) {
        // Traitez ici les déclarations d'importation (import statements)
        // Vous pouvez compter ces lignes ou effectuer d'autres opérations

        lineCount++; // Vous pouvez personnaliser cette logique en fonction de vos besoins

        return super.visit(node);
    }

    public int getLineCount() {
        return lineCount;
    }
}
