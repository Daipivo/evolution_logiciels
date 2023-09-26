package org.example;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.example.parser.Parser;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class FileChooser {

    private JFrame frame;

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
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setBounds(0, 0, 440, 270);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        //Ajouter une action au bouton de selection de dossier
        JButton btnSelectionerUnDossier = new JButton("Selectioner un dossier ");
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
                    System.out.println("Fichier sélectionné : " + selectedFile.getAbsolutePath());
                    Parser p=new Parser(""+selectedFile.getAbsolutePath());
                    p.ParseFolder();
                } else {
                    System.out.println("Aucun fichier sélectionné");
                }
            }
        });
        btnSelectionerUnDossier.setBounds(92, 101, 267, 25);
        panel.add(btnSelectionerUnDossier);
    }
}
