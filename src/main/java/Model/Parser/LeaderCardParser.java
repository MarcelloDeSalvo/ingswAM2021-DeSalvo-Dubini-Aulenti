package Model.Parser;

import Model.Cards.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class LeaderCardParser {

    private LeaderCardParser() {
    }

    /**
     * Reads the JSON file containing the leader cards infos
     * @return the list containing all the cards
     * @throws FileNotFoundException
     */
    public static ArrayList<LeaderCard> deserializeLeaderList() throws FileNotFoundException {
        String path = "src/main/resources/Json/LeaderCards.json";

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



        Reader reader = new FileReader(path);

        Type leaderList = new TypeToken<ArrayList<LeaderCard>>(){}.getType();
        ArrayList<LeaderCard> cards = gson.fromJson(reader, leaderList);

        return cards;
    }
}
