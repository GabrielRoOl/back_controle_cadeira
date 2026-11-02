package br.com.cadeira.controle.vitrium.vitrium.repositories;

import br.com.cadeira.controle.vitrium.vitrium.entity.Cadeiras;
import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testa as consultas JPQL que decidem se uma cadeira está emprestada,
 * contra um banco H2 em memória.
 */
@DataJpaTest
class CadeiraRepositoryTest {

    @Autowired
    private CadeiraRepository cadeiraRepository;

    private Cadeiras salvaEmprestimo(ECadeira cadeira, boolean devolvido) {
        Cadeiras emprestimo = new Cadeiras("Paciente Teste", "Clínica Teste", 101, cadeira);
        emprestimo.registraHorarioEntrega();
        if (devolvido) {
            emprestimo.registraHorarioDevolucao();
        }
        return cadeiraRepository.save(emprestimo);
    }

    @Test
    @DisplayName("Cadeira com empréstimo em aberto conta como em uso")
    void cadeiraComEmprestimoAbertoEstaEmUso() {
        salvaEmprestimo(ECadeira.CADEIRA_01, false);

        assertThat(cadeiraRepository.existsByCadeiraAndNaoDevolvida(ECadeira.CADEIRA_01)).isTrue();
    }

    @Test
    @DisplayName("Cadeira só com empréstimos devolvidos está livre")
    void cadeiraDevolvidaEstaLivre() {
        salvaEmprestimo(ECadeira.CADEIRA_01, true);

        assertThat(cadeiraRepository.existsByCadeiraAndNaoDevolvida(ECadeira.CADEIRA_01)).isFalse();
    }

    @Test
    @DisplayName("Empréstimo de outra cadeira não afeta a consulta")
    void outraCadeiraNaoAfeta() {
        salvaEmprestimo(ECadeira.CADEIRA_02, false);

        assertThat(cadeiraRepository.existsByCadeiraAndNaoDevolvida(ECadeira.CADEIRA_01)).isFalse();
    }

    @Test
    @DisplayName("Retorna só o id do empréstimo em aberto da cadeira")
    void retornaSoEmprestimoAberto() {
        salvaEmprestimo(ECadeira.CADEIRA_01, true);
        Cadeiras aberto = salvaEmprestimo(ECadeira.CADEIRA_01, false);

        assertThat(cadeiraRepository.findIdsByCadeiraAndNaoDevolvida(ECadeira.CADEIRA_01))
                .containsExactly(aberto.getId());
    }
}
