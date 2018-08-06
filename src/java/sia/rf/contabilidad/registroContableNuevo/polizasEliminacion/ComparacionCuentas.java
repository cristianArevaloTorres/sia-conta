package sia.rf.contabilidad.registroContableNuevo.polizasEliminacion;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;

import sia.db.sql.Vista;

import sia.libs.periodo.Fecha;

public class ComparacionCuentas {

    private double totalIzquierdo;
    private double totalDerecho;
    private double totalDiferencia;

    public ComparacionCuentas() {
      setTotalDerecho(0.00);
      setTotalIzquierdo(0.00);
      setTotalDiferencia(0.00);
    }
    
    public void setTotalIzquierdo(double totalIzquierdo) {
        this.totalIzquierdo = totalIzquierdo;
    }

    public double getTotalIzquierdo() {
        return totalIzquierdo;
    }

    public void setTotalDerecho(double totalDerecho) {
        this.totalDerecho = totalDerecho;
    }

    public double getTotalDerecho() {
        return totalDerecho;
    }

    public void setTotalDiferencia(double totalDiferencia) {
        this.totalDiferencia = totalDiferencia;
    }

    public double getTotalDiferencia() {
        return totalDiferencia;
    }
    
    public StringBuffer codigoHtml(Connection conexion, String fechaCierre){
        StringBuffer regresar = null;
        List<Vista> registrosIzq = null;
        List<Vista> registrosDer = null;
        Map parametros = null;
        String fechaValidacion = null;
        String strMesActual = null;
        String strMesAnterior = null;
        
        try{
            fechaValidacion = String.valueOf(fechaCierre);
            Fecha fechaPeriodo = new Fecha(fechaValidacion, "/");
            strMesActual = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
            fechaPeriodo.addMeses(-1);
            strMesAnterior = sia.libs.formato.Fecha.getNombreMesCorto(fechaPeriodo.getMes() - 1);
            parametros = new HashMap();
            registrosIzq = new ArrayList<Vista>();
            registrosDer = new ArrayList<Vista>();
            regresar = new StringBuffer();
            regresar.append("<p><b>Comparacion de cuentas</b></p>\n");
            regresar.append(crearEncabezado("Saldo Actual","Saldo Actual","8110","8210","1. 8110 LEY DE INGRESOS ESTIMADA - 8210 PRESUPUESTO DE EGRESOS APROBADO"));
            parametros.put("mesActualCargo",strMesActual+"_cargo_eli");
            parametros.put("mesActualAbono",strMesActual+"_abono_eli");
            
            if (strMesActual.equals("ENE")){
                parametros.put("mesAnteriorCargo","ene_cargo_ini_eli");
                parametros.put("mesAnteriorAbono","ene_abono_ini_eli");
            }else{
                parametros.put("mesAnteriorCargo",strMesAnterior+"_cargo_acum_eli");
                parametros.put("mesAnteriorAbono",strMesAnterior+"_abono_acum_eli");
            }
            parametros.put("idCatalogoCuenta",1);
            parametros.put("ejercicio",fechaCierre.substring(0,4));   
            parametros.put("campoCuenta","decode(substr(c.cuenta_contable,18,4),'0001','1','0002','2','') cuenta"); 
            parametros.put("condicionCuenta","and (c.cuenta_contable like '8110_____________0001%' or c.cuenta_contable like '8110_____________0002%')"); 
            registrosIzq = obtenerQuery(conexion,"eliminacion.select.ingresosEgresos",parametros);
            parametros.put("condicionCuenta","and (c.cuenta_contable like '8210_____________0001%' or c.cuenta_contable like '8210_____________0002%')"); 
            registrosDer = obtenerQuery(conexion,"eliminacion.select.ingresosEgresos",parametros);
            regresar.append(crearDetalleTabla(registrosIzq,registrosDer));
            regresar.append(crearTotal(getTotalIzquierdo(),getTotalDerecho(),getTotalDiferencia()));
            
            regresar.append("<br>\n");
            regresar.append(crearEncabezado("Cargo Acumulado","Saldo Actual","8120","4221 y 4319","2. 8120 LEY DE INGRESOS POR EJECUTAR - 4221 TRANSFERENCIAS INTERNAS Y ASIGNACIONES AL SECTOR PÚBLICO Y 4319 INGRESOS EXCEDENTES"));
            parametros.put("mesActual",strMesActual+"_cargo_eli");
            if (strMesActual.equals("ENE")){
                parametros.put("mesAnterior","ene_cargo_ini_eli");
            }else{        
                parametros.put("mesAnterior",strMesAnterior+"_cargo_acum_eli");
            }
            parametros.put("condicionCuenta","and (c.cuenta_contable like '8120_____________0001%' or c.cuenta_contable like '8120_____________0002%')"); 
            registrosIzq = obtenerQuery(conexion,"eliminacion.select.leyIngresos",parametros);
            registrosDer = obtenerQuery(conexion,"eliminacion.select.transInternasyAsignaciones",parametros);
            regresar.append(crearDetalleTabla(registrosIzq,registrosDer));
            regresar.append(crearTotal(getTotalIzquierdo(),getTotalDerecho(),getTotalDiferencia()));

             regresar.append("<br>\n");
             regresar.append(crearEncabezado("Cargo Acumulado","Abono Acumulado","8130","8230","3. 8130 MODIFICACIONES A LA LEY DE INGRESOS ESTIMADA - 8230 MODIFICACIONES AL PRESUPUESTO DE EGRESOS APROBADO"));
             parametros.put("campoCuenta","decode(substr(c.cuenta_contable,18,4),'0003','3','0004','4','') cuenta"); 
             parametros.put("condicionCuenta","and (c.cuenta_contable like '8130_____________0003%' or c.cuenta_contable like '8130_____________0004%')"); 
             registrosIzq = obtenerQuery(conexion,"eliminacion.select.leyIngresos",parametros);
             parametros.put("mesActual",strMesActual+"_abono_eli");
            if (strMesActual.equals("ENE")){
                parametros.put("mesAnterior","ene_abono_ini_eli");
            }else{  
                parametros.put("mesAnterior",strMesAnterior+"_abono_acum_eli");
            }
             parametros.put("condicionCuenta","and (c.cuenta_contable like '8230_____________0003%' or c.cuenta_contable like '8230_____________0004%')"); 
             registrosDer = obtenerQuery(conexion,"eliminacion.select.leyIngresos",parametros);
             regresar.append(crearDetalleTabla(registrosIzq,registrosDer));
             regresar.append(crearTotal(getTotalIzquierdo(),getTotalDerecho(),getTotalDiferencia()));
             
             regresar.append("<br>\n");
             regresar.append(crearEncabezado("Abono Acumulado","Cargo Acumulado","8130","8230","4. 8130 MODIFICACIONES A LA LEY DE INGRESOS ESTIMADA - 8230 MODIFICACIONES AL PRESUPUESTO DE EGRESOS APROBADO"));
            parametros.put("condicionCuenta","and (c.cuenta_contable like '8130_____________0003%' or c.cuenta_contable like '8130_____________0004%')"); 
            registrosIzq = obtenerQuery(conexion,"eliminacion.select.leyIngresos",parametros);
            parametros.put("mesActual",strMesActual+"_cargo_eli");
            if (strMesActual.equals("ENE")){
                parametros.put("mesAnterior","ene_cargo_ini_eli");
            }else{    
                parametros.put("mesAnterior",strMesAnterior+"_cargo_acum_eli");
            }            
            parametros.put("condicionCuenta","and (c.cuenta_contable like '8230_____________0003%' or c.cuenta_contable like '8230_____________0004%')"); 
            registrosDer = obtenerQuery(conexion,"eliminacion.select.leyIngresos",parametros);
            regresar.append(crearDetalleTabla(registrosIzq,registrosDer));
            regresar.append(crearTotal(getTotalIzquierdo(),getTotalDerecho(),getTotalDiferencia()));
            
            regresar.append("<br>\n");
            regresar.append(crearEncabezado("Cargo Acumulado","Saldo Actual","8140","4221 y 4319","5. 8140 LEY DE INGRESOS DEVENGADA - 4221 TRANSFERENCIAS INTERNAS Y ASIGNACIONES AL SECTOR PÚBLICO Y 4319 INGRESOS EXCEDENTES"));
            parametros.put("campoCuenta","decode(substr(c.cuenta_contable,18,4),'0001','1','0002','2','') cuenta"); 
            parametros.put("condicionCuenta","and (c.cuenta_contable like '8140_____________0001%' or c.cuenta_contable like '8140_____________0002%')"); 
            registrosIzq = obtenerQuery(conexion,"eliminacion.select.leyIngresos",parametros);
            registrosDer = obtenerQuery(conexion,"eliminacion.select.transInternasyAsignaciones",parametros);
            regresar.append(crearDetalleTabla(registrosIzq,registrosDer));
            regresar.append(crearTotal(getTotalIzquierdo(),getTotalDerecho(),getTotalDiferencia()));
            
            regresar.append("<br>\n");
            regresar.append(crearEncabezado("Abono Acumulado","Saldo Actual","8140","4221 y 4319","6. 8140 LEY DE INGRESOS DEVENGADA - 4221 TRANSFERENCIAS INTERNAS Y ASIGNACIONES AL SECTOR PÚBLICO Y 4319 INGRESOS EXCEDENTES"));
            parametros.put("mesActual",strMesActual+"_cargo_eli");
            if (strMesActual.equals("ENE")){
                parametros.put("mesAnterior","ene_cargo_ini_eli");
            }else{   
                parametros.put("mesAnterior",strMesAnterior+"_cargo_acum_eli");
            }            
            registrosIzq = obtenerQuery(conexion,"eliminacion.select.leyIngresos",parametros);
            registrosDer = obtenerQuery(conexion,"eliminacion.select.transInternasyAsignaciones",parametros);
            regresar.append(crearDetalleTabla(registrosIzq,registrosDer));
            regresar.append(crearTotal(getTotalIzquierdo(),getTotalDerecho(),getTotalDiferencia()));
            
            regresar.append("<br>");
            regresar.append(crearEncabezado("Abono Acumulado","Saldo Actual","8150","4221 y 4319","7. 8150 LEY DE INGRESOS RECAUDADA - 4221 TRANSFERENCIAS INTERNAS Y ASIGNACIONES AL SECTOR PÚBLICO Y 4319 INGRESOS EXCEDENTES"));
            parametros.put("condicionCuenta","and (c.cuenta_contable like '8140_____________0001%' or c.cuenta_contable like '8140_____________0002%')"); 
            registrosIzq = obtenerQuery(conexion,"eliminacion.select.leyIngresos",parametros);
            registrosDer = obtenerQuery(conexion,"eliminacion.select.transInternasyAsignaciones",parametros);
            regresar.append(crearDetalleTabla(registrosIzq,registrosDer));
            regresar.append(crearTotal(getTotalIzquierdo(),getTotalDerecho(),getTotalDiferencia()));
        }
        catch(Exception e){
          e.printStackTrace();
          System.err.println("Error en el método codigoHtml");
        } 
        finally{
          registrosIzq = null;
          registrosDer = null;
          parametros = null;
        }
        return regresar;
    }
    
