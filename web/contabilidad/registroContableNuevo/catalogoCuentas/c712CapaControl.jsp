<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.sql.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.beans.seguridad.*"%>

<jsp:useBean id="pbCtaCont" class="sia.rf.contabilidad.registroContableNuevo.bcCuentaContable" scope="page"/>
<jsp:useBean id="pbClaCta" class="sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas" scope="page"/>
<jsp:useBean id="pbDetallePoliza" class="sia.rf.contabilidad.registroContableNuevo.bcDetallePoliza" scope="page"/>
<jsp:useBean id="pbCierres" class="sia.rf.contabilidad.registroContableNuevo.bcCierresMensuales" scope="page"/>
<jsp:useBean id="pbEstadoCat" class="sia.rf.contabilidad.registroContableEvento.bcEstadoCatalogo" scope="page"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  </head>
  
  

<BODY OnLoad ="parent.loadSourceFinish('capaControl');parent.regresa();">
<%

//String descripcion= request.getParameter("txtDescripcion");

String catCuentaId=request.getParameter("catCuentaId");
String cuentaId=request.getParameter("id");//CUENTA_CONTABLE_ID
String uniEje=request.getParameter("uniE");//UNIDAD EJECUTORA
String entidad=request.getParameter("ent");//ENTIDAD
String ambito=request.getParameter("amb");//AMBITO
String ejercicio=request.getParameter("ejercicio");//EJERCICIO
String lsFecIni = request.getParameter("fec1");//FECHA DE INICIO
String lsFecFin = request.getParameter("fec2");//FECHA DE TERMINO
String lsFecReg = request.getParameter("fecReg");//FECHA ACTUAL
String lsDescr = request.getParameter("ldesc");//DESCRIPCION DE LA  CUENTA
String lsCtaConta = request.getParameter("ctaConta");//CUENTA CONTABLE

//lsDescr= new String(request.getParameter("ldesc").getBytes("ISO-8859-1"),"UTF-8");

String lsOpcion = request.getParameter("opc");//OPCION -AGREGAR - MODIFICAR -ELIMINAR
String lsNivel = request.getParameter("nivelCta");
String lsNivelesConf = request.getParameter("nivelesConf");
String lsIdCtaMay = request.getParameter("idCtMay");
String lsCtaVal = request.getParameter("ctaVal");
String lsError="";
String lsMsg="";
boolean bandera=true;
String arrayNiveles[] = lsNivelesConf.split(",");
String cadenaCeros = "";
Connection con = DaoFactory.getContabilidad();

switch (Integer.valueOf(lsNivel)){
    case 1: cadenaCeros="00000000000000000000000000000";
            break;
    case 2: cadenaCeros="0000000000000000000000000000";
            break;            
    case 3: cadenaCeros="000000000000000000000000";
            break;      
    case 4: cadenaCeros="00000000000000000000";
            break;                
    case 5: cadenaCeros="0000000000000000";    
            break;                         
    case 6: cadenaCeros="000000000000";
            break;                                          
    case 7: cadenaCeros="00000000";
            break;               
    case 8: cadenaCeros="0000";
            break;               
    case 9: cadenaCeros="";
            break;              
}


/*
for(int x=Integer.valueOf(lsNivel)-1; x < arrayNiveles.length-1; x++){
  for(int z=0; z< Integer.valueOf(arrayNiveles[x]); z++){
    cadenaCeros = cadenaCeros+"0";
  }
}*/


if(!lsOpcion.equals("E"))
  lsCtaConta = lsCtaConta + cadenaCeros;
//se obtiene el programa
lsDescr=lsDescr.replace('{','%');
lsDescr=lsDescr.replace('|','&');
lsDescr=lsDescr.replace('}','#');
lsDescr=lsDescr.replace('[','+');

