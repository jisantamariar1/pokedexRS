package ec.edu.uce.pokedexRS.handler;

import ec.edu.uce.pokedexRS.api.PokemonApiClient;
import ec.edu.uce.pokedexRS.model.Pokemon;
import ec.edu.uce.pokedexRS.repository.PokemonRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PokemonProcessor {
    @Autowired
    private PokemonRepository pokemonRepository;
    @Autowired
    private PokemonApiClient pokemonApiClient;

    public PokemonProcessor() {
    }

    public Pokemon processPokemon(JSONObject pokemonData) {
        long id = pokemonData.getInt("id");
        String name = pokemonData.getString("name");
        // Extraer estadísticas (hp, attack, defense, etc.)
        JSONArray stats = pokemonData.getJSONArray("stats");
        int hp = 0, attack = 0, defense = 0, specialAttack = 0, specialDefense = 0, speed = 0;
        // Recorremos el arreglo de estadísticas y asignamos los valores correctos
        for (int i = 0; i < stats.length(); i++) {
            JSONObject stat = stats.getJSONObject(i);
            String statName = stat.getJSONObject("stat").getString("name");
            int baseStat = stat.getInt("base_stat");
            switch (statName) {
                case "hp":
                    hp = baseStat;
                    break;
                case "attack":
                    attack = baseStat;
                    break;
                case "defense":
                    defense = baseStat;
                    break;
                case "special-attack":
                    specialAttack = baseStat;
                    break;
                case "special-defense":
                    specialDefense = baseStat;
                    break;
                case "speed":
                    speed = baseStat;
                    break;
                default:
                    break;
            }
        }
        // Calcular el total
        int total = hp + attack + defense + specialAttack + specialDefense + speed;
        // Obtener los nuevos atributos
        Double height = pokemonData.getDouble("height");
        Double weight = pokemonData.getDouble("weight");
        String description = getPokemonDescription(name);
        String spriteUrl = pokemonData.getJSONObject("sprites").getJSONObject("other").getJSONObject("official-artwork").getString("front_default");

        // Crear el Pokémon con los atributos obtenidos
        Pokemon pokemon = new Pokemon();
        pokemon.setId(id);
        pokemon.setName(name);
        pokemon.setHp(hp);
        pokemon.setAttack(attack);
        pokemon.setDefense(defense);
        pokemon.setSpecialAttack(specialAttack);
        pokemon.setSpecialDefense(specialDefense);
        pokemon.setSpeed(speed);
        pokemon.setTotal(total);
        pokemon.setHeight(height);
        pokemon.setWeight(weight);
        pokemon.setDescription(description);
        pokemon.setSpriteUrl(spriteUrl);
        return pokemon;
    }

    //obtener la descripcion
    private String getPokemonDescription(String name) {
        try {
            return pokemonApiClient.getPokemonDetails("https://pokeapi.co/api/v2/pokemon-species/" + name.toLowerCase()).getJSONArray("flavor_text_entries").toList().stream().map(entry -> new JSONObject((java.util.Map<?, ?>) entry)).filter(entry -> entry.getJSONObject("language").getString("name").equals("es")).map(entry -> entry.getString("flavor_text")).findFirst().orElse("No description available.");
        } catch (Exception e) {
            // Manejo de excepciones si la descripción no se encuentra o hay un error
            System.out.println("Error al obtener la descripción para el Pokémon " + name + ": " + e.getMessage());
            return "Descripción no disponible.";
        }
    }
}
