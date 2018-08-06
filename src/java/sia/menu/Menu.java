package sia.menu;

import javax.servlet.http.HttpSession;
import sia.beans.seguridad.Autentifica;
import sia.libs.recurso.Propiedades;

public class Menu {

    public static enum Tipo{ARBOL,OUTLOOK};
    private Tipo tipoMenu;
    private String nombre;
    private String pagina;
    private String navegacion;
    private boolean ocultarSeguimiento;
    private static final String PAGINA = "sistema.sia.menu.";
    private static final String NAVEGACION = "sistema.sia.navegacion.";
    private static final String SISTEMA = "sistema.sia.produccion";
    private static final String PERFIL_PRU = "sistema.sia.perfil.outlook.pru";
    private static final String PERFIL_PRO = "sistema.sia.perfil.outlook.pro";
    
    public Menu(HttpSession session) {
        Autentifica aut = (Autentifica)session.getAttribute("Autentifica");
        String perfiles;
        if(Propiedades.getInstance().getPropiedad(SISTEMA).equals("1")) {
            perfiles = Propiedades.getInstance().getPropiedad(PERFIL_PRO);
        }
        else {
            perfiles = Propiedades.getInstance().getPropiedad(PERFIL_PRU);
        }
        String[] perfilesCad = perfiles.split(",");
        boolean perfil = false;
        for (int i = 0; i < perfilesCad.length; i++) {  
            if (aut.isPerfil(perfilesCad[i]) ){
                perfil = true;        
                break;
            }
        }
        if (perfil) {
            setTipoMenu(Tipo.OUTLOOK);
        }
        else {
            setTipoMenu(Tipo.ARBOL);
        }
    }

    public final void setTipoMenu(Tipo tipoMenu){
        this.tipoMenu = tipoMenu;
        switch (tipoMenu)  {
            case OUTLOOK: {
                    setNombre("outlook");
                    setOcultarSeguimiento(false);                    
                }
                break;
            case ARBOL: 
            default: {
                    setNombre("arbol");
                    setOcultarSeguimiento(true);
                }
                break;
        }        
    }
    
    private void setNombre(String nombre) {
        this.nombre = nombre;
        setPagina(Propiedades.getInstance().getPropiedad(PAGINA.concat(nombre)));
        setNavegacion(Propiedades.getInstance().getPropiedad(NAVEGACION.concat(nombre)));
    }

    public Tipo getTipoMenu() {
        return tipoMenu;
    }

    private void setNavegacion(String navegacion) {
        this.navegacion = navegacion;
    }

    public String getNavegacion() {
        return navegacion;
    }

    private void setOcultarSeguimiento(boolean ocultarSeguimiento) {
        this.ocultarSeguimiento = ocultarSeguimiento;
    }

    public boolean isOcultarSeguimiento() {
        return ocultarSeguimiento;
    }
    
    private void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public String getPagina() {
        return pagina;
    }
    
}
