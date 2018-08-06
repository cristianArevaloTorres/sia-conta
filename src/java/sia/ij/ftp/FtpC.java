package sia.ij.ftp;

import sia.libs.archivo.Fitipi;
public class FtpC extends Fitipi {

public FtpC(String ruta) {
 this.ruta      = ruta;
 this.ftpNombre = "uftpint.senado.gob.mx";
 this.user      = "siirw";
 this.pawd      = "S1$Full";
 this.sinpwd    = true;
 this.protocolo = "ftp";
}
  
public FtpC() { this(null); }

}