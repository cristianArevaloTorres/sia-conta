package sia.rf.contabilidad.sistemas.inventarios;

import java.util.HashMap;

import java.sql.Connection;
import java.sql.SQLException;

import sia.rf.contabilidad.registroContableEvento.Cadena;

import sia.rf.contabilidad.registroContableNuevo.bcProveedores;

import sia.ws.publicar.contabilidad.Registro;

import sun.jdbc.rowset.CachedRowSet;

public class SiafmInventarios {

    private String rfc;
    private String nombre_proveedor;
    private String unidad;
    private String ambito;
    private String concepto;
    private String ejercicio;
    private String mes;
    private String tipo;
    private String entidad;
    String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre" };
    
    public SiafmInventarios() {
    }
    
  //= 
     public String getRfc() {
	return rfc;
    }
     public String getNombreProveedor() {
	return nombre_proveedor;
    }
     public String getUnidad() {
	return unidad;
    }
     public String getAmbito() {
	return ambito;
    }
     public String getConcepto() {
	return concepto;
    }
     public String getEjercicio() {
	return ejercicio;
    }
     public String getMes() {
	return mes;
    }
    public String getTipo() {
	return tipo;
    }
 
  public String registroActivoContable (Connection conInvenario,String fecha, String numero, String unidad, String entidad, String ambito, Connection conContabilidad, String idCatalogo, Registro registro,String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  bcProveedores bcProveedor = null;
    String resultado = null;
    StringBuffer cadenaTem= new StringBuffer("");
	  String partida52101 = "|5100|5200|5300|5400|5500|5900|6000|";
    String descripcion = null;
    StringBuffer SQL = new StringBuffer("select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,c.proveedor_id prov,d.rfc_proveedor rfc, d.razon_social nombre_proveedor,");
    SQL.append (" sum(b.precio) importe_antes_iva, sum(b.precio*(b.iva/100)) iva, sum(b.precio*(1+b.iva/100)) importe_neto ");
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d, rm_tr_distribucion e, rm_rh_concentrado_inst f "); 
    SQL.append (" where a.documento_adquisicion_id in (2,3,4,5) ");
    SQL.append (" and a.numero= '" + numero + "'");
    SQL.append (" and b.partida= '" + partida + "'");
    SQL.append (" and b.inciso= '" + inciso + "'");
    SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.proveedor_id=b.proveedor_id ");
    SQL.append (" and a.adquisicion_id=b.adquisicion_id "); 
    SQL.append (" and b.proveedor_id=c.proveedor_id ");
    SQL.append (" and b.adquisicion_id=c.adquisicion_id ");
    SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id ");
    SQL.append (" and c.proveedor_id=d.proveedor_id ");
    SQL.append (" and c.proveedor_id=e.proveedor_id ");
    SQL.append (" and c.adquisicion_id=e.adquisicion_id ");
    SQL.append (" and c.detalle_adquisicion_id=e.detalle_adquisicion_id ");
    SQL.append (" and c.distribucion_id=e.distribucion_id ");
    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) "); 
	  SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0),c.proveedor_id,d.rfc_proveedor,d.razon_social,b.iva ");
    System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      bcProveedor = new bcProveedores();
      crs.setCommand(SQL.toString());
      crs.execute(conInvenario);
      crs.beforeFirst();
      while(crs.next()){
        if(crs.getString("conc").equals("5100")){
          descripcion = "IVA DE MOBILIARIO Y EQUIPO DE ADMINISTRACIÓN";
        }else{
          if(crs.getString("conc").equals("5200")){
            descripcion = "IVA DE EQUIPO AGROINDUSTRIAL, COMUNICACIONES Y USO INFORMÁTICO";
          }else{
            if(crs.getString("conc").equals("5300")){
              descripcion = "IVA DE VEHICULOS TERRESTRES, MARITIMOS Y ÁREOS";
            }else{
               if(crs.getString("conc").equals("5400")){
                 descripcion = "IVA DE EQUIPO INSTRUMENTAL MEDICO Y DE LABORATORIO"; 
               }else{
                  if(crs.getString("conc").equals("5500")){
                    descripcion = "IVA DE HERRAMIENTAS Y REFACCIONES"; 
                  }else{
                     if(crs.getString("conc").equals("5900")){
                       descripcion = "IVA DE OTROS BIENES MUEBLES"; 
                     }
                     else{
                       descripcion = "IVA DE OBRA"; 
                     }
                  } 
               }
            }
          }
        }
        String lsIdContable=bcProveedor.select_rf_tc_proveedores_rfc(conContabilidad,crs.getString("rfc"));
        if (lsIdContable.equals("0")){
          lsIdContable=bcProveedor.select_SEQ_rf_tc_proveedores(conContabilidad);
          hm.put ("PROVEEDOR",lsIdContable);//imprimeHM(hm);
          bcProveedor.setId_contable(lsIdContable);
          bcProveedor.setRfc(crs.getString("rfc"));
          bcProveedor.setNombre_razon_social(crs.getString("nombre_proveedor"));
          bcProveedor.insert_rf_tc_proveedores(conContabilidad);
        }
        else{
          hm.put ("PROVEEDOR",lsIdContable);//imprimeHM(hm);
        }    
        resultado=registro.AgregaCuentaContable("11106","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),lsIdContable,"0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
        resultado=registro.AgregaCuentaContable("21203","0001",crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000",fecha,crs.getString("nombre_proveedor"),"5",idCatalogo);
        
        if(partida52101.contains(crs.getString("conc")))
          resultado=registro.AgregaCuentaContable("52101","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("IMPORTE_ADQUISICION",crs.getString("importe_antes_iva"));//1imprimeHM(hm);
        hm.put ("IMPORTE_COSTO",crs.getString("iva"));//imprimeHM(hm);
        hm.put ("IMPORTE_PROVEEDORES",crs.getString("importe_neto"));//imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        rfc = crs.getString("rfc");
        nombre_proveedor = crs.getString("nombre_proveedor");
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo RegistroActivoContable");
      System.out.println ("RegistroActivoContable: " + SQL.toString());
      crs.close();
      crs=null;
      throw e; 
    } finally {
      SQL.setLength(0);
      SQL=null;
      crs=null;
	}
	  return cadenaTem.toString();
	}
  
  public String Forma7 (Connection conInventarios,String fecha, String numero, String unidad, String entidad, String ambito, Connection conContabilidad, String idCatalogo, Registro registro, String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  bcProveedores bcProveedor = null;
	  String resultado = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  String partida12203 = "|5200|5400|5500|5900|";
          
	  int liMes = 0;
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,c.proveedor_id prov,d.rfc_proveedor rfc, d.razon_social nombre_proveedor," ); 
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',f.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,'53',f.valor_bien,'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d, rm_tr_distribucion e, rm_rh_concentrado_inst f " ); 
    SQL.append (" where a.documento_adquisicion_id in (2,3,4,5) " ); 
    SQL.append (" and a.numero= '" + numero + "'");
    SQL.append (" and b.partida= '" + partida + "'");
    SQL.append (" and b.inciso= '" + inciso + "'");
	  SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
    SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
    SQL.append (" and b.proveedor_id=c.proveedor_id " );
    SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
    SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
    SQL.append (" and c.proveedor_id=d.proveedor_id" );
    SQL.append (" and c.proveedor_id=e.proveedor_id ");
    SQL.append (" and c.adquisicion_id=e.adquisicion_id ");
    SQL.append (" and c.detalle_adquisicion_id=e.detalle_adquisicion_id ");
    SQL.append (" and c.distribucion_id=e.distribucion_id "); 
    SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" and c.control_inventario_id=f.control_inventario_id "); 
    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) "); 
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0)," ); 
    SQL.append (" c.proveedor_id,d.rfc_proveedor,d.razon_social," );
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0) " );
	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      bcProveedor = new bcProveedores();
      crs.setCommand(SQL.toString());
      crs.execute(conInventarios);
      crs.beforeFirst();
      while(crs.next()){
        String lsIdContable=bcProveedor.select_rf_tc_proveedores_rfc(conContabilidad,crs.getString("rfc"));
        if (lsIdContable.equals("0")){
          lsIdContable=bcProveedor.select_SEQ_rf_tc_proveedores(conContabilidad);
          hm.put ("PROVEEDOR",lsIdContable);//imprimeHM(hm);
          bcProveedor.setId_contable(lsIdContable);
          bcProveedor.setRfc(crs.getString("rfc"));
          bcProveedor.setNombre_razon_social(crs.getString("nombre_proveedor"));
          bcProveedor.insert_rf_tc_proveedores(conContabilidad);          
        }
        else{
          hm.put ("PROVEEDOR",lsIdContable);//imprimeHM(hm);
        }     
        resultado=registro.AgregaCuentaContable("11106","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),lsIdContable,"0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
        
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Vehículos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida12203.contains(crs.getString("conc"))){
          String descripcion="";           
          switch (Integer.parseInt(crs.getString("conc"))){
             case 5200 :{ descripcion="Equipo agroindustrial";  break;}
             case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
             case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
             case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
          }
          resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_ADQUISICION",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        rfc = crs.getString("rfc");
        nombre_proveedor = crs.getString("nombre_proveedor");
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma7");
      System.out.println ("Forma7:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
      SQL.setLength(0);
      SQL=null;
      crs=null;
	  }
	  return cadenaTem.toString();
	}
  public String Forma9 (Connection con,String fecha, String orden, String tsTraspaso, String ejercicio, String unidad, String entidad, String ambito, String idCatalogo, Registro registro) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  String resultado = "";
	  String partida41201 = "|5100|5200|5300|5400|5500|5900|6000|";
	  String partida12206 = "|5100|5200|5300|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer("select lpad(a.unidad_ejecutora_emisora,4,0) unid,lpad(a.coord_estatal_origen||a.ambito_origen,4,0) ambi,a.unidad_ejecutora_emisora unidad, a.coord_estatal_origen entidad, a.ambito_origen ambito, rpad(substr(c.partida_presupuestal,1,2),4,0) conc, ");
    SQL.append (" c.año_afectacion ejer, lpad(substr(c.fecha_factura,4,2),4,0) mes, ");
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,'53',c.valor_bien,'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_todos" );
    SQL.append (" from rm_tr_orden_traspaso a, rm_tr_detalle_traspaso b,rm_tr_control_inventario c ");
    SQL.append (" where a.orden_traspaso_id= '" + orden + "' and a.tipo_solic_traspaso_id =" + tsTraspaso + " and a.ejercicio=" + ejercicio );  
	  SQL.append (" and to_date(to_char(a.fecha_atencion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.orden_traspaso_id=b.orden_traspaso_id ");
    SQL.append (" and a.tipo_solic_traspaso_id=b.tipo_solic_traspaso_id ");
    SQL.append (" and a.ejercicio=b.ejercicio ");
    SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
	 //SQL.append (" and a.unidad_ejecutora_emisora='"+unidad+"' and a.coord_estatal_origen="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora_emisora,4,0),lpad(a.coord_estatal_origen||a.ambito_origen,4,0),a.unidad_ejecutora_emisora,a.coord_estatal_origen, a.ambito_origen,rpad(substr(c.partida_presupuestal,1,2),4,0), ");
    SQL.append (" c.año_afectacion, lpad(substr(c.fecha_factura,4,2),4,0) ");
SQL.append (" union all ");
    SQL.append (" select lpad(a.unidad_ejecutora_emisora,4,0) unid,lpad(a.coord_estatal_origen||a.ambito_origen,4,0) ambi,a.unidad_ejecutora_emisora unidad, a.coord_estatal_origen entidad, a.ambito_origen ambito, rpad(substr(c.partida_presupuestal,1,2),4,0) conc, ");
    SQL.append (" c.año_afectacion ejer, lpad(substr(c.fecha_factura,4,2),4,0) mes, ");
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,'53',c.valor_bien,'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_todos" );
    SQL.append (" from rm_tr_orden_traspaso a, rm_tr_detalle_traspaso b,rm_rh_control_inventario c ");
    SQL.append (" where a.orden_traspaso_id= '" + orden + "' and a.tipo_solic_traspaso_id =" + tsTraspaso + " and a.ejercicio=" + ejercicio );  
	  SQL.append (" and to_date(to_char(a.fecha_atencion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.orden_traspaso_id=b.orden_traspaso_id ");
    SQL.append (" and a.tipo_solic_traspaso_id=b.tipo_solic_traspaso_id ");
    SQL.append (" and a.ejercicio=b.ejercicio ");
    SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
	  //SQL.append (" and a.unidad_ejecutora_emisora='"+unidad+"' and a.coord_estatal_origen="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora_emisora,4,0),lpad(a.coord_estatal_origen||a.ambito_origen,4,0),a.unidad_ejecutora_emisora,a.coord_estatal_origen, a.ambito_origen,rpad(substr(c.partida_presupuestal,1,2),4,0), ");
    SQL.append (" c.año_afectacion, lpad(substr(c.fecha_factura,4,2),4,0) ");

	  System.out.println(SQL.toString());
    try {
     
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
        if(partida41201.contains(crs.getString("conc"))){
           resultado=registro.AgregaCuentaContable("41201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0004",crs.getString("ejer"),fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);                            
        }
        if(partida12206.contains(crs.getString("conc"))){
          resultado=registro.AgregaCuentaContable("12206","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
        }
        String cantidad=(crs.getString("conc_todos"));
        if (!cantidad.equals("0")) {
          hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
          hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
          hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
          hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
          hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
          hm.put ("IMPORTE_DECREMENTOS",crs.getString("conc_todos"));//1imprimeHM(hm);
          hm.put ("IMPORTE_REMESAS",crs.getString("conc_todos"));//1imprimeHM(hm);
          hm.put ("REFERENCIA"," ");//imprimeHM(hm);
          cadenaTem.append(Cadena.construyeCadena(hm));
          cadenaTem.append("~");
          hm.clear();
          this.unidad = crs.getString("unidad");
          this.ambito = crs.getString("ambito");
          this.entidad = crs.getString("entidad");
          concepto = crs.getString("conc");
          this.ejercicio = crs.getString("ejer");
          mes = crs.getString("mes");
        }
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma9");
      System.out.println ("Forma9:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
      } finally {
          SQL.setLength(0);
          SQL=null;
          crs=null;
      }
	  return cadenaTem.toString();
	}

  public String Forma10 (Connection con,String fecha, String orden, String tsTraspaso, String ejercicio, String unidad, String entidad, String ambito, String idCatalogo, Registro registro) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  String resultado = "";
	  int liMes = 0;
	  String partida12203 = "|5200|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,2),4,0) conc, ");
    SQL.append (" c.año_afectacion ejer,lpad(substr(c.fecha_factura,4,2),4,0) mes," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'53',c.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,'53',c.valor_bien,'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_orden_traspaso a, rm_tr_detalle_traspaso b,rm_tr_control_inventario c ");
    SQL.append (" where a.orden_traspaso_id= '" + orden + "' and a.tipo_solic_traspaso_id =" + tsTraspaso + " and a.ejercicio=" + ejercicio );  
	  SQL.append (" and to_date(to_char(a.fecha_atencion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.orden_traspaso_id=b.orden_traspaso_id ");
    SQL.append (" and a.tipo_solic_traspaso_id=b.tipo_solic_traspaso_id ");
    SQL.append (" and a.ejercicio=b.ejercicio ");
    SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
	  SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,2),4,0), ");
    SQL.append (" c.año_afectacion, lpad(substr(c.fecha_factura,4,2),4,0) ");
