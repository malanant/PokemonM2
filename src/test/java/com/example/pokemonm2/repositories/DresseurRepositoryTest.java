package com.example.pokemonm2.repositories;

import com.example.pokemonm2.entities.DresseurEntity;
import com.example.pokemonm2.entities.PokemonEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DresseurRepositoryTest {

    @Autowired
    private DresseurRepository repository;

    @Test
    void should_return_3_dresseurs_when_data_sql_is_loaded() {
        // Arrange : deja fait par le script data.sql

        // Act
        List<DresseurEntity> list = repository.findAll();

        // Assert
        assertEquals(3, list.size());
    }

    @Test
    void should_return_sacha_when_dresseur_id_is_1() {
        // Arrange : deja fait par le script data.sql

        // Act
        Optional<DresseurEntity> dresseur = repository.findById(1L);

        // Assert
        assertTrue(dresseur.isPresent());
        assertEquals("Sacha", dresseur.get().getNom());
        assertEquals("Kanto", dresseur.get().getRegion());
    }

    @Test
    void should_return_3_pokemons_when_dresseur_is_sacha() {
        // Arrange : deja fait par le script data.sql
        DresseurEntity sacha = repository.findById(1L).get();

        // Act
        List<PokemonEntity> pokemons = sacha.getPokemons();
        Set<String> noms = pokemons.stream().map(PokemonEntity::getNom).collect(Collectors.toSet());

        // Assert
        assertEquals(3, pokemons.size());
        assertEquals(Set.of("Pikachu", "Dracaufeu", "Carapuce"), noms);
    }

    @Test
    void should_return_no_pokemon_when_dresseur_is_pierre() {
        // Arrange : deja fait par le script data.sql
        DresseurEntity pierre = repository.findById(3L).get();

        // Act
        List<PokemonEntity> pokemons = pierre.getPokemons();

        // Assert
        assertEquals("Pierre", pierre.getNom());
        assertEquals(0, pokemons.size());
    }

    @Test
    void should_return_empty_when_dresseur_id_does_not_exist() {
        // Arrange : aucun dresseur n'a l'id 99

        // Act
        Optional<DresseurEntity> dresseur = repository.findById(99L);
        boolean exist = repository.existsById(99L);

        // Assert
        assertFalse(dresseur.isPresent());
        assertFalse(exist);
    }

    @Test
    void should_persist_dresseur_when_save_is_called() {
        // Arrange
        DresseurEntity nouveau = new DresseurEntity();
        nouveau.setNom("Jessie");
        nouveau.setRegion("Johto");

        // Act
        DresseurEntity saved = repository.save(nouveau);

        // Assert
        assertNotNull(saved.getId());
        assertEquals(4, repository.count());
        assertEquals("Jessie", repository.findById(saved.getId()).get().getNom());
    }
}
