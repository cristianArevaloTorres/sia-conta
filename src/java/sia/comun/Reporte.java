package sia.comun;



//~--- JDK imports ------------------------------------------------------------

//import java.sql.Connection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
//import oracle.adf.controller.v2.context.PageLifecycleContext;
//import oracle.adf.model.binding.DCBindingContainer;
//import oracle.jbo.server.ApplicationModuleImpl;
//import sia.configuracion.UtileriasComun;

//import sia.db.dao.DaoFactory;

//~--- classes ----------------------------------------------------------------

public class Reporte {

    private HttpServletRequest request;
    private String strJspReporte;
    private static String PAQ_REPORTE="/Librerias/reportes/";
    private String rutaAlojar;
    private String formatoSalida;
    private String strContexto;
    private ExistenRegistros existenReg;

    public Reporte() 
    {
        strJspReporte="generaReporte.jsp";
        formatoSalida="pdf";
        request=null;
        //existenReg=null;
    }
    
  public Reporte(String pagina, String formato) 
  {
      strJspReporte=pagina;
      formatoSalida=formato;
      request=null;
      //existenReg=null;
  }

    //~--- methods --------------------------------------------------------------

     public void generar(HttpServletRequest request, String strQuery, Map mapParams,String rutaJasper,String formatoSalida,String titulo,String strModulo, int iConexion) {
        //request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        this.request = request;
        existenReg=new ExistenRegistros(iConexion,strQuery);
        genImpresion(strQuery, mapParams,rutaJasper,formatoSalida,titulo,strModulo, iConexion);
    }
    
    private void genImpresion(String strQuery, Map mapParams,String rutaJasper,String formatoSalida,String titulo,String strModulo, int iConexion){
      if(existenReg.isExistenReg()){
       StringBuffer query = new StringBuffer();
       strContexto = request.getContextPath();
       strContexto=strContexto + PAQ_REPORTE+strJspReporte;
            rutaAlojar=rutaJasper.substring(rutaJasper.lastIndexOf("/")+1,rutaJasper.length());
      imprimir(query.append(strQuery), mapParams, rutaAlojar, rutaJasper, iConexion, formatoSalida, titulo, strModulo);  
      }
    }
     
     /*public void generar(HttpServletRequest request,String strQuery, Map mapParams,String rutaJasper,String formatoSalida,String titulo,String strModulo,int iConexion){
          generar(request, strQuery, mapParams, rutaJasper, formatoSalida, titulo, strModulo, iConexion);
     }*/
      
     public void generar(HttpServletRequest request,String strQuery, Map mapParams,String rutaJasper,String titulo,String strModulo,int iConexion){
         generar(request, strQuery,mapParams,rutaJasper,formatoSalida,titulo,strModulo,iConexion);
     }
     
    /* public void generar(HttpServletRequest request, String strQuery, Map mapParams,String rutaJasper,String titulo,String strModulo,int iConexion){
        generar(request, strQuery,mapParams,rutaJasper,formatoSalida,titulo,strModulo, iConexion);
     }*/
     
  public void imprimir(HttpServletRequest request, StringBuffer query, Map params, String nombreArchivoDest, String rutaArchivoJasper, 
    int conexion, String formatoSalida, String accionPagina, String modulo) {
       this.request = request;
       imprimir(query, params, nombreArchivoDest, rutaArchivoJasper, 
      conexion, formatoSalida, accionPagina, modulo, false);
  }
  
  public void imprimir(StringBuffer query, Map params, String nombreArchivoDest, String rutaArchivoJasper, 
    int conexion, String formatoSalida, String accionPagina, String modulo) {
       imprimir(query, params, nombreArchivoDest, rutaArchivoJasper, 
      conexion, formatoSalida, accionPagina, modulo, false);
  }
  
  public void imprimir(StringBuffer query, Map params, String nombreArchivoDest, String rutaArchivoJasper, 
    int iConexion, String formatoSalida, String accionPagina, String modulo, boolean divideClaveCuenta){
    try {
      //request = request==null?((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()):request;
      request.getSession().setAttribute("abrirReporte", "abrir");
      request.getSession().setAttribute("nombreArchivo", nombreArchivoDest);
      request.getSession().setAttribute("rutaArchivo", rutaArchivoJasper);
      request.getSession().setAttribute("parametros", params);
      request.getSession().setAttribute("conexion", iConexion);
      request.getSession().setAttribute("query", query);
      request.getSession().setAttribute("formatoSalida", formatoSalida);
      request.getSession().setAttribute("accionPagina", accionPagina);
      request.getSession().setAttribute("modulo", modulo);
      request.getSession().setAttribute("divideClaveCuenta", divideClaveCuenta);
    }
    catch ( Exception e ) {
      System.out.println(e.toString());
    }
  }
  
    public void setStrContexto(String strContexto) {
        this.strContexto = strContexto;
    }

    public String getStrContexto() {
        return strContexto;
    }

  public void setStrJspReporte(String strJspReporte) {
    this.strJspReporte = strJspReporte;
  }

  public String getStrJspReporte() {
    return strJspReporte;
  }

  public ExistenRegistros getExistenReg() {
    return existenReg;
  }
}
