package ec.edu.uce.pokedexRS.handler;

import ec.edu.uce.pokedexRS.api.PokemonApiClient;
import ec.edu.uce.pokedexRS.model.Species;
import ec.edu.uce.pokedexRS.repository.SpeciesRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpeciesProcessor {
    @Autowired
    private SpeciesRepository speciesRepository;
    @Autowired
    private PokemonApiClient apiClient;

    public SpeciesProcessor() {
    }

    public Species processSpecies(String speciesUrl) {
        // Obtener los detalles de la especie desde la API
        JSONObject speciesDetails = apiClient.getPokemonDetails(speciesUrl);
        // Obtener el género en español
        String genus = getSpanishGenus(speciesDetails.getJSONArray("genera"));
        // Buscar si la especie ya existe en la base de datos
        Species species = speciesRepository.findByName(genus).orElse(new Species());
        // Asignar los valores
        species.setName(genus);
        // Guardar la especie si es nueva
        speciesRepository.save(species);
        return species;
    }

    //Busca el género del Pokémon en español dentro del array "genera".
    private String getSpanishGenus(JSONArray generaArray) {
        for (int i = 0; i < generaArray.length(); i++) {
            JSONObject genusEntry = generaArray.getJSONObject(i);
            JSONObject language = genusEntry.getJSONObject("language");

            if (language.getString("name").equals("es")) {
                return genusEntry.getString("genus"); // Retorna el género en español
            }
        }
        return "Género desconocido"; // Si no encuentra en español, retorna un mensaje genérico
    }
}
