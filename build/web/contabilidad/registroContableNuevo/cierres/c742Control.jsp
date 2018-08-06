<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.beans.seguridad.*,sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<html>
<head>
<meta http-equiv="Content-Language" content="es-mx">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Abrir Cierre Definitivo</title>
<link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type='text/css'>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/dynapi/src/dynapi.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/almacen.js" type="text/javascript"></script>
</head>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="sbAutentifica" class = "sia.beans.seguridad.Autentifica" scope="session" />
<jsp:useBean id="pbUnidadesEjec" class = "sia.rf.contabilidad.registroContableNuevo.bcUnidadesEjec" scope="page"/>
<jsp:useBean id="pbCierresMensuales" class = "sia.rf.contabilidad.registroContableNuevo.bcCierresMensuales" scope="page"/>
<jsp:useBean id="pbPoliza" class = "sia.rf.contabilidad.registroContableNuevo.bcPoliza" scope="page"/>
<jsp:useBean id="pbCuentaContable" class = "sia.rf.contabilidad.registroContableNuevo.bcCuentaContable" scope="page"/>
<jsp:useBean id="pbProcesos" class = "sia.rf.contabilidad.registroContableNuevo.bcProcesosCierre" scope="page"/>
<body topmargin=1 leftmargin=10>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Cierre Definitivo", "Resultado", true);</jsp:scriptlet>
<br><br>

<br>
<FORM Method="post">


<%!//Función que graba manda valores a las propiedades del bean bcCierresMensuales
 public void grabaCierresMensuales(sia.rf.contabilidad.registroContableNuevo.bcCierresMensuales pbCierresMensuales,String pUnidad,String pAmbito,String pPais,String pEntidad,String pMes,String pEstatus,String pEjer,String pIdCat,String pPrograma,String pNumEmp){   
          pbCierresMensuales.setUnidad_ejecutora(pUnidad);
          pbCierresMensuales.setAmbito(pAmbito);
          pbCierresMensuales.setPais(pPais);
          pbCierresMensuales.setEntidad(pEntidad);
          pbCierresMensuales.setMes(pMes);
          pbCierresMensuales.setEstatus_cierre_id(pEstatus);
          pbCierresMensuales.setEjercicio(pEjer);
          pbCierresMensuales.setId_catalogo_cuenta(pIdCat);               
          pbCierresMensuales.setPrograma(pPrograma);
          pbCierresMensuales.setNum_empleado(pNumEmp);
          
          
 }%>


<%

 sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
 
 Connection conexion=null;
 sbAutentifica.setPagina("c742Control");
 
  
