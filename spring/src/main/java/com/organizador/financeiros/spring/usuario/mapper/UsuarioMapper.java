package com.organizador.financeiros.spring.usuario.mapper;

import com.organizador.financeiros.spring.usuario.dto.UsuarioRequestDto;
import com.organizador.financeiros.spring.usuario.dto.UsuarioResponseDto;
import com.organizador.financeiros.spring.usuario.model.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDto dto) {
        if (dto == null) return null;
        Usuario entity = new Usuario();
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setSenha(dto.getSenha());
        entity.setTipoUsuario(dto.getTipoUsuario());
        entity.setAtivo(dto.getAtivo());
        entity.setEspectativas(dto.getEspectativas());
        entity.setPublico(dto.getPublico());

        entity.setDataNascimento(dto.getDataNascimento());

        return entity;
    }

    public UsuarioResponseDto toResponseDto(Usuario entity) {
        if (entity == null) return null;
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEmail(entity.getEmail());
        dto.setTipoUsuario(entity.getTipoUsuario());
        dto.setAtivo(entity.getAtivo());
        dto.setEspectativas(entity.getEspectativas());
        dto.setPublico(entity.getPublico());

        dto.setDataNascimento(entity.getDataNascimento());

        if (entity.getDataNascimento() != null) {
            int anos = Period.between(entity.getDataNascimento(), LocalDate.now()).getYears();
            dto.setIdade((byte) anos);
        }

        return dto;
    }

    public void updateEntityFromDto(UsuarioRequestDto dto, Usuario entity) {
        if (dto == null || entity == null) return;

        if (dto.getNome() != null) entity.setNome(dto.getNome());
        if (dto.getEspectativas() != null) entity.setEspectativas(dto.getEspectativas());
        if (dto.getPublico() != null) entity.setPublico(dto.getPublico());

        if (dto.getDataNascimento() != null) {
            entity.setDataNascimento(dto.getDataNascimento());
        }

    }
}