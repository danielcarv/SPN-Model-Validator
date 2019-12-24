package server;

import java.io.IOException;
import java.util.List;


public class ConnectionServer extends Thread{
		private Integer port;
		private Server serv;
		//private List<String> ports_serv;

		public ConnectionServer(Server server, Integer port) {
		    this.serv = server;
		    this.port = port;
		}
		
		public void run() {
	    	try {
				serv.initServers(serv, port);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
