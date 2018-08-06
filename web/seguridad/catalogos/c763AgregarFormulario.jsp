
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 

<jsp:useBean id="consultaAmbito" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   consultaAmbito.setTableName("rh_tc_ambitos");
   String SQL = "select ambito, descripcion from rh_tc_ambitos ORDER by ambito ASC ";
   consultaAmbito.setCommand(SQL);
   Connection con=null;
   try{
     con = DaoFactory.getContabilidad();
     consultaAmbito.execute(con);
   } catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo consultaAmbito "+e.getMessage());
   } finally{
      if (con!=null){
          con.close();
          con=null;
      }
   } //Fin finally
 %>
</jsp:useBean>

<jsp:useBean id="consultaUnidadEjecutora" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   consultaUnidadEjecutora.setTableName("rh_tc_uni_ejecutoras");
   String SQL2 = "select unidad_ejecutora, descripcion from rh_tc_uni_ejecutoras where unidad_ejecutora = '001' order by unidad_ejecutora ASC ";
   consultaUnidadEjecutora.setCommand(SQL2);
   Connection con2=null;
   try{
     con2 = DaoFactory.getContabilidad();
     consultaUnidadEjecutora.execute(con2);
   } catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo consultaUnidadEjecutora "+e.getMessage());
   } finally{
      if (con2!=null){
          con2.close();
          con2=null;
      }
   } //Fin finally
   %>
 
</jsp:useBean>

<jsp:useBean id="consultaPuestos" class="sun.jdbc.rowset.CachedRowSet" scope="page" >
    <%
   consultaPuestos.setTableName("rh_tr_puestos");
   String SQL3 = "select nvl(max(t.id_puesto)+1,1) as consecutivo from rh_tr_puestos t ";
   consultaPuestos.setCommand(SQL3);
   Connection con3=null;
   try{
     con3 = DaoFactory.getContabilidad();
     consultaPuestos.execute(con3);
   } catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo consultaPuestos "+e.getMessage());
   } finally{
      if (con3!=null){
          con3.close();
          con3=null;
      }
   } //Fin finally
  %>
 
</jsp:useBean>

<!--Consulta para el combo de Modificar Puestos-->
<jsp:useBean id="consultaPuestos2" class="sun.jdbc.rowset.CachedRowSet" scope="page" >
    <%
   consultaPuestos2.setTableName("rh_tr_puestos");
   String SQL4 = "select id_puesto from rh_tr_puestos order by id_puesto ";
   consultaPuestos2.setCommand(SQL4);
   Connection con4=null;
   try{
     con4 = DaoFactory.getContabilidad();
     consultaPuestos2.execute(con4);
   } catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo consultaPuestos2 "+e.getMessage());
   } finally{
      if (con4!=null){
          con4.close();
          con4=null;
      }
   } //Fin finally
  %>
 
</jsp:useBean>






