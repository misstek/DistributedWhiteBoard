package client;


import remote.RemoteMethods;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import whiteboard.*;

public class Client {
    private String hostname;
    private int port;
    public String username;
    public RemoteMethods remoteMethods;

    public Client(String[] args){
        setPort(args);
        setHostname(args);
        setUsername(args);

    }

    public static void main(String[] args) {
	Client client = new Client(args);

	try {



	    client.createConnection();




	} catch (Exception e) {
	    System.out.println("RMIConnection failed");
	    e.printStackTrace();
	}



    }


    public boolean createConnection(){
	try {

	    Registry registry;
	    if(this.hostname.equals("localhost")){
		registry = LocateRegistry.getRegistry(this.port);
		//System.out.println(registry);
	    }
	    else {
		registry = LocateRegistry.getRegistry(this.hostname, this.port);
		System.out.println("1");
	    }

	    System.out.println("port number: "+this.port);

	    this.remoteMethods = (RemoteMethods) registry.lookup("WhiteBoard");

	    return true;

	} catch (Exception e) {
	    System.out.println("RMIConnection failed");
	    e.printStackTrace();
	    return false;
	}
    }




    public void setUsername(String[] args){
        try{
            username = args[2];
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }


    public void setHostname(String[] args){
	try{
	    hostname = args[0];
	} catch (Exception e) {
	    System.out.println("Wrong hostname");
	    e.printStackTrace();
	}
    }




    public void setPort(String[] args){
	try{
	    port = Integer.parseInt(args[1]);
	    if(port>65535||port<=0){
		System.out.println("Wrong port number argument and use default port:6667");
		port = 6667;
	    }
	}
	catch (ArrayIndexOutOfBoundsException e){
	    System.out.println("No port number argument and use default port:6667");
	    port = 6667;
	    e.printStackTrace();
	}
	catch(NumberFormatException e){
	    System.out.println("Wrong port number argument and use default port:6667");
	    port = 6667;
	    e.printStackTrace();
	}

    }
}
