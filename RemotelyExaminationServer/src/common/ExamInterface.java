package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ExamInterface extends Remote {
    void enterClass(StudentInterface student) throws RemoteException;
    void checkAnswers(StudentInterface student, List<String> answers) throws RemoteException;
}
