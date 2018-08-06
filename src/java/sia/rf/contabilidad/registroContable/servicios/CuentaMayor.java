package sia.rf.contabilidad.registroContable.servicios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;
import sia.libs.formato.Error;


public class CuentaMayor {
  private Integer longitudCtaMayor;
  private Integer longitudMayor;
  private String carater;
  private String mayor;
 // private ModuloRegistroContableImpl am;
  private Integer cuentaMax; 
  private int ejercicio;
  
  public CuentaMayor(int ejercicio, Connection conexion) {
    cuentaMax=-1;
    setEjercicio(ejercicio);
    procesoWs(conexion);
  }
  
 /* public CuentaMayor(ModuloRegistroContableImpl am, int ejercicio) {
    cuentaMax=-1;
    this.setAm(am);
    setEjercicio(ejercicio);
    proceso();
  }*/
  
  
 
/*  private void proceso(){
    ViewObject voRecInf = null;
    try {
      voRecInf = am.createViewObjectFromQueryStmt("VistaInforGral","select max(length(cuenta_mayor)) from RF_TC_CLASIFICADOR_CUENTAS");
      voRecInf.executeQuery();
      voRecInf.first();
      setLongitudCtaMayor(Integer.parseInt(voRecInf.first().getAttribute(0).toString()));
      voRecInf.remove();
      voRecInf = am.createViewObjectFromQueryStmt("VistaInforGral","select max(longitud),caracter from RF_TR_CONFIGURA_CLAVES group by caracter");
      voRecInf.executeQuery();
      voRecInf.first();
      setLongitudMayor(Integer.parseInt(voRecInf.first().getAttribute(0).toString()));
      setCarater((String)voRecInf.first().getAttribute(1));
      voRecInf.remove();   
    }//try
    catch (Exception e){
      Error.mensaje(this,e,"proceso","error al recuperar la informacion");
    }// catch
  }*/
  
