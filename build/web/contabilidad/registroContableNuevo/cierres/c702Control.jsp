<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.beans.seguridad.*"%>
<html>
<head>
<meta http-equiv="Content-Language" content="es-mx">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Cierre Definitivo</title>
<link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type='text/css'>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/dynapi/src/dynapi.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/almacen.js" type="text/javascript"></script>
</head>


<jsp:useBean id="sbAutentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>
<jsp:useBean id="pbCierresMensuales" class="sia.rf.contabilidad.registroContableNuevo.bcCierresMensuales" scope="page"/>
<jsp:useBean id="pbUnidadesEjec" class="sia.rf.contabilidad.registroContableNuevo.bcUnidadesEjec" scope="page"/>
<jsp:useBean id="pbCuentaContable" class="sia.rf.contabilidad.registroContableNuevo.bcCuentaContable" scope="page"/>
<jsp:useBean id="pbEstado" class="sia.rf.contabilidad.registroContableEvento.Catalogo" scope="page"/>
<jsp:useBean id="pbProcesos" class="sia.rf.contabilidad.registroContableNuevo.bcProcesosCierre" scope="page"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  





<body topmargin=1 leftmargin=10>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Cierre Definitivo", "Resultado", true);</jsp:scriptlet>
<br><br>
      <jsp:directive.include file="../encabezadoFechaActual.jspf"/>
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
 sbAutentifica.setPagina("c702Control");
 //String operacion=request.getParameter("txtOperacion");
 
  
