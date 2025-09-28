package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.SubCategoria;
import java.util.List;
import java.util.Optional;

public interface SubCategoriaService {

    SubCategoria guardar(SubCategoria subCategoria);

    List<SubCategoria> listarTodas();

    Optional<SubCategoria> buscarPorId(Integer id);

    void eliminar(Integer id);

    List<SubCategoria> listarPorCategoria(Integer idCategoria);



}
