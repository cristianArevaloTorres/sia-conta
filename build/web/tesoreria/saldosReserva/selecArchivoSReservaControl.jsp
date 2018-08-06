<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.util.*" %>
<%@ page import="sia.db.sql.Vista" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="sia.libs.formato.Error" %>
<%@ page import="sia.rf.tesoreria.registro.saldosReserva.bcRfTrSaldosReserva" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>selecArchivoSReservaControl</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
     <script language="JavaScript"  type="text/javascript">

    
      
        function regresa(){
          document.getElementById('form1').action='seleccArchSReserva.jsp';
          document.getElementById('form1').submit();
       }
    

    </script>
  </head>
  <%!
    private String getIdCuenta(String ctaArch){
        String regresa = null;
        List<Vista> registro = null;
        Map parametros = new HashMap();
        parametros.put("numCuenta",ctaArch);
        SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {
          registro = sentenciasSE.registros("catalogos.select.obtieneIdCuenta.saldoReserva",parametros);
          if (registro != null)
            regresa = registro.get(0).getField("ID_CUENTA");
          else 
            regresa = "";
        } catch (Exception e)  {
          Error.mensaje(e,"TESORERIA");
          regresa = ""; 
        } finally  {
          sentenciasSE = null;
        }
        return regresa;
     }
     
     private String existeSaldo(String idCuenta, String fechaCarga){
        String regresa = null;
        Map parametro = new HashMap(); 
        List <Vista> registros= null;
        parametro.put("fechaCarga",fechaCarga);
        parametro.put("idCuenta",idCuenta);
        SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {
          registros = sentenciasSE.registros("criterios.select.existeSaldoReserva.saldoReserva",parametro);
          if (registros != null)
            regresa = registros.get(0).getField("ID_CUENTA");
          else 
            regresa = null;
        } catch (Exception e)  {
          Error.mensaje(e,"TESORERIA");
          regresa = null; 
        } finally  {
          sentenciasSE = null;
        }
        return regresa;
    }
     
   %>
  <body>
     <form id="form1" name="form1" action="seleccArchSReserva.jsp" method="POST">
     <%util.tituloPagina(out,"Tesorería","Carga de saldos reserva","Resultado de la carga de saldos",true);%>
     <%
        Connection con=null;
        String token = null;
        FileReader fr = null;
        String existeCuenta = null;
        String cuentaArch = null;
        String saldoArch = "0.00";
        String fechaArch = null;
        boolean regresar = true;
        bcRfTrSaldosReserva rfTrSaldosReserva = null;
        try {
          con = DaoFactory.getTesoreria();
          con.setAutoCommit(false);
          rfTrSaldosReserva = new bcRfTrSaldosReserva();
          fr = new FileReader("faces/uploads/"+ request.getSession().getAttribute("nombre"));
          BufferedReader linea = new BufferedReader(fr);
          while ((token=linea.readLine()) != null && regresar ) {
            if (!token.equals("")) {
              cuentaArch =  token.substring(0,16).trim();
              existeCuenta = getIdCuenta(cuentaArch);
              if (existeCuenta!=null){
                if (existeSaldo(existeCuenta,token.substring(56,58).concat("/").concat(token.substring(58,60)).concat("/").concat(token.substring(60,64)))!=null){
                  rfTrSaldosReserva.setIdCuenta(existeCuenta);
                  rfTrSaldosReserva.setFecha(token.substring(56,58).concat("/").concat(token.substring(58,60)).concat("/").concat(token.substring(60,64)));
                  rfTrSaldosReserva.select(con);
                  rfTrSaldosReserva.setSaldoCierre(token.substring(69,82).concat(".").concat(token.substring(82,84)));
                  rfTrSaldosReserva.update(con);
                }
                else{
                  rfTrSaldosReserva.setIdCuenta(existeCuenta);
                  rfTrSaldosReserva.setBanco("STDR");
                  rfTrSaldosReserva.setTipoPertenencia("PRO");
                  rfTrSaldosReserva.setMoneda("$");                           
                  rfTrSaldosReserva.setFecha(token.substring(56,58).concat("/").concat(token.substring(58,60)).concat("/").concat(token.substring(60,64)));
                  rfTrSaldosReserva.setSaldoCierre(token.substring(69,82).concat(".").concat(token.substring(82,84)));
                  rfTrSaldosReserva.insert(con);
                }
              }
              else 
                regresar = false;
            }  // if (!token.equals(""))
          }// while
          linea.close();
          linea = null;
          fr.close();
          fr = null;
          con.commit();
        }
        catch (Exception e) {
          DaoFactory.rollback(con);   
          regresar = false;
          Error.mensaje(e,"TESORERIA");
        } finally  {
            DaoFactory.closeConnection(con);
        }
        %>
         <br>
         <br>
         <table align="center">
           <thead></thead>
           <tbody>
             <tr align="center">
               <td style="font-size:large; color:rgb(0,99,148)">Estatus de la importación</td>
             </tr>
             <tr>
               <td></td>
             </tr>
             <tr align="center">
               <td style="font-size:small; font-style:italic; font-weight:normal"><%=regresar?"Se guardo la información de manera exitosa":"Ocurrio un error al guardar la información en la base de datos"%></td>
             </tr>
           </tbody>
         </table> 
         <br>
         <hr class="piePagina">
         <br>
         <table align="center">
           <thead></thead>
           <tbody>
             <tr>
               <td><input type="button" class="boton" value="Aceptar" onclick="regresa()"></td>
             </tr>
           </tbody>
         </table>
     </form>
  </body>
</html>