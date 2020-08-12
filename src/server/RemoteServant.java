package server;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


//import server.DrawingBoardMonitor;

import remote.RemoteMethods;


public class RemoteServant extends UnicastRemoteObject implements RemoteMethods{

    int canvasWidth = 1024;
    int canvasHeight = 1024;
    int downloadTime = 0;
    int uploadTime = 0;

    private BufferedImage image;
    private ArrayList<String> userList = new ArrayList<String>();
    private boolean request = false;
    private int reply = 0;
    private String requestUsername = "";
    private boolean managerState = false;
    private String kickUsername = "";


    public RemoteServant() throws RemoteException{
        ;
    }

    public String getKickUsername() throws RemoteException{
        return this.kickUsername;
    }

    public void kickUser(String username) throws RemoteException{
        this.kickUsername = username;
    }

    public void clearUserlist()throws RemoteException{
        this.userList.clear();
    }

    public boolean getManagerState()throws RemoteException{
        return managerState;
    }

    public void setManagerState(boolean managerState) throws RemoteException{
        this.managerState = managerState;
    }

    public void setRequest(boolean request)throws RemoteException {
	this.request = request;
    }

    public boolean getRequest()throws RemoteException{
        return this.request;
    }

    public void setReply(int reply)throws RemoteException {
	this.reply = reply;
    }

    public int getReply()throws RemoteException{
        return this.reply;
    }

    public void setRequestUsername(String requestUsername)throws RemoteException {
	this.requestUsername = requestUsername;
    }

    public String getRequestUsername()throws RemoteException{
        return this.requestUsername;
    }

    public void addNewUsername(String username) throws RemoteException {
	this.userList.add(username);
    }

    public boolean isExist(String username) throws  RemoteException{
        return userList.contains(username);
    }

    public ArrayList<String> getUserList()throws RemoteException{
        return this.userList;
    }

    public void removeUsername(String username) throws RemoteException{
        this.userList.remove(username);
    }



    public void createWhiteBoard() throws RemoteException{
	this.image = new BufferedImage( canvasWidth, canvasHeight, BufferedImage.TYPE_INT_BGR);
    }

    public byte[] downloadImage() throws RemoteException{
	try {
	    // Convert the buffered image to bytes and return
	   // BufferedImage outputImg = this.image;
	    //byte[] imageBytes = null;
	    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
	    ImageIO.write(this.image, "jpg", byteOutput);
	    byteOutput.flush();
	    byte[] imageBytes = byteOutput.toByteArray();
	    byteOutput.close();

	    downloadTime ++;
	    System.out.println("download times: "+downloadTime);

	    return imageBytes;
	} catch (IOException e) {
	    System.out.println("Download Falied");
	    e.printStackTrace();
	    return null;
	}
    }


    public void uploadImage(byte[] imageBytes) throws RemoteException{
	try {
	    // Convert to buffer image
	    InputStream byteInput = new ByteArrayInputStream(imageBytes);
	    BufferedImage imputImage = ImageIO.read(byteInput);
	    byteInput.close();

	    this.image = imputImage;
	    uploadTime ++;
	    System.out.println("upload times: "+uploadTime);

	} catch (IOException e) {
	    System.out.println("uploadImage falied");
	    e.printStackTrace();
	}
    }


    @Override
    public int test(int a, int b) throws RemoteException {
	return a+b;
    }
}
