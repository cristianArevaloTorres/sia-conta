<%-- 
    Document   : c745Formulario
    Created on : 12/07/2013, 10:56:35 AM
    Author     : EST.JOSE.FLORES
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.util.*,java.sql.*"%>
<%@page import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="consultaGenero" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   consultaGenero.setTableName("rf_tc_genero_clasf_cta");
   String SQL2 = "select id_genero, descripcion from rf_tc_genero_clasf_cta order by id_genero ";
   consultaGenero.setCommand(SQL2);
   Connection con2=null;
   try{
     con2 = DaoFactory.getContabilidad();
     consultaGenero.execute(con2);
   } catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo consultaGenero "+e.getMessage());
   } finally{
      if (con2!=null){
          con2.close();
          con2=null;
      }
   } //Fin finally
 %>
</jsp:useBean>

<jsp:useBean id="consultaGrupo" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   consultaGrupo.setTableName("rf_tc_grupo_clasf_cta");
   String SQL = " select gen.id_genero, gen.descripcion des_genero, gru.id_grupo, gru.descripcion des_grupo ". 
                concat(" from rf_tc_genero_clasf_cta gen, rf_tc_grupo_clasf_cta  gru "). 
                concat(" where gen.id_genero = gru.id_genero ").
                concat(" order by gen.id_genero, gru.id_grupo ");
   consultaGrupo.setCommand(SQL);
   Connection con =null;
   try{
     con = DaoFactory.getContabilidad();
     consultaGrupo.execute(con);
   } catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo consultaGenero "+e.getMessage());
   } finally{
      if (con!=null){
          con.close();
          con=null;
      }
   } //Fin finally
 %>
 </jsp:useBean>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>c745Formulario</title>
      <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
      <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
      <script language="JavaScript">
            function regresar(){ 
                window.location.href="../configuracionCuenta/c745Filtro.jsp"; 
            } 
 
            function validaDescripcion(){
                var x= 0;
            }      

            function fcapaDescripcion(tipo,operacion){
                if (tipo =='genero' && operacion !='Agregar'){
                    loadSource('capaDescripcionGenero',null,'c745CapaDescripcionGenero.jsp?idGenero='+document.forms.filtroGenero.lstGenero.value,bufferFrame7);
                    return true;    
                }
                if (tipo =='grupo'  && operacion !='Agregar'){
                    loadSource('capaDescripcionGrupo',null,'c745CapaDescripcionGrupo.jsp?idGenero='+document.forms.filtroGrupo.lstGenero2.value+'&idGrupo='+document.forms.filtroGrupo.lstGrupo2.value,bufferFrame8); 
                    return true;
                }
                if (tipo =='clase' && operacion !='Agregar'){
                    loadSource('capaDescripcion',null,'c745CapaDescripcion.jsp?idGenero='+document.forms.filtroClase.lstGenero3.value+'&idGrupo='+document.forms.filtroClase.lstGrupo3.value+'&idClase='+document.forms.filtroClase.lstClase.value,bufferFrame9);  
                    return true;
                }
            }
 
            function preguntaConfirmacion(tipo,operacion) {
                if (tipo =='genero' && operacion=='Eliminar'){
                    mensaje = '¿Está seguro que desea eliminar el genero seleccionado?';
                    if (confirm(mensaje)) {
                        if(validar(tipo, operacion)){
                            document.forms.filtroGenero.submit();
                            return true;
                        } else return false;
                    } else return false;
                }
                if (tipo =='grupo' && operacion=='Eliminar'){
                    mensaje = '¿Está seguro que desea eliminar el grupo seleccionado?';
                    if (confirm(mensaje)) {
                        if(validar(tipo, operacion)){
                            document.forms.filtroGrupo.submit();
                            return true;
                        } else return false;    
                    } else return false;
                }
                if (tipo =='clase' && operacion=='Eliminar'){
                    mensaje = '¿Está seguro que desea eliminar el rubro seleccionado?';
                    if (confirm(mensaje)) {
                        if(validar(tipo, operacion)){
                            document.forms.filtroClase.submit();
                            return true;
                        } else return false;     
                    } else return false;
                }
            }//Fin preguntaConfirmacion
                     
            function mostrarDivs(id){
                var elDiv = document.getElementById(id);
                elDiv.style.display='block';
            }
           
            function inicializa(){
                ComponerListaPro (document.forms.filtroClase.lstGenero3[0].value);//Lista-Padre
                filtroClase.lstGrupo3.options[1].selected = true;//Lista-Hija
            }
            
            function validar(formulario, operacion){
                mensaje= '------------------ ALERTA ------------------\n';  
                var aprobado = true;
                if( formulario == 'genero' ){
                    var genero = document.forms.filtroGenero.lstGenero.value;
                    var valorCampo;
                    
                    if(operacion=='Agregar'){
                        valorCampo  = document.forms.filtroGenero.txtGenero.value;
                    }
                    if((operacion=='Modificar' ) && genero!='-1'){
                        valorCampo  = document.forms.filtroGenero.txtGenero.value;
                    }
                    if(genero=='-1'){
                       mensaje+='Por favor seleccione un ID para el Género';
                       alert(mensaje);
                       mensaje='';
                       aprobado = false;
                    }
                    if(valorCampo=='' && operacion=='Agregar' ){
                       mensaje+='Por favor escriba en el campo Descripción';
                       alert(mensaje);
                       mensaje='';
                       aprobado = false;
                   }
                }
                
                if( formulario == 'grupo' ){
                   var genero = document.forms.filtroGrupo.lstGenero2.value;
                   var grupo  = document.forms.filtroGrupo.lstGrupo2.value;
                   var valorCampo; 
                   
                   if(operacion=='Agregar'){
                      valorCampo = document.forms.filtroGrupo.txtGrupo.value;
                   }
                   if((operacion=='Modificar' || operacion=='Eliminar') &&( genero !='-1' && grupo !='-1' )){
                        valorCampo  = document.forms.filtroGrupo.txtGrupo.value;
                   }               
                   if(genero == '-1'){
                       mensaje+='Por favor seleccione un ID para el Genero';
                       alert(mensaje);
                       menaje='';
                       mensaje= '------------------ ALERTA ------------------\n';
                       aprobado = false;
                   }                   
                   if(grupo == '-1'){
                       mensaje+='Por favor seleccione un ID para el Grupo';
                       alert(mensaje);
                       menaje='';
                       mensaje= '------------------ ALERTA ------------------\n';
                       aprobado = false;
                   }
                   if(valorCampo=='' && operacion=='Agregar'){
                       mensaje+='Por favor escriba en el campo Descripción';
                       alert(mensaje);
                       menaje='';
                       mensaje= '------------------ ALERTA ------------------\n';
                       aprobado = false;
                   }
                }
                
                if( formulario == 'clase'){
                    var clase = document.forms.filtroClase.lstClase.value;
                    var valorCampo;
                    
                    if(operacion=='Agregar'){
                        valorCampo = document.forms.filtroClase.txtClase.value;
                    }
                    if((operacion=='Modificar' || operacion=='Eliminar') && clase !='-1'){
                        valorCampo  = document.forms.filtroClase.txtClase.value;
                    }
                    if(clase == '-1' ){
                        mensaje+='Por favor seleccione el Rubro';
                        alert(mensaje);
                        menaje='';
                        mensaje= '------------------ ALERTA ------------------\n';
                        aprobado = false;
                    }
                    if(valorCampo=='' && operacion=='Agregar'){
                       mensaje+='Por favor escriba en el campo Descripción';
                       alert(mensaje);
                       menaje='';
                       mensaje= '------------------ ALERTA ------------------\n';
                       aprobado = false;
                    }
                }
                return aprobado;
            }
    </script>

    </head>
    <body onLoad="inicializa();mostrarDivs('<%=request.getParameter("tipo")%>');">
    <%util.tituloPagina(out, "Contabilidad", "Catálogos Género, Grupo y Rubro","", "Formulario", true);%>
    <br></br> 
    <table align="center">
      <tr><td  class="negrita" align="right" ><font size="2" class="negrita">
              <%out.println(request.getParameter("operacion"));%>
              <% if(request.getParameter("tipo").equals("genero"))  out.println("género");
                 if(request.getParameter("tipo").equals("grupo" ))  out.println("grupo");
                 if(request.getParameter("tipo").equals("clase" ))  out.println("rubro");
              %>
              </font>
          </td>
      </tr>
    </table>      
    
    <DIV id='genero'  style="display: none;">
    <form id="filtroGenero" method="post" action="c745Control.jsp">    
     <table align="center">
      <tr><td  class="negrita" align="right" >Género: </td>
          <td>
              <select name="lstGenero" class=cajaTexto onChange="javaScript: fcapaDescripcion('<%=request.getParameter("tipo")%>','<%=request.getParameter("operacion")%>');">
                <option SELECTED Value="-1">- Seleccione -</option>
                <option Value="1">1</option>
                <option Value="2">2</option>
                <option Value="3">3</option>
                <option Value="4">4</option>
                <option Value="5">5</option>
                <option Value="6">6</option>
                <option Value="7">7</option>
                <option Value="8">8</option>
                <option Value="9">9</option>
              </select>
          </td>      
      </tr> 
      <tr><td  class="negrita" align="right">Descripción :</td>
          
