package com.example.pokemonm2.controler;

import com.example.pokemonm2.services.IDresseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dresseurs")
public class DresseurController {

    @Autowired
    private IDresseurService service;

    @GetMapping("{id}")
    ResponseEntity<Object> get(@PathVariable Long id) {

        // on verifie si le dresseur existe sinon on s'arrete
        if (!service.exist(id))
            return new ResponseEntity<>("Le dresseur n'existe pas", HttpStatusCode.valueOf(201));
        return new ResponseEntity<>(service.get(id), HttpStatusCode.valueOf(200));
    }
}
