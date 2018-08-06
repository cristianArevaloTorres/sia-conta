
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesCuentasInversionMenu</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script type="text/javascript" language="javascript">
      function cambia(ima) {
        imgs = document.getElementsByTagName('img');
        for(i=0; i<imgs.length; i++) {
          imgs[i].src="../../Librerias/Imagenes/pelota_azuloscuro.gif";
        }
        imgs[ima].src="../../Librerias/Imagenes/pelota_azulclaro.gif";
      }
    </script>
  </head>
  <body  topmargin='0' leftmargin='0'>
<div align="center">

	<table width="80%" border="0" cellspacing="0">
      <thead></thead>
      <tr>
      <td width="15%">
        <div align="center"><font color="#0066CC"><strong><img src="../../Librerias/Imagenes/pelota_azulclaro.gif" width="14" height="16" align="texttop">
          &nbsp;<a href='tesAgregarCuentasInversion.jsp' target="contenido" onclick="cambia(0)">Agregar</a>&nbsp;</strong></font></div>
        <div align="center"><font color="#0066CC"></font></div></td>
      <td width="15%">
        <div align="center"><font color="#0066CC"><strong><img src="../../Librerias/Imagenes/pelota_azuloscuro.gif"  width="14" height="16" align="texttop">
          &nbsp;<a href='tesFiltroCuentasInversion.jsp?arbol=1' target="contenido" onclick="cambia(1)">Modificar</a>&nbsp;</strong></font></div></td>
      
    </tr>
  </table>
  <hr align="center" size="2">
</div>

</body>
</html>