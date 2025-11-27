package com.organizador.financeiros.spring.usuario.dto;

import com.organizador.financeiros.spring.usuario.dto.validation.OnCreate;
import com.organizador.financeiros.spring.usuario.dto.validation.OnUpdate;
import com.organizador.financeiros.spring.usuario.enums.PublicoEnum;
import com.organizador.financeiros.spring.usuario.enums.TipoUsuarioEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Dados para criação (obrigatórios) ou edição (parciais)")
public class UsuarioRequestDto {

    @NotBlank(message = "Nome é obrigatório", groups = OnCreate.class)
    @Schema(example = "Carlos Trabalhador")
    private String nome;

    @NotBlank(message = "Email é obrigatório", groups = OnCreate.class)
    @Email(message = "Email inválido", groups = {OnCreate.class, OnUpdate.class})
    @Schema(example = "carlos.clt@email.com")
    private String email;

    @NotBlank(message = "Senha é obrigatória", groups = OnCreate.class)
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres", groups = {OnCreate.class, OnUpdate.class})
    @Schema(example = "SenhaForte123!")
    private String senha;

    @NotNull(message = "Data de nascimento é obrigatória", groups = OnCreate.class)
    @Past(message = "Data de nascimento deve ser no passado", groups = {OnCreate.class, OnUpdate.class})
    @Schema(example = "1995-10-25", type = "string", format = "date")
    private LocalDate dataNascimento;

    @Schema(example = "CLT")
    private TipoUsuarioEnum tipoUsuario;

    @NotNull(groups = OnCreate.class)
    @Schema(example = "true")
    private Boolean ativo = true;

    @Schema(example = "Busco estabilidade financeira.")
    private String espectativas;

    @NotNull(message = "Público é obrigatório", groups = OnCreate.class)
    @Schema(example = "PESSOAL")
    private PublicoEnum publico;
}