package com.organizador.financeiros.spring.usuario.dto;

import com.organizador.financeiros.spring.usuario.enums.PublicoEnum;
import com.organizador.financeiros.spring.usuario.enums.TipoUsuarioEnum;
import lombok.Data;

@Data
public class UsuarioResponseDto {
    private Long id;
    private String nome;
    private String email;
    //private String Senha;
    private TipoUsuarioEnum tipoUsuario;
    private Boolean ativo;
    private String espectativas;
    private PublicoEnum publico;
}