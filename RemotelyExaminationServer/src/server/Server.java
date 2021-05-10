package server;

import common.Exam;

import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Server {


    private static Registry startRegistry(Integer port) throws RemoteException {
        if (port == null) {
            port = 1099;
        }

        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.list();
            return registry;

        } catch (RemoteException exception) {
            System.out.println("RMI registry cannot be located");
            Registry registry = LocateRegistry.createRegistry(port);
            System.out.println("RMI registry created at port ");
            return registry;
        }
    }


    public static void main(String[] args) throws IOException {

        String pathToCsv = "../exam.csv";
        String[] examQuestions = saveExam(pathToCsv);

        Exam exam = new ExamImplementation();

        try {
            startRegistry(null);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Exam", exam);
            synchronized (exam) {
                while (true) {
                    System.out.println(exam.getNumStudents());
                    exam.wait();
                }
            }
        } catch (AlreadyBoundException | InterruptedException e) {
            e.printStackTrace();
        }


        try {

        } catch (Exception e) {

        }
    }

    //Professor upload the exam
    private static String[] saveExam(String pathToCsv) throws IOException {
        //Save line by line the exam
        String line;
        String[] exam = new String[50];
        File file = new File(pathToCsv);
        Scanner scanner = new Scanner(file);

        for (int i = 0; scanner.hasNextLine(); i++) {
            line = scanner.nextLine();
            exam[i] = line;
            i++;
        }

        return exam;
    }
}
