package core;

public class RuleOfLife implements Rules{
    @Override
    public int apply(Cell new_cell,Boolean isAlive, int numLiveCells) {
        if(numLiveCells == 3 && !isAlive ){
            new_cell.revive();
            return 1;
        }
        if(isAlive && numLiveCells == 2){
            new_cell.revive();
            return 1;
        }
        if(isAlive && numLiveCells ==3){
            new_cell.revive();
            return 1;
        }
        return 0;
    }
}
