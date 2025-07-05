package br.com.cadeira.controle.vitrium.vitrium.controllers;

import br.com.cadeira.controle.vitrium.vitrium.dto.ListAllDTO;
import br.com.cadeira.controle.vitrium.vitrium.servicies.CadeiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/cadeira")
public class CadeiraController {

    @Autowired
    private final CadeiraService cadeiraService;

    public CadeiraController(CadeiraService service) {
        this.cadeiraService = service;
    }


    @GetMapping
    public List<ListAllDTO> findAll() {
        var cadeiras = cadeiraService.findAll();
        return cadeiras;
    }

}
