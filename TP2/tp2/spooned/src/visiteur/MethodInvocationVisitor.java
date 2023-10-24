package visiteur;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
public class MethodInvocationVisitor extends ASTVisitor {
    private List<MethodInvocation> methods = new ArrayList<>();

    private List<SuperMethodInvocation> superMethods = new ArrayList<>();

    /* Visite un nœud d'invocation de méthode (MethodInvocation)
    @param node Le nœud d'invocation de méthode à visiter.
    @return true si la visite est réussie, sinon false.
     */
    public boolean visit(MethodInvocation node) {
        methods.add(node);
        return super.visit(node);
    }

    /* Visite un nœud de super invocation de méthode (SuperMethodInvocation)
    @param node Le nœud de super invocation de méthode à visiter.
    @return true si la visite est réussie, sinon false.
     */
    public boolean visit(SuperMethodInvocation node) {
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