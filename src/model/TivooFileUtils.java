package model;

import java.io.*;

public class TivooFileUtils {    
    
    public static boolean dirExists(String dirname) {
	File dir = new File(dirname);
	return dir.exists();
    }
    
    public static void makeDirectory(String dirname) {
	if (dirExists(dirname)) {
	    clearDirectory(dirname);
	    return;
	}
	try { 
	    File dir = new File(dirname);  
	    boolean bFile = dir.mkdir(); 
	    if (bFile == false) { 
		throw new TivooException("Cannot create directory!");
	    }
	} catch(Exception e) { 
	    e.printStackTrace();
	} 
    }
    
    public static String getWorkingDirectory() {
	try {
	    return new File(".").getCanonicalPath();
	} catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void deleteFile(String path) {
	File file = new File(path);
	if (file.exists()) {
	    if (file.isFile())
		file.delete();
	    else if (file.isDirectory()) {
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++)
		    deleteFile(files[i].getPath());
	    }
	    file.delete();
	} 
	else
	    throw new TivooException("File not found!");
    }

    public static void clearDirectory(String path) {
	File file = new File(path);
	if (file.exists()) {
	    if (file.isDirectory()) {
		File files[] = file.listFiles();
		for (int i = 0; i < files.length; i++)
		    deleteFile(files[i].getPath());
	    }
	}
	else
	    throw new TivooException("Directory not found!");
    }

}