<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>c710AgregarFormulario</title>
        <link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
          <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
           <script language="JavaScript">
                
                function regresar(){ 
                    window.location.href="../catalogos/c763Filtro.jsp"; 
                } 
         
            function fcapaDescripcion(catalogo,operacion){
                    if (catalogo =='unidades' && (operacion=='Modificar' || operacion=='Eliminar')){
                       loadSource('capaUnidad',null,'c763CapaUnidad.jsp?unidad_ejecutora='+document.forms.formUnidades.lstUnidadEjecutora.value+'&operacion='+document.forms.formUnidades.operacion.value,bufferFrame7);
                        return true;    
                    }
                    if (catalogo =='puestos' && (operacion=='Modificar' || operacion=='Eliminar')){
                        loadSource('capaPuestos',null,'c763CapaPuesto.jsp?id_puesto='+document.forms.formPuestos.lstPuestos.value+'&operacion='+document.forms.formPuestos.operacion.value,bufferFrame8); 
                        return true;
                    }
                   /* if (catalogo =='estruc' && (operacion=='Modificar' || operacion=='Eliminar')){
                        loadSource('capaDescripcion',null,'c745CapaDescripcion.jsp?idGenero='+document.forms.filtroClase.lstGenero3.value+'&idGrupo='+document.forms.filtroClase.lstGrupo3.value+'&idClase='+document.forms.filtroClase.lstClase.value,bufferFrame9);  
                        return true;
                    }*/
                   
                }
    
            </script>  
            
             <script language="JavaScript">            
                function mostrarDivs(id){
                    var elDiv = document.getElementById(id);
                    elDiv.style.display='block'; //desoculta
                }
            </script> 
            
            <script language="JavaScript">
                
            function preguntaConfirmacion(catalogo,operacion) {
                if (catalogo =='unidades' && operacion=='Eliminar'){
                    mensaje = '¿Está seguro que desea eliminar la Unidad Ejecutora seleccionada?';
                    if (confirm(mensaje)) {
                        if(validar(catalogo, operacion)){
                            document.forms.formUnidades.submit();
                            return true;
                        } else return false;
                    } else return false;
                }
                if (catalogo =='puestos' && operacion=='Eliminar'){
                    mensaje = '¿Está seguro que desea eliminar el puesto seleccionado?';
                    if (confirm(mensaje)) {
                        if(validar(catalogo, operacion)){
                            document.forms.formPuestos.submit();
                            return true;
                        } else return false;    
                    } else return false;
                }
                
            }//Fin preguntaConfirmacion
            </script>
            
            
            
            
           
   <script language="JavaScript" type="text/javascript">
       function validar(formulario, operacion){
           mensaje= '------------------ ALERTA ------------------\n'; 
            var aprobado = true;
              
           if( formulario == 'unidades' ){
                var valorCampo; 
                var valorCampo2;
                var ambito;
                var clave;
                var descripcion;
                                          
                 if(operacion=='Agregar'){
                     valorCampo  = document.forms.formUnidades.txtClave.value; 
                     valorCampo2 = document.forms.formUnidades.txtDesc.value;
                     ambito = document.forms.formUnidades.lstAmbito.value;
               
              
                 if(valorCampo=='' && operacion=='Agregar'){
                    mensaje+='Por favor escriba en el campo Clave';
                    alert(mensaje);
                    mensaje='';
                    mensaje= '------------------ ALERTA ------------------\n';
                    aprobado = false;
                   }
                   
              if(valorCampo2=='' && operacion=='Agregar'){
                mensaje+='Por favor escriba en el campo Descripcion';
                alert(mensaje);
                menaje='';
                mensaje= '------------------ ALERTA ------------------\n';
                aprobado = false;
                   }
                   
               if(ambito =='null' ){
                mensaje+='Por favor seleccione un ID para el Ámbito';
                alert(mensaje);
                mensaje='';
                aprobado = false;
               }
              }  
              if(operacion=='Modificar'){
               clave = document.forms.formUnidades.lstUnidadEjecutora.value;
              if(clave =='null' ){
                mensaje+='Por favor seleccione un ID para la clave';
                alert(mensaje);
                mensaje='';
                aprobado = false;
               }
               
                if (clave !='null'){
                   if (document.forms.formUnidades.txtDescripcion.value==''){
                       mensaje+='Por favor escriba en el campo Descripcion';
                        alert(mensaje);
                         mensaje='';
                        aprobado = false;
                   }
                }       
                
               if(descripcion==''){
                mensaje+='Por favor escriba en el campo Descripcion';
                alert(mensaje);
                menaje='';
                mensaje= '------------------ ALERTA ------------------\n';
                aprobado = false;
                   }
                   
                if(ambito =='null' ){
                mensaje+='Por favor seleccione un ID para el Ámbito';
                alert(mensaje);
                mensaje='';
                aprobado = false;
               }
             }
           } // Fin de Unidades  
         if(formulario == 'puestos'){
               var descPuesto;
               var clavePuesto;
                 if(operacion=='Agregar'){
                       document.forms.formPuestos.txtIdPuesto.disabled=false;
                       var descPuesto = document.forms.formPuestos.txtDesc_puesto.value;
                    if(descPuesto=='' && operacion=='Agregar'){
                       mensaje+='Por favor escriba en el campo Descripción';
                       alert(mensaje);
                       menaje='';
                       mensaje= '------------------ ALERTA ------------------\n';
                       aprobado = false;
                      }
                  }  
                 if(operacion=='Modificar'){
                      var clavePuesto = document.forms.formPuestos.lstPuestos.value;
                        
                      if(clavePuesto =='null' ){
                        mensaje+='Por favor seleccione un ID para la clave del Puesto';
                        alert(mensaje);
                        mensaje='';
                        aprobado = false;
                    }
                    
                    if (clavePuesto !='null'){
                        if (document.forms.formPuestos.txtDescripcion.value==''){
                        mensaje+='Por favor escriba en el campo Descripcion';
                        alert(mensaje);
                        mensaje='';
                        aprobado = false;
                     }
                   }       
                 }
               }
           return aprobado;
          }
      </script> 
   </head>
    
     
     <body onLoad="mostrarDivs('<%=request.getParameter("catalogo")%>');">
     <%util.tituloPagina(out, "Contabilidad", "Administración de Catálogos ","", "Formulario", true);%>
     
    <table align="center">
      <tr><td  class="negrita" align="right" ><font size="2" class="negrita">
              <%out.println(request.getParameter("operacion"));%>
              <% if(request.getParameter("catalogo").equals("unidades")){
                    out.println("Unidades Ejecutoras");
                 }
                 if(request.getParameter("catalogo").equals("puestos")){
                    out.println("Puestos");
                 }
                 if(request.getParameter("catalogo").equals("estruc")){
                    out.println("Estructura Funcional");
                 }%>
              </font>
          </td>
      </tr>
    </table>      
              
