package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sun.jdbc.rowset.CachedRowSet;

public class IjTiPropuesta {
    public IjTiPropuesta() {
    }
    
    private String idPropuesta;
    private String numPropuesta;
    private String fecSuscripcion;
    private String otraJecutora;
    private String descObjetivo;
    private String organismoD;
    private String idOrganismo;
    private String compromiso;
    private String descripcion;
    private String compContra;
    private String compAmbos;
    private String comentario;
    private String responsable;
    private String nomArch;
    private String ruta;
    private String estatus;
    private String fecCrea;
    private String fecNotifica;
    private String fecLibera;
    private String anio;
    private String fecAutoriza;
    private String idTipoObjet;
    private String idInstJurid;
    private String idDRegional;

    public void setIdPropuesta(String idPropuesta) {
        this.idPropuesta = idPropuesta;
    }

    public String getIdPropuesta() {
        return idPropuesta;
    }

    public void setNumPropuesta(String numPropuesta) {
        this.numPropuesta = numPropuesta;
    }

    public String getNumPropuesta() {
        return numPropuesta;
    }

    public void setFecSuscripcion(String fecSuscripcion) {
        this.fecSuscripcion = fecSuscripcion;
    }

    public String getFecSuscripcion() {
        return fecSuscripcion;
    }

    public void setOtraJecutora(String otraJecutora) {
      if(otraJecutora == null || otraJecutora.equals(""))
          this.otraJecutora = "null";
      else
        this.otraJecutora = "'"+otraJecutora+"'";
    }

    public String getOtraJecutora() {
        return (otraJecutora ==null?"":otraJecutora);
    }

    public void setDescObjetivo(String descObjetivo) {
        this.descObjetivo = descObjetivo;
    }

    public String getDescObjetivo() {
        return descObjetivo;
    }

    public void setOrganismoD(String organismo) {
        this.organismoD = organismo;
    }

    public String getOrganismoD() {
        return organismoD;
    }
    
    public String getOrganismo() {
        return this.idOrganismo;
    }

    public void setCompromiso(String compromiso) {
        this.compromiso = compromiso;
    }

