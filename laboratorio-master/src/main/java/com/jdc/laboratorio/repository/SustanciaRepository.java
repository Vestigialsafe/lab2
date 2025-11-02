package com.jdc.laboratorio.repository;

import com.jdc.laboratorio.model.Sustancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SustanciaRepository extends JpaRepository<Sustancia, Long> {

    // 🔹 Buscar por subcategoría
    List<Sustancia> findBySubcategorias_IdSubCategoria(Integer idSubCategoria);

    // 🔹 Buscar sustancias por laboratorio
    List<Sustancia> findByLaboratorio_IdLaboratorio(Integer idLaboratorio);

    // 🔹 Buscar sustancias por subcategoría y laboratorio
    List<Sustancia> findBySubcategorias_IdSubCategoriaAndLaboratorio_IdLaboratorio(
            Integer idSubCategoria,
            Integer idLaboratorio
    );

    // 🔹 Contar sustancias próximas a vencer
    long countByFechaVencimientoBetween(LocalDate inicio, LocalDate fin);

    // 🔹 Contar sustancias agotadas (stock <= cantidad)
    long countByStockLessThanEqual(int cantidad);

    // 🔹 Contar sustancias por categoría
    @Query("""
            SELECT sc.categoria.nombre, COUNT(DISTINCT s)
            FROM Sustancia s
            JOIN s.subcategorias sc
            GROUP BY sc.categoria.nombre
            ORDER BY sc.categoria.nombre ASC
            """)
    List<Object[]> contarPorCategoria();

    // 🔹 Listar sustancias próximas a vencer (30 días o 1 mes)
    @Query("""
            SELECT s
            FROM Sustancia s
            WHERE s.fechaVencimiento BETWEEN :hoy AND :limite
            ORDER BY s.fechaVencimiento ASC
            """)
    List<Sustancia> findProximasAVencer(@Param("hoy") LocalDate hoy,
                                        @Param("limite") LocalDate limite);

    // 🔹 Listar sustancias agotadas (stock <= 0)
    @Query("""
            SELECT s
            FROM Sustancia s
            WHERE s.stock <= 0
            ORDER BY s.nombre ASC
            """)
    List<Sustancia> findAgotadas();
}