  // metodo usado para el ws
  private void procesoWs(Connection conexion){
   Sentencias sentencia;
   List<Vista> regConfClaves;
   try {
     regConfClaves = new ArrayList<Vista>();
     sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
     setLongitudCtaMayor(Integer.valueOf(sentencia.consultar(conexion,"webService.select.maxClasificadorCuentas","").toString()));
     regConfClaves = sentencia.registros( sentencia.getComando("webService.select.maxConfiguraClaves",""),conexion);
     setLongitudMayor(Integer.valueOf(regConfClaves.get(0).getField("LONGITUD")));
     setCarater(regConfClaves.get(0).getField("CARACTER"));
   }//try
   catch (Exception e){
     Error.mensaje(this,e,"proceso","error al recuperar la informacion en la clase CuentaMayor");
   }// catch
  }
  
/*  private Integer obtenerCuentaMax()
  {
    Formatos formatos=null;
    UtileriasComun util= null;
    ViewObject cuentaMaxi = null;
    if(cuentaMax.equals(-1))
    {
      try{
        util=new UtileriasComun("ModuloRegistroContableDataControl");
        formatos=new Formatos(Contabilidad.getInstance().getPropiedad("clasificadorCuenta.query.obtenerMaxCuentaFechaAct"),String.valueOf(getEjercicio()));
        cuentaMaxi=util.createViewObjectFromSentencia(formatos.getSentencia());
        this.cuentaMax= cuentaMaxi.getRowCount()>0?Integer.parseInt(cuentaMaxi.first().getAttribute(0).toString()):0;
        util=null;
        cuentaMaxi=null;
      }
      catch(Exception e){
        System.err.print(Error.getMensaje(this,"Contabilidad","obtenerCuentaMax",e.getMessage()));
      }
      finally{
        formatos = null;
        util = null;
        cuentaMaxi = null;
      }
    }
    return this.cuentaMax;
    
  }
  */
  /*
   * Este metodo se cre� nuevamente para no usar el modulo
   * es el mismo con obtenerCuentaMax()
   * */
  private Integer obtenerCuentaMaxima()
  {
    Sentencias sentencia;
    //Formatos formatos=null;
    //UtileriasComun util= null;
    //ViewObject cuentaMaxi = null;
    List<Vista> registros;
    Map parametros = new HashMap();
    
    if(cuentaMax == null || cuentaMax.equals(-1))
    {
      try{
        sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);
        parametros.put("ejercicio",getEjercicio());
        registros = sentencia.registros("clasificadorCuenta.select.obtenerMaxCuentaFechaAct",parametros);
        if(registros == null) {
              registros = new ArrayList<Vista>();
          }
        //util=new UtileriasComun("ModuloRegistroContableDataControl");
        //formatos=new Formatos(Contabilidad.getInstance().getPropiedad("clasificadorCuenta.query.obtenerMaxCuentaFechaAct"),String.valueOf(getEjercicio()));
        //cuentaMaxi=util.createViewObjectFromSentencia(formatos.getSentencia());
        if(registros.size()>0) {
              this.cuentaMax = Integer.valueOf(registros.get(0).getField("CUENTA_CONTABLE_ID").toString());
          }
        //this.cuentaMax= cuentaMaxi.getRowCount()>0?Integer.parseInt(cuentaMaxi.first().getAttribute(0).toString()):0;
        //util=null;
        //cuentaMaxi=null;
      }
      catch(Exception e){
        System.err.print(Error.getMensaje(this,"Contabilidad","obtenerCuentaMaxima",e.getMessage()));
      }
    }
    return this.cuentaMax;
  }

  public String rellenaRaiz(){
    String strCadena ="";
    try{
      for (int i =1; i <=getLongitudMayor();i++) {
            strCadena+= getCarater();
        }
    }//try
    catch(Exception e){
      Error.mensaje(this,e,"rellenaRaiz","error al rellenar la raiz");
    }// catch
    return strCadena;
  }

  public String rellenaRaizNivel(ArrayList ArNiveles){
    String strCadena="";
    try{
      for(int i=0; i< ArNiveles.size();i++){
        for (int j=1;j <= (Integer)ArNiveles.get(i);j++){
          strCadena+=getCarater();
        }// for i
        strCadena+=",";
      }//for
    }
    catch (Exception e){
      Error.mensaje(this,e,"rellenaRaizNivel","error al rellenar la raiz con comas");
    }// catch
    return strCadena;
  }

  public String regresaCondicionRaiz(){
    String strCadena="";
    try{
      for (int i= 1;i<= getLongitudCtaMayor();i++) {
            strCadena+="_";
        }
      for(int i=1; i <9;i++){
        strCadena+=getCarater();
      }//for
    }
    catch (Exception e){
      Error.mensaje(this,e,"rellenaRaizNivel","error formular la condicion de raiz");
    }// catch
    return strCadena;

  }
  
/*  public String rellenarClaves(int parametro,String strClave,String strClave1){
   String strClaveRellena = ""; 
    try{ 
      Cuenta cuenta = new Cuenta(1, am);
      if (parametro==1)  // unidad ejecutora
        strClaveRellena = cuenta.rellenaCeros(3,strClave);
      else //entidad y ambito
        strClaveRellena = cuenta.rellenaCeros(3,strClave+strClave1);
    }//try
    catch (Exception e){
      Error.mensaje(this,e,"rellenarClaves","error al rellenar la clave");
    }
    return strClaveRellena;
  }*/
  
