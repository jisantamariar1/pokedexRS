package ec.edu.uce.pokedexRS.service;

import ec.edu.uce.pokedexRS.model.Evolution;
import ec.edu.uce.pokedexRS.model.Pokemon;
import ec.edu.uce.pokedexRS.repository.EvolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvolutionService {

    @Autowired
    private EvolutionRepository evolutionRepository;

    // del pokemon base obtener las evoluciones
    public List<Evolution> getEvolutionsByPokemon(Pokemon basePokemon) {
        return evolutionRepository.findByBasePokemon(basePokemon);
    }
}