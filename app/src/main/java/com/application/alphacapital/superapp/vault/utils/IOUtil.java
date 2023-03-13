package com.application.alphacapital.superapp.vault.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IOUtil {

    public static byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfLastSeparator(filename);
        return filename.substring(index + 1);
    }

    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';
    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
        return Math.max(lastUnixPos, lastWindowsPos);
    }
    
    public static String getExtension(String fileName)
    {
     	String[] namearr = fileName.split("\\.");
     	String extension = namearr[namearr.length - 1];
    	
        return extension;
    }


    public static String getFileNameWithoutExtension(String fileName)
    {
        String[] namearr = fileName.split("\\.");
        String name = namearr[0];

        return name;
    }
    
    public static String getFileName(String filePath)
    {
    	String[] mediafile = filePath.split("/");
     	String name = mediafile[mediafile.length - 1];
     	
     	return name ;
    }
}
