package middleware;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Middleware {
	private Socket middlewareSocket;
	private Integer port;
	private String ip; 
	private int[][] filePath;

	
	public Middleware(String ip, Integer port, int[][] filePath) {
		this.ip = ip;
		this.port = port;
		this.filePath = filePath;
	}

	public int[][] execute() throws FileNotFoundException, IOException, ClassNotFoundException {
		createConnection();
		return enviarArquivo();
	}



	private void createConnection() throws IOException {
		middlewareSocket = new Socket(ip, port);
	}

	private int[][] enviarArquivo() throws IOException, ClassNotFoundException {
		ObjectOutputStream output = new ObjectOutputStream(middlewareSocket.getOutputStream());
		output.writeObject(filePath);

	    ObjectInputStream input = new ObjectInputStream(middlewareSocket.getInputStream());
		int[][] lido = (int[][]) input.readObject();
		
		input.close();
		output.close();
		
		return lido;
	}
	
}
