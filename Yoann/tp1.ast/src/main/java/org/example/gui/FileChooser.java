package org.example.gui;

import org.example.parser.Parser;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;

public class FileChooser {

    private JFrame frame;
    private JTextField textFieldFilePath;
    private JTextArea textAreaResults;

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
        ImageIcon folderIcon = new ImageIcon("folder.png"); // Assurez-vous d'ajouter une image de dossier dans votre projet
        JLabel folderLabel = new JLabel(folderIcon);
        folderLabel.setBounds(125, 50, 100, 100);
        panel.add(folderLabel);

        // Ajouter un champ de texte pour afficher le chemin du fichier sélectionné
        textFieldFilePath = new JTextField();
        textFieldFilePath.setBounds(30, 200, 295, 30);
        panel.add(textFieldFilePath);

        // Ajouter un bouton "Parcourir" pour ouvrir la boîte de dialogue de sélection de fichier
        JButton btnBrowse = new JButton("Analyser");
        btnBrowse.setBounds(125, 250, 100, 30);
        panel.add(btnBrowse);
        btnBrowse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    String selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
                    textFieldFilePath.setText(selectedFile);

                    // Renommer le bouton "Analyser" en "Parcourir" après la sélection
                    btnBrowse.setText("Parcourir");
                    //btnBrowse.setEnabled(false); // Désactivez le bouton pour éviter une nouvelle sélection
                    Parser p=new Parser(selectedFile);
                    String res=p.ParseFolder();
                    appendResultsToTextArea(res);


                } else {
                    System.out.println("Aucun fichier sélectionné");
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
        textAreaResults.setEditable(false); // Empêche l'édition
        JScrollPane scrollPane = new JScrollPane(textAreaResults);
        scrollPane.setBounds(410, 50, 350, 400);
        frame.getContentPane().add(scrollPane);


        frame.setVisible(true);
    }
    public void appendResultsToTextArea(String results) {
        textAreaResults.append(results + "\n"); // Ajoutez les résultats avec un saut de ligne
    }
}
