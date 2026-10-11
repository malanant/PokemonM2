package com.example.pokemonm2.services;

import com.example.pokemonm2.dto.DresseurDto;
import com.example.pokemonm2.entities.DresseurEntity;
import com.example.pokemonm2.entities.PokemonEntity;
import com.example.pokemonm2.repositories.DresseurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DresseurServiceTest {

    @Mock
    private DresseurRepository repository;

    @InjectMocks
    private DresseurService service;

    // ---------- methodes utilitaires pour construire les donnees du test ----------

    private PokemonEntity creerPokemon(String nom, String type, int niveau) {
        PokemonEntity pokemon = new PokemonEntity();
        pokemon.setNom(nom);
        pokemon.setType(type);
        pokemon.setNiveau(niveau);
        return pokemon;
    }

    private DresseurEntity creerDresseur(Long id, String nom, PokemonEntity... pokemons) {
        DresseurEntity dresseur = new DresseurEntity();
        dresseur.setId(id);
        dresseur.setNom(nom);
        dresseur.setRegion("Kanto");
        dresseur.setPokemons(new ArrayList<>(Arrays.asList(pokemons)));
        return dresseur;
    }

    // ---------- calculerNiveauDresseur : cas nominal ----------

    @Test
    void should_return_sum_of_levels_when_dresseur_has_pokemons_without_bonus() {
        // Arrange
        DresseurEntity dresseur = creerDresseur(1L, "Sacha",
                creerPokemon("Salameche", "Feu", 10),
                creerPokemon("Carapuce", "Eau", 20));
        when(repository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = service.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(30, niveau);
        verify(repository).findById(1L);
    }

    // ---------- calculerNiveauDresseur : cas 0 Pokemon ----------

    @Test
    void should_return_1_when_dresseur_has_no_pokemon() {
        // Arrange
        DresseurEntity dresseur = creerDresseur(3L, "Pierre");
        when(repository.findById(3L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = service.calculerNiveauDresseur(3L);

        // Assert
        assertEquals(1, niveau);
    }

    // ---------- calculerNiveauDresseur : bonus diversite de types ----------

    @Test
    void should_apply_diversity_bonus_when_dresseur_has_3_different_types() {
        // Arrange : 10 + 20 + 15 = 45, puis +20 (3 types distincts)
        DresseurEntity dresseur = creerDresseur(1L, "Sacha",
                creerPokemon("Salameche", "Feu", 10),
                creerPokemon("Carapuce", "Eau", 20),
                creerPokemon("Bulbizarre", "Plante", 15));
        when(repository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = service.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(65, niveau);
    }

    @Test
    void should_not_apply_diversity_bonus_when_dresseur_has_only_2_different_types() {
        // Arrange : 3 pokemons mais seulement 2 types distincts -> pas de bonus
        DresseurEntity dresseur = creerDresseur(1L, "Sacha",
                creerPokemon("Salameche", "Feu", 10),
                creerPokemon("Dracaufeu", "Feu", 20),
                creerPokemon("Carapuce", "Eau", 15));
        when(repository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = service.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(45, niveau);
    }

    // ---------- calculerNiveauDresseur : bonus pokemon elite ----------

    @Test
    void should_apply_elite_bonus_when_pokemon_level_is_50() {
        // Arrange : le niveau 50 est la limite incluse (>= 50)
        DresseurEntity dresseur = creerDresseur(1L, "Sacha",
                creerPokemon("Dracaufeu", "Feu", 50));
        when(repository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = service.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(60, niveau);
    }

    @Test
    void should_not_apply_elite_bonus_when_pokemon_level_is_49() {
        // Arrange
        DresseurEntity dresseur = creerDresseur(1L, "Sacha",
                creerPokemon("Dracaufeu", "Feu", 49));
        when(repository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = service.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(49, niveau);
    }

    @Test
    void should_apply_elite_bonus_for_each_elite_pokemon_when_dresseur_has_2_elite_pokemons() {
        // Arrange : 50 + 70 = 120, puis +10 +10 (2 pokemons elite), meme type donc pas de bonus diversite
        DresseurEntity dresseur = creerDresseur(1L, "Sacha",
                creerPokemon("Dracaufeu", "Feu", 50),
                creerPokemon("Arcanin", "Feu", 70));
        when(repository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = service.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(140, niveau);
    }

    // ---------- calculerNiveauDresseur : cumul des bonus ----------

    @Test
    void should_cumulate_all_bonuses_when_dresseur_has_3_types_and_2_elite_pokemons() {
        // Arrange : 55 + 60 + 10 = 125, +20 (2 pokemons elite), +20 (3 types) = 165
        DresseurEntity dresseur = creerDresseur(1L, "Sacha",
                creerPokemon("Pikachu", "Électrik", 55),
                creerPokemon("Dracaufeu", "Feu", 60),
                creerPokemon("Carapuce", "Eau", 10));
        when(repository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        int niveau = service.calculerNiveauDresseur(1L);

        // Assert
        assertEquals(165, niveau);
    }

    // ---------- get / toDto ----------

    @Test
    void should_return_dresseur_dto_when_dresseur_exists() {
        // Arrange : 55 + 36 + 10 = 101, +10 (Pikachu elite), +20 (3 types) = 131
        DresseurEntity dresseur = creerDresseur(1L, "Sacha",
                creerPokemon("Pikachu", "Électrik", 55),
                creerPokemon("Dracaufeu", "Feu", 36),
                creerPokemon("Carapuce", "Eau", 10));
        when(repository.findById(1L)).thenReturn(Optional.of(dresseur));

        // Act
        DresseurDto dto = service.get(1L);

        // Assert
        assertEquals("Sacha", dto.getNomDresseur());
        assertEquals(3, dto.getNombrePokemons());
        assertEquals(131, dto.getNiveauDresseur());
    }

    // ---------- exist ----------

    @Test
    void should_return_true_when_dresseur_exists() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(true);

        // Act
        Boolean exist = service.exist(1L);

        // Assert
        assertTrue(exist);
    }

    @Test
    void should_return_false_when_dresseur_does_not_exist() {
        // Arrange
        when(repository.existsById(99L)).thenReturn(false);

        // Act
        Boolean exist = service.exist(99L);

        // Assert
        assertFalse(exist);
    }
}
