package sia.libs.formato;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Variables {

  private String codigos;
  private char tokens;

  public Variables(String codigos) {
    setTokens('|');
    if(codigos == null) {
          codigos = "";
      }
    if(codigos.indexOf("=")>= 0 || codigos.indexOf("|")>= 0) {
          init(codigos);
      }
    else {
          parse(codigos);
      }
  }

  public Variables(String codigos, char tokens) {
    setTokens(tokens);
    setCodigos(codigos);
  }

  public Variables(Object ... codigos) {
    this('|', codigos);
  }

  public Variables(char token, Object ... codigos) {
    setTokens(token);
    parse(codigos);
  }

  private void parse(Object ... codigos) {
    StringBuffer cadena= new StringBuffer(getTokens());
    for(int x= 0; x< codigos.length; x++) {
      cadena.append(x);
      cadena.append("=");
      cadena.append(codigos[x].toString());
      cadena.append(getTokens());
    } // for   
    init(cadena.toString());
  }
  
  private void init(String cadena) {
    setCodigos(cadena);
  }
  
  public Map getMap() {
    Map variables = new HashMap();
    String token  = null;
    String nomVar = null;
    String valVar = null;
    StringTokenizer st= null;
    switchEquals();
    StringTokenizer tokens = new StringTokenizer(getCodigos(), String.valueOf(getTokens()), false);
    while (tokens.hasMoreTokens()) {
      token = tokens.nextToken();
      if (token.trim().length()>0){
        st = new StringTokenizer(token, "=", false);
        if (st.countTokens() >= 1) {
          nomVar = st.nextToken();
          valVar = st.nextToken();
          variables.put(nomVar, valVar.replace('~', '='));
        } // if
      } // if
    }
    return variables;
  }

  public void setCodigos(String codigos) {
    this.codigos = codigos;
  }

  public String getCodigos() {
    return codigos;
  }

  public void setTokens(char tokens) {
    this.tokens = tokens;
  }

  public char getTokens() {
    return tokens;
  }
  
  private void switchEquals() {
    StringBuffer    sb= new StringBuffer("|");
    StringTokenizer st= new StringTokenizer(getCodigos(), "|", false);
    while(st.hasMoreTokens()) {
      String token= st.nextToken();
      if(token.indexOf("=")>= 0) {
        sb.append(token.substring(0, token.indexOf("=")+ 1));
        token= token.substring(token.indexOf("=")+ 1);
        if(token.indexOf("=")>= 0)
          token= token.replace('=', '~');
      } // if
      sb.append(token);
      sb.append("|");
    } // while
    setCodigos(sb.toString());
  }
  
  public static void main(String ... args) {
    Variables variables= new Variables("uno=12|dos====");
    System.out.println(variables.getMap());
  }
  
}