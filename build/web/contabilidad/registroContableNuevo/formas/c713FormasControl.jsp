<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.formas.*" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.exceptions.contabilidad.formas.FormasError" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="formaContable" class="sia.rf.contabilidad.registroContableNuevo.formas.bcRfTcFormasContables" scope="page"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />

 

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c713FormasControl</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script type="text/javascript" language="javascript">
      function envia(pagina) {
        document.getElementById("forma").action = pagina;
        document.getElementById("forma").submit();
      }
    </script>
  </head>
  <body>
  <%util.tituloPagina(out,"Contabilidad institucional","Formas contables","Control",true);%>
  <%! 
  
    public void crearConfiguraForma(Connection con, HttpServletRequest request, bcRfTcSecuenciaForma sec, int reg, String nombre, int col, int nivel, String[] cuentas) {
      crearConfiguraForma(con, request, sec, reg, nombre, col, nivel, cuentas, false);
    }
  
    public void crearConfiguraForma(Connection con, HttpServletRequest request, bcRfTcSecuenciaForma sec, int reg, String nombre, int col, int nivel, String[] cuentas, boolean signo) {
      bcRfTcConfiguraForma conf = null;
      String codigo = request.getParameterValues("txt"+nombre+col)[reg];
      if(codigo != null && !codigo.trim().equals("")) {
        try  {
          String variable = "0";
          String sig = null;
          //int param = reg+1;
          if(request.getParameterValues("hd"+nombre+col) != null)
            variable = request.getParameterValues("hd"+nombre+col)[reg];
          if(signo)
            sig = request.getParameter("chbPosNeg"+cuentas[reg]);
          codigo = codigo.replaceAll(" ","_");
          conf = new bcRfTcConfiguraForma("", codigo, String.valueOf(nivel), variable, sec.getSecuenciaFormaId(), sig );
          conf.insert(con);
        } catch (Exception ex)  {
          System.err.println("Error al crear la subcuenta");
        } finally  {
          conf = null;
        }
      }
      
    }
    
    
  
    public void crearConfiguraForma(Connection con, HttpServletRequest request, bcRfTcSecuenciaForma sec, int reg, String[] cuentas) throws Exception {
        int col = 1;
        int i;
        int niveles = Integer.parseInt(request.getParameter("nivelesConfigura"));
        for (i = 1; i <= niveles; i++)  {
          crearConfiguraForma(con, request, sec, reg, "Cta", i, i, cuentas);
        }
        crearConfiguraForma(con, request, sec, reg, "Cta", i++, 98, cuentas,true);
        crearConfiguraForma(con, request, sec, reg, "Cta", i++, 99, cuentas);
    }
  
    public void crearSecuenciaForma(Connection con, HttpServletRequest request, bcRfTcFormasContables forma, int reg, String[] cuentas) throws Exception {
      bcRfTcSecuenciaForma sec = null;
      try  {
        sec = new bcRfTcSecuenciaForma();
        sec.setOperacionContableId(request.getParameter("chbTipoOper"+cuentas[reg]));
        sec.setFormaContableId(forma.getFormaContableId());
        sec.setCrearCuentaContable("0");
        sec.setAcumularImporte("0");
        sec.setMovible("1");
        
          sec.insert(con);
          crearConfiguraForma(con, request,sec,reg, cuentas);
        
      } catch (Exception ex)  {
        System.out.println("Error al generar la secuencia");
      } finally  {
      }
      
    }
    
    public void modConfiguraForma(Connection con, HttpServletRequest request, bcRfTcSecuenciaForma sec, int reg, String nombre, int col, int nivel, String[] cuentas)  throws Exception {
      modConfiguraForma(con, request, sec, reg, nombre, col, nivel, cuentas, false);
    }
  
    public void modConfiguraForma(Connection con, HttpServletRequest request, bcRfTcSecuenciaForma sec, int reg, String nombre, int col, int nivel, String[] cuentas, boolean signo)  throws Exception {
      bcRfTcConfiguraForma conf = null;
      try  {
        String configuraFormaId = request.getParameterValues("hdCfg"+col)[reg];
        if(configuraFormaId!=null && !configuraFormaId.equals("")) {
          conf = new bcRfTcConfiguraForma();
          conf.setConfiguraFormaId(configuraFormaId);
          conf.select(con);
          
          String variable = "0";
          String sig = null;
          //int param = reg+1;
          if(request.getParameterValues("hd"+nombre+col) != null)
            variable = request.getParameterValues("hd"+nombre+col)[reg];
          if(signo)
            sig = request.getParameter("chbPosNeg"+cuentas[reg]);
          //conf = new bcRfTcConfiguraForma("", request.getParameterValues("txt"+nombre+col)[reg], String.valueOf(nivel), variable, sec.getSecuenciaFormaId(), sig );
              /*public bcRfTcConfiguraForma(String configuraFormaId, String codigo, String nivel, String esvariable,  
                                String secuenciaFormaId, String signo) {*/
          conf.setCodigo(request.getParameterValues("txt"+nombre+col)[reg]);
          conf.setCodigo(conf.getCodigo().replaceAll(" ","_"));
          conf.setSigno(sig);
          conf.update(con);
        }
      } catch (Exception ex)  {
        System.err.println("Error al generar la configuracion");
        
      } finally  {
        conf = null;
      }
      
    }
    
    public void modificarConfiguraForma(Connection con, HttpServletRequest request, bcRfTcSecuenciaForma sec, int reg, String[] cuentas) throws Exception {
      int col = 1;
        int niveles = Integer.parseInt(request.getParameter("nivelesConfigura"));
        int i;
        for (i = 1; i <= niveles; i++)  {
          modConfiguraForma(con, request, sec, reg, "Cta", i, i, cuentas);
        }
        modConfiguraForma(con, request, sec, reg, "Cta", i++, 98, cuentas,true);
        modConfiguraForma(con, request, sec, reg, "Cta", i++, 99, cuentas);
    }
    
    public void modificarSecuenciaForma(Connection con, HttpServletRequest request, bcRfTcFormasContables forma, int reg, String[] cuentas, String secuenciaFormaId) {
      bcRfTcSecuenciaForma sec = null;
      try  {
        sec = new bcRfTcSecuenciaForma();
        sec.setSecuenciaFormaId(secuenciaFormaId);
        sec.select(con);
        
        sec.setOperacionContableId(request.getParameter("chbTipoOper"+cuentas[reg]));
        sec.setFormaContableId(forma.getFormaContableId());
        sec.setCrearCuentaContable("0");
        sec.setAcumularImporte("0");
        sec.setMovible("1");
        
        sec.update(con);
        modificarConfiguraForma(con, request,sec,reg, cuentas);
        
      } catch (Exception ex)  {
        System.err.println("Error al generar la secuencia");
      } finally  {
        sec = null;
      }
    }
    
    public void crearModificarSecuenciasFomas(Connection con, HttpServletRequest request, bcRfTcFormasContables forma) throws Exception {
      String[] cuentas = request.getParameter("registros").split(",");
      String[] creados = request.getParameterValues("hdCrea");
      for (int i = 0; i < cuentas.length; i++)  {
        if(creados[i].equals("0"))
          crearSecuenciaForma(con, request,forma,i, cuentas);
        else
          modificarSecuenciaForma(con, request,forma,i, cuentas, creados[i]);
      }
    }
    
    public void eliminarSecuenciasFormas(Connection con, HttpServletRequest request) {
      String[] eliminados = request.getParameter("regEliminar").split(",");
      bcRfTcSecuenciaForma secuencia = new bcRfTcSecuenciaForma();
      for(String eliminado : eliminados) {
        if(eliminado != null && !eliminado.equals("")) {
          secuencia.setSecuenciaFormaId(eliminado);
          secuencia.borraEnCascada(con);
        }
      }
    }
  %>
  <%
    Connection con = null;
    String pagina = null;
    boolean error = false;
    String mensaje = null;
    try  {
    
      con = DaoFactory.getConnection(DaoFactory.CONEXION_CONTABILIDAD);
      con.setAutoCommit(false);
      if(request.getParameter("formaContableId") !=null ){
          formaContable.setFormaContableId(request.getParameter("formaContableId"));
          formaContable.select(con);  
      }
      String forma = request.getParameter("hdforma") != null ? request.getParameter("hdforma") : "";
      String concepto = request.getParameter("hdconcepto") != null ? request.getParameter("hdconcepto") : "";
      String documentoFuente = request.getParameter("hddocumentoFuente") != null ? request.getParameter("hddocumentoFuente") : "";
      String tipoPolizaId = request.getParameter("hdtipoPolizaId") != null ? request.getParameter("hdtipoPolizaId") : "";
      

%>
    <form name="forma" action="forma" method="POST">
    <jsp:setProperty name="formaContable" property="*" />
      <input type="hidden" name="accion" id="accion" value="<%=request.getParameter("accion")%>">
      <input type="hidden" name="hdforma" id="hdforma" value="<%=forma%>">
      <input type="hidden" name="hdconcepto" id="hdconcepto" value="<%=concepto%>">
      <input type="hidden" name="hddocumentoFuente" id="hddocumentoFuente" value="<%=documentoFuente%>">
      <input type="hidden" name="hdtipoPolizaId" id="hdtipoPolizaId" value="<%=tipoPolizaId%>">
    <table align="center" width="50%" class="general">
        <thead></thead>
        <tr>
         <td class="azulObs">Unidad ejecutora</td>
         <td class="azulObs">Entidad</td>
         <td class="azulObs">Ambito</td>
        </tr>
        <tr>
         <td align="center"><%=controlRegistro.getUnidad()%></td>
         <td align="center"><%=controlRegistro.getEntidad()%></td>
         <td align="center"><%=controlRegistro.getAmbito()%></td>
        </tr>
      </table>
      <br/>
      <br/>

<%    
      switch(Integer.parseInt(request.getParameter("accion"))) {
        case 1: pagina = "c713FormasFormulario.jsp";
                formaContable.insert(con); //INSERTA FORMA PADRE
                //INSERTA SECUENCIA FORMA
                String[] cuentas = request.getParameter("registros").split(",");
                for (int i = 0; i < cuentas.length; i++)  {
                  crearSecuenciaForma(con, request,formaContable,i, cuentas);
                }
                mensaje = "Se di&oacute; de alta con éxito la forma " + formaContable.getForma();
                break;
                
        case 3: pagina = "c713FormasResultado.jsp";
                formaContable.setFormaContableId(request.getParameter("clave"));
                formaContable.select(con); 
                formaContable.validaNoPolizasAsociadas(formaContable.getForma(),controlRegistro.getUnidad(),controlRegistro.getEntidad(),controlRegistro.getAmbito(),Integer.parseInt(formaContable.getIdCatalogoCuenta()),Integer.parseInt(formaContable.getEsmanual()));
                formaContable.borraEnCascada(con);
                mensaje = "Se borr&oacute; con éxito la forma " + formaContable.getForma();
                break;
        case 2: pagina = "c713FormasResultado.jsp";
                formaContable.update(con);
                crearModificarSecuenciasFomas(con, request,formaContable);
                eliminarSecuenciasFormas(con, request);
                mensaje = "Se modific&oacute; con éxito la forma " + formaContable.getForma();
                break;
      }
      con.commit();
    } catch (Exception ex)  {
      error = true; 
      ex.printStackTrace();
      con.rollback();
      new FormasError(ex);
    } finally  {
      if(con != null)
        con.close();
      con = null;
    }
      if(error){
  %>
                  <table align="center">
                    <thead></thead>
                    <tbody>
                      <tr>
                        <td>Ocurrio un error en la base de datos. <br> Favor de comunicarlo al administrador.</td>
                      </tr>
                    </tbody>
                  </table>
                <%}else {%>
                <table width="70%" align="center">
                  <thead></thead>
                  <tr>
                    <td class="azulCla" align="center"><%=mensaje%></td>
                  </tr>
                </table>
  <%}%>
                <br/>
                <hr class="piePagina">
                <table align="center">
                  <thead></thead>
                  <tr>
                    <td><input type="button" class="boton" value="regresar" onclick="envia('<%=pagina%>')"></td>
                  </tr>
                </table>
    
    
    </form>
  </body>
</html>