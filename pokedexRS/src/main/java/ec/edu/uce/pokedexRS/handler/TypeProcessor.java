package ec.edu.uce.pokedexRS.handler;

import ec.edu.uce.pokedexRS.api.PokemonApiClient;
import ec.edu.uce.pokedexRS.model.Type;
import ec.edu.uce.pokedexRS.repository.TypeRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class TypeProcessor {
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private PokemonApiClient apiClient;

    public TypeProcessor() {
    }

    public Set<Type> processTypes(JSONArray typesArray) {
        Set<Type> types = new HashSet<>();
        for (int i = 0; i < typesArray.length(); i++) {
            String typeName = typesArray.getJSONObject(i).getJSONObject("type").getString("name");
            // Buscar si el tipo ya existe en la base de datos
            Optional<Type> existingType = typeRepository.findByName(typeName);
            if (existingType.isPresent()) {
                types.add(existingType.get()); // Reutilizar el existente
            } else {
                // Si no existe, obtener detalles de la API
                String typeUrl = typesArray.getJSONObject(i).getJSONObject("type").getString("url");
                JSONObject typeDetails = apiClient.getPokemonDetails(typeUrl);
                String spriteUrl = extractSpriteUrl(typeDetails);
                // Crear y guardar el nuevo tipo
                Type newType = new Type();
                newType.setName(typeName);
                newType.setSpriteUrl(spriteUrl);
                typeRepository.save(newType);
                types.add(newType);
            }
        }
        return types;
    }

    //metodo para obtener el extractSpriteUrl del tipo
    private String extractSpriteUrl(JSONObject typeDetails) {
        return typeDetails.getJSONObject("sprites").getJSONObject("generation-viii").getJSONObject("sword-shield").getString("name_icon");
    }
}
