package client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;

import whiteboard.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ManagerClient {
    private Client client;

    private Thread uploadImage;
    private Thread downloadImage;
    private Thread updataUserlist;
    private WhiteBoard whiteboard;

    public ManagerClient(String[] args) {
	this.client = new Client(args);
    }


    public static void main(String[] args) {
	ManagerClient managerClient = new ManagerClient(args);
	try{
	    managerClient.client.createConnection();
	    managerClient.client.remoteMethods.addNewUsername(managerClient.client.username);
	    managerClient.client.remoteMethods.setManagerState(true);

	    managerClient.client.remoteMethods.createWhiteBoard();

	    managerClient.whiteboard = new WhiteBoard(managerClient.client);
	    managerClient.whiteboard.setVisible(true);
	    managerClient.uploadOnce();
	    managerClient.initiateThread();

	} catch (RemoteException e) {
	    e.printStackTrace();
	}

    }


    public void initiateThread(){
	try{
	    uploadImage = new Thread(new UploadImage());
	    uploadImage.start();

	    downloadImage = new Thread(new DownloadImage());
	    downloadImage.start();

	    updataUserlist = new Thread(new UpdateUserList());
	    updataUserlist.start();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }



    public void uploadOnce(){
	try{

	    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
	    ImageIO.write(whiteboard.image, "jpg", byteOutput);
	    byteOutput.flush();
	    byte[] imageBytes = byteOutput.toByteArray();
	    byteOutput.close();
	    client.remoteMethods.uploadImage(imageBytes);
	} catch (RemoteException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    class UploadImage implements Runnable{
	public void run() {
	    try {
		while(true) {
		    Thread.sleep(200);

		    if(whiteboard.uploadFlag==true){
			whiteboard.downloadFlag=false;
			ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			ImageIO.write(whiteboard.image, "jpg", byteOutput);
			byteOutput.flush();
			byte[] imageBytes = byteOutput.toByteArray();
			byteOutput.close();
			client.remoteMethods.uploadImage(imageBytes);
			whiteboard.downloadFlag=true;
			whiteboard.uploadFlag=false;
		    }
		}
	    } catch (RemoteException e) {
		e.printStackTrace();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }catch (IOException e) {
		e.printStackTrace();
	    } catch (NullPointerException e) {
		e.printStackTrace();
	    }
	}
    }


    class DownloadImage implements Runnable{

	public void run() {
	    try {
		while(true) {
		    Thread.sleep(1000);
		    if(whiteboard.uploadFlag==false && whiteboard.downloadFlag==true){
			InputStream input = new ByteArrayInputStream(client.remoteMethods.downloadImage());
			BufferedImage downloadedImage = ImageIO.read(input);
			input.close();
			whiteboard.updateImage(downloadedImage);
		    }
		}
	    } catch (RemoteException e) {
		e.printStackTrace();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }catch (IOException e) {
		e.printStackTrace();
	    } catch (NullPointerException e) {
		e.printStackTrace();
	    }
	}
    }


    class UpdateUserList implements Runnable{
	@Override
	public void run() {
	    try {
		while(true) {
		    Thread.sleep(1000);
		    if(client.remoteMethods.getRequest()){
		        String requestUsername = client.remoteMethods.getRequestUsername();
			int answer=JOptionPane.showConfirmDialog(whiteboard, "Do you allow user: "+requestUsername+" to connect?", "Warning", JOptionPane.YES_NO_OPTION);
			if(answer==0){
			    client.remoteMethods.setReply(1);
			}
			else if(answer==1){
			    client.remoteMethods.setReply(2);
			}
			client.remoteMethods.setRequest(false);
		    }

		    ArrayList<String> userListImported = client.remoteMethods.getUserList();
		    userListImported.set(userListImported.indexOf(client.username), client.username + " (you)");
		    String[] displayUserList = (String[])userListImported.toArray(new String[0]);

		    if(whiteboard.userList.getModel().getSize() != displayUserList.length) {
			whiteboard.userList.setListData(displayUserList);
		    }

		}
	    } catch (NullPointerException e) {
		e.printStackTrace();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    } catch (RemoteException e) {
		e.printStackTrace();
	    }
	}
    }



}
