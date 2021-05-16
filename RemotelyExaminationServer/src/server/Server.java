package server;

import common.ExamInterface;
import common.StudentInterface;

import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class Server {

    //variable to change when start
    public static boolean started = false;


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
        Variables.setExamQuestions(saveExam(pathToCsv));

        //Semaphore for reading from terminal work while in background
        String start_word = "start";

        ExamImplementation exam = new ExamImplementation();
        Interrupt interrupt = new Interrupt(exam, start_word);


        try {
            startRegistry(null);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("Exam", exam);
            synchronized (exam) {
                while (true) {
                    //Professor indica quan comensar l'examen
                    if(exam.getNumStudents() == 0){
                        interrupt.start();
                            while(!started){
                                System.out.println("Numero de alumnes: " + exam.getNumStudents());
                                System.out.println("Escriu \""+ start_word+"\" per començar l'examen");
                                exam.wait();
                            }
                            System.out.println("Començant el examen");
                            exam.notifyStart();
                    }

                    List<StudentInterface> students = exam.getStudents();

                    for (StudentInterface s: students){
                        s.sendQuestions(Variables.getExamQuestions());
                    }

                    exam.notifyStart();
                    exam.wait();

                    //Acaba examen
                    while (true) {
                        System.out.println("Indica quan vulguis que l'examen s'acabi fent escribint: \"stop\" ");
                        Scanner scanner = new Scanner(System.in);
                        String x = scanner.nextLine();
                        if (x.equals("stop")) {
                            for (StudentInterface s: students){
                                exam.checkAnswers(s, s.getAnswers());
                            }
                        }
                    }

                    //FINISH
                }
            }

        } catch (AlreadyBoundException | InterruptedException e) {
            e.printStackTrace();
        }

        //Save marks on a csv file
        Map<Integer, Float> marks = exam.getMarks();

        File file = new File("../marks.csv");
        FileWriter myWriter = new FileWriter("../marks.csv");
        Set set = marks.entrySet();
        Iterator iterator = set.iterator();

        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            myWriter.write("Alumne: " + mentry.getKey() + "Nota: " + mentry.getValue());
        }
        myWriter.close();



    }

    //Professor upload the exam
    private static List<String> saveExam(String pathToCsv) throws IOException {
        //Save line by line the exam
        String line;
        List<String>  exam = new ArrayList<>();
        File file = new File(pathToCsv);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            exam.add(line);
        }

        return exam;
    }

    private static class Interrupt extends Thread {

        String interrupt_key;
        Object semaphore;

        //semaphore must be the syncronized object
        private Interrupt(Object semaphore, String interrupt_key){
            this.semaphore = semaphore;
            this.interrupt_key = interrupt_key;
        }

        public void run(){
            while (true) {
                //read the key
                Scanner scanner = new Scanner(System.in);
                String x = scanner.nextLine();
                if (x.equals(this.interrupt_key)) {
                    //if is the key we expect, change the variable, notify and return(finish thread)
                    synchronized (this.semaphore) {
                        started = true;
                        this.semaphore.notify();
                        return;
                    }
                }
            }
        }
    }
}







