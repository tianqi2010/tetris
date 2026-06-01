package Tetris;
import javax.swing.*;
import java.awt.*;

public class Tetris extends JFrame {
    private Board board;
    private GameOver gameOver;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public Tetris(){
        setTitle("Tetris");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        board = new Board(this);
        gameOver = new GameOver(this);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(board, "Tetris");
        mainPanel.add(gameOver, "Game Over");   
        add(mainPanel);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center window
        setVisible(true);
        setResizable(true);

        cardLayout.show(mainPanel, "Tetris");
        board.requestFocus();
    }

    public void showGameOver(){
        cardLayout.show(mainPanel, "Game Over");
        gameOver.requestFocus();
    }
    
    public void showTetris(){
        cardLayout.show(mainPanel, "Tetris");
        board.requestFocus();
        board.restart();
    }

     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Tetris().setVisible(true);
        }); 
    }
}