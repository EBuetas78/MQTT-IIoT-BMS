//<editor-fold desc="Cabecera">
/**
 * Programa principal para el control del programa 
 * Propagador de Averias, comprueba si el programa Porpagador de Averias esta 
 * ejecutandose en el servidor y de lo contrario lo lanza
 * Destinatario: Trabajo Fin de Master                              
 *               Master Investigacion en Ingenieria de Software y   
 *               Sistemas Informaticos                              
 * Proyecto: Sistema de propagacion de averias en la Industria 4.0  
 * Fecha Creacion: 28/04/2018                                       
 * Fecha Revision: 28/04/2018                                       
 * @author: Eduardo Buetas
 * @version: 1.0
 */
//</editor-fold>
package EBuetas.TFM;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Clase principal del Control del Propagador de averias,comprueba si el programa Porpagador de Averias esta 
 * ejecutandose en el servidor y de lo contrario lo lanza
 * @author Eduardo Buetas 
 */
public class ControlServerTFM {

    /**
     * Metodo inicial del programa Controlador del Propagador de Averias
     * Comprueba si esta ejecutandose el programa Propagador de Averias y si no lo 
     * esta lo lanza. 
     * Este programa esta pensado para ejecutarse de manera ciclica en el servidor 
     * para en el caso de que por cualquier motivo el programa Propagador de averias se 
     * ha parado, este se vuelva a ejecutar
     */
    public static void main(String[] args) {
         try{
             //primero comprobamos si se esta ejecutando el propagador de averias (PropagadorAverias.jar)
            Boolean encontrado=false;
            Process p = Runtime.getRuntime().exec(new String[] { "bash", "-c", "ps aux | grep PropagadorAverias" });
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null)
            {
                if (line.contains("PropagadorAverias.jar"))
                {
                    encontrado=true;
                    break;
                }
            }
            //Si no esta ejecutandose lo reiniciamos
            if (!encontrado)
            {
                p = Runtime.getRuntime().exec(new String[] { "bash","-c","sudo java -jar /etc/programa/PropagadorAverias.jar" });
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
    
}
