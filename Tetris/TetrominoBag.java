package Tetris;

public class TetrominoBag {
    private final int[] startingBag = {0, 1, 2, 3, 4, 5, 6}; 
    private final int bagSize = 7;
    private int[] currentBag;
    private int[] nextBag;
    private int placeInBag;
    
    public TetrominoBag() {
        currentBag = shuffleBag(startingBag);
        nextBag = shuffleBag(startingBag);
        placeInBag = 0;
    }
    
    public int getNext() {
        int next = currentBag[placeInBag];
        placeInBag++;
        
        if (placeInBag == 7) {
            placeInBag = 0;
            nextBag = shuffleBag(startingBag);
        }
        
        return next;
    }

    // peek into the next n pieces, n should be less than 7 or something like that
    public int[] peekNextn(int n){
        int[] nextPieces = new int[n];
        int tempPosition = placeInBag;
        int[] tempBag = currentBag;
        int[] tempNextBag = nextBag;

        for (int i = 0; i < n; i++){
            nextPieces[i] = tempBag[tempPosition];
            tempPosition++;

            if (tempPosition == bagSize){
                tempBag = tempNextBag;
                tempPosition = 0;
            }
        }
        return nextPieces;
    }
    
    private int[] shuffleBag(int[] unshuffledBag) {
        int[] bag = unshuffledBag.clone();
        //fisher yates shuffle
        for (int i = bag.length-1; i > 0; i--){
            int j = (int)(Math.random()*(i+1));
            int temp = bag[i];
            bag[i] = bag[j];
            bag[j] = temp;
        }
        return bag;
    }
}