try{
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);
    String lsUnidad_ejecutora=request.getParameter("lstProceso");   
    Integer Ambito=Integer.parseInt(request.getParameter("lstSubProceso")); 
    String lsAmbito=lsUnidad_ejecutora.equals("0")?Ambito.toString():Ambito.toString().substring(Ambito.toString().length()-1);   
    Integer Entidad=Integer.parseInt(request.getParameter("lstActividad")); 
    String lsEntidad=lsUnidad_ejecutora.equals("0")?Entidad.toString(): (Entidad.toString().length()==3?Entidad.toString().substring(Entidad.toString().length()-2,Entidad.toString().length()):Entidad.toString().substring(Entidad.toString().length()-1));           
    
    String lsEjercicio=request.getParameter("lstCierre");      
    String lsId_catalogo_cuenta=request.getParameter("idCatalogoCuenta");   
    String lsPrograma=request.getParameter("programa");   
    String lsMes=request.getParameter("lstMes");       
    String lsMesAnterior="";
    String lsMesSiguiente="";
    String lsEjerAnterior=lsEjercicio;
    String lsEjerSiguiente=lsEjercicio; 
    String desMeses [] ={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    String lsMensajeCierre="El Cierre Definitivo se ha aplicado correctamente, para las siguientes unidades: <br> ";
    String numEmp=Integer.toString(sbAutentifica.getNumeroEmpleado());
    int contadorCierres=0;
    
    Vector vecUnidad;
    Vector vecEntidad;
    Vector vecAmbito;
    Vector vecPrograma;
    
    Boolean continua=true;  
    Boolean saldos=true;
    System.out.println("Cierre de la unidad: "+lsUnidad_ejecutora+", ambito: "+lsAmbito+",Entidad: "+lsEntidad+", Programa:"+lsPrograma); 
 
    //Se obtiene el mes y ejercicio anterior, así como mes y ejercicio siguiente.
    if (lsMes.equals("01")){//Si es Enero poner el mes anterior Dic. y un ejercicio anterior
       lsMesAnterior="12";
       lsEjerAnterior=String.valueOf(Integer.parseInt(lsEjerAnterior)-1);       
    }else{
       lsMesAnterior=String.valueOf(Integer.parseInt(lsMes)-1);
    } 
  
    if (lsMes.equals("12")){//Si es Dic. poner el mes sig. Enero y un ejercicio siguiente
        lsMesSiguiente="1";
        lsEjerSiguiente=String.valueOf(Integer.parseInt(lsEjerSiguiente)+1);       
    }else{
         lsMesSiguiente=String.valueOf(Integer.parseInt(lsMes)+1);            
    }
    
    
 /* ***** CIERRE AL NIVEL MAS BAJO PARA ESTATALES  ***** */ 
 if  (lsAmbito.equals("1")||lsAmbito.equals("3")){
   //Verificar si el mes anterior ya tiene un cierre definitivo aplicado
   pbCierresMensuales.select_cierre_mensual(conexion, lsUnidad_ejecutora,lsAmbito,lsEntidad,lsEjerAnterior,lsId_catalogo_cuenta,lsMesAnterior,lsPrograma,"2");
   if (pbCierresMensuales.getEstatus_cierre_id().equals("")){%>
      <p>No es posible efectuar el cierre debido a que  no existe un <strong><I>Cierre Definitivo</I></strong> para el Mes Anterior:  <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMesAnterior)-1]%></font></b>, Ejercicio:<b><font color='#003399'><%=lsEjerAnterior%></font></b> 
         Unidad Ejecutora:  <b><font color='#003399'><%=lsUnidad_ejecutora%></font></b>, Entidad: <b><font color='#003399'><%=lsEntidad%></font></b>, Ambito: <b><font color='#003399'><%=lsAmbito%></font></b>,
         y Programa: <b><font color='#003399'><%=lsPrograma%>.</font></b>
       </p>
      <br><br><br>  
<% 
      continua=false; 
   }
   else{ 
       //Verificar si existe un cierre definitivo para el mes actual    
       pbCierresMensuales.select_cierre_mensual(conexion, lsUnidad_ejecutora,lsAmbito,lsEntidad,lsEjercicio,lsId_catalogo_cuenta,lsMes,lsPrograma,"2");
       if (pbCierresMensuales.getEstatus_cierre_id().equals("2")){%>
           <p>No es posible efectuar el cierre debido a que ya existe un <strong><I>Cierre Definitivo</I></strong> para la Unidad Ejecutora:
           <b><font color='#003399'><%=lsUnidad_ejecutora%></font></b>, Entidad: <b><font color='#003399'><%=lsEntidad%></font></b>, Ambito: <b><font color='#003399'><%=lsAmbito%></font></b>,
            Mes: <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMes)-1]%></font></b>, Ejercicio:<b><font color='#003399'><%=lsEjercicio%></font></b> y Programa: <b><font color='#003399'><%=lsPrograma%>.</font></b>
           </p>
           <br><br><br>  
