package cat.nilcm01;

import cat.nilcm01.screens.Start;
import cat.nilcm01.utils.DirectoryManagement;

import java.nio.file.*;

public class Main {
    public static void main(String[] args) {
        //libraryTest();
        guiTest();
        //copyTest();
        //deleteTest();
    }

    public static void libraryTest() {
        Library library = new Library("M:/HQ");
        // Save to file (on the specified directory) the returned string of library.toJSON()
        Path path = Paths.get("M:/HQ/library.json");
        try {
            Files.writeString(path, library.toJSON());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void guiTest() {
        // Create a new instance of Start
        Start start = new Start();
    }

    private final static String source = "M:\\HQ\\Golden Sextion";
    private final static String destination = "M:\\TEST\\Golden Sextion";

    public static void copyTest() {
        System.out.println("Copying " + source + " to " + destination);

        if (DirectoryManagement.copyDirectory(source, destination, progress -> {System.out.println(progress + "%");}))
            System.out.println("Copy successful");
        else
            System.out.println("Copy failed");
    }

    public static void deleteTest() {
        System.out.println("Deleting " + destination);

        if (DirectoryManagement.deleteDirectory(destination, progress -> {System.out.println(progress + "%");}))
            System.out.println("Delete successful");
        else
            System.out.println("Delete failed");
    }
}