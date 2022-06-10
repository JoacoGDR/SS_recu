package core;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;


import javafx.geometry.Point3D;

public class CellularAutomaton {
	
    private int size = 100;
    private Space space;
    private static final int MAX_ITERATIONS = 250;
    Scanner dynamicScanner;
    private String statsFile = "./resources/stats";
    private String dynamicFile;
    private boolean treeD;

    public CellularAutomaton(String staticFile, String dynamicFileInput){

    	this.dynamicFile= dynamicFileInput.concat(".xyz");

        InputStream dynamicStream = CellularAutomaton.class.getClassLoader().getResourceAsStream(dynamicFileInput + ".txt");
        assert dynamicStream != null;
        InputStream staticStream = CellularAutomaton.class.getClassLoader().getResourceAsStream(staticFile + ".txt");
        assert staticStream != null;

        dynamicScanner = new Scanner(dynamicStream);
        Scanner staticScanner = new Scanner(staticStream);

        size = Integer.parseInt(staticScanner.next()); //Tam del tablero
        
        String dim = staticScanner.next(); //Dim del tablero
        staticScanner.close();

        treeD = dim.equals("3D");
		if(treeD){
			statsFile = statsFile + "_3D_";
		}
		else{
			statsFile = statsFile + "_2D_";
		}
        //lee del static el size y si el 3D o no
        space = new Space(size, treeD);
        
        int initialParticles= Integer.parseInt(dynamicScanner.next());//First line initial particles
		if(treeD){
			statsFile = statsFile + initialParticles/10 + ".txt";
		}
		else{
			statsFile = statsFile + initialParticles + ".txt";
		}

        dynamicScanner.next(); //Skip comments line
        
        int i= 0;
        while (dynamicScanner.hasNext() && i < initialParticles) {
        	int x= Integer.parseInt(dynamicScanner.next());
        	int y= Integer.parseInt(dynamicScanner.next());
        	int z= Integer.parseInt(dynamicScanner.next());
        	space.add(x, y, z);
        	i++;
        }
        dynamicScanner.close();
		writeOutput(space,0);
    }

    public void solve(Rules rules){
    	int t=1;
		Space oldspace;
		do {
			oldspace = space;
			space = space.update(rules);
			writeOutput(space,t);
			t++;
		}while(t< MAX_ITERATIONS && !space.gameOver(oldspace));

    }
    
