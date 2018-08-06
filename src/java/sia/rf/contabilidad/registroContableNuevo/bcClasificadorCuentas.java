package sia.rf.contabilidad.registroContableNuevo;

import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sun.jdbc.rowset.*;

public class bcClasificadorCuentas {
   // public bcClasificadorCuentas() {
   // }

       private String cuenta_mayor_id; 
       private String cuenta_mayor; 
       private String conf_cve_mayor_id; 
       private String conf_cve_cta_cont_id; 
       private String descripcion; 
       private String naturaleza; 
       private String fecha_vig_ini; 
       private String fecha_vig_fin; 
       private String id_clase; 
       private String id_grupo; 
       private String id_genero; 
       private String fecha_registro;
       
       // private String grupo; 
        //Consecutivo 
     private String numCons;  
     private String numConsecutivo;
      // var insertadas por Jusaji el 16/12/2010, para rf_tr_configura_claves y rf_tr_detalle_conf_cve
       private String id_conf_cve;
       private String longitud;
       private String ajuste;
       private String caracter;
       private String nivel_operacion;
       private String orden;
       private String tamanio;
       private String posicion;
       private String agrupar;
       private String nivel_cta_id;
       private String codigo;
      
    /** Creates a new instance of bcClasificadorCuentas */    
     public bcClasificadorCuentas() {
         inicializa();        
     }    
           
       /** 
       * cuenta_mayor_id 
       * @return cuenta_mayor_id 
       */ 
       public String getCuenta_mayor_id() { 
          return cuenta_mayor_id; 
       } 
      
       /** 
       * cuenta_mayor_id 
       * @param cuenta_mayor_id 
       */ 
       public void setCuenta_mayor_id( String cuenta_mayor_id ) { 
          this.cuenta_mayor_id=cuenta_mayor_id; 
       } 
      
       /** 
       * cuenta_mayor 
       * @return cuenta_mayor 
       */ 
       public String getCuenta_mayor() { 
          return cuenta_mayor; 
       } 
      
       /** 
       * cuenta_mayor 
       * @param cuenta_mayor 
       */ 
       public void setCuenta_mayor( String cuenta_mayor ) { 
          this.cuenta_mayor=cuenta_mayor; 
       } 
      
       /** 
       * conf_cve_mayor_id 
       * @return conf_cve_mayor_id 
       */ 
       public String getConf_cve_mayor_id() { 
          return conf_cve_mayor_id; 
       } 
      
       /** 
       * conf_cve_mayor_id 
       * @param conf_cve_mayor_id 
       */ 
       public void setConf_cve_mayor_id( String conf_cve_mayor_id ) { 
          this.conf_cve_mayor_id=conf_cve_mayor_id; 
       } 
      
       /** 
       * conf_cve_cta_cont_id 
       * @return conf_cve_cta_cont_id 
       */ 
       public String getConf_cve_cta_cont_id() { 
          return conf_cve_cta_cont_id; 
       } 
      
       /** 
       * conf_cve_cta_cont_id 
       * @param conf_cve_cta_cont_id 
       */ 
       public void setConf_cve_cta_cont_id( String conf_cve_cta_cont_id ) { 
          this.conf_cve_cta_cont_id=conf_cve_cta_cont_id; 
       } 
      
       /** 
       * descripcion 
       * @return descripcion 
       */ 
       public String getDescripcion() { 
          return descripcion; 
       } 
      
       /** 
       * descripcion 
       * @param descripcion 
       */ 
       public void setDescripcion( String descripcion ) { 
          this.descripcion=descripcion; 
       } 
      
       /** 
       * naturaleza 
       * @return naturaleza 
       */ 
       public String getNaturaleza() { 
          return naturaleza; 
       } 
      
       /** 
       * naturaleza 
       * @param naturaleza 
       */ 
       public void setNaturaleza( String naturaleza ) { 
          this.naturaleza=naturaleza; 
       } 
      
       /** 
       * fecha_vig_ini 
       * @return fecha_vig_ini 
       */ 
       public String getFecha_vig_ini() { 
          return fecha_vig_ini; 
       } 
      
       /** 
       * fecha_vig_ini 
       * @param fecha_vig_ini 
       */ 
       public void setFecha_vig_ini( String fecha_vig_ini ) { 
          this.fecha_vig_ini=fecha_vig_ini; 
       } 
      
       /** 
       * fecha_vig_fin 
       * @return fecha_vig_fin 
       */ 
       public String getFecha_vig_fin() { 
          return fecha_vig_fin; 
       } 
      
       /** 
       * fecha_vig_fin 
       * @param fecha_vig_fin 
       */ 
       public void setFecha_vig_fin( String fecha_vig_fin ) { 
          this.fecha_vig_fin=fecha_vig_fin; 
       } 
      
       /** 
       * id_clase 
       * @return id_clase 
       */ 
       public String getId_clase() { 
          return id_clase; 
       } 
      
       /** 
       * id_clase 
       * @param id_clase 
       */ 
       public void setId_clase( String id_clase ) { 
          this.id_clase=id_clase; 
       } 
      
       /** 
       * id_grupo 
       * @return id_grupo 
       */ 
       public String getId_grupo() { 
          return id_grupo; 
       } 
      
       /** 
       * id_grupo 
       * @param id_grupo 
       */ 
       public void setId_grupo( String id_grupo ) { 
          this.id_grupo=id_grupo; 
       } 
      
       /** 
       * id_genero 
       * @return id_genero 
       */ 
       public String getId_genero() { 
          return id_genero; 
       } 
      
       /** 
       * id_genero 
       * @param id_genero 
       */ 
       public void setId_genero( String id_genero ) { 
          this.id_genero=id_genero; 
       } 

       // Metodos set y get insertados por Jusaji el 16/12/2010, para rf_tr_configura_claves y rf_tr_detalle_conf_cve
         /** 
         * id_conf_cve 
         * @return id_conf_cve 
         */ 
         public String getId_conf_cve() { 
            return id_conf_cve; 
         } 
         
         /** 
         * id_conf_cve 
         * @param id_conf_cve 
         */ 
         public void setId_conf_cve( String id_conf_cve ) { 
            this.id_conf_cve=id_conf_cve; 
         } 

        /** 
        * longitud 
        * @return longitud 
        */ 
        public String getLongitud() { 
           return longitud; 
        } 
        
        /** 
        * longitud 
        * @param longitud 
        */ 
        public void setLongitud( String longitud ) { 
           this.longitud=longitud; 
        } 

        /** 
        * ajuste 
        * @return ajuste 
        */ 
        public String getAjuste() { 
           return ajuste; 
        } 
        
        /** 
        * ajuste 
        * @param ajuste 
        */ 
        public void setAjuste( String ajuste ) { 
           this.ajuste=ajuste; 
        } 

       /** 
       * caracter 
       * @return caracter 
       */ 
       public String getCaracter() { 
           return caracter; 
       } 
                
       /** 
       * caracter 
       * @param caracter 
       */ 
       public void setCaracter( String caracter ) { 
           this.caracter=caracter; 
       } 

        /** 
        * nivel_operacion 
        * @return nivel_operacion 
        */ 
        public String getNivel_operacion() { 
            return nivel_operacion; 
        } 
                 
        /** 
        * nivel_operacion 
        * @param nivel_operacion 
        */ 
        public void setNivel_operacion( String nivel_operacion ) { 
            this.nivel_operacion=nivel_operacion; 
        } 
        
        /** 
        * orden 
        * @return orden 
        */ 
        public String getOrden() { 
            return orden; 
        } 
                 
