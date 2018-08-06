<%@page contentType="text/html" language="java" import="sia.menu.outlook.MenuSection, java.util.*"%>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session" />
<jsp:useBean id="tree"        class="com.coolservlets.beans.TreeBean"  scope="session" />
<jsp:useBean id="util"       class="sia.libs.pagina.Funciones" scope="session"/>
<html>
	<head>
		<title></title>
  	<meta http-equiv="Cache-Control" content="no-cache, no-store" />
	  <meta http-equiv="Pragma" content="no-cache, no-store" />
    <meta http-equiv="expires" content="-1" >
      <script type="text/javascript" language="JavaScript">
        function getFrame(fameName) {
          var obj= null;
          if (window.parent.document.all) {
            //alert( elemID + ' - ' + document.all(fameName) );
            obj = window.parent.document.all(fameName)
          } 
          else 
            if (window.parent.document.layers) {
              //alert( elemID + ' - ' + document.layers[fameName] );
              obj = window.parent.document.layers[fameName]
            } 
            else 
              if (window.parent.document.getElementById) {
                //alert( elemID + ' - ' + document.getElementById(fameName) );
                obj = window.parent.document.getElementById(fameName)
              }
          return obj;
        }

        function resizeFrame() {
          var obj = getFrame('control');
          
          var width= 0, height= 18;
          if(typeof( window.innerWidth )== 'number') {
            //Non-IE
            width = window.innerWidth;
            height= window.innerHeight;
          } 
          else 
            if(document.documentElement && (document.documentElement.clientWidth || document.documentElement.clientHeight)) {
              //IE 6+ in 'standards compliant mode'
              width = document.documentElement.clientWidth;
              height= document.documentElement.clientHeight;
            } 
            else 
              if(document.body && (document.body.clientWidth || document.body.clientHeight)) {
                //IE 4 compatible
                width = document.body.clientWidth;
                height= document.body.clientHeight;
              }     
          if(obj!= null) {
            obj.rows= document.body.scrollHeight+ ",*";
            // alert("document.body.offsetHeight: "+ document.body.offsetHeight);     
            // alert("document.body.scrollHeight: "+ document.body.scrollHeight);     
            // alert("Objeto   : "+ obj.rows);     
          } // if  
        }
        
        window.onload= resizeFrame;
    </script>
  </head>
	<body><%util.getHojaEstilo(out, request);%>
		<table cellspacing="0" cellpadding="0" border="0" width="100%" height="100%">
			<tr align="left" valign="middle">
				<td style="font: 5pt arial;">
          <%MenuSection menuSection= new MenuSection(tree.getTree());%>
					<%= menuSection.paintSection(session)%>
				</td>
			</tr>
		</table>
	</body>
</html>
