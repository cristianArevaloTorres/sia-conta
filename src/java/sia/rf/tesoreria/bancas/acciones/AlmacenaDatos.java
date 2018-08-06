package sia.rf.tesoreria.bancas.acciones;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.util.Iterator;
import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;

public class AlmacenaDatos {

    private String ruta;
    private String archivoleer;
    boolean correcto;
    public static final String RUTA_DEFAULT="tesoreria//descargas//adminTransaccion//bancas//";
    
    public AlmacenaDatos(String nombre) {
      setRuta(RUTA_DEFAULT);
      setArchivoleer(nombre);
      setCorrecto(true);
    }
    
    public AlmacenaDatos(String ruta, String nombre) {
      setRuta(ruta);
      setArchivoleer(nombre);
      setCorrecto(true);
    }
    
  
    
    public boolean leerRegistros(Connection conn,String seccion, String idPrograma){
      boolean regresa = true;
      String token;
      FileReader fr;
      try {
        System.out.println("archivo de movimientos: "+getArchivoleer());
        System.out.println("ruta: "+getRuta());
        fr = new FileReader(getRuta() +"//"+ getArchivoleer()); 
        System.out.println("Archivo abierto ");
        BufferedReader linea = new BufferedReader(fr);
        RegistroBanca registroBanca = new RegistroBanca("loyoutTesoreria",seccion);
        while ((token=linea.readLine()) != null  && regresa ) {
          if (!token.equals("")){
              if (seccion.equals("independiente")){
                if(idPrograma.equals("8")){ 
                    if(registroBanca.movimientosBBVA(token)) {
                        regresa = obtieneDatos(registroBanca, conn,seccion);
                    }
                    else {
                        regresa = false;
                    }
                }else if (idPrograma.equals("11")) {
                            if(registroBanca.movimientosBajio(token)) {
                        regresa = obtieneDatos(registroBanca, conn,seccion);
                    }
                            else {
                        regresa = false;
                    }
                }
                    else {
                        if(registroBanca.movimientosMultiva(token)) {
                            regresa = obtieneDatos(registroBanca, conn,seccion);
                        }
                        else {
                            regresa = false;
                        }
                    }
              }
              else {
                if(registroBanca.tokens(token)) {
                      regresa = obtieneDatos(registroBanca, conn, seccion);
                  }
                else {
                      regresa = false;
                  }
              }
          }
          else { 
            regresa = false;
          }
        }// while
        linea.close();
        fr.close();
      }
      catch (Exception e) {
          e.printStackTrace();
      }  
      return regresa; /// false si fallo algo 
    }
    
