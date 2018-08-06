package sia.libs.archivo;

import cz.dhl.ftp.Ftp;
import cz.dhl.ftp.FtpContext;
import cz.dhl.ftp.FtpFile;
import cz.dhl.ftp.FtpSetting;
import cz.dhl.io.CoFile;
import cz.dhl.io.CoLoad;
import cz.dhl.io.LocalFile;

import cz.dhl.ui.CoConsole;

import java.io.File;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class Fitipi {

  protected String ftpNombre;
  protected String ruta; //ruta apartir de la raiz del ftp con la que la instancia estará trabajando
  protected String user;
  protected String pawd;
  private CoFile dir;
  private Ftp con;
  protected String[] rutas;
  protected boolean sinpwd;
  protected String protocolo;

  public Fitipi() {
  }
  
  public void conectar() {
    con = null;
    CoConsole console = null;
    try  {
      con = new Ftp(); //FtpSetting (pasive or active mode)
      con.getContext().setConsole(console); //Ocultar los mensajes que se mandan a consola
      con.connect(ftpNombre,Ftp.PORT);
      con.login(user,pawd); 
      rutas = ruta.split("/");
    } catch (Exception ex)  {
      ex.printStackTrace();
    } 
  }
  
  public void uploadFile(String rutaAbsoluta, String archivo) {
    CoFile from = new LocalFile(rutaAbsoluta,archivo);
    dir = new FtpFile(this.ruta,archivo,con);
    CoLoad.copy(dir,from);
  }
  
  public void uploadFileRealPath(HttpServletRequest request, String ruta, String archivo) throws Exception {
    ruta = getRealPath(request,ruta);
    CoFile from = new LocalFile(ruta,archivo);
    dir = new FtpFile(this.ruta,archivo,con);
    //isDirValido();
    if(!CoLoad.copy(dir,from))
      throw new Exception("No se hizo la transferencia completa del archivo");
  }
  
  public void replaceFileRealPath(HttpServletRequest request, String ruta, String archivo, String anterior)  throws Exception {
    deleteFile(anterior);
    uploadFileRealPath(request, ruta, archivo);
  }
  
  public void deleteFile(String anterior) {
    con.cd(this.ruta);
    con.rm(anterior);
  }
  
  private void deleteFile(String anterior, String ruta) {
    con.cd(ruta);
    con.rm(anterior);
  }
  
  private void deleteFileDir(String directorio) {
    dir = new FtpFile(directorio,con); 
    dir.delete();
  }
  
  public void deleteDir(String directorio) {
    deleteDirFtp(this.ruta + directorio + "/");
    deleteFile(directorio,this.ruta);
  }
  
  private void deleteDirFtp(String directorio) {
    CoFile[] coFile;
    dir = new FtpFile(directorio,con); 
    coFile = dir.listCoFiles();
    for (int i = 0; i < coFile.length; i++)  {
      if(coFile[i].isDirectory()) {
        deleteDirFtp(directorio.concat(coFile[i].getName()).concat("/"));
        deleteFileDir(directorio.concat(coFile[i].getName()).concat("/"));
      }
      else
        deleteFile(coFile[i].getName(),directorio);
      System.out.println("Eliminado ".concat(coFile[i].getName()));
    }
    con.rmdir(ruta);
  }
  
  private String getRealPath(HttpServletRequest request, String ruta) {
    ruta=ruta.replaceAll(request.getContextPath(),"");
    ruta=request.getSession().getServletContext().getRealPath(ruta);
    return ruta;
  }
  
  protected void isDirValido(String r, String[] rutas,int index) {
    if(ruta!=null && index<rutas.length) {
      r = r.concat(rutas[index]).concat("/");
      if(!con.cd(r)) {
        con.mkdir(r);
      }
      isDirValido(r,rutas,index+1);
    }
  }
  
  public void desconectar() {
    try  {
      con.disconnect();
    } catch (Exception ex)  {
      ex.printStackTrace();
    }
  } 
  
  public void desconectarYBorrarTemporales(HttpServletRequest request, String ruta) {
    try  {
      borrar(new File(getRealPath(request,ruta)));
      desconectar();
    } catch (Exception ex)  {
      ex.printStackTrace();
    } 
  }
  
  private void borrar(File lf) {
    File[] archivos = null;
    if(lf.isDirectory()) {
      archivos = lf.listFiles();
      for(File file : archivos)
        borrar(file);
    }
    System.out.println(lf.getName());
    lf.delete();
  }
  
  public String getRutaDescarga(){
    //if(pawd.equals("") || sinpwd)
    if(pawd.equals(""))
      return protocolo.concat("://").concat(ftpNombre);
    else
      return protocolo.concat("://").concat(user).concat(":").concat(pawd).concat("@").concat(ftpNombre);
  }
  
  public String getRutaDescargaCompleta(){
    return getRutaDescarga().concat(this.ruta);
  }

  public static void main(String[] args) {
  }
}
