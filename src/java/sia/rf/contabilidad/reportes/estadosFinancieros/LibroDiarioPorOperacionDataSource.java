package sia.rf.contabilidad.reportes.estadosFinancieros;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class LibroDiarioPorOperacionDataSource implements JRDataSource {

  private final int LIMITE_CARACTERS_DOCUMENTO  = 15;
  private final int LIMITE_CARACTERS_DESCRIPCION= 23;
  private final int LIMITE_CARACTERS_FUENTE     = 28;
  private final String SEPARADOR_PALABRAS       = " ,.;";
		
  private Timestamp FECHA_AFECTACION;
  private String NUM_EVENTO;
  private String NUM_ASIENTO;
  private String CUENTA_MAYOR;
  private String PRESUPUESTAL;
  private String DESCRIPCION_CUENTA;
  private String DOCUMENTO;
  private String DESCRIPCION;
  private Double DEBE;
  private Double HABER;
  private String CORTE_POLIZA;
	
	private Connection connection;
	private Statement statement;
	private ResultSet resultset;
	private String sql;
	
	/**
	 *
	 */
	private int indexDocumento;
        private String XDOCUMENTO;
	private int indexDescripcion;
        private String XDESCRIPCION;
	private int indexFuente;
        private String XFUENTE;
	private boolean mismaPoliza;
	/**
	 *
	 */
	private int lineasFuente;
	private int lineasDocumento;
	private int lineasDescripcion;
	private int count;
	private int acumulado;
        private boolean pintarUnaSoloVez;
	/**
	 *
	 */
	public LibroDiarioPorOperacionDataSource(String sql, Connection connection) throws SQLException {
		this.sql               = sql;
		this.connection        = connection;
                this.lineasFuente      = -1;
                this.lineasDocumento   = 0;
                this.lineasDescripcion = 0;
                this.count             = 0;
		this.acumulado         = 0;
                this.indexDocumento    = 0;
                this.indexDescripcion  = 0;
                this.indexFuente       = 0;
		this.mismaPoliza       = true;
		this.CORTE_POLIZA      = "";
		this.pintarUnaSoloVez  = true;
	}

	private void load() throws SQLException {
		this.CUENTA_MAYOR      = this.resultset.getString("CUENTA_MAYOR");
		this.PRESUPUESTAL      = this.resultset.getString("PRESUPUESTAL");
		this.DEBE              = this.resultset.getDouble("DEBE");
		this.HABER             = this.resultset.getDouble("HABER");
		this.count             = 0;
		this.indexFuente       = 0;
		this.XFUENTE           = this.resultset.getString("DESCRIPCION_CUENTA");
		this.lineasFuente      = this.XFUENTE     != null? ((int)(this.XFUENTE.length()/ LIMITE_CARACTERS_FUENTE))+ 1: 0;
		if(this.CORTE_POLIZA .length()== 0 || pintarUnaSoloVez) {
			this.acumulado           = 0;
			this.FECHA_AFECTACION  = this.resultset.getTimestamp("FECHA_AFECTACION");
			this.NUM_EVENTO        = this.resultset.getString("NUM_EVENTO");
			this.NUM_ASIENTO       = this.resultset.getString("NUM_ASIENTO");
			this.indexDocumento    = 0;
			this.XDOCUMENTO        = this.resultset.getString("DOCUMENTO");
			this.lineasDocumento   = this.XDOCUMENTO  != null? ((int)(this.XDOCUMENTO.length()/ LIMITE_CARACTERS_DOCUMENTO))+ 1: 0;
			this.indexDescripcion  = 0;
			this.XDESCRIPCION      = this.resultset.getString("DESCRIPCION");
			this.lineasDescripcion = this.XDESCRIPCION!= null? ((int)(this.XDESCRIPCION.length()/ LIMITE_CARACTERS_DESCRIPCION))+ 6: 0;
		} // if	
		this.DOCUMENTO           = toReCortarDocumento(this.XDOCUMENTO);
		this.DESCRIPCION         = toReCortarDescripcion(this.XDESCRIPCION);
		this.DESCRIPCION_CUENTA  = toReCortarFuente(this.XFUENTE);
		this.CORTE_POLIZA        = this.resultset.getString("NUM_ASIENTO");
		pintarUnaSoloVez         = false;
}
	
	private boolean cargar() throws SQLException {
		boolean result= this.resultset.next();
		if(result) {
		  mismaPoliza= this.CORTE_POLIZA .length()== 0 || this.CORTE_POLIZA.equals(this.resultset.getString("NUM_ASIENTO"));
		  if(mismaPoliza) {
    		    //System.out.println("Registro: "+ this.resultset.getRow());
                    load();
		  } // if	
		  else {
		    pintarUnaSoloVez    = true;
		    this.CORTE_POLIZA   = this.resultset.getString("NUM_ASIENTO");
		    this.resultset.previous();
		    this.count          = this.acumulado;
		    this.lineasFuente   = this.lineasDescripcion;
		    siguiente();
		  } // else
		} // if
		else
		  if(this.acumulado<= this.lineasDescripcion) {
		    this.count          = this.acumulado;
		    this.lineasFuente   = this.lineasDescripcion;
		    siguiente();
		  } // if
		  else
		    this.lineasFuente   = -2;
		return this.lineasFuente>= 0;
	}

	private void siguiente() {
        
		this.FECHA_AFECTACION  = null;
		this.NUM_EVENTO        = "";
		this.NUM_ASIENTO       = "";
		this.CUENTA_MAYOR      = "";
		this.DEBE              = 0D;
		this.HABER             = 0D;
                this.DESCRIPCION_CUENTA= toReCortarFuente(this.XFUENTE);
		this.DOCUMENTO         = toReCortarDocumento(this.XDOCUMENTO);
		this.DESCRIPCION       = toReCortarDescripcion(this.XDESCRIPCION);
	}
	
	/**
	 *
	 */
	//@Override
	public boolean next() throws JRException {
	  boolean result= true;
		try {
			try {
				if(this.lineasFuente== -1) {
				  this.statement= this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					this.resultset= this.statement.executeQuery(this.sql);
				} // if
				if(this.lineasFuente== -1 || this.count> this.lineasFuente) {
					result= cargar();
				}	 // if
				else {
				  siguiente(); 
					if(this.DOCUMENTO== null && this.DESCRIPCION_CUENTA== null && this.DESCRIPCION== null) {
						this.count    = 0;
						this.acumulado= this.lineasDescripcion+ 1;
  					result= cargar();
					} // if
				} // else	
				if(!result) {
					if(this.resultset!= null)
						this.resultset.close();
					if(this.statement!= null)
						this.statement.close();
				} // else
			} // try
			catch(Exception e) {
				e.printStackTrace();
				if(this.resultset!= null)
					this.resultset.close();
				if(this.statement!= null)
					this.statement.close();
  			result= false;
			} // catch	
		} // try
		catch(Exception e) {
			result= false;
		} // catch
		this.count++;
		this.acumulado++;
    return result;
	}

	/**
	 *
	 */
	//@Override
	public Object getFieldValue(JRField field) throws JRException {
		Object value    = null;
		String fieldName= field.getName();
		if ("FECHA_AFECTACION".equals(fieldName)) 
			value= FECHA_AFECTACION;
		else
			if ("NUM_EVENTO".equals(fieldName)) 
				value= NUM_EVENTO;
			else
				if ("NUM_ASIENTO".equals(fieldName)) 
					value= NUM_ASIENTO;
				else
					if ("DOCUMENTO".equals(fieldName)) 
						value= DOCUMENTO;
					else
						if ("CUENTA_MAYOR".equals(fieldName)) 
							value= CUENTA_MAYOR;
						else
							if ("PRESUPUESTAL".equals(fieldName)) 
								value= PRESUPUESTAL;
							else
								if ("DESCRIPCION_CUENTA".equals(fieldName)) 
									value= DESCRIPCION_CUENTA;
								else
									if ("DESCRIPCION".equals(fieldName)) 
										value= DESCRIPCION;
									else
										if ("DEBE".equals(fieldName)) 
											value= DEBE;
										else
											if ("HABER".equals(fieldName)) 
												value= HABER;
		return value;
	}
	
	private String toReCortarDocumento(String frase) {
    String regresar= frase;
		if (regresar!= null && regresar.length()> 0 && this.indexDocumento< regresar.length()) {
			int length= this.indexDocumento+ LIMITE_CARACTERS_DOCUMENTO;
			// verificar si la cadea es mayor al numoer de caracteres permitidos por renglon
			if ((this.indexDocumento+ LIMITE_CARACTERS_DOCUMENTO)< regresar.length()) {
				while (length>= 0 && SEPARADOR_PALABRAS.indexOf(regresar.charAt(length))< 0) {
					length--;
				} // while
				// por si no existe ningun caractares que determine si termina una palabra
				if (length<= this.indexDocumento) 
					length= frase.length();
			} // if
			else 
				length= frase.length();
			// recortar la frase por palabras completas segun el separador
			regresar=regresar.substring(this.indexDocumento, length);
			// ajustar el indice para comenzar en le siguiente renglon
			this.indexDocumento=length;
		} //if
		else 
			regresar= null;
		return regresar;
	}

	private String toReCortarDescripcion(String frase) {
		String regresar= frase;
		if (regresar!= null && regresar.length()> 0 && this.indexDescripcion< regresar.length()) {
			int length= this.indexDescripcion+ LIMITE_CARACTERS_DESCRIPCION;
			// verificar si la cadea es mayor al numoer de caracteres permitidos por renglon
			if ((this.indexDescripcion+ LIMITE_CARACTERS_DESCRIPCION)< regresar.length()) {
				while (length>= 0 && SEPARADOR_PALABRAS.indexOf(regresar.charAt(length))< 0) {
					length--;
				} // while
				// por si no existe ningun caractares que determine si termina una palabra
				if (length<= this.indexDescripcion) 
					length= frase.length();
			} // if
			else 
				length= frase.length();
			// recortar la frase por palabras completas segun el separador
			regresar=regresar.substring(this.indexDescripcion, length);
			// ajustar el indice para comenzar en le siguiente renglon
			this.indexDescripcion=length;
		} //if
		else 
			regresar= null;
		return regresar;
	}

	private String toReCortarFuente(String frase) {
		String regresar= frase;
		if (regresar!= null && regresar.length()> 0 && this.indexFuente< regresar.length()) {
			int length= this.indexFuente+ LIMITE_CARACTERS_FUENTE;
			// verificar si la cadea es mayor al numoer de caracteres permitidos por renglon
			if ((this.indexFuente+ LIMITE_CARACTERS_FUENTE)< regresar.length()) {
				while (length>= 0 && SEPARADOR_PALABRAS.indexOf(regresar.charAt(length))< 0) {
					length--;
				} // while
				// por si no existe ningun caractares que determine si termina una palabra
				if (length<= this.indexFuente) 
					length= frase.length();
			} // if
			else 
				length= frase.length();
			// recortar la frase por palabras completas segun el separador
			regresar=regresar.substring(this.indexFuente, length);
			// ajustar el indice para comenzar en le siguiente renglon
			this.indexFuente=length;
		} //if
		else 
			regresar= null;
		return regresar;
	}
	
}
