package cr.ac.una.admproyectosws.ws;

import cr.ac.una.admproyectosws.dto.RespuestaExcel;
import cr.ac.una.admproyectosws.service.ExcelService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@Stateless
@WebService(name = "ReporteWS", serviceName = "ReporteWSService")
public class ReporteWS {

    @EJB
    private ExcelService excelService;

    @WebMethod(operationName = "verificarEstadoServicio")
    public RespuestaExcel verificarEstadoServicio() {
        return new RespuestaExcel(true, "ReporteWS en ejecución (verificado OK)");
    }

    @WebMethod(operationName = "generarCronogramaProyecto")
    public RespuestaExcel generarCronogramaProyecto(
            @WebParam(name = "proyectoId") Long proyectoId) {

        if (proyectoId == null) {
            return new RespuestaExcel(false, "El ID del proyecto es requerido");
        }

        try {
            // Delegar al servicio real que construye el Excel
            return excelService.generarCronogramaProyecto(proyectoId);
        } catch (Exception e) {
            // Captura cualquier error para no devolver mensaje vacío al cliente
            e.printStackTrace();
            return new RespuestaExcel(false, "Error interno al generar Excel: " + e.getMessage());
        }
    }
}
