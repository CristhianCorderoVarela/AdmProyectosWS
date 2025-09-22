package cr.ac.una.admproyectosws.ws;

import cr.ac.una.admproyectosws.dto.RespuestaExcel;
import cr.ac.una.admproyectosws.service.ExcelService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

// Esto expone un servicio SOAP para reportes en Excel
@Stateless
@WebService(name = "ReporteWS", serviceName = "ReporteWSService")
public class ReporteWS {

    @EJB
    private ExcelService excelService;

    // Esto sirve para confirmar que el servicio está activo
    @WebMethod(operationName = "verificarEstadoServicio")
    public RespuestaExcel verificarEstadoServicio() {
        return new RespuestaExcel(true, "ReporteWS en ejecución (verificado OK)");
    }

    // Esto genera el Excel del cronograma para un proyecto
    @WebMethod(operationName = "generarCronogramaProyecto")
    public RespuestaExcel generarCronogramaProyecto(
            @WebParam(name = "proyectoId") Long proyectoId) {

        if (proyectoId == null) {
            return new RespuestaExcel(false, "El ID del proyecto es requerido");
        }

        try {
            
            return excelService.generarCronogramaProyecto(proyectoId);
        } catch (Exception e) {
           
            e.printStackTrace();
            return new RespuestaExcel(false, "Error interno al generar Excel: " + e.getMessage());
        }
    }
}
