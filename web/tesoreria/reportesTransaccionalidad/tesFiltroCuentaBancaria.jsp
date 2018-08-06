<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.rf.tesoreria.catalogos.tiposReporte.ListaTiposReporte"%>
<%@ page import="sia.rf.tesoreria.catalogos.tiposReporte.TipoReporte"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.sql.Vista" %>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>
<jsp:useBean id="crsCtaBanDGC" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsCtaBanDSP" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsCtaBanCGC" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsCtaBanCSP" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsCtaBanENL" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsCtaBanIXV" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsCtaBanDBM" class="sia.db.sql.SentenciasCRS" scope="page"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesFiltroCuentaBancaria</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script src="../../Librerias/Javascript/validacionesFecha.js" type="text/javascript" >
    </script>
    <script language="JavaScript"  type="text/javascript">

     function guardaNomRep(){
       document.getElementById('nomReporte').value = document.getElementById('listaReporte').options[document.getElementById('listaReporte').selectedIndex].text;
     }

     function validarFechas(){  
       correcto=true; 
       mensaje='------  ALERTA  ------ \n';
       fInicio=document.getElementById('fechaInicio');
       ffinal=document.getElementById('fechaFinal');
       correcto = validaFIniFFin(fInicio,ffinal);
       if (correcto == false){
         mensaje=mensaje+'Verifique el periodo de fechas \n';
         correcto=false;
       }
       if(!correcto)
         throw new Error(mensaje);
       return correcto;
     }
     
      function dispersorasGC() { 
            if (document.form1.elements['cbDispGC'].checked){
                for (i=0; i<document.form1.elements['smDispGC'].options.length; i++) 
                    document.form1.elements['smDispGC'][i].selected = false;
            }
            if ( document.form1.elements['smDispGC'].selectedIndex == -1) { 
                for (i=0; i<document.form1.elements['smDispGC'].options.length; i++) 
                   document.form1.elements['smDispGC'][i].selected = true;
            } 
            else { 
                for (i=0; i<document.form1.elements['smDispGC'].options.length; i++) 
                   document.form1.elements['smDispGC'][i].selected = false;
                }    
        }
        
        function noSeleccionaDispGC() {
          document.form1.elements['cbDispGC'].checked = false;
        }
        
      
      function dispersorasSP () { 
            if (document.form1.elements['cbDispSP'].checked){
                for (i=0; i<document.form1.elements['smDispSP'].options.length; i++) 
                    document.form1.elements['smDispSP'][i].selected = false;
            }
            if ( document.form1.elements['smDispSP'].selectedIndex == -1) { 
                for (i=0; i<document.form1.elements['smDispSP'].options.length; i++) 
                   document.form1.elements['smDispSP'][i].selected = true;
            } 
            else { 
                for (i=0; i<document.form1.elements['smDispSP'].options.length; i++) 
                   document.form1.elements['smDispSP'][i].selected = false;
                }    
        }
        
        function noSeleccionaDispSP() {
          document.form1.elements['cbDispSP'].checked = false;
        }
    
        function chequerasGC () { 
            if (document.form1.elements['cbDChqGC'].checked){
                for (i=0; i<document.form1.elements['smChqGC'].options.length; i++) 
                    document.form1.elements['smChqGC'][i].selected = false;
            }
            if ( document.form1.elements['smChqGC'].selectedIndex == -1) { 
                for (i=0; i<document.form1.elements['smChqGC'].options.length; i++) 
                   document.form1.elements['smChqGC'][i].selected = true;
            } 
            else { 
                for (i=0; i<document.form1.elements['smChqGC'].options.length; i++) 
                   document.form1.elements['smChqGC'][i].selected = false;
                }    
        }
        
        function noSeleccionaChqGC() {
          document.form1.elements['cbDChqGC'].checked = false;
        }   
    
        function chequerasSP() { 
            if (document.form1.elements['cbChqSP'].checked){
                for (i=0; i<document.form1.elements['smChqSP'].options.length; i++) 
                    document.form1.elements['smChqSP'][i].selected = false;
            }
            if ( document.form1.elements['smChqSP'].selectedIndex == -1) { 
                for (i=0; i<document.form1.elements['smChqSP'].options.length; i++) 
                   document.form1.elements['smChqSP'][i].selected = true;
            } 
            else { 
                for (i=0; i<document.form1.elements['smChqSP'].options.length; i++) 
                   document.form1.elements['smChqSP'][i].selected = false;
                }    
        }
        
        function noSeleccionaChqSP() {
          document.form1.elements['cbChqSP'].checked = false;
        }  
    
        function enlace() { 
            if (document.form1.elements['cbEnlace'].checked){
                for (i=0; i<document.form1.elements['smEnlace'].options.length; i++) 
                    document.form1.elements['smEnlace'][i].selected = false;
            }
            if ( document.form1.elements['smEnlace'].selectedIndex == -1) { 
                for (i=0; i<document.form1.elements['smEnlace'].options.length; i++) 
                   document.form1.elements['smEnlace'][i].selected = true;
            } 
            else { 
                for (i=0; i<document.form1.elements['smEnlace'].options.length; i++) 
                   document.form1.elements['smEnlace'][i].selected = false;
                }    
        }
        
        function noSeleccionaEnlace() {
          document.form1.elements['cbEnlace'].checked = false;
        } 
        
        function ingresosXV() { 
            if (document.form1.elements['cbIngXV'].checked){
                for (i=0; i<document.form1.elements['smIngXV'].options.length; i++) 
                    document.form1.elements['smIngXV'][i].selected = false;
            }
            if ( document.form1.elements['smIngXV'].selectedIndex == -1) { 
                for (i=0; i<document.form1.elements['smIngXV'].options.length; i++) 
                   document.form1.elements['smIngXV'][i].selected = true;
            } 
            else { 
                for (i=0; i<document.form1.elements['smIngXV'].options.length; i++) 
                   document.form1.elements['smIngXV'][i].selected = false;
                }    
        }
        
        function noSeleccionaIngXV() {
          document.form1.elements['cbIngXV'].checked = false;
        }    
        
                            
        function donativo() { 
            if (document.form1.elements['cbDonativo'].checked){
                for (i=0; i<document.form1.elements['smDonativo'].options.length; i++) 
                    document.form1.elements['smDonativo'][i].selected = false;
            }
            if ( document.form1.elements['smDonativo'].selectedIndex == -1) { 
                for (i=0; i<document.form1.elements['smDonativo'].options.length; i++) 
                   document.form1.elements['smDonativo'][i].selected = true;
            } 
            else { 
                for (i=0; i<document.form1.elements['smDonativo'].options.length; i++) 
                   document.form1.elements['smDonativo'][i].selected = false;
                }    
        }
        
        function noSeleccionaDon() {
          document.form1.elements['cbDonativo'].checked = false;
        }   
        
        function ocultarElemento(idElemento)
        {
          document.getElementById(idElemento).style["display"] = "none"
        }
          
        function verElemento(idElemento)
        {
          document.getElementById(idElemento).style["display"] = ""
        } 
           
        function ocultarCajita() {
          if (document.getElementById('listaReporte').options[document.getElementById('listaReporte').selectedIndex].text == 'Consolidación de saldos bancarios')
            ocultarElemento('colFI');
          else 
            verElemento('colFI');
        }
        
        function verificaSeleccionCta(){
          correcto=true; 
          mensaje='------  ALERTA  ------ \n';
          if ( (document.getElementById("smDispGC")==null || document.form1.elements['smDispGC'].selectedIndex == -1) &&
               (document.getElementById("smDispSP")==null || document.form1.elements['smDispSP'].selectedIndex == -1) && 
               (document.getElementById("smChqGC")==null || document.form1.elements['smChqGC'].selectedIndex == -1) && 
               (document.getElementById("smChqSP")==null || document.form1.elements['smChqSP'].selectedIndex == -1) && 
               (document.getElementById("smEnlace")==null || document.form1.elements['smEnlace'].selectedIndex == -1) && 
               (document.getElementById("smIngXV")==null || document.form1.elements['smIngXV'].selectedIndex == -1) && 
               (document.getElementById("smDonativo")==null || document.form1.elements['smDonativo'].selectedIndex == -1) )
                 correcto = false; 
          if (correcto == false){
            mensaje=mensaje+'Debe seleccionar al menos una cuenta bancaria \n';
            correcto=false;
          }
          if(!correcto)
            throw new Error(mensaje);
          return correcto;
        }
        
        
        function asignaValorRep(boton){
          try{
            document.getElementById('numReporte').value= document.getElementById('listaReporte').options[document.getElementById('listaReporte').selectedIndex].value;
            document.getElementById('tipoReporte').value= boton;
            
            if (document.getElementById('listaReporte').options[document.getElementById('listaReporte').selectedIndex].text == 'Consolidación de saldos bancarios'){
               if (verificaSeleccionCta()){
                document.getElementById('fechaInicio').value= document.getElementById('fechaFinal').value;
                document.getElementById('form1').action='tesFiltroCtaBancariaControl.jsp';
                document.getElementById('form1').target='_blank';
                document.getElementById('form1').submit();
              }
            }
            else{
              if (validarFechas() && verificaSeleccionCta()){
                document.getElementById('form1').action='tesFiltroCtaBancariaControl.jsp';
                document.getElementById('form1').target='_blank';
                document.getElementById('form1').submit();
              }
            }
            
          } catch(e){
            alert(e.message);
          }
        }
        
        
    </script>
  </head>
