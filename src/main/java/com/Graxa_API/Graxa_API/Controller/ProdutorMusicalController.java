package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.ProdutorMusicalEntity;
import com.Graxa_API.Graxa_API.Service.ProdutorMusicalService;
import com.Graxa_API.Graxa_API.dto.ProdutorMusicalDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/produtores")
public class ProdutorMusicalController {
    private final ProdutorMusicalService service;

    public ProdutorMusicalController(ProdutorMusicalService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<?> getProdutor(){
        try{
            return service.getProdutor();
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getProdutorId(@PathVariable Long id){

            return service.getProdutorId(id);

    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody ProdutorMusicalDto produtor){
        ProdutorMusicalEntity produtorMusicalEntity = new ProdutorMusicalEntity();
        produtorMusicalEntity.setNome(produtor.getNome());
        produtorMusicalEntity.setEmail(produtor.getEmail());
        produtorMusicalEntity.setSenha(produtor.getSenha());
        produtorMusicalEntity.setCpf(produtor.getCpf());
        produtorMusicalEntity.setAtivo(produtor.getAtivo());
        try{
            return service.cadastrar(produtor);
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ProdutorMusicalEntity produtor){
        try{
           return service.atualizar(id, produtor);
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> desativar(@PathVariable Long id){
        try{
            return service.desativar(id);
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @GetMapping("/ativos")
    public ResponseEntity<?> getUsuarioAtivo(){
        try{
            return service.getUsuarioAtivo();
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> findUsuariosByEmail(@PathVariable String email){
        try{
            return service.findUsuariosByEmail(email);
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<?> findUsuarioByCpf(@PathVariable String cpf){
        try{
            return service.findUsuarioByCpf(cpf);
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }


}
