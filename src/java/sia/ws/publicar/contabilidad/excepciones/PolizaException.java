package sia.ws.publicar.contabilidad.excepciones;



public class PolizaException extends Exception {

  public static final int IMPORTES_CON_CEROS            = 0;  
  public static final int IMPORTE_INCONSISTENTE         = 1;
  public static final int IMPORTE_DEBE                  = 2;
  public static final int IMPORTE_HABER                 = 3;
  public static final int PREPARAR_POLIZA               = 4;
  public static final int INCORPORAR_IMPORTES           = 5;
  public static final int CUENTA_CONTABLE               = 6;
  public static final int REGISTRO_CUENTA_CONTABLE      = 7;  
  
  
  private static String[] mensajes= 
  {
   "Hay importes con ceros,verificar por favor",
   "No coinciden las sumas del debe con el haber",
   "No existen importes para el debe",
   "No existen importes para el haber",
   "Los detalles para generar la poliza deben ser minimo 2 cuentas contables",
   "Error al incorporar los importes a las cuentas contables",
   "No existe la cuenta contable",
   "No existe la forma contable",
   "No fue posible insetar la cuenta contable"   
  };
  
  public PolizaException() {
    super("Error en la afectaci�n de la P�liza.");
  }

  public PolizaException(String mensaje) {
    super(mensaje);
  }

  public PolizaException(int numeroMensaje) {
    super(mensajes[numeroMensaje]);
  }
  
  

}
