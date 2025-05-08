package gui;

import application.controller.Controller;
import javafx.application.Application;
import storage.ListStorage;
import storage.Storage;
import java.io.*;

public class App{
    public static void main(String[] args) {

        Storage storage = loadStorage();
        if (storage == null) {
            storage = new ListStorage();
        }
        Controller.setStorage(storage);
        Application.launch(Main.class);
        saveStorage(storage);
    }

    public static Storage loadStorage() {
        String fileName = "storage.ser";
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream objIn = new ObjectInputStream(fileIn)
        ) {
            Object obj = objIn.readObject();
            Storage storage = (ListStorage) obj;
            System.out.println("Storage loaded from file " + fileName);
            return storage;
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error deserializing storage");
            System.out.println(ex);
            return null;
        }
    }

    public static void saveStorage(Storage storage) {
        String fileName = "storage.ser";
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream objOut = new ObjectOutputStream(fileOut)
        ) {
            objOut.writeObject(storage);
            System.out.println("Storage saved in file " + fileName);
        } catch (IOException ex) {
            System.out.println("Error serializing storage");
            System.out.println(ex);
            throw new RuntimeException();
        }
    }
}
