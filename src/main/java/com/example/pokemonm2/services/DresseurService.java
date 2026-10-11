package com.example.pokemonm2.services;

import com.example.pokemonm2.dto.DresseurDto;
import com.example.pokemonm2.entities.DresseurEntity;
import com.example.pokemonm2.entities.PokemonEntity;
import com.example.pokemonm2.repositories.DresseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DresseurService implements IDresseurService {

    // constantes de la règle métier
    private static final int NIVEAU_SANS_POKEMON = 1;
    private static final int NB_TYPES_POUR_BONUS_DIVERSITE = 3;
    private static final int BONUS_DIVERSITE = 20;
    private static final int NIVEAU_POKEMON_ELITE = 50;
    private static final int BONUS_ELITE = 10;

    @Autowired
    private DresseurRepository repository;

    @Override
    public DresseurDto toDto(DresseurEntity entity) {
        DresseurDto dto = new DresseurDto();
        dto.setNomDresseur(entity.getNom());

        // on recupere le nombre de pokemons
        List<PokemonEntity> pokemons = entity.getPokemons();
        dto.setNombrePokemons(pokemons == null ? 0 : pokemons.size());

        // on calcule le niveau du dresseur
        dto.setNiveauDresseur(calculerNiveau(entity));
        return dto;
    }

    @Override
    public int calculerNiveauDresseur(Long dresseurId) {
        return calculerNiveau(repository.findById(dresseurId).get());
    }

    @Override
    public DresseurDto get(Long id) {
        return toDto(repository.findById(id).get());
    }

    @Override
    public Boolean exist(Long id) {
        return repository.existsById(id);
    }

    private int calculerNiveau(DresseurEntity dresseur) {
        List<PokemonEntity> pokemons = dresseur.getPokemons();

        // cas limite : le dresseur n'a aucun pokemon
        if (pokemons == null || pokemons.isEmpty()) {
            return NIVEAU_SANS_POKEMON;
        }

        int niveau = 0;
        Set<String> types = new HashSet<>();

        for (PokemonEntity pokemon : pokemons) {
            // base de calcul : somme exacte des niveaux
            niveau += pokemon.getNiveau();

            // bonus pokemon elite : +10 par pokemon de niveau >= 50
            if (pokemon.getNiveau() >= NIVEAU_POKEMON_ELITE) {
                niveau += BONUS_ELITE;
            }

            types.add(pokemon.getType());
        }

        // bonus diversite de types : +20 si au moins 3 types distincts
        if (types.size() >= NB_TYPES_POUR_BONUS_DIVERSITE) {
            niveau += BONUS_DIVERSITE;
        }

        return niveau;
    }
}
