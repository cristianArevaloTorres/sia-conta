package sia.comun;

import java.util.List;

//import oracle.adf.controller.v2.context.PageLifecycleContext;

//import oracle.adf.model.binding.DCBindingContainer;

//import oracle.jbo.ApplicationModule;

//import oracle.jbo.server.ApplicationModuleImpl;

import sia.libs.formato.Error;

//import sia.configuracion.Modulo;
//import sia.configuracion.UtileriasComun;

//import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;

public class ExistenRegistros {

  private boolean existenReg;
  //private ApplicationModule AM;
  

  /*public ExistenRegistros(String moduloDataControl, String query, PageLifecycleContext ctx) {
    if(ctx!=null)
      AM=(ApplicationModule)((DCBindingContainer)ctx.getBindingContainer()).getDataControl().getApplicationModule();
    else
      AM = Modulo.obtenerModulo(moduloDataControl)==null?null : Modulo.obtenerModulo(moduloDataControl);
    existenReg=verificaExistenRegistros(AM,query);
  }*/
  
  /*public ExistenRegistros(String moduloDataControl, String query) {
    this(moduloDataControl,query,null);
  }*/
  
  public ExistenRegistros(int tipoConexion, String query) {
    this.existenReg=verificaExistenRegistros(tipoConexion,query);
  }
  
  private boolean verificaExistenRegistros(int tipoConexion, String query) {
    boolean regresa = false;
    Sentencias sentencias = new Sentencias(tipoConexion, Sentencias.XML);
    try  {
      List registros = sentencias.registros(query);
      regresa = registros != null && registros.size() > 0;
    } catch (Exception ex)  {
      Error.mensaje(ex,"SIAFM");
    } finally  {
      sentencias = null;
    }
    return regresa;
  }
  

 /* private boolean verificaExistenRegistros(ApplicationModule am, String query)
  {
    boolean regresa=false;
    UtileriasComun util=new UtileriasComun((ApplicationModuleImpl)am);
    try{
      util.removerVistas("nueva");
      regresa=(util.crearVista("nueva",query)).getRowCount()>0?true:false; 
      //regresa=am.getTransaction().executeCommand(query)>0?true:false;
    }
    catch(Exception e)
    {
      System.err.println(Error.getMensaje(this,"Contabilidad","verificaExistenRegistros",e.getMessage()));
    }
    finally
    {
      util.removerVistas("nueva");
      util=null;
    }
    return regresa;
    
  }*/

  public boolean isExistenReg() {
    return existenReg;
  }
}
