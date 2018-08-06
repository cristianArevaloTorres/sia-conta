package sia.rf.contabilidad.reportes;

import java.util.Iterator;
import net.sf.jasperreports.engine.JRScriptletException;
import sia.libs.formato.Fecha;
import sia.rf.contabilidad.registroContable.servicios.Cuenta;
import sia.rf.contabilidad.registroContable.servicios.SubCuenta;
import sia.scriptlets.BarraProgreso;

public class Polizas extends BarraProgreso {

    @Override
    public void afterDetailEval() throws JRScriptletException {
        super.afterDetailEval();
        //ModuloRegistroContableImpl am = null;   
        try {
            String consecutivoPoliza;
            String mesOriginal;
            //am = (ModuloRegistroContableImpl)Configuration.createRootApplicationModule("sia.rf.contabilidad.registroContable.servicios.ModuloRegistroContable", 
            //  "ModuloRegistroContableLocal");      
            Integer cuenta_contable_id = (Integer) checkField("CUENTA_CONTABLE_ID", new Integer(1));
            Cuenta cuenta = new Cuenta(cuenta_contable_id, getConnection());
            DatosReportes datosReportes = new DatosReportes();
            StringBuilder sb = new StringBuilder();
            Iterator iterator = cuenta.getSubcuentas().iterator();
            int i = 1;
            while (iterator.hasNext()) {
                SubCuenta subCuenta = (SubCuenta) iterator.next();
                if (i > 1) {
                    sb.append("  ");
                }
                sb.append(subCuenta.getCuentaCorta() == null ? "" : subCuenta.getCuentaCorta());
                sb.append("  ");
                sb.append(subCuenta.getCuentaPadre().getCuentaContable());
                sb.append("  ");
                sb.append("\n");
                sb.append("  ");
                i++;
            } // while
            this.setVariableValue("DESCRIPCION", sb.toString());
            Integer tipoOperacion = (Integer) checkField("DEBE_HABER", new Integer(5));
            if (checkParameter("TIPO_REPORTE") == "1") {
                Double Importe = (Double) checkField("IMPORTE", 0.00);
                if (tipoOperacion == 0) {
                    this.setVariableValue("DEBE", Importe.doubleValue());
                    this.setVariableValue("HABER", 0.00);
                } else {
                    this.setVariableValue("HABER", Importe.doubleValue());
                    this.setVariableValue("DEBE", 0.00);
                } //if
                int tipoPolizaId = Integer.valueOf(getFieldValue("CLASIFICACION_POLIZA_ID").toString());
                int ejercicio = Integer.valueOf(getParameterValue("EJERCICIO").toString());
                int mes = Integer.valueOf(getFieldValue("MES").toString()) - 1;
                sia.libs.formato.Fecha fecha = null;
                int mesFecha = Fecha.getDiasEnElMes(ejercicio, mes);
//         if(Cadena.isCadena(getFieldValue("MAESTRO_OPERACION").toString()) || !getFieldValue("MAESTRO_OPERACION").toString().equals("99")){
//                if (getFieldValue("MAESTRO_OPERACION").toString() instanceof String || !getFieldValue("MAESTRO_OPERACION").toString().equals("99")) {
//           setVariableValue("AUTOMATICO", "GENERADA POR EL SISTEMA");
//                    setVariableValue("IMPRIME_FIRMA", true);
//                } else {
//                    setVariableValue("IMPRIME_FIRMA", false);
//                }
                setVariableValue("FECHA_MAY", String.valueOf(mesFecha));
                if (tipoPolizaId == 1) {
                    this.setVariableValue("CLASIFICACION_POLIZA", getFieldValue("CONCEPTO").toString() + " DEL MES DE " + getFieldValue("NOMBRE_MES").toString() + " AMBITO " + getFieldValue("AMBITO").toString());
                } else {
                    if (checkField("POLIZA_REFERENCIA") != null) {
                        consecutivoPoliza = datosReportes.getPolizaCancelada(getFieldValue("POLIZA_REFERENCIA").toString(), "consecutivo");
                        mesOriginal = datosReportes.getPolizaCancelada(getFieldValue("POLIZA_REFERENCIA").toString(), "mes");
                        this.setVariableValue("CLASIFICACION_POLIZA", "De Cancelación a la póliza " + getFieldValue("UNIDAD_EJECUTORA").toString() + " " + getFieldValue("ENTAMB").toString() + consecutivoPoliza + " " + getFieldValue("CONCEPTO").toString() + " DEL MES DE " + mesOriginal.toUpperCase() + " AMBITO " + getFieldValue("AMBITO").toString());
                    } else {
                        this.setVariableValue("CLASIFICACION_POLIZA", getFieldValue("CONCEPTO").toString() + " DEL MES DE " + getFieldValue("NOMBRE_MES").toString() + " AMBITO " + getFieldValue("AMBITO").toString());
                    }
                }
            } else {
                if (checkParameter("TIPO_REPORTE") == "2") {
                    if (tipoOperacion == 0) {
                        this.setVariableValue("DEBE", "$$$$");
                    } else {
                        this.setVariableValue("HABER", "$$$$");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            /*if(am !=null){
             Configuration.releaseRootApplicationModule(am, true);
             }
             am = null;  */
        }
    }
    /* public String getPolizaCancelada(String polizaId){
     Sentencias sentencia = null;
     Map parametros = null;
     List<Vista> registros = null;
     String desCiudad = null;
     String regresa = null;
     try{
     sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
     parametros = new HashMap();
     registros = new ArrayList<Vista>();
     parametros.put("polizaId",polizaId);
     registros = sentencia.registros("reportes.select.obtenerPolizaCancelada",parametros);
     if(registros!=null){
     for(Vista registro:registros){ 
     regresa = registro.getField("POLIZA");
     }
        
     }
     }
     catch(Exception e){
     e.printStackTrace();
     }
     finally{
     sentencia = null;
     registros = null;
     parametros = null;
     }
     return regresa;
     }*/
}