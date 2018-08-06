package sia.rf.contabilidad.registroContableNuevo;

public class bcCuentaContableArmonizadaV2 extends bcCuentaContableArmonizada {
  
  public static final int NIVEL_OPERACIONES = 5;
  public static final int NIVEL_UNIDAD = 4;
  public static final int NIVEL_AMBITO = 5;
  
  public bcCuentaContableArmonizadaV2() {
  }


  protected String getCond(String pUniEje, String temAmbito, int nivRep) {
      String condicion=null;
      if(nivRep == NIVEL_UNIDAD)  
        condicion=" and substr(cuenta_contable,10,4) = '" + pUniEje +"'"; 
      if(nivRep >= NIVEL_AMBITO)
        condicion= " and substr(cuenta_contable,10,8) = '" + pUniEje + temAmbito + "'"; 
      return condicion;
    
  }
}
