package org.example.visiteur;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodLineCountVisitor extends ASTVisitor {
    private int methodLineCounts = 0;

    @Override
    public boolean visit(MethodDeclaration node) {
        int startLine = ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition());
        int endLine = ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition() + node.getLength());
        methodLineCounts = endLine - startLine + 1;
        System.out.println(node);
        System.out.println(methodLineCounts);
        return true;
    }

    public int getMethodLineCounts() {
        return methodLineCounts;
    }
}
