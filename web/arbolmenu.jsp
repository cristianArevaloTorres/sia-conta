<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*,java.util.*,java.io.*" errorPage=""%>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/> 
<jsp:useBean id="util"       class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="tree"        class="com.coolservlets.beans.TreeBean"  scope="session" />
  <jsp:setProperty name="tree" property="key" value="CLAVE" />
  <jsp:setProperty name="tree" property="url" value="RUTA" />
  <jsp:setProperty name="tree" property="text" value="DES" />
  <jsp:setProperty name="tree" property="imageIn" value="IMAGEN_IN" />
  <jsp:setProperty name="tree" property="imageOut" value="IMAGEN_OUT" />
  <jsp:setProperty name="tree" property="imageOver" value="IMAGEN_OVER" />
  <jsp:setProperty name="tree" property="children" value="2" />
  <jsp:setProperty name="tree" property="treePage" value="arbolmenu.jsp" />
  <jsp:setProperty name="tree" property="leafTarget" value="siacuerpo" />
  <jsp:setProperty name="tree" property="openImage"   value='<%=util.getContexto(request)+ "/Librerias/Imagenes/abierto.gif"%>'/>
  <jsp:setProperty name="tree" property="closedImage" value='<%=util.getContexto(request)+ "/Librerias/Imagenes/cerrado.gif"%>'/>
  <jsp:setProperty name="tree" property="leafImage" value='<%=util.getContexto(request)+ "/Librerias/Imagenes/hoja.gif"%>' />
  <jsp:setProperty name="tree" property="cuenta" value="<%=Autentifica.getLogin()%>" />
  <jsp:setProperty name="tree" property="*" />
<html>
<head>
<title>SIA (Sistema Integral de Administración)</title>
<%util.getHojaEstilo(out, request);%>
</head>
<body>
  <table cellSpacing=0 cellPadding=0 width="100%" border=0 align="center">
  <tbody>
    <tr>
      <td bgColor=#4682b4><img height=1 src="Librerias/Imagenes/pixel.gif" width=10 border=0></td>
      <th noWrap bgColor=#4682b4><img height=16 src="Librerias/Imagenes/usuario.gif" width=17 border=0>&nbsp;Men&uacute;&nbsp;<img height=1 src="Librerias/Imagenes/pixel.gif" width=50 border=0></th>
      <td><img height=21 src="Librerias/Imagenes/tab_r.gif" width=10 border=0></td>
      <td width="100%" background="Librerias/Imagenes/line_t.gif">&nbsp;</td>
      <td background="Librerias/Imagenes/line_t.gif"><img height=1 src="Librerias/Imagenes/pixel.gif" width=10 border=0></td>
    </tr>
    <tr>
      <td background="Librerias/Imagenes/line_l.gif"><img src="Librerias/Imagenes/pixel.gif" border=0></td>
      <td colSpan=3><img height=10 src="Librerias/Imagenes/pixel.gif" width=1 border=0><br>
      <table cellSpacing=0 cellPadding=0 width="100%" border=0>
        <tbody>
        <tr>
          <td bgColor=#a0a0a0>
            <table cellSpacing=1 cellPadding=5 width="100%" border=0>
              <tbody>
              <tr bgColor=#ffffff>
                <td id=login_password><%=tree.renderHTML(Autentifica.getModulos())%></td>
               </tr>
              </tbody>
            </table>
          </td>
        </tr>
      </tbody>
     </table>
       <img height=10 src="Librerias/Imagenes/pixel.gif" width=1 border=0><br>
          </td>
     <td background="Librerias/Imagenes/line_r.gif"><img src="Librerias/Imagenes/pixel.gif" border=0></td>
  </tr>
  <tr>
    <td width=10><img height=20 src="Librerias/Imagenes/tab_b.gif" width=10 border=0></td>
    <td align=right bgColor=#4682b4 colSpan=4>
      <table cellSpacing=0 cellPadding=0 border=0>
        <tbody>
         <tr>
           <td width=100>&nbsp;</td>
         </tr>
        </tbody>
      </table>
     </td>
    </tr>
  </tbody>
  </table>
<br>
<br>
<% if (Autentifica.getTipo()== 1) { %>
 <strong>url:</strong><%= util.getInstanciaBD()%>
 <br>
<% } // if %>
</body>
</html>