package common;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StudentInterface extends Remote {
    public void notifyStart() throws RemoteException;

}
