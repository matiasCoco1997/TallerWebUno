package com.tallerwebi.presentacion;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.dominio.servicios.ServicioPDF;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class ControladorPDF {

    private ServicioPDF servicioPDF;

    @Autowired
    public ControladorPDF(ServicioPDF servicioPDF){
        this.servicioPDF=servicioPDF;
    }

    @RequestMapping("/categorias/pdf")
    public void generarPDFCategorias(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");

        response.setHeader("Content-Disposition", "inline; filename=ejemplo.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        servicioPDF.generarTituloFecha(document,"¡Informe de las Categorías de Sunn!");

        List<String> encabezados=servicioPDF.completarListaDeEncabezados();

        PdfPTable table= new PdfPTable(3);
        servicioPDF.establecerAnchoDeColumnas(table);
        servicioPDF.generarEncabezados(encabezados,table);

        List<String> valores=servicioPDF.completarTablaDeValores();

        servicioPDF.generarTablaDeDatos(valores,table);
        document.add(table);

        document.close();
    }

    @RequestMapping("/usuarios/pdf")
    public void generarPDFUsuarios(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");

        response.setHeader("Content-Disposition", "inline; filename=ejemplo.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        servicioPDF.generarTituloFecha(document,"¡Informe de los usuarios de Sunn!");
        document.add(new Paragraph("\n"));

        List<Usuario> usuarios=servicioPDF.obtenerUsuarios();
        Integer contador=0;
        Boolean flag=false;
        servicioPDF.generarTablaUsuarios(usuarios,contador,flag,document);
        document.close();
    }

    @RequestMapping("/noticias/pdf")
    public void generarPDFNoticias(HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");

        response.setHeader("Content-Disposition", "inline; filename=ejemplo.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        servicioPDF.generarTituloFecha(document,"¡Informe de las noticias de Sunn!");
        document.add(new Paragraph("\n"));

        List<Noticia> noticias=servicioPDF.obtenerNoticias();
        Integer contador=0;
        Boolean flag = false;

        servicioPDF.generarTablaNoticias(noticias,contador,flag,document);

        document.close();
    }
}
