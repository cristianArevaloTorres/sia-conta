package sia.db.sql;

import java.io.IOException;

import java.sql.SQLException;

import javax.servlet.jsp.JspWriter;

public class SenCRSR extends SentenciasCRS {

  public int maxRegistros = 200;

  public SenCRSR() throws Exception, SQLException {
  }

  public void limite(JspWriter out) throws IOException {
    if(size()>=maxRegistros) {
      out.println("Resultado muy grande: <b>Encontrados:</b> <font color='red'>"+size()+"</font>, <b>Desplegados:</b> <font color='red'>"+maxRegistros+"</font>. Usar criterios de búsqueda para acortar su resultado");
    }
  }

  public boolean next() throws SQLException {
    return next(getRow()+1);
  }
  
  public boolean next(int row) throws SQLException {
    if(row > maxRegistros) {
      return false;
    } else {
      return super.next();
    }
  }
}
