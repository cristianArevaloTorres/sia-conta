package sia.rf.contabilidad.registroContableEvento;

import java.util.Date;

public class Lanzador implements Runnable{
    private static  Thread t;
    private static int tiempo; //indicara el tiempo en minutos de Ejecucion
    private static boolean activo; //indica si esta activo o no
    private static Date inicio;
    private static double ejecuciones;
    private static double errores;

/*
     public Connection getConnection() throws SQLException {
        String username = "rf_contabilidad_ws";
        String password = "c4nt1b3l3d1d";
        String thinConn = "jdbc:oracle:thin:@10.1.8.209:1521:SIA_PRU";
        DriverManager.registerDriver(new OracleDriver());
        Connection conn = DriverManager.getConnection(thinConn,username,password);
        conn.setAutoCommit(false);
        return conn;
    }
    
  */
    public Lanzador() {
       t = null;
       setTiempo(1);
       setActivo(false);
       inicio = null;
       ejecuciones = 0;
       errores = 0;
    }
    public void run(){
      while(this.isActivo()){
         System.out.println("Ejecutando " + Thread.currentThread().getName() + " ejecuciones:" + ejecuciones);
         try {
            Thread.sleep(tiempo*1000);
            if (this.isActivo() == false) return;
            ejecutar();
            ejecuciones ++;
         } catch (InterruptedException e) {
            System.out.println("-----Error al ejecutar el Hilo");
            errores ++;
            //setActivo(false);
         } catch(Exception e1)
         {
            System.out.println("-----Error al ejecutar el Hilo "+e1.getClass().getName()+": "+e1.getMessage());
            errores ++;
         }
        }//while
    }
    public int iniciar(){
        if (t == null) {
            t = new Thread(this, "H1");
            setActivo(true);
            ejecuciones = 0;
            errores = 0;
            setInicio(new Date());
            t.start();
            System.out.println("Iniciando");
        }    
        else {
           setActivo(true);
           ejecuciones = 0;
           errores = 0;
           setInicio(new Date());
        }
        return 0;
    }
    public int detener(){
       if (t != null) {
          setActivo(false);
          System.out.println("Detenido");
          t = null;
       }
       return 0;
    }
    

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public boolean isActivo() {
        return activo;
    }

    private void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getInicio() {
        return inicio;
    }
    public double getEjecuciones() {
        return ejecuciones;
    }

    public double getErrores() {
        return errores;
    }
    /* Puedes sobreescribir este metodo para que en el implementes lo que quieres que se ejecute
     * 
     * **/
    public synchronized void ejecutar(){
       //System.out.println("----- overriding el metodo Lanzador.ejecutar() puedes lanzar lo que quieras");
        RecuperaEvento re = new RecuperaEvento();
       // Connection con =null;
        try {
             re.procesarEvento("2ea13f9c34942a91c4"); //IP DE LA LAPTOP
           // re.procesarEvento("98cb15b20d4c8b2ca0"); //IP DEL UASIPRO5
            //re.imprime();
           //  con =   getConnection();
           // bcCheque cheque = new bcCheque();
           // System.out.println(cheque.select_SEQ_rf_tr_cheques(con));
            
        } catch (Exception e) {
            // TODO
            System.out.println("Error en el demonio "+ e.getMessage());
        }finally{
            re = null;
            // con=null;
        } 
    }
    public static void main(String[] args) {
        Lanzador pruebaHilo = new Lanzador();
        if (pruebaHilo.iniciar() == 0) {
           System.out.println("Inicio Correctamente");
        } else {System.out.println("Error al iniciar");}
        
        
    }

    
}
