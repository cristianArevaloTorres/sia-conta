<%@ page contentType="text/html;charset=ISO-8859-1"
    import="java.util.List"
    import="org.apache.commons.fileupload.*"
    import="org.apache.commons.fileupload.servlet.*"
    import="org.apache.commons.fileupload.disk.*"
    import="java.io.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>catalogoCuentas</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css"></link>
    <script type="text/javascript" language="javascript">
      function cerrar() {
        window.open('','_parent','');
        window.close();
      }
      
      function selecciona() {
        ihNombreArchivo = document.getElementById('fichero').value;
        ihAccion = document.getElementById('accion').value;
        ihRutaArchivo = document.getElementById('ruta').value;
        if(ihRutaArchivo.value!='') {
          window.opener.regresaSeleccionaArchivo(ihNombreArchivo,ihRutaArchivo,ihAccion);
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
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Procesar archivo", "Cargar", true);</jsp:scriptlet>    
<form id="form" method=post enctype=multipart/form-data>
<table width="50%" align="center">
    <tr>
    <td>Fichero:</td>
    <td><input type=file size="80" name=fichero id="fichero"></td>
    <!--</tr>
    <tr><td colspan=2><input type="submit" value="Cargar archivo" name="CargarArchivo">
    </td>
    </tr>-->
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
        <td><input type="submit" class="boton" value="Cargar archivo" onclick=" return cargarArchivo()"></td>
        <td><input type="button" class="boton" value="Cancelar" onclick="cerrar()"></td>

      </tr>
    </tbody>
  </table>
<% 
   String realizaAccion = request.getParameter("accion");
         switch (Integer.valueOf(realizaAccion)){
        case 0: realizaAccion ="carga";
          break;
        case 1: realizaAccion ="saldos";
          break;
        case 2: realizaAccion ="descripciones";
          break;
        case 3: realizaAccion ="eliminacion";
          break;
      }
            
   ServletRequestContext src = new ServletRequestContext(request);            
   String rutaDescarga = request.getServletContext().getRealPath("contabilidad//descargas//registroContable//clasificadorCuentas//".concat(realizaAccion));
   String nombreArchivo = "";
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
	  //out.println(item.getFieldName()+"<br>");
          System.out.println("Archivo: " + item.getFieldName());
	else{
	  file=new File(item.getName().substring(item.getName().lastIndexOf("\\")+1,item.getName().length()));
          if (!(destino.exists())) {
            //if(destino.isDirectory())
              destino.mkdirs();
          }
	  item.write(new File(destino,file.getName()));
          nombreArchivo = file.getName();
          //nombreArchivo = nombreArchivo.substring(0, nombreArchivo.lastIndexOf("."));
          System.out.println("Fichero subido");
	  //out.println("Fichero subido");
	} // end if
      } // end while
   } // end if
%>
<input type="hidden" name="archivo" id="archivo" value="<%=nombreArchivo%>">
<input type="hidden" name="ruta" id="ruta" value="<%=rutaDescarga%>">
<input type="hidden" name="accion" id="accion" value="<%=realizaAccion%>">
  </form>
  </body>
</html>