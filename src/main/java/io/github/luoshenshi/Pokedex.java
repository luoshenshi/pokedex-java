package io.github.luoshenshi;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Pokedex {
    private static final OkHttpClient client = new OkHttpClient();

    public static void pokedex(String pokemonName, PokemonInfo callback) {
        String name = pokemonName.toLowerCase();
        String baseUrl = "https://pokeapi.co/api/v2/pokemon/";

        Request request = new Request.Builder()
                .url(baseUrl + name)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        if (response.code() == 404) {
                            callback.onFailure(new IOException("Pokémon not found: " + name));
                        } else {
                            throw new IOException("Unexpected code " + response);
                        }
                        return;
                    }

                    assert response.body() != null;
                    JSONObject data = new JSONObject(response.body().string());

                    ArrayList<String> typesList = PokedexUtils.extractTypes(data);
                    ArrayList<String> abilitiesList = PokedexUtils.extractAbilities(data);
                    ArrayList<String> movesList = PokedexUtils.extractMoves(data);
                    ArrayList<JSONObject> statsList = PokedexUtils.extractStats(data);
                    String pokemonPic = PokedexUtils.extractPokemonPic(data);
                    String height = PokedexUtils.calculateHeight(data);
                    String weight = PokedexUtils.calculateWeight(data);

                    JSONObject speciesData = PokedexUtils.getJsonObject(data.getJSONObject("species").getString("url"));
                    boolean isLegendary = speciesData.getBoolean("is_legendary");
                    String category = PokedexUtils.extractCategory(speciesData);
                    String genders = PokedexUtils.extractGenders(speciesData);

                    ArrayList<JSONObject> evolutionChain = PokedexUtils.getEvolutionChain(speciesData);
                    ArrayList<String> evolutionPics = PokedexUtils.getEvolutionPics(evolutionChain);
                    ArrayList<ArrayList<String>> weaknessList = PokedexUtils.getWeaknessList(typesList);
                    ArrayList<String> flavorList = PokedexUtils.getFlavorTextList(name);

                    Pokemon pokemon = new Pokemon(
                            data.getString("name"),
                            data.getInt("id"),
                            typesList,
                            abilitiesList,
                            statsList,
                            pokemonPic,
                            height,
                            weight,
                            movesList,
                            isLegendary,
                            category,
                            genders,
                            evolutionChain,
                            evolutionPics,
                            weaknessList,
                            flavorList
                    );

                    callback.onResponse(pokemon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
