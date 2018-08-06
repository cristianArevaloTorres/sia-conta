<%@page import="sia.beans.seguridad.Autentifica"%>
<%@ page import="sia.rf.contabilidad.registroContable.servicios.Condicion"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*,sia.db.sql.Vista"%>
<%@ page  import="sia.rf.contabilidad.reportes.DatosReportes"%>
<%@ page  import="sia.beans.seguridad.Autentifica"%>
<%@ page  import="sia.libs.formato.Fecha"%>


<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>



<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>c700ConsultarResultado</title>
        <link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>

        <script languaje="JavaScript" type='text/JavaScript'>
            function envia() {
                if(document.getElementById("genera").value=='true')
                    forma.submit();
            }
        </script>
        <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de pólizas", "Consultar", true);</jsp:scriptlet>        
        </head>
        <body onload="envia()">
            <form id="forma" name="forma"  action="../../Librerias/reportes/generaReporte.jsp">

                <br><br>
                <b>Fecha Actual</b> [<%=request.getParameter("txtfechaActual")%>]
            <br><br>

            <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=request.getParameter("txtUnidad")%>, Entidad=<%=request.getParameter("txtEntidad")%>, Ambito=<%=request.getParameter("txtAmbito")%> y Ejercicio=<%=request.getParameter("txtEjercicio")%>  </H2>

            <br>
            <!-- Encabezado de la tabla con resultado del filtro -->
            <table width="15%" align="left" class="general">
                <tr>
                    <th height="21" colspan="3" align="left" class="general">Resultado del filtro</th>
                </tr>
            </table>
            <br><br><br>



            <!-- (Inicia codigo JAVA) -->
            <%
                Connection conexion = null;
                String lsOpcion = request.getParameter("txtOpcion");
                String hIdCatalogo = request.getParameter("txtIdCatalogo");
                String hUnidad = request.getParameter("txtUnidad");
                String hAmbito = request.getParameter("txtAmbito");
                String hEntidad = request.getParameter("txtEntidad");
                String hEjercicio = request.getParameter("txtEjercicio");
                String lsFechaActual = request.getParameter("txtfechaActual");
                String lstOperaciones = request.getParameter("lstOperaciones");
                String lstpoliza = request.getParameter("lstpoliza");
                String txtReferencia = request.getParameter("txtReferencia");
                String txtConcepto = request.getParameter("txtConcepto");
                String lstMes = request.getParameter("lstMes");
                String txtFecha01 = request.getParameter("txtFecha01");
                String txtFecha02 = request.getParameter("txtFecha02");
                String txtPoliza01 = request.getParameter("txtPoliza01");
                String st_poliza = request.getParameter("st_poliza");
                String txtImporte = request.getParameter("txtImporte");
                String chk_poliz = request.getParameter("chk_poliz") == null ? "" : request.getParameter("chk_poliz");
                String tip_or = request.getParameter("tip_or");
                String chk_formato = request.getParameter("chk_formato");





                Autentifica autUsuario = (Autentifica) request.getSession().getAttribute("Autentifica");
                List<Vista> registro = new ArrayList<Vista>();
                DatosReportes datosReportes = new DatosReportes();
                if (controlRegistro.getFechaAfectacion() == null) {
                    fechaAfectacion.establecerFechaAfectacion(request, fechaAfectacion.obtenerFechaAfectacion());
                }

            %>



            <!-- (Inicia codigo JAVA) -->
            <%
                StringBuffer Query = new StringBuffer("");
                StringBuffer query2 = new StringBuffer("");
                StringBuffer lsCondicion = new StringBuffer("");
                StringBuffer lsCondicionImporte = new StringBuffer("");
                boolean genera = false;
                try {
                    conexion = DaoFactory.getContabilidad();

                    if (!hIdCatalogo.equals("")) {
                        lsCondicion.append(" and pl.id_catalogo_cuenta=").append(hIdCatalogo);
                    }

                    if (!hUnidad.equals("")) {
                        lsCondicion.append(" and pl.unidad_ejecutora=").append(hUnidad);
                    }
                    if (!hAmbito.equals("")) {
                        lsCondicion.append(" and pl.ambito=").append(hAmbito);
                    }
                    if (!hEntidad.equals("")) {
                        lsCondicion.append(" and pl.entidad=").append(hEntidad);
                    }
                    if (!hEjercicio.equals("")) {
                        lsCondicion.append(" and pl.ejercicio=").append(hEjercicio);
                    }
                    if (!lstMes.equals("")) {
                        lsCondicion.append(" and pl.mes=").append(lstMes);
                    }
                    if (!lstpoliza.equals("")) {
                        lsCondicion.append(" and pl.tipo_poliza_id=").append(lstpoliza);
                    }
                    if (!lstOperaciones.equals("")) {
                        lsCondicion.append(" and  pl.maestro_operacion_id='").append(lstOperaciones).append("' ");
                    }
                    if (!txtReferencia.equals("")) {
                        lsCondicion.append("and  upper(pl.referencia) like upper('%").append(txtReferencia).append("%') ");
                    }
                    if (!txtConcepto.equals("")) {
                        lsCondicion.append("and  upper(pl.concepto) like upper('%").append(txtConcepto).append("%') ");
                    }



                    if (!txtImporte.equals("")) {
                        lsCondicionImporte.append("and  upper(dp.importe) like upper('%").append(txtImporte).append("%') ");
                    }




                    if (!lstMes.equals("")) {
                        lsCondicion.append(" and to_date(to_char(FECHA,'MM'),'MM')=to_date('").append(lstMes.toString()).append("','MM')");
                    }
                    if (!txtFecha01.equals("")) {
                        lsCondicion.append(" and to_date(to_char(fecha,'dd/MM/yyyy'),'dd/MM/yyyy')>=to_date('").append(txtFecha01.toString()).append("','dd/MM/yyyy') and to_date(to_char(fecha,'dd/MM/yyyy'),'dd/MM/yyyy')<=to_date('").append(txtFecha02.toString()).append("','dd/MM/yyyy') ");
                    }


                    Condicion condicionRango = new Condicion();
                    if (!txtPoliza01.equals("")) {
                        condicionRango.setCriterios(txtPoliza01.toUpperCase());
                        lsCondicion.append(" and " + condicionRango.getSentencia("tp.abreviatura", "pl.consecutivo "));
                    }



                    switch (Integer.parseInt(st_poliza.toString())) {
                        case 1: {
                            lsCondicion.append(" and  pl.clasificacion_poliza_id in (1,2,3) ");

                            break;
                        }
                        case 2: {
                            lsCondicion.append(" and  pl.clasificacion_poliza_id in (1) ");
                            break;
                        }
                        case 3: {
                            lsCondicion.append(" and  pl.clasificacion_poliza_id in (2,3) ");
                            break;
                        }
                        
                    }


                    String ambitoCatalogo = hEntidad.concat(hAmbito);
                    ambitoCatalogo = ambitoCatalogo.length() == 2 ? "00".concat(ambitoCatalogo) : "0".concat(ambitoCatalogo);
                    String unidadCatalogo = hUnidad.length() == 3 ? "0".concat(hUnidad) : hUnidad;


                    //condición para el check 
                    if (chk_poliz.equals("si")) {
                        lsCondicion.append("and cc.cuenta_contable like '_________" + unidadCatalogo + ambitoCatalogo + "%'");
                    } else {
                        lsCondicion.append(" and (( pl.unidad_ejecutora = '" + hUnidad + "' and pl.entidad =" + hEntidad + " and pl.ambito = " + hAmbito + ") ");
                        lsCondicion.append(" or cc.cuenta_contable like '_________" + unidadCatalogo + ambitoCatalogo + "%') ");
                    }



                    lsCondicionImporte.append(lsCondicion);
                    if (txtImporte != null && (!txtImporte.equals(""))) {
                        lsCondicion.append(" and pl.poliza_id in (select distinct pl.poliza_id ");
                        lsCondicion.append(" from rf_tr_polizas pl, rf_tr_detalle_poliza dp, rf_tr_cuentas_contables cc ");
                        lsCondicion.append(" where pl.poliza_id=dp.poliza_id and dp.cuenta_contable_id = cc.cuenta_contable_id ");
                        lsCondicion.append(" and abs(dp.importe)=").append(txtImporte);
                        lsCondicion.append(" and ejercicio=").append(hEjercicio).append(" and pl.id_catalogo_cuenta=").append(hIdCatalogo);



                        if (chk_poliz.equals("si")) {
                            lsCondicion.append(" and cc.cuenta_contable like '_________").append(unidadCatalogo).append(ambitoCatalogo).append("%'");
                        } else {
                            lsCondicion.append(" and (( pl.unidad_ejecutora = '" + hUnidad + "' and pl.entidad =" + hEntidad + " and pl.ambito = " + hAmbito + ") ");
                            lsCondicion.append(" or cc.cuenta_contable like '_________" + unidadCatalogo + ambitoCatalogo + "%') ");


                            lsCondicion.append(")");
                        }
                        lsCondicion.append(lsCondicionImporte);
                        if (!lstMes.equals("")) {
                            lsCondicion.append(" and mes=" + lstMes);
                        }
                    }
                    switch (Integer.parseInt(tip_or.toString())) {
                        case 1: {
                            lsCondicion.append("order by  pl.unidad_ejecutora, pl.poliza_id, dp.id_detalle");
                            break;
                        }
                        case 2: {
                            lsCondicion.append("order by lpad(pl.entidad||pl.ambito,3,0),pl.poliza_id, dp.id_detalle ");
                            break;
                        }
                        case 3: {
                            lsCondicion.append("order by pl.clasificacion_poliza_id,pl.poliza_id, dp.id_detalle ");
                            break;
                        }
                        case 4: {
                            lsCondicion.append("order by pl.consecutivo,pl.poliza_id,dp.id_detalle");
                            break;
                        }
                        case 5: {
                            lsCondicion.append("order by pl.poliza_id, debe_haber, cc.cuenta_contable");

                            break;
                        }
                    }

                    Query.append("select ")
                        .append("tp.abreviatura || lpad(pl.consecutivo, 5, '0') campo1, ")
                        .append("pl.unidad_ejecutora,  ")
                        .append("lpad(pl.entidad||pl.ambito,3,0) entamb, ")
                        .append("pl.ambito,  ")
                        .append("cc.cuenta_contable,   ")
                        .append("substr(cc.cuenta_contable,1,4)||' '||substr(cc.cuenta_contable,5,1)||' '||substr(cc.cuenta_contable,6,4)||")
                        .append("' '||substr(cc.cuenta_contable,10,4)||' '||substr(cc.cuenta_contable,14,4)||' '||substr(cc.cuenta_contable,18,4) ")
                        .append("||' '||substr(cc.cuenta_contable,22,4)||' '||substr(cc.cuenta_contable,26,4)||' '||substr(cc.cuenta_contable,30,4) ")
                        .append("as cuenta_contable_esp,")
                        .append("pl.origen as maestro_operacion,  ")
                        .append("to_char(pl.fecha,'dd/MM/yyyy') fecha,  ")
                        .append("pl.fecha_afectacion,  ")
                        .append("pl.poliza_id, ")
                        .append("pl.clasificacion_poliza_id, ")
                        .append("pl.poliza_referencia,  ")
                        .append("dp.cuenta_contable_id, cc.descripcion, dp.referencia, dp.importe,  ")
                        .append("oc.operacion_contable_id debe_haber,  ")
                        .append("pl.concepto, pl.referencia ref_gral, ")
                        .append("case when pl.idevento is null then he.apellido_pat || ' ' || he.apellido_mat || ' ' || he.nombres else 'PROCESO AUTOMÁTICO' END AS nombre,  ")
                        .append("pl.num_empleado, ")
                        .append("decode(pl.mes,1,'ENERO',2,'FEBRERO',3,'MARZO',4,'ABRIL',5,'MAYO',6,'JUNIO',7,'JULIO',8,'AGOSTO',9,'SEPTIEMBRE',10,'OCTUBRE',11,'NOVIEMBRE',12,'DICIEMBRE') nombre_mes, ")
                        .append("pl.mes ")
                        .append("from rf_tr_detalle_poliza dp, ")
                        .append("rf_tr_polizas pl,  ")
                        .append("rf_tr_cuentas_contables cc,  ")
                        .append("rf_tc_operaciones_contables oc,  ")
                        .append("rf_tc_tipos_polizas tp,  ")
                        .append("rf_tc_maestro_operaciones mo,  ")
                        .append("rh_tr_empleados he  ")
                        .append("where  pl.poliza_id = dp.poliza_id  ")
                        .append("and dp.cuenta_contable_id = cc.cuenta_contable_id  ")
                        .append("and oc.operacion_contable_id = dp.operacion_contable_id  ")
                        .append("and pl.tipo_poliza_id = tp.tipo_poliza_id  ")
                        .append("and pl.maestro_operacion_id = mo.maestro_operacion_id  ")
                        .append("and pl.num_empleado = he.num_empleado (+)  ")
                        .append(lsCondicion);

                   System.out.println("queryyy: " + Query);
                    //query para consultar polizas manuales de acuerdo al mes
                    query2.append("SELECT sqlresul.*")
                          .append("FROM")
                          .append("b.abreviatura ||a.consecutivo AS consecutivo,")
                          .append("TO_CHAR(a.fecha,'dd/mm/yyyy') fecha,")
                          .append("a.FECHA_AFECTACION FECHA_AFECTACION,")
                          .append("a.num_empleado,")
                          .append("a.referencia,")
                          .append("a.concepto,")
                          .append("a.origen,")
                          .append("d.importe IMPORTE,")
                          .append("DECODE(a.ban_cheque,1,'Cheque',DECODE(a.tipo_poliza_id,1,'Diario',3,'Egreso','Ingreso')) poliza")
                          .append("FROM RF_TR_POLIZAS a,")
                          .append("RF_TC_TIPOS_POLIZAS b,")
                          .append("RF_TR_DETALLE_POLIZA d,")
                          .append("RF_TR_CUENTAS_CONTABLES c")
                          .append("WHERE a.poliza_id =d.poliza_id")
                          .append("AND d.cuenta_contable_id = c.cuenta_contable_id")
                          .append("AND a.tipo_poliza_id = b.tipo_poliza_id")
                          .append("AND a.id_catalogo_cuenta = "+hIdCatalogo+"") //1
                          .append("AND a.unidad_ejecutora = '"+hUnidad+"'") //001
                          .append("AND a.ambito = "+hAmbito+"") //1
                          .append("AND a.entidad = "+hEntidad+"") //9
                          .append(" and a.mes ='"+lstMes+"'")//var
                          .append("and a.ejercicio = 2017")
                          .append("and a.origen = '99'")
                          .append("ORDER BY polizaid ASC)")
                          .append("sqlresul;");
                    if(st_poliza.equals("4")){
                        Query = null;
                        Query = query2;
                    }

                    try {
                        String usuario = (autUsuario == null) ? "Desconocido" : autUsuario.getNombre();
                        registro = datosReportes.obtenerTitulos(hUnidad, hEntidad, hAmbito);
                        Map mapParams = new HashMap();
                        mapParams.put("USUARIO", usuario);
                        mapParams.put("UNIDAD", hUnidad);
                        mapParams.put("ENTIDAD", hEntidad);
                        mapParams.put("AMBITO", hAmbito);
                        mapParams.put("EJERCICIO", Integer.parseInt(hEjercicio));
                        mapParams.put("MES", "1");
                        mapParams.put("DOCUMENTO", "PLZ");
                        mapParams.put("FECHA_ACTUAL", sia.libs.formato.Fecha.formatear(Fecha.FECHA_LARGA, controlRegistro.getFechaAfectacion()));
                        mapParams.put("TITULO1", registro.get(0).getField("TITULO1"));
                        mapParams.put("TITULO2", registro.get(0).getField("TITULO2"));
                        mapParams.put("TITULO3", registro.get(0).getField("TITULO3"));
                        mapParams.put("IMG_DIR", request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/").toString());
                            
                        mapParams.put("ELABORO", request.getParameter("elaboro"));
                        mapParams.put("REVISO", request.getParameter("reviso"));
                        mapParams.put("AUTORIZO", request.getParameter("autorizo"));
                        
                        request.getSession().setAttribute("abrirReporte", "abrir");
                        if (chk_formato.equals("1")) {
                            request.getSession().setAttribute("nombreArchivo", "repPolizas");
                            request.getSession().setAttribute("rutaArchivo", "/contabilidad/registroContable/procesos/polizas/reportes/repPolizas");
                        } else {
                            request.getSession().setAttribute("nombreArchivo", "repPolizasSimple");
                            request.getSession().setAttribute("rutaArchivo", "/contabilidad/registroContable/procesos/polizas/reportes/repPolizasSimple");
                        }
                        request.getSession().setAttribute("parametros", mapParams);
                        request.getSession().setAttribute("conexion", DaoFactory.CONEXION_CONTABILIDAD);
                        request.getSession().setAttribute("query", Query);
                        request.getSession().setAttribute("formatoSalida", "pdf");
                        request.getSession().setAttribute("accionPagina", "Generación de reporte de pólizas");
                        request.getSession().setAttribute("modulo", "Contabilidad");
                        genera = true;
                        
                    }//end try
                    catch (Exception e) {
                        System.err.println("Error al generar el reporte");

                    }
                } catch (Exception E) {
                    System.out.println("Error en " + E.getMessage());
                    System.out.println("Query Modifica/Cancela Pólizas : " + Query);
            %>
            <p>Ha ocurrido un error al accesar la Base de Datos,</p>
            <p>favor de reportarlo al Administrador del Sistema.</p>
            <p>Gracias.</p>
            <%
                } finally {
                    if (conexion != null) {
                        conexion.close();
                        conexion = null;
                    }

                }

            %>


            <table width='100%'>
                <tr><td width='73%'>&nbsp;</td>
                    <td width='10%'>
                        <input type="hidden" name="genera" id="genera" value="<%=genera%>">
                        <INPUT type="hidden" name="txtfechaActual" value=<%=lsFechaActual%>>
                        <INPUT type="hidden" name="txtOperacion" value=<%=lsOpcion%>>        
                    </td>
                    <td width='80%'>
                        <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c700ConsultarFiltro.jsp','');" >
                    </td></tr>
            </table>

        </FORM>
    </body>
</html>