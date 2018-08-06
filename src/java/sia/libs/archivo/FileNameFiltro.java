/*
 * Clase: FileNameFiltro.java
 *
 * Creado: 4 de mayo de 2007, 08:24 PM
 *
 * Write by: alejandro.jimenez
 */

/*
 * Clase: FileFiltro.java
 *
 * Creado: 4 de mayo de 2007, 08:20 PM
 *
 * Write by: alejandro.jimenez
 */
package sia.libs.archivo;

import java.io.File;
import java.io.FilenameFilter;

public class FileNameFiltro implements FilenameFilter {
  
   private String patron= null;
   private boolean igual= false;

   public FileNameFiltro() {
     this("xSia");
   }; // FileNameFiltro
   
   public FileNameFiltro(String patron) {
     this(patron, false);
   }; // FileNameFiltro

   public FileNameFiltro(String patron, boolean igual) {
     super();
     setPatron(patron); 
     setIgual(igual);
   }; // FileNameFiltro
   
   
   public boolean accept(File dir, String name) {
     boolean comodin= patron.indexOf("?")>= 0;
     String empieza = null;
     String termina = null;
     if(comodin) {
       if(patron.indexOf("?")> 0)
         empieza= patron.substring(0, patron.indexOf("?"));
       if(patron.indexOf("?")< patron.length()- 1)
         termina= patron.substring(patron.indexOf("?")+ 1, patron.length());
       comodin= (empieza!= null? name.startsWith(empieza): true) && (termina!= null? name.endsWith(termina): true);
     }; // if comodin    
     if(patron== null)
       patron= "xSia";
     return igual? name.equals(patron) || comodin: name.startsWith("xSia") && name.compareTo(patron)< 0;
   }; // accept

  public String getPatron() {
    return patron;
  }

  public void setPatron(String patron) {
    this.patron = patron;
  }

  public boolean isIgual() {
    return igual;
  }

  public void setIgual(boolean igual) {
    this.igual = igual;
  }
  
}
