package Tetris;

import javax.swing.*;
import java.awt.*;

public class Tetris {

     public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("Tetris");
            Board board = new Board();

            frame.add(board);

            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null); // center window
            frame.setVisible(true);

        }); 
    }
}