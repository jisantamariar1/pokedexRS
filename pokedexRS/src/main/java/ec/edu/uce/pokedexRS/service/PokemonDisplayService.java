package ec.edu.uce.pokedexRS.service;

import ec.edu.uce.pokedexRS.model.Evolution;
import ec.edu.uce.pokedexRS.model.Pokemon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PokemonDisplayService {
    @Autowired
    private PokemonService pokemonService;
    @Autowired
    private EvolutionService evolutionService;

    // Método para obtener los datos del Pokémon (por nombre o ID)
    public Optional<Pokemon> getPokemonData(String selectedPokemonName, String inputId) {
        Optional<Pokemon> selectedPokemonOpt = Optional.empty();

        if (selectedPokemonName != null && !selectedPokemonName.isEmpty()) {
            selectedPokemonOpt = pokemonService.getPokemonByName(selectedPokemonName);
        } else if (inputId != null && !inputId.isEmpty()) {
            try {
                Long id = Long.parseLong(inputId);
                selectedPokemonOpt = pokemonService.getPokemonById(id);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ID no válido. Debe ser un número.");
            }
        }

        return selectedPokemonOpt;
    }

    // evoluciones del pokemon
    public List<Evolution> getEvolutions(Pokemon selectedPokemon) {
        return evolutionService.getEvolutionsByPokemon(selectedPokemon);
    }

    // cargar la imagen del pokemon
    public void loadPokemonImage(String imageUrl, ImageView imagePoke) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                imagePoke.setImage(new Image(imageUrl, true));
            } catch (Exception e) {
                imagePoke.setImage(new Image(getClass().getResourceAsStream("/images/images.jpg")));
            }
        } else {
            imagePoke.setImage(new Image(getClass().getResourceAsStream("/images/images.jpg")));
        }
    }
}
