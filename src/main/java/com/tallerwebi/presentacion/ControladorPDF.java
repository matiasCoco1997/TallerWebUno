package com.tallerwebi.presentacion;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.servicios.ServicioPDF;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.StyleConstants;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

        servicioPDF.generarTituloFecha(document);

        List<String> encabezados=servicioPDF.completarListaDeEncabezados();

        PdfPTable table= new PdfPTable(3);
        servicioPDF.establecerAnchoDeColumnas(table);
        servicioPDF.generarEncabezados(encabezados,table);

        List<String> valores=servicioPDF.completarTablaDeValores();

        servicioPDF.generarTablaDeDatos(valores,table);
        document.add(table);

        document.close();
    }
}
