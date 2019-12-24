package torre;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Torre {
	private Socket torreSocket;
	private Integer port;
	private String ip; 
	private int[][] filePath;

	
	public Torre(String ip, Integer port, int[][] filePath) {
		this.ip = ip;
		this.port = port;
		this.filePath = filePath;
	}

	public int[][] execute() throws FileNotFoundException, IOException, ClassNotFoundException {
		createConnection();
		return enviarArquivo();
	}



	private void createConnection() throws IOException {
		torreSocket = new Socket(ip, port);
	}

	private int[][] enviarArquivo() throws IOException, ClassNotFoundException {
		ObjectOutputStream output = new ObjectOutputStream(torreSocket.getOutputStream());
		output.writeObject(filePath);

	    ObjectInputStream input = new ObjectInputStream(torreSocket.getInputStream());
		int[][] lido = (int[][]) input.readObject();
		
		output.close();
		input.close();
		
		return lido;
	}
	
}
