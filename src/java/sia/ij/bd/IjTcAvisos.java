package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import sun.jdbc.rowset.CachedRowSet;

public class IjTcAvisos {
    public IjTcAvisos() {
    }
    


    private String evento;
    private String ctaorigen;
    private String listadestinatario;
    private String copiapara;
    private String copiaoculta;
    private String titulo;
    private String machote;
    private String html;
    private String estatus;
    
    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getEvento() {
        return evento;
    }

    public void setCtaorigen(String ctaorigen) {
        this.ctaorigen = ctaorigen;
    }

    public String getCtaorigen() {
        return ctaorigen;
    }

    public void setListadestinatario(String listadestinatario) {
        this.listadestinatario = listadestinatario;
    }

    public String getListadestinatario() {
        return listadestinatario;
    }

    public void setCopiapara(String copiapara) {
        this.copiapara = copiapara;
    }

    public String getCopiapara() {
        return copiapara;
    }

    public void setCopiaoculta(String copiaoculta) {
        this.copiaoculta = copiaoculta;
    }

    public String getCopiaoculta() {
        return copiaoculta;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setMachote(String machote) {
        this.machote = machote;
    }

    public String getMachote() {
        return machote;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getEstatus() {
        return estatus;
    }    
    
    public void obtenerAviso(Connection conec) throws Exception{
        CachedRowSet cached = null;
        StringBuffer cadenaSql = null;
     try  {
        cadenaSql = new StringBuffer();
        cached = new CachedRowSet();
        cadenaSql.append("select evento, ctaorigen, listadestinatario, copiapara, copiaoculta, titulo, machote, html");
        cadenaSql.append(" from ij_tc_avisos");
        cadenaSql.append(" Where evento = '"+this.evento+"'");
       
        cached.setCommand(cadenaSql.toString());
        cached.execute(conec);
        
         if(cached.next()){
           this.ctaorigen   = cached.getString("ctaorigen");
           this.listadestinatario = cached.getString("listadestinatario");
           this.copiapara = cached.getString("copiapara");
           this.copiaoculta = cached.getString("copiaoculta");
           this.titulo      = cached.getString("titulo");     
           this.machote     = cached.getString("machote");     
           this.html        = cached.getString("html");     
         }
     }
     catch (Exception ex)  {
          System.out.println("Error en IjTcAvisos.obtenerAviso "+ex.getMessage());
          System.out.println("query "+cadenaSql.toString());
        }
    finally  {
         if(cached != null){
             cached.close();
             cached = null;
         }
    }
   }//obtenerAviso
}
