package ec.edu.uce.pokedexRS.view;


import ec.edu.uce.pokedexRS.model.Evolution;
import ec.edu.uce.pokedexRS.model.Pokemon;
import ec.edu.uce.pokedexRS.service.PokemonService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import java.util.List;


@Controller
public class EvolutionsController {
    @Autowired
    private PokemonService pokemonService;
    @FXML
    private ImageView basePoke;

    @FXML
    private ImageView envolPoke;
    @FXML
    private Label evolutionInfoLabel, nameBase, nameBase1;  // Usando javafx.scene.control.Label


    @FXML
    void showHello(ActionEvent event) {

    }
    private Pokemon selectedPokemon;
    private List<Evolution> evolutions;

    // pasa el pokemon y las evoluciones de hellocontroller
    public void setPokemonForEvolution(Pokemon selectedPokemon, List<Evolution> evolutions) {
        this.selectedPokemon = selectedPokemon;
        this.evolutions = evolutions;
        displayEvolutionInfo();
    }

    private void displayEvolutionInfo() {
        // Primero, verifica si hay alguna evolución disponible
        if (evolutions != null && !evolutions.isEmpty()) {
            Evolution evolution = evolutions.get(0); // Tomamos la primera evolución

            // Mostrar las imágenes de los Pokémon base y evolucionado
            basePoke.setImage(new Image(selectedPokemon.getSpriteUrl()));

            // En este caso, asumimos que la evolución ya tiene el Pokémon evolucionado
            envolPoke.setImage(new Image(evolution.getEvolvedPokemon().getSpriteUrl()));

            // Mostrar el nombre de los Pokémon
            evolutionInfoLabel.setText("La evolución de " + selectedPokemon.getName() + " es " +
                    evolution.getEvolvedPokemon().getName());
            nameBase.setText(selectedPokemon.getName());
            nameBase1.setText(evolution.getEvolvedPokemon().getName());
        } else {
            // Si no hay evolución, muestra un mensaje adecuado
            evolutionInfoLabel.setText("Este Pokémon no tiene evolución.");
        }
    }
}
