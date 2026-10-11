package com.example.pokemonm2.repositories;

import com.example.pokemonm2.entities.DresseurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DresseurRepository extends JpaRepository<DresseurEntity, Long> {
}