SQL.append (" union all ");
    SQL.append (" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,2),4,0) conc, ");
    SQL.append (" c.año_afectacion ejer,lpad(substr(c.fecha_factura,4,2),4,0) mes," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'53',c.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,'53',c.valor_bien,'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_orden_traspaso a, rm_tr_detalle_traspaso b,rm_rh_control_inventario c ");
    SQL.append (" where a.orden_traspaso_id= '" + orden + "' and a.tipo_solic_traspaso_id =" + tsTraspaso + " and a.ejercicio=" + ejercicio );  
	  SQL.append (" and to_date(to_char(a.fecha_atencion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.orden_traspaso_id=b.orden_traspaso_id ");
    SQL.append (" and a.tipo_solic_traspaso_id=b.tipo_solic_traspaso_id ");
    SQL.append (" and a.ejercicio=b.ejercicio ");
    SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
	  SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,2),4,0), ");
    SQL.append (" c.año_afectacion, lpad(substr(c.fecha_factura,4,2),4,0) ");

	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        resultado=registro.AgregaCuentaContable("41202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0004",crs.getString("ejer"),fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_INCREMENTOS_TRANS",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        this.ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma10");
      System.out.println ("Forma10:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
      SQL.setLength(0);
      SQL=null;
      crs=null;
	  }
	  return cadenaTem.toString();
	}
  
  public String Forma8 (Connection con,String fecha, String orden, String tsTraspaso, String ejercicio, String unidad, String entidad, String ambito, String idCatalogo, Registro registro) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  String resultado = "";
	  int liMes = 0;
	  String partida12203 = "|5200|5400|5500|5900|";
	  String partida12206 = "|5100|5200|5300|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora_emisora,4,0) unid,lpad(a.coord_estatal_origen||a.ambito_origen,4,0) ambi,a.unidad_ejecutora_emisora unidad,a.coord_estatal_origen entidad, a.ambito_origen ambito, rpad(substr(c.partida_presupuestal,1,2),4,0) conc, ");
    SQL.append (" c.año_afectacion ejer, lpad(substr(c.fecha_factura,4,2),4,0) mes," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'53',c.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,'53',c.valor_bien,'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_orden_traspaso a, rm_tr_detalle_traspaso b,rm_tr_control_inventario c ");
    SQL.append (" where a.orden_traspaso_id= '" + orden + "' and a.tipo_solic_traspaso_id =" + tsTraspaso + " and a.ejercicio=" + ejercicio );  
	  SQL.append (" and to_date(to_char(a.fecha_atencion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.orden_traspaso_id=b.orden_traspaso_id "); 
    SQL.append (" and a.tipo_solic_traspaso_id=b.tipo_solic_traspaso_id ");
    SQL.append (" and a.ejercicio=b.ejercicio ");  
    SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
	//SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora_emisora,4,0),lpad(a.coord_estatal_origen||a.ambito_origen,4,0),a.unidad_ejecutora_emisora, a.coord_estatal_origen, a.ambito_origen, rpad(substr(c.partida_presupuestal,1,2),4,0), ");
    SQL.append (" c.año_afectacion, lpad(substr(c.fecha_factura,4,2),4,0) ");
SQL.append (" union all ");
SQL.append (" select lpad(a.unidad_ejecutora_emisora,4,0) unid,lpad(a.coord_estatal_origen||a.ambito_origen,4,0) ambi,a.unidad_ejecutora_emisora unidad,a.coord_estatal_origen entidad, a.ambito_origen ambito, rpad(substr(c.partida_presupuestal,1,2),4,0) conc, ");
    SQL.append (" c.año_afectacion ejer, lpad(substr(c.fecha_factura,4,2),4,0) mes," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'53',c.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,'53',c.valor_bien,'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_orden_traspaso a, rm_tr_detalle_traspaso b,rm_rh_control_inventario c ");
    SQL.append (" where a.orden_traspaso_id= '" + orden + "' and a.tipo_solic_traspaso_id =" + tsTraspaso + " and a.ejercicio=" + ejercicio );  
	  SQL.append (" and to_date(to_char(a.fecha_atencion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.orden_traspaso_id=b.orden_traspaso_id "); 
    SQL.append (" and a.tipo_solic_traspaso_id=b.tipo_solic_traspaso_id ");
    SQL.append (" and a.ejercicio=b.ejercicio ");  
    SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
	 //SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora_emisora,4,0),lpad(a.coord_estatal_origen||a.ambito_origen,4,0),a.unidad_ejecutora_emisora, a.coord_estatal_origen, a.ambito_origen, rpad(substr(c.partida_presupuestal,1,2),4,0), ");
    SQL.append (" c.año_afectacion, lpad(substr(c.fecha_factura,4,2),4,0) ");

	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida12206.contains(crs.getString("conc")))
          resultado=registro.AgregaCuentaContable("12206","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);                            
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_REMESAS",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        this.unidad = crs.getString("unidad");
        this.ambito = crs.getString("ambito");
        this.entidad = crs.getString("entidad");
        concepto = crs.getString("conc");
        this.ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
      }
    }catch (Exception e){
        System.out.println ("Ha ocurrido un error en el metodo Forma8");
        System.out.println ("Forma8:" + SQL.toString());
        crs.close();
        crs=null;
        throw e;
    } finally {
        SQL.setLength(0);
        SQL=null;
        crs=null;
	}
	  return cadenaTem.toString();
	}
	
	
