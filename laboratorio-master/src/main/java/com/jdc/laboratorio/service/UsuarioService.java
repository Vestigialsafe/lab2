package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(Integer id);
    Optional<Usuario> buscarPorUserName(String userName);
    List<Usuario> listarTodos();
    void eliminar(Integer id);
}
