<%@page contentType="text/html" language="java" import="sia.menu.outlook.TableOutlook,sia.menu.outlook.MenuSection"%>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session" />
<jsp:useBean id="tree"        class="com.coolservlets.beans.TreeBean"  scope="session" />
<jsp:useBean id="util"        class="sia.libs.pagina.Funciones" scope="session"/>
<html>
	<head>
  	<meta http-equiv="Cache-Control" content="no-cache, no-store" />
	  <meta http-equiv="Pragma" content="no-cache, no-store" />
    <meta http-equiv="expires" content="-1" >
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
    </script>
	</head>
  <body onLoad="parent.loadSourceFinish('iconsMenu');">
        <%util.getHojaEstilo(out, request);%>
        <%
            MenuSection menuSection= new MenuSection(tree.getTree());
            int id     = request.getParameter("id")== null? 0: Integer.parseInt(request.getParameter("id"));
            String rama= request.getParameter("rama")== null? menuSection.getMask(): request.getParameter("rama");
            int columns= request.getParameter("columns")== null? 3: Integer.parseInt(request.getParameter("columns"));
            session.setAttribute("rama", rama);
            TableOutlook tableOutlook= new TableOutlook(rama, columns, tree.getTree());
            out.println(tableOutlook.render());
        %>
	</body>
</html>

