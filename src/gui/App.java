package gui;

import application.controller.Controller;
import storage.ListStorage;
import storage.Storage;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Storage storage = loadStorage();
        if (storage == null) {
            storage = new ListStorage();
        }
        Controller.setStorage(storage);
        var b1 = Controller.createBatch("Test", 2.2);
        var b2 = Controller.createBatch("Bøv", 9.2);
        var b3 = Controller.createBatch("Bæv", 3.2);
        var f1 = Controller.createFad(2.2, "Søren", "Eg", false);
        Controller.registrerPåfyldning(
                LocalDateTime.now(),
                0.6,
                List.of(b1, b2, b3),
                List.of(f1)
        );


        System.out.println("=================");
        System.out.println("Batches");
        Controller.getBatches().forEach(System.out::println);
        System.out.println("=================");
        System.out.println("Fade");
        Controller.getFade().forEach(System.out::println);
        System.out.println("=================");
        System.out.println("Destillater");
        Controller.getDestillater().forEach(System.out::println);
        System.out.println("=================");

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
