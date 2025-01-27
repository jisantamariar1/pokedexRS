package ec.edu.uce.pokedexRS.handler;

import ec.edu.uce.pokedexRS.model.Evolution;
import ec.edu.uce.pokedexRS.model.Pokemon;
import ec.edu.uce.pokedexRS.repository.EvolutionRepository;
import ec.edu.uce.pokedexRS.repository.PokemonRepository;
import ec.edu.uce.pokedexRS.api.PokemonApiClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EvolutionProcessor {
    @Autowired
    private PokemonRepository pokemonRepository;
    @Autowired
    private EvolutionRepository evolutionRepository;
    @Autowired
    private PokemonApiClient apiClient;

    public EvolutionProcessor() {
    }

    // Procesa las evoluciones de un Pokémon base
    public Set<Evolution> processEvolutions(String speciesUrl, String currentPokemonName) {
        Set<Evolution> evolutions = new HashSet<>();
        // Obtener los datos de la especie del Pokémon
        JSONObject speciesData = apiClient.getPokemonDetails(speciesUrl);
        String evolutionChainUrl = speciesData.getJSONObject("evolution_chain").getString("url");
        // Obtener la cadena de evolución de la PokeAPI
        JSONObject evolutionData = apiClient.getPokemonDetails(evolutionChainUrl);
        JSONObject chain = evolutionData.getJSONObject("chain");
        // Extraer los nombres de las evoluciones
        List<String> evolutionNames = extractEvolutionNames(chain);
        evolutionNames.remove(currentPokemonName);  // Eliminar el nombre del Pokémon base
        // Obtener el Pokémon base de la base de datos
        Optional<Pokemon> currentPokemonOpt = pokemonRepository.findByName(currentPokemonName);
        if (currentPokemonOpt.isPresent()) {
            Pokemon currentPokemon = currentPokemonOpt.get();
            // Verificar y guardar las relaciones de evolución
            for (String evolvedName : evolutionNames) {
                Optional<Pokemon> evolvedPokemonOpt = pokemonRepository.findByName(evolvedName);
                if (evolvedPokemonOpt.isPresent()) {
                    Pokemon evolvedPokemon = evolvedPokemonOpt.get();
                    // Verificar si la relación de evolución ya existe
                    Optional<Evolution> existingEvolution = evolutionRepository.findByBasePokemonAndEvolvedPokemon(currentPokemon, evolvedPokemon);
                    if (existingEvolution.isEmpty()) {
                        // Si no existe, crear una nueva relación de evolución
                        Evolution evolution = new Evolution();
                        evolution.setBasePokemon(currentPokemon);
                        evolution.setEvolvedPokemon(evolvedPokemon);
                        evolutions.add(evolution);
                        // Guardar la nueva relación de evolución en la base de datos
                        evolutionRepository.save(evolution);
                        // Crear la relación inversa (de Pokémon evolucionado al base)
                        Evolution reverseEvolution = new Evolution();
                        reverseEvolution.setBasePokemon(evolvedPokemon);
                        reverseEvolution.setEvolvedPokemon(currentPokemon);
                        evolutionRepository.save(reverseEvolution);
                    }
                }
            }
        }
        return evolutions;
    }

    // Extrae los nombres de las evoluciones de la cadena de evolución
    private List<String> extractEvolutionNames(JSONObject chain) {
        List<String> evolutionNames = new ArrayList<>();
        // Agregar el nombre del Pokémon actual a la lista
        evolutionNames.add(chain.getJSONObject("species").getString("name"));
        // Recorrer las evoluciones
        for (int i = 0; i < chain.getJSONArray("evolves_to").length(); i++) {
            JSONObject evolutionJson = chain.getJSONArray("evolves_to").getJSONObject(i);
            evolutionNames.addAll(extractEvolutionNames(evolutionJson));  // Llamada recursiva para obtener evoluciones en cadena
        }
        return evolutionNames;
    }
}

