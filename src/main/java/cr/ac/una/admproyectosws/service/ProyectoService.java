package cr.ac.una.admproyectosws.service;

import cr.ac.una.admproyectosws.dao.AdministradorDao;
import cr.ac.una.admproyectosws.dao.ProyectoDao;
import cr.ac.una.admproyectosws.dto.ProyectoDto;
import cr.ac.una.admproyectosws.dto.RespuestaGeneral;
import cr.ac.una.admproyectosws.model.Administrador;
import cr.ac.una.admproyectosws.model.Proyecto;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class ProyectoService {
    
    @EJB
    private ProyectoDao proyectoDao;
    
    @EJB
    private AdministradorDao administradorDao;
    
    @EJB
    private EmailService emailService;
    
    public RespuestaGeneral<ProyectoDto> crear(ProyectoDto dto, Long creadoPorId) {
        try {
            // Validaciones básicas
            if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
                return new RespuestaGeneral<>(false, "El nombre del proyecto es requerido");
            }
            if (dto.getPatrocinadorNombre() == null || dto.getPatrocinadorNombre().trim().isEmpty()) {
                return new RespuestaGeneral<>(false, "El nombre del patrocinador es requerido");
            }
            if (dto.getFechaInicioPlanificada() == null) {
                return new RespuestaGeneral<>(false, "La fecha de inicio planificada es requerida");
            }
            if (dto.getFechaFinalPlanificada() == null) {
                return new RespuestaGeneral<>(false, "La fecha final planificada es requerida");
            }
            if (dto.getFechaInicioPlanificada().after(dto.getFechaFinalPlanificada())) {
                return new RespuestaGeneral<>(false, "La fecha de inicio no puede ser posterior a la fecha final");
            }
            
            Optional<Administrador> creadorOpt = administradorDao.buscarPorId(creadoPorId);
            if (!creadorOpt.isPresent()) {
                return new RespuestaGeneral<>(false, "Administrador creador no encontrado");
            }
            
            Proyecto proyecto = dto.toEntity();
            proyecto.setCreadoPor(creadorOpt.get());
            if (proyecto.getEstado() == null) {
                proyecto.setEstado("PLANIFICADO");
            }
            if (proyecto.getPorcentajeAvance() == null) {
                proyecto.setPorcentajeAvance(0);
            }
            
            proyecto = proyectoDao.crear(proyecto);
            
            // Enviar notificación por correo
            try {
                emailService.notificarCreacionProyecto(proyecto);
            } catch (Exception e) {
                // Log error but don't fail the creation
                System.err.println("Error enviando notificación: " + e.getMessage());
            }
            
            return new RespuestaGeneral<>(true, "Proyecto creado exitosamente", new ProyectoDto(proyecto));
            
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al crear proyecto: " + e.getMessage());
        }
    }
    
    public RespuestaGeneral<ProyectoDto> actualizar(ProyectoDto dto) {
        try {
            Optional<Proyecto> proyectoOpt = proyectoDao.buscarPorId(dto.getId());
            if (!proyectoOpt.isPresent()) {
                return new RespuestaGeneral<>(false, "Proyecto no encontrado");
            }
            
            Proyecto proyecto = proyectoOpt.get();
            String estadoAnterior = proyecto.getEstado();
            
            // Actualizar campos
            proyecto.setNombre(dto.getNombre());
            proyecto.setPatrocinadorNombre(dto.getPatrocinadorNombre());
            proyecto.setPatrocinadorCorreo(dto.getPatrocinadorCorreo());
            proyecto.setLiderUsuarioNombre(dto.getLiderUsuarioNombre());
            proyecto.setLiderUsuarioCorreo(dto.getLiderUsuarioCorreo());
            proyecto.setLiderTecnicoNombre(dto.getLiderTecnicoNombre());
            proyecto.setLiderTecnicoCorreo(dto.getLiderTecnicoCorreo());
            proyecto.setFechaInicioPlanificada(dto.getFechaInicioPlanificada());
            proyecto.setFechaFinalPlanificada(dto.getFechaFinalPlanificada());
            proyecto.setFechaInicioReal(dto.getFechaInicioReal());
            proyecto.setFechaFinalReal(dto.getFechaFinalReal());
            proyecto.setEstado(dto.getEstado());
            proyecto.setPorcentajeAvance(dto.getPorcentajeAvance());
            proyecto.setDescripcion(dto.getDescripcion());
            
            proyecto = proyectoDao.actualizar(proyecto);
            
            // Notificar cambio de estado si es necesario
            if (!estadoAnterior.equals(dto.getEstado())) {
                try {
                    emailService.notificarCambioEstadoProyecto(proyecto, estadoAnterior);
                } catch (Exception e) {
                    System.err.println("Error enviando notificación: " + e.getMessage());
                }
            }
            
            return new RespuestaGeneral<>(true, "Proyecto actualizado exitosamente", new ProyectoDto(proyecto));
            
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al actualizar proyecto: " + e.getMessage());
        }
    }
    
    public RespuestaGeneral<Void> eliminar(Long id) {
        try {
            proyectoDao.eliminar(id);
            return new RespuestaGeneral<>(true, "Proyecto eliminado exitosamente");
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al eliminar proyecto: " + e.getMessage());
        }
    }
    
    public RespuestaGeneral<ProyectoDto> buscarPorId(Long id) {
        try {
            Optional<Proyecto> proyectoOpt = proyectoDao.buscarPorId(id);
            if (proyectoOpt.isPresent()) {
                return new RespuestaGeneral<>(true, "Proyecto encontrado", new ProyectoDto(proyectoOpt.get()));
            } else {
                return new RespuestaGeneral<>(false, "Proyecto no encontrado");
            }
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al buscar proyecto: " + e.getMessage());
        }
    }
    
    public RespuestaGeneral<List<ProyectoDto>> obtenerTodos() {
        try {
            List<Proyecto> proyectos = proyectoDao.obtenerTodos();
            List<ProyectoDto> dtos = proyectos.stream()
                    .map(ProyectoDto::new)
                    .collect(Collectors.toList());
            return new RespuestaGeneral<>(true, "Proyectos obtenidos exitosamente", dtos);
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al obtener proyectos: " + e.getMessage());
        }
    }
    
    public RespuestaGeneral<List<ProyectoDto>> buscarActivos() {
        try {
            List<Proyecto> proyectos = proyectoDao.buscarActivos();
            List<ProyectoDto> dtos = proyectos.stream()
                    .map(ProyectoDto::new)
                    .collect(Collectors.toList());
            return new RespuestaGeneral<>(true, "Proyectos activos obtenidos exitosamente", dtos);
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al obtener proyectos activos: " + e.getMessage());
        }
    }
    
    // Esta es una Implementación usando Streams como lo pide Carranza
    public RespuestaGeneral<List<ProyectoDto>> buscarConStreams(String filtro) {
        try {
            List<ProyectoDto> dtos = proyectoDao.buscarConStreams(filtro)
                    .map(ProyectoDto::new)
                    .collect(Collectors.toList());
            return new RespuestaGeneral<>(true, "Búsqueda con streams realizada exitosamente", dtos);
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error en la búsqueda con streams: " + e.getMessage());
        }
    }
}