
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>
<%@ include file="../../../../Librerias/Funciones/utilscpv.jsp"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsFormas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsVariables" class="sia.db.sql.SentenciasCRS" scope="page"/>
<%
    try  {
      crsFormas.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"eventos.select.RfTcFormasContables-sinEventosAsignados.Formas");
      crsVariables.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"eventos.select.RfTcFunciones.Formas");
    } catch (Exception ex)  {
      ex.printStackTrace();
    }   
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c728EventosFormasAgregar</title>
    <link rel="stylesheet" href="../../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
    <script type="text/javascript" languaje="javascript">
      var tamanio= new Array();
      var padre = new Array();
      var ind=-1;
      
      function getElemento(objPadre,nombreHijo) {
        ind++;
        padre[ind] = objPadre; 
        nodos = padre[ind].childNodes;
        var nodo = objPadre;
        for(tamanio[ind]=0; tamanio[ind]<nodos.length && nodo.name!=nombreHijo; tamanio[ind]++) {
          if(nodos[tamanio[ind]].name==nombreHijo)
            nodo = nodos[tamanio[ind]];
          else {
            nodo = getElemento(nodos[tamanio[ind]],nombreHijo);
            nodos = padre[ind].childNodes;
          }
        }
        ind--;
        return nodo;
      }
      
      function beforeAgregar() {
        if(document.getElementById("idfuncion").selectedIndex != 0) {
          agrega();
        }
        else
          alert("Debe seleccionar un nombre de variable");
      }
      
      function agrega() {
        //alert('entra');
        tabla = document.getElementById("tbFunciones");
        //tabla.appendChild(document.getElementById("trbase").cloneNode(true));
        fila  = tabla.insertRow(tabla.rows.length);
        if(tabla.rows.length%2!=0)
          fila.className="resGrisClaro";
        fila.appendChild(document.getElementById("checkbase").cloneNode(true));
        fila.appendChild(document.getElementById("nomVariable").cloneNode(true));
        fila.appendChild(document.getElementById("tipVariable").cloneNode(true));
        fila.appendChild(document.getElementById("regContable").cloneNode(true));
        fila.appendChild(document.getElementById("tipRegla").cloneNode(true));
        
        document.getElementsByName("idFunBase")[tabla.rows.length-1].value = document.getElementById("idfuncion").value;
        
        document.getElementsByName("nomVariable")[tabla.rows.length-1].innerHTML = document.getElementById("idfuncion").options[document.getElementById("idfuncion").selectedIndex].text;
        document.getElementsByName("tipVariable")[tabla.rows.length-1].innerHTML = document.getElementById("nombreVariable").value;
        document.getElementsByName("regContable")[tabla.rows.length-1].innerHTML = document.getElementById("reglaContable").innerHTML;
        document.getElementsByName("tipRegla")[tabla.rows.length-1].innerHTML = document.getElementById("tipo").value;
        //alert(document.getElementById("idfuncion").value +'-'+ document.getElementsByName("idFunBase")[tabla.rows.length-1].value);
        
      }
      
      
      
      function llamaCapaVariable() {
       // alert(document.getElementById("idfuncion").selectedIndex);
        if(document.getElementById("idfuncion").selectedIndex != 0) {
          funcion = document.getElementById('idfuncion');
          loadSource('divTipoMeta',null,'c728EventosFormasCapaFunciones.jsp?','idfuncion='+funcion.value);
        }
      }
      
      function regresaDeFunciones(tipo, regla, nombre) {
        //alert(tipo+" - "+regla+" - "+nombre);
        document.getElementById('tipo').value = tipo;
        document.getElementById('reglaContable').innerHTML = regla;
        document.getElementById('nombreVariable').value = nombre;
      }
      
      function eliminarReg() {
       
        ids = document.getElementsByName("elBase");
        for (i = ids.length-1; i > 0; i--)  {
          if(ids[i].checked) {
            reg = ids[i].parentNode.parentNode;
            document.getElementById('tbFunciones').deleteRow(reg.rowIndex);
          }
        }
      }
      
      function validarFormulario() {
        
        if(document.getElementById("forma").selectedIndex == 0)
          alert("Debe seleccionar una forma");
        else {
          document.getElementById("form1").action = "c728EventosFormasControl.jsp";
          document.getElementById("form1").submit();
        }
        
          
      }
      
      function limpiar() {
        document.getElementById('idfuncion').selectedIndex = 0;
        document.getElementById('nombreVariable').value=''; 
        document.getElementById('tipo').value=''; 
        document.getElementById('reglaContable').innerHTML=''
      }
    </script>
  </head>
  <body>
    <form name="form1" id="form1" action="" method="post">
      <%util.tituloPagina(out,"Contabilidad","Agrega en grupo funciones a las formas contables","Agregar",true);%>
      <input type="hidden" name="accion" id="accion" value="1">
      <br>
      <IFrame style="display:none" name = "bufferCurp" id='bufferCurp'>
      </IFrame>
      <div id="dfuncion"></div>
      
      <table align="center" style="display:none" class="general">
        <thead></thead>
        <tbody>
          <tr id="trbase">
            <td id="checkbase" align="center"><input type="checkbox" name="elBase" id="elBase" value="">
                <input type="hidden" name="idFunBase" id="idFunBase" value=""></td>
            <td id="nomVariable" name="nomVariable"></td>
            <td id="tipVariable" name="tipVariable">Prueba b</td>
            <td id="regContable" name="regContable">Prueba c</td>
            <td id="tipRegla" name="tipRegla" align="center">Prueba d</td>
          </tr>
        </tbody>
      </table>
      
      <table cellpadding="5">
        <thead></thead>
        <tbody>
          <tr>
            <td>Formas autom&aacute;ticas</td>
            <td><select id="forma" name="forma" class="cajaTexto">
                  <option>-Seleccione-</option>
                  <%CRSComboBox(crsFormas, out,"forma","forma","");%>
                </select>
            </td>
          </tr>
          <tr>
            <td>Nombre de la variable</td>
            <td><select id="idfuncion" name="idfuncion" class="cajaTexto" onchange="llamaCapaVariable()">
                  <option>-Seleccione-</option>
                  <%CRSComboBox(crsVariables, out,"IDFUNCION","FUNCION_NOMBRE_VARIABLE","");%>
                </select>
            </td>
          </tr>
          <tr>
            <td>Tipo de la variable</td>
            <td><input type="text" class="cajaTexto" name="nombreVariable" id="nombreVariable" value="" readonly>
            </td>
            <td width="16px"></td>
            <td rowspan="2">Regla contable</td>
            <td rowspan="2"><textarea rows="3" cols="50" name="reglaContable" id="reglaContable" class="cajaTexto" readonly></textarea></td>
            <td></td>
          </tr>
          <tr>
            <td>Tipo regla contable</td>
            <td><input type="text" class="cajaTexto" name="tipo" id="tipo" value="" readonly>
            </td>
          </tr>
        </tbody>
      </table>
      <br>
      <hr class="piePagina">
      <table align="center">
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="button" class="boton" value="Agregar" onclick="beforeAgregar();document.getElementById('resetear').click() "></td>
            <td style="display:none"><input type="button" id="resetear" name="resetear" class="boton" value="Limpiar" onclick="limpiar()"></td>
          </tr>
        </tbody>
      </table>
      <br>
      <br>
      <table align="center" class="resultado" width="80%" id="tbFunciones">
        <thead>
          <tr>
            <th class="azulObs"><img src="../../../../Librerias/Imagenes/botonEliminar2.gif" onclick="eliminarReg()"></th>
            <th class="azulObs">Nombre de la variable</th>
            <th class="azulObs">Tipo de la variable</th>
            <th class="azulObs">Regla contable</th>
            <th class="azulObs">Tipo regla contable</th>
          </tr>
          
        </thead>
        <tbody>
        </tbody>
      </table>
      <br>
      <hr class="piePagina">
      <br>
      <table align="center" cellpadding="3">
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="button" class="boton" value="Aceptar" onclick="validarFormulario()"></td>
            <td><input type="submit" class="boton" value="Cancelar"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>