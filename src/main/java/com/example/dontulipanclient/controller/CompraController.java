package com.example.dontulipanclient.controller;

import com.example.dontulipanclient.repositories.CarritoRepository;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.dontulipanclient.entities.Compra;
import com.example.dontulipanclient.entities.Plantas;
import com.example.dontulipanclient.entities.Usuario;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CompraController {
    @Autowired
    CarritoRepository carritoRepository;

    //Crear un Dao para que cree las Compras SIN PROCESAR PAGO -pendiente


    //Listar las COMPRAS SIN PAGAR que tiene el usuaruio

    @GetMapping("/pendientes")
    public String comprasPendientes(Model model) {
        // Obtenemos el usuario actual, por ejemplo, desde el contexto de seguridad
        Usuario usuarioActual = ...;

        // Obtenemos las compras pendientes del usuario actual desde el repositorio
        List<Compra> comprasPendientes = carritoRepository.findByUsuarioAndEstado(usuarioActual, Estado.PENDIENTE);

        // Añadimos las compras pendientes al modelo y mostramos la vista correspondiente
        model.addAttribute("comprasPendientes", comprasPendientes);
        return "compras-pendientes";
    }

    //Borrar alguna de las COMPRAS SIN PAGAR listadas
    @PostMapping("/borrar")
    public String borrarCompra(@RequestParam("idCarrito") int idCarrito) {
        // Obtener la compra a borrar
        Compra compra = carritoRepository.findById(idCarrito).orElse(null);

        // Si la compra existe y no ha sido pagada, la borramos
        if (compra != null && compra.getEstado() != Estado.PAGADO) {
            // Devolver el stock a la planta
            Plantas planta = compra.getPlantas();
            planta.setStock(planta.getStock() + compra.getNumPlantas());

            // Borrar la compra
            carritoRepository.delete(compra);
        }

        // Redireccionar al listado de compras pendientes
        return "redirect:/carrito/pendientes";
    }

    //Procesar pag -> TODAS LAS COMPRAS LISTADAS PASAN A UN ESTADO PAGADAS y se reduce el stock
    @PostMapping("/pagar")
    public String procesarPago() {
        // Obtenemos el usuario actual
        Usuario usuarioActual = ...;

        // Obtenemos todas las compras pendientes del usuario actual
        List<Compra> comprasPendientes = carritoRepository.findByUsuarioAndEstado(usuarioActual, Estado.PENDIENTE);

        // Cambiamos el estado de todas las compras a "pagado" y reducimos el stock de la planta
        for (Compra compra : comprasPendientes) {
            compra.setEstado(Estado.PAGADO);
            Plantas planta = compra.getPlantas();
            planta.setStock(planta.getStock() - compra.getNumPlantas());
            plantasRepository.save(planta);
        }

        // Guardamos los cambios en la base de datos y redireccionamos al listado de compras pagadas
        carritoRepository.saveAll(comprasPendientes);
        return "redirect:/carrito/pagadas";
    }
}