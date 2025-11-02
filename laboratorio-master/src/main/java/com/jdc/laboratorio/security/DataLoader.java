package com.jdc.laboratorio.security;

import com.jdc.laboratorio.model.Usuario;
import com.jdc.laboratorio.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Bean
    CommandLineRunner initUsuarios(UsuarioRepository repo, BCryptPasswordEncoder encoder) {
        return args -> {

            // ✅ Crear SUPERADMIN solo si no existe
            if (repo.findByUserName("superadmin").isEmpty()) {
                Usuario superadmin = new Usuario();
                superadmin.setNombre("Super Administrador");
                superadmin.setUserName("superadmin");
                superadmin.setContrasena(encoder.encode("1234"));
                superadmin.setRol("SUPERADMIN");
                repo.save(superadmin);
                System.out.println("✅ Usuario SUPERADMIN creado: user=superadmin, pass=1234");
            } else {
                System.out.println("⚙️ Usuario SUPERADMIN ya existe, no se crea.");
            }
        };
    }
}
