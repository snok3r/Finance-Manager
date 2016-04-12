package main.util;

import java.io.*;
import java.util.Collection;

public class Serialize<T extends Serializable> {

    /**
     * Serializes Collection <tt>collection</tt>
     * to File named <tt>fileName</tt>.
     *
     * @param fileName   name of file where to save it
     * @param collection Collection to serialize
     */
    public void serialize(String fileName, Collection<? extends T> collection) {
        FileOutputStream fo = null;
        ObjectOutputStream output = null;
        try {
            File file = new File(fileName);
            fo = new FileOutputStream(file);
            output = new ObjectOutputStream(fo);

            for (T item : collection)
                output.writeObject(item);

        } catch (FileNotFoundException e) {
            System.out.printf("File %s not found", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStreams(fo, output);
        }
    }

    /**
     * Deserializes <tt>collection</tt> from File <tt>fileName</tt>,
     * and you may delete the file after it's done passing true
     * for <tt>delete</tt> boolean. If something went wrong
     * and File <tt>fileName</tt> hasn't been read, then it doesn't get deleted.
     *
     * @param fileName   name of File from which you want to deserialize
     * @param collection Collection to save deserialized data to
     * @param delete     whether the <tt>fileName</tt> will be deleted or not
     */
    public void deserialize(String fileName, Collection<? super T> collection, boolean delete) {
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
                    T item = (T) input.readObject();
                    collection.add(item);
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
     * Deserializes <tt>collection</tt> from File <tt>fileName</tt>
     *
     * @param fileName   name of File from which you're deserializing
     * @param collection Collection to save the deserialized data to
     */
    public void deserialize(String fileName, Collection<? super T> collection) {
        deserialize(fileName, collection, false);
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
