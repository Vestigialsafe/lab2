package com.jdc.laboratorio.repository;

import com.jdc.laboratorio.model.Laboratorio;
import com.jdc.laboratorio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUserName(String userName);
    List<Usuario> findByLaboratorio(Laboratorio laboratorio);

}
