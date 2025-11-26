package com.organizador.financeiros.spring.usuario.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Dados completos para criação de perfil CLT")
public class UsuarioCltRequestDto {

    @Valid
    @NotNull(message = "Dados do usuário base são obrigatórios")
    private UsuarioRequestDto usuario;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Schema(example = "4500.50")
    private BigDecimal salarioMensal;

    @NotNull
    @Min(value = 14, message = "Idade mínima permitida é 14 anos")
    @Schema(example = "28")
    private Byte idade;

    @NotBlank
    @Schema(example = "12345678900")
    private String cpf;

    @NotBlank
    @Schema(example = "Tech Solutions Ltda")
    private String empresa;

    @Schema(example = "1")
    private Byte filhos;

    @Schema(example = "false")
    private Boolean moraSo;

    @Schema(example = "true")
    private Boolean possiReservaDeEnergencia;
}