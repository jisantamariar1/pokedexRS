package ec.edu.uce.pokedexRS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    //crea una instancia de RestTemplate que luego puede ser usada para hacer solicitudes HTTP
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
