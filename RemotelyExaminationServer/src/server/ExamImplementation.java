package server;

import common.ExamInterface;
import common.StudentInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ExamImplementation extends UnicastRemoteObject implements ExamInterface {

    List<StudentInterface> studentList = new ArrayList<StudentInterface>();
    boolean started = false;

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
    public synchronized void enterClass(StudentInterface student) throws RemoteException {
        if(!started){
            this.studentList.add(student);
            this.notify();
        }else {
            student.notifyExamStarted();
        }
    }

    @Override
    public void notifyStart() throws RemoteException {
        //Classe ja ha comensat ningu més es pot unir
        started = true;

        for( StudentInterface student: studentList){
            student.notifyStart();
        }
    }

    public List<StudentInterface> getStudents(){
        return studentList;
    }


}
