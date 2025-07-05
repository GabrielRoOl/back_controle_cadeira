package br.com.cadeira.controle.vitrium.vitrium.entity;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.cadeira.controle.vitrium.vitrium.entity.enums.ECadeira;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private Boolean devolvida;
    
}
