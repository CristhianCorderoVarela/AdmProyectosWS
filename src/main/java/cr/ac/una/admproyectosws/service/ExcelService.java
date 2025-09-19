package cr.ac.una.admproyectosws.service;

import cr.ac.una.admproyectosws.dao.ProyectoDao;
import cr.ac.una.admproyectosws.dto.RespuestaExcel;
import cr.ac.una.admproyectosws.model.Actividad;
import cr.ac.una.admproyectosws.model.Proyecto;
import cr.ac.una.admproyectosws.utils.Constants;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Stateless
public class ExcelService {
    
    @EJB
    private ProyectoDao proyectoDao;
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    
    public RespuestaExcel generarCronogramaProyecto(Long proyectoId) {
        try {
            // Buscar el proyecto
            Optional<Proyecto> proyectoOpt = proyectoDao.buscarPorId(proyectoId);
            if (!proyectoOpt.isPresent()) {
                return new RespuestaExcel(false, "Proyecto no encontrado");
            }
            
            Proyecto proyecto = proyectoOpt.get();
            
            // Crear el archivo Excel
            try (Workbook workbook = new XSSFWorkbook();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                
                Sheet sheet = workbook.createSheet("Cronograma - " + proyecto.getNombre());
                
                // Crear estilos
                CellStyle headerStyle = crearEstiloEncabezado(workbook);
                CellStyle projectStyle = crearEstiloProyecto(workbook);
                CellStyle dataStyle = crearEstiloDatos(workbook);
                CellStyle dateStyle = crearEstiloFecha(workbook);
                
                int rowNum = 0;
                
                // Título del proyecto
                Row titleRow = sheet.createRow(rowNum++);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellValue("CRONOGRAMA DEL PROYECTO");
                titleCell.setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
                
                // Espacio
                rowNum++;
                
                // Información del proyecto
                rowNum = crearInfoProyecto(sheet, proyecto, projectStyle, dataStyle, rowNum);
                
                // Espacio
                rowNum++;
                
                // Encabezados de actividades
                Row headerRow = sheet.createRow(rowNum++);
                String[] headers = {
                    "Orden", "Descripción", "Encargado", "Estado", 
                    "Fecha Inicio Plan.", "Fecha Final Plan.", "Fecha Inicio Real", "Fecha Final Real"
                };
                
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                }
                
                // Datos de actividades
                List<Actividad> actividades = proyecto.getActividades();
                if (actividades != null && !actividades.isEmpty()) {
                    for (Actividad actividad : actividades) {
                        Row row = sheet.createRow(rowNum++);
                        
                        // Orden
                        Cell orderCell = row.createCell(0);
                        orderCell.setCellValue(actividad.getOrdenEjecucion() != null ? 
                            actividad.getOrdenEjecucion() : 0);
                        orderCell.setCellStyle(dataStyle);
                        
                        // Descripción
                        Cell descCell = row.createCell(1);
                        descCell.setCellValue(actividad.getDescripcion() != null ? 
                            actividad.getDescripcion() : "");
                        descCell.setCellStyle(dataStyle);
                        
                        // Encargado
                        Cell encargadoCell = row.createCell(2);
                        encargadoCell.setCellValue(actividad.getEncargadoNombre() != null ? 
                            actividad.getEncargadoNombre() : "");
                        encargadoCell.setCellStyle(dataStyle);
                        
                        // Estado
                        Cell estadoCell = row.createCell(3);
                        estadoCell.setCellValue(actividad.getEstado() != null ? 
                            actividad.getEstado() : "");
                        estadoCell.setCellStyle(dataStyle);
                        
                        // Fechas
                        crearCeldaFecha(row, 4, actividad.getFechaInicioPlanificada(), dateStyle);
                        crearCeldaFecha(row, 5, actividad.getFechaFinalPlanificada(), dateStyle);
                        crearCeldaFecha(row, 6, actividad.getFechaInicioReal(), dateStyle);
                        crearCeldaFecha(row, 7, actividad.getFechaFinalReal(), dateStyle);
                    }
                } else {
                    // Si no hay actividades, mostrar mensaje
                    Row row = sheet.createRow(rowNum++);
                    Cell cell = row.createCell(1);
                    cell.setCellValue("No hay actividades registradas para este proyecto");
                    cell.setCellStyle(dataStyle);
                    sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 1, 7));
                }
                