public String Forma12 (Connection con,String fecha, String numero,  String unidad, String entidad, String ambito,String idCatalogo, Registro registro, String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  String resultado = "";
	  int liMes = 0;
	  String partida12203 = "|5200|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,c.proveedor_id prov,d.rfc_proveedor rfc, d.razon_social nombre_proveedor," ); 
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',f.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,'53',f.valor_bien,'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d, rm_tr_distribucion e, rm_rh_concentrado_inst f " ); 
    SQL.append (" where a.documento_adquisicion_id in (7) " ); 
    SQL.append (" and a.numero= '" + numero + "'");
    SQL.append (" and b.partida= '" + partida + "'");
    SQL.append (" and b.inciso= '" + inciso + "'");
	  SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
    SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
    SQL.append (" and b.proveedor_id=c.proveedor_id " );
    SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
    SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
    SQL.append (" and c.proveedor_id=d.proveedor_id" ); 
    SQL.append (" and c.proveedor_id=e.proveedor_id ");
    SQL.append (" and c.adquisicion_id=e.adquisicion_id ");
    SQL.append (" and c.detalle_adquisicion_id=e.detalle_adquisicion_id ");
    SQL.append (" and c.distribucion_id=e.distribucion_id ");
    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) "); 
    SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0)," ); 
    SQL.append (" c.proveedor_id,d.rfc_proveedor,d.razon_social," );
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0) " );  
	  System.out.println(SQL.toString());
    try {
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    while(crs.next()){
      liMes=Integer.parseInt(crs.getString("mes"))-1;
      if (crs.getString("conc").equals("5100")){
        resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
        resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);          
        resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
      }
      if(crs.getString("conc").equals("5300")){
        resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
        resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);      
        resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
      }
      if(partida12203.contains(crs.getString("conc"))){
          String descripcion="";           
          switch (Integer.parseInt(crs.getString("conc"))){
             case 5200 :{ descripcion="Equipo agroindustrial";  break;}
             case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
             case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
             case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
          }
          resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
      }  
      hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
      hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
      hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
      hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
      hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
      hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
      hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
      hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
      hm.put ("IMPORTE_BIENES",crs.getString("conc_todos"));//1imprimeHM(hm);
      hm.put ("REFERENCIA"," ");//imprimeHM(hm);
      cadenaTem.append(Cadena.construyeCadena(hm));
      cadenaTem.append("~");
      hm.clear();
      rfc = crs.getString("rfc");
      nombre_proveedor = crs.getString("nombre_proveedor");
      this.unidad = crs.getString("unid");
      this.ambito = crs.getString("ambi");
      concepto = crs.getString("conc");
      ejercicio = crs.getString("ejer");
      mes = crs.getString("mes");
    }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma12");
      System.out.println ("Forma12:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
        SQL.setLength(0);
        SQL=null;
        crs=null;
    }
	  return cadenaTem.toString();
	}
  public String Forma13 (Connection con,String fecha, String numero, String unidad, String entidad, String ambito, String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
    StringBuffer SQL = new StringBuffer("select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,c.proveedor_id prov,d.rfc_proveedor rfc, d.razon_social nombre_proveedor, ");
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes, ");
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos" );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d,rm_tr_distribucion e, rm_rh_concentrado_inst f  ");
    SQL.append (" where a.documento_adquisicion_id in (7) ");
    SQL.append (" and a.numero= '" + numero + "'");
    SQL.append (" and b.partida= '" + partida + "'");
    SQL.append (" and b.inciso= '" + inciso + "'");
	  SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.proveedor_id=b.proveedor_id ");
    SQL.append (" and a.adquisicion_id=b.adquisicion_id ");  
    SQL.append (" and b.proveedor_id=c.proveedor_id ");
    SQL.append (" and b.adquisicion_id=c.adquisicion_id "); 
    SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id ");
    SQL.append (" and c.proveedor_id=d.proveedor_id ");
    SQL.append (" and c.proveedor_id=e.proveedor_id ");
    SQL.append (" and c.adquisicion_id=e.adquisicion_id ");
    SQL.append (" and c.detalle_adquisicion_id=e.detalle_adquisicion_id ");
    SQL.append (" and c.distribucion_id=e.distribucion_id ");
    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) ");  
    SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0),c.proveedor_id,d.rfc_proveedor,d.razon_social, ");
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0) ");
	  System.out.println(SQL.toString());
    try {
	  crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    while(crs.next()){
      String cantidad=(crs.getString("conc_todos"));
      if (!cantidad.equals("0")) {
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("IMPORTE_BIENES",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("IMPORTE_PATRIMONIALES",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        rfc = crs.getString("rfc");
        nombre_proveedor = crs.getString("nombre_proveedor");
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
      }
    }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma13");
      System.out.println ("Forma13:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
        SQL.setLength(0);
        SQL=null;
        crs=null;
    }
  return cadenaTem.toString();
	}

  public String Forma16 (Connection con,String fecha, String numero, String unidad, String entidad, String ambito, String idCatalogo, Registro registro, String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  int liMes = 0;
	  String partida12203 = "|5200|5400|5500|5900|";
	  String partida41202 = "|5100|5200|5300|5400|5500|5900|6000|";
    String resultado = "";
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,d.rfc_proveedor rfc, d.razon_social nombre_proveedor," ); 
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',f.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,'53',f.valor_bien,'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d, rm_tr_distribucion e, rm_rh_concentrado_inst f " ); 
    SQL.append (" where a.documento_adquisicion_id in (6) " ); 
    SQL.append (" and a.numero= '" + numero + "'");
    SQL.append (" and b.partida= '" + partida + "'");
    SQL.append (" and b.inciso= '" + inciso + "'");
    SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
    SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
    SQL.append (" and b.proveedor_id=c.proveedor_id " );
    SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
    SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
    SQL.append (" and c.proveedor_id=d.proveedor_id" ); 
    SQL.append (" and c.proveedor_id=e.proveedor_id ");
    SQL.append (" and c.adquisicion_id=e.adquisicion_id ");
    SQL.append (" and c.detalle_adquisicion_id=e.detalle_adquisicion_id ");
    SQL.append (" and c.distribucion_id=e.distribucion_id ");
    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) ");  
    SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0)," ); 
    SQL.append (" d.rfc_proveedor,d.razon_social," );
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0) " );
	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);          
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);          
        }
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);                                                  
        }
        if(partida41202.contains(crs.getString("conc")))
          resultado=registro.AgregaCuentaContable("41202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0003",crs.getString("ejer"),fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_INCREMENTOS_DONA",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        rfc = crs.getString("rfc");
        nombre_proveedor = crs.getString("nombre_proveedor");
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma16");
      System.out.println ("Forma16:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
      } finally {
        SQL.setLength(0);
        SQL=null;
        crs=null;
    }
	  return cadenaTem.toString();
	}
  
  public String Forma18 (Connection con,String fecha, String numero, String unidad, String entidad, String ambito, String idCatalogo, Registro registro, String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  int liMes = 0;
	  String resultado = "";
	  String partida12203 = "|5200|5400|5500|5900|";
	  String partida51201 = "|5100|5200|5300|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,d.rfc_proveedor rfc, d.razon_social nombre_proveedor," ); 
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',f.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,'53',f.valor_bien,'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d,rm_tr_distribucion e, rm_rh_concentrado_inst f " ); 
    SQL.append (" where a.documento_adquisicion_id in (11) " ); 
    SQL.append (" and a.numero= '" + numero + "'");
    SQL.append (" and b.partida= '" + partida + "'");
    SQL.append (" and b.inciso= '" + inciso + "'");
    SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
    SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
    SQL.append (" and b.proveedor_id=c.proveedor_id " );
    SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
    SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
    SQL.append (" and c.proveedor_id=d.proveedor_id" ); 
    SQL.append (" and c.proveedor_id=e.proveedor_id ");
    SQL.append (" and c.adquisicion_id=e.adquisicion_id ");
    SQL.append (" and c.detalle_adquisicion_id=e.detalle_adquisicion_id ");
    SQL.append (" and c.distribucion_id=e.distribucion_id ");
    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) "); 
	  SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0)," ); 
    SQL.append (" d.rfc_proveedor,d.razon_social," );
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0) " );
	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
       liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);                  
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida51201.contains(crs.getString("conc")))
          resultado=registro.AgregaCuentaContable("51201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0001",crs.getString("ejer"),fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO","1");//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_BENEFICIOS",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        rfc = crs.getString("rfc");
        nombre_proveedor = crs.getString("nombre_proveedor");
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
        tipo = crs.getString("tipo");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma18");
      System.out.println ("Forma18:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
     } finally {
        SQL.setLength(0);
        SQL=null;
        crs=null;
	  }
	  return cadenaTem.toString();
	}
  
  public String Forma19 (Connection con,String fecha, String numero, String unidad, String entidad, String ambito, String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
    String tipo = null;
	  StringBuffer cadenaTem= new StringBuffer("");
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,d.rfc_proveedor rfc, d.razon_social nombre_proveedor," ); 
    SQL.append (" f.año_afectacion ejer,lpad(substr(f.fecha_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',f.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,'53',f.valor_bien,'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d, rm_tr_distribucion e, rm_rh_concentrado_inst f " ); 
    SQL.append (" where a.documento_adquisicion_id in (8,13) " ); 
    SQL.append (" and a.numero= '" + numero + "'");
    SQL.append (" and b.partida= '" + partida + "'");
    SQL.append (" and b.inciso= '" + inciso + "'");
    SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
    SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
    SQL.append (" and b.proveedor_id=c.proveedor_id " );
    SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
    SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
    SQL.append (" and c.proveedor_id=d.proveedor_id" ); 
    SQL.append (" and c.proveedor_id=e.proveedor_id ");
    SQL.append (" and c.adquisicion_id=e.adquisicion_id ");
    SQL.append (" and c.detalle_adquisicion_id=e.detalle_adquisicion_id ");
    SQL.append (" and c.distribucion_id=e.distribucion_id ");
    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) ");  
	  SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0)," ); 
    SQL.append (" d.rfc_proveedor,d.razon_social," );
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0) " );
	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
        tipo = crs.getString("tipo").equals("0008")?"0002":crs.getString("tipo").equals("0013")?"0003":"0000";
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO",tipo);//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_BENEFICIOS",crs.getString("conc_todos"));//imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        rfc = crs.getString("rfc");
        nombre_proveedor = crs.getString("nombre_proveedor");
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
        tipo = crs.getString("tipo");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma19");
      System.out.println ("Forma19:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
        SQL.setLength(0);
        SQL=null;
        crs=null;
    }
	  return cadenaTem.toString();
	}
  
  public String Forma24 (Connection con,String fecha, String numero, String unidad, String entidad, String ambito, String idCatalogo, Registro registro, String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  int liMes = 0;
	  String resultado = "";
	  String partida12203 = "|5200|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,d.rfc_proveedor rfc, d.razon_social nombre_proveedor," ); 
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',f.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,'53',f.valor_bien,'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d, rm_tr_distribucion e, rm_rh_concentrado_inst f " ); 
    SQL.append (" where a.documento_adquisicion_id in (12) " ); 
    SQL.append (" and a.numero= '" + numero + "'");
    SQL.append (" and b.partida= '" + partida + "'");
    SQL.append (" and b.inciso= '" + inciso + "'");
	  SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
    SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
    SQL.append (" and b.proveedor_id=c.proveedor_id " );
    SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
    SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
    SQL.append (" and c.proveedor_id=d.proveedor_id" ); 
    SQL.append (" and c.proveedor_id=e.proveedor_id ");
    SQL.append (" and c.adquisicion_id=e.adquisicion_id ");
    SQL.append (" and c.detalle_adquisicion_id=e.detalle_adquisicion_id ");
    SQL.append (" and c.distribucion_id=e.distribucion_id ");
    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) ");  
    SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0)," ); 
    SQL.append (" d.rfc_proveedor,d.razon_social," );
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0)  " );
	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();   
      while(crs.next()){
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);          
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);        
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        resultado=registro.AgregaCuentaContable("12701","0001",crs.getString("unid"),crs.getString("ambi"),"5000",crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO",crs.getString("tipo"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_ACTIVOS",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        rfc = crs.getString("rfc");
        nombre_proveedor = crs.getString("nombre_proveedor");
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
        tipo = crs.getString("tipo");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma24");
      System.out.println ("Forma24:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
      SQL.setLength(0);
      SQL=null;
      crs=null;
	  }
	  return cadenaTem.toString();
	}
	