    public List<Vista> obtenerQuery(Connection conexion, String propiedad, Map parametros ){
        Sentencias sentencia = null;
        List<Vista> registros = null;
        try{
            sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
            registros = sentencia.registros(sentencia.getComando(propiedad,parametros),conexion);
            //System.out.println("Query: "+propiedad+" "+sentencia.getComando(propiedad,parametros));
        }
        catch(Exception e){
          e.printStackTrace();
          System.err.println("Error al obtener query");
        }  
        finally{
          sentencia = null;
        }
        return registros;
    }
    
    public StringBuffer crearEncabezado(String saldoIzq, String saldoDer, String cuentaIzq, String cuentaDer, String comparacion){
        StringBuffer regresar = new StringBuffer();
        
        regresar.append("<p><b>").append(comparacion).append(".</b></p>\n");
        regresar.append("<table width='100%' align='center' class='general' name='tAmbitos' id='tAmbitos' border=3>");
        regresar.append("<tr><td colspan='3' align='center'><b>Cuenta ").append(cuentaIzq).append("</b></td>\n");
        regresar.append("<td align='center'>- -</td>\n ");
        regresar.append("<td colspan='3' align='center'><b>Cuenta ").append(cuentaDer).append("</b></td>\n");
        regresar.append("<td align='center'>- -</td> \n");
        regresar.append("<td align='center'>&nbsp;</td>\n ");
        regresar.append("</tr>\n");
        regresar.append("<tr>\n");
        regresar.append("<td align='center'><b>Cuenta</b></td>\n");
        regresar.append("<td align='center'><b>Descripción</b></td>\n");
        regresar.append("<td align='center'><b>").append(saldoIzq).append("</b></td>\n");
        regresar.append("<td align='center'>- -</td> \n");
        regresar.append("<td align='center'><b>Cuenta</b></td>\n");
        regresar.append("<td align='center'><b>Descripción</b></td>\n");
        regresar.append("<td align='center'><b>").append(saldoDer).append("</b></td>\n");
        regresar.append("<td align='center'>- -</td> \n");
        regresar.append("<td align='center'><b>Diferencia</b></td>\n");
        regresar.append("</tr>\n");
        return regresar;
    }
    
