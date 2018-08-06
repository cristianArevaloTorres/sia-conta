<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
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
    <title>Catalogo de Cuentas</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'></link>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css"></link>
    <script type="text/javascript" language="javascript">

        //Cierra la ventana de seleccion de archivo para cargar al presionar el boton de cargar
        function cerrar() {
            window.open('','_parent','');
            window.close();
        }

        // Obtiene la informacion (nombre y ruta) del archivo seleccionado para su posterior 
        // extraccion del contenido y su carga en el sistema
        function selecciona() {
            ihNombreArchivo = document.getElementById('fichero').value;
            ihAccion = document.getElementById('accion').value;
            ihRutaArchivo = document.getElementById('ruta').value;
            if(ihRutaArchivo.value!='') {
                window.opener.regresaSeleccionaArchivo(ihNombreArchivo,ihRutaArchivo,ihAccion);
                cerrar();
            }
        }
      
        // Valida que el usuario realmente haya seleccionado un archivo correctamente para cargar,
        // en caso erroneo simplemente manda un mensaje de alerta.
        function cargarArchivo(){
            bandera = false;
            if((document.getElementById('fichero').value=='')||(document.getElementById('fichero').value==null)){
                alert('Seleccione un archivo.');
                bandera = false;
            } else {         
                bandera = true
            }
            
            return bandera;
        }
    
    </script>
  </head>
      
  <body id="bCargarArchivo" onunload="selecciona()">
        
      <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Seleccionar el archivo de Excel a cargar", "Registro de Pólizas", true);</jsp:scriptlet>      
      
          <form id="form" method=post enctype=multipart/form-data>
              <br><br>
              <table width="50%" align="center">  
                  <tr>
                      <td></td>
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
                          <td><input type="submit" class="boton" name="Cargar" value="Cargar archivo" onclick="return cargarArchivo();"></td>
                          <td><input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="window.open('../filtroGeneral.jsp?opcion=cargarPolizas&idCatalogoCuenta=1','_self');" ></td>
            
                      </tr>
                  </tbody>
              </table>
              <br><br>
              <b>Definición de cada uno de los campos del archivo a cargar:</b>
              <br><br>
              <table width="100%">
                  <tr>
                      <td><b>Nombre del campo</b></td>
                      <td><b>Descripción</b></td>
                      <td><b>Tipo</b></td>
                      <td><b>Longitud</b></td>
                      <td><b>Observaciones</b></td>
                  </tr>
                  <tr>
                      <td>Unidad</td>
                      <td>Es la unidad ejcutora dueña de la póliza</td>
                      <td>Texto</td>
                      <td>3</td>
                      <td></td>
                  </tr>
                  <tr>
                      <td>Ambito</td>
                      <td>Es la unión de los campos entidad mas ámbito</td>
                      <td>Texto</td>
                      <td>3</td>
                      <td>Dos caracteres para la enditad y el ambito debe de ser 1, 2 o 3</td>
                  </tr>
                  <tr>
                      <td>Numero_de_poliza</td>
                      <td>Es el número de póliza que la identifica y su tipo</td>
                      <td>Texto</td>
                      <td>6</td>
                      <td>El primer caracter debe de ser D, C, E o I</td>
                  </tr>
                  <tr>
                      <td>Numero_de_operacion</td>
                      <td>Número de operación a utilizar</td>
                      <td>Texto</td>
                      <td>2</td>
                      <td>Sólo números</td>
                  </tr>
                  <tr>
                      <td>Fecha_contable</td>
                      <td>Es la fecha de aplicación contable</td>
                      <td>Texto</td>
                      <td>10</td>
                      <td>Formato dd/mm/aaaa</td>
                  </tr>
                  <tr>
                      <td>Concepto</td>
                      <td>Es el concepto de la póliza</td>
                      <td>Texto</td>
                      <td>1000</td>
                      <td></td>
                  </tr>     
                  <tr>
                      <td>Fecha_de_alta</td>
                      <td>Fecha de solicitud de la carga de póliza</td>
                      <td>Texto</td>
                      <td>10</td>
                      <td>Formato dd/mm/aaaa</td>
                  </tr>   
                  <tr>
                      <td>Referencia_general</td>
                      <td>Es la referencia general de la póliza</td>
                      <td>Texto</td>
                      <td>500</td>
                      <td></td>
                  </tr>   
                  <tr>
                      <td>Cuenta_contable</td>
                      <td>Es la cuenta contable del detalle</td>
                      <td>Texto</td>
                      <td>33</td>
                      <td>Si es menor de 33 caracteres completar con ceros</td>
                  </tr>   
                  <tr>
                      <td>Debe_haber</td>
                      <td>Indica si el detalle es un debe o un haber</td>
                      <td>Texto</td>
                      <td>1</td>
                      <td>El caracter debe de ser D o H</td>
                  </tr>  
                  <tr>
                      <td>Importe</td>
                      <td>Es el importe del detalle</td>
                      <td>Texto</td>
                      <td>15</td>
                      <td>13 pociciones enteras y 2 decimales como máximo</td>
                  </tr>  
                  <tr>
                      <td>referencia_detalle</td>
                      <td>Es la referencia para cada detalle</td>
                      <td>Texto</td>
                      <td>255</td>
                      <td></td>
                  </tr> 
              </table>
              <br>
              <IMG SRC="../../../Librerias/Imagenes/Contabilidad/archivoExcel.png" width="100%" height="100%">
    
          <%
              ServletRequestContext src = new ServletRequestContext(request);
              // Establece la ruta del servidor por defecto en la cual se guardara temporalmente el archivo de excel
              // que el sistema tomara en cuenta para la carga de la informacion contable.
              String rutaDescarga = request.getServletContext().getRealPath("//contabilidad//descargas//registroContable//cargaDBF//");
              String nombreArchivo = "";
              
              // Abre un flujo de escritura necesario para guardar fisicamente el archivo cargado
              File destino = new File(rutaDescarga);
              
              // Verifica que realmente sea un archivo en que se pretende subir
              if (ServletFileUpload.isMultipartContent(src)) {
                  
                  // Construye el objeto que es capaz de parsear la petición de cargar de archivo
                  // estableciendo el tamaño por encima del cual sera escrito directamente en disco
                  DiskFileItemFactory factory = new DiskFileItemFactory((1024 * 1024), destino);
                  // Se crea el manejador del archivo
                  ServletFileUpload upload = new ServletFileUpload(factory);
                  List lista = upload.parseRequest(src);
                  File file = null;
                  
                  // Se realiza una iteraccion por cada uno de los archivos que se pretende cargar,
                  // en este caso solamente se realizara una unica vuelta
                  java.util.Iterator it = lista.iterator();
                  while (it.hasNext()) {
                       // Crea un objeto FileItem para recuperar la ruta completa
                      FileItem item = (FileItem) it.next();
                      if (item.isFormField()) //out.println(item.getFieldName()+"<br>");
                      {
                          System.out.println("Archivo: " + item.getFieldName());
                      } else {
                          // Se extrae solamenteel nombre del archivo, se descarta la ruta
                          file = new File(item.getName());
                          
                          // Valida que el archivo NO exista
                          if (!(destino.exists())) {
                              //if(destino.isDirectory())
                                // Construye el directo destino
                              destino.mkdirs();
                          }
                          
                          // Realiza la escritura fisica del archivo cargado en el servidor para su 
                          // posterior lectura y carga de la informacion contenida en el
                          item.write(new File(destino, file.getName()));
                          nombreArchivo = file.getName();
                          
                          //nombreArchivo = nombreArchivo.substring(0, nombreArchivo.lastIndexOf("."));
                          System.out.println("Fichero subido");
                          System.out.println("rutaDescarga:" + rutaDescarga);
                          System.out.println("nombreArchivo" + nombreArchivo);
                          //out.println("Fichero subido");
                      } // end if
                  } // end while
          %>
          <%
                  // Se establece en el objeto sesion la ruta y nombre de archivo, ya que esta informacion
                  // es necesario que este disponible para su posterior lectura.
                 session.setAttribute("ruta", rutaDescarga + "//" + nombreArchivo);

                  // Redirecciona a la pagina encargada de la lectura de la informacion contable
                  // contenida en el archivo cargado, realizando la afectacion correspondiente en
                  // la base de datos.
                  response.sendRedirect("c704cargaExc.jsp");
            
              } // end if
          %>
          <input type="hidden" name="archivo" id="archivo" value="<%=nombreArchivo%>">
          <input type="hidden" name="ruta" id="ruta" value="<%=rutaDescarga%>">
      </form>
  </body>
</html>