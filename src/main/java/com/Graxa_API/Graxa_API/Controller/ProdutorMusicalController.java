package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.ProdutorMusicalEntity;
import com.Graxa_API.Graxa_API.Service.ProdutorMusicalService;
import com.Graxa_API.Graxa_API.dto.ProdutorMusical.RequestProdutorMusicalDto;
import com.Graxa_API.Graxa_API.dto.ProdutorMusical.ResponseProdutorMusicalDto;
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

        return service.getProdutor();
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getProdutorId(@PathVariable Long id){
            return service.getProdutorId(id);
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody RequestProdutorMusicalDto produtor){
        ProdutorMusicalEntity produtorMusicalEntity = new ProdutorMusicalEntity(produtor);
            return service.cadastrar(produtor);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody RequestProdutorMusicalDto produtor){
           return service.atualizar(id, produtor);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> desativar(@PathVariable Long id){
            return service.desativar(id);
    }

    @GetMapping("/ativos")
    public ResponseEntity<?> getUsuarioAtivo(){

            return service.getUsuarioAtivo();

    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> findUsuariosByEmail(@PathVariable String email){
        return service.findUsuariosByEmail(email);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<?> findUsuarioByCpf(@PathVariable String cpf){
            return service.findUsuarioByCpf(cpf);

    }


}
