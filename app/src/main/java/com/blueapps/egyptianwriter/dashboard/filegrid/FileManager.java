package com.blueapps.egyptianwriter.dashboard.filegrid;

import android.content.Context;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FileManager {

    private static final String TAG = "FileManager";

    private ArrayList<FileGridData> files = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private File path;
    private String suffix;

    public FileManager(Context context, String folder, String suffix){
        path = new File(context.getFilesDir() + "/" + folder);
        this.suffix = suffix;
    }

    public ArrayList<FileGridData> getFiles(){

        files = new ArrayList<>();
        names = new ArrayList<>();
        try {
            if (!path.exists()){
                if (!path.mkdir()){
                    // TODO: Error handling
                }
            }
            File[] filesArray = path.listFiles((dir, name) -> name.toLowerCase().endsWith(suffix));
            ArrayList<File> files;
            if (filesArray != null) {
                // Sort Files in alphabetical order
                Arrays.sort(filesArray, Comparator.comparing(File::getName));

                files = new ArrayList<>(Arrays.asList(filesArray));

                Log.d(TAG, "This files where found: " + files);

                for (File file : files) {
                    String name = file.getName();
                    String filename = name;
                    if (!name.equals(suffix)) {
                        int lastPointIndex = StringUtils.lastIndexOf(name, '.');
                        if (lastPointIndex > 0) {
                            name = name.substring(0, lastPointIndex);
                        }
                        FileGridData fileData = new FileGridData(name, filename);
                        this.files.add(fileData);
                        names.add(name);
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        return files;
    }

    public ArrayList<String> getNames(){
        return names;
    }

    public void addFile(String filename){
        if(!path.exists()){
            path.mkdir();
        }
        File file = new File(this.path, filename + suffix);
        try {
            if (!file.createNewFile()) {
                Log.e(TAG, "File already exist");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(String filename){
        File file = new File(this.path, filename + suffix);
        try {
            if (file.delete()){
                Log.e(TAG, "File could not deleted");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
