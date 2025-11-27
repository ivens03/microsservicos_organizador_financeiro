package com.organizador.financeiros.spring.usuario.dto;

import com.organizador.financeiros.spring.usuario.enums.PublicoEnum;
import com.organizador.financeiros.spring.usuario.enums.TipoUsuarioEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Dados básicos para criação/edição de usuário")
public class UsuarioRequestDto {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(example = "Carlos Trabalhador")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(example = "carlos.clt@email.com")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Schema(example = "SenhaForte123!")
    private String senha;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Past(message = "Data de nascimento deve ser no passado")
    @Schema(example = "1995-10-25", type = "string", format = "date")
    private LocalDate dataNascimento;

    @NotNull(message = "Tipo de usuário é obrigatório")
    @Schema(example = "CLT")
    private TipoUsuarioEnum tipoUsuario;

    @NotNull
    @Schema(example = "true")
    private Boolean ativo;

    @Schema(example = "Busco estabilidade financeira.")
    private String espectativas;

    @NotNull(message = "Público é obrigatório")
    @Schema(example = "PESSOAL")
    private PublicoEnum publico;
}