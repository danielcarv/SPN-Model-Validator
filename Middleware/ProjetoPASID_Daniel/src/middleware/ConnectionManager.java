package middleware;

import java.io.IOException;
import java.util.List;

public class ConnectionManager extends Thread{
	private Integer port;
	private int port_;
	private String ip;
	private MiddewareManager mid;
	//private List<String> ports_serv;

	public ConnectionManager(MiddewareManager mid, Integer port, String ip, int port_) {
	    this.mid = mid;
	    this.port = port;
	    this.ip = ip;
	    this.port_ = port_;
	}
	
	public void run() {
	    try {
			mid.initMids(mid, port, ip, port_);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
