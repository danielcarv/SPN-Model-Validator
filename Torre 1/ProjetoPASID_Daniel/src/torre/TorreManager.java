package torre;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TorreManager {
	private ServerSocket welcomeSokect;
	private Socket connectionSocket;
	//private Integer port = 12345;
	//private Integer discard = 0;
	
	public static void main(String[] args) throws IOException {
		System.err.println("TORRE");
		
		String number_ports = "ports_torres.properties"; 
		List<Integer> ports = new ArrayList<Integer>();
		ports = getPorts(number_ports);
		
		String properties = "server_ips.properties";
		List<String> ports_server = new ArrayList<String>();
		ports_server = getPortsServer(properties);
		
		List<TorreManager> torres = new ArrayList<TorreManager>();
	    List<Integer> ports_ = new ArrayList<Integer>();
	    List<String> ips = new ArrayList<String>();
	    
	    
	    for(int i=0;i<ports_server.size();i++) {
	    	TorreManager torre = new TorreManager();
	    	torres.add(torre);
		}
	    
	    
	    for(int z=0;z<ports_server.size();z+=2) {	
    		ips.add(ports_server.get(z+1));
    		ports_.add(Integer.parseInt(ports_server.get(z)));
    	}
	    
		ConnectionTorre t;
		for (int i=0;i<ports.size();i++) {
			t = new ConnectionTorre(torres.get(i),ports.get(i),ips.get(i), ports_.get(i));
			t.start();
		}
		
	}
	
	public void initTorre(TorreManager torr, Integer port, String ip, int port_) throws IOException, ClassNotFoundException {
		int result[][] = null;
		
		//cleaning file
	    FileWriter writer1 = new FileWriter("times_torre.txt", false);
    	writer1.close();
		
    	//cleaning file
	    FileWriter writer2 = new FileWriter("discards_torre1.txt", false);
	    PrintWriter print2 = new PrintWriter(writer2);
	    print2.print("0");
	    writer2.close();
    	int ver = 0;
    	while(true) {

			String discards = "discards_torre1.txt";
			
			 //number of discards
		    int discard = getDiscard(discards);
		    
		    torr.createConnection(port);
			
			FileWriter writerr = new FileWriter("times_torre.txt", true);
    	    PrintWriter printt = new PrintWriter(writerr);
    	    long init=getTime();
    	    printt.print("Init Time = "+getTime());
			
			int[][] receivefile = torr.receiveFile();
			
        	Torre torre_serv = new Torre(ip,port_,receivefile);
        	try {
        		result = torre_serv.execute();
			} catch (ConnectException e) {
				// TODO: handle exception
			}
        	  
        	torr.returnResultToClient(result);
        	torr.welcomeSokect.close();
		    
		    FileWriter writer = new FileWriter("discards_torre1.txt");
		    PrintWriter print = new PrintWriter(writer);
		    print.print(discard);
		    writer.close();
		    
		    printt.println("   Final Time = "+getTime()+" Total Time = "+ (getTime()-init));
		    writerr.close();
		    
		    ver+=2;
		}
	}
	
	private void createConnection(Integer port) throws IOException {
		welcomeSokect = new ServerSocket(port);
		System.out.println("Port "+port+" opened!");
		connectionSocket = welcomeSokect.accept();
	}
	
	private int[][] receiveFile() throws IOException, ClassNotFoundException {
		ObjectInputStream input = new ObjectInputStream(connectionSocket.getInputStream());
		int[][] result = (int[][]) input.readObject();
		return result;
	}
	
	public static List<Torre> getMids(int[][] filePath,String propertiesFile) throws IOException {
		List<Torre> mids = new ArrayList<Torre>();
		File file = new File(propertiesFile); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		  
		String st; 
		while ((st = br.readLine()) != null && st.contains(";")) {
			  String port = st.split(";")[1];
			Integer p = new Integer(port);
			mids.add(new Torre(st.split(";")[0], p,filePath));
	    }
	  
	    return mids;
    }
	
	private void returnResultToClient(int[][] result) throws IOException {
		ObjectOutputStream output = new ObjectOutputStream(connectionSocket.getOutputStream());
		output.writeObject(result);
		output.flush();
		output.reset();
		output.close();
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
	
	public static List<String> getPortsServer(String propertiesFile) throws IOException {
		List<String> ports = new ArrayList<String>();
		File file = new File(propertiesFile); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		  
		String st; 
		while ((st = br.readLine()) != null && st.contains(";")) {
			ports.add(st.split(";")[1]);
			ports.add(st.split(";")[0]);
		}
		return ports;
	}
	
	public static long getTime() {
		long Datetime;
	    Datetime = System.currentTimeMillis();
		return Datetime;
	}
	
	public static Integer getDiscard(String propertiesFile) throws IOException {
		File file = new File(propertiesFile); 
		BufferedReader br = new BufferedReader(new FileReader(file));  
		int numero =0;
		String st; 
		while ((st = br.readLine()) != null) {
			numero = Integer.parseInt(st.split(" ")[0]);
	    }
	    return numero;
	}
}
