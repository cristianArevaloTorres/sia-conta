package sia.rf.contabilidad.sistemas.mipf.modPrueba;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import sia.rf.contabilidad.registroContableEvento.Cadena;
import sia.rf.contabilidad.registroContableNuevo.bcProveedores;

import sia.rf.contabilidad.sistemas.mipf.modDescarga.Funcion;

import sia.ws.publicar.contabilidad.Registro;

import sun.jdbc.rowset.CachedRowSet;

public class Compromiso {
    public Compromiso() {
    
    }

    private String rfc;
    private String nombre_proveedor;
    
    private String unidad_afecta;
    private String entidad_afecta;
    private String ambito_afecta;
    
    private String unidad;
    private String ambito;
    private String tiga;
    private String tigaCapitulo;
    private String entidad;
    private String concepto;
    private String programa;
    private String proveedor;
    private String importes;
    private String referencia;
    private String ejercicio;
    private String capitulo;
    private String partida;
    
    
    private CachedRowSet crsCompromiso=null;
    private CachedRowSet crsCompromisoDevengado=null;
    private CachedRowSet crsCompromisoModificacion=null;
    private CachedRowSet crsSentenciaFuncion=null;
    
     public String getRfc() {
        return rfc;
    }
     public String getNombreProveedor() {
        return nombre_proveedor;
    }
    public void setUnidad_afecta(String unidad_afecta) {
        this.unidad_afecta = unidad_afecta;
    }

    public String getUnidad_afecta() {
        return unidad_afecta;
    }

    public void setEntidad_afecta(String entidad_afecta) {
        this.entidad_afecta = entidad_afecta;
    }

    public String getEntidad_afecta() {
        return entidad_afecta;
    }

    public void setAmbito_afecta(String ambito_afecta) {
        this.ambito_afecta = ambito_afecta;
    }

    public String getAmbito_afecta() {
        return ambito_afecta;
    }
     public String getUnidad() {
        return unidad;
    }
     public String getAmbito() {
        return ambito;
    }
     public String getTiga() {
       return tiga;
     }

    public String getEntidad() {
      return entidad;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }
    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getPrograma() {
        return programa;
    }
    public void setImportes(String importes) {
        this.importes = importes;
    }
    public String getImportes() {
        return importes;
    }

    public void setTigaCapitulo(String tigaCapitulo) {
        this.tigaCapitulo = tigaCapitulo;
    }

