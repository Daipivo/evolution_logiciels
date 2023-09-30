package org.example.visiteur;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.*;

public class MethodDeclarationVisitor extends ASTVisitor {
    List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
    private int methodCount = 0;
    private int maxParameters = 0;

    private Map<TypeDeclaration, List<MethodDeclaration>> map = new HashMap<>();



    public boolean visit(MethodDeclaration node) {
        methodCount++;
        methods.add(node);
        int parameterCount = node.parameters().size();
        if (parameterCount > maxParameters) {
            maxParameters = parameterCount;
        }
        return super.visit(node);
    }

    public boolean visit(TypeDeclaration type) {
        if(!type.isInterface() && !map.containsKey(type))
            map.put(type, Arrays.asList(type.getMethods()));

        return super.visit(type);
    }

    public int getMethodCount() {
        return methodCount;
    }

    public List<MethodDeclaration> getMethods(TypeDeclaration cls) {
        cls.accept(this);
        return map.get(cls);
    }

    public List<MethodDeclaration> getMethods() {
        return methods;
    }

    public int getMaxParameters() {
        return maxParameters;
    }


}