        /** 
        * orden 
        * @param orden 
        */ 
        public void setOrden( String orden ) { 
            this.orden=orden; 
        } 
        
        /** 
        * tamanio 
        * @return tamanio 
        */ 
        public String getTamanio() { 
            return tamanio; 
        } 
                 
        /** 
        * tamanio 
        * @param tamanio 
        */ 
        public void setTamanio( String tamanio ) { 
            this.tamanio=tamanio; 
        } 

        /** 
        * posicion 
        * @return posicion 
        */ 
        public String getPosicion() { 
            return posicion; 
        } 
                 
        /** 
        * posicion 
        * @param posicion 
        */ 
        public void setPosicion( String posicion ) { 
            this.posicion=posicion; 
        } 
      
        /** 
        * agrupar 
        * @return agrupar 
        */ 
        public String getAgrupar() { 
            return agrupar; 
        } 
                 
        /** 
        * agrupar 
        * @param agrupar 
        */ 
        public void setAgrupar( String agrupar ) { 
            this.agrupar=agrupar; 
        } 

        /** 
        * nivel_cta_id 
        * @return nivel_cta_id 
        */ 
        public String getNivel_cta_id() { 
            return nivel_cta_id; 
        } 
                 
        /** 
        * nivel_cta_id 
        * @param nivel_cta_id 
        */ 
        public void setNivel_cta_id( String nivel_cta_id ) { 
            this.nivel_cta_id=nivel_cta_id; 
        } 

        /** 
        * codigo 
        * @return codigo 
        */ 
        public String getCodigo() { 
            return codigo; 
        } 
                 
        /** 
        * codigo 
        * @param codigo 
        */ 
        public void setCodigo( String codigo ) { 
            this.codigo=codigo; 
        } 
      /**
            * Fecha de Registro de la cuenta
            * @return fecha_registro Fecha de Registro
           */
         public String getFecha_registro() { 
            return fecha_registro; 
         } 
         
          /**
            * Consecutivo fecha_registro de la cuenta
            * @param  fecha_registro Registro de la fecha 
            */
         public void setFecha_registro( String fecha_registro ) { 
            this.fecha_registro=fecha_registro; 
         } 
         
         /**
           * Número Consecutivo de la cuenta
           * @return numCons Consecutivo de Cuenta
          */
           public String getNumCons() {
                  numCons=(numCons==null)?"":numCons;
                  return numCons;
              }                         
                      
           /**
             * Consecutivo numCons de la cuenta
             * @param  numCons Consecutivo cuenta 
             */
             public void setNumCons(String numCons) {
                      this.numCons = numCons;
              }      
              
        /** 
        * numConsecutivo 
        * @return numConsecutivo 
        */ 
        public String getNumconsecutivo() { 
            return numConsecutivo; 
        } 
                 
        /** 
        * numConsecutivo 
        * @param numConsecutivo 
        */ 
        public void setNumConsecutivo( String numConsecutivo ) { 
            this.numConsecutivo=numConsecutivo; 
        } 

        /** 
        * grupo 
        * @return grupo 
        */ 
      //  public String getGrupo() { 
           // return grupo; 
       // } 
                 
        /** 
        * grupo 
        * @param grupo 
        */ 
        //public void setGrupo( String grupo ) { 
          //  this.grupo=grupo; 
       // } 
         public void inicializa() {
            id_genero="";
            id_grupo="";
            id_clase="";
            descripcion="";
            cuenta_mayor_id="";
            cuenta_mayor  ="";
            naturaleza  ="";
            fecha_vig_ini  ="";
            fecha_vig_fin  ="";
            fecha_registro = "";
            numCons = "";
            numConsecutivo="";
          //  grupo="";
         }    


