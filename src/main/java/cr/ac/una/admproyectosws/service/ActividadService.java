package cr.ac.una.admproyectosws.service;

import cr.ac.una.admproyectosws.dao.ActividadDao;
import cr.ac.una.admproyectosws.dao.ProyectoDao;
import cr.ac.una.admproyectosws.dto.ActividadDto;
import cr.ac.una.admproyectosws.dto.RespuestaGeneral;
import cr.ac.una.admproyectosws.model.Actividad;
import cr.ac.una.admproyectosws.model.Proyecto;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Esto concentra la logica para actividades
@Stateless
public class ActividadService {
    
    // Esto accede a la persistencia de actividades
    @EJB
    private ActividadDao actividadDao;
    
    // Esto permite buscar datos del proyecto
    @EJB
    private ProyectoDao proyectoDao;
    
    // Esto envía notificaciones por correo
    @EJB
    private EmailService emailService;
    
     // Esto crea una actividad nueva con validaciones basicas
    public RespuestaGeneral<ActividadDto> crear(ActividadDto dto) {
        try {
            //validaciones
            if (dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()) {
                return new RespuestaGeneral<>(false, "La descripción de la actividad es requerida");
            }
            if (dto.getEncargadoNombre() == null || dto.getEncargadoNombre().trim().isEmpty()) {
                return new RespuestaGeneral<>(false, "El nombre del encargado es requerido");
            }
            if (dto.getEncargadoCorreo() == null || dto.getEncargadoCorreo().trim().isEmpty()) {
                return new RespuestaGeneral<>(false, "El correo del encargado es requerido");
            }
            if (dto.getProyectoId() == null) {
                return new RespuestaGeneral<>(false, "El proyecto es requerido");
            }
            
            Optional<Proyecto> proyectoOpt = proyectoDao.buscarPorId(dto.getProyectoId());
            if (!proyectoOpt.isPresent()) {
                return new RespuestaGeneral<>(false, "Proyecto no encontrado");
            }
            
            Actividad actividad = dto.toEntity();
            actividad.setProyecto(proyectoOpt.get());
            
            if (actividad.getEstado() == null) {
                actividad.setEstado("PLANIFICADA");
            }
            if (actividad.getOrdenEjecucion() == null) {
                actividad.setOrdenEjecucion(actividadDao.obtenerSiguienteOrden(dto.getProyectoId()));
            }
            
            actividad = actividadDao.crear(actividad);
            
            
            try {
                emailService.notificarCreacionActividad(actividad);
            } catch (Exception e) {
                System.err.println("Error enviando notificación: " + e.getMessage());
            }
            
            return new RespuestaGeneral<>(true, "Actividad creada exitosamente", new ActividadDto(actividad));
            
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al crear actividad: " + e.getMessage());
        }
    }
    
    // Esto actualiza una actividad y notifica si el estado cambió
    public RespuestaGeneral<ActividadDto> actualizar(ActividadDto dto) {
        try {
            Optional<Actividad> actividadOpt = actividadDao.buscarPorId(dto.getId());
            if (!actividadOpt.isPresent()) {
                return new RespuestaGeneral<>(false, "Actividad no encontrada");
            }
            
            Actividad actividad = actividadOpt.get();
            String estadoAnterior = actividad.getEstado();
            
            actividad.setDescripcion(dto.getDescripcion());
            actividad.setEncargadoNombre(dto.getEncargadoNombre());
            actividad.setEncargadoCorreo(dto.getEncargadoCorreo());
            actividad.setEstado(dto.getEstado());
            actividad.setFechaInicioPlanificada(dto.getFechaInicioPlanificada());
            actividad.setFechaFinalPlanificada(dto.getFechaFinalPlanificada());
            actividad.setFechaInicioReal(dto.getFechaInicioReal());
            actividad.setFechaFinalReal(dto.getFechaFinalReal());
            actividad.setOrdenEjecucion(dto.getOrdenEjecucion());
            
            actividad = actividadDao.actualizar(actividad);
            
            
            if (!estadoAnterior.equals(dto.getEstado())) {
                try {
                    emailService.notificarCambioEstadoActividad(actividad, estadoAnterior);
                } catch (Exception e) {
                    System.err.println("Error enviando notificación: " + e.getMessage());
                }
            }
            
            return new RespuestaGeneral<>(true, "Actividad actualizada exitosamente", new ActividadDto(actividad));
            
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al actualizar actividad: " + e.getMessage());
        }
    }
    
    // Esto elimina una actividad
    public RespuestaGeneral<Void> eliminar(Long id) {
        try {
            actividadDao.eliminar(id);
            return new RespuestaGeneral<>(true, "Actividad eliminada exitosamente");
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al eliminar actividad: " + e.getMessage());
        }
    }
    
    // Esto busca las actividades de un proyecto y las retorna como DTOs
    public RespuestaGeneral<List<ActividadDto>> buscarPorProyecto(Long proyectoId) {
        try {
            List<Actividad> actividades = actividadDao.buscarPorProyecto(proyectoId);
            List<ActividadDto> dtos = actividades.stream()
                    .map(ActividadDto::new)
                    .collect(Collectors.toList());
            return new RespuestaGeneral<>(true, "Actividades obtenidas exitosamente", dtos);
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al obtener actividades: " + e.getMessage());
        }
    }
    
    // Esto reordena las actividades de un proyecto segun el nuevo orden
    public RespuestaGeneral<Void> reordenarActividades(Long proyectoId, List<Long> nuevoOrden) {
        try {
            actividadDao.reordenarActividades(proyectoId, nuevoOrden);
            return new RespuestaGeneral<>(true, "Actividades reordenadas exitosamente");
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al reordenar actividades: " + e.getMessage());
        }
    }
}