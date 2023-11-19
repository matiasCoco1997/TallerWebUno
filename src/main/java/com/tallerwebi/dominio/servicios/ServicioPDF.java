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

    int obtenerCantidadDeNoticiasDeUnUsuario(Long idUsuario);

    int obtenerCantidadDeLikesDeUnUsuario(Long idUsuario);

    int obtenerCantidadDeNoticiasEnListaDeUnUsuario(Long idUsuario);

    int obtenerCantidadDeSeguidoresDeUnUsuario(Long idUsuario);

    int obtenerCantidadDeSeguidosDeUnUsuario(Long idUsuario);

    void generarTablaDeUsuarios(PdfPTable table,Usuario usuario) throws BadElementException, IOException;

    void generarEspacioEnBlanco(boolean encabezado,PdfPTable table);

    List<Noticia> obtenerNoticias();

    void generarTablaDeNoticias(PdfPTable table, Noticia noticia) throws BadElementException, IOException;
}
