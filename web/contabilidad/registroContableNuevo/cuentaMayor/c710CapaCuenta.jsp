<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*"%>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session"/>
<jsp:useBean id="pbCuenta" class="sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas" scope="page"/>
<%@ include file="../../../Librerias/Funciones/utilscpv.jsp"%>
<body onload="parent.loadSourceFinish('capaCuenta', parent.bufferFrame1);parent.regresa();">
<script language="JavaScript"      type="text/javascript">



</script><%
   Connection conexion=null;
 try{
    conexion=DaoFactory.getContabilidad();
    
    String lsGenero = request.getParameter("cveGenero");
    String lsGpo = request.getParameter("cveGpo");  
    String lsClase = request.getParameter("cveClase");  
    
    if (lsGpo.equals("null") ){       
      lsGpo=""; }
      
    if (lsClase.equals("null") ){
      lsClase=""; }
             
    pbCuenta.selectMaxCta(conexion,lsGenero,lsGpo,lsClase); 
    
    int NumMax;   
    String lsNumCons="";
    String lsNumCta="";                                                                                                                                                                                                                                                                                                                                                                                                                                               
    lsNumCons = pbCuenta.getNumCons();
                
    if (lsNumCons.equals("") || lsNumCons==null || lsNumCons=="0"){
       NumMax=0;             
    }
    else{
       NumMax=Integer.parseInt(lsNumCons);      
    }        
    
    NumMax++;   
        
    lsNumCons=Integer.toString(NumMax);   
    lsNumCons=(lsNumCons.substring(0,1));    

    lsNumCta=lsGenero+lsGpo+lsClase+lsNumCons;
    
%>
<table width="100%">
        <tr><td width="36%"><div align="right">Cuenta:</div></td>
            <td><div align="left"><input type="text" class="cajaTexto" name="txtCta" size=3 maxlength=1 value="<%=lsNumCons%>" onBlur="CambiaCuenta();">
            </input></div></td>
        </tr>
        <tr><td><div align="right">N&uacute;mero de la cuenta:</div></td>
            <td><div align="left"><input type="text" class="cajaTexto" name="txtCve" size=3 maxlength=4 value="<%=lsNumCta%>" readonly="readonly">
            </input></div></td>
        </tr>
</table>
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
%></body>