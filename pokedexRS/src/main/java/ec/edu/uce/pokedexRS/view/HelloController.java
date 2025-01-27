package ec.edu.uce.pokedexRS.view;

import ec.edu.uce.pokedexRS.model.*;
import ec.edu.uce.pokedexRS.service.EvolutionService;
import ec.edu.uce.pokedexRS.service.PokemonService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ec.edu.uce.pokedexRS.PokedexRoseroSantamariaApplication.applicationContext;


@Controller
public class HelloController {
    @Autowired
    private PokemonService pokemonService;
    @Autowired
    private EvolutionService evolutionService;
    // Definición de elementos de la interfaz gráfica vinculados a los controles FXML.
    @FXML
    private ComboBox<String> habita, speci, generati, poke, habiliPoke;
    @FXML
    private ImageView imagePoke;
    @FXML
    private Label NamePoke, heighpoke, weightpoke, nameCombo, information;
    @FXML
    private TextArea descriptionpoke, descrip;
    @FXML
    private BarChart<String, Number> stadistics;
    @FXML
    private TableView<Pokemon> tablePoke;
    @FXML
    private TableColumn<Pokemon, Long> idColumn;
    @FXML
    private TableColumn<Pokemon, String> nombreColumn, tipoColumn;
    @FXML
    private TextField searchpoke;
    @FXML
    private Button searchId ;
    //variables para la musica
    private MediaPlayer mediaPlayer;
    private boolean isPlaying=false;// rastrear el estado actual
    // Llama direcetamente en el fxml.
    @FXML
    public void initialize() {

        loadSpeciesComboBox();  // Cargar especies en el ComboBox.
        loadHabitatComboBox();  // Cargar hábitats en el ComboBox.
        loadPokemonComboBox();  // Cargar nombres de Pokémon en el ComboBox.
        loadGenerationComboBox();  // Cargar generaciones en el ComboBox.
        loadHabilityComboBox();  // Cargar habilidades en el ComboBox.
        configureTableView();  // Configurar las columnas de la tabla.
        loadAllPokemons();  // Cargar todos los Pokémon en la tabla.

        // Configuración de eventos para cada ComboBox.
        poke.setOnAction(event -> loadPokemonData());
        speci.setOnAction(event -> loadPokemonBySpecies());
        habita.setOnAction(event -> loadPokemonByHabitat());
        generati.setOnAction(event -> loadPokemonByGeneration());
        habiliPoke.setOnAction(event -> loadPokemonByHability());
        searchId.setOnAction(event->searchPokemonId());

    }


    //evento para el boton evolucion llamar a la ventaba evolucion y capturar la informacion del combobox
    @FXML
    void showEvolution(ActionEvent event) throws IOException {
        String selectedPokemonName = poke.getValue();

        if (selectedPokemonName != null && !selectedPokemonName.isEmpty()) {
            Optional<Pokemon> selectedPokemonOpt = pokemonService.getPokemonByName(selectedPokemonName);

            if (selectedPokemonOpt.isPresent()) {
                Pokemon selectedPokemon = selectedPokemonOpt.get();
                List<Evolution> evolutions = evolutionService.getEvolutionsByPokemon(selectedPokemon);

                if (evolutions.isEmpty()) {
                    showAlert("Advertencia", "Este Pokémon no tiene evoluciones.");
                } else {
                    openEvolutionWindow(selectedPokemon, evolutions);
                }
            } else {
                showAlert("Error", "No se pudo encontrar el Pokémon seleccionado.");
            }
        }
    }
    //evolucion por id
    @FXML
    void onSearchById(ActionEvent event) throws IOException {
        String inputId = searchpoke.getText();

        if (inputId != null && !inputId.isEmpty()) {
            try {
                Long id = Long.parseLong(inputId);
                Optional<Pokemon> selectedPokemonOpt = pokemonService.getPokemonById(id);

                if (selectedPokemonOpt.isPresent()) {
                    Pokemon selectedPokemon = selectedPokemonOpt.get();
                    List<Evolution> evolutions = evolutionService.getEvolutionsByPokemon(selectedPokemon);

                    if (evolutions.isEmpty()) {
                        showAlert("Advertencia", "Este Pokémon no tiene evoluciones.");
                    } else {
                        openEvolutionWindow(selectedPokemon, evolutions);
                    }
                } else {
                    showAlert("Error", "No se encontró ningún Pokémon con ese ID.");
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "ID no válido. Debe ser un número.");
            }
        } else {
            showAlert("Error", "Por favor, ingrese un ID válido.");
        }
    }

    //llamar a la segunda ventana para la evolucion
    private void openEvolutionWindow(Pokemon selectedPokemon, List<Evolution> evolutions) throws IOException {
        // Cargar la ventana de evolución y pasarle los datos
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/evolutions.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean); // Inyectar el controller desde Spring
        Scene scene = new Scene(fxmlLoader.load(), 533, 375); // Dimensiones de la ventana de evolución
        Stage stage = new Stage();
        stage.setTitle("Evolución");
        stage.setScene(scene);
        stage.show();