try{
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);
    ControlRegistro controlReg = (ControlRegistro)session.getAttribute("controlRegistro");    
   
    String lsUnidad_ejecutora=request.getParameter("lstProceso");   
    Integer Ambito=Integer.parseInt(request.getParameter("lstSubProceso")); 
    String lsAmbito=lsUnidad_ejecutora.equals("0")?Ambito.toString():Ambito.toString().substring(Ambito.toString().length()-1);   
    Integer Entidad=Integer.parseInt(request.getParameter("lstActividad")); 
    String lsEntidad=lsUnidad_ejecutora.equals("0")?Entidad.toString(): (Entidad.toString().length()==3?Entidad.toString().substring(Entidad.toString().length()-2,Entidad.toString().length()):Entidad.toString().substring(Entidad.toString().length()-1));           
    String lsEjercicio=request.getParameter("lstCierre");      
    String lsId_catalogo_cuenta=request.getParameter("idCatalogoCuenta");   
    String lsPrograma=request.getParameter("programa");   
    String lsMes=request.getParameter("lstMes");       
    String lsMesSiguiente="";
    String lsEjerSiguiente=lsEjercicio; 
    String desMeses [] ={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    String lsMensajeCierre="La Apertura del Cierre Definitivo se ha aplicado correctamente, para las siguientes unidades: <br> ";
    String numEmp=Integer.toString(sbAutentifica.getNumeroEmpleado());
    int contadorCierres=0;
    
    Vector vecUnidad;
    Vector vecEntidad;
    Vector vecAmbito;
    Vector vecPrograma;   
    Boolean continua=true;     
    if (lsMes.equals("12")){//Si es Dic. poner el mes sig. Enero y un ejercicio siguiente
        lsMesSiguiente="1";
        lsEjerSiguiente=String.valueOf(Integer.parseInt(lsEjerSiguiente)+1);       
    }else{
         lsMesSiguiente=String.valueOf(Integer.parseInt(lsMes)+1);            
    }
    
    
 /* ***** CIERRE AL NIVEL MAS BAJO PARA ESTATALES  ***** */ 
 if  (lsAmbito.equals("1")||lsAmbito.equals("3")){
    //Verificar si existe un cierre definitivo del mes siguiente para todos los programas unidades y ámbitos, dentro de la tabla procesos.          
    pbProcesos.select_rf_tr_procesos_cierre(conexion,lsEjerSiguiente,lsMesSiguiente);
    if (pbProcesos.getCierre_definitivo().equals("1")){
        continua=false; 
%>
       <p>No es posible abrir el cierre debido a que el mes siguiente:  <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMesSiguiente)-1]%></font></b>
        del Ejercicio:<b><font color='#003399'><%=lsEjerSiguiente%></font></b> ya tiene un <strong><I>Cierre Definitivo</I></strong> 
        Para todas las unidad, entidades, ámbitos y programas.
       </p>
       <br><br><br>  
<% 
    }else{ // no hay cierre del mes siguiente             
        //Verificar si existe un cierre definitivo para el mes actual    
       pbCierresMensuales.select_cierre_mensual(conexion, lsUnidad_ejecutora,lsAmbito,lsEntidad,lsEjercicio,lsId_catalogo_cuenta,lsMes,lsPrograma,"2");
       if (pbCierresMensuales.getEstatus_cierre_id().equals("2")){
          //Cambia el cierre Definitivo a Cierre Preliminar
           grabaCierresMensuales(pbCierresMensuales,lsUnidad_ejecutora,lsAmbito,"147",lsEntidad,lsMes,"2",lsEjercicio,lsId_catalogo_cuenta,lsPrograma,numEmp);
           pbCierresMensuales.update_cierre_mensual_estatus(conexion,"1");
           //Elimina si hay unidades bloqueadas
           pbCierresMensuales.delete_unidades_bloqueadas(conexion);
           //cambia el estatus en la tabla de procesos
           pbProcesos.update_abrir_procesos_cierre(conexion,lsEjercicio,lsMes);

           lsMensajeCierre=lsMensajeCierre+" &nbsp; &nbsp; &nbsp; &nbsp; Unidad: <b>"+lsUnidad_ejecutora+"</b>, Entidad: <b>"+lsEntidad+"</b>, Ambito: <b>"+lsAmbito+"</b>. <br>";    
           lsMensajeCierre=lsMensajeCierre+" Mes: <b>"+desMeses[Integer.parseInt(lsMes)-1]+"</b>, Ejercicio: <b>"+lsEjercicio+"</b> y Programa: <b>"+lsPrograma+".</b> <br>";    

           if (lsMes.equals("12")){//Si es Dic. eliminar polizas de traspaso
               pbPoliza.delete_rf_tr_polizas_detalle_traspaso(conexion,lsEjercicio,lsMes,lsId_catalogo_cuenta);
               pbPoliza.delete_rf_tr_polizas_traspaso(conexion,lsEjercicio,lsMes,lsId_catalogo_cuenta);
               pbCuentaContable.update_rf_tr_cuentas_contables_limpia_traspaso(conexion,lsEjercicio,lsId_catalogo_cuenta);               
               lsMensajeCierre=lsMensajeCierre+"<b> Nota Importante: </b> Se han eliminado las pólizas de traspaso para el cierre de unidades y central de cuenta pública. ";
           }           
        }
        else{ %>
           <p>No existe <strong><I>Cierre Definitivo</I></strong> para la Unidad Ejecutora:
           <b><font color='#003399'><%=lsUnidad_ejecutora%></font></b>, Entidad: <b><font color='#003399'><%=lsEntidad%></font></b>, Ambito: <b><font color='#003399'><%=lsAmbito%></font></b>,
            Mes: <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMes)-1]%></font></b>, Ejercicio:<b><font color='#003399'><%=lsEjercicio%></font></b> y Programa: <b><font color='#003399'><%=lsPrograma%></font></b>, 
            por lo tanto no es posible abrir el mes.
           </p>
           <br><br><br>  
