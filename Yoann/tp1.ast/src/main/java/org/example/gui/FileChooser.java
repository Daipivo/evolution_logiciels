package org.example.gui;

import java.awt.*;

import javax.swing.*;

import org.example.parser.Parser;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class FileChooser {

    private JFrame frame;
    private String nameFile;
    JTextField textField = new JTextField();


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

    /**
     * Create the application.
     */
    public FileChooser() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Calculer la position pour centrer la fenêtre sur l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        int x = (screenSize.width - windowWidth) / 2;
        int y = (screenSize.height - windowHeight) / 2;
        frame.setLocation(x, y);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setBounds(0, 0, 640, 720);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        textField.setEditable(false); // Empêche la modification manuelle du texte
        textField.setBounds(50, 131, 180, 20);
        panel.add(textField);


        //Ajouter une action au bouton de selection de dossier
        JButton btnSelectionerUnDossier = new JButton("Choix du dossier ! ");
        btnSelectionerUnDossier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                // Définissez le mode de sélection sur "Dossiers uniquement"
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                // Affichez le sélecteur de fichiers et attendez la sélection de l'utilisateur
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    // L'utilisateur a sélectionné un fichier
                    File selectedFile = fileChooser.getSelectedFile();
                    nameFile = selectedFile.getName();
                    textField.setText(nameFile);
                    System.out.println("Fichier sélectionné : " + selectedFile.getAbsolutePath());
                    Parser p=new Parser(selectedFile.getAbsolutePath());
                    p.ParseFolder();
                } else {
                    System.out.println("Aucun fichier sélectionné");
                }
            }
        });

        btnSelectionerUnDossier.setBounds(50, 101, 180, 20);
        panel.add(btnSelectionerUnDossier);
    }
}
