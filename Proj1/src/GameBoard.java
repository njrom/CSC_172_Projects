/*
Project 1: 2048 Created by Nick Romano on 2/11/18
This game was going to use images for the tiles, but unfortunately had to be changed at the last minute because
of a displaying bug I was having
 */
import java.util.Random;
import java.util.ArrayList;

public class GameBoard{

    private int boardSize = 4;
    private double lowProbablility = 0.8; // Probability of a 2 tile spawning
    private Tile[][] tiles = new Tile[boardSize][boardSize];
    Tile[][] preMoveTiles = new Tile[boardSize][boardSize];
    private boolean validMove = false;
    public boolean gameOver = false;

    // Populates GameBoard with tiles of value zero
    public GameBoard(){
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles.length; j++){
                tiles[i][j] = new Tile();
            }
        }

        // At the Start of the game Generate two tiles:
        generateTile();
        generateTile();
    }
    public boolean generateTile(){
        Random random = new Random();

        if(!validMove){
            if((isFull())||(this.getMax() == 2048)){
                gameOver = true;
            }
            validMove = true;
            return false;
        }

        while(true){
            int x = random.nextInt(boardSize);
            int y = random.nextInt(boardSize);

            if(tiles[x][y].isEmpty()){
                tiles[x][y].setValue(newTileValue());
                return true;
            }

        }
    }

    public int newTileValue(){
        Random random = new Random();
        if(random.nextDouble() >= lowProbablility){
            return 4;
        }
        else {
            return 2;
        }
    }

    public boolean isFull(){
        for(int i =0; i < tiles.length; i++){
            for(int j=0; j < tiles[i].length; j++){
                if(tiles[i][j].isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }


    public void slideRight(){
        // Used to check for any change i.e the move being valid
        for(int i = 0; i < preMoveTiles.length; i++){
            for(int j = 0; j < preMoveTiles.length; j++){
                preMoveTiles[i][j] = tiles[i][j];
            }
        }

        for(int i = 0; i < tiles.length; i++){
            ArrayList<Tile> row = new ArrayList<>();

            // Copies non zero tiles to an array list in the same order
            for(int j = 0; j < tiles[i].length; j++){
                if(!(tiles[i][j].isEmpty())){
                    row.add(tiles[i][j]);
                }
            }
            // Merge similar tiles
            for(int j = row.size() - 1; j > 0;  j--){
                if (row.get(j).getValue() == row.get(j-1).getValue()){
                    row.get(j).mergeWith(row.get(j-1)); // Merges two equal tiles
                    row.get(j-1).erase();
                }
            }
            // Remove the Zeros created by merging tiles
            for(int j = 0; j < row.size(); j++){
                if(row.get(j).isEmpty()){
                    row.remove(j);
                }
            }
            // Re add the zeros to the left of the row
            int zerosRemoved = tiles[i].length - row.size();
            for(int j = 0; j < (zerosRemoved); j++){
                row.add(0, new Tile());
            }
            // Write the slid row arrayList back to the array
            for(int j = 0; j < tiles[i].length; j++){
                tiles[i][j] = row.get(j);
            }
        }
    }

    public void move(Direction direction){

        switch(direction){ // Rotates the arrays so that you can only slide to the right
            case UP:
                rotate(Direction.UP);
                slideRight();
                rotate(Direction.UP);
                rotate(Direction.UP);
                rotate(Direction.UP);
                if(sameBoard(preMoveTiles))
                    validMove = false;
                break;
            case DOWN:
                rotate(Direction.DOWN);
                slideRight();
                rotate(Direction.DOWN);
                rotate(Direction.DOWN);
                rotate(Direction.DOWN);
                if(sameBoard(preMoveTiles))
                    validMove = false;
                break;
            case LEFT:
                rotate(Direction.LEFT);
                slideRight();
                rotate(Direction.RIGHT);
                if(sameBoard(preMoveTiles))
                    validMove = false;
                break;
            case RIGHT:
                slideRight();
                if(sameBoard(preMoveTiles))
                    validMove = false;
                break;
        }
    }

    public boolean sameBoard(Tile[][] preMoveArray){
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++){
                if(preMoveArray[i][j].getValue() != tiles[i][j].getValue()){
                    return false;
                }
            }
        }
        return true;
    }

    public void rotate(Direction direction){ // Rely on rotating and reflecting the board so then i can slide right
        Tile[][] preRotTiles = new Tile[boardSize][boardSize];
        for(int i = 0; i < preRotTiles.length; i++){
            for(int j = 0; j < preRotTiles.length; j++){
                preRotTiles[i][j] = tiles[i][j];
            }
        }
        switch(direction){
            case LEFT:
                for(int i = 0; i < tiles.length; i++){
                    for(int j = 0; j < tiles.length; j++){
                        tiles[i][j] = preRotTiles[i][tiles.length-1-j];
                    }
                }
                break;
            case RIGHT:
                for(int i = 0; i < tiles.length; i++){
                    for(int j = 0; j < tiles.length; j++){
                        tiles[i][j] = preRotTiles[i][tiles.length-1-j];
                    }
                }
                break;
            case UP:
                for(int i = 0; i < tiles.length; i++){ // Cols
                    for(int j = 0; j < tiles[i].length; j++){ // Rows
                        tiles[j][tiles[i].length - 1 - i] = preRotTiles[i][j];
                    }
                }
                break;
            case DOWN:
                for(int i = 0; i < tiles.length; i++){
                    for(int j = 0; j < tiles[i].length; j++){
                        tiles[j][i] = preRotTiles[i][j];
                    }
                }
                break;
        }
    }

    public int getMax(){
        int max = 0;
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles[i].length; j++)
                if(tiles[i][j].getValue() > max){
                    max = tiles[i][j].getValue();
                }
        }
        return max;
    }
    public int[][] boardArr(){
        int[][] boardArr = new int[boardSize][boardSize];
        for(int i = 0; i < tiles.length; i++){
            for(int j = 0; j < tiles.length; j++){
                boardArr[i][j] = tiles[i][j].getValue();
            }
        }
        return boardArr;
    }
}

