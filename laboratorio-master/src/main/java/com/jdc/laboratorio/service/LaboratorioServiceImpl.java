package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Laboratorio;
import com.jdc.laboratorio.model.Usuario;
import com.jdc.laboratorio.repository.LaboratorioRepository;
import com.jdc.laboratorio.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LaboratorioServiceImpl implements LaboratorioService {

    private final LaboratorioRepository laboratorioRepository;
    private final UsuarioRepository usuarioRepository;

    public LaboratorioServiceImpl(LaboratorioRepository laboratorioRepository, UsuarioRepository usuarioRepository) {
        this.laboratorioRepository = laboratorioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Laboratorio guardar(Laboratorio laboratorio) {
        return laboratorioRepository.save(laboratorio);
    }

    @Override
    public Optional<Laboratorio> buscarPorId(Integer id) {
        return laboratorioRepository.findById(id);
    }

    @Override
    public List<Laboratorio> listarTodos() {
        return laboratorioRepository.findAll();
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        Laboratorio laboratorio = laboratorioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado"));

        // 🔹 Desvincular usuarios asociados antes de eliminar
        List<Usuario> usuarios = usuarioRepository.findByLaboratorio(laboratorio);
        for (Usuario usuario : usuarios) {
            usuario.setLaboratorio(null);
            usuarioRepository.save(usuario);
        }

        laboratorioRepository.delete(laboratorio);
    }
}
