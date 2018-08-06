/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sia.ws.publicar.contabilidad;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

 import sia.rf.contabilidad.registroContableEvento.Evento;
 import sia.rf.contabilidad.registroContableEvento.EventoTraspaso;
 
/**
 *
 * @author LUZ.LOPEZ
 */
@WebService(serviceName = "RegistroContableEvento")
public class RegistroContableEvento {



    /**
     * Web service operation
     */
    @WebMethod(operationName = "procesarEvento")
    public String procesarEvento(@WebParam(name = "eventoContable") int eventoContable, @WebParam(name = "eventoProceso") int eventoProceso, @WebParam(name = "parametros") String parametros, @WebParam(name = "claveSeguridadWS") String claveSeguridadWS) {
      String resultado=null;
      try{
          if (eventoContable>=6 && eventoContable<=23){          
                //EventoInventario eventoInventario = new EventoInventario();      
                //resultado = eventoInventario.procesarEvento(eventoContable,eventoProceso,parametros,  claveSeguridadWS);
                resultado= "1, [El evento contable " + eventoContable+ " sera procesado en unos momentos, verificar la generacion de las polizas mediante su referencia.] ";
          }
          else  if ((eventoContable>=1 && eventoContable<=4)||(eventoContable>=40 && eventoContable<=70)){  
                    // EventoConprove eventoConprove = new EventoConprove();      
                   //resultado = eventoConprove.procesarEvento(eventoContable,eventoProceso,parametros,  claveSeguridadWS);
                    resultado= "1, [El evento contable " + eventoContable+ " sera procesado en unos momentos, verificar la generacion de las polizas mediante su referencia.] ";
          }
          else  if (eventoContable>=81 && eventoContable<=92){           
               // EventoSiga eventoSiga = new EventoSiga();      
               // resultado = eventoSiga.procesarEvento(eventoContable,eventoProceso,parametros,  claveSeguridadWS);
                 resultado= "1, [El evento contable " + eventoContable+ " sera procesado en unos momentos, verificar la generacion de las polizas mediante su referencia.] ";
          }
          else   if ((eventoContable>=29 && eventoContable<=31)||(eventoContable>=100 && eventoContable<300)){
              //Evento evento = new Evento();      
              //resultado = evento.procesarEvento(eventoContable,eventoProceso,parametros,  claveSeguridadWS);                
              resultado= "1, [El evento contable " + eventoContable+ " sera procesado en unos momentos, verificar la generacion de las polizas mediante su referencia.] "; 
          }
          else   if (eventoContable==300){ // Eliminacion
                   //Evento evento = new Evento();
                  // resultado = evento.procesarEvento(eventoContable,eventoProceso,parametros,  claveSeguridadWS);     
                  // resultado= "1, [El evento contable " + eventoContable+ " ha sido procesado y generó las siguientes pólizas: "+resultado +" ] ";
                    resultado= "1, [El evento contable " + eventoContable+ " sera procesado en unos momentos, verificar la generacion de las polizas mediante su referencia.] "; 
          }   
           else   if (eventoContable>=301 && eventoContable<=302 ){ // Cuenta Publica
                   // EventoTraspaso eventoTraspaso = new EventoTraspaso();
                   // resultado = eventoTraspaso.procesarEvento(eventoContable,eventoProceso,parametros,  claveSeguridadWS);     
                   // resultado= "1, [El evento contable " + eventoContable+ " ha sido procesado y generó las siguientes pólizas: "+resultado +" ] ";
                    resultado= "1, [El evento contable " + eventoContable+ " sera procesado en unos momentos, verificar la generacion de las polizas mediante su referencia.] ";
           }            
          else   if (eventoContable>=350 && eventoContable<=355){
              //  EventoSici eventoSici = new EventoSici();      
               // resultado = eventoSici.procesarEvento(eventoContable,eventoProceso,parametros,  claveSeguridadWS);     
                resultado= "1, [El evento contable " + eventoContable+ " sera procesado en unos momentos, verificar la generacion de las polizas mediante su referencia.] ";
          }  
          else
                return "0, No existe el evento contable: " + eventoContable + " para ser procesado.";
         }catch(Exception e){
             resultado="0, Ocurrio un errror al accesar el metodo procesarEvento "+e.getMessage() +"  para el evento contable " +eventoContable;  
         }
      return resultado;
    }
}
