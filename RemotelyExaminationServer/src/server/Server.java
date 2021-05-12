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
    public static List<String> examQuestions;


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
        examQuestions = saveExam(pathToCsv);

        //Semaphore for reading from terminal work while in background
        String start_word = "start";

        ExamInterface exam = new ExamImplementation();
        Interrupt interrupt = new Interrupt(exam, start_word);

        List<String> answers = null;
        float mark;

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
                    //FER QUE ENTRIN MES D'UN A LA VEGADA
                    for (StudentInterface s: students){
                        //ES QUEDA TRAVAT PERQUE ESPERA RESPOSTA PER CONTINUAR BUCLE
                        //FUNCIO PER AGAFAR RESPOSTES QUE ES GUARDARAN AL CLIENT
                        s.sendQuestions(examQuestions);
                    }

                    for (StudentInterface s: students){
                        mark = checkAnswers(answers);
                        s.sendMark(mark);
                    }

                    exam.wait();
                    //FINISH
                }
            }
        } catch (AlreadyBoundException | InterruptedException e) {
            e.printStackTrace();
        }

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

    private static float checkAnswers(List<String> answers){

        String questionAnswer, currentAnswer;
        int examMark = 0;

        for (int i = 0; i < answers.size() - 1; i++){
            questionAnswer = examQuestions.get(i);
            currentAnswer = questionAnswer.substring(questionAnswer.lastIndexOf(";") + 1, questionAnswer.length() - 1);


            if(currentAnswer.equals(answers.get(i))){
                examMark++;
            }
        }
       return ((float) examMark/(answers.size()-1)) * 10;
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







