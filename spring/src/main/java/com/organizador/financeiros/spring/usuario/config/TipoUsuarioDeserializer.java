package com.organizador.financeiros.spring.usuario.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.organizador.financeiros.spring.usuario.enums.TipoUsuarioEnum;

import java.io.IOException;

public class TipoUsuarioDeserializer extends JsonDeserializer<TipoUsuarioEnum>{

    @Override
    public TipoUsuarioEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();

        for (TipoUsuarioEnum tipo : TipoUsuarioEnum.values()) {
            if (tipo.name().equalsIgnoreCase(value)) {
                return tipo;
            }
        }

        throw new InvalidFormatException(
                p,
                "Valor inválido para o tipo de usuário: " + value,
                value,
                TipoUsuarioEnum.class
        );
    }

}
