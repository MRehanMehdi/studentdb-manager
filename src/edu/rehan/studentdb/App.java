package edu.rehan.studentdb;

import edu.rehan.studentdb.ui.MainFrame;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setTitle("StudentDB Manager");
            mainFrame.setSize(900, 600);
            mainFrame.setLocationRelativeTo(null); // Center screen
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setVisible(true);
        });
    }
}
