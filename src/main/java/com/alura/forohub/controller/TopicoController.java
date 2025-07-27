package com.alura.forohub.controller;

import com.alura.forohub.domain.topico.Topico;
import com.alura.forohub.domain.topico.TopicoRepository;
import com.alura.forohub.domain.topico.dto.DatosRegistroTopico;
import com.alura.forohub.domain.topico.dto.DatosActualizarTopico;
import com.alura.forohub.domain.topico.dto.DatosRespuestaTopico;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<Topico> registrar(@RequestBody @Valid DatosRegistroTopico datos) {
        if (repository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Topico topico = new Topico();
        topico.setTitulo(datos.titulo());
        topico.setMensaje(datos.mensaje());
        topico.setAutor(datos.autor());
        topico.setCurso(datos.curso());
        repository.save(topico);
        return ResponseEntity.status(HttpStatus.CREATED).body(topico);
    }

    @GetMapping
    public ResponseEntity<List<DatosRespuestaTopico>> listar() {
        List<DatosRespuestaTopico> lista = repository.findAll().stream()
                .map(t -> new DatosRespuestaTopico(
                        t.getTitulo(),
                        t.getMensaje(),
                        t.getFechaCreacion(),
                        t.getStatus(),
                        t.getAutor(),
                        t.getCurso()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> detalle(@PathVariable Long id) {
        return repository.findById(id)
                .map(t -> new DatosRespuestaTopico(
                        t.getTitulo(),
                        t.getMensaje(),
                        t.getFechaCreacion(),
                        t.getStatus(),
                        t.getAutor(),
                        t.getCurso()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizar(@PathVariable Long id,
                                                           @RequestBody @Valid DatosActualizarTopico datos) {
        return repository.findById(id)
                .map(topico -> {
                    if (repository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).<DatosRespuestaTopico>build();
                    }
                    topico.setTitulo(datos.titulo());
                    topico.setMensaje(datos.mensaje());
                    return ResponseEntity.ok(new DatosRespuestaTopico(
                            topico.getTitulo(),
                            topico.getMensaje(),
                            topico.getFechaCreacion(),
                            topico.getStatus(),
                            topico.getAutor(),
                            topico.getCurso()
                    ));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return repository.findById(id)
                .map(topico -> {
                    repository.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}