package com.organizador.financeiros.spring.usuario.mapper;

import com.organizador.financeiros.spring.usuario.dto.UsuarioCltRequestDto;
import com.organizador.financeiros.spring.usuario.dto.UsuarioCltResponseDto;
import com.organizador.financeiros.spring.usuario.model.UsuarioClt;
import org.springframework.stereotype.Component;

@Component
public class UsuarioCltMapper {

    private final UsuarioMapper usuarioMapper;

    public UsuarioCltMapper(UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    public UsuarioClt toEntity(UsuarioCltRequestDto dto) {
        if (dto == null) return null;
        UsuarioClt entity = new UsuarioClt();

        entity.setSalarioMensal(dto.getSalarioMensal());
        entity.setCpf(dto.getCpf());
        entity.setEmpresa(dto.getEmpresa());
        entity.setFilhos(dto.getFilhos());
        entity.setMoraSo(dto.getMoraSo());
        entity.setPossiReservaDeEnergencia(dto.getPossiReservaDeEnergencia());

        return entity;
    }

    public UsuarioCltResponseDto toResponseDto(UsuarioClt entity) {
        if (entity == null) return null;
        UsuarioCltResponseDto dto = new UsuarioCltResponseDto();
        dto.setUsuario(usuarioMapper.toResponseDto(entity.getUsuario()));
        dto.setSalarioMensal(entity.getSalarioMensal());
        dto.setCpf(entity.getCpf());
        dto.setEmpresa(entity.getEmpresa());
        dto.setFilhos(entity.getFilhos());
        dto.setMoraSo(entity.getMoraSo());
        dto.setPossiReservaDeEnergencia(entity.getPossiReservaDeEnergencia());
        return dto;
    }

    public void updateEntityFromDto(UsuarioCltRequestDto dto, UsuarioClt entity) {
        if (dto == null || entity == null) return;

        if (dto.getUsuario() != null) {
            usuarioMapper.updateEntityFromDto(dto.getUsuario(), entity.getUsuario());
        }

        if (dto.getSalarioMensal() != null) entity.setSalarioMensal(dto.getSalarioMensal());
        if (dto.getEmpresa() != null) entity.setEmpresa(dto.getEmpresa());
        if (dto.getFilhos() != null) entity.setFilhos(dto.getFilhos());
        if (dto.getMoraSo() != null) entity.setMoraSo(dto.getMoraSo());
        if (dto.getPossiReservaDeEnergencia() != null) entity.setPossiReservaDeEnergencia(dto.getPossiReservaDeEnergencia());
    }
}