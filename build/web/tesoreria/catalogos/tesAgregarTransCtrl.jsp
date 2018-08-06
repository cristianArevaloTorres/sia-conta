<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ page import="sia.db.sql.SentenciasCRS" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesAgregarTransCtrl</title>
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
  
   
    public boolean encuentraMismoTipo(HttpServletRequest request) {
        boolean encuentra = false;
        SentennciasSE sen = null;
        int numReg = 0;
        try  {
            sen = new SentennciasSE();
            sen.addParam("claveTrans",request.getParameter("clave"));
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.obtenIdTipoAplicaTrans.catTransacciones");
            numReg = sen.size();
            if(numReg==0){
                encuentra = true;
            }
            else{ /// checar este next
                sen.next();
                int i=0;
                do{
                    String tipoClave = sen.getString("id_tipo_aplica");
                    if(tipoClave.equals(request.getParameter("clave")))
                        encuentra = true;
                    else
                        encuentra = false;
                    sen.next();
                    i++;
                } while ((i<numReg) && (encuentra));
            }
        } catch (Exception ex)  {
            ex.printStackTrace();
        } finally  {
            sen = null;
        }
        return encuentra;
    }
   
     public boolean insertaDatos(StringBuffer listaCampos, StringBuffer listaValores, Connection conn, String tabla){
      boolean regresa = false; 
      int ejecuto = -1;
      SentennciasSE sentenciasSE = null;
      try  {
        sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        ejecuto = sentenciasSE.ejecutar(conn,"insert into ".concat(tabla).concat(" (").concat(listaCampos.toString()).concat(") values (").concat(listaValores.toString()).concat(")"));
        if (ejecuto!=-1)
          regresa = true;
        else 
          regresa =false;
      } catch (Exception e)  {
            e.printStackTrace();
            regresa = false; 
        } finally  {
            sentenciasSE = null;
          }
      return regresa; 
    }
    
    
    public boolean actualizaDatos(String listaCamposValores, String condicion, Connection con, String tabla){
      boolean regresa = false; 
      int ejecuto = -1;
      SentennciasSE sentenciasSE = null;
      try  {
        sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        ejecuto = sentenciasSE.ejecutar(con,"update ".concat(tabla).concat(" set ").concat(listaCamposValores).concat(" where ").concat(condicion));
        if (ejecuto!=-1)
          regresa = true;
        else 
          regresa =false;
      } catch (Exception e)  {
            e.printStackTrace();
            regresa = false; 
        } finally  {
            sentenciasSE = null;
          }
      return regresa; 
    }
   
    public int generaIdClave(){
        int regresa = 0;
        SentenciasCRS sen = null;
        try  {
            sen = new SentenciasCRS();
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.obtenerIdClaveTrans.catTransacciones");
            if(sen.next())
              regresa = Integer.parseInt(sen.getString("idClaveTrans"));
        } catch (Exception e)  {
            e.printStackTrace();
        }  finally  {
            sen = null;
        }
        return regresa;
    }
     
    public boolean registratransNormal(Connection conn, HttpServletRequest request, boolean esComision){
      boolean regresa = false;
      StringBuffer listaValores = new StringBuffer();
      StringBuffer listaCampos = new StringBuffer();
      int idClave = 0;
      try  {
        idClave = generaIdClave();
        listaValores.append(idClave);
        listaValores.append(",'");
        listaCampos.append("id_clave_trans,");
        listaValores.append(request.getParameter("tipo").concat("','"));
        listaCampos.append("id_tipo_aplica,");
        listaValores.append(request.getParameter("clave").concat("',"));
        listaCampos.append("clave_trans,");
        listaValores.append("to_date('".concat(request.getParameter("fecha")).concat("','dd/mm/yyyy'),"));
        listaCampos.append("vigencia_ini,");
        listaValores.append("null,'");
        listaCampos.append("vigencia_fin,");
        listaValores.append(request.getParameter("descripcion").concat("',"));
        listaCampos.append("descripcion,");
        listaValores.append(request.getParameter("claveTb")==null?"null":request.getParameter("claveTb"));
        listaCampos.append("id_clave_tb");
        if (esComision){
            listaValores.append(",");
            listaValores.append(request.getParameter("claveEgreso").equals("") || request.getParameter("claveEgreso").equals("0")?"null":request.getParameter("claveEgreso"));
            listaCampos.append(",id_clave_egreso");
        }
        regresa = insertaDatos(listaCampos,listaValores,conn,"RF_TC_CLAVES_TRANSACCION");
        listaCampos.setLength(0);
        listaValores.setLength(0);
        if (esComision){
            if (regresa){
                listaValores.append(idClave);
                listaValores.append(",");
                listaCampos.append("id_clave_trans,");
                listaValores.append("SEQ_RF_TC_COSTOS_TRANSACCION.nextval,");
                listaCampos.append("id_costo_transaccion,");
                listaValores.append("null,");
                listaCampos.append("limite_inferior,");
                listaValores.append("null,");
                listaCampos.append("limite_superior,");
                listaValores.append(request.getParameter("costo")==null?"null":request.getParameter("costo"));
                listaCampos.append("costo");
                regresa = insertaDatos(listaCampos,listaValores, conn, "RF_TC_COSTOS_TRANSACCION");
            }
            else 
                regresa = false; 
        }
      } catch (Exception ex)  {
          ex.printStackTrace();
          regresa = false; 
      }
      return regresa; 
    }
    
    public boolean insertaNormalNoSPEI(HttpServletRequest request, Connection conn) {
        boolean regresa = false;
        StringBuffer listaValores = new StringBuffer();
        StringBuffer listaCampos = new StringBuffer();
        int idClave = 0;
        try  {
            idClave = generaIdClave();
            listaValores.append(idClave);
            listaValores.append(",'");
            listaCampos.append("id_clave_trans,");
            listaValores.append(request.getParameter("tipo").concat("','"));
            listaCampos.append("id_tipo_aplica,");
            listaValores.append(request.getParameter("clave").concat("',"));
            listaCampos.append("clave_trans,");
            listaValores.append("to_date('".concat(request.getParameter("fecha")).concat("','dd/mm/yyyy'),"));
            listaCampos.append("vigencia_ini,");
            listaValores.append("null,'");
            listaCampos.append("vigencia_fin,");
            listaValores.append(request.getParameter("descripcion").concat("',"));
            listaCampos.append("descripcion,");
            listaValores.append(request.getParameter("claveEgreso")==null?"null":request.getParameter("claveEgreso").concat("',"));
            listaCampos.append("id_clave_egreso,");
            listaValores.append(request.getParameter("claveTb")==null?"null":request.getParameter("claveTb"));
            listaCampos.append("id_clave_tb");
            regresa = insertaDatos(listaCampos,listaValores, conn, "RF_TC_CLAVES_TRANSACCION");
            listaCampos.setLength(0);
            listaValores.setLength(0);
            if (regresa){
                listaValores.append(idClave);
                listaValores.append(",");
                listaCampos.append("id_clave_trans,");
                listaValores.append("SEQ_RF_TC_COSTOS_TRANSACCION.nextval,");
                listaCampos.append("id_costo_transaccion,");
                listaValores.append("null,");
                listaCampos.append("limite_inferior,");
                listaValores.append("null,");
                listaCampos.append("limite_superior,");
                listaValores.append(request.getParameter("costo")==null?"null":request.getParameter("costo"));
                listaCampos.append("costo,");
                regresa = insertaDatos(listaCampos,listaValores, conn, "RF_TC_COSTOS_TRANSACCION");
            }
            else 
                regresa = false; 
        } catch (Exception ex)  {
            ex.printStackTrace();
            regresa = false; 
        }
        return regresa; 
    }
     
    public boolean actualizaAnteriorNoSPEI(HttpServletRequest request, Connection con) {
        boolean regresa = false;
        String strIdClaveTransAnterior = null;
        SentenciasCRS sen = null;
        try  {
            sen = new SentenciasCRS();
            sen.addParam("claveTrans",request.getParameter("clave"));
            sen.addParam("fecha",request.getParameter("fecha"));
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.obtenFechaMaximaAnterior.catTransacciones");
            strIdClaveTransAnterior = sen.getString("idClaveTransAnterior");
        }
        catch (Exception e) {
                strIdClaveTransAnterior = "";
        }
        if (!strIdClaveTransAnterior.equals("")) {
            try {
                regresa = actualizaDatos("vigencia_fin = date('".concat(request.getParameter("fecha")).concat("'-1"), "id_clave_tran =".concat(strIdClaveTransAnterior), con, "RF_TC_CLAVES_TRANSACCION");
            }
            catch (Exception e) {
                regresa = false;
            }
        }
        return regresa;
    }
    
    public String obtFechaMin(HttpServletRequest request){
        String regresa = null;
        SentenciasCRS sen = null;
        try  {
            sen = new SentenciasCRS();
            sen.addParam("claveTrans",request.getParameter("clave"));
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.obtenFechaMinSiguiente.catTransacciones");
            if(sen.next())
              regresa = sen.getString("fechaMinima");
        } catch (Exception ex)  {
            ex.printStackTrace();
        } finally  {
            sen = null;
        }
        return regresa;
    }
    
    public boolean insertaAnteriorNoSPEI(HttpServletRequest request,Connection con, String fechaMin) {
        boolean regresar = false;
        StringBuffer listaValores = new StringBuffer();
        StringBuffer listaCampos = new StringBuffer();
        int idClave = 0;
        try  {
            idClave = generaIdClave();
            listaValores.append(idClave);
            listaValores.append(",'");
            listaCampos.append("id_clave_trans,");
            listaValores.append(request.getParameter("tipo").concat("','"));
            listaCampos.append("id_tipo_aplica,");
            listaValores.append(request.getParameter("clave").concat("',"));
            listaCampos.append("clave_trans,");
            listaValores.append("to_date('".concat(request.getParameter("fecha")).concat("','dd/mm/yyyy'),"));
            listaCampos.append("vigencia_ini,");
            listaValores.append("to_date('".concat(fechaMin).concat("','dd/mm/yyyy'),"));
            listaCampos.append("vigencia_fin,");
            listaValores.append(request.getParameter("descripcion").concat("',"));
            listaCampos.append("descripcion,");
            listaValores.append(request.getParameter("claveEgreso")==null?"null":request.getParameter("claveEgreso").concat("',"));
            listaCampos.append("id_clave_egreso,");
            listaValores.append(request.getParameter("claveTb")==null?"null":request.getParameter("claveTb"));
            listaCampos.append("id_clave_tb");
            regresar = insertaDatos(listaCampos,listaValores, con, "RF_TC_CLAVES_TRANSACCION");
            listaCampos.setLength(0);
            listaValores.setLength(0);
            if (regresar){
                listaValores.append(idClave);
                listaValores.append(",");
                listaCampos.append("id_clave_trans,");
                listaValores.append("SEQ_RF_TC_COSTOS_TRANSACCION.nextval,");
                listaCampos.append("id_costo_transaccion,");
                listaValores.append("null,");
                listaCampos.append("limite_inferior,");
                listaValores.append("null,");
                listaCampos.append("limite_superior,");
                listaValores.append(request.getParameter("costo")==null?"0.00":request.getParameter("costo"));
                listaCampos.append("costo,");
                regresar = insertaDatos(listaCampos,listaValores, con, "RF_TC_COSTOS_TRANSACCION");
            }
            else 
                regresar = false; 
        } catch (Exception ex)  {
            ex.printStackTrace();
            regresar = false; 
        }
        return regresar; 
    }
    
    public String obtFechaMinSig(HttpServletRequest request){
        String regresa = null;
        SentenciasCRS sen = null;
        try  {
            sen = new SentenciasCRS();
            sen.addParam("claveTrans",request.getParameter("clave"));
            sen.addParam("fecha",request.getParameter("fecha"));
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.obtenFechaMinimaSiguiente.catTransacciones");
            if(sen.next())
              regresa = sen.getString("fechaMinimaSig");
        } catch (Exception ex)  {
            ex.printStackTrace();
        } finally  {
            sen = null;
        }
        return regresa;
    }
    
    public boolean insertaIntermedioNoSPEI(HttpServletRequest request, Connection con, String fechaMinSig) {
        boolean regresar = false;
        StringBuffer listaValores = new StringBuffer();
        StringBuffer listaCampos = new StringBuffer();
        int idClave = 0;
        try  {
            idClave = generaIdClave();
            listaValores.append(idClave);
            listaValores.append(",'");
            listaCampos.append("id_clave_trans,");
            listaValores.append(request.getParameter("tipo").concat("','"));
            listaCampos.append("id_tipo_aplica,");
            listaValores.append(request.getParameter("clave").concat("',"));
            listaCampos.append("clave_trans,");
            listaValores.append("to_date('".concat(request.getParameter("fecha")).concat("','dd/mm/yyyy'),"));
            listaCampos.append("vigencia_ini,");
            listaValores.append("to_date('".concat(fechaMinSig).concat("','dd/mm/yyyy'),"));
            listaCampos.append("vigencia_fin,");
            listaValores.append(request.getParameter("descripcion").concat("',"));
            listaCampos.append("descripcion,");
            listaValores.append(request.getParameter("claveEgreso")==null?"null":request.getParameter("claveEgreso").concat("',"));
            listaCampos.append("id_clave_egreso,");
            listaValores.append(request.getParameter("claveTb")==null?"null":request.getParameter("claveTb"));
            listaCampos.append("id_clave_tb");
            regresar = insertaDatos(listaCampos,listaValores, con, "RF_TC_CLAVES_TRANSACCION");
            listaCampos.setLength(0);
            listaValores.setLength(0);
            if (regresar){
                listaValores.append(idClave);
                listaValores.append(",");
                listaCampos.append("id_clave_trans,");
                listaValores.append("SEQ_RF_TC_COSTOS_TRANSACCION.nextval,");
                listaCampos.append("id_costo_transaccion,");
                listaValores.append("null,");
                listaCampos.append("limite_inferior,");
                listaValores.append("null,");
                listaCampos.append("limite_superior,");
                listaValores.append(request.getParameter("costo")==null?"0.00":request.getParameter("costo"));
                listaCampos.append("costo");
                regresar = insertaDatos(listaCampos,listaValores, con, "RF_TC_COSTOS_TRANSACCION");
            }
            else 
                regresar = false; 
        } catch (Exception ex)  {
            ex.printStackTrace();
            regresar = false; 
        }
        return regresar;
    }
     
      public boolean ejecutaInsercionNoSPEI(HttpServletRequest request, Connection con) {
        boolean regresar = false;
        String fechaInicial  = request.getParameter("fecha");
        String fechaMinima = null;
        String fechaMinimaSig = null;
        int cuantasFechas = 0;
        int cuantosAntes = 0;
        int cuantosDespues = 0;
        SentenciasCRS sen = null;
        try  {
            sen = new SentenciasCRS();
            sen.addParam("claveTrans",request.getParameter("clave"));
            sen.addParam("fecha",request.getParameter("fecha"));
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.obtenCuantasFechasExisten.catTransacciones");
            cuantasFechas = Integer.parseInt(sen.getString("cuantasFechasExisten"));
            if (cuantasFechas == 0) {
                sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.obtenCuantosPeriodosAntes.catTransacciones");
                cuantosAntes = Integer.parseInt(sen.getString("cuantosPeriodosAntes"));
                sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.obtenCuantosPeriodosDespues.catTransacciones");
                cuantosDespues = Integer.parseInt(sen.getString("cuantosPeriodosDespues"));
                if ((cuantosAntes == 0) && (cuantosDespues == 0)){
                    regresar = insertaNormalNoSPEI(request,con);
                } else if ((cuantosAntes > 0) && (cuantosDespues == 0)){
                            regresar = actualizaAnteriorNoSPEI(request,con);
                            if (regresar)
                                regresar = insertaNormalNoSPEI(request,con);
                            else
                                regresar = false;
                       } else if ((cuantosAntes == 0) && (cuantosDespues > 0)) {
                                    fechaMinima = obtFechaMin(request); 
                                    regresar = insertaAnteriorNoSPEI(request,con,fechaMinima);
                              }else if ((cuantosAntes > 0) && (cuantosDespues > 0)){
                                        regresar = actualizaAnteriorNoSPEI(request,con);
                                        if (regresar){
                                            fechaMinimaSig = obtFechaMinSig(request);
                                            regresar = insertaIntermedioNoSPEI(request,con,fechaMinimaSig);
                                        }
                                        else
                                            regresar = false;
                                    }
            }
            else {
                regresar = false;
            }
                
        } catch (Exception ex)  {
            ex.printStackTrace();
        } finally  {
            sen = null;
        }
        
        
       
        return regresar;
    }
   
        
    
  %>
  <%
        Connection con    = null;
        String pagina     = "tesListaTransacciones.jsp";
        String mensaje = null;
        boolean mismoTipo   = true;  
        boolean insertClave = false;
        try  {
            con = DaoFactory.getTesoreria();
            con.setAutoCommit(false);  
            String tipo = request.getParameter("tipo");
            if (!tipo.equals("C")){
                insertClave = registratransNormal(con,request,false);
                if (insertClave){
                     con.commit();
                     pagina = "tesListaTransacciones.jsp";
                     mensaje = "";
                }
                else{
                    insertClave = false;
                    if(con!=null)
                        con.rollback();
                    mensaje = "Error al registrar la clave de transacción";
                }
          
            }
            else{
                insertClave = registratransNormal(con,request,true);
                if (insertClave){
                    con.commit();
                    pagina = "tesListaTransacciones.jsp";
                    mensaje = "";
                }
                else{
                    insertClave = false;
                    if(con!=null)
                        con.rollback();
                    mensaje = "Error al registrar la clave de transacción";
                }
            }
        } catch (Exception ex)  {
            ex.printStackTrace();
        } finally  {
            DaoFactory.closeConnection(con);
            con = null;
        }
        
        
  %>  
  <body onload="ir('<%=pagina%>')">
     <form id="form1" name="form1"  method="POST">
        
        <input type="hidden" id="param" name="param" value="<%=request.getParameter("param")%>">
        <input type="hidden" id="clave" name="clave" value="<%=request.getParameter("clave")%>">
        <input type="hidden" id="mensaje" name="mensaje" value="<%=mensaje%>">
     </form>
  </body>
</html>