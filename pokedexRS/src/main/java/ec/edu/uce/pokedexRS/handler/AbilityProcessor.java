package ec.edu.uce.pokedexRS.handler;

import ec.edu.uce.pokedexRS.api.PokemonApiClient;
import ec.edu.uce.pokedexRS.model.Ability;
import ec.edu.uce.pokedexRS.repository.AbilityRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class AbilityProcessor {
    @Autowired
    private AbilityRepository abilityRepository;
    @Autowired
    private PokemonApiClient apiClient;

    public AbilityProcessor() {
    }

    public Set<Ability> processAbilities(JSONArray abilitiesArray) {
        Set<Ability> abilities = new HashSet<>();
        Set<String> processedAbilities = new HashSet<>(); // Para evitar duplicados

        for (int i = 0; i < abilitiesArray.length(); i++) {
            String abilityName = abilitiesArray.getJSONObject(i).getJSONObject("ability").getString("name");
            // Si ya hemos procesado esta habilidad, evitamos agregarla de nuevo
            if (processedAbilities.contains(abilityName)) {
                continue;
            }
            // Buscar si la habilidad ya existe en la base de datos
            Optional<Ability> existingAbility = abilityRepository.findByName(abilityName);
            if (existingAbility.isPresent()) {
                abilities.add(existingAbility.get()); // Reutilizar la existente
            } else {
                try {
                    // Obtener detalles desde la API
                    String abilityUrl = abilitiesArray.getJSONObject(i).getJSONObject("ability").getString("url");
                    JSONObject abilityDetails = apiClient.getPokemonDetails(abilityUrl);
                    String description = extractDescription(abilityDetails);
                    // Crear y guardar la nueva habilidad
                    Ability newAbility = new Ability();
                    newAbility.setName(abilityName);
                    newAbility.setDescription(description);
                    abilityRepository.save(newAbility); // Guardar en la BD solo si es nueva
                    abilities.add(newAbility);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            processedAbilities.add(abilityName); // Marcar habilidad como procesada
        }
        return abilities;
    }

    private String extractDescription(JSONObject abilityDetails) {
        JSONArray flavorTextEntries = abilityDetails.optJSONArray("flavor_text_entries");
        if (flavorTextEntries != null) {
            for (int i = 0; i < flavorTextEntries.length(); i++) {
                JSONObject entry = flavorTextEntries.getJSONObject(i);
                if (entry.getJSONObject("language").getString("name").equals("es")) {
                    return entry.getString("flavor_text");
                }
            }
        }
        return "No description available.";
    }
}
