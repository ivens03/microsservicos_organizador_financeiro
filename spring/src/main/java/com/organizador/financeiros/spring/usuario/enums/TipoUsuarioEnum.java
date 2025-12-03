package com.organizador.financeiros.spring.usuario.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.organizador.financeiros.spring.usuario.config.TipoUsuarioDeserializer;

@JsonDeserialize(using = TipoUsuarioDeserializer.class)
public enum TipoUsuarioEnum {
    ADM,
    PF,
    PJ,
    MEI,
    CLT,
    CNPJ
}
