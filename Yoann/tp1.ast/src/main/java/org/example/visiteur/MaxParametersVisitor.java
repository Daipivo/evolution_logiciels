package org.example.visiteur;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MaxParametersVisitor extends ASTVisitor {
    private int maxParameters = 0;

    @Override
    public boolean visit(MethodDeclaration node) {
        int parameterCount = node.parameters().size();
        if (parameterCount > maxParameters) {
            maxParameters = parameterCount;
        }
        return super.visit(node);
    }

    public int getMaxParameters() {
        return maxParameters;
    }
}
