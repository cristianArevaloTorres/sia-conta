<%@page contentType="text/html" language="java" import="sia.menu.outlook.TableOutlook,sia.menu.outlook.MenuSection"%>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session" />
<jsp:useBean id="tree"        class="com.coolservlets.beans.TreeBean"  scope="session" />
<jsp:useBean id="util"        class="sia.libs.pagina.Funciones" scope="session"/>
<html>
	<head>
  	<meta http-equiv="Cache-Control" content="no-cache, no-store" />
	  <meta http-equiv="Pragma" content="no-cache, no-store" />
    <meta http-equiv="expires" content="-1" >
    <script type="text/javascript" src="Librerias/Javascript/refrescarCapa.js">
    </script>
    <script type="text/javascript">
      function imageOver(layer, cuanto) {
        var ancho, alto;
        var object=document.getElementById("_"+layer);
        ancho=object.width;
        alto=object.height;
        object.width=cuanto*ancho;
        object.height=cuanto*alto;
      }
      
      function imageOut(layer, cuanto) {
        var ancho, alto;
        var object=document.getElementById("_"+layer);
        ancho=object.width;
        alto=object.height;
        object.width=ancho/cuanto;
        object.height=alto/cuanto;
      }

      function getColumns() {
        var width  = screen.width;
        var columns= 3;
        if (width <= 800)
            columns= 2;
        else
          if(width>= 800 && width< 1240)
            columns= 3;
          else
            columns= 4;
        return columns;    
      } // getColumns
      
      function paintIcons(id, rama) {
        loadSource('iconsMenu', null, 'iconsMenu.jsp', '?id='+ id+ '&rama='+ rama+ '&columns='+ getColumns());
      }
    </script>
	</head>
  <%
     MenuSection menuSection= new MenuSection(tree.getTree());
     String id  = request.getParameter("id")  == null? "0": request.getParameter("id");
     String rama= request.getParameter("rama")== null? menuSection.getMask(): request.getParameter("rama");
  %>            
  <body onLoad="paintIcons('<%= id%>', '<%= rama%>');">
    <%util.getHojaEstilo(out, request);%>
    <iframe id="bufferCurp" name="bufferCurp" style="display:none"></iframe>
    <div id="iconsMenu"></div>
	</body>
</html>

