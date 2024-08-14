package io.github.luoshenshi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

class PokedexUtils {
    private static final OkHttpClient client = new OkHttpClient();

    public static ArrayList<String> extractTypes(JSONObject data) throws JSONException {
        JSONArray typesArray = data.getJSONArray("types");
        ArrayList<String> typesList = new ArrayList<>();
        for (int i = 0; i < typesArray.length(); i++) {
            JSONObject typeObj = typesArray.getJSONObject(i).getJSONObject("type");
            typesList.add(typeObj.getString("name"));
        }
        return typesList;
    }

    public static ArrayList<String> extractAbilities(JSONObject data) throws JSONException {
        JSONArray abilitiesArray = data.getJSONArray("abilities");
        ArrayList<String> abilitiesList = new ArrayList<>();
        for (int i = 0; i < abilitiesArray.length(); i++) {
            JSONObject abilityObj = abilitiesArray.getJSONObject(i).getJSONObject("ability");
            abilitiesList.add(abilityObj.getString("name"));
        }
        return abilitiesList;
    }

    public static ArrayList<String> extractMoves(JSONObject data) throws JSONException {
        JSONArray movesArray = data.getJSONArray("moves");
        ArrayList<String> movesList = new ArrayList<>();
        for (int i = 0; i < movesArray.length(); i++) {
            JSONObject moveObj = movesArray.getJSONObject(i).getJSONObject("move");
            movesList.add(moveObj.getString("name"));
        }
        return movesList;
    }

    public static ArrayList<JSONObject> extractStats(JSONObject data) throws JSONException {
        JSONArray statsArray = data.getJSONArray("stats");
        ArrayList<JSONObject> statsList = new ArrayList<>();
        for (int i = 0; i < statsArray.length(); i++) {
            statsList.add(statsArray.getJSONObject(i));
        }
        return statsList;
    }

    public static String extractPokemonPic(JSONObject data) throws JSONException {
        return data.getJSONObject("sprites").getJSONObject("other").getJSONObject("official-artwork").getString("front_default");
    }

    public static String calculateHeight(JSONObject data) throws JSONException {
        double heightInInches = (10 * data.getDouble("height")) / 2.54;
        return (int) Math.floor(heightInInches / 12) + "' " +
                String.format("%02d", (int) Math.round(heightInInches % 12)) + '"';
    }

    public static String calculateWeight(JSONObject data) throws JSONException {
        double weightInKg = data.getDouble("weight") / 10;
        return String.format("%.1f lbs", 2.20462 * weightInKg);
    }

    public static String extractCategory(JSONObject speciesData) throws JSONException {
        JSONArray generaArray = speciesData.getJSONArray("genera");
        String category = "";
        for (int i = 0; i < generaArray.length(); i++) {
            JSONObject generaObj = generaArray.getJSONObject(i);
            if ("en".equals(generaObj.getJSONObject("language").getString("name"))) {
                category = generaObj.getString("genus").replace("Pokémon", "");
                break;
            }
        }
        return category;
    }

    public static String extractGenders(JSONObject speciesData) throws JSONException {
        return speciesData.getInt("gender_rate") == -1 ? "Unknown" : "Male, Female";
    }

    public static ArrayList<JSONObject> getEvolutionChain(JSONObject speciesData) throws IOException, JSONException {
        JSONObject evolutionChainData = getJsonObject(speciesData.getJSONObject("evolution_chain").getString("url"));
        JSONObject chain = evolutionChainData.getJSONObject("chain");
        ArrayList<JSONObject> evolutionChain = new ArrayList<>();
        getEvolution(chain, evolutionChain);
        return evolutionChain;
    }

    public static ArrayList<String> getEvolutionPics(ArrayList<JSONObject> evolutionChain) throws IOException, JSONException {
        ArrayList<String> evolutionPics = new ArrayList<>();
        for (JSONObject evolution : evolutionChain) {
            JSONObject species = getJsonObject("https://pokeapi.co/api/v2/pokemon/" + evolution.getString("species_name"));
            JSONObject spritesObj = species.getJSONObject("sprites").getJSONObject("other").getJSONObject("official-artwork");
            evolutionPics.add(spritesObj.getString("front_default"));
        }
        return evolutionPics;
    }

    public static ArrayList<ArrayList<String>> getWeaknessList(ArrayList<String> typesList) throws IOException, JSONException {
        ArrayList<ArrayList<String>> weaknessList = new ArrayList<>();
        for (String type : typesList) {
            JSONObject typeData = getJsonObject("https://pokeapi.co/api/v2/type/" + type);
            JSONArray doubleDamageArray = typeData.getJSONObject("damage_relations").getJSONArray("double_damage_from");
            ArrayList<String> weakness = new ArrayList<>();
            for (int i = 0; i < doubleDamageArray.length(); i++) {
                JSONObject damageObj = doubleDamageArray.getJSONObject(i);
                weakness.add(damageObj.getString("name"));
            }
            weaknessList.add(weakness);
        }
        return weaknessList;
    }

    public static ArrayList<String> getFlavorTextList(String name) throws IOException, JSONException {
        ArrayList<String> flavorList = new ArrayList<>();
        try {
            JSONObject flavorObject = getJsonObject("https://pokeapi.co/api/v2/pokemon-species/" + name);
            JSONArray flavorTextEntries = flavorObject.getJSONArray("flavor_text_entries");

            for (int i = 0; i < flavorTextEntries.length(); i++) {
                JSONObject textEntry = flavorTextEntries.getJSONObject(i);
                String languageName = textEntry.getJSONObject("language").getString("name");
                String versionName = textEntry.getJSONObject("version").getString("name");

                if (("en".equals(languageName) && "shield".equals(versionName)) ||
                        "blue".equals(versionName) ||
                        "yellow".equals(versionName) ||
                        "gold".equals(versionName) ||
                        "silver".equals(versionName) ||
                        "ruby".equals(versionName)) {
                    String flavorText = textEntry.getString("flavor_text").replace("\n", " ");
                    flavorList.add(flavorText.substring(0, 1).toUpperCase() + flavorText.substring(1).toLowerCase());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return flavorList;
    }

    private static void getEvolution(JSONObject evolution, ArrayList<JSONObject> evolutionChain) throws JSONException {
        if (evolution != null) {
            JSONObject speciesObj = evolution.getJSONObject("species");
            JSONObject evolutionObj = new JSONObject();
            evolutionObj.put("species_name", speciesObj.getString("name"));
            evolutionObj.put("evolution_details", evolution.getJSONArray("evolution_details"));
            evolutionChain.add(evolutionObj);

            JSONArray evolvesToArray = evolution.getJSONArray("evolves_to");
            for (int i = 0; i < evolvesToArray.length(); i++) {
                getEvolution(evolvesToArray.getJSONObject(i), evolutionChain);
            }
        }
    }

    public static JSONObject getJsonObject(String url) throws IOException, JSONException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return new JSONObject(response.body().string());
    }
}
