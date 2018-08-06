package sia.rf.contabilidad.sistemas.inventarios;

import java.util.HashMap;

import java.sql.Connection;
import java.sql.SQLException;

import sia.rf.contabilidad.registroContableEvento.Cadena;

import sia.rf.contabilidad.registroContableNuevo.bcProveedores;

import sia.ws.publicar.contabilidad.Registro;

import sun.jdbc.rowset.CachedRowSet;

public class SiafmInventariosCOG {

    private String rfc;
    private String nombre_proveedor;
    private String unidad;
    private String ambito;
    private String concepto;
    private String ejercicio;
    private String mes;    
    private String tipo;
    private String entidad;
    private String refGral;
    String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre" };
    
    public SiafmInventariosCOG() {
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
 
//  public String registroActivoContable (Connection conInvenario,String fecha, String numero, String unidad, String entidad, String ambito, Connection conContabilidad, String idCatalogo, Registro registro,String partida, String inciso) throws SQLException, Exception {
//    HashMap hm = new HashMap();
//    CachedRowSet crs = null;
//	  bcProveedores bcProveedor = null;
//    String resultado = null;
//    StringBuffer cadenaTem= new StringBuffer("");
//
//   String partida52101 = "|51|52|53|54|56|58|59|";
//    
//    StringBuffer SQL = new StringBuffer("select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc,c.proveedor_id prov,d.rfc_proveedor rfc, d.razon_social nombre_proveedor,");
//    SQL.append (" sum(b.precio) importe_antes_iva, sum(b.precio*(b.iva/100)) iva, sum(b.precio*(1+b.iva/100)) importe_neto, g.descripcion des_partida ");
//    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d, rm_tr_distribucion e, rh_tc_partidas_presupuestales g "); 
//    SQL.append (" where a.documento_adquisicion_id in (2,3,4,5) ");
//    SQL.append (" and a.numero= '" + numero + "'");
//    SQL.append (" and b.partida= '" + partida + "'");
//    SQL.append (" and b.inciso= '" + inciso + "'");
//    SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
//    SQL.append (" and a.proveedor_id=b.proveedor_id ");
//    SQL.append (" and a.adquisicion_id=b.adquisicion_id "); 
//    SQL.append (" and b.proveedor_id=c.proveedor_id ");
//    SQL.append (" and b.adquisicion_id=c.adquisicion_id ");
//    SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id ");
//    SQL.append (" and c.proveedor_id=d.proveedor_id ");
//    SQL.append (" and c.proveedor_id=e.proveedor_id ");
//    SQL.append (" and c.adquisicion_id=e.adquisicion_id ");
//    SQL.append (" and c.detalle_adquisicion_id=e.detalle_adquisicion_id ");
//    SQL.append (" and c.distribucion_id=e.distribucion_id ");
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
//    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) ");
//    SQL.append (" and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) "); 
//    SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
//    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0),c.proveedor_id,d.rfc_proveedor,d.razon_social,b.iva,g.descripcion ");
//    System.out.println(SQL.toString());
//    try {
//      crs = new CachedRowSet();
//      bcProveedor = new bcProveedores();
//      crs.setCommand(SQL.toString());
//      crs.execute(conInvenario);
//      crs.beforeFirst();
//      while(crs.next()){
//        String lsIdContable=bcProveedor.select_rf_tc_proveedores_rfc(conContabilidad,crs.getString("rfc"));
//        if (lsIdContable.equals("0")){
//          lsIdContable=bcProveedor.select_SEQ_rf_tc_proveedores(conContabilidad);
//          hm.put ("PROVEEDOR",lsIdContable);//imprimeHM(hm);
//          bcProveedor.setId_contable(lsIdContable);
//          bcProveedor.setRfc(crs.getString("rfc"));
//          bcProveedor.setNombre_razon_social(crs.getString("nombre_proveedor"));
//          bcProveedor.insert_rf_tc_proveedores(conContabilidad);
//        }
//        else{
//          hm.put ("PROVEEDOR",lsIdContable);//imprimeHM(hm);
//        }    
//        resultado=registro.AgregaCuentaContable("11106",programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
//        resultado=registro.AgregaCuentaContable("11106",programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),lsIdContable,"0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
        
//        resultado=registro.AgregaCuentaContable("21203",programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000",fecha,crs.getString("nombre_proveedor"),"5",idCatalogo);
        
//        if(partida52101.contains(crs.getString("conc").substring(0,2)))
//          resultado=registro.AgregaCuentaContable("52101",programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("conc"),"0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
        
//        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
//        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
//        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
//        hm.put ("IMPORTE_ADQUISICION",crs.getString("importe_antes_iva"));//1imprimeHM(hm);
//        hm.put ("IMPORTE_COSTO",crs.getString("iva"));//imprimeHM(hm);
//        hm.put ("IMPORTE_PROVEEDORES",crs.getString("importe_neto"));//imprimeHM(hm);
//        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
//        cadenaTem.append(Cadena.construyeCadena(hm));
//        cadenaTem.append("~");
//        hm.clear();
//        rfc = crs.getString("rfc");
//        nombre_proveedor = crs.getString("nombre_proveedor");
//        this.unidad = crs.getString("unid");
//        this.ambito = crs.getString("ambi");
//        concepto = crs.getString("conc");
//      }
//    }catch (Exception e){
//      System.out.println ("Ha ocurrido un error en el metodo RegistroActivoContable");
//      System.out.println ("RegistroActivoContable: " + SQL.toString());
//      crs.close();
//      crs=null;
//      throw e; 
//    } finally {
//      SQL.setLength(0);
//      SQL=null;
//      crs=null;
//	}
//	  return cadenaTem.toString();
//	}
  
 public String Forma7 (Connection conInventarios,String fecha, String numero, String unidad, String entidad, String ambito, Connection conContabilidad, String idCatalogo, Registro registro, String partida, String inciso) throws SQLException, Exception {
   HashMap hm = new HashMap();
   CachedRowSet crs = null;
   bcProveedores bcProveedor = null;
   String resultado = null;
   StringBuffer cadenaTem= new StringBuffer("");
   String partidaGen="";
   String capitulo="";
   String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
   String partidaCom="";
   String programa1="0001";        
           
   int liMes = 0;
   StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc,c.proveedor_id prov,d.rfc_proveedor rfc, d.razon_social nombre_proveedor," ); 
   SQL.append ("\n c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, g.descripcion des_partida, " ); 
   SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
   SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
   SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
   SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
   SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
   SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
   SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
   SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259, sum(c.valor_bien) imp_proveedor" ); 
 // no va-->   SQL.append (" sum(b.precio*(b.iva/100)) imp_iva ");
   SQL.append ("\n from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d, rm_tr_distribucion e, rh_tc_partidas_presupuestales g " ); 
   SQL.append ("\n where a.documento_adquisicion_id in (2,3,4,5) " ); 
   SQL.append ("\n and a.numero= '" + numero + "'");
   SQL.append ("\n and b.partida= '" + partida + "'");
   SQL.append ("\n and b.inciso= '" + inciso + "'");
   SQL.append ("\n and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
   SQL.append ("\n and a.proveedor_id=b.proveedor_id " ); 
   SQL.append ("\n and a.adquisicion_id=b.adquisicion_id " );
   SQL.append ("\n and b.proveedor_id=c.proveedor_id " );
   SQL.append ("\n and b.adquisicion_id=c.adquisicion_id " ); 
   SQL.append ("\n and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
   SQL.append ("\n and c.proveedor_id=d.proveedor_id" );
   SQL.append ("\n and c.proveedor_id=e.proveedor_id ");
   SQL.append ("\n and c.adquisicion_id=e.adquisicion_id ");
   SQL.append ("\n and c.detalle_adquisicion_id=e.detalle_adquisicion_id ");
   SQL.append ("\n and c.distribucion_id=e.distribucion_id "); 
   SQL.append ("\n and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
 //    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
 //    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) ");
   SQL.append ("\n and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
   SQL.append ("\n group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0)," ); 
   SQL.append ("\n c.proveedor_id,d.rfc_proveedor,d.razon_social," );
   SQL.append ("\n c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),g.descripcion " );
       //  System.out.println(SQL.toString());
   try {
     crs = new CachedRowSet();
     bcProveedor = new bcProveedores();
     crs.setCommand(SQL.toString());
     crs.execute(conInventarios);
     crs.beforeFirst();
     while(crs.next()){
       partidaGen="";
       capitulo="";
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
       liMes=Integer.parseInt(crs.getString("mes"))-1;
       
       if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
         if (crs.getString("conc").substring(0,3).equals("511"))
           partidaGen="1";
         else if (crs.getString("conc").substring(0,3).equals("512"))
           partidaGen="2";
         else if (crs.getString("conc").substring(0,3).equals("515"))
           partidaGen="3";
         else    
           partidaGen="9";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }  
       
       if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
         if (crs.getString("conc").substring(0,3).equals("521"))
           partidaGen="1";
         else if (crs.getString("conc").substring(0,3).equals("522"))
           partidaGen="2";              
         else if (crs.getString("conc").substring(0,3).equals("523"))
           partidaGen="3";
         else    
           partidaGen="9";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }  
         
       if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
         if (crs.getString("conc").substring(0,3).equals("531"))
          partidaGen="1";
         else    
           partidaGen="2";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }          

       if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
         if (crs.getString("conc").substring(0,3).equals("541"))
           partidaGen="1";
         else if (crs.getString("conc").substring(0,3).equals("542"))
           partidaGen="2";
         else if (crs.getString("conc").substring(0,3).equals("543"))
           partidaGen="3";
         else if (crs.getString("conc").substring(0,3).equals("545"))
           partidaGen="5";
         else    
           partidaGen="9";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }  

       if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
         if (crs.getString("conc").substring(0,3).equals("561"))
           partidaGen="1";
         else if (crs.getString("conc").substring(0,3).equals("562"))
           partidaGen="2";
         else if (crs.getString("conc").substring(0,3).equals("563"))
           partidaGen="3";
         else if (crs.getString("conc").substring(0,3).equals("564"))
           partidaGen="4";
         else if (crs.getString("conc").substring(0,3).equals("565"))
           partidaGen="5"; 
         else if (crs.getString("conc").substring(0,3).equals("566"))
           partidaGen="6";
         else if (crs.getString("conc").substring(0,3).equals("567"))
           partidaGen="7";
         else    
           partidaGen="9";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }  
       
       if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
         if (crs.getString("conc").substring(0,3).equals("513"))
          partidaGen="1";
         else    
           partidaGen="2";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }           
       
       if (crs.getString("conc").substring(0,3).equals("591")){
          partidaGen="1";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }             
       
       if (crs.getString("conc").substring(0,3).equals("599")){
          partidaGen="9";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }         

       if (crs.getString("conc").substring(0,1).equals("5")){
          capitulo="5";
          resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);           
       }         
       
       
         if (partidasGenIva.contains(crs.getString("conc"))){
           partidaCom=crs.getString("conc");
         }
       
       hm.put ("PROGRAMA",programa1);//imprimeHM(hm);         
       hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
       hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
       hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
       hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
       hm.put ("CAPITULO",capitulo);//imprimeHM(hm);
       hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
       hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);
       hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
       hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
       hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
       hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
       hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
       hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
       hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
       hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);
       hm.put ("IMPORTE_PROVEEDOR",crs.getString("imp_proveedor"));//imprimeHM(hm);
 // no va-->        hm.put ("IMPORTE_IVA",crs.getString("imp_iva"));//imprimeHM(hm);
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
        
    public String Forma8 (Connection con,String fecha, String orden, String tsTraspaso, String ejercicio, String unidad, String entidad, String ambito, String idCatalogo, Registro registro) throws SQLException, Exception {
        HashMap hm = new HashMap();
        CachedRowSet crs = null;
        StringBuffer cadenaTem= new StringBuffer("");
        String resultado = "";
        int liMes = 0;
        String partidaGen="";
        String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
        String partidaCom="";
        String programa1="0001";    
        // String partida12203 = "|53|56|59|";    
        // String partida12206 = "|51|52|53|54|56|58|59|";
      
        StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora_emisora,4,0) unid,lpad(a.coord_estatal_origen||a.ambito_origen,4,0) ambi,a.unidad_ejecutora_emisora unidad,a.coord_estatal_origen entidad, a.ambito_origen ambito, rpad(substr(b.partida_presupuestal,1,3),4,0) conc, ");
        SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, g.descripcion des_partida," ); 
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259," );
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
        SQL.append (" '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
        SQL.append (" '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
        SQL.append (" '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
        SQL.append (" '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_remesas_1145 " );
            SQL.append (" from rm_tr_orden_traspaso a, rm_tr_detalle_traspaso b,rm_tr_control_inventario c, rh_tc_partidas_presupuestales g ");
        SQL.append (" where a.orden_traspaso_id= '" + orden + "' and a.tipo_solic_traspaso_id =" + tsTraspaso + " and a.ejercicio=" + ejercicio );  
        SQL.append (" and to_date(to_char(a.fecha_atencion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
        SQL.append (" and a.orden_traspaso_id=b.orden_traspaso_id "); 
        SQL.append (" and a.tipo_solic_traspaso_id=b.tipo_solic_traspaso_id ");
        SQL.append (" and a.ejercicio=b.ejercicio ");  
        SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
        SQL.append (" and substr(trim(to_char(b.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
            //SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
        SQL.append (" group by lpad(a.unidad_ejecutora_emisora,4,0),lpad(a.coord_estatal_origen||a.ambito_origen,4,0),a.unidad_ejecutora_emisora, a.coord_estatal_origen, a.ambito_origen, rpad(substr(b.partida_presupuestal,1,3),4,0), ");
        SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),g.descripcion ");
    /*    SQL.append (" union all ");
        SQL.append (" select lpad(a.unidad_ejecutora_emisora,4,0) unid,lpad(a.coord_estatal_origen||a.ambito_origen,4,0) ambi,a.unidad_ejecutora_emisora unidad,a.coord_estatal_origen entidad, a.ambito_origen ambito, rpad(substr(b.partida_presupuestal,1,3),4,0) conc, ");
        SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, g.descripcion des_partida," ); 
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259," );
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
        SQL.append (" '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
        SQL.append (" '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
        SQL.append (" '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
        SQL.append (" '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_deudores_1123 " );
        SQL.append (" from rm_tr_orden_traspaso a, rm_tr_detalle_traspaso b,rm_rh_control_inventario c, rh_tc_partidas_presupuestales g ");
        SQL.append (" where a.orden_traspaso_id= '" + orden + "' and a.tipo_solic_traspaso_id =" + tsTraspaso + " and a.ejercicio=" + ejercicio );  
        SQL.append (" and to_date(to_char(a.fecha_atencion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
        SQL.append (" and a.orden_traspaso_id=b.orden_traspaso_id "); 
        SQL.append (" and a.tipo_solic_traspaso_id=b.tipo_solic_traspaso_id ");
        SQL.append (" and a.ejercicio=b.ejercicio ");  
        SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
        SQL.append (" and substr(trim(to_char(b.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
             //SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
        SQL.append (" group by lpad(a.unidad_ejecutora_emisora,4,0),lpad(a.coord_estatal_origen||a.ambito_origen,4,0),a.unidad_ejecutora_emisora, a.coord_estatal_origen, a.ambito_origen, rpad(substr(b.partida_presupuestal,1,3),4,0), ");
        SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),g.descripcion ");
    */
            //  System.out.println(SQL.toString());
        try {
          crs = new CachedRowSet();
          crs.setCommand(SQL.toString());
          crs.execute(con);
          crs.beforeFirst();
          while(crs.next()){
            partidaGen="";
            liMes=Integer.parseInt(crs.getString("mes"))-1;
              if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
                if (crs.getString("conc").substring(0,3).equals("511"))
                  partidaGen="1";
                else if (crs.getString("conc").substring(0,3).equals("512"))
                  partidaGen="2";
                else if (crs.getString("conc").substring(0,3).equals("515"))
                  partidaGen="3";
                else    
                  partidaGen="9";
                //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
                resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
                resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
                }
                 
                if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
                if (crs.getString("conc").substring(0,3).equals("521"))
                  partidaGen="1";
                else if (crs.getString("conc").substring(0,3).equals("522"))
                  partidaGen="2";              
                else if (crs.getString("conc").substring(0,3).equals("523"))
                  partidaGen="3";
                else    
                  partidaGen="9";
                //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
                resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
                resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
                }
                
                if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
                if (crs.getString("conc").substring(0,3).equals("531"))
                partidaGen="1";
                else    
                 partidaGen="2";
                //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
                resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
                resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
                }

                if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
                if (crs.getString("conc").substring(0,3).equals("541"))
                  partidaGen="1";
                else if (crs.getString("conc").substring(0,3).equals("542"))
                  partidaGen="2";
                else if (crs.getString("conc").substring(0,3).equals("543"))
                  partidaGen="3";
                else if (crs.getString("conc").substring(0,3).equals("545"))
                  partidaGen="5";
                else    
                  partidaGen="9";
                //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
                resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
                resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
                }

                if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
                if (crs.getString("conc").substring(0,3).equals("561"))
                  partidaGen="1";
                else if (crs.getString("conc").substring(0,3).equals("562"))
                  partidaGen="2";
                else if (crs.getString("conc").substring(0,3).equals("563"))
                  partidaGen="3";
                else if (crs.getString("conc").substring(0,3).equals("564"))
                  partidaGen="4";
                else if (crs.getString("conc").substring(0,3).equals("565"))
                  partidaGen="5"; 
                else if (crs.getString("conc").substring(0,3).equals("566"))
                  partidaGen="6";
                else if (crs.getString("conc").substring(0,3).equals("567"))
                  partidaGen="7";
                else    
                  partidaGen="9";
                //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
                resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
                resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
                }
                 
                if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
                if (crs.getString("conc").substring(0,3).equals("513"))
                  partidaGen="1";
                else    
                  partidaGen="2";
                //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
                resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
                resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
                }
                 
                if (crs.getString("conc").substring(0,3).equals("591")){
                partidaGen="1";
                //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
                resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
                resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
                }
                 
                if (crs.getString("conc").substring(0,3).equals("599")){
                partidaGen="9";
                //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
                resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
                resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
                }

                //    if (crs.getString("conc").substring(0,1).equals("5")){
                //       capitulo="5";
                //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
                //    }
                 
                /*if (crs.getString("tipo").equals("0008"))
                tipoBaja="0006";
                else if (crs.getString("tipo").equals("0013"))
                tipoBaja="0003";
                else
                tipoBaja="";
                */

                if (partidasGenIva.contains(crs.getString("conc"))){
                  partidaCom=crs.getString("conc");
                  resultado=registro.AgregaCuentaContable("1145","6",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,crs.getString("ejer"),"0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);
                }
              

            hm.put ("PROGRAMA",programa1);//imprimeHM(hm);
            hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
            hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
            hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
            hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
            hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
            hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);        
            hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
            hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
            hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
            hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
            hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
            hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
            hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
            hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
            hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);
            hm.put ("IMPORTE_REMESAS_1145",crs.getString("imp_remesas_1145"));//imprimeHM(hm);
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

      public String Forma9 (Connection con,String fecha, String orden, String tsTraspaso, String ejercicio, String unidad, String entidad, String ambito, String idCatalogo, Registro registro) throws SQLException, Exception {
        HashMap hm = new HashMap();
        CachedRowSet crs = null;
        StringBuffer cadenaTem= new StringBuffer("");
        String resultado = "";
        //String partida41201 = "|51|52|53|54|56|58|59|";
        //String partida12206 = "|51|52|53|54|56|58|59|";
        String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
        String partidaCom="";
        String programa1="0001";      
        
        StringBuffer SQL = new StringBuffer("select lpad(a.unidad_ejecutora_emisora,4,0) unid,lpad(a.coord_estatal_origen||a.ambito_origen,4,0) ambi,a.unidad_ejecutora_emisora unidad, a.coord_estatal_origen entidad, a.ambito_origen ambito, rpad(substr(b.partida_presupuestal,1,3),4,0) conc, ");
        SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, g.descripcion des_partida, ");
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
        SQL.append (" '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
        SQL.append (" '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
        SQL.append (" '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
        SQL.append (" '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_otros_5599 " );
        //SQL.append (" sum(decode(substr(b.partida_presupuestal,1,2),'51',c.valor_bien,'52',c.valor_bien,'54',c.valor_bien,'53',c.valor_bien,'56',c.valor_bien,'59',c.valor_bien,0)) conc_todos" );
        SQL.append (" from rm_tr_orden_traspaso a, rm_tr_detalle_traspaso b,rm_tr_control_inventario c, rh_tc_partidas_presupuestales g ");
        SQL.append (" where a.orden_traspaso_id= '" + orden + "' and a.tipo_solic_traspaso_id =" + tsTraspaso + " and a.ejercicio=" + ejercicio );  
              SQL.append (" and to_date(to_char(a.fecha_atencion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
        SQL.append (" and a.orden_traspaso_id=b.orden_traspaso_id ");
        SQL.append (" and a.tipo_solic_traspaso_id=b.tipo_solic_traspaso_id ");
        SQL.append (" and a.ejercicio=b.ejercicio ");
        SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
        SQL.append (" and substr(trim(to_char(b.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");    
             //SQL.append (" and a.unidad_ejecutora_emisora='"+unidad+"' and a.coord_estatal_origen="+entidad);
        SQL.append (" group by lpad(a.unidad_ejecutora_emisora,4,0),lpad(a.coord_estatal_origen||a.ambito_origen,4,0),a.unidad_ejecutora_emisora,a.coord_estatal_origen, a.ambito_origen,rpad(substr(b.partida_presupuestal,1,3),4,0), ");
        SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),g.descripcion ");
    /*  SQL.append (" union all ");
        SQL.append (" select lpad(a.unidad_ejecutora_emisora,4,0) unid,lpad(a.coord_estatal_origen||a.ambito_origen,4,0) ambi,a.unidad_ejecutora_emisora unidad, a.coord_estatal_origen entidad, a.ambito_origen ambito, rpad(substr(b.partida_presupuestal,1,3),4,0) conc, ");
        SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, g.descripcion des_partida, ");
        SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
        SQL.append (" '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
        SQL.append (" '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
        SQL.append (" '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
        SQL.append (" '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_otros_5599 " );    
        //SQL.append (" sum(decode(substr(b.partida_presupuestal,1,2),'51',c.valor_bien,'52',c.valor_bien,'54',c.valor_bien,'53',c.valor_bien,'56',c.valor_bien,'59',c.valor_bien,0)) conc_todos" );
        SQL.append (" from rm_tr_orden_traspaso a, rm_tr_detalle_traspaso b,rm_rh_control_inventario c, rh_tc_partidas_presupuestales g ");
        SQL.append (" where a.orden_traspaso_id= '" + orden + "' and a.tipo_solic_traspaso_id =" + tsTraspaso + " and a.ejercicio=" + ejercicio );  
              SQL.append (" and to_date(to_char(a.fecha_atencion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
        SQL.append (" and a.orden_traspaso_id=b.orden_traspaso_id ");
        SQL.append (" and a.tipo_solic_traspaso_id=b.tipo_solic_traspaso_id ");
        SQL.append (" and a.ejercicio=b.ejercicio ");
        SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
        SQL.append (" and substr(trim(to_char(b.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
              //SQL.append (" and a.unidad_ejecutora_emisora='"+unidad+"' and a.coord_estatal_origen="+entidad);
        SQL.append (" group by lpad(a.unidad_ejecutora_emisora,4,0),lpad(a.coord_estatal_origen||a.ambito_origen,4,0),a.unidad_ejecutora_emisora,a.coord_estatal_origen, a.ambito_origen,rpad(substr(b.partida_presupuestal,1,3),4,0), ");
        SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),g.descripcion ");
    */
            //  System.out.println(SQL.toString());
        try {
         
          crs = new CachedRowSet();
          crs.setCommand(SQL.toString());
          crs.execute(con);
          crs.beforeFirst();
          while(crs.next()){

            if (partidasGenIva.contains(crs.getString("conc"))){
              partidaCom=crs.getString("conc");
              resultado=registro.AgregaCuentaContable("5599","3",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,crs.getString("ejer"),"0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);
              resultado=registro.AgregaCuentaContable("1145","6",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,crs.getString("ejer"),"0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);
            }
            
            // String cantidad=(crs.getString("conc_todos"));
            // if (!cantidad.equals("0")) {
              hm.put ("PROGRAMA",programa1);//imprimeHM(hm);
              hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
              hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
              hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
              hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
              hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);   
              hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
              hm.put ("IMPORTE_OTROS_5599",crs.getString("imp_otros_5599"));//1imprimeHM(hm);
              hm.put ("IMPORTE_REMESAS_1145",crs.getString("imp_otros_5599"));//1imprimeHM(hm);
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
            //}
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
    String partidaGen="";
    String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
    String partidaCom="";
    String programa1="0001";    
    // String partida12203 = "|53|56|59|";
    //System.out.println("Antes de SQL ");
    
    StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(b.partida_presupuestal,1,3),4,0) conc, ");
    SQL.append (" c.año_afectacion ejer,lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, g.descripcion des_partida, " );
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259," );
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
    SQL.append (" '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
    SQL.append (" '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
    SQL.append (" '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
    SQL.append (" '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_otros_4399 " );
    SQL.append (" from rm_tr_orden_traspaso a, rm_tr_detalle_traspaso b,rm_tr_control_inventario c, rh_tc_partidas_presupuestales g ");
    SQL.append (" where a.orden_traspaso_id= '" + orden + "' and a.tipo_solic_traspaso_id =" + tsTraspaso + " and a.ejercicio=" + ejercicio );  
    SQL.append (" and to_date(to_char(a.fecha_atencion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.orden_traspaso_id=b.orden_traspaso_id ");
    SQL.append (" and a.tipo_solic_traspaso_id=b.tipo_solic_traspaso_id ");
    SQL.append (" and a.ejercicio=b.ejercicio ");
    SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
    SQL.append (" and substr(trim(to_char(b.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(b.partida_presupuestal,1,3),4,0), ");
    SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),g.descripcion ");
/*    SQL.append (" union all ");
    SQL.append (" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(b.partida_presupuestal,1,3),4,0) conc, ");
    SQL.append (" c.año_afectacion ejer,lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, g.descripcion des_partida," ); 
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259," );
    SQL.append (" sum(decode(substr(b.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
    SQL.append (" '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
    SQL.append (" '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
    SQL.append (" '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
    SQL.append (" '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_otros_4399 " );
    SQL.append (" from rm_tr_orden_traspaso a, rm_tr_detalle_traspaso b,rm_rh_control_inventario c, rh_tc_partidas_presupuestales g ");
    SQL.append (" where a.orden_traspaso_id= '" + orden + "' and a.tipo_solic_traspaso_id =" + tsTraspaso + " and a.ejercicio=" + ejercicio );  
    SQL.append (" and to_date(to_char(a.fecha_atencion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.orden_traspaso_id=b.orden_traspaso_id ");
    SQL.append (" and a.tipo_solic_traspaso_id=b.tipo_solic_traspaso_id ");
    SQL.append (" and a.ejercicio=b.ejercicio ");
    SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
    SQL.append (" and substr(trim(to_char(b.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(b.partida_presupuestal,1,3),4,0), ");
    SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),g.descripcion ");
*/
	//  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
        partidaGen=""; 
        // System.out.println("entro a ciclo con conc"+crs.getString("conc"));
        // System.out.println("entro a ciclo*"+crs.getString("mes")+"*");
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        // System.out.println("aaa*"+liMes+"*");
          if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
            if (crs.getString("conc").substring(0,3).equals("511"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("512"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("515"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            // System.out.println("aaaAgregaCuentaCon1"+partidaGen+"*"+programa1+"*"+crs.getString("unid")+"*"+crs.getString("ambi")+"*"+crs.getString("ejer")+"*"+"0000"+"*"+"0000"+"*"+"0000"+"*"+fecha+"*"+"Ejercicio "+crs.getString("ejer")+"*"+"6"+"*"+idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            // System.out.println("dddAgregaCuentaCon1");
             // System.out.println("aaaAgregaCuentaCon2"+partidaGen+"*"+programa1+"*"+crs.getString("unid")+"*"+crs.getString("ambi")+"*"+crs.getString("ejer")+"*"+crs.getString("mes")+"*"+"0000"+"*"+"0000"+"*"+fecha+"*"+meses[liMes]+"*"+"7"+"*"+idCatalogo);             
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
              // System.out.println("dddAgregaCuentaCon2");
          }  
          
          if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
            if (crs.getString("conc").substring(0,3).equals("521"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("522"))
              partidaGen="2";              
            else if (crs.getString("conc").substring(0,3).equals("523"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
          
          if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
            if (crs.getString("conc").substring(0,3).equals("531"))
             partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }          

          if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
            if (crs.getString("conc").substring(0,3).equals("541"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("542"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("543"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("545"))
              partidaGen="5";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  

          // System.out.println("aaa562"+crs.getString("conc"));
          if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
            if (crs.getString("conc").substring(0,3).equals("561"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("562"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("563"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("564"))
              partidaGen="4";
            else if (crs.getString("conc").substring(0,3).equals("565"))
              partidaGen="5"; 
            else if (crs.getString("conc").substring(0,3).equals("566"))
              partidaGen="6";
            else if (crs.getString("conc").substring(0,3).equals("567"))
              partidaGen="7";
            else    
              partidaGen="9";
            // System.out.println("ddd562 "+partidaGen);  
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            // System.out.println("dddAgregaCuenta "+resultado);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            // System.out.println("dddAgregaCuenta "+resultado);
          }  
          
          if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
            if (crs.getString("conc").substring(0,3).equals("513"))
             partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }           
          
          if (crs.getString("conc").substring(0,3).equals("591")){
             partidaGen="1";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }             
          
          if (crs.getString("conc").substring(0,3).equals("599")){
             partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }         

          //    if (crs.getString("conc").substring(0,1).equals("5")){
          //       capitulo="5";
          //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
          //    }
          
          
            if (partidasGenIva.contains(crs.getString("conc"))){
              partidaCom=crs.getString("conc");
              // System.out.println("aaaAgregaCuenta4399"+partidaCom);
              resultado=registro.AgregaCuentaContable("4399","4",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,crs.getString("ejer"),"0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"7",idCatalogo);
              // System.out.println("dddAgregaCuenta4399");
            }

        // System.out.println("aaaVariables");
        hm.put ("PROGRAMA",programa1);//imprimeHM(hm);        
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
        hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);        
        hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
        hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
        hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
        hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);
        hm.put ("IMPORTE_OTROS_REM_4399",crs.getString("imp_otros_4399"));//imprimeHM(hm);   
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        // System.out.println("dddVariables");
        cadenaTem.append(Cadena.construyeCadena(hm));
        // System.out.println("dddConstruyeCadena");
        cadenaTem.append("~");
        hm.clear();
        // System.out.println("aaaBase");
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
        concepto = crs.getString("conc");
        this.ejercicio = crs.getString("ejer");
        mes = crs.getString("mes");
        // System.out.println("dddBase");
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

public String Forma12 (Connection con,String fecha, String numero,  String unidad, String entidad, String ambito,String idCatalogo, Registro registro, String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
    StringBuffer cadenaTem= new StringBuffer("");
    String resultado = "";
    int liMes = 0;
    // String partida12203 = "|53|56|59|";
    String partidaGen="";
    String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
    String partidaCom="";
    String programa1="0001";    
    
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc,c.proveedor_id prov,d.rfc_proveedor rfc, d.razon_social nombre_proveedor," ); 
    SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, g.descripcion des_partida," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259, " );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
    SQL.append (" '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
    SQL.append (" '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
    SQL.append (" '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
    SQL.append (" '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_otros_4399 " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d, rm_tr_distribucion e, rh_tc_partidas_presupuestales g " ); 
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
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
//    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) "); 
    SQL.append (" and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0)," ); 
    SQL.append (" c.proveedor_id,d.rfc_proveedor,d.razon_social," );
    SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),g.descripcion " );  
	//  System.out.println(SQL.toString());
    try {
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con);
    crs.beforeFirst();
    while(crs.next()){
      partidaGen="";
      liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
          if (crs.getString("conc").substring(0,3).equals("511"))
            partidaGen="1";
          else if (crs.getString("conc").substring(0,3).equals("512"))
            partidaGen="2";
          else if (crs.getString("conc").substring(0,3).equals("515"))
            partidaGen="3";
          else    
            partidaGen="9";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
        }  
        
        if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
          if (crs.getString("conc").substring(0,3).equals("521"))
            partidaGen="1";
          else if (crs.getString("conc").substring(0,3).equals("522"))
            partidaGen="2";              
          else if (crs.getString("conc").substring(0,3).equals("523"))
            partidaGen="3";
          else    
            partidaGen="9";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
        }  
        
        if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
          if (crs.getString("conc").substring(0,3).equals("531"))
           partidaGen="1";
          else    
            partidaGen="2";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
        }          

        if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
          if (crs.getString("conc").substring(0,3).equals("541"))
            partidaGen="1";
          else if (crs.getString("conc").substring(0,3).equals("542"))
            partidaGen="2";
          else if (crs.getString("conc").substring(0,3).equals("543"))
            partidaGen="3";
          else if (crs.getString("conc").substring(0,3).equals("545"))
            partidaGen="5";
          else    
            partidaGen="9";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
        }  

        if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
          if (crs.getString("conc").substring(0,3).equals("561"))
            partidaGen="1";
          else if (crs.getString("conc").substring(0,3).equals("562"))
            partidaGen="2";
          else if (crs.getString("conc").substring(0,3).equals("563"))
            partidaGen="3";
          else if (crs.getString("conc").substring(0,3).equals("564"))
            partidaGen="4";
          else if (crs.getString("conc").substring(0,3).equals("565"))
            partidaGen="5"; 
          else if (crs.getString("conc").substring(0,3).equals("566"))
            partidaGen="6";
          else if (crs.getString("conc").substring(0,3).equals("567"))
            partidaGen="7";
          else    
            partidaGen="9";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
        }  
        
        if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
          if (crs.getString("conc").substring(0,3).equals("513"))
           partidaGen="1";
          else    
            partidaGen="2";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
        }           
        
        if (crs.getString("conc").substring(0,3).equals("591")){
           partidaGen="1";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
        }             
        
        if (crs.getString("conc").substring(0,3).equals("599")){
           partidaGen="9";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
        }         

        //    if (crs.getString("conc").substring(0,1).equals("5")){
        //       capitulo="5";
        //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
        //    }
        
        
          if (partidasGenIva.contains(crs.getString("conc"))){
            partidaCom=crs.getString("conc");
            resultado=registro.AgregaCuentaContable("4399","2",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,"0008",crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"8",idCatalogo);
          }
          
      hm.put ("PROGRAMA",programa1);//imprimeHM(hm);
      hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
      hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
      hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
      hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);      
      hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
      hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
      hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
      hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
      hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
      hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
      hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
      hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
      hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
      hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
      hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);
      hm.put ("IMPORTE_OTROS_4399",crs.getString("imp_otros_4399"));//imprimeHM(hm);
      hm.put ("REFERENCIA"," ");//imprimeHM(hm);
      hm.put ("TIPO_ALTA","0008");//imprimeHM(hm);
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
        
/*     Se elimino la llamaba el evento 7
  public String Forma13 (Connection con,String fecha, String numero, String unidad, String entidad, String ambito, String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
    StringBuffer SQL = new StringBuffer("select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc,c.proveedor_id prov,d.rfc_proveedor rfc, d.razon_social nombre_proveedor, ");
    SQL.append (" c.año_afectacion ejer, lpad(substr(c.fecha_factura,4,2),4,0) mes, g.descripcion des_partida, ");
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
    SQL.append (" '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
    SQL.append (" '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
    SQL.append (" '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
    SQL.append (" '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_otros_4399 " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d,rm_tr_distribucion e, rh_tc_partidas_presupuestales g ");
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
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
//    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) ");  
    SQL.append (" and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0),c.proveedor_id,d.rfc_proveedor,d.razon_social, ");
    SQL.append (" c.año_afectacion, lpad(substr(c.fecha_factura,4,2),4,0),g.descripcion ");
//  System.out.println(SQL.toString());
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
        hm.put ("IMPORTE_BIENES",crs.getString("imp_otros_4399"));//1imprimeHM(hm);
        hm.put ("IMPORTE_PATRIMONIALES",crs.getString("imp_otros_4399"));//1imprimeHM(hm);
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
*/
  public String Forma16 (Connection con,String fecha, String numero, String unidad, String entidad, String ambito, String idCatalogo, Registro registro, String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
    StringBuffer cadenaTem= new StringBuffer("");
    int liMes = 0;
    String partidaGen="";
    String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
    String partidaCom="";
    String programa1="0001";
    String resultado = "";
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc,d.rfc_proveedor rfc, d.razon_social nombre_proveedor," ); 
    SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, g.descripcion des_partida," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
    SQL.append (" '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
    SQL.append (" '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
    SQL.append (" '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
    SQL.append (" '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_otros_4399 " ); 
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d, rm_tr_distribucion e, rh_tc_partidas_presupuestales g " ); 
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
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
//    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) ");  
    SQL.append (" and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0)," ); 
    SQL.append (" d.rfc_proveedor,d.razon_social," );
    SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),g.descripcion " );
	//  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
        partidaGen="";
      
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        
          if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
            if (crs.getString("conc").substring(0,3).equals("511"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("512"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("515"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
          
          if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
            if (crs.getString("conc").substring(0,3).equals("521"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("522"))
              partidaGen="2";              
            else if (crs.getString("conc").substring(0,3).equals("523"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
          
          if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
            if (crs.getString("conc").substring(0,3).equals("531"))
             partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }          

          if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
            if (crs.getString("conc").substring(0,3).equals("541"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("542"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("543"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("545"))
              partidaGen="5";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  

          if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
            if (crs.getString("conc").substring(0,3).equals("561"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("562"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("563"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("564"))
              partidaGen="4";
            else if (crs.getString("conc").substring(0,3).equals("565"))
              partidaGen="5"; 
            else if (crs.getString("conc").substring(0,3).equals("566"))
              partidaGen="6";
            else if (crs.getString("conc").substring(0,3).equals("567"))
              partidaGen="7";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
          
          if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
            if (crs.getString("conc").substring(0,3).equals("513"))
             partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }           
          
          if (crs.getString("conc").substring(0,3).equals("591")){
             partidaGen="1";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }             
          
          if (crs.getString("conc").substring(0,3).equals("599")){
             partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }         

      //    if (crs.getString("conc").substring(0,1).equals("5")){
      //       capitulo="5";
      //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);           
      //    }         
          
          
            if (partidasGenIva.contains(crs.getString("conc"))){
              partidaCom=crs.getString("conc");
              resultado=registro.AgregaCuentaContable("4399","2",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,"0007",crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"8",idCatalogo);
            }

        hm.put ("PROGRAMA",programa1);//imprimeHM(hm);           
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
        hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
        hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
        hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
        hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);        
        hm.put ("IMPORTE_OTROS_4399",crs.getString("imp_otros_4399"));//1imprimeHM(hm);
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        hm.put ("TIPO_ALTA","0007");//imprimeHM(hm); Donaciones
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

    String partidaGen="";
    String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
    String partidaCom="";
    String programa1="0001";
    
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc,d.rfc_proveedor rfc, d.razon_social nombre_proveedor," ); 
    SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259, " );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
    SQL.append (" '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
    SQL.append (" '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
    SQL.append (" '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
    SQL.append (" '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_otros_4399 " ); 
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d,rm_tr_distribucion e, rh_tc_partidas_presupuestales g " ); 
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
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
//    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) "); 
    SQL.append (" and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
	  SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0)," ); 
    SQL.append (" d.rfc_proveedor,d.razon_social," );
    SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
//	  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
       partidaGen="";
       liMes=Integer.parseInt(crs.getString("mes"))-1;
       if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
         if (crs.getString("conc").substring(0,3).equals("511"))
           partidaGen="1";
         else if (crs.getString("conc").substring(0,3).equals("512"))
           partidaGen="2";
         else if (crs.getString("conc").substring(0,3).equals("515"))
           partidaGen="3";
         else    
           partidaGen="9";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }  
          
       if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
         if (crs.getString("conc").substring(0,3).equals("521"))
           partidaGen="1";
         else if (crs.getString("conc").substring(0,3).equals("522"))
           partidaGen="2";              
         else if (crs.getString("conc").substring(0,3).equals("523"))
           partidaGen="3";
         else    
           partidaGen="9";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }  
       
       if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
         if (crs.getString("conc").substring(0,3).equals("531"))
         partidaGen="1";
         else    
          partidaGen="2";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }          

       if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
         if (crs.getString("conc").substring(0,3).equals("541"))
           partidaGen="1";
         else if (crs.getString("conc").substring(0,3).equals("542"))
           partidaGen="2";
         else if (crs.getString("conc").substring(0,3).equals("543"))
           partidaGen="3";
         else if (crs.getString("conc").substring(0,3).equals("545"))
           partidaGen="5";
         else    
           partidaGen="9";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }  

       if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
         if (crs.getString("conc").substring(0,3).equals("561"))
           partidaGen="1";
         else if (crs.getString("conc").substring(0,3).equals("562"))
           partidaGen="2";
         else if (crs.getString("conc").substring(0,3).equals("563"))
           partidaGen="3";
         else if (crs.getString("conc").substring(0,3).equals("564"))
           partidaGen="4";
         else if (crs.getString("conc").substring(0,3).equals("565"))
           partidaGen="5"; 
         else if (crs.getString("conc").substring(0,3).equals("566"))
           partidaGen="6";
         else if (crs.getString("conc").substring(0,3).equals("567"))
           partidaGen="7";
         else    
           partidaGen="9";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }  
          
       if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
         if (crs.getString("conc").substring(0,3).equals("513"))
           partidaGen="1";
         else    
           partidaGen="2";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
        }           
          
       if (crs.getString("conc").substring(0,3).equals("591")){
         partidaGen="1";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }             
          
       if (crs.getString("conc").substring(0,3).equals("599")){
         partidaGen="9";
         //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
         resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
         resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
       }         

       //    if (crs.getString("conc").substring(0,1).equals("5")){
       //       capitulo="5";
       //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
       //    }
          
       if (partidasGenIva.contains(crs.getString("conc"))){
          partidaCom=crs.getString("conc");
          resultado=registro.AgregaCuentaContable("4399","2",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,"0001",crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"8",idCatalogo);
       }

       hm.put ("PROGRAMA",programa1);//imprimeHM(hm);
       hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
       hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
       hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
       hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
       hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
       hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);
       hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
       hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
       hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
       hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
       hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm);
       hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
       hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
       hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);
       hm.put ("IMPORTE_OTROS_4399",crs.getString("imp_otros_4399"));//1imprimeHM(hm);
       hm.put ("REFERENCIA"," ");//imprimeHM(hm);
       hm.put ("TIPO_ALTA","0001");//imprimeHM(hm); orden de trabajo (Elaboraciones)

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
  
  public String Forma19 (Connection con,String fecha, String numero, String unidad, String entidad, String ambito,String idCatalogo, Registro registro, String partida, String inciso) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
    String tipo = null;
    int liMes = 0;
    String resultado = "";    
    String partidaGen="";
    String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
    String partidaCom="";
    String programa1="0001";
    String tipoAlta="";
    
    StringBuffer cadenaTem= new StringBuffer("");
   
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc,d.rfc_proveedor rfc, d.razon_social nombre_proveedor," ); 
    SQL.append ("\n c.año_afectacion ejer,lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259, " ); 
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
    SQL.append ("\n '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
    SQL.append ("\n '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
    SQL.append ("\n '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
    SQL.append ("\n '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_otros_4399 " );
    SQL.append ("\n from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d, rm_tr_distribucion e, rh_tc_partidas_presupuestales g " ); 
    SQL.append ("\n where a.documento_adquisicion_id in (8,13) " ); 
    SQL.append ("\n and a.numero= '" + numero + "'");
    SQL.append ("\n and b.partida= '" + partida + "'");
    SQL.append ("\n and b.inciso= '" + inciso + "'");
    SQL.append ("\n and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append ("\n and a.proveedor_id=b.proveedor_id " ); 
    SQL.append ("\n and a.adquisicion_id=b.adquisicion_id " );
    SQL.append ("\n and b.proveedor_id=c.proveedor_id " );
    SQL.append ("\n and b.adquisicion_id=c.adquisicion_id " ); 
    SQL.append ("\n and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
    SQL.append ("\n and c.proveedor_id=d.proveedor_id" ); 
    SQL.append ("\n and c.proveedor_id=e.proveedor_id ");
    SQL.append ("\n and c.adquisicion_id=e.adquisicion_id ");
    SQL.append ("\n and c.detalle_adquisicion_id=e.detalle_adquisicion_id ");
    SQL.append ("\n and c.distribucion_id=e.distribucion_id ");
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
//    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) ");  
    SQL.append ("\n and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append ("\n and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append ("\n group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0)," ); 
    SQL.append ("\n d.rfc_proveedor,d.razon_social," );
    SQL.append ("\n c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
	//  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
          partidaGen="";
          liMes=Integer.parseInt(crs.getString("mes"))-1;
          if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
            if (crs.getString("conc").substring(0,3).equals("511"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("512"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("515"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
             
          if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
            if (crs.getString("conc").substring(0,3).equals("521"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("522"))
              partidaGen="2";              
            else if (crs.getString("conc").substring(0,3).equals("523"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
          
          if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
            if (crs.getString("conc").substring(0,3).equals("531"))
            partidaGen="1";
            else    
             partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }          

          if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
            if (crs.getString("conc").substring(0,3).equals("541"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("542"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("543"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("545"))
              partidaGen="5";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  

          if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
            if (crs.getString("conc").substring(0,3).equals("561"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("562"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("563"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("564"))
              partidaGen="4";
            else if (crs.getString("conc").substring(0,3).equals("565"))
              partidaGen="5"; 
            else if (crs.getString("conc").substring(0,3).equals("566"))
              partidaGen="6";
            else if (crs.getString("conc").substring(0,3).equals("567"))
              partidaGen="7";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
             
          if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
            if (crs.getString("conc").substring(0,3).equals("513"))
              partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
           }           
             
          if (crs.getString("conc").substring(0,3).equals("591")){
            partidaGen="1";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }             
             
          if (crs.getString("conc").substring(0,3).equals("599")){
            partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }         

          //    if (crs.getString("conc").substring(0,1).equals("5")){
          //       capitulo="5";
          //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
          //    }
             
          if (crs.getString("tipo").equals("0008")){
            tipoAlta="0006";
              concepto="ALTA DE BIENES MUEBLES INVENTARIABLES POR ACTA DE PROPIEDAD FEDERAL. ";
          }  
          else if (crs.getString("tipo").equals("0013")){
            tipoAlta="0003";
            concepto="ALTA DE BIENES MUEBLES INVENTARIABLES POR REPOSICION. ";
          }  
          else
            tipoAlta="";
              
          if (partidasGenIva.contains(crs.getString("conc"))){
             partidaCom=crs.getString("conc");
             if (!tipoAlta.equals(""))
               resultado=registro.AgregaCuentaContable("4399","2",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,tipoAlta,crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"8",idCatalogo);
          }
          
          hm.put ("PROGRAMA",programa1);//imprimeHM(hm);
          hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
          hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
          hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
          hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
          hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
          hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);
          hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
          hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
          hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
          hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
          hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm);
          hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
          hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
          hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);
          hm.put ("IMPORTE_OTROS_4399",crs.getString("imp_otros_4399"));//1imprimeHM(hm);
          hm.put ("REFERENCIA"," ");//imprimeHM(hm);
          hm.put ("TIPO_ALTA",tipoAlta);//imprimeHM(hm); Acta de propiedad federal y Alta por reposicion

        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        rfc = crs.getString("rfc");
        nombre_proveedor = crs.getString("nombre_proveedor");
        this.unidad = crs.getString("unid");
        this.ambito = crs.getString("ambi");
       // concepto = crs.getString("conc");
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
    String partidaGen="";
    String programa1="0001";
    String conPartida="";
    
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc,d.rfc_proveedor rfc, d.razon_social nombre_proveedor," ); 
    SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259, " ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
    SQL.append (" '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
    SQL.append (" '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
    SQL.append (" '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
    SQL.append (" '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_bienes_1293 " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tc_proveedor d, rm_tr_distribucion e, rh_tc_partidas_presupuestales g " ); 
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
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
//    SQL.append (" and trunc(c.fecha_alta_conc)=trunc(f.fecha_conciliacion) ");  
    SQL.append (" and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0)," ); 
    SQL.append (" d.rfc_proveedor,d.razon_social," );
    SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion  " );
	//  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();   
      while(crs.next()){
        partidaGen="";
        conPartida="";
        liMes=Integer.parseInt(crs.getString("mes"))-1;
/*
        if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
          if (crs.getString("conc").substring(0,3).equals("511"))
            partidaGen="1";
          else if (crs.getString("conc").substring(0,3).equals("512"))
            partidaGen="2";
          else if (crs.getString("conc").substring(0,3).equals("515"))
            partidaGen="3";
          else    
            partidaGen="9";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
        }  
 */           
          if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
            if (crs.getString("conc").substring(0,3).equals("521"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("522"))
              partidaGen="2";              
            else if (crs.getString("conc").substring(0,3).equals("523"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
/*          
          if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
            if (crs.getString("conc").substring(0,3).equals("531"))
            partidaGen="1";
            else    
             partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }          

          if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
            if (crs.getString("conc").substring(0,3).equals("541"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("542"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("543"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("545"))
              partidaGen="5";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  

          if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
            if (crs.getString("conc").substring(0,3).equals("561"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("562"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("563"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("564"))
              partidaGen="4";
            else if (crs.getString("conc").substring(0,3).equals("565"))
              partidaGen="5"; 
            else if (crs.getString("conc").substring(0,3).equals("566"))
              partidaGen="6";
            else if (crs.getString("conc").substring(0,3).equals("567"))
              partidaGen="7";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
             
          if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
            if (crs.getString("conc").substring(0,3).equals("513"))
              partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
           }           
             
          if (crs.getString("conc").substring(0,3).equals("591")){
            partidaGen="1";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }             
             
          if (crs.getString("conc").substring(0,3).equals("599")){
            partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }         
*/
          if (crs.getString("conc").substring(0,3).equals("52")){
             conPartida="2";
             resultado=registro.AgregaCuentaContable("1293",conPartida,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
             resultado=registro.AgregaCuentaContable("1293",conPartida,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }
             
          
          hm.put ("PROGRAMA",programa1);//imprimeHM(hm);
          hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
          hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
          hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
          hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
          hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
          hm.put ("CONCEPTO_PARTIDA",conPartida);//imprimeHM(hm);
          hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
          hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
          hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
          hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
          hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm);
          hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
          hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
          hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);
          hm.put ("IMPORTE_BIENES_1293",crs.getString("imp_bienes_1293"));//1imprimeHM(hm);
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
	
public String Forma11 (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito, String idCatalogo, Registro registro, String idAsociacion) throws SQLException, Exception {
    HashMap hm = new HashMap();  
    CachedRowSet crs = null;
    StringBuffer cadenaTem= new StringBuffer("");
    String resultado = "";
    int liMes = 0;
    //String partida12203 = "|53|56|59|";
    String partidaGen="";
    String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
    String partidaCom="";
    String programa1="0001";
    String tipoBaja="";            
            
     StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(d.partida_presupuestal,1,3),4,0) conc,");
    SQL.append (" d.año_afectacion ejer, lpad(substr(to_char(d.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, g.descripcion des_partida," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'511',d.valor_bien,'512',d.valor_bien,'515',d.valor_bien,'519',d.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'521',d.valor_bien,'522',d.valor_bien,'523',d.valor_bien,'529',d.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'531',d.valor_bien,'532',d.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'541',d.valor_bien,'542',d.valor_bien,'543',d.valor_bien,'545',d.valor_bien,'549',d.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'561',d.valor_bien,'562',d.valor_bien,'563',d.valor_bien,'564',d.valor_bien,'565',d.valor_bien,'566',d.valor_bien,'567',d.valor_bien,'569',d.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'513',d.valor_bien,'514',d.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'591',d.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'599',d.valor_bien,0)) imp_otros_1259, " );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'511',d.valor_bien,'512',d.valor_bien,'515',d.valor_bien,'519',d.valor_bien," ); 
    SQL.append (" '521',d.valor_bien,'522',d.valor_bien,'523',d.valor_bien,'529',d.valor_bien,'531',d.valor_bien,'532',d.valor_bien," );
    SQL.append (" '541',d.valor_bien,'542',d.valor_bien,'543',d.valor_bien,'545',d.valor_bien,'549',d.valor_bien," );   
    SQL.append (" '561',d.valor_bien,'562',d.valor_bien,'563',d.valor_bien,'564',d.valor_bien,'565',d.valor_bien,'566',d.valor_bien,'567',d.valor_bien,'569',d.valor_bien," );   
    SQL.append (" '513',d.valor_bien,'514',d.valor_bien,'591',d.valor_bien,'599',d.valor_bien,0)) imp_otros_5599 " );
    SQL.append (" from rm_tr_asociacion_lote a, rm_tr_conforma_lote_baja b, rm_tr_detalle_conforma c, rm_rh_control_inventario d, rh_tc_partidas_presupuestales g "); 
    SQL.append (" where a.disposicion_final_id = 6 " ); 
    SQL.append (" and a.nombre_evento= '" + nombre + "' ");
    SQL.append (" and to_date(to_char(d.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.asociacion_lote_id=" + idAsociacion);
    SQL.append (" and a.asociacion_lote_id=b.asociacion_lote_id ");
    SQL.append (" and a.ejercicio=b.ejercicio_asociacion ");
    SQL.append (" and b.conforma_lote_id=c.conforma_lote_id ");
    SQL.append (" and b.ejercicio=c.ejercicio ");
    SQL.append (" and c.control_inventario_id=d.control_inventario_id");
//    SQL.append (" and d.control_inventario_id=f.control_inventario_id");
//    SQL.append (" and trunc(d.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and substr(trim(to_char(d.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(d.partida_presupuestal,1,3),4,0),");
    SQL.append (" d.año_afectacion, lpad(substr(to_char(d.fecha_factura,'dd/mm/yyyy'),4,2),4,0),g.descripcion ");
   // SQL.append (" union all ");
   // SQL.append(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(a.partida_presupuestal,1,3),4,0) conc,");
   // SQL.append (" a.año_afectacion ejer, lpad(substr(a.fecha_factura,4,2),4,0) mes," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,0)) conc_mob," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'53',a.valor_bien,0)) conc_vehi," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'52',a.valor_bien,'54',a.valor_bien,'59',a.valor_bien,0)) conc_maq," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,'53',a.valor_bien,'52',a.valor_bien,'54',a.valor_bien,'59',a.valor_bien,0)) conc_todos " );
   // SQL.append (" from rm_rh_control_inventario a "); 
   // SQL.append (" where a.disposicion_final_id = 6 " ); 
   // SQL.append (" and a.nombre_evento= '" + nombre + "' ");
	//  SQL.append (" and to_date(to_char(a.fecha_movimiento,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
	//  SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
  //  SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(a.partida_presupuestal,1,3),4,0),");
  //  SQL.append (" a.año_afectacion, lpad(substr(a.fecha_factura,4,2),4,0) ");
	//  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
        //tipo = crs.getString("tipo").equals("0000")?"0005":crs.getString("tipo").equals("0007")?"0001":crs.getString("tipo").equals("0010")?"0005":crs.getString("tipo").equals("0012")?"0008":"0000";
        partidaGen="";
        liMes=Integer.parseInt(crs.getString("mes"))-1;
          if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
            if (crs.getString("conc").substring(0,3).equals("511"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("512"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("515"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
            if (crs.getString("conc").substring(0,3).equals("521"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("522"))
              partidaGen="2";              
            else if (crs.getString("conc").substring(0,3).equals("523"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
            
            if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
            if (crs.getString("conc").substring(0,3).equals("531"))
            partidaGen="1";
            else    
             partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
            if (crs.getString("conc").substring(0,3).equals("541"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("542"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("543"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("545"))
              partidaGen="5";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
            if (crs.getString("conc").substring(0,3).equals("561"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("562"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("563"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("564"))
              partidaGen="4";
            else if (crs.getString("conc").substring(0,3).equals("565"))
              partidaGen="5"; 
            else if (crs.getString("conc").substring(0,3).equals("566"))
              partidaGen="6";
            else if (crs.getString("conc").substring(0,3).equals("567"))
              partidaGen="7";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
            if (crs.getString("conc").substring(0,3).equals("513"))
              partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("591")){
            partidaGen="1";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("599")){
            partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            //    if (crs.getString("conc").substring(0,1).equals("5")){
            //       capitulo="5";
            //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
            //    }
             
            /*if (crs.getString("tipo").equals("0008"))
            tipoBaja="0006";
            else if (crs.getString("tipo").equals("0013"))
            tipoBaja="0003";
            else
            tipoBaja="";
            */
            tipoBaja="0004";
            if (partidasGenIva.contains(crs.getString("conc"))){
             partidaCom=crs.getString("conc");
             if (!tipoBaja.equals(""))
               resultado=registro.AgregaCuentaContable("5599","1",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,tipoBaja,crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"8",idCatalogo);
            }

        hm.put ("PROGRAMA",programa1);//imprimeHM(hm);        
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
        hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);                  
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
         hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
        hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
        hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
        hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);        
        hm.put ("IMPORTE_OTROS_5599",crs.getString("imp_otros_5599"));//1imprimeHM(hm);
        hm.put ("TIPO_BAJA",tipoBaja);//imprimeHM(hm); Transferencias
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
        
/* Se elimino la llamaba el evento 16        
public String Forma14 (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
    StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(d.partida_presupuestal,1,3),4,0) conc,");
    SQL.append (" d.año_afectacion ejer, lpad(substr(d.fecha_factura,4,2),4,0) mes, g.descripcion des_partida, ");
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'511',d.valor_bien,'512',d.valor_bien,'515',d.valor_bien,'519',d.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'521',d.valor_bien,'522',d.valor_bien,'523',d.valor_bien,'529',d.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'531',d.valor_bien,'532',d.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'541',d.valor_bien,'542',d.valor_bien,'543',d.valor_bien,'545',d.valor_bien,'549',d.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'561',d.valor_bien,'562',d.valor_bien,'563',d.valor_bien,'564',d.valor_bien,'565',d.valor_bien,'566',d.valor_bien,'567',d.valor_bien,'569',d.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'513',d.valor_bien,'514',d.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'591',d.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'599',d.valor_bien,0)) imp_otros_1259, " );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'511',d.valor_bien,'512',d.valor_bien,'515',d.valor_bien,'519',d.valor_bien," ); 
    SQL.append (" '521',d.valor_bien,'522',d.valor_bien,'523',d.valor_bien,'529',d.valor_bien,'531',d.valor_bien,'532',d.valor_bien," );
    SQL.append (" '541',d.valor_bien,'542',d.valor_bien,'543',d.valor_bien,'545',d.valor_bien,'549',d.valor_bien," );   
    SQL.append (" '561',d.valor_bien,'562',d.valor_bien,'563',d.valor_bien,'564',d.valor_bien,'565',d.valor_bien,'566',d.valor_bien,'567',d.valor_bien,'569',d.valor_bien," );   
    SQL.append (" '513',d.valor_bien,'514',d.valor_bien,'591',d.valor_bien,'599',d.valor_bien,0)) imp_otros_5599 " );
    SQL.append (" from rm_tr_asociacion_lote a, rm_tr_conforma_lote_baja b, rm_tr_detalle_conforma c, rm_rh_control_inventario d, rh_tc_partidas_presupuestales g "); 
    SQL.append (" where "); 
    SQL.append (" a.nombre_evento= '" + nombre + "' ");
	  SQL.append (" and to_date(to_char(d.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.disposicion_final_id = 6 ");
    SQL.append (" and a.asociacion_lote_id=b.asociacion_lote_id ");
    SQL.append (" and a.ejercicio=b.ejercicio_asociacion ");
    SQL.append (" and b.conforma_lote_id=c.conforma_lote_id ");
    SQL.append (" and b.ejercicio=c.ejercicio ");
    SQL.append (" and c.control_inventario_id=d.control_inventario_id");
//    SQL.append (" and d.control_inventario_id=f.control_inventario_id");
//    SQL.append (" and trunc(d.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and substr(trim(to_char(d.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||d.ambito,4,0),rpad(substr(d.partida_presupuestal,1,3),4,0),");
    SQL.append (" d.año_afectacion, lpad(substr(d.fecha_factura,4,2),4,0),g.descripcion ");
   // SQL.append (" union all ");
   // SQL.append (" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(a.partida_presupuestal,1,3),4,0) conc,");
   // SQL.append (" a.año_afectacion ejer, lpad(substr(a.fecha_factura,4,2),4,0) mes, ");
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'52',a.valor_bien,'54',a.valor_bien,'59',a.valor_bien,0)) conc_todos" );
   // SQL.append (" from rm_rh_control_inventario a");
   // SQL.append (" where "); 
   // SQL.append (" a.nombre_evento= '" + nombre + "' ");
	//  SQL.append (" and to_date(to_char(a.fecha_movimiento,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
	//  SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);

  //  SQL.append (" and a.disposicion_final_id = 6 ");
  //  SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(a.partida_presupuestal,1,3),4,0),");
  //  SQL.append (" a.año_afectacion, lpad(substr(a.fecha_factura,4,2),4,0) ");
//	  System.out.println(SQL.toString());
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
        hm.put ("IMPORTE_PATRIMONIALES",crs.getString("imp_otros_5599"));//1imprimeHM(hm);
        hm.put ("IMPORTE_BIENES",crs.getString("imp_otros_5599"));//1imprimeHM(hm);
        hm.put ("TIPO_BAJA","0004");//imprimeHM(hm); Transferencias
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
*/
public String Forma17 (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito, String idCatalogo, Registro registro, String idAsociacion) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
    StringBuffer cadenaTem= new StringBuffer("");
    String resultado = "";
    int liMes = 0;
    //String partida41201 = "|51|52|53|54|56|58|59|";
    //String partida12203 = "|53|56|59|";
    String partidaGen="";
    String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
    String partidaCom="";
    String programa1="0001";
    String tipoBaja="";    
    
    StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(d.partida_presupuestal,1,3),4,0) conc,");
    SQL.append (" d.año_afectacion ejer, lpad(substr(to_char(d.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, g.descripcion des_partida," ); 
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'511',d.valor_bien,'512',d.valor_bien,'515',d.valor_bien,'519',d.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'521',d.valor_bien,'522',d.valor_bien,'523',d.valor_bien,'529',d.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'531',d.valor_bien,'532',d.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'541',d.valor_bien,'542',d.valor_bien,'543',d.valor_bien,'545',d.valor_bien,'549',d.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'561',d.valor_bien,'562',d.valor_bien,'563',d.valor_bien,'564',d.valor_bien,'565',d.valor_bien,'566',d.valor_bien,'567',d.valor_bien,'569',d.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'513',d.valor_bien,'514',d.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'591',d.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'599',d.valor_bien,0)) imp_otros_1259, " );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'511',d.valor_bien,'512',d.valor_bien,'515',d.valor_bien,'519',d.valor_bien," ); 
    SQL.append (" '521',d.valor_bien,'522',d.valor_bien,'523',d.valor_bien,'529',d.valor_bien,'531',d.valor_bien,'532',d.valor_bien," );
    SQL.append (" '541',d.valor_bien,'542',d.valor_bien,'543',d.valor_bien,'545',d.valor_bien,'549',d.valor_bien," );   
    SQL.append (" '561',d.valor_bien,'562',d.valor_bien,'563',d.valor_bien,'564',d.valor_bien,'565',d.valor_bien,'566',d.valor_bien,'567',d.valor_bien,'569',d.valor_bien," );   
    SQL.append (" '513',d.valor_bien,'514',d.valor_bien,'591',d.valor_bien,'599',d.valor_bien,0)) imp_otros_5599 " );
    SQL.append (" from rm_tr_asociacion_lote a, rm_tr_conforma_lote_baja b, rm_tr_detalle_conforma c, rm_rh_control_inventario d, rh_tc_partidas_presupuestales g "); 
    SQL.append (" where a.disposicion_final_id = 3 " ); 
    SQL.append (" and a.nombre_evento= '" + nombre + "' ");
    SQL.append (" and to_date(to_char(d.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.asociacion_lote_id=" + idAsociacion);
    SQL.append (" and a.asociacion_lote_id=b.asociacion_lote_id ");
    SQL.append (" and a.ejercicio=b.ejercicio_asociacion ");
    SQL.append (" and b.conforma_lote_id=c.conforma_lote_id ");
    SQL.append (" and b.ejercicio=c.ejercicio ");
    SQL.append (" and c.control_inventario_id=d.control_inventario_id");
//    SQL.append (" and d.control_inventario_id=f.control_inventario_id");
//    SQL.append (" and trunc(d.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and substr(trim(to_char(d.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(d.partida_presupuestal,1,3),4,0),");
    SQL.append (" d.año_afectacion, lpad(substr(to_char(d.fecha_factura,'dd/mm/yyyy'),4,2),4,0),g.descripcion ");
   // SQL.append (" union all ");
   // SQL.append (" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(a.partida_presupuestal,1,3),4,0) conc,");
   // SQL.append (" a.año_afectacion ejer, lpad(substr(a.fecha_factura,4,2),4,0) mes," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,0)) conc_mob," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'53',a.valor_bien,0)) conc_vehi," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'52',a.valor_bien,'54',a.valor_bien,'59',a.valor_bien,0)) conc_maq," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,'53',a.valor_bien,'52',a.valor_bien,'54',a.valor_bien,'59',a.valor_bien,0)) conc_todos " );
   // SQL.append (" from rm_rh_control_inventario a");
   // SQL.append (" where a.disposicion_final_id = 3 " ); 
   // SQL.append (" and a.nombre_evento= '" + nombre + "' ");
	//  SQL.append (" and to_date(to_char(a.fecha_movimiento,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
	//  SQL.append (" and a.unidad_ejecutora='"+unidad+"' and a.coord_estatal="+entidad);
  //  SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(a.partida_presupuestal,1,3),4,0),");
  //  SQL.append (" a.año_afectacion, lpad(substr(a.fecha_factura,4,2),4,0) ");
	//  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
     
      while(crs.next()){
        partidaGen="";
        liMes=Integer.parseInt(crs.getString("mes"))-1;
          if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
            if (crs.getString("conc").substring(0,3).equals("511"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("512"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("515"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
            if (crs.getString("conc").substring(0,3).equals("521"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("522"))
              partidaGen="2";              
            else if (crs.getString("conc").substring(0,3).equals("523"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
            
            if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
            if (crs.getString("conc").substring(0,3).equals("531"))
            partidaGen="1";
            else    
             partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
            if (crs.getString("conc").substring(0,3).equals("541"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("542"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("543"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("545"))
              partidaGen="5";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
            if (crs.getString("conc").substring(0,3).equals("561"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("562"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("563"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("564"))
              partidaGen="4";
            else if (crs.getString("conc").substring(0,3).equals("565"))
              partidaGen="5"; 
            else if (crs.getString("conc").substring(0,3).equals("566"))
              partidaGen="6";
            else if (crs.getString("conc").substring(0,3).equals("567"))
              partidaGen="7";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
            if (crs.getString("conc").substring(0,3).equals("513"))
              partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("591")){
            partidaGen="1";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("599")){
            partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            //    if (crs.getString("conc").substring(0,1).equals("5")){
            //       capitulo="5";
            //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
            //    }
             
            /*if (crs.getString("tipo").equals("0008"))
            tipoBaja="0006";
            else if (crs.getString("tipo").equals("0013"))
            tipoBaja="0003";
            else
            tipoBaja="";
            */
            tipoBaja="0003";
            if (partidasGenIva.contains(crs.getString("conc"))){
             partidaCom=crs.getString("conc");
             if (!tipoBaja.equals(""))
               resultado=registro.AgregaCuentaContable("5599","1",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,tipoBaja,crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"8",idCatalogo);
            }

        hm.put ("PROGRAMA",programa1);//imprimeHM(hm);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
        hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);        
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
        hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
        hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
        hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);        
        hm.put ("IMPORTE_OTROS_5599",crs.getString("imp_otros_5599"));//1imprimeHM(hm);
        hm.put ("TIPO_BAJA",tipoBaja);//imprimeHM(hm); Donaciones
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
  public String Forma22A (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito, String idCatalogo, Registro registro,String idSiniestro) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
	  String resultado = "";
	  int liMes = 0;	  
	  //String partida12203 = "|53|56|59|";
	  //String partida52202 = "|51|52|53|54|56|58|59|";
          String partidaGen="";
	  String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
	  String partidaCom="";
	  String programa1="0001";
	  String tipoBaja="";
          
          
    StringBuffer SQL = new StringBuffer(" select lpad(c.unidad_ejecutora,4,0) unid,lpad(c.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc,a.numero_documento,");
   // SQL.append (" c.año_afectacion ejer, lpad(substr(c.fecha_factura,4,2),4,0) mes, lpad(1,4,0) tipo, ");
   // SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,0)) conc_mob," ); 
   // SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'53',c.valor_bien,0)) conc_vehi," );
   // SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'52',c.valor_bien,'54',c.valor_bien,'59',c.valor_bien,0)) conc_maq," );
   // SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,'53',c.valor_bien,'52',c.valor_bien,'54',c.valor_bien,'59',c.valor_bien,0)) conc_todos " );
   // SQL.append (" from  rm_tr_siniestro a, rm_tr_detalle_siniestro b,rm_rh_control_inventario c ");
   // SQL.append (" where "); 
   // SQL.append (" a.numero_documento= '" + nombre + "' ");	
   //  SQL.append (" and to_date(to_char(a.fecha_elaboracion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    //SQL.append (" and a.siniestro_id=b.siniestro_id ");
   // SQL.append (" and a.ejercicio=b.ejercicio ");
   // SQL.append (" and c.unidad_ejecutora='" +unidad+ "' and c.coord_estatal="+ entidad);
   // SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
   // SQL.append (" group by lpad(c.unidad_ejecutora,4,0),lpad(c.coord_estatal||'"+ambito+"',4,0),a.numero_documento,rpad(substr(c.partida_presupuestal,1,3),4,0),");
   // SQL.append (" c.año_afectacion, lpad(substr(c.fecha_factura,4,2),4,0),lpad(1,4,0) ");
   // SQL.append (" union all ");
   // SQL.append (" select lpad(c.unidad_ejecutora,4,0) unid,lpad(c.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc,a.numero_documento,");
    SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, lpad(c.disposicion_final_id,4,0) tipo, g.descripcion des_partida, ");
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259, " );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
    SQL.append (" '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
    SQL.append (" '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
    SQL.append (" '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
    SQL.append (" '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_otros_5599 " );
    SQL.append (" from  rm_rh_siniestro a, rm_rh_detalle_siniestro b,rm_rh_control_inventario c, rh_tc_partidas_presupuestales g ");
    SQL.append (" where "); 
    SQL.append (" a.numero_documento= '" + nombre + "' ");
    SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.siniestro_id=" + idSiniestro);
    SQL.append (" and a.siniestro_id=b.siniestro_id ");
    SQL.append (" and a.ejercicio=b.ejercicio ");
    SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
//    SQL.append (" and trunc(c.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append (" and c.disposicion_final_id in (1) ");
	  SQL.append (" and c.unidad_ejecutora='" +unidad+ "' and c.coord_estatal="+ entidad);
    SQL.append (" group by lpad(c.unidad_ejecutora,4,0),lpad(c.coord_estatal||'"+ambito+"',4,0),a.numero_documento,rpad(substr(c.partida_presupuestal,1,3),4,0),");
    SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),lpad(c.disposicion_final_id,4,0),g.descripcion ");
	//  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();   
      while(crs.next()){
        partidaGen="";
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
          if (crs.getString("conc").substring(0,3).equals("511"))
            partidaGen="1";
          else if (crs.getString("conc").substring(0,3).equals("512"))
            partidaGen="2";
          else if (crs.getString("conc").substring(0,3).equals("515"))
            partidaGen="3";
          else    
            partidaGen="9";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }
           
          if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
          if (crs.getString("conc").substring(0,3).equals("521"))
            partidaGen="1";
          else if (crs.getString("conc").substring(0,3).equals("522"))
            partidaGen="2";              
          else if (crs.getString("conc").substring(0,3).equals("523"))
            partidaGen="3";
          else    
            partidaGen="9";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }
          
          if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
          if (crs.getString("conc").substring(0,3).equals("531"))
          partidaGen="1";
          else    
           partidaGen="2";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }

          if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
          if (crs.getString("conc").substring(0,3).equals("541"))
            partidaGen="1";
          else if (crs.getString("conc").substring(0,3).equals("542"))
            partidaGen="2";
          else if (crs.getString("conc").substring(0,3).equals("543"))
            partidaGen="3";
          else if (crs.getString("conc").substring(0,3).equals("545"))
            partidaGen="5";
          else    
            partidaGen="9";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }

          if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
          if (crs.getString("conc").substring(0,3).equals("561"))
            partidaGen="1";
          else if (crs.getString("conc").substring(0,3).equals("562"))
            partidaGen="2";
          else if (crs.getString("conc").substring(0,3).equals("563"))
            partidaGen="3";
          else if (crs.getString("conc").substring(0,3).equals("564"))
            partidaGen="4";
          else if (crs.getString("conc").substring(0,3).equals("565"))
            partidaGen="5"; 
          else if (crs.getString("conc").substring(0,3).equals("566"))
            partidaGen="6";
          else if (crs.getString("conc").substring(0,3).equals("567"))
            partidaGen="7";
          else    
            partidaGen="9";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }
           
          if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
          if (crs.getString("conc").substring(0,3).equals("513"))
            partidaGen="1";
          else    
            partidaGen="2";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }
           
          if (crs.getString("conc").substring(0,3).equals("591")){
          partidaGen="1";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }
           
          if (crs.getString("conc").substring(0,3).equals("599")){
          partidaGen="9";
          //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
          resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
          resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }

          //    if (crs.getString("conc").substring(0,1).equals("5")){
          //       capitulo="5";
          //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
          //    }
           
          /*if (crs.getString("tipo").equals("0008"))
          tipoBaja="0006";
          else if (crs.getString("tipo").equals("0013"))
          tipoBaja="0003";
          else
          tipoBaja="";
          */
          tipoBaja="0002";
          if (partidasGenIva.contains(crs.getString("conc"))){
           partidaCom=crs.getString("conc");
           if (!tipoBaja.equals(""))
             resultado=registro.AgregaCuentaContable("5599","1",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,tipoBaja,crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"8",idCatalogo);
          }

        hm.put ("PROGRAMA",programa1);//imprimeHM(hm);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
        hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);          
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO","2");//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
        hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
        hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
        hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);        
        hm.put ("IMPORTE_OTROS_5599",crs.getString("imp_otros_5599"));//1imprimeHM(hm);
        hm.put ("TIPO_BAJA",tipoBaja);//imprimeHM(hm); Siniestro   
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
	  //String partida12203 = "|53|56|59|";
	  //String partida52202 = "|51|52|53|54|56|58|59|";
	  String partidaGen="";
	  String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
	  String partidaCom="";
	  String programa1="0001";
	  String tipoBaja="";          
    StringBuffer SQL = new StringBuffer(" select lpad(c.unidad_ejecutora,4,0) unid,lpad(c.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc,");
  //  SQL.append (" c.año_afectacion ejer, lpad(substr(a.fecha_elaboracion,4,2),4,0) mes,lpad(2,4,0) tipo, ");
  //  SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,0)) conc_mob," ); 
  //  SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'53',c.valor_bien,0)) conc_vehi," );
  //  SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'52',c.valor_bien,'54',c.valor_bien,'59',c.valor_bien,0)) conc_maq," );
  //  SQL.append (" sum(decode(substr(c.partida_presupuestal,1,2),'51',c.valor_bien,'53',c.valor_bien,'52',c.valor_bien,'54',c.valor_bien,'59',c.valor_bien,0)) conc_todos " );
  //  SQL.append (" from  rm_tr_pago_numerario a, rm_tr_detalle_pago_numerario b,rm_tr_control_inventario c ");
  //  SQL.append (" where "); 
  //  SQL.append (" a.nombre_lote= '" + nombre + "' ");
  //    SQL.append (" and to_date(to_char(a.fecha_elaboracion,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
  //  SQL.append (" and a.pago_numerario_id=b.pago_numerario_id ");
   // SQL.append (" and a.ejercicio=b.ejercicio ");
  //  SQL.append (" and c.unidad_ejecutora='" +unidad+ "' and c.coord_estatal="+ entidad);
  //  SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
  //  SQL.append (" group by lpad(c.unidad_ejecutora,4,0),lpad(c.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0),");
  //  SQL.append (" c.año_afectacion, lpad(substr(a.fecha_elaboracion,4,2),4,0),lpad(2,4,0) ");
  //  SQL.append (" union all ");
  //  SQL.append(" select lpad(c.unidad_ejecutora,4,0) unid,lpad(c.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc,");
    SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes,lpad(c.disposicion_final_id,4,0) tipo, g.descripcion des_partida, ");
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259, " );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
    SQL.append (" '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
    SQL.append (" '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
    SQL.append (" '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
    SQL.append (" '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_otros_5599 " );
    SQL.append (" from  rm_rh_pago_numerario a, rm_rh_detalle_pago_numerario b, rm_rh_control_inventario c, rh_tc_partidas_presupuestales g ");
    SQL.append (" where "); 
    SQL.append (" a.nombre_lote= '" + nombre + "' ");
	  SQL.append (" and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.pago_numerario_id=b.pago_numerario_id ");
    SQL.append (" and a.ejercicio=b.ejercicio ");
	  SQL.append (" and c.unidad_ejecutora='" +unidad+ "' and c.coord_estatal="+ entidad);
    SQL.append (" and b.control_inventario_id=c.control_inventario_id ");
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
//    SQL.append (" and trunc(c.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append (" and c.disposicion_final_id in (2) ");
    SQL.append (" group by lpad(c.unidad_ejecutora,4,0),lpad(c.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0),");
    SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),lpad(c.disposicion_final_id,4,0),g.descripcion ");
	//  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();   
      while(crs.next()){
        partidaGen="";
        liMes=Integer.parseInt(crs.getString("mes"))-1;        
          if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
            if (crs.getString("conc").substring(0,3).equals("511"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("512"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("515"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
            if (crs.getString("conc").substring(0,3).equals("521"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("522"))
              partidaGen="2";              
            else if (crs.getString("conc").substring(0,3).equals("523"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
            
            if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
            if (crs.getString("conc").substring(0,3).equals("531"))
            partidaGen="1";
            else    
             partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
            if (crs.getString("conc").substring(0,3).equals("541"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("542"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("543"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("545"))
              partidaGen="5";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
            if (crs.getString("conc").substring(0,3).equals("561"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("562"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("563"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("564"))
              partidaGen="4";
            else if (crs.getString("conc").substring(0,3).equals("565"))
              partidaGen="5"; 
            else if (crs.getString("conc").substring(0,3).equals("566"))
              partidaGen="6";
            else if (crs.getString("conc").substring(0,3).equals("567"))
              partidaGen="7";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
            if (crs.getString("conc").substring(0,3).equals("513"))
              partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("591")){
            partidaGen="1";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("599")){
            partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            //    if (crs.getString("conc").substring(0,1).equals("5")){
            //       capitulo="5";
            //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
            //    }
             
            /*if (crs.getString("tipo").equals("0008"))
            tipoBaja="0006";
            else if (crs.getString("tipo").equals("0013"))
            tipoBaja="0003";
            else
            tipoBaja="";
            */
            tipoBaja="0009";
            if (partidasGenIva.contains(crs.getString("conc"))){
             partidaCom=crs.getString("conc");
             if (!tipoBaja.equals(""))
               resultado=registro.AgregaCuentaContable("5599","1",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,tipoBaja,crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"8",idCatalogo);
            }
        
        
        hm.put ("PROGRAMA",programa1);//imprimeHM(hm);        
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
        hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);           
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO","9");//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
        hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
        hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
        hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);        
        hm.put ("IMPORTE_OTROS_5599",crs.getString("imp_otros_5599"));//1imprimeHM(hm);
        hm.put ("TIPO_BAJA",tipoBaja);//imprimeHM(hm); Pago por numerario
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
          //String partida12203 = "|53|56|59|";
	  //String partida52202 = "|51|52|53|54|56|58|59|";
	  String partidaGen="";
	  String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
	  String partidaCom="";
	  String programa1="0001";
	  String tipoBaja="";          
    StringBuffer SQL = new StringBuffer(" select lpad(c.unidad_ejecutora,4,0) unid,lpad(c.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc,a.numero_documento,");
       SQL.append ("\n c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, lpad(c.disposicion_final_id,4,0) tipo, g.descripcion des_partida, ");
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'531',c.valor_bien,'532',c.valor_bien,0)) imp_equipo_1243," );
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien,0)) imp_equipo_1244," );   
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'513',c.valor_bien,'514',c.valor_bien,0)) imp_colecciones_1247," );
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'591',c.valor_bien,0)) imp_software_1251," );
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'599',c.valor_bien,0)) imp_otros_1259, " );
    SQL.append ("\n sum(decode(substr(c.partida_presupuestal,1,3),'511',c.valor_bien,'512',c.valor_bien,'515',c.valor_bien,'519',c.valor_bien," ); 
    SQL.append ("\n '521',c.valor_bien,'522',c.valor_bien,'523',c.valor_bien,'529',c.valor_bien,'531',c.valor_bien,'532',c.valor_bien," );
    SQL.append ("\n '541',c.valor_bien,'542',c.valor_bien,'543',c.valor_bien,'545',c.valor_bien,'549',c.valor_bien," );   
    SQL.append ("\n '561',c.valor_bien,'562',c.valor_bien,'563',c.valor_bien,'564',c.valor_bien,'565',c.valor_bien,'566',c.valor_bien,'567',c.valor_bien,'569',c.valor_bien," );   
    SQL.append ("\n '513',c.valor_bien,'514',c.valor_bien,'591',c.valor_bien,'599',c.valor_bien,0)) imp_otros_5599 " );
    SQL.append ("\n from  rm_rh_baja_reposicion a, rm_rh_detalle_baja_reposicion b, rm_rh_control_inventario c, rh_tc_partidas_presupuestales g ");
    SQL.append ("\n where "); 
    SQL.append ("\n a.numero_documento= '" + nombre + "' ");
	  SQL.append ("\n and to_date(to_char(c.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append ("\n and a.baja_reposicion_id=b.baja_reposicion_id ");
    SQL.append ("\n and a.ejercicio=b.ejercicio ");
    SQL.append ("\n and b.control_inventario_id=c.control_inventario_id ");
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id ");
//    SQL.append (" and trunc(c.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append ("\n and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append ("\n and c.disposicion_final_id in (12) ");
	  SQL.append ("\n and c.unidad_ejecutora='" +unidad+ "' and c.coord_estatal="+ entidad);
    SQL.append ("\n group by lpad(c.unidad_ejecutora,4,0),lpad(c.coord_estatal||'"+ambito+"',4,0),a.numero_documento,rpad(substr(c.partida_presupuestal,1,3),4,0),");
    SQL.append ("\n c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),lpad(c.disposicion_final_id,4,0),g.descripcion ");
	//  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();  
      while(crs.next()){
        partidaGen="";
        liMes=Integer.parseInt(crs.getString("mes"))-1;
          if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
            if (crs.getString("conc").substring(0,3).equals("511"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("512"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("515"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
            if (crs.getString("conc").substring(0,3).equals("521"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("522"))
              partidaGen="2";              
            else if (crs.getString("conc").substring(0,3).equals("523"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
            
            if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
            if (crs.getString("conc").substring(0,3).equals("531"))
            partidaGen="1";
            else    
             partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
            if (crs.getString("conc").substring(0,3).equals("541"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("542"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("543"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("545"))
              partidaGen="5";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
            if (crs.getString("conc").substring(0,3).equals("561"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("562"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("563"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("564"))
              partidaGen="4";
            else if (crs.getString("conc").substring(0,3).equals("565"))
              partidaGen="5"; 
            else if (crs.getString("conc").substring(0,3).equals("566"))
              partidaGen="6";
            else if (crs.getString("conc").substring(0,3).equals("567"))
              partidaGen="7";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
            if (crs.getString("conc").substring(0,3).equals("513"))
              partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("591")){
            partidaGen="1";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("599")){
            partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            //    if (crs.getString("conc").substring(0,1).equals("5")){
            //       capitulo="5";
            //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
            //    }
             
            /*if (crs.getString("tipo").equals("0008"))
            tipoBaja="0006";
            else if (crs.getString("tipo").equals("0013"))
            tipoBaja="0003";
            else
            tipoBaja="";
            */
            tipoBaja="0008";
            if (partidasGenIva.contains(crs.getString("conc"))){
             partidaCom=crs.getString("conc");
             if (!tipoBaja.equals(""))
               resultado=registro.AgregaCuentaContable("5599","1",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,tipoBaja,crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"8",idCatalogo);
            }

        hm.put ("PROGRAMA",programa1);//imprimeHM(hm);        
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
        hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);          
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO","0008");//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
        hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
        hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
        hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);        
        hm.put ("IMPORTE_OTROS_5599",crs.getString("imp_otros_5599"));//1imprimeHM(hm);
        hm.put ("TIPO_BAJA",tipoBaja);//imprimeHM(hm); Baja por reposicion
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
  

public String Forma22 (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito, String idCatalogo, Registro registro, String idAsociacion) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
	  StringBuffer cadenaTem= new StringBuffer("");
    String resultado = "";
    int liMes = 0;
    // String partida12203 = "|53|56|59|";
    // String partida52202 = "|51|52|53|54|56|58|59|";
    String partidaGen="";
    String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
    String partidaCom="";
    String programa1="0001";
    String tipoBaja="";    
    
    StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(d.partida_presupuestal,1,3),4,0) conc,");
    SQL.append (" d.año_afectacion ejer,lpad(substr(to_char(d.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes,lpad(a.disposicion_final_id,4,0) tipo, g.descripcion des_partida," ); 
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'511',d.valor_bien,'512',d.valor_bien,'515',d.valor_bien,'519',d.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'521',d.valor_bien,'522',d.valor_bien,'523',d.valor_bien,'529',d.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'531',d.valor_bien,'532',d.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'541',d.valor_bien,'542',d.valor_bien,'543',d.valor_bien,'545',d.valor_bien,'549',d.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'561',d.valor_bien,'562',d.valor_bien,'563',d.valor_bien,'564',d.valor_bien,'565',d.valor_bien,'566',d.valor_bien,'567',d.valor_bien,'569',d.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'513',d.valor_bien,'514',d.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'591',d.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'599',d.valor_bien,0)) imp_otros_1259, " );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'511',d.valor_bien,'512',d.valor_bien,'515',d.valor_bien,'519',d.valor_bien," ); 
    SQL.append (" '521',d.valor_bien,'522',d.valor_bien,'523',d.valor_bien,'529',d.valor_bien,'531',d.valor_bien,'532',d.valor_bien," );
    SQL.append (" '541',d.valor_bien,'542',d.valor_bien,'543',d.valor_bien,'545',d.valor_bien,'549',d.valor_bien," );   
    SQL.append (" '561',d.valor_bien,'562',d.valor_bien,'563',d.valor_bien,'564',d.valor_bien,'565',d.valor_bien,'566',d.valor_bien,'567',d.valor_bien,'569',d.valor_bien," );   
    SQL.append (" '513',d.valor_bien,'514',d.valor_bien,'591',d.valor_bien,'599',d.valor_bien,0)) imp_otros_5599 " );
    SQL.append (" , a.SUBTIPO_DISP_FINAL_ID " );
    SQL.append (" from rm_tr_asociacion_lote a, rm_tr_conforma_lote_baja b, rm_tr_detalle_conforma c, rm_rh_control_inventario d, rh_tc_partidas_presupuestales g "); 
    //SQL.append (" where a.disposicion_final_id in (0,4,5,7,8,10) " ); 
     SQL.append (" where a.disposicion_final_id in (7,8) " ); // se elimino por Juan y Vicente 11/Nov/2011
    SQL.append (" and a.nombre_evento= '" + nombre + "' ");
    SQL.append (" and to_date(to_char(d.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.asociacion_lote_id=" + idAsociacion);
    SQL.append (" and a.asociacion_lote_id=b.asociacion_lote_id ");
    SQL.append (" and a.ejercicio=b.ejercicio_asociacion ");
    SQL.append (" and b.conforma_lote_id=c.conforma_lote_id ");
    SQL.append (" and b.ejercicio=c.ejercicio ");
    SQL.append (" and c.control_inventario_id=d.control_inventario_id");
//    SQL.append (" and d.control_inventario_id=f.control_inventario_id");
//    SQL.append (" and trunc(d.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and substr(trim(to_char(d.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append (" and a.unidad_ejecutora='" +unidad+ "' and a.coord_estatal="+ entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(d.partida_presupuestal,1,3),4,0),");
    SQL.append (" d.año_afectacion, lpad(substr(to_char(d.fecha_factura,'dd/mm/yyyy'),4,2),4,0),lpad(a.disposicion_final_id,4,0),g.descripcion, a.SUBTIPO_DISP_FINAL_ID ");
   // SQL.append (" union all ");
   // SQL.append(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(a.partida_presupuestal,1,3),4,0) conc,");
   // SQL.append (" a.año_afectacion ejer,lpad(substr(a.fecha_factura,4,2),4,0) mes,lpad(a.disposicion_final_id,4,0) tipo," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,0)) conc_mob," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'53',a.valor_bien,0)) conc_vehi," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'52',a.valor_bien,'54',a.valor_bien,'59',a.valor_bien,0)) conc_maq," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,'53',a.valor_bien,'52',a.valor_bien,'54',a.valor_bien,'59',a.valor_bien,0)) conc_todos " );
   // SQL.append (" from rm_rh_control_inventario a ");
   // SQL.append (" where a.disposicion_final_id in (0,4,5,7,8,10) " ); 
   // SQL.append (" and a.nombre_evento= '" + nombre + "' ");
   //  SQL.append (" and to_date(to_char(a.fecha_movimiento,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
   //  SQL.append (" and a.unidad_ejecutora='" +unidad+ "' and a.coord_estatal="+ entidad);
   // SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(a.partida_presupuestal,1,3),4,0),");
   // SQL.append (" a.año_afectacion, lpad(substr(a.fecha_factura,4,2),4,0),lpad(a.disposicion_final_id,4,0) ");

	  //SiaFmInventarios.java Se modificaron las formas 19 y 22 que seguramente se probarán de nuevo:  el motivo textualmente 
    //?En la 19 se agrego el valor alta por reposición que fue un 13 y en la 22 se agrego el valor de baja por reposición que fue un 12?
    //
    //      System.out.println(SQL.toString());
     try {
      
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      String tipo = null;  
      String destipo=null;
      while(crs.next()){
       // tipo = crs.getString("tipo").equals("0000")?"0005":(crs.getString("tipo").equals("0007")&&crs.getString("SUBTIPO_DISP_FINAL_ID").equals("9"))?"0006":(crs.getString("tipo").equals("0007")&&!crs.getString("SUBTIPO_DISP_FINAL_ID").equals("9"))?"0001":crs.getString("tipo").equals("0010")?"0005":crs.getString("tipo").equals("0012")?"0008":"0000";
       // destipo = crs.getString("tipo").equals("0000")?"Correcciones en el SCI":(crs.getString("tipo").equals("0007")&&crs.getString("SUBTIPO_DISP_FINAL_ID").equals("9"))?"Venta de Desechos":(crs.getString("tipo").equals("0007")&&!crs.getString("SUBTIPO_DISP_FINAL_ID").equals("9"))?"Enajenaciones":crs.getString("tipo").equals("0010")?"Correcciones en el SCI":crs.getString("tipo").equals("0012")?"Baja por Reposicion":"Pendiente";
        tipo = (crs.getString("tipo").equals("0007")&&crs.getString("SUBTIPO_DISP_FINAL_ID").equals("9"))?"0006":(crs.getString("tipo").equals("0007")&&crs.getString("SUBTIPO_DISP_FINAL_ID").equals("2"))?"0006":(crs.getString("tipo").equals("0007")&&!crs.getString("SUBTIPO_DISP_FINAL_ID").equals("9"))?"0001":crs.getString("tipo").equals("0008")?"0012":"0000"; // solo se aplican 7 Ventas de desechos(0006) o enajenaciones(0001) y destrucciones 8 desrucciones(0012)
        destipo = (crs.getString("tipo").equals("0007")&&crs.getString("SUBTIPO_DISP_FINAL_ID").equals("9"))?"Venta de Desechos":(crs.getString("tipo").equals("0007")&&crs.getString("SUBTIPO_DISP_FINAL_ID").equals("2"))?"Venta de Desechos":(crs.getString("tipo").equals("0007")&&!crs.getString("SUBTIPO_DISP_FINAL_ID").equals("9"))?"Enajenaciones":crs.getString("tipo").equals("0008")?"Destrucciones":"Pendiente";
        partidaGen="";
        liMes=Integer.parseInt(crs.getString("mes"))-1;
          if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
            if (crs.getString("conc").substring(0,3).equals("511"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("512"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("515"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
            if (crs.getString("conc").substring(0,3).equals("521"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("522"))
              partidaGen="2";              
            else if (crs.getString("conc").substring(0,3).equals("523"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
            
            if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
            if (crs.getString("conc").substring(0,3).equals("531"))
            partidaGen="1";
            else    
             partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
            if (crs.getString("conc").substring(0,3).equals("541"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("542"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("543"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("545"))
              partidaGen="5";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
            if (crs.getString("conc").substring(0,3).equals("561"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("562"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("563"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("564"))
              partidaGen="4";
            else if (crs.getString("conc").substring(0,3).equals("565"))
              partidaGen="5"; 
            else if (crs.getString("conc").substring(0,3).equals("566"))
              partidaGen="6";
            else if (crs.getString("conc").substring(0,3).equals("567"))
              partidaGen="7";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
            if (crs.getString("conc").substring(0,3).equals("513"))
              partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("591")){
            partidaGen="1";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("599")){
            partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            //    if (crs.getString("conc").substring(0,1).equals("5")){
            //       capitulo="5";
            //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
            //    }
             
            /*if (crs.getString("tipo").equals("0008"))
            tipoBaja="0006";
            else if (crs.getString("tipo").equals("0013"))
            tipoBaja="0003";
            else
            tipoBaja="";
            */
            tipoBaja=tipo;
            if (partidasGenIva.contains(crs.getString("conc"))){
             partidaCom=crs.getString("conc");
             if (!tipoBaja.equals(""))
               resultado=registro.AgregaCuentaContable("5599","1",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,tipoBaja,crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"8",idCatalogo);
            }
            
        hm.put ("PROGRAMA",programa1);//imprimeHM(hm);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
        hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm)        
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO",tipo);//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
        hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
        hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
        hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);        
        hm.put ("IMPORTE_OTROS_5599",crs.getString("imp_otros_5599"));//1imprimeHM(hm);
        hm.put ("TIPO_BAJA",tipoBaja);//imprimeHM(hm); 0 Cancelacion y depuracion de saldos, 4 permuta, 5 dacion en pago, 7 venta, 8 destruccion, 10 cancelacion
        hm.put ("REFERENCIA"," ");//imprimeHM(hm);
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
        refGral=tipo.equals("0006")?"BAJA VENTA DESECHOS":tipo.equals("0001")?"BAJA POR ENAJENACIONES":"BAJA POR DESTRUCCIONES";
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

public String Forma23 (Connection con,String fecha, String nombre, String unidad, String entidad, String ambito, String idCatalogo, Registro registro, String idAsociacion) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;      
    StringBuffer cadenaTem= new StringBuffer("");   
    String resultado = "";
    int liMes = 0;
    //String partida12203 = "|53|56|59|";
    String partidaGen="";
    String programa1="0001";
    String conPartida="";    
    StringBuffer SQL = new StringBuffer(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(d.partida_presupuestal,1,3),4,0) conc,");
    SQL.append (" d.año_afectacion ejer, lpad(substr(to_char(d.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes, g.descripcion des_partida," ); 
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'511',d.valor_bien,'512',d.valor_bien,'515',d.valor_bien,'519',d.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'521',d.valor_bien,'522',d.valor_bien,'523',d.valor_bien,'529',d.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'531',d.valor_bien,'532',d.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'541',d.valor_bien,'542',d.valor_bien,'543',d.valor_bien,'545',d.valor_bien,'549',d.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'561',d.valor_bien,'562',d.valor_bien,'563',d.valor_bien,'564',d.valor_bien,'565',d.valor_bien,'566',d.valor_bien,'567',d.valor_bien,'569',d.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'513',d.valor_bien,'514',d.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'591',d.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'599',d.valor_bien,0)) imp_otros_1259, " );
    SQL.append (" sum(decode(substr(d.partida_presupuestal,1,3),'511',d.valor_bien,'512',d.valor_bien,'515',d.valor_bien,'519',d.valor_bien," ); 
    SQL.append (" '521',d.valor_bien,'522',d.valor_bien,'523',d.valor_bien,'529',d.valor_bien,'531',d.valor_bien,'532',d.valor_bien," );
    SQL.append (" '541',d.valor_bien,'542',d.valor_bien,'543',d.valor_bien,'545',d.valor_bien,'549',d.valor_bien," );   
    SQL.append (" '561',d.valor_bien,'562',d.valor_bien,'563',d.valor_bien,'564',d.valor_bien,'565',d.valor_bien,'566',d.valor_bien,'567',d.valor_bien,'569',d.valor_bien," );   
    SQL.append (" '513',d.valor_bien,'514',d.valor_bien,'591',d.valor_bien,'599',d.valor_bien,0)) imp_bienes_1293 " );
    SQL.append (" from rm_tr_asociacion_lote a, rm_tr_conforma_lote_baja b, rm_tr_detalle_conforma c, rm_rh_control_inventario d, rh_tc_partidas_presupuestales g "); 
    SQL.append (" where a.disposicion_final_id in (9) " ); 
    SQL.append (" and a.nombre_evento= '" + nombre + "' ");
    SQL.append (" and to_date(to_char(d.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
    SQL.append (" and a.asociacion_lote_id=" + idAsociacion);
    SQL.append (" and a.asociacion_lote_id=b.asociacion_lote_id ");
    SQL.append (" and a.ejercicio=b.ejercicio_asociacion ");
    SQL.append (" and b.conforma_lote_id=c.conforma_lote_id ");
    SQL.append (" and b.ejercicio=c.ejercicio ");
    SQL.append (" and c.control_inventario_id=d.control_inventario_id");
//    SQL.append (" and d.control_inventario_id=f.control_inventario_id");
//    SQL.append (" and trunc(d.fecha_movimiento)=trunc(f.fecha_conciliacion) ");
    SQL.append (" and substr(trim(to_char(d.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
    SQL.append (" and a.unidad_ejecutora='" +unidad+ "' and a.coord_estatal="+ entidad);
    SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(d.partida_presupuestal,1,3),4,0), ");
    SQL.append (" d.año_afectacion, lpad(substr(to_char(d.fecha_factura,'dd/mm/yyyy'),4,2),4,0),g.descripcion ");
   // SQL.append (" union all ");
   // SQL.append(" select lpad(a.unidad_ejecutora,4,0) unid,lpad(a.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(a.partida_presupuestal,1,3),4,0) conc,");
   // SQL.append (" a.año_afectacion ejer, lpad(substr(a.fecha_factura,4,2),4,0) mes," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,0)) conc_mob," ); 
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'53',a.valor_bien,0)) conc_vehi," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'52',a.valor_bien,'54',a.valor_bien,'59',a.valor_bien,0)) conc_maq," );
   // SQL.append (" sum(decode(substr(a.partida_presupuestal,1,2),'51',a.valor_bien,'53',a.valor_bien,'52',a.valor_bien,'54',a.valor_bien,'59',a.valor_bien,0)) conc_todos " );
   // SQL.append (" from rm_rh_control_inventario a ");
   // SQL.append (" where a.disposicion_final_id in (9) " ); 
   // SQL.append (" and a.nombre_evento= '" + nombre + "' ");
	//SQL.append (" and to_date(to_char(a.fecha_movimiento,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
	//  SQL.append (" and a.unidad_ejecutora='" +unidad+ "' and a.coord_estatal="+ entidad);
  //  SQL.append (" group by lpad(a.unidad_ejecutora,4,0),lpad(a.coord_estatal||'"+ambito+"',4,0),rpad(substr(a.partida_presupuestal,1,3),4,0), ");
  //  SQL.append (" a.año_afectacion, lpad(substr(a.fecha_factura,4,2),4,0) ");
	//  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();  
      while(crs.next()){
        partidaGen="";
        conPartida="";
        liMes=Integer.parseInt(crs.getString("mes"))-1;
        /*  if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
            if (crs.getString("conc").substring(0,3).equals("511"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("512"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("515"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
         */    
            if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
            if (crs.getString("conc").substring(0,3).equals("521"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("522"))
              partidaGen="2";              
            else if (crs.getString("conc").substring(0,3).equals("523"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
            
        /*  if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
            if (crs.getString("conc").substring(0,3).equals("531"))
            partidaGen="1";
            else    
             partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
            if (crs.getString("conc").substring(0,3).equals("541"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("542"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("543"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("545"))
              partidaGen="5";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
            if (crs.getString("conc").substring(0,3).equals("561"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("562"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("563"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("564"))
              partidaGen="4";
            else if (crs.getString("conc").substring(0,3).equals("565"))
              partidaGen="5"; 
            else if (crs.getString("conc").substring(0,3).equals("566"))
              partidaGen="6";
            else if (crs.getString("conc").substring(0,3).equals("567"))
              partidaGen="7";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
            if (crs.getString("conc").substring(0,3).equals("513"))
              partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("591")){
            partidaGen="1";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("599")){
            partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            //    if (crs.getString("conc").substring(0,1).equals("5")){
            //       capitulo="5";
            //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
            //    }
             
            if (crs.getString("tipo").equals("0008"))
            tipoBaja="0006";
            else if (crs.getString("tipo").equals("0013"))
            tipoBaja="0003";
            else
            tipoBaja="";
          */
             if (crs.getString("conc").substring(0,3).equals("52")){
                conPartida="2";
                resultado=registro.AgregaCuentaContable("1293",conPartida,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
                resultado=registro.AgregaCuentaContable("1293",conPartida,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
             }


 
        hm.put ("PROGRAMA",programa1);//imprimeHM(hm);    
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
        hm.put ("CONCEPTO_PARTIDA",conPartida);//imprimeHM(hm);        
        hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
        hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
        hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
        hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);        
        hm.put ("IMPORTE_BIENES_1293",crs.getString("imp_bienes_1293"));//1imprimeHM(hm);
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
    // String partida12203 = "|53|56|59|";
    // String partida51201 = "|51|52|53|54|56|58|59|";
    String partidaGen="";
    String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
    String partidaCom="";
    String programa1="0001";
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc," ); 
    SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',e.valor_bien_actual-e.valor_bien,'512',e.valor_bien_actual-e.valor_bien,'515',e.valor_bien_actual-e.valor_bien,'519',e.valor_bien_actual-e.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'521',e.valor_bien_actual-e.valor_bien,'522',e.valor_bien_actual-e.valor_bien,'523',e.valor_bien_actual-e.valor_bien,'529',e.valor_bien_actual-e.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'531',e.valor_bien_actual-e.valor_bien,'532',e.valor_bien_actual-e.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'541',e.valor_bien_actual-e.valor_bien,'542',e.valor_bien_actual-e.valor_bien,'543',e.valor_bien_actual-e.valor_bien,'545',e.valor_bien_actual-e.valor_bien,'549',e.valor_bien_actual-e.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'561',e.valor_bien_actual-e.valor_bien,'562',e.valor_bien_actual-e.valor_bien,'563',e.valor_bien_actual-e.valor_bien,'564',e.valor_bien_actual-e.valor_bien,'565',e.valor_bien_actual-e.valor_bien,'566',e.valor_bien_actual-e.valor_bien,'567',e.valor_bien_actual-e.valor_bien,'569',e.valor_bien_actual-e.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'513',e.valor_bien_actual-e.valor_bien,'514',e.valor_bien_actual-e.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'591',e.valor_bien_actual-e.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'599',e.valor_bien_actual-e.valor_bien,0)) imp_otros_1259, " );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',e.valor_bien_actual-e.valor_bien,'512',e.valor_bien_actual-e.valor_bien,'515',e.valor_bien_actual-e.valor_bien,'519',e.valor_bien_actual-e.valor_bien," ); 
    SQL.append (" '521',e.valor_bien_actual-e.valor_bien,'522',e.valor_bien_actual-e.valor_bien,'523',e.valor_bien_actual-e.valor_bien,'529',e.valor_bien_actual-e.valor_bien,'531',e.valor_bien_actual-e.valor_bien,'532',e.valor_bien_actual-e.valor_bien," );
    SQL.append (" '541',e.valor_bien_actual-e.valor_bien,'542',e.valor_bien_actual-e.valor_bien,'543',e.valor_bien_actual-e.valor_bien,'545',e.valor_bien_actual-e.valor_bien,'549',e.valor_bien_actual-e.valor_bien," );   
    SQL.append (" '561',e.valor_bien_actual-e.valor_bien,'562',e.valor_bien_actual-e.valor_bien,'563',e.valor_bien_actual-e.valor_bien,'564',e.valor_bien_actual-e.valor_bien,'565',e.valor_bien_actual-e.valor_bien,'566',e.valor_bien_actual-e.valor_bien,'567',e.valor_bien_actual-e.valor_bien,'569',e.valor_bien_actual-e.valor_bien," );   
    SQL.append (" '513',e.valor_bien_actual-e.valor_bien,'514',e.valor_bien_actual-e.valor_bien,'591',e.valor_bien_actual-e.valor_bien,'599',e.valor_bien_actual-e.valor_bien,0)) imp_otros_4399 " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g  " ); 
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
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id" );
//    SQL.append (" and trunc(e.fecha_conciliacion)=trunc(f.fecha_conciliacion) " );
    SQL.append (" and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
	  SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0)," ); 
    SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
    SQL.append (" union all ");
    SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc," ); 
    SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',e.valor_bien_actual-e.valor_bien,'512',e.valor_bien_actual-e.valor_bien,'515',e.valor_bien_actual-e.valor_bien,'519',e.valor_bien_actual-e.valor_bien,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'521',e.valor_bien_actual-e.valor_bien,'522',e.valor_bien_actual-e.valor_bien,'523',e.valor_bien_actual-e.valor_bien,'529',e.valor_bien_actual-e.valor_bien,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'531',e.valor_bien_actual-e.valor_bien,'532',e.valor_bien_actual-e.valor_bien,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'541',e.valor_bien_actual-e.valor_bien,'542',e.valor_bien_actual-e.valor_bien,'543',e.valor_bien_actual-e.valor_bien,'545',e.valor_bien_actual-e.valor_bien,'549',e.valor_bien_actual-e.valor_bien,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'561',e.valor_bien_actual-e.valor_bien,'562',e.valor_bien_actual-e.valor_bien,'563',e.valor_bien_actual-e.valor_bien,'564',e.valor_bien_actual-e.valor_bien,'565',e.valor_bien_actual-e.valor_bien,'566',e.valor_bien_actual-e.valor_bien,'567',e.valor_bien_actual-e.valor_bien,'569',e.valor_bien_actual-e.valor_bien,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'513',e.valor_bien_actual-e.valor_bien,'514',e.valor_bien_actual-e.valor_bien,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'591',e.valor_bien_actual-e.valor_bien,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'599',e.valor_bien_actual-e.valor_bien,0)) imp_otros_1259, " );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',e.valor_bien_actual-e.valor_bien,'512',e.valor_bien_actual-e.valor_bien,'515',e.valor_bien_actual-e.valor_bien,'519',e.valor_bien_actual-e.valor_bien," ); 
    SQL.append (" '521',e.valor_bien_actual-e.valor_bien,'522',e.valor_bien_actual-e.valor_bien,'523',e.valor_bien_actual-e.valor_bien,'529',e.valor_bien_actual-e.valor_bien,'531',e.valor_bien_actual-e.valor_bien,'532',e.valor_bien_actual-e.valor_bien," );
    SQL.append (" '541',e.valor_bien_actual-e.valor_bien,'542',e.valor_bien_actual-e.valor_bien,'543',e.valor_bien_actual-e.valor_bien,'545',e.valor_bien_actual-e.valor_bien,'549',e.valor_bien_actual-e.valor_bien," );   
    SQL.append (" '561',e.valor_bien_actual-e.valor_bien,'562',e.valor_bien_actual-e.valor_bien,'563',e.valor_bien_actual-e.valor_bien,'564',e.valor_bien_actual-e.valor_bien,'565',e.valor_bien_actual-e.valor_bien,'566',e.valor_bien_actual-e.valor_bien,'567',e.valor_bien_actual-e.valor_bien,'569',e.valor_bien_actual-e.valor_bien," );   
    SQL.append (" '513',e.valor_bien_actual-e.valor_bien,'514',e.valor_bien_actual-e.valor_bien,'591',e.valor_bien_actual-e.valor_bien,'599',e.valor_bien_actual-e.valor_bien,0)) imp_otros_4399 " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_rh_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g  " ); 
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
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id" );
//    SQL.append (" and trunc(e.fecha_conciliacion)=trunc(f.fecha_conciliacion) " );
    SQL.append (" and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
	  SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0)," ); 
    SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
	//  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();    
      while(crs.next()){
        partidaGen="";
        liMes=Integer.parseInt(crs.getString("mes"))-1;
          if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
            if (crs.getString("conc").substring(0,3).equals("511"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("512"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("515"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
          
          if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
            if (crs.getString("conc").substring(0,3).equals("521"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("522"))
              partidaGen="2";              
            else if (crs.getString("conc").substring(0,3).equals("523"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
          
          if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
            if (crs.getString("conc").substring(0,3).equals("531"))
             partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }          

          if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
            if (crs.getString("conc").substring(0,3).equals("541"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("542"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("543"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("545"))
              partidaGen="5";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  

          if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
            if (crs.getString("conc").substring(0,3).equals("561"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("562"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("563"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("564"))
              partidaGen="4";
            else if (crs.getString("conc").substring(0,3).equals("565"))
              partidaGen="5"; 
            else if (crs.getString("conc").substring(0,3).equals("566"))
              partidaGen="6";
            else if (crs.getString("conc").substring(0,3).equals("567"))
              partidaGen="7";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
          
          if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
            if (crs.getString("conc").substring(0,3).equals("513"))
             partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }           
          
          if (crs.getString("conc").substring(0,3).equals("591")){
             partidaGen="1";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }             
          
          if (crs.getString("conc").substring(0,3).equals("599")){
             partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }         

          //    if (crs.getString("conc").substring(0,1).equals("5")){
          //       capitulo="5";
          //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
          //    }
          
          
            if (partidasGenIva.contains(crs.getString("conc"))){
              partidaCom=crs.getString("conc");
              resultado=registro.AgregaCuentaContable("4399","2",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,"0004",crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"8",idCatalogo);
            }

        hm.put ("PROGRAMA",programa1);//imprimeHM(hm);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
        hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);        
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
        hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
        hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
        hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);        
        hm.put ("IMPORTE_OTROS_4399",crs.getString("imp_otros_4399"));//1imprimeHM(hm);
        hm.put ("TIPO_ALTA","0004");//imprimeHM(hm);        
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
    String partidaGen="";
    String partidasGenIva="|5110|5120|5130|5140|5150|5190|5210|5220|5230|5290|5310|5320|5410|5420|5430|5450|5490|5610|5620|5630|5640|5650|5660|5670|5690|5910|5990|";
    String partidaCom="";
    String programa1="0001";
    String tipoBaja="";             
   // String partida12203 =  "|53|56|58|59|";
   // String partida52202 = "|51|52|53|54|56|58|59|";
    StringBuffer SQL = new StringBuffer(" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc," ); 
    SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',(e.valor_bien_actual-e.valor_bien)*-1,'512',(e.valor_bien_actual-e.valor_bien)*-1,'515',(e.valor_bien_actual-e.valor_bien)*-1,'519',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'521',(e.valor_bien_actual-e.valor_bien)*-1,'522',(e.valor_bien_actual-e.valor_bien)*-1,'523',(e.valor_bien_actual-e.valor_bien)*-1,'529',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'531',(e.valor_bien_actual-e.valor_bien)*-1,'532',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'541',(e.valor_bien_actual-e.valor_bien)*-1,'542',(e.valor_bien_actual-e.valor_bien)*-1,'543',(e.valor_bien_actual-e.valor_bien)*-1,'545',(e.valor_bien_actual-e.valor_bien)*-1,'549',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'561',(e.valor_bien_actual-e.valor_bien)*-1,'562',(e.valor_bien_actual-e.valor_bien)*-1,'563',(e.valor_bien_actual-e.valor_bien)*-1,'564',(e.valor_bien_actual-e.valor_bien)*-1,'565',(e.valor_bien_actual-e.valor_bien)*-1,'566',(e.valor_bien_actual-e.valor_bien)*-1,'567',(e.valor_bien_actual-e.valor_bien)*-1,'569',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'513',(e.valor_bien_actual-e.valor_bien)*-1,'514',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'591',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'599',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_otros_1259, " );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',(e.valor_bien_actual-e.valor_bien)*-1,'512',(e.valor_bien_actual-e.valor_bien)*-1,'515',(e.valor_bien_actual-e.valor_bien)*-1,'519',(e.valor_bien_actual-e.valor_bien)*-1," ); 
    SQL.append (" '521',(e.valor_bien_actual-e.valor_bien)*-1,'522',(e.valor_bien_actual-e.valor_bien)*-1,'523',(e.valor_bien_actual-e.valor_bien)*-1,'529',(e.valor_bien_actual-e.valor_bien)*-1,'531',(e.valor_bien_actual-e.valor_bien)*-1,'532',(e.valor_bien_actual-e.valor_bien)*-1," );
    SQL.append (" '541',(e.valor_bien_actual-e.valor_bien)*-1,'542',(e.valor_bien_actual-e.valor_bien)*-1,'543',(e.valor_bien_actual-e.valor_bien)*-1,'545',(e.valor_bien_actual-e.valor_bien)*-1,'549',(e.valor_bien_actual-e.valor_bien)*-1," );   
    SQL.append (" '561',(e.valor_bien_actual-e.valor_bien)*-1,'562',(e.valor_bien_actual-e.valor_bien)*-1,'563',(e.valor_bien_actual-e.valor_bien)*-1,'564',(e.valor_bien_actual-e.valor_bien)*-1,'565',(e.valor_bien_actual-e.valor_bien)*-1,'566',(e.valor_bien_actual-e.valor_bien)*-1,'567',(e.valor_bien_actual-e.valor_bien)*-1,'569',(e.valor_bien_actual-e.valor_bien)*-1," );   
    SQL.append (" '513',(e.valor_bien_actual-e.valor_bien)*-1,'514',(e.valor_bien_actual-e.valor_bien)*-1,'591',(e.valor_bien_actual-e.valor_bien)*-1,'599',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_otros_5599 " );
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g  " ); 
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
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id" );
//    SQL.append (" and trunc(e.fecha_conciliacion)=trunc(f.fecha_conciliacion) " );
    SQL.append (" and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
	  SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0)," ); 
    SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
    SQL.append (" union all ");
    SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(c.partida_presupuestal,1,3),4,0) conc," ); 
    SQL.append (" c.año_afectacion ejer, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',(e.valor_bien_actual-e.valor_bien)*-1,'512',(e.valor_bien_actual-e.valor_bien)*-1,'515',(e.valor_bien_actual-e.valor_bien)*-1,'519',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_mobiliario_1241," ); 
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'521',(e.valor_bien_actual-e.valor_bien)*-1,'522',(e.valor_bien_actual-e.valor_bien)*-1,'523',(e.valor_bien_actual-e.valor_bien)*-1,'529',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_mobiliario_1242," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'531',(e.valor_bien_actual-e.valor_bien)*-1,'532',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_equipo_1243," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'541',(e.valor_bien_actual-e.valor_bien)*-1,'542',(e.valor_bien_actual-e.valor_bien)*-1,'543',(e.valor_bien_actual-e.valor_bien)*-1,'545',(e.valor_bien_actual-e.valor_bien)*-1,'549',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_equipo_1244," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'561',(e.valor_bien_actual-e.valor_bien)*-1,'562',(e.valor_bien_actual-e.valor_bien)*-1,'563',(e.valor_bien_actual-e.valor_bien)*-1,'564',(e.valor_bien_actual-e.valor_bien)*-1,'565',(e.valor_bien_actual-e.valor_bien)*-1,'566',(e.valor_bien_actual-e.valor_bien)*-1,'567',(e.valor_bien_actual-e.valor_bien)*-1,'569',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_maquinaria_1246," );   
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'513',(e.valor_bien_actual-e.valor_bien)*-1,'514',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_colecciones_1247," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'591',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_software_1251," );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'599',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_otros_1259, " );
    SQL.append (" sum(decode(substr(c.partida_presupuestal,1,3),'511',(e.valor_bien_actual-e.valor_bien)*-1,'512',(e.valor_bien_actual-e.valor_bien)*-1,'515',(e.valor_bien_actual-e.valor_bien)*-1,'519',(e.valor_bien_actual-e.valor_bien)*-1," ); 
    SQL.append (" '521',(e.valor_bien_actual-e.valor_bien)*-1,'522',(e.valor_bien_actual-e.valor_bien)*-1,'523',(e.valor_bien_actual-e.valor_bien)*-1,'529',(e.valor_bien_actual-e.valor_bien)*-1,'531',(e.valor_bien_actual-e.valor_bien)*-1,'532',(e.valor_bien_actual-e.valor_bien)*-1," );
    SQL.append (" '541',(e.valor_bien_actual-e.valor_bien)*-1,'542',(e.valor_bien_actual-e.valor_bien)*-1,'543',(e.valor_bien_actual-e.valor_bien)*-1,'545',(e.valor_bien_actual-e.valor_bien)*-1,'549',(e.valor_bien_actual-e.valor_bien)*-1," );   
    SQL.append (" '561',(e.valor_bien_actual-e.valor_bien)*-1,'562',(e.valor_bien_actual-e.valor_bien)*-1,'563',(e.valor_bien_actual-e.valor_bien)*-1,'564',(e.valor_bien_actual-e.valor_bien)*-1,'565',(e.valor_bien_actual-e.valor_bien)*-1,'566',(e.valor_bien_actual-e.valor_bien)*-1,'567',(e.valor_bien_actual-e.valor_bien)*-1,'569',(e.valor_bien_actual-e.valor_bien)*-1," );   
    SQL.append (" '513',(e.valor_bien_actual-e.valor_bien)*-1,'514',(e.valor_bien_actual-e.valor_bien)*-1,'591',(e.valor_bien_actual-e.valor_bien)*-1,'599',(e.valor_bien_actual-e.valor_bien)*-1,0)) imp_otros_5599 " );
  
    SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_rh_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g " ); 
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
//    SQL.append (" and c.control_inventario_id=f.control_inventario_id" );
//    SQL.append (" and trunc(e.fecha_conciliacion)=trunc(f.fecha_conciliacion) " );
    SQL.append (" and substr(trim(to_char(c.partida_presupuestal)),1,3)=trim(to_char(g.partida_presupuestal)) ");
	  SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
    SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(c.partida_presupuestal,1,3),4,0)," ); 
    SQL.append (" c.año_afectacion, lpad(substr(to_char(c.fecha_factura,'dd/mm/yyyy'),4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
  //  System.out.println(SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst();
      while(crs.next()){
        //falta preguntar cuando dispocicion final es 0 que corresponde ahora a una cancelacion y a que num de subcuenta pertenece en contabilidad, y como identificar 
        //a que evento pertenece
        partidaGen="";
        liMes=Integer.parseInt(crs.getString("mes"))-1;

          if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
            if (crs.getString("conc").substring(0,3).equals("511"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("512"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("515"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
            if (crs.getString("conc").substring(0,3).equals("521"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("522"))
              partidaGen="2";              
            else if (crs.getString("conc").substring(0,3).equals("523"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
            
            if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
            if (crs.getString("conc").substring(0,3).equals("531"))
            partidaGen="1";
            else    
             partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
            if (crs.getString("conc").substring(0,3).equals("541"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("542"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("543"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("545"))
              partidaGen="5";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
            if (crs.getString("conc").substring(0,3).equals("561"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("562"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("563"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("564"))
              partidaGen="4";
            else if (crs.getString("conc").substring(0,3).equals("565"))
              partidaGen="5"; 
            else if (crs.getString("conc").substring(0,3).equals("566"))
              partidaGen="6";
            else if (crs.getString("conc").substring(0,3).equals("567"))
              partidaGen="7";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
            if (crs.getString("conc").substring(0,3).equals("513"))
              partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("591")){
            partidaGen="1";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }
             
            if (crs.getString("conc").substring(0,3).equals("599")){
            partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
            }

            //    if (crs.getString("conc").substring(0,1).equals("5")){
            //       capitulo="5";
            //       resultado=registro.AgregaCuentaContable("2112",capitulo,programa1,crs.getString("unid"),crs.getString("ambi"),lsIdContable,"0000","0000","0000",fecha,crs.getString("nombre_proveedor"),"6",idCatalogo);
            //    }
             
            /*if (crs.getString("tipo").equals("0008"))
            tipoBaja="0006";
            else if (crs.getString("tipo").equals("0013"))
            tipoBaja="0003";
            else
            tipoBaja="";
            */
            tipoBaja="0005";
            if (partidasGenIva.contains(crs.getString("conc"))){
             partidaCom=crs.getString("conc");
             if (!tipoBaja.equals(""))
               resultado=registro.AgregaCuentaContable("5599","1",programa1,crs.getString("unid"),crs.getString("ambi"),partidaCom,tipoBaja,crs.getString("ejer"),"0000",fecha,"Ejercicio "+crs.getString("ejer"),"8",idCatalogo);
            }

        hm.put ("PROGRAMA",programa1);//imprimeHM(hm);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
        hm.put ("PARTIDA_COM",partidaCom);//imprimeHM(hm);          
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1241",crs.getString("imp_mobiliario_1241"));//imprimeHM(hm);
        hm.put ("IMPORTE_MOBILIARIO_1242",crs.getString("imp_mobiliario_1242"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1243",crs.getString("imp_equipo_1243"));//imprimeHM(hm);
        hm.put ("IMPORTE_EQUIPO_1244",crs.getString("imp_equipo_1244"));//imprimeHM(hm);
        hm.put ("IMPORTE_MAQUINARIA_1246",crs.getString("imp_maquinaria_1246"));//imprimeHM(hm); 
        hm.put ("IMPORTE_COLECCIONES_1247",crs.getString("imp_colecciones_1247"));//imprimeHM(hm);
        hm.put ("IMPORTE_SOFTWARE_1251",crs.getString("imp_software_1251"));//imprimeHM(hm);
        hm.put ("IMPORTE_OTROS_1259",crs.getString("imp_otros_1259"));//imprimeHM(hm);        
        hm.put ("IMPORTE_OTROS_5599",crs.getString("imp_otros_5599"));//1imprimeHM(hm);
        hm.put ("TIPO_BAJA",tipoBaja);//1imprimeHM(hm);        
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

    public String Forma27 (Connection con,String fecha, String numero, String unidad, String entidad, String ambito,String idCatalogo, Registro registro, String partida, String inciso,String tipoAjuste) throws SQLException, Exception {
      HashMap hm = new HashMap();
      CachedRowSet crs = null;
      StringBuffer cadenaTem= new StringBuffer("");
      String resultado = "";
      int liMes = 0;
      String partidaGen="";
      String programa1="0001";      
      // String partida12203 = "|53|55|56|59|";
      StringBuffer SQL;
      SQL = new StringBuffer("");

      if (tipoAjuste.equals("3")) {
        if (fecha.equals("28/02/2011")) {

          SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_anterior,1,3),4,0) conc," ); 
          SQL.append ("    e.a�o_afecta_anterior ejer, lpad(substr(e.fecha_anterior_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'511',e.valor_bien_actual*-1,'512',e.valor_bien_actual*-1,'515',e.valor_bien_actual*-1,'519',e.valor_bien_actual*-1,0)) imp_mobiliario_1241," ); 
    	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'521',e.valor_bien_actual*-1,'522',e.valor_bien_actual*-1,'523',e.valor_bien_actual*-1,'529',e.valor_bien_actual*-1,0)) imp_mobiliario_1242," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'531',e.valor_bien_actual*-1,'532',e.valor_bien_actual*-1,0)) imp_equipo_1243," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'541',e.valor_bien_actual*-1,'542',e.valor_bien_actual*-1,'543',e.valor_bien_actual*-1,'545',e.valor_bien_actual*-1,'549',e.valor_bien_actual*-1,0)) imp_equipo_1244," );   
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'561',e.valor_bien_actual*-1,'562',e.valor_bien_actual*-1,'563',e.valor_bien_actual*-1,'564',e.valor_bien_actual*-1,'565',e.valor_bien_actual*-1,'566',e.valor_bien_actual*-1,'567',e.valor_bien_actual*-1,'569',e.valor_bien_actual*-1,0)) imp_maquinaria_1246," );   
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'513',e.valor_bien_actual*-1,'514',e.valor_bien_actual*-1,0)) imp_colecciones_1247," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'591',e.valor_bien_actual*-1,0)) imp_software_1251," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'599',e.valor_bien_actual*-1,0)) imp_otros_1259 " );
          SQL.append ("  from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g " ); 
          SQL.append (" where e.numero= '" + numero + "'");
	  SQL.append ("   and e.partida= '" + partida + "'");
    	  SQL.append ("   and e.inciso= '" + inciso + "'");
          SQL.append ("   and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append ("   and e.tipo_ajuste_id=3 " ); 
          SQL.append ("   and a.proveedor_id=b.proveedor_id " ); 
          SQL.append ("   and a.adquisicion_id=b.adquisicion_id " );
          SQL.append ("   and b.proveedor_id=c.proveedor_id " );
          SQL.append ("   and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append ("   and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append ("   and c.control_inventario_id=e.control_inventario_id" );
          SQL.append ("   and substr(trim(to_char(e.partida_presup_anterior)),1,4)=trim(to_char(g.partida_presupuestal)) ");          
          SQL.append ("   and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_anterior,1,3),4,0)," ); 
          SQL.append ("       e.a�o_afecta_anterior , lpad(substr(e.fecha_anterior_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
          SQL.append (" union all ");
          SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_actual,1,3),4,0) conc," ); 
          SQL.append ("    e.a�o_afecta_actual ejer, lpad(substr(e.fecha_actual_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," );           
          SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'511',e.valor_bien_actual,'512',e.valor_bien_actual,'515',e.valor_bien_actual,'519',e.valor_bien_actual,0)) imp_mobiliario_1241," ); 
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'521',e.valor_bien_actual,'522',e.valor_bien_actual,'523',e.valor_bien_actual,'529',e.valor_bien_actual,0)) imp_mobiliario_1242," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'531',e.valor_bien_actual,'532',e.valor_bien_actual,0)) imp_equipo_1243," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'541',e.valor_bien_actual,'542',e.valor_bien_actual,'543',e.valor_bien_actual,'545',e.valor_bien_actual,'549',e.valor_bien_actual,0)) imp_equipo_1244," );   
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'561',e.valor_bien_actual,'562',e.valor_bien_actual,'563',e.valor_bien_actual,'564',e.valor_bien_actual,'565',e.valor_bien_actual,'566',e.valor_bien_actual,'567',e.valor_bien_actual,'569',e.valor_bien_actual,0)) imp_maquinaria_1246," );   
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'513',e.valor_bien_actual,'514',e.valor_bien_actual,0)) imp_colecciones_1247," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'591',e.valor_bien_actual,0)) imp_software_1251," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'599',e.valor_bien_actual,0)) imp_otros_1259 " ); 
          SQL.append ("  from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g " ); 
          SQL.append (" where e.numero= '" + numero + "'");
          SQL.append ("   and e.partida= '" + partida + "'");
    	  SQL.append ("   and e.inciso= '" + inciso + "'");
          SQL.append ("   and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append ("   and e.tipo_ajuste_id=3 " ); 
          SQL.append ("   and a.proveedor_id=b.proveedor_id " ); 
          SQL.append ("   and a.adquisicion_id=b.adquisicion_id " );
          SQL.append ("   and b.proveedor_id=c.proveedor_id " );
          SQL.append ("   and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append ("   and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append ("   and c.control_inventario_id=e.control_inventario_id" );
          SQL.append ("   and substr(trim(to_char(e.partida_presup_actual)),1,3)=trim(to_char(g.partida_presupuestal)) ");          
          SQL.append ("   and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_actual,1,3),4,0)," ); 
          SQL.append ("       e.a�o_afecta_actual, lpad(substr(e.fecha_actual_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
          SQL.append (" union all ");
          SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_anterior,1,3),4,0) conc," ); 
          SQL.append ("    e.a�o_afecta_anterior ejer, lpad(substr(e.fecha_anterior_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'511',e.valor_bien_actual*-1,'512',e.valor_bien_actual*-1,'515',e.valor_bien_actual*-1,'519',e.valor_bien_actual*-1,0)) imp_mobiliario_1241," ); 
    	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'521',e.valor_bien_actual*-1,'522',e.valor_bien_actual*-1,'523',e.valor_bien_actual*-1,'529',e.valor_bien_actual*-1,0)) imp_mobiliario_1242," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'531',e.valor_bien_actual*-1,'532',e.valor_bien_actual*-1,0)) imp_equipo_1243," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'541',e.valor_bien_actual*-1,'542',e.valor_bien_actual*-1,'543',e.valor_bien_actual*-1,'545',e.valor_bien_actual*-1,'549',e.valor_bien_actual*-1,0)) imp_equipo_1244," );   
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'561',e.valor_bien_actual*-1,'562',e.valor_bien_actual*-1,'563',e.valor_bien_actual*-1,'564',e.valor_bien_actual*-1,'565',e.valor_bien_actual*-1,'566',e.valor_bien_actual*-1,'567',e.valor_bien_actual*-1,'569',e.valor_bien_actual*-1,0)) imp_maquinaria_1246," );   
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'513',e.valor_bien_actual*-1,'514',e.valor_bien_actual*-1,0)) imp_colecciones_1247," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'591',e.valor_bien_actual*-1,0)) imp_software_1251," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'599',e.valor_bien_actual*-1,0)) imp_otros_1259 " );
          SQL.append ("  from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_rh_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g " ); 
          SQL.append (" where e.numero= '" + numero + "'");
	  SQL.append ("   and e.partida= '" + partida + "'");
    	  SQL.append ("   and e.inciso= '" + inciso + "'");
          SQL.append ("   and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append ("   and e.tipo_ajuste_id=3 " ); 
          SQL.append ("   and a.proveedor_id=b.proveedor_id " ); 
          SQL.append ("   and a.adquisicion_id=b.adquisicion_id " );
          SQL.append ("   and b.proveedor_id=c.proveedor_id " );
          SQL.append ("   and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append ("   and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append ("   and c.control_inventario_id=e.control_inventario_id" );
          SQL.append ("   and substr(trim(to_char(e.partida_presup_anterior)),1,4)=trim(to_char(g.partida_presupuestal)) ");          
          SQL.append ("   and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_anterior,1,3),4,0)," ); 
          SQL.append ("       e.a�o_afecta_anterior , lpad(substr(e.fecha_anterior_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
          SQL.append (" union all ");
          SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_actual,1,3),4,0) conc," ); 
          SQL.append ("    e.a�o_afecta_actual ejer, lpad(substr(e.fecha_actual_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," );           
          SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'511',e.valor_bien_actual,'512',e.valor_bien_actual,'515',e.valor_bien_actual,'519',e.valor_bien_actual,0)) imp_mobiliario_1241," ); 
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'521',e.valor_bien_actual,'522',e.valor_bien_actual,'523',e.valor_bien_actual,'529',e.valor_bien_actual,0)) imp_mobiliario_1242," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'531',e.valor_bien_actual,'532',e.valor_bien_actual,0)) imp_equipo_1243," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'541',e.valor_bien_actual,'542',e.valor_bien_actual,'543',e.valor_bien_actual,'545',e.valor_bien_actual,'549',e.valor_bien_actual,0)) imp_equipo_1244," );   
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'561',e.valor_bien_actual,'562',e.valor_bien_actual,'563',e.valor_bien_actual,'564',e.valor_bien_actual,'565',e.valor_bien_actual,'566',e.valor_bien_actual,'567',e.valor_bien_actual,'569',e.valor_bien_actual,0)) imp_maquinaria_1246," );   
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'513',e.valor_bien_actual,'514',e.valor_bien_actual,0)) imp_colecciones_1247," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'591',e.valor_bien_actual,0)) imp_software_1251," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'599',e.valor_bien_actual,0)) imp_otros_1259 " ); 
          SQL.append ("  from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_rh_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g " ); 
          SQL.append (" where e.numero= '" + numero + "'");
          SQL.append ("   and e.partida= '" + partida + "'");
    	  SQL.append ("   and e.inciso= '" + inciso + "'");
          SQL.append ("   and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append ("   and e.tipo_ajuste_id=3 " ); 
          SQL.append ("   and a.proveedor_id=b.proveedor_id " ); 
          SQL.append ("   and a.adquisicion_id=b.adquisicion_id " );
          SQL.append ("   and b.proveedor_id=c.proveedor_id " );
          SQL.append ("   and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append ("   and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append ("   and c.control_inventario_id=e.control_inventario_id" );
          SQL.append ("   and substr(trim(to_char(e.partida_presup_actual)),1,3)=trim(to_char(g.partida_presupuestal)) ");          
          SQL.append ("   and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_actual,1,3),4,0)," ); 
          SQL.append ("       e.a�o_afecta_actual, lpad(substr(e.fecha_actual_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );


        } else {

          SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_anterior,1,3),4,0) conc," ); 
          SQL.append ("    e.a�o_afecta_anterior ejer, lpad(substr(e.fecha_anterior_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'511',e.valor_bien_actual*-1,'512',e.valor_bien_actual*-1,'515',e.valor_bien_actual*-1,'519',e.valor_bien_actual*-1,0)) imp_mobiliario_1241," ); 
    	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'521',e.valor_bien_actual*-1,'522',e.valor_bien_actual*-1,'523',e.valor_bien_actual*-1,'529',e.valor_bien_actual*-1,0)) imp_mobiliario_1242," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'531',e.valor_bien_actual*-1,'532',e.valor_bien_actual*-1,0)) imp_equipo_1243," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'541',e.valor_bien_actual*-1,'542',e.valor_bien_actual*-1,'543',e.valor_bien_actual*-1,'545',e.valor_bien_actual*-1,'549',e.valor_bien_actual*-1,0)) imp_equipo_1244," );   
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'561',e.valor_bien_actual*-1,'562',e.valor_bien_actual*-1,'563',e.valor_bien_actual*-1,'564',e.valor_bien_actual*-1,'565',e.valor_bien_actual*-1,'566',e.valor_bien_actual*-1,'567',e.valor_bien_actual*-1,'569',e.valor_bien_actual*-1,0)) imp_maquinaria_1246," );   
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'513',e.valor_bien_actual*-1,'514',e.valor_bien_actual*-1,0)) imp_colecciones_1247," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'591',e.valor_bien_actual*-1,0)) imp_software_1251," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'599',e.valor_bien_actual*-1,0)) imp_otros_1259 " );
          SQL.append ("  from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g " ); 
          SQL.append (" where e.numero= '" + numero + "'");
	  SQL.append ("   and e.partida= '" + partida + "'");
    	  SQL.append ("   and e.inciso= '" + inciso + "'");
          SQL.append ("   and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append ("   and e.tipo_ajuste_id=3 " ); 
          SQL.append ("   and a.proveedor_id=b.proveedor_id " ); 
          SQL.append ("   and a.adquisicion_id=b.adquisicion_id " );
          SQL.append ("   and b.proveedor_id=c.proveedor_id " );
          SQL.append ("   and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append ("   and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append ("   and c.control_inventario_id=e.control_inventario_id" );
          SQL.append ("   and substr(trim(to_char(e.partida_presup_anterior)),1,3)=trim(to_char(g.partida_presupuestal)) ");          
          SQL.append ("   and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_anterior,1,3),4,0)," ); 
          SQL.append ("       e.a�o_afecta_anterior , lpad(substr(e.fecha_anterior_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
          SQL.append (" union all ");
          SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_actual,1,3),4,0) conc," ); 
          SQL.append ("    e.a�o_afecta_actual ejer, lpad(substr(e.fecha_actual_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," );           
          SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'511',e.valor_bien_actual,'512',e.valor_bien_actual,'515',e.valor_bien_actual,'519',e.valor_bien_actual,0)) imp_mobiliario_1241," ); 
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'521',e.valor_bien_actual,'522',e.valor_bien_actual,'523',e.valor_bien_actual,'529',e.valor_bien_actual,0)) imp_mobiliario_1242," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'531',e.valor_bien_actual,'532',e.valor_bien_actual,0)) imp_equipo_1243," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'541',e.valor_bien_actual,'542',e.valor_bien_actual,'543',e.valor_bien_actual,'545',e.valor_bien_actual,'549',e.valor_bien_actual,0)) imp_equipo_1244," );   
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'561',e.valor_bien_actual,'562',e.valor_bien_actual,'563',e.valor_bien_actual,'564',e.valor_bien_actual,'565',e.valor_bien_actual,'566',e.valor_bien_actual,'567',e.valor_bien_actual,'569',e.valor_bien_actual,0)) imp_maquinaria_1246," );   
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'513',e.valor_bien_actual,'514',e.valor_bien_actual,0)) imp_colecciones_1247," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'591',e.valor_bien_actual,0)) imp_software_1251," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'599',e.valor_bien_actual,0)) imp_otros_1259 " ); 
          SQL.append ("  from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g " ); 
          SQL.append (" where e.numero= '" + numero + "'");
          SQL.append ("   and e.partida= '" + partida + "'");
    	  SQL.append ("   and e.inciso= '" + inciso + "'");
          SQL.append ("   and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append ("   and e.tipo_ajuste_id=3 " ); 
          SQL.append ("   and a.proveedor_id=b.proveedor_id " ); 
          SQL.append ("   and a.adquisicion_id=b.adquisicion_id " );
          SQL.append ("   and b.proveedor_id=c.proveedor_id " );
          SQL.append ("   and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append ("   and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append ("   and c.control_inventario_id=e.control_inventario_id" );
          SQL.append ("   and substr(trim(to_char(e.partida_presup_actual)),1,3)=trim(to_char(g.partida_presupuestal)) ");          
          SQL.append ("   and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_actual,1,3),4,0)," ); 
          SQL.append ("       e.a�o_afecta_actual, lpad(substr(e.fecha_actual_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
          SQL.append (" union all ");
          SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_anterior,1,3),4,0) conc," ); 
          SQL.append ("    e.a�o_afecta_anterior ejer, lpad(substr(e.fecha_anterior_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'511',e.valor_bien_actual*-1,'512',e.valor_bien_actual*-1,'515',e.valor_bien_actual*-1,'519',e.valor_bien_actual*-1,0)) imp_mobiliario_1241," ); 
    	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'521',e.valor_bien_actual*-1,'522',e.valor_bien_actual*-1,'523',e.valor_bien_actual*-1,'529',e.valor_bien_actual*-1,0)) imp_mobiliario_1242," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'531',e.valor_bien_actual*-1,'532',e.valor_bien_actual*-1,0)) imp_equipo_1243," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'541',e.valor_bien_actual*-1,'542',e.valor_bien_actual*-1,'543',e.valor_bien_actual*-1,'545',e.valor_bien_actual*-1,'549',e.valor_bien_actual*-1,0)) imp_equipo_1244," );   
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'561',e.valor_bien_actual*-1,'562',e.valor_bien_actual*-1,'563',e.valor_bien_actual*-1,'564',e.valor_bien_actual*-1,'565',e.valor_bien_actual*-1,'566',e.valor_bien_actual*-1,'567',e.valor_bien_actual*-1,'569',e.valor_bien_actual*-1,0)) imp_maquinaria_1246," );   
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'513',e.valor_bien_actual*-1,'514',e.valor_bien_actual*-1,0)) imp_colecciones_1247," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'591',e.valor_bien_actual*-1,0)) imp_software_1251," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'599',e.valor_bien_actual*-1,0)) imp_otros_1259 " );
          SQL.append ("  from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_rh_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g " ); 
          SQL.append (" where e.numero= '" + numero + "'");
	  SQL.append ("   and e.partida= '" + partida + "'");
    	  SQL.append ("   and e.inciso= '" + inciso + "'");
          SQL.append ("   and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append ("   and e.tipo_ajuste_id=3 " ); 
          SQL.append ("   and a.proveedor_id=b.proveedor_id " ); 
          SQL.append ("   and a.adquisicion_id=b.adquisicion_id " );
          SQL.append ("   and b.proveedor_id=c.proveedor_id " );
          SQL.append ("   and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append ("   and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append ("   and c.control_inventario_id=e.control_inventario_id" );
          SQL.append ("   and substr(trim(to_char(e.partida_presup_anterior)),1,3)=trim(to_char(g.partida_presupuestal)) ");          
          SQL.append ("   and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_anterior,1,3),4,0)," ); 
          SQL.append ("       e.a�o_afecta_anterior , lpad(substr(e.fecha_anterior_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
          SQL.append (" union all ");
          SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_actual,1,3),4,0) conc," ); 
          SQL.append ("    e.a�o_afecta_actual ejer, lpad(substr(e.fecha_actual_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," );           
          SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'511',e.valor_bien_actual,'512',e.valor_bien_actual,'515',e.valor_bien_actual,'519',e.valor_bien_actual,0)) imp_mobiliario_1241," ); 
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'521',e.valor_bien_actual,'522',e.valor_bien_actual,'523',e.valor_bien_actual,'529',e.valor_bien_actual,0)) imp_mobiliario_1242," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'531',e.valor_bien_actual,'532',e.valor_bien_actual,0)) imp_equipo_1243," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'541',e.valor_bien_actual,'542',e.valor_bien_actual,'543',e.valor_bien_actual,'545',e.valor_bien_actual,'549',e.valor_bien_actual,0)) imp_equipo_1244," );   
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'561',e.valor_bien_actual,'562',e.valor_bien_actual,'563',e.valor_bien_actual,'564',e.valor_bien_actual,'565',e.valor_bien_actual,'566',e.valor_bien_actual,'567',e.valor_bien_actual,'569',e.valor_bien_actual,0)) imp_maquinaria_1246," );   
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'513',e.valor_bien_actual,'514',e.valor_bien_actual,0)) imp_colecciones_1247," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'591',e.valor_bien_actual,0)) imp_software_1251," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'599',e.valor_bien_actual,0)) imp_otros_1259 " );
          SQL.append ("  from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_rh_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g " ); 
          SQL.append (" where e.numero= '" + numero + "'");
          SQL.append ("   and e.partida= '" + partida + "'");
    	  SQL.append ("   and e.inciso= '" + inciso + "'");
          SQL.append ("   and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append ("   and e.tipo_ajuste_id=3 " ); 
          SQL.append ("   and a.proveedor_id=b.proveedor_id " ); 
          SQL.append ("   and a.adquisicion_id=b.adquisicion_id " );
          SQL.append ("   and b.proveedor_id=c.proveedor_id " );
          SQL.append ("   and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append ("   and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append ("   and c.control_inventario_id=e.control_inventario_id" );
          SQL.append ("   and substr(trim(to_char(e.partida_presup_actual)),1,3)=trim(to_char(g.partida_presupuestal)) ");          
          SQL.append ("   and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_actual,1,3),4,0)," ); 
          SQL.append ("       e.a�o_afecta_actual, lpad(substr(e.fecha_actual_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );

        }
      } else {

          SQL.append ("select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_anterior,1,3),4,0) conc," ); 
          SQL.append (" e.a�o_afecta_anterior ejer, lpad(substr(e.fecha_anterior_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'511',e.valor_bien_actual*-1,'512',e.valor_bien_actual*-1,'515',e.valor_bien_actual*-1,'519',e.valor_bien_actual*-1,0)) imp_mobiliario_1241," ); 
    	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'521',e.valor_bien_actual*-1,'522',e.valor_bien_actual*-1,'523',e.valor_bien_actual*-1,'529',e.valor_bien_actual*-1,0)) imp_mobiliario_1242," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'531',e.valor_bien_actual*-1,'532',e.valor_bien_actual*-1,0)) imp_equipo_1243," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'541',e.valor_bien_actual*-1,'542',e.valor_bien_actual*-1,'543',e.valor_bien_actual*-1,'545',e.valor_bien_actual*-1,'549',e.valor_bien_actual*-1,0)) imp_equipo_1244," );   
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'561',e.valor_bien_actual*-1,'562',e.valor_bien_actual*-1,'563',e.valor_bien_actual*-1,'564',e.valor_bien_actual*-1,'565',e.valor_bien_actual*-1,'566',e.valor_bien_actual*-1,'567',e.valor_bien_actual*-1,'569',e.valor_bien_actual*-1,0)) imp_maquinaria_1246," );   
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'513',e.valor_bien_actual*-1,'514',e.valor_bien_actual*-1,0)) imp_colecciones_1247," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'591',e.valor_bien_actual*-1,0)) imp_software_1251," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'599',e.valor_bien_actual*-1,0)) imp_otros_1259 " );
          SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g " ); 
          SQL.append (" where " ); 
          SQL.append (" e.numero= '" + numero + "'");
	  SQL.append (" and e.partida= '" + partida + "'");
    	  SQL.append (" and e.inciso= '" + inciso + "'");
          SQL.append (" and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append ("   and e.tipo_ajuste_id=2 " ); 
          SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
          SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
          SQL.append (" and b.proveedor_id=c.proveedor_id " );
          SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append (" and c.control_inventario_id=e.control_inventario_id" );
          SQL.append (" and substr(trim(to_char(e.partida_presup_anterior)),1,3)=trim(to_char(g.partida_presupuestal)) ");
          SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_anterior,1,3),4,0)," ); 
          SQL.append (" e.a�o_afecta_anterior , lpad(substr(e.fecha_anterior_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
          SQL.append (" union all ");
          SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_actual,1,3),4,0) conc," ); 
          SQL.append (" e.a�o_afecta_actual ejer, lpad(substr(e.fecha_actual_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'511',e.valor_bien_actual,'512',e.valor_bien_actual,'515',e.valor_bien_actual,'519',e.valor_bien_actual,0)) imp_mobiliario_1241," ); 
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'521',e.valor_bien_actual,'522',e.valor_bien_actual,'523',e.valor_bien_actual,'529',e.valor_bien_actual,0)) imp_mobiliario_1242," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'531',e.valor_bien_actual,'532',e.valor_bien_actual,0)) imp_equipo_1243," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'541',e.valor_bien_actual,'542',e.valor_bien_actual,'543',e.valor_bien_actual,'545',e.valor_bien_actual,'549',e.valor_bien_actual,0)) imp_equipo_1244," );   
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'561',e.valor_bien_actual,'562',e.valor_bien_actual,'563',e.valor_bien_actual,'564',e.valor_bien_actual,'565',e.valor_bien_actual,'566',e.valor_bien_actual,'567',e.valor_bien_actual,'569',e.valor_bien_actual,0)) imp_maquinaria_1246," );   
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'513',e.valor_bien_actual,'514',e.valor_bien_actual,0)) imp_colecciones_1247," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'591',e.valor_bien_actual,0)) imp_software_1251," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'599',e.valor_bien_actual,0)) imp_otros_1259 " );
          SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_tr_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g " ); 
          SQL.append (" where " ); 
          SQL.append (" e.numero= '" + numero + "'");
          SQL.append (" and e.partida= '" + partida + "'");
          SQL.append (" and e.inciso= '" + inciso + "'");
          SQL.append (" and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append ("   and e.tipo_ajuste_id=2 " ); 
          SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
          SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
          SQL.append (" and b.proveedor_id=c.proveedor_id " );
          SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append (" and c.control_inventario_id=e.control_inventario_id" );
          SQL.append (" and substr(trim(to_char(e.partida_presup_actual)),1,3)=trim(to_char(g.partida_presupuestal)) ");
          SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_actual,1,3),4,0)," ); 
          SQL.append (" e.a�o_afecta_actual , lpad(substr(e.fecha_actual_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
          SQL.append (" union all ");
          SQL.append ("select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_anterior,1,3),4,0) conc," ); 
          SQL.append (" e.a�o_afecta_anterior ejer, lpad(substr(e.fecha_anterior_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'511',e.valor_bien_actual*-1,'512',e.valor_bien_actual*-1,'515',e.valor_bien_actual*-1,'519',e.valor_bien_actual*-1,0)) imp_mobiliario_1241," ); 
    	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'521',e.valor_bien_actual*-1,'522',e.valor_bien_actual*-1,'523',e.valor_bien_actual*-1,'529',e.valor_bien_actual*-1,0)) imp_mobiliario_1242," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'531',e.valor_bien_actual*-1,'532',e.valor_bien_actual*-1,0)) imp_equipo_1243," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'541',e.valor_bien_actual*-1,'542',e.valor_bien_actual*-1,'543',e.valor_bien_actual*-1,'545',e.valor_bien_actual*-1,'549',e.valor_bien_actual*-1,0)) imp_equipo_1244," );   
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'561',e.valor_bien_actual*-1,'562',e.valor_bien_actual*-1,'563',e.valor_bien_actual*-1,'564',e.valor_bien_actual*-1,'565',e.valor_bien_actual*-1,'566',e.valor_bien_actual*-1,'567',e.valor_bien_actual*-1,'569',e.valor_bien_actual*-1,0)) imp_maquinaria_1246," );   
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'513',e.valor_bien_actual*-1,'514',e.valor_bien_actual*-1,0)) imp_colecciones_1247," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'591',e.valor_bien_actual*-1,0)) imp_software_1251," );
	  SQL.append (" sum(decode(substr(e.partida_presup_anterior,1,3),'599',e.valor_bien_actual*-1,0)) imp_otros_1259 " );
          SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_rh_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g " ); 
          SQL.append (" where " ); 
          SQL.append (" e.numero= '" + numero + "'");
	  SQL.append (" and e.partida= '" + partida + "'");
    	  SQL.append (" and e.inciso= '" + inciso + "'");
          SQL.append (" and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append ("   and e.tipo_ajuste_id=2 " ); 
          SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
          SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
          SQL.append (" and b.proveedor_id=c.proveedor_id " );
          SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append (" and c.control_inventario_id=e.control_inventario_id" );
          SQL.append (" and substr(trim(to_char(e.partida_presup_anterior)),1,3)=trim(to_char(g.partida_presupuestal)) ");
          SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_anterior,1,3),4,0)," ); 
          SQL.append (" e.a�o_afecta_anterior , lpad(substr(e.fecha_anterior_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );
          SQL.append (" union all ");
          SQL.append (" select lpad(e.unidad_ejecutora,4,0) unid,lpad(e.coord_estatal||'"+ambito+"',4,0) ambi,rpad(substr(e.partida_presup_actual,1,3),4,0) conc," ); 
          SQL.append (" e.a�o_afecta_actual ejer, lpad(substr(e.fecha_actual_factura,4,2),4,0) mes,lpad(a.documento_adquisicion_id,4,0) tipo, g.descripcion des_partida," ); 
          SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'511',e.valor_bien_actual,'512',e.valor_bien_actual,'515',e.valor_bien_actual,'519',e.valor_bien_actual,0)) imp_mobiliario_1241," ); 
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'521',e.valor_bien_actual,'522',e.valor_bien_actual,'523',e.valor_bien_actual,'529',e.valor_bien_actual,0)) imp_mobiliario_1242," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'531',e.valor_bien_actual,'532',e.valor_bien_actual,0)) imp_equipo_1243," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'541',e.valor_bien_actual,'542',e.valor_bien_actual,'543',e.valor_bien_actual,'545',e.valor_bien_actual,'549',e.valor_bien_actual,0)) imp_equipo_1244," );   
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'561',e.valor_bien_actual,'562',e.valor_bien_actual,'563',e.valor_bien_actual,'564',e.valor_bien_actual,'565',e.valor_bien_actual,'566',e.valor_bien_actual,'567',e.valor_bien_actual,'569',e.valor_bien_actual,0)) imp_maquinaria_1246," );   
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'513',e.valor_bien_actual,'514',e.valor_bien_actual,0)) imp_colecciones_1247," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'591',e.valor_bien_actual,0)) imp_software_1251," );
    	  SQL.append (" sum(decode(substr(e.partida_presup_actual,1,3),'599',e.valor_bien_actual,0)) imp_otros_1259 " );
          SQL.append (" from rm_tr_adquisicion a, rm_tr_detalle_adquisicion b,rm_rh_control_inventario c, rm_tr_valor_ajustes e, rh_tc_partidas_presupuestales g " ); 
          SQL.append (" where " ); 
          SQL.append (" e.numero= '" + numero + "'");
          SQL.append (" and e.partida= '" + partida + "'");
          SQL.append (" and e.inciso= '" + inciso + "'");
          SQL.append (" and to_date(to_char(e.fecha_afectacion_contable,'dd/mm/yyyy'),'dd/mm/yyyy') = to_date('" +fecha + "','dd/mm/yyyy') ");
          SQL.append ("   and e.tipo_ajuste_id=2 " ); 
          SQL.append (" and a.proveedor_id=b.proveedor_id " ); 
          SQL.append (" and a.adquisicion_id=b.adquisicion_id " );
          SQL.append (" and b.proveedor_id=c.proveedor_id " );
          SQL.append (" and b.adquisicion_id=c.adquisicion_id " ); 
          SQL.append (" and b.detalle_adquisicion_id=c.detalle_adquisicion_id" ); 
          SQL.append (" and c.control_inventario_id=e.control_inventario_id" );
          SQL.append (" and substr(trim(to_char(e.partida_presup_actual)),1,3)=trim(to_char(g.partida_presupuestal)) ");
          SQL.append (" and e.unidad_ejecutora='"+unidad+"' and e.coord_estatal="+entidad);
          SQL.append (" group by lpad(e.unidad_ejecutora,4,0),lpad(e.coord_estatal||'"+ambito+"',4,0),rpad(substr(e.partida_presup_actual,1,3),4,0)," ); 
          SQL.append (" e.a�o_afecta_actual , lpad(substr(e.fecha_actual_factura,4,2),4,0),lpad(a.documento_adquisicion_id,4,0),g.descripcion " );

     }
   //  System.out.println("SQL Forma27:" +SQL.toString());
     try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(con);
      crs.beforeFirst(); 
      while(crs.next()){
        partidaGen="";      
        liMes=Integer.parseInt(crs.getString("mes"))-1;
          if (crs.getString("conc").substring(0,3).equals("511")||crs.getString("conc").substring(0,3).equals("512")||crs.getString("conc").substring(0,3).equals("515")||crs.getString("conc").substring(0,3).equals("519")){
            if (crs.getString("conc").substring(0,3).equals("511"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("512"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("515"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
          
          if (crs.getString("conc").substring(0,3).equals("521")||crs.getString("conc").substring(0,3).equals("522")||crs.getString("conc").substring(0,3).equals("523")||crs.getString("conc").substring(0,3).equals("529")){
            if (crs.getString("conc").substring(0,3).equals("521"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("522"))
              partidaGen="2";              
            else if (crs.getString("conc").substring(0,3).equals("523"))
              partidaGen="3";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1242",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
          
          if (crs.getString("conc").substring(0,3).equals("531")||crs.getString("conc").substring(0,3).equals("532")){
            if (crs.getString("conc").substring(0,3).equals("531"))
             partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1243",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }          

          if (crs.getString("conc").substring(0,3).equals("541")||crs.getString("conc").substring(0,3).equals("542")||crs.getString("conc").substring(0,3).equals("543")||crs.getString("conc").substring(0,3).equals("545")||crs.getString("conc").substring(0,3).equals("549")){
            if (crs.getString("conc").substring(0,3).equals("541"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("542"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("543"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("545"))
              partidaGen="5";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1244",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  

          if (crs.getString("conc").substring(0,3).equals("561")||crs.getString("conc").substring(0,3).equals("562")||crs.getString("conc").substring(0,3).equals("563")||crs.getString("conc").substring(0,3).equals("564")||crs.getString("conc").substring(0,3).equals("565")||crs.getString("conc").substring(0,3).equals("566")||crs.getString("conc").substring(0,3).equals("567")||crs.getString("conc").substring(0,3).equals("569")){
            if (crs.getString("conc").substring(0,3).equals("561"))
              partidaGen="1";
            else if (crs.getString("conc").substring(0,3).equals("562"))
              partidaGen="2";
            else if (crs.getString("conc").substring(0,3).equals("563"))
              partidaGen="3";
            else if (crs.getString("conc").substring(0,3).equals("564"))
              partidaGen="4";
            else if (crs.getString("conc").substring(0,3).equals("565"))
              partidaGen="5"; 
            else if (crs.getString("conc").substring(0,3).equals("566"))
              partidaGen="6";
            else if (crs.getString("conc").substring(0,3).equals("567"))
              partidaGen="7";
            else    
              partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1246",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }  
          
          if (crs.getString("conc").substring(0,3).equals("513")||crs.getString("conc").substring(0,3).equals("514")){
            if (crs.getString("conc").substring(0,3).equals("513"))
             partidaGen="1";
            else    
              partidaGen="2";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1247",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }           
          
          if (crs.getString("conc").substring(0,3).equals("591")){
             partidaGen="1";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1251",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }             
          
          if (crs.getString("conc").substring(0,3).equals("599")){
             partidaGen="9";
            //resultado=registro.AgregaCuentaContable("1241",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),"0000","0000","0000","0000",fecha,crs.getString("des_partida"),"5",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),"0000","0000","0000",fecha,"Ejercicio "+crs.getString("ejer"),"6",idCatalogo);
            resultado=registro.AgregaCuentaContable("1259",partidaGen,programa1,crs.getString("unid"),crs.getString("ambi"),crs.getString("ejer"),crs.getString("mes"),"0000","0000",fecha,meses[liMes],"7",idCatalogo);
          }         

        hm.put ("PROGRAMA",programa1);//imprimeHM(hm);
        hm.put ("PARTIDA_GEN",partidaGen);//imprimeHM(hm);
        hm.put ("UNIDAD",crs.getString("unid"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("ambi"));//imprimeHM(hm);
        hm.put ("EJERCICIO",crs.getString("ejer"));//imprimeHM(hm);
        hm.put ("MES",crs.getString("mes"));//imprimeHM(hm);
        hm.put ("CONCEPTO",crs.getString("conc"));//imprimeHM(hm);
        hm.put ("TIPO",crs.getString("tipo"));//imprimeHM(hm);
        
        if(crs.getDouble("IMP_MOBILIARIO_1241") != 0.0 && crs.getString("IMP_MOBILIARIO_1241").indexOf("-") == -1)
          hm.put ("IMPORTE_MOBILIARIO_1241_POS",crs.getString("IMP_MOBILIARIO_1241"));//imprimeHM(hm);
        if(crs.getDouble("IMP_MOBILIARIO_1242") != 0.0 && crs.getString("IMP_MOBILIARIO_1242").indexOf("-") == -1)
          hm.put ("IMPORTE_MOBILIARIO_1242_POS",crs.getString("IMP_MOBILIARIO_1242"));//imprimeHM(hm);
        if(crs.getDouble("IMP_EQUIPO_1243") != 0.0 && crs.getString("IMP_EQUIPO_1243").indexOf("-") == -1)
          hm.put ("IMPORTE_EQUIPO_1243_POS",crs.getString("IMP_EQUIPO_1243"));//imprimeHM(hm);
        if(crs.getDouble("IMP_EQUIPO_1244") != 0.0 && crs.getString("IMP_EQUIPO_1244").indexOf("-") == -1)
          hm.put ("IMPORTE_EQUIPO_1244_POS",crs.getString("IMP_EQUIPO_1244"));//imprimeHM(hm);
        if(crs.getDouble("IMP_MAQUINARIA_1246") != 0.0 && crs.getString("IMP_MAQUINARIA_1246").indexOf("-") == -1)
          hm.put ("IMPORTE_MAQUINARIA_1246_POS",crs.getString("IMP_MAQUINARIA_1246"));//imprimeHM(hm);
        if(crs.getDouble("IMP_COLECCIONES_1247") != 0.0 && crs.getString("IMP_COLECCIONES_1247").indexOf("-") == -1)
          hm.put ("IMPORTE_COLECCIONES_1247_POS",crs.getString("IMP_COLECCIONES_1247"));//imprimeHM(hm);
        if(crs.getDouble("IMP_SOFTWARE_1251") != 0.0 && crs.getString("IMP_SOFTWARE_1251").indexOf("-") == -1)
          hm.put ("IMPORTE_SOFTWARE_1251_POS",crs.getString("IMP_SOFTWARE_1251"));//imprimeHM(hm);
        if(crs.getDouble("IMP_OTROS_1259") != 0.0 && crs.getString("IMP_OTROS_1259").indexOf("-") == -1)
          hm.put ("IMPORTE_OTROS_1259_POS",crs.getString("IMP_OTROS_1259"));//imprimeHM(hm);

        if(crs.getDouble("IMP_MOBILIARIO_1241") != 0.0 && crs.getString("IMP_MOBILIARIO_1241").indexOf("-") != -1)
          hm.put ("IMPORTE_MOBILIARIO_1241_NEG",crs.getString("IMP_MOBILIARIO_1241"));//imprimeHM(hm);
        if(crs.getDouble("IMP_MOBILIARIO_1242") != 0.0 && crs.getString("IMP_MOBILIARIO_1242").indexOf("-") != -1)
          hm.put ("IMPORTE_MOBILIARIO_1242_NEG",crs.getString("IMP_MOBILIARIO_1242"));//imprimeHM(hm);
        if(crs.getDouble("IMP_EQUIPO_1243") != 0.0 && crs.getString("IMP_EQUIPO_1243").indexOf("-") != -1)
          hm.put ("IMPORTE_EQUIPO_1243_NEG",crs.getString("IMP_EQUIPO_1243"));//imprimeHM(hm);
        if(crs.getDouble("IMP_EQUIPO_1244") != 0.0 && crs.getString("IMP_EQUIPO_1244").indexOf("-") != -1)
          hm.put ("IMPORTE_EQUIPO_1244_NEG",crs.getString("IMP_EQUIPO_1244"));//imprimeHM(hm);
        if(crs.getDouble("IMP_MAQUINARIA_1246") != 0.0 && crs.getString("IMP_MAQUINARIA_1246").indexOf("-") != -1)
          hm.put ("IMPORTE_MAQUINARIA_1246_NEG",crs.getString("IMP_MAQUINARIA_1246"));//imprimeHM(hm);
        if(crs.getDouble("IMP_COLECCIONES_1247") != 0.0 && crs.getString("IMP_COLECCIONES_1247").indexOf("-") != -1)
          hm.put ("IMPORTE_COLECCIONES_1247_NEG",crs.getString("IMP_COLECCIONES_1247"));//imprimeHM(hm);
        if(crs.getDouble("IMP_SOFTWARE_1251") != 0.0 && crs.getString("IMP_SOFTWARE_1251").indexOf("-") != -1)
          hm.put ("IMPORTE_SOFTWARE_1251_NEG",crs.getString("IMP_SOFTWARE_1251"));//imprimeHM(hm);
        if(crs.getDouble("IMP_OTROS_1259") != 0.0 && crs.getString("IMP_OTROS_1259").indexOf("-") != -1)
          hm.put ("IMPORTE_OTROS_1259_NEG",crs.getString("IMP_OTROS_1259"));//imprimeHM(hm);
       
 /*       if(crs.getDouble("conc_mob") != 0.0 && crs.getString("conc_mob").indexOf("-") == -1)
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
*/
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

    public void setRefGral(String refGral) {
        this.refGral = refGral;
    }

    public String getRefGral() {
        return refGral;
    }

}
