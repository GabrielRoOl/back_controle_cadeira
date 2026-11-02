package br.com.cadeira.controle.vitrium.vitrium.servicies;

import br.com.cadeira.controle.vitrium.common.exceptions.ChairAlreadyReturnedException;
import br.com.cadeira.controle.vitrium.common.exceptions.ChairInUseException;
import br.com.cadeira.controle.vitrium.common.exceptions.ChairNotFoundException;
import br.com.cadeira.controle.vitrium.vitrium.dto.AdicionaCadeiraDTO;
import br.com.cadeira.controle.vitrium.vitrium.dto.ListaCadeiraPorIdDTO;
import br.com.cadeira.controle.vitrium.vitrium.entity.Cadeiras;
import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;
import br.com.cadeira.controle.vitrium.vitrium.repositories.CadeiraRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testa as regras de empréstimo e devolução sem subir o Spring:
 * o repositório é um mock do Mockito.
 */
@ExtendWith(MockitoExtension.class)
class CadeiraServiceTest {

    @Mock
    private CadeiraRepository cadeiraRepository;

    @InjectMocks
    private CadeiraService cadeiraService;

    private final AdicionaCadeiraDTO novoEmprestimo =
            new AdicionaCadeiraDTO("Paciente Teste", "Clínica Teste", 101, ECadeira.CADEIRA_01);

    private Cadeiras emprestimoAberto() {
        Cadeiras cadeira = new Cadeiras("Paciente Teste", "Clínica Teste", 101, ECadeira.CADEIRA_01);
        cadeira.setId(1L);
        cadeira.registraHorarioEntrega();
        return cadeira;
    }

    private Cadeiras emprestimoDevolvido() {
        Cadeiras cadeira = emprestimoAberto();
        cadeira.registraHorarioDevolucao();
        return cadeira;
    }

    @Nested
    @DisplayName("Empréstimo")
    class Emprestimo {

        @Test
        @DisplayName("Registra o empréstimo como não devolvido e com data de entrega quando a cadeira está livre")
        void registraEmprestimoQuandoCadeiraLivre() {
            when(cadeiraRepository.existsByCadeiraAndNaoDevolvida(ECadeira.CADEIRA_01)).thenReturn(false);

            cadeiraService.addCadeira(novoEmprestimo);

            ArgumentCaptor<Cadeiras> captor = ArgumentCaptor.forClass(Cadeiras.class);
            verify(cadeiraRepository).save(captor.capture());
            Cadeiras salva = captor.getValue();
            assertThat(salva.getCadeira()).isEqualTo(ECadeira.CADEIRA_01);
            assertThat(salva.getNomePaciente()).isEqualTo("Paciente Teste");
            assertThat(salva.getDevolvida()).isFalse();
            assertThat(salva.getDtEntrega()).isNotNull();
            assertThat(salva.getDtDevolucao()).isNull();
        }

        @Test
        @DisplayName("Lança ChairInUseException e não salva quando a cadeira já está emprestada")
        void recusaEmprestimoQuandoCadeiraEmUso() {
            when(cadeiraRepository.existsByCadeiraAndNaoDevolvida(ECadeira.CADEIRA_01)).thenReturn(true);

            assertThatThrownBy(() -> cadeiraService.addCadeira(novoEmprestimo))
                    .isInstanceOf(ChairInUseException.class);
            verify(cadeiraRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Devolução por id")
    class DevolucaoPorId {

        @Test
        @DisplayName("Marca como devolvida e registra a data de devolução")
        void devolveEmprestimoAberto() {
            Cadeiras aberto = emprestimoAberto();
            when(cadeiraRepository.findById(1L)).thenReturn(Optional.of(aberto));

            ListaCadeiraPorIdDTO resposta = cadeiraService.devolucao(1L);

            assertThat(resposta.devolvida()).isTrue();
            assertThat(resposta.dataDevolucao()).isNotNull();
            assertThat(aberto.getDevolvida()).isTrue();
            assertThat(aberto.getDtDevolucao()).isNotNull();
        }

        @Test
        @DisplayName("Lança ChairAlreadyReturnedException quando o empréstimo já foi devolvido")
        void recusaDevolucaoRepetida() {
            Cadeiras devolvido = emprestimoDevolvido();
            var dataDevolucaoOriginal = devolvido.getDtDevolucao();
            when(cadeiraRepository.findById(1L)).thenReturn(Optional.of(devolvido));

            assertThatThrownBy(() -> cadeiraService.devolucao(1L))
                    .isInstanceOf(ChairAlreadyReturnedException.class);
            assertThat(devolvido.getDtDevolucao()).isEqualTo(dataDevolucaoOriginal);
        }

        @Test
        @DisplayName("Lança ChairNotFoundException quando o id não existe")
        void recusaIdInexistente() {
            when(cadeiraRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> cadeiraService.devolucao(99L))
                    .isInstanceOf(ChairNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Devolução pela identificação da cadeira")
    class DevolucaoPorCadeira {

        @Test
        @DisplayName("Devolve o empréstimo em aberto da cadeira informada")
        void devolveEmprestimoAbertoDaCadeira() {
            Cadeiras aberto = emprestimoAberto();
            when(cadeiraRepository.findIdsByCadeiraAndNaoDevolvida(ECadeira.CADEIRA_01)).thenReturn(List.of(1L));
            when(cadeiraRepository.findById(1L)).thenReturn(Optional.of(aberto));

            ListaCadeiraPorIdDTO resposta = cadeiraService.devolucaoByCadeira(ECadeira.CADEIRA_01);

            assertThat(resposta.cadeira()).isEqualTo(ECadeira.CADEIRA_01);
            assertThat(resposta.devolvida()).isTrue();
            assertThat(aberto.getDtDevolucao()).isNotNull();
        }

        @Test
        @DisplayName("Lança ChairAlreadyReturnedException quando a cadeira não tem empréstimo em aberto")
        void recusaCadeiraSemEmprestimoAberto() {
            when(cadeiraRepository.findIdsByCadeiraAndNaoDevolvida(ECadeira.CADEIRA_02)).thenReturn(List.of());

            assertThatThrownBy(() -> cadeiraService.devolucaoByCadeira(ECadeira.CADEIRA_02))
                    .isInstanceOf(ChairAlreadyReturnedException.class);
            verify(cadeiraRepository, never()).findById(any());
        }
    }

    @Test
    @DisplayName("findById lança ChairNotFoundException quando o id não existe")
    void findByIdInexistente() {
        when(cadeiraRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cadeiraService.findById(99L))
                .isInstanceOf(ChairNotFoundException.class);
    }
}
