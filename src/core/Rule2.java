package core;

public class Rule2 implements Rules{
    @Override
    public int apply(Cell new_cell,Boolean isAlive, int numLiveCells) {
        if(!isAlive && numLiveCells > 4){
            new_cell.revive();
            return 1;
        }
        if(isAlive && numLiveCells < 5){
            new_cell.revive();
            return 1;
        }
        return 0;
    }
}
