package com.jdc.laboratorio.security;

import com.jdc.laboratorio.model.Usuario;
import com.jdc.laboratorio.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Spring espera los roles con el prefijo "ROLE_"
        return new User(
                usuario.getUserName(),
                usuario.getContrasena(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toUpperCase()))
        );
    }
}