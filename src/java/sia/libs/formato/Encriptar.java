/*
 * Clase: Encriptar.java
 *
 * Creado: 21 de mayo de 2007, 12:16 AM
 *
 * Write by: alejandro.jimenez
 */
package sia.libs.formato;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class Encriptar {

  public static String SIA_CLAVE= "SIANOMINA2005";
  private int KeyLen;
  private int KeyPos;
  private int Offset;
  private int SrcPos;
  private int SrcAsc;
  private int TmpSrcAsc;
  private int Range;
  private BigInteger bits;
  private String dest;
  private String Pass;
  private String Llave;
  private boolean fijar;

  public Encriptar() {
    inicializa();
  };

  public Encriptar(String Llave) {
    inicializa();
    setLlave(Llave);
  };

  public Encriptar(String Pass, String Llave) {
    inicializa();
    setPass(Pass);
    setLlave(Llave);
  };
  
  private void inicializa() {
    inicializa(getLlave());
  }

  private void inicializa(String k) {
    dest= "";
    KeyPos= 0;
    SrcPos= 0;
    SrcAsc= 0;
    Range = 256;
    Llave = k;
  }

  public void setPass(String Pass) {
    this.Pass= Pass;
  }

  public String getPass() {
    return Pass;
  }

  public void setLlave(String Llave) {
    this.Llave= Llave;
  }

  public String getLlave() {
    if(Llave == null)
      Llave = SIA_CLAVE;
    return Llave;
  }

  public String encriptar() {
    return encriptar(getPass(), getLlave());
  }

  public String encriptar(String Src) {
    return encriptar(Src, getLlave());
  }
  
  public String getEncriptado(String Src) {
    setFijar(true);
    return encriptar(Src, getLlave());
  }

  public String encriptar(String Src, String Key) {
    inicializa(Key);
    KeyLen= Key.length();
    if (isFijar())
      Offset= (Range-1);
    else 
      Offset= new Random().nextInt(Range);
    dest  = Integer.toHexString(Offset).length()==1?"0"+Integer.toHexString(Offset):Integer.toHexString(Offset);
    for (SrcPos= 0; SrcPos< Src.length(); SrcPos++) {
      SrcAsc= ((byte)(Src.charAt(SrcPos))+Offset) % (Range - 1);
      if (KeyPos < KeyLen-1)
        KeyPos++;
      else
        KeyPos= 0;
      bits= new BigInteger(String.valueOf(SrcAsc), 10);
      SrcAsc=bits.xor(new BigInteger(String.valueOf((byte)(Key.charAt(KeyPos))), 10)).intValue();
      dest+= Integer.toHexString(SrcAsc).length()==1?"0"+Integer.toHexString(SrcAsc):Integer.toHexString(SrcAsc);
      Offset= SrcAsc;
    }
    return dest;
  }

  public String desencriptar() {
    return desencriptar(encriptar(getPass()), getLlave()) ;
  }

  public String desencriptar(String Src) {
    return desencriptar(Src, getLlave()) ;
  }

  public String datosAcceso(String dato, String driver) { //wakko
    return desencriptar(dato, driver);
  }
  
  public String desencriptar(String Src, String Key) {
    inicializa(Key);
    KeyLen= Key.length();
    Offset= Integer.parseInt(Src.substring(0, 2), 16);
    SrcPos= 2;
    while (SrcPos < Src.length()) {
      SrcAsc= Integer.parseInt(Src.substring(SrcPos, SrcPos+2), 16);
      if (KeyPos < KeyLen-1)
        KeyPos++;
      else
        KeyPos= 0;
      bits= new BigInteger(String.valueOf(SrcAsc), 10);
      TmpSrcAsc=bits.xor(new BigInteger(String.valueOf((byte)(Key.charAt(KeyPos))), 10)).intValue();
      if (TmpSrcAsc > Offset)
        TmpSrcAsc= TmpSrcAsc - Offset;
      else
        TmpSrcAsc= (Range - 1) + TmpSrcAsc - Offset;
      dest+= (char)(TmpSrcAsc);
      Offset= SrcAsc;
      SrcPos+= 2;
    }
    return dest;
  }
  
  public boolean isFijar() {
    return fijar;
  }
  
  public void setFijar(boolean fijar) {
    this.fijar = fijar;
  }

  public static void main(String [] args) throws IOException, Exception {
     //Encriptar enc = new Encriptar("sistema");
      Encriptar enc = new Encriptar();
     System.out.println(enc.getEncriptado("jflores"));
     System.out.println(enc.desencriptar("ff5de646ca52a752ec","sistema"));
     
   }
    
};