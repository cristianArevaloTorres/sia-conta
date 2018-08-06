<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ include file="Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="crsArbol" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>Opciones asignadas</title>
    <link rel="stylesheet" type="text/css"
          href="Librerias/Javascript/cooltree/tree_styles.css"/>
    <script language="JavaScript" src="Librerias/Javascript/refrescarCapa.js" type="text/javascript">
    </script>
    <script language="JavaScript" src="Librerias/Javascript/tabla.js" type="text/javascript">
    </script>
    <script type="text/javascript" language="javascript">
      
    </script>
    <script type="text/javascript" language="javascript">
      
      
      function mostrar(padre) {
        var i = 0;
        var rows = document.getElementsByName(padre);
        for (i = 0; i < rows.length; i++)  {
          if(document.getElementsByName(getElemento(rows[i],'hijos').value).length == 0) {
            if(getElemento(rows[i],'ruta').value=='')
              getElemento(rows[i],'ima').src = 'Librerias/Imagenes/borrar.gif';
            else 
              getElemento(rows[i],'ima').src = 'Librerias/Imagenes/hoja.gif';
            getElemento(rows[i],'hoja').value = 1
          } else {
            if(getElemento(rows[i],'desplegado').value==1) {
              mostrar(getElemento(rows[i],'hijos').value);
              getElemento(rows[i],'ima').src = 'Librerias/Imagenes/abierto.gif';
            }
          }
          rows[i].style.display = '';
        }
      }
      
      
      function ocultar(padre) {
        var rows = document.getElementsByName(padre);
        if(rows.length > 0 && rows[0].style.display=='') {
          var i;
          for(i=0; i<rows.length; i++) {
            rows[i].style.display='none';
            if(getElemento(rows[i],'hoja').value!=1) {
              //getElemento(rows[i],'ima').src='Librerias/Imagenes/cerrado.gif';
              ocultar(getElemento(rows[i],'hijos').value);
            }
          }
        }
      }
      
      function desplegar(d,padre,tis) {
        //document.getElementById(d).style.display='';
        tis = tis.parentNode;
        var rows = document.getElementsByName(padre);
        if(rows.length > 0) {
          if(rows[0].style.display=='') {
            ocultar(padre);
            getElemento(tis,'ima').src='Librerias/Imagenes/cerrado.gif';
            getElemento(tis,'desplegado').value = 0;
          }
          else {
            mostrar(padre);
            getElemento(tis,'ima').src='Librerias/Imagenes/abierto.gif';
            getElemento(tis,'desplegado').value = 1;
          }
        }
      }
      
      function regresarDeResultado() {
        
      }
    </script>
    <script type="text/javascript" language="javascript">
      function cerrar() {
          window.open('','_parent','');
          window.close();
      }
      
      function aceptar() {
        document.getElementById("form1").action='s004OpcionesDelArbolControl.jsp';
        document.getElementById("form1").submit();
      }
    </script>
    <script language='javascript' type='text/javascript'>
      function sleep(millisegundos) {
        //var inicio = new Date().getTime();
        //while ((new Date().getTime() - inicio) < millisegundos){}
        //document.devCheater.sleep(milliSeconds); 
        var start = new Date().getTime();
          for (var i = 0; i < 1e7; i++) {
            if ((new Date().getTime() - start) > millisegundos){
              break;
            }
          }
      }
      
      function getFrame(fameName) {
          var obj= null;
          if (window.parent.document.all) {
            obj = window.parent.document.all(fameName)
          } 
          else 
            if (window.parent.document.layers) {
              obj = window.parent.document.layers[fameName]
            } 
            else 
              if (window.parent.document.getElementById) {
                obj = window.parent.document.getElementById(fameName)
              }
          return obj;
        }
        
        var cls = 219;

        function hiddenFrame() {
            var obj = getFrame('secundario');
            if(obj!=null) {
              //obj.cols="1,10,*";//TODO:
              /*cls--;
              if(cls>1)
                inter = window.setTimeout("hiddenFrame()",1);
              else
                {cls = 219;
              //document.getElementById("reloj").value = clearInterval(inter);
              window.clearInterval(inter);}*/
            }
        }
        var inter;
        
        function hide() {
          
          hiddenFrame();
          //for (i = 219; i > 0; i--)  {
            //inter = setTimeout("hiddenFrame()",5);
          //}
          
        }
        
        var dentro = false;
        
        function coordenadas(event) {
          x=event.clientX;
          y=event.clientY;
          if(parseInt(x) > 200 && dentro) 
            hide();
          if(parseInt(x) < 200)
            dentro = true;
        }
    </script>
  </head>
  <%
    crsArbol = Autentifica.getModulos();
    crsArbol.beforeFirst();
  %>
  <body onload="" onMouseMove="coordenadas(event);" bgcolor=''>
  <IFrame style="display:none" name="bufferCurp" id="bufferCurp">  </IFrame>
    <form name="form1" id="form1" action="" method="post">
      <table align="center" class="resultado" width="95%" id="tbPerfiles" bgcolor=''>
       <thead>
       </thead>
       <tbody>
        <tr>
          <td>
            <table width="100%" onmouseover="" > 
              <tbody> 
                <%int d=0;
                boolean isRuta=false;
                while(crsArbol.next()) { d++;
                  isRuta = crsArbol.getStr("ruta")!=null && !crsArbol.getStr("ruta").equals("");
                %>
                <tr style="cursor:pointer;<%=!crsArbol.getStr("Padre").equals("")?"display:none":""%>" id="<%=crsArbol.getStr("padre")%>" name="<%=crsArbol.getStr("padre")%>">
                  <td title="" nowrap="true" style="text-indent: <%=crsArbol.getInt("espacios")*5%>pt">
                    <input type="hidden" name="hijos" id="hijos" value="<%=crsArbol.getStr("clave")%>"></input>
                    
                    <input type="hidden" name="ruta" id="ruta" value="<%=crsArbol.getStr("ruta")%>"></input>
                    <a onclick="desplegar('usuariosResultado<%=d%>','<%=crsArbol.getStr("clave")%>',this)" style="text-decoration: none" <%=isRuta?"href='"+crsArbol.getStr("ruta")+"' target='siacuerpo'":""%>><img id='ima' name='ima' height="16px" width="16px" src='Librerias/Imagenes/<%=(crsArbol.getStr("imagen"))%>' <%=isRuta?"border='0'":""%>>&nbsp;<%=(crsArbol.getStr("descripcion"))%></a>
                    <div id="usuariosResultado<%=d%>" style="display:none"></div>
                    <input type="hidden" name="desplegado" id="desplegado" value="0"></input>
                  </td>
                </tr>
                <%}%>
              </tbody>
            </table>
          </td>
        </tr>
       </tbody>
      </table>
    </form>
  </body>
</html>