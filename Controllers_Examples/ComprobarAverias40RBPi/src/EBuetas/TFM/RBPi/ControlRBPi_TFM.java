//<editor-fold desc="Cabecera">
/**
 * Programa principal para el control del programa 
 * AveriasI40RBPi, comprueba si el programa AveriasI40RBPi esta 
 * ejecutandose en la Raspberry Pi y de lo contrario lo lanza
 * Destinatario: Trabajo Fin de Master                              
 *               Master Investigacion en Ingenieria de Software y   
 *               Sistemas Informaticos                              
 * Proyecto: Sistema de propagacion de averias en la Industria 4.0  
 * Fecha Creacion: 13/05/2018                                       
 * Fecha Revision: 13/05/2018                                       
 * @author: Eduardo Buetas
 * @version: 1.0
 */
//</editor-fold>
package EBuetas.TFM.RBPi;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Clase principal del Control de AveriasI40RBPi, comprueba si el programa AveriasI40RBPi esta 
 * ejecutandose en el servidor y de lo contrario lo lanza
 * @author Eduardo Buetas 
 */
public class ControlRBPi_TFM {

    /**
     * Metodo inicial del programa Controlador de AveriasI40RBPi
     * Comprueba si esta ejecutandose el programa  AveriasI40RBPi y si no lo 
     * esta lo lanza. 
     * Este programa esta pensado para ejecutarse de manera ciclica en la Raspberry Pi 
     * para en el caso de que por cualquier motivo el programa  AveriasI40RBPi, este se vuelva a ejecutar
     */
    public static void main(String[] args) {
         try{
             //primero comprobamos si se esta ejecutando el propagador de averias (AveriasI40RBPi.jar)
            Boolean encontrado=false;
            Process p = Runtime.getRuntime().exec(new String[] { "bash", "-c", "ps aux | grep Averias40RBPi" });
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null)
            {
                if (line.contains("Averias40RBPi.jar"))
                {
                    encontrado=true;
                    break;
                }
            }
            //Si no esta ejecutandose lo reiniciamos
            if (!encontrado)
            {
                p = Runtime.getRuntime().exec(new String[] { "bash","-c","sudo java -jar /etc/programa/Averias40RBPi.jar" });
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
}