//se excluye la cuenta  de mayor para esta validacion
if(lsCtaConta.length() > 5 && Integer.parseInt(lsNivel)>2){
    String y =lsCtaConta.substring(5,9);
    String lsMes=lsFecIni.substring(3,5);
    //se utiliza para verificar si el mes esta abierto
    pbCierres.select_cierre_mensual(con,uniEje,ambito,entidad,ejercicio,catCuentaId,lsMes,y,"0");
    String lsMesAbierto=pbCierres.getMes();
    pbCierres.setMes("");
    //se utiliza para verificar si el mes esta cerrado
    pbCierres.select_cierre_mensual(con,uniEje,ambito,entidad,ejercicio,catCuentaId,lsMes,y,"2");
    lsMes=pbCierres.getMes();
    //para poder realizar los movimientos se debe verificar que se haya abierto el mes y que no este cerrado
    if (lsMesAbierto==null){ //No hay mes abierto
          bandera=true;
    }else{
       if(!lsMes.equals("")){//si entra entonces hay cierre del mes 
          bandera=true;
       }
     }

}

    String estatus="";
    String desCatalogo="";
    pbEstadoCat.select_rf_tc_estado_catalogo(con);
    estatus=pbEstadoCat.getEstatus();
    desCatalogo=pbEstadoCat.getDescripcion();
    if (!estatus.equals("1")){
        //throw new Exception(" En estos momentos no es posible crear, modificar o cancelar polizas, ya que hay un proceso de "+ descri+" ejecutandose. Favor de intentarlo mas tarde.");
        lsMsg="AVISO: En estos momentos no es posible crear, modificar o eliminar cuentas contables, ya que hay un proceso de "+ desCatalogo+" ejecutandose. Favor de intentarlo mas tarde.";
       bandera=true;
    }