        // Obtener el controlador de la nueva ventana y pasarle los datos
        EvolutionsController controller = fxmlLoader.getController();
        controller.setPokemonForEvolution(selectedPokemon, evolutions);
    }
    // cargar la imagen del pokemon.
    private void loadPokemonImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                imagePoke.setImage(new Image(imageUrl, true));
            } catch (Exception e) {
                // Si falla, cargar una imagen por defecto.
                imagePoke.setImage(new Image(getClass().getResourceAsStream("/images/images.jpg")));
            }
        } else {
            imagePoke.setImage(new Image(getClass().getResourceAsStream("/images/images.jpg")));
        }
    }

    // Configuración de la tabla con las columnas id, nombre y tipos.
    private void configureTableView() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id")); // Asigna la propiedad `id` a la columna.
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("name")); // Asigna la propiedad `name` a la columna.

        tipoColumn.setCellValueFactory(data -> {
            Pokemon pokemon = data.getValue();
            if (pokemon.getTypes() != null) {
                String types = pokemon.getTypes().stream()
                        .map(Type::getName)
                        .sorted() // Orden ascendente de los tipos.
                        .collect(Collectors.joining(", "));
                return new SimpleStringProperty(types);
            }
            return new SimpleStringProperty("N/A");
        });

        tipoColumn.setCellFactory(column -> {
            return new TableCell<Pokemon, String>() {
                @Override
                protected void updateItem(String types, boolean empty) {
                    super.updateItem(types, empty);

                    if (empty || types == null || types.isEmpty()) {
                        setGraphic(null);
                    } else {
                        Pokemon pokemon = getTableView().getItems().get(getIndex());
                        Set<Type> typeSet = pokemon.getTypes();

                        HBox hbox = new HBox(5); // Espaciado de 5 entre imágenes.
                        for (Type type : typeSet) {
                            String spriteUrl = type.getSpriteUrl();
                            if (spriteUrl != null && !spriteUrl.isEmpty()) {
                                Image image = new Image(spriteUrl);
                                ImageView imageView = new ImageView(image);
                                imageView.setFitHeight(12);
                                imageView.setFitWidth(50);
                                hbox.getChildren().add(imageView);
                            }
                        }
                        setGraphic(hbox);
                    }
                }
            };
        });
    }


    //cargar los datos por la categoria por defecto
    private void loadAllPokemons() {
        List<Pokemon> pokemons = pokemonService.getAllPokemons();
        ObservableList<Pokemon> observableList = FXCollections.observableArrayList(pokemons);
        nameCombo.setText("Pokemon");
        descrip.setVisible(false);
        information.setVisible(false);
        tablePoke.setItems(observableList);
    }
    //Metodo para buscar por Id al pokemon
    @FXML
    private void searchPokemonId() {
        // Obtener el ID del campo de texto
        String inputId = searchpoke.getText();

        // Verificar si el campo de texto no está vacío
        if (inputId != null && !inputId.isEmpty()) {
            try {
                // Convertir el ID a un número largo (long)
                Long id = Long.parseLong(inputId);

                // Validar que el ID sea positivo
                if (id <= 0) {
                    showAlert("Advertencia", "El ID debe ser un número positivo.");
                    return;
                }

                // Buscar el Pokémon por ID usando el servicio
                Optional<Pokemon> pokemonOptional = pokemonService.getPokemonById(id);
                if (pokemonOptional.isPresent()) {
                    // Si el Pokémon es encontrado, mostrar sus datos
                    displayPokemonData(pokemonOptional.get());
                } else {
                    // Si no se encuentra el Pokémon, mostrar un mensaje adecuado
                    showAlert("Advertencia", "Pokémon no encontrado.");
                }
            } catch (NumberFormatException e) {
                // Si el ID no es un número válido, mostrar un mensaje de error
                showAlert("Error", "ID no válido. Debe ser un número.");
            }
        } else {
            // Si el campo de texto está vacío, mostrar un mensaje
            showAlert("Advertencia", "Por favor, ingrese un ID.");
        }
    }

    private void showAlert(String title, String message) {
        // Crear una alerta de tipo WARNING (puedes cambiarlo a ERROR o INFORMATION según corresponda)
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);  // Sin encabezado (puedes agregar uno si quieres)
        alert.setContentText(message);  // El mensaje que se mostrará en la ventana

        // Mostrar la alerta y esperar que el usuario la cierre
        alert.showAndWait();
    }

    // Métodos para cargar datos por categoría seleccionada.
    @FXML
    private void loadPokemonBySpecies() {
        String selectedSpecies = speci.getValue();
        nameCombo.setText("Especie");
        descrip.setVisible(false);
        information.setVisible(false);
        List<Pokemon> pokemons = pokemonService.getPokemonsBySpecies(selectedSpecies);
        tablePoke.setItems(FXCollections.observableArrayList(pokemons));
    }

    @FXML
    private void loadPokemonByHabitat() {
        String selectedHabitat = habita.getValue();
        nameCombo.setText("Hábitat");
        descrip.setVisible(false);
        information.setVisible(false);
        List<Pokemon> pokemons = pokemonService.getPokemonsByHabitat(selectedHabitat);
        tablePoke.setItems(FXCollections.observableArrayList(pokemons));
    }

    @FXML
    private void loadPokemonByGeneration() {
        String selectedGeneration = generati.getValue();
        nameCombo.setText("Generación");
        descrip.setVisible(false);
        information.setVisible(false);
        List<Pokemon> pokemons = pokemonService.getPokemonsByGeneration(selectedGeneration);
        tablePoke.setItems(FXCollections.observableArrayList(pokemons));
    }

    @FXML
    private void loadPokemonByHability() {
        String selectedAbility = habiliPoke.getValue();
        nameCombo.setText("Habilidad");
        // Aquí obtienes la descripción de la habilidad seleccionada.
        String abilityDescription = pokemonService.getAbilityDescription(selectedAbility);
        descrip.setVisible(true);
        descrip.setWrapText(true);
        information.setVisible(true);
        descrip.setText(abilityDescription);
        List<Pokemon> pokemons = pokemonService.getPokemonsByAbility(selectedAbility);
        tablePoke.setItems(FXCollections.observableArrayList(pokemons));
    }

    @FXML
    public void loadPokemonData() {
        String selectedPokemonName = poke.getValue();
        Optional<Pokemon> selectedPokemon = pokemonService.getPokemonByName(selectedPokemonName);

        if (selectedPokemon.isPresent()) {
            displayPokemonData(selectedPokemon.get());
        } else {
            displayDefaultPokemonData("Pokémon no encontrado.");
        }
    }

    // Métodos para cargar datos en ComboBoxes en orden ascendente.
    private void loadSpeciesComboBox() {
        List<Species> species = pokemonService.getAllSpecies();
        speci.setItems(FXCollections.observableArrayList(
                species.stream()
                        .map(Species::getName)
                        .sorted() // Orden ascendente.
                        .collect(Collectors.toList())
        ));
    }

    private void loadHabitatComboBox() {
        List<Habitat> habitats = pokemonService.getAllHabitats();
        habita.setItems(FXCollections.observableArrayList(
                habitats.stream()
                        .map(Habitat::getName)
                        .sorted() // Orden ascendente.
                        .collect(Collectors.toList())
        ));
    }

    private void loadPokemonComboBox() {
        List<Pokemon> pokemons = pokemonService.getAllPokemons();
        poke.setItems(FXCollections.observableArrayList(
                pokemons.stream()
                        .map(Pokemon::getName)
                        .sorted() // Orden ascendente.
                        .collect(Collectors.toList())
        ));
    }

    private void loadGenerationComboBox() {
        List<Generation> generations = pokemonService.getAllGenerations();
        generati.setItems(FXCollections.observableArrayList(
                generations.stream()
                        .map(Generation::getName)
                        .sorted() // Orden ascendente.
                        .collect(Collectors.toList())
        ));
    }

    private void loadHabilityComboBox() {
        List<Ability> abilities = pokemonService.getAllAbilities();
        habiliPoke.setItems(FXCollections.observableArrayList(
                abilities.stream()
                        .map(Ability::getName)
                        .sorted() // Orden ascendente.
                        .collect(Collectors.toList())
        ));
    }

    // Métodos para mostrar datos del Pokémon seleccionado.
    private void displayPokemonData(Pokemon pokemon) {
        NamePoke.setText(pokemon.getName());
        loadPokemonImage(pokemon.getSpriteUrl());
        weightpoke.setText(String.valueOf(pokemon.getWeight()));
        heighpoke.setText(String.valueOf(pokemon.getHeight()));
        descriptionpoke.setText(pokemon.getDescription());
        descriptionpoke.setWrapText(true);
        descrip.setVisible(false);
        information.setVisible(false);
        loadPokemonStadistics(pokemon);
    }

    private void displayDefaultPokemonData(String message) {
        descriptionpoke.setText(message);
        NamePoke.setText("Nombre no disponible.");
        heighpoke.setText("");
        weightpoke.setText("");
    }

    // cargar la estadisticas grafico de barras
    private void loadPokemonStadistics(Pokemon selectedPokemon) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Estadísticas");

        series.getData().add(new XYChart.Data<>("Hp", selectedPokemon.getHp()));
        series.getData().add(new XYChart.Data<>("Ataque", selectedPokemon.getAttack()));
        series.getData().add(new XYChart.Data<>("Defensa", selectedPokemon.getDefense()));
        series.getData().add(new XYChart.Data<>("Velocidad", selectedPokemon.getSpeed()));
        series.getData().add(new XYChart.Data<>("Ataque esp.", selectedPokemon.getSpecialAttack()));
        series.getData().add(new XYChart.Data<>("Defensa esp.", selectedPokemon.getSpecialDefense()));

        stadistics.getData().clear();
        stadistics.getData().add(series);
    }
}

