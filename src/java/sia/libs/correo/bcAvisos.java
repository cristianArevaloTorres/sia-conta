package sia.libs.correo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sun.jdbc.rowset.CachedRowSet;

public class bcAvisos {
 private String modulo;
 private String evento;
 private String ctaorigen;
 private String listadestinatarios;
 private String machote;
 private String titulo;
 private String estatus;
 private String copiapara;
 private String html;
 private String copiaOculta;
  
 public bcAvisos () { inicializa(); }

 public String getCtaorigen() {return this.ctaorigen;}
 public String getEstatus() {return this.estatus;}
 public String getEvento() {return this.evento;}
 public String getListadestinatarios() {return this.listadestinatarios;}
 public String getMachote() {return this.machote;}
 public String getModulo() {return this.modulo;}
 public String getTitulo() {return this.titulo;}
 public String getCopiapara() {return copiapara;}
 public String getHtml() {return html;}
 public String getCopiaOculta() { return copiaOculta; }

 public void setCtaorigen(String ctaorigen) {this.ctaorigen = ctaorigen;}
 public void setEstatus(String estatus) {this.estatus = estatus;}
 public void setEvento(String evento) {this.evento = evento;}
 public void setListadestinatarios(String listadestinatarios) {this.listadestinatarios = listadestinatarios;}
 public void setMachote(String machote) {this.machote = machote;}
 public void setModulo(String modulo) {this.modulo = modulo;}
 public void setTitulo(String titulo) {this.titulo = titulo;}
 public void setCopiapara(String copiapara) {this.copiapara = copiapara;}
 public void setHtml(String html) {this.html = html;}
 public void setCopiaOculta(String copiaOculta) { this.copiaOculta = copiaOculta; }

private void inicializa() {
 modulo = "";
 evento = "";
 ctaorigen = "";
 listadestinatarios = "";
 machote = "";
 titulo = "" ;
 estatus = "";
 copiapara = "";
 html = "";
 copiaOculta = "";
}
  
private void adecuaCampos() { 
 modulo = ((modulo==null)||(modulo.equals("")))?null                                     :"'"+modulo+"'";
 evento =((evento==null)||(evento.equals("")))?null                                      :evento;
 ctaorigen =((ctaorigen==null)||(ctaorigen.equals("")))?null                             :"'"+ctaorigen+"'";
 listadestinatarios =((listadestinatarios==null)||(listadestinatarios.equals("")))?null  :"'"+listadestinatarios+"'";
 machote =((machote==null)||(machote.equals("")))?null                                   :"'"+machote+"'";
 titulo =((titulo==null)||(titulo.equals("")))?null                                      :"'"+titulo+"'";
 estatus =((estatus==null)||(estatus.equals("")))?null                                   :"'"+estatus+"'";
 copiapara =((copiapara==null)||(copiapara.equals("")))?null                             :"'"+copiapara+"'";
 html =((html==null)||(html.equals("")))?null                                            :"'"+html+"'";
 copiaOculta =((copiaOculta==null)||(copiaOculta.equals("")))?null                       :"'"+html+"'";
}
  
public String insert(Connection con) throws SQLException, Exception {
 adecuaCampos();
 StringBuffer SQL = new StringBuffer("");
 SQL.append("select max(evento)+1 evento from gtcavisos");
 CachedRowSet crs= null;
 Statement stQuery=null;
 try{
  stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
  crs = new CachedRowSet();
  crs.setCommand(SQL.toString());
  crs.execute(con);
  if (crs.next()) evento = (crs.getString("evento")== null)? "":crs.getString("evento");
  SQL.setLength(0);
  SQL.append("Insert into gtcavisos (modulo,evento,ctaorigen,listadestinatarios,machote,titulo,estatus,copiapara,html,copiaOculta) values (");
  SQL.append(modulo);
  SQL.append(",");
  SQL.append(evento);
  SQL.append(",");
  SQL.append(ctaorigen);
  SQL.append(",");
  SQL.append(listadestinatarios);
  SQL.append(",");
  SQL.append(machote);
  SQL.append(",");
  SQL.append(titulo);
  SQL.append(",");
  SQL.append(estatus);
  SQL.append(",");
  SQL.append(copiapara);
  SQL.append(",");
  SQL.append(html);
  SQL.append(",");
  SQL.append(copiaOculta);
  SQL.append(")");
  //int regsAfectados = -1;
  //regsAfectados = sbAutentifica.executeUpdate("gtcavisos",SQL.toString(),stQuery);
  stQuery.executeUpdate(SQL.toString());
 }
 catch (Exception e){
  System.out.println("Ha ocurrido un error en el metodo bcAvisos.insert");
  System.out.println("bcAvisos.insert:"+SQL.toString());     
  throw e;         }
 finally {
  if (crs != null){
   crs.close();
   crs=null;   }
  if (stQuery != null){
   stQuery.close();
   stQuery=null;   }           
  SQL.setLength(0);
  SQL=null;
 }
 return evento;
}
  
public void select(Connection con) throws SQLException, Exception {
 StringBuffer SQL = new StringBuffer("select evento, ctaorigen, listadestinatario, copiapara, copiaoculta, titulo, machote, html, estatus");
 SQL.append(" from ij_tc_avisos Where evento = "+evento);
 CachedRowSet crs = null;             
 try{
  crs = new CachedRowSet();
  crs.setCommand(SQL.toString());
  crs.execute(con);
  if (crs.next())  {
   //modulo             =(crs.getString("modulo")            == null)?"":crs.getString("modulo");
   evento             =(crs.getString("evento")            == null)?"":crs.getString("evento");
   ctaorigen          =(crs.getString("ctaorigen")         == null)?"":crs.getString("ctaorigen");
   listadestinatarios =(crs.getString("listadestinatario")== null)?"":crs.getString("listadestinatario");
   machote            =(crs.getString("machote")           == null)?"":crs.getString("machote");
   titulo             =(crs.getString("titulo")            == null)?"":crs.getString("titulo");
   estatus            =(crs.getString("estatus")           == null)?"":crs.getString("estatus");
   copiapara          =(crs.getString("copiapara")         == null)?"":crs.getString("copiapara");
   html               =(crs.getString("html")              == null)?"":crs.getString("html");
   copiaOculta        =(crs.getString("copiaOculta")       == null)?"":crs.getString("copiaOculta");
  }else inicializa();
 }
 catch (Exception e){
  System.out.println("Ha ocurrido un error en el metodo bcAvisos.selectAvisos");
  System.out.println("bcAvisos.select:"+SQL.toString());
  throw e;
 }
 finally {
  if (crs != null){
   crs.close();
   crs=null;      }
  SQL.setLength(0);   
  SQL=null;
 }
}

public void delete(Connection con) throws SQLException, Exception {
 //int rs=-1;
 StringBuffer SQL = new StringBuffer("");
 Statement stQuery=null;
 try{
  stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
  SQL.append("delete from gtcavisos where evento =");
  SQL.append(evento);
  stQuery.executeUpdate(SQL.toString());
  //rs=sbAutentifica.executeUpdate("gtcavisos",SQL.toString(), stQuery);
 }
 catch (Exception e){
  System.out.println("Ha occurrido un error en el metodo bcAvisos.deleteAviso");
  System.out.println("bcAvisos.delete:"+SQL.toString());
  throw e;
 }
 finally {
  if (stQuery != null){
   stQuery.close();
   stQuery=null;      }           
  SQL.setLength(0);
  SQL=null;
 }  
} 

public int update(Connection con) throws SQLException, Exception  {
 adecuaCampos();
 StringBuffer SQL = new StringBuffer("");
 SQL.append("update gtcAvisos set modulo=");
 SQL.append(modulo);
 SQL.append(",ctaorigen=");
 SQL.append(ctaorigen);
 SQL.append(",listadestinatarios=");
 SQL.append(listadestinatarios);
 SQL.append(",machote=");
 SQL.append(machote);
 SQL.append(",titulo=");
 SQL.append(titulo);
 SQL.append(",estatus=");
 SQL.append(estatus);
 SQL.append(",html=");
 SQL.append(html);
 SQL.append(",copiapara=");
 SQL.append(copiapara);
 SQL.append(",copiaOculta=");
 SQL.append(copiaOculta);
 SQL.append(" where evento=");
 SQL.append(evento);
 int regsAfectados =-1;
 Statement stQuery=null;
 try{
  stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
  stQuery.executeUpdate(SQL.toString());
  //regsAfectados=sbAutentifica.executeUpdate("gtcAvisos",SQL.toString(),stQuery);
  stQuery.close();
  stQuery=null;
 }
 catch (Exception e){
  System.out.println("Ha ocurrido un error en el metodo bcAvisos.update");
  System.out.println("bcReimpresion.update:"+SQL.toString());
  throw new Exception(e);
 }
 finally {
  if (stQuery != null){
   stQuery.close();
   stQuery=null;     }           
  SQL.setLength(0);
  SQL=null;
  return regsAfectados;
 } 
}

}
