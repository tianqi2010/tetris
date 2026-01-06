package Tetris;

public class GameController {
    private int pieceX, pieceY;
    private int currentTetrominoShape;
    private int[][] currentPieceShape;
    private int currentRotation;
    private int[][] grid;
    public int heldTetromino = -1;
    public boolean holdingTetromino = false;
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
    
    public boolean canMove(int newX, int newY, int[][] shape) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                if (shape[row][col] != 0) {
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
    
    //puts the tetromino data into grid, and resets holdingTetromino boolean
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
        holdingTetromino = false;
    }
    
    public void hardDrop() {
        while (canMove(pieceX, pieceY + 1, getCurrentPieceShape())) {
            pieceY++;
        }
    }
    
    public void spawnNewTetromino(int nextShape) {
        pieceX = startCol;
        pieceY = startRow;
        setCurrentPiece(nextShape, 0);
    }

    // only takes in 0, 1, 2, 3, so have to mod it when inputting i guess
    public void rotateTetromino(int targetRotation){

        int[][] newRotatedShape = Tetromino.getShape(getCurrentTetrominoShape(), targetRotation);

        if (canMove(getPieceX(), getPieceY(), newRotatedShape)){
            currentPieceShape = newRotatedShape;
            currentRotation = targetRotation;
        } else { // wall kick test
            if (currentTetrominoShape != 3){
                int[][] wallKicks = WallKickData.getWallKickData(currentRotation, targetRotation, currentTetrominoShape);
                for (int i = 0; i < WallKickData.numberOfTests; i++){
                    int newKickX = getPieceX() + wallKicks[i][0];
                    int newKickY = getPieceY() + wallKicks[i][1];

                    if (canMove(newKickX, newKickY, newRotatedShape)){
                        setPieceX(newKickX);
                        setPieceY(newKickY);
                        currentPieceShape = newRotatedShape;
                        currentRotation = targetRotation;
                        return;
                    }
                }
            }
        }
    }

    public void clearLines(){
        int linesCleared = 0;
        int currentRow = totalRows - 1;
    
        while (currentRow >= 0) {
            boolean lineComplete = true;

            // check if line can be cleared
            for (int col = 0; col < columns; col++) {
                if (grid[currentRow][col] == 0) {
                    lineComplete = false;
                    break;
                }
            }

            if (lineComplete) {
                linesCleared++;

                // move all the rows above the current one down
                for (int row = currentRow; row > 0; row--) {
                    System.arraycopy(grid[row - 1], 0, grid[row], 0, columns);
                }

                // clear top row because i moved everything down one
                for (int col = 0; col < columns; col++) {
                    grid[0][col] = 0;
                }

            } else {
                // move to next row
                currentRow--;
            }
        }
    }
    
    // getters
    public int getPieceX() { return pieceX; }
    public int getPieceY() { return pieceY; }

    // i realize these variable names are confusing, will change later. for now, piece shape is the int[][]. how did i even manage to do this anyway.
    public int getCurrentTetrominoShape() { return currentTetrominoShape; }
    public int[][] getCurrentPieceShape() { return currentPieceShape; }

    public int getCurrentRotation() { return currentRotation; }
    public int[][] getGrid() { return grid; }

    public boolean isHoldingTetromino() { return holdingTetromino; };
    public int getHeldTetromino() { return heldTetromino; }

    
    // setters
    public void setPieceX(int x) { pieceX = x; }
    public void setPieceY(int y) { pieceY = y; }
    public void setHoldingTetromino(boolean isHolding) { this.holdingTetromino = isHolding; }
    public void setHeldTetromino (int tetrominoType) { this.heldTetromino = tetrominoType; }
}