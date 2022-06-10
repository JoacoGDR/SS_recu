package core;

public class Rule3 implements Rules{
    @Override
    public int apply(Cell new_cell,Boolean isAlive, int numLiveCells) {
        if(!isAlive && numLiveCells > 1){
            new_cell.revive();
            return 1;
        }
        if(isAlive && numLiveCells > 1){
            new_cell.revive();
            return 1;
        }
        return 0;
    }
}
