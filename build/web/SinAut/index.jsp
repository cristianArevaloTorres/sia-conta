<%@page contentType="text/html; charset=iso-8859-1" language="java"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<html>
<head>
<title>Sistema de Contabilidad</title>
<script language="JavaScript" src="../Librerias/Javascript/catalogos.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" >
  function validaAlfanumerico(objeto){
    if (objeto.value.length> 0)
    if ((!funcionObjeto(objeto.value, 'Q', 'cuenta')) && (objeto.value.length>0)) {
	  	  alert("Introducir sólo caracteres alfanuméricos");
		  	objeto.focus();
    }
  }
  
  function KeyPress(caracter) {
    var key = caracter.keyCode || caracter.which;
    if(key == 13)
      validaCamposLlenos();
  }
  
	function validaCamposLlenos(){
	  if (document.form1.cuentaSia.value.length>0 && document.form1.contraseniaSia.value.length>0){
		  document.form1.submit();
		}
    else{
		  alert("Error: Debe de introducir una cuenta y una contraseña !");
                  return false;x
		}
    return true;
	}
	
  function MM_reloadPage(init) {  //reloads the window if Nav4 resized
	  if (init) with (navigator) {
	    if ((appName=="Netscape") && (parseInt(appVersion)==4)) {
    	  document.MM_pgW=innerWidth;
		    document.MM_pgH=innerHeight;
		    onresize=MM_reloadPage;
	    }
	  }
    else {
	    if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) {
		    location.reload();
	    }
    }
  }
  
  MM_reloadPage(true);
	
  function irFramePrincipal() {
    if(parent!= null && parent.frames.length> 0) 
      window.parent.location='index.jsp'
  }; // irFramePrincipal
  
