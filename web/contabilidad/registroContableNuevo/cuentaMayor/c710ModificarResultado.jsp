<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*" %>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Contabilidad - Cuentas de Mayor - Resultado</title>
<link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/dynapi/src/dynapi.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/almacen.js" type="text/javascript"></script>
<script type="text/javascript" src="../../../Librerias/Javascript/jquery.js"></script>
<script type="text/javascript" src="../../../Librerias/Javascript/jquery.meio.mask.js" charset="utf-8"></script>
<script language="JavaScript" type="text/javascript">
 
function revisa(){
    //Valida que exista al menos un elemento check box seleccionado y si es así confirmar su eliminación
    var avanza = true;
    var checksOK = false;
    checksOK = verificaChecksSinSubmit(document.Resultado, 'AMBITOS', 'Seleccione al menos un registro.');
    if (checksOK==true){    //Al menos un check box esta seleccionado
        var borrar= confirm('¿Desea Eliminar estos registros?');
        if (borrar == false){
            avanza = false;
        }
    }else{                   //Ningún check box esta seleccionado
        avanza=false;
    }
    
    if(avanza==true){
        Resultado.submit();
    }
 }
 
</script>
</head>

<body>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", "Cuentas de Mayor ", "[Modificar]", "Resultado", true);</jsp:scriptlet>
<FORM name="Resultado" id="Resultado" Method="post" action="" >
<!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
<table width="100%" align="center" class="general" name="tAmbitos" id="tAmbitos"><tr>
<td>
<!-- (Inicia codigo JAVA) -->
<%
   Connection conexion=null;
          String lsOpcion= request.getParameter("txtOpcion");
 try{
    conexion=DaoFactory.getContabilidad();
    
       String lsCondicion=" where ";     
       String lsGenero = request.getParameter("lstGenero");
       String lsGrupo = request.getParameter("lstGrupo");
       String lsClase = request.getParameter("lstClase");
       String lsNumero = request.getParameter("txtCve");  
       String lsNombre = request.getParameter("txtNombre");    
       String lsNatura = request.getParameter("btrNatura");
       String ban="0";
   
       
       if (!lsNumero.equals("")){   
           lsCondicion = lsCondicion + " cuenta_mayor like '%" + lsNumero+"%' ";
           ban="1";
          }
          
       if (!lsGenero.equals("null") && lsNumero.equals("")){
          lsCondicion = lsCondicion + " id_genero = '" + lsGenero+"' ";
          ban="1";}
                
       if (!lsGrupo.equals("null")  && lsNumero.equals(""))
          lsCondicion = lsCondicion + " and id_grupo = '" + lsGrupo+"' ";
       
       if (!lsClase.equals("null") && lsNumero.equals(""))
          lsCondicion = lsCondicion + " and id_clase = '" + lsClase+"' ";
          
       if (!lsNombre.equals("")){
          if(ban.equals("1")){
                  lsCondicion = lsCondicion + " and descripcion like '%" + lsNombre+"%' ";                                              
          }else{
                  lsCondicion = lsCondicion + " descripcion like '%" + lsNombre+"%' "; 
                  ban="1";
          }}
       
        if (!lsNatura.equals("C")){
          if(ban.equals("1")){
                  lsCondicion = lsCondicion + " and naturaleza = '" + lsNatura+"' ";
          }else{
                  lsCondicion = lsCondicion + " naturaleza = '" + lsNatura+"' ";
          }}
          
       
       if (lsCondicion.equals(" where ")){
       lsCondicion = lsCondicion + " 0=0 ";
       }  
     
        StringBuffer SQL = new StringBuffer("select ROWNUM CONSFILA, sqlresul.* from ");
        SQL.append(" (SELECT '<a href='||LOWER('c')||'710M'||LOWER('odificar')||'F'||LOWER('ormulario.jsp?cve')||'C'||LOWER('ta=')||cuenta_mayor||' >'||cuenta_mayor||'</a>' valor, ");
        SQL.append(" cuenta_mayor as cuenta, ");
        SQL.append(" descripcion, (CASE WHEN naturaleza='A' THEN 'Acreedora' ELSE 'Deudora' END) as naturaleza,");
        SQL.append(" to_char(fecha_vig_ini,'DD/MM/YYYY') as fechaini, to_char(fecha_vig_fin,'DD/MM/YYYY') as fechafin");
        SQL.append(" FROM RF_TC_CLASIFICADOR_CUENTAS "+lsCondicion);
        SQL.append(" ORDER BY cuenta_mayor )sqlresul");
    
       String [] DefQuery={SQL.toString(),"100%","LEFT","2",""};
       String [] DefAlias={"CONSFILA-,VALOR-,DESCRIPCION-,NATURALEZA-,FECHAINI-,FECHAFIN","10%,15%,45%,10%,10%,10%","No. Consecutivo,Cuenta de Mayor,Descripción,Naturaleza,Fecha Inicial,Fecha Final","4,4,4,4,4"};
       String [] DefInput={"CUENTA","CHECKBOX","ambitos","LEFT"};
       xPaginacion.SeleccionarHorizontalQuery(conexion, DefQuery, DefAlias, DefInput, out, 0, "",""," >", false);

}
catch(Exception E)
   {
   
    System.out.println("Error en pagina en "+" "+E.getMessage()); 
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



<table align="center">
 <tr>
     <td align="center">
            <INPUT type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c710ModificarFiltro.jsp','');" >
        </td>
    </tr>   
</table>

  </FORM>
  </body>
</html>