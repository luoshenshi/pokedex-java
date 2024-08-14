package io.github.luoshenshi;

import java.io.IOException;

interface PokemonInfo {
    void onResponse(Pokemon pokemon);

    void onFailure(IOException e);
}