    public void getAnimationFile() {
        try {
			writeAnimationFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }

    private void writeOutput(Space liveCell, int t){
    	try {
    	    String filename= "./resources/" + dynamicFile;
    	    FileWriter fw = new FileWriter(filename,true);
			FileWriter sw = new FileWriter(statsFile,!(t == 0));
			double max_center_dist = 0;
			double center_dist = 0;
			int aliveCells = 0;
			if(t != 0) {
				fw.write(space.cellAmmo + "\n");
				fw.write("t=" + Integer.toString(t) + "\n");
			}
    	    //
    	    for(int i=0;i<size; i++){
    			for(int j=0; j<size; j++){
    				if(space.isTreeD()){
    					for(int k=0; k<size; k++){
    						if(space.getSpace()[i][j][k].isAlive()) {
								aliveCells++;
								if(t != 0) {
									fw.write(i + "\t" + j + "\t" + k + "\n");
								}
								center_dist = getDistanceFromCenter(space.isTreeD(),i,j,k);
								if(center_dist > max_center_dist ){
									max_center_dist = center_dist;
								}

    						}
    					}
    				} else {
    					if(space.getSpace()[i][j][0].isAlive()){
							aliveCells++;
							if(t != 0) {
								fw.write(i + "\t" + j + "\t0" + "\n");
							}
							center_dist = getDistanceFromCenter(space.isTreeD(),i,j,0);
							if(center_dist > max_center_dist ){
								max_center_dist = center_dist;
							}
    					}
    				}
    			}
    		}
    	    sw.write(t + "\t" + max_center_dist + "\t" + aliveCells + "\n");
    	    fw.close();
			sw.close();
    	}
    	catch(IOException ioe) {
    	    System.err.println("IOException: " + ioe.getMessage());
    	}
    }

	public double getDistanceFromCenter(boolean is3d, double x, double y, double z){
		if(is3d){
			return (Math.sqrt((x-size/2)*(x-size/2) +(y-size/2)*(y-size/2) + (z-size/2)*(z-size/2)));
		}
		return (Math.sqrt((x-size/2)*(x-size/2) +(y-size/2)*(y-size/2)));
	}


    private void writeAnimationFile() throws IOException{

    	int centerXY= size/2;
        int centerZ= (treeD) ? (size/2) : 0;
        Point3D center = new Point3D(centerXY, centerXY ,centerZ);
        double maxDist= Math.sqrt(Math.pow(size/2, 2) + Math.pow(size/2, 2));

        dynamicScanner = new Scanner(dynamicFile);
        InputStream dynamicStream = CellularAutomaton.class.getClassLoader().getResourceAsStream(dynamicFile);
        assert dynamicStream != null;

        dynamicScanner = new Scanner(dynamicStream);

        String date = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

	    String filename= ("./resources/animation_" + date + dynamicFile);//
	    FileWriter fw = new FileWriter(filename,true);//

	    boolean stopX= false;
	    boolean stopY= false;
	    boolean stopZ= false;

        while (dynamicScanner.hasNext()) {
        	int initialParticles= Integer.parseInt(dynamicScanner.next()); //First line initial particles
        	String time= dynamicScanner.next(); //time line

        	fw.write(treeD ? (initialParticles + 8) + "\n" : (initialParticles + 4) + "\n");//
    	    fw.write(time + "\n");//

    	    for (int i = 0; i < initialParticles; i++) {
    	    	int x= Integer.parseInt(dynamicScanner.next());
            	int y= Integer.parseInt(dynamicScanner.next());
            	int z= Integer.parseInt(dynamicScanner.next());

    			Point3D auxPoint= new Point3D(x,  y, z);

            	fw.write("" + x + "\t" + "" + y + "\t" + "" + z + "\t" + (1 - auxPoint.distance(center)/maxDist) + "\t" + (auxPoint.distance(center)/maxDist) + "\t" + (auxPoint.distance(center)/maxDist) + "\t0\n");//

            	stopX= stopX || margin(x);
            	stopY= stopY || margin(y);
            	stopZ= stopZ || (treeD && margin(z)); //In 2D does not make sense

    	    }

    	    //Escribo las particulas de borde//

    	    if(space.isTreeD()){
    			fw.write("0\t0\t0\t0\t0\t0\t100\n" +
    					size + "\t" + size + "\t" + size + "\t0\t0\t0\t100\n" +
    					"0\t0\t" + size + "\t0\t0\t0\t100\n" +
    					size + "\t0\t0\t0\t0\t0\t100\n" +
    					"0\t" + size + "\t0\t0\t0\t0\t100\n" +
    					size + "\t" + size + "\t0\t0\t0\t0\t100\n" +
    					size + "\t0 " + size + "\t0\t0\t0\t100\n" +
    					"0\t" + size + "\t" + size + "\t0\t0\t0\t100\n");
    		} else {
    			fw.write("0\t0\t0\t0\t0\t0\t100\n" +
    					size + "\t" + size + "\t0\t0\t0\t0\t100\n" +
    					"0\t" + size + "\t0\t0\t0\t0\t100\n" +
    					size + "\t0\t0\t0\t0\t0\t100\n");
    		}
		}

        fw.close();
        dynamicStream.close();
    }

	private boolean margin(int pos) {
		if (pos == 1 || pos == (size)) {
			return true;
		}
		return false;
	}

	static public void main(String[] args) {
		Integer[] percentages = new Integer[]{10,20,30,60,70,80};
		for(Integer p : percentages) {
			CellularAutomaton cell = new CellularAutomaton("static" + p, "dynamic" + p);
			System.out.println("Creating simulation");
			cell.solve(new Rule2());
			System.out.println("Simulation done");
			cell.getAnimationFile();
		}
		System.out.println("End");

    }

}

/**/
//
	//Regla 1: Juego de la vida (2D y 3D)
/*
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
*/
	//2D
	//Regla 2: muerta y mas de 4 vive, viva y menos de 5 vive
/*
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

*/
	//Regla 3: muerta y más de 1 vive, viva y más de 1 vive
/*
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
*/
	//3D
	//Regla 2: muerta y mas de 6 vive, viva y menos de 16 vive 
/*
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
*/
	//Regla 3: muerta y mas de 6 vive, viva y mas de 12 vive
/*
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
*/
//
/**/