package Tetris;

public class TetrominoBag {
    private final int[] startingBag = {0, 1, 2, 3, 4, 5, 6};
    private int[] shuffledBag;
    private int placeInBag;
    
    public TetrominoBag() {
        shuffledBag = shuffleBag(startingBag);
        placeInBag = 0;
    }
    
    public int getNext() {
        int next = shuffledBag[placeInBag];
        placeInBag++;
        
        if (placeInBag == 7) {
            placeInBag = 0;
            shuffledBag = shuffleBag(startingBag);
        }
        
        return next;
    }
    
    private int[] shuffleBag(int[] unshuffledBag) {
        int[] bag = unshuffledBag.clone();
        for (int i = bag.length-1; i > 0; i--){
            int j = (int)(Math.random()*(i+1));
            int temp = bag[i];
            bag[i] = bag[j];
            bag[j] = temp;
        }
        return bag;
    }
}