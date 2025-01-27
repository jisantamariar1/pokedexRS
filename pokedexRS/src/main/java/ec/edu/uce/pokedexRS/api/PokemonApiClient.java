package ec.edu.uce.pokedexRS.api;

import ec.edu.uce.pokedexRS.model.Pokemon;
import ec.edu.uce.pokedexRS.repository.PokemonRepository;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class PokemonApiClient {
    @Autowired
    private PokemonRepository pokemonRepository;
    private static final String BASE_URL = "https://pokeapi.co/api/v2/pokemon/";
    private final RestTemplate restTemplate;
    private final long numeroPokemones = 1025;
    private final int batchSize = 50;  // Tamaño del lote

    // Constructor con solo RestTemplate para realizar solicitudes HTTP
    public PokemonApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Solicitud a url y traer informacion en formato JSON(JSONObject)
    public JSONObject getPokemonDetails(String url) {
        String response = restTemplate.getForObject(url, String.class);
        return new JSONObject(response);
    }

    // Orquesta el flujo de la obtencion y guardado de los datos
    @Transactional
    public void fetch(PokemonInfo pokemonInfo) throws ExecutionException, InterruptedException {
        List<Pokemon> pokemonList = new ArrayList<>();
        List<String> pokemonUrls = new ArrayList<>();
        long startTime = System.currentTimeMillis(); // Captura el tiempo inicial
        for (long id = 1; id <= numeroPokemones; id++) {
            String pokemonUrl = BASE_URL + id + "/"; // Construir la URL directamente aquí
            pokemonUrls.add(pokemonUrl);
            JSONObject pokemonData = getPokemonDetails(pokemonUrl);// Obtener los detalles del Pokémon
            // Llamar a PokemonInfo para guardar los datos
            pokemonList.add(pokemonInfo.fetchAndSavePokemon(pokemonData));
            // Si alcanzamos el tamaño del lote, guardamos y limpiamos la lista
            if (pokemonList.size() >= batchSize) {
                pokemonRepository.saveAll(pokemonList);
                pokemonList.clear();  // Limpiar la lista para el siguiente lote
            }
        }
        if (!pokemonList.isEmpty()) {// Si hay Pokémon restantes que no se guardaron en un lote completo, los guardamos
            pokemonRepository.saveAll(pokemonList);
        }
        pokemonInfo.procesarEvoluciones(pokemonUrls); //las evoluciones se manejan despues de guardar los pokemones
        long endTime = System.currentTimeMillis(); // Captura el tiempo final
        long duration = endTime - startTime; //
        System.out.println("Se tardo: " + duration / 1000.0 + " segundos en guardar " + numeroPokemones + " pokemones");
        System.out.println((duration / 1000.0) / 60 + " minutos");
        System.out.println("------------------------------lll--------------------------------");
    }
}