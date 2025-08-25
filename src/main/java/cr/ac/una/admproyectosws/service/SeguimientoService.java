package cr.ac.una.admproyectosws.service;

import cr.ac.una.admproyectosws.dao.SeguimientoProyectoDao;
import cr.ac.una.admproyectosws.dao.ProyectoDao;
import cr.ac.una.admproyectosws.dao.AdministradorDao;
import cr.ac.una.admproyectosws.dto.SeguimientoProyectoDto;
import cr.ac.una.admproyectosws.dto.RespuestaGeneral;
import cr.ac.una.admproyectosws.model.SeguimientoProyecto;
import cr.ac.una.admproyectosws.model.Proyecto;
import cr.ac.una.admproyectosws.model.Administrador;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class SeguimientoService {
    
    @EJB
    private SeguimientoProyectoDao seguimientoDao;
    
    @EJB
    private ProyectoDao proyectoDao;
    
    @EJB
    private AdministradorDao administradorDao;
    
    @EJB
    private EmailService emailService;
    
    public RespuestaGeneral<List<SeguimientoProyectoDto>> buscarPorProyecto(Long proyectoId) {
        try {
            List<SeguimientoProyecto> seguimientos = seguimientoDao.buscarPorProyecto(proyectoId);
            List<SeguimientoProyectoDto> dtos = seguimientos.stream()
                    .map(SeguimientoProyectoDto::new)
                    .collect(Collectors.toList());
            return new RespuestaGeneral<>(true, "Seguimientos obtenidos exitosamente", dtos);
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al obtener seguimientos: " + e.getMessage());
        }
    }
    
    public RespuestaGeneral<SeguimientoProyectoDto> buscarUltimo(Long proyectoId) {
        try {
            Optional<SeguimientoProyecto> seguimientoOpt = seguimientoDao.buscarUltimoPorProyecto(proyectoId);
            if (seguimientoOpt.isPresent()) {
                return new RespuestaGeneral<>(true, "Último seguimiento encontrado", new SeguimientoProyectoDto(seguimientoOpt.get()));
            } else {
                return new RespuestaGeneral<>(false, "No se encontraron seguimientos para este proyecto");
            }
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al buscar último seguimiento: " + e.getMessage());
        }
    }
    
    public RespuestaGeneral<List<SeguimientoProyectoDto>> buscarPorFecha(Date fechaInicio, Date fechaFin) {
        try {
            List<SeguimientoProyecto> seguimientos = seguimientoDao.buscarPorFecha(fechaInicio, fechaFin);
            List<SeguimientoProyectoDto> dtos = seguimientos.stream()
                    .map(SeguimientoProyectoDto::new)
                    .collect(Collectors.toList());
            return new RespuestaGeneral<>(true, "Seguimientos por fecha obtenidos exitosamente", dtos);
        } catch (Exception e) {
            return new RespuestaGeneral<>(false, "Error al buscar seguimientos por fecha: " + e.getMessage());
        }
    }
}
