    package cli;

    import couplage.Instance;
    import facade.Facade;

    import java.util.Scanner;

    public class MainCLI {

        private static Facade facade = new Facade();
        private static Scanner scanner = new Scanner(System.in);

        private static boolean isInstance;
        private static boolean continuer;

        public static void main(String[] args) {

            continuer = true;
            isInstance = false;

            while(continuer) {
                System.out.println("Choisissez une option :");
                System.out.println("1 - Entrez le path du projet");
                System.out.println("2 - Choisir une instance");

                int choix = scanner.nextInt();

                switch (choix) {
                    case 1:
                        handleProjectPathInput(scanner);
                        break;
                    case 2:
                        handleInstanceSelection(scanner);
                        break;
                    default:
                        System.out.println("Option non valide. Veuillez choisir 1 ou 2.");
                }
            }
        }

        private static void handleProjectPathInput(Scanner scanner) {
            System.out.println("Entrez le chemin du projet (ex : /home/name/Bureau/projet) : ");
            String path = scanner.next();
            displayOptions(path);
        }
        private static void handleInstanceSelection(Scanner scanner) {
            System.out.println("Sélectionnez une instance : ");
            Instance[] instances = Instance.values();
            for (int i = 0; i < instances.length; i++) {
                System.out.println((i + 1) + " - " + instances[i].name());
            }
            int choixInstance = scanner.nextInt();
            if (choixInstance < 1 || choixInstance > instances.length) {
                System.out.println("Option non valide. Veuillez choisir une option valide.");
            } else {
                Instance selectedInstance = instances[choixInstance - 1];
                isInstance = true;
                displayOptions(selectedInstance);
            }
        }

        public static void displayOptions(Object object) {
            while (true) {

                System.out.println("Choisissez une action :");
                System.out.println("0 - Pour arrêter");
                System.out.println("1 - Affichage du graphe de couplage");
                System.out.println("2 - Affichage du clustering hiérarchique");
                System.out.println("3 - Affichage du module identifier");
                int choix = scanner.nextInt();

                switch (choix) {
                    case 0:
                        continuer = false;
                        return;
                    case 1:
                        if (isInstance) {
                            facade.createAndDisplayCallGraph((Instance) object);
                        } else {
                            facade.createAndDisplayCallGraph((String) object);
                        }
                        break;
                    case 2:
                        if (isInstance) {
                            facade.createAndDisplayClusteringHierarchique((Instance) object);
                        } else {
                            facade.createAndDisplayClusteringHierarchique((String) object);
                        }
                        break;
                    case 3:
                        System.out.println("Choisissez un CP (x,xx) : ");
                        float cp = scanner.nextFloat();
                        if (isInstance) {
                            facade.createAndDisplayModuleIdentifier((Instance) object, cp);
                        } else {
                            facade.createAndDisplayModuleIdentifier((String) object, cp);
                        }
                        break;
                    default:
                        System.out.println("Option non valide. Veuillez choisir entre 1, 2 ou 3.");
                }
            }
        }
    }
