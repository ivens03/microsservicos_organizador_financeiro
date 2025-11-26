package com.organizador.financeiros.spring.usuario.repository;

import com.organizador.financeiros.spring.usuario.model.UsuarioClt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioCltRepository extends JpaRepository<UsuarioClt, Long> {
    Optional<UsuarioClt> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}