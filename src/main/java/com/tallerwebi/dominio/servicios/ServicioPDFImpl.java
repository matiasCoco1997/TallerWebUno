package com.tallerwebi.dominio.servicios;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.infraestructura.RepositorioHome;
import com.tallerwebi.infraestructura.RepositorioNoticia;
import com.tallerwebi.infraestructura.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service("servicioPDF")
@Transactional
public class ServicioPDFImpl implements ServicioPDF{

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioNoticia repositorioNoticia;
    private final RepositorioHome repositorioHome;

    @Autowired
    public ServicioPDFImpl(RepositorioHome repositorioHome,RepositorioNoticia repositorioNoticia, RepositorioUsuario repositorioUsuario){
        this.repositorioHome=repositorioHome;
        this.repositorioNoticia=repositorioNoticia;
        this.repositorioUsuario=repositorioUsuario;
    }

    @Override
    public List<Categoria> obtenerCategorias() {
        return repositorioHome.listarCategorias();
    }

    @Override
    public Long obtenerCantidadNoticiasRelacionadas(String categoria) {
        return repositorioNoticia.obtenerCantidadNoticiasRelacionadas(categoria);
    }

    @Override
    public void generarEncabezados(List<String> encabezados, PdfPTable table) {
        for (String encabezado: encabezados) {
            PdfPCell cell = new PdfPCell(new Paragraph(encabezado));
            cell.setBackgroundColor(new BaseColor(135, 206, 250, 255));
            cell.setFixedHeight(25); // Establecer la altura de la celda en puntos
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Centrar verticalmente
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    @Override
    public void establecerAnchoDeColumnas(PdfPTable table) throws DocumentException {
        float[] columnWidths = {70f, 60f,90f};
        table.setWidths(columnWidths);
    }

    @Override
    public List<String> completarTablaDeValores() {
        List<Categoria> categorias=repositorioHome.listarCategorias();
        List<String> valores=new ArrayList<>();
        for (Categoria categoria:categorias) {
            valores.add(categoria.getDescripcion());
            valores.add(String.valueOf(categoria.getVistas()));
            valores.add(String.valueOf(obtenerCantidadNoticiasRelacionadas(categoria.getDescripcion())));
        }
        return valores;
    }

    @Override
    public void generarTablaDeDatos(List<String> valores,PdfPTable table) {
        for (String valor: valores) {
            PdfPCell cell = new PdfPCell(new Paragraph(valor));
            cell.setFixedHeight(25); // Establecer la altura de la celda en puntos
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Centrar verticalmente
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    @Override
    public void generarTituloFecha(Document document) throws DocumentException {
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
        Paragraph paragraph=new Paragraph("¡Informe de las Categorías de Sunn!",fontTitulo);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        Paragraph fecha=new Paragraph(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        fecha.setAlignment(Element.ALIGN_LEFT);
        document.add(fecha);
        document.add(paragraph);
        document.add(new Paragraph("\n"));
    }

    @Override
    public List<String> completarListaDeEncabezados() {
        List<String> encabezados=new ArrayList<>();
        encabezados.add("Categoría");
        encabezados.add("N° de visitas");
        encabezados.add("N° de Noticias relacionadas");
        return encabezados;
    }
}
