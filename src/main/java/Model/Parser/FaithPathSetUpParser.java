package Model.Parser;

import Model.FaithPath;
import Model.Resources.ResourceContainer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FaithPathSetUpParser {

    private FaithPathSetUpParser(){

    }

    /**
     * Reads the JSON file containing the initial faithpath
     * @return An initialized faithpath without players
     * @throws FileNotFoundException
     */
    public static FaithPath deserializeFaithPathSetUp() throws FileNotFoundException {
        String path = "src/main/resources/Json/FaithPathSetUp.json";
        Gson gson = new Gson();

        Reader reader = new FileReader(path);
        FaithPath setUp = gson.fromJson(reader, FaithPath.class);

        return setUp;
    }


}
