package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.ProdutorMusicalEntity;
//import com.Graxa_API.Graxa_API.dto.ProdutorMusical;
import com.Graxa_API.Graxa_API.Repository.ProdutorMusicalRepository;
import com.Graxa_API.Graxa_API.dto.ProdutorMusicalDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/produtores")
public class ProdutorMusicalController {
    private final ProdutorMusicalRepository repository;

    public ProdutorMusicalController(ProdutorMusicalRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<?> getProdutor(){
        try{
            return ResponseEntity.status(200).body(repository.findAll());
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getProdutorId(@PathVariable Long id){
        try{
            return ResponseEntity.status(200).body(repository.findById(id));
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody ProdutorMusicalDto produtor){
        ProdutorMusicalEntity produtorMusicalEntity = new ProdutorMusicalEntity();
        produtorMusicalEntity.setNome(produtor.getNome());
        produtorMusicalEntity.setEmail(produtor.getEmail());
        produtorMusicalEntity.setSenha(produtor.getSenha());
        produtorMusicalEntity.setCpf(produtor.getCpf());
        produtorMusicalEntity.setAtivo(produtor.getAtivo());
        try{
            return ResponseEntity.status(201).body(repository.save(produtorMusicalEntity));
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ProdutorMusicalEntity produtor){
        try{
           ProdutorMusicalEntity response =  repository.findById(id)
                    .map(product -> {
                        if (produtor.getNome() != null) {
                            product.setNome(produtor.getNome());
                        }
                        if (produtor.getEmail() != null) {
                            product.setEmail(produtor.getEmail());
                        }
                        if (produtor.getCpf() != null) {
                            product.setCpf(produtor.getCpf());
                        }
                        return repository.save(product);
                    })
                    .orElseThrow(() -> new RuntimeException("Usuario não encontrado com o ID: " + id));

            return ResponseEntity.status(200).body(response);
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> desativar(@PathVariable Long id){
        try{
            ProdutorMusicalEntity response =  repository.findById(id)
                    .map(product -> {
                        product.setAtivo(false);
                        return repository.save(product);
                    })
                    .orElseThrow(() -> new RuntimeException("Usuario não encontrado com o ID: " + id));

            return ResponseEntity.status(204).build();
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @GetMapping("/ativos")
    public ResponseEntity<?> getUsuarioAtivo(){
        try{
            return ResponseEntity.status(200).body(repository.findByAtivoTrueOrderByNomeAsc());
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> findUsuariosByEmail(@PathVariable String email){
        try{
            return ResponseEntity.status(200).body(repository.findByEmail(email));
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<?> findUsuarioByCpf(@PathVariable String cpf){
        try{
            return ResponseEntity.status(200).body(repository.findByCpf(cpf));
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }
}
