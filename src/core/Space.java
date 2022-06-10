package core;

import javafx.geometry.Point3D;

import java.util.Arrays;

public class Space{

    private int centerXY, centerZ;

    public Cell[][][] getSpace() {
        return space;
    }
    public int cellAmmo;
    private final Cell[][][] space;

    public int getSize() {
        return size;
    }

    public boolean isTreeD() {
        return treeD;
    }

    private final int size;
    private final boolean treeD; //3D
    private final Point3D center;

    public Space(int size, boolean treeD ) {
        this.treeD = treeD;
        this.size = size;
        
        centerXY= size/2;
        centerZ= (treeD) ? size/2 : 0;
        
        cellAmmo = 0;
        if (treeD){
            center = new Point3D(centerXY, centerXY ,centerZ);
            space = new Cell[size][size][size];
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                    for(int k=0; k<size; k++){
                        space[i][j][k] = new Cell(false, (int) getDistOrigin(i,j,k));
                    }
                }
            }
        }else{
            center = new Point3D(centerXY, centerXY ,0);
            space = new Cell[size][size][1];
            for(int i=0; i<size; i++){
                for(int j=0; j<size; j++){
                        space[i][j][0] = new Cell(false, (int) getDistOrigin(i,j,0));
                }
            }
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(space);
    }

    public void add(int x, int y, int z){
        cellAmmo ++;
        space[x][y][z].revive();
    }

    public Space update(Rules rules) {
        Space toRet = new Space(size,treeD);
        Cell[][][] new_space = toRet.getSpace();
        for(int i=0; i<size; i++){
            for(int j=0; j<size;j++){
                if(treeD) {
                    for (int k = 0; k < size; k++) {
                        toRet.cellAmmo += rules.apply(new_space[i][j][k],space[i][j][k].isAlive(),getNeighbors(i,j,k));
                    }
                }
                else{
                    toRet.cellAmmo += rules.apply(new_space[i][j][0], space[i][j][0].isAlive(), getNeighbors(i,j,0));
                }
            }
        }
        return toRet;
        //primero matamos todos
        //itera sobre la lista reviveindo las celdas que estan en la lista
    }



    public int getNeighbors(int x, int y, int z) {
        int toRet = space[x][y][z].isAlive() ? -1 : 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (treeD) {
                    for (int k = -1; k < 2; k++) {
                        if (x + i > 0 && x + i < size - 1 && y + j > 0 && y + j < size - 1 && z + k > 0 && (z + k < size - 1)) { //checkea que no este contando celdas que no existen
                            toRet += space[x + i][y + j][z + k].isAlive() ? 1 : 0;
                        }
                    }
                } else {
                    if (x + i > 0 && x + i < size - 1 && y + j > 0 && y + j < size - 1) { // lo mismo que el if de arriba pero en 2D
                         toRet += space[x + i][y + j][z].isAlive() ? 1 : 0;
                    }
                }
            }
        }
        return toRet;
    }

    private double getDistOrigin(int x, int y, int z){
        return center.distance( x, y, z);
    }

    public boolean gameOver(Space prevspace){
        Cell[][][] prev  = prevspace.getSpace();
        boolean allAreDead = true,
                spaceDidntChange = true; //lo pongo en false pero tiene que ser true. habría que comparar tn con tn+1 y si difieren entonces apagar el flag
        if(treeD) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    for (int k = 0; k < size; k++) {
                        if(!prev[i][j][k].equals(space[i][j][k]) ){
                            spaceDidntChange = false;
                        }
                        if (space[i][j][k].isAlive()) {
                            allAreDead = false;
                            if ((k == 0 || j == 0 || i == 0 || k == size - 1 || j == size - 1 || i == size - 1)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        else {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if(!prev[i][j][0].equals(space[i][j][0])){
                        spaceDidntChange = false;
                    }
                    if (space[i][j][0].isAlive()) {
                        allAreDead = false;
                        if ((j == 0 || i == 0 || j == size - 1 || i == size - 1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return allAreDead || spaceDidntChange;
    }


}