public String Forma11 (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito, String idCatalogo, Registro registro) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  String resultado = "";
	  int liMes = 0;
	  String partida12203 = "|5200|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,");
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',f.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,'53',f.valor_bien,'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_asociacion_lote a, rm_tr_conforma_lote_baja b, rm_tr_detalle_conforma c, rm_rh_control_inventario d, rm_rh_concentrado_inst f "); 
    SQL.append (" where a.disposicion_final_id = 6 " ); 
    SQL.append (" and a.nombre_evento= '" + nombre + "' ");
	  SQL.append (" and to_date(to_char(d.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.asociacion_lote_id=b.asociacion_lote_id ");
    SQL.append (" and a.ejercicio=b.ejercicio_asociacion ");
    SQL.append (" and b.conforma_lote_id=c.conforma_lote_id ");
    SQL.append (" and b.ejercicio=c.ejercicio ");
    SQL.append (" and c.control_inventario_id=d.control_inventario_id");
    SQL.append (" and d.control_inventario_id=f.control_inventario_id");
    SQL.append (" and trunc(d.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0),");
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0) ");
   // SQL.append (" union all ");
   // SQL.append(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(a.partida_presupuestal,1,2),4,0) conc,");
   // SQL.append (" a.año_afectacion ejer, lpad(substr(a.fecha_factura,4,2),4,0) mes," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,0)) conc_mob," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'53',a.valor_bien,0)) conc_vehi," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'52',a.valor_bien,'54',a.valor_bien,'55',a.valor_bien,'59',a.valor_bien,0)) conc_maq," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,'53',a.valor_bien,'52',a.valor_bien,'54',a.valor_bien,'55',a.valor_bien,'59',a.valor_bien,0)) conc_todos " );
   // SQL.append (" from rm_rh_control_inventario a "); 
   // SQL.append (" where a.disposicion_final_id = 6 " ); 
   // SQL.append (" and a.nombre_evento= '" + nombre + "' ");
	//  SQL.append (" and to_date(to_char(a.fecha_movimiento,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
	//  SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
  //  SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(a.partida_presupuestal,1,2),4,0),");
  //  SQL.append (" a.año_afectacion, lpad(substr(a.fecha_factura,4,2),4,0) ");
	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
        tipo = crs.getString("tipo").equals("0000")?"0005":crs.getString("tipo").equals("0007")?"0001":crs.getString("tipo").equals("0010")?"0005":crs.getString("tipo").equals("0012")?"0008":"0000";
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);          
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(crs.getString("conc").equals("5300")){
            resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_BIENES",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma11");
      System.out.println ("Forma11:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
      SQL.setLength(0);
      SQL=null;
      crs=null;
	}
	  return cadenaTem.toString();
	}
