package client;

import common.Exam;
import common.Student;

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
            Student student = new Student(name, id);
            System.out.println(student.toString());

            //START CONNECTION
            Registry registry = LocateRegistry.getRegistry(host);
            Exam exam = (Exam) registry.lookup("Exam");
            exam.enterClass(student);
            System.out.println("Has entrat a classe, esperant a que el professor comensi el examen");
            synchronized (student){
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
