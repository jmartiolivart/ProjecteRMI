package client;

import common.ExamInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) {

        String host = (args.length < 1)? null: args[0];

        try {

            //CREATING STUDENT
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter a valid username:");
            String name = reader.readLine();
            System.out.println("Enter your ID card:");
            String id = reader.readLine();
            StudentImplementation student = new StudentImplementation(name, id);
            System.out.println(student.toString());

            //START CONNECTION
            Registry registry = LocateRegistry.getRegistry(host);
            ExamInterface exam = (ExamInterface) registry.lookup("Exam");
            exam.enterClass(student);
            System.out.println("Has entrat a classe, esperant a que el professor comensi el examen");
            synchronized (student){
                student.wait();
                System.out.println("COMENSA L'EXAMEN!!!");
                student.wait();

            }
            //Enter class
            //While professor no diu comensar examen
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