<%!
  private String getCuentasAdicionales(String login){
    String regresa = "";
    List<Vista> registros = null;
    Map mapParams = new HashMap();
    mapParams.put("login", login);
    SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
    try  {
      registros = sentenciasSE.registros("criterios.select.cuentasAdicionales.reportesMovimientos",mapParams);
      if (registros!=null){
        for (int i = 0; i < registros.size(); i++)  {
          regresa = regresa + registros.get(i).getField("ID_CUENTA") + ",";
        }
        regresa = regresa.substring(0,regresa.length()-1);
      }
      else
        regresa = "''";
    } catch (Exception e)  {
          e.printStackTrace();
          regresa = "''"; 
    } finally  {
      sentenciasSE = null;
      registros = null;
    }
    return regresa;
  }
  
    public String fomatearFecha(String fecha)  {
    String regresa = null;
    if (fecha!=null)
      regresa = Fecha.formatear(2,fecha.replace("-",""));
    return regresa;
  }
  
    public String fechaUltimaMovimientos(String programa){
    String regresa = null;
    SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
    Map parametros = new HashMap();
    List<Vista> registros = null;
    try  {
      parametros.put("idPrograma",programa);
      registros = sentenciasSE.registros("criterios.select.ultimaFechaCargada.reportesMovimientos",parametros);
      if (registros!=null){
          regresa = registros.get(0).getField("FECHA");
      }
    } catch (Exception e)  {
          e.printStackTrace();
          regresa = "''"; 
    } finally  {
      sentenciasSE = null;
      registros = null;
    }
    return fomatearFecha(regresa); 
  }
  