//   System.out.println("lsMsg" +lsMsg);
ResultSet crs=null;
try{
con.setAutoCommit(false);
   
    //lsDescr= new String(request.getParameter("ldesc").getBytes("ISO-8859-1"),"UTF-8");
    
    //verificar que el ejercicio no esté cerrado para entonces poder agregar informacion
    if(lsOpcion.equals("A")){//AGREGAR
        if(bandera == true){ //el mes se encuentra abierto por tanto si se puede registrar la cuenta          
          if (lsCtaConta.length()==33 && !(lsCtaVal.equals(""))){             
            //verificar que la cuenta del nivel dos en adelante no se encuentre duplicada
            String existeCuenta=pbCtaCont.selectCuentaContable(con,lsCtaConta,catCuentaId,lsFecIni.substring(6,10));
            //verificar que la cuenta padre no tenga movimientos
            int mov=pbDetallePoliza.selectTotalPol(con,cuentaId);
            if (existeCuenta.equals("") && mov==0){ 
                //verificar que los niveles anteriores si existan
                int xx=0;
                String existe2="0";
                if (!lsNivel.equals("2")){
                  xx=lsCtaVal.length()-4;
                  existe2=pbCtaCont.selectCuentaContables(con,lsCtaConta.substring(0,xx),catCuentaId,lsFecIni.substring(6,10));
                }
                if (!existe2.equals("")){
                       pbCtaCont.setFecha_vig_ini(lsFecIni);
                       pbCtaCont.setFecha_vig_fin(lsFecFin);
                       pbCtaCont.setRegistro(lsFecReg);
                       pbCtaCont.setId_catalogo_cuenta(catCuentaId);
                       pbCtaCont.setCuenta_mayor_id(lsIdCtaMay);
                       pbCtaCont.setCuenta_contable(lsCtaConta);
                       pbCtaCont.setDescripcion(lsDescr);
                       pbCtaCont.setNivel(lsNivel);
                       pbCtaCont.setCodigo_registro(String.valueOf(Autentifica.getNumeroEmpleado()));
                       //pbCtaCont.insert_rf_tr_cuentas_contables_general(con);
                       pbCtaCont.insert_cuentas_contables2(con);       
                }else{//por lo pronto se deja D, hay que agregar el error que seria que no existe la cuenta anterior por eso no se puede dar de alta
                    lsError="CA";
                }
              
                String existe="";
                if(lsNivel.equals("2")){
                    //verifica que ya exista en la tabla rf_tr_cuentas_contables la cuenta de mayor
                    String tempo="";
                    tempo=lsCtaConta.substring(0,4)+"00000000000000000000000000000";                    
                      //buscar el registro para ver si existe utlizar tambien la fecha
                      existe=pbCtaCont.selectCuentaContable(con,tempo,catCuentaId,lsFecIni.substring(6,10));
                      if(existe.equals("")){//si no existe la cuenta de mayor
                          //agregarla
                          pbClaCta.select_RF_TC_CLASIFICADOR_CUENTAS(con,lsCtaConta.substring(0,4));
                          pbCtaCont.setDescripcion(pbClaCta.getDescripcion());
                          pbCtaCont.setCuenta_contable(tempo);
                          pbCtaCont.setNivel("1");
                          //pbCtaCont.insert_rf_tr_cuentas_contables_general(con);
                          pbCtaCont.insert_cuentas_contables2(con);
                          lsError="";
                      }
                }//if de nivel dos
             }else{//mensaje de que la cuenta se encuentra duplicada
                lsError="D";
            }    
         } else {
            lsError="FA";
         }            
        }else{//de bandera
            if(!lsMsg.equals("")){//existe un cierre en este momento
                lsError="EC";
            }else{
                //mensaje de que esta cerrado el mes por tanto no se puede registrar la cuenta
                lsError="C"; 
            }
        }
    }
    
    if (lsOpcion.equals("M")){
        if(bandera==true){
            pbCtaCont.setFecha_vig_ini(lsFecIni);
            pbCtaCont.setFecha_vig_fin(lsFecFin);
            pbCtaCont.setRegistro(lsFecReg);
            pbCtaCont.setDescripcion(lsDescr);
            pbCtaCont.setCodigo_registro(String.valueOf(Autentifica.getNumeroEmpleado()));
            pbCtaCont.update_cuentas_contables(con,cuentaId,catCuentaId);
            lsError="";
        }else{
            if(!lsMsg.equals("")){//existe un cierre en este momento
                lsError="EC";
            }else{
                //mensaje de que esta cerrado el mes por tanto no se puede registrar la cuenta
                lsError="C"; 
            }
        }
    }
    
    
    if(lsOpcion.equals("E")){
        //bandera=false;
        if(bandera==true){
            //verificar que no tenga información relacionada
            int total=pbDetallePoliza.selectTotalPol(con,cuentaId);
            if (total == 0){//no tiene informacion, se puede eliminar la cuenta contable
                String xx="";
                if( (Integer.parseInt(lsNivel)-1) ==1 ||  (Integer.parseInt(lsNivel)-1) ==2 ||  (Integer.parseInt(lsNivel)-1) ==3){//si es nivel uno o dos
                    xx=pbCtaCont.selectCuentaContables(con,lsCtaConta,catCuentaId,ejercicio);
                    if (Integer.parseInt(xx) ==1){//si solamente existe un registro entonces si se elimina
                        pbCtaCont.delete_rf_tr_cuentas_contables(con,cuentaId); 
                    }else{
                        lsError="I";
                    }
               }else{//SE VERIFICA QUE NO EXISTAN SUBCUENTAS DE LA CUENTA QUE SE AGREGARA
                    xx="";
                    xx=pbCtaCont.selectCuentaContables(con,lsCtaConta,catCuentaId,ejercicio);  
                    if(Integer.parseInt(xx)== 1){
                        pbCtaCont.delete_rf_tr_cuentas_contables(con,cuentaId);        
                    }else{
                        lsError="I";
                    }
                }
            }else{
                lsError="P";
            }
        }else{
            if(!lsMsg.equals("")){//existe un cierre en este momento
                lsError="EC";
            }else{
                //mensaje de que esta cerrado el mes por tanto no se puede registrar la cuenta
                lsError="C"; 
            }
        }   
    }
    con.commit();  
%>

<script type="text/javascript" language="javascript">
    parent.Fin('<%=lsOpcion%>',"<%=lsError%>");
</script>

<%

}//try
     catch(Exception e){
       con.rollback(); 
       System.out.println("Ocurrio un error al accesar la capa c712CapaCuentas.jsp "+e.getMessage());
%>
<p>Ha ocurrido un error al accesar la Base de Datos,</p>

<p>favor de reportarlo al Administrador del Sistema.</p>

<p>Gracias.</p>
<%
     } //Fin catch
     finally{
       if (crs!=null){
         crs.close();
         crs=null;
       }
       if (con!=null){
         con.close();
         con=null;
       }
     } //Fin finally
%>

</BODY>

</html>