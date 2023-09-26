package org.example;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

public class PackageDeclarationVisitor extends ASTVisitor {
    private int packageCount = 0;

    @Override
    public boolean visit(PackageDeclaration node) {
        // Compter chaque déclaration de package
        packageCount++;
        return super.visit(node);
    }

    public int getPackageCount() {
        return packageCount;
    }
}
