package org.example.visiteur;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.*;

public class MethodDeclarationVisitor extends ASTVisitor {
    List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
    private int methodCount = 0;
    private int maxParameters = 0;
    private  int nbrLigneMethodes=0;

    private Map<TypeDeclaration, List<MethodDeclaration>> map = new HashMap<>();



    public boolean visit(MethodDeclaration node) {
        methodCount++;
        methods.add(node);

        if (node != null && node.getBody() != null) {
            System.out.println(node);
            //System.out.println(node.getBody().statements().iterator());
            nbrLigneMethodes+=node.getBody().toString().split("\n").length;

        } else {
            System.out.println("Une des méthodes renvoie null.");
        }

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

    public int getNbrLigneMethodes(){
        return nbrLigneMethodes;
    }


}
