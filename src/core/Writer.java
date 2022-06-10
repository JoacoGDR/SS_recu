package core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javafx.geometry.Point3D;

public class Writer {
	
	private static final int RANGE= 5;
    private Point3D center;
    private double maxDist;

    public Writer(int L, int dim, int max, String type,String name) {
    	
        int centerXY= L/2;
        int centerZ= (dim == 3) ? (L/2) : 0;
        center = new Point3D(centerXY, centerXY ,centerZ);
        maxDist= Math.sqrt(Math.pow(L/2, 2) + Math.pow(L/2, 2));

        try {
            File file = new File("./resources/" + name + ".txt");
            FileWriter myWriter = new FileWriter("./resources/" + name + ".txt");
            try {
            	if (type.compareTo("static") == 0) {
					this.staticFile(L, dim, myWriter);
				} else {
		            File file2 = new File("./resources/" + name + ".xyz");
		            FileWriter myWriter2 = new FileWriter("./resources/" + name + ".xyz");
					this.randomizePositions(L, dim, max, myWriter, myWriter2);
					myWriter2.close();
		            System.out.println("Successfully wrote to the file ./resources/" + name + ".xyz");
				}
			} catch (Exception e) {
				System.err.println("IOException");
			}
            myWriter.close();
            System.out.println("Successfully wrote to the file ./resources/" + name + ".txt");
        } catch (IOException e) {
            System.out.println("IOException ocurred");
            e.printStackTrace();
        }
    }

	public void writeSpace(int t,int cellnum, Space space, FileWriter writer) throws IOException {
		int size = space.getSize();
		writer.write(space.isTreeD() ? (cellnum + 8) : (cellnum + 4)); //numero de particulas + particulas de borde
		String time= "\nt=" + t +"\n";
		writer.write(time); //el numero de iteracion, (comentario que no se usa)
		for(int i=0;i<size; i++){
			for(int j=0; j<size; j++){
				if(space.isTreeD()){
					for(int k=0; k<size; k++){
						if(space.getSpace()[i][j][k].isAlive()) {
							writer.write(i + " " + j + " " + k + "\n") ;
						}
					}
				}
				else{
					if(space.getSpace()[i][j][0].isAlive()){
						writer.write(i + " " + j + " 0" +"\n");
					}
				}
			}
		}
		// Escribo las particulass de borde..
		if(space.isTreeD()){
			writer.write("0 0 0\n" + size + " " + size + " " + size + "\n"
			 				+ "0 0 " + size + "\n"
							+ size + " 0 0\n"
							+ "0 " + size + " 0\n"
							+ size + " " + size + " 0\n"
							+ size + " 0 " + size + "\n"
							+ "0 " + size + " " + size + "\n");
		}
		else{
			writer.write("0 0 0\n" + size + " " + size + " 0\n" +
						"0 " + size + " 0\n" +
						size + " 0 0\n");
		}
	}

	private void randomizePositions(int l, int dim, int startingMax, FileWriter myWriter, FileWriter myWriter2) throws IOException {
		Set<Point3D> cells = new HashSet<>();
		int middle= (int) Math.floor(l/2);
		int max= (dim == 2) ? startingMax : startingMax * 10;
		//int fileStartingMax= (dim == 2) ? max + 4 : max + 8;
		myWriter.write(max + "\n");
		myWriter2.write(max + "\n");
		myWriter.write("t=0\n");
		myWriter2.write("t=0\n");
		int i = 0;
		while (i < max) {
			int x= (int) Math.floor(Math.random() * 2 * RANGE) + (middle - RANGE);
			int y= (int) Math.floor(Math.random() * 2 * RANGE) + (middle - RANGE);
			int z= (dim == 2) ? 0 :  (int) Math.floor(Math.random() * 2 * RANGE) + (middle - RANGE);
			Point3D auxPoint= new Point3D(x,  y, z);
			if(!cells.contains(auxPoint)) {
				cells.add(auxPoint);
				myWriter.write("" + x + "\t" + "" + y + "\t" + "" + z + "\n");
				myWriter2.write("" + x + "\t" + "" + y + "\t" + "" + z + "\n");
				i++;
			}
			//myWriter.write("" + x + "\t" + "" + y + "\t" + "" + z + "\t" + (1 - auxPoint.distance(center)/maxDist) + "\t" + (auxPoint.distance(center)/maxDist) + "\t" + (auxPoint.distance(center)/maxDist) + "\t0\n");
		}
	    //Escribo las particulas de borde
	    /*if(dim != 2){
	    	myWriter.write("0\t0\t0\t0\t0\t0\t0\n" +
					l + "\t" + l + "\t" + l + "\t0\t0\t0\t100\n" +
					"0\t0\t" + l + "\t0\t0\t0\t100\n" +
					l + "\t0\t0\t0\t0\t0\t100\n" +
					"0\t" + l + "\t0\t0\t0\t0\t100\n" +
					l + "\t" + l + "\t0\t0\t0\t0\t100\n" +
					l + "\t0 " + l + "\t0\t0\t0\t100\n" +
					"0\t" + l + "\t" + l + "\t0\t0\t0\t100\n");
		}else {
			myWriter.write("0\t0\t0\t0\t0\t0\t100\n" +
					l + "\t" + l + "\t0\t0\t0\t0\t100\n" +
					"0\t" + l + "\t0\t0\t0\t0\t100\n" +
					l + "\t0\t0\t0\t0\t0\t100\n");
		}*/
		
	}

	private void staticFile(int l, int dim, FileWriter myWriter) throws IOException {
		myWriter.write("" + l + "\n");
		myWriter.write("" + dim + "D\n");
	}
}