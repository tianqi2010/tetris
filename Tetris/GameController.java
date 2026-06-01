package Tetris;

import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    private int pieceX, pieceY;
    private int currentTetrominoShape;
    private int[][] currentPieceShape;
    private int currentRotation;
    private int[][] grid;
    public int previousHeldTeromino = -1;
    public int heldTetromino = -1;
    public boolean holdingTetromino = false;
    private final int startCol;
    private final int startRow;
    private final int columns;
    private final int totalRows;
    private Timer timer = new Timer();
    private TimerTask task;
    public int delay = 1000; // milliseconds, gravity and soft drop
    public final int LOCK_DELAY = 500;
    public boolean isLocked = false;
    public double lockTime = 0;
    public int score = 0;
    public int totalLinesCleared = 0;

    private TetrominoBag bag;
    private Board board;

    private boolean isLost = false;
    public boolean isSpin = false;
    String linesClearedText = " ";

    public GameController(int columns, int totalRows, int startCol, int startRow, Board board, TetrominoBag bag) {

        this.columns = columns;
        this.totalRows = totalRows;
        this.startCol = startCol;
        this.startRow = startRow;
        this.grid = new int[totalRows][columns];
        this.pieceX = startCol;
        this.pieceY = startRow;
        this.board = board;
        this.bag = bag;
        startGameLoop();
    }
    private void startGameLoop() {
        if (timer != null) {
            timer.cancel();
        }

        Timer newTimer = new Timer();


        TimerTask task = new TimerTask() {
            @Override  
            public void run(){
                try{
                if (isLocked) {
                    if (System.currentTimeMillis() - lockTime >= LOCK_DELAY){
                        lockTetromino();
                        spawnNewTetromino(bag.getNext());
                        clearLines();
                        isLocked = false;
                    }
                }
                else {
                    if (canMove(getPieceX(), getPieceY() + 1, getCurrentPieceShape())){
                        setPieceY(getPieceY() + 1);
                    } else {
                        isLocked = true;
                        lockTime = System.currentTimeMillis();
                    }
                }
                board.repaint();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        newTimer.schedule(task, delay, delay);
    }      
    private int getDelayFromLevel(int level) {
    // Exponential speed increase
    // Base speed: 500ms at level 0
        double speed = 500.0 / Math.pow(1.2, level);
        return Math.max(16, (int) speed); // Cap at ~1 frame minimum
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
        if (isTSpin()){ isSpin = true; }
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
        isLocked = false;
        isSpin = false;

        if (isLost()){
            timer.cancel();
            try {
                Thread.sleep(2000);
            } catch (Exception e){
                e.printStackTrace();
            }
            board.showGameOver();
        }
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
                        isSpin = true;
                        return;
                    }
                }
            }
        }
    }

    private boolean isTSpin(){
        if (currentTetrominoShape != 3){
            return false;
        }

        int centerX = pieceX + 1;
        int centerY = pieceY + 1;

        int[][] corners = {
            {-1, -1},
            {1, -1},
            {-1, 1},
            {1, 1}
        };

        int filledCorners = 0;

        for (int i = 0; i < corners.length; i++) {
            int checkX = centerX + corners[i][0];
            int checkY = centerY + corners[i][1];
            if (checkY >= 0 && checkY < totalRows && checkX >= 0 && checkX < columns) {
                if (grid[checkY][checkX] != 0) {
                filledCorners++;
                }
            } else {
                filledCorners++;
            }
        }
        return filledCorners >= 3;
    }

    public void newTetromino(){
        lockTetromino();
        spawnNewTetromino(bag.getNext());
        clearLines();
    }

    public void clearLines(){
        int linesCleared = 0;
        int currentRow = totalRows - 1;

        previousHeldTeromino = currentTetrominoShape;
    
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

        totalLinesCleared += linesCleared;

        switch(linesCleared) {
            case 1:
                linesClearedText = "SINGLE";
                score += 100;
                break;
            case 2:
                linesClearedText = "DOUBLE";
                score += 200;
                break;
            case 3:
                linesClearedText = "TRIPLE";
                score += 500;
                break;
            case 4:
                linesClearedText = "QUAD";
                score += 800;
                break;
            default:
                linesClearedText = " ";
        }
        delay = getDelayFromLevel((int)(totalLinesCleared/10));
    }

    public boolean isLost(){
        for (int i = 0; i < columns; i++){
            for (int j = 0; j < 3; j++){
                if (grid[j][i] != 0){
                    System.out.println("GAME OVER");
                    score = 0;
                    return true;
                }
            }
        }
        return false;
    }

    public void resetGame() {

        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = 0;
            }
        }
    
 
        pieceX = startCol;
        pieceY = startRow;
        heldTetromino = -1;
        holdingTetromino = false;
        isLocked = false;
        lockTime = 0;

        setCurrentPiece(bag.getNext(), 0);
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
    public int getPreviousHeldTetromino() { return previousHeldTeromino; }

    
    // setters
    public void setPieceX(int x) { pieceX = x; }
    public void setPieceY(int y) { pieceY = y; }
    public void setHoldingTetromino(boolean isHolding) { this.holdingTetromino = isHolding; }
    public void setHeldTetromino (int tetrominoType) { this.heldTetromino = tetrominoType; }
}