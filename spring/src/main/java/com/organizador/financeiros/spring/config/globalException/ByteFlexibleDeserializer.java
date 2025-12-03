package com.organizador.financeiros.spring.config.globalException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.io.IOException;

public class ByteFlexibleDeserializer extends JsonDeserializer<Byte>{

    @Override
    public Byte deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        try {
            return Byte.parseByte(text);
        } catch (NumberFormatException e) {
            // Lança exceção que será capturada pelo GlobalExceptionHandler
            throw new InvalidFormatException(p, "Valor não é um número inteiro válido", text, Byte.class);
        }
    }

}
