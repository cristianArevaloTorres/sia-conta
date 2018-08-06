/*
 * Clase: FileFiltro.java
 *
 * Creado: 4 de mayo de 2007, 08:20 PM
 *
 * Write by: alejandro.jimenez
 */

package sia.libs.archivo;

import java.io.File;
import java.io.FileFilter;

public class FileFiltro implements FileFilter {
  
   private String patron= null;

   public FileFiltro() {
     this("xSia");
   }; // FileFiltro
   
   public FileFiltro(String patron) {
     super();
     setPatron(patron); 
   }; // FileFiltro
   
   public boolean accept(File f) {
     if (f.isDirectory()) 
       return false;
     if(getPatron()== null)
       setPatron("xSia");
     return f.getName().startsWith("xSia") && f.getName().compareTo(getPatron())< 0;
   }; // accept

  public String getPatron() {
    return patron;
  }

  public void setPatron(String patron) {
    this.patron = patron;
  }
  
}
