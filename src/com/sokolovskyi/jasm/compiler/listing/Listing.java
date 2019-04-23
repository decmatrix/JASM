package com.sokolovskyi.jasm.compiler.listing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Listing {
    private final String pathListing = "./src/tmp/list.lst";

    public Listing(){
        initListing();
    }

    private void initListing(){
        try(FileWriter writer = new FileWriter(pathListing)){
            writer.write("\t\t\tJASM Java Assembly Compiler v.0.3 beta \t\t" +  new Date() + '\n');
            writer.write("\t\t\t\tby Sokolovskyi Bohdan FAM KPI KV-73\n");

            //write in Listing
            writeMachineCommands(writer);
            writeSegmentTable(writer);
            writeIdTable(writer);

        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void writeMachineCommands(FileWriter writer) throws IOException{
        
    }

    private void writeSegmentTable(FileWriter writer) throws IOException{

    }

    private void writeIdTable(FileWriter writer)throws IOException{

    }
}
