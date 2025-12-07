package com.Graxa_API.Graxa_API.Controller;

import com.Graxa_API.Graxa_API.Entity.Evento.HotelEventoEntity;
import com.Graxa_API.Graxa_API.Service.HotelEventoService;
import com.Graxa_API.Graxa_API.dto.Hotel.HotelEventoCreateDTO;
import com.Graxa_API.Graxa_API.dto.Logistica.HotelDTO;
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

    @PostMapping
    public ResponseEntity<?> criarHotel(@RequestBody HotelEventoCreateDTO dto) {
        HotelEventoEntity saved = hotelEventoService.criar(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/show/{showId}")
    public ResponseEntity<List<HotelDTO>> listarPorShow(@PathVariable Long showId) {
        return ResponseEntity.ok(hotelEventoService.listarPorShow(showId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remover(@PathVariable Long id) {
        hotelEventoService.remover(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelEventoEntity> atualizar(
            @PathVariable Long id,
            @RequestBody HotelEventoEntity dto) {

        return ResponseEntity.ok(hotelEventoService.atualizar(id, dto));
    }
}
