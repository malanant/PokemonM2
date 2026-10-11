package com.example.pokemonm2.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Pokemons")
public class PokemonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "nom", length = 50, nullable = false)
    private String nom;

    // 'Feu', 'Eau', 'Électrik'...
    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @Column(name = "niveau", nullable = false)
    private int niveau;

    // plusieurs pokemons pour un seul dresseur
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dresseur")
    private DresseurEntity dresseur;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public DresseurEntity getDresseur() {
        return dresseur;
    }

    public void setDresseur(DresseurEntity dresseur) {
        this.dresseur = dresseur;
    }
}
