package cr.ac.una.admproyectosws.dto;

import java.io.Serializable;

//Esto representa la respuesta al generar un archivo excel

public class RespuestaExcel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Boolean ok;
    private String mensaje;
    private byte[] archivoExcel;
    private String nombreArchivo;
    
    //Este crea uan respuesta vacia
    public RespuestaExcel() {
    }
    
    //Esto crea una respuesta con estado y mensaje
    public RespuestaExcel(Boolean ok, String mensaje) {
        this.ok = ok;
        this.mensaje = mensaje;
    }
    
    //Esto crea una respuesta completa con el archivo en memoria y su nombre
    public RespuestaExcel(Boolean ok, String mensaje, byte[] archivoExcel, String nombreArchivo) {
        this.ok = ok;
        this.mensaje = mensaje;
        this.archivoExcel = archivoExcel;
        this.nombreArchivo = nombreArchivo;
    }
    
    // Getters y Setters
    public Boolean getOk() { return ok; }
    public void setOk(Boolean ok) { this.ok = ok; }
    
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    
    public byte[] getArchivoExcel() { return archivoExcel; }
    public void setArchivoExcel(byte[] archivoExcel) { this.archivoExcel = archivoExcel; }
    
    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }
    public boolean isOk() { return Boolean.TRUE.equals(ok); }
}