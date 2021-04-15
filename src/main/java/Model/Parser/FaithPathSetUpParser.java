package Model.Parser;

import Model.FaithPath;
import Model.Resources.ResourceContainer;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

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
    public static FaithPath deserializeFaithPathSetUp(int numberOfP) throws FileNotFoundException,JsonIOException, JsonSyntaxException {
        String path = "src/main/resources/Json/FaithPathSetUp.json";
        return FaithPathSetUpParser.deserializeFaithPathSetUp(numberOfP, path);
    }

    /**
     * Reads the custom JSON file containing the initial faithpath
     * @return An initialized faithpath without players
     * @throws FileNotFoundException if the file cannot be opened or it's missing
     * @throws JsonSyntaxException if the file contains some syntax errors;
     * @throws JsonIOException if the file cannot be read by Json
     */
    public static FaithPath deserializeFaithPathSetUp(int numberOfP,String path) throws FileNotFoundException,JsonIOException, JsonSyntaxException {
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

        setUp.setNumberOfPlayers(numberOfP);
        return setUp;
    }



}