    public StringBuffer crearDetalleTabla(List<Vista> registrosIzq, List<Vista> registrosDer){
        StringBuffer regresar = new StringBuffer();
        double totalIzq = 0.00;
        double totalDer = 0.00;
        double diferencia = 0.00;
        try{
            for(int der=0; der<registrosDer.size(); der++){
            
            if(der<=1)
              totalIzq = totalIzq + registrosIzq.get(der).getDouble("SALDO");
            totalDer = totalDer + registrosDer.get(der).getDouble("SALDO");
            diferencia = totalDer - totalIzq;
            setTotalIzquierdo(totalIzq);
            setTotalDerecho(totalDer);
            setTotalDiferencia(diferencia);
            
            if(der<=1){
                regresar.append("<td align='center'>").append(registrosIzq.get(der).getField("CUENTA")).append("</td>\n");
                regresar.append("<td align='left'>").append(registrosIzq.get(der).getField("DESCRIPCION")).append("</td>\n");
                regresar.append("<td align='right'>").append(sia.libs.formato.Numero.formatear(sia.libs.formato.Numero.NUMERO_MILES_CON_DECIMALES,registrosIzq.get(der).getDouble("SALDO"))).append("</td>\n");
                regresar.append("<td align='center'>- -</td>\n");
            }else{
                regresar.append("<td align='center'>&nbsp;</td>\n");
                regresar.append("<td align='center'>&nbsp;</td>\n");
                regresar.append("<td align='center'>&nbsp;</td>\n");
                regresar.append("<td align='center'>- -</td>\n");
            }
                regresar.append("<td align='center'>").append(registrosDer.get(der).getField("CUENTA")).append("</td>\n");
                regresar.append("<td align='left'>").append(registrosDer.get(der).getField("DESCRIPCION")).append("</td>\n");
                regresar.append("<td align='right'>").append(sia.libs.formato.Numero.formatear(sia.libs.formato.Numero.NUMERO_MILES_CON_DECIMALES,registrosDer.get(der).getDouble("SALDO"))).append("</td>\n");
                regresar.append("<td align='center'>- -</td>\n");
                regresar.append("<td align='center'>&nbsp;</td>\n");
                regresar.append("</tr>\n");
            }
        }        
        catch(Exception e){
          e.printStackTrace();
          System.err.println("Error al crear el detalle de la tabla");
        }  
        return regresar;
    }
    
