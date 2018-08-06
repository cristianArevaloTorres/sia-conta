<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.io.File" %>
<%@ page import="sia.db.sql.Vista" %>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="sia.rf.tesoreria.bancas.acciones.AlmacenaDatos"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="rfTrMovimientosCuentaTmp" class="sia.rf.tesoreria.registro.movimientos.bcRfTrMovimientosCuentaTmp" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesCarArchBanControl</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
    
      function ir(pagina) {
        if(pagina!='' && pagina!='null') {
          document.getElementById('form1').action = pagina;
          document.getElementById('form1').submit();
        }
      }
      
    </script>
  </head>
  <%!
     private boolean existeRegistro(Map parametro, String seccionXML){
        boolean regresa = false;
        List <Vista> registros= null;
        String[] atributos = null;
        SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {
          registros = sentenciasSE.registros(seccionXML,parametro);
          System.out.println("registros "+registros);
          if (registros != null){
            atributos = seccionXML.split("\\.");
            if(atributos[2].equals("unicaFecha")){
                if(registros.size()>1)
                    regresa = true;
                else
                    regresa = false;
            }
            else
                regresa = true;
          }
          else 
            regresa = false;
        } catch (Exception e)  {
          e.printStackTrace();
          regresa = false; 
        } finally  {
          sentenciasSE = null;
        }
        return regresa;
     }
    
    
   %>
   <%
        Connection con    = null;
        String pagina     = "tesArchivoIncorrecto.jsp";
        String programa = null;
        String token = null;
        String seccionL = null;
        String mensaje = null;
        FileReader fr = null;
        Map parametros = new HashMap(); 
        boolean regresa = true;
        AlmacenaDatos almacenaDatos = null;
        try  {
          almacenaDatos = new AlmacenaDatos(request.getServletContext().getRealPath(AlmacenaDatos.RUTA_DEFAULT),(String)session.getAttribute("nombre"));
          con = DaoFactory.getTesoreria();
          con.setAutoCommit(false);
          rfTrMovimientosCuentaTmp.deleteInicio(con);
          if (request.getParameter("idProgramaS").toString().equals("7"))
            seccionL = "movBanamex";
          else if (request.getParameter("idProgramaS").toString().equals("6"))  
                 seccionL = "movimientos";
               else if ((request.getParameter("idProgramaS").toString().equals("10")))
                      seccionL = "movSantander";
                    else 
                      seccionL = "independiente";
          if(almacenaDatos.leerRegistros(con,seccionL,request.getParameter("idProgramaS"))){            
            con.commit();
            if(!existeRegistro(null,"criterios.select.nuevasClaveTrans.bancas")){
              if(!existeRegistro(null,"criterios.select.ctaBancariaNoExisten.bancas")){
                if (request.getParameter("idProgramaS").equals("7"))
                    parametros.put("prog",request.getParameter("idProgramaS").concat(",9")); 
                else
                    parametros.put("prog",request.getParameter("idProgramaS")); 
                if(!existeRegistro(parametros,"criterios.select.ctaBanOtroProg.bancas")){
                  if(existeRegistro(parametros,"criterios.select.unicaFecha.bancas")){
                    File archivoCargado  = new File(request.getServletContext().getRealPath(AlmacenaDatos.RUTA_DEFAULT)+(String)session.getAttribute("nombre"));
                    if (!archivoCargado.delete()) 
                        mensaje = "¡Error! No se ha podido eliminar el archivo.";
                    mensaje = "El archivo contiene transacciones de diferentes fechas";
                  }
                  else
                    pagina = "tesImportarTransacciones.jsp";
                }
                else 
                  mensaje = "El archivo contiene cuentas bancarias de otro programa";
              }
              else
                pagina = "tesListaCtasBancarias.jsp";
            }
            else
                pagina = "tesListaNuevasClavesT.jsp";
          }
          else{
            con.rollback();
            File archivoCargado  = new File(request.getServletContext().getRealPath(AlmacenaDatos.RUTA_DEFAULT)+(String)session.getAttribute("nombre"));
            if (!archivoCargado.delete()) {
              mensaje = "¡Error! No se ha podido eliminar el archivo.";
            }
            mensaje = "Problemas en los registros del archivo de importación";
            pagina = "tesArchivoIncorrecto.jsp";
          }
        } catch (Exception ex)  {
            System.out.println("tesCarArchBanControl.jsp "+ex.getClass().getName()+": "+ex.getMessage());
          ex.printStackTrace();
          if(con!=null)
            con.rollback();
        } finally  {
          DaoFactory.closeConnection(con);
          con = null;
        }
        
  %>
  <body onload="ir('<%=pagina%>')">
     <form id="form1" name="form1"  method="POST">
     <input type="hidden" id="nombre" name="nombre" value="<%=(String)session.getAttribute("nombre")%>">
     <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
     <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
     <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
     <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
     <input type="hidden" id="mensaje" name="mensaje" value="<%=mensaje%>">
     </form>
  </body>
</html>