public String Forma14 (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
    StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,");
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes, ");
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos" );
    SQL.append (" from rm_tr_asociacion_lote a, rm_tr_conforma_lote_baja b, rm_tr_detalle_conforma c, rm_rh_control_inventario d, rm_rh_concentrado_inst f "); 
    SQL.append (" where "); 
    SQL.append (" a.nombre_evento= '" + nombre + "' ");
	  SQL.append (" and to_date(to_char(d.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.disposicion_final_id = 6 ");
    SQL.append (" and a.asociacion_lote_id=b.asociacion_lote_id ");
    SQL.append (" and a.ejercicio=b.ejercicio_asociacion ");
    SQL.append (" and b.conforma_lote_id=c.conforma_lote_id ");
    SQL.append (" and b.ejercicio=c.ejercicio ");
    SQL.append (" and c.control_inventario_id=d.control_inventario_id");
    SQL.append (" and d.control_inventario_id=f.control_inventario_id");
    SQL.append (" and trunc(d.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||d.ambito,4,0),rpad(substr(f.partida_presupuestal,1,2),4,0),");
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0) ");
   // SQL.append (" union all ");
   // SQL.append (" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(a.partida_presupuestal,1,2),4,0) conc,");
   // SQL.append (" a.año_afectacion ejer, lpad(substr(a.fecha_factura,4,2),4,0) mes, ");
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'52',a.valor_bien,'54',a.valor_bien,'55',a.valor_bien,'59',a.valor_bien,0)) conc_todos" );
   // SQL.append (" from rm_rh_control_inventario a");
   // SQL.append (" where "); 
   // SQL.append (" a.nombre_evento= '" + nombre + "' ");
	//  SQL.append (" and to_date(to_char(a.fecha_movimiento,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
	//  SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);

  //  SQL.append (" and a.disposicion_final_id = 6 ");
  //  SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(a.partida_presupuestal,1,2),4,0),");
  //  SQL.append (" a.año_afectacion, lpad(substr(a.fecha_factura,4,2),4,0) ");
	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
      String cantidad=(crs.getString("conc_todos"));
        if (!cantidad.equals("0")) {
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("IMPORTE_PATRIMONIALES",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("IMPORTE_BIENES",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
        }
      }
  }catch (Exception e){
    System.out.println ("Ha ocurrido un error en el metodo Forma14");
    System.out.println ("Forma14:" + SQL.toString());
    crs.close();
    crs=null;
    throw e;
    } finally {
    SQL.setLength(0);
    SQL=null;
    crs=null;
	}
	  return cadenaTem.toString();
	}

public String Forma17 (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito, String idCatalogo, Registro registro) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  String resultado = "";
	  int liMes = 0;
	  String partida41201 = "|5100|5200|5300|5400|5500|5900|6000|";
	  String partida12203 = "|5200|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,");
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',f.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,'53',f.valor_bien,'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_asociacion_lote a, rm_tr_conforma_lote_baja b, rm_tr_detalle_conforma c, rm_rh_control_inventario d, rm_rh_concentrado_inst f "); 
    SQL.append (" where a.disposicion_final_id = 3 " ); 
    SQL.append (" and a.nombre_evento= '" + nombre + "' ");
	  SQL.append (" and to_date(to_char(d.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.asociacion_lote_id=b.asociacion_lote_id ");
    SQL.append (" and a.ejercicio=b.ejercicio_asociacion ");
    SQL.append (" and b.conforma_lote_id=c.conforma_lote_id ");
    SQL.append (" and b.ejercicio=c.ejercicio ");
    SQL.append (" and c.control_inventario_id=d.control_inventario_id");
    SQL.append (" and d.control_inventario_id=f.control_inventario_id");
    SQL.append (" and trunc(d.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0),");
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0) ");
   // SQL.append (" union all ");
   // SQL.append (" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(a.partida_presupuestal,1,2),4,0) conc,");
   // SQL.append (" a.año_afectacion ejer, lpad(substr(a.fecha_factura,4,2),4,0) mes," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,0)) conc_mob," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'53',a.valor_bien,0)) conc_vehi," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'52',a.valor_bien,'54',a.valor_bien,'55',a.valor_bien,'59',a.valor_bien,0)) conc_maq," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,'53',a.valor_bien,'52',a.valor_bien,'54',a.valor_bien,'55',a.valor_bien,'59',a.valor_bien,0)) conc_todos " );
   // SQL.append (" from rm_rh_control_inventario a");
   // SQL.append (" where a.disposicion_final_id = 3 " ); 
   // SQL.append (" and a.nombre_evento= '" + nombre + "' ");
	//  SQL.append (" and to_date(to_char(a.fecha_movimiento,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
	//  SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
  //  SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(a.partida_presupuestal,1,2),4,0),");
  //  SQL.append (" a.año_afectacion, lpad(substr(a.fecha_factura,4,2),4,0) ");
	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
     
      while(crs.next()){
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);          
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida41201.contains(crs.getString("conc")))
          resultado=registro.AgregaCuentaContable("41201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0003",crs.getString("ejer"),fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);                 
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_DECREMENTOS",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma17");
      System.out.println ("Forma17:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
        SQL.setLength(0);
        SQL=null;
        crs=null;
	}
	  return cadenaTem.toString();
	}
  public String Forma22A (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito, String idCatalogo, Registro registro) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  String resultado = "";
	  int liMes = 0;
	  String partida12203 = "|5200|5400|5500|5900|";
	  String partida52202 = "|5100|5200|5300|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer(" select lpad(c.unidad_ejecutora,4,0) unid,lpad(c.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,a.numero_documento,");
   // SQL.append (" c.año_afectacion ejer, lpad(substr(c.fecha_factura,4,2),4,0) mes, lpad(1,4,0) tipo, ");
   // SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,0)) conc_mob," ); 
   // SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'53',c.valor_bien,0)) conc_vehi," );
   // SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_maq," );
   // SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,'53',c.valor_bien,'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_todos " );
   // SQL.append (" from  rm_tr_siniestro a, rm_tr_detalle_siniestro b,rm_rh_control_inventario c ");
   // SQL.append (" where "); 
   // SQL.append (" a.numero_documento= '" + nombre + "' ");	
   //  SQL.append (" and to_date(to_char(a.fecha_elaboracion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    //SQL.append (" and a.siniestro_id=b.siniestro_id ");
   // SQL.append (" and a.ejercicio=b.ejercicio ");
   // SQL.append (" and c.unidad_ejecutora='" +unidad+ "' and c.coord_estatal="+ entidad);
   // SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
   // SQL.append (" group by lpad(c.unidad_ejecutora,4,0),lpad(c.coord_estatal||'"+ambito+"',4,0),a.numero_documento,rpad(substr(c.partida_presupuestal,1,2),4,0),");
   // SQL.append (" c.año_afectacion, lpad(substr(c.fecha_factura,4,2),4,0),lpad(1,4,0) ");
   // SQL.append (" union all ");
   // SQL.append (" select lpad(c.unidad_ejecutora,4,0) unid,lpad(c.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,2),4,0) conc,a.numero_documento,");
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes, lpad(c.disposicion_final_id,4,0) tipo, ");
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',f.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,'53',f.valor_bien,'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos " );
    SQL.append (" from  rm_rh_siniestro a, rm_rh_detalle_siniestro b,rm_rh_control_inventario c, rm_rh_concentrado_inst f ");
    SQL.append (" where "); 
    SQL.append (" a.numero_documento= '" + nombre + "' ");
	  SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.siniestro_id=b.siniestro_id ");
    SQL.append (" and a.ejercicio=b.ejercicio ");
    SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
    SQL.append (" and trunc(c.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and c.disposicion_final_id in (1) ");
	  SQL.append (" and c.unidad_ejecutora='" +unidad+ "' and c.coord_estatal="+ entidad);
    SQL.append (" group by lpad(c.unidad_ejecutora,4,0),lpad(c.coord_estatal||'"+ambito+"',4,0),a.numero_documento,rpad(substr(f.partida_presupuestal,1,2),4,0),");
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0),lpad(c.disposicion_final_id,4,0) ");
	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();   
      while(crs.next()){
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);          
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(partida52202.contains(crs.getString("conc")))
          resultado=registro.AgregaCuentaContable("52202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0002",crs.getString("ejer"),fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO","2");//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_PERDIDAS",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("numero_documento");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
        tipo = crs.getString("tipo");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma22A");
      System.out.println ("Forma22A:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
        SQL.setLength(0);
        SQL=null;
        crs=null;
	}
	  return cadenaTem.toString();
	}
  
  public String Forma22B (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito, String idCatalogo, Registro registro) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  String resultado = "";
	  int liMes = 0;
	  String partida12203 = "|5200|5400|5500|5900|";
	  String partida52202 = "|5100|5200|5300|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer(" select lpad(c.unidad_ejecutora,4,0) unid,lpad(c.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,");
  //  SQL.append (" c.año_afectacion ejer, lpad(substr(a.fecha_elaboracion,4,2),4,0) mes,lpad(2,4,0) tipo, ");
  //  SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,0)) conc_mob," ); 
  //  SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'53',c.valor_bien,0)) conc_vehi," );
  //  SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_maq," );
  //  SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,'53',c.valor_bien,'52',c.valor_bien,'54',c.valor_bien,'55',c.valor_bien,'59',c.valor_bien,0)) conc_todos " );
  //  SQL.append (" from  rm_tr_pago_numerario a, rm_tr_detalle_pago_numerario b,rm_tr_control_inventario c ");
  //  SQL.append (" where "); 
  //  SQL.append (" a.nombre_lote= '" + nombre + "' ");
  //    SQL.append (" and to_date(to_char(a.fecha_elaboracion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
  //  SQL.append (" and a.pago_numerario_id=b.pago_numerario_id ");
   // SQL.append (" and a.ejercicio=b.ejercicio ");
  //  SQL.append (" and c.unidad_ejecutora='" +unidad+ "' and c.coord_estatal="+ entidad);
  //  SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
  //  SQL.append (" group by lpad(c.unidad_ejecutora,4,0),lpad(c.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,2),4,0),");
  //  SQL.append (" c.año_afectacion, lpad(substr(a.fecha_elaboracion,4,2),4,0),lpad(2,4,0) ");
  //  SQL.append (" union all ");
  //  SQL.append(" select lpad(c.unidad_ejecutora,4,0) unid,lpad(c.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,2),4,0) conc,");
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes,lpad(c.disposicion_final_id,4,0) tipo, ");
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',f.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,'53',f.valor_bien,'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos " );
    SQL.append (" from  rm_rh_pago_numerario a, rm_rh_detalle_pago_numerario b, rm_rh_control_inventario c, rm_rh_concentrado_inst f ");
    SQL.append (" where "); 
    SQL.append (" a.nombre_lote= '" + nombre + "' ");
	  SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.pago_numerario_id=b.pago_numerario_id ");
    SQL.append (" and a.ejercicio=b.ejercicio ");
	  SQL.append (" and c.unidad_ejecutora='" +unidad+ "' and c.coord_estatal="+ entidad);
    SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
    SQL.append (" and trunc(c.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and c.disposicion_final_id in (2) ");
    SQL.append (" group by lpad(c.unidad_ejecutora,4,0),lpad(c.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0),");
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0),lpad(c.disposicion_final_id,4,0) ");
	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();   
      while(crs.next()){
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);          
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);        
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(partida52202.contains(crs.getString("conc")))   
          resultado=registro.AgregaCuentaContable("52202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0009",crs.getString("ejer"),fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO","9");//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_PERDIDAS",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
        tipo = crs.getString("tipo");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma22B");
      System.out.println ("Forma22B:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
        SQL.setLength(0);
        SQL=null;
        crs=null;
	}
	  return cadenaTem.toString();
	}
