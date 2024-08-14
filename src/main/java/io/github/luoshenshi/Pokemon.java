package io.github.luoshenshi;

import org.json.JSONObject;

import java.util.ArrayList;

class Pokemon {
    private String name;
    private int id;
    private ArrayList<String> types;
    private ArrayList<String> abilities;
    private ArrayList<JSONObject> stats;
    private String pokemonPic;
    private String height;
    private String weight;
    private ArrayList<String> moves;
    private boolean isLegendary;
    private String category;
    private String genders;
    private ArrayList<JSONObject> evolutionChain;
    private ArrayList<String> evolutionPics;
    private ArrayList<ArrayList<String>> weaknessList;
    private ArrayList<String> flavorList;

    public Pokemon(String name, int id, ArrayList<String> types, ArrayList<String> abilities, ArrayList<JSONObject> stats, String pokemonPic, String height, String weight, ArrayList<String> moves, boolean isLegendary, String category, String genders, ArrayList<JSONObject> evolutionChain, ArrayList<String> evolutionPics, ArrayList<ArrayList<String>> weaknessList, ArrayList<String> flavorList) {
        this.name = name;
        this.id = id;
        this.types = types;
        this.abilities = abilities;
        this.stats = stats;
        this.pokemonPic = pokemonPic;
        this.height = height;
        this.weight = weight;
        this.moves = moves;
        this.isLegendary = isLegendary;
        this.category = category;
        this.genders = genders;
        this.evolutionChain = evolutionChain;
        this.evolutionPics = evolutionPics;
        this.weaknessList = weaknessList;
        this.flavorList = flavorList;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public ArrayList<String> getAbilities() {
        return abilities;
    }

    public ArrayList<JSONObject> getStats() {
        return stats;
    }

    public String getPokemonPic() {
        return pokemonPic;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public ArrayList<String> getMoves() {
        return moves;
    }

    public boolean isLegendary() {
        return isLegendary;
    }

    public String getCategory() {
        return category;
    }

    public String getGenders() {
        return genders;
    }

    public ArrayList<JSONObject> getEvolutionChain() {
        return evolutionChain;
    }

    public ArrayList<String> getEvolutionPics() {
        return evolutionPics;
    }

    public ArrayList<ArrayList<String>> getWeaknessList() {
        return weaknessList;
    }

    public ArrayList<String> getFlavorList() {
        return flavorList;
    }
}
