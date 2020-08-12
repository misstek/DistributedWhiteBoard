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

public class UserClient {
    private Client client;
    private boolean firstFlag = true;
    private boolean uniqueUsernameFlag = false;
    private boolean requestFlag=false;
    private boolean selfRequestFlag = false;
    private WhiteBoardUser whiteboard;

    private Thread uploadImage;
    private Thread downloadImage;
    private Thread updataUserlist;

    public UserClient(String[] args) {
	this.client = new Client(args);
    }


    public static void main(String[] args) {
	UserClient userClient = new UserClient(args);

	userClient.client.createConnection();
	//userClient.client.remoteMethods.addNewUsername(userClient.client.username);

	userClient.whiteboard = new WhiteBoardUser(userClient.client);
	userClient.whiteboard.setVisible(true);

	//userClient.client.remoteMethods.setRequest(true);
	//userClient.client.remoteMethods.setRequestUsername(userClient.client.username);

	userClient.whiteboard.downloadFlag = false;
	userClient.initiateThread();


    }


    public void initiateThread(){
	try{
	    downloadImage = new Thread(new DownloadImage());
	    downloadImage.start();

	    uploadImage = new Thread(new UploadImage());
	    uploadImage.start();

	    updataUserlist = new Thread(new UpdateUserList());
	    updataUserlist.start();

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }



    class UploadImage implements Runnable{
	@Override
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
	@Override
	public void run() {
	    try {
		while(true) {
		    Thread.sleep(1000);
		    if(whiteboard.uploadFlag==false && whiteboard.downloadFlag==true){
			InputStream input = new ByteArrayInputStream(client.remoteMethods.downloadImage());
			BufferedImage downloadedImage = ImageIO.read(input);
			input.close();
			whiteboard.setCanvas(downloadedImage);
		    }
		}

	    } catch (RemoteException e) {
		e.printStackTrace();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    } catch (NullPointerException e) {
		e.printStackTrace();
	    }
	}
    }


    class UpdateUserList implements Runnable{
	public void run() {
	    try {
		while(true) {
		    if(firstFlag){
			Thread.sleep(500);
			if(client.remoteMethods.getManagerState()==false){
			    JOptionPane.showMessageDialog(whiteboard,"There is no manager, please close the application.");
			    System.exit(0);
			}
			else if(!requestFlag){
			    if(client.remoteMethods.getRequest()==true){
				JOptionPane.showMessageDialog(whiteboard,"Other user is connecting the whiteboard, please wait in line");
			    }
			    else {

				requestFlag=true;
			    }
			}
		        else if(!uniqueUsernameFlag){
			    if(!client.remoteMethods.isExist(client.username)){

				uniqueUsernameFlag=true;
			    }
			    else {
				JOptionPane.showMessageDialog(whiteboard,"There exists the username, please try another name.");
				System.exit(0);
			    }
			}
		        else if(!selfRequestFlag){
			    client.remoteMethods.setRequest(true);
			    client.remoteMethods.setRequestUsername(client.username);
			    selfRequestFlag=true;
			}
		        else {
			    if(client.remoteMethods.getReply()==0){
				JOptionPane.showMessageDialog(whiteboard,"Waiting for permission...");
			    }
			    else if(client.remoteMethods.getReply()==1){
				firstFlag=false;
				client.remoteMethods.addNewUsername(client.username);
				client.remoteMethods.setReply(0);
				client.remoteMethods.setRequestUsername("");
				whiteboard.downloadFlag = true;
			    }
			    else if(client.remoteMethods.getReply()==2){
				JOptionPane.showMessageDialog(whiteboard,"You are denied.");
				//int answer = JOptionPane.showConfirmDialog(whiteboard,"You are denied.");
				client.remoteMethods.setReply(0);
				client.remoteMethods.removeUsername(client.username);
				client.remoteMethods.setRequestUsername("");
				System.exit(0);
			    }
			}
		    }
		    else {
			Thread.sleep(1000);

			if(client.remoteMethods.getKickUsername().equals(client.username)){
			    client.remoteMethods.kickUser("");
			    whiteboard.downloadFlag=false;
			    JOptionPane.showMessageDialog(whiteboard,"You have been kicked out by manager");
			    //client.remoteMethods.removeUsername(client.username);
			    System.exit(0);
			}
			if(client.remoteMethods.getManagerState()==false){
			    JOptionPane.showMessageDialog(whiteboard,"The application has been closed by manager");
			    System.exit(0);
			}
			ArrayList<String> userListImported = client.remoteMethods.getUserList();
			userListImported.set(userListImported.indexOf(client.username), client.username + " (you)");
			String[] displayUserList = (String[])userListImported.toArray(new String[0]);

			if(whiteboard.userList.getModel().getSize() != displayUserList.length) {
			    whiteboard.userList.setListData(displayUserList);
			}
			//whiteboard.userList.setListData(displayUserList);
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
