package client;


import common.StudentInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class StudentImplementation extends UnicastRemoteObject implements StudentInterface {

    private final int id;
    private final String name;
    private float mark;

    private List<String> answers = new ArrayList<String>();
    public static List<String> examQuestions;


    public StudentImplementation( String name, int id, float mark) throws RemoteException {
        super();
        this.id = id;
        this.name = name;
        this.mark = mark;
    }

    @Override
    public String toString() {
        return this.name + " " + this.id;
    }

    @Override
    public void notifyStart() throws RemoteException {
        System.out.println("L'examen esta a punt de començar");
        synchronized (this){
            this.notify();
        }
    }

    @Override
    public void notifyExamStarted() throws RemoteException {
        System.out.println("Ho sentim, l'examen ja ha comensat contacta amb el tutor per" +
                "veure les alternatives\n");
        exit(0);

    }

    @Override
    public void sendQuestions(List<String> examQuestions) throws RemoteException{
        StudentImplementation.examQuestions = examQuestions;
    }

    @Override
    public void sendMark(float mark) throws RemoteException {
        System.out.println("La nota del examen fet es: " + mark);
    }

    @Override
    public List<String> getAnswers() throws RemoteException {
        return this.answers;

    }

    @Override
    public void putMark(float mark) throws RemoteException {
        this.mark = mark;
    }

    @Override
    public float getMark() throws RemoteException {
        return this.mark;
    }

    public List<String> getExamQuestions()  {
        return examQuestions;
    }

    public void answerQuestion() {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> answers = new ArrayList<>();
        String line;

        try {
            for (int i = 0; i < examQuestions.size(); i++){
                line = examQuestions.get(i);
                System.out.println(line.substring(0, line.lastIndexOf(";")));
                answers.add(reader.readLine());
                this.answers = answers; //Guardo per si el client/estudiant es desconectes
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.answers = answers;
    }

    public int getId() throws RemoteException{
        return id;
    }
}
