package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RemoteMethods extends Remote{
    public int test(int a, int b) throws RemoteException;
    public void createWhiteBoard()throws RemoteException;
    public byte[] downloadImage() throws RemoteException;
    public void uploadImage(byte[] imageBytes) throws RemoteException;
    public void removeUsername(String username) throws RemoteException;
    public ArrayList<String> getUserList()throws RemoteException;
    public void addNewUsername(String username) throws RemoteException;
    public void setRequest(boolean request)throws RemoteException;
    public boolean getRequest()throws RemoteException;
    public void setReply(int reply)throws RemoteException ;
    public int getReply()throws RemoteException;
    public void setRequestUsername(String requestUsername)throws RemoteException;
    public String getRequestUsername()throws RemoteException;
    public boolean isExist(String username) throws  RemoteException;
    public boolean getManagerState()throws RemoteException;

    public void setManagerState(boolean managerState) throws RemoteException;
    public void clearUserlist()throws RemoteException;
    public String getKickUsername() throws RemoteException;

    public void kickUser(String username) throws RemoteException;
}
