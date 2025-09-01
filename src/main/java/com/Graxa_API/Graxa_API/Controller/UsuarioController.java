package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @GetMapping
    public ResponseEntity<?> getUsuarios(){
        Object Usuario = null;
        return ResponseEntity.status(201).body(Usuario);
    }
}