/*  public String rellenarClaves(String strUnidad){
    String strClaveRellena = ""; 
    try{ 
      Cuenta cuenta = new Cuenta(obtenerCuentaMax(), am);
      strClaveRellena = cuenta.rellenaCeros(3,strUnidad);
    }//try
    catch (Exception e){
      Error.mensaje(this,e,"rellenarClaves","error al rellenar la clave");
    }
    return strClaveRellena;
  }*/
  
  /*
   * Este metodo se cre� nuevamente para no usar el modulo
   * es igual rellenarClaves(String strUnidad)
   * */
  public String rellenarCadena(String strUnidad, Connection conexion){
    String strClaveRellena = ""; 
    try{ 
      Cuenta cuenta = new Cuenta(obtenerCuentaMaxima(), conexion);
      strClaveRellena = cuenta.rellenaCeros(3,strUnidad);
    }//try
    catch (Exception e){
      Error.mensaje(this,e,"rellenarClaves","error al rellenar la clave");
    }
    return strClaveRellena;
  }
  
/*  public String rellenarClaves(String strEntidad,String strAmbito){
    String strClaveRellena = ""; 
    try{ 
      Cuenta cuenta = new Cuenta(obtenerCuentaMax(), am);
      strClaveRellena = cuenta.rellenaCeros(4,strEntidad+strAmbito);
    }//try
    catch (Exception e){
      Error.mensaje(this,e,"rellenarClaves","error al rellenar la clave");
    }
    return strClaveRellena;
  }*/
  
  /*
   * Este metodo se creo nuevamente para no usar el modulo
   * es igual rellenarClaves(String strEntidad,String strAmbito)
   * */
  public String rellenarCadena(String strEntidad,String strAmbito,Connection conexion){
    String strClaveRellena = ""; 
    try{ 
      Cuenta cuenta = new Cuenta(obtenerCuentaMaxima(), conexion);
      strClaveRellena = cuenta.rellenaCeros(4,strEntidad+strAmbito);
    }//try
    catch (Exception e){
      Error.mensaje(this,e,"rellenarClaves","error al rellenar la clave");
    }
    return strClaveRellena;
  }
  
/*  public int getPosicion(int iNivel){
    int iPosicion = 1;
   try{ 
      Cuenta cuenta = new Cuenta(obtenerCuentaMax(), am);
      for (int i=0; i< iNivel-1;i++)
        iPosicion += Integer.parseInt(cuenta.getLongitudNivel().get(i).toString());
    }//try
    catch (Exception e){
      Error.mensaje(this,e,"getLongitud","error al obtener la longitud");
    }
    return iPosicion;
  }*/
  
  /*
   * Metodo que no usa modulo
   * Es igual al de getPosicion(int iNivel)
   * */
  public int getPosicionNivel(int iNivel, Connection conexion){
    int iPosicion = 1;
    try{ 
      Cuenta cuenta = new Cuenta(obtenerCuentaMaxima(), conexion);
      for (int i=0; i< iNivel-1;i++) {
            iPosicion += Integer.parseInt(cuenta.getLongitudNivel().get(i).toString());
        }
    }//try
    catch (Exception e){
      Error.mensaje(this,e,"getLongitud","error al obtener la longitud");
    }
    return iPosicion;
  }
  
 /* public int getLongitud(int iNivel){
    int iLongitud = 0;
    try{ 
      Cuenta cuenta = new Cuenta(13705, am);
      iLongitud = Integer.parseInt(cuenta.getLongitudNivel().get(iNivel).toString());
    }//try
    catch (Exception e){
      Error.mensaje(this,e,"getLongitud","error al obtener la longitud");
    }
    return iLongitud;
  }*/
  
  public void setLongitudCtaMayor(Integer longitudCtaMayor) {
    this.longitudCtaMayor = longitudCtaMayor;
  }

  public Integer getLongitudCtaMayor() {
    return longitudCtaMayor;
  }

  public void setLongitudMayor(Integer longitudMayor) {
    this.longitudMayor = longitudMayor;
  }

  public Integer getLongitudMayor() {
    return longitudMayor;
  }

  public void setCarater(String carater) {
    this.carater = carater;
  }

  public String getCarater() {
    return carater;
  }
  
    @Override
  public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append("Cuenta Mayor [");
    sb.append(getLongitudCtaMayor());
    sb.append(",");
    sb.append(getLongitudMayor());
    sb.append(",");
    sb.append(getCarater());
    sb.append("]");
    return sb.toString();
  }
