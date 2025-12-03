package com.organizador.financeiros.spring.config.security;

import com.organizador.financeiros.spring.config.globalException.exceotion.BadRequestException;
import com.organizador.financeiros.spring.usuario.dto.UsuarioRequestDto;
import com.organizador.financeiros.spring.usuario.enums.TipoUsuarioEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrivilegeEscalationGuard {

    private static final String ERRO_ESCALONAMENTO = "Operação não permitida: Não é possível alterar o perfil para ADMINISTRADOR.";

    /**
     * Valida se o payload contém uma tentativa de definir o tipo de usuário como ADM.
     * Caso positivo, loga o incidente e lança exceção.
     *
     * @param targetUserId ID do usuário sendo criado ou editado (para log).
     * @param usuarioDto DTO contendo os dados do usuário.
     * @throws BadRequestException se houver tentativa de escalonamento.
     */
    public void validateNoEscalation(Long targetUserId, UsuarioRequestDto usuarioDto) {
        if (isEscalationAttempt(usuarioDto)) {
            log.warn("Tentativa de escalonamento de privilégio para ADM bloqueada. Usuário Alvo ID: {}, Email: {}",
                    targetUserId != null ? targetUserId : "NOVO",
                    usuarioDto.getEmail());

            throw new BadRequestException(ERRO_ESCALONAMENTO);
        }
    }

    /**
     * Verifica se o DTO contém a definição do tipo ADM.
     * Seguro contra nulos (Null-safe).
     */
    public boolean isEscalationAttempt(UsuarioRequestDto usuarioDto) {
        return usuarioDto != null
                && usuarioDto.getTipoUsuario() != null
                && usuarioDto.getTipoUsuario() == TipoUsuarioEnum.ADM;
    }

}
