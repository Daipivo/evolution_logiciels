package org.example.visiteur;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;

import java.util.ArrayList;
import java.util.List;

public class MethodInvocationVisitor extends ASTVisitor {


    private List<MethodInvocation> methods = new ArrayList<>();
    private List<SuperMethodInvocation> superMethods = new ArrayList<>();


    public boolean visit(MethodInvocation node){
        methods.add(node);
        return super.visit(node);
    }

    public boolean visit(SuperMethodInvocation node){
        superMethods.add(node);
        return super.visit(node);
    }

    public List<MethodInvocation> getMethodsInvocation(MethodDeclaration methode) {
        methode.accept(this);
        return methods;
    }

    public List<SuperMethodInvocation> getSuperMethods() {
        return superMethods;
    }

}
