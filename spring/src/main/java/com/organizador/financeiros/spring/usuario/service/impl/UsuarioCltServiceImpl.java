package com.organizador.financeiros.spring.usuario.service.impl;

import com.organizador.financeiros.spring.usuario.dto.UsuarioCltRequestDto;
import com.organizador.financeiros.spring.usuario.dto.UsuarioCltResponseDto;
import com.organizador.financeiros.spring.usuario.mapper.UsuarioCltMapper;
import com.organizador.financeiros.spring.usuario.mapper.UsuarioMapper;
import com.organizador.financeiros.spring.usuario.model.Usuario;
import com.organizador.financeiros.spring.usuario.model.UsuarioClt;
import com.organizador.financeiros.spring.usuario.repository.UsuarioCltRepository;
import com.organizador.financeiros.spring.usuario.repository.UsuarioRepository;
import com.organizador.financeiros.spring.usuario.service.UsuarioCltService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.organizador.financeiros.spring.usuario.advice.BadRequestException;
import com.organizador.financeiros.spring.usuario.advice.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioCltServiceImpl implements UsuarioCltService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioCltRepository usuarioCltRepository;
    private final UsuarioCltMapper usuarioCltMapper;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioCltResponseDto criar(UsuarioCltRequestDto dto) {
        log.info("Iniciando criação de Usuário CLT. Email: {}, CPF: {}", dto.getUsuario().getEmail(), dto.getCpf());

        if (usuarioRepository.existsByEmail(dto.getUsuario().getEmail())) {
            log.warn("Tentativa de cadastro com email já existente: {}", dto.getUsuario().getEmail());
            throw new BadRequestException("Email já cadastrado no sistema.");
        }

        if (usuarioCltRepository.existsByCpf(dto.getCpf())) {
            log.warn("Tentativa de cadastro com CPF já existente: {}", dto.getCpf());
            throw new BadRequestException("CPF já cadastrado no sistema.");
        }

        // 1. Prepara Usuário Base
        Usuario usuario = usuarioMapper.toEntity(dto.getUsuario());
        usuario.setSenha(passwordEncoder.encode(dto.getUsuario().getSenha()));

        // 2. Prepara Perfil CLT e vincula
        UsuarioClt usuarioClt = usuarioCltMapper.toEntity(dto);
        usuarioClt.setUsuario(usuario); // @MapsId vai usar o ID do usuário ao persistir

        // 3. Salva (Cascade ALL salvará o usuário também)
        usuarioClt = usuarioCltRepository.save(usuarioClt);

        log.info("Usuário CLT criado com sucesso. ID: {}", usuarioClt.getId());
        return usuarioCltMapper.toResponseDto(usuarioClt);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioCltResponseDto buscarPorId(Long id) {
        log.debug("Buscando Usuário CLT por ID: {}", id);
        return usuarioCltRepository.findById(id)
                .map(usuarioCltMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário CLT não encontrado com ID: " + id));
    }

    @Override
    @Transactional
    public UsuarioCltResponseDto editar(Long id, UsuarioCltRequestDto dto) {
        log.info("Atualizando Usuário CLT. ID: {}", id);

        UsuarioClt usuarioClt = usuarioCltRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário CLT não encontrado para edição."));

        // Verifica unicidade de email se foi alterado
        if (!usuarioClt.getUsuario().getEmail().equals(dto.getUsuario().getEmail()) &&
                usuarioRepository.existsByEmail(dto.getUsuario().getEmail())) {
            throw new BadRequestException("Novo email já está em uso.");
        }

        usuarioCltMapper.updateEntityFromDto(dto, usuarioClt);

        // Se a senha foi enviada, criptografa novamente
        if (dto.getUsuario().getSenha() != null && !dto.getUsuario().getSenha().isBlank()) {
            usuarioClt.getUsuario().setSenha(passwordEncoder.encode(dto.getUsuario().getSenha()));
        }

        UsuarioClt atualizado = usuarioCltRepository.save(usuarioClt);
        log.info("Usuário CLT atualizado com sucesso.");
        return usuarioCltMapper.toResponseDto(atualizado);
    }

    @Override
    @Transactional
    public void desativar(Long id) {
        log.info("Desativando Usuário CLT. ID: {}", id);
        UsuarioClt usuarioClt = usuarioCltRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário CLT não encontrado para desativação."));

        usuarioClt.getUsuario().setAtivo(false);
        usuarioCltRepository.save(usuarioClt);
        log.info("Usuário desativado com sucesso.");
    }

    @Override
    @Transactional
    public UsuarioCltResponseDto alimentarIa(Long id, Object dadosIa) {
        log.info("Enviando dados para IA. Usuário ID: {}", id);

        UsuarioClt usuarioClt = usuarioCltRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário CLT não encontrado."));

        // Simulação de integração com IA
        log.debug("Payload enviado para IA (mock): {}", dadosIa.toString());

        // Aqui entraria a chamada real para o serviço de IA

        log.info("Processamento de IA concluído para Usuário ID: {}", id);
        return usuarioCltMapper.toResponseDto(usuarioClt);
    }
}