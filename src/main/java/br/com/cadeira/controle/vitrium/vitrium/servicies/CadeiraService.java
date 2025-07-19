package br.com.cadeira.controle.vitrium.vitrium.servicies;

import br.com.cadeira.controle.vitrium.vitrium.dto.AdicionaCadeiraDTO;
import br.com.cadeira.controle.vitrium.vitrium.dto.ListaCadeiraPorIdDTO;
import br.com.cadeira.controle.vitrium.vitrium.dto.ListaCadeirasDTO;
import br.com.cadeira.controle.vitrium.vitrium.entity.Cadeiras;
import br.com.cadeira.controle.vitrium.vitrium.exceptions.ChairNotFoundException;
import br.com.cadeira.controle.vitrium.vitrium.repositories.CadeiraRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CadeiraService {

    @Autowired
    private final CadeiraRepository cadeiraRepository;

    public CadeiraService(CadeiraRepository cadeiraRepository) {
        this.cadeiraRepository = cadeiraRepository;
    }


    @Transactional
    public List<ListaCadeirasDTO> findAll() {
        List<Cadeiras> cadeiras = cadeiraRepository.findAll();
        return cadeiras.stream().map(c -> new ListaCadeirasDTO(
                        c.getId(),
                        c.getNomePaciente(),
                        c.getDestino(),
                        c.getNmrClinica(),
                        c.getDtEntrega(),
                        c.getDtDevolucao(),
                        c.getCadeira(),
                        c.getDevolvida()))
                .collect(Collectors.toList());
    }


    @Transactional
    public void addCadeira(AdicionaCadeiraDTO dto) {
        Cadeiras novaCadeira = new Cadeiras(
                dto.nomePaciente(),
                dto.destino(),
                dto.numeroClinica(),
                dto.cadeira()
        );

        novaCadeira.registraHorarioEntrega();

        cadeiraRepository.save(novaCadeira);

    }

    @Transactional
    public ListaCadeiraPorIdDTO findById(Long id) {
        Cadeiras cadeiras = cadeiraRepository.findById(id).orElseThrow(ChairNotFoundException::new);

        return new ListaCadeiraPorIdDTO(
                cadeiras.getNomePaciente(),
                cadeiras.getDestino(),
                cadeiras.getNmrClinica(),
                cadeiras.getDtEntrega(),
                cadeiras.getDtDevolucao(),
                cadeiras.getCadeira(),
                cadeiras.getDevolvida()
        );
    }

    @Transactional
    public ListaCadeiraPorIdDTO devolucao(Long id) {
        Cadeiras cadeiras = cadeiraRepository.findById(id).orElseThrow(ChairNotFoundException::new);
        cadeiras.registraHorarioDevolucao();
        return new ListaCadeiraPorIdDTO(
                cadeiras.getNomePaciente(),
                cadeiras.getDestino(),
                cadeiras.getNmrClinica(),
                cadeiras.getDtEntrega(),
                cadeiras.getDtDevolucao(),
                cadeiras.getCadeira(),
                cadeiras.getDevolvida()
        );
    }
}