package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) throws JSONException{
        // Read JSON data for all attributes
        JSONObject reader = new JSONObject(json);
        JSONObject name = reader.getJSONObject("name");
        String mainName = name.getString("mainName");
        JSONArray alsoKnownAsJson = name.getJSONArray("alsoKnownAs");
        ArrayList<String> alsoKnownAs = new ArrayList<>();
        if(!alsoKnownAsJson.isNull(0)) {
            for(int i = 0; i < alsoKnownAsJson.length(); i++) {
                alsoKnownAs.add(alsoKnownAsJson.getString(i));
            }
        }

        String placeOfOrigin = reader.getString("placeOfOrigin");
        String description = reader.getString("description");
        String imgURL = reader.getString("image");
        JSONArray ingredientsJson = reader.getJSONArray("ingredients");
        ArrayList<String> ingredients = new ArrayList<>();
        if(!ingredientsJson.isNull(0)) {
            for(int i = 0; i < ingredientsJson.length(); i++) {
                ingredients.add(ingredientsJson.getString(i));
            }
        }





        return new Sandwich(mainName, alsoKnownAs, placeOfOrigin,
                description, imgURL, ingredients
        );
    }


}
