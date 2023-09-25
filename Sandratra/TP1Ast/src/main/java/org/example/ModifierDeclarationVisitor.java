package org.example;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

import java.util.ArrayList;
import java.util.List;

public class ModifierDeclarationVisitor extends ASTVisitor {
    List<Modifier> methods = new ArrayList<Modifier>();

    public boolean visit(Modifier node) {
        methods.add(node);
        return super.visit(node);
    }

    public List<Modifier> getMethods() {
        return methods;
    }
}
