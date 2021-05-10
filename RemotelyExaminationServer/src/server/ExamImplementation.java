package server;

import common.Exam;
import common.Student;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ExamImplementation extends UnicastRemoteObject implements Exam {

    List<Student> studentList = null;

    public ExamImplementation() throws RemoteException {
        super();


    }

    @Override
    public int getNumStudents() throws RemoteException {
        if (studentList == null){
            return 0;
        }
        return studentList.size();
    }

    //Afegeix i avisa de nou alumne
    @Override
    public synchronized void enterClass(Student student) throws RemoteException {
        this.studentList.add(student);
        this.notify();
    }
}
