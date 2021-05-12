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

    private String id;
    private String name;
    private List<String> answers = new ArrayList<String>();


    public StudentImplementation(String id, String name) throws RemoteException {
        super();
        this.id = id;
        this.name = name;
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
    public void sendQuestions(List<String> examQuestions) throws RemoteException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;

        try {
                for (int i = 0; i < examQuestions.size(); i++){
                    line = examQuestions.get(i);
                    System.out.println(line.substring(0, line.lastIndexOf(";")));
                    answers.add(reader.readLine());
                }

        } catch (IOException e) {
                e.printStackTrace();
        }
    }



    @Override
    public void sendMark(float mark) throws RemoteException {
        System.out.println("La nota del examen fet es: " + mark);
    }
}
