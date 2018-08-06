<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*"%>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<html>
<head>
<title>SIA error de acceso</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language="JavaScript" type="text/javascript" src="../Javascript/catalogos.js"></script>
</script>
<script language="JavaScript" type="text/javascript">
  function irFramePrincipal(){
    if(parent!= null && parent.frames.length> 0){
      window.parent.location='<%=sia.libs.pagina.Funciones.getContexto(request)%>/index.jsp';
    }
  }//end function irFramePrincipal
</script>
<%util.getHojaEstilo(out, request);%>
</head>
<%
String error      = request.getParameter("error");
String loginError = request.getParameter("loginError");
if (loginError==null)
    loginError= "";

if (error== null)
  error= "No tiene acceso a esta página";
else
  error= error.substring(error.lastIndexOf("]")+ 1);
%>
<body onload="irFramePrincipal(); document.form1.cuentaSia.focus();">
<div id="capa" style="position:absolute; width:200px; height:115px; z-index:1; left: 0px; top: 0px;">
<image src="<%=util.getContexto(request)%>/Librerias/Imagenes/enc_PA.gif">
</div>
<p>&nbsp;</p>
<p>&nbsp;</p>
<br>
<br>
<br>
<br>
<form  name="form1" id="form1" action="<%= util.getContexto(request)%>/index.jsp" method="post">
  <input name="cuentaSia" id="cuentaSia" type="hidden" value="<%= loginError%>">
  <table cellSpacing=0 cellPadding=0 width="55%" border=0 align="center">
  <tbody>
  <tr>
    <td bgColor=#4682b4><img height=1
      src="<%= util.getContexto(request)%>/Librerias/Imagenes/pixel.gif"
      width=10 border=0></td>
    <TH noWrap bgColor=#4682b4>Error<img height=1
      src="<%= util.getContexto(request)%>/Librerias/Imagenes/pixel.gif"
      width=50 border=0></TH>
    <td><img height=21 src="<%= util.getContexto(request)%>/Librerias/Imagenes/tab_r.gif" width=10
      border=0></td>
    <td width="100%"
    background="<%= util.getContexto(request)%>/Librerias/Imagenes/line_t.gif">&nbsp;</td>
    <td background="<%= util.getContexto(request)%>/Librerias/Imagenes/line_t.gif"><img height=1
      src="<%= util.getContexto(request)%>/Librerias/Imagenes/pixel.gif"
      width=10 border=0></td></tr>
  <tr>
    <td background="<%= util.getContexto(request)%>/Librerias/Imagenes/line_l.gif"><img
      src="<%= util.getContexto(request)%>/Librerias/Imagenes/pixel.gif"
      border=0></td>
    <td colSpan=3><img height=10
      src="<%= util.getContexto(request)%>/Librerias/Imagenes/pixel.gif"
      width=1 border=0><br>
      <DIV id=form_error style="DISPLAY: none"></DIV>
      <table cellSpacing=0 cellPadding=0 width="100%" border=0>
        <tbody>
        <tr>
          <td bgColor=#a0a0a0>
            <table cellSpacing=1 cellPadding=5 width="100%" border=0>
              <tbody>
              <tr bgColor="#dbeaf5">
                <td width="35%" align="right">
								 Cuenta de acceso:
								</td>
                <td style="PADDING-RIGHT: 2px; PADDING-LEFT: 2px; PADDING-BOTTOM: 2px; PADDING-TOP: 2px">
								  <font color="#003399"><strong><%= loginError%></strong></font>
								 </td>
						  </tr>		
              <tr bgColor=#ffffff>
                <td colspan="2" align="center" style="PADDING-RIGHT: 2px; PADDING-LEFT: 2px; PADDING-BOTTOM: 2px; PADDING-TOP: 2px" bgColor=#ffffff>
								<br>
								<br>
								<br>
								<font color="#FF0000"><strong><%= error%></strong></font>
								<br>
								<br>
								<br>
                </td>
							</tr>
						</tbody>
					</table></td></tr></tbody></table><img
      height=10 src="<%= util.getContexto(request)%>/Librerias/Imagenes/pixel.gif" width=1
    border=0><br></td>
    <td background="<%= util.getContexto(request)%>/Librerias/Imagenes/line_r.gif"><img
      src="<%= util.getContexto(request)%>/Librerias/Imagenes/pixel.gif"
      border=0></td></tr>
  <tr>
    <td width=10><img height=20 src="<%= util.getContexto(request)%>/Librerias/Imagenes/tab_b.gif"
      width=10 border=0></td>
    <td align=right bgColor=#4682b4 colSpan=4>
      <table cellSpacing=0 cellPadding=0 border=0>
        <tbody>
        <tr>
          <td width=100>&nbsp;</td>
          <td width=1><img height=18
            src="<%= util.getContexto(request)%>/Librerias/Imagenes/pixel.gif" width=1 border=0></td>
          <td class=btn width=100><input name=accion_btn type="submit" class=btnform id="accion_btn" value="Regresar" width="100"></td>
          <td width=1><img height=18
            src="<%= util.getContexto(request)%>/Librerias/Imagenes/pixel.gif" width=1
        border=0></td></tr></tbody></table></td></tr></tbody></table>
</form>
<br>
<br>
<br>
      <table cellSpacing=0 cellPadding=2 width="100%" border=0>
        <tbody>
        <tr>
          <td style="color: white" noWrap class=header1><img height=18
            src="../Imagenes/pixel.gif" width=1 align=absMiddle
            border=0> Atenci&oacute;n a usuarios <A style="COLOR: white"
            href="http://www.senado.gob.mx/">INEGI</A>. ©2000 <A style="COLOR: white"
            href="http://intranet.sia.senado.gob.mx/">SIA.</A>
        </td></tr></tbody></table></td></tr></tbody></table>
</body>
</html>
