package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Usuario;
import com.jdc.laboratorio.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // ✅ Inyectamos el encoder
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        // ✅ Solo ciframos si no está ya cifrada
        if (usuario.getContrasena() != null && !usuario.getContrasena().startsWith("$2a$")) {
            String hashedPassword = passwordEncoder.encode(usuario.getContrasena());
            usuario.setContrasena(hashedPassword);
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorUserName(String userName) {
        return usuarioRepository.findByUserName(userName);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
