//<editor-fold desc="Cabecera">
/**
 * Programa principal para la gestion del sistema      
 *       de propagacion de averias en la Industria 4.0. El sistema de    
 *       basa en la comunicacion a traves del protocolo MQTT.            
 *       Como sistema SGBD para el tratamiento de las averias se utiliza 
 *       Postgres SQL  
 * Destinatario: Trabajo Fin de Master                              
 *               Master Investigacion en Ingenieria de Software y   
 *               Sistemas Informaticos                              
 * Proyecto: Sistema de propagacion de averias en la Industria 4.0  
 * Fecha Creacion: 27/04/2018                                       
 * Fecha Revision: 27/04/2018                                       
 * @author: Eduardo Buetas
 * @version: 1.0
 */
//</editor-fold>
package EBuetas.TFM;
import EBuetas.TFM.MQTT.MQTTSus_Pub;
/**
 * Clase principal del programa Propagador de averias 
 */
public class PropagadorAverias {

    /**
     * Metodo inicial del programa, activa el suscriptor y se queda en un bucle
     * infinito comprobando cada cierto tiempo si la conexion de los dos 
     * suscriptores MQTT, si hay algun problema de conexion se vuelve a reiniciar la 
     * conexion
     */
    public static void main(String[] args){
        MQTTSus_Pub suscriptor=null;
        //creamos el objeto de conexion y realizamos las suscripciones
        suscriptor= new MQTTSus_Pub("localhost",1883, "eduardo", "asdf1234");
        suscriptor.ActivarSuscriptor();
        suscriptor.ActivarPublicador();
        EscribeLog.EscribeError("Servicio de propagacion de averias activado", 0);
        //en este bucle cada cierto tiempo comprobamos si la conexion es correcta 
        //si no lo es volvemos a intentar la conexion
        while (true) {
            try{
                Thread.sleep(1000);
                if (!suscriptor.conexionOK){
                    suscriptor.ActivarSuscriptor();
                    suscriptor.ActivarPublicador();
                    Thread.sleep(10000);                    
                }
            }catch(Exception ex){
                EscribeLog.EscribeError("fallo en el bucle de espera", 2);
                break;
            }
            
        }         
        EscribeLog.EscribeError("Servicio de propagacion de averias detenido por fallo de conexion con broker MQTT", 1);       
    }
    
}