%>
<%
 StringBuffer cdgHTMLList = new StringBuffer();
 ListaTiposReporte listaTiposReporte = new ListaTiposReporte();
 listaTiposReporte.iniciar();
 String fechaUltCarga = null;
 fechaUltCarga = fechaUltimaMovimientos(request.getParameter("idProgramaS"));
 if (Autentifica.getLogin().equals("celso.castillo") || Autentifica.getLogin().equals("pedro.trejomigueles") ){
   listaTiposReporte.removerContraloria();
 }
 else {
 
 /* 
      if(request.getParameter("idProgramaS").equals("7"))
        listaTiposReporte.removerBMX();
      else {  
        if(request.getParameter("idProgramaS").equals("8") || request.getParameter("idProgramaS").equals("9") )
          listaTiposReporte.removerBBVA();
        else {
          if (request.getParameter("idProgramaS").equals("10"))
            listaTiposReporte.removerSantander();
          else
            if (!request.getParameter("idProgramaS").equals("6") && !request.getParameter("idProgramaS").equals("1") ) 
               listaTiposReporte.remover();
         }
      }
  */

 
      if(request.getParameter("idProgramaS").equals("7"))
        listaTiposReporte.removerBMX();
      else if(request.getParameter("idProgramaS").equals("8") || request.getParameter("idProgramaS").equals("9") )
              listaTiposReporte.removerBBVA();
           else if (request.getParameter("idProgramaS").equals("10"))
                    listaTiposReporte.removerSantander();
                else if (request.getParameter("idProgramaS").equals("11") ) 
                        listaTiposReporte.removerBajio();
                     else if (request.getParameter("idProgramaS").equals("12") ) 
                                listaTiposReporte.removerMultiva();
                         
 }  
 List<TipoReporte> nombreReporte = listaTiposReporte.getTipoReporte();
 Iterator iter = nombreReporte.iterator();
 while (iter.hasNext()){
   TipoReporte tipoReporte = (TipoReporte) iter.next();
   cdgHTMLList.append("<option value=" + tipoReporte.getValor() );
   cdgHTMLList.append(">"+ tipoReporte.getDescripcion()   + "</option>");
 }
 
 
 
 String ctasAdicionales = null;
 ctasAdicionales = getCuentasAdicionales(Autentifica.getLogin());
 
 if(request.getParameter("administrador").equals("1")){
     crsCtaBanDGC.addParamVal("tipoCuenta"," (id_tipo_cta = '4' or id_tipo_cta = '3') and id_tipo_programa = :param",request.getParameter("idProgramaS"));
     crsCtaBanDGC.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
     crsCtaBanDSP.addParamVal("tipoCuenta"," id_tipo_cta = '5' and id_tipo_programa = :param",request.getParameter("idProgramaS"));
     crsCtaBanDSP.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
     crsCtaBanCGC.addParamVal("tipoCuenta"," (id_tipo_cta = '1' or id_tipo_cta = '9') and id_tipo_programa = :param",request.getParameter("idProgramaS"));
     crsCtaBanCGC.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
     crsCtaBanCSP.addParamVal("tipoCuenta"," id_tipo_cta = '2' and id_tipo_programa = :param",request.getParameter("idProgramaS"));
     crsCtaBanCSP.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
     crsCtaBanENL.addParamVal("tipoCuenta"," id_tipo_cta = '7' and id_tipo_programa = :param",request.getParameter("idProgramaS"));
     crsCtaBanENL.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
     crsCtaBanIXV.addParamVal("tipoCuenta"," id_tipo_cta = '6' and id_tipo_programa = :param",request.getParameter("idProgramaS"));
     crsCtaBanIXV.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
     crsCtaBanDBM.addParamVal("tipoCuenta"," id_tipo_cta = '10' and id_tipo_programa = :param",request.getParameter("idProgramaS"));
     crsCtaBanDBM.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
 } else if(request.getParameter("administrador").equals("2")){
     crsCtaBanDGC.addParamVal("tipoCuenta"," id_tipo_cta in (".concat(ctasAdicionales).concat(") and id_tipo_programa = :param"),request.getParameter("idProgramaS"));
     crsCtaBanDGC.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
     crsCtaBanDSP.addParamVal("tipoCuenta"," 1=2 and id_tipo_programa = :param",request.getParameter("idProgramaS"));
     crsCtaBanDSP.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
     crsCtaBanCGC.addParamVal("tipoCuenta"," 1=2 and id_tipo_programa = :param",request.getParameter("idProgramaS"));
     crsCtaBanCGC.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
     crsCtaBanCSP.addParamVal("tipoCuenta"," 1=2 and id_tipo_programa = :param",request.getParameter("idProgramaS"));
     crsCtaBanCSP.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
     crsCtaBanENL.addParamVal("tipoCuenta"," 1=2 and id_tipo_programa = :param",request.getParameter("idProgramaS"));
     crsCtaBanENL.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
     crsCtaBanIXV.addParamVal("tipoCuenta"," id_tipo_cta = '6' and id_tipo_programa = :param",request.getParameter("idProgramaS"));
     crsCtaBanIXV.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
     crsCtaBanDBM.addParamVal("tipoCuenta"," 1=2 and id_tipo_programa = :param",request.getParameter("idProgramaS"));
     crsCtaBanDBM.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
   } else {
        switch (Integer.parseInt(Autentifica.getAmbito())) {
        case 1:
        case 2: 
            if(Autentifica.getUnidadEjecutora().equals("109") && request.getParameter("idProgramaS").equals("7") ){
                 crsCtaBanDGC.addParamVal("tipoCuenta"," 1=2 and id_tipo_programa = :param",request.getParameter("idProgramaS"));
                 crsCtaBanDGC.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
                 crsCtaBanDSP.addParamVal("tipoCuenta"," 1=2 and id_tipo_programa = :param",request.getParameter("idProgramaS"));
                 crsCtaBanDSP.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
                 crsCtaBanCGC.addParamVal("tipoCuenta"," id_cuenta in (969,970,955) and id_tipo_programa = :param",request.getParameter("idProgramaS"));
                 crsCtaBanCGC.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
                 crsCtaBanCSP.addParamVal("tipoCuenta"," 1=2 and id_tipo_programa = :param",request.getParameter("idProgramaS"));
                 crsCtaBanCSP.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
                 crsCtaBanENL.addParamVal("tipoCuenta"," 1=2 and id_tipo_programa = :param",request.getParameter("idProgramaS"));
                 crsCtaBanENL.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
                 crsCtaBanIXV.addParamVal("tipoCuenta"," id_tipo_cta = '6' and id_tipo_programa = :param",request.getParameter("idProgramaS"));
                 crsCtaBanIXV.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
                 crsCtaBanDBM.addParamVal("tipoCuenta"," 1=2 and id_tipo_programa = :param",request.getParameter("idProgramaS"));
                 crsCtaBanDBM.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
            }else{
                 crsCtaBanDGC.addParamVal("tipoCuenta"," id_tipo_cta='4' and (unidad_ejecutora ='".concat(Autentifica.getUnidadEjecutora()).concat("' or id_cuenta in (").concat(ctasAdicionales).concat(" )) and id_tipo_programa = :param"),request.getParameter("idProgramaS"));
                 crsCtaBanDGC.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
                 crsCtaBanDSP.addParamVal("tipoCuenta"," id_tipo_cta='5' and (unidad_ejecutora='".concat(Autentifica.getUnidadEjecutora()).concat("' or id_cuenta in (").concat(ctasAdicionales).concat(")) and  id_tipo_programa = :param"),request.getParameter("idProgramaS"));
                 crsCtaBanDSP.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
                 if (request.getParameter("idProgramaS").equals("7"))
                    crsCtaBanCGC.addParamVal("tipoCuenta"," id_tipo_cta='1' and (unidad_ejecutora='".concat(Autentifica.getUnidadEjecutora()).concat("' or id_cuenta in (").concat(ctasAdicionales).concat(") ) and id_tipo_programa = :param or id_cuenta=955"),request.getParameter("idProgramaS"));
                 else
                    crsCtaBanCGC.addParamVal("tipoCuenta"," id_tipo_cta='1' and (unidad_ejecutora='".concat(Autentifica.getUnidadEjecutora()).concat("' or id_cuenta in (").concat(ctasAdicionales).concat(")) and id_tipo_programa = :param"),request.getParameter("idProgramaS"));
                 crsCtaBanCGC.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
                 crsCtaBanCSP.addParamVal("tipoCuenta"," id_tipo_cta='2' and (unidad_ejecutora='".concat(Autentifica.getUnidadEjecutora()).concat("' or id_cuenta in (").concat(ctasAdicionales).concat(")) and id_tipo_programa = :param"),request.getParameter("idProgramaS"));
                 crsCtaBanCSP.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
                 crsCtaBanENL.addParamVal("tipoCuenta"," id_tipo_cta='7' and (unidad_ejecutora='".concat(Autentifica.getUnidadEjecutora()).concat("' or id_cuenta in (").concat(ctasAdicionales).concat(")) and id_tipo_programa = :param"),request.getParameter("idProgramaS"));
                 crsCtaBanENL.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
                 crsCtaBanIXV.addParamVal("tipoCuenta"," id_tipo_cta='6' and (unidad_ejecutora='".concat(Autentifica.getUnidadEjecutora()).concat("' or id_cuenta in (").concat(ctasAdicionales).concat(")) and id_tipo_programa = :param"),request.getParameter("idProgramaS"));
                 crsCtaBanIXV.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
                 crsCtaBanDBM.addParamVal("tipoCuenta"," 1=2 and id_tipo_programa = :param",request.getParameter("idProgramaS"));
                 crsCtaBanDBM.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
            }
            break;
        case 3:
            crsCtaBanDGC.addParamVal("tipoCuenta"," ambito=3 and id_tipo_cta='4' and (entidad=".concat(Autentifica.getEntidad()).concat(" or id_cuenta in (").concat(ctasAdicionales).concat(")) and id_tipo_programa = :param"),request.getParameter("idProgramaS"));
            crsCtaBanDGC.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
            crsCtaBanDSP.addParamVal("tipoCuenta"," ambito=3 and id_tipo_cta='5' and (entidad=".concat(Autentifica.getEntidad()).concat(" or id_cuenta in (").concat(ctasAdicionales).concat(")) and id_tipo_programa = :param"),request.getParameter("idProgramaS"));
            crsCtaBanDSP.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
            if (request.getParameter("idProgramaS").equals("7"))
                crsCtaBanCGC.addParamVal("tipoCuenta"," ambito=3 and id_tipo_cta='1' and (entidad=".concat(Autentifica.getEntidad()).concat(" or id_cuenta in (").concat(ctasAdicionales).concat(")) and id_tipo_programa = :param or id_cuenta=955"),request.getParameter("idProgramaS"));
            else
                crsCtaBanCGC.addParamVal("tipoCuenta"," ambito=3 and id_tipo_cta='1' and (entidad=".concat(Autentifica.getEntidad()).concat(" or id_cuenta in (").concat(ctasAdicionales).concat(")) and id_tipo_programa = :param"),request.getParameter("idProgramaS"));
            crsCtaBanCGC.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
            crsCtaBanCSP.addParamVal("tipoCuenta"," ambito=3 and id_tipo_cta='2' and (entidad=".concat(Autentifica.getEntidad()).concat(" or id_cuenta in (").concat(ctasAdicionales).concat(")) and id_tipo_programa = :param"),request.getParameter("idProgramaS"));
            crsCtaBanCSP.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
            crsCtaBanENL.addParamVal("tipoCuenta"," ambito=3 and id_tipo_cta='7' and (entidad=".concat(Autentifica.getEntidad()).concat(" or id_cuenta in (").concat(ctasAdicionales).concat(")) and id_tipo_programa = :param"),request.getParameter("idProgramaS"));
            crsCtaBanENL.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
            crsCtaBanIXV.addParamVal("tipoCuenta"," ambito=2 and id_tipo_cta='6' and (unidad_ejecutora=".concat(Autentifica.getUnidadEjecutora()).concat(" or id_cuenta in (").concat(ctasAdicionales).concat(")) and id_tipo_programa = :param"),request.getParameter("idProgramaS"));
            crsCtaBanIXV.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
            crsCtaBanDBM.addParamVal("tipoCuenta"," 1=2 and id_tipo_programa = :param",request.getParameter("idProgramaS"));
            crsCtaBanDBM.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reportesMovimientos");
        }
     }
 
