<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*"%>
<%@ page import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="abCtasMayor" class="sun.jdbc.rowset.CachedRowSet" scope="page"/>

<%
  //para el llenado de combo con inf. de RF_TC_CLASIFICADOR_CUENTAS - RF_TR_CONFIGURA_CLAVES, sólo los que esten en configura_claves
  /**
    * Descripcion: Metodo que lee la informacion de RF_TC_CLASIFICADOR_CUENTAS QUE 
    *  EXISTE NE RF_TR_CONFIGURA_CLAVES, PARA LLENAR COMBO DE FILTRO
    * <p>Fecha de creación: 10/dic/2010               </p>
    * <p>Autor: Juan Santos Jiménez A.                </p>
    * <p> Última modificación:                        </p>
    * <p> Fecha de última modificación:               </p>
    * <p> Autor última modificación:                  </p>
    * @param: bean de seguridad para hacer las transacciones con la BD),param. de entrada 
    *  
  */ 
  abCtasMayor.setTableName("rf_tc_clasificador_cuentas");
  String SQL = "SELECT RF_TC_CLASIFICADOR_CUENTAS.CUENTA_MAYOR_ID, RF_TC_CLASIFICADOR_CUENTAS.CUENTA_MAYOR, "+
               " RF_TC_CLASIFICADOR_CUENTAS.DESCRIPCION " +
               " FROM RF_TC_CLASIFICADOR_CUENTAS " +
               " WHERE conf_cve_cta_cont_id <> 0  " +
               " ORDER BY CUENTA_MAYOR "; 

//  System.out.println(SQL);             
  abCtasMayor.setCommand(SQL);
  Connection con=null;
  try{
     con = DaoFactory.getContabilidad();
     abCtasMayor.execute(con);
   }
     catch(Exception e){
       System.out.println("Ocurrio un error al crear CachedRowSet de CtasMayor en c711ConsultarFiltro.jsp "+e.getMessage());
     } //Fin catch
     finally{
       if (con!=null){
         con.close();
         con=null;
       }
     } //Fin finally
%>

<html>
  <head>
    <meta http-equiv="Content-Language" content="es-mx">
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title>c711ConsultarFiltro</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>

    <script language="JavaScript" type="text/javascript">
        function Valida(){
            ok=true;
            mess="AVISO:\n";
            
            if(!isEmpty(Filtro.txtNumero.value)){
                if(Filtro.txtNumero.value.length != 4 ){
                   ok=false; mess=mess+'Número de Cuenta debe ser de 4 carácteres\n';
                }
                // revisar si es numérico
                if(!isInteger(Filtro.txtNumero.value)){
                    ok=false; mess=mess+'Número de Cuenta debe ser numérico\n';
                }
            }
            if(!isEmpty(Filtro.txtNombre.value)){
                if (Filtro.txtNombre.value.length >100){
                    ok=false; mess=mess+'Nombre de la Cuenta no debe ser mayor de 100 Pos.\n'; 
                }
            }        
            if (ok==true)
                return true;
            else{
                 alert(mess)
                 return false;
            }        
        }

        function Enviar(){
            if(Valida()){
               Filtro.submit();
            }
        }
    </script>
  </head>
  <body>
  <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", "Clasificador de Cuentas", "[Consultar]","Filtro", true);</jsp:scriptlet>

     <form name="Filtro" id="Filtro" method="post" action="c711ConsultarResultado.jsp">
      <table width="100%" border="0">
         <tr> 
            <td width="479" height="30"> <div align="left"></div></td>
            <td width="553">&nbsp; </td>
         </tr>
         <tr> 
            <td><div align="right"></div></td>
            <td>&nbsp;</td>
         </tr>
         <tr> 
            <td height="26"> <div align="right">Cuentas de mayor:</div></td>
            <td class="negrita">
                <select name="lstCtasMayor" class="cajaTexto">
                   <option value="null" selected="selected"> - Seleccione - </option>
                       <%  String IdCM="";
			   String CtaMay="";
                           String Descrip="";
                           int i=0;
                           abCtasMayor.beforeFirst();
                           while (abCtasMayor.next()){ 
                                  IdCM=abCtasMayor.getString(1);
                                  CtaMay=abCtasMayor.getString(2);
				  Descrip=abCtasMayor.getString(3);
                                  if(!IdCM.equals("")){
                       %>
                   <OPTION VALUE="<%=IdCM%>"><%=CtaMay%>&nbsp;<%=Descrip%></OPTION>
                       <%          
                                  } else { %>
                   <OPTION selected="selected" VALUE="<%=IdCM%>"> </OPTION>                      
                       <%         }
                           }   
                           abCtasMayor.close();
                           abCtasMayor = null;
                       %>
                </select>
            </td>
         </tr>
         <tr> 
            <td>
               <div align="right">N&uacute;mero de cuenta:</div>
            </td>
            <td width="553">
               <input name="txtNumero" type="text"  class="cajaTexto" id="txtNumero" size="10" maxlength="4" />
            </td>
         </tr>
         <tr> 
            <td> <div align="right">Nombre:</div></td>
            <td><input type="text" name="txtNombre" maxlength="100" size="60"  class="cajaTexto" /></td>
         </tr>
         <tr> 
            <td><div align="right">Naturaleza:</div></td>
            <td><select name="lstNaturaleza" class="cajaTexto">
               <option value="00" SELECTED>- Seleccione -</option>
               <option value="01">Deudora</option>
               <option value="02">Acreedora</option>
            </select></td>
         </tr>
         <tr> 
            <td height="21"> <div align="left"></div></td>
            <td>&nbsp;</td>
         </tr>
      </table> 

      <br>
      <table width="100%">
         <tr>
            <td width="47%">
               <div align="right">
                  <input type="button" name="btnAceptar" value="Aceptar" class=boton onclick="Enviar()" >
               </div>
            </td>
            <td width="53%" align="right">
               <div align="left">
                  <input type="button" name="btnRegresar" value="Regresar" class=boton onClick="javascript:LlamaPagina('c711Menu.jsp','');">
               </div>
            </td>
         </tr>
      </table>
   </form>
 </body>
</html>