package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Sort {
	public static void main(String[] args) {
		int m[][] = new int[100][100];
		
		Random creator = new Random();
		
		int cont = 0;
		
		for(int i=0;i<100;i++)
			for(int x=0;x<100;x++)
				m[i][x] = creator.nextInt(50);
				cont+=1;
		
		m = ordenation(m);
				
		for(int i=0;i<100;i++)
			for(int x=0;x<100;x++)
				System.out.println(m[i][x]);
	}
	
	public static int[][] ordenation(int m[][]) {
		int lines = 0;
		int columns = 0;
		    
		if (m.length > 0){
			lines = m.length;
			columns = m[0].length;            
		}
		
		ArrayList<Integer> aux = new ArrayList<Integer>(); 
		for(int i=0;i<lines;i++)
			for(int x=0;x<columns;x++)
				aux.add(m[i][x]);
				
		Collections.sort(aux);
		
		int z=0;
		for(int i=0;i<lines;i++)
			for(int x=0;x<columns;x++) {
				m[i][x] = aux.get(z);
				z++;
		}
		return m;
	}
}
