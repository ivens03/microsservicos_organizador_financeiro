package com.organizador.financeiros.spring.usuario.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UsuarioCltResponseDto {
    private UsuarioResponseDto usuario;
    private BigDecimal salarioMensal;
    private String cpf;
    private String empresa;
    private Byte filhos;
    private Boolean moraSo;
    private Boolean possiReservaDeEnergencia;
}