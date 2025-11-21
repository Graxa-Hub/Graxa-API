package com.Graxa_API.Graxa_API.Service;

import com.Graxa_API.Graxa_API.Entity.NotificacaoEntity;
import com.Graxa_API.Graxa_API.Entity.Usuario.ColaboradorEntity;
import com.Graxa_API.Graxa_API.Repository.ColaboradorRepository;
import com.Graxa_API.Graxa_API.Repository.NotificacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final ColaboradorRepository colaboradorRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository,
                              ColaboradorRepository colaboradorRepository) {
        this.notificacaoRepository = notificacaoRepository;
        this.colaboradorRepository = colaboradorRepository;
    }

    // Criar notificação com validação de colaborador existente
    public NotificacaoEntity criarNotificacao(Long colaboradorId, String mensagem, String tipo) {
        ColaboradorEntity colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

        NotificacaoEntity notificacao = new NotificacaoEntity();
        notificacao.setColaborador(colaborador);
        notificacao.setMensagem(mensagem);
        notificacao.setTipo(tipo);

        return notificacaoRepository.save(notificacao);
    }

    // Listar todas notificações de um colaborador
    public List<NotificacaoEntity> listarPorColaborador(Long colaboradorId) {
        return notificacaoRepository.findByColaboradorId(colaboradorId);
    }

    // Listar notificações não lidas
    public List<NotificacaoEntity> listarNaoLidas(Long colaboradorId) {
        return notificacaoRepository.findByColaboradorIdAndLidaFalse(colaboradorId);
    }

    // Marcar notificação como lida
    public NotificacaoEntity marcarComoLida(Long id) {
        NotificacaoEntity notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
        notificacao.setLida(true);
        return notificacaoRepository.save(notificacao);
    }
}