<%       
          continua=false;
         }//fin abrir cierre para el mes actual     
    }//fin cierre del mes siguiente
 }//fin de if ambito = 3



/* ***** CIERRE AL NIVEL INTERMEDIO "REGIONAL" INCLUYENDO ESTATALES   ***** */   

 if  (lsAmbito.equals("2")){
      vecEntidad = new Vector();
      vecAmbito = new Vector();
      pbUnidadesEjec.select_UnidadesEjecporUnidad(conexion,lsUnidad_ejecutora,lsEjercicio,lsId_catalogo_cuenta,lsPrograma);
      //Revisar cada una de las entidades y ambitos pertenecientes a la unidad de la Regional
      vecEntidad = pbUnidadesEjec.getVEntidad();
      vecAmbito = pbUnidadesEjec.getVAmbito();
      int   numElem=vecEntidad.size();
      //Verificar si existe un cierre definitivo del mes siguiente para todos los programas unidades y ámbitos, dentro de la tabla procesos.                
      pbProcesos.select_rf_tr_procesos_cierre(conexion,lsEjerSiguiente,lsMesSiguiente);
      if (pbProcesos.getCierre_definitivo().equals("1")){
          continua=false; 
%>
         <p>No es posible abrir el cierre debido a que el mes siguiente:  <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMesSiguiente)-1]%></font></b>
         del Ejercicio:<b><font color='#003399'><%=lsEjerSiguiente%></font></b> ya tiene un <strong><I>Cierre Definitivo</I></strong> 
         Para todas las unidad, entidades, ámbitos y programas.
         </p>
         <br><br><br>  
