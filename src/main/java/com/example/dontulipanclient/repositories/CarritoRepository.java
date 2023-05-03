package com.example.dontulipanclient.repositories;

import com.example.dontulipanclient.entities.Compra;
import com.example.dontulipanclient.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CarritoRepository extends JpaRepository<Compra, Integer> {
    List<Compra> findByUsuarioAndEstado(Usuario usuario, byte estado);
}
