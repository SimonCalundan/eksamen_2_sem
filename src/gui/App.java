package gui;

import application.controller.Controller;
import javafx.application.Application;
import storage.ListStorage;
import storage.Storage;

import java.io.*;

public class App {
    public static void main(String[] args) {


        Storage storage = loadStorage();
        if (storage == null) {
            storage = new ListStorage();
        }
        Controller.setStorage(storage);
//        initStorage();
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

    public static void initStorage() {
        // Lager 1: Simons stash
        var lager1 = Controller.createLager("Simons stash");

        var reol1 = Controller.createReol(lager1, "Reol A");
        var reol2 = Controller.createReol(lager1, "Reol B");

        var hylde1A = Controller.createHylde(reol1, "øverst");
        var hylde1B = Controller.createHylde(reol1, "nederst");
        var hylde2A = Controller.createHylde(reol2, "øverst");
        var hylde2B = Controller.createHylde(reol2, "nederst");

        Controller.createFad(225.0, "Mikkel", "Eg", 3, hylde1A);
        Controller.createFad(180.0, "Sara", "Ask", 2, hylde1A);
        Controller.createFad(150.0, "Peter", "Bøg", 1, hylde1B);
        Controller.createFad(300.0, "Julie", "Valnød", 4, hylde2A);
        Controller.createFad(275.0, "Thomas", "Eg", 2, hylde2B);

        // Lager 2: Firmaets lager
        var lager2 = Controller.createLager("Nordisk Træfade ApS");

        var reol3 = Controller.createReol(lager2, "Reol 1");
        var reol4 = Controller.createReol(lager2, "Reol 2");

        var hylde3A = Controller.createHylde(reol3, "øverst");
        var hylde3B = Controller.createHylde(reol3, "midt");
        var hylde4A = Controller.createHylde(reol4, "øverst");
        var hylde4B = Controller.createHylde(reol4, "nederst");

        Controller.createFad(320.0, "Anders", "Eg", 5, hylde3A);
        Controller.createFad(260.0, "Lise", "Ask", 2, hylde3B);
        Controller.createFad(210.0, "Jonas", "Birk", 1, hylde4A);
        Controller.createFad(195.0, "Emilie", "Eg", 3, hylde4B);
        Controller.createFad(400.0, "Niels", "Teak", 6, hylde4B);

        // Evt. logning
        System.out.println("Lagre og fade er initialiseret.");
    }


}