<% String operacion ="";
   if(request.getParameter("operacion") != null){
        operacion =(String)request.getParameter("operacion");
   }
   if(operacion.equals("Modificar") || operacion.equals("Eliminar") ){ %>
            <td>
              <!-- Capa DescripcionGrupo -->
              <IFrame style="display:none" name="bufferFrame7">
              </IFrame>
              <div id = "capaDescripcionGenero"></div>
            </td>  
<% } else { %> 
          <td><INPUT TYPE="text" NAME="txtGenero" SIZE="55"  class=cajaTexto ></td> 
<% } %>            
      </tr>
     </table>

     <table cellpadding="5" align="center">
        <thead></thead>
        <tr>
          <td>
<% if(operacion.equals("Eliminar")){ %>
            <input type="submit" class="boton" value="Aceptar" onClick="javaScript: if(preguntaConfirmacion('<%=request.getParameter("tipo")%>','<%=request.getParameter("operacion")%>') == false ) return false;" />
<% } else if(operacion.equals("Agregar") || operacion.equals("Modificar")) { %> 
            <input type="submit" class="boton" value="Aceptar" onClick="javaScript: if( validar('<%=request.getParameter("tipo")%>','<%=request.getParameter("operacion")%>') == false) return false; "/>
<% } %>
          </td>
          <td>
              <input type="button" class="boton" value="Regresar" onclick="regresar();" >
          </td>
        </tr>
     </table>        
    </form>    
    </DIV>
    
    <DIV id='grupo'  style="display: none;">
    <form id="filtroGrupo" method="post" action="c745Control.jsp" >    
     <table align="center">
      <tr>
          <td  class="negrita" align="right">Género :</td>
          <td >
              <select id="lstGenero2" name="lstGenero2" class="cajaTexto" >
                <option value="-1">- Seleccione -</option>
                <%
                    consultaGenero.beforeFirst(); 
                    while (consultaGenero.next()){
                %>
                    <option value="<%=consultaGenero.getString("id_genero")%>"><%=consultaGenero.getString("id_genero")+"-"+consultaGenero.getString("descripcion")%></option>
                <%  }   %>
              </select> 
          </td>      
      </tr>     
      <tr><td  class="negrita" align="right">Grupo: </td>
          <td >
              <select name="lstGrupo2" class=cajaTexto onChange="javaScript: fcapaDescripcion('<%=request.getParameter("tipo")%>','<%=request.getParameter("operacion")%>');">
                <option SELECTED value="-1">- Seleccione -</option>
                <option Value="1">1</option>
                <option Value="2">2</option>
                <option Value="3">3</option>
                <option Value="4">4</option>
                <option Value="5">5</option>
                <option Value="6">6</option>
                <option Value="7">7</option>
                <option Value="8">8</option>
                <option Value="9">9</option>
              </select>
          </td>      
      </tr> 
      <tr><td  class="negrita" align="right">Descripción :</td>
