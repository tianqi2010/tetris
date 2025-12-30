package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel {
    private final int columns = 10;
    private final int visibleRows = 20;
    private final int invisibleRows = 3; // invis rows on the top of the board; implement clutch?
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
                buttonInput(e);
                repaint();
            }
        });
    }
    
    //keyboard inputs
    private void buttonInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C && !controller.isHoldingTetromino()) {

            int currentPiece = controller.getCurrentTetrominoShape();
            int heldPiece = controller.getHeldTetromino();
            controller.setHeldTetromino(currentPiece);
            
            if (heldPiece != -1){
                controller.spawnNewTetromino(heldPiece);
            } else {
                controller.spawnNewTetromino(bag.getNext());
            }

            controller.setHoldingTetromino(true); // can be turned false in lockTetromino()
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (controller.canMove(controller.getPieceX() - 1, controller.getPieceY(), controller.getCurrentPieceShape())) {
                controller.setPieceX(controller.getPieceX() - 1);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (controller.canMove(controller.getPieceX() + 1, controller.getPieceY(), controller.getCurrentPieceShape())) {
                controller.setPieceX(controller.getPieceX() + 1);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (controller.canMove(controller.getPieceX(), controller.getPieceY() + 1, controller.getCurrentPieceShape())) {
                controller.setPieceY(controller.getPieceY() + 1);
            } else {
                controller.lockTetromino();
                controller.spawnNewTetromino(bag.getNext());
                controller.clearLines();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            controller.rotateTetromino((controller.getCurrentRotation()+1)%4); // %4 so when it is 3 and adds 1 to make 4, it loops back to 0
        }
        if (e.isControlDown()){
            controller.rotateTetromino((controller.getCurrentRotation()+3)%4);
        }
        if (e.getKeyCode() == KeyEvent.VK_SHIFT){
            controller.rotateTetromino((controller.getCurrentRotation()+2)%4);
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            controller.hardDrop();
            controller.lockTetromino();
            controller.spawnNewTetromino(bag.getNext());
            controller.clearLines();
        }
    }
    
    @Override 
    protected void paintComponent(Graphics g) {
        //clear board
        super.paintComponent(g);
        
        renderer.calculateBoardPosition(getWidth(), getHeight());
        renderer.generateBoard(g);
        renderer.redrawGrid(g, controller.getGrid());
        renderer.drawCurrentTetromino(g, controller.getPieceX(), controller.getPieceY(), 
                                     controller.getCurrentTetrominoShape(), 
                                     controller.getCurrentPieceShape());
    }
}