<DIV id='unidades'  style="display: none;">
  <form id="formUnidades" method="post" action="c763Control.jsp"> 
  <table width="100%" > 
     <% String operacion1 ="";
   if(request.getParameter("operacion") != null){
        operacion1 =(String)request.getParameter("operacion");
   }
      
  if(operacion1.equals("Agregar")){ %>
 <tr>
 <td  class="negrita" align="right"  width="36%"><div align="right">Clave</div></td>
      <td><div align="left"><input type="text" name="txtClave" maxlength="100" size="60"  class="cajaTexto" onChange="javascript:this.value=this.value.toUpperCase();"/>
      </div></td>
  </tr>
  
  <tr>
 <td  class="negrita" align="right" width="36%"><div align="right">Descripción de la unidad</div></td>
      <td><div align="left"><input type="text" name="txtDesc" maxlength="100" size="60"  class="cajaTexto" onChange="javascript:this.value=this.value.toUpperCase();"/>
      </div></td>
  </tr>
 
   <tr> 
      <td width="36%"><div align="right">Ámbito</div></td>
      <td><div align="left"><select NAME="lstAmbito" class='cajaTexto' OnChange=''>  <!--CapaCuenta(Filtro.lstGenero.value,Filtro.lstGrupo.value,Filtro.lstClase.value)-->
                  <option value="null" >- Seleccione -</option>
           <%
                    consultaAmbito.beforeFirst(); 
                    while (consultaAmbito.next()){
                %>
                    <option value="<%=consultaAmbito.getString("ambito")%>"><%=consultaAmbito.getString("descripcion")%></option>
            <%
            }
            %>
         </select>
         </div>
       </td>
    </tr>
     
   <% } else if(operacion1.equals("Modificar") || operacion1.equals("Eliminar")) { %> 
        
     <tr> 
      <td width="36%"><div align="right">Clave:</div></td>
      <td><div align="left"><select NAME="lstUnidadEjecutora" class='cajaTexto' onChange="javaScript: fcapaDescripcion('<%=request.getParameter("catalogo")%>','<%=request.getParameter("operacion")%>');">  
                  <option value="null" >- Seleccione -</option>
                  <%
                    consultaUnidadEjecutora.beforeFirst(); 
                    while (consultaUnidadEjecutora.next()){
                %>
                    <option value="<%=consultaUnidadEjecutora.getString("unidad_ejecutora")%>"><%=consultaUnidadEjecutora.getString("unidad_ejecutora")%></option>
            <%
            }
            %>
       </select>
        </div>
       
       <tr>
            <td width="36%"><div align="right">Descripción:</div></td>
              <td>
              <!-- Capa capaUnidad -->
              <IFrame style="display:none" name="bufferFrame7">
              </IFrame>
              <div id = "capaUnidad"></div>
            </td>  
         
          
       </td>
    </tr>
<%} %>            
  </table>
   
  <table cellpadding="5" align="center">
        <thead></thead>
        <tr>
          <td>
            <% if(operacion1.equals("Eliminar")) { %>
               <input type="submit" class="boton" value="Aceptar" onClick="javaScript: if(preguntaConfirmacion('<%=request.getParameter("catalogo")%>','<%=request.getParameter("operacion")%>') == false ) return false;" />
            <% }else if (operacion1.equals("Agregar") || operacion1.equals("Modificar")) { %> 
                <input  type="submit"  class="boton"  value="Aceptar" onClick="javaScript: if( validar('<%=request.getParameter("catalogo")%>','<%=request.getParameter("operacion")%>') == false) return false; "/>
    <% } %>         
             <input type="hidden" name="catalogo" value=" <%=request.getParameter("catalogo")%> "  >
             <input type="hidden" name="operacion" value=" <%=request.getParameter("operacion")%> "  >
             
            </td>
          <td>
              <input type="button" class="boton" value="Regresar" onclick="regresar();" >
          </td>
        </tr>
        
       </table>  
            
         
    
   </form>
 </DIV>
       
       
