package cr.ac.una.admproyectosws.service;

import cr.ac.una.admproyectosws.model.Actividad;
import cr.ac.una.admproyectosws.model.Proyecto;
import cr.ac.una.admproyectosws.model.SeguimientoProyecto;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.Stateless;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Stateless
public class EmailService {
    
    @Asynchronous
    public void notificarCreacionProyecto(Proyecto proyecto) {
        try {
            String subject = "Nuevo Proyecto Creado: " + proyecto.getNombre();
            String htmlContent = construirHtmlProyecto(proyecto, "creado");
            
            List<String> destinatarios = Arrays.asList(
                proyecto.getPatrocinadorCorreo(),
                proyecto.getLiderUsuarioCorreo(),
                proyecto.getLiderTecnicoCorreo()
            );
            
            enviarCorreoHtml(destinatarios, subject, htmlContent);
            
        } catch (Exception e) {
            System.err.println("Error enviando notificación de creación de proyecto: " + e.getMessage());
        }
    }
    
    @Asynchronous
    public void notificarCambioEstadoProyecto(Proyecto proyecto, String estadoAnterior) {
        try {
            String subject = String.format("Cambio de Estado - Proyecto: %s [%s → %s]", 
                    proyecto.getNombre(), estadoAnterior, proyecto.getEstado());
            String htmlContent = construirHtmlCambioEstado(proyecto, estadoAnterior);
            
            List<String> destinatarios = Arrays.asList(
                proyecto.getPatrocinadorCorreo(),
                proyecto.getLiderUsuarioCorreo(),
                proyecto.getLiderTecnicoCorreo()
            );
            
            enviarCorreoHtml(destinatarios, subject, htmlContent);
            
        } catch (Exception e) {
            System.err.println("Error enviando notificación de cambio de estado: " + e.getMessage());
        }
    }
    
    @Asynchronous
    public void notificarCreacionActividad(Actividad actividad) {
        try {
            String subject = "Nueva Actividad Asignada: " + actividad.getDescripcion();
            String htmlContent = construirHtmlActividad(actividad, "asignada");
            
            enviarCorreoHtml(Arrays.asList(actividad.getEncargadoCorreo()), subject, htmlContent);
            
        } catch (Exception e) {
            System.err.println("Error enviando notificación de actividad: " + e.getMessage());
        }
    }
    
    @Asynchronous
    public void notificarCambioEstadoActividad(Actividad actividad, String estadoAnterior) {
        try {
            String subject = String.format("Cambio de Estado - Actividad: %s [%s → %s]", 
                    actividad.getDescripcion(), estadoAnterior, actividad.getEstado());
            String htmlContent = construirHtmlCambioEstadoActividad(actividad, estadoAnterior);
            
            enviarCorreoHtml(Arrays.asList(actividad.getEncargadoCorreo()), subject, htmlContent);
            
        } catch (Exception e) {
            System.err.println("Error enviando notificación de cambio de estado de actividad: " + e.getMessage());
        }
    }
    
    @Asynchronous
    public void notificarSeguimientoProyecto(SeguimientoProyecto seguimiento) {
        try {
            Proyecto proyecto = seguimiento.getProyecto();
            String subject = "Nuevo Seguimiento - Proyecto: " + proyecto.getNombre();
            String htmlContent = construirHtmlSeguimiento(seguimiento);
            
            List<String> destinatarios = Arrays.asList(
                proyecto.getPatrocinadorCorreo(),
                proyecto.getLiderUsuarioCorreo(),
                proyecto.getLiderTecnicoCorreo()
            );
            
            enviarCorreoHtml(destinatarios, subject, htmlContent);
            
        } catch (Exception e) {
            System.err.println("Error enviando notificación de seguimiento: " + e.getMessage());
        }
    }
    
