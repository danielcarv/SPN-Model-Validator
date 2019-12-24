package torre;

import java.io.IOException;
import java.util.List;

public class ConnectionTorre extends Thread{
	private Integer port;
	private int port_;
	private String ip;
	private TorreManager torre;
	//private List<String> ports_serv;

	public ConnectionTorre(TorreManager torre, Integer port, String ip, int port_) {
	    this.torre = torre;
	    this.port = port;
	    this.ip = ip;
	    this.port_ = port_;
	}
	
	public void run() {
	    try {
	    	torre.initTorre(torre, port, ip, port_);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
