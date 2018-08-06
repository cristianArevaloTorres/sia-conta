<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<jsp:useBean id="crsTasas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesTablaTasaCapa</title>
     <script language="JavaScript"  type="text/javascript">
   
       function validarFlotante(event,campo){
         tecla = (document.all) ? event.keyCode : event.which; 
         if(tecla!=46  && tecla !=8 && tecla !=0 && (tecla < 48 || tecla > 57 )) {
           (document.all) ? event.returnValue = false : event.preventDefault();
         }      
         if(tecla == 46){
           var punto = campo.value.indexOf(".",0)
           if (punto != -1){ 
             (document.all) ? event.returnValue = false : event.preventDefault();
           }
         }         
       }
   
    </script>
  </head>
  <body onload="parent.loadSourceFinish('tasasResultado')">
    <%
      String fechaConsulta           =  request.getParameter("fecha");
      sia.libs.periodo.Fecha regresa = null;
      String [] renglonesT = null;
      String [] columnasT = {" "," "," "," "," "};
      int datosCap = 0;
   
      if (!fechaConsulta.equals("")){
        crsTasas.addParam("dia",fechaConsulta);      
      }
      crsTasas.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrTasasRendimientoBancosCotizacion.reportesInversiones");  
    %>
      <table align="left">
       <thead>
         <tr class="azulObs">
           <th class="azulObs" >Institución bancaria</th>
           <th class="azulObs">1</th>
           <th class="azulObs">semana</th>
           <th class="azulObs">mes</th>
           <th class="azulObs">ene-mes actual</th>
         </tr>
         <%
          if (request.getParameter("tablaHist")==null) {
            while (crsTasas.next()){
         
         %>
             <tr >
               <td id='<%=crsTasas.getStr("ID_BANCO")%>'><%=crsTasas.getStr("INSTITUCION_FINANCIERA")%></td>
               <td><input type="text" name="valor<%=crsTasas.getStr("ID_BANCO")%>" id="valor<%=crsTasas.getStr("ID_BANCO")%>" class="cajaTexto" size="15" onkeypress="validarFlotante(event,this)"></td>
               <td><input type="text" name="valor<%=crsTasas.getStr("ID_BANCO")%>" id="valor<%=crsTasas.getStr("ID_BANCO")%>" class="cajaTexto" size="15" onkeypress="validarFlotante(event,this)"></td>
               <td><input type="text" name="valor<%=crsTasas.getStr("ID_BANCO")%>" id="valor<%=crsTasas.getStr("ID_BANCO")%>" class="cajaTexto" size="15" onkeypress="validarFlotante(event,this)"></td>
               <td><input type="text" name="valor<%=crsTasas.getStr("ID_BANCO")%>" id="valor<%=crsTasas.getStr("ID_BANCO")%>" class="cajaTexto" size="15" onkeypress="validarFlotante(event,this)"></td>
             </tr>
         <%  }
         }
         else { 
            renglonesT = request.getParameter("tablaHist").split("\\|");
            for (int i = 0; i < renglonesT.length; i++)  { 
                crsTasas.next();
         %>
             <tr >
               <td id='<%=crsTasas.getStr("ID_BANCO")%>'><%=crsTasas.getStr("INSTITUCION_FINANCIERA")%>
               </td>
            <%
                columnasT = renglonesT[i].replaceAll(",",", ").split(",");
                for (int j = 0; j < 4; j++)  {
                    columnasT[j+1] = columnasT[j+1].replaceAll(" ","");
                 %>
                <td><input type="text" name="valor<%=crsTasas.getStr("ID_BANCO")%>" id="valor<%=crsTasas.getStr("ID_BANCO")%>" value="<%=columnasT[j+1]%>" class="cajaTexto" size="15" onkeypress="validarFlotante(event,this)">
               </td>
             <%      
                }%>
                   </tr>
            <%
            }
          }
         %>
       </thead>
      </table>
  </body>
</html>