<% 
      }else{ // no hay cierre del mes siguiente             
         //Verificar si todas las unidades y la regional ya hicieron cierre para el mes actual
         for (int j=0; j<numElem; j++){    
            pbCierresMensuales.select_cierre_mensual(conexion, lsUnidad_ejecutora, vecAmbito.elementAt(j).toString(),vecEntidad.elementAt(j).toString(),lsEjercicio,lsId_catalogo_cuenta,lsMes,lsPrograma,"2");
            if (pbCierresMensuales.getEstatus_cierre_id().equals("2")){
               contadorCierres++;
            }//fin if cierre definitivo
         }// fin for verifica cierre anteriores   
         //Si toda la unidad ya cerró
         if (contadorCierres>0){ 
           for (int j=0; j<numElem; j++){   
             //Cambia el cierre Definitivo a Cierre Preliminar
             grabaCierresMensuales(pbCierresMensuales,lsUnidad_ejecutora,vecAmbito.elementAt(j).toString(),"147",vecEntidad.elementAt(j).toString(),lsMes,"2",lsEjercicio,lsId_catalogo_cuenta,lsPrograma,numEmp);
             pbCierresMensuales.update_cierre_mensual_estatus(conexion,"1");
             //Elimina si hay unidades bloqueadas
             pbCierresMensuales.delete_unidades_bloqueadas(conexion);             
             lsMensajeCierre=lsMensajeCierre+" &nbsp; &nbsp; &nbsp; &nbsp; Unidad: <b>"+lsUnidad_ejecutora+"</b>, Entidad: <b>"+vecEntidad.elementAt(j).toString()+"</b>, Ambito: <b>"+vecAmbito.elementAt(j).toString()+"</b>. <br>";                 
           }// fin for abrir mes  regional 
           //cambia el estatus en la tabla de procesos
           pbProcesos.update_abrir_procesos_cierre(conexion,lsEjercicio,lsMes);     
          
          lsMensajeCierre=lsMensajeCierre+" Mes: <b>"+desMeses[Integer.parseInt(lsMes)-1]+"</b>, Ejercicio: <b>"+lsEjercicio+"</b> y Programa: <b>"+lsPrograma+".</b> <br>";    
           if (lsMes.equals("12")){//Si es Dic. eliminar polizas de traspaso
               pbPoliza.delete_rf_tr_polizas_detalle_traspaso(conexion,lsEjercicio,lsMes,lsId_catalogo_cuenta);
               pbPoliza.delete_rf_tr_polizas_traspaso(conexion,lsEjercicio,lsMes,lsId_catalogo_cuenta);
               pbCuentaContable.update_rf_tr_cuentas_contables_limpia_traspaso(conexion,lsEjercicio,lsId_catalogo_cuenta);
               lsMensajeCierre=lsMensajeCierre+"<b> Nota Importante: </b> Se han eliminado las pólizas de traspaso para el cierre de unidades y central de cuenta pública. ";
           }           
         }else{     
           continua=false; 
         %>
           <p>No existe un <strong><I>Cierre Definitivo</I></strong> para toda la Unidad Ejecutora:
           <b><font color='#003399'><%=lsUnidad_ejecutora%></font></b>, Mes: <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMes)-1]%></font></b>, Ejercicio:<b><font color='#003399'><%=lsEjercicio%></font></b> y Programa: <b><font color='#003399'><%=lsPrograma%>.</font></b>, 
             por lo tanto no es posible abrir el mes.
           </p>
           <br><br><br>  
        <%
         } //fin if toda la unidad cerró     
      }//fin cierre del mes siguiente
 } //fin ambito = 2
 
 /* ***** CIERRE AL NIVEL SUPER USUARIO "CENTRAL" INCLUYENDO TODAS LAS UNIDADES, ENTIDADES Y AMBITOS **** */   
 
  if  (lsAmbito.equals("0")&&!lsPrograma.equals("0")){
      vecUnidad = new Vector();   
      vecEntidad = new Vector();
      vecAmbito = new Vector();
      vecPrograma = new Vector();
      
      pbUnidadesEjec.select_UnidadesEjecTodas(conexion,lsEjercicio,lsId_catalogo_cuenta,lsPrograma);
      //Revisar cada una de las unidades, entidades, ambitos, y programas 
      vecUnidad = pbUnidadesEjec.getVUnidad();
      vecEntidad = pbUnidadesEjec.getVEntidad();
      vecAmbito = pbUnidadesEjec.getVAmbito();
      vecPrograma = pbUnidadesEjec.getVPrograma();      
      int   numElem=vecUnidad.size();
      //Verificar si existe un cierre definitivo del mes siguiente para todos los programas unidades y ámbitos, dentro de la tabla procesos.          
      pbProcesos.select_rf_tr_procesos_cierre(conexion,lsEjerSiguiente,lsMesSiguiente);
      if (pbProcesos.getCierre_definitivo().equals("1")){
          continua=false; 
%>
         <p>No es posible abrir el cierre debido a que el mes siguiente:  <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMesSiguiente)-1]%></font></b>
         del Ejercicio:<b><font color='#003399'><%=lsEjerSiguiente%></font></b> ya tiene un <strong><I>Cierre Definitivo</I></strong> 
         Para todas las unidad, entidades, ámbitos y programas.
         </p>
         <br><br><br>  
