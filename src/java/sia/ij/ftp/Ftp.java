package sia.ij.ftp;

import sia.libs.archivo.Fitipi;
public class Ftp extends Fitipi {
  
public Ftp(String ruta) {
 this.ruta = ruta;  
 this.ftpNombre  = "uftpint.senado.gob.mx";
 this.user       = "siirw";
 this.pawd       = "S1$Full";
 this.sinpwd     = true;
 this.protocolo  = "ftp";
 conectar();
 isDirValido("",rutas,0);
 desconectar();
}
  
public Ftp() { this(null); }

}
