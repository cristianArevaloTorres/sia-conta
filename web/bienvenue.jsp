
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.beans.seguridad.Autentifica" %>
<%@ page import="java.util.List" %>
<%@ page import="sia.db.sql.Vista" %>
<%@ page import="sia.db.sql.SentennciasSE" %>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>
<%!
  private int getAreaContenedora(String area, String[] areas) {  
    int i;
    for (i = 1; i <= areas.length; i++)  {
      if(areas[i-1].indexOf(area) != -1)
        break;
    }
    return i;
  }

%>
<%
  SentennciasSE sen = null;
  Autentifica aut = null;
  List<Vista> mensajes = null;
  int cont = 0;
  String[] areas = {"|1|2|","",""};//{"|1|2|","|3|4|","|5|6|"}
  String[] imgs = {"recursosFinancieros.png","recursosHumanos.png","recursosMateriales.png"};
  try  {
    sen = new SentennciasSE(DaoFactory.CONEXION_SEGURIDAD);
    aut = (Autentifica)session.getAttribute("Autentifica");
    
    sen.addParam("cveGrupo",aut.getGrupoAcceso());
    sen.addParam("cvePerfil",aut.getPerfilAcceso());
    sen.addParam("numEmpleado",aut.getNumeroEmpleado());
    sen.addParam("idProceso","");
    
    mensajes = sen.registros(sen.getCommand("mensajitos.select.bienvenue"));
  } catch (Exception ex)  {
    ex.printStackTrace();
  } finally  {
  }
  
    
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>bienvenue</title>
    <link rel="stylesheet" href="Librerias/Estilos/siaexplorer.css" type="text/css">
    <script>
      var act=0;
      function siguiente() {
        //alert("entra");
        tds = document.getElementsByName("tdMen");
        act++;
        if(act >= tds.length)
          act=0;
        pon(act);
      }
    
      function pon(num) {
        act = num;
        objs = document.getElementById("mensaje"+num);
        document.getElementById("general").innerHTML = objs.innerHTML;
        tds = document.getElementsByName("tdMen");
        //alert(tds.length);
        for (i = 0; i < tds.length; i++)  {
          tds[i].className="azulObs";
        }
        tds[num].className='azulClaCen';
      }
    </script>
  </head>
  <body>
    <form name="form1" id="form1" action="" method="post">
      <%if(mensajes!=null && mensajes.size()!=0) { %>
      <table align="center" width="70%">
         <tbody>
           <tr>
             <td>
               <table align="center">
                 <tbody>
                   <tr>
       <% boolean pintado=false;
       String area;
       int areabase = 0;
       int areacontenedora=0;
        for(Vista mensaje:mensajes) {
        //while(mensajes.next()) {
          area = "|".concat(mensaje.getField("id_area")).concat("|");
          areacontenedora = getAreaContenedora(area,areas);
          if(areabase!=areacontenedora) {
            pintado=false; areabase=areacontenedora;
          }
          if(!pintado) {
            pintado=true;
          %><td width="" align="center"></td><%
          }
        }
       %>
                  </tr>
       
                  <tr>
                  
       <%
       //mensajes.beforeFirst();
       int contMensajes = 0;
       cont = 0;
       areabase = 0;
       areacontenedora=0;
        for(Vista mensaje:mensajes) {
        //while(mensajes.next()) {
          cont++;
          area = "|".concat(mensaje.getField("id_area")).concat("|");
          areacontenedora = getAreaContenedora(area,areas);
          if(areabase != areacontenedora) {
            if(areabase != 0) {
              %>
                </tr></table></td>
              <%
            }
            %>
            <td align="center"><table align="center"><tr><td>
            <%
            areabase=areacontenedora;
          }
          %>
            <%if(mensajes.size()>1) {%>
            <td class="<%=cont==1?"azulClaCen":"azulObs"%>" id="tdMen"  name="tdMen" onclick="pon(<%=cont-1%>)"><%=cont%></td>
            <%}%>
            <div id="mensaje<%=cont-1%>" style="display:none"><%=mensaje.getClob("mensaje")%></div>
          <%
          
        }
        //mensajes.absolute(1);
       %>
                    </td></table></tr>
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
        </tbody>
      </table>

       <br>
       <hr class="piePagina">
         
      <br>
      <table align="center">
        <tbody>
          <tr>
            <td  onclick="siguiente()">
            <div id="general">
        <%=mensajes.get(0).getClob("mensaje")%>
      
      </div>
            </td>
          </tr>
        </tbody>
      </table>
      <%} else {%>
      
       <br>
       <table align="center">
         <tbody>
           <tr>
             <td width="150px" align="center"><img src="Librerias/Imagenes/<%=imgs[0]%>" width="50px" height="50px"></td>
             <td width="150px" align="center"><img src="Librerias/Imagenes/<%=imgs[1]%>" width="50px" height="50px"></td>
             <td width="150px" align="center"><img src="Librerias/Imagenes/<%=imgs[2]%>" width="50px" height="50px"></td>
           </tr>
         </tbody>
       </table>
       <hr class="piePagina">
       <br>
       <br>
      <table align="center">
        <tbody>
          <tr>
            <td><span style="font-size: 12pt; font-family: &quot;Trebuchet MS&quot;,&quot;sans-serif&quot;; color: rgb(31, 73, 125); font-style: italic;">&iexcl;&iexcl; No existen mensajes para mostrar !!</span></td>
          </tr>
        </tbody>
      </table>
      <% } %>
    </form>
  </body>
</html>