%>
  <body onload="ocultarCajita()" >
    <form id="form1" name="form1" action="tesFiltroCtaBancariaControl.jsp" method="POST">
      <%util.tituloPagina(out,"Tesorería","Filtro cuentas bancarias","Generador de reportes",true);%>
      <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
      <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
      <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
      <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
      <input type="hidden" id="nomReporte" name="nomReporte" >
      <input type="hidden" id="numReporte" name="numReporte">
      <input type="hidden" id="tipoReporte" name="tipoReporte">
      <input type="hidden" id="tipAdmon" name="tipAdmon"  value="<%=request.getParameter("administrador")%>"> 
      <table>
        <thead></thead>
        <tbody>
          <tr align="left">
            <td style="color:rgb(0,99,148); font-style:italic; font-weight:bold;"><%=request.getParameter("opcion")%> >> <%=request.getParameter("nomProg")%></td>
          </tr>
        </tbody>
      </table>
      <br>
      <br>
      <table align="center">
      <thead></thead>
      <tbody>
        <tr>
          <td>Información a consultar:</td>
          <td>
            <select class="cajaTexto" id="listaReporte" name="listaReporte" onchange="guardaNomRep();ocultarCajita()">
             <%=cdgHTMLList%>
            </select>
          </td>
        </tr>
      </tbody>
      </table>
      <br>
      <br>
      <table width="80%" align="center" >
      <thead></thead>
      <tbody>
        <tr>
          <td align="left">Cuentas Bancarias</td>
        </tr>
      </tbody>
      </table>
      <table width="80%" style="background-color:transparent; border-color:rgb(0,132,198); border-style:solid; border-width:2.0px;" align="center">
      <thead></thead>
      <tbody>
        <tr> 
          <td> 
            <table>
            <thead></thead>
            <tbody>
                <%if(crsCtaBanDGC.size()>0){%>                  
                <tr>
                  <td></td><td></td><td></td>
                </tr>
                <tr>
                  <td></td><td>Dispersoras de gasto corriente:</td><td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><input type="checkbox" name="cbDispGC" id="cbDispGC" onclick="dispersorasGC()"> Todas las cuentas dispersoras de gasto corriente</td>
                  <td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><select  class="cajaTexto" id="smDispGC" name="smDispGC" multiple="multiple" onclick="noSeleccionaDispGC()"> 
                        <%CRSComboBox(crsCtaBanDGC, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                      </select>
                  </td><td></td>
                </tr>
                <%}%>
                <%if(crsCtaBanDSP.size()>0){%>
                <tr>
                  <td height="20"></td><td></td><td></td>
                </tr>
                <tr>
                  <td></td><td>Dispersoras de servicios personales:</td><td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><input type="checkbox" name="cbDispSP" id="cbDispSP" onclick="dispersorasSP()"> Todas las cuentas dispersoras de servicios personales</td>
                  <td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><select  class="cajaTexto" id="smDispSP" name="smDispSP" multiple="multiple" onclick="noSeleccionaDispSP()"> 
                        <%CRSComboBox(crsCtaBanDSP, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                      </select>
                  </td><td></td>
                </tr>
                <%}%>
                <%if(crsCtaBanCGC.size()>0){%>
                <tr>
                  <td height="20"></td><td></td><td></td>
                </tr>
                <tr>
                  <td></td><td>Chequeras de gasto corriente:</td><td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><input type="checkbox" name="cbDChqGC" id="cbChqGC" onclick="chequerasGC()"> Todas las chequeras de gasto corriente</td>
                  <td></td>
                </tr>
                <tr>
                  <td></td> 
                  <td><select  class="cajaTexto" id="smChqGC" name="smChqGC" multiple="multiple" onclick="noSeleccionaChqGC()"> 
                        <%CRSComboBox(crsCtaBanCGC, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                      </select>
                  </td><td></td>
                </tr>
                <%}%>
                <%if(crsCtaBanCSP.size()>0){%>
                <tr>
                  <td height="20"></td><td></td><td></td>
                </tr>
                <tr>
                  <td></td><td>Chequeras de servicios personales:</td><td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><input type="checkbox" name="cbChqSP" id="cbChqSP" onclick="chequerasSP()"> Todas las chequeras de servicios personales</td>
                  <td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><select  class="cajaTexto" id="smChqSP" name="smChqSP" multiple="multiple" onclick="noSeleccionaChqSP()"> 
                        <%CRSComboBox(crsCtaBanCSP, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                      </select>
                  </td><td></td>
                </tr>
                <%}%>
                <%if(crsCtaBanENL.size()>0){%>
                <tr>
                  <td height="20"></td><td></td><td></td>
                </tr>
                <tr>
                  <td></td><td>Enlace para internet y cheque inteligente:</td><td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><input type="checkbox" name="cbEnlace" id="cbEnlace" onclick="enlace()"> Todas las cuentas dispersoras de servicios personales</td>
                  <td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><select  class="cajaTexto" id="smEnlace" name="smEnlace " multiple="multiple" onclick="noSeleccionaEnlace()"> 
                        <%CRSComboBox(crsCtaBanENL, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                      </select>
                  </td><td></td>
                </tr>
                <%}%>
                <%if(crsCtaBanIXV.size()>0){%>
                <tr>
                  <td height="20"></td><td></td><td></td>
                </tr>
                <tr>
                  <td></td><td>Ingresos por venta:</td><td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><input type="checkbox" name="cbIngXV" id="cbIngXV" onclick="ingresosXV()"> Todas las cuentas dispersoras de ingresos por venta</td>
                  <td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><select  class="cajaTexto" id="smIngXV" name="smIngXV" multiple="multiple" onclick="noSeleccionaIngXV()"> 
                        <%CRSComboBox(crsCtaBanIXV, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                      </select>
                  </td><td></td>
                </tr>
                <%}%>
                <%if(crsCtaBanDBM.size()>0){%>
                <tr>
                  <td height="20"></td><td></td><td></td>
                </tr>
                <tr>
                  <td></td><td>Donativo banco mundial:</td><td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><input type="checkbox" name="cbDonativo" id="cbDonativo" onclick="donativo()"> Todas las cuentas donativos banco mundial</td>
                  <td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><select  class="cajaTexto" id="smDonativo" name="smDonativo" multiple="multiple" onclick="noSeleccionaDon()"> 
                        <%CRSComboBox(crsCtaBanDBM, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                      </select>
                  </td><td></td>
                </tr>
                <%}%>
            </tbody>
            </table>
          </td>
        </tr>
      </tbody>
      </table>
      <br>
      <br>
      <table align="center">
        <thead></thead>
        <tbody>
          <tr>
            <td align="right">Periodo del: </td>
            <td>
              <table align='left'>
              <thead></thead>
                <tbody>
                <tr >
                  <td id="colFI"><input type="text" name="fechaInicio" id="fechaInicio" value="<%=fechaUltCarga%>" class="cajaTexto" readonly size="13">
                      <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                      onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaInicio')">
                      <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a> al: </td>
                  <td><input type="text" name="fechaFinal" id="fechaFinal" value="<%=fechaUltCarga%>" class="cajaTexto" readonly size="13">
                     <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                     onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaFinal')">
                     <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
                </tr>
                </tbody>
              </table>
             </td>
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
          <td><input type="button" class="boton" value="Generar PDF" onclick="asignaValorRep('pdf')"></td>
          <td><input type="button" class="boton" value="Generar XLS" onclick="asignaValorRep('xls')"></td>
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>