    public StringBuffer crearTotal(double totalIzq, double totalDer, double diferencia){
        StringBuffer regresar = new StringBuffer();
      try{
        regresar.append("<tr>\n");
        regresar.append("<td align='center'><b>-</b></td>\n");
        regresar.append("<td align='right'><b>Total</b></td>\n");
        regresar.append("<td align='right'><b>").append(sia.libs.formato.Numero.formatear(sia.libs.formato.Numero.NUMERO_MILES_CON_DECIMALES,totalIzq)).append("</b></td>\n");
        regresar.append("<td align='center'>- -</td>\n");
        regresar.append("<td align='center'><b>&nbsp;</b></td>\n");
        regresar.append("<td align='right'><b>Total</b></td>\n");
        regresar.append("<td align='right''><b>").append(sia.libs.formato.Numero.formatear(sia.libs.formato.Numero.NUMERO_MILES_CON_DECIMALES,totalDer)).append("</b></td>\n");
        regresar.append("<td align='center'>- -</td>\n");
        regresar.append("<td align='right'><b>").append(sia.libs.formato.Numero.formatear(sia.libs.formato.Numero.NUMERO_MILES_CON_DECIMALES,diferencia)).append("</b></td>\n");
        regresar.append("</tr>\n");
        regresar.append("</table>");
      } 
      catch(Exception e){
        e.printStackTrace();
        System.err.println("Error al crear el total");
      }  
        return regresar;
    }


}
