package br.com.cadeira.controle.vitrium.vitrium.entity;

import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_cadeira")
public class Cadeiras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Nome_Paciente")
    private String nomePaciente;

    @Column(name = "Nome_Clinica")
    private String destino;

    @Column(name = "Numero_Sala")
    private int nmrClinica;

    @Column(name = "Data_Entrega")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC-3")
    private Instant dtEntrega;

    @Column(name = "Data_Devolucao")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC-3")
    private Instant dtDevolucao;

    @Enumerated(EnumType.STRING)
    private ECadeira cadeira;

    private Boolean devolvida = false;

    public Cadeiras(String nomePaciente, String destino, int nmrClinica, ECadeira cadeira) {
        this.nomePaciente = nomePaciente;
        this.destino = destino;
        this.nmrClinica = nmrClinica;
        this.cadeira = cadeira;
    }

    public Cadeiras(String nomePaciente, String destino, int nmrClinica) {
        this.nomePaciente = nomePaciente;
        this.destino = destino;
        this.nmrClinica = nmrClinica;
    }

    public void registraHorarioEntrega() {
        this.dtEntrega = Instant.now();
    }
}