</script>
<%util.getHojaEstilo(out, request);%>
</head>
<body onLoad="irFramePrincipal();document.form1.cuentaSia.focus();">
<div id="titulo" style="position:absolute; width:200px; height:115px; z-index:1; left: 0px; top: 0px;">
<img src="<%=util.getContexto(request)%>/Librerias/Imagenes/titulosia2.gif" alt=""> 
</div>
<p>&nbsp; </p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<center>
<form action="<%= util.getContexto(request)%>/SinAut/Acceso.jsp" method="post" name="form1" onLoad="document.form1.cuentaSia.focus();">
  <table cellSpacing=0 cellPadding=0 width="30%" border=0 align="center">
  <tbody>
    <tr>
      <td bgColor=#4682b4><img height=1 src="../Librerias/Imagenes/pixel.gif" width=10 border=0 alt=""></td>
      <th noWrap bgColor=#4682b4><img height=16 src="../Librerias/Imagenes/login_sm.gif" width=17 border=0 alt="">&nbsp;Acceso al sitio&nbsp;<img height=1 src="../Librerias/Imagenes/pixel.gif" width=50 border=0 alt=""></th>
      <td><img height=21 src="../Librerias/Imagenes/tab_r.gif" width=10 border=0 alt=""></td>
      <td width="100%" background="../Librerias/Imagenes/line_t.gif">&nbsp;</td>
      <td background="../Librerias/Imagenes/line_t.gif"><img height=1 src="../Librerias/Imagenes/pixel.gif" width=10 border=0 alt=""></td>
    </tr>
    <tr>
      <td background="../Librerias/Imagenes/line_l.gif"><img src="../Librerias/Imagenes/pixel.gif" border=0 alt=""></td>
      <td colSpan=3><img height=10 src="../Librerias/Imagenes/pixel.gif" width=1 border=0 alt=""><br>
      <table cellSpacing=0 cellPadding=0 width="100%" border=0>
        <tbody>
        <tr>
          <td bgColor=#a0a0a0>
            <table cellSpacing=1 cellPadding=5 width="100%" border=0>
              <tbody>
              <tr bgColor=#dbeaf5>
                <td id=login_login width=65>Cuenta:</td>
                <td style="PADDING-RIGHT: 2px; PADDING-LEFT: 2px; PADDING-BOTTOM: 2px; PADDING-TOP: 2px">
                  <input type="text" name="cuentaSia" id="cuentaSia" class="cajaTexto" value="<%= request.getParameter("cuentaSia")== null? "": request.getParameter("cuentaSia")%>" onBlur="validaAlfanumerico(this)" onkeypress="">
								</td>
              </tr>
              <tr bgColor=#ffffff>
                <td id=login_password>Contrase&ntilde;a:</td>
                <td style="PADDING-RIGHT: 2px; PADDING-LEFT: 2px; PADDING-BOTTOM: 2px; PADDING-TOP: 2px" bgColor=#ffffff>
                <input type="password" name="contraseniaSia" id="contraseniaSia" class="cajaTexto" value="" onBlur="" onkeypress="KeyPress(event)">
								</td>
               </tr>
              </tbody>
            </table>
          </td>
        </tr>
      </tbody>
     </table>
       <img height=10 src="../Librerias/Imagenes/pixel.gif" width=1 border=0 alt=""><br>
    </td>
     <td background="../Librerias/Imagenes/line_r.gif"><img src="../Librerias/Imagenes/pixel.gif" border=0 alt=""></td>
  </tr>
  <tr>
    <td width=10><img height=20 src="../Librerias/Imagenes/tab_b.gif" width=10 border=0 alt=""></td>
    <td align=right bgColor=#4682b4 colSpan=4>
      <table cellSpacing=0 cellPadding=0 border=0>
        <tbody>
         <tr>
           <td class=btn width=100>
	         <input name=olvido_btn type=button class="btnform" id="olvido_btn" onclick="javascript:alert('Restablecer contraseña')" value="Restablecer" width="100" title="Restablecer contraseña"></td>
           <td width=1><img height=18 src="../Librerias/Imagenes/pixel.gif" width=1 border=0 alt=""></td>
           <td class=btn width=100> <input name=entrar_btn type=button class="btnform" id="entrar_btn" onclick="return validaCamposLlenos();" value="Ingresar" width="100"></td>
           <td width=1><img height=18 src="../Librerias/Imagenes/pixel.gif" width=1 border=0 alt=""></td>
         </tr>
        </tbody>
      </table>
     </td>
    </tr>
  </tbody>
  </table>
  <br>
  <table cellSpacing=1 cellPadding=1 width="70%" align=center border=0>
    <tbody>
      <tr>
        <td style="font-SIZE: 9pt" class="ta_justificado">Los datos personales recabados est&aacute;n protegidos por los cap&iacute;tulos IV de la Ley Federal de Transparencia y Acceso a la Informaci&oacute;n P&uacute;blica Gubernamental y VIII de su Reglamento. Se da cumplimiento as&iacute; al numeral d&eacute;cimo s&eacute;ptimo de los Lineamientos de Protecci&oacute;n de Datos Personales publicados por el Instituto Federal de Acceso a la Informaci&oacute;n P&uacute;blica en el Diario Oficial de la Federaci&oacute;n, el 30 de septiembre de 2005.</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td style="font-SIZE: 9pt" class="ta_justificado">Para acceder a la aplicaci&oacute;n, ingresa tu <strong>clave de usuario</strong> y <strong>contrase&ntilde;a</strong> previamente asignada </td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td style="font-SIZE: 9pt" align=left class="ta_justificado">Si tienes alg&uacute;n problema o comentario, comun&iacute;cate con el administrador del sistema para levantar tu reporte</td>
      </tr>
    </tbody>
  </table>
</form>
</center>
<div align="right">
<script language="JavaScript" type="text/javascript" >
  document.write('Aplicacion: ' + navigator.appName + '  -  version = ' + parseInt(navigator.appVersion) );
</script>
</div>
</body>
</html>
