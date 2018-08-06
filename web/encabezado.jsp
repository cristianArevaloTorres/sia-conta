<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/> 
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<html>
<head>
<title>Encabezado del Sistema Integral de Administraci&oacute;n</title>
<script language="JavaScript" type="text/javascript" src="Librerias/Javascript/refrescarCapa.js"></script>
<script language="JavaScript" type="text/javascript">

 function fecha(){
    meses = new Array ("enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre");
	 data = new Date();
	 index = data.getMonth();
	 var an=data.getYear();
	 if (an<1000){
		 an = 1900+an;
	 }
	 var Hora;
	 var Minutos;
   var Segundos;
	 var AmPm;
	 Hora = data.getHours();
	 if (Hora >= 12) {
		 AmPm = " p.m.";
	 }else {
		 AmPm = " a.m.";
	 }
   if (Hora > 12) {
		 Hora -= 12;
	 }
	 if (Hora == 0) {
		 Hora = 12;
	 }
	 Minutos = data.getMinutes();
	 if (Minutos < 10) {
		 Minutos = "0" + Minutos;
	 }

	 Segundos = data.getSeconds();
	 if (Segundos < 10){
	   Segundos = "0" + Segundos;
	 }
        texto="Hoy es "+ data.getDate()+ " de " + meses[index] +" de " + an+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

	 if (Minutos=="00"){texto+="<font color=00FF00>";}
	 else {texto+="<font color=YELLOW>";}
	 texto+=Hora+":"+Minutos+":"+Segundos+" "+AmPm;
	 texto+='<' + '/font>';

        getObjeto('reloj').innerHTML = texto;
	 if (document.layers){
         	alert(texto);
	   document.layers.reloj.document.write(texto);
		 document.layers.reloj.document.close();
	}else if (document.all){
		//alert(texto);
		reloj.innerHTML = texto;
	}
  setTimeout("fecha()", 1000);
}
</script>
<style type="text/css">
<!--
table {
	font-weight: bold;
	color: #FFFFFF;
}
-->
</style>
<%util.getHojaEstilo(out, request);%>
</head>
<body>
<table border="0" cellpadding="0" cellspacing="0" bgcolor="navy">
  <tr>
    <td width="26%" height="20">
<%
		if (Autentifica.getGenero().equals("2")) {
			out.print(" Bienvenida ");
		}else{
			out.print(" Bienvenido ");
		}
		out.print(Autentifica.getNombre());
                String imagenArbol = util.getContexto(request).concat("/Librerias/Imagenes/encabezado/arbol.gif");
                String imagenOutlook = util.getContexto(request).concat("/Librerias/Imagenes/encabezado/outlook.gif");
%>
    </td>
        <td width="26%">
            <span id="reloj" style="position:relative"></span>
  	</td>
        <td width="10%" align="center">
            <a href="<%=request.getContextPath()%>/contenedor.jsp?menu=arbol" target="_top" style="color:white; text-decoration:none;">
                <img src="<%=imagenArbol%>" alt="Menu" border="0"/>
            </a>         
            <a href="<%=request.getContextPath()%>/bienvenue.jsp" target="siacuerpo" class="imagen" 
               style="color:white; text-decoration:none;">            
                <img src="<%=util.getContexto(request)%>/Librerias/Imagenes/encabezado/mensaje.gif" alt="Mensaje" border="0"/>
            </a>                
            <a href="<%=util.getContexto(request)%>/Librerias/Funciones/CerrarSesion.jsp" class="imagen"
               target="_top" style="color:white; text-decoration:none;" >
                <img src="<%=util.getContexto(request)%>/Librerias/Imagenes/encabezado/cerrar.gif" alt="Cerrar sesi&oacute;n" border="0"/>
            </a>
        </td>
        <td width="30%" align="right">          
           &nbsp;&nbsp;&nbsp;           
    </td>
  </tr>
  <tr>
    <td colspan="4"><img src="<%=util.getContexto(request)%>/Librerias/Imagenes/titulosia2.gif"></td>
  </tr>
</table>
<!--/span-->
<script>fecha();</script>
<form name="datos" id="datos" method="post" action="">
  <IFRAME STYLE="display:none" NAME="bufferCurp" id="bufferCurp"></IFRAME>
	<div id="capaCerrar"></div>
</form>
<!--span id="staticcombo" style="position:absolute;left:0;top:0;"--> 
</body>
</html>
