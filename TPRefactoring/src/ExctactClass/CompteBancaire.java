package ExctactClass;
// Refactoring : Nous allons appliquer le refactoring "Extract Class" en séparant les responsabilités
// de gestion des détails du compte client et des opérations financières en deux classes distinctes.

// Classe d'origine représentant un compte bancaire avec des détails et des opérations financières
class CompteBancaire {
    private String numeroCompte;
    private String titulaire;
    private double solde;

    // Constructeur
    public CompteBancaire(String numeroCompte, String titulaire, double soldeInitial) {
        this.numeroCompte = numeroCompte;
        this.titulaire = titulaire;
        this.solde = soldeInitial;
    }

    // Méthode pour effectuer un dépôt
    public void deposer(double montant) {
        solde += montant;
        System.out.println("Dépôt de " + montant + " effectué. Nouveau solde: " + solde);
    }

    // Méthode pour effectuer un retrait
    public void retirer(double montant) {
        if (montant <= solde) {
            solde -= montant;
            System.out.println("Retrait de " + montant + " effectué. Nouveau solde: " + solde);
        } else {
            System.out.println("Fond insuffisant pour effectuer le retrait.");
        }
    }

    // Méthode pour afficher les détails du compte
    public void afficherDetails() {
        System.out.println("Numéro de compte: " + numeroCompte);
        System.out.println("Titulaire: " + titulaire);
        System.out.println("Solde: " + solde);
    }

    // Fonction de test main
    public static void main(String[] args) {
        // Création d'un compte et exécution de quelques opérations
        CompteBancaire compte = new CompteBancaire("123456", "John Doe", 1000);
        compte.afficherDetails();
        compte.deposer(500);
        compte.retirer(200);
        compte.afficherDetails();
    }
}
