package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel {
    private final int columns = 10;
    private final int visibleRows = 20;
    private final int invisibleRows = 3;
    private final int totalRows = visibleRows + invisibleRows;
    private final int blockSize = 55;
    private final int startRow = 0;
    private final int startCol = 3;
    
    private TetrominoBag bag;
    private BoardRenderer renderer;
    private GameController controller;
    
    public Board() {
        renderer = new BoardRenderer(columns, visibleRows, invisibleRows, blockSize);
        controller = new GameController(columns, totalRows, startCol, startRow);
        bag = new TetrominoBag();
        
        controller.setCurrentPiece(bag.getNext(), 0);
        
        setBackground(Color.BLACK);
        setFocusable(true);
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
                repaint();
            }
        });
    }
    
    private void handleKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C) {
            System.out.println("Hold (todo)");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (controller.canMove(controller.getPieceX() - 1, controller.getPieceY())) {
                controller.setPieceX(controller.getPieceX() - 1);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (controller.canMove(controller.getPieceX() + 1, controller.getPieceY())) {
                controller.setPieceX(controller.getPieceX() + 1);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (controller.canMove(controller.getPieceX(), controller.getPieceY() + 1)) {
                controller.setPieceY(controller.getPieceY() + 1);
            } else {
                controller.lockTetromino();
                controller.spawnNewTetromino(bag.getNext());
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            System.out.println("Rotate (todo)");
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            controller.hardDrop();
            controller.lockTetromino();
            controller.spawnNewTetromino(bag.getNext());
        }
    }
    
    @Override 
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        renderer.calculateBoardPosition(getWidth(), getHeight());
        renderer.generateBoard(g);
        renderer.redrawGrid(g, controller.getGrid());
        renderer.drawCurrentTetromino(g, controller.getPieceX(), controller.getPieceY(), 
                                     controller.getCurrentTetrominoShape(), 
                                     controller.getCurrentPieceShape());
    }
}