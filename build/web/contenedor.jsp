<%@page contentType="text/html" language="java" import="sia.menu.Menu" errorPage="" %>
<html>
<head>
     <script type="text/javascript" language="javascript">
        function refreshLinks(name, page) {
          var seguimiento= null;
          for(var x= 0; x< frames.length && seguimiento== null; x++) {
            if(frames[x].name== name)
              seguimiento= frames[x];
          } // for  
          if(seguimiento!= null)
            seguimiento.location.href= page;          
        } // refreshLinks     
        
        function refresh_() {
          setTimeout("refreshLinks('seguimiento', 'seguimiento.jsp')", 250);
        }  
        
         function ocultarFrame(ocultar){
         var marco = document.getElementById('control');
           if (marco != null){
               if (ocultar) 
                   marco.rows = '0,*';
               else
                   marco.rows = '18,*';
           }            
        }

     </script>
<title>SIA (Sistema Integral Administrativo)</title>
</head>
<%
    Menu menu = null;
    if (session.getAttribute("menu")!=null && session.getAttribute("menu") instanceof Menu)
        menu = (Menu)session.getAttribute("menu");
    else
        menu = new Menu(session);
        
    String strMenu = request.getParameter("menu");
    if (strMenu!=null){
        if (strMenu.equals("arbol"))
            menu.setTipoMenu(Menu.Tipo.ARBOL);
        if (strMenu.equals("outlook"))
            menu.setTipoMenu(Menu.Tipo.OUTLOOK);    
    }
    menu.setTipoMenu(Menu.Tipo.ARBOL);
    /*if (!pbLanzador.isActivo()){ 
      pbLanzador.setTiempo(43200);// segundos
    //  pbLanzador.setTiempo(60);// segundos
      pbLanzador.iniciar();
    }*/
%>
<frameset name="principal" id="principal" rows="100,*" cols="*" border="0" bordercolor="#000066">
  <frame name="siatitulo" id="siatitulo" src="<%=request.getContextPath()%>/encabezado.jsp" scrolling="no" noresize marginwidth="0" marginheight="0">
  <frameset name="secundario" id="secundario" cols="219,10,*" border="0" bordercolor="#000066">
    <frame name="siamenu" id="siamenu" src="<%=request.getContextPath()%>/<%=menu.getPagina()%>" scrolling="<%=menu.getNavegacion()%>" noresize marginwidth="0" marginheight="0">
    <frame name="ocultar" id="ocultar" src="<%=request.getContextPath()%>/ocultar.jsp" class="Arbol" scrolling="no" noresize>
     <frameset id="control" name="control" rows="18,*">
        <frame id="seguimiento" name="seguimiento" src="<%=request.getContextPath()%>/seguimiento.jsp" marginwidth="0" marginheight="0" onLoad="ocultarFrame(<%=menu.isOcultarSeguimiento()%>)">
        <frame id="siacuerpo" name="siacuerpo" src="<%=request.getContextPath()%>/bienvenue.jsp" scrolling="auto" onLoad="refresh_()" noresize>
        <noframes>
          <body>
            <p>Esta pagina usa marcos, pero su explorador no los admite.</p>
          </body>
        </noframes> 
     </frameset>
  </frameset>	    
</frameset>
</html>