    public String quitaCaracter(String cadena, String caracter){
     int longitud= cadena.length();
     while(cadena.indexOf(caracter)!=-1) {
       int posicion= cadena.indexOf(caracter);
       cadena= cadena.substring(0, posicion).concat(cadena.substring(posicion+1, longitud--));
     }
     return cadena;
    }
    
     
    public boolean obtieneDatos(RegistroBanca registroBan, Connection conn, String seccion){
      boolean regresa;
      boolean dobleRegistro = false;
      StringBuffer listaCampos = new StringBuffer();
      StringBuffer listaValores = new StringBuffer();
      StringBuffer auxListaCampos = new StringBuffer();
      StringBuffer auxListaValores = new StringBuffer();
      Iterator iteratorColumna = registroBan.getNombresColumnas();
      Iterator iteratorTipo = registroBan.getTipoDatos();
      Iterator iteratorValores = registroBan.getValores();
      try {
        while(iteratorValores.hasNext() && isCorrecto()){
            String valor = ((String)iteratorValores.next());
            String columna = ((String)iteratorColumna.next());
            
          
          if(!seccion.equals("independiente")){
              String tipo = ((String)iteratorTipo.next());
              if (columna.equals("clave_trans")){
                if (seccion.equals("movSantander")) {
                      valor = "lpad('".concat(valor).concat("',4,'0')");
                  }
                else if (seccion.equals("movBanamex")) {
                      valor = "lpad('".concat(valor).concat("',3,'0')");
                  }  
              }  
              
              if(seccion.equals("movSantander")){
               
                  if (columna.equals("id_tipo_trans")){
                    if (valor.equals("+")) {
                          valor = "CR";
                      }  
                    else {
                          valor = "DR";
                      }  
                  }
                  
                  if (columna.equals("saldo")){
                      listaCampos.append("tipo_saldo").append(",");
                        if(valor.contains("-")) {
                          listaValores.append("'DR'").append(",");
                      }
                        else {
                          listaValores.append("'CR'").append(",");
                      }    
                  }
                  
              }
              
              if (tipo.equals("texto")){
                if (columna.equals("monto") || columna.equals("saldo") || columna.equals("saldoinicial")  ) {
                  if (valor.contains(",")) {
                        valor = quitaCaracter(valor,",");
                    }
                  if (valor.contains("(")){
                    valor = quitaCaracter(valor,"(");
                    valor = "-".concat(valor);
                  }
                  if (valor.contains(")")) {
                        valor = quitaCaracter(valor,")");
                    }
                } 
                if (!columna.equals("clave_trans")){
                  valor = valor.replaceAll("'"," ");
                  valor = new StringBuffer().append("'").append(valor).append("'").toString();
                }
              }
              
              if (tipo.equals("fecha")){
                if (seccion.equals("movSantander")) {
                      valor = "to_date('"+valor.substring(4,8)+valor.substring(2,4)+valor.substring(0,2)+"','yyyymmdd')";
                  }
                else if (seccion.equals("movBanamex")) {
                      valor = "to_date('"+valor.substring(0,2)+valor.substring(2,4)+valor.substring(4,6)+"','yymmdd')";
                  }
                    else if (seccion.equals("movimientos")) {
                      valor = "to_date('"+valor.substring(6,8)+"/"+valor.substring(4,6)+"/"+valor.substring(0,4)+ " " +
                                          valor.substring(8,10) +":" + valor.substring(10,12) + ":"+ valor.substring(12,14) +"','dd/mm/yyyy HH24:mi:ss')";
                  }
              }
          }
        
          listaCampos.append(columna).append(",");
          listaValores.append(valor).append(",");
          
          if (columna.equals("2DOREGISTROBAJIO") && valor.equals("2DOREGISTROBAJIO")) {
                dobleRegistro = true;
            }
          
        }
        
        if (dobleRegistro){
            auxListaCampos.append(listaCampos.substring(0,listaCampos.indexOf("2DOREGISTROBAJIO")));
            auxListaValores.append(listaValores.substring(0,listaValores.indexOf("2DOREGISTROBAJIO")));
            auxListaCampos.append("id_movimiento");
            auxListaValores.append("SEQ_TR_MOVIMIENTOS_CUENTA_TMP.nextval");
            regresa = insertaDatos(auxListaCampos,auxListaValores, conn);
            if (regresa) {
                if (listaValores.indexOf("3ERREGISTROBAJIO")!=-1){
                    auxListaCampos.delete(0,auxListaCampos.length());
                    auxListaValores.delete(0,auxListaValores.length());
                    auxListaCampos.append(listaCampos.substring(listaCampos.indexOf("2DOREGISTROBAJIO")+17,listaCampos.indexOf("3ERREGISTROBAJIO")));
                    auxListaValores.append(listaValores.substring(listaValores.indexOf("2DOREGISTROBAJIO")+17,listaValores.indexOf("3ERREGISTROBAJIO")));
                    auxListaCampos.append("id_movimiento");
                    auxListaValores.append("SEQ_TR_MOVIMIENTOS_CUENTA_TMP.nextval");
                    regresa = insertaDatos(auxListaCampos,auxListaValores, conn);
                    if (regresa){
                        auxListaCampos.delete(0,auxListaCampos.length());
                        auxListaValores.delete(0,auxListaValores.length());
                        auxListaCampos.append(listaCampos.substring(listaCampos.indexOf("3ERREGISTROBAJIO")+17,listaCampos.length()));
                        auxListaValores.append(listaValores.substring(listaValores.indexOf("3ERREGISTROBAJIO")+17,listaValores.length()));
                        auxListaCampos.append("id_movimiento");
                        auxListaValores.append("SEQ_TR_MOVIMIENTOS_CUENTA_TMP.nextval");
                        regresa = insertaDatos(auxListaCampos,auxListaValores, conn);
                        if (!regresa) {
                            setCorrecto(false);
                        }
                    }
                    else {
                        setCorrecto(false);
                    }
                }
                else {
                    auxListaCampos.delete(0,auxListaCampos.length());
                    auxListaValores.delete(0,auxListaValores.length());
                    auxListaCampos.append(listaCampos.substring(listaCampos.indexOf("2DOREGISTROBAJIO")+17,listaCampos.length()));
                    auxListaValores.append(listaValores.substring(listaValores.indexOf("2DOREGISTROBAJIO")+17,listaValores.length()));
                    auxListaCampos.append("id_movimiento");
                    auxListaValores.append("SEQ_TR_MOVIMIENTOS_CUENTA_TMP.nextval");
                    regresa = insertaDatos(auxListaCampos,auxListaValores, conn);
                    if (!regresa) {
                        setCorrecto(false);
                    }
                }
            }
            else{
              setCorrecto(false);
            }
        }
        else {
            listaCampos.append("id_movimiento");
            listaValores.append("SEQ_TR_MOVIMIENTOS_CUENTA_TMP.nextval");
            if (isCorrecto()) {
                regresa = insertaDatos(listaCampos,listaValores, conn);
            }
            else {
                setCorrecto(false);
            }
        }
      }
       catch (Exception e) {
        e.printStackTrace();
        setCorrecto(false);
      }
     return isCorrecto();
    }
    
    public boolean insertaDatos(StringBuffer listaCampos, StringBuffer listaValores, Connection conn){
      int ejecuto;
      Sentencias sentencias = new Sentencias(DaoFactory.CONEXION_TESORERIA);
      try  {
        ejecuto = sentencias.ejecutar(conn,"insert into RF_TR_MOVIMIENTOS_CUENTA_TMP (".concat(listaCampos.toString()).concat(") values (").concat(listaValores.toString()).concat(")"));
        if (ejecuto!=-1){
          setCorrecto(true);
        }
        else {
          setCorrecto(false);
        }
      } catch (Exception e) {
          e.printStackTrace();
         setCorrecto(false);
      }
      return isCorrecto();
    }// insertaDatos


    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getRuta() {
        return ruta;
    }

    public void setArchivoleer(String archivoleer) {
        this.archivoleer = archivoleer;
    }

    public String getArchivoleer() {
        return archivoleer;
    }

    public void setCorrecto(boolean correcto) {
        this.correcto = correcto;
    }

    public boolean isCorrecto() {
        return correcto;
    }
}
