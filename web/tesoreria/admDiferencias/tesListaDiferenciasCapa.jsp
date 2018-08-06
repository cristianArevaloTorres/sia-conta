<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbDiferencias" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesListaDiferenciasCapa</title>
  </head>
   <body onload="parent.loadSourceFinish('dResultado');">
  <%
    String estatus  =  request.getParameter("idEstatus");
    String idPrograma =  request.getParameter("idProgramaS");
    String nomProgS = request.getParameter("nomProg");
    String opcionS = request.getParameter("opcion");
    String procesoS = request.getParameter("proceso");
    try  {
   
        pbDiferencias.addParamVal("estatus",estatus);
        pbDiferencias.addParamVal("programa",idPrograma);
        pbDiferencias.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.listaDiferencias.diferenciaSaldo");
        
        pag.addCampo(new Campo("num_cuenta","No. cuenta",Campo.AL_CEN,null));
        pag.addCampo(new Campo("fechaInicioF","Fecha",Campo.AL_CEN,null));
        pag.addCampo(new Campo("diferencia_real","Diferencia",Campo.AL_CEN,Campo.FORMATO_DOS_DEC));
        pag.addImg(new Img(!estatus.equals("2")?"tesBitacoraAclaracion.jsp":"",!estatus.equals("2")?Img.IMG_CONSULTAR:Img.IMG_CONSULTAR_DES,"idEstatus="+estatus+"&idProgramaS="+idPrograma+"&nomProg="+nomProgS+"&opcion="+opcionS+"&proceso="+procesoS,"id_cuenta,id_saldo_diario",false,"Consulta bitácora"));
        pag.addImg(new Img(estatus.equals("2")?"tesBitacoraAclaracion.jsp":"",estatus.equals("2")?Img.IMG_DOCUMENTO:Img.IMG_DOCUMENTO_DES,"idEstatus="+estatus+"&idProgramaS="+idPrograma+"&nomProg="+nomProgS+"&opcion="+opcionS+"&proceso="+procesoS,"id_cuenta,id_saldo_diario",false,"Registrar actividades"));
   //     pag.addImg(new Img(estatus.equals("3")?"tesConciliaCuenta.jsp":"",estatus.equals("3")?Img.IMG_MODIFICAR:Img.IMG_MODIFICAR_DES,"parametros","ID_CUENTA,ID_SALDO_DIARIO", false, "Conciliación por cuenta"));
  //      pag.addImg(new Img(estatus.equals("3")?"tesConciliaRegistro.jsp":"",estatus.equals("3")?Img.IMG_TRANSFER:Img.IMG_TRANSFER_DES,"parametros","ID_CUENTA,ID_SALDO_DIARIO", false, "Conciliación por registro"));
        int param = request.getParameter("param") != null ? Integer.parseInt(request.getParameter("param")) : 0;    
        pag.seleccionarPagina(pbDiferencias,out,10,param, "../../","id_cuenta,id_saldo_diario",null,"Diferencias en saldo en estatus:" );  
        
    } catch(Exception e) {
      
    }
  %>
  </body>
</html>