public String Forma22C (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito, String idCatalogo, Registro registro) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  String resultado = "";
	  int liMes = 0;
	  String partida12203 = "|5200|5400|5500|5900|";
	  String partida52202 = "|5100|5200|5300|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer(" select lpad(c.unidad_ejecutora,4,0) unid,lpad(c.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,a.numero_documento,");
       SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes, lpad(c.disposicion_final_id,4,0) tipo, ");
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',f.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,'53',f.valor_bien,'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos " );
    SQL.append (" from  rm_rh_baja_reposicion a, rm_rh_detalle_baja_reposicion b, rm_rh_control_inventario c, rm_rh_concentrado_inst f ");
    SQL.append (" where "); 
    SQL.append (" a.numero_documento= '" + nombre + "' ");
	  SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.baja_reposicion_id=b.baja_reposicion_id ");
    SQL.append (" and a.ejercicio=b.ejercicio ");
    SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
    SQL.append (" and trunc(c.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and c.disposicion_final_id in (12) ");
	  SQL.append (" and c.unidad_ejecutora='" +unidad+ "' and c.coord_estatal="+ entidad);
    SQL.append (" group by lpad(c.unidad_ejecutora,4,0),lpad(c.coord_estatal||'"+ambito+"',4,0),a.numero_documento,rpad(substr(f.partida_presupuestal,1,2),4,0),");
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0),lpad(c.disposicion_final_id,4,0) ");
	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();  
      while(crs.next()){
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);                  
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);        
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(partida52202.contains(crs.getString("conc")))
          resultado=registro.AgregaCuentaContable("52202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0002",crs.getString("ejer"),fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO","0008");//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_PERDIDAS",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("numero_documento");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
        tipo = crs.getString("tipo");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma22C");
      System.out.println ("Forma22C:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
        SQL.setLength(0);
        SQL=null;
        crs=null;
	}
	  return cadenaTem.toString();
	}
  

public String Forma22 (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito, String idCatalogo, Registro registro) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
    String resultado = "";
    int liMes = 0;
    String partida12203 = "|5200|5400|5500|5900|";
    String partida52202 = "|5100|5200|5300|5400|5500|5900|";
    
    StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,");
    SQL.append (" f.año_afectacion ejer,lpad(substr(f.fecha_factura,4,2),4,0) mes,lpad(a.disposicion_final_id,4,0) tipo," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',f.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,'53',f.valor_bien,'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_asociacion_lote a, rm_tr_conforma_lote_baja b, rm_tr_detalle_conforma c, rm_rh_control_inventario d, rm_rh_concentrado_inst f "); 
    SQL.append (" where a.disposicion_final_id in (0,4,5,7,8,10,12) " ); 
    SQL.append (" and a.nombre_evento= '" + nombre + "' ");
	  SQL.append (" and to_date(to_char(d.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.asociacion_lote_id=b.asociacion_lote_id ");
    SQL.append (" and a.ejercicio=b.ejercicio_asociacion ");
    SQL.append (" and b.conforma_lote_id=c.conforma_lote_id ");
    SQL.append (" and b.ejercicio=c.ejercicio ");
    SQL.append (" and c.control_inventario_id=d.control_inventario_id");
    SQL.append (" and d.control_inventario_id=f.control_inventario_id");
    SQL.append (" and trunc(d.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and a.unidad_ejecutora='" +unidad+ "' and a.coord_estatal="+ entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0),");
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0),lpad(a.disposicion_final_id,4,0) ");
   // SQL.append (" union all ");
   // SQL.append(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(a.partida_presupuestal,1,2),4,0) conc,");
   // SQL.append (" a.año_afectacion ejer,lpad(substr(a.fecha_factura,4,2),4,0) mes,lpad(a.disposicion_final_id,4,0) tipo," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,0)) conc_mob," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'53',a.valor_bien,0)) conc_vehi," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'52',a.valor_bien,'54',a.valor_bien,'55',a.valor_bien,'59',a.valor_bien,0)) conc_maq," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,'53',a.valor_bien,'52',a.valor_bien,'54',a.valor_bien,'55',a.valor_bien,'59',a.valor_bien,0)) conc_todos " );
   // SQL.append (" from rm_rh_control_inventario a ");
   // SQL.append (" where a.disposicion_final_id in (0,4,5,7,8,10) " ); 
   // SQL.append (" and a.nombre_evento= '" + nombre + "' ");
   //  SQL.append (" and to_date(to_char(a.fecha_movimiento,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
   //  SQL.append (" and a.unidad_ejecutora='" +unidad+ "' and a.coord_estatal="+ entidad);
   // SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(a.partida_presupuestal,1,2),4,0),");
   // SQL.append (" a.año_afectacion, lpad(substr(a.fecha_factura,4,2),4,0),lpad(a.disposicion_final_id,4,0) ");
	  /*SiaFmInventarios.java Se modificaron las formas 19 y 22 que seguramente se probarán de nuevo:  el motivo textualmente 
    ?En la 19 se agrego el valor alta por reposición que fue un 13 y en la 22 se agrego el valor de baja por reposición que fue un 12?
    */
          System.out.println(SQL.toString());
     try {
      
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      String tipo = null;  
      while(crs.next()){
        tipo = crs.getString("tipo").equals("0000")?"0005":crs.getString("tipo").equals("0007")?"0001":crs.getString("tipo").equals("0010")?"0005":crs.getString("tipo").equals("0012")?"0008":"0000";
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);          
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);                                                  
        }
        if(partida52202.contains(crs.getString("conc")))
          resultado=registro.AgregaCuentaContable("52202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),tipo,crs.getString("ejer"),fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO",tipo);//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_PERDIDAS",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma22");
      System.out.println ("Forma22:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
        SQL.setLength(0);
        SQL=null;
        crs=null;
	}
	  return cadenaTem.toString();
	}

