package Tetris;

public class GameController {
    private int pieceX, pieceY;
    private int currentTetrominoShape;
    private int[][] currentPieceShape;
    private int currentRotation;
    private int[][] grid;
    private final int startCol;
    private final int startRow;
    private final int columns;
    private final int totalRows;
    
    public GameController(int columns, int totalRows, int startCol, int startRow) {
        this.columns = columns;
        this.totalRows = totalRows;
        this.startCol = startCol;
        this.startRow = startRow;
        this.grid = new int[totalRows][columns];
        this.pieceX = startCol;
        this.pieceY = startRow;
    }
    
    public void setCurrentPiece(int tetrominoShape, int rotation) {
        this.currentTetrominoShape = tetrominoShape;
        this.currentRotation = rotation;
        this.currentPieceShape = Tetromino.getShape(tetrominoShape, rotation);
    }
    
    public boolean canMove(int newX, int newY) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (currentPieceShape[row][col] != 0) {
                    int newPosX = newX + col;
                    int newPosY = newY + row;
                    
                    if (newPosX < 0 || newPosX >= columns) return false;
                    if (newPosY >= totalRows) return false;
                    if (grid[newPosY][newPosX] != 0) return false;
                }
            }
        }
        return true;
    }
    
    //puts the tetromino data into grid
    public void lockTetromino() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (currentPieceShape[row][col] != 0) {
                    int gridX = pieceX + col;  
                    int gridY = pieceY + row; 
                    if (gridY >= 0 && gridX >= 0) {
                        grid[gridY][gridX] = currentPieceShape[row][col];
                    }
                }
            }
        }
    }
    
    public void hardDrop() {
        while (canMove(pieceX, pieceY + 1)) {
            pieceY++;
        }
    }
    
    public void spawnNewTetromino(int nextShape) {
        pieceX = startCol;
        pieceY = startRow;
        setCurrentPiece(nextShape, 0);
    }
    
    // getters
    public int getPieceX() { return pieceX; }
    public int getPieceY() { return pieceY; }
    public int getCurrentTetrominoShape() { return currentTetrominoShape; }
    public int[][] getCurrentPieceShape() { return currentPieceShape; }
    public int getCurrentRotation() { return currentRotation; }
    public int[][] getGrid() { return grid; }
    
    // setters
    public void setPieceX(int x) { pieceX = x; }
    public void setPieceY(int y) { pieceY = y; }
}