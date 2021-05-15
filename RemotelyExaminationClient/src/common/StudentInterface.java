package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface StudentInterface extends Remote {
    public void notifyStart() throws RemoteException;
    public void notifyExamStarted() throws RemoteException;
    public void sendQuestions(List<String> examQuestions) throws RemoteException;
    public void sendMark(float mark) throws RemoteException;
    public List<String> getAnswers() throws RemoteException;
    public void putMark(float mark) throws RemoteException;
    public float getMark() throws RemoteException;
    public int getId() throws RemoteException;
}