                // Ajustar ancho de columnas
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                    // Limitar el ancho máximo
                    if (sheet.getColumnWidth(i) > 15000) {
                        sheet.setColumnWidth(i, 15000);
                    }
                }
                
                // Escribir a byte array
                workbook.write(outputStream);
                
                String nombreArchivo = "Cronograma_" + proyecto.getNombre().replaceAll("[^a-zA-Z0-9]", "_") + 
                    "_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + 
                    Constants.EXCEL_FILE_EXTENSION;
                
                return new RespuestaExcel(true, "Excel generado exitosamente", 
                    outputStream.toByteArray(), nombreArchivo);
                
            }
            
        } catch (Exception e) {
            return new RespuestaExcel(false, "Error al generar el Excel: " + e.getMessage());
        }
    }
    
    private int crearInfoProyecto(Sheet sheet, Proyecto proyecto, CellStyle projectStyle, 
                                 CellStyle dataStyle, int startRow) {
        int rowNum = startRow;
        
        // Nombre del proyecto
        Row proyectoRow = sheet.createRow(rowNum++);
        Cell proyectoLabelCell = proyectoRow.createCell(0);
        proyectoLabelCell.setCellValue("PROYECTO:");
        proyectoLabelCell.setCellStyle(projectStyle);
        Cell proyectoValueCell = proyectoRow.createCell(1);
        proyectoValueCell.setCellValue(proyecto.getNombre());
        proyectoValueCell.setCellStyle(dataStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 1, 4));
        
        // Patrocinador
        Row patrocinadoRow = sheet.createRow(rowNum++);
        Cell patrocinadoLabelCell = patrocinadoRow.createCell(0);
        patrocinadoLabelCell.setCellValue("PATROCINADOR:");
        patrocinadoLabelCell.setCellStyle(projectStyle);
        Cell patrocinadoValueCell = patrocinadoRow.createCell(1);
        patrocinadoValueCell.setCellValue(proyecto.getPatrocinadorNombre());
        patrocinadoValueCell.setCellStyle(dataStyle);
        
        // Estado
        Row estadoRow = sheet.createRow(rowNum++);
        Cell estadoLabelCell = estadoRow.createCell(0);
        estadoLabelCell.setCellValue("ESTADO:");
        estadoLabelCell.setCellStyle(projectStyle);
        Cell estadoValueCell = estadoRow.createCell(1);
        estadoValueCell.setCellValue(proyecto.getEstado());
        estadoValueCell.setCellStyle(dataStyle);
        
        // Porcentaje de avance
        Cell avanceLabelCell = estadoRow.createCell(3);
        avanceLabelCell.setCellValue("% AVANCE:");
        avanceLabelCell.setCellStyle(projectStyle);
        Cell avanceValueCell = estadoRow.createCell(4);
        avanceValueCell.setCellValue(proyecto.getPorcentajeAvance() + "%");
        avanceValueCell.setCellStyle(dataStyle);
        
        return rowNum;
    }
    
    private void crearCeldaFecha(Row row, int columnIndex, Date fecha, CellStyle dateStyle) {
        Cell cell = row.createCell(columnIndex);
        if (fecha != null) {
            cell.setCellValue(DATE_FORMAT.format(fecha));
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(dateStyle);
    }
    
    private CellStyle crearEstiloEncabezado(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
    
    private CellStyle crearEstiloProyecto(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    private CellStyle crearEstiloDatos(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return style;
    }
    
    private CellStyle crearEstiloFecha(Workbook workbook) {
        CellStyle style = crearEstiloDatos(workbook);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}