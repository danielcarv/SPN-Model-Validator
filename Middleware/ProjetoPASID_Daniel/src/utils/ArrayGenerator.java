package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class ArrayGenerator {
	public static void main(String[] args) throws IOException {
		generator();
		
	}
	public static void generator() throws IOException {
		int m[][] = new int[4][4];

		Random gerador = new Random();
		for(int i=0;i<4;i++)
			for(int x=0;x<4;x++)
				m[i][x] = gerador.nextInt(50);
		
		FileWriter writer = new FileWriter("origin1.txt");
	    PrintWriter print = new PrintWriter(writer);
		
		for(int i=0;i<4;i++) {
			for(int x=0;x<4;x++) {
				print.print(m[i][x]+" ");
			}
			print.print("\n");
		}
		
		writer.close();
	}
}
