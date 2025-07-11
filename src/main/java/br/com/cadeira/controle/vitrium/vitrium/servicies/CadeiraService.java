package br.com.cadeira.controle.vitrium.vitrium.servicies;

import br.com.cadeira.controle.vitrium.vitrium.dto.AdicionaCadeiraDTO;
import br.com.cadeira.controle.vitrium.vitrium.dto.ListaCadeirasDTO;
import br.com.cadeira.controle.vitrium.vitrium.entity.Cadeiras;
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
                        c.getNomePaciente(),
                        c.getDestino(),
                        c.getNmrClinica(),
                        c.getDtEntrega(),
                        c.getDtDevolucao(),
                        c.getCadeira(),
                        c.getDevolvida()))
                .collect(Collectors.toList());
    }


    public Object addCadeira(AdicionaCadeiraDTO dto) {
        Cadeiras novaCadeira = new Cadeiras(
                dto.nomePaciente(),
                dto.destino(),
                dto.numeroClinica(),
                dto.cadeira()
        );

        novaCadeira.registraHorarioEntrega();

        cadeiraRepository.save(novaCadeira);

        return novaCadeira;
    }
}