<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.libs.fun.tabla.*" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="pbFormas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesListaTransacciones</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
     
     
       function llamamisma(pagina){
        document.getElementById('param').value=pagina;
        document.form1.submit();
       }
       
       function asignaPagina() {
         document.getElementById('form1').action = 'tesAgregarTrans.jsp';
         document.getElementById('form1').submit();
       }
     
       
    </script>
  </head>
  <body>
  <form id="form1" name="form1"  method="POST">
      <%util.tituloPagina(out,"Tesorería","Listado de transacciones","Transacciones",true);%>
      <br>
      <%
        boolean mostrar = request.getParameter("mensaje")!=null?true:false;
        int param = request.getParameter("param") != null ? Integer.parseInt(request.getParameter("param")) : 0;
        pbFormas.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.obtieneTransacciones.catTransacciones");
        pag.addCampo(new Campo("clave_trans","Clave",Campo.AL_CEN,null));  
        pag.addCampo(new Campo("CTDescripcion","Descrpción",Campo.AL_IZQ,null));  
        pag.addCampo(new Campo("ClaveEgreso","Generado por",Campo.AL_IZQ,null));  
        pag.addCampo(new Campo("fechaformato","Vigencia a partir de",Campo.AL_CEN,null));  
        pag.addCampo(new Campo("TATDescripcion","Tipo",Campo.AL_IZQ,null));  
        pag.addCampo(new Campo("limite_inferior","Lim. inferior",Campo.AL_DER,Campo.FORMATO_DOS_DEC));  
        pag.addCampo(new Campo("limite_superior","Lim. superior",Campo.AL_DER,Campo.FORMATO_DOS_DEC));  
        pag.addCampo(new Campo("costo","Costo",Campo.AL_DER,Campo.FORMATO_DOS_DEC));  
        pag.addImg(new Img("tesListaTransCtrl.jsp",Img.IMG_ELIMINAR,"param="+param,"id_clave_trans",false,"Eliminar"));
        pag.addImg(new Img("tesModTransaccion.jsp",Img.IMG_MODIFICAR,"param="+param,"id_clave_trans",false,"Modificar"));
        
        pag.seleccionarPagina(pbFormas, out, 20, param, "../../", "id_clave_trans",null,"Listado de transacciones");
      %>
    <input type="hidden" name="param" id="param" value="<%=param%>"/>
    <br>
    <hr class="piePagina">
      <br> 
      <table align="center" <%=mostrar?"style='display:'":"style='display:none'"%>>
        <thead></thead>
           <tbody>
             <tr align="center">
               <td  style="font-size:medium;"><%=request.getParameter("mensaje")%></td>
             </tr>
           </tbody>
      </table> 
      <br>
       <table align='center'>
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="button" class="boton" value="Registrar transacción" onclick="asignaPagina()"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>