/*
 * Expresion.java
 *
 * Created on 2 de diciembre de 2007, 08:38 PM
 *
 * Write by, alejandro.jimenez
 *
 */

package sia.libs.parser;

import com.eteks.jeks.JeksExpressionParser;
import com.eteks.jeks.JeksInterpreter;
import com.eteks.jeks.JeksTableModel;
import com.eteks.parser.CompiledExpression;
import sia.libs.parser.funciones.Blancos;
import sia.libs.parser.funciones.Concatenar;
import sia.libs.parser.funciones.Mayusculas;
import sia.libs.parser.funciones.Minusculas;
import sia.libs.parser.funciones.Redondea;
import sia.libs.parser.funciones.Buscar;
import sia.libs.parser.funciones.Igual;
import sia.libs.parser.funciones.Vacio;

import sia.libs.formato.Error;
import sia.libs.parser.funciones.Capitulo;
import sia.libs.parser.funciones.Comprometido;
import sia.libs.parser.funciones.Concepto;
import sia.libs.parser.funciones.Rubro;
import sia.libs.parser.funciones.SubCadena;

/**
 * @author alejandro.jimenez
 */
public class Expresion {

  private JeksTableModel tableModel; 
  private JeksExpressionParser parser;
  private CompiledExpression expression;
  private JeksInterpreter interprete;
  private String formula;
  private Object resultado;

  /**
   * Creates a new instance of Expresion
   */
  public Expresion() {
    this("");
  }
  
  public Expresion(String formula) {
    setFormula(formula);
    setResultado(Boolean.FALSE);
    setInterprete(new JeksInterpreter());
    setTableModel(new JeksTableModel(1, 1));
    setParser(new JeksExpressionParser(getTableModel()));
    getParser().addUserFunction(new Buscar());
    getParser().addUserFunction(new Redondea());
    getParser().addUserFunction(new Mayusculas());
    getParser().addUserFunction(new Minusculas());
    getParser().addUserFunction(new Concatenar());
    getParser().addUserFunction(new Blancos()); 
    getParser().addUserFunction(new Vacio()); 
    getParser().addUserFunction(new Igual()); 
    getParser().addUserFunction(new SubCadena()); 
    getParser().addUserFunction(new Capitulo()); 
    getParser().addUserFunction(new Rubro()); 
    getParser().addUserFunction(new Comprometido()); 
    getParser().addUserFunction(new Concepto()); 
  }
  
  public Object evaluar() {
    try {
      if(getFormula().length()> 0) {
        setExpression(getParser().compileExpression("=".concat(getFormula())));
        getTableModel().setValueAt(getExpression(), 0, 0);
        setExpression((CompiledExpression) getTableModel().getValueAt(0, 0));
        setResultado(getExpression().computeExpression(getInterprete()));
      }
      else
        setResultado(Boolean.TRUE);
    }
    catch (Exception e) {
      Error.mensaje(e, "SIAFM");
    } // try
    return getResultado();  
  }
  
  public String getFormula() {
    return formula;
  }

  public void setFormula(String formula) {
    if(formula!= null)
      this.formula = formula.trim();
    else
      throw new IllegalArgumentException("La formula no puede ser nula");
    setResultado("");  
  }

  public Object getResultado() {
    return resultado;
  }

  private void setResultado(Object resultado) {
    this.resultado = resultado;
  }
  
  private JeksTableModel getTableModel() {
    return tableModel;
  }

  private void setTableModel(JeksTableModel tableModel) {
    this.tableModel = tableModel;
  }

  private JeksExpressionParser getParser() {
    return parser;
  }

  private void setParser(JeksExpressionParser parser) {
    this.parser = parser;
  }

  private CompiledExpression getExpression() {
    return expression;
  }

  private void setExpression(CompiledExpression expression) {
    this.expression = expression;
  }

  private JeksInterpreter getInterprete() {
    return interprete;
  }

  private void setInterprete(JeksInterpreter interprete) {
    this.interprete = interprete;
  }
  
  public void finalize() {
    setTableModel(null); 
    setParser(null);
    setExpression(null);
    setInterprete(null);
  }

  public static void main(String[] args) {
    Expresion expresion= new Expresion("AND(1= 1, SEARCH(\"1\",\"|1|2|3|4|5|\"))");
    System.out.println(expresion.getFormula().concat("= ").concat(expresion.evaluar().toString()));
    expresion.setFormula("CONCAT(\"UNO\", \" MAS\", \" UNO\", \" IGUAL\", \" A MUCHOS\")");
    System.out.println(expresion.getFormula().concat("= ").concat(expresion.evaluar().toString()));
    expresion.setFormula("LOWER(CONCAT(\"UNO\", \" MAS\", \" UNO\", \" IGUAL\", \" A MUCHOS\"))");
    System.out.println(expresion.getFormula().concat("= ").concat(expresion.evaluar().toString()));
    expresion.setFormula("NOT(EQUALS(\"uno\",\"UNO\"))");
    System.out.println(expresion.getFormula().concat("= ").concat(expresion.evaluar().toString()));
    expresion.setFormula("EMPTY(\"\")");
    System.out.println(expresion.getFormula().concat("= ").concat(expresion.evaluar().toString()));
  }

}
