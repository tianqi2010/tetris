package Tetris;

public class WallKickData {
    // JLSTZ wall kicks
    public static final int[][][] JLSTZWallKicks = {
        // 0->R (0->1)
        {{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}},
        // R->0 (1->0)
        {{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
        // R->2 (1->2)
        {{0, 0}, {1, 0}, {1, 1}, {0, -2}, {1, -2}},
        // 2->R (2->1)
        {{0, 0}, {-1, 0}, {-1, -1}, {0, 2}, {-1, 2}},
        // 2->L (2->3)
        {{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}},
        // L->2 (3->2)
        {{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}},
        // L->0 (3->0)
        {{0, 0}, {-1, 0}, {-1, 1}, {0, -2}, {-1, -2}},
        // 0->L (0->3)
        {{0, 0}, {1, 0}, {1, -1}, {0, 2}, {1, 2}}
    };
    
    // I-piece wall kicks 
    public static final int[][][] IWallKicks = {
        // 0->R (0->1)
        {{0, 0}, {-2, 0}, {1, 0}, {-2, -1}, {1, 2}},
        // R->0 (1->0)
        {{0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2}},
        // R->2 (1->2)
        {{0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1}},
        // 2->R (2->1)
        {{0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1}},
        // 2->L (2->3)
        {{0, 0}, {2, 0}, {-1, 0}, {2, 1}, {-1, -2}},
        // L->2 (3->2)
        {{0, 0}, {-2, 0}, {1, 0}, {-2, -1}, {1, 2}},
        // L->0 (3->0)
        {{0, 0}, {1, 0}, {-2, 0}, {1, -2}, {-2, 1}},
        // 0->L (0->3)
        {{0, 0}, {-1, 0}, {2, 0}, {-1, 2}, {2, -1}}
    };

    public static final int numberOfTests = 5;

    public static int[][] getWallKickData(int currentRotation, int targetRotation, int currentTetrominoShape) {
        // chooose to use I table or JLSTZ table
        int[][][] kickTable = (currentTetrominoShape == 0) ? IWallKicks : JLSTZWallKicks;
        
        if (currentRotation == 0 && targetRotation == 1) return kickTable[0]; // 0->R
        if (currentRotation == 1 && targetRotation == 0) return kickTable[1]; // R->0
        if (currentRotation == 1 && targetRotation == 2) return kickTable[2]; // R->2
        if (currentRotation == 2 && targetRotation == 1) return kickTable[3]; // 2->R
        if (currentRotation == 2 && targetRotation == 3) return kickTable[4]; // 2->L
        if (currentRotation == 3 && targetRotation == 2) return kickTable[5]; // L->2
        if (currentRotation == 3 && targetRotation == 0) return kickTable[6]; // L->0
        if (currentRotation == 0 && targetRotation == 3) return kickTable[7]; // 0->L
        
        return new int[0][0];
    }
}