public String Forma23 (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito, String idCatalogo, Registro registro) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");   
	  String resultado = "";
	  int liMes = 0;
	  String partida12203 = "|5200|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc,");
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',f.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',f.valor_bien,'53',f.valor_bien,'52',f.valor_bien,'54',f.valor_bien,'55',f.valor_bien,'59',f.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_asociacion_lote a, rm_tr_conforma_lote_baja b, rm_tr_detalle_conforma c, rm_rh_control_inventario d, rm_rh_concentrado_inst f "); 
    SQL.append (" where a.disposicion_final_id in (9) " ); 
    SQL.append (" and a.nombre_evento= '" + nombre + "' ");
	  SQL.append (" and to_date(to_char(d.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.asociacion_lote_id=b.asociacion_lote_id ");
    SQL.append (" and a.ejercicio=b.ejercicio_asociacion ");
    SQL.append (" and b.conforma_lote_id=c.conforma_lote_id ");
    SQL.append (" and b.ejercicio=c.ejercicio ");
    SQL.append (" and c.control_inventario_id=d.control_inventario_id");
    SQL.append (" and d.control_inventario_id=f.control_inventario_id");
    SQL.append (" and trunc(d.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and a.unidad_ejecutora='" +unidad+ "' and a.coord_estatal="+ entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0), ");
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0) ");
   // SQL.append (" union all ");
   // SQL.append(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(a.partida_presupuestal,1,2),4,0) conc,");
   // SQL.append (" a.año_afectacion ejer, lpad(substr(a.fecha_factura,4,2),4,0) mes," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,0)) conc_mob," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'53',a.valor_bien,0)) conc_vehi," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'52',a.valor_bien,'54',a.valor_bien,'55',a.valor_bien,'59',a.valor_bien,0)) conc_maq," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,'53',a.valor_bien,'52',a.valor_bien,'54',a.valor_bien,'55',a.valor_bien,'59',a.valor_bien,0)) conc_todos " );
   // SQL.append (" from rm_rh_control_inventario a ");
   // SQL.append (" where a.disposicion_final_id in (9) " ); 
   // SQL.append (" and a.nombre_evento= '" + nombre + "' ");
	//SQL.append (" and to_date(to_char(a.fecha_movimiento,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
	//  SQL.append (" and a.unidad_ejecutora='" +unidad+ "' and a.coord_estatal="+ entidad);
  //  SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(a.partida_presupuestal,1,2),4,0), ");
  //  SQL.append (" a.año_afectacion, lpad(substr(a.fecha_factura,4,2),4,0) ");
	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();  
      while(crs.next()){
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);                   
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);        
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
          resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);        
          resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);                                                  
        }  
        resultado=registro.AgregaCuentaContable("12701","0001",crs.getString("unid"),crs.getString("ambi"),"5000",crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);   
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_ACTIVOS",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
      }
    }catch (Exception e){
	System.out.println ("Ha ocurrido un error en el metodo Forma23");
	System.out.println ("Forma23:" + SQL.toString());
	crs.close();
	crs=null;
	throw e;
    } finally {
	SQL.setLength(0);
	SQL=null;
	crs=null;
	}
	  return cadenaTem.toString();
	}
  
  public String Forma25 (Connection con,String fecha, String numero, String unidad, String entidad, String ambito, String idCatalogo, Registro registro, String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
    String tipo = null;
	  String resultado = "";
	  int liMes = 0;
	  String partida12203 = "|5200|5400|5500|5900|";
	  String partida51201 = "|5100|5200|5300|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc," ); 
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',e.valor_bien_actual-e.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',e.valor_bien_actual-e.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',e.valor_bien_actual-e.valor_bien,'54',e.valor_bien_actual-e.valor_bien,'55',e.valor_bien_actual-e.valor_bien,'59',e.valor_bien_actual-e.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',e.valor_bien_actual-e.valor_bien,'53',e.valor_bien_actual-e.valor_bien,'52',e.valor_bien_actual-e.valor_bien,'54',e.valor_bien_actual-e.valor_bien,'55',e.valor_bien_actual-e.valor_bien,'59',e.valor_bien_actual-e.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tr_valor_ajustes e, rm_rh_concentrado_inst f  " ); 
    SQL.append (" where " ); 
    SQL.append (" e.numero= '" + numero + "'");
    SQL.append (" and e.partida= '" + partida + "'");
    SQL.append (" and e.inciso= '" + inciso + "'");
	  SQL.append (" and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
    SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
    SQL.append (" and b.proveedor_id=c.proveedor_id " );
    SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
    SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
    SQL.append (" and c.control_inventario_id=e.control_inventario_id" );
    SQL.append (" and c.control_inventario_id=f.control_inventario_id" );
    SQL.append (" and trunc(e.fecha_conciliacion)=trunc(f.fecha_conciliacion) " );
	  SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0)," ); 
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0) " );
    SQL.append (" union all ");
    SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc," ); 
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',e.valor_bien_actual-e.valor_bien,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',e.valor_bien_actual-e.valor_bien,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',e.valor_bien_actual-e.valor_bien,'54',e.valor_bien_actual-e.valor_bien,'55',e.valor_bien_actual-e.valor_bien,'59',e.valor_bien_actual-e.valor_bien,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',e.valor_bien_actual-e.valor_bien,'53',e.valor_bien_actual-e.valor_bien,'52',e.valor_bien_actual-e.valor_bien,'54',e.valor_bien_actual-e.valor_bien,'55',e.valor_bien_actual-e.valor_bien,'59',e.valor_bien_actual-e.valor_bien,0)) conc_todos " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_rh_control_inventario c, rm_tr_valor_ajustes e, rm_rh_concentrado_inst f  " ); 
    SQL.append (" where " ); 
    SQL.append (" e.numero= '" + numero + "'");
    SQL.append (" and e.partida= '" + partida + "'");
    SQL.append (" and e.inciso= '" + inciso + "'");
	  SQL.append (" and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
    SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
    SQL.append (" and b.proveedor_id=c.proveedor_id " );
    SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
    SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
    SQL.append (" and c.control_inventario_id=e.control_inventario_id" );
    SQL.append (" and c.control_inventario_id=f.control_inventario_id" );
    SQL.append (" and trunc(e.fecha_conciliacion)=trunc(f.fecha_conciliacion) " );
	  SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0)," ); 
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0) " );
	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();    
      while(crs.next()){
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);                   
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);        
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(partida51201.contains(crs.getString("conc")))
          resultado=registro.AgregaCuentaContable("51201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("tipo"),fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO","0003");//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_BENEFICIOS",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");;
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
        tipo = crs.getString("tipo");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma25");
      System.out.println ("Forma25:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
        SQL.setLength(0);
        SQL=null;
        crs=null;
	  }
	  return cadenaTem.toString();
	}
  
  public String Forma26 (Connection con,String fecha, String numero, String unidad, String entidad, String ambito,String idCatalogo, Registro registro, String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
    String tipo = null;
	  String resultado = "";
	  int liMes = 0;
	  String partida12203 = "|5200|5400|5500|5900|";
	  String partida52202 = "|5100|5200|5300|5400|5500|5900|";
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc," ); 
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',(e.valor_bien_actual-e.valor_bien)*-1,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',(e.valor_bien_actual-e.valor_bien)*-1,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',(e.valor_bien_actual-e.valor_bien)*-1,'54',(e.valor_bien_actual-e.valor_bien)*-1,'55',(e.valor_bien_actual-e.valor_bien)*-1,'59',(e.valor_bien_actual-e.valor_bien)*-1,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',(e.valor_bien_actual-e.valor_bien)*-1,'53',(e.valor_bien_actual-e.valor_bien)*-1,'52',(e.valor_bien_actual-e.valor_bien)*-1,'54',(e.valor_bien_actual-e.valor_bien)*-1,'55',(e.valor_bien_actual-e.valor_bien)*-1,'59',(e.valor_bien_actual-e.valor_bien)*-1,0)) conc_todos " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tr_valor_ajustes e, rm_rh_concentrado_inst f  " ); 
    SQL.append (" where " ); 
    SQL.append (" e.numero= '" + numero + "'");
    SQL.append (" and e.partida= '" + partida + "'");
    SQL.append (" and e.inciso= '" + inciso + "'");
	  SQL.append (" and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
    SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
    SQL.append (" and b.proveedor_id=c.proveedor_id " );
    SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
    SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
    SQL.append (" and c.control_inventario_id=e.control_inventario_id" );
    SQL.append (" and c.control_inventario_id=f.control_inventario_id" );
    SQL.append (" and trunc(e.fecha_conciliacion)=trunc(f.fecha_conciliacion) " );
	  SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0)," ); 
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0) " );
    SQL.append (" union all ");
    SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(f.partida_presupuestal,1,2),4,0) conc," ); 
    SQL.append (" f.año_afectacion ejer, lpad(substr(f.fecha_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',(e.valor_bien_actual-e.valor_bien)*-1,0)) conc_mob," ); 
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'53',(e.valor_bien_actual-e.valor_bien)*-1,0)) conc_vehi," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'52',(e.valor_bien_actual-e.valor_bien)*-1,'54',(e.valor_bien_actual-e.valor_bien)*-1,'55',(e.valor_bien_actual-e.valor_bien)*-1,'59',(e.valor_bien_actual-e.valor_bien)*-1,0)) conc_maq," );
    SQL.append (" sum(decode(substr(f.partida_presupuestal,1,2),'51',(e.valor_bien_actual-e.valor_bien)*-1,'53',(e.valor_bien_actual-e.valor_bien)*-1,'52',(e.valor_bien_actual-e.valor_bien)*-1,'54',(e.valor_bien_actual-e.valor_bien)*-1,'55',(e.valor_bien_actual-e.valor_bien)*-1,'59',(e.valor_bien_actual-e.valor_bien)*-1,0)) conc_todos " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_rh_control_inventario c, rm_tr_valor_ajustes e, rm_rh_concentrado_inst f " ); 
    SQL.append (" where " ); 
    SQL.append (" e.numero= '" + numero + "'");
    SQL.append (" and e.partida= '" + partida + "'");
    SQL.append (" and e.inciso= '" + inciso + "'");
	  SQL.append (" and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
    SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
    SQL.append (" and b.proveedor_id=c.proveedor_id " );
    SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
    SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
    SQL.append (" and c.control_inventario_id=e.control_inventario_id" );
    SQL.append (" and c.control_inventario_id=f.control_inventario_id" );
    SQL.append (" and trunc(e.fecha_conciliacion)=trunc(f.fecha_conciliacion) " );
	  SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(f.partida_presupuestal,1,2),4,0)," ); 
    SQL.append (" f.año_afectacion, lpad(substr(f.fecha_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0) " );
    System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
        //falta preguntar cuando dispocicion final es 0 que corresponde ahora a una cancelacion y a que num de subcuenta pertenece en contabilidad, y como identificar 
        //a que evento pertenece
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);                  
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }  
        if(partida52202.contains(crs.getString("conc")))
          resultado=registro.AgregaCuentaContable("52202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("tipo"),crs.getString("ejer"),fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO","0005");//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO",crs.getString("conc_mob"));//imprimeHM(hm);
        hm.put ("IMPORTE_VEHICULOS",crs.getString("conc_vehi"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("IMPORTE_PERDIDAS",crs.getString("conc_todos"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
        tipo = crs.getString("tipo");
      }
    }catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Forma26");
      System.out.println ("Forma26:" + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } finally {
      SQL.setLength(0);
      SQL=null;
      crs=null;
	  }
	  return cadenaTem.toString();
	}
    public String Forma27 (Connection con,String fecha, String numero, String unidad, String entidad, String ambito,String idCatalogo, Registro registro, String partida, String inciso) throws SQLException, Exception {
      HashMap hm = new HashMap();
      CachedRowSet crs = null;
      StringBuffer cadenaTem= new StringBuffer("");
      String resultado = "";
      int liMes = 0;
      String partida12203 = "|5200|5400|5500|5900|";
      StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_anterior,1,2),4,0) conc," ); 
      SQL.append (" e.a�o_afecta_anterior ejer, lpad(substr(e.fecha_anterior_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,2),'51',e.valor_bien_actual*-1,0)) conc_mob," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,2),'53',e.valor_bien_actual*-1,0)) conc_vehi," );
          SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,2),'52',e.valor_bien_actual*-1,'54',e.valor_bien_actual*-1,'55',e.valor_bien_actual*-1,'59',e.valor_bien_actual*-1,0)) conc_maq " );
          SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tr_valor_ajustes e " ); 
          SQL.append (" where " ); 
          SQL.append (" e.numero= '" + numero + "'");
	    SQL.append (" and e.partida= '" + partida + "'");
    	    SQL.append (" and e.inciso= '" + inciso + "'");
      SQL.append (" and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
          SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
          SQL.append (" and b.proveedor_id=c.proveedor_id " );
          SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append (" and c.control_inventario_id=e.control_inventario_id" );
          SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_anterior,1,2),4,0)," ); 
          SQL.append (" e.a�o_afecta_anterior , lpad(substr(e.fecha_anterior_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0) " );
          SQL.append (" union all ");
          SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_actual,1,2),4,0) conc," ); 
          SQL.append (" e.a�o_afecta_actual ejer, lpad(substr(e.fecha_actual_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_actual,1,2),'51',e.valor_bien_actual,0)) conc_mob," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_actual,1,2),'53',e.valor_bien_actual,0)) conc_vehi," );
          SQL.append (" sum(decode(substr(e.partida_presup_actual,1,2),'52',e.valor_bien_actual,'54',e.valor_bien_actual,'55',e.valor_bien_actual,'59',e.valor_bien_actual,0)) conc_maq" );
          SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tr_valor_ajustes e " ); 
          SQL.append (" where " ); 
          SQL.append (" e.numero= '" + numero + "'");
          SQL.append (" and e.partida= '" + partida + "'");
    	    SQL.append (" and e.inciso= '" + inciso + "'");
          SQL.append (" and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
          SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
          SQL.append (" and b.proveedor_id=c.proveedor_id " );
          SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append (" and c.control_inventario_id=e.control_inventario_id" );
          SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_actual,1,2),4,0)," ); 
          SQL.append (" e.a�o_afecta_actual , lpad(substr(e.fecha_actual_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0) " );
          SQL.append (" union all ");
          SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_anterior,1,2),4,0) conc," ); 
      SQL.append (" e.a�o_afecta_anterior ejer, lpad(substr(e.fecha_anterior_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,2),'51',e.valor_bien_actual*-1,0)) conc_mob," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,2),'53',e.valor_bien_actual*-1,0)) conc_vehi," );
          SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,2),'52',e.valor_bien_actual*-1,'54',e.valor_bien_actual*-1,'55',e.valor_bien_actual*-1,'59',e.valor_bien_actual*-1,0)) conc_maq " );
          SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_rh_control_inventario c, rm_tr_valor_ajustes e " ); 
          SQL.append (" where " ); 
          SQL.append (" e.numero= '" + numero + "'");
	    SQL.append (" and e.partida= '" + partida + "'");
    	    SQL.append (" and e.inciso= '" + inciso + "'");
      SQL.append (" and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
          SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
          SQL.append (" and b.proveedor_id=c.proveedor_id " );
          SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append (" and c.control_inventario_id=e.control_inventario_id" );
          SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_anterior,1,2),4,0)," ); 
          SQL.append (" e.a�o_afecta_anterior , lpad(substr(e.fecha_anterior_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0) " );
          SQL.append (" union all ");
          SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_actual,1,2),4,0) conc," ); 
          SQL.append (" e.a�o_afecta_actual ejer, lpad(substr(e.fecha_actual_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_actual,1,2),'51',e.valor_bien_actual,0)) conc_mob," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_actual,1,2),'53',e.valor_bien_actual,0)) conc_vehi," );
          SQL.append (" sum(decode(substr(e.partida_presup_actual,1,2),'52',e.valor_bien_actual,'54',e.valor_bien_actual,'55',e.valor_bien_actual,'59',e.valor_bien_actual,0)) conc_maq" );
          SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_rh_control_inventario c, rm_tr_valor_ajustes e " ); 
          SQL.append (" where " ); 
          SQL.append (" e.numero= '" + numero + "'");
          SQL.append (" and e.partida= '" + partida + "'");
    	    SQL.append (" and e.inciso= '" + inciso + "'");
          SQL.append (" and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
          SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
          SQL.append (" and b.proveedor_id=c.proveedor_id " );
          SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append (" and c.control_inventario_id=e.control_inventario_id" );
          SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_actual,1,2),4,0)," ); 
          SQL.append (" e.a�o_afecta_actual , lpad(substr(e.fecha_actual_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0) " );
          System.out.println("SQL Forma27:" +SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst(); 
      while(crs.next()){
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").equals("5100")){
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Mobiliario y Equipo de Admon.","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);          
          resultado=registro.AgregaCuentaContable("12201","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(crs.getString("conc").equals("5300")){
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,"Veh�culos y eq. de Trasporte","5",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("12202","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        if(partida12203.contains(crs.getString("conc"))){
            String descripcion="";           
            switch (Integer.parseInt(crs.getString("conc"))){
               case 5200 :{ descripcion="Equipo agroindustrial";  break;}
               case 5400 :{ descripcion="Equipo instrumental medico y lab."; break;}              
               case 5500 :{ descripcion="Herramientas y Refacciones";  break; }
               case 5900 :{ descripcion="Otros Bienes Muebles"; break;}              
            }
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,descripcion,"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("12203","0001",crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),crs.getString("ejer"),crs.getString("mes"),fecha,meses[liMes],"7",idCatalogo);
        }
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO",crs.getString("tipo"));//imprimeHM(hm);
        if(crs.getDouble("conc_mob") != 0.0 && crs.getString("conc_mob").indexOf("-") == -1)
          hm.put ("IMPORTE_MOBILIARIO_POS",crs.getString("conc_mob"));//imprimeHM(hm);
        if(crs.getDouble("conc_vehi") != 0.0 && crs.getString("conc_vehi").indexOf("-") == -1)
          hm.put ("IMPORTE_VEHICULOS_POS",crs.getString("conc_vehi"));//imprimeHM(hm);
        if(crs.getDouble("conc_maq") != 0.0 && crs.getString("conc_maq").indexOf("-") == -1)
          hm.put ("IMPORTE_MAQUINARIA_POS",crs.getString("conc_maq"));//imprimeHM(hm);
        if(crs.getDouble("conc_mob") != 0.0 && crs.getString("conc_mob").indexOf("-") != -1)
          hm.put ("IMPORTE_MOBILIARIO_NEG",crs.getString("conc_mob"));//imprimeHM(hm);
        if(crs.getDouble("conc_vehi") != 0.0 && crs.getString("conc_vehi").indexOf("-") != -1)
          hm.put ("IMPORTE_VEHICULOS_NEG",crs.getString("conc_vehi"));//imprimeHM(hm);
        if(crs.getDouble("conc_maq") != 0.0 && crs.getString("conc_maq").indexOf("-") != -1)
          hm.put ("IMPORTE_MAQUINARIA_NEG",crs.getString("conc_maq"));//imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
        tipo = crs.getString("tipo");
      }

    }catch (Exception e){
    System.out.println ("Ha ocurrido un error en el metodo Forma27");
    System.out.println ("Forma27:" + SQL.toString());
    crs.close();
    crs=null;
    throw e;
      } finally {
    SQL.setLength(0);
    SQL=null;
    crs=null;
    }
    return cadenaTem.toString();
    }

  public void setEntidad(String entidad) {
    this.entidad = entidad;
  }

  public String getEntidad() {
    return entidad;
  }
}
