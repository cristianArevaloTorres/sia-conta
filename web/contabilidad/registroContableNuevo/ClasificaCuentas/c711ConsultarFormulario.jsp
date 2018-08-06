<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*, java.io.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="Autentifica"  class="sia.beans.seguridad.Autentifica" scope="session"/>  

<%
   // obtiener numero de cuenta
   String lsNCta=request.getParameter("NCta");
%>

<jsp:useBean id="pbClasificaCuentas" class = "sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas" scope="page"/>
<jsp:useBean id="abCfgCves"  class="sun.jdbc.rowset.CachedRowSet" scope="page"/>

<%
  // Lectura. de RF_TC_CLASIFICADOR_CUENTAS - RF_TR_CONFIGURA_CLAVES, para el llenado de tabla de niveles de cuenta
  /**
    * Descripcion: Metodo que lee la informacion de RF_TR_CONFIGURA_CLAVES y RF_TR_DETALLE_CONF_CVE, PARA LLENAR DETALLE DE CLASIFICADOR DE CUENTAS
    * <p>Fecha de creación: 05/ene/2011               </p>
    * <p>Autor: Juan Santos Jiménez A.                   </p>
    * <p> Última modificación:                                   </p>
    * <p> Fecha de última modificación:                   </p>
    * <p> Autor última modificación:                          </p>
    * @param: bean de seguridad para hacer las transacciones con la BD),param. de entrada 
    *  
  */ 
  abCfgCves.setTableName("rf_tc_clasificador_cuentas");
  String SQL = "SELECT RF_TR_CONFIGURA_CLAVES.ID_CONF_CVE AS ID_CONF_CVE,RF_TR_CONFIGURA_CLAVES.LONGITUD AS LONGITUD, RF_TR_CONFIGURA_CLAVES.NIVEL_OPERACION AS NIVEL_OPERACION, "+
               " RF_TC_CLASIFICADOR_CUENTAS.CUENTA_MAYOR AS CUENTA_MAYOR, RF_TR_DETALLE_CONF_CVE.TAMANIO, RF_TR_DETALLE_CONF_CVE.POSICION, " +
               " RF_TR_DETALLE_CONF_CVE.NIVEL_CUENTA_ID, RF_TR_DETALLE_CONF_CVE.CODIGO "+
               " FROM RF_TR_CONFIGURA_CLAVES INNER JOIN " +
               " RF_TC_CLASIFICADOR_CUENTAS ON RF_TR_CONFIGURA_CLAVES.ID_CONF_CVE = RF_TC_CLASIFICADOR_CUENTAS.CONF_CVE_CTA_CONT_ID INNER JOIN "+
               " RF_TR_DETALLE_CONF_CVE ON RF_TR_CONFIGURA_CLAVES.ID_CONF_CVE = RF_TR_DETALLE_CONF_CVE.ID_CONF_CVE"+
               " WHERE (RF_TC_CLASIFICADOR_CUENTAS.CUENTA_MAYOR = '"+lsNCta+"') "; 

 // System.out.println(SQL);             
  abCfgCves.setCommand(SQL);
  Connection con=null;
  try{
     con = DaoFactory.getContabilidad();
     abCfgCves.execute(con);
   }
     catch(Exception e){
       System.out.println("Ocurrio un error al crear CachedRowSet de Configura_Claves en c711ModificarFormulario.jsp "+e.getMessage());
     } //Fin catch
     finally{
       if (con!=null){
         con.close();
         con=null;
       }
     } //Fin finally
%>