       /** 
       * Metodo que lee la informacion de RF_TC_CLASIFICADOR_CUENTAS 
       * Fecha de creacion: 
       * Autor: 
       * Fecha de modificacion: 17/12/2010
       * Modificado por: Jusaji
       */ 
       public void select_RF_TC_CLASIFICADOR_CUENTAS(Connection con, String pCuentaMayor)throws SQLException, Exception{ 
          Statement stQuery=null; 
          ResultSet rsQuery=null; 
          try{ 
           stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
           StringBuffer SQL=new StringBuffer("SELECT a.cuenta_mayor_id,a.cuenta_mayor,a.conf_cve_mayor_id,a.conf_cve_cta_cont_id,a.descripcion,a.naturaleza, ");
           SQL.append(" to_char(a.fecha_vig_ini, 'DD/MM/YYYY') as fecha_vig_ini,to_char(a.fecha_vig_fin, 'DD/MM/YYYY') as fecha_vig_fin,a.id_clase,a.id_grupo,a.id_genero");  
           SQL.append(" FROM RF_TC_CLASIFICADOR_CUENTAS a "); 
           SQL.append(" WHERE a.cuenta_mayor='").append(pCuentaMayor).append("'"); 
           //System.out.println(SQL.toString()); 
           rsQuery=stQuery.executeQuery(SQL.toString()); 
           while (rsQuery.next()){ 
              cuenta_mayor_id=(rsQuery.getString("cuenta_mayor_id")==null) ? "" : rsQuery.getString("cuenta_mayor_id"); 
              cuenta_mayor=(rsQuery.getString("cuenta_mayor")==null) ? "" : rsQuery.getString("cuenta_mayor"); 
              conf_cve_mayor_id=(rsQuery.getString("conf_cve_mayor_id")==null) ? "" : rsQuery.getString("conf_cve_mayor_id"); 
              conf_cve_cta_cont_id=(rsQuery.getString("conf_cve_cta_cont_id")==null) ? "" : rsQuery.getString("conf_cve_cta_cont_id"); 
              descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
              naturaleza=(rsQuery.getString("naturaleza")==null) ? "" : rsQuery.getString("naturaleza"); 
              fecha_vig_ini=(rsQuery.getString("fecha_vig_ini")==null) ? "" : rsQuery.getString("fecha_vig_ini"); 
              fecha_vig_fin=(rsQuery.getString("fecha_vig_fin")==null) ? "" : rsQuery.getString("fecha_vig_fin"); 
              id_clase=(rsQuery.getString("id_clase")==null) ? "" : rsQuery.getString("id_clase"); 
              id_grupo=(rsQuery.getString("id_grupo")==null) ? "" : rsQuery.getString("id_grupo"); 
              id_genero=(rsQuery.getString("id_genero")==null) ? "" : rsQuery.getString("id_genero"); 
           } // Fin while 
         } //Fin try 
         catch(Exception e){ 
           System.out.println("Ocurrio un error al accesar al metodo select_RF_TC_CLASIFICADOR_CUENTAS "+e.getMessage()); 
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
       } //Fin metodo select_RF_TC_CLASIFICADOR_CUENTAS 
      
      
        /** 
        * Metodo que lee la informacion de RF_TC_CLASIFICADOR_CUENTAS para obtener cuenta de mayor
        * Fecha de creacion: 
        * Autor: 
        * Fecha de modificacion: 
        * Modificado por: 
        */ 
        public String select_RF_TC_CLASIFICADOR_CUENTAS_mayor(Connection con, String pCuentaMayor)throws SQLException, Exception{ 
           Statement stQuery=null; 
           ResultSet rsQuery=null; 
           String lsResul="";
           try{ 
            stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
            StringBuffer SQL=new StringBuffer("SELECT a.cuenta_mayor_id,a.cuenta_mayor,a.conf_cve_mayor_id,a.conf_cve_cta_cont_id,a.descripcion,a.naturaleza,a.fecha_vig_ini,a.fecha_vig_fin,a.id_clase,a.id_grupo,a.id_genero");  
            SQL.append(" FROM RF_TC_CLASIFICADOR_CUENTAS a "); 
            SQL.append(" WHERE a.cuenta_mayor='").append(pCuentaMayor).append("'"); 
            //System.out.println(SQL.toString()); 
            rsQuery=stQuery.executeQuery(SQL.toString()); 
            while (rsQuery.next()){ 
               cuenta_mayor_id=(rsQuery.getString("cuenta_mayor_id")==null) ? "" : rsQuery.getString("cuenta_mayor_id"); 
               cuenta_mayor=(rsQuery.getString("cuenta_mayor")==null) ? "" : rsQuery.getString("cuenta_mayor"); 
               conf_cve_mayor_id=(rsQuery.getString("conf_cve_mayor_id")==null) ? "" : rsQuery.getString("conf_cve_mayor_id"); 
               conf_cve_cta_cont_id=(rsQuery.getString("conf_cve_cta_cont_id")==null) ? "" : rsQuery.getString("conf_cve_cta_cont_id"); 
               descripcion=(rsQuery.getString("descripcion")==null) ? "" : rsQuery.getString("descripcion"); 
               naturaleza=(rsQuery.getString("naturaleza")==null) ? "" : rsQuery.getString("naturaleza"); 
               fecha_vig_ini=(rsQuery.getString("fecha_vig_ini")==null) ? "" : rsQuery.getString("fecha_vig_ini"); 
               fecha_vig_fin=(rsQuery.getString("fecha_vig_fin")==null) ? "" : rsQuery.getString("fecha_vig_fin"); 
               id_clase=(rsQuery.getString("id_clase")==null) ? "" : rsQuery.getString("id_clase"); 
               id_grupo=(rsQuery.getString("id_grupo")==null) ? "" : rsQuery.getString("id_grupo"); 
               id_genero=(rsQuery.getString("id_genero")==null) ? "" : rsQuery.getString("id_genero"); 
               lsResul=pCuentaMayor;
            } // Fin while 
          } //Fin try 
          catch(Exception e){ 
            System.out.println("Ocurrio un error al accesar al metodo select_RF_TC_CLASIFICADOR_CUENTAS_mayor "+e.getMessage()); 
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
          return lsResul;
        } //Fin metodo select_RF_TC_CLASIFICADOR_CUENTAS_mayor       
      
       /** 
       * Metodo que inserta la informacion de RF_TC_CLASIFICADOR_CUENTAS 
       * Fecha de creacion: 
       * Autor: 
       * Fecha de modificacion: 05/01/2011
       * Modificado por: ROSARIO ALCARAZ MTZ
       */ 
       public void insert_RF_TC_CLASIFICADOR_CUENTAS(Connection con)throws SQLException, Exception{ 
          Statement stQuery=null; 
          try{ 
           stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
           StringBuffer SQL=new StringBuffer("INSERT INTO RF_TC_CLASIFICADOR_CUENTAS( cuenta_mayor,conf_cve_mayor_id,conf_cve_cta_cont_id,descripcion,naturaleza,fecha_vig_ini,fecha_vig_fin,");  
           //StringBuffer SQL=new StringBuffer("INSERT INTO RF_TC_CLASIFICADOR_CUENTAS( cuenta_mayor,conf_cve_mayor_id,descripcion,naturaleza,fecha_vig_ini,fecha_vig_fin,");  
           SQL.append("id_clase,id_grupo,id_genero,fecha_registro) values ('" );
          // SQL.append(cuenta_mayor_id).append(","); 
          // SQL.append("'").append(cuenta_mayor).append("',"); 
           SQL.append(cuenta_mayor).append("',"); 
           SQL.append(conf_cve_mayor_id).append(","); 
           SQL.append(conf_cve_cta_cont_id).append(","); 
           SQL.append("'").append(descripcion).append("',"); 
           SQL.append("'").append(naturaleza).append("',"); 
           SQL.append("to_date('").append(fecha_vig_ini).append("','dd/mm/yyyy'),"); 
           SQL.append("to_date('").append(fecha_vig_fin).append("','dd/mm/yyyy'),"); 
           SQL.append(id_clase).append(","); 
           SQL.append(id_grupo).append(","); 
           SQL.append(id_genero).append(",");
           SQL.append("SYSDATE)"); //Fecha de registro
           
           //SQL.append(fecha_registro).append("')"); 
           //System.out.println(SQL.toString()); 
           int rs=-1; 
           rs=stQuery.executeUpdate(SQL.toString()); 
         } //Fin try 
         catch(Exception e){ 
           System.out.println("Ocurrio un error al accesar al metodo insert_RF_TC_CLASIFICADOR_CUENTAS "+e.getMessage()); 
           throw e; 
         } //Fin catch 
         finally{ 
           if (stQuery!=null){ 
             stQuery.close(); 
             stQuery=null; 
           } 
         } //Fin finally 
       } //Fin metodo insert_RF_TC_CLASIFICADOR_CUENTAS 
      
       /** 
       * Metodo que modifica la informacion de RF_TC_CLASIFICADOR_CUENTAS 
       * Fecha de creacion: 
       * Autor: 
       * Fecha de modificacion: 20-ene-2011
       * Modificado por: ROSARIO ALCARAZ
       */ 
       public void update_RF_TC_CLASIFICADOR_CUENTAS(Connection con, String clave)throws SQLException, Exception{ 
          Statement stQuery=null; 
          try{ 
           stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
           StringBuffer SQL=new StringBuffer("UPDATE RF_TC_CLASIFICADOR_CUENTAS"); 
           SQL.append(" SET descripcion=").append("'").append(descripcion).append("',"); 
           SQL.append("naturaleza=").append("'").append(naturaleza).append("',"); 
           SQL.append("fecha_vig_ini=to_date('").append(fecha_vig_ini).append("','dd/mm/yyyy'),"); 
           SQL.append("fecha_vig_fin=to_date('").append(fecha_vig_fin).append("','dd/mm/yyyy')"); 
           SQL.append(" WHERE cuenta_mayor='").append(clave).append("'"); 
           //System.out.println(SQL.toString()); 
           int rs=-1; 
           rs=stQuery.executeUpdate(SQL.toString()); 
         } //Fin try  
         catch(Exception e){ 
           System.out.println("Ocurrio un error al accesar al metodo update_RF_TC_CLASIFICADOR_CUENTAS "+e.getMessage()); 
           throw e; 
         } //Fin catch 
         finally{ 
           if (stQuery!=null){ 
             stQuery.close(); 
             stQuery=null; 
           } 
         } //Fin finally 
       } //Fin metodo update_RF_TC_CLASIFICADOR_CUENTAS 
      
       /** 
       * Metodo que borra la informacion de RF_TC_CLASIFICADOR_CUENTAS 
       * Fecha de creacion: 
       * Autor: 
       * Fecha de modificacion: 20-dic-2011
       * Modificado por: ROSARIO ALCARAZ MTZ
       */ 
       public void delete_RF_TC_CLASIFICADOR_CUENTAS(Connection con, String clave)throws SQLException, Exception{ 
          Statement stQuery=null; 
          try{ 
           stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
           StringBuffer SQL=new StringBuffer("DELETE FROM RF_TC_CLASIFICADOR_CUENTAS a "); 
           SQL.append("WHERE cuenta_mayor ='"+clave+"' "); 
           //System.out.println(SQL.toString()); 
           int rs=-1; 
           rs=stQuery.executeUpdate(SQL.toString()); 
         } //Fin try 
         catch(Exception e){ 
           System.out.println("Ocurrio un error al accesar al metodo delete_RF_TC_CLASIFICADOR_CUENTAS "+e.getMessage()); 
           throw e; 
         } //Fin catch 
         finally{ 
           if (stQuery!=null){ 
             stQuery.close(); 
             stQuery=null; 
           } 
         } //Fin finally 
       } //Fin metodo delete_RF_TC_CLASIFICADOR_CUENTAS
       
       
        /**
           * CATALOGO DE GENERO
           * Descripcion: Lista los registros del catalogo RF_TC_GENERO_CLASF_CTA para mostrar en  los combos
           * Fecha de creacion: 15/11/2010
           * @parametro con conexion a la base de datos
           * @Autor: ROSARIO ALCARAZ MARTINEZ
           */
                       
           public CachedRowSet selectGenero(Connection conexion) throws SQLException, Exception {
                StringBuffer SQL;
                SQL = new StringBuffer("SELECT ID_GENERO, DESCRIPCION FROM RF_TC_GENERO_CLASF_CTA order by ID_GENERO");
                CachedRowSet crs = null;     
                try{
                    crs = new CachedRowSet();
                    crs.setCommand(SQL.toString());
                    crs.execute(conexion);
                }
                catch (Exception e){
                       System.out.println("Ha ocurrido un error en el metodo bcClasificadorCuentas.selectGenero ");
                       System.out.println(SQL);
                       crs.close();
                       crs = null;
                       throw e;
                }
                finally {
                    SQL.setLength(0);
                    SQL = null;
                }  
                return crs;
           }//selectGenero
           
            /**
            * CATALOGO DE GRUPO
            * Descripcion: Lista los registros del catalogo RF_TC_GRUPO_CLASF_CTA para mostrar en  los combos
            * Fecha de creacion: 15/11/2010
            * @parametro con conexion a la base de datos
            * @Autor: ROSARIO ALCARAZ MARTINEZ
            */
                        
            public CachedRowSet selectGrupo(Connection con) throws SQLException, Exception {
                 StringBuffer SQL;
                 SQL = new StringBuffer("SELECT ID_GRUPO, DESCRIPCION, ID_GENERO, (ID_GENERO||ID_GRUPO) AS GRUPO  FROM RF_TC_GRUPO_CLASF_CTA order by ID_GENERO, ID_GRUPO");
                 CachedRowSet crs = null;                      
                 try{
                     crs = new CachedRowSet();
                     crs.setCommand(SQL.toString());
                     crs.execute(con);
                 }
                 catch (Exception e){
                        System.out.println("Ha ocurrido un error en el metodo bcClasificadorCuentas.selectGrupo ");
                        System.out.println(SQL);
                        crs.close();
                        crs = null;
                        throw e;
                 }
                 finally {
                     SQL.setLength(0);
                     SQL = null;
                 }  
                 return crs;
            }//selectGrupo
            
             /**
             * CATALOGO DE CLASE
             * Descripcion: Lista los registros del catalogo RF_TC_CLASE_CLASIF_CTA para mostrar en  los combos
             * Fecha de creacion: 16/11/2010
             * @parametro con conexion a la base de datos
             * @Autor: ROSARIO ALCARAZ MARTINEZ
             */
                         
             public CachedRowSet selectClase(Connection con) throws SQLException, Exception {
                  StringBuffer SQL;
                 SQL = new StringBuffer("SELECT GRUPO, ID_GENERO,ID_GRUPO,ID_CLASE, DESCRIPCION FROM ");
                 SQL.append(" (SELECT ID_GENERO,ID_GRUPO,ID_CLASE,DESCRIPCION,(ID_GENERO||ID_GRUPO) AS GRUPO  FROM RF_TC_CLASE_CLASIF_CTA)  ");
                 SQL.append(" ORDER BY ID_GENERO,ID_GRUPO,ID_CLASE  ");
                  CachedRowSet crs = null; 
                  try{
                      crs = new CachedRowSet();
                      crs.setCommand(SQL.toString());
                      crs.execute(con);
                  }
                  catch (Exception e){
                         System.out.println("Ha ocurrido un error en el metodo bcClasificadorCuentas.selectClase ");
                         System.out.println(SQL);
                         crs.close();
                         crs = null;
                         throw e;
                  }
                  finally {
                      SQL.setLength(0);
                      SQL = null;
                  }  
                  return crs;
             }//selectClase
           
              /**
              *Descripcion: Obtiene el ultimo numero de numCons para mostrar consecutivo
              *el nuevo valor consecutivo de cuenta.
              *Fecha de creación: 07/dic/2010
              *Autor: Rosario Alcaraz
              * @param con (tipo de coneccion)
              */
              public void selectMaxCta(Connection con, String lgenero, String lgpo, String lclase) throws SQLException, Exception {     
                 Statement stQuery=null;
                 ResultSet rsQuery= null;   
                 setNumCons("");   
                 try{
                 stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                 StringBuffer SQL = new StringBuffer("select max(cant)as maximo from ");                
                 SQL.append(" (SELECT cuenta_mayor_id, cuenta_mayor,");
                 SQL.append(" SUBSTR(cuenta_mayor,4,1) AS cant   ");
                 SQL.append(" FROM RF_TC_CLASIFICADOR_CUENTAS ");
                 SQL.append(" WHERE (id_genero ='"+lgenero+"') and (id_grupo ='"+lgpo+"') and (id_clase ='"+lclase+"')) paso");                                                          
                rsQuery = stQuery.executeQuery(SQL.toString());                
                 rsQuery.previous();          
                 while (rsQuery.next()){
                    numCons=(rsQuery.getString("maximo")== "") ? "0" : rsQuery.getString("maximo");
                  }                                                 
                 }
                 catch (Exception e){
                       System.out.println("Ha ocurrido un error en el metodo bcClasificadorCuentas.selectMaxCta " + e.getMessage());              
                 }
                 finally {
                      if (rsQuery != null){
                            rsQuery.close();
                            rsQuery=null;
                       }
                       if (stQuery != null){
                             stQuery.close();
                             stQuery=null;
                       }
                  }//finally                                
    }//metodo  

     /**
     * TABLA RF_TC_CLASIFICADOR_CUENTAS
     * Descripcion: Lista los consecutivos del catalogo RF_TC_CLASIFICADOR_CUENTAS
     * Fecha de creacion: 15/12/2010
     * @parametro con conexion a la base de datos
     * @Autor: ROSARIO ALCARAZ MARTINEZ
     */
                 
     public CachedRowSet selectConsecutivo(Connection con, String lgenero, String lgpo, String lclase) throws SQLException, Exception {     
         StringBuffer SQL = new StringBuffer("select cant as conse from ");        
         SQL.append(" (SELECT cuenta_mayor_id, cuenta_mayor,");
         SQL.append(" SUBSTR(cuenta_mayor,4,1) as cant ");
         SQL.append(" FROM RF_TC_CLASIFICADOR_CUENTAS ");
         SQL.append(" WHERE (id_genero ='"+lgenero+"') and (id_grupo ='"+lgpo+"') and (id_clase ='"+lclase+"')) paso ORDER BY cant ");                                         
         CachedRowSet crs = null;     
          try{
              crs = new CachedRowSet();
              crs.setCommand(SQL.toString());
              crs.execute(con);
          }
          catch (Exception e){
                 System.out.println("Ha ocurrido un error en el metodo bcClasificadorCuentas.selectConsecutivo ");
                 System.out.println(SQL);
                 crs.close();
                 crs = null;
                 throw e;
          }
          finally {
              SQL.setLength(0);
              SQL = null;
          }  
          return crs;
     }//selectConsecutivo
     
      /**
      * TABLA RF_TC_CLASIFICADOR_CUENTAS
      * Descripcion: Lista los consecutivos del catalogo RF_TC_CLASIFICADOR_CUENTAS
      * Fecha de creacion: 15/12/2010
      * @parametro con conexion a la base de datos
      * @Autor: ROSARIO ALCARAZ MARTINEZ
      */
                  
      public CachedRowSet selectVerificaCuenta(Connection con, String cveCuenta) throws SQLException, Exception {     
          StringBuffer SQL = new StringBuffer("SELECT cuenta_mayor, descripcion");        
           SQL.append(" FROM RF_TC_CLASIFICADOR_CUENTAS ");
          SQL.append(" WHERE cuenta_mayor ='"+cveCuenta+"' ");                                         
          CachedRowSet crs = null;     
          //System.out.println(SQL);
           try{
               crs = new CachedRowSet();
               crs.setCommand(SQL.toString());
               crs.execute(con);
           }
           catch (Exception e){
                  System.out.println("Ha ocurrido un error en el metodo bcClasificadorCuentas.selectVerificaCuenta ");
                  System.out.println(SQL);
                  crs.close();
                  crs = null;
                  throw e;
           }
           finally {
               SQL.setLength(0);
               SQL = null;
           }  
           return crs;
      }//selectVerificaCuenta
      
       /**
       * TABLA RF_TR_CUENTAS_CONTABLES
       * Descripcion: Lista los consecutivos del catalogo RF_TC_CLASIFICADOR_CUENTAS
       * Fecha de creacion: 04/03/2011
       * @parametro con conexion a la base de datos
       * @Autor: ROSARIO ALCARAZ MARTINEZ
       */
                   
       public CachedRowSet selectVerifica(Connection con, String cveCuenta) throws SQLException, Exception {     
           StringBuffer SQL = new StringBuffer("SELECT cuenta_contable");        
            SQL.append(" FROM RF_TR_CUENTAS_CONTABLES ");
           SQL.append(" WHERE  SUBSTR(cuenta_contable,1,4)='"+cveCuenta+"' ");                                         
           CachedRowSet crs = null;     
           //System.out.println(SQL);
            try{
                crs = new CachedRowSet();
                crs.setCommand(SQL.toString());
                crs.execute(con);
            }
            catch (Exception e){
                   System.out.println("Ha ocurrido un error en el metodo bcClasificadorCuentas. selectVerifica ");
                   System.out.println(SQL);
                   crs.close();
                   crs = null;
                   throw e;
            }
            finally {
                SQL.setLength(0);
                SQL = null;
            }  
            return crs;
       }//selectVerifica


      /**
      * Descripcion: Consulta la tabla RF_TC_CLASIFICADOR_CUENTAS por clave de cuenta
      * Fecha de creacion: 10/12/2010
      * @autor: Rosario Alcaraz
      * @parametro IdCuenta clave de la cuenta de mayor
      */
      public void selectCuenta(Connection con, String IdCuenta) throws SQLException, Exception {
          StringBuffer SQL;
          SQL = new StringBuffer("select a.CUENTA_MAYOR, a.DESCRIPCION, a.NATURALEZA,TO_CHAR(fecha_vig_ini,'DD/MM/YYYY') as FECHA_VIG_INI, ");
          SQL.append("TO_CHAR(a.fecha_vig_fin,'DD/MM/YYYY') as FECHA_VIG_FIN, a.ID_GENERO,a.ID_GRUPO, a.ID_CLASE, ");        
          SQL.append("(SELECT (b.ID_GENERO||' '||b.DESCRIPCION) from RF_TC_GENERO_CLASF_CTA b WHERE a.ID_GENERO=b.ID_GENERO)AS GENERO, ");        
          SQL.append("(SELECT (c.ID_GRUPO||' '||c.DESCRIPCION) from RF_TC_GRUPO_CLASF_CTA c WHERE a.ID_GRUPO=c.ID_GRUPO AND a.ID_GENERO=c.ID_GENERO)AS GRUPO, ");        
          SQL.append("(SELECT (d.ID_CLASE||' '||d.DESCRIPCION) from RF_TC_CLASE_CLASIF_CTA d WHERE a.ID_GRUPO=d.ID_GRUPO AND a.ID_GENERO=d.ID_GENERO AND a.ID_CLASE=d.ID_CLASE)AS CLASE, ");        
          SQL.append(" cuenta_mayor_id  ");
          SQL.append("from RF_TC_CLASIFICADOR_CUENTAS a where cuenta_mayor = "+IdCuenta );
          CachedRowSet crs = null;     
          try{
              crs = new CachedRowSet();
              crs.setCommand(SQL.toString());
              //System.out.println("bcClasificadorCuentas.selectCuenta: " + SQL.toString());
              crs.execute(con);
              if (crs.next()){
                  cuenta_mayor_id = (crs.getString("cuenta_mayor_id")==null)?"":crs.getString("cuenta_mayor_id");
                  cuenta_mayor = (crs.getString("CUENTA_MAYOR")==null)?"":crs.getString("CUENTA_MAYOR");
                  descripcion = (crs.getString("DESCRIPCION")==null)?"":crs.getString("DESCRIPCION");
                  naturaleza = (crs.getString("NATURALEZA")==null)?"":crs.getString("NATURALEZA");
                  fecha_vig_ini = (crs.getString("FECHA_VIG_INI")==null)?"":crs.getString("FECHA_VIG_INI");
                  fecha_vig_fin = (crs.getString("FECHA_VIG_FIN")==null)?"":crs.getString("FECHA_VIG_FIN");
                  id_genero = (crs.getString("ID_GENERO")==null)?"":crs.getString("ID_GENERO");
                  id_grupo = (crs.getString("ID_GRUPO")==null)?"":crs.getString("ID_GRUPO");
                  id_clase = (crs.getString("ID_CLASE")==null)?"":crs.getString("ID_CLASE");                                  
                  id_clase = (crs.getString("ID_CLASE")==null)?"":crs.getString("ID_CLASE");     
              }
              else inicializa();
          }
          catch (Exception e){
                 System.out.println("Ha ocurrido un error en el metodo bcClasificadorCuentas.selectCuenta");
                 crs.close();
                 crs = null;
                 throw e;
          }
          finally {
              SQL.setLength(0);
              SQL = null;
              if (crs != null){
                 crs.close();
                 crs = null;
              }
          }         
      }   
      
    /**
    * CATALOGO DE GRUPO
    * Descripcion: Lista los registros del catalogo RF_TC_GRUPO_CLASF_CTA para mostrar en  los combos
    * Fecha de creacion: 15/11/2010
    * @parametro con conexion a la base de datos
    * @Autor: ROSARIO ALCARAZ MARTINEZ
    */
                
    public CachedRowSet selectGrupoS(Connection con, String lgenero, String lgrupo) throws SQLException, Exception {
         StringBuffer SQL;
         SQL = new StringBuffer("SELECT ID_GRUPO, DESCRIPCION, ID_GENERO FROM RF_TC_GRUPO_CLASF_CTA ");
         SQL.append("WHERE (id_genero ='"+lgenero+"') and (id_grupo ='"+lgrupo+"')");               
         CachedRowSet crs = null;     
         //System.out.println(SQL);
         try{
             crs = new CachedRowSet();
             crs.setCommand(SQL.toString());
             crs.execute(con);
         }
         catch (Exception e){
                System.out.println("Ha ocurrido un error en el metodo bcClasificadorCuentas.selectGrupo ");
                System.out.println(SQL);
                crs.close();
                crs = null;
                throw e;
         }
         finally {
             SQL.setLength(0);
             SQL = null;
         }  
         return crs;
    }//selectGrupo

     /**
     * CATALOGO DE CLASE
     * Descripcion: Lista los registros del catalogo RF_TC_CLASE_CLASF_CTA para mostrar en  los combos
     * Fecha de creacion: 16/11/2010
     * @parametro con conexion a la base de datos
     * @Autor: ROSARIO ALCARAZ MARTINEZ
     */
                 
     public CachedRowSet selectClaseS(Connection con, String lgenero, String lgrupo, String lclase) throws SQLException, Exception {
          StringBuffer SQL;
          SQL = new StringBuffer("SELECT ID_CLASE, ID_GENERO, ID_GRUPO, DESCRIPCION FROM RF_TC_CLASE_CLASIF_CTA ");
          SQL.append("WHERE (id_genero ='"+lgenero+"') and (id_grupo ='"+lgrupo+"') and (id_clase ='"+lclase+"')");   
          CachedRowSet crs = null;    
         
          try{
              crs = new CachedRowSet();
              crs.setCommand(SQL.toString());
              crs.execute(con);
          }
          catch (Exception e){
                 System.out.println("Ha ocurrido un error en el metodo bcClasificador.selectClase ");
                 System.out.println(SQL);
                 crs.close();
                 crs = null;
                 throw e;
          }
          finally {
              SQL.setLength(0);
              SQL = null;
          }  
          return crs;
     }//selectClase   
              
        /** 
        * Metodo que muestra datos de RF_TC_CLASIFICADOR_CUENTAS 
        * Fecha de creacion: 09/12/2010
        * Autor: Elida Pozos Vazquez
        * Fecha de modificacion: 23/09/2011
        * Autor: Elida Pozos Vazquez
        * Se modifican los select para la armonizacion
        */ 
        public ResultSet cargaCuentasMayor(Connection con,int nivRep,String niv1, String niv2, String niv3, String niv4, String niv5, String niv6, String niv7, String niv8,String pCatCuentaId,String pUniEje, String pEntidad, String pAmbito, String pEjercicio, String tipoUsuario) throws SQLException, Exception  {
          Statement stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rsQuery = null;
            String x="";
            StringBuffer SQL = new StringBuffer("");
            String condicion="";
            String  condicionUniEje="";
            String temAmbito="";
            
            if (pEntidad.length()==1)
              temAmbito="00"+pEntidad+pAmbito;
            else  
               temAmbito="0"+pEntidad+pAmbito;
            if (pUniEje.length()==3) 
              pUniEje="0"+pUniEje;
            
            //Cambio agregado por CLHL 05/08/2009 para que para el catálogo de cuentas 2 (Ingresos x ventas - banquito)
            //Le muestre todas las unidades ejecutoras, ya que ellos entrar con la 100 pero pueden agregarle a cualquier unidad.
            if   (!(pCatCuentaId.equals("2") || tipoUsuario.equals("1"))){
                    condicionUniEje=" and substr(cuenta_contable,10,4) = '" + pUniEje +"'";             
                    condicion= " and substr(cuenta_contable,10,8) = '" + pUniEje + temAmbito + "'"; 
            }
           
            // StringBuffer SQL = new StringBuffer("select nivel"+nivRep+" cuenta,descrip from rtcCuentas where nivel1='"+niv1);
            if (nivRep == 1){
              x ="select cuenta_mayor, cuenta_mayor||' ' ||descripcion as descripcion from RF_TC_CLASIFICADOR_CUENTAS where extract(year from fecha_vig_fin)>="+pEjercicio+" and conf_cve_cta_cont_id <>0 order by descripcion ";        
            }else if (nivRep == 2){
                x ="select substr(cuenta_contable,5,1) cuenta, substr(cuenta_contable,5,1)||' '||descripcion as descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,4) = '"+niv1+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
            } else if (nivRep == 3){
                x ="select substr(cuenta_contable,6,4) cuenta, substr(cuenta_contable,6,4)||' '||descripcion as descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,5) = '"+niv1+niv2+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";        
            } else if (nivRep ==4){
                x ="select substr(cuenta_contable,10,4) cuenta, substr(cuenta_contable,10,4)||' '||descripcion as descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,9) = '"+niv1+niv2+niv3+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+ condicionUniEje+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";                
            } else if (nivRep ==5){
                x ="select substr(cuenta_contable,14,4) cuenta, substr(cuenta_contable,14,4)||' '||descripcion as descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,13) = '"+niv1+niv2+niv3+niv4+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+condicion+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";                
            } else if (nivRep ==6){
                x ="select substr(cuenta_contable,18,4) cuenta, substr(cuenta_contable,18,4)||' '||descripcion as descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,17) = '"+niv1+niv2+niv3+niv4+niv5+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+condicion+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";                
            } else if (nivRep ==7){
                x ="select substr(cuenta_contable,22,4) cuenta, substr(cuenta_contable,22,4)||' '||descripcion as descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,21) = '"+niv1+niv2+niv3+niv4+niv5+niv6+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+condicion+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";                    
            } else if (nivRep ==8){
                x ="select substr(cuenta_contable,26,4) cuenta, substr(cuenta_contable,26,4)||' '||descripcion as descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,25) = '"+niv1+niv2+niv3+niv4+niv5+niv6+niv7+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+condicion+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";                        
            } else if (nivRep ==9){
                x ="select substr(cuenta_contable,30,4) cuenta, substr(cuenta_contable,30,4)||' '||descripcion as descripcion from RF_TR_CUENTAS_CONTABLES r where substr(cuenta_contable,1,29) = '"+niv1+niv2+niv3+niv4+niv5+niv6+niv7+niv8+"' and nivel = "+ nivRep+ " and id_catalogo_cuenta = '"+pCatCuentaId+"'"+condicion+" and extract(year from fecha_vig_ini)="+pEjercicio+" order by cuenta";                        
            }        
            
            SQL.append(x);
      //      System.out.println("va "+SQL.toString());
            rsQuery = stQuery.executeQuery(SQL.toString());
            return rsQuery;
        }      


        /** 
        * Metodo que muestra datos de RF_TC_CLASIFICADOR_CUENTAS 
        * Fecha de creacion: 14/12/2010
        * Autor: Elida Pozos Vazquez
        */ 
        public ResultSet selectCuentasDatos(Connection con,String niv,String cveCta) throws SQLException, Exception  {
          Statement stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rsQuery = null;
            StringBuffer SQL = new StringBuffer("");
            SQL.append("SELECT A.cuenta_mayor_id,A.cuenta_mayor,B.longitud,B.nivel_operacion,c.orden,c.tamanio,c.codigo, ");
            SQL.append("TO_CHAR(A.fecha_vig_ini,'dd/mm/yyyy') as fecha1,TO_CHAR(A.fecha_vig_fin,'dd/mm/yyyy') as fecha2 ");
            SQL.append("FROM RF_TC_CLASIFICADOR_CUENTAS A, RF_TR_CONFIGURA_CLAVES B, RF_TR_DETALLE_CONF_CVE C ");
            SQL.append("WHERE A.conf_cve_cta_cont_id=B.id_conf_cve AND A.conf_cve_cta_cont_id=c.id_conf_cve ");
            SQL.append("AND a.cuenta_mayor='"+cveCta+"' and c.orden='"+niv+"' ");
            SQL.append("ORDER BY A.cuenta_mayor_id,c.orden ");    
       //     System.out.println("metodo selectCuentasDatos: "+SQL.toString());
            rsQuery = stQuery.executeQuery(SQL.toString());
            return rsQuery;
        }      

        /** 
        * Metodo que obtiene el último id_conf:cve de RF_TR_CONFIGURA_CLAVES y regresa el valor siguiente
        * Fecha de creacion: 20/12/2010
        * Autor: Jusaji
        * Fecha de modificacion: 
        * Modificado por: 
        */ 
         public String Siguiente_Id_conf_cve(Connection con)throws SQLException, Exception{ 
            Statement stQuery=null; 
            ResultSet rsQuery=null; 
            int liResult=0;
            try{ 
             stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
             StringBuffer SQL=new StringBuffer("SELECT ID_CONF_CVE FROM ( SELECT MAX(ID_CONF_CVE) AS ID_CONF_CVE FROM RF_TR_CONFIGURA_CLAVES)");  
             //System.out.println(SQL.toString()); 
             rsQuery=stQuery.executeQuery(SQL.toString()); 
             while (rsQuery.next()){ 
                id_conf_cve=(rsQuery.getString("id_conf_cve")==null) ? "" : rsQuery.getString("id_conf_cve"); 
             } // Fin while 
           } //Fin try 
           catch(Exception e){ 
             System.out.println("Ocurrio un error al accesar al metodo Siguiente_Id_conf_cve "+e.getMessage()); 
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
           liResult = Integer.parseInt(id_conf_cve ) + 1;
           System.out.println(id_conf_cve);
//           setId_conf_cve(Integer.toString(liResult) );
           return  Integer.toString(liResult);
         } //Fin metodo select_RF_TC_CLASIFICADOR_CUENTAS_mayor       

        /** 
        * Metodo que inserta la informacion de RF_TR_CONFIGURA_CLAVES 
        * Fecha de creacion: 16/12/2010
        * Autor: Jusaji
        * Fecha de modificacion: 
        * Modificado por: 
        */ 
        public void insert_RF_TR_CONFIGURA_CLAVES(Connection con)throws SQLException, Exception{ 
           Statement stQuery=null; 
           try{            
            id_conf_cve = Siguiente_Id_conf_cve(con);
            stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
            StringBuffer SQL=new StringBuffer("INSERT INTO RF_TR_CONFIGURA_CLAVES( id_conf_cve,descripcion,fecha_vig_ini,fecha_vig_fin, longitud,ajuste,caracter,nivel_operacion)");
            SQL.append("VALUES("); 
            SQL.append(id_conf_cve).append(","); 
            SQL.append("'").append(descripcion).append("',"); 
            SQL.append("to_date('").append(fecha_vig_ini).append("','dd/mm/yyyy'),"); 
            SQL.append("to_date('").append(fecha_vig_fin).append("','dd/mm/yyyy'),"); 
            SQL.append(longitud).append(","); 
            SQL.append(ajuste).append(",'"); 
            SQL.append(caracter).append("',"); 
            SQL.append(nivel_operacion).append(")"); 
            
            //System.out.println(SQL.toString()); 
            int rs=-1; 
            rs=stQuery.executeUpdate(SQL.toString()); 
          } //Fin try 
          catch(Exception e){ 
            System.out.println("Ocurrio un error al accesar al metodo insert_RF_TR_CONFIGURA_CLAVES "+e.getMessage()); 
            throw e; 
          } //Fin catch 
          finally{ 
            if (stQuery!=null){ 
              stQuery.close(); 
              stQuery=null; 
            } 
          } //Fin finally 
        } //Fin metodo insert_RF_TR_CONFIGURA_CLAVES 
        
         /** 
         * Metodo que inserta la informacion de RF_TR_DETALLE_CONF_CVE 
         * Fecha de creacion: 16/12/2010
         * Autor: Jusaji
         * Fecha de modificacion: 
         * Modificado por: 
         */ 
         public void insert_RF_TR_DETALLE_CONF_CVE(Connection con)throws SQLException, Exception{ 
            Statement stQuery=null; 
            try{ 
             stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
             StringBuffer SQL=new StringBuffer("INSERT INTO RF_TR_DETALLE_CONF_CVE( orden,id_conf_cve,tamanio,posicion, agrupar,nivel_cuenta_id,codigo)");
             SQL.append("VALUES("); 
             SQL.append(orden).append(","); 
             SQL.append(id_conf_cve).append(","); 
             SQL.append(tamanio).append(","); 
             SQL.append(posicion).append(","); 
             SQL.append(agrupar).append(","); 
             SQL.append(nivel_cta_id).append(",'"); 
             SQL.append(codigo).append("')"); 
             
             //System.out.println(SQL.toString()); 
             int rs=-1; 
             rs=stQuery.executeUpdate(SQL.toString()); 
           } //Fin try 
           catch(Exception e){ 
             System.out.println("Ocurrio un error al accesar al metodo insert_RF_TR_DETALLE_CONF_CVE "+e.getMessage()); 
             throw e; 
           } //Fin catch 
           finally{ 
             if (stQuery!=null){ 
               stQuery.close(); 
               stQuery=null; 
             } 
           } //Fin finally 
         } //Fin metodo insert_RF_TR_DETALLE_CONF_CVE 
         
          /**
          * Método que modifica el atributo Cuenta_Mayor de la tabla RF_TC_CLASIFICADOR_CUENTAS, cuando se da de alta un reg. en 
          * RF_TR_CONFIGURA_CLAVES --> RF_TR_DETALLE_CONF_CVE.
          * Fecha de creacion: 20/Diciembre/2010
          * Autor:Jusaji
          */
          public void update_Clasificador_ctas(Connection con, String pCuenta_Mayor, String pConf_cve_cta_cont_id)throws SQLException, Exception{
             Statement stQuery=null;
             try{
              stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
              StringBuffer SQL=new StringBuffer("UPDATE rf_tc_clasificador_cuentas");
              SQL.append(" set conf_cve_cta_cont_id='"+pConf_cve_cta_cont_id+"' ");            
              SQL.append(" WHERE cuenta_mayor=").append(pCuenta_Mayor);
              //System.out.println(SQL.toString());
              int rs=-1;
              rs=stQuery.executeUpdate(SQL.toString());
            } //Fin try
            catch(Exception e){
              System.out.println("Ocurrio un error al accesar al metodo bcCuentaContable.update_Clasificador_ctas() "+e.getMessage());
              throw e;
            } //Fin catch
            finally{
              if (stQuery!=null){
                stQuery.close();
                stQuery=null;
              }
            } //Fin finally
          } //Fin metodo update_Clasificador_ctas
          
           /** 
           * Metodo que modifica la informacion de RF_TR_CONFIGURA_CLAVES 
           * Fecha de creacion: 06/Enero/2011
           * Autor: Jusaji
           * Fecha de modificacion: 
           * Modificado por: 
           */ 
           public void update_RF_TR_CONFIGURA_CLAVES(Connection con, String clave)throws SQLException, Exception{ 
              Statement stQuery=null; 
              try{ 
               stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
               StringBuffer SQL=new StringBuffer("UPDATE RF_TR_CONFIGURA_CLAVES"); 
               SQL.append(" SET ID_CONF_CVE=").append(id_conf_cve).append(","); 
               SQL.append("descripcion=").append("'").append(descripcion).append("',"); 
               SQL.append("fecha_vig_ini=to_date('").append(fecha_vig_ini).append("','dd/mm/yyyy'),"); 
               SQL.append("fecha_vig_fin=to_date('").append(fecha_vig_fin).append("','dd/mm/yyyy'),"); 
               SQL.append("longitud=").append("'").append(longitud).append("',"); 
               SQL.append("ajuste=").append("'").append(ajuste).append("',"); 
               SQL.append("caracter=").append(caracter).append(","); 
               SQL.append("nivel_operacion=").append(nivel_operacion); 
               SQL.append(" WHERE ID_CONF_CVE='").append(clave).append("'"); 
               //System.out.println(SQL.toString()); 
               int rs=-1; 
               rs=stQuery.executeUpdate(SQL.toString()); 
             } //Fin try 
             catch(Exception e){ 
               System.out.println("Ocurrio un error al accesar al metodo update_RF_TR_CONFIGURA_CLAVES "+e.getMessage()); 
               throw e; 
             } //Fin catch 
             finally{ 
               if (stQuery!=null){ 
                 stQuery.close(); 
                 stQuery=null; 
               } 
             } //Fin finally 
           } //Fin metodo update_RF_TR_CONFIGURA_CLAVES 
          
            /** 
            * Metodo que borra la informacion de RF_TR_CONFIGURA_CLAVE 
            * Fecha de creacion: 06/Enero/2011
            * Autor: Jusaji
            * Fecha de modificacion: 
            * Modificado por: 
            */ 
            public void delete_RF_TR_CONFIGURA_CLAVE(Connection con, String clave)throws SQLException, Exception{ 
               Statement stQuery=null; 
               try{ 
                stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                StringBuffer SQL=new StringBuffer("DELETE FROM RF_TR_CONFIGURA_CLAVES a "); 
                SQL.append("WHERE a.id_conf_cve='").append(clave).append("'"); 
                //System.out.println(SQL.toString()); 
                int rs=-1; 
                rs=stQuery.executeUpdate(SQL.toString()); 
              } //Fin try 
              catch(Exception e){ 
                System.out.println("Ocurrio un error al accesar al metodo delete_RF_TR_CONFIGURA_CLAVE "+e.getMessage()); 
                throw e; 
              } //Fin catch 
              finally{ 
                if (stQuery!=null){ 
                  stQuery.close(); 
                  stQuery=null; 
                } 
              } //Fin finally 
            } //Fin metodo delete_RF_TR_CONFIGURA_CLAVE 

            /** 
            * Metodo que borra la informacion de RF_TR_DETALLE_CONF_CVE 
            * Fecha de creacion: 06/Enero/2011
            * Autor: Jusaji
            * Fecha de modificacion: 
            * Modificado por: 
            */ 
            public void delete_RF_TR_DETALLE_CONF_CVE(Connection con, String clave)throws SQLException, Exception{ 
               Statement stQuery=null; 
               try{ 
                stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                StringBuffer SQL=new StringBuffer("DELETE FROM RF_TR_DETALLE_CONF_CVE a "); 
                SQL.append("WHERE a.id_conf_cve='").append(clave).append("'"); 
                //System.out.println(SQL.toString()); 
                int rs=-1; 
                rs=stQuery.executeUpdate(SQL.toString()); 
              } //Fin try 
              catch(Exception e){ 
                System.out.println("Ocurrio un error al accesar al metodo delete_RF_TR_DETALLE_CONF_CVE "+e.getMessage()); 
                throw e; 
              } //Fin catch 
              finally{ 
                if (stQuery!=null){ 
                  stQuery.close(); 
                  stQuery=null; 
                } 
              } //Fin finally 
            } //Fin metodo delete_RF_TR_DETALLE_CONF_CVE 

             /** 
             * Metodo que lee la informacion de RF_TR_CONFIGURA_CLAVES para obtener toda la inf. de la tabla usando la ID_CONF_CVE 
             * Fecha de creacion: 06/01/2011
             * Autor: Jusaji
             * Fecha de modificacion: 
             * Modificado por: 
             */ 
             public String select_RF_TR_CONFIGURA_CLAVES_CfgCve(Connection con, String Clave)throws SQLException, Exception{ 
                Statement stQuery=null; 
                ResultSet rsQuery=null; 
                String lsResul="";
                try{ 
                 stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                 StringBuffer SQL=new StringBuffer("SELECT a.ID_CONF_CVE,a.DESCRIPCION,a.FECHA_VIG_INI,a.FECHA_VIG_FIN,a.LONGITUD,a.AJUSTE,a.CARACTER,a.NIVEL_OPERACION ");  
                 SQL.append(" FROM RF_TR_CONFIGURA_CLAVES a "); 
                 SQL.append(" WHERE a.ID_CONF_CVE='").append(Clave).append("'"); 
                 //System.out.println(SQL.toString()); 
                 rsQuery=stQuery.executeQuery(SQL.toString()); 
                 while (rsQuery.next()){ 
                    id_conf_cve=(rsQuery.getString("id_conf_cve")==null) ? "" : rsQuery.getString("id_conf_cve"); 
                    longitud=(rsQuery.getString("longitud")==null) ? "" : rsQuery.getString("longitud"); 
                    nivel_operacion=(rsQuery.getString("nivel_operacion")==null) ? "" : rsQuery.getString("nivel_operacion"); 
                    lsResul=Clave;
                 } // Fin while 
               } //Fin try 
               catch(Exception e){ 
                 System.out.println("Ocurrio un error al accesar al metodo select_RF_TR_CONFIGURA_CLAVES_CfgCve "+e.getMessage()); 
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
               return lsResul;
             } //Fin metodo select_RF_TR_CONFIGURA_CLAVES_CfgCve 
             
          /** 
        * Metodo que muestra cuentas de mayor configuradas 
        * Fecha de creacion: 11/06/2013
        * Autor: Claudia Torres Macario
        */            
        public ResultSet selectCuentasMayorConf(Connection con) throws SQLException, Exception  {
          Statement stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          ResultSet rsQuery = null;
            StringBuffer SQL = new StringBuffer("");
            SQL.append("SELECT A.cuenta_mayor_id,A.cuenta_mayor,B.longitud,B.nivel_operacion,c.orden,c.tamanio,c.codigo, ");
            SQL.append("TO_CHAR(A.fecha_vig_ini,'dd/mm/yyyy') as fecha1,TO_CHAR(A.fecha_vig_fin,'dd/mm/yyyy') as fecha2 ");
            SQL.append("FROM RF_TC_CLASIFICADOR_CUENTAS A, RF_TR_CONFIGURA_CLAVES B, RF_TR_DETALLE_CONF_CVE C ");
            SQL.append("WHERE A.conf_cve_cta_cont_id=B.id_conf_cve AND A.conf_cve_cta_cont_id=c.id_conf_cve ");
            SQL.append("AND a.conf_cve_cta_cont_id > 0 and c.orden='1'");
            SQL.append("ORDER BY A.cuenta_mayor_id,c.orden ");    
       //     System.out.println("metodo selectCuentasDatos: "+SQL.toString());
            rsQuery = stQuery.executeQuery(SQL.toString());
            return rsQuery;
        }  
    } //Fin clase bcRf_tc_clasificador_cuentas 
