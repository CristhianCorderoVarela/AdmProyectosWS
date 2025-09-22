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

/**
 Reglas de negocio para gestionar proyectos.
  Coordina validaciones de entrada.
  Interactúa con DAO para persistencia.
  Da notificaciones por correo según eventos.
 */


@Stateless
public class ProyectoService {
    
    @EJB
    private ProyectoDao proyectoDao;
    
    @EJB
    private AdministradorDao administradorDao;
    
    @EJB
    private EmailService emailService;
    
    
    
   public RespuestaGeneral<ProyectoDto> crear(ProyectoDto dto, Long creadoPorId) {
    System.out.println("=== INICIO DEBUG CREACION PROYECTO ===");
    try {
        System.out.println("1. Iniciando creación de proyecto: " + dto.getNombre());
        
        // Validaciones 
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            System.out.println("ERROR: Nombre vacío");
            return new RespuestaGeneral<>(false, "El nombre del proyecto es requerido");
        }
        if (dto.getPatrocinadorNombre() == null || dto.getPatrocinadorNombre().trim().isEmpty()) {
            System.out.println("ERROR: Patrocinador nombre vacío");
            return new RespuestaGeneral<>(false, "El nombre del patrocinador es requerido");
        }
        if (dto.getFechaInicioPlanificada() == null) {
            System.out.println("ERROR: Fecha inicio vacía");
            return new RespuestaGeneral<>(false, "La fecha de inicio planificada es requerida");
        }
        if (dto.getFechaFinalPlanificada() == null) {
            System.out.println("ERROR: Fecha final vacía");
            return new RespuestaGeneral<>(false, "La fecha final planificada es requerida");
        }
        if (dto.getFechaInicioPlanificada().after(dto.getFechaFinalPlanificada())) {
            System.out.println("ERROR: Fecha inicio después de fecha final");
            return new RespuestaGeneral<>(false, "La fecha de inicio no puede ser posterior a la fecha final");
        }
        
        System.out.println("2. Validaciones básicas pasadas");
        
        Optional<Administrador> creadorOpt = administradorDao.buscarPorId(creadoPorId);
        if (!creadorOpt.isPresent()) {
            System.out.println("ERROR: Administrador no encontrado ID: " + creadoPorId);
            return new RespuestaGeneral<>(false, "Administrador creador no encontrado");
        }
        
        System.out.println("3. Administrador encontrado: " + creadorOpt.get().getNombre());
        
        
        System.out.println("4. DATOS DEL DTO RECIBIDO:");
        System.out.println("   - Nombre: " + dto.getNombre());
        System.out.println("   - Patrocinador nombre: " + dto.getPatrocinadorNombre());
        System.out.println("   - Patrocinador correo: " + dto.getPatrocinadorCorreo());
        System.out.println("   - Líder usuario nombre: " + dto.getLiderUsuarioNombre());
        System.out.println("   - Líder usuario correo: " + dto.getLiderUsuarioCorreo());
        System.out.println("   - Líder técnico nombre: " + dto.getLiderTecnicoNombre());
        System.out.println("   - Líder técnico correo: " + dto.getLiderTecnicoCorreo());
        
        Proyecto proyecto = dto.toEntity();
        proyecto.setCreadoPor(creadorOpt.get());
        if (proyecto.getEstado() == null) {
            proyecto.setEstado("PLANIFICADO");
        }
        if (proyecto.getPorcentajeAvance() == null) {
            proyecto.setPorcentajeAvance(0);
        }
        
        System.out.println("5. Entidad creada, guardando en BD...");
        proyecto = proyectoDao.crear(proyecto);
        System.out.println("6. Proyecto guardado en BD con ID: " + proyecto.getId());
        
        
        System.out.println("7. DATOS DEL PROYECTO DESPUÉS DE GUARDAR:");
        System.out.println("   - ID: " + proyecto.getId());
        System.out.println("   - Nombre: " + proyecto.getNombre());
        System.out.println("   - Estado: " + proyecto.getEstado());
        System.out.println("   - Patrocinador nombre: " + proyecto.getPatrocinadorNombre());
        System.out.println("   - Patrocinador correo: " + proyecto.getPatrocinadorCorreo());
        System.out.println("   - Líder usuario nombre: " + proyecto.getLiderUsuarioNombre());
        System.out.println("   - Líder usuario correo: " + proyecto.getLiderUsuarioCorreo());
        System.out.println("   - Líder técnico nombre: " + proyecto.getLiderTecnicoNombre());
        System.out.println("   - Líder técnico correo: " + proyecto.getLiderTecnicoCorreo());
        
        
        boolean hayCorreos = false;
        if (proyecto.getPatrocinadorCorreo() != null && !proyecto.getPatrocinadorCorreo().trim().isEmpty()) {
            System.out.println("   ✓ Patrocinador correo válido");
            hayCorreos = true;
        } else {
            System.out.println("   ✗ Patrocinador correo VACÍO/NULL");
        }
        
        if (proyecto.getLiderUsuarioCorreo() != null && !proyecto.getLiderUsuarioCorreo().trim().isEmpty()) {
            System.out.println("   ✓ Líder usuario correo válido");
            hayCorreos = true;
        } else {
            System.out.println("   ✗ Líder usuario correo VACÍO/NULL");
        }
        
        if (proyecto.getLiderTecnicoCorreo() != null && !proyecto.getLiderTecnicoCorreo().trim().isEmpty()) {
            System.out.println("   ✓ Líder técnico correo válido");
            hayCorreos = true;
        } else {
            System.out.println("   ✗ Líder técnico correo VACÍO/NULL");
        }
        
        System.out.println("8. ¿Hay al menos un correo válido? " + hayCorreos);
        
        
        System.out.println("9. INTENTANDO ENVIAR NOTIFICACIÓN...");
        try {
            emailService.notificarCreacionProyecto(proyecto);
            System.out.println("10. ✓ Llamada a emailService.notificarCreacionProyecto() completada SIN excepciones");
        } catch (Exception e) {
            System.err.println("10. ✗ ERROR en emailService.notificarCreacionProyecto():");
            System.err.println("    Mensaje: " + e.getMessage());
            System.err.println("    Clase: " + e.getClass().getName());
            e.printStackTrace();
        }
        
        System.out.println("11. Creando respuesta final...");
        ProyectoDto respuestaDto = new ProyectoDto(proyecto);
        System.out.println("12. ✓ Proyecto creado exitosamente");
        
        return new RespuestaGeneral<>(true, "Proyecto creado exitosamente", respuestaDto);
        
    } catch (Exception e) {
        System.err.println("ERROR GENERAL en crear proyecto:");
        System.err.println("Mensaje: " + e.getMessage());
        System.err.println("Clase: " + e.getClass().getName());
        e.printStackTrace();
        return new RespuestaGeneral<>(false, "Error al crear proyecto: " + e.getMessage());
    } finally {
        System.out.println("=== FIN DEBUG CREACION PROYECTO ===");
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