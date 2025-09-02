package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Model.Usuario;
import com.Graxa_API.Graxa_API.Repository.UsuarioRepository;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioRepository repository;

    public UsuarioController(UsuarioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<?> getUsuarios(){
        try{
            return ResponseEntity.status(201).body(repository.findAll());
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUsuariosId(@PathVariable Long id){
        try{
            return ResponseEntity.status(201).body(repository.findById(id));
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuarios){
        try{
            return ResponseEntity.status(201).body(repository.save(usuarios));
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Usuario usuarios){
        try{
           Usuario response =  repository.findById(id)
                    .map(product -> {
                        product.setNome(usuarios.getNome());
                        return repository.save(product);
                    })
                    .orElseThrow(() -> new RuntimeException("Usuario não encontrado com o ID: " + id));

            return ResponseEntity.status(201).body(response);
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @DeleteMapping("{id")
    public ResponseEntity<?> desativar(@PathVariable Long id){
        try{
            Usuario response =  repository.findById(id)
                    .map(product -> {
                        product.setStatusUsuarioAtivo(false);
                        return repository.save(product);
                    })
                    .orElseThrow(() -> new RuntimeException("Usuario não encontrado com o ID: " + id));

            return ResponseEntity.status(201).body(response);
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }
}
