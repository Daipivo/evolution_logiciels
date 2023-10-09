package org.example.gui;

import org.example.graph.CallGraph;
import org.example.graph.DisplayGraph;
import org.example.parser.Parser;
import org.example.utils.ApplicationStatistics;
import org.graphstream.ui.view.Viewer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.*;

public class FileChooser {

    private JFrame frame;
    private JTextField textFieldFilePath;
    private JLabel folderLabel;
    JButton btnAnalyse = new JButton("Analyser");
    HashMap<String,JLabel> Reslabels=new HashMap<>();
    HashMap<String,JComboBox> ResCombo=new HashMap<>();
    String selectedFile;
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FileChooser window = new FileChooser();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public FileChooser() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(31, 81, 113));
        frame.setBounds(100, 100,1200, 630);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(32, 105, 323, 336);
        frame.getContentPane().add(panel);
        panel.setLayout(null);



        // Ajouter une image de dossier
        ImageIcon folderIcon = loadAndResizeImage("images/dossier.png", 100, 100); // Charger et redimensionner l'image
        folderLabel = new JLabel(folderIcon);
        folderLabel.setBounds(125, 40, 80, 80);
        folderLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrez horizontalement
        folderLabel.setVerticalAlignment(SwingConstants.CENTER); // Centrez verticalement
        panel.add(folderLabel);

        // Ajouter un champ de texte pour afficher le chemin du fichier sélectionné
        textFieldFilePath = new JTextField();
        textFieldFilePath.setBounds(18, 153, 295, 30);
        panel.add(textFieldFilePath);

        // Ajouter un bouton "Parcourir" pour ouvrir la boîte de dialogue de sélection de fichier
        JButton btnBrowse = new JButton("Parcourir");
        btnBrowse.setBounds(111, 194, 100, 30);
        panel.add(btnBrowse);

        JButton btnShowResults = new JButton("Graphe");
        btnShowResults.setBounds(111, 276, 100, 30);
        btnShowResults.setEnabled(false); // Définir l'état initial sur désactivé
        panel.add(btnShowResults);


        btnAnalyse.setBounds(111, 235, 100, 30);
        btnAnalyse.setEnabled(false);
        panel.add(btnAnalyse);
        btnBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
                    textFieldFilePath.setText(selectedFile);
                    btnAnalyse.setEnabled(true); // Activer le bouton "Analyser" après la sélection
                    btnShowResults.setEnabled(true);
                } else {
                    System.out.println("Aucun fichier sélectionné");
                }
            }
        });

        btnShowResults.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CallGraph graph = new CallGraph(selectedFile);
                graph.start();
                DisplayGraph displayGraph = new DisplayGraph(graph);
                Viewer viewer = displayGraph.displayGraph();
                viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
            }
        });


        btnAnalyse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFilePath = textFieldFilePath.getText();
                if (!selectedFilePath.isEmpty()) {
                    File selectedFile = new File(selectedFilePath);
                   if(checkValidFolder(selectedFile)){

                       if (selectedFile.exists() && selectedFile.isDirectory()) {
                           String input = JOptionPane.showInputDialog("Saisissez le nombre de méthodes souhaité pour la question 11 :");
                           try {
                               int x = Integer.parseInt(input);
                               Parser parser = new Parser(selectedFile.getAbsolutePath(),x);
                               ApplicationStatistics res;
                               res = parser.ParseFolder();
                               afficherStatistiques(res,x);

                           } catch (NumberFormatException ex) {
                               JOptionPane.showMessageDialog(null, "Veuillez saisir un nombre valide pour X !", "Erreur", JOptionPane.ERROR_MESSAGE);
                           }

                       }

                   }
                   else{
                       JOptionPane.showMessageDialog(null, "Veuillez sélectionner un dossier valide !", "Avertissement", JOptionPane.WARNING_MESSAGE);
                   }

                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un dossier à analyser !", "Avertissement", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        // Ajouter un JLabel pour le titre "Statistiques"
        JLabel titleLabel = new JLabel("RESULTAT");
        titleLabel.setBackground(new Color(31, 88, 113));
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setFont(new Font("Arial Narrow", Font.BOLD, 27));
        titleLabel.setBounds(398, 9, 350, 30);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        frame.getContentPane().add(titleLabel);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(31, 88, 113));
        panel_1.setBounds(398, 50, 350, 511);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(null);

        addStatisticLabel(panel_1, "Classes de l’application : ", "lblNombreClasses");
        addStatisticLabel(panel_1, "Lignes de code de l’application : ", "lblNombreLignesCode");
        addStatisticLabel(panel_1, "Méthodes de l’application : ", "lblNombreTotalMethodes");
        addStatisticLabel(panel_1, "Packages de l’application : ", "lblNombreTotalPackages");
        addStatisticLabel(panel_1, "Méthodes par classe : ", "lblNombreMoyenMethodesParClasse");
        addStatisticLabel(panel_1, "Lignes de code par méthode : ", "lblNombreMoyenLignesCodeParMethode");
        addStatisticLabel(panel_1, "Moyen d’attributs par classe : ", "lblNombreMoyenAttributsParClasse");
        addStatisticLabel(panel_1, "Le nombre maximal de paramètres de l’application : ", "lblNombreMaxParametres");

        JLabel lblNombre_1 = new JLabel("NOMBRE");
        lblNombre_1.setForeground(Color.WHITE);
        lblNombre_1.setFont(new Font("Arial Narrow", Font.BOLD, 20));
        lblNombre_1.setBounds(20, 11, 106, 26);
        panel_1.add(lblNombre_1);



        JPanel panel_1_1 = new JPanel();
        panel_1_1.setBackground(new Color(31, 88, 113));
        panel_1_1.setBounds(786, 50, 365, 511);
        frame.getContentPane().add(panel_1_1);
        panel_1_1.setLayout(null);

        JLabel lblNombre = new JLabel("METHODE\\CLASS");
        lblNombre.setBounds(26, 11, 202, 26);
        panel_1_1.add(lblNombre);
        lblNombre.setFont(new Font("Arial Narrow", Font.BOLD, 20));
        lblNombre.setForeground(new Color(255, 255, 255));

        addStatisticComboBox(panel_1_1, "Les 10% des classes qui possèdent le plus grand nombre de méthodes : ", "cmbTop10ClassesMethods");
        addStatisticComboBox(panel_1_1, "Les 10% des classes qui possèdent le plus grand nombre d’attributs : ", "cmbTop10ClassesAttributes");
        addStatisticComboBox(panel_1_1, "Les classes qui font partie en même temps des deux catégories précédentes : ", "cmbClassesInBothCategories");
        addStatisticComboBox(panel_1_1, "Les classes qui possèdent plus de X méthodes (la valeur de X est donnée) : ", "cmbClassesWithMoreThanXMethods");
        addStatisticComboBox(panel_1_1, "Les 10% des méthodes qui possèdent le plus grand nombre de lignes de code (par classe) : ", "cmbTop10MethodsLinesOfCode");


        frame.setVisible(true);
    }

    private static boolean checkValidFolder(File file) {
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();

            if(!file.isDirectory()){
                return false;
            }

            if (subFiles != null) {
                for (File subFile : subFiles) {
                    if (subFile.isDirectory() && subFile.getName().equals("src")) {
                        return true; // Le sous-dossier "src" a été trouvé
                    }
                }
            }
        }
        return false; // Aucun sous-dossier nommé "src" trouvé
    }

    private void addStatisticLabel(JPanel panel, String labelText, String variableName) {
        JLabel label = new JLabel(labelText); // Ajoutez le texte au label
        label.setFont(new Font("Arial Narrow", Font.PLAIN, 16));
        label.setForeground(new Color(255, 255, 255));
        label.setBounds(20, panel.getComponentCount() * 30 + 29, 350, 26); // Ajustez les coordonnées et la largeur du label
        panel.add(label);

        JLabel valueLabel = new JLabel(); // Pas de valeur initiale
        valueLabel.setFont(new Font("Arial Narrow", Font.PLAIN, 16));
        valueLabel.setForeground(new Color(255, 255, 255));
        valueLabel.setBounds(20, panel.getComponentCount() * 30 + 29, 50, 26);
        valueLabel.setName(variableName); // Définir le nom de la variable

        Reslabels.put(variableName, valueLabel);

        panel.add(valueLabel);
    }

    // Ajoutez cette fonction dans votre classe FileChooser
    private void afficherStatistiques(ApplicationStatistics stats,int x) {
        // Affichez les résultats sur les JLabels appropriés
        JLabel lblNombreClasses = getLabelByName("lblNombreClasses");
        lblNombreClasses.setText("" + stats.getNumberOfClasses());

        JLabel lblNombreLignesCode = getLabelByName("lblNombreLignesCode");
        lblNombreLignesCode.setText(" " + stats.getNumberOfLinesOfCode());

        JLabel lblNombreMethodes = getLabelByName("lblNombreTotalMethodes");
        lblNombreMethodes.setText("" + stats.getTotalNumberOfMethods());

        JLabel lblNombreTotalPackages = getLabelByName("lblNombreTotalPackages");
        lblNombreTotalPackages.setText(""+stats.getTotalNumberOfPackages());

        JLabel lblNombreMoyenMethodesParClasse = getLabelByName("lblNombreMoyenMethodesParClasse");
        lblNombreMoyenMethodesParClasse.setText("" + stats.getAverageMethodsPerClass());

        JLabel lblNombreMoyenLignesCodeParMethode = getLabelByName("lblNombreMoyenLignesCodeParMethode");
        lblNombreMoyenLignesCodeParMethode.setText("" + stats.getAverageLinesOfCodePerMethod());

        JLabel lblNombreMoyenAttributsParClasse = getLabelByName("lblNombreMoyenAttributsParClasse");
        lblNombreMoyenAttributsParClasse.setText("" + stats.getAverageAttributesPerClass());

        // Les 10% des classes qui possèdent le plus grand nombre de méthodes.
        JComboBox<String> cmbTop10ClassesMethods = getComboBoxByName("cmbTop10ClassesMethods");
        String[] top10ClassesMethodsOptions = stats.getClassesWithMostMethods().toArray(new String[0]);
        cmbTop10ClassesMethods.setModel(new DefaultComboBoxModel<>(top10ClassesMethodsOptions));

        // Les 10% des classes qui possèdent le plus grand nombre d’attributs.
        JComboBox<String> cmbTop10ClassesAttributes = getComboBoxByName("cmbTop10ClassesAttributes");
        String[] top10ClassesAttributesOptions = stats.getClassesWithMostAttributes().toArray(new String[0]);
        cmbTop10ClassesAttributes.setModel(new DefaultComboBoxModel<>(top10ClassesAttributesOptions));

        // Les classes qui font partie en même temps des deux catégories précédentes.
        JComboBox<String> cmbClassesInBothCategories = getComboBoxByName("cmbClassesInBothCategories");
        String[] classesInBothCategoriesOptions = stats.getClassesWithBothAttributesAndMethods().toArray(new String[0]);
        cmbClassesInBothCategories.setModel(new DefaultComboBoxModel<>(classesInBothCategoriesOptions));

        // Les classes qui possèdent plus de X méthodes (la valeur de X est donnée).
        JComboBox<String> cmbClassesWithMoreThanXMethods = getComboBoxByName("cmbClassesWithMoreThanXMethods");
        String[] classesWithMoreThanXMethodsOptions = stats.getClassesWithMoreThanXMethods().toArray(new String[0]);
        cmbClassesWithMoreThanXMethods.setModel(new DefaultComboBoxModel<>(classesWithMoreThanXMethodsOptions));

        // Les 10% des méthodes qui possèdent le plus grand nombre de lignes de code (par classe).
        JComboBox<String> cmbTop10MethodsLinesOfCode = getComboBoxByName("cmbTop10MethodsLinesOfCode");
        String[] top10MethodsLinesOfCodeOptions = stats.getMethodsWithMostLinesOfCode().toArray(new String[0]);
        cmbTop10MethodsLinesOfCode.setModel(new DefaultComboBoxModel<>(top10MethodsLinesOfCodeOptions));
//        // Les 10% des classes qui possèdent le plus grand nombre de méthodes.
//        JLabel lblTop10ClassesMethods = getLabelByName("lblTop10ClassesMethods");
//        lblTop10ClassesMethods.setText("" + String.join(", ", stats.getClassesWithMostMethods()));
//
//        // Les 10% des classes qui possèdent le plus grand nombre d’attributs.
//        JLabel lblTop10ClassesAttributes = getLabelByName("lblTop10ClassesAttributes");
//        lblTop10ClassesAttributes.setText("" + String.join(", ", stats.getClassesWithMostAttributes()));
//
//        // Les classes qui font partie en même temps des deux catégories précédentes.
//        JLabel lblClassesInBothCategories = getLabelByName("lblClassesInBothCategories");
//        lblClassesInBothCategories.setText("" + String.join(", ", stats.getClassesWithBothAttributesAndMethods()));
//
//        // Les classes qui possèdent plus de X méthodes (la valeur de X est donnée).
//        JLabel lblClassesWithMoreThanXMethods = getLabelByName("lblClassesWithMoreThanXMethods");
//        lblClassesWithMoreThanXMethods.setText("Les classes qui possèdent plus de " + x + " méthodes : " + String.join(", ", stats.getClassesWithMoreThanXMethods()));

        // Le nombre maximal de paramètres par rapport à toutes les méthodes de l’application.
        JLabel lblNombreMaxParametres = getLabelByName("lblNombreMaxParametres");
        lblNombreMaxParametres.setText("" + stats.getMaxParametersInMethods());
    }

    private JComboBox getComboBoxByName(String name) {
        JComboBox res=ResCombo.get(name);
        if (res!=null) return res;
        return null;
    }

    private void addStatisticComboBox(JPanel panel, String labelText, String variableName) {
        JLabel label = new JLabel("<html>"+labelText+"</html>");
        label.setFont(new Font("Arial Narrow", Font.PLAIN, 16));
        label.setForeground(new Color(255, 255, 255));
        label.setBounds(20, panel.getComponentCount() * 45 + 10, 350, 30); // Augmentez la hauteur à 60
        panel.add(label);

        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setBounds(20, panel.getComponentCount() * 45 + 5, 300, 30); // Augmentez la hauteur à 60
        comboBox.setName(variableName);
        panel.add(comboBox);
        ResCombo.put(variableName, comboBox);
    }

    // Ajoutez cette fonction dans votre classe FileChooser pour obtenir un JLabel par son nom
    private JLabel getLabelByName(String name) {
        JLabel res=Reslabels.get(name);
        if (res!=null) return res;
        return null;
    }



    private ImageIcon loadAndResizeImage(String filename, int width, int height) {
        try {
            BufferedImage img = ImageIO.read(new File(filename));
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
