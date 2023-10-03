package org.example.gui;

import org.example.parser.Parser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileChooser {

    private JFrame frame;
    private JTextField textFieldFilePath;
    private JTextArea textAreaResults;
    private JButton btnAnalyze,btnBrowse;
    private JLabel folderLabel;

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
        frame.setBounds(100, 100, 837, 510);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBounds(0, 0, 355, 471);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        // Ajouter une image de dossier
        ImageIcon folderIcon = loadAndResizeImage("images/dossier.png", 100, 100); // Charger et redimensionner l'image
        folderLabel = new JLabel(folderIcon);
        folderLabel.setBounds(125, 50, 100, 100);
        folderLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrez horizontalement
        folderLabel.setVerticalAlignment(SwingConstants.CENTER); // Centrez verticalement
        panel.add(folderLabel);

        // Ajouter un champ de texte pour afficher le chemin du fichier sélectionné
        textFieldFilePath = new JTextField();
        textFieldFilePath.setBounds(30, 200, 295, 30);
        panel.add(textFieldFilePath);

        // Ajouter un bouton "Analyser"
        btnBrowse = new JButton("Parcourir");
        btnBrowse.setBounds(125, 250, 100, 30);
        panel.add(btnBrowse);

        // Ajouter un bouton "Analyser"
        btnAnalyze = new JButton("Analyser");
        btnAnalyze.setBounds(125, 290, 100, 30);
        panel.add(btnAnalyze);
        btnAnalyze.setEnabled(false); // Désactiver le bouton jusqu'à ce qu'un dossier soit sélectionné

        // Action pour le bouton "Parcourir"
        btnBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
                    textFieldFilePath.setText(selectedFile);
                    btnAnalyze.setEnabled(true); // Activer le bouton "Analyser" après la sélection
                } else {
                    System.out.println("Aucun fichier sélectionné");
                }
            }
        });

        // Action pour le bouton "Analyser"
        btnAnalyze.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFilePath = textFieldFilePath.getText();
                if (!selectedFilePath.isEmpty()) {
                    File selectedFile = new File(selectedFilePath);
                    if (selectedFile.exists() && selectedFile.isDirectory()) {
                        String input = JOptionPane.showInputDialog("Saisissez le nombre de méthodes voulue :");
                        try {
                            int x = Integer.parseInt(input);
                            Parser parser = new Parser(selectedFile.getAbsolutePath(),x);
                            String res = parser.ParseFolder();
                            appendResultsToTextArea(res);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Veuillez saisir un nombre valide pour X.", "Erreur", JOptionPane.ERROR_MESSAGE);
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Le chemin spécifié n'est pas un dossier valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un dossier à analyser.", "Avertissement", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Ajouter un JLabel pour le titre "Statistiques"
        JLabel titleLabel = new JLabel("Statistiques");
        titleLabel.setFont(new Font("Monotype Corsiva", Font.BOLD, 18));
        titleLabel.setBounds(410, 10, 350, 30);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(titleLabel);

        // Ajouter une JTextArea pour afficher les résultats
        textAreaResults = new JTextArea();
        textAreaResults.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textAreaResults);
        scrollPane.setBounds(410, 50, 350, 400);
        frame.getContentPane().add(scrollPane);

        frame.setVisible(true);
    }

    public void appendResultsToTextArea(String results) {
        textAreaResults.setText(results);
    }
    // Fonction pour charger et redimensionner une image
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
