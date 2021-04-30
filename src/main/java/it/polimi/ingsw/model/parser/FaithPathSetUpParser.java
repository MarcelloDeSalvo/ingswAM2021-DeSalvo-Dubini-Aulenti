package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.FaithPath;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class FaithPathSetUpParser {

    private FaithPathSetUpParser()throws IllegalAccessException{
        throw new IllegalAccessException("Parser Class");
    }

    /**
     * Reads the default JSON file containing the initial faithpath
     * @throws FileNotFoundException if the file cannot be opened or it's missing
     * @throws JsonSyntaxException if the file contains some syntax errors;
     * @throws JsonIOException if the file cannot be read by Json
     */
    public static FaithPath deserializeFaithPathSetUp() throws FileNotFoundException,JsonIOException, JsonSyntaxException {
        String path = "src/main/resources/Json/FaithPathSetUp.json";
        return FaithPathSetUpParser.deserializeFaithPathSetUp(path);
    }

    /**
     * Reads the custom JSON file containing the initial faithpath
     * @return An initialized faithpath without players
     * @throws FileNotFoundException if the file cannot be opened or it's missing
     * @throws JsonSyntaxException if the file contains some syntax errors;
     * @throws JsonIOException if the file cannot be read by Json
     */
    public static FaithPath deserializeFaithPathSetUp(String path) throws FileNotFoundException,JsonIOException, JsonSyntaxException {
        Gson gson = new Gson();
        Reader reader;
        FaithPath setUp;

        try {
            reader = new FileReader(path);
        }catch (FileNotFoundException e){
            throw new FileNotFoundException( path +  ": File not found");
        }

        try {
            setUp = gson.fromJson(reader, FaithPath.class);

        }catch (JsonIOException jsonIOException){
            jsonIOException.printStackTrace();
            throw new JsonIOException( path + ": File cannot be read by Json");

        }catch (JsonSyntaxException jsonSyntaxException){
            jsonSyntaxException.printStackTrace();
            throw new JsonSyntaxException( path + ": File is malformed");
        }

        return setUp;
    }



}