package Model.Parser;

import Model.Cards.DevelopmentCard;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DevelopmentCardParser {


    private DevelopmentCardParser() {

    }

    /**
     * Reads the JSON file containing the development cards infos
     * @return the list containing all the cards
     * @throws FileNotFoundException
     */
    public static ArrayList<DevelopmentCard> deserializeDevelopmentList() throws FileNotFoundException {
        String path = "src/main/resources/Json/DevelopmentCards.json";
        Gson gson = new Gson();

        Reader reader = new FileReader(path);

        Type cardList = new TypeToken<ArrayList<DevelopmentCard>>(){}.getType();
        ArrayList<DevelopmentCard> cards = gson.fromJson(reader, cardList);

        return cards;
    }


}
