
public class ShipBoard {
    public static final int DIM = 10;
    private BoardCell[][] board = new BoardCell[DIM][DIM];
    private int numShips = 10;
    private int[][] shipLocationData = new int[numShips][4];
    private boolean[] sunkShips = new boolean[numShips];
    private int numSunkShips = 0; 

    public ShipBoard() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                board[i][j] = BoardCell.WATER;
            }
        }
    }
        
    public void placeShip(int x, int y, int length, int isHorizontal) throws GameException {
        if (isHorizontal == 1) {
            checkValidCoordinate(x+length-1, y);
            for (int i = 0; i < length; i++) {
                if (board[x++][y] == BoardCell.SHIP) {
                    throw new GameException("collision");
                }
            }
            for (int i = 0; i < length; i++) {
                board[x++][y] = BoardCell.SHIP;
            }
        }
        
        if (isHorizontal == 0) {
            checkValidCoordinate(x, y+length-1);
            for (int i = 0; i < length; i++) {
                if (board[x][y++] == BoardCell.SHIP) {
                    throw new GameException("collision");
                }
            }
            for (int i = 0; i < length; i++) {
                board[x][y++] = BoardCell.SHIP;
            }
        }
        
        for (int i = 0; i < shipLocationData.length; i++) {
            if (shipLocationData[i] != null) {
                shipLocationData[i][0] = x;
                shipLocationData[i][1] = y;
                shipLocationData[i][2] = length;
                shipLocationData[i][3] = isHorizontal;
                break;
            }
        }
        
    }
    
    public int[] shoot(int x, int y) throws GameException {
        checkValidShoot(x, y);
        int[] result = new int[5];
        if (this.board[x][y] == BoardCell.WATER) {
            board[x][y] = BoardCell.MISSED;
            result[0] = ShootResults.MISSED.getValue();
            return result;           
        } else {
            board[x][y] = BoardCell.SHOT;
            result = checkSunk();
            if (result == null) {
                result[0] = ShootResults.SHOT.getValue();
                return result;
            }
            else return result;
        }
    }
    
    public boolean isGameOver() {
        return numSunkShips == numShips;
    }
    
    public void display() {
        System.out.println("   1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < DIM; i++) {
            
            if (i == 9) System.out.print(i+1 + " ");
            else System.out.print(" " + (i+1) + " ");
            
            for (int j = 0; j < DIM; j++) {
                System.out.print(board[i][j].getSymbol() + " ");
            }
            System.out.println();
        }
        
    }
    
    private void checkValidCoordinate(int x, int y) throws GameException {
        if (x >= DIM || y >= DIM || x < 0 || y < 0) {
            throw new GameException("wrong parameters");
        }
    }
    
    public void checkValidShoot(int x, int y) throws GameException {
        checkValidCoordinate(x, y);
        if (this.board[x][y] == BoardCell.MISSED ||
            this.board[x][y] == BoardCell.SHOT ||
            this.board[x][y] == BoardCell.SUNK) {
            throw new GameException("position already occupied");
        }
    }
      
    private int[] checkSunk() {
        for (int i = 0; i < shipLocationData.length; i++) {
            if (sunkShips[i] == false) {
                int x = shipLocationData[i][0];
                int y = shipLocationData[i][1];
                int length = shipLocationData[i][2];
                int isHorizontal = shipLocationData[i][3];
                if (isHorizontal == 1) {
                    int counter = 0;
                    for (int j = 0; j < length; j++) {
                        if (board[x++][y] == BoardCell.SHOT) {
                            counter++;
                        }
                    }
                    if (counter == length) {
                        for (int j = 0; j < length; j++) {
                            board[x++][y] = BoardCell.SUNK;
                        }
                        numSunkShips++;
                    }
                }
                if (isHorizontal == 0) {
                    int counter = 0;
                    for (int j = 0; j < length; j++) {
                        if (board[x][y++] == BoardCell.SHOT) {
                            counter++;
                        }
                    }
                    if (counter == length) {
                        for (int j = 0; j < length; j++) {
                            board[x][y++] = BoardCell.SUNK;
                        }
                        numSunkShips++;
                    }
                }
                int[] result = new int[5];
                result[0] = ShootResults.SUNK_1_SHIP.getValue();
                result[1] = shipLocationData[i][0];
                result[2] = shipLocationData[i][1];
                result[3] = shipLocationData[i][2];
                result[4] = shipLocationData[i][3];
                return result;
            }
        }
        return null;
    }
    
}
