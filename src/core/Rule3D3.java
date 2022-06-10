package core;

public class Rule3D3 implements Rules{
        //Regla 3: muerta y mas de 6 vive, viva y mas de 12 vive
        @Override
        public int apply(Cell new_cell,Boolean isAlive, int numLiveCells) {
                if(!isAlive && numLiveCells > 6){
                        new_cell.revive();
                        return 1;
                }
                if(isAlive && numLiveCells > 12){
                        new_cell.revive();
                        return 1;
                }
                return 0;
        }
}
