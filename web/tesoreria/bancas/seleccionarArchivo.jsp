<%-- 
    Document   : seleccionarArchivo
    Created on : 28/07/2014, 10:39:42 AM
    Author     : Paula.Martinez
--%>

<%@page contentType="text/html;charset=ISO-8859-1"
    import="java.util.List"
    import="org.apache.commons.fileupload.*"
    import="org.apache.commons.fileupload.servlet.*"
    import="org.apache.commons.fileupload.disk.*"
    import="java.io.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   

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
  <body id="bCargarArchivo" onunload="selecciona()">
<jsp:scriptlet>util.tituloPagina(out, "Tesoreria", " ", "Selección de archivo de movimientos", "Importación de transacciones bancarias", true);
        </jsp:scriptlet>    
<form id="form1" name="form1" method="POST" enctype="multipart/form-data" >
    
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
   String rutaDescarga = request.getServletContext().getRealPath("tesoreria//descargas//adminTransaccion//bancas//");
   String nombre = "";
   Long tamanio = 0L;
   String contenido = "";
   File destino = new File(rutaDescarga);
    String opcion = "";
    String nomProg = "";
    String idProgramaS = "";
    String proceso = "";
   if(ServletFileUpload.isMultipartContent(src)){
     DiskFileItemFactory factory = new DiskFileItemFactory((1024*1024),destino);
     ServletFileUpload upload = new  ServletFileUpload(factory);
     List lista = upload.parseRequest(src);
     File file = null;
     java.util.Iterator it = lista.iterator();
     while(it.hasNext()){
	FileItem item=(FileItem)it.next();
	if(item.isFormField())
        {
          if(item.getFieldName().equals("opcion"))
              opcion = item.getString();
          if(item.getFieldName().equals("nomProg"))
              nomProg = item.getString();
          if(item.getFieldName().equals("idProgramaS"))
              idProgramaS = item.getString();
          if(item.getFieldName().equals("proceso"))
              proceso = item.getString();
        }
	else{
	  //file=new File(item.getName().substring(item.getName().lastIndexOf("\\")+1,item.getName().length()));
	  file=new File(item.getName());
          if (!(destino.exists())) {
              destino.mkdirs();
          }
	  item.write(new File(destino,file.getName()));
          File dest = new File(destino+"//"+file.getName());
          System.out.println("Archivo: "+destino+"//"+file.getName());
          nombre = dest.getName();
          tamanio = dest.getUsableSpace();
          System.out.println("Tamaño: "+dest.getTotalSpace());
          System.out.println("Fichero subido");
	} // end if
      } // end while
       session.setAttribute("ruta", rutaDescarga );
       session.setAttribute("nombre",  nombre);
       session.setAttribute("tamanio",  tamanio);
       session.setAttribute("opcion",  opcion);
       session.setAttribute("nomProg",  nomProg);
       session.setAttribute("idProgramaS",  idProgramaS);
       session.setAttribute("proceso",  proceso);
       response.sendRedirect("tesCargaArchivoBanco.jsp");
   } // end if
%>
    <input type="hidden" name="nombre" id="nombre" value="<%=nombre%>">
    <input type="hidden" name="ruta" id="ruta" value="<%=rutaDescarga%>">
    <input type="hidden" name="tamanio" id="tamanio" value="<%=tamanio%>">
    <input type="hidden" value="<%=request.getParameter("opcion")%>" id="opcion" name="opcion">
    <input type="hidden" value="<%=request.getParameter("nomProg")%>" id="nomProg" name="nomProg">
    <input type="hidden" value="<%=request.getParameter("idProgramaS")%>" id="idProgramaS" name="idProgramaS">
    <input type="hidden" value="<%=request.getParameter("proceso")%>" id="proceso" name="proceso">
  </form>
  </body>
</html>