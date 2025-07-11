package br.com.cadeira.controle.vitrium.vitrium.controllers;

import br.com.cadeira.controle.vitrium.vitrium.dto.AdicionaCadeiraDTO;
import br.com.cadeira.controle.vitrium.vitrium.dto.ListaCadeirasDTO;
import br.com.cadeira.controle.vitrium.vitrium.servicies.CadeiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<ListaCadeirasDTO>> findAll() {
        var cadeiras = cadeiraService.findAll();
        return ResponseEntity.ok(cadeiras);
    }

    @PostMapping
    public void AddCadeira(@RequestBody AdicionaCadeiraDTO dto) {
        var cadeiras = cadeiraService.addCadeira(dto);
    }
}
