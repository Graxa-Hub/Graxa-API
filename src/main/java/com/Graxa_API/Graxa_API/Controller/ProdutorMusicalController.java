package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Model.ProdutorMusical;
import com.Graxa_API.Graxa_API.Repository.ProdutorMusicalRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/produtor")
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
    public ResponseEntity<?> cadastrar(@RequestBody ProdutorMusical produtor){
        try{
            return ResponseEntity.status(201).body(repository.save(produtor));
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ProdutorMusical produtor){
        try{
           ProdutorMusical response =  repository.findById(id)
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
            ProdutorMusical response =  repository.findById(id)
                    .map(product -> {
                        product.setStatusUsuarioAtivo(false);
                        return repository.save(product);
                    })
                    .orElseThrow(() -> new RuntimeException("Usuario não encontrado com o ID: " + id));

            return ResponseEntity.status(204).build();
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }
}
