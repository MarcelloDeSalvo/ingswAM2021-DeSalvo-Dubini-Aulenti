package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DevelopmentCardParser {


    private DevelopmentCardParser() throws IllegalAccessException {
        throw new IllegalAccessException("Parser Class");
    }

    /**
     * Reads the default JSON file containing the development cards infos
     * @return the list containing all the cards
     * @throws FileNotFoundException if the file cannot be opened or it's missing
     * @throws JsonSyntaxException if the file contains some syntax errors;
     * @throws JsonIOException if the file cannot be read by Json
     */
    public static ArrayList<DevelopmentCard> deserializeDevelopmentList() throws FileNotFoundException,JsonIOException, JsonSyntaxException {
        String path = "/Json/DevelopmentCards.json";
        return DevelopmentCardParser.deserializeDevelopmentList(path);
    }

    /**
     * Reads the custom or default JSON file containing the development cards infos
     * @return the list containing all the cards
     * @throws FileNotFoundException if the file cannot be opened or it's missing
     */
    public static ArrayList<DevelopmentCard> deserializeDevelopmentList(String path) throws FileNotFoundException,JsonIOException, JsonSyntaxException {
        Gson gson = new Gson();
        Reader reader;
        ArrayList<DevelopmentCard> cards;

        try {
            reader = new InputStreamReader(DevelopmentCardParser.class.getResourceAsStream(path), StandardCharsets.UTF_8);
        }catch (NullPointerException e){
            throw new FileNotFoundException( path +  ": File not found");
        }


        Type cardList = new TypeToken<ArrayList<DevelopmentCard>>(){}.getType();

        try {
            cards = gson.fromJson(reader, cardList);

        }catch (JsonIOException jsonIOException){
            jsonIOException.printStackTrace();
            throw new JsonIOException( path + ": File cannot be read by Json");

        }catch (JsonSyntaxException jsonSyntaxException){
            jsonSyntaxException.printStackTrace();
            throw new JsonSyntaxException( path + ": File is malformed");
        }

        return cards;
    }


}
