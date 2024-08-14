# Pokedex-Java

![Pokedex Java](https://img.shields.io/badge/Pokedex_Java-v1.0-crimson?style=flat)

**Pokedex-Java** is a simple and easy-to-use Java library for fetching detailed information about Pokémon.

## Installation

### Gradle

Add the following dependency to your `build.gradle` file:

```gradle
implementation("io.github.luoshenshi:pokedex:1.0")
```

### Maven

Add the following dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>io.github.luoshenshi</groupId>
    <artifactId>pokedex</artifactId>
    <version>1.0</version>
</dependency>
```

## Usage

Here’s an example of how to use the library to fetch the evolution chain of a Pokémon:

```java
Pokedex.pokedex("emolga", new PokemonInfo() {
    @Override
    public void onResponse(Pokemon pokemon) {
        System.out.println(pokemon.getEvolutionChain());
    }

    @Override
    public void onFailure(IOException e) {
        System.out.println(e.getLocalizedMessage());
    }
});
```

This code snippet demonstrates how to retrieve and print the evolution chain of a Pokémon (in this case, Emolga).
---
