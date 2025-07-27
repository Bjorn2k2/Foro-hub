package com.alura.forohub.domain.topico.dto;

import java.time.LocalDateTime;
import com.alura.forohub.domain.topico.StatusTopico;

public record DatosRespuestaTopico(
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        StatusTopico status,
        String autor,
        String curso
) {}