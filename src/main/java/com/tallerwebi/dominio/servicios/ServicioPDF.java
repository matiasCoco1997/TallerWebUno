package com.tallerwebi.dominio.servicios;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.tallerwebi.dominio.entidades.Categoria;

import java.util.List;

public interface ServicioPDF {

    List<Categoria> obtenerCategorias();

    Long obtenerCantidadNoticiasRelacionadas(String categoria);

    void generarEncabezados(List<String> encabezados, PdfPTable table);

    void establecerAnchoDeColumnas(PdfPTable table) throws DocumentException;

    List<String> completarTablaDeValores();

    void generarTablaDeDatos(List<String> valores,PdfPTable table);

    void generarTituloFecha(Document document) throws DocumentException;

    List<String> completarListaDeEncabezados();
}
