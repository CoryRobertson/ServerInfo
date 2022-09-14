package com.github.coryrobertson.serverinfoclient;

import java.io.*;
import java.util.Date;

public record PreviousSession(String hostname, Date dateLastConnected) implements Serializable {

    public PreviousSession
    {
        if(hostname.length() == 0)
        {
            throw new IllegalArgumentException("Cannot save hostname of length 0 or less.");
        }
    }

    /**
     * Saves the object to file with filename "./previousSession.sav"
     * @return true if saving to a file and writing to system was possible.
     */
    public boolean saveToFile()
    {
        File saveFile = new File("./previousSession.sav");
        FileOutputStream fileOutputStream;
        try
        {
            fileOutputStream = new FileOutputStream(saveFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }

    /**
     * Returns a PreviousSession object if a save file is present, found, and valid.
     * @return a previous session object that is null if the file is not present
     */
    public static PreviousSession loadFromFile()
    {
        try
        {
            FileInputStream fis = new FileInputStream("./previousSession.sav");
            ObjectInputStream ois = new ObjectInputStream(fis);
            return (PreviousSession) ois.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            return null;
        }
    }

    /**
     * Checks if a save file exists
     * @return true if and only if a save file is present, found, and valid.
     */
    public static boolean saveFileExists()
    {
        try
        {
            FileInputStream fis = new FileInputStream("./previousSession.sav");
            ObjectInputStream ois = new ObjectInputStream(fis);
            PreviousSession ps = (PreviousSession) ois.readObject();
            return true;
        }
        catch (IOException | ClassNotFoundException e)
        {
            return false;
        }
    }
}
