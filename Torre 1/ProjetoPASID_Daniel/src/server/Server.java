package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//import com.mysql.cj.result.IntegerValueFactory;

//import Controller.ControllerServer1;
import utils.Sort;
import utils.WordCount;

public class Server {
	private ServerSocket welcomeSokect;
	private Socket connectionSocket;
	private static Integer port;
	
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		System.err.println("SERVER");
		/*String port_server = "port_server1.properties";
		port = getPorts(port_server);
		Server serv = new Server();*/
		
		String number_ports = "port_server1.properties"; 
		List<Integer> ports = new ArrayList<Integer>();
		ports = getPorts(number_ports);
		
		List<Server> servs = new ArrayList<Server>();
		
		for(int z=0;z<ports.size();z++) {	
    		Server server = new Server();
    		servs.add(server);
    	}
		
		ConnectionServer t;
		for (int i=0;i<ports.size();i++) {
			t = new ConnectionServer(servs.get(i),ports.get(i));
			t.start();
		}
	}
	
	public void initServers(Server serv, Integer port2) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		//cleaning file
	    FileWriter writer1 = new FileWriter("times_server.txt", false);
    	writer1.close();
		
    	//time of execution
	    //long start = System.currentTimeMillis();
	    
    	while(true) {
    		//time to create request
        	//try { Thread.sleep (100); } catch (InterruptedException ex) {}
			serv.createConnection(port2);
			
			FileWriter writerr = new FileWriter("times_server.txt", true);
    	    PrintWriter printt = new PrintWriter(writerr);
    	    printt.print("Init Time = "+getTime());
			
			int[][] receiveFile = serv.receiveFile();
			
			//matriz of ordenation
			int result[][] = Sort.ordenation(receiveFile);
			
			serv.returnResultToClient(result);
			serv.welcomeSokect.close();
			
			printt.println("   Final Time = "+getTime());
		    writerr.close();
		    
		    //showing average after a certain time
		    /*if((System.currentTimeMillis() - start)>=300000) {
		    	getData(getDate1());
		    	start = System.currentTimeMillis();
		    }*/
		    
		}
	}
	
	
	private int[][] receiveFile() throws IOException, ClassNotFoundException {
		ObjectInputStream input = new ObjectInputStream(connectionSocket.getInputStream());
		int[][] inputFile = (int[][]) input.readObject();
		return inputFile;
	}

	private void createConnection(Integer port) throws IOException {
		welcomeSokect = new ServerSocket(port);
		System.out.println("Port "+port+" opened!");
		connectionSocket = welcomeSokect.accept();
		System.out.println("Server: "+port+" new connection with client: " + connectionSocket.getInetAddress().getHostAddress());
	}

	private void returnResultToClient(int[][] result) throws IOException {
		ObjectOutputStream output = new ObjectOutputStream(connectionSocket.getOutputStream());
		output.writeObject(result);
		output.flush();
		output.reset();
		output.close();
	}
	
	
	public static long getTime() {
		long Datetime;
	    Datetime = System.currentTimeMillis();
		return Datetime;
	}
	
	public static List<Integer> getPorts(String number_ports) throws NumberFormatException, IOException{
		List<Integer> ports = new ArrayList<Integer>();
		File file = new File(number_ports); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		  
		String st; 
		while ((st = br.readLine()) != null && st.contains(";")) {
			ports.add(Integer.parseInt(st.split(";")[0]));
	    }
	    return ports;
	}


}