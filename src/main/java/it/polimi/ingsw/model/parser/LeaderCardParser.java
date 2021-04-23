package it.polimi.ingsw.model.parser;

import it.polimi.ingsw.model.cards.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class LeaderCardParser {

    private LeaderCardParser() throws IllegalAccessException {
        throw new IllegalAccessException("Parser Class");
    }

    /**
     * Reads the default JSON file containing the leader cards infos
     * @return the list containing all the cards
     * @throws FileNotFoundException if the file cannot be opened or it's missing
     * @throws JsonSyntaxException if the file contains some syntax errors;
     * @throws JsonIOException if the file cannot be read by Json
     */
    public static ArrayList<LeaderCard> deserializeLeaderList() throws FileNotFoundException,JsonIOException, JsonSyntaxException {
        String path = "src/main/resources/Json/LeaderCards.json";
        return LeaderCardParser.deserializeLeaderList(path);
    }

    /**
     * Reads the default or custom JSON file containing the leader cards infos
     * @return the list containing all the cards
     * @throws FileNotFoundException if the file cannot be opened or it's missing
     * @throws JsonSyntaxException if the file contains some syntax errors;
     * @throws JsonIOException if the file cannot be read by Json
     */
    public static ArrayList<LeaderCard> deserializeLeaderList(String path) throws FileNotFoundException,JsonIOException, JsonSyntaxException{
        Reader reader;
        ArrayList<LeaderCard> cards;

        RuntimeTypeAdapterFactory<Requirement> requirementAdapterFactory
                = RuntimeTypeAdapterFactory.of(Requirement.class, "type")
                .registerSubtype(ResourceRequirement.class, "ResourceRequirement")
                .registerSubtype(DevelopmentRequirement.class, "DevelopmentRequirement");

        RuntimeTypeAdapterFactory<Ability> abilityAdapterFactory
                = RuntimeTypeAdapterFactory.of(Ability.class, "type")
                .registerSubtype(ConversionAbility.class, "ConversionAbility")
                .registerSubtype(DiscountAbility.class, "DiscountAbility")
                .registerSubtype(ProductionAbility.class, "ProductionAbility")
                .registerSubtype(StoreAbility.class, "StoreAbility");

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(requirementAdapterFactory)
                .registerTypeAdapterFactory(abilityAdapterFactory).create();

        try {
            reader = new FileReader(path);
        }catch (FileNotFoundException e){
            throw new FileNotFoundException( path +  ": File not found");
        }

        try {
            Type leaderList = new TypeToken<ArrayList<LeaderCard>>(){}.getType();
            cards = gson.fromJson(reader, leaderList);
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
