package com.example.dontulipanclient.controller;

import com.example.dontulipanclient.dao.ProductDao;
import com.example.dontulipanclient.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    ProductDao productDao;

    @GetMapping(value={"/list","/",""})
    public String listarProductos(Model model) throws JsonProcessingException {
        model.addAttribute("productList",productDao.listarProductos());

        // creas un objeto ObjectMapper para convertir objetos a JSON
        ObjectMapper mapper = new ObjectMapper();

        // conviertes la lista de productos a JSON
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        String json = writer.writeValueAsString(productDao.listarProductos());

        // Se envia al javascript
        model.addAttribute("productListJSON",json);
        return "product/list";
    }

    @GetMapping(value = "/new")
    public String nuevoProducto(@ModelAttribute("product") Product product){
        return "product/form";
    }


    @PostMapping("/save")
    public String guardarProducto(RedirectAttributes attr,
                                  @ModelAttribute("product")  Product product,
                                  BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return "product/form";
        }else{
            if(product.getId().equals("")){
                product.setId(null);
                attr.addFlashAttribute("msg", "Producto creado exitosamente");
            }else{
                attr.addFlashAttribute("msg1", "Producto actualizado exitosamente");
            }
            if(product.getStock()==null){
                product.setStock(false);
            }

            productDao.guardarProducto(product);
            return "redirect:/product";
        }


    }

    @GetMapping("/edit")
    public String editarProducto(Model model, @RequestParam("id") String id) {

        Product productBuscar = productDao.obtenerProductoPorId(id);

        if (productBuscar != null) {
            model.addAttribute("product", productBuscar);
            return "product/form";
        } else {
            return "redirect:/product";
        }
    }

    @GetMapping("/delete")
    public String borrarProducto( @RequestParam("id") String id,
                                 RedirectAttributes attr) {

        Product productBuscar = productDao.obtenerProductoPorId(id);

        if (productBuscar != null) {
            productDao.borrarProducto(id);
            attr.addFlashAttribute("msg2", "Producto borrado exitosamente");
        }
        return "redirect:/product";

    }
}
