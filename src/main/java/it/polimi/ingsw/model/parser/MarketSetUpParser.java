package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.resources.ResourceContainer;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MarketSetUpParser {


    private MarketSetUpParser() throws IllegalAccessException {
        throw new IllegalAccessException("Parser Class");
    }

    /**
     * Reads the JSON file containing the initial marbles contained in the market
     * @return An arraylist of the initial marbles
     * @throws FileNotFoundException if the file cannot be opened or it's missing
     * @throws JsonSyntaxException if the file contains some syntax errors;
     * @throws JsonIOException if the file cannot be read by Json
     */
    public static ArrayList<ResourceContainer> deserializeMarketElements() throws FileNotFoundException,JsonIOException, JsonSyntaxException {
        String path = "src/main/resources/Json/MarketSetUp.json";
        return MarketSetUpParser.deserializeMarketElements(path);
    }

    /**
     * Reads the JSON file containing the initial marbles contained in the market
     * @return An arraylist of the initial marbles
     * @throws FileNotFoundException if the file cannot be opened or it's missing
     * @throws JsonSyntaxException if the file contains some syntax errors;
     * @throws JsonIOException if the file cannot be read by Json
     */
    public static ArrayList<ResourceContainer> deserializeMarketElements(String path) throws FileNotFoundException,JsonIOException, JsonSyntaxException {
        Gson gson = new Gson();
        Reader reader;
        ArrayList<ResourceContainer> marbles;

        try {
            reader = new FileReader(path);
        }catch (FileNotFoundException e){
            throw new FileNotFoundException( path +  ": File not found");
        }

        try {
            Type marbleList = new TypeToken<ArrayList<ResourceContainer>>(){}.getType();
            marbles = gson.fromJson(reader, marbleList);
        }catch (JsonIOException jsonIOException){
            jsonIOException.printStackTrace();
            throw new JsonIOException( path + ": File cannot be read by Json");

        }catch (JsonSyntaxException jsonSyntaxException){
            jsonSyntaxException.printStackTrace();
            throw new JsonSyntaxException( path + ": File is malformed");
        }


        return marbles;
    }


}
