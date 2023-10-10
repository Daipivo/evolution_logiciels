package visiteur;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.PackageDeclaration;

import java.util.ArrayList;

public class PackageDeclarationVisitor extends ASTVisitor {
    private int packageCount = 0;
    private ArrayList<String> packages = new ArrayList<>();

    /*
   Visite un nœud de déclaration de package (PackageDeclaration)
   @param node Le nœud de déclaration de package à visiter.
   @return true si la visite est réussie, sinon false.
    */
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
