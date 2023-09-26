package org.example.visiteur;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationVisitor extends ASTVisitor {
    List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
    private int methodCount = 0;

    public boolean visit(MethodDeclaration node) {
        methodCount++;
        methods.add(node);
        return super.visit(node);
    }

    public int getMethodCount() {
        return methodCount;
    }

    public List<MethodDeclaration> getMethods() {
        return methods;
    }
}
