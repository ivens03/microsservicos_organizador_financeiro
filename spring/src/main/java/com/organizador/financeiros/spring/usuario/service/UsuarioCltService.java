package com.organizador.financeiros.spring.usuario.service;

import com.organizador.financeiros.spring.usuario.dto.UsuarioCltRequestDto;
import com.organizador.financeiros.spring.usuario.dto.UsuarioCltResponseDto;

public interface UsuarioCltService {
    UsuarioCltResponseDto criar(UsuarioCltRequestDto dto);
    UsuarioCltResponseDto buscarPorId(Long id);
    UsuarioCltResponseDto editar(Long id, UsuarioCltRequestDto dto);
    void desativar(Long id);
    UsuarioCltResponseDto alimentarIa(Long id, Object dadosIa);
}