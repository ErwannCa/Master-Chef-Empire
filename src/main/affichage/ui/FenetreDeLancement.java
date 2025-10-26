// Écran d'accueil
package main.affichage.ui;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class FenetreDeLancement extends JFrame {
    private boolean gameStarted = false;

    public FenetreDeLancement() {
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Restaurant Simulator");
        setLayout(new BorderLayout());

        // Création d'un JLayeredPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(500, 250));
        add(layeredPane, BorderLayout.CENTER);

        // ImagePanel personnalisé pour l'arrière-plan
        ImagePanel imagePanel = new ImagePanel("src/main/images/accueil.png");
        imagePanel.setBounds(0, 0, 500, 250);
        layeredPane.add(imagePanel, Integer.valueOf(1));

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(0, 140, 500, 100);
        layeredPane.add(buttonPanel, Integer.valueOf(2));

        // Boutons
        JButton startButton = createStyledButton("Commencer");
        JButton quitButton = createStyledButton("Quitter");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameStarted = true;
                dispose();
            }
        });

        quitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(startButton);
        buttonPanel.add(quitButton);

        setVisible(true);
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    // Classe interne pour dessiner l'image d'arrière-plan
    private class ImagePanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private Image image;

        public ImagePanel(String imagePath) {
            this.image = Toolkit.getDefaultToolkit().getImage(imagePath);
            this.setPreferredSize(new Dimension(500, 250));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // Méthode pour styliser les boutons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(140, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }
}