<% 
      }else{ // no hay cierre del mes siguiente             
        for (int j=0; j<numElem; j++){    
           //Verificar si todas las unidades y la regional ya hicieron cierre
           pbCierresMensuales.select_cierre_mensual(conexion, vecUnidad.elementAt(j).toString(), vecAmbito.elementAt(j).toString(),vecEntidad.elementAt(j).toString(),lsEjercicio,lsId_catalogo_cuenta,lsMes,vecPrograma.elementAt(j).toString(),"2");
           if (pbCierresMensuales.getEstatus_cierre_id().equals("2")){
             contadorCierres++;
           }//fin if cierre definitivo
        }// fin for verifica cierre anteriores 
        //Si ya hay cierre definitivo de al menos una unidad
        if (contadorCierres>0){ 
          for (int j=0; j<numElem; j++){   
             //Cambia el cierre Definitivo a Cierre Preliminar
             grabaCierresMensuales(pbCierresMensuales,vecUnidad.elementAt(j).toString(),vecAmbito.elementAt(j).toString(),"147",vecEntidad.elementAt(j).toString(),lsMes,"2",lsEjercicio,lsId_catalogo_cuenta,vecPrograma.elementAt(j).toString(),numEmp);
             pbCierresMensuales.update_cierre_mensual_estatus(conexion,"1");
             //Elimina si hay unidades bloqueadas
             pbCierresMensuales.delete_unidades_bloqueadas(conexion);             
             lsMensajeCierre=lsMensajeCierre+" &nbsp; &nbsp; &nbsp; &nbsp; Unidad: <b>"+vecUnidad.elementAt(j).toString()+"</b>, Entidad: <b>"+vecEntidad.elementAt(j).toString()+"</b>, Ambito: <b>"+vecAmbito.elementAt(j).toString()+"</b>. <br>";                 
          }// fin for abrir mes  regional 
          //cambia el estatus en la tabla de procesos
          pbProcesos.update_abrir_procesos_cierre(conexion,lsEjercicio,lsMes);     
          lsMensajeCierre=lsMensajeCierre+" Mes: <b>"+desMeses[Integer.parseInt(lsMes)-1]+"</b>, Ejercicio: <b>"+lsEjercicio+"</b> y Programa: <b>"+lsPrograma+".</b> <br>";    
          if (lsMes.equals("12")){//Si es Dic. eliminar polizas de traspaso
               pbPoliza.delete_rf_tr_polizas_detalle_traspaso(conexion,lsEjercicio,lsMes,lsId_catalogo_cuenta);
               pbPoliza.delete_rf_tr_polizas_traspaso(conexion,lsEjercicio,lsMes,lsId_catalogo_cuenta);
               pbCuentaContable.update_rf_tr_cuentas_contables_limpia_traspaso(conexion,lsEjercicio,lsId_catalogo_cuenta);
               lsMensajeCierre=lsMensajeCierre+"<b> Nota Importante: </b> Se han eliminado las pólizas de traspaso para el cierre de unidades y central de cuenta pública. ";
           }        
          }else{     
             continua=false; 
          %>
             <p>No existe un <strong><I>Cierre Definitivo</I></strong> para todas las Unidades Ejecutoras del
             Mes: <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMes)-1]%></font></b>, Ejercicio:<b><font color='#003399'><%=lsEjercicio%></font></b> y Programa: <b><font color='#003399'><%=lsPrograma%>.</font></b>, 
             por lo tanto no es posible abrir el mes.
             </p>
             <br><br><br>  
          <%
          } //fin if al menos una unidad
      }//fin cierre del mes siguiente
  }// fin ambito 0 y algun programa
   
 conexion.commit();  
 if (continua){
%>
     <p> <font color='#003399'><%=lsMensajeCierre%></font></p>
     <br><br><br>
<%
 } //FIN DE IF
}
catch(Exception E){
    conexion.rollback(); 
    sbAutentifica.setError("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage());      
    System.out.println("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage()); 
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
}
finally{
   if (conexion != null){
       conexion.close();
       conexion=null;
   }  
}
%>
<input type="hidden" name=idCatalogo value=<%=request.getParameter("idCatalogoCuenta")%> >
<table width="100%">
 <tr><td width="73%">&nbsp;</td>
     <td width="10%">
         <INPUT TYPE="button" NAME="btnAceptar" VALUE="Aceptar" class=boton onClick=javascript:cad='c742Filtro.jsp?opcion=abrirCierre&idCatalogoCuenta='+document.forms[0].idCatalogo.value;LlamaPagina(cad,'');>
     

  </td>
  </tr>
</table>
</FORM>
</body>
</html>