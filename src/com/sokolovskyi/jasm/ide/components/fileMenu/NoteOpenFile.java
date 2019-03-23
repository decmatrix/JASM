package com.sokolovskyi.jasm.ide.components.fileMenu;

import com.sokolovskyi.jasm.ide.components.ideErrors.NoteError;

import javax.swing.*;
import java.io.*;

public class NoteOpenFile {
    private File choosenFile;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private NoteError error;

    //path to config file of path of last using file
    private final static String PATH_CONFIG_LAST_FILE = "./src/configs/lastFile.jasm";

    public NoteOpenFile(JTextArea textArea, NoteError error){
        this.textArea = textArea;
        this.error = error;

        fileChooser = new JFileChooser();

    }

    public void setChoosenFile(){
        chooseFile();
        readFile(choosenFile);
    }

    public void initLastFile(){
        if(isEmptyLastFile()) return;
        readFile(new File(getLastFile()));
    }

    private String getLastFile(){
        String path = null;

        try(FileReader reader = new FileReader(PATH_CONFIG_LAST_FILE)){
            BufferedReader br = new BufferedReader(reader);
            path = br.readLine();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        return path;
    }

    private boolean isEmptyLastFile(){
        return new File(PATH_CONFIG_LAST_FILE).length() == 0;
    }

    private void chooseFile(){
        if(fileChooser.showDialog(null, "Открыть файл") == JFileChooser.APPROVE_OPTION){
            choosenFile = fileChooser.getSelectedFile();
            saveLastFile();
        }
    }

    private void saveLastFile(){
        String path = choosenFile.getPath();

        try(FileWriter writer = new FileWriter(PATH_CONFIG_LAST_FILE, false)){
            writer.write(choosenFile.getPath());
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }



    private void readFile(File file){
        try(FileReader fr = new FileReader(file)){
            textArea.setText(null);

            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();

            while(line != null){
                textArea.append(line + '\n');
                line = reader.readLine();
            }

        }catch (IOException e){
            e.printStackTrace();


            error.setVisible(true);
        }catch (NullPointerException ignored){

        }
    }
}
