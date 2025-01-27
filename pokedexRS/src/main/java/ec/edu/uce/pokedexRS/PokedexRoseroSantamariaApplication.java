package ec.edu.uce.pokedexRS;

import ec.edu.uce.pokedexRS.api.PokemonApiClient;
import ec.edu.uce.pokedexRS.api.PokemonInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.CompletableFuture;


@SpringBootApplication
public class PokedexRoseroSantamariaApplication extends Application {
	public static ConfigurableApplicationContext applicationContext;

	public static void main(String[] args) {
		//SpringApplication.run(PokedexRoseroSantamariaApplication.class, args);
		launch(); //
	}

	@Bean
	public CommandLineRunner run(PokemonApiClient pokemonApiClient, PokemonInfo pokemonInfo) {
		return args -> {
			// Ejecutar la carga de datos en un hilo separado
			CompletableFuture.runAsync(() -> {
				try {
					pokemonApiClient.fetch(pokemonInfo); // Obtiene y guarda los Pokémon
				} catch (Exception e) {
					e.printStackTrace();
				}
			});


		};
	}

	@Override
	public void start(Stage stage) throws Exception {
		applicationContext = SpringApplication.run(PokedexRoseroSantamariaApplication.class);
		FXMLLoader fxml = new FXMLLoader(getClass().getResource("/hello-view.fxml"));
		fxml.setControllerFactory(applicationContext::getBean);
		Scene scene = new Scene(fxml.load(),692,618);
		stage.setTitle("Pokedex");
		stage.setScene(scene);
		stage.show();
	}
}
