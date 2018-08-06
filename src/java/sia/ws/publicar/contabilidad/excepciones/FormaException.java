package sia.ws.publicar.contabilidad.excepciones;

public class FormaException extends Exception {
    public static final int FORMA_CONTABLE       = 0;
    
    private static String[] mensajes= 
    {
      "No existe la forma contable"
    };
    
    
    public FormaException() {
      super("Error en la obtencion de la forma contable.");
    }

    public FormaException(String mensaje) {
      super(mensaje);
    }

    public FormaException(int numeroMensaje) {
      super(mensajes[numeroMensaje]);
    }
    
    
    
}
