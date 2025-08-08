package br.com.cadeira.controle.vitrium.vitrium.servicies;

import br.com.cadeira.controle.vitrium.vitrium.dto.AdicionaCadeiraDTO;
import br.com.cadeira.controle.vitrium.vitrium.dto.ListaCadeiraPorIdDTO;
import br.com.cadeira.controle.vitrium.vitrium.dto.ListaCadeirasDTO;
import br.com.cadeira.controle.vitrium.vitrium.entity.Cadeiras;
import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;
import br.com.cadeira.controle.vitrium.vitrium.exceptions.ChairAlreadyReturnedException;
import br.com.cadeira.controle.vitrium.vitrium.exceptions.ChairInUseException;
import br.com.cadeira.controle.vitrium.vitrium.exceptions.ChairNotFoundException;
import br.com.cadeira.controle.vitrium.vitrium.repositories.CadeiraRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
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

        var cadeiraNaoDev = cadeiraRepository.existsByCadeiraAndNaoDevolvida(dto.cadeira());
        if (cadeiraNaoDev) {
            throw new ChairInUseException();
        }

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
    public ListaCadeiraPorIdDTO devolucao(Long id) throws ChairAlreadyReturnedException {
        Cadeiras cadeiras = cadeiraRepository.findById(id).orElseThrow(ChairNotFoundException::new);

        var dev = cadeiras.getDtEntrega();


        if (cadeiras.getDevolvida() == true && dev.isBefore(OffsetDateTime.now())) {
            throw new ChairAlreadyReturnedException();
        }

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

    @Transactional
    public ListaCadeiraPorIdDTO devolucaoByCadeira(ECadeira cadeira) {

        var idCadeira = cadeiraRepository.findIdsByCadeiraAndNaoDevolvida(cadeira);

        if (idCadeira.isEmpty()) {
            throw new ChairAlreadyReturnedException();
        }

        Cadeiras cadeiras = cadeiraRepository.findById(idCadeira.get(0)).orElseThrow(ChairNotFoundException::new);

        if (cadeiras.getDevolvida() && !cadeiras.getDtDevolucao().isBefore(cadeiras.getDtEntrega())) {
            throw new ChairAlreadyReturnedException();
        }

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