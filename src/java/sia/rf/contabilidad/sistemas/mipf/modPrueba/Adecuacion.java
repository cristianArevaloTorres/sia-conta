package sia.rf.contabilidad.sistemas.mipf.modPrueba;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;

import sia.rf.contabilidad.registroContableEvento.Cadena;

import sun.jdbc.rowset.CachedRowSet;

public class Adecuacion {
    public Adecuacion() {
    
    }

    private String unidad;
    private String ambito;
    private String tiga;
    private String entidad;
    private String concepto;
    private String programa;
    
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

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getConcepto() {
        return concepto;
    }    
    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getPrograma() {
        return programa;
    }    
    public String registroContable (Connection con,  String fecha, String ejercicio, String oficio) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
    boolean bandera=false;
    StringBuffer cadenaTem= new StringBuffer("");
    StringBuffer cadena= new StringBuffer("");
    StringBuffer SQL = new StringBuffer("SELECT ");
    SQL.append("programa, decode(unidad,'0104',decode(programa,'0008','0118','0117'),'0108','0118',unidad) unidad, decode(unidad,'0100','0011',estado) estado, entidad, oficio, tipomod, tiga, concepto, fechaaut, fechacap, decode(unidad_afecta,'0104',decode(programa,'0008','0118','0117'),'0108','0118',unidad_afecta) unidad_afecta, estado_afecta,  ");
    SQL.append("sum(mes1) as mes1, sum(mes2) as mes2, sum(mes3)as mes3, sum(mes4) as mes4, sum(mes5) as mes5, sum(mes6) as mes6, ");
    SQL.append("sum(mes7) as mes7, sum(mes8) as mes8, sum(mes9)as mes9, sum(mes10) as mes10, sum(mes11) as mes11, sum(mes12) as mes12, ");
    SQL.append("sum(mes1+mes2+mes3+mes4+mes5+mes6+mes7+mes8+mes9+mes10+mes11+mes12) as total ");
    SQL.append("FROM ");
    SQL.append("  (  ");              
    SQL.append("    SELECT distinct ");
    SQL.append("           afecta.id_adecuacion , ");
    SQL.append("           afecta.consecutivo as oficio, ");
    SQL.append("           F_PROGRAMA(cvep.clave_presupuestaria_id)  programa, "); 
    SQL.append("           substr(cvep.clave_programatica,1,2) as actividad, ");
    SQL.append("           substr(cvep.clave_programatica,3,3) as prioridad, ");
    SQL.append("           substr(cvep.clave_programatica,6,2) as proceso, ");
    SQL.append("           substr(cvep.clave_programatica,8,2) as subproceso, ");
    SQL.append("           substr(cvep.clave_programatica,10,2) as macro, ");
    SQL.append("           lpad(cvep.unidad_ejecutora,4,'0') as unidad, ");
    SQL.append("           lpad(substr(afecta.consecutivo,1,3),4,'0') as unidad_afecta, ");    
    SQL.append("           to_number(substr(afecta.consecutivo,4,2)) as entidad, ");    
    SQL.append("           (lpad(cvep.estado,3,'0') || cvep.ambito) as estado, ");
    SQL.append("           lpad(substr(afecta.consecutivo,4,3),4,'0') as estado_afecta, ");        
    SQL.append("           cvep.partida, afectadet.id_tipo_mov as tipomod, ");
    SQL.append("           case when substr(cvep.PARTIDA,1,1) ='1' then '0001' ");     
    SQL.append("           else '0002' ");
    SQL.append(" 	   end as tiga, ");
    SQL.append("	   (afectadet.mes1) as mes1, (afectadet.mes2) as mes2, (afectadet.mes3) as mes3, ");
    SQL.append("	   (afectadet.mes4) as mes4, (afectadet.mes5) as mes5, (afectadet.mes6) as mes6, ");
    SQL.append("	   (afectadet.mes7) as mes7, (afectadet.mes8) as mes8, (afectadet.mes9) as mes9, ");
    SQL.append("           (afectadet.mes10) as mes10, (afectadet.mes11) as mes11, (afectadet.mes12) as mes12, ");
    SQL.append("  	   to_char(afecta.fecha_estatus,'MM/DD/YYYY') as fechaaut, ");
    SQL.append(" 	   to_char(afecta.fecha_elabora,'MM/DD/YYYY') as fechacap, ");
    SQL.append(" 	   afecta.num_empleado as ident, ");
    SQL.append(" 	   decode(afecta.id_tipo_adecuacion,1,'ADECUACIÓN PRESUPUESTARIA',2,'REDUCCIONES LÍQUIDAS','AMPLIACIONES LÍQUIDAS ')||' : '||replace(replace(afecta.justificacion ,chr(39),''),chr(124),'') as CONCEPTO, ");
    SQL.append("	   afectadet.id_det_adecuacion as detalle ");
    SQL.append("	   FROM ");
    SQL.append("	     rf_tr_detalle_adecua_presup afectadet ");
    SQL.append("	     Inner join rf_tr_adecuaciones_presup afecta on afecta.id_adecuacion = afectadet.id_adecuacion ");
    SQL.append("	     Inner join rf_tv_claves_presupuestarias cvep on cvep.clave_presupuestaria_id = afectadet.clave_presupuestaria_id "); 
    SQL.append("	    WHERE ");
    SQL.append(" 	     afecta.id_estatus_adecuacion = 8 ");
    SQL.append("	     and afecta.consecutivo='").append(oficio).append("' and afecta.ejercicio=").append(ejercicio);
    SQL.append("   ) ");
    SQL.append(" GROUP BY "); 
    SQL.append(" programa,  oficio, unidad, estado, entidad, tipomod, tiga, fechaaut, fechacap, concepto, unidad_afecta, estado_afecta"); 
    
