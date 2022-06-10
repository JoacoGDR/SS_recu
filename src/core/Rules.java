package core;

@FunctionalInterface
public interface Rules {

    //true si va a vivier en el estado siguinete
    //false si muere en el estado siguiente
    //no tiene que cambiar el estado de la celda
    int apply(Cell new_cell, Boolean old_state, int numLiveCells);
    //boolean applyRulesToKill(Cell cell, int numLiveCells);



}
