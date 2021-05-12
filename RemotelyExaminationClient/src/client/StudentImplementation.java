package client;

import common.StudentInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Student extends UnicastRemoteObject implements StudentInterface {

    private String id;
    private String name;

    public Student(String id, String name) throws RemoteException {
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
}
