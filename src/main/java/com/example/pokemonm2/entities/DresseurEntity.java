package com.example.pokemonm2.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Dresseurs")
public class DresseurEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "nom", length = 50, nullable = false)
    private String nom;

    @Column(name = "region", length = 50, nullable = false)
    private String region;

    // un dresseur possede plusieurs pokemons (la cle etrangere id_dresseur est dans la table Pokemons)
    @OneToMany(mappedBy = "dresseur", fetch = FetchType.LAZY)
    private List<PokemonEntity> pokemons;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<PokemonEntity> getPokemons() {
        return pokemons;
    }

    public void setPokemons(List<PokemonEntity> pokemons) {
        this.pokemons = pokemons;
    }
}