/*  public static void main(String[] args) {
    CuentaMayor cuenta= new CuentaMayor(null, 2007);
 //   System.out.println(cuenta.toString());
  //  System.out.println(cuenta.rellenaRaiz());
//    ArrayList arLista = new ArrayList();
//    arLista.add(5);
//    arLista.add(4);
//    arLista.add(4);
//    arLista.add(4);
//    System.out.println(cuenta.rellenaRaizNivel(arLista));
//    System.out.println(cuenta.regresaCondicionRaiz());
    System.out.println(cuenta.rellenarClaves("101"));
    System.out.println(cuenta.rellenarClaves("8","3"));
    System.out.println(cuenta.getPosicion(3));
    System.out.println(cuenta.getLongitud(3));

  }*/

  public void setMayor(String mayor) {
    this.mayor = mayor;
  }

  public String getMayor() {
    return mayor;
  }

 /*   public void setAm(ModuloRegistroContableImpl am) {
        this.am = am;
    }

    public ModuloRegistroContableImpl getAm() {
        return am;
    }*/
    
 /* public int getCuentaMayorId(String cuentaMayor){
    Formatos formatos = null;
    ViewObject vista = null;
    int cuentaMayorId = -1;
    try  {
      formatos=new Formatos(Contabilidad.getInstance().getPropiedad("cuentaMayor.query.cuentaMayorId"),cuentaMayor);
      vista = am.createViewObjectFromQueryStmt("VistaCuentaMayor",formatos.getSentencia());
      vista.executeQuery();
      cuentaMayorId = ((Number)vista.first().getAttribute(0)).intValue();
    }//end try
    catch (Exception e){
      Error.mensaje(e, "CONTABILIDAD");
      e.printStackTrace();
    }//end catch
    finally{
      if(vista!=null){
        vista.remove();
      }
      if(am.findViewObject("VistaCuentaMayor")!=null){
        am.findViewObject("VistaCuentaMayor").remove();
      }
      formatos=null;
      vista=null;
    }//end finally
    return cuentaMayorId;
  }    */
    
/*	public ArrayList getCuentaMayor(String cuentaContable, String fecha)throws Exception{
	  ViewObject vista = null;
    Formatos formatos = null;
	  ArrayList cuentaMayorId = null;
	 try  {
	   
      formatos = new Formatos(Contabilidad.getInstance().getPropiedad("cuentaMayor.query.obtenerCuentaMayorId"),cuentaContable, fecha);
	    vista = am.createViewObjectFromQueryStmt("VistaCuentaMayor", formatos.getSentencia());
	    vista.executeQuery();
		cuentaMayorId = new ArrayList();
		cuentaMayorId.add(vista.first().getAttribute(0));
		cuentaMayorId.add(vista.first().getAttribute(1));
	    cuentaMayorId.add(vista.first().getAttribute(2));
	    cuentaMayorId.add(vista.first().getAttribute(3));
	    cuentaMayorId.add(vista.first().getAttribute(4));
	  }//end try 
	  catch(Exception e)  {
		Error.mensaje(e, "CONTABILIDAD");
		e.printStackTrace();
		throw new Exception();
	  }//end catch 
	  finally{
		if(vista!=null){
		  vista.remove();
		}
		if(am.findViewObject("VistaCuentaMayor")!=null){
		  am.findViewObject("VistaCuentaMayor").remove();
		}
		vista=null;
    formatos=null;
	  }//end finally
    return cuentaMayorId;
	}//public int regresarCuentaMayorId*/

  public void setEjercicio(int ejercicio) {
    this.ejercicio = ejercicio;
  }

  public int getEjercicio() {
    return ejercicio;
  }
}