    private void enviarCorreoHtml(List<String> destinatarios, String subject, String htmlContent) {
        // TODO: Implementar envío real de correo usando JavaMail
        // Por ahora solo log para desarrollo
        System.out.println("=== CORREO ELECTRÓNICO ===");
        System.out.println("Para: " + String.join(", ", destinatarios));
        System.out.println("Asunto: " + subject);
        System.out.println("Contenido HTML:");
        System.out.println(htmlContent);
        System.out.println("========================");
    }
    
    private String construirHtmlProyecto(Proyecto proyecto, String accion) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }");
        html.append(".container { background-color: white; max-width: 600px; margin: 0 auto; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        html.append(".header { background-color: #2c3e50; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; margin: -30px -30px 30px -30px; }");
        html.append(".info-table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        html.append(".info-table th, .info-table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
        html.append(".info-table th { background-color: #ecf0f1; font-weight: bold; }");
        html.append(".status { padding: 5px 10px; border-radius: 15px; color: white; font-weight: bold; }");
        html.append(".status.planificado { background-color: #3498db; }");
        html.append(".status.en_curso { background-color: #f39c12; }");
        html.append(".status.suspendido { background-color: #e74c3c; }");
        html.append(".status.finalizado { background-color: #27ae60; }");
        html.append("</style></head><body>");
        
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h1>Proyecto ").append(accion.toUpperCase()).append("</h1>");
        html.append("</div>");
        
        html.append("<h2>").append(proyecto.getNombre()).append("</h2>");
        
        html.append("<table class='info-table'>");
        html.append("<tr><th>Patrocinador</th><td>").append(proyecto.getPatrocinadorNombre()).append("</td></tr>");
        html.append("<tr><th>Líder Usuario</th><td>").append(proyecto.getLiderUsuarioNombre()).append("</td></tr>");
        html.append("<tr><th>Líder Técnico</th><td>").append(proyecto.getLiderTecnicoNombre()).append("</td></tr>");
        html.append("<tr><th>Estado</th><td><span class='status ").append(proyecto.getEstado().toLowerCase().replace("_", "")).append("'>")
                   .append(proyecto.getEstado()).append("</span></td></tr>");
        html.append("<tr><th>Avance</th><td>").append(proyecto.getPorcentajeAvance()).append("%</td></tr>");
        html.append("<tr><th>Fecha Inicio Planificada</th><td>").append(formatearFecha(proyecto.getFechaInicioPlanificada())).append("</td></tr>");
        html.append("<tr><th>Fecha Final Planificada</th><td>").append(formatearFecha(proyecto.getFechaFinalPlanificada())).append("</td></tr>");
        
        if (proyecto.getDescripcion() != null && !proyecto.getDescripcion().trim().isEmpty()) {
            html.append("<tr><th>Descripción</th><td>").append(proyecto.getDescripcion()).append("</td></tr>");
        }
        html.append("</table>");
        
        html.append("<p><em>Notificación automática del Sistema de Gestión de Proyectos - UNA</em></p>");
        html.append("</div></body></html>");
        
        return html.toString();
    }
    
    private String construirHtmlCambioEstado(Proyecto proyecto, String estadoAnterior) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }");
        html.append(".container { background-color: white; max-width: 600px; margin: 0 auto; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        html.append(".header { background-color: #e67e22; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; margin: -30px -30px 30px -30px; }");
        html.append(".cambio { background-color: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; border-radius: 5px; margin: 20px 0; }");
        html.append(".estado { padding: 5px 10px; border-radius: 15px; color: white; font-weight: bold; margin: 0 5px; }");
        html.append("</style></head><body>");
        
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h1>CAMBIO DE ESTADO</h1>");
        html.append("</div>");
        
        html.append("<h2>").append(proyecto.getNombre()).append("</h2>");
        
        html.append("<div class='cambio'>");
        html.append("<h3>Estado Actualizado:</h3>");
        html.append("<p><span class='estado'>").append(estadoAnterior).append("</span> → <span class='estado'>")
                   .append(proyecto.getEstado()).append("</span></p>");
        html.append("<p><strong>Fecha del cambio:</strong> ").append(formatearFecha(new Date())).append("</p>");
        html.append("</div>");
        
        html.append("<p><em>Notificación automática del Sistema de Gestión de Proyectos - UNA</em></p>");
        html.append("</div></body></html>");
        
        return html.toString();
    }
    
    private String construirHtmlActividad(Actividad actividad, String accion) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }");
        html.append(".container { background-color: white; max-width: 600px; margin: 0 auto; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        html.append(".header { background-color: #8e44ad; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; margin: -30px -30px 30px -30px; }");
        html.append(".info-table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        html.append(".info-table th, .info-table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
        html.append(".info-table th { background-color: #ecf0f1; font-weight: bold; }");
        html.append("</style></head><body>");
        
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h1>ACTIVIDAD ").append(accion.toUpperCase()).append("</h1>");
        html.append("</div>");
        
        html.append("<h2>").append(actividad.getDescripcion()).append("</h2>");
        
        html.append("<table class='info-table'>");
        html.append("<tr><th>Proyecto</th><td>").append(actividad.getProyecto().getNombre()).append("</td></tr>");
        html.append("<tr><th>Encargado</th><td>").append(actividad.getEncargadoNombre()).append("</td></tr>");
        html.append("<tr><th>Estado</th><td>").append(actividad.getEstado()).append("</td></tr>");
        html.append("<tr><th>Orden de Ejecución</th><td>").append(actividad.getOrdenEjecucion()).append("</td></tr>");
        html.append("<tr><th>Fecha Inicio Planificada</th><td>").append(formatearFecha(actividad.getFechaInicioPlanificada())).append("</td></tr>");
        html.append("<tr><th>Fecha Final Planificada</th><td>").append(formatearFecha(actividad.getFechaFinalPlanificada())).append("</td></tr>");
        html.append("</table>");
        
        html.append("<p><em>Notificación automática del Sistema de Gestión de Proyectos - UNA</em></p>");
        html.append("</div></body></html>");
        
        return html.toString();
    }
    
    private String construirHtmlCambioEstadoActividad(Actividad actividad, String estadoAnterior) {
        return construirHtmlCambioEstado(actividad.getProyecto(), estadoAnterior)
                .replace("CAMBIO DE ESTADO", "CAMBIO DE ESTADO - ACTIVIDAD")
                .replace(actividad.getProyecto().getNombre(), actividad.getDescripcion());
    }
    
    private String construirHtmlSeguimiento(SeguimientoProyecto seguimiento) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html><head><meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }");
        html.append(".container { background-color: white; max-width: 600px; margin: 0 auto; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        html.append(".header { background-color: #16a085; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; margin: -30px -30px 30px -30px; }");
        html.append(".observaciones { background-color: #f8f9fa; padding: 20px; border-left: 4px solid #16a085; margin: 20px 0; }");
        html.append("</style></head><body>");
        
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h1>NUEVO SEGUIMIENTO</h1>");
        html.append("</div>");
        
        html.append("<h2>").append(seguimiento.getProyecto().getNombre()).append("</h2>");
        
        html.append("<p><strong>Fecha del Seguimiento:</strong> ").append(formatearFecha(seguimiento.getFechaSeguimiento())).append("</p>");
        html.append("<p><strong>Porcentaje de Avance:</strong> ").append(seguimiento.getPorcentajeAvance()).append("%</p>");
        html.append("<p><strong>Creado por:</strong> ").append(seguimiento.getCreadoPor().getNombre())
                   .append(" ").append(seguimiento.getCreadoPor().getApellidos()).append("</p>");
        
        html.append("<div class='observaciones'>");
        html.append("<h3>Observaciones:</h3>");
        html.append("<p>").append(seguimiento.getObservaciones()).append("</p>");
        html.append("</div>");
        
        html.append("<p><em>Notificación automática del Sistema de Gestión de Proyectos - UNA</em></p>");
        html.append("</div></body></html>");
        
        return html.toString();
    }
    
    private String formatearFecha(Date fecha) {
        if (fecha == null) return "No definida";
        return java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(fecha);
    }
}