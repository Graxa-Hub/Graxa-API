package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.dto.NotificacaoDto.RequestNotificacaoDto;
import com.Graxa_API.Graxa_API.dto.NotificacaoDto.ResponseNotificacaoDto;
import com.Graxa_API.Graxa_API.Service.NotificacaoService;
import com.Graxa_API.Graxa_API.Service.NotificacaoWebSocketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notificacoes")
@CrossOrigin(origins = "*") // Ajuste conforme sua necessidade de CORS
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    // ✅ NOVO: WebSocket service para testes manuais
    @Autowired
    private NotificacaoWebSocketService webSocketService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    // ✅ Criar notificação usando DTO no body (agora com WebSocket automático)
    @PostMapping
    public ResponseEntity<ResponseNotificacaoDto> criar(@Valid @RequestBody RequestNotificacaoDto request) {
        try {
            ResponseNotificacaoDto notificacao = notificacaoService.criarNotificacao(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(notificacao);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ✅ Criar notificação de alocação (endpoint específico com WebSocket automático)
    @PostMapping("/alocacao")
    public ResponseEntity<ResponseNotificacaoDto> criarNotificacaoAlocacao(
            @RequestParam Long alocacaoId,
            @RequestParam(required = false) String mensagem) {
        try {
            ResponseNotificacaoDto notificacao = notificacaoService.criarNotificacaoAlocacao(alocacaoId, mensagem);
            return ResponseEntity.status(HttpStatus.CREATED).body(notificacao);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ✅ NOVO: Notificar resposta de alocação via WebSocket
    @PostMapping("/alocacao/{alocacaoId}/resposta")
    public ResponseEntity<Map<String, String>> notificarRespostaAlocacao(
            @PathVariable Long alocacaoId,
            @RequestParam boolean aceito,
            @RequestParam String usernameGestor) {
        try {
            notificacaoService.notificarRespostaAlocacao(alocacaoId, aceito, usernameGestor);
            String mensagem = String.format("Notificação de %s enviada ao gestor via WebSocket",
                    aceito ? "aceitação" : "recusa");
            return ResponseEntity.ok(Map.of("message", mensagem));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Erro ao enviar notificação: " + e.getMessage()));
        }
    }

    // ✅ NOVO: Teste manual de WebSocket - enviar para usuário específico
    @PostMapping("/teste/websocket")
    public ResponseEntity<Map<String, String>> testarWebSocket(
            @RequestParam String username,
            @RequestParam String mensagem) {
        try {
            // Cria uma notificação mock para teste
            ResponseNotificacaoDto notificacaoTeste = new ResponseNotificacaoDto(
                    999L,
                    mensagem,
                    "TESTE",
                    false,
                    java.time.LocalDateTime.now(),
                    null // alocacao será null para teste
            );

            webSocketService.enviarNotificacaoParaColaborador(username, notificacaoTeste);
            return ResponseEntity.ok(Map.of("message", "Mensagem WebSocket enviada para: " + username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao enviar WebSocket: " + e.getMessage()));
        }
    }

    // ✅ NOVO: Teste broadcast para todos
    @PostMapping("/teste/broadcast")
    public ResponseEntity<Map<String, String>> testarBroadcast(@RequestParam String mensagem) {
        try {
            webSocketService.enviarParaTodos(mensagem);
            return ResponseEntity.ok(Map.of("message", "Broadcast enviado para todos os usuários conectados"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao enviar broadcast: " + e.getMessage()));
        }
    }

    // ✅ NOVO: Atualizar contador manualmente
    @PostMapping("/teste/contador")
    public ResponseEntity<Map<String, String>> atualizarContador(
            @RequestParam String username,
            @RequestParam long contador) {
        try {
            webSocketService.enviarContadorParaColaborador(username, contador);
            return ResponseEntity.ok(Map.of("message", "Contador atualizado para: " + username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao atualizar contador: " + e.getMessage()));
        }
    }

    // Criar com query params (mantém compatibilidade)
    @PostMapping("/params")
    public ResponseEntity<ResponseNotificacaoDto> criarComParams(
            @RequestParam Long colaboradorId,
            @RequestParam String mensagem,
            @RequestParam String tipo,
            @RequestParam(required = false) Long alocacaoId) {
        try {
            RequestNotificacaoDto request = new RequestNotificacaoDto(colaboradorId, mensagem, tipo, alocacaoId);
            ResponseNotificacaoDto notificacao = notificacaoService.criarNotificacao(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(notificacao);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Listar todas notificações de um colaborador
    @GetMapping("/colaborador/{colaboradorId}")
    public ResponseEntity<List<ResponseNotificacaoDto>> listarPorColaborador(@PathVariable Long colaboradorId) {
        try {
            List<ResponseNotificacaoDto> notificacoes = notificacaoService.listarPorColaborador(colaboradorId);
            return ResponseEntity.ok(notificacoes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Listar notificações não lidas
    @GetMapping("/colaborador/{colaboradorId}/nao-lidas")
    public ResponseEntity<List<ResponseNotificacaoDto>> listarNaoLidas(@PathVariable Long colaboradorId) {
        try {
            List<ResponseNotificacaoDto> notificacoes = notificacaoService.listarNaoLidas(colaboradorId);
            return ResponseEntity.ok(notificacoes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Contar notificações não lidas
    @GetMapping("/colaborador/{colaboradorId}/contador")
    public ResponseEntity<Map<String, Long>> contarNaoLidas(@PathVariable Long colaboradorId) {
        try {
            long count = notificacaoService.contarNaoLidas(colaboradorId);
            return ResponseEntity.ok(Map.of("naoLidas", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Marcar uma notificação como lida (agora com WebSocket automático para contador)
    @PutMapping("/{id}/lida")
    public ResponseEntity<ResponseNotificacaoDto> marcarComoLida(@PathVariable Long id) {
        try {
            ResponseNotificacaoDto notificacao = notificacaoService.marcarComoLida(id);
            return ResponseEntity.ok(notificacao);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Marcar todas as notificações como lidas (agora com WebSocket automático)
    @PutMapping("/colaborador/{colaboradorId}/marcar-todas-lidas")
    public ResponseEntity<Map<String, String>> marcarTodasComoLidas(@PathVariable Long colaboradorId) {
        try {
            notificacaoService.marcarTodasComoLidas(colaboradorId);
            return ResponseEntity.ok(Map.of("message", "Todas as notificações foram marcadas como lidas"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao marcar notificações como lidas"));
        }
    }

    // ✅ Status da conexão WebSocket
    @GetMapping("/status/websocket")
    public ResponseEntity<Map<String, String>> statusWebSocket() {
        return ResponseEntity.ok(Map.of(
                "status", "WebSocket configurado",
                "endpoint", "/ws/notificacoes",
                "canais", "/user/queue/notificacoes, /user/queue/contador, /topic/broadcast"
        ));
    }

    // Deletar uma notificação (opcional)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        try {
            // Implementar se necessário
            return ResponseEntity.ok(Map.of("message", "Funcionalidade ainda não implementada"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}