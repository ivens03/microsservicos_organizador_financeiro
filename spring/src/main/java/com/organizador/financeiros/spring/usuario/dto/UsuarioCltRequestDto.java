package com.organizador.financeiros.spring.usuario.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.organizador.financeiros.spring.config.globalException.ByteFlexibleDeserializer;
import com.organizador.financeiros.spring.usuario.dto.validation.OnCreate;
import com.organizador.financeiros.spring.usuario.dto.validation.OnUpdate;
import com.organizador.financeiros.spring.usuario.enums.TipoUsuarioEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Dados para perfil CLT. No PATCH, envie apenas os campos que deseja alterar.")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioCltRequestDto {

    @Valid
    @NotNull(message = "Dados do usuário base são obrigatórios", groups = OnCreate.class)
    @ConvertGroup(from = OnCreate.class, to = OnCreate.class)
    @ConvertGroup(from = OnUpdate.class, to = OnUpdate.class)
    private UsuarioRequestDto usuario;

    @NotNull(groups = OnCreate.class)
    @DecimalMin(value = "0.0", inclusive = false, groups = {OnCreate.class, OnUpdate.class})
    @Schema(example = "4500.50")
    private BigDecimal salarioMensal;

    @NotBlank(groups = OnCreate.class)
    @Schema(example = "12345678900")
    private String cpf;

    @NotBlank(groups = OnCreate.class)
    @Schema(example = "Tech Solutions Ltda")
    private String empresa;

    @Schema(example = "1")
    @JsonDeserialize(using = ByteFlexibleDeserializer.class)
    private Byte filhos;

    @Schema(example = "false")
    private Boolean moraSo;

    @Schema(example = "true")
    private Boolean possiReservaDeEnergencia;

    @NotNull(groups = OnCreate.class)
    private TipoUsuarioEnum tipoUsuario = TipoUsuarioEnum.CLT;
}