    try {
	crs = new CachedRowSet();
	crs.setCommand(SQL.toString());
      
	crs.execute(con);
	crs.beforeFirst();
	while (crs.next()){
          bandera=true; 
          hm.put ("PROGRAMA",crs.getString("programa"));//imprimeHM(hm);
          hm.put ("UNIDAD",crs.getString("unidad"));//imprimeHM(hm);
          hm.put ("AMBITO",crs.getString("estado"));//imprimeHM(hm);
          hm.put ("TIGA",crs.getString("tiga"));//imprimeHM(hm);
          hm.put ("REFERENCIA",crs.getString("oficio"));//imprimeHM(hm);
          unidad=crs.getString("unidad_afecta");
          ambito=crs.getString("estado_afecta");
          tiga=crs.getString("tiga");
          entidad=crs.getString("entidad");     
          concepto=crs.getString("concepto");    
          programa=crs.getString("programa");    
          if (crs.getString("tipomod").equals("1")){
            hm.put ("IMPORTE_PRESUP_NEG",crs.getString("total"));//imprimeHM(hm);  
            hm.put ("IMPORTE_REDUCCION",crs.getString("total"));//imprimeHM(hm);
          }
          else{
            hm.put ("IMPORTE_PRESUP_POS",crs.getString("total"));//imprimeHM(hm);  
            hm.put ("IMPORTE_AMPLIACION",crs.getString("total"));//imprimeHM(hm);
          }
          cadenaTem.append(Cadena.construyeCadena(hm));
          cadenaTem.append("~");
          hm.clear();
        }
        
        if (bandera==true){
         // cadena.append("~");
          cadena.append(cadenaTem.toString()); 
        }  
	
    }catch (Exception e){
	System.out.println ("Ha ocurrido un error en el metodo RegistroContable");
	System.out.println ("RegistroContable:" + SQL.toString());
	crs.close();
	crs=null;
	throw e;
    } finally {
	SQL.setLength(0);
	SQL=null;
	crs=null;
	}
	return cadena.toString();
	}



}
