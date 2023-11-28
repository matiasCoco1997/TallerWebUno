package com.tallerwebi.dominio.servicios;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.tallerwebi.dominio.entidades.Categoria;
import com.tallerwebi.dominio.entidades.Noticia;
import com.tallerwebi.dominio.entidades.Usuario;
import com.tallerwebi.infraestructura.RepositorioHome;
import com.tallerwebi.infraestructura.RepositorioNoticia;
import com.tallerwebi.infraestructura.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
            if(categoria!=null || categoria.getVistas()!=0){
                valores.add(String.valueOf(categoria.getVistas()));
            }else{
                valores.add(String.valueOf(0));
            }
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
    public void generarTituloFecha(Document document, String titulo) throws DocumentException {
        Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
        Paragraph paragraph=new Paragraph(titulo,fontTitulo);
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

    @Override
    public List<Usuario> obtenerUsuarios() {
        return repositorioUsuario.obtenerUsuarios();
    }

    @Override
    public void agregarFila(String valor, PdfPTable table,Boolean borde) {
        PdfPCell cell = new PdfPCell(new Paragraph(valor));
        cell.setFixedHeight(25); // Establecer la altura de la celda en puntos
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Centrar verticalmente
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if(borde==true){
            cell.setBorder(PdfPCell.NO_BORDER);
        }
        table.addCell(cell);
    }


    public int obtenerCantidadDeNoticiasDeUnUsuario(Long idUsuario) {
        return repositorioUsuario.obtenerMisNoticias(idUsuario).size();
    }


    public int obtenerCantidadDeLikesDeUnUsuario(Long idUsuario) {
        return repositorioUsuario.obtenerNoticiasLikeadasPorElUsuario(idUsuario).size();
    }


    public int obtenerCantidadDeNoticiasEnListaDeUnUsuario(Long idUsuario) {
        return repositorioUsuario.obtenerMiListaDeReproduccion(idUsuario).size();
    }


    public int obtenerCantidadDeSeguidoresDeUnUsuario(Long idUsuario) {
        return repositorioUsuario.obtenerListaDeSeguidores(idUsuario).size();
    }


    public int obtenerCantidadDeSeguidosDeUnUsuario(Long idUsuario) {
        return repositorioUsuario.obtenerListaDeSeguidos(idUsuario).size();
    }

    @Override
    public void cargarDatosTablaUsuarios(PdfPTable table, Usuario usuario) throws BadElementException, IOException {
        Image imagen=Image.getInstance("src/main/webapp/resources/core/"+usuario.getFotoPerfil());
        imagen.scaleToFit(70,70);
        PdfPCell imagenCell = new PdfPCell(imagen);
        imagenCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        imagenCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        imagenCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(imagenCell);

        PdfPCell textoCell = new PdfPCell(Phrase.getInstance(usuario.getNombre() + " "+ usuario.getApellido()));
        textoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        textoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        textoCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(textoCell);
        agregarFila("",table,true);
        agregarFila("",table,true);
        agregarFila("Email: "+usuario.getEmail(),table,true);
        agregarFila("Ciudad y país: "+usuario.getCiudad()+", "+usuario.getPais(),table,true);
        agregarFila("Fecha de nacimiento: "+ usuario.getFechaDeNacimiento(),table,true);
        agregarFila("Cantidad de noticias: "+obtenerCantidadDeNoticiasDeUnUsuario(usuario.getIdUsuario()),table,true);
        agregarFila("Cantidad de likes: "+obtenerCantidadDeLikesDeUnUsuario(usuario.getIdUsuario()),table,true);
        agregarFila("Cantidad de noticias en Lista: "+obtenerCantidadDeNoticiasEnListaDeUnUsuario(usuario.getIdUsuario()),table,true);
        agregarFila("Seguidores: "+obtenerCantidadDeSeguidoresDeUnUsuario(usuario.getIdUsuario()),table,true);
        agregarFila("Seguidos: "+obtenerCantidadDeSeguidosDeUnUsuario(usuario.getIdUsuario()),table,true);

    }

    @Override
    public void generarEspacioEnBlanco(boolean encabezado,PdfPTable table) {
        if(encabezado==true){
            table.addCell("");
            table.addCell("");
            agregarFila("",table,true);
            agregarFila("",table,true);
        }else{
            agregarFila("",table,true);
            agregarFila("",table,true);
            table.addCell("");
            table.addCell("");
        }
    }

    @Override
    public List<Noticia> obtenerNoticias() {
        return repositorioHome.listarNoticias();
    }

    @Override
    public void cargarDatosATablaNoticias(PdfPTable table, Noticia noticia) throws BadElementException, IOException {
        Image imagen=Image.getInstance("src/main/webapp/resources/core/"+noticia.getRutaDeimagen());
        imagen.scaleToFit(70,70);
        PdfPCell imagenCell = new PdfPCell(imagen);
        imagenCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        imagenCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        imagenCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(imagenCell);

        PdfPCell textoCell = new PdfPCell(Phrase.getInstance(noticia.getTitulo()));
        textoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        textoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        textoCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(textoCell);
        agregarFila("",table,true);
        agregarFila("",table,true);
        agregarFila("Autor: "+noticia.getUsuario().getNombre(),table,true);
        agregarFila("Categoria: "+noticia.getCategoria(),table,true);
        agregarFila("Fecha de publicación: "+noticia.getFechaDePublicacion(),table,true);
        agregarFila("Likes: "+noticia.getLikes(),table,true);
        agregarFila("Comentarios: "+obtenerCantidadDeComentariosDeUnaNoticia(noticia.getIdNoticia()),table,true);
        agregarFila("Listas de reproducción: "+obtenerCantidadDeListasDeUnaNoticia(noticia.getIdNoticia()),table,true);

    }



    @Override
    public void generarTablaNoticias(List<Noticia> noticias, Integer contador, Boolean flag, Document document) throws DocumentException, IOException {
        for (Noticia noticia: noticias) {
            contador++;
            PdfPTable table=new PdfPTable(2);
            generarEspacioEnBlanco(true,table);
            cargarDatosATablaNoticias(table,noticia);
            generarEspacioEnBlanco(false,table);

            document.add(table);
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));

            if(flag==false && contador%2==0){
                flag=true;
                contador=0;
                document.newPage();
            }else if(contador%3==0){
                document.newPage();
            }
        }
    }

    @Override
    public void generarTablaUsuarios(List<Usuario> usuarios, Integer contador, Boolean flag, Document document) throws DocumentException, IOException {
        for (Usuario usuario: usuarios) {
            contador++;
            PdfPTable table=new PdfPTable(2);
            generarEspacioEnBlanco(true,table);
            cargarDatosTablaUsuarios(table,usuario);
            generarEspacioEnBlanco(false,table);

            document.add(table);
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));

            if(flag==false && contador%2==0){
                flag=true;
                contador=0;
                document.newPage();
            }else if(contador%3==0){
                document.newPage();
            }
        }
    }

    private Long obtenerCantidadDeListasDeUnaNoticia(Long idNoticia) {
        return repositorioNoticia.obtenerCantidadDeListasDeUnaNoticia(idNoticia);
    }

    private int obtenerCantidadDeComentariosDeUnaNoticia(Long idNoticia) {
        return repositorioNoticia.obtenerComentarios(idNoticia).size();
    }
}
