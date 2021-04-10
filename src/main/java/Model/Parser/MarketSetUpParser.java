package Model.Parser;

import Model.Cards.DevelopmentCard;
import Model.Deck;
import Model.Resources.ResourceContainer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;


import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MarketSetUpParser {


    private MarketSetUpParser() {

    }

    /**
     * Reads the JSON file containing the initial marbles contained in the market
     * @return An arraylist of the initial marbles
     * @throws FileNotFoundException
     */
    public static ArrayList<ResourceContainer> deserializeMarketElements() throws FileNotFoundException {
        String path = "src/main/resources/Json/MarketSetUp.json";
        Gson gson = new Gson();

        Reader reader = new FileReader(path);

        Type marbleList = new TypeToken<ArrayList<ResourceContainer>>(){}.getType();
        ArrayList<ResourceContainer> marbles = gson.fromJson(reader, marbleList);

        return marbles;
    }


}