    public String getTigaCapitulo() {
        return tigaCapitulo;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getReferencia() {
        return referencia;
    }   
    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getProveedor() {
        return proveedor;
    }
    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getEjercicio() {
        return ejercicio;
    }
    public void setCapitulo(String capitulo) {
        this.capitulo = capitulo;
    }

    public String getCapitulo() {
        return capitulo;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public String getPartida() {
        return partida;
    }
    public HashMap creaHashMap(int numForma) throws SQLException, Exception{
        Context context = Context.enter();
        Scriptable scope = context.initStandardObjects();
        context.getWrapFactory().setJavaPrimitiveWrap(false);
        org.mozilla.javascript.Script compiled = null;
        Object r = null;

        Object resultado=0;
        String llave=null;
        String valor=null;
        boolean bandera=false;
        
        HashMap variables=null; 
        variables = new HashMap();
        Funcion funcion = new Funcion();
        
        try{
          crsSentenciaFuncion=funcion.select_rf_tc_FuncionesPorForma(numForma); 
          crsSentenciaFuncion.beforeFirst();    
          while (crsSentenciaFuncion.next()){
            bandera=false;
            resultado="0";
            if (crsSentenciaFuncion.getString("regla_contable").equals("SIN")){ 
              bandera=true;
            }
            else{                       
              ScriptableObject.putProperty(scope,"partidaGen",Context.javaToJS(partida,scope));          
              ScriptableObject.putProperty(scope,"capitulo",Context.javaToJS(capitulo,scope));          
                          
              compiled = context.compileString(crsSentenciaFuncion.getString("regla_contable"),"defLoaf",0,null);             
              compiled.exec(context,scope);
              resultado=ScriptableObject.getProperty(scope,"resultado"); 
              if (resultado.equals("1")){
                bandera=true;
              } 
            }
            if (bandera==true){
              llave=crsSentenciaFuncion.getString("nombreVariable");
              valor="0.0";
              switch (crsSentenciaFuncion.getInt("idVariable")){
                case 1: valor=getImportes();break;
                case 9: valor=getUnidad(); break;
                case 11: valor=getAmbito(); break;
                case 12: valor=tiga; break;
                case 13: valor=getReferencia();break;
                case 14: valor = getConcepto();break;
                case 15: valor=getProveedor();break;
                case 16: valor=getCapitulo();break;
                case 22: valor = getTigaCapitulo();break;        
                case 24: valor = getEjercicio();break;   
                case 25: valor = getPrograma();break;        
                case 27: valor = getPartida().substring(2);break;        
               
              }
              variables.put(llave,valor);  
            }
          }  
        } 
        catch(Exception e){ 
           System.out.println("Ocurrio un error al accesar al metodo creaHashMap "+e.getMessage()); 
           throw e; 
         } //Fin catch 
         finally{    
         }    
      return variables;
    }

  public int leeCompromiso (Connection con,  String ejercicio, String numeroDocumento) throws SQLException, Exception {
    int regs=0;
    StringBuffer SQL = new StringBuffer("SELECT \n");
    SQL.append(" F_PROGRAMA(cve.clave_presupuestaria_id)  programa, \n");
    SQL.append(" decode(lpad(cve.unidad_ejecutora,4,'0'),'0104',decode( F_PROGRAMA(cve.clave_presupuestaria_id) \n");
    SQL.append("  ,'0008','0118','0117'),'0108','0118',lpad(cve.unidad_ejecutora,4,'0')) as unidad, \n");
    SQL.append("  cve.estado as entidad, \n");
    SQL.append(" (lpad(cve.estado,3,'0') || cve.ambito) as estado,     \n");
    SQL.append("  substr(CVE.PARTIDA,1,1)||'000' as capitulo, \n");
    SQL.append("   substr(CVE.PARTIDA,1,3) as partida_gen, \n");
    SQL.append(" case when substr(CVE.PARTIDA,1,1) ='1' then '0001' \n");
    SQL.append("      when substr(CVE.PARTIDA,1,1) ='5' then '5000' \n");
    SQL.append("      when substr(CVE.PARTIDA,1,1) ='6' then '6000' \n");
    SQL.append("      else '0002'  \n");
    SQL.append("      end as tigaCapitulo, \n"); 
    SQL.append(" case when substr(CVE.PARTIDA,1,1) ='1' then '0001' \n"); 
    SQL.append("      else '0002' \n"); 
    SQL.append("      end as tiga, \n"); 
    SQL.append(" '1' AS TIPOCOM, \n"); 
    SQL.append(" BEN.RFC AS CVEPROV, \n"); 
    SQL.append(" case when BEN.tipo_persona='F' then (BEN.nombre || ' ' || BEN.ap_paterno || ' ' || BEN.ap_materno)  \n"); 
    SQL.append("      when BEN.tipo_persona in ('M','P','E') then (BEN.razon_social) \n"); 
    SQL.append("      end as NOMPROV, \n"); 
    SQL.append(" BEN.ID_CONTABLE  AS PROV, \n"); 
    SQL.append(" ' ' AS PEDIDO, \n"); 
    SQL.append(" CONSECUTIVO AS REFERENCIA, \n"); 
    SQL.append(" sum(COMD.IMPORTE) AS IMPORTE, \n"); 
    SQL.append(" replace(replace(COM.Obs_Ue,chr(39),''),chr(124),'') AS CONCEPTO, \n"); 
    SQL.append(" TO_CHAR(COM.FECHA_ESTATUS,'YYYYMMDD') as FECHAAUT, \n"); 
    SQL.append(" TO_CHAR(COM.FECHA_EXPED,'YYYYMMDD') as FECHACAP, \n"); 
    SQL.append(" decode(lpad(substr(COM.CONSECUTIVO,1,3),4,'0'),'0104',decode( F_PROGRAMA(cve.clave_presupuestaria_id) \n"); 
    SQL.append(" ,'0008','0118','0117'),'0108','0118',lpad(substr(COM.CONSECUTIVO,1,3),4,'0')) as unidad_afecta, \n"); 
    SQL.append(" lpad(substr(COM.CONSECUTIVO,4,3),4,'0') as estado_afecta,      \n"); 
    SQL.append(" substr(COM.CONSECUTIVO,4,2) as entidad_afecta, com.ejercicio      \n"); 
    SQL.append(" FROM \n"); 
    SQL.append("   sapfin_pa.RF_TR_COMP_PAGO COM \n"); 
    SQL.append("   INNER JOIN sapfin_pa.RF_TR_COMP_PAGO_DET COMD ON COM.ID_COMP_PAGO = COMD.ID_COMP_PAGO \n"); 
    SQL.append("   INNER JOIN RF_TC_BENEFICIARIO BEN ON COM.ID_BENEFICIARIO = BEN.ID_BENEFICIARIO \n"); 
    SQL.append("   INNER JOIN RF_TC_ORIGEN_COMPROMISOS ORIG ON COM.ID_ORIGEN_COMPROMISO = ORIG.ID_ORIGEN_COMPROMISO \n"); 
    SQL.append("   INNER JOIN RF_TV_CLAVES_PRESUPUESTARIAS CVE ON COMD.CLAVE_PRESUPUESTARIA_ID = CVE.CLAVE_PRESUPUESTARIA_ID \n"); 
    SQL.append(" WHERE \n"); 
    SQL.append(" COM.ID_STATUS >= 4 \n"); 
    SQL.append(" and com.ejercicio  = ").append(ejercicio);
    SQL.append(" and COM.CONSECUTIVO = '").append(numeroDocumento).append("'"); 
    SQL.append(" GROUP BY \n"); 
    SQL.append(" cve.clave_presupuestaria_id,substr(CVE.CLAVE_PROGRAMATICA,1,2),substr(CVE.CLAVE_PROGRAMATICA,3,3),substr(CVE.CLAVE_PROGRAMATICA,6,2), \n"); 
    SQL.append(" substr(CVE.CLAVE_PROGRAMATICA,8,2),substr(CVE.CLAVE_PROGRAMATICA,10,2), \n"); 
    SQL.append(" lpad(cve.unidad_ejecutora,4,'0'),  \n"); 
    SQL.append(" cve.estado,     \n"); 
    SQL.append(" (lpad(cve.estado,3,'0') || cve.ambito), \n");
    SQL.append("  substr(CVE.PARTIDA,1,1)||'000', \n");
    SQL.append("   substr(CVE.PARTIDA,1,3), \n");
    SQL.append(" case when substr(CVE.PARTIDA,1,1) ='1' then '0001'  \n"); 
    SQL.append("      when substr(CVE.PARTIDA,1,1) ='5' then '5000' \n"); 
    SQL.append("      when substr(CVE.PARTIDA,1,1) ='6' then '6000' \n"); 
    SQL.append("      else '0002' \n"); 
    SQL.append("      end, \n"); 
    SQL.append(" case when substr(CVE.PARTIDA,1,1) ='1' then '0001' \n"); 
    SQL.append("      else '0002' end, \n"); 
    SQL.append(" BEN.RFC, \n"); 
    SQL.append(" case when BEN.tipo_persona='F' then (BEN.nombre || ' ' || BEN.ap_paterno || ' ' || BEN.ap_materno) \n"); 
    SQL.append("      when BEN.tipo_persona in ('M','P','E') then (BEN.razon_social) \n"); 
    SQL.append("      end, \n"); 
    SQL.append(" BEN.ID_CONTABLE, \n"); 
    SQL.append(" CONSECUTIVO, \n"); 
    SQL.append(" COM.Obs_Ue, \n"); 
    SQL.append(" TO_CHAR(COM.FECHA_ESTATUS,'YYYYMMDD'), \n"); 
    SQL.append(" TO_CHAR(COM.FECHA_EXPED,'YYYYMMDD'), \n"); 
    SQL.append(" lpad(substr(COM.CONSECUTIVO,1,3),4,'0'), \n"); 
    SQL.append(" lpad(substr(COM.CONSECUTIVO,4,3),4,'0'),       \n"); 
    SQL.append(" substr(COM.CONSECUTIVO,4,2), com.ejercicio      \n"); 
    try {
        crsCompromiso = new CachedRowSet();
        crsCompromiso.setCommand(SQL.toString());         
        crsCompromiso.execute(con);
        regs=crsCompromiso.size();  
    }catch (Exception e){
        System.out.println ("Ha ocurrido un error en el metodo leeCompromiso");
        System.out.println ("leeCompromiso:" + SQL.toString());
        crsCompromiso.close();
        crsCompromiso=null;
        throw e;
    } finally {
        SQL.setLength(0);
        SQL=null;
     }
     return regs;
  
  }    

    public int LeeCompromisoModificacion (Connection con,  String ejercicio, String numeroDocumento) throws SQLException, Exception {
       int regs=0;
       StringBuffer SQL = new StringBuffer("SELECT ");
       SQL.append("   F_PROGRAMA(cvep.clave_presupuestaria_id)   programa, ");
       SQL.append("   decode(lpad(cvep.unidad_ejecutora,4,'0'),'0104',decode( F_PROGRAMA(cvep.clave_presupuestaria_id)  ");
       SQL.append("   ,'0008','0118','0117'),'0108','0118',lpad(cvep.unidad_ejecutora,4,'0')) as unidad,");
       SQL.append("   CVEP.ESTADO AS ENTIDAD, ");
       SQL.append("   (LPAD(CVEP.ESTADO,3,'0') || CVEP.AMBITO) AS ESTADO,           ");
       SQL.append("  substr(CVEP.PARTIDA,1,1)||'000' as capitulo, \n");
       SQL.append("   substr(CVEP.PARTIDA,1,3) as partida_gen, \n");
       SQL.append("   CASE WHEN SUBSTR(CVEP.PARTIDA,1,1) ='1' THEN '0001' ");
       SQL.append("      WHEN SUBSTR(CVEP.PARTIDA,1,1) ='5' THEN '5000' ");
       SQL.append("      WHEN SUBSTR(CVEP.PARTIDA,1,1) ='6' THEN '6000' ");
       SQL.append("      ELSE '0002' ");
       SQL.append("   END AS TIGACAPITULO,           ");
       SQL.append("   CASE WHEN SUBSTR(CVEP.PARTIDA,1,1) ='1' THEN '0001' ");
       SQL.append("      ELSE '0002' ");
       SQL.append("   END AS TIGA, ");
       SQL.append("   '3' AS TIPOCOM, ");
       SQL.append("   BEN.RFC AS CVEPROV, "); 
       SQL.append("   CASE WHEN BEN.TIPO_PERSONA='F' THEN (BEN.NOMBRE || ' ' || BEN.AP_PATERNO || ' ' || BEN.AP_MATERNO)  ");
       SQL.append("        WHEN BEN.TIPO_PERSONA IN ('M','P','E') THEN (BEN.RAZON_SOCIAL) ");
       SQL.append("       END AS NOMPROV,           ");
       SQL.append("   ' ' AS PEDIDO, ");
       SQL.append("   CMPMOD.CONSECUTIVO AS REFERENCIA, ");
       SQL.append("   REPLACE(REPLACE(DECODE(CMPMOD.CONCEPTO_MODIFICA, NULL, 'MODIFICACION AL COMPROMISO - '||CMP.CONSECUTIVO,CMPMOD.CONCEPTO_MODIFICA),CHR(39),''),CHR(124),'') AS CONCEPTO,  ");
       SQL.append("   TO_CHAR(CMPMOD.FECHA_ESTATUS,'YYYYMMDD') AS FECHAAUT,  ");
       SQL.append("   TO_CHAR(CMPMOD.FECHA_ESTATUS,'YYYYMMDD') AS FECHACAP,");
       SQL.append("   DECODE(LPAD(UE.UNIDAD_EJECUTORA,4,'0'),'0104',DECODE( F_PROGRAMA(CVEP.CLAVE_PRESUPUESTARIA_ID) ");
       SQL.append("   ,'0008','0118','0117'),'0108','0118',LPAD(UE.UNIDAD_EJECUTORA,4,'0')) AS UNIDAD_AFECTA,");
       SQL.append("   lpad(UE.ESTADO||UE.AMBITO,4,'0') AS ESTADO_AFECTA,   ");
       SQL.append("   UE.ESTADO AS ENTIDAD_AFECTA, CMPMOD.ejercicio,               ");
       SQL.append("   CASE WHEN CMPMOD.TIPO_MODIFICACION='D' THEN (SUM(MODDET.IMPORTE)*-1) ELSE SUM(MODDET.IMPORTE) END AS IMPORTE  ");
       SQL.append("   FROM ");
       SQL.append("   sapfin_pa.RF_TR_COMP_MODIFICACION CMPMOD ");
       SQL.append("   INNER JOIN sapfin_pa.RF_TR_COMP_MODIFICACION_DET MODDET ON MODDET.ID_COMP_MODIFICACION = CMPMOD.ID_COMP_MODIFICACION ");
       SQL.append("   INNER JOIN sapfin_pa.RF_TR_COMP_PAGO CMP ON CMP.ID_COMP_PAGO=CMPMOD.ID_COMP_PAGO ");
       SQL.append("   INNER JOIN RF_TV_CLAVES_PRESUPUESTARIAS CVEP ON CVEP.CLAVE_PRESUPUESTARIA_ID = MODDET.CLAVE_PRESUPUESTARIA_ID  ");
       SQL.append("   INNER JOIN RF_TC_BENEFICIARIO BEN ON CMP.ID_BENEFICIARIO = BEN.ID_BENEFICIARIO ");
       SQL.append("   INNER JOIN RF_TC_UNIDADES_ESTADOS UE ON UE.ID_UNIDAD_ESTADO=CMP.ID_UNIDAD_ESTADO ");
       SQL.append("   WHERE ");
       SQL.append("   CMPMOD.ID_STATUS = 4 ");
       SQL.append("   AND CMPMOD.EJERCICIO = ").append(ejercicio);
       SQL.append("   AND CMPMOD.CONSECUTIVO  ='").append(numeroDocumento.substring(0,14)).append("-").append(numeroDocumento.substring(15)).append("-").append(numeroDocumento.substring(14,15)).append("'");  //        ='125192COM00006-01-D' ");
       SQL.append("   GROUP BY ");
       SQL.append("    F_PROGRAMA(cvep.clave_presupuestaria_id), ");
       SQL.append("   decode(lpad(cvep.unidad_ejecutora,4,'0'),'0104',decode( F_PROGRAMA(cvep.clave_presupuestaria_id)  ");
       SQL.append("   ,'0008','0118','0117'),'0108','0118',lpad(cvep.unidad_ejecutora,4,'0')),");
       SQL.append("   CVEP.ESTADO, ");
       SQL.append("   (LPAD(CVEP.ESTADO,3,'0') || CVEP.AMBITO),           ");
       SQL.append("  substr(CVEP.PARTIDA,1,1)||'000', \n");
       SQL.append("   substr(CVEP.PARTIDA,1,3), \n");       
       SQL.append("   CASE WHEN SUBSTR(CVEP.PARTIDA,1,1) ='1' THEN '0001' ");
       SQL.append("      WHEN SUBSTR(CVEP.PARTIDA,1,1) ='5' THEN '5000' ");
       SQL.append("      WHEN SUBSTR(CVEP.PARTIDA,1,1) ='6' THEN '6000' ");
       SQL.append("      ELSE '0002' END,           ");
       SQL.append("   CASE WHEN SUBSTR(CVEP.PARTIDA,1,1) ='1' THEN '0001' ");
       SQL.append("      ELSE '0002' END, ");
       SQL.append("   '3', ");
       SQL.append("   BEN.RFC, "); 
       SQL.append("   CASE WHEN BEN.TIPO_PERSONA='F' THEN (BEN.NOMBRE || ' ' || BEN.AP_PATERNO || ' ' || BEN.AP_MATERNO)  ");
       SQL.append("        WHEN BEN.TIPO_PERSONA IN ('M','P','E') THEN (BEN.RAZON_SOCIAL) ");
       SQL.append("       END,           ");
       SQL.append("   ' ', ");
       SQL.append("   CMPMOD.CONSECUTIVO, ");
       SQL.append("   REPLACE(REPLACE(DECODE(CMPMOD.CONCEPTO_MODIFICA, NULL, 'MODIFICACION AL COMPROMISO - '||CMP.CONSECUTIVO,CMPMOD.CONCEPTO_MODIFICA),CHR(39),''),CHR(124),''),  ");
       SQL.append("   TO_CHAR(CMPMOD.FECHA_ESTATUS,'YYYYMMDD'),  ");
       SQL.append("   TO_CHAR(CMPMOD.FECHA_ESTATUS,'YYYYMMDD'),");
       SQL.append("   DECODE(LPAD(UE.UNIDAD_EJECUTORA,4,'0'),'0104',DECODE( F_PROGRAMA(CVEP.CLAVE_PRESUPUESTARIA_ID) ");
       SQL.append("   ,'0008','0118','0117'),'0108','0118',LPAD(UE.UNIDAD_EJECUTORA,4,'0')),");
       SQL.append("   lpad(UE.ESTADO||UE.AMBITO,4,'0'),   ");
       SQL.append("   UE.ESTADO,CMPMOD.TIPO_MODIFICACION, CMPMOD.ejercicio ");  

       try {

           crsCompromisoModificacion = new CachedRowSet();
           crsCompromisoModificacion.setCommand(SQL.toString());         
           crsCompromisoModificacion.execute(con);
           regs=crsCompromisoModificacion.size();            
       }catch (Exception e){
            System.out.println ("Ha ocurrido un error en el metodo compromisoModificacion");
            System.out.println ("compromisoModificacion:" + SQL.toString());
            crsCompromisoModificacion.close();
            crsCompromisoModificacion=null;
            throw e;
       } finally {
             SQL.setLength(0);
             SQL=null;
          
       }
       return regs;
   }


    public int leeCompromisoDevengado (Connection con,  String ejercicio, String numeroDocumento) throws SQLException, Exception {
      int regs=0;
      StringBuffer SQL = new StringBuffer("");
      SQL.append(" select F_PROGRAMA(cve.clave_presupuestaria_id)  programa, \n")
         .append("   decode(lpad(cve.unidad_ejecutora,4,'0'),'0104',decode( F_PROGRAMA(cve.clave_presupuestaria_id) \n")
         .append("    ,'0008','0118','0117'),'0108','0118',lpad(cve.unidad_ejecutora,4,'0')) as unidad, \n")
         .append("    cve.estado as entidad, \n")
         .append("   (lpad(cve.estado,3,'0') || cve.ambito) as estado,     \n")
         .append("    substr(CVE.PARTIDA,1,1)||'000' as capitulo, \n")
         .append("     substr(CVE.PARTIDA,1,3) as partida_gen, \n")
         .append("   case when substr(CVE.PARTIDA,1,1) ='1' then '0001' \n")
         .append("        when substr(CVE.PARTIDA,1,1) ='5' then '5000' \n")
         .append("        when substr(CVE.PARTIDA,1,1) ='6' then '6000' \n")
         .append("        else '0002'  \n")
         .append("        end as tigaCapitulo,  \n")
         .append("   case when substr(CVE.PARTIDA,1,1) ='1' then '0001'  \n")
         .append("        else '0002'  \n")
         .append("        end as tiga,  \n")
         .append("   '1' AS TIPOCOM,  \n")
         .append("   BEN.RFC AS CVEPROV, \n")
         .append("   case when BEN.tipo_persona='F' then (BEN.nombre || ' ' || BEN.ap_paterno || ' ' || BEN.ap_materno)   \n")
         .append("        when BEN.tipo_persona in ('M','P','E') then (BEN.razon_social)  \n")
         .append("        end as NOMPROV,  \n")
         .append("   BEN.ID_CONTABLE  AS PROV,  \n")
         .append("   ' ' AS PEDIDO,  \n")
         .append("   CONSECUTIVO AS REFERENCIA,  \n")
         .append("   sum(ci.saldo) AS IMPORTE,  \n")
         .append("   replace(replace(ci.obs_ue,chr(39),''),chr(124),'') AS CONCEPTO,  \n")
         .append("   decode(lpad(substr(COM.CONSECUTIVO,1,3),4,'0'),'0104',decode( F_PROGRAMA(cve.clave_presupuestaria_id)  \n")
         .append("   ,'0008','0118','0117'),'0108','0118',lpad(substr(COM.CONSECUTIVO,1,3),4,'0')) as unidad_afecta,  \n")
         .append("   lpad(substr(COM.CONSECUTIVO,4,3),4,'0') as estado_afecta,       \n")
         .append("   substr(COM.CONSECUTIVO,4,2) as entidad_afecta, ci.ejercicio \n")
         .append(" from rf_th_cierre_comp_pago ci,  \n")
         .append("    rf_tv_control_meses m, \n")
         .append("    RF_TR_COMP_PAGO com, \n")
         .append("    RF_TV_CLAVES_PRESUPUESTARIAS CVE, \n")
         .append("    RF_TC_BENEFICIARIO BEN   \n")
         .append(" where ci.ejercicio=m.EJERCICIO and ci.mes=m.MES and ci.corte=m.CORTE   \n")
         .append(" and ci.id_comp_pago=com.id_comp_pago and ci.CLAVE_PRESUPUESTARIA_ID = CVE.CLAVE_PRESUPUESTARIA_ID    \n")
         .append(" and  COM.ID_BENEFICIARIO = BEN.ID_BENEFICIARIO    \n")
         .append(" and ci.saldo<>0  \n")
         .append(" and ci.mes=12 and ci.ejercicio=").append(ejercicio).append(" \n")
         .append(" and COM.CONSECUTIVO ='").append(numeroDocumento).append("'  \n")
         .append("and m.CORTE = (select max(n.corte) from rf_tv_control_meses n where  n.ejercicio=").append(ejercicio).append(" and n.mes=12)  \n")
         .append(" group by  \n")
         .append("   cve.clave_presupuestaria_id,substr(CVE.CLAVE_PROGRAMATICA,1,2),substr(CVE.CLAVE_PROGRAMATICA,3,3),substr(CVE.CLAVE_PROGRAMATICA,6,2),   \n")
         .append("   substr(CVE.CLAVE_PROGRAMATICA,8,2),substr(CVE.CLAVE_PROGRAMATICA,10,2),   \n")
         .append("   lpad(cve.unidad_ejecutora,4,'0'),    \n")
         .append("   cve.estado,       \n")
         .append("   (lpad(cve.estado,3,'0') || cve.ambito),  \n")
         .append("    substr(CVE.PARTIDA,1,1)||'000',  \n")
         .append("     substr(CVE.PARTIDA,1,3),  \n")
         .append("   case when substr(CVE.PARTIDA,1,1) ='1' then '0001'    \n")
         .append("        when substr(CVE.PARTIDA,1,1) ='5' then '5000'   \n")
         .append("        when substr(CVE.PARTIDA,1,1) ='6' then '6000'   \n")
         .append("        else '0002'   \n")
         .append("        end,   \n")
         .append("   case when substr(CVE.PARTIDA,1,1) ='1' then '0001'   \n")
         .append("        else '0002' end,   \n")
         .append("   BEN.RFC,   \n")
         .append("   case when BEN.tipo_persona='F' then (BEN.nombre || ' ' || BEN.ap_paterno || ' ' || BEN.ap_materno)   \n")
         .append("        when BEN.tipo_persona in ('M','P','E') then (BEN.razon_social)   \n")
         .append("        end,   \n")
         .append("   BEN.ID_CONTABLE,  \n")
         .append("   CONSECUTIVO,   \n")
         .append("   ci.obs_ue,   \n")
         .append("   lpad(substr(COM.CONSECUTIVO,1,3),4,'0'),   \n")
         .append("   lpad(substr(COM.CONSECUTIVO,4,3),4,'0'),         \n")
         .append("   substr(COM.CONSECUTIVO,4,2), ci.ejercicio        \n");

      try {
          crsCompromisoDevengado = new CachedRowSet();
          crsCompromisoDevengado.setCommand(SQL.toString());         
          crsCompromisoDevengado.execute(con);
          regs=crsCompromisoDevengado.size();  
      }catch (Exception e){
          System.out.println ("Ha ocurrido un error en el metodo leeCompromisoDevengado");
          System.out.println ("leeCompromisoDevengado:" + SQL.toString());
          crsCompromisoDevengado.close();
          crsCompromisoDevengado=null;
          throw e;
      } finally {
          SQL.setLength(0);
          SQL=null;
       }
       return regs;
    
    }    
    public String registroContableCompromiso (String fecha, Connection conexionContabilidad, Registro registro, String pCatalogoCuenta,int numForma) throws SQLException, Exception {
    HashMap hm = new HashMap();
    boolean bandera=false;
    StringBuffer cadenaTem= new StringBuffer("");
    StringBuffer cadena= new StringBuffer("");
    try{
            crsCompromiso.beforeFirst();
            bcProveedores bcProveedor = new bcProveedores();
            String lsIdContable="0";
            String resultado="0";
            while (crsCompromiso.next()){
              bandera=true;
              rfc=crsCompromiso.getString("cveprov");
              nombre_proveedor=crsCompromiso.getString("nomprov");
              unidad_afecta=crsCompromiso.getString("unidad_afecta");
              entidad_afecta=crsCompromiso.getString("entidad_afecta");
              ambito_afecta=crsCompromiso.getString("estado_afecta");
              unidad=crsCompromiso.getString("unidad");             
              ambito=crsCompromiso.getString("estado");              
              tigaCapitulo=crsCompromiso.getString("tigaCapitulo");
              tiga=crsCompromiso.getString("tiga");
              entidad=crsCompromiso.getString("entidad_afecta");
              concepto=crsCompromiso.getString("concepto");
              programa=crsCompromiso.getString("programa");
              ejercicio=crsCompromiso.getString("ejercicio");
              importes=crsCompromiso.getString("importe");
              referencia=crsCompromiso.getString("referencia");
              capitulo=crsCompromiso.getString("capitulo");
              partida=crsCompromiso.getString("partida_gen");
              
              lsIdContable=bcProveedor.select_rf_tc_proveedores_rfc(conexionContabilidad,rfc);
              if (lsIdContable.equals("0")){
                lsIdContable=bcProveedor.select_SEQ_rf_tc_proveedores(conexionContabilidad);
                bcProveedor.setId_contable(lsIdContable);
                bcProveedor.setRfc(rfc);
                bcProveedor.setNombre_razon_social(nombre_proveedor);
                bcProveedor.insert_rf_tc_proveedores(conexionContabilidad);         
              } 
              proveedor=lsIdContable;
              resultado=registro.AgregaCuentaContable("8241","1",crsCompromiso.getString("programa"),crsCompromiso.getString("unidad"),crsCompromiso.getString("estado"),tigaCapitulo,lsIdContable,"0000","0000",fecha,nombre_proveedor,"7",pCatalogoCuenta);
              System.out.println("Compromiso.registroContableCompromiso "+resultado);
              if (capitulo.equals("5000")){
              System.out.println("Compromiso.registroContableCompromiso cap 5000");
                  resultado=registro.AgregaCuentaContable("2112","5",programa,unidad,ambito,lsIdContable,"0000","0000","0000",fecha,nombre_proveedor,"6",pCatalogoCuenta);
              System.out.println("Compromiso.registroContableCompromiso cap 5000"+resultado);
              }

              hm=creaHashMap(numForma);
              cadenaTem.append(Cadena.construyeCadena(hm));
              cadenaTem.append("~");
              hm.clear();               
            }
            if (bandera==true){
              cadena.append(cadenaTem.toString()); 
            }                
        }catch (Exception e){
            System.out.println ("Ha ocurrido un error en el metodo registroContableCompromiso");
            crsCompromiso.close();
            crsCompromiso=null;
            throw e;
        } finally {
            }
       return cadena.toString();
    }    

 public String compromisoModificacion (String fecha,Connection conexionContabilidad, Registro registro, String pCatalogoCuenta,int numForma) throws SQLException, Exception {
    HashMap hm = new HashMap();    
    boolean bandera=false;
    StringBuffer cadenaTem= new StringBuffer("");
    StringBuffer cadena= new StringBuffer("");
 
    try{
       crsCompromisoModificacion.beforeFirst();
       bcProveedores bcProveedor = new bcProveedores();
       String lsIdContable="0";
       String resultado="0";
       while (crsCompromisoModificacion.next()){
            bandera=true;
            rfc=crsCompromisoModificacion.getString("cveprov");
            nombre_proveedor=crsCompromisoModificacion.getString("nomprov");
            unidad_afecta=crsCompromisoModificacion.getString("unidad_afecta");
            entidad_afecta=crsCompromisoModificacion.getString("entidad_afecta");
            ambito_afecta=crsCompromisoModificacion.getString("estado_afecta");
            unidad=crsCompromisoModificacion.getString("unidad");             
            ambito=crsCompromisoModificacion.getString("estado");              
            tigaCapitulo=crsCompromisoModificacion.getString("tigaCapitulo");
            tiga=crsCompromisoModificacion.getString("tiga");
            entidad=crsCompromisoModificacion.getString("entidad_afecta");
            concepto=crsCompromisoModificacion.getString("concepto");
            programa=crsCompromisoModificacion.getString("programa");
            ejercicio=crsCompromisoModificacion.getString("ejercicio");
            importes=crsCompromisoModificacion.getString("importe");
            referencia=crsCompromisoModificacion.getString("referencia");
            capitulo=crsCompromisoModificacion.getString("capitulo");
            partida=crsCompromisoModificacion.getString("partida_gen");
                            
            lsIdContable=bcProveedor.select_rf_tc_proveedores_rfc(conexionContabilidad,rfc);
            if (lsIdContable.equals("0")){
                lsIdContable=bcProveedor.select_SEQ_rf_tc_proveedores(conexionContabilidad);
                bcProveedor.setId_contable(lsIdContable);
                bcProveedor.setRfc(rfc);
                bcProveedor.setNombre_razon_social(nombre_proveedor);
                bcProveedor.insert_rf_tc_proveedores(conexionContabilidad);         
            } 
            proveedor=lsIdContable;
            resultado=registro.AgregaCuentaContable("8241","1",crsCompromisoModificacion.getString("programa"),crsCompromisoModificacion.getString("unidad"),crsCompromisoModificacion.getString("estado"),tigaCapitulo,lsIdContable,"0000","0000",fecha,nombre_proveedor,"7",pCatalogoCuenta);
            if (capitulo.equals("5000")){
                resultado=registro.AgregaCuentaContable("2112","5",programa,unidad,ambito,lsIdContable,"0000","0000","0000",fecha,nombre_proveedor,"6",pCatalogoCuenta);
            }

            hm=creaHashMap(numForma);
            cadenaTem.append(Cadena.construyeCadena(hm));
            cadenaTem.append("~");
            hm.clear();               
       }
       if (bandera==true){
           cadena.append(cadenaTem.toString()); 
       }                
    }catch (Exception e){
          System.out.println ("Ha ocurrido un error en el metodo compromisoModificacion");
          crsCompromisoModificacion.close();
          crsCompromisoModificacion=null;
          throw e;
    } finally {
    }
    return cadena.toString();
}    

    public String registroContableCompromisoDevengado (String fecha, Connection conexionContabilidad, Registro registro, String pCatalogoCuenta,int numForma) throws SQLException, Exception {
    HashMap hm = new HashMap();
    boolean bandera=false;
    StringBuffer cadenaTem= new StringBuffer("");
    StringBuffer cadena= new StringBuffer("");
    try{
            crsCompromisoDevengado.beforeFirst();
            bcProveedores bcProveedor = new bcProveedores();
            String lsIdContable="0";
            String resultado="0";
            while (crsCompromisoDevengado.next()){
              bandera=true;
              rfc=crsCompromisoDevengado.getString("cveprov");
              nombre_proveedor=crsCompromisoDevengado.getString("nomprov");
              unidad_afecta=crsCompromisoDevengado.getString("unidad_afecta");
              entidad_afecta=crsCompromisoDevengado.getString("entidad_afecta");
              ambito_afecta=crsCompromisoDevengado.getString("estado_afecta");
              unidad=crsCompromisoDevengado.getString("unidad");             
              ambito=crsCompromisoDevengado.getString("estado");              
              tigaCapitulo=crsCompromisoDevengado.getString("tigaCapitulo");
              tiga=crsCompromisoDevengado.getString("tiga");
              entidad=crsCompromisoDevengado.getString("entidad_afecta");
              concepto=crsCompromisoDevengado.getString("concepto");
              programa=crsCompromisoDevengado.getString("programa");
              ejercicio=crsCompromisoDevengado.getString("ejercicio");
              importes=crsCompromisoDevengado.getString("importe");
              referencia=crsCompromisoDevengado.getString("referencia");
              capitulo=crsCompromisoDevengado.getString("capitulo");
              partida=crsCompromisoDevengado.getString("partida_gen");
              
              lsIdContable=bcProveedor.select_rf_tc_proveedores_rfc(conexionContabilidad,rfc);
              if (lsIdContable.equals("0")){
                lsIdContable=bcProveedor.select_SEQ_rf_tc_proveedores(conexionContabilidad);
                bcProveedor.setId_contable(lsIdContable);
                bcProveedor.setRfc(rfc);
                bcProveedor.setNombre_razon_social(nombre_proveedor);
                bcProveedor.insert_rf_tc_proveedores(conexionContabilidad);         
              } 
              proveedor=lsIdContable;
              resultado=registro.AgregaCuentaContable("8241","1",crsCompromisoDevengado.getString("programa"),crsCompromisoDevengado.getString("unidad"),crsCompromisoDevengado.getString("estado"),tigaCapitulo,lsIdContable,"0000","0000",fecha,nombre_proveedor,"7",pCatalogoCuenta);
              if (capitulo.equals("5000")){
                  resultado=registro.AgregaCuentaContable("2112","5",programa,unidad,ambito,lsIdContable,"0000","0000","0000",fecha,nombre_proveedor,"6",pCatalogoCuenta);
              }

              hm=creaHashMap(numForma);
              cadenaTem.append(Cadena.construyeCadena(hm));
              cadenaTem.append("~");
              hm.clear();               
            }
            if (bandera==true){
              cadena.append(cadenaTem.toString()); 
            }                
        }catch (Exception e){
            System.out.println ("Ha ocurrido un error en el metodo registroContableCompromisoDevengado");
            crsCompromisoDevengado.close();
            crsCompromisoDevengado=null;
            throw e;
        } finally {
            }
       return cadena.toString();
    }    


}
