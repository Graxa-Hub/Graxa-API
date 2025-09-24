package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.ProdutorMusicalEntity;
import com.Graxa_API.Graxa_API.Exception.CpfDuplicadoException;
import com.Graxa_API.Graxa_API.Exception.EmailDuplicadoException;
import com.Graxa_API.Graxa_API.Exception.UsuarioNaoEncontradoException;
import com.Graxa_API.Graxa_API.Exception.UsuariosNaoEncontradosException;
import com.Graxa_API.Graxa_API.Repository.ProdutorMusicalRepository;
import com.Graxa_API.Graxa_API.dto.ProdutorMusical.RequestProdutorMusicalDto;
import org.springframework.http.ResponseEntity;
import com.Graxa_API.Graxa_API.dto.ProdutorMusical.ResponseProdutorMusicalDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ProdutorMusicalService {
    private final ProdutorMusicalRepository repository;

    public ProdutorMusicalService(ProdutorMusicalRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<?> getProdutor(){

            List<ProdutorMusicalEntity> produtores = repository.findAll();
            if(produtores.isEmpty()){
                throw new UsuariosNaoEncontradosException();
            }
            return ResponseEntity.status(200).body(ResponseProdutorMusicalDto.toResponse(produtores));


    }

    public ResponseEntity<?> getProdutorId(@PathVariable Long id){
        return ResponseEntity.status(200)
                .body(ResponseProdutorMusicalDto.toResponse(repository.findById(id)
                        .orElseThrow(() -> new UsuarioNaoEncontradoException(id))));
    }


    public ResponseEntity<Object> cadastrar(@RequestBody RequestProdutorMusicalDto produtor){
        if (repository.existsByCpfAllIgnoreCase(produtor.cpf())) {
            throw new CpfDuplicadoException();
        }
        if(repository.existsByEmailIgnoreCase(produtor.email())){
            throw new EmailDuplicadoException();
        }

        ProdutorMusicalEntity produtorMusicalEntity = new ProdutorMusicalEntity(produtor);

        return ResponseEntity.ok(ResponseProdutorMusicalDto.toResponse(repository.save(produtorMusicalEntity)));

    }

    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody RequestProdutorMusicalDto produtor){
        try{
            ProdutorMusicalEntity response =  repository.findById(id)
                    .map(product -> {
                        if (produtor.nome() != null) {
                            product.setNome(produtor.nome());
                        }
                        if (produtor.email() != null) {
                            product.setEmail(produtor.email());
                        }
                        if (produtor.cpf() != null) {
                            product.setCpf(produtor.cpf());
                        }
                        return repository.save(product);
                    })
                    .orElseThrow(() -> new RuntimeException("Usuario não encontrado com o ID: " + id));

            return ResponseEntity.status(200).body(ResponseProdutorMusicalDto.toResponse(response));
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

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

    public ResponseEntity<?> getUsuarioAtivo(){
        try{
            return ResponseEntity.status(200).body(repository.findByAtivoTrueOrderByNomeAsc());
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    public ResponseEntity<?> findUsuariosByEmail(@PathVariable String email){
        try{
            return ResponseEntity.status(200).body(repository.findByEmail(email));
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }

    public ResponseEntity<?> findUsuarioByCpf(@PathVariable String cpf){
        try{
            return ResponseEntity.status(200).body(repository.findByCpf(cpf));
        }catch(Exception e){
            return ResponseEntity.status(500).body(e);
        }
    }
}