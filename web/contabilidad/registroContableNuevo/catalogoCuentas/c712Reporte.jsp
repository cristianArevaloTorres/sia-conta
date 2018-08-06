<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="sia.db.sql.Vista"%>
<%@ page import="sia.rf.contabilidad.reportes.DatosReportes"%>
<%@ page import="sia.rf.contabilidad.registroContable.servicios.Cuenta"%>
<%@ page import="sia.xml.Dml"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>



<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c712Reporte</title>  
    <script type="text/javascript" language="javascript">
      function envia() {
        if(document.getElementById("genera").value=='true')
          forma.submit();
      }
    </script>
  </head>
  
  <body onload="envia()">
    <form id="forma" name="forma" action="../../../Librerias/reportes/generaReporte.jsp">
  <%
         // HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
    ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
    Map mapParams = new HashMap();
    //Reporte repClase = new Reporte();
    Dml dml = null; 
    StringBuffer sentencia = new StringBuffer();
    StringBuffer condicion = new StringBuffer();
    DatosReportes datosReportes   = null; 
    List<Vista> registro          = null; 
    String[] lsCtas=request.getParameterValues("txtCta1");
    boolean genera=false;
    try{
    dml = new Dml(Dml.CONTABILIDAD);
      datosReportes = new DatosReportes(); 
      registro = new ArrayList<Vista>();
        //formar condicion
        
        for(int i=0; i<lsCtas.length; i++){
          if(i==2 || i==3)
            lsCtas[i] = lsCtas[i].equals("")?"0":lsCtas[i];
          else
            lsCtas[i] = lsCtas[i].equals("")?"0000":lsCtas[i];
        }
       /*for(int i=0; i<lsCtas.length; i++){
            if(!lsCtas[i].equals("")){
                if(i==0)
                    lsCondicion=lsCondicion+" and SUBSTR(cuenta_contable,1,5)>='"+lsCtas[i]+"' AND SUBSTR(cuenta_contable,1,5)<='"+lsCtas[i+1]+"' ";
                if(i==2)
                    lsCondicion=lsCondicion+"AND SUBSTR(cuenta_contable,6,4)>='"+lsCtas[i]+"' AND SUBSTR(cuenta_contable,6,4)<='"+lsCtas[i+1]+"' ";
                if(i==4)
                    lsCondicion=lsCondicion+"AND SUBSTR(cuenta_contable,10,4)>='"+lsCtas[i]+"' AND SUBSTR(cuenta_contable,10,4)<='"+lsCtas[i+1]+"' ";
                if(i==6)
                    lsCondicion=lsCondicion+"AND SUBSTR(cuenta_contable,14,4)>='"+lsCtas[i]+"' AND SUBSTR(cuenta_contable,14,4)<='"+lsCtas[i+1]+"' ";
                if(i==8)
                    lsCondicion=lsCondicion+"AND SUBSTR(cuenta_contable,18,4)>='"+lsCtas[i]+"' AND SUBSTR(cuenta_contable,18,4)<='"+lsCtas[i+1]+"' ";
                if(i==10)
                    lsCondicion=lsCondicion+"AND SUBSTR(cuenta_contable,22,4)>='"+lsCtas[i]+"' AND SUBSTR(cuenta_contable,22,4)<='"+lsCtas[i+1]+"' ";
                if(i==12)
                    lsCondicion=lsCondicion+"AND SUBSTR(cuenta_contable,26,4)>='"+lsCtas[i]+"' AND SUBSTR(cuenta_contable,26,4)<='"+lsCtas[i+1]+"' ";                    
            }
        }*/
        //formar query
        condicion.append("(substr(cuenta_contable,1,4) between '"+lsCtas[0]+"' and '"+lsCtas[1]+"' and nivel=1 \n");
        condicion.append("and substr(cuenta_contable,1,4) in ");
        condicion.append("(select distinct substr(cuenta_contable,1,4) from RF_TR_CUENTAS_CONTABLES  \n ");
        condicion.append("where substr(cuenta_contable,5,1) between '"+lsCtas[2]+"' and '"+lsCtas[3]+"' \n");
        condicion.append("and substr(cuenta_contable,6,4) between '"+lsCtas[4]+"' and '"+lsCtas[5]+"' \n");
        condicion.append("and substr(cuenta_contable,10,4) between '"+lsCtas[6]+"' and '"+lsCtas[7]+"' \n");
        condicion.append("and substr(cuenta_contable,14,4) between '"+lsCtas[8]+"' and '"+lsCtas[9]+"' \n");
        condicion.append("and extract(year from fecha_vig_ini)="+controlReg.getEjercicio()+"\n");
        condicion.append("and id_catalogo_cuenta = "+controlReg.getIdCatalogoCuenta()+") \n");
        condicion.append(") ");
        condicion.append("or (substr(cuenta_contable,1,4) between '"+lsCtas[0]+"' and '"+lsCtas[1]+"'  \n");
        condicion.append("and substr(cuenta_contable,5,1) between '"+lsCtas[2]+"' and '"+lsCtas[3]+"' and nivel=2 \n ");
        condicion.append("and substr(cuenta_contable,1,5) in \n");
        condicion.append("(select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES  \n ");
        condicion.append("where substr(cuenta_contable,5,1) between '"+lsCtas[2]+"' and '"+lsCtas[3]+"' \n");
        condicion.append("and substr(cuenta_contable,6,4) between '"+lsCtas[4]+"' and '"+lsCtas[5]+"' \n");
        condicion.append("and substr(cuenta_contable,10,4) between '"+lsCtas[6]+"' and '"+lsCtas[7]+"' \n");
        condicion.append("and substr(cuenta_contable,14,4) between '"+lsCtas[8]+"' and '"+lsCtas[9]+"' \n");
        condicion.append("   and extract(year from fecha_vig_ini)="+controlReg.getEjercicio()+"\n");
        condicion.append("   and id_catalogo_cuenta = "+controlReg.getIdCatalogoCuenta()+" )\n ");
        condicion.append(") \n ");
        condicion.append("or (substr(cuenta_contable,1,4) between '"+lsCtas[0]+"' and '"+lsCtas[1]+"' \n ");
        condicion.append("  and substr(cuenta_contable,5,1) between '"+lsCtas[2]+"' and '"+lsCtas[3]+"' \n ");
        condicion.append("  and substr(cuenta_contable,6,4) between '"+lsCtas[4]+"' and '"+lsCtas[5]+"' and nivel=3 \n ");
        condicion.append("and substr(cuenta_contable,1,5) in \n");
        condicion.append("(select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES  \n ");
        condicion.append("where substr(cuenta_contable,5,1) between '"+lsCtas[2]+"' and '"+lsCtas[3]+"' \n");
        condicion.append("and substr(cuenta_contable,6,4) between '"+lsCtas[4]+"' and '"+lsCtas[5]+"' \n");
        condicion.append("and substr(cuenta_contable,10,4) between '"+lsCtas[6]+"' and '"+lsCtas[7]+"' \n");
        condicion.append("and substr(cuenta_contable,14,4) between '"+lsCtas[8]+"' and '"+lsCtas[9]+"' \n");
        condicion.append("   and extract(year from fecha_vig_ini)="+controlReg.getEjercicio()+"\n");
        condicion.append("   and id_catalogo_cuenta = "+controlReg.getIdCatalogoCuenta()+" )\n ");
        condicion.append(")  \n");
        condicion.append("or (substr(cuenta_contable,1,4) between '"+lsCtas[0]+"' and '"+lsCtas[1]+"'  \n");
        condicion.append("  and substr(cuenta_contable,5,1) between '"+lsCtas[2]+"' and '"+lsCtas[3]+"' \n ");
        condicion.append("  and substr(cuenta_contable,6,4) between '"+lsCtas[4]+"' and '"+lsCtas[5]+"'\n  ");
        condicion.append(" and substr(cuenta_contable,10,4) between '"+lsCtas[6]+"' and '"+lsCtas[7]+"' and nivel=4 \n ");
        condicion.append("and substr(cuenta_contable,1,5) in \n");
        condicion.append("(select distinct substr(cuenta_contable,1,5) from RF_TR_CUENTAS_CONTABLES  \n ");
        condicion.append("where substr(cuenta_contable,5,1) between '"+lsCtas[2]+"' and '"+lsCtas[3]+"' \n");
        condicion.append("and substr(cuenta_contable,6,4) between '"+lsCtas[4]+"' and '"+lsCtas[5]+"' \n");
        condicion.append("and substr(cuenta_contable,10,4) between '"+lsCtas[6]+"' and '"+lsCtas[7]+"' \n");
        condicion.append("and substr(cuenta_contable,14,4) between '"+lsCtas[8]+"' and '"+lsCtas[9]+"' \n");
        condicion.append("   and extract(year from fecha_vig_ini)="+controlReg.getEjercicio()+"\n");
        condicion.append("   and id_catalogo_cuenta = "+controlReg.getIdCatalogoCuenta()+" )\n ");
        condicion.append(") \n ");
        condicion.append("or (substr(cuenta_contable,1,4) between '"+lsCtas[0]+"' and '"+lsCtas[1]+"'  \n");
        condicion.append("and substr(cuenta_contable,5,1) between '"+lsCtas[2]+"' and '"+lsCtas[3]+"' \n");
        condicion.append("  and substr(cuenta_contable,6,4) between '"+lsCtas[4]+"' and '"+lsCtas[5]+"'\n  ");
        condicion.append("  and substr(cuenta_contable,10,4) between '"+lsCtas[6]+"' and '"+lsCtas[7]+"'\n ");
        condicion.append(" and substr(cuenta_contable,14,4) between '"+lsCtas[8]+"' and '"+lsCtas[9]+"'  and nivel=5\n ");
        condicion.append(") \n ");
        condicion.append("or (substr(cuenta_contable,1,4) between '"+lsCtas[0]+"' and '"+lsCtas[1]+"' \n ");
        condicion.append("and substr(cuenta_contable,5,1) between '"+lsCtas[2]+"' and '"+lsCtas[3]+"' \n");
        condicion.append("  and substr(cuenta_contable,6,4) between '"+lsCtas[4]+"' and '"+lsCtas[5]+"' \n ");
        condicion.append("  and substr(cuenta_contable,10,4) between '"+lsCtas[6]+"' and '"+lsCtas[7]+"' \n");
        condicion.append(" and substr(cuenta_contable,14,4) between '"+lsCtas[8]+"' and '"+lsCtas[9]+"' \n");
        condicion.append("  and substr(cuenta_contable,18,4) between '"+lsCtas[10]+"' and '"+lsCtas[11]+"'  and nivel=6 \n");
        condicion.append(") \n ");
        condicion.append("or (substr(cuenta_contable,1,4) between '"+lsCtas[0]+"' and '"+lsCtas[1]+"' \n ");
        condicion.append("and substr(cuenta_contable,5,1) between '"+lsCtas[2]+"' and '"+lsCtas[3]+"' \n");
        condicion.append("  and substr(cuenta_contable,6,4) between '"+lsCtas[4]+"' and '"+lsCtas[5]+"'\n  ");
        condicion.append("  and substr(cuenta_contable,10,4) between '"+lsCtas[6]+"' and '"+lsCtas[7]+"'\n ");
        condicion.append(" and substr(cuenta_contable,14,4) between '"+lsCtas[8]+"' and '"+lsCtas[9]+"'\n ");
        condicion.append("  and substr(cuenta_contable,18,4) between '"+lsCtas[10]+"' and '"+lsCtas[11]+"'\n ");
        condicion.append("  and substr(cuenta_contable,22,4) between '"+lsCtas[12]+"' and '"+lsCtas[13]+"' and nivel=7\n ");
        condicion.append(")  \n");
        condicion.append("or (substr(cuenta_contable,1,5) between '"+lsCtas[0]+"' and '"+lsCtas[1]+"' \n ");
        condicion.append("and substr(cuenta_contable,5,1) between '"+lsCtas[2]+"' and '"+lsCtas[3]+"' \n");
        condicion.append("  and substr(cuenta_contable,6,4) between '"+lsCtas[4]+"' and '"+lsCtas[5]+"' \n ");
        condicion.append("  and substr(cuenta_contable,10,4) between '"+lsCtas[6]+"' and '"+lsCtas[7]+"'\n ");
        condicion.append(" and substr(cuenta_contable,14,4) between '"+lsCtas[8]+"' and '"+lsCtas[9]+"'\n ");
        condicion.append("  and substr(cuenta_contable,18,4) between '"+lsCtas[10]+"' and '"+lsCtas[11]+"'\n ");
        condicion.append("  and substr(cuenta_contable,22,4) between '"+lsCtas[12]+"' and '"+lsCtas[13]+"'\n ");
        condicion.append("  and substr(cuenta_contable,26,4) between '"+lsCtas[14]+"' and '"+lsCtas[15]+"'and nivel=8\n ");
        condicion.append(")  \n");
        condicion.append("or (substr(cuenta_contable,1,5) between '"+lsCtas[0]+"' and '"+lsCtas[1]+"' \n ");
        condicion.append("and substr(cuenta_contable,5,1) between '"+lsCtas[2]+"' and '"+lsCtas[3]+"' \n");
        condicion.append("  and substr(cuenta_contable,6,4) between '"+lsCtas[4]+"' and '"+lsCtas[5]+"' \n ");
        condicion.append("  and substr(cuenta_contable,10,4) between '"+lsCtas[6]+"' and '"+lsCtas[7]+"'\n ");
        condicion.append(" and substr(cuenta_contable,14,4) between '"+lsCtas[8]+"' and '"+lsCtas[9]+"'\n ");
        condicion.append("  and substr(cuenta_contable,18,4) between '"+lsCtas[10]+"' and '"+lsCtas[11]+"'\n ");
        condicion.append("  and substr(cuenta_contable,22,4) between '"+lsCtas[12]+"' and '"+lsCtas[13]+"'\n ");
        condicion.append("  and substr(cuenta_contable,22,4) between '"+lsCtas[14]+"' and '"+lsCtas[15]+"'\n ");
        condicion.append("  and substr(cuenta_contable,26,4) between '"+lsCtas[16]+"' and '"+lsCtas[1]+"'and nivel=8\n ");
        condicion.append(") \n ");
        mapParams.put("validacion", "");
        mapParams.put("ejercicio", controlReg.getEjercicio());
        mapParams.put("condicion",condicion.toString());
        mapParams.put("idCatalogo", controlReg.getIdCatalogoCuenta());
        sentencia.append(dml.getSelect("registroContable", "reporteCuentasContables", mapParams));
        sentencia.append("");
        mapParams.clear();
        /*sentencia=sentencia.append("SELECT SUBSTR(cuenta_contable,1,5) AS mayor,SUBSTR(cuenta_contable,6,4) AS programa, ");
        sentencia.append("SUBSTR(cuenta_contable,10,4) AS uniejec, SUBSTR(cuenta_contable,14,4) AS entamb, ");
        sentencia.append("SUBSTR(cuenta_contable,18,4) AS subcta1,SUBSTR(cuenta_contable,22,4) AS subcta2, ");
        sentencia.append("SUBSTR(cuenta_contable,26,4) AS subcta3,UPPER(descripcion) AS descripcion ");
        sentencia.append("FROM RF_TR_CUENTAS_CONTABLES WHERE "+lsCondicion);
        sentencia.append(" order by cuenta_contable");*/
       
       //System.out.println("sentencia "+sentencia);
       
      registro = datosReportes.obtenerTitulos(controlReg.getUnidad(),String.valueOf(controlReg.getEntidad()),String.valueOf(controlReg.getAmbito()));
      mapParams.put("TITULO1",registro==null?"T1":registro.get(0).getField("TITULO1")); 
      mapParams.put("TITULO2",registro==null?"T2":registro.get(0).getField("TITULO2")); 
      mapParams.put("TITULO3",registro==null?"T3":registro.get(0).getField("TITULO3")); 

      mapParams.put("EJERCICIO",controlReg.getEjercicio());
      mapParams.put("FECHA_ACTUAL", sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_LARGA,controlReg.getFechaAfectacion()));
       mapParams.put("FECHA", sia.libs.formato.Fecha.formatear(6,controlReg.getFechaAfectacion()).toUpperCase());
      mapParams.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/").toString());
      /*repClase.imprimir(sentencia, mapParams, "CuentasContables", "/contabilidad/registroContable/reportes/cuentasContables", 
      DaoFactory.CONEXION_CONTABILIDAD, "pdf", "Generacion de reporte de cuentas contables", "Contabilidad");*/
    request.getSession().setAttribute("abrirReporte", "abrir");
    request.getSession().setAttribute("nombreArchivo", "CuentasContables");
    request.getSession().setAttribute("rutaArchivo", "/contabilidad/registroContable/reportes/cuentasContables");
    request.getSession().setAttribute("parametros", mapParams);
    request.getSession().setAttribute("conexion", DaoFactory.CONEXION_CONTABILIDAD);
    request.getSession().setAttribute("query", sentencia);
    request.getSession().setAttribute("formatoSalida", "pdf");
    request.getSession().setAttribute("accionPagina", "Generacion de reporte");
    request.getSession().setAttribute("modulo", "Contabilidad");
      genera = true;
    }
    catch(Exception e){
      System.err.println("Error al generar el reporte de formas contables");
    }
  
  %>
    <input type="hidden" name="genera" id="genera" value="<%=genera%>">
    </form>
  </body>
</html>