<%
   Connection conexion=null;
   
   String lsDescrip=null;
   String lsFecVigI=null;
   String lsFecVigF=null;
   
  try{
        conexion=DaoFactory.getContabilidad();
        // obtener datos generales de Cuenta Mayor 
        pbClasificaCuentas.select_RF_TC_CLASIFICADOR_CUENTAS(conexion,lsNCta);
        lsDescrip  = pbClasificaCuentas.getDescripcion();
        lsFecVigI  = pbClasificaCuentas.getFecha_vig_ini();
        lsFecVigF = pbClasificaCuentas.getFecha_vig_fin();
        
%>

<html>
  <head>
    <meta http-equiv="Content-Language" content="es-mx">
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title>c711ConsultarFormulario</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
    <script language="JavaScript" type="text/javascript">
        var lslong =0;
        var ctaMay="";
        ctaMay=<%=lsNCta%>;
 
        function cargaNivCta() {
<%  String IdConfCve="";
       String Tamanio = "";
       String Codigo   = "";
       int indice=0;
       
       abCfgCves.beforeFirst();
       int i=0;
       while (abCfgCves.next()){ 
                   IdConfCve=abCfgCves.getString(1);
                   Tamanio=abCfgCves.getString(5);
                   Codigo=abCfgCves.getString(8);
                    if(!IdConfCve.equals("")){
%>
                        var   Tam ="<%=Tamanio%>";
                        var   Cod ="<%=Codigo%>";
                        lslong = lslong+parseInt(Tam);
                        AgregaNiv(Tam,Cod);
                        
<%                   indice++;
                        if(indice > 1 ){
%>
//                           Formulario.txtTamanio[<%=i%>].value  = Tam;
//                           Formulario.txtCodigo[<%=i%>].value     = Cod;
<%
                       }
                       i++;
                    }
       }   
       abCfgCves.close();
       abCfgCves = null;
%>

        } //fin_cargaNivCta()
       
        function validaCodigos(tuCampoTexto){
            if(/^[0-9,".",","]+$/.test(tuCampoTexto)) return true
                return false
        } // fin_validaCodigos();
        
        function Valida(){
            ok=true;
            mess="AVISO:\n";
            
            var TabINEGI = document.getElementById("TablaNiv");
            var lReng = TabINEGI.rows.length; //Obtiene los renglones de la Tabla
       
            if(lReng < 2 ){
               ok=false;
               mess=mess+"Debe agregar información de los niveles. \n";       
             }else {
                         if(lReng > 2){
                            for(var i=0; i<(lReng-1); i++){
                                   if(isEmpty(Formulario.txtCodigo[i].value)){     
                                      ok=false;
                                      mess=mess+"Debe capturar el código en posición "+(i+1)+" \n";
                                   }else {
                                              // validar si es numerico y contiene separador
                                              if(!validaCodigos (Formulario.txtCodigo[i].value)){
                                                  ok=false;
                                                  mess=mess+"Debe capturar código válido (digit, ',' como separador ó '.' para rangos) en posición "+(i+1)+" \n";                                                 
                                              }
                                   }
                             }//for
                         }
            }

            if (ok==true)
                return true;
            else{
                 alert(mess)
                 return false;
            }        
        } // fin_Valida()

        function Enviar(){
            if(Valida()){
               Formulario.submit();
            }
        } // fin_Enviar()

        
        function asignaCta(){
              ctaMay=<%=lsNCta%>
        } //fin_asignaCta()
        
function AgregaNiv( tam, cod){
    var Tabla = document.getElementById("TablaNiv");
    var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
    var fila = Tabla.insertRow(lRenglones);
    var celda = fila.insertCell(0);

    var txtN  = document.createTextNode(lRenglones);

    var txt = document.createTextNode(tam);

    switch (lRenglones) {
            case 1 : var nivCta = document.createTextNode("Cuenta de Mayor"); break;
            case 2 : var nivCta = document.createTextNode("subcuenta de Mayor"); break;
            case 3 : var nivCta = document.createTextNode("Programa"); break;
            case 4 : var nivCta = document.createTextNode("Unidad Responsable"); break;
            case 5 : var nivCta = document.createTextNode("Ámbito"); break;
            default :
                          var nivCta = document.createTextNode("Nivel "+lRenglones); break;
    }

             var txtCo   = document.createElement("textarea");
             txtCo.cols  = "120";
             txtCo.value = cod;
             txtCo.name  ="txtCodigo";
             if (cod.length > 120)
                  txtCo.rows  = "5";
             else
                  txtCo.rows  = "1";             
             txtCo.id    = "txtCodigo";
             txtCo.className="cajaTexto"; 
             txtCo.disabled=1;

 
    celda = fila.insertCell(0);
    celda.appendChild(txtN);
    celda = fila.insertCell(1);
    celda.appendChild(txt);
    celda = fila.insertCell(2);
    celda.appendChild(nivCta);txtCo
    celda = fila.insertCell(3);
    celda.appendChild(txtCo);
    
    Formulario.txtNivel.focus();
    Formulario.txtNivel.value = lRenglones;
    
    if (lRenglones == 1){
        Formulario.txtLong.focus();
        Formulario.txtLong.value = "4";
    }
    if (lRenglones == 2){
        Formulario.txtLong.focus();
        Formulario.txtLong.value = "1";
    }
    
   ponTam();
    
}//Fin_AgregaNiv()

//funcion que borra renglones de la tabla
function Borra(i){
   var Tabla = document.getElementById("TablaNiv");
   Tabla.deleteRow(i);
} //fin_Borra()

  function enviar(){
     document.form1[0].submit();
  } //fin_enviarr()

function ponTam(){
/*  
   var Tabla = document.getElementById("TablaNiv");
   var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
   var tam=0;
    if(lRenglones >2){//es mas de un elemento
       for(i=0; i<(lRenglones-1); i++){
             tam=tam+parseInt(Formulario.txtTamanio[i].value);
       }
       Formulario.txtLong.value=tam;
    }
*/
       Formulario.txtLong.value=lslong;

} //fin_ponTam()


function validaTam(ind){
   var Tabla = document.getElementById("TablaNiv");
   var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla

   if(lRenglones > 2){
      if((parseInt(Formulario.txtTamanio[ind-1].value) < 4)  || (parseInt(Formulario.txtTamanio[ind-1].value)  > 6)){
           alert("Debe capturar el tamaño de cuenta válido");
           Formulario.txtTamanio[ind-1].focus();
           Formulario.txtTamanio[ind-1].value="4";
      }else
         ponTam();
  }
 }//fin_validaTam()
 

</script>
  </head>
     <body onload="cargaNivCta();">
          <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", "Clasificador de Cuentas", "[Consultar]","Formulario", true);</jsp:scriptlet>
     	 <form name="Formulario" action="" method="post" >
            <table cellspacing="2" cellpadding="3" border="0" width="100%">
                <thead>
                    <tr> 
                        <td width="46%" align="left"><div align="right">Cuenta de Mayor:</div></td>
                        <td width="54%"><%=lsNCta%> </td>
                    </tr>
                    <tr> 
                        <td width="46%" align="left"><div align="right">Descripci&oacute;n:</div></td>
                        <td width="54%"><%=lsDescrip%></td>
                    </tr>
                </thead>
            </table>
            
            <table cellspacing="3" cellpadding="2" border="0" align="left" width="100%">
                <thead>
                    <tr> 
                        <td width="46%" align="left"><div align="right">&nbsp;Fecha de Inicio:</div></td>
                        <td width="54%"><%=lsFecVigI%></td>
                    </tr>
                     <tr> 
                         <td width="46%" align="left"><div align="right">&nbsp;Fecha de Fin</div></td>
                        <td><%=lsFecVigF%></td>
                    </tr>
                    <tr> 
                        <td width="46%" align="left"><div align="right">&nbsp;Longitud:</div></td>
                        <td> <input type="text" name="txtLong" maxlength="2" size="5" value="" class="cajaTexto" readonly="readonly"> </td>
                    </tr>
                    <tr> 
                        <td width="46%" align="left"><div align="right">Nivel de Operaci&oacute;n:</div></td>
                        <td> <input type="text" name="txtNivel" maxlength="2" size="5"  value="" class="cajaTexto" readonly="readonly"> </td>
                    </tr>
                    <tr> 
                        <td width="46%"><div align="right"></div></td>
                        <td>&nbsp;</td>
                    </tr>
                </thead>
            </table>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            
            <hr noshade size="5"/>
            
            <table id='TablaNiv' border='0' width="100%">
                <thead class="tabla01">
                    <tr> 
                        <td width="8%" height="21"><div align="left">Nivel</div></td>
                        <td width="8%"><div align="left">Tama&ntilde;o</div></td>
                        <td width="15%"><div align="left">Nivel de Cuenta</div></td>
                        <td width="69%"><div align="left">C&oacute;digos</div></td>
                    </tr>
                </thead>
            </table>
            <p>&nbsp;</p>
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
                <thead>           
                    <tr>
                        <td>
                                &nbsp;
                        </td>
                        <td width="5">&nbsp;</td>
                   	<td>
                            <div align="center">
                                <input type="button" name="btnCancelar " value=" Regresar" class='boton' onClick="javascript:LlamaPagina('c711ConsultarFiltro.jsp','');">                        
                            </div>
                        </td>
                    </tr>
                </thead>           
            </table>
	</form>
<%
    }
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar  "+e.getMessage());
    } //Fin catch
    finally{
       if (conexion!=null){
         conexion.close();
         conexion=null;
       }
     } //Fin finally

%>
    </body>
</html>