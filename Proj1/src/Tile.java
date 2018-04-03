/*
Project 1: 2048 Created by Nick Romano on 2/11/18
This game was going to use images for the tiles, but unfortunately had to be changed at the last minute because
of a displaying bug I was having
 */
public class Tile {
    private int value;

    public Tile(){
        this.setValue(0);
    }

    public Tile(int value){
        this.value = value;
    }

    public void setValue(int value){
        this.value = value;
    }

    public int getValue(){
        return(this.value);
    }

    public boolean isEmpty(){
        if(this.value == 0)
            return true;
        else
            return false;
    }

    public void mergeWith(Tile tile){
        this.setValue(value + tile.getValue());
    }

    public void erase(){
        this.setValue(0);
    }
}
