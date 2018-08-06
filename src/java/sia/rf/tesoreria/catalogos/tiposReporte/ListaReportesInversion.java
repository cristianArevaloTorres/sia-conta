package sia.rf.tesoreria.catalogos.tiposReporte;

import java.util.ArrayList;
import java.util.List;

import sia.beans.seguridad.Autentifica;

public class ListaReportesInversion {

    private List<TipoReporte> tipoReporte;
      
    public ListaReportesInversion() {
        tipoReporte = new ArrayList();
    }
    
    public void iniciar(Autentifica aut) {
      tipoReporte.clear();
      if ( aut.getLogin().equals("minerva.vidales") || aut.getLogin().equals("luzmaria.gonzalez") ||
           aut.getLogin().equals("gerardo.arenas") || aut.getLogin().equals("patricia.casas"))    {
            tipoReporte.add(new TipoReporte("Rendimientos por Institución Financiera","Rendimientos",6));
            tipoReporte.add(new TipoReporte("Composición de la cartera por institucion financiera al cierre de mes","CarteraCierreMes",12));
      } else {
            tipoReporte.add(new TipoReporte("Cotización de tasas de rendimientos por Institución Financiera","CotTasas",1));
            tipoReporte.add(new TipoReporte("Disponibilidades financieras","dispNLinea",3));
            tipoReporte.add(new TipoReporte("Rendimientos por Institución Financiera","Rendimientos",6));
            tipoReporte.add(new TipoReporte("Resumen de tasas de rendimiento por Plazos","Ren1Dia",7));
            tipoReporte.add(new TipoReporte("Resumen de tasas de rendimiento por Institución Financiera","RenInstFinanciera",8));
            tipoReporte.add(new TipoReporte("Inversión de las Disponibilidades por Plazo","Inversion",9));
            tipoReporte.add(new TipoReporte("Inversión de las Disponibilidades por Plazo e Institución Financiera","InversionInstFinanciera",10));
            tipoReporte.add(new TipoReporte("Composición de la cartera por Institución Financiera y fecha de inversión","CarteraFecha",11));
            tipoReporte.add(new TipoReporte("Composición de la cartera por institución financiera al cierre de mes","CarteraCierreMes",12));
            tipoReporte.add(new TipoReporte("Comportamiento de la reserva expresado en mdp","Reserva",13));
            tipoReporte.add(new TipoReporte("Composición de la cartera al cierre de mes","CarteraMes",14));
            tipoReporte.add(new TipoReporte("Inversión por tipo de instrumento","InvTipoInstrumento",15));
            tipoReporte.add(new TipoReporte("Autorización de Inversión de Disponibilidades Financieras","ResumenInv",16));
            tipoReporte.add(new TipoReporte("Detalle de operaciones de Inversión por Institución Financiera","Detalle",17));
            tipoReporte.add(new TipoReporte("Inversión por tipo de instrumento global","InvTipoInstrumentoGlobal",18));
            tipoReporte.add(new TipoReporte("Consolidado de transferencias por un periodo determinado","globalTransf",19));
            tipoReporte.add(new TipoReporte("Total de rendimimientos en un periodo","globalRendimientos",20));
            tipoReporte.add(new TipoReporte("Tasas de rendimiento pactadas en la inversión de Disponibilidad Financiera","tasasPactadas",21));
      }
    }

    public void setTipoReporte(List<TipoReporte> tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public List<TipoReporte> getTipoReporte() {
        return tipoReporte;
    }
}
