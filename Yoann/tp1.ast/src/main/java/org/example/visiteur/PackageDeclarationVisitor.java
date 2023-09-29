package org.example.visiteur;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;

public class PackageDeclarationVisitor extends ASTVisitor {
    private int packageCount = 0;
    private ArrayList<String> packages = new ArrayList<>();

    @Override
    public boolean visit(PackageDeclaration node) {

        String packageName = String.valueOf(node.getName());

        if(!packages.contains(packageName)){

            packages.add(packageName);
            packageCount++;

        }

        return super.visit(node);
    }

    public int getPackageCount() {
        return packageCount;
    }
}