<%
   if(request.getParameter("operacion") != null){
        operacion =(String)request.getParameter("operacion");
   } 
   if(operacion.equals("Modificar") || operacion.equals("Eliminar")){ %>
            <td>
              <!-- Capa DescripcionGrupo -->
              <IFrame style="display:none" name="bufferFrame8">
              </IFrame>
              <div id = "capaDescripcionGrupo"></div>
            </td>  
<% } else { %> 
            <td><INPUT TYPE="text" NAME="txtGrupo" SIZE="55"   class="cajaTexto" value=""></td>
<% } %>              
      </tr>
     </table>

     <table cellpadding="5" align="center">
        <thead></thead>
        <tr>
          <td>
 <%if(operacion.equals("Eliminar")){ %>
            <input type="submit" class="boton" value="Aceptar" onClick="javaScript: if(preguntaConfirmacion('<%=request.getParameter("tipo")%>','<%=request.getParameter("operacion")%>') == false ) return false;" />
 <%} else if(operacion.equals("Agregar") || operacion.equals("Modificar")) { %>
            <input type="submit" class="boton" value="Aceptar" onClick="javaScript: if( validar('<%=request.getParameter("tipo")%>','<%=request.getParameter("operacion")%>') == false) return false; "/>
 <%} %>
          </td>
          <td>
              <input type="button" class="boton" value="Regresar" onclick="regresar();" >
          </td>
        </tr>
     </table>        
    </form>    
    </DIV>
    
    <DIV id='clase'  style="display: none;">
    <form id="filtroClase"  method="post" action="c745Control.jsp">    
     <table align="center">
      <tr>
          <td  class="negrita" align="right">Género : </td>
          <td >
              <select id="lstGenero3" name="lstGenero3" class="cajaTexto" onChange="ComponerListaPro(document.forms.filtroClase.lstGenero3[selectedIndex].value);filtroClase.lstGrupo3.options[1].selected=true;">
                <%
                    //Este código genera la lista padre dinamicamente
                    int cuenta;
                    String cat;
                    consultaGenero.beforeFirst(); 
                    while (consultaGenero.next()){
                %>
                    <option value="<%=consultaGenero.getString("id_genero")%>"><%=consultaGenero.getString("id_genero")+"-"+consultaGenero.getString("descripcion")%></option>
                <%       
                    }
                %>           
        </select> 
          </td>      
      </tr>     

      <tr>
          <td  class="negrita" align="right">Grupo :</td>
          <td >
              <select id="lstGrupo3" name="lstGrupo3" class="cajaTexto" >        
              </select>
              
 <script language="JavaScript">
       // Comienza rutina para listas dinamicas
       function Tupla ( campo1, campo2 ) {
            this.campo1 = campo1;
            this.campo2 = campo2;
       }
       var opcionesnull = new Array();
           // opcionesnull[0]= new Tupla("- Seleccione -","null");
       <%
             // Este cÓdigo genera un arreglo para cada proceso padre y almacena en
             //dichos arreglos cada una de los subprocesos hijos que le corresponden
            cuenta  = 1; 
            cat     = "null";
            try{
                consultaGrupo.beforeFirst();
                while (consultaGrupo.next()){
                       if (!cat.equals(consultaGrupo.getString("id_genero"))){
                            cuenta=1;
                            cat=consultaGrupo.getString("id_genero");
       %> 
                            var opciones<%=cat%> = new Array();   
       <%
                       } //fin if
       %>
           opciones<%=cat%>[<%=cuenta%>]=new Tupla("<%=consultaGrupo.getString("id_grupo")+" "+consultaGrupo.getString("des_grupo")%>","<%=consultaGrupo.getString("id_grupo")%>");
       <%
                       cuenta=cuenta+1;
                } // fin while
            }catch (Exception e) {
                    System.out.println("Error en programa: "+ e.getMessage());
            }         
       %>
       var contador;
    
       function ComponerListaPro ( array ) {
            // Compone la lista dependiente a partir
            // del valor de la opcion escogida en la lista "padre"
            BorrarListaPro();
            array = eval("opciones" + array);
            for(contador=1; contador<array.length; contador++) {
                //Añade elementos a nuestro combobox dependiente
                var optionObj = new Option( array[contador].campo1, array[contador].campo2 );
                filtroClase.lstGrupo3.options[contador] = optionObj;
            } // for
       } // ComponerLista
 
       //Formulario.lstSubProceso.options[0].selected=true;
       //Elimina las opciones de nuestro vector dependiente cada que se requiera
       function BorrarListaPro() {
            filtroClase.lstGrupo3.length=0;
       }
    //Inicializamos enviando la clave del proceso padre para este caso
    ComponerListaPro ("null");
