package middleware;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MiddewareManager {
	private ServerSocket welcomeSokect;
	private Socket connectionSocket;
	
	public static void main(String[] args) throws IOException {
		System.err.println("MIDDLEWARE");
		
		String number_ports = "ports.properties"; 
		List<Integer> ports = new ArrayList<Integer>();
		ports = getPorts(number_ports);
		
		String properties = "torres_ips.properties";
		List<String> ports_server = new ArrayList<String>();
		ports_server = getPortsServer(properties);
		
		List<MiddewareManager> mids = new ArrayList<MiddewareManager>();
	    List<Integer> ports_ = new ArrayList<Integer>();
	    List<String> ips = new ArrayList<String>();
	    
	    
	    for(int i=0;i<ports_server.size();i++) {
			MiddewareManager mid = new MiddewareManager();
			mids.add(mid);
		}
	    
	    
	    for(int z=0;z<ports_server.size();z+=2) {	
    		ips.add(ports_server.get(z+1));
    		ports_.add(Integer.parseInt(ports_server.get(z)));
    	}

		ConnectionManager t;
		for (int i=0;i<ports.size();i++) {
			t = new ConnectionManager(mids.get(i),ports.get(i),ips.get(i), ports_.get(i));
			t.start();
		}
		
	}
	
	public void initMids(MiddewareManager mid, Integer port, String ip, int port_) throws IOException, ClassNotFoundException {
		int result[][] = null;
		
		//cleaning file
	    FileWriter writer1 = new FileWriter("times_middleware.txt", false);
    	writer1.close();
		
    	//cleaning file
	    /*FileWriter writer2 = new FileWriter("discards_middlware.txt", false);
	    PrintWriter print2 = new PrintWriter(writer2);
	    print2.print("0");
	    writer2.close();*/
    	int ver = 0;
    	while(true) {

			//String discards = "discards_middlware.txt";
			
			 //number of discards
		    //int discard = getDiscard(discards);
		    
			mid.createConnection(port);
			
			FileWriter writerr = new FileWriter("times_middleware.txt", true);
    	    PrintWriter printt = new PrintWriter(writerr);
    	    long init = getTime();
    	    printt.print("Init Time = "+getTime());
			
			int[][] receivefile = mid.receiveFile();
			
        	Middleware mid_serv = new Middleware(ip,port_,receivefile);
        	try {
        		result = mid_serv.execute();
			} catch (ConnectException e) {
				System.err.println("Descarte");
			}
        	  
        	mid.returnResultToClient(result);
		    mid.welcomeSokect.close();
		    
		    /*FileWriter writer = new FileWriter("discards_middlware.txt");
		    PrintWriter print = new PrintWriter(writer);
		    print.print(discard);
		    writer.close();*/
		    
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
	
	public static List<Middleware> getMids(int[][] filePath,String propertiesFile) throws IOException {
		List<Middleware> mids = new ArrayList<Middleware>();
		File file = new File(propertiesFile); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 
		  
		String st; 
		while ((st = br.readLine()) != null && st.contains(";")) {
			  String port = st.split(";")[1];
			Integer p = new Integer(port);
			mids.add(new Middleware(st.split(";")[0], p,filePath));
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
}
