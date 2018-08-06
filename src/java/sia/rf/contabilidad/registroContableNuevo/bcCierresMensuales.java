package sia.rf.contabilidad.registroContableNuevo;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import sia.rf.contabilidad.registroContableNuevo.bloqueo.BloquearUnidades;

public class bcCierresMensuales {
    public bcCierresMensuales() {
    }
    
    private String cierre_mensual_id; 
    private String unidad_ejecutora; 
    private String ambito; 
    private String pais; 
    private String entidad; 
    private String programa;   
    private String mes; 
    private String estatus_cierre_id; 
    private String ejercicio;
    private String id_catalogo_cuenta;
    private String registro;
    private String num_empleado;
    
    private Vector vUnidad;
    private Vector vEntidad;
    private Vector vAmbito;
    private Vector vPrograma;
    
    private List<BloquearUnidades> listUnidades; 


    public void setCierre_mensual_id(String cierre_mensual_id) {
        this.cierre_mensual_id = cierre_mensual_id;
    }

    public String getCierre_mensual_id() {
        return cierre_mensual_id;
    }

    public void setUnidad_ejecutora(String unidad_ejecutora) {
        this.unidad_ejecutora = unidad_ejecutora;
    }

    public String getUnidad_ejecutora() {
        return unidad_ejecutora;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPais() {
        return pais;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getPrograma() {
        return programa;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getMes() {
        return mes;
    }

    public void setEstatus_cierre_id(String estatus_cierre_id) {
        this.estatus_cierre_id = estatus_cierre_id;
    }

    public String getEstatus_cierre_id() {
        return estatus_cierre_id;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public void setId_catalogo_cuenta(String id_catalogo_cuenta) {
        this.id_catalogo_cuenta = id_catalogo_cuenta;
    }

    public String getId_catalogo_cuenta() {
        return id_catalogo_cuenta;
    }
    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public String getRegistro() {
        return registro;
    }
    public void setNum_empleado(String num_empleado) {
        this.num_empleado = num_empleado;
    }

    public String getNum_empleado() {
        return num_empleado;
    }   

    public void setVUnidad(Vector vUnidad) {
        this.vUnidad = vUnidad;
    }

    public Vector getVUnidad() {
        return vUnidad;
    }

    public void setVEntidad(Vector vEntidad) {
        this.vEntidad = vEntidad;
    }

    public Vector getVEntidad() {
        return vEntidad;
    }

    public void setVAmbito(Vector vAmbito) {
        this.vAmbito = vAmbito;
    }

    public Vector getVAmbito() {
        return vAmbito;
    }

    public void setVPrograma(Vector vPrograma) {
        this.vPrograma = vPrograma;
    }

    public Vector getVPrograma() {
        return vPrograma;
    }

  public void setListUnidades(List<BloquearUnidades> listUnidades) {
    this.listUnidades = listUnidades;
  }

  public List<BloquearUnidades> getListUnidades() {
    return listUnidades;
  }
    
    /** 
    * Metodo que lee la informacion de RF_TR_CIERRES_MENSUALES 
    * Fecha de creacion: 29/09/2009
    * Autor: Claudia Luz Hernandez Lopez
    * Fecha de modificacion: 
    * Modificado por: 
    */ 
    public void select_cierre_mensual(Connection con, String pUnidad_ejecutora,String pAmbito,String pEntidad,String pEjecicio,String pIdCatalogoCuenta,String pMes, String pPrograma,String pTipoCierre)throws SQLException, Exception{ 
       Statement stQuery=null; 
       ResultSet rsQuery=null; 
       StringBuffer SQL=new StringBuffer("");
       try{ 
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          SQL.append("select t.cierre_mensual_id,t.unidad_ejecutora,t.ambito,t.entidad,t.pais, t.programa, t.mes, t.ejercicio,t.registro,t.estatus_cierre_id,t.id_catalogo_cuenta ");
          SQL.append(" from rf_tr_cierres_mensuales t");
          SQL.append(" where t.unidad_ejecutora='").append(pUnidad_ejecutora).append("' and t.ambito='").append(pAmbito).append("' ");
          SQL.append(" and t.entidad='").append(pEntidad).append("' and t.ejercicio='").append(pEjecicio).append("' and t.id_catalogo_cuenta='").append(pIdCatalogoCuenta).append("' and t.mes='");
          SQL.append(pMes).append("' and t.programa='").append(pPrograma).append("' and t.estatus_cierre_id='").append(pTipoCierre).append("' ");
          
          rsQuery=stQuery.executeQuery(SQL.toString()); 
          if (rsQuery.next()){ 
            cierre_mensual_id=(rsQuery.getString("cierre_mensual_id")==null) ? "" : rsQuery.getString("cierre_mensual_id"); 
            unidad_ejecutora=(rsQuery.getString("unidad_ejecutora")==null) ? "" : rsQuery.getString("unidad_ejecutora"); 
            ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito"); 
            pais=(rsQuery.getString("pais")==null) ? "" : rsQuery.getString("pais"); 
            entidad=(rsQuery.getString("entidad")==null) ? "" : rsQuery.getString("entidad"); 
            programa=(rsQuery.getString("programa")==null) ? "" : rsQuery.getString("programa"); 
            mes=(rsQuery.getString("mes")==null) ? "" : rsQuery.getString("mes"); 
            estatus_cierre_id=(rsQuery.getString("estatus_cierre_id")==null) ? "" : rsQuery.getString("estatus_cierre_id"); 
            ejercicio=(rsQuery.getString("ejercicio")==null) ? "" : rsQuery.getString("ejercicio"); 
            id_catalogo_cuenta=(rsQuery.getString("id_catalogo_cuenta")==null) ? "" : rsQuery.getString("id_catalogo_cuenta"); 
            registro=(rsQuery.getString("registro")==null) ? "" : rsQuery.getString("registro"); 
          } // Fin while 
          else{
            estatus_cierre_id="";
          }
      } //Fin try 
      catch(Exception e){ 
        System.out.println(SQL.toString()); 
        System.out.println("Ocurrio un error al accesar al metodo select_cierre_mensual "+e.getMessage()); 
      
        throw e; 
      } //Fin catch 
      finally{ 
        if (rsQuery!=null){ 
          rsQuery.close(); 
          rsQuery=null; 
        } 
        if (stQuery!=null){ 
          stQuery.close(); 
          stQuery=null; 
        } 
        SQL.replace(0,SQL.length(),"");
        SQL = null;
      } //Fin finally 
    } //Fin metodo select_cierre_mensual 




     /** 
     * Metodo que lee la informacion de RF_TR_CIERRES_MENSUALES - REGISTROS CON ESTATUS 0
     * Fecha de creacion: 03/07/2012
     * Autor: Claudia Luz Hernandez Lopez
     */ 
     public void select_cierre_mensual_todas_unidad_programas(Connection con, String pEjecicio,String pIdCatalogoCuenta,String pMes, String pTipoCierre)throws SQLException, Exception{ 
        Statement stQuery=null; 
        ResultSet rsQuery=null; 
        StringBuffer SQL=new StringBuffer("");
        try{ 
           stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
           SQL.append("select t.cierre_mensual_id,t.unidad_ejecutora,t.ambito,t.entidad,t.pais, t.programa, t.mes, t.ejercicio,t.registro,t.estatus_cierre_id,t.id_catalogo_cuenta ");
           SQL.append(" from rf_tr_cierres_mensuales t");
           SQL.append(" where t.ejercicio='").append(pEjecicio).append("' and t.id_catalogo_cuenta='").append(pIdCatalogoCuenta).append("' and t.mes='");
           SQL.append(pMes).append("' and t.estatus_cierre_id='").append(pTipoCierre).append("' ");
           SQL.append(" order by t.programa,t.unidad_ejecutora,t.ambito,t.entidad");
           rsQuery=stQuery.executeQuery(SQL.toString()); 

           vUnidad = new Vector();   
           vEntidad = new Vector();
           vAmbito = new Vector();
           vPrograma = new Vector();           
           setListUnidades(new ArrayList<BloquearUnidades>());
           
           while (rsQuery.next()){ 
             cierre_mensual_id=(rsQuery.getString("cierre_mensual_id")==null) ? "" : rsQuery.getString("cierre_mensual_id"); 
             unidad_ejecutora=(rsQuery.getString("unidad_ejecutora")==null) ? "" : rsQuery.getString("unidad_ejecutora"); 
             ambito=(rsQuery.getString("ambito")==null) ? "" : rsQuery.getString("ambito"); 
             pais=(rsQuery.getString("pais")==null) ? "" : rsQuery.getString("pais"); 
             entidad=(rsQuery.getString("entidad")==null) ? "" : rsQuery.getString("entidad"); 
             programa=(rsQuery.getString("programa")==null) ? "" : rsQuery.getString("programa"); 
             mes=(rsQuery.getString("mes")==null) ? "" : rsQuery.getString("mes"); 
             estatus_cierre_id=(rsQuery.getString("estatus_cierre_id")==null) ? "" : rsQuery.getString("estatus_cierre_id"); 
             ejercicio=(rsQuery.getString("ejercicio")==null) ? "" : rsQuery.getString("ejercicio"); 
             id_catalogo_cuenta=(rsQuery.getString("id_catalogo_cuenta")==null) ? "" : rsQuery.getString("id_catalogo_cuenta"); 
             registro=(rsQuery.getString("registro")==null) ? "" : rsQuery.getString("registro"); 
             
             vUnidad.addElement(unidad_ejecutora);            
             vEntidad.addElement(entidad);
             vAmbito.addElement(ambito);
             vPrograma.addElement(programa);
             getListUnidades().add(new BloquearUnidades(unidad_ejecutora,ambito,entidad,programa));
             
             
           } // Fin while 
       } //Fin try 
       catch(Exception e){ 
         System.out.println(SQL.toString()); 
         System.out.println("Ocurrio un error al accesar al metodo select_cierre_mensual_todas_unidad_programas "+e.getMessage()); 
       
         throw e; 
       } //Fin catch 
       finally{ 
         if (rsQuery!=null){ 
           rsQuery.close(); 
           rsQuery=null; 
         } 
         if (stQuery!=null){ 
           stQuery.close(); 
           stQuery=null; 
         } 
         SQL.replace(0,SQL.length(),"");
         SQL = null;
       } //Fin finally 
     } //Fin metodo select_cierre_mensual_todas_unidad_programas 



     /** 
     * Metodo que lee la informacion de RF_TR_CIERRES_MENSUALES y lo regresa en una lista
     * Fecha de creacion: 16/08/2011
     * Autor: Claudia Torres Macario
     * Fecha de modificacion: 
     * Modificado por: 
     */ 
     public List select_cierre_mensual(Connection con, String pUnidad_ejecutora,String pAmbito,String pEntidad,String pEjecicio,String pIdCatalogoCuenta,String pMes, String pPrograma)throws SQLException, Exception{ 
        Statement stQuery=null; 
        ResultSet rsQuery=null; 
        StringBuffer SQL=new StringBuffer("");
        List registros = null;
        try{ 
           registros = new ArrayList();
           stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
           SQL.append("select t.cierre_mensual_id,t.unidad_ejecutora,t.ambito,t.entidad,t.pais, t.programa, t.mes, t.ejercicio,t.registro,t.estatus_cierre_id,t.id_catalogo_cuenta ");
           SQL.append(" from rf_tr_cierres_mensuales t");
           SQL.append(" where t.unidad_ejecutora='").append(pUnidad_ejecutora).append("' and t.ambito='").append(pAmbito).append("' ");
           SQL.append(" and t.entidad='").append(pEntidad).append("' and t.ejercicio='").append(pEjecicio).append("' and t.id_catalogo_cuenta='").append(pIdCatalogoCuenta).append("' and t.mes='");
           SQL.append(pMes).append("' and t.programa='").append(pPrograma).append("' order by t.estatus_cierre_id");          
           rsQuery=stQuery.executeQuery(SQL.toString());
           while (rsQuery.next()){ 
             registros.add(rsQuery.getString("estatus_cierre_id"));
           } // Fin while 
       } //Fin try 
       catch(Exception e){ 
         System.out.println(SQL.toString()); 
         System.out.println("Ocurrio un error al accesar al metodo select_cierre_mensual "+e.getMessage()); 
         throw e; 
       } //Fin catch 
       finally{ 
         if (rsQuery!=null){ 
           rsQuery.close(); 
           rsQuery=null; 
         } 
         if (stQuery!=null){ 
           stQuery.close(); 
           stQuery=null; 
         } 
       } //Fin finally 
       return registros;
     } //Fin metodo select_cierre_mensual 

 
 
     /** 
     * Metodo que inserta la informacion de RF_TR_CIERRES_MENSUALES 
     * Fecha de creacion: 27/10/2009
     * Autor: Claudia Luz Hernandez Lopez
     * Fecha de modificacion: 
     * Modificado por: 
     */ 
     public void insert_cierre_mensual(Connection con) throws SQLException, Exception{ 
        Statement stQuery=null; 
        StringBuffer SQL=new StringBuffer("");
        try{ 
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
         SQL.append("INSERT INTO rf_tr_cierres_mensuales(unidad_ejecutora,ambito,pais,entidad,mes,registro,estatus_cierre_id,ejercicio,id_catalogo_cuenta,programa,num_empleado) ");  
         SQL.append("VALUES('"); 
         SQL.append(unidad_ejecutora).append("',"); 
         SQL.append(ambito).append(","); 
         SQL.append(pais).append(","); 
         SQL.append(entidad).append(","); 
         SQL.append(mes).append(",");          
         SQL.append(" sysdate,"); 
         SQL.append(estatus_cierre_id).append(","); 
         SQL.append(ejercicio).append(","); 
         SQL.append(id_catalogo_cuenta).append(",");          
         SQL.append("'").append(programa).append("',");
         SQL.append("'").append(num_empleado).append("')");
         //System.out.println(SQL.toString()); 
         int rs=-1; 
         rs=stQuery.executeUpdate(SQL.toString()); 
       } //Fin try 
       catch(Exception e){ 
         System.out.println(SQL.toString()); 
         System.out.println("Ocurrio un error al accesar al metodo insert_cierre_mensual "+e.getMessage()); 
         throw e; 
       } //Fin catch 
       finally{ 
         if (stQuery!=null){ 
           stQuery.close(); 
           stQuery=null; 
         } 
         SQL.replace(0,SQL.length(),"");
         SQL = null;
       } //Fin finally 
     } //Fin metodo insert_cierre_mensual 
     
  /** 
  * Metodo que lee cuántos regs la informacion de RF_TR_CIERRES_MENSUALES por estatus
  * Fecha de creacion: 29/09/2009
  * Autor: Susana Medina
  * Fecha de modificacion: 
  * Modificado por: 
  */ 
   public int select_cierre_mensual_estatus(Connection con, String pCondicionUnidad,String pCondicionAmbito,String pCondicionEntidad,String pEjercicio,String pIdCatalogoCuenta,String pMes, String pPrograma,String pTipoCierre)throws SQLException, Exception{ 
      //select_cierre_mensual_estatus(        conexion,       lsUnidad,                lsAmbito,          lsEntidad,        lsEjercicio,lsIdCatalogoCuenta,     lsMes,           lsPrograma,    '2')
      Statement stQuery=null;
      ResultSet rsQuery=null;
      StringBuffer SQL=new StringBuffer("");
      int registros=0;
      try{
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
        SQL.append("select t.cierre_mensual_id,t.unidad_ejecutora,t.ambito,t.entidad,t.pais, t.programa, t.mes, t.ejercicio,t.registro,t.estatus_cierre_id,t.id_catalogo_cuenta ");
        SQL.append(" from rf_tr_cierres_mensuales t");
        SQL.append(" where ");
        SQL.append(" t.id_catalogo_cuenta='").append(pIdCatalogoCuenta).append("'");
        SQL.append(" and t.unidad_ejecutora="+pCondicionUnidad);
        SQL.append(" and t.ambito="+pCondicionAmbito);
        SQL.append(" and t.entidad="+pCondicionEntidad);
        SQL.append(" and t.ejercicio='").append(pEjercicio).append("'");
        SQL.append(" and t.id_catalogo_cuenta='").append(pIdCatalogoCuenta).append("'");
        SQL.append(" and t.mes='").append(pMes).append("' ");
        SQL.append(" and t.programa='").append(pPrograma).append("'");
        SQL.append(" and t.estatus_cierre_id='").append(pTipoCierre).append("' ");

        //System.out.println(SQL.toString());
        rsQuery=stQuery.executeQuery(SQL.toString()); 
        while(rsQuery.next()){ 
          registros=+1;
        } // Fin while 
        
      } //Fin try
      catch(Exception e){
      System.out.println(SQL.toString()); 
      System.out.println("Ocurrio un error al accesar al metodo select_cierre_mensual "+e.getMessage()); 
      
      throw e; 
      } //Fin catch
      finally{
      if (rsQuery!=null){ 
        rsQuery.close(); 
        rsQuery=null; 
      } 
      if (stQuery!=null){ 
        stQuery.close(); 
        stQuery=null; 
      } 
        SQL.replace(0,SQL.length(),"");
        SQL = null;
      } //Fin finally
      return registros;
  } //Fin metodo select_cierre_mensual_estatus     INSERT INTO tMarcas(marca)SELECT DISTINCT marca FROM tCoches;
/** 
* Metodo que inserta la informacion por lote de RF_TR_CIERRES_MENSUALES 
* Fecha de creacion: 08/02/2012
* Autor: Susana Medina Morales
* Fecha de modificacion: 
* Modificado por: 
*/ 
 public void insert_cierre_mensual_estatus(Connection con, String pCondicionUnidad,String pCondicionAmbito,String pCondicionEntidad,String pEjercicio,String pIdCatalogoCuenta,String pMes, String pPrograma,String pTipoCierre) throws SQLException, Exception{ 
    Statement stQuery=null; 
    StringBuffer SQL=new StringBuffer("");
    try{ 
     stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
       SQL.append("INSERT INTO rf_tr_cierres_mensuales(unidad_ejecutora,ambito,pais,entidad,mes,registro,estatus_cierre_id,ejercicio,id_catalogo_cuenta,programa,num_empleado) ");  
       SQL.append("select t.cierre_mensual_id,t.unidad_ejecutora,t.ambito,t.entidad,t.pais, t.programa, t.mes, t.ejercicio,t.registro,'"+ pTipoCierre+"' estatus_cierre_id,t.id_catalogo_cuenta ");
       SQL.append(" from rf_tr_cierres_mensuales t");
       SQL.append(" where ");
       SQL.append(pCondicionUnidad);
       SQL.append(pCondicionAmbito);
       SQL.append(pCondicionEntidad);
      
       SQL.append(" and t.ejercicio='").append(pEjercicio).append("'");
       SQL.append(" and t.id_catalogo_cuenta='").append(pIdCatalogoCuenta).append("'");
       SQL.append(" and t.mes='").append(pMes).append("'");
       SQL.append(" and t.programa='").append(pPrograma).append("'");
       SQL.append(" and t.estatus_cierre_id='0' ");
       
     System.out.println(SQL.toString()); 
     int rs=-1; 
     rs=stQuery.executeUpdate(SQL.toString()); 
    } //Fin try
    catch(Exception e){
     System.out.println(SQL.toString()); 
     System.out.println("Ocurrio un error al accesar al metodo insert_cierre_mensual "+e.getMessage()); 
     throw e; 
    } //Fin catch
    finally{
     if (stQuery!=null){ 
       stQuery.close(); 
       stQuery=null; 
     }
   SQL.replace(0,SQL.length(),"");
   SQL = null;
 } //Fin finally 
} //Fin metodo insert_cierre_mensual por lote  

 public void delete_unidades_bloqueadas(Connection con)throws SQLException, Exception{
    Statement stQuery=null;
    StringBuffer SQL=new StringBuffer("");
    try{
     stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
     SQL.append("delete rf_tr_cierres_mensuales t ");
     SQL.append("where t.estatus_cierre_id=3 ");
     SQL.append("and t.unidad_ejecutora=").append(getUnidad_ejecutora()).append(" ");
     SQL.append("and t.entidad=").append(getEntidad()).append(" ");
     SQL.append("and t.ambito=").append(getAmbito()).append(" ");
     SQL.append("and t.mes=").append(getMes()).append(" ");
     SQL.append("and t.ejercicio=").append(getEjercicio()).append(" ");
     SQL.append("and t.id_catalogo_cuenta=").append(getId_catalogo_cuenta()).append(" ");
     SQL.append("and t.programa='").append(getPrograma()).append("' ");
     //System.out.println(SQL.toString());
     int rs=-1;
     rs=stQuery.executeUpdate(SQL.toString());
     //System.out.println("Registro actualizado "+rs);
   } //Fin try
   catch(Exception e){
     System.out.println(SQL.toString()); 
     System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_polizas "+e.getMessage());
     //System.out.println(SQL.toString());
     throw e;
   } //Fin catch
   finally{
     if (stQuery!=null){
       stQuery.close();
       stQuery=null;
     }
     SQL.replace(0,SQL.length(),"");
     SQL = null;
   } //Fin finally
 } //Fin metodo delete_unidades_bloqueadas

  /** 
  * Metodo que realiza el update del tipo de cierre de la tabla RF_TR_CIERRES_MENSUALES 
  * Fecha de creacion: 08/01/2012
  * Autor: Claudia Luz Hernandez Lopez
  * Fecha de modificacion: 
  * Modificado por: 
  */ 
  public void update_cierre_mensual_estatus(Connection con, String pEstatus) throws SQLException, Exception{ 
     Statement stQuery=null; 
     StringBuffer SQL=new StringBuffer("");
     try{ 
      stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
      SQL.append("UPDATE rf_tr_cierres_mensuales ");  
      SQL.append("set estatus_cierre_id=").append(pEstatus); 
      SQL.append(" ,registro=").append("sysdate"); 
      SQL.append(" ,num_empleado=").append(num_empleado); 
      SQL.append(" WHERE unidad_ejecutora=").append(unidad_ejecutora); 
      SQL.append(" and  ambito=").append(ambito); 
      SQL.append(" and  entidad=").append(entidad); 
      SQL.append(" and  mes=").append(mes); 
      SQL.append(" and  estatus_cierre_id=").append(estatus_cierre_id); 
      SQL.append(" and  ejercicio=").append(ejercicio); 
      SQL.append(" and  id_catalogo_cuenta=").append(id_catalogo_cuenta); 
      SQL.append(" and  programa='").append(programa).append("'");       
      int rs=-1; 
      rs=stQuery.executeUpdate(SQL.toString()); 
    } //Fin try 
    catch(Exception e){ 
      System.out.println(SQL.toString()); 
      System.out.println("Ocurrio un error al accesar al metodo update_cierre_mensual_estatus "+e.getMessage()); 
      throw e; 
    } //Fin catch 
    finally{ 
      if (stQuery!=null){ 
        stQuery.close(); 
        stQuery=null; 
      } 
      SQL.replace(0,SQL.length(),"");
      SQL = null;
    } //Fin finally 
  } //Fin metodo update_cierre_mensual_estatus 



}
