package main.util;

import main.User;

import java.io.*;
import java.util.Collection;
import java.util.List;

public class Serialize {

    /**
     * Serializes Collection of <tt>users</tt>
     * to File named <tt>fileName</tt>.
     *
     * @param fileName name of file where to save it
     * @param users    Collection to serialize
     */
    public static void serializeUsers(String fileName, Collection<? extends User> users) {
        FileOutputStream fo = null;
        ObjectOutputStream output = null;
        try {
            File file = new File(fileName);
            fo = new FileOutputStream(file);
            output = new ObjectOutputStream(fo);

            for (Object u : users)
                output.writeObject((User) u);

        } catch (FileNotFoundException e) {
            System.out.printf("File %s not found", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStreams(fo, output);
        }
    }

    /**
     * Deserializes <tt>users</tt> from File <tt>fileName</tt>,
     * and you may delete the file after it's done passing true
     * for <tt>delete</tt> boolean. If something went wrong
     * and File <tt>fileName</tt> hasn't been read, then it doesn't get deleted.
     *
     * @param fileName name of File from which you want to deserialize
     * @param users    Collection of users to save deserialized data to
     * @param delete   whether the <tt>fileName</tt> will be deleted or not
     */
    public static void deserializeUsers(String fileName, Collection<? super User> users, boolean delete) {
        boolean done = false;

        File file = null;
        FileInputStream fi = null;
        ObjectInputStream input = null;
        try {
            file = new File(fileName);
            fi = new FileInputStream(file);
            input = new ObjectInputStream(fi);

            try {
                while (true) {
                    User user = (User) input.readObject();
                    users.add(user);
                }
            } catch (EOFException e) {
                done = true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.printf("File %s not found", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (done && delete && file.exists())
                file.delete();
            closeStreams(fi, input);
        }
    }

    /**
     * Deserializes <tt>users</tt> from File <tt>fileName</tt>
     *
     * @param fileName name of File from which you're deserializing
     * @param users    Collection of users to save deserialized data to
     */
    public static void deserializeUsers(String fileName, List<? super User> users) {
        deserializeUsers(fileName, users, false);
    }

    /**
     * Closes given streams
     *
     * @param fileStream   file stream to close
     * @param objectStream object stream to close
     */
    private static void closeStreams(Closeable fileStream, Closeable objectStream) {
        try {
            if (fileStream != null) fileStream.close();
            if (objectStream != null) objectStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
