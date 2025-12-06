package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.Evento.HotelEventoEntity;
import com.Graxa_API.Graxa_API.Service.HotelEventoService;
import com.Graxa_API.Graxa_API.dto.Hotel.HotelEventoCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel-evento")
@CrossOrigin("*")
public class HotelEventoController {

    @Autowired
    private HotelEventoService hotelEventoService;

    // 👉 Criar hotel para evento
    @PostMapping
    public ResponseEntity<?> criarHotel(@RequestBody HotelEventoCreateDTO dto) {
        HotelEventoEntity saved = hotelEventoService.criar(dto);
        return ResponseEntity.ok(saved);
    }

    // 👉 Listar hotéis de um evento
    @GetMapping("/show/{showId}")
    public ResponseEntity<List<HotelEventoEntity>> listarPorShow(@PathVariable Long showId) {
        List<HotelEventoEntity> lista = hotelEventoService.listarPorShow(showId);
        return ResponseEntity.ok(lista);
    }

    // 👉 Remover item de hotel
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        hotelEventoService.remover(id);
        return ResponseEntity.ok().build();
    }
}
