package com.example.pokemonm2.services;

import com.example.pokemonm2.dto.DresseurDto;
import com.example.pokemonm2.entities.DresseurEntity;

public interface IDresseurService {
    /**
     * Cette fonction transforme une entité en DTO
     * @param entity
     * @return
     */
    DresseurDto toDto(DresseurEntity entity);

    /**
     * Calcule le niveau du dresseur selon la règle métier
     * @param dresseurId
     * @return
     */
    int calculerNiveauDresseur(Long dresseurId);

    DresseurDto get(Long id);

    Boolean exist(Long id);
}
