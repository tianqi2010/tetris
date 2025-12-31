package Tetris;

import java.awt.*;

public class BoardRenderer {
    private int boardX, boardY;
    private int holdX, holdY;
    private int nextX, nextY;
    private final int boardWidth;
    private final int boardHeight;
    private final int blockSize;
    private final int columns;
    private final int visibleRows;
    private final int invisibleRows; 
    private final int totalRows;
    private final int holdWidth;
    private final int holdHeight;
    private final int nextWidth;
    private final int nextHeight;
    
    public BoardRenderer(int columns, int visibleRows, int invisibleRows, int blockSize, int holdWidth, int holdHeight, int nextWidth, int nextHeight) {
        this.columns = columns;
        this.visibleRows = visibleRows;
        this.invisibleRows = invisibleRows;
        this.totalRows = visibleRows + invisibleRows;
        this.blockSize = blockSize;
        this.boardWidth = columns * blockSize;
        this.boardHeight = totalRows * blockSize;
        this.holdWidth = holdWidth;
        this.holdHeight = holdHeight;
        this.nextWidth = nextWidth;
        this.nextHeight = nextHeight;
    }
    
    // x and y poses for top left of the board, respective to the window dimensions.
    public void calculateBoardPosition(int windowWidth, int windowHeight) {
        boardX = (windowWidth - boardWidth) / 2;
        boardY = (windowHeight - boardHeight) / 2;
        holdX = boardX - holdWidth * blockSize;
        holdY = boardY + (holdHeight-1) * blockSize; // idk why -1 works but ill find that later :)
        nextX = boardX + columns * blockSize;
        nextY = boardY + 3*blockSize; // idk why +3 either but prob something to do with invis lines too lazy fix later :)
    }
    
    public void generateBoard(Graphics g) {
        for (int row = 0; row < visibleRows; row++) {
            for (int col = 0; col < columns; col++) {
                int gridRow = row + invisibleRows;
                int x = boardX + col * blockSize;
                int y = boardY + gridRow * blockSize;
                g.setColor(Color.GRAY);
                g.drawRect(x, y, blockSize, blockSize);
            }
        }
    }

    //4 long x 6?
    public void generateHoldBar(Graphics g){
        for (int row = 0; row < holdHeight; row++){
            for (int col = 0; col < holdWidth; col++){
                int x = holdX + col * blockSize;
                int y = holdY + row * blockSize;
                g.setColor(Color.GRAY);
                g.drawRect(x, y, blockSize, blockSize);
            }
        }
    }

    public void drawHeldPiece(Graphics g, int currentTetrominoShape, int[][] currentPieceShape){

        // -1 case in paintcomponent
        for (int row = 0; row < 4; row++){
            for (int col = 0; col < 4; col++){
                if (currentPieceShape[row][col] != 0){
                    int heldGridX = col + 1;
                    int heldGridY = row + 1;
                    int x = holdX + heldGridX * blockSize;
                    int y = holdY + heldGridY * blockSize;
                    g.setColor(Tetromino.TetrominoColors[currentTetrominoShape+1]);
                    g.fillRect(x, y, blockSize, blockSize);
                }
            }
        }
    }

    //16 long x 6?
    public void generateNextBar(Graphics g){
        for (int row = 0; row < nextHeight; row++){
            for (int col = 0; col < nextWidth; col++){
                int x = nextX + col * blockSize;
                int y = nextY + row * blockSize;
                g.setColor(Color.GRAY);
                g.drawRect(x, y, blockSize, blockSize);
            }
        }       
    }

    public void drawCurrentTetromino(Graphics g, int pieceX, int pieceY, 
                                     int currentTetrominoShape, int[][] currentPieceShape) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (currentPieceShape[row][col] != 0) {
                    int gridX = pieceX + col;  
                    int gridY = pieceY + row; 
                    int x = boardX + gridX * blockSize;
                    int y = boardY + gridY * blockSize;  
                    g.setColor(Tetromino.TetrominoColors[currentTetrominoShape+1]);
                    g.fillRect(x, y, blockSize, blockSize);
                }
            }   
        }
    }
    
    //for repaint() / paintcomponent and maybe other things
    public void redrawGrid(Graphics g, int[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] != 0) {
                    int x = boardX + col * blockSize;
                    int y = boardY + row * blockSize; 
                    g.setColor(Tetromino.TetrominoColors[grid[row][col]]);
                    g.fillRect(x, y, blockSize, blockSize);
                }
            }   
        }
    }
}