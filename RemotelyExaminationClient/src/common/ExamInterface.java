package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ExamInterface extends Remote {
    public int getNumStudents() throws RemoteException;
    public void enterClass(StudentInterface student) throws RemoteException;
    public void notifyStart() throws RemoteException;
    public List<StudentInterface> getStudents() throws RemoteException;
    public void checkAnswers(StudentInterface student, List<String> answers) throws RemoteException;
}
