package ec.edu.uce.pokedexRS.handler;

import ec.edu.uce.pokedexRS.api.PokemonApiClient;
import ec.edu.uce.pokedexRS.model.Habitat;
import ec.edu.uce.pokedexRS.repository.HabitatRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HabitatProcessor {
    @Autowired
    private HabitatRepository habitatRepository;
    @Autowired
    private PokemonApiClient apiClient;

    public HabitatProcessor() {
    }

    public Habitat processHabitat(String speciesUrl) {
        // Obtener detalles de la especie desde la API
        JSONObject speciesDetails = apiClient.getPokemonDetails(speciesUrl);
        // Verificar si el hábitat está presente en la respuesta
        if (!speciesDetails.has("habitat") || speciesDetails.isNull("habitat")) {
            return null; // No hay hábitat definido para este Pokémon
        }
        // Obtener el nombre del hábitat
        String habitatName = speciesDetails.getJSONObject("habitat").getString("name");
        // Buscar si el hábitat ya existe en la base de datos
        Habitat habitat = habitatRepository.findByName(habitatName).orElse(new Habitat());
        // Establecer el nombre del hábitat
        habitat.setName(habitatName);
        // Guardar el hábitat en la base de datos si no existe
        habitatRepository.save(habitat);
        return habitat;
    }
}