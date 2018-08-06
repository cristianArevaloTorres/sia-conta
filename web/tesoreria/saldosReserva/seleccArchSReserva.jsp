<%-- 
    Document   : seleccArchSReserva
    Created on : 5/08/2014, 04:31:29 PM
    Author     : paula.martinez
--%>

<%@page contentType="text/html;charset=ISO-8859-1"
    import="java.util.List"
    import="org.apache.commons.fileupload.*"
    import="org.apache.commons.fileupload.servlet.*"
    import="org.apache.commons.fileupload.disk.*"
    import="java.io.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="dtsProceso" class="sia.rf.tesoreria.VariablesSession" scope="session"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>seleccionarArchivo</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script type="text/javascript" language="javascript">
      function regresa(){
          document.getElementById('form1').action='../tesListadoProgramas.jsp';
          document.getElementById('form1').target='';
          document.getElementById('form1').submit();
      }
      
     function selecciona() {
            ihNombreArchivo = document.getElementById('fichero').value;
            ihRutaArchivo = document.getElementById('ruta').value;
            if(ihRutaArchivo.value!='') {
                window.opener.regresaSeleccionaArchivo(ihNombreArchivo,ihRutaArchivo);
                cerrar();
            }
        }
      
      function cargarArchivo(){
        bandera = false;
        if((document.getElementById('fichero').value=='')||(document.getElementById('fichero').value==null)){
          alert('Seleccione un archivo.');
          bandera = false;
        }else{bandera = true}
        return bandera;
      }
    </script>
  </head>
  <body id="bCargarArchivo" >
<jsp:scriptlet>util.tituloPagina(out, "Tesoreria", " ", "Saldos reserva", "Carga saldos bancarios", true);</jsp:scriptlet>    
<form id="form1" name="form1" method="POST" enctype="multipart/form-data" >
    <input type="hidden" value="<%=dtsProceso.getOpcion()%>" id="opcion" name="opcion">
    <input type="hidden" value="<%=dtsProceso.getNomProg()%>" id="nomProg" name="nomProg">
    <input type="hidden" value="<%=dtsProceso.getIdProgramaS()%>" id="idProgramaS" name="idProgramaS">
    <input type="hidden" value="<%=dtsProceso.getProceso()%>" id="proceso" >
<table>
        <thead></thead>
        <tbody>
           <tr align="left">
              <td style="color:rgb(0,99,148); font-style:italic; font-weight:bold;"><%=dtsProceso.getOpcion()%> >> <%=dtsProceso.getNomProg()%></td>
           </tr>
        </tbody>
 </table>    
<br>           
<table width="50%" align="center">
    <tr>
    <td>Seleccione un archivo:</td>
    <td><input type=file size="80" name=fichero id="fichero"></td>
</table>

<br>
<table width="50%" align="center">
<thead>
<tr>
<td><hr noshade="noshade" width="80%" align="center" style="border-width: 1px;border-style:solid"></td>
</tr>
</thead>
</table>
  <table align="center" cellpadding="3">
    <thead></thead>
    <tbody>
      <tr>
        <td><input type="submit" class="boton" value="Cargar archivo" onclick="return cargarArchivo()"></td>
        <td><input type="button" class="boton" value="Cancelar" onclick="regresa()"></td>

      </tr>
    </tbody>
  </table>
<% 
            
   ServletRequestContext src = new ServletRequestContext(request);            
   String rutaDescarga = request.getServletContext().getRealPath("tesoreria//descargas//saldosReserva//");
   String nombre = "";
   Long tamanio = 0L;
   String contenido = "";
   File destino = new File(rutaDescarga);
   if(ServletFileUpload.isMultipartContent(src)){
     DiskFileItemFactory factory = new DiskFileItemFactory((1024*1024),destino);
     ServletFileUpload upload = new  ServletFileUpload(factory);
     List lista = upload.parseRequest(src);
     File file = null;
     java.util.Iterator it = lista.iterator();
     while(it.hasNext()){
	FileItem item=(FileItem)it.next();
	if(item.isFormField())
          System.out.println("Archivo: " + item.getFieldName());
	else{
	  file=new File(item.getName().substring(item.getName().lastIndexOf("\\")+1,item.getName().length()));
          if (!(destino.exists())) {
              destino.mkdirs();
          }
	  item.write(new File(destino,file.getName()));
          nombre = file.getName();
          tamanio = file.getUsableSpace();
          System.out.println("Fichero subido");
	} // end if
      } // end while
       session.setAttribute("ruta", rutaDescarga );
       session.setAttribute("nombre",  nombre);
       session.setAttribute("tamanio",  tamanio);
       response.sendRedirect("registraSReserva.jsp");
   } // end if
%>
    <input type="hidden" name="nombre" id="nombre" value="<%=nombre%>">
    <input type="hidden" name="ruta" id="ruta" value="<%=rutaDescarga%>">
    <input type="hidden" name="tamanio" id="tamanio" value="<%=tamanio%>">
   
  </form>
  </body>
</html>