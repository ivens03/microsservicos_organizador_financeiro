package com.organizador.financeiros.spring.usuario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios_clt", schema = "usuarios")
public class UsuarioClt implements Serializable {

    @Id
    @Column(name = "usuario_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "salario_mensal", nullable = false, precision = 19, scale = 2)
    private BigDecimal salarioMensal;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false)
    private String empresa;

    private Byte filhos;

    @Column(name = "mora_so")
    private Boolean moraSo;

    @Column(name = "possi_reserva_emergencia")
    private Boolean possiReservaDeEnergencia;
}