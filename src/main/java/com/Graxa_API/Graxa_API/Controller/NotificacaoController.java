package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.NotificacaoEntity;
import com.Graxa_API.Graxa_API.Service.NotificacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    // Criar notificação
    @PostMapping
    public ResponseEntity<NotificacaoEntity> criar(@RequestParam Long colaboradorId,
                                                   @RequestParam String mensagem,
                                                   @RequestParam String tipo) {
        return ResponseEntity.ok(notificacaoService.criarNotificacao(colaboradorId, mensagem, tipo));
    }

    // Listar todas notificações de um colaborador
    @GetMapping("/colaborador/{colaboradorId}")
    public ResponseEntity<List<NotificacaoEntity>> listarPorColaborador(@PathVariable Long colaboradorId) {
        return ResponseEntity.ok(notificacaoService.listarPorColaborador(colaboradorId));
    }

    // Listar notificações não lidas
    @GetMapping("/colaborador/{colaboradorId}/nao-lidas")
    public ResponseEntity<List<NotificacaoEntity>> listarNaoLidas(@PathVariable Long colaboradorId) {
        return ResponseEntity.ok(notificacaoService.listarNaoLidas(colaboradorId));
    }

    // Marcar notificação como lida
    @PutMapping("/{id}/lida")
    public ResponseEntity<NotificacaoEntity> marcarComoLida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacaoService.marcarComoLida(id));
    }
}
