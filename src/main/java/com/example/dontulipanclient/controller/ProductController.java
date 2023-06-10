package com.example.dontulipanclient.controller;

import com.example.dontulipanclient.dao.ProductDao;
import com.example.dontulipanclient.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

@Controller
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    ProductDao productDao;

    @GetMapping(value={"/list","/",""})
    public String listarProductos(Model model) {
        model.addAttribute("productList",productDao.listarProductos());
        return "product/list";
    }

    @GetMapping(value = "/new")
    public String nuevoProducto(@ModelAttribute("product") Product product){
        System.out.println(product.getId());
        System.out.println(product.getId()==null? '1' : '0');
        return "product/form";
    }


    @PostMapping("/save")
    public String guardarProducto(@RequestParam("archivo") MultipartFile archivo,
                                  @RequestParam("fotos") MultipartFile[] archivos,
                                  RedirectAttributes attr,
                                  @ModelAttribute("product")  Product product,
                                  BindingResult bindingResult,
                                  Model model) {

        if(archivo.isEmpty()){
            model.addAttribute("msg","Debe subir un archivo");
            return "product/form";
        }
        String fileName = archivo.getOriginalFilename();
        if(fileName.contains("..")){
            model.addAttribute("msg", "No se permiten '..' en el archivo");
            return "product/form";
        }

        if (archivos == null || archivos.length == 0) {
            model.addAttribute("msg", "Debe subir al menos un archivo");
            return "product/form";
        }

        for (MultipartFile f0t0 : archivos) {
            if (f0t0.isEmpty()) {
                model.addAttribute("msg", "Debe subir archivos válidos");
                return "product/form";
            }

            String fName = archivo.getOriginalFilename();
            if (fName.contains("..")) {
                model.addAttribute("msg", "No se permiten '..' en el nombre del archivo");
                return "product/form";
            }
        }
        if(bindingResult.hasErrors()){
            return "product/form";
        }else{
            if(product.getId().equals("") || product.getId()==null){
                productDao.crearProducto(product, archivos, archivo);
                attr.addFlashAttribute("msg", "Producto creado exitosamente");
            }
            return "redirect:/product";
        }


    }

    @PostMapping("/update")
    public String actualizarProducto(@RequestParam("archivo") MultipartFile archivo,
                                     @RequestParam("fotos") MultipartFile[] archivos,
                                     @ModelAttribute("product")  Product product,
                                     RedirectAttributes attr) {
        Product productBuscar = productDao.obtenerProductoPorId(product.getId());

        if (productBuscar != null) {
            productDao.updatePlant(product.getId(),product,archivos,archivo);
            attr.addFlashAttribute("msg1", "Producto actualizado exitosamente");
            return "redirect:/product";
        } else {
            return "product/form";
        }
    }


    @GetMapping("/edit")
    public String editarProducto(Model model, @RequestParam("id") String id) {

        Product productBuscar = productDao.obtenerProductoPorId(id);

        if (productBuscar != null) {
            System.out.println(id);
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
