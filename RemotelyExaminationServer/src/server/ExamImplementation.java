package server;

import common.ExamInterface;
import common.StudentInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExamImplementation extends UnicastRemoteObject implements ExamInterface {

    List<StudentInterface> studentList = new ArrayList<StudentInterface>();
    boolean started = false;
    Map<Integer,Float> marks;

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

    @Override
    public void checkAnswers(StudentInterface student, List<String> answers) throws RemoteException {
        synchronized (this) {
            String questionAnswer, currentAnswer;
            int examMark = 0;

            for (int i = 0; i < answers.size() - 1; i++) {
                questionAnswer = Variables.getExamQuestions().get(i);
                currentAnswer = questionAnswer.substring(questionAnswer.lastIndexOf(";") + 1, questionAnswer.length() - 1);


                if (currentAnswer.equals(answers.get(i))) {
                    examMark++;
                }
            }
            //Guardo al professor/servidor totes les notes
            //float finalMark = (((float) examMark / (answers.size() - 1)) * 10);
            //marks.put(student.getId(), finalMark);

            student.putMark((((float) examMark / (answers.size() - 1)) * 10));
        }
    }
}
