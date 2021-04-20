package model.parser;

import model.ActionToken;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ActionTokensParser {

    public ActionTokensParser() throws IllegalAccessException {
        throw new IllegalAccessException("Parser Class");
    }

    /**
     * Reads the JSON file containing the initial ActionTokens
     * @return An arraylist of the initial ActionTokens
     * @throws FileNotFoundException if the file cannot be opened or it's missing
     * @throws JsonSyntaxException if the file contains some syntax errors;
     * @throws JsonIOException if the file cannot be read by Json
     */
    public static ArrayList<ActionToken> deserializeActionTokens() throws FileNotFoundException,JsonIOException, JsonSyntaxException {
        String path = "src/main/resources/Json/ActionTokens.json";
        return ActionTokensParser.deserializeActionTokens(path);
    }


    /**
     * Reads the JSON file containing the initial ActionTokens
     * @return An arraylist of the initial ActionTokens
     * @throws FileNotFoundException if the file cannot be opened or it's missing
     * @throws JsonSyntaxException if the file contains some syntax errors;
     * @throws JsonIOException if the file cannot be read by Json
     */
    public static ArrayList<ActionToken> deserializeActionTokens(String path) throws FileNotFoundException,JsonIOException, JsonSyntaxException {
        Gson gson = new Gson();
        Reader reader;
        ArrayList<ActionToken> actionTokens;

        try {
            reader = new FileReader(path);
        }catch (FileNotFoundException e){
            throw new FileNotFoundException( path +  ": File not found");
        }

        try {
            Type tokenList = new TypeToken<ArrayList<ActionToken>>(){}.getType();
            actionTokens = gson.fromJson(reader, tokenList);
        }catch (JsonIOException jsonIOException){
            jsonIOException.printStackTrace();
            throw new JsonIOException( path + ": File cannot be read by Json");

        }catch (JsonSyntaxException jsonSyntaxException){
            jsonSyntaxException.printStackTrace();
            throw new JsonSyntaxException( path + ": File is malformed");
        }

        //Collections.shuffle(actionTokens);
        return actionTokens;
    }
}
