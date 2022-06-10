package core;

import java.util.Objects;

public class Cell{
    private final int distance;
    private boolean alive;


    public Cell(Boolean alive, int distance) {
        this.alive = alive;
        this.distance = distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance, alive);
    }

    public boolean isAlive() {
        return alive;
    }

    public void revive() {
        alive= true;
    }
    public void kill() {
        alive= false;
    }

    public void ChangeStatus(){
        alive = !alive;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return alive == cell.alive;
    }
}
