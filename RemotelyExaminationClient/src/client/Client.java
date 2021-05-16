package client;

import common.ExamInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) throws IOException {

        String host = (args.length < 1)? null: args[0];

        //CREATING STUDENT
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter a valid username:");
        String name = reader.readLine();
        System.out.println("Enter your ID card:");
        int id = Integer.parseInt(reader.readLine());
        StudentImplementation student = new StudentImplementation(name, id, 0);
        System.out.println(student.toString());

        try {

            //START CONNECTION
            Registry registry = LocateRegistry.getRegistry(host);
            ExamInterface exam = (ExamInterface) registry.lookup("Exam");
            exam.enterClass(student);
            System.out.println("Has entrat a classe, esperant a que el professor comensi el examen");
            synchronized (student){
                student.wait();
                System.out.println("COMENSA L'EXAMEN!!!");
                student.wait();
                student.answerQuestion();
                exam.checkAnswers(student, student.getAnswers());
                System.out .println("La nota de l'examen es " + student.getMark());
                System.exit(0);
            }
        } catch (ConnectException e){
            System.out.println("Ja s'ha acabat el temps donat de fer l'examen o t'has desconectat");
            System.out .println("La nota de l'examen es " + student.getMark());
            System.exit(1);
        } catch (RemoteException | NotBoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }




}
