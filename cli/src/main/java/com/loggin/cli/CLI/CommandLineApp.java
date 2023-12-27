package com.loggin.cli.CLI;

import com.loggin.cli.model.Product;
import com.loggin.cli.model.User;
import com.loggin.cli.service.ApiService;
import com.loggin.cli.service.ProductService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


@SpringBootApplication
public class CommandLineApp {
    private final ApiService apiService;
    private final ProductService productService;
    private final Scanner scanner;

    public CommandLineApp(ApiService apiService, ProductService productService) {
        this.apiService = apiService;
        this.productService = productService;
        this.scanner = new Scanner(System.in);
    }
    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("Menu:");
            System.out.println("1. Se connecter");
            System.out.println("2. Créer un utilisateur");
            System.out.println("3. Quitter");
            System.out.print("Choisissez une option : ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    createUser();
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }
    private void productMenu() {
        boolean running = true;
        while (running) {
            System.out.println("Gestion des produits:");
            System.out.println("1. Afficher tous les produits");
            System.out.println("2. Rechercher un produit par ID");
            System.out.println("3. Ajouter un nouveau produit");
            System.out.println("4. Supprimer un produit");
            System.out.println("5. Mettre à jour un produit");
            System.out.println("6. Déconnexion");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    displayAllProducts();
                    break;
                case 2:
                    fetchProductById();
                    break;
                case 3:
                    addProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    updateProduct();
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private void login() {
        System.out.println("Entrée des informations de connexion.");
        System.out.print("Entrez l'email (tapez 'retour' pour revenir au menu) : ");
        String email = scanner.next();
        if (email.equalsIgnoreCase("retour")) {
            return;
        }

        System.out.print("Entrez le mot de passe : ");
        String password = scanner.next();

        User user = new User();  // Créez une instance de User
        user.setEmail(email);
        user.setPassword(password);

        User foundUser = apiService.findUserByEmailAndPassword(user);
        if (foundUser != null) {
            System.out.println("Connexion réussie pour l'utilisateur : " + foundUser.getName());
            //maintenant on affiche le second menu
            productMenu();
        } else {
            System.out.println("Email ou mot de passe incorrect.");
        }
    }

    private void createUser() {
        scanner.nextLine(); // Nettoyage du buffer d'entrée

        System.out.print("Entrez le nom de l'utilisateur (ou tapez 'retour' pour revenir au menu) : ");
        String name = scanner.nextLine();
        if ("retour".equalsIgnoreCase(name)) {
            return;
        }

        int age;
        System.out.print("Entrez l'âge de l'utilisateur : ");
        try {
            age = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrée invalide pour l'âge. Veuillez entrer un nombre.");
            return;
        }

        String email;
        do {
            System.out.print("Entrez l'email de l'utilisateur (ne doit pas être vide, tapez 'retour' pour annuler) : ");
            email = scanner.nextLine();
            if ("retour".equalsIgnoreCase(email)) {
                return;
            }
        } while (email.isEmpty());

        String password;
        do {
            System.out.print("Entrez le mot de passe de l'utilisateur (ne doit pas être vide, tapez 'retour' pour annuler) : ");
            password = scanner.nextLine();
            if ("retour".equalsIgnoreCase(password)) {
                return;
            }
        } while (password.isEmpty());

        User newUser = new User(name, age, email, password);
        try {
            apiService.createUser(newUser);
            System.out.println("Utilisateur créé avec succès.");
        } catch (ResourceAccessException e) {
            System.err.println("Impossible de se connecter au serveur. Assurez-vous que le serveur est en cours d'exécution.");
        } catch (Exception e) {
            System.err.println("Une erreur est survenue lors de la création de l'utilisateur: " + e.getMessage());
        }
    }


    private void displayAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            if (products != null && !products.isEmpty()) {
                products.forEach(product -> System.out.println(product));
            } else {
                System.out.println("Aucun produit trouvé.");
            }
        } catch (Exception e) {
            System.err.println("Une erreur est survenue lors de la récupération des produits: " + e.getMessage());
        }
    }

    private void fetchProductById() {
        System.out.print("Entrez l'ID du produit : ");
        String productId = scanner.next();
        try {
            Product product = productService.getProductById(productId);
            System.out.println(product);
        } catch (Exception e) {
            System.err.println("Produit non trouvé.");
        }
    }
    private void addProduct() {
        String name;
        do {
            System.out.print("Entrez le nom du produit (obligatoire) : ");
            name = scanner.nextLine();
        } while (name.isEmpty());

        double price = 0;
        boolean validPrice = false;
        while (!validPrice) {
            System.out.print("Entrez le prix du produit (obligatoire) : ");
            try {
                price = scanner.nextDouble();
                validPrice = true;
            } catch (InputMismatchException e) {
                System.out.println("Veuillez entrer un nombre valide pour le prix.");
                scanner.nextLine(); // Nettoyer le buffer d'entrée
            }
        }

        Date expirationDate = null;
        while (expirationDate == null) {
            System.out.print("Entrez la date d'expiration du produit (format YYYY-MM-DD, obligatoire) : ");
            String dateInput = scanner.next();
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                expirationDate = dateFormat.parse(dateInput);
            } catch (ParseException e) {
                System.out.println("Format de date invalide. Veuillez réessayer (YYYY-MM-DD).");
            }
        }

        Product product = new Product(name, price, expirationDate);
        try {
            productService.addProduct(product);
            System.out.println("Produit ajouté avec succès.");
        } catch (Exception e) {
            System.err.println("Une erreur est survenue lors de l'ajout du produit: " + e.getMessage());
        }
    }
    private void deleteProduct() {
        System.out.print("Entrez l'ID du produit à supprimer : ");
        String productId = scanner.next();
        try {
            productService.deleteProduct(productId);
            System.out.println("Produit supprimé avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression du produit.");
        }
    }

    private void updateProduct() {
        System.out.print("Entrez l'ID du produit à mettre à jour : ");
        String productId = scanner.next();
        // Demander les nouvelles informations du produit
        String name;
        do {
            System.out.print("Entrez le nouveau nom du produit (obligatoire) : ");
            name = scanner.next();
        } while (name.isEmpty());

        double price = 0;
        boolean validPrice = false;
        while (!validPrice) {
            System.out.print("Entrez le nouveau prix du produit (obligatoire) : ");
            try {
                price = scanner.nextDouble();
                validPrice = true;
            } catch (InputMismatchException e) {
                System.out.println("Veuillez entrer un nombre valide pour le prix.");
                scanner.next(); // Nettoyer le buffer d'entrée
            }
        }

        Date expirationDate = null;
        while (expirationDate == null) {
            System.out.print("Entrez la nouvelle date d'expiration du produit (format YYYY-MM-DD, obligatoire) : ");
            String dateInput = scanner.next();
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                expirationDate = dateFormat.parse(dateInput);
            } catch (ParseException e) {
                System.out.println("Format de date invalide. Veuillez réessayer (YYYY-MM-DD).");
            }
        }

        Product updatedProduct = new Product(name, price, expirationDate);
        updatedProduct.setId(productId);

        try {
            productService.updateProduct(productId, updatedProduct);
            System.out.println("Produit mis à jour avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du produit : " + e.getMessage());
        }
    }



    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        // Création des services
        ApiService apiService = new ApiService(restTemplate);
        ProductService productService = new ProductService(restTemplate);
        // Initialisation de la classe CommandLineApp avec les services nécessaires
        CommandLineApp app = new CommandLineApp(apiService, productService);
        // Lancement de l'application CLI
        app.run();
    }
}
