package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IjTiJuridico {
    public IjTiJuridico() {
    }

    private String idJuridico;
    private String numJuridico;
    private String fecSuscripcion;
    private String otraUbica;
    private String otraEjecutora;
    private String fecVigIni;
    private String fecVigFin;
    private String otraConclusion;
    private String descObjeto;
    private String compromiso;
    private String prodServ;
    private String compContra;
    private String compAmbos;
    private String comentario;
    private String progTrabajo;
    private String motivoPt;
    private String fecRegistro;
    private String horaRegistro;
    private String nomArch;
    private String ruta;
    private String fecNotifica;
    private String fecLibera;
    private String fecConclusion;
    private String motivoCon;
    private String numEmpleado;
    private String anio;
    private String idConclusion;
    private String idTipoObjetivo;
    private String idDRegional;
    private String idDRegional1;
    private String idInstJurid;
    private String estatus;
    
    /*campos que no pertenecen a la tabla ij_ti_juridico*/
    private String horaLibera;
    private String horaNotifica;
    private String horaConclus;

    public void setIdJuridico(String idJuridico) {
        this.idJuridico = idJuridico;
    }

    public String getIdJuridico() {
        return idJuridico;
    }

    public void setNumJuridico(String numJuridico) {
        this.numJuridico = numJuridico;
    }

    public String getNumJuridico() {
        return numJuridico;
    }

    public void setFecSuscripcion(String fecSuscripcion) {
        this.fecSuscripcion = fecSuscripcion;
    }

    public String getFecSuscripcion() {
        return fecSuscripcion;
    }

    public void setOtraUbica(String otraUbica) {
      if(otraUbica == null || otraUbica.equals(""))
          this.otraUbica = "null";
      else
          this.otraUbica = "'"+otraUbica+"'";
    }

    public String getOtraUbica() {
        return otraUbica;
    }

    public void setOtraEjecutora(String otraEjecutora) {
      if(otraEjecutora == null || otraEjecutora.equals(""))
         this.otraEjecutora = "null";
      else
        this.otraEjecutora = "'"+otraEjecutora+"'";
    }

    public String getOtraEjecutora() {
        return otraEjecutora;
    }

    public void setFecVigIni(String fecVigIni) {
        this.fecVigIni = fecVigIni;
    }

    public String getFecVigIni() {
        return fecVigIni;
    }

    public void setFecVigFin(String fecVigFin) {
      if(fecVigFin == null || fecVigFin.equals(""))
        this.fecVigFin = "null";
       else
        this.fecVigFin = "to_date('"+fecVigFin+"','dd/MM/yyyy')";
    }

    public String getFecVigFin() {
        return fecVigFin;
    }

    public void setOtraConclusion(String otraConclusion) {
       if(otraConclusion == null || otraConclusion.equals(""))
           this.otraConclusion = "null";
       else
         this.otraConclusion = "'"+ otraConclusion+"'";
    }

    public String getOtraConclusion() {
        return otraConclusion;
    }

    public void setDescObjeto(String descObjeto) {
        this.descObjeto = descObjeto;
    }

    public String getDescObjeto() {
        return descObjeto;
    }

    public void setCompromiso(String compromiso) {
        this.compromiso = compromiso;
    }

    public String getCompromiso() {
        return compromiso;
    }

    public void setProdServ(String prodServ) {
        this.prodServ = prodServ;
    }

    public String getProdServ() {
        return prodServ;
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

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setProgTrabajo(String progTrabajo) {
        this.progTrabajo = progTrabajo;
    }

    public String getProgTrabajo() {
        return progTrabajo;
    }

    public void setMotivoPt(String motivoPt) {
        this.motivoPt = motivoPt;
    }

    public String getMotivoPt() {
        return motivoPt;
    }

    public void setFecRegistro(String fecRegistro) {
        this.fecRegistro = fecRegistro;
    }

    public String getFecRegistro() {
        return fecRegistro;
    }

    public void setHoraRegistro(String horaRegistro) {
        this.horaRegistro = horaRegistro;
    }

    public String getHoraRegistro() {
        return horaRegistro;
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

    public void setFecConclusion(String fecConclusion) {
        this.fecConclusion = fecConclusion;
    }

    public String getFecConclusion() {
        return fecConclusion;
    }

    public void setMotivoCon(String motivoCon) {
        this.motivoCon = motivoCon;
    }

    public String getMotivoCon() {
        return motivoCon;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getAnio() {
        return this.anio;
    }

    public void setIdConclusion(String idConclusion) {
        if(idConclusion.equals(""))
            this.idConclusion = "null";
        else
            this.idConclusion = idConclusion;
    }

    public String getIdConclusion() {
        return idConclusion;
    }

    public void setIdTipoObjetivo(String idTipoObjetivo) {
        this.idTipoObjetivo = idTipoObjetivo;
    }

    public String getIdTipoObjetivo() {
        return idTipoObjetivo;
    }

    public void setIdDRegional(String idDRegional) {
        this.idDRegional = (idDRegional.equals("")?null:idDRegional);
    }

    public String getIdDRegional() {
        return idDRegional;
    }


    public void setIdInstJurid(String idInstJurid) {
        this.idInstJurid = idInstJurid;
    }

    public String getIdInstJurid() {
        return idInstJurid;
    }
    
    public void setIdDRegional1(String idDRegional1) {
    
      if(idDRegional1.equals("") || idDRegional1 == null)
        this.idDRegional1 = null;
      else
        this.idDRegional1 = idDRegional1;
    }

    public String getIdDRegional1() {
        return idDRegional1;
    }    
    
    public String getHoraLibera() {
        return horaLibera;
    }

    public String getHoraNotifica() {
        return horaNotifica;
    }

    public String getHoraConclus() {
        return horaConclus;
    }
    
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setHoraLibera(String horaLibera) {
        this.horaLibera = horaLibera;
    }

    public void setHoraNotifica(String horaNotifica) {
        this.horaNotifica = horaNotifica;
    }

    public void setHoraConclus(String horaConclus) {
        this.horaConclus = horaConclus;
    }
    

   public void setNumEmpleado(String numEmpleado) {
        this.numEmpleado = numEmpleado;
   }

   public String getNumEmpleado() {
        return numEmpleado;
   }
        
    public void insertIjTiJuridico(Connection con) throws  Exception {
        ResultSet rsQuery  = null;
        Statement statement  = null;
        StringBuffer cadenaSql   = null;

      try  {
          getNext(con);
          getNumeroIj(con);

          cadenaSql = new StringBuffer();
          cadenaSql.append("INSERT INTO IJ_TI_JURIDICO (idjuridico,numjuridico,fecsuscripcion,otraubica,otraejecutora,fecvigini,fecvigfin,otraconclusion,descobjeto, \n");
          cadenaSql.append(" compromiso, prodserv,compcontra,compambos,comentario,progtrabajo,motivopt,fecregistro,horaregistro,nomarch,ruta, \n");
          cadenaSql.append(" fecnotifica,feclibera,fecconclusion,motivocon, numEmpleado ,anio, idconclusion,idtipoobjetivo,iddregional,iddregional1, idinstjurid, estatus)  \n");
          cadenaSql.append(" values ( \n");
          cadenaSql.append(this.idJuridico +",'"+this.numJuridico+"', to_date('"+this.fecSuscripcion+"','dd/mm/yyyy'), "+this.otraUbica+","+this.otraEjecutora+",");
          cadenaSql.append("'"+this.fecVigIni+"',");
          cadenaSql.append(this.fecVigFin+",");
          cadenaSql.append(this.otraConclusion+",'"+this.descObjeto+"','"+this.compromiso+"','"+this.prodServ+"','"+this.compContra+"','"+this.compAmbos+"','"+this.comentario+"',");
          cadenaSql.append("'"+this.progTrabajo+"','"+this.motivoPt+"', sysdate,to_char(sysdate,'HH24:MI'),'"+this.nomArch+"','"+this.ruta+"',");
          
          if(this.fecConclusion != null)
            this.fecConclusion = "to_date('"+this.fecConclusion+"','dd/MM/yyyy HH24:MI:SS')"; 
          cadenaSql.append(this.fecNotifica + "," + this.fecLibera + "," + this.fecConclusion + ",");
          cadenaSql.append("'"+this.motivoCon+"',"+this.numEmpleado+ ",'"+this.anio+"',");
          
          cadenaSql.append(this.idConclusion+","+this.idTipoObjetivo+","+this.idDRegional+","+this.idDRegional1+"," + this.idInstJurid +",'"+this.estatus+"'");
          cadenaSql.append(")");
          
          statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          statement.executeUpdate(cadenaSql.toString());
      } 
      catch (Exception ex)  {
          System.out.println("Error en el metodo IjTiJuridico.insertIjTiJuridico"+ex.getMessage());
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
    }//insertIjTiJuridico
     
    public void updateArchivo(Connection con)throws SQLException, Exception{
        Statement stQuery=null;
        try{
         stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
         StringBuffer SQL=new StringBuffer(" ");
         SQL.append("UPDATE ij_ti_juridico juridico set nomARch='"+this.nomArch+"', ruta ='"+this.ruta+"' where juridico.idjuridico ="+this.idJuridico+" \n");
         
         int rs=-1;
         rs=stQuery.executeUpdate(SQL.toString());
         
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

    public void select(Connection con) throws SQLException, Exception{
            StringBuffer SQL;
            SQL = new StringBuffer();
              SQL.append("Select estatus, ijTiJuridico.Idjuridico, ijTiJuridico.Numjuridico, to_char(ijTiJuridico.Fecsuscripcion,'dd/MM/yyyy') Fecsuscripcion, ijTiJuridico.Otraubica, ijTiJuridico.Otraejecutora, \n");
              SQL.append("       to_char(ijTiJuridico.Fecvigini,'dd/MM/yyyy') as Fecvigini, to_char(ijTiJuridico.Fecvigfin,'dd/MM/yyyy') Fecvigfin, ijTiJuridico.Otraconclusion, ijTiJuridico.Descobjeto,\n");
              SQL.append("       ijTiJuridico.Descobjeto, ijTiJuridico.Compromiso, ijTiJuridico.Prodserv, ijTiJuridico.Compcontra, ijTiJuridico.Compambos,  \n");
              SQL.append("       ijTiJuridico.Comentario, ijTiJuridico.Progtrabajo, ijTiJuridico.Motivopt, to_char(ijTiJuridico.Fecregistro,'dd/MM/yyyy') as Fecregistro, ijTiJuridico.Horaregistro,  \n");
              SQL.append("       ijTiJuridico.Nomarch, ijTiJuridico.Ruta, to_char(ijTiJuridico.Fecnotifica,'dd/MM/yyyy') Fecnotifica, to_char(ijTiJuridico.Feclibera,'dd/MM/yyyy') as Feclibera, to_char(ijTiJuridico.Fecconclusion,'dd/MM/yyyy') Fecconclusion,  \n");
              SQL.append("       to_char(ijTiJuridico.Fecnotifica,'HH24:MI:SS') horaNotifica,  \n");
              SQL.append("       to_char(ijTiJuridico.Feclibera,'HH24:MI:SS') as horaLibera, to_char(ijTiJuridico.Fecconclusion,'HH24:MI:SS') horaConclu,  \n");
              SQL.append("       ijTiJuridico.Motivocon, ijTiJuridico.numEmpleado, ijTiJuridico.Anio, ijTiJuridico.Idconclusion,  \n");
              SQL.append("       ijTiJuridico.Idtipoobjetivo, ijTiJuridico.Iddregional, ijTiJuridico.Iddregional1, ijTiJuridico.Idinstjurid  \n");
              SQL.append(" From Ij_Ti_Juridico ijTiJuridico  \n");
              SQL.append(" where ijTiJuridico.Idjuridico = "+this.idJuridico+" \n");
              
            Statement stQuery = null;
            ResultSet rsQuery = null;
            int resultado = 0;
            try {
              stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
              rsQuery = stQuery.executeQuery(SQL.toString());
              
              rsQuery.beforeFirst();
              if (rsQuery.next() )  {
                 this.anio = rsQuery.getString("ANIO");
                 this.comentario = (rsQuery.getString("COMENTARIO")==null?"":rsQuery.getString("COMENTARIO"));
                 this.compAmbos   = (rsQuery.getString("COMPAMBOS")==null?"":rsQuery.getString("COMPAMBOS"));
                 this.compContra  = (rsQuery.getString("COMPCONTRA")==null?"":rsQuery.getString("COMPCONTRA"));
                 this.compromiso  = (rsQuery.getString("COMPROMISO")==null?"":rsQuery.getString("COMPROMISO"));
                 this.descObjeto  = (rsQuery.getString("DESCOBJETO")==null?"":rsQuery.getString("DESCOBJETO"));
                 this.fecConclusion  = (rsQuery.getString("FECCONCLUSION")==null?"":rsQuery.getString("FECCONCLUSION"));
                 this.fecLibera  = (rsQuery.getString("FECLIBERA")==null?"":rsQuery.getString("FECLIBERA"));
                 this.fecNotifica = (rsQuery.getString("FECNOTIFICA")==null?"":rsQuery.getString("FECNOTIFICA"));
                 this.fecRegistro = (rsQuery.getString("FECREGISTRO")==null?"":rsQuery.getString("FECREGISTRO"));
                 this.fecSuscripcion = rsQuery.getString("FECSUSCRIPCION");
                 this.fecVigFin = (rsQuery.getString("FECVIGFIN")==null?"":rsQuery.getString("FECVIGFIN"));
                 this.fecVigIni = rsQuery.getString("FECVIGINI");
                 this.horaRegistro = (rsQuery.getString("HORAREGISTRO")==null?"":rsQuery.getString("HORAREGISTRO"));
                 this.idConclusion  = rsQuery.getString("IDCONCLUSION");
                 this.idDRegional   = rsQuery.getString("Iddregional");
                 this.idDRegional1  = rsQuery.getString("Iddregional1");
                 this.idInstJurid   = rsQuery.getString("IDINSTJURID");
                 this.idJuridico    = rsQuery.getString("IDJURIDICO");
                 this.idTipoObjetivo = rsQuery.getString("Idtipoobjetivo");
                 this.motivoCon      = (rsQuery.getString("MOTIVOCON")==null?"":rsQuery.getString("MOTIVOCON"));
                 this.motivoPt       = (rsQuery.getString("MOTIVOPT")==null?"":rsQuery.getString("MOTIVOPT"));
                 this.nomArch     = rsQuery.getString("NOMARCH");
                 this.numJuridico    = rsQuery.getString("NUMJURIDICO");
                 this.otraConclusion = (rsQuery.getString("OTRACONCLUSION")==null?"":rsQuery.getString("OTRACONCLUSION"));
                 this.otraEjecutora = (rsQuery.getString("OTRAEJECUTORA")==null?"":rsQuery.getString("OTRAEJECUTORA"));
                 this.otraUbica     = (rsQuery.getString("OTRAUBICA")==null?"":rsQuery.getString("OTRAUBICA"));
                 this.prodServ      = rsQuery.getString("PRODSERV");
                 this.progTrabajo =  rsQuery.getString("PROGTRABAJO");
                 this.numEmpleado = (rsQuery.getString("NUMEMPLEADO")==null?"":rsQuery.getString("NUMEMPLEADO"));
                 this.ruta        = rsQuery.getString("RUTA");
                 this.horaConclus = (rsQuery.getString("horaConclu")==null?"":rsQuery.getString("horaConclu"));
                 this.horaLibera  = (rsQuery.getString("horaLibera")==null?"":rsQuery.getString("horaLibera"));
                 this.horaNotifica = (rsQuery.getString("horaNotifica")==null?"":rsQuery.getString("horaNotifica"));
                 this.estatus     = rsQuery.getString("estatus");
              }
            }
            catch (Exception e) {
              System.out.println("Ha ocurrido un error en el metodo IjTiJuridico.select");
              System.out.println("IjTiJuridico.select: " + SQL.toString());
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
          
          
    public void update(Connection con)throws SQLException, Exception{
       Statement stQuery=null;
        StringBuffer SQL = new StringBuffer();
       try{
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        
        SQL.append(" UPDATE IJ_TI_JURIDICO SET fecsuscripcion = '"+this.fecSuscripcion+"', otraubica = "+this.otraUbica+", otraejecutora = "+this.otraEjecutora+",  \n");
        SQL.append(" fecvigini = '"+this.fecVigIni+"', fecvigfin = "+this.fecVigFin+", otraconclusion = "+this.otraConclusion+", descobjeto = '"+this.descObjeto+"',  \n");
        SQL.append(" compromiso = '"+this.compromiso+"', prodserv = '"+this.prodServ+"', compcontra = '"+this.compContra+"', compambos = '"+this.compAmbos+"',  \n");
        SQL.append(" comentario = '"+this.comentario+"', progtrabajo = '"+this.progTrabajo+"', motivopt = '"+this.motivoCon+"',fecregistro = sysdate,  \n");
        SQL.append(" horaregistro = to_char(sysdate,'HH24:MI'),");
        
          if(this.fecNotifica != null && !this.fecNotifica.equals(""))
            this.fecNotifica = "to_date('"+this.fecNotifica+" "+this.horaNotifica+" ','dd/MM/yyyy HH24:MI:SS')";
          else
              this.fecNotifica = null;
              
          if(this.fecLibera != null && !this.fecLibera.equals(""))
            this.fecLibera = "to_date('"+this.fecLibera+" "+this.horaLibera+" ','dd/MM/yyyy HH24:MI:SS')";
         else
             this.fecLibera = null;
             
          if(this.fecConclusion != null && !this.fecConclusion.equals(""))
            this.fecConclusion = "to_date('"+this.fecConclusion +" " + this.horaConclus +"','dd/MM/yyyy HH24:MI:SS')"; 
          else
              this.fecConclusion = null;
              
        SQL.append("fecnotifica = "+this.fecNotifica+", feclibera = "+this.fecLibera+",fecconclusion = "+this.fecConclusion+", motivocon = '"+this.motivoCon+"',  \n");
        SQL.append(" numEmpleado = "+this.numEmpleado+", anio = '"+this.anio+"', idconclusion = "+this.idConclusion+", idtipoobjetivo = "+this.idTipoObjetivo+",  \n");
        SQL.append(" iddregional = "+this.idDRegional+", iddregional1 = "+this.idDRegional1+", estatus = '"+this.estatus+"'  \n");
        SQL.append(" Where IJ_TI_JURIDICO.Numjuridico = '"+this.numJuridico+"'  \n");
        
        int rs=-1;
        rs=stQuery.executeUpdate(SQL.toString());
      } //Fin try
      catch(Exception e){
        System.out.println("Ocurrio un error al accesar al metodo IjTiJuridico.update "+e.getMessage());
        System.out.println("query "+SQL.toString());
        throw e;
      } //Fin catch
      finally{
        if (stQuery!=null){
          stQuery.close();
          stQuery=null;
        }
      } //Fin finally
    } //Fin metodo 
          
    private void getNext(Connection con) throws  Exception {
      Statement stQuery=null;
      StringBuffer SQL = null;
      ResultSet rsQuerySeq = null;
      try {
        stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQL = new StringBuffer("select MAX(t.idjuridico) + 1 AS valoractual from ij_ti_juridico t");
        rsQuerySeq = stQuery.executeQuery(SQL.toString());
        while (rsQuerySeq.next()){
          this.idJuridico = rsQuerySeq.getString("valoractual");
        } //del while
        if(this.idJuridico == null)
          this.idJuridico = "1";

      }
      catch(Exception e) {
          throw new Exception(SQL.toString(),e);
      }
      finally {
        SQL.setLength(0);
        if(rsQuerySeq != null)
          rsQuerySeq.close();
        rsQuerySeq = null;
        if(stQuery != null)
          stQuery.close();
        stQuery = null;
        
      }
    }          

    private void getNumeroIj(Connection con) throws  Exception {
      Statement stQuery=null;
      StringBuffer SQL = null;
      ResultSet rsQuerySeq = null;
      try {
        bcIjTcInstjuridico cInstrumentos = new bcIjTcInstjuridico();
        cInstrumentos.setIdinstjuridico(this.idInstJurid);
        String siblasIj = cInstrumentos.selectSiglasIj(con);
        
        stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQL = new StringBuffer();
        SQL.append("select Max(Ij_Ti_Secuencia.Siguiente +1) numIj from Ij_Ti_Secuencia \n");
        SQL.append(" where Ij_Ti_Secuencia.Anio = '"+this.anio+"' and Ij_Ti_Secuencia.Idinstjurid = "+this.idInstJurid);
         
        rsQuerySeq = stQuery.executeQuery(SQL.toString());
        if(rsQuerySeq.next())
          this.numJuridico = (rsQuerySeq.getString("numIj")==null?siblasIj.trim()+this.anio.trim()+"0001":siblasIj.trim() + rsQuerySeq.getString("numIj"));
        else
         this.numJuridico = siblasIj.trim() + this.anio.trim() + "0001";
      }
      catch(Exception e) {
          throw new Exception(SQL.toString(),e);
      }
      finally {
        SQL.setLength(0);
        if(rsQuerySeq != null)
          rsQuerySeq.close();
        rsQuerySeq = null;
        if(stQuery != null)
          stQuery.close();
        stQuery = null;
        
      }
    }

}
