<%@page contentType="text/html" language="java" import="sia.menu.outlook.MenuOutLook"%>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session" />
<jsp:useBean id="util"       class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="tree"        class="com.coolservlets.beans.TreeBean"  scope="session" />
<jsp:setProperty name="tree" property="key" value="CLAVE"/>
<jsp:setProperty name="tree" property="url" value="RUTA"/>
<jsp:setProperty name="tree" property="text" value="DES"/>
<jsp:setProperty name="tree" property="imageIn" value="IMAGEN_IN" />
<jsp:setProperty name="tree" property="imageOut" value="IMAGEN_OUT" />
<jsp:setProperty name="tree" property="imageOver" value="IMAGEN_OVER" />
<jsp:setProperty name="tree" property="children" value="2"/>
<jsp:setProperty name="tree" property="treePage" value=""/>
<jsp:setProperty name="tree" property="leafTarget" value="siacuerpo"/>
<jsp:setProperty name="tree" property="openImage" value='<%=util.getContexto(request) + "/Librerias/Imagenes/abierto.gif"%>'/>
<jsp:setProperty name="tree" property="closedImage" value='<%=util.getContexto(request) + "/Librerias/Imagenes/cerrado.gif"%>'/>
<jsp:setProperty name="tree" property="leafImage" value='<%=util.getContexto(request) + "/Librerias/Imagenes/hoja.gif"%>'/>
<jsp:setProperty name="tree" property="cuenta" value="<%=Autentifica.getLogin()%>"/>
<jsp:setProperty name="tree" property="*"/>
<html>
    <head>
        <script type="text/javascript" src="Librerias/Javascript/Outlook/outlookpro.js"></script>
        <script type="text/javascript">
            var n=navigator.appName;
            var netscape= n== "Netscape";
            var explorer= n== "Microsoft Internet Explorer";
 
            function getHeightPanel() {
                var regresar= '35';
                if (netscape)
                    regresar= '25';
                return regresar;
            }  
    
            function getHeightItem() {
                var regresar= '92';
                if (netscape)
                    return '84';
                return regresar;
            }
      
            function getTextPanelNormal() {
                return '<div  style="background: right top url(Librerias/Imagenes/Outlook/panel_right_r.gif);"><div style="background: left top no-repeat url(Librerias/Imagenes/Outlook/panel_left_r.gif); padding: 10px 13px 0 13px;  font: bold 9pt arial; text-align: left; height:'+ getHeightPanel()+ 'px;">{text}</div></div>';
            }
 
            function getTextItemNormal() {
                return '<div style="background: top right url(Librerias/Imagenes/Outlook/item_bg_right_r.gif);"><div style="background: top left no-repeat url(Librerias/Imagenes/Outlook/item_bg_left_r.gif); height:'+ getHeightItem()+ 'px; text-align: center; padding-top: 9px;"><img src="{image}" width="32" height="32" /><table align="center" cellspacing="0" cellpading="0" border="0" height="50"><tr><td align="center" valign="middle" style="font: 8pt arial;">{text}</td></tr></table></div></div>';
            }
 
            function getTextItemRollOvered() {
                return '<div style="background: top right url(Librerias/Imagenes/Outlook/item_bg_right.gif);"><div style="background: top left no-repeat url(Librerias/Imagenes/Outlook/item_bg_left.gif); height:'+ getHeightItem()+ 'px; text-align: center; padding-top: 9px;"><img src="{image}" width="32" height="32" /><table align="center" cellspacing="0" cellpading="0" border="0" height="50"><tr><td align="center" valign="middle" style="font: 8pt arial;">{text}</td></tr></table></div></div>';
            }
            <%
                //tree.populateJdbc(Autentifica.getModulos());
                //MenuOutLook menuOutlook= new MenuOutLook(tree.getTree());
                //out.println(menuOutlook.render());
%>
        </script> 
    </head>
    <body marginwidth="0" marginheight="0">
        <script type="text/javascript">
            new COOLjsOutlookBarPRO(OutlookBar);
        </script>
    </body>


</html>

