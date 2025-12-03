package com.organizador.financeiros.spring.usuario.controller;

import com.organizador.financeiros.spring.usuario.dto.UsuarioCltRequestDto;
import com.organizador.financeiros.spring.usuario.dto.UsuarioCltResponseDto;
import com.organizador.financeiros.spring.usuario.dto.validation.OnCreate;
import com.organizador.financeiros.spring.usuario.dto.validation.OnUpdate;
import com.organizador.financeiros.spring.usuario.service.UsuarioCltService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/API/CLT/usuario")
@RequiredArgsConstructor
@Tag(name = "Usuários CLT", description = "Gerenciamento do ciclo de vida dos usuários CLT e integração com IA")
public class UsuarioCltController {

    private final UsuarioCltService usuarioCltService;

    @Operation(summary = "Cadastrar Usuario CLT", description = "Cria um novo usuário no sistema com perfil CLT.")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "400", description = "Bad Request - Dados inválidos ou duplicados")
    @PostMapping
    public ResponseEntity<UsuarioCltResponseDto> criar(@Validated(OnCreate.class) @RequestBody UsuarioCltRequestDto dto) {
        log.info("POST /API/CLT/usuario - Payload recebido para CPF: {}, Email: {}", dto.getCpf(), dto.getUsuario().getEmail());
        UsuarioCltResponseDto response = usuarioCltService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Editar Usuário", description = "Atualiza os dados cadastrais de um usuário CLT.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @PatchMapping("/{id}")
    public ResponseEntity<UsuarioCltResponseDto> editar(
            @Parameter(description = "ID do usuário") @PathVariable Long id,
            @Validated(OnUpdate.class) @RequestBody UsuarioCltRequestDto dto) {
        log.info("Requisição recebida para editar Usuario CLT ID: {}", id);
        return ResponseEntity.ok(usuarioCltService.editar(id, dto));
    }

    @Operation(summary = "Desativar Usuário", description = "Realiza a inativação lógica do usuário.")
    @ApiResponse(responseCode = "204", description = "No Content")
    @DeleteMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        log.info("Requisição recebida para desativar Usuario CLT ID: {}", id);
        usuarioCltService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Alimentar a IA", description = "Envia dados do usuário para processamento da Inteligência Artificial.")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/{id}/alimentar-ia")
    public ResponseEntity<UsuarioCltResponseDto> alimentarIa(
            @PathVariable Long id,
            @RequestBody Object dadosIaPayload) {
        log.info("Requisição recebida para alimentar IA do Usuario CLT ID: {}", id);
        return ResponseEntity.ok(usuarioCltService.alimentarIa(id, dadosIaPayload));
    }

    @Operation(summary = "Buscar Usuário por ID", description = "Retorna os detalhes de um usuário CLT específico.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found - Usuário não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioCltResponseDto> buscarPorIdAtivo(
            @Parameter(description = "ID do usuário") @PathVariable Long id) {
        log.info("Requisição recebida para buscar Usuario CLT ID: {}", id);
        UsuarioCltResponseDto response = usuarioCltService.buscarPorIdAtivo(id);
        return ResponseEntity.ok(response);
    }
}