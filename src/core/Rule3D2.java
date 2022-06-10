package core;

public class Rule3D2 implements Rules{
    //Regla 2: muerta y mas de 6 vive, viva y menos de 16 vive
    @Override
    public int apply(Cell new_cell,Boolean isAlive, int numLiveCells) {
        if(!isAlive && numLiveCells > 6){
            new_cell.revive();
            return 1;
        }
        if(isAlive && numLiveCells < 16){
            new_cell.revive();
            return 1;
        }
        return 0;
    }
}
