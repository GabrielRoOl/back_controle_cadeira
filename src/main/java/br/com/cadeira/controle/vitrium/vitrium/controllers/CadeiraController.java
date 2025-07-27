package br.com.cadeira.controle.vitrium.vitrium.controllers;

import br.com.cadeira.controle.vitrium.vitrium.dto.AdicionaCadeiraDTO;
import br.com.cadeira.controle.vitrium.vitrium.dto.DetalhamentoAdicionaCadeiraDTO;
import br.com.cadeira.controle.vitrium.vitrium.dto.ListaCadeiraPorIdDTO;
import br.com.cadeira.controle.vitrium.vitrium.dto.ListaCadeirasDTO;
import br.com.cadeira.controle.vitrium.vitrium.entity.Cadeiras;
import br.com.cadeira.controle.vitrium.vitrium.exceptions.ChairAlreadyReturned;
import br.com.cadeira.controle.vitrium.vitrium.servicies.CadeiraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "api/cadeira")
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

    @GetMapping("/{id}")
    public ResponseEntity<ListaCadeiraPorIdDTO> findById(@PathVariable Long id) {
        ListaCadeiraPorIdDTO cadeira = cadeiraService.findById(id);
        return ResponseEntity.ok(cadeira);
    }

    @PostMapping
    public ResponseEntity<DetalhamentoAdicionaCadeiraDTO> AddCadeira(@RequestBody @Valid AdicionaCadeiraDTO dto, UriComponentsBuilder uriBuilder) {
        Cadeiras cadeira = new Cadeiras(dto);
        cadeira.registraHorarioEntrega();

        cadeiraService.addCadeira(dto);

        URI uri = uriBuilder.path("/cadeira").buildAndExpand(cadeira.getId()).toUri();

        return ResponseEntity.created(uri).body(new DetalhamentoAdicionaCadeiraDTO(cadeira));
    }

    @PutMapping("/devolucao/{id}")
    public ResponseEntity<ListaCadeiraPorIdDTO> devolucao(@PathVariable Long id) throws ChairAlreadyReturned {
        ListaCadeiraPorIdDTO cadeira = cadeiraService.devolucao(id);
        return ResponseEntity.ok(cadeira);
    }

}
