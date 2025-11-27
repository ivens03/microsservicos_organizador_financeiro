package com.organizador.financeiros.spring.usuario.dto;

import com.organizador.financeiros.spring.usuario.enums.PublicoEnum;
import com.organizador.financeiros.spring.usuario.enums.TipoUsuarioEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioResponseDto {
    private Long id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private Byte idade;
    private TipoUsuarioEnum tipoUsuario;
    private Boolean ativo;
    private String espectativas;
    private PublicoEnum publico;
}