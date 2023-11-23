package com.tallerwebi.dominio.servicios;


import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;

import java.io.IOException;
import java.util.List;

public interface ServicioPDF {

    List<Categoria> obtenerCategorias();

    Long obtenerCantidadNoticiasRelacionadas(String categoria);

    void generarEncabezados(List<String> encabezados, PdfPTable table);

    void establecerAnchoDeColumnas(PdfPTable table) throws DocumentException;

    List<String> completarTablaDeValores();

    void generarTablaDeDatos(List<String> valores,PdfPTable table);

    void generarTituloFecha(Document document,String titulo) throws DocumentException;

    List<String> completarListaDeEncabezados();

    List<Usuario> obtenerUsuarios();

    void agregarFila(String valor, PdfPTable table,Boolean borde);
    void cargarDatosTablaUsuarios(PdfPTable table, Usuario usuario) throws BadElementException, IOException;

    void generarEspacioEnBlanco(boolean encabezado,PdfPTable table);

    List<Noticia> obtenerNoticias();

    void cargarDatosATablaNoticias(PdfPTable table, Noticia noticia) throws BadElementException, IOException;

    void generarTablaNoticias(List<Noticia> noticias, Integer contador, Boolean flag, Document document) throws DocumentException, IOException;

    void generarTablaUsuarios(List<Usuario> usuarios, Integer contador, Boolean flag, Document document) throws DocumentException, IOException;
}
