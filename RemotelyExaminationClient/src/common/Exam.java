package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Exam extends Remote {
    public int getNumStudents() throws RemoteException;
    public void enterClass(Student student) throws RemoteException;


}
