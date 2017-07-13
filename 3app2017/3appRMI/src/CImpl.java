import com.sun.org.apache.regexp.internal.RE;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/**
 * Created by aless on 11/07/2017.
 */
interface ScacchieraInterface extends Remote{

    public static final int NRIGHE = 8;
    public static final int NCOLONNE = 8;
    public int add(int r, int c)throws RemoteException;
    public void swap()throws RemoteException;
    public void trySwap()throws RemoteException;
    public void stsampaFine() throws RemoteException;
}