<%       
          continua=false;
        }
        else{ 
            //Verificar si ya está el siguiente mes ABIERTO sino se inserta con estatus 0 - abierto
            pbCierresMensuales.select_cierre_mensual(conexion, lsUnidad_ejecutora,lsAmbito,lsEntidad,lsEjerSiguiente,lsId_catalogo_cuenta,lsMesSiguiente,lsPrograma,"0");
            if (pbCierresMensuales.getEstatus_cierre_id().equals("")){
                grabaCierresMensuales(pbCierresMensuales,lsUnidad_ejecutora,lsAmbito,"147",lsEntidad,lsMesSiguiente,"0",lsEjerSiguiente,lsId_catalogo_cuenta,lsPrograma,numEmp);
                pbCierresMensuales.insert_cierre_mensual(conexion);
            }             
           
           //Actualizar los saldos de las cuentas de la unidad, entidad, ambito
           pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, lsUnidad_ejecutora,lsAmbito, lsEntidad,lsEjercicio, lsPrograma, lsId_catalogo_cuenta, lsMes);
           //Actualizar los saldos de las cuentas de la unidad
           pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, lsUnidad_ejecutora,"0", "0",lsEjercicio, lsPrograma, lsId_catalogo_cuenta, lsMes); 
          
          //Actualizar los saldos del programa
           pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, "0","0", "0",lsEjercicio, lsPrograma, lsId_catalogo_cuenta, lsMes); 
           //Actualizar los saldos de las cuentas de mayor
           pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, "0","0", "0",lsEjercicio, "0", lsId_catalogo_cuenta, lsMes);                     
          
               
           //Insertar cierre estatus 2 - Cierre Definitivo
           grabaCierresMensuales(pbCierresMensuales,lsUnidad_ejecutora,lsAmbito,"147",lsEntidad,lsMes,"2",lsEjercicio,lsId_catalogo_cuenta,lsPrograma,numEmp);
           pbCierresMensuales.insert_cierre_mensual(conexion);
           lsMensajeCierre=lsMensajeCierre+" &nbsp; &nbsp; &nbsp; &nbsp; Unidad: <b>"+lsUnidad_ejecutora+"</b>, Entidad: <b>"+lsEntidad+"</b>, Ambito: <b>"+lsAmbito+"</b>. <br>";    
           lsMensajeCierre=lsMensajeCierre+" Mes: <b>"+desMeses[Integer.parseInt(lsMes)-1]+"</b>, Ejercicio: <b>"+lsEjercicio+"</b> y Programa: <b>"+lsPrograma+".</b> <br>";    
         }//fin cierre definitivo para el mes actual   
   }// fin cierre definitivo en el mes anterior 
 }//fin de if ambito = 3



/* ***** CIERRE AL NIVEL INTERMEDIO "REGIONAL" INCLUYENDO ESTATALES QUE NO HAYAN CERRADO  ***** */   
 if  (lsAmbito.equals("2")){
      vecEntidad = new Vector();
      vecAmbito = new Vector();
      pbUnidadesEjec.select_UnidadesEjecporUnidad(conexion,lsUnidad_ejecutora,lsEjercicio,lsId_catalogo_cuenta,lsPrograma);
      //Revisar cada una de las entidades y ambitos pertenecientes a la unidad de la Regional
      vecEntidad = pbUnidadesEjec.getVEntidad();
      vecAmbito = pbUnidadesEjec.getVAmbito();
      int   numElem=vecEntidad.size();
      for (int j=0; j<numElem; j++){    
       //Verificar si el mes anterior ya tiene un cierre definitivo aplicado
       pbCierresMensuales.select_cierre_mensual(conexion, lsUnidad_ejecutora,vecAmbito.elementAt(j).toString(),vecEntidad.elementAt(j).toString(),lsEjerAnterior,lsId_catalogo_cuenta,lsMesAnterior,lsPrograma,"2");
       if (pbCierresMensuales.getEstatus_cierre_id().equals("")){%>
           <p>No es posible efectuar el cierre debido a que no existe un <strong><I>Cierre Definitivo</I></strong> para el Mes Anterior:  <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMesAnterior)-1]%></font></b>, Ejercicio:<b><font color='#003399'><%=lsEjerAnterior%></font></b> 
              Unidad Ejecutora:  <b><font color='#003399'><%=lsUnidad_ejecutora%></font></b>, Entidad: <b><font color='#003399'><%=vecEntidad.elementAt(j).toString()%></font></b>, Ambito: <b><font color='#003399'><%=vecAmbito.elementAt(j).toString()%></font></b>,
              y Programa: <b><font color='#003399'><%=lsPrograma%>.</font></b>
           </p>
           <br><br><br>  
       <% 
           continua=false; 
           break;
       }//fin if cierre anterior     
      }// fin for verifica cierre anteriores   
      
      
      for (int j=0; j<numElem; j++){    
      //Verificar si todas las unidades y la regional ya hicieron cierre
       pbCierresMensuales.select_cierre_mensual(conexion, lsUnidad_ejecutora, vecAmbito.elementAt(j).toString(),vecEntidad.elementAt(j).toString(),lsEjercicio,lsId_catalogo_cuenta,lsMes,lsPrograma,"2");
       if (pbCierresMensuales.getEstatus_cierre_id().equals("2")){
           contadorCierres++;
       }//fin if cierre definitivo
      }// fin for verifica cierre anteriores   
      if (numElem==contadorCierres){ 
          continua=false; 
      %>
           <p>No es posible efectuar el cierre debido a que ya existe un <strong><I>Cierre Definitivo</I></strong> para toda la Unidad Ejecutora:
           <b><font color='#003399'><%=lsUnidad_ejecutora%></font></b>, Mes: <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMes)-1]%></font></b>, Ejercicio:<b><font color='#003399'><%=lsEjercicio%></font></b> y Programa: <b><font color='#003399'><%=lsPrograma%>.</font></b>
           </p>
           <br><br><br>  
      <%
      }
      if (continua){
        for (int j=0; j<numElem; j++){        
          //Verificar si existe un cierre definitivo para el mes actual sino lo aplica.   
          pbCierresMensuales.select_cierre_mensual(conexion, lsUnidad_ejecutora, vecAmbito.elementAt(j).toString(),vecEntidad.elementAt(j).toString(),lsEjercicio,lsId_catalogo_cuenta,lsMes,lsPrograma,"2");
          if (!pbCierresMensuales.getEstatus_cierre_id().equals("2")){              
               //Verificar si ya está el siguiente mes ABIERTO sino se inserta con estatus 0 - abierto
               pbCierresMensuales.select_cierre_mensual(conexion, lsUnidad_ejecutora,vecAmbito.elementAt(j).toString(),vecEntidad.elementAt(j).toString(),lsEjerSiguiente,lsId_catalogo_cuenta,lsMesSiguiente,lsPrograma,"0");
               if (pbCierresMensuales.getEstatus_cierre_id().equals("")){
                   grabaCierresMensuales(pbCierresMensuales,lsUnidad_ejecutora,vecAmbito.elementAt(j).toString(),"147",vecEntidad.elementAt(j).toString(),lsMesSiguiente,"0",lsEjerSiguiente,lsId_catalogo_cuenta,lsPrograma,numEmp);
                   pbCierresMensuales.insert_cierre_mensual(conexion);
               }             
               //Actualizar los saldos de las cuentas
               pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, lsUnidad_ejecutora,vecAmbito.elementAt(j).toString(), vecEntidad.elementAt(j).toString(),lsEjercicio, lsPrograma, lsId_catalogo_cuenta, lsMes);
               
               //Insertar cierre estatus 2 - Cierre Definitivo              
               grabaCierresMensuales(pbCierresMensuales,lsUnidad_ejecutora,vecAmbito.elementAt(j).toString(),"147",vecEntidad.elementAt(j).toString(),lsMes,"2",lsEjercicio,lsId_catalogo_cuenta,lsPrograma,numEmp);
               pbCierresMensuales.insert_cierre_mensual(conexion);
               lsMensajeCierre=lsMensajeCierre+" &nbsp; &nbsp; &nbsp; &nbsp; Unidad: <b>"+lsUnidad_ejecutora+"</b>, Entidad: <b>"+vecEntidad.elementAt(j).toString()+"</b>, Ambito: <b>"+vecAmbito.elementAt(j).toString()+"</b>. <br>";    
          }//  fin if si el cierre definitivo ya no está hecho
        }  //fin for cierres definitivos
        //Actualizar los saldos de las cuentas en el nivel superior de la regional
        pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, lsUnidad_ejecutora,"0", "0",lsEjercicio, lsPrograma, lsId_catalogo_cuenta, lsMes);
        //Actualizar los saldos del programa
        pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, "0","0", "0",lsEjercicio, lsPrograma, lsId_catalogo_cuenta, lsMes); 
        //Actualizar los saldos de las cuentas de mayor
        pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, "0","0", "0",lsEjercicio, "0", lsId_catalogo_cuenta, lsMes);                     

        
        
        lsMensajeCierre=lsMensajeCierre+" Mes: <b>"+desMeses[Integer.parseInt(lsMes)-1]+"</b>, Ejercicio: <b>"+lsEjercicio+"</b> y Programa: <b>"+lsPrograma+".</b> <br>";    
      }// fin if continua
 } //fin ambito = 2
 
 /* ***** CIERRE AL NIVEL SUPER USUARIO "CENTRAL" INCLUYENDO TODAS LAS UNIDADES, ENTIDADES Y AMBITOS QUE QUE NO HAYAN CERRADO **** */   
 
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
      for (int j=0; j<numElem; j++){    
       //Verificar si el mes anterior ya tiene un cierre definitivo aplicado
       pbCierresMensuales.select_cierre_mensual(conexion, vecUnidad.elementAt(j).toString(),vecAmbito.elementAt(j).toString(),vecEntidad.elementAt(j).toString(),lsEjerAnterior,lsId_catalogo_cuenta,lsMesAnterior,vecPrograma.elementAt(j).toString(),"2");
       if (pbCierresMensuales.getEstatus_cierre_id().equals("")){%>
           <p>No es posible efectuar el cierre debido a que no existe un <strong><I>Cierre Definitivo</I></strong> para el Mes Anterior:  <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMesAnterior)-1]%></font></b>, Ejercicio:<b><font color='#003399'><%=lsEjerAnterior%></font></b> 
              Unidad Ejecutora:  <b><font color='#003399'><%=vecUnidad.elementAt(j).toString()%></font></b>, Entidad: <b><font color='#003399'><%=vecEntidad.elementAt(j).toString()%></font></b>, Ambito: <b><font color='#003399'><%=vecAmbito.elementAt(j).toString()%></font></b>,
              y Programa: <b><font color='#003399'><%=vecPrograma.elementAt(j).toString()%>.</font></b>
           </p>
           <br><br><br>  
       <% 
           continua=false; 
           break;
       }//fin if cierre anterior     
      }// fin for verifica cierre anteriores   
      
      
      for (int j=0; j<numElem; j++){    
      //Verificar si todas las unidades y la regional ya hicieron cierre
       pbCierresMensuales.select_cierre_mensual(conexion, vecUnidad.elementAt(j).toString(), vecAmbito.elementAt(j).toString(),vecEntidad.elementAt(j).toString(),lsEjercicio,lsId_catalogo_cuenta,lsMes,vecPrograma.elementAt(j).toString(),"2");
       if (pbCierresMensuales.getEstatus_cierre_id().equals("2")){
           contadorCierres++;
       }//fin if cierre definitivo
      }// fin for verifica cierre anteriores 
    
     
 
      
      //Si ya todas las unidades hicieron el cierre verificar los saldos de las cuentas de mayor y programa 
      // si no están actualizados, se hace el update.
      if (numElem==contadorCierres){ 
          continua=false;                    
          pbCuentaContable.select_saldos_acum(conexion,lsEjercicio, lsPrograma, lsId_catalogo_cuenta, lsMes);
          if (pbCuentaContable.getCuenta_contable_id().equals("")){
              saldos=false;
          }else{
               pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, "0","0", "0",lsEjercicio, "0", lsId_catalogo_cuenta, lsMes);                     
               pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, "0","0", "0",lsEjercicio, lsPrograma, lsId_catalogo_cuenta, lsMes);                     
          }                   
          if (saldos){
            %>                 
              <p> <font color='#003399'>Todas las unidades ya han realizado su cierre, únicamente se han actualizado los saldos de 1er nivel (cuenta de mayor) y 2o. nivel(Programa) </font></p>
             <%      
          }else{
            %>
           <p>No es posible efectuar el cierre debido a que ya existe un <strong><I>Cierre Definitivo</I></strong> para todas las Unidades Ejecutoras,
            Mes: <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMes)-1]%></font></b>, Ejercicio:<b><font color='#003399'><%=lsEjercicio%></font></b> y Programa: <b><font color='#003399'><%=lsPrograma%></font></b> .
           </p>
           <br><br><br>  
            <%
          }
      }
      
      
      if (continua){
        for (int j=0; j<numElem; j++){        
          //Verificar si existe un cierre definitivo para el mes actual sino lo aplica.   
          pbCierresMensuales.select_cierre_mensual(conexion, vecUnidad.elementAt(j).toString(), vecAmbito.elementAt(j).toString(),vecEntidad.elementAt(j).toString(),lsEjercicio,lsId_catalogo_cuenta,lsMes,vecPrograma.elementAt(j).toString(),"2");
          if (!pbCierresMensuales.getEstatus_cierre_id().equals("2")){              
               //Verificar si ya está el siguiente mes ABIERTO sino se inserta con estatus 0 - abierto
               pbCierresMensuales.select_cierre_mensual(conexion, vecUnidad.elementAt(j).toString(),vecAmbito.elementAt(j).toString(),vecEntidad.elementAt(j).toString(),lsEjerSiguiente,lsId_catalogo_cuenta,lsMesSiguiente,vecPrograma.elementAt(j).toString(),"0");
               if (pbCierresMensuales.getEstatus_cierre_id().equals("")){
                   grabaCierresMensuales(pbCierresMensuales,vecUnidad.elementAt(j).toString(),vecAmbito.elementAt(j).toString(),"147",vecEntidad.elementAt(j).toString(),lsMesSiguiente,"0",lsEjerSiguiente,lsId_catalogo_cuenta,vecPrograma.elementAt(j).toString(),numEmp);    
                   pbCierresMensuales.insert_cierre_mensual(conexion);
               }             
               //Actualizar los saldos de las cuentas unidad, entidad, ambito, programa
             /*  pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, vecUnidad.elementAt(j).toString(),vecAmbito.elementAt(j).toString(), vecEntidad.elementAt(j).toString(),lsEjercicio, vecPrograma.elementAt(j).toString(), lsId_catalogo_cuenta, lsMes);*/
               
               //Insertar cierre estatus 2 - Cierre Definitivo               
               grabaCierresMensuales(pbCierresMensuales,vecUnidad.elementAt(j).toString(),vecAmbito.elementAt(j).toString(),"147",vecEntidad.elementAt(j).toString(),lsMes,"2",lsEjercicio,lsId_catalogo_cuenta,vecPrograma.elementAt(j).toString(),numEmp);
               pbCierresMensuales.insert_cierre_mensual(conexion);
               //Actualizar los saldos de las cuentas en el nivel superior de la regional
               /*pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, vecUnidad.elementAt(j).toString(),"0", "0",lsEjercicio, vecPrograma.elementAt(j).toString(), lsId_catalogo_cuenta, lsMes);                     
               pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, "0","0", "0",lsEjercicio, vecPrograma.elementAt(j).toString(), lsId_catalogo_cuenta, lsMes);                     */
               lsMensajeCierre=lsMensajeCierre+" &nbsp; &nbsp; &nbsp; &nbsp; Unidad: <b>"+vecUnidad.elementAt(j).toString()+"</b>, Entidad: <b>"+vecEntidad.elementAt(j).toString()+"</b>, Ambito: <b>"+vecAmbito.elementAt(j).toString()+"</b>  y Programa: <b>"+vecPrograma.elementAt(j).toString()+".</b> <br>";                   
          }//  fin if si el cierre definitivo ya no está hecho
         /* else{
               pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, vecUnidad.elementAt(j).toString(),"0", "0",lsEjercicio, vecPrograma.elementAt(j).toString(), lsId_catalogo_cuenta, lsMes);                     
          }*/
        }  //fin for cierres definitivos
        //Actualizar los saldos de las cuentas de mayor y programa
        pbCuentaContable.update_rf_tr_cuentas_contables_acumulados_todos(conexion,lsPrograma,lsEjercicio,lsId_catalogo_cuenta,lsMes);
        pbCuentaContable.update_rf_tr_cuentas_contables_acumulados(conexion, "0","0", "0",lsEjercicio, "0", lsId_catalogo_cuenta, lsMes);                     
        lsMensajeCierre=lsMensajeCierre+" Mes: <b>"+desMeses[Integer.parseInt(lsMes)-1]+"</b> y Ejercicio: <b>"+lsEjercicio+"</b>. <br>";    
      }// fin if continua
  }
 
 
  /* ***** CIERRE AL NIVEL SUPER USUARIO "CENTRAL" INCLUYENDO TODAS LAS UNIDADES, ENTIDADES Y AMBITOS QUE QUE NO HAYAN CERRADO DE TODOS LOS PROGRAMAS **** */   
 
  if  (lsAmbito.equals("0")&&lsPrograma.equals("0")){ 
  
    pbEstado.select_estado_actual(conexion);
    if (!pbEstado.getEstatus().equals("2")){
     continua=false;
 %>
  <p>El proceso de Cierre Definitivo no puede ser lanzado ya que NO se ha cambiado el estatus del catálogo de cuentas para realizar esta acción.</p>
 <%
    } else{  
       pbProcesos.select_rf_tr_procesos_cierre(conexion,lsEjercicio,lsMes);
       if (pbProcesos.getEjercicio()==null){       
          continua=false;
%>
          <p>El proceso de Cierre Definitivo no puede ser lanzado, ya que NO existe un proceso de Congruencia con anteriodidad.</p>     
<%
       } else if (pbProcesos.getCongruencia().equals("0")){
          continua=false;
%>
          <p>El proceso de Cierre Definitivo no puede ser lanzado, ya que NO existe un proceso de Congruencia con anteriodidad.</p>     
<%     
       }     
       else{//realiza el cierre de todos los programas, entidad y ambitos
          //Verificar si existe un cierre definitivo del mes anterior para todos los programas unidades y ámbitos.          
            pbProcesos.select_rf_tr_procesos_cierre(conexion,lsEjerAnterior,lsMesAnterior);
            if (pbProcesos.getCierre_definitivo().equals("0")){
               continua=false; 
%>
              <p>No es posible efectuar el cierre debido a que no existe un <strong><I>Cierre Definitivo</I></strong> para el Mes Anterior:  <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMesAnterior)-1]%></font></b>, Ejercicio:<b><font color='#003399'><%=lsEjerAnterior%></font></b> 
                 Para todoas las unidad, entidades, ámbitos y programas.
              </p>
             <br><br><br>  
       <% 
          }else{              
              //Verificar si existe un cierre definitivo para el mes actual  
              pbProcesos.select_rf_tr_procesos_cierre(conexion,lsEjercicio,lsMes);
              if (pbProcesos.getCierre_definitivo().equals("1")){
                  continua=false; 
       %>                     
                 <p>No es posible efectuar el cierre debido a que ya existe un <strong><I>Cierre Definitivo</I></strong> para todas las Unidades Ejecutoras y Programas,
                 Mes: <b><font color='#003399'><%=desMeses[Integer.parseInt(lsMes)-1]%></font></b>, Ejercicio:<b><font color='#003399'><%=lsEjercicio%></font></b>.
                 </p>
                 <br><br><br>  
<%
              
              }else{
                   vecUnidad = new Vector();   
                   vecEntidad = new Vector();
                   vecAmbito = new Vector();
                   vecPrograma = new Vector();
                    //Revisar cada una de las unidades, entidades, ambitos, y programas                      
                   pbCierresMensuales.select_cierre_mensual_todas_unidad_programas(conexion,lsEjercicio,lsId_catalogo_cuenta,lsMes,"0");
      
                   vecUnidad = pbCierresMensuales.getVUnidad();
                   vecEntidad = pbCierresMensuales.getVEntidad();
                   vecAmbito = pbCierresMensuales.getVAmbito();
                   vecPrograma = pbCierresMensuales.getVPrograma();
      
                   int   numElem=vecUnidad.size();
                   for (int j=0; j<numElem; j++){                 
                      //Verificar si ya está el siguiente mes ABIERTO sino se inserta con estatus 0 - abierto
                      pbCierresMensuales.select_cierre_mensual(conexion, vecUnidad.elementAt(j).toString(),vecAmbito.elementAt(j).toString(),vecEntidad.elementAt(j).toString(),lsEjerSiguiente,lsId_catalogo_cuenta,lsMesSiguiente,vecPrograma.elementAt(j).toString(),"0");
                      if (pbCierresMensuales.getEstatus_cierre_id().equals("")){
                         grabaCierresMensuales(pbCierresMensuales,vecUnidad.elementAt(j).toString(),vecAmbito.elementAt(j).toString(),"147",vecEntidad.elementAt(j).toString(),lsMesSiguiente,"0",lsEjerSiguiente,lsId_catalogo_cuenta,vecPrograma.elementAt(j).toString(),numEmp);    
                         pbCierresMensuales.insert_cierre_mensual(conexion);
                      }                           
                     //Verificar si ya hay cierre definitvo del mes con estatus 2 - Cierre Definitivo, sino agregar el registro               
                      pbCierresMensuales.select_cierre_mensual(conexion, vecUnidad.elementAt(j).toString(),vecAmbito.elementAt(j).toString(),vecEntidad.elementAt(j).toString(),lsEjercicio,lsId_catalogo_cuenta,lsMes,vecPrograma.elementAt(j).toString(),"2");
                      if (pbCierresMensuales.getEstatus_cierre_id().equals("")){
                         grabaCierresMensuales(pbCierresMensuales,vecUnidad.elementAt(j).toString(),vecAmbito.elementAt(j).toString(),"147",vecEntidad.elementAt(j).toString(),lsMes,"2",lsEjercicio,lsId_catalogo_cuenta,vecPrograma.elementAt(j).toString(),numEmp);
                         pbCierresMensuales.insert_cierre_mensual(conexion);
                         }
                      lsMensajeCierre=lsMensajeCierre+" &nbsp; &nbsp; &nbsp; &nbsp; Unidad: <b>"+vecUnidad.elementAt(j).toString()+"</b>, Entidad: <b>"+vecEntidad.elementAt(j).toString()+"</b>, Ambito: <b>"+vecAmbito.elementAt(j).toString()+"</b>  y Programa: <b>"+vecPrograma.elementAt(j).toString()+".</b> <br>";                   
                   }
                   //Actualizar los saldos 
                   pbCuentaContable.update_rf_tr_cuentas_contables_acumulados_todos(conexion,lsPrograma,lsEjercicio,lsId_catalogo_cuenta,lsMes);        
                   lsMensajeCierre=lsMensajeCierre+" Mes: <b>"+desMeses[Integer.parseInt(lsMes)-1]+"</b> y Ejercicio: <b>"+lsEjercicio+"</b>. <br>";    
                   //Actualizar el estatus del Cierre Definitivo en la tabla de Procesos
                   pbProcesos.update_rf_tr_procesos_cierre_estatus(conexion,lsEjercicio,lsMes,"4","1");                   
                 }
          }
       }
    }
  }
  
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
     <INPUT TYPE="button" NAME="btnAceptar" VALUE="Aceptar" class=boton onClick=javascript:cad='c742Filtro.jsp?opcion=Cierre&idCatalogoCuenta='+document.forms[0].idCatalogo.value;LlamaPagina(cad,'');>
     

  </td>
  </tr>
</table>
</FORM>
</body>
</html>