    public String getCompromiso() {
        return compromiso;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setCompContra(String compContra) {
        this.compContra = compContra;
    }

    public String getCompContra() {
        return compContra;
    }

    public void setCompAmbos(String compAmbos) {
        this.compAmbos = compAmbos;
    }

    public String getCompAmbos() {
        return compAmbos;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setNomArch(String nomArch) {
        this.nomArch = nomArch;
    }

    public String getNomArch() {
        return nomArch;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getRuta() {
        return ruta;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setFecCrea(String fecCrea) {
        this.fecCrea = fecCrea;
    }

    public String getFecCrea() {
        return fecCrea;
    }

    public void setFecNotifica(String fecNotifica) {
        this.fecNotifica = fecNotifica;
    }

    public String getFecNotifica() {
        return fecNotifica;
    }

    public void setFecLibera(String fecLibera) {
        this.fecLibera = fecLibera;
    }

    public String getFecLibera() {
        return fecLibera;
    }

    public void setFecAutoriza(String fecAutoriza) {
        this.fecAutoriza = fecAutoriza;
    }

    public String getFecAutoriza() {
        return fecAutoriza;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getAnio() {
        return anio;
    }   
    
    public void setIdTipoObjet(String idTipoObjet) {
        this.idTipoObjet = idTipoObjet;
    }

    public String getIdTipoObjet() {
        return idTipoObjet;
    }

    public void setIdInstJurid(String idInstJurid) {
        this.idInstJurid = idInstJurid;
    }

    public String getIdInstJurid() {
        return idInstJurid;
    }

    public void setIdDRegional(String idDRegional) {
      if(idDRegional == null || idDRegional.equals(""))
        this.idDRegional = "null";
      else
        this.idDRegional = idDRegional;
    }

    public String getIdDRegional() {
        return idDRegional;
    }    
    
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }    

    public void setIdOrganismo(String idOrganismo) {
        this.idOrganismo = idOrganismo;
    }

    public String getIdOrganismo() {
        return idOrganismo;
    }    
        
    public String insertIjTiPropuesta(Connection con) throws  Exception {
        ResultSet rsQuery  = null;
        Statement statement  = null;
        StringBuffer cadenaSql   = null;

      try  {
          getNextIdPropuesta(con);
          IjTiSecPropuesta secPropuest = new IjTiSecPropuesta();
          secPropuest.setAnio(this.anio);
          this.numPropuesta = secPropuest.getNumPropuesta(con);
          
          cadenaSql = new StringBuffer();
          cadenaSql.append("insert into Ij_Ti_Propuesta (idPropuesta, numPropuesta, fecSuscripcion, otraJecutora, descObjeto, organismoD, compromiso, compContra, compAmbos,");
          cadenaSql.append("comentario, responsable, nomArch, ruta, estatus, fecCrea, anio, idInstJurid, idTipoObjet, idDRegional, idOrganismo)");
          //cadenaSql.append("fecNotifica, fecLibera, fecAutoriza) ");
          cadenaSql.append(" values (");
          cadenaSql.append(getIdPropuesta()+","+this.numPropuesta+",'"+this.fecSuscripcion+"',"+this.otraJecutora+",'"+this.descObjetivo+"','"+this.organismoD+"',");
          cadenaSql.append("'"+this.compromiso+"','"+this.compContra+"','"+this.compAmbos+"','"+this.comentario+"','"+this.responsable+"','"+this.nomArch+"',");
          cadenaSql.append("'"+this.ruta +"','"+this.estatus +"','"+this.fecCrea +"','" +this.anio+"'," + this.idInstJurid + "," + this.idTipoObjet + "," + this.idDRegional);
          //cadenaSql.append("'"+this.fecNotifica +"','"+ this.fecLibera + "','"+this.fecAutoriza+"'");
           cadenaSql.append(","+this.idOrganismo);
          cadenaSql.append(")");
          
          statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          statement.executeUpdate(cadenaSql.toString());
       return this.numPropuesta;
      } 
      catch (Exception ex)  {
          System.out.println("Error en el metodo IjTiPropuesta.insertIjTiPropuesta"+ex.getMessage());
          throw new Exception(cadenaSql.toString(),ex);
      } 
      finally  {
          cadenaSql.setLength(0);
          
          if(rsQuery != null){
              rsQuery.close();
              rsQuery = null;
          }
          
          if(statement != null){
              statement.close();
              statement = null;
          }
      }
    }
    
    public void updatePropuestaIj(Connection con) throws SQLException, Exception{
       Statement stQuery=null;
        StringBuffer SQL = null;
       try{
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQL = new StringBuffer("UPDATE IJ_TI_PROPUESTA PROP SET PROP.NOMARCH = '"+this.nomArch+"', PROP.RUTA = '"+this.ruta+"'");
        SQL.append(" WHERE PROP.IDPROPUESTA = "+this.idPropuesta);  
        
        int rs=-1;
        rs=stQuery.executeUpdate(SQL.toString());
      } //Fin try
      catch(Exception e){
        System.out.println("SQL"+SQL.toString());
        System.out.println("Ocurrio un error al accesar al metodo IjTiPropuesta.updatePropuestaIj() "+e.getMessage());
        throw e;
      } //Fin catch
      finally{
        if (stQuery!=null){
          stQuery.close();
          stQuery=null;
        }
      } //Fin finally
    } //Fin metodo updatePropuestaIj   
    
    
    public void selectPropuestaIj(Connection con) throws SQLException, Exception{
          StringBuffer SQL;
          SQL = new StringBuffer();
          SQL.append("Select ijTiPropuesta.idOrganismo, ijTiPropuesta.Idpropuesta, ijTiPropuesta.numpropuesta, to_char(ijTiPropuesta.fecsuscripcion,'dd/MM/yyyy') fecsuscripcion, ijTiPropuesta.otrajecutora, \n");
          SQL.append("ijTiPropuesta.descobjeto,ijTiPropuesta.organismod, ijTiPropuesta.compromiso, ijTiPropuesta.compcontra, ijTiPropuesta.compambos, \n");
          SQL.append("ijTiPropuesta.comentario, ijTiPropuesta.responsable, ijTiPropuesta.nomarch,ijTiPropuesta.ruta,ijTiPropuesta.estatus, \n");
          SQL.append("to_char(ijTiPropuesta.feccrea,'dd/MM/yyyy') as feccrea, to_char(ijTiPropuesta.fecnotifica,'dd/MM/yyyy') as fecnotifica, to_char(ijTiPropuesta.feclibera,'dd/MM/yyyy') as feclibera, ijTiPropuesta.anio,ijTiPropuesta.idinstjurid, \n");
          SQL.append("ijTiPropuesta.idtipoobjet, ijTiPropuesta.iddregional \n");
          SQL.append(" from ij_ti_propuesta ijTiPropuesta \n");
          
          if(this.idPropuesta != null){
            SQL.append(" where ijTiPropuesta.Idpropuesta = "+this.idPropuesta+" \n");
          }
          
          if(this.numPropuesta != null && !this.numPropuesta.equals(""))
            SQL.append(" where ijTiPropuesta.numpropuesta ="+this.numPropuesta);

          Statement stQuery = null;
          ResultSet rsQuery = null;
          int resultado = 0;
          try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());
            
            rsQuery.beforeFirst();
            if (rsQuery.next() )  {
              //limpia();
               this.idPropuesta  = rsQuery.getString("IDPROPUESTA");
               this.numPropuesta = rsQuery.getString("NUMPROPUESTA");
               this.fecSuscripcion = rsQuery.getString("FECSUSCRIPCION");
               this.otraJecutora = rsQuery.getString("OTRAJECUTORA");
               this.descObjetivo = rsQuery.getString("DESCOBJETO");
               this.organismoD    = rsQuery.getString("ORGANISMOD");
               this.compromiso    = rsQuery.getString("COMPROMISO");
               this.compContra    = rsQuery.getString("COMPCONTRA");
               this.compAmbos    = rsQuery.getString("COMPAMBOS");
               this.comentario   = rsQuery.getString("COMENTARIO");
               this.responsable  = (rsQuery.getString("RESPONSABLE")==null?"":rsQuery.getString("RESPONSABLE"));
               this.nomArch      = rsQuery.getString("NOMARCH");
               this.ruta         = rsQuery.getString("RUTA");
               this.estatus      = rsQuery.getString("ESTATUS");
               this.fecCrea      = rsQuery.getString("FECCREA");
               this.fecNotifica  = (rsQuery.getString("FECNOTIFICA")==null?"":rsQuery.getString("FECNOTIFICA"));
               this.fecLibera    = (rsQuery.getString("FECLIBERA")==null?"":rsQuery.getString("FECLIBERA"));
               this.anio         = rsQuery.getString("ANIO");
               this.idInstJurid  = rsQuery.getString("IDINSTJURID");
               this.idDRegional   = rsQuery.getString("IDDREGIONAL");
               this.idTipoObjet   = rsQuery.getString("IDTIPOOBJET");
               this.idOrganismo   = (rsQuery.getString("IDORGANISMO")==null?"":rsQuery.getString("IDORGANISMO"));
            }
          }
          catch (Exception e) {
            System.out.println("Ha ocurrido un error en el metodo IjTiPropuesta.selectPropuestaIj");
            System.out.println("IjTiPropuesta.selectPropuestaIj: " + SQL.toString());
            throw new Exception(SQL.toString(),e);
          }
          finally {
            if (rsQuery != null) 
              rsQuery.close(); 
            rsQuery=null;
            if (stQuery != null)
              stQuery.close();
            stQuery = null;
            SQL.setLength(0);
            SQL = null;
          }         
        }
        
    
    private void getNextIdPropuesta(Connection con) throws  Exception {
      Statement stQuery=null;
      StringBuffer SQL = null;
      ResultSet rsQuerySeq = null;
      try {
          stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          //SE OBTIENE LA SECUENCIA
          SQL = new StringBuffer("select SEQPROPUESTA.nextval valoractual from dual");
          
          rsQuerySeq = stQuery.executeQuery(SQL.toString());
          
          if (rsQuerySeq.next()){
            setIdPropuesta(rsQuerySeq.getString("valoractual"));
          } //del while
      }
      catch(Exception e) {
          throw new Exception(SQL.toString(),e);
      }
      finally {
        SQL.setLength(0);
        if(rsQuerySeq != null){
          rsQuerySeq.close();
          rsQuerySeq = null;
        }
        if(stQuery != null){
          stQuery.close();
         stQuery = null;
        }
      }
    }
    
    public CachedRowSet select(Connection con) throws SQLException, Exception{
          StringBuffer SQL;
          SQL = new StringBuffer();
          SQL.append("Select ijTiPropuesta.idOrganismo, ijTiPropuesta.Idpropuesta, ijTiPropuesta.numpropuesta, to_char(ijTiPropuesta.fecsuscripcion,'dd/MM/yyyy') fecsuscripcion, ijTiPropuesta.otrajecutora, \n");
          SQL.append("ijTiPropuesta.descobjeto,ijTiPropuesta.organismod, ijTiPropuesta.compromiso, ijTiPropuesta.compcontra, ijTiPropuesta.compambos, \n");
          SQL.append("ijTiPropuesta.comentario, ijTiPropuesta.responsable, ijTiPropuesta.nomarch,ijTiPropuesta.ruta,ijTiPropuesta.estatus, \n");
          SQL.append("to_char(ijTiPropuesta.feccrea,'dd/MM/yyyy') as feccrea, to_char(ijTiPropuesta.fecnotifica,'dd/MM/yyyy') as fecnotifica, to_char(ijTiPropuesta.feclibera,'dd/MM/yyyy') as feclibera, ijTiPropuesta.anio,ijTiPropuesta.idinstjurid, \n");
          SQL.append("ijTiPropuesta.idtipoobjet, ijTiPropuesta.iddregional \n");
          SQL.append(" from ij_ti_propuesta ijTiPropuesta \n");
          
          if(this.idPropuesta != null){
            SQL.append(" where ijTiPropuesta.Idpropuesta = "+this.idPropuesta+" \n");
          }
          
          if(this.numPropuesta != null && !this.numPropuesta.equals(""))
            SQL.append(" where ijTiPropuesta.numpropuesta ="+this.numPropuesta);

          CachedRowSet crResultado = null;
          int resultado = 0;
          try {
            crResultado = new CachedRowSet();
            crResultado.setCommand(SQL.toString());
            crResultado.execute(con);
            
            return crResultado;
          }
          catch (Exception e) {
            System.out.println("Ha ocurrido un error en el metodo IjTiPropuesta.select");
            System.out.println("IjTiPropuesta.select: " + SQL.toString());
            throw new Exception(SQL.toString(),e);
          }
          finally {
            SQL.setLength(0);
            SQL = null;
          }         
        }
    

    
    public String deleteIjTiPropuesta(Connection con) throws  Exception {
             int rs=-1;
             Statement stQuery=null;
             String SQL = "";
             String regresa = "";
             
             try {
               regresa = selectNumPropuesta(con);
                 
               stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
               SQL = "delete ij_ti_propuesta ijTiPropuesta where ijTiPropuesta.Idpropuesta = "+ this.idPropuesta;
               rs =stQuery.executeUpdate(SQL);
                 
               return regresa;   
             }
            catch (Exception e) {
               System.out.println("Ha occurrido un error en el metodo IjTiPropuesta.deleteIjTiPropuesta: "+SQL.toString());         
               throw e;
             }
             finally {                  
               if (stQuery != null) {
                 stQuery.close();
                 stQuery=null;
               }                         
             }
    }//deleteIjTrFirmantes    
    

     public void updateIjTiPropuesta(Connection con)throws SQLException, Exception{
        Statement stQuery=null;
        try{
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         StringBuffer SQL=new StringBuffer(" ");
         SQL.append("UPDATE IJ_TI_PROPUESTA Propuesta set Propuesta.Fecsuscripcion = '"+this.fecSuscripcion+"', Propuesta.Otrajecutora = '"+this.otraJecutora+"', Propuesta.Descobjeto = '"+this.descObjetivo+"', \n");
         SQL.append("  Propuesta.Organismod = '"+this.organismoD+"', Propuesta.Compromiso = '"+this.compromiso+"', Propuesta.Compcontra = '"+this.compAmbos+"',  \n");
         SQL.append("  Propuesta.Compambos = '"+this.compAmbos+"', Propuesta.Comentario = '"+this.comentario+"', Propuesta.Responsable = '"+this.responsable+"',   \n");
         SQL.append("  Propuesta.Estatus = '"+this.estatus+"', Propuesta.Feccrea = '"+this.fecCrea+"', Propuesta.Fecnotifica = '"+this.fecNotifica+"',   \n");
         
         if(this.fecLibera != null && !this.fecLibera.equals(""))
             this.fecLibera = "'"+this.fecLibera+"'";
        else
            this.fecLibera = "null";
             
         SQL.append("  propuesta.feclibera = "+this.fecLibera+", \n");
         SQL.append("  propuesta.idinstjurid = '"+this.idInstJurid+"', propuesta.idtipoobjet = '"+this.idTipoObjet+"', propuesta.iddregional = '"+this.idDRegional+"',   \n");
         SQL.append("  propuesta.idorganismo = '"+this.idOrganismo+"'   \n");
         SQL.append("  WHERE propuesta.idpropuesta = "+this.idPropuesta+"  \n");
         
         int rs=-1;
         rs=stQuery.executeUpdate(SQL.toString());
         
         //limpiaSentencia(con);
       } //Fin try
       catch(Exception e){
         System.out.println("Ocurrio un error al accesar al metodo IjTiPropuesta.updateIjTiPropuesta() "+e.getMessage());
         throw e;
       } //Fin catch
       finally{
         if (stQuery!=null){
           stQuery.close();
           stQuery=null;
         }
       } //Fin finally
     } //Fin metodo 
     
      public String selectNumPropuesta(Connection con) throws SQLException, Exception{
            StringBuffer SQL;
            SQL = new StringBuffer();
            SQL.append(" Select propuesta.numPropuesta From Ij_Ti_Propuesta propuesta where propuesta.idpropuesta = "+this.idPropuesta+" \n");
            String regresa = "";
            Statement stQuery = null;
            ResultSet rsQuery = null;
            try {
              stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
              rsQuery = stQuery.executeQuery(SQL.toString());
              
              if(rsQuery.next())
                  regresa = rsQuery.getString("numPropuesta");
                  
             return regresa;
            }//try
            catch (Exception e) {
              System.out.println("Ha ocurrido un error en el metodo IjTiPropuesta.selectNumPropuesta");
              System.out.println("query : " + SQL.toString());
              throw new Exception(SQL.toString(),e);
            }
            finally {
              if (rsQuery != null) 
                rsQuery.close(); 
              rsQuery=null;
              if (stQuery != null)
                stQuery.close();
              stQuery = null;
              SQL.setLength(0);
              SQL = null;
            }         
       }     
       
    public void selectDatosArchivo(Connection con) throws SQLException, Exception{
          StringBuffer SQL;
          SQL = new StringBuffer();
          SQL.append("Select propuesta.nomarch, propuesta.ruta From Ij_Ti_Propuesta propuesta Where idpropuesta = "+this.idPropuesta+" \n");
          Statement stQuery = null;
          ResultSet rsQuery = null;
          try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());
            
            if(rsQuery.next()){
              this.nomArch = rsQuery.getString("NOMARCH");
              this.ruta    = rsQuery.getString("RUTA");
            }
          }//try
          catch (Exception e) {
            System.out.println("Ha ocurrido un error en el metodo IjTiPropuesta.selectDatosArchivo");
            System.out.println("query : " + SQL.toString());
            throw new Exception(SQL.toString(),e);
          }
          finally {
            if (rsQuery != null) 
              rsQuery.close(); 
            rsQuery=null;
            if (stQuery != null)
              stQuery.close();
            stQuery = null;
            SQL.setLength(0);
            SQL = null;
          }         
     }


}
