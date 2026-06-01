package Tetris;

import javax.swing.*;
import java.awt.*;

public class GameOver extends JPanel{
    
    private Tetris tetris;

    public GameOver(Tetris tetris){
        this.tetris = tetris;
        setBackground(Color.BLACK);
        setLayout(new GridLayout(5, 1, 10, 10));

        JLabel gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 48));
        gameOverLabel.setForeground(Color.RED);
        add(gameOverLabel);
    }
}