</script>
          </td>      
      </tr>      
         
      <tr><td  class="negrita" align="right">Rubro :</td>
          <td>
              <select name="lstClase" class="cajaTexto" onChange="javaScript: fcapaDescripcion('<%=request.getParameter("tipo")%>','<%=request.getParameter("operacion")%>');">
                <option SELECTED value="-1">- Seleccione -</option>
                <option Value="1">1</option>
                <option Value="2">2</option>
                <option Value="3">3</option>
                <option Value="4">4</option>
                <option Value="5">5</option>
                <option Value="6">6</option>
                <option Value="7">7</option>
                <option Value="8">8</option>
                <option Value="9">9</option>
              </select>
          </td>      
      </tr> 
      <tr>
          <td  class="negrita" align="right">Descripción :</td>
          <td>
<% 
   if(request.getParameter("operacion") != null){
        operacion =(String)request.getParameter("operacion");
   }
   if(operacion.equals("Modificar") || operacion.equals("Eliminar")){ %>
              <!-- Capa Descripcion -->
              <IFrame style="display:none" name="bufferFrame9">
              </IFrame>
              <div id = "capaDescripcion"></div>
<% } else { %> 
              <INPUT TYPE="text" NAME="txtClase" SIZE="55"   class="cajaTexto" value="">
<%} %>              
          </td>
      </tr>
     </table>

     <table cellpadding="5" align="center">
        <thead></thead>
        <tr>
          <td>
 <%if(operacion.equals("Eliminar")){ %>
            <input type="submit" class="boton" value="Aceptar" onClick="javaScript: if(preguntaConfirmacion('<%=request.getParameter("tipo")%>','<%=request.getParameter("operacion")%>') == false ) return false;" />
<% } else if(operacion.equals("Agregar") || operacion.equals("Modificar")) { %>
            <input type="submit" class="boton" value="Aceptar" onClick="javaScript: if( validar('<%=request.getParameter("tipo")%>','<%=request.getParameter("operacion")%>') == false) return false; "/>
<% } %>
          </td>
          <td>
              <input type="button" class="boton" value="Regresar" onclick="regresar();" >
          </td>
        </tr>
     </table>        
    </form>    
          <%       
                HttpSession sesion=request.getSession();
                if( request.getParameter("operacion")!=null && request.getParameter("tipo")!= null){
                    sesion.setAttribute("tipo"      ,request.getParameter("operacion"));
                    sesion.setAttribute("operacion" ,request.getParameter("tipo"));
                }
          %>           
    </DIV>           
    </body>
</html>