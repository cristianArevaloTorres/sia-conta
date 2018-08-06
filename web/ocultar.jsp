<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<html>
<head>
<title>Documento sin t&iacute;tulo</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%util.getHojaEstilo(out, request);%>
      <script type="text/javascript" language="JavaScript">
        function getFrame(fameName) {
          var obj= null;
          if (window.parent.document.all) {
            //alert( fameName + ' - ' + document.all(fameName) );
            obj = window.parent.document.all(fameName)
          } 
          else 
            if (window.parent.document.layers) {
              //alert( fameName + ' - ' + document.layers[fameName] );
              obj = window.parent.document.layers[fameName]
            } 
            else 
              if (window.parent.document.getElementById) {
                //alert( fameName + ' - ' + document.getElementById(fameName) );
                obj = window.parent.document.getElementById(fameName)
              }
          return obj;
        }

        function hiddenFrame() {
          var obj = getFrame('secundario');
          if(obj!= null)
        	  if (obj.cols=="219,10,*")
              obj.cols="1,10,*";
            else 
              obj.cols="219,10,*";
        }
      </script>
</head>
<body background="Librerias/Imagenes/fondo.gif" >
  <span id="staticcombo" style="position:absolute;left:0;top:250;"> 
     <input name="x" title="Ocultar frame" type="button" value="X" onClick="hiddenFrame();" class="boton">
  </span>
</body>
</html>
