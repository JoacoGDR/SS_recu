package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unused") //Warnings because Writer is not used, it only creates files
public class FilesCreator {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

    	boolean correctL= false;
    	int L= 50;

  
		System.out.println("Insert option: 2D/3D (default 2D)");
		BufferedReader readerDim = new BufferedReader(new InputStreamReader(System.in));
		String auxDim = readerDim.readLine();
		
		int dim= (auxDim.contains("3")) ? 3 : 2;
		//int dim = 3;

    	boolean correctMax= false;
		Integer[] percentages = new Integer[]{10,20,30,60,70,80};
		for(Integer p : percentages) {

			System.out.println("L: " + L + " dimension: " + dim + "D" + " starting: %" + p);

			Writer writerStatic = new Writer(L, dim, p, "static", "static" + p);
			Writer writerDynamic = new Writer(L, dim, p, "dynamic", "dynamic" + p);
		}

    }

}
