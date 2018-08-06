<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page  import="sia.beans.seguridad.*"%>
<%@ page import="sia.rf.contabilidad.acceso.CargaExc"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="java.util.*,java.sql.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>

<html>
    <head>
        <meta http-equiv="Content-Language" content="es-mx">
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>Agregar polizas manuales.</title>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type='text/css'>
    </head>
    <jsp:useBean id="sbAutentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  

    <%
        // Recuperacion del objeto de sesion ControlRegistro
        ControlRegistro controlReg = (ControlRegistro) request.getSession().getAttribute("controlRegistro");
        // Se obtiene la Unidad Ejecutora 
        String lsUniEje = controlReg.getUnidad();
        // Pais: México - 147
        String lsPais = "147";
        // Entidad
        String lsEntidad = String.valueOf(controlReg.getEntidad());
        // Ambito
        String lsAmbito = String.valueOf(controlReg.getAmbito());
        // Tipo de usuario
        String lsTipoUsuario = String.valueOf(controlReg.getTipoUsuario());
        // Ejercicio
        String lsEjercicio = String.valueOf(controlReg.getEjercicio());
        // Id del Catalogo de Cuentas
        String lsCatCuenta = String.valueOf(controlReg.getIdCatalogoCuenta());
        
        String lsRuta = (String) session.getAttribute("ruta");
    %>
    <body>
        <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Carga de Polizas", "Cargar", true);</jsp:scriptlet>      
            <FORM Method="post" action="" name="formulario" >
                <H2>Informaci&oacute;n: Unidad ejecutora=<%=lsUniEje%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%>, Ejercicio=<%=lsEjercicio%></H2>
            <%
                // Objeto que contiene la informacion de auntentificacion actual del sistema
                sbAutentifica = (Autentifica) request.getSession().getAttribute("Autentifica");
                // Se obtiene el numero del empleado actualmente logueado.
                String numEmpleado = Integer.toString(sbAutentifica.getNumeroEmpleado());
                String resultado = "";
                Connection conexion = null;
                
                // Si la entidad es de 1 a 9 se agrega un 0 al principio de la misma (01 ... 09)
                if (lsEntidad.length() == 1) {
                    lsEntidad = "0" + lsEntidad;
                }
                
                try {
                    // Factoria encargada de la creacion de la conexion a la base de datos tomando en cuenta el datasource de Contabilidad
                    conexion = DaoFactory.getContabilidad();
                    conexion.setAutoCommit(false);
                    // Instancia de la clase que realiza todo el manejo del archivo de excel.
                    CargaExc cargaExc = new CargaExc();
                    // Manda llamar al proceso de lectura del archivo de excel pasandole todos los parametros necesarios
                    resultado = cargaExc.procesa(conexion, numEmpleado, lsUniEje, lsEntidad + lsAmbito, lsRuta, lsEjercicio, lsCatCuenta, "0001", lsTipoUsuario);
                    conexion.commit();
            %>
            <H2><%=resultado.substring(2)%></H2>

            <%
                System.out.println(resultado);
            } catch (Exception E) {
                System.out.println(resultado);
                conexion.rollback();
            %>
            <p>Ha ocurrido un error al accesar la Base de Datos,</p>
            <p><H2><%=E.getMessage()%>
                ,verifique la información del archivo de carga, </H2></p>
        <p>si el problema persiste favor de reportarlo al Administrador del Sistema.</p>
        <p>Gracias.</p>

        <%
            } finally {
                if (conexion != null) {
                    conexion.close();
                    conexion = null;
                }
            }
        %>
        <table width='100%' align="right">
            <tr  align="right">
                <td width='10%'>
                    <br/>
                    <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="window.open('../filtroGeneral.jsp?opcion=cargarPolizas&idCatalogoCuenta=1','_self');" >
                </td></tr>
        </table>
    </form>  
</body>
</html>