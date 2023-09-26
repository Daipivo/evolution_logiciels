package org.example.visiteur;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassCountVisitor extends ASTVisitor {
    private int classCount = 0;

    @Override
    public boolean visit(TypeDeclaration node) {
        classCount++;
        return super.visit(node);
    }

    public int getClassCount() {
        return classCount;
    }
}
