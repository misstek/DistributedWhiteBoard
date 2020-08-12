package server;

import java.rmi.AccessException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.UIManager;


import remote.RemoteMethods;

public class Server {
    private int port = 6667;
    private String hostname = "";
    public RemoteMethods remoteServant;




    public Server(){

    }


    public static void main(String[] args) {
        Server server = new Server();

        try{
            server.setHostname(args);
            server.setPort(args);

            server.createConnection();


	} catch (Exception e) {
	    e.printStackTrace();
	}

    }


    public boolean createConnection(){
        try{
	    System.setProperty("java.rmi.server.hostname", this.hostname.trim());

	    remoteServant = new RemoteServant();

	    Registry registry = LocateRegistry.createRegistry(this.port);

	    //Registry registry = LocateRegistry.getRegistry("localhost");
	    registry.rebind("WhiteBoard", remoteServant);


	    System.out.println(registry);
	    // ip and host for connection
	    System.out.println("Waiting for clients");
	    System.out.println("host: " + this.hostname);
	    System.out.println("port: " + this.port);
	    return true;
	} catch (AccessException e) {
	    e.printStackTrace();
	    return false;
	} catch (RemoteException e) {
	    e.printStackTrace();
	    return false;
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

    /**
     * check the port number and handle the exception
     * @param args
     */
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