<!--------------------------------OPCION DE PUESTOS----------------------------------------->


<DIV id='puestos'  style="display: none;">
  <form id="formPuestos" method="post" action="c763Control.jsp"> 
    <table width="100%" > 
     <%    
   if(request.getParameter("operacion") != null){
        operacion1 =(String)request.getParameter("operacion");
   }
      
  if(operacion1.equals("Agregar")){ %>
 <tr>
   <td class="negrita" align="right"  width="36%"><div align="right">Clave</div></td>
       <%
        consultaPuestos.beforeFirst(); 
        consultaPuestos.next();
       %>
   
      <td><div align="left"><input disabled  type="text" value="<%=consultaPuestos.getString("consecutivo")%>" name="txtIdPuesto" maxlength="100" size="60"  class="cajaTexto" />
      </div></td>
  </tr>
  <tr>
 <td  class="negrita" align="right" width="36%"><div align="right">Descripción del puesto</div></td>
      <td><div align="left"><input type="text" name="txtDesc_puesto" maxlength="100" size="60"  class="cajaTexto" onChange="javascript:this.value=this.value.toUpperCase();"/>
      </div></td>
  </tr>
  
   <% } else if(operacion1.equals("Modificar") || operacion1.equals("Eliminar")) { %> 
    <tr> 
      <td width="36%"><div align="right">Clave:</div></td>
      <td><div align="left"><select NAME="lstPuestos" class='cajaTexto' onChange="javaScript: fcapaDescripcion('<%=request.getParameter("catalogo")%>','<%=request.getParameter("operacion")%>');">  
                  <option value="null" >- Seleccione -</option>
                  <%
                    consultaPuestos2.beforeFirst(); 
                    while (consultaPuestos2.next()){
                %>
                    <option value="<%=consultaPuestos2.getString("id_puesto")%>"><%=consultaPuestos2.getString("id_puesto")%></option>
            <%
            }
            %>
       </select>
        </div>
       
       <tr>
            <td width="36%"><div align="right">Descripción:</div></td>
             <td>
              <!-- Capa capaPuestos -->
              <IFrame style="display:none" name="bufferFrame8">
              </IFrame>
              <div id = "capaPuestos"></div>
            </td>  
                                    
    </tr>
<%} %>            
                 
</table> 

<table cellpadding="5" align="center">
        <thead></thead>
        <tr>
          <td>
            <% if(operacion1.equals("Eliminar")) { %>
               <input type="submit" class="boton" value="Aceptar" onClick="javaScript: if(preguntaConfirmacion('<%=request.getParameter("catalogo")%>','<%=request.getParameter("operacion")%>') == false ) return false;" />
            <% }else if (operacion1.equals("Agregar") || operacion1.equals("Modificar")) { %> 
                <input  type="submit"  class="boton"  value="Aceptar" onClick="javaScript: if( validar('<%=request.getParameter("catalogo")%>','<%=request.getParameter("operacion")%>') == false) return false; "/>
    <% } %>         
              <input type="hidden" name="catalogo" value=" <%=request.getParameter("catalogo")%> "  >
             <input type="hidden" name="operacion" value=" <%=request.getParameter("operacion")%> "  >
          </td>
          <td>
              <input type="button" class="boton" value="Regresar" onclick="regresar();" >
          </td>
        </tr>
    </table>  
 </form>
 </DIV>
         
    </body>
</html>
