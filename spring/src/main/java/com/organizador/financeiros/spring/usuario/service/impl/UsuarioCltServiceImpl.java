package com.organizador.financeiros.spring.usuario.service.impl;

import com.organizador.financeiros.spring.config.security.PrivilegeEscalationGuard;
import com.organizador.financeiros.spring.usuario.dto.UsuarioCltRequestDto;
import com.organizador.financeiros.spring.usuario.dto.UsuarioCltResponseDto;
import com.organizador.financeiros.spring.usuario.enums.TipoUsuarioEnum;
import com.organizador.financeiros.spring.usuario.mapper.UsuarioCltMapper;
import com.organizador.financeiros.spring.usuario.mapper.UsuarioMapper;
import com.organizador.financeiros.spring.usuario.model.Usuario;
import com.organizador.financeiros.spring.usuario.model.UsuarioClt;
import com.organizador.financeiros.spring.usuario.repository.UsuarioCltRepository;
import com.organizador.financeiros.spring.usuario.repository.UsuarioRepository;
import com.organizador.financeiros.spring.usuario.service.UsuarioCltService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.organizador.financeiros.spring.config.globalException.exceotion.BadRequestException;
import com.organizador.financeiros.spring.config.globalException.exceotion.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioCltServiceImpl implements UsuarioCltService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioCltRepository usuarioCltRepository;
    private final UsuarioCltMapper usuarioCltMapper;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final PrivilegeEscalationGuard privilegeGuard;

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

        Usuario usuario = usuarioMapper.toEntity(dto.getUsuario());
        usuario.setSenha(passwordEncoder.encode(dto.getUsuario().getSenha()));
        usuario.setCriadoEm(LocalDateTime.now());
        usuario.setTipoUsuario(TipoUsuarioEnum.CLT);

        UsuarioClt usuarioClt = usuarioCltMapper.toEntity(dto);
        usuarioClt.setUsuario(usuario);

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
    @Transactional(readOnly = true)
    public UsuarioCltResponseDto buscarPorIdAtivo(Long id) {
        log.debug("Buscando Usuário CLT Ativo por ID: {}", id);
        UsuarioClt usuarioClt = usuarioCltRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário CLT não encontrado com ID: " + id));

        if (Boolean.FALSE.equals(usuarioClt.getUsuario().getAtivo())) {
            log.warn("Tentativa de acesso a usuário inativo. ID: {}", id);
            throw new ResourceNotFoundException("Usuário CLT não encontrado ou inativo. ID: " + id);
        }

        return usuarioCltMapper.toResponseDto(usuarioClt);
    }

    @Override
    @Transactional
    public UsuarioCltResponseDto editar(Long id, UsuarioCltRequestDto dto) {
        log.info("Iniciando processo de edição parcial (PATCH) para Usuário CLT ID: {}", id);

        UsuarioClt usuarioClt = usuarioCltRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário CLT não encontrado para edição."));

        // 1. Validação de usuário ativo
        if (Boolean.FALSE.equals(usuarioClt.getUsuario().getAtivo())) {
            log.warn("Bloqueio de edição: Usuário ID {} está inativo.", id);
            throw new BadRequestException("Não é permitido editar um usuário inativo. Ative-o primeiro.");
        }

        // 2. Validação de Email duplicado
        if (dto.getUsuario() != null && dto.getUsuario().getEmail() != null) {
            String novoEmail = dto.getUsuario().getEmail();
            String emailAtual = usuarioClt.getUsuario().getEmail();

            if (!novoEmail.equals(emailAtual) && usuarioRepository.existsByEmail(novoEmail)) {
                log.warn("Conflito de dados: Email {} já existe.", novoEmail);
                throw new BadRequestException("Novo email já está em uso.");
            }
        }

        // 3. ALTERAÇÃO INSERIDA: Prevenção de Escalonamento de Privilégios
        // Garante que ninguém consiga virar ADM através deste endpoint
        privilegeGuard.validateNoEscalation(id, dto.getUsuario());

        // 4. Aplicação das alterações (Mapper)
        log.debug("Aplicando alterações parciais na entidade via Mapper.");
        // Agora é seguro chamar o mapper, pois já garantimos que não é uma promoção para ADM
        usuarioCltMapper.updateEntityFromDto(dto, usuarioClt);

        // 5. Atualização de Senha (se enviada)
        if (dto.getUsuario() != null &&
                dto.getUsuario().getSenha() != null &&
                !dto.getUsuario().getSenha().isBlank()) {

            log.info("Atualizando senha do usuário ID: {}", id);
            usuarioClt.getUsuario().setSenha(passwordEncoder.encode(dto.getUsuario().getSenha()));
        }

        UsuarioClt atualizado = usuarioCltRepository.save(usuarioClt);
        log.info("Edição concluída com sucesso para Usuário CLT ID: {}", id);
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