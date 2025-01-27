package ec.edu.uce.pokedexRS.handler;

import ec.edu.uce.pokedexRS.api.PokemonApiClient;
import ec.edu.uce.pokedexRS.model.Generation;
import ec.edu.uce.pokedexRS.repository.GenerationRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerationProcessor {
    @Autowired
    private GenerationRepository generationRepository;
    @Autowired
    private PokemonApiClient apiClient;

    public GenerationProcessor() {

    }

    public Generation processGeneration(String generationUrl) {
        // Obtener detalles de la generación desde la API
        JSONObject generationDetails = apiClient.getPokemonDetails(generationUrl);
        // Extraer el nombre de la generación
        String generationName = generationDetails.getJSONObject("generation").getString("name");
        // Buscar si la generación ya está en la base de datos
        Generation generation = generationRepository.findByName(generationName).orElse(new Generation());
        // Establecer el nombre de la generación
        generation.setName(generationName);
        // Guardar la generación en la base de datos si no existe
        generationRepository.save(generation);
        return generation;
    }
}