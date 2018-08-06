<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ page import="sia.db.sql.Vista" %>
<%@ page import="sia.beans.seguridad.Autentifica"%>
<%@ page import="sia.rf.tesoreria.bancas.acciones.Criterios"%>
<%@ page import="sia.ws.publicar.contabilidad.test.ConsumeEventoWS"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="crsDiaHabil" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="sbAutentifica" class ="sia.beans.seguridad.Autentifica" scope="session"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesImportarTransControl</title>
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
    private int getIdProcesoBitacora (String query){
        int regresa = 0;
        List <Vista> reg= null;
        SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {
           reg = sentenciasSE.registros(query);
           if (reg !=null && reg.size() > 0)
             regresa = Integer.parseInt(reg.get(0).getField("SIGUIENTE").toString());
           else
             regresa = 0;
        } catch (Exception e)  {
          e.printStackTrace();
          regresa = 0; 
        } finally  {
          sentenciasSE = null;
        }
        return regresa;
    }
  %>
  <%
        Connection con    = null;
        String pagina     = "tesArchivoIncorrecto.jsp";
        String mensaje = null;
        String nomSentencia = null;
        List <Vista> saldos = null;
        Map parametros = new HashMap();
        String parametrosConta = null;
        int idProcesoWS = 0;
        ConsumeEventoWS bcWeb = new ConsumeEventoWS();
        String resultadoPoliza=""; 
        SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        Criterios criterios = null;
        int ejecuto = -1;
        try  {
          sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
          con = DaoFactory.getTesoreria();
          con.setAutoCommit(false);  
          criterios = new Criterios();
          if (request.getParameter("idProgramaS").equals("10"))
            nomSentencia = "criterios.insert.insertaMovBancariosSder.bancas";
          else if (request.getParameter("idProgramaS").equals("7"))
                  nomSentencia = "criterios.insert.insertaMovBancariosBMX.bancas";
               else /// agregar lo de hsbc y bancomer 
                  nomSentencia = "criterios.insert.insertaMovBancarios.bancas";
          
          if (sentenciasSE.ejecutar(con,sentenciasSE.getCommand(nomSentencia)) != -1){
            crsDiaHabil.addParam("fechaCarga",request.getParameter("fechaCarga"));
            //crsDiaHabil.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.diaAnteriorHabil.bancas");
            crsDiaHabil.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.diaAnterior.bancas");
            if (crsDiaHabil.next())
               parametros.put("fechaAntHabil",crsDiaHabil.getStr("fecha_inicial"));
            parametros.put("fechaCarga",request.getParameter("fechaCarga"));
            
            if (request.getParameter("idProgramaS").equals("7"))
                parametros.put("programa",request.getParameter("idProgramaS").concat(",9"));
            else
                parametros.put("programa",request.getParameter("idProgramaS"));
            System.out.println("tesImportarTransControl.jsp programa "+parametros.get("programa"));
           
            if (request.getParameter("idProgramaS").equals("8"))
                saldos = sentenciasSE.registros("criterios.select.calculaSaldosBBVA.bancas",parametros);
            else
                saldos = sentenciasSE.registros("criterios.select.calculaSaldos.bancas",parametros);
            System.out.println("tesImportarTransControl.jsp idProgramaS "+request.getParameter("idProgramaS").length());
            System.out.println("tesImportarTransControl.jsp saldos "+saldos.size());
	    boolean resultado = criterios.registraSaldos(saldos,con,request.getParameter("fechaCarga"),request.getParameter("idProgramaS"));
            System.out.println("tesImportarTransControl.jsp resultado "+resultado);
            if(resultado){
            System.out.println("tesImportarTransControl.jsp registraSaldos ");
                if(criterios.actualizaBitacora(sbAutentifica.getNumeroEmpleado(),Integer.parseInt(request.getParameter("idProgramaS")),request.getParameter("fechaCarga"),con)){
            System.out.println("tesImportarTransControl.jsp actualizaBitacora ");
                
                   if(request.getParameter("idProgramaS").equals("7")){
                     if(criterios.actualizaSaldo(Integer.parseInt(request.getParameter("idProgramaS")),request.getParameter("fechaCarga"),con)){
                        parametrosConta = request.getParameter("fechaCarga").concat("&").concat(request.getParameter("fechaCarga")).concat("&").concat(request.getParameter("fechaCarga").substring(0,5));
                        idProcesoWS = getIdProcesoBitacora("select seqbitacoralocal.nextval SIGUIENTE from dual");
                        if (request.getParameter("recarga")=="" || request.getParameter("recarga") == null ){
                            if(criterios.registraBitacoraLocal(sbAutentifica.getNumeroEmpleado(),250,idProcesoWS,parametrosConta,"0",con)){
                              con.commit();
                              pagina = "tesResultadoCarga.jsp"; 
                              /// En pruebas comentar sig 9 lineas 
                              bcWeb.setIdevento(250);
                              bcWeb.setParametros(parametrosConta); 
                              bcWeb.setClaveWS("b2144a9f31"); 
                              bcWeb.setIdproceso(idProcesoWS); 
                              try{ 
                                resultadoPoliza=bcWeb.aplicaRegistroContable(); 
                              }catch(Exception e) {
                                e.printStackTrace();
                               }
                            }
                            else{
                               pagina     = "tesArchivoIncorrecto.jsp";
                               if(con!=null)
                                 con.rollback();
                               mensaje = "Error al registrar en la bitacora para registros contables";
                            }
                        }
                        else{
                           if(criterios.registraBitacoraLocal(sbAutentifica.getNumeroEmpleado(),250,idProcesoWS,parametrosConta,"1",con)){
                             con.commit();
                             pagina = "tesResultadoCarga.jsp";
                           }
                           else{
                               pagina     = "tesArchivoIncorrecto.jsp";
                               if(con!=null)
                                 con.rollback();
                               mensaje = "Error al registrar en la bitacora para registros contables";
                           }
                        }
                     }
                     else{
                       pagina     = "tesArchivoIncorrecto.jsp";
                       if(con!=null)
                         con.rollback();
                       mensaje = "Error al registrar la bitacora de actualización";
                     }
                   }
                   else if(request.getParameter("idProgramaS").equals("6")){
                        parametrosConta = request.getParameter("fechaCarga").concat("&").concat(request.getParameter("fechaCarga")).concat("&").concat(request.getParameter("fechaCarga").substring(0,5));
                        idProcesoWS = getIdProcesoBitacora("select seqbitacoralocal.nextval SIGUIENTE from dual");
                        if (request.getParameter("recarga")=="" || request.getParameter("recarga") == null ){
                            if(criterios.registraBitacoraLocal(sbAutentifica.getNumeroEmpleado(),251,idProcesoWS,parametrosConta,"0",con)){
                              con.commit();
                              pagina = "tesResultadoCarga.jsp";
                              /// En pruebas comentar sig 9 lineas 
                              bcWeb.setIdevento(251);
                              bcWeb.setParametros(parametrosConta); 
                              bcWeb.setClaveWS("b2144a9f31"); 
                              bcWeb.setIdproceso(idProcesoWS); 
                              try{ 
                                resultadoPoliza=bcWeb.aplicaRegistroContable(); 
                              }catch(Exception e) {
                                e.printStackTrace();
                               }
                            }
                            else{
                               pagina     = "tesArchivoIncorrecto.jsp";
                               if(con!=null)
                                 con.rollback();
                               mensaje = "Error al registrar en la bitacora para registros contables";
                            }
                        }
                        else{
                           if(criterios.registraBitacoraLocal(sbAutentifica.getNumeroEmpleado(),251,idProcesoWS,parametrosConta,"1",con)){
                             con.commit();
                             pagina = "tesResultadoCarga.jsp";
                           }
                           else{
                               pagina     = "tesArchivoIncorrecto.jsp";
                               if(con!=null)
                                 con.rollback();
                               mensaje = "Error al registrar en la bitacora para registros contables";
                           }
                        }
           
                   }
                   else{
                   con.commit();
                    System.out.println("tesImportarTransControl.jsp commit ");
                  pagina = "tesResultadoCarga.jsp";
                   }                
                }
                else{
                  pagina     = "tesArchivoIncorrecto.jsp";
                  if(con!=null)
                    con.rollback();
                  mensaje = "Error al registrar la bitacora de actualización";
                }
            }
            else{
              pagina     = "tesArchivoIncorrecto.jsp";
              if(con!=null)
                con.rollback();
              mensaje = "Error al registrar los saldos de las cuentas que incluye el archivo";
            }
          }
          else{
            pagina     = "tesArchivoIncorrecto.jsp";
             if(con!=null)
               con.commit();//rollback();
             mensaje = "Error al registrar las transacciones bancarias";
          }
        } catch (Exception ex)  {
          ex.printStackTrace();
          mensaje = "Error al registrar las transacciones bancarias";
          if(con!=null)
            con.rollback();
        } finally  {
          DaoFactory.closeConnection(con);
          con = null;
        }
        
  %>  
  <body onload="ir('<%=pagina%>')">
     <form id="form1" name="form1"  method="POST">
     <input type="hidden" id="nombre" name="nombre" value="<%=request.getParameter("nombre")%>">
     <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
     <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
     <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
     <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
     <input type="hidden" id="mensaje" name="mensaje" value="<%=mensaje%>">
     <input type="hidden" id="fechaCarga" name="fechaCarga" value="<%=request.getParameter("fechaCarga")%>">
     </form>
  </body>
</html>