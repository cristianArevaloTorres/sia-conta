package sia.rf.tesoreria.catalogos.tiposReporte;

import java.util.ArrayList;
import java.util.List;

public class ListaTiposReporte {
  
  private List<TipoReporte> tipoReporte;
  
  public ListaTiposReporte() {
    tipoReporte = new ArrayList();
  }
  
  public void iniciar() {
    tipoReporte.clear();
    tipoReporte.add(new TipoReporte("Movimientos de las cuentas (detalle)",6));                  // 0
    tipoReporte.add(new TipoReporte("Movimientos de las cuentas (totales por dia)",8));          // 1
    tipoReporte.add(new TipoReporte("Saldos diarios",7));                                        // 2
    tipoReporte.add(new TipoReporte("Transaccionalidad de las cuentas",13));                     // 3  
    tipoReporte.add(new TipoReporte("Movimientos cuentas propias",15));                          // 4
    tipoReporte.add(new TipoReporte("Comisiones cobradas por el banco (detalle)",14));           // 5
    tipoReporte.add(new TipoReporte("Comisiones cobradas por el banco (total)",12));             // 6
    tipoReporte.add(new TipoReporte("Comisiones indebidas de cheques de caja",4));               // 7
    tipoReporte.add(new TipoReporte("Reverso de comisiones cobradas por el banco",10));          // 8
    tipoReporte.add(new TipoReporte("Total reverso de comisiones cobradas por el banco",11));    // 9
    tipoReporte.add(new TipoReporte("Cheques pagados de GC con monto mayor a $5,000.00",9));     // 10
    tipoReporte.add(new TipoReporte("Referencias identificadas como reintegros",16));            // 11
    tipoReporte.add(new TipoReporte("Consolidacion de saldos bancarios",17));                    // 12
    tipoReporte.add(new TipoReporte("Rendimientos global",2));                                   // 13
    tipoReporte.add(new TipoReporte("Rendimientos detalle",3));                                  // 14
    tipoReporte.add(new TipoReporte("Cheques depositados salvo buen cobro",1));                  // 15
    tipoReporte.add(new TipoReporte("Terminal punto de venta",5));                               // 16
  }
  
  
     public void removerMultiva(){
       tipoReporte.remove(16);
       tipoReporte.remove(15);
       tipoReporte.remove(12);
       tipoReporte.remove(11);
       tipoReporte.remove(10);
       tipoReporte.remove(9);
       tipoReporte.remove(8);
       tipoReporte.remove(7);
       tipoReporte.remove(6);
       tipoReporte.remove(5);
       tipoReporte.remove(4);
       tipoReporte.remove(3);
       tipoReporte.remove(1);
     }
  
    public void removerBajio(){
        tipoReporte.remove(16);
        tipoReporte.remove(15);
        tipoReporte.remove(12);
        tipoReporte.remove(11);
        tipoReporte.remove(10);
        tipoReporte.remove(9);
        tipoReporte.remove(8);
        tipoReporte.remove(7);
        tipoReporte.remove(6);
        tipoReporte.remove(5); 
        tipoReporte.remove(4);
        tipoReporte.remove(3);
        tipoReporte.remove(1);
    }  
     
  
  public void removerSantander(){
      tipoReporte.remove(16);
      tipoReporte.remove(15);
      tipoReporte.remove(12);
      tipoReporte.remove(11);
      tipoReporte.remove(10);
      tipoReporte.remove(9);
      tipoReporte.remove(8);
      tipoReporte.remove(7);
      tipoReporte.remove(6);
      tipoReporte.remove(5);
      tipoReporte.remove(4);
      tipoReporte.remove(3);
      tipoReporte.remove(1);
  }
  
  public void remover() {
    tipoReporte.remove(16);
    tipoReporte.remove(14);
    tipoReporte.remove(13);
    tipoReporte.remove(12);
  }

  // Produccion activar 1, 3-11, 15, 
  public void removerBMX(){
    tipoReporte.remove(15);
    tipoReporte.remove(11);
    tipoReporte.remove(10);
    tipoReporte.remove(9);
    tipoReporte.remove(8);
    tipoReporte.remove(7);
    tipoReporte.remove(6);
    tipoReporte.remove(5);
    tipoReporte.remove(4);
    tipoReporte.remove(3);
    tipoReporte.remove(1);
    tipoReporte.add(new TipoReporte("Comisiones por uso de TPV",18));
  }

  public void removerBBVA(){
    tipoReporte.remove(16);
    tipoReporte.remove(15);
    tipoReporte.remove(12);
    tipoReporte.remove(11);
    tipoReporte.remove(10);
    tipoReporte.remove(9);
    tipoReporte.remove(8);
    tipoReporte.remove(7);
    tipoReporte.remove(6);
    tipoReporte.remove(5);
    tipoReporte.remove(4);
    tipoReporte.remove(3);
    tipoReporte.remove(1);
  }
  
    public void removerContraloria(){
      tipoReporte.remove(16);
      tipoReporte.remove(15);
      tipoReporte.remove(14);
      tipoReporte.remove(13);
      tipoReporte.remove(12);
      tipoReporte.remove(11);
      tipoReporte.remove(10);
      tipoReporte.remove(9);
      tipoReporte.remove(8);
      tipoReporte.remove(7);
      tipoReporte.remove(6);
      tipoReporte.remove(5);
      tipoReporte.remove(4);
      tipoReporte.remove(3);
      tipoReporte.remove(2);
      tipoReporte.remove(1);
    }


  public void setTipoReporte(List<TipoReporte> tipoReporte) {
    this.tipoReporte = tipoReporte;
  }

  public List<TipoReporte> getTipoReporte() {
    return tipoReporte;
  }
}
