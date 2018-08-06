<%@ page contentType="text/html;charset=ISO-8859-1"%>
<jsp:useBean id="dtsProceso" class="sia.rf.tesoreria.VariablesSession" scope="session"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesListadoProgramasControl</title>
    <link rel="stylesheet" href="../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
        
    function asignaPag(opcion){
    try{
    
       if (opcion=='Carga de clave de rastreo'){
          document.getElementById('form1').action='../faces/tesoreria/bancas/seleccionaArchivoBanco.jspx';
          document.getElementById('form1').submit();
       }
       else if (opcion=='Transaccionalidad bancaria'){
              document.getElementById('form1').action='../faces/tesoreria/bancas/seleccionaArchivoBanco.jspx';
              document.getElementById('form1').submit();              
             }
             else if (opcion=='Arrastre de saldos bancarios'){
                    document.getElementById('form1').action='arrastreSaldosR/tesArrastreSaldosR.jsp';
                    document.getElementById('form1').submit();              
                  }
                  else if (opcion=='Actualizar informacion'){
                        //   document.getElementById('form1').action='../faces/tesoreria/bancas/seleccionaArchivoBanco.jspx';
                          document.getElementById('form1').action='bancas/seleccionarArchivo.jsp';
                          document.getElementById('form1').submit(); 
                       }
                       else if (opcion=='Validacion de saldos'){
                              document.getElementById('form1').action='validaSaldos/tesFiltroSaldos.jsp';
                              document.getElementById('form1').submit();    
                            }
                            else if (opcion=='Modificacion de clave de transaccion'){
                                   document.getElementById('form1').action='adminClaveTransaccion/tesListaCtasBanca.jsp';
                                   document.getElementById('form1').submit();    
                                 }
                                 else if (opcion=='Carga saldos de reserva'){
                                   // document.getElementById('form1').action='../faces/tesoreria/saldosReserva/selecArchivoSReserva.jspx';
                                    document.getElementById('form1').action='saldosReserva/seleccArchSReserva.jsp';
                                    document.getElementById('form1').submit();              
                                 }
                                       else if (opcion=='Arrastre de saldos diarios - bancas'){
                                         document.getElementById('form1').action='arrastreSaldosD/tesArrastreSaldosD.jsp ';
                                         document.getElementById('form1').submit();              
                                       }
                                            else if (opcion=='Consulta diferencia en saldos'){
                                                document.getElementById('form1').action='admDiferencias/tesListaDiferencias.jsp?estatusDef=2';
                                                document.getElementById('form1').submit();              
                                            }
                                            else{
                                                document.getElementById('form1').action='reportesTransaccionalidad/tesFiltroCuentaBancaria.jsp';
                                                document.getElementById('form1').submit();    
                                            }
      } catch(e){
        alert(e.message);
      }
        }
    </script>
  </head>
  <body onload="asignaPag('<%=request.getParameter("opcion")%>')">
    <form id="form1" name="form1" method="POST">
         <input type="hidden" value="<%=request.getParameter("opcion")%>" id="opcion" name="opcion">
         <input type="hidden" value="<%=request.getParameter("nomProg")%>" id="nomProg" name="nomProg">
         <input type="hidden" value="<%=request.getParameter("listaProg")%>" id="idProgramaS" name="idProgramaS">
         <input type="hidden" value="<%=request.getParameter("proceso")%>" id="proceso" name="proceso">
         <input type="hidden" value="<%=request.getParameter("administrador")%>" id="administrador" name="administrador">
         <%
            dtsProceso.setAdministrador(request.getParameter("administrador"));
            dtsProceso.setIdProgramaS(request.getParameter("listaProg"));
            dtsProceso.setNomProg(request.getParameter("nomProg"));
            dtsProceso.setOpcion(request.getParameter("opcion"));
            dtsProceso.setProceso(request.getParameter("proceso"));
         %>
    </form>
  </body>
</html>