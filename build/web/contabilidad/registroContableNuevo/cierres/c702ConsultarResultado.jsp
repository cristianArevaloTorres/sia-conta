<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c702ConsultarResultado</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
</head>
<body>


<jsp:scriptlet>util.tituloPagina(out,"Contabilidad"," ","Consultar Cierres","Consultar",true);</jsp:scriptlet>
<br><br>
      <jsp:directive.include file="../encabezadoFechaActual.jspf"/>
<br><br>

<br>

<FORM name="resultado" id="resultado" Method="post">

<!-- Encabezado de la tabla con resultado del filtro -->
<table width="15%" align="left" class="general">
    <tr>
      <th height="21" colspan="3" align="left" class="general">Resultado del filtro</th>
    </tr>
 </table>
 <br><br><br>



<!-- (Inicia codigo JAVA) -->
<%
 Connection conexion=null;
    String lsUnidad_ejecutora=request.getParameter("lstProceso");   
    Integer Ambito=Integer.parseInt(request.getParameter("lstSubProceso")); 
    String lsAmbito=lsUnidad_ejecutora.equals("0")?Ambito.toString():Ambito.toString().substring(3);   
    Integer Entidad=Integer.parseInt(request.getParameter("lstActividad")); 
    String lsEntidad=lsUnidad_ejecutora.equals("0")?Entidad.toString():Entidad.toString().substring(3);       
    String lsEjercicio=request.getParameter("lstCierre");      
    String lsId_catalogo_cuenta=request.getParameter("idCatalogoCuenta");   
    String lsPrograma=request.getParameter("programa");   
    String lsMes=request.getParameter("lstMes");   
    String lsCondicion="";
    

%>

<!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
<table width="100%" align="center" class="general" name="tAmbitos" id="tAmbitos">
<tr>
<td>
<<input name="idCatalogo" type="hidden" value=<%=lsId_catalogo_cuenta%> >>
<!-- (Inicia codigo JAVA) -->
<%  

 String Query="";
 try{
    conexion=DaoFactory.getContabilidad();

    if (!(lsUnidad_ejecutora.equals("")||lsUnidad_ejecutora.equals("0"))) {
        lsCondicion = lsCondicion + " and t.unidad_ejecutora='" + lsUnidad_ejecutora +"' ";
    }
    if (!(lsAmbito.equals("")||lsAmbito.equals("0"))) {
       if (lsAmbito.equals("1")||lsAmbito.equals("3")) {    
           lsCondicion = lsCondicion + " and t.ambito='"+lsAmbito+"'";
           if (!lsEntidad.equals("")) {
              lsCondicion = lsCondicion + " and t.entidad='"+lsEntidad+"'";       
           }  
       }    
    }      
    if (!lsEjercicio.equals("")) {
        lsCondicion=lsCondicion+ " and t.ejercicio='"+lsEjercicio+"'";
    }
    if (!lsId_catalogo_cuenta.equals("")) {
        lsCondicion=lsCondicion+ " and t.id_catalogo_cuenta='"+lsId_catalogo_cuenta+"'";          
    }
    if (!(lsPrograma.equals("0")||lsPrograma.equals(""))) {
        lsCondicion=lsCondicion+ " and t.programa='"+lsPrograma+"'";          
    }
    if (!lsMes.equals("")) {
        lsCondicion=lsCondicion+ " and t.mes='"+lsMes+"'";          
    }


     Query="select ROWNUM CONSFILA, sqlresul.* from "+
                 "( select t.unidad_ejecutora,decode(t.ambito,'1','Central','2','Regional','3','Estatal') as ambito,c.descripcion as entidad,t.programa, "+
                 " decode(t.mes,'1','Enero','2','Febrero','3','Marzo','4','Abril','5','Mayo','6','Junio','7','Julio','8','Agosto','9','Septiembre','10','Octubre','11','Noviembre','12','Diciembre') as Mes, "+
                 " t.ejercicio, decode(t.estatus_cierre_id,0,'Abierto',1,'Preliminar',2,'Definitivo') as TipoCierre, to_char(t.registro,'dd/mm/yy hh:mm:ss') as fecha, "+
                 " e.nombres||' '||e.apellido_pat||' '||e.apellido_mat as respCierre " +
                 " from rf_tr_cierres_mensuales t, rh_tr_empleados e,rh_tc_entidades c  "+
                 " where t.num_empleado=e.num_empleado and t.entidad=c.entidad   "+lsCondicion+                 
                 " order by t.unidad_ejecutora,t.entidad,t.ambito "+                                
                 " ) sqlresul ";

   
   
   
      String [] DefQuery={Query,"100%","LEFT","2",""};
      String [] DefAlias={"UNIDAD_EJECUTORA-,AMBITO-,ENTIDAD-,PROGRAMA-,MES-,TIPOCIERRE-,FECHA-,RESPCIERRE-","10%,10%,10%,10%,10%,10%,10%,30%","Unidad ejecutora,Ambito,Entidad,Programa,Mes,Tipo de Cierre,Fecha de Cierre,Persona que efectuó el cierre","4,4,4,4,4,4,4,4"};
      String [] DefInput={"POLIZAID","CHECKBOX","ambitos","LEFT"};
      
      xPaginacion.SeleccionarHorizontalQuery(conexion,DefQuery,DefAlias, DefInput,out,0,"","","",false);

}
catch(Exception E)
   {
   System.out.println("Query: "+Query);
   System.out.println("Error en "+E.getMessage()); 
 
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
   }
   finally{ 
     if (conexion!=null){
         conexion.close();
         conexion=null;
     }

   }
%>
<!-- (Termina codigo JAVA) -->
</td>
</tr>
</table>
<!-- Termina tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->



<table width='100%'>
 <tr><td width='73%'>&nbsp;</td>
     <td width='10%'>
    </td>
     <td width='80%'>
     <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick=javascript:cad='c742Filtro.jsp?opcion=Cierre&idCatalogoCuenta='+document.forms[0].idCatalogo.value;LlamaPagina(cad,''); >
     </td></tr>
 </table>

  </FORM>
  </body>
</html>