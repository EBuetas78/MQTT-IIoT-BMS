//<editor-fold desc="Cabecera">
/**
 * Clase para escribir los logs del programa
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
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedWriter;
/**
 * Clase para escribir los logs del programa
 * @author Eduardo Buetas
 */
public class EscribeLog {
    /**
     * Metodo estatico para escribir el log del programa, a cada linea se le anade
     * la fecha del log para su posterior analisis
     * @param Descripcion valor String con la descripcion del error 
     * @param Numero_Error valor Int con el numero de error, ayuda a buscar el error en el programa
     */
    public static void EscribeError(String Descripcion, int Numero_Error)
    {
        SimpleDateFormat formato=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");        
        Date fecha=new Date();
	try {
            String archivo ="/var/log/PropagacionAverias.log";
            File f=new File(archivo);
            if (!f.exists())
            {
                BufferedWriter bw=null;
                bw = new BufferedWriter(new FileWriter(archivo));
                bw.write(Descripcion+ "--Nº Error:"+String.valueOf(Numero_Error) + "--"+"Hora:" + formato.format(fecha)+"\n");
                bw.close();
            }else{
                FileWriter fwriter= new FileWriter(archivo,true);
                fwriter.write(Descripcion+ "--Nº Error:"+String.valueOf(Numero_Error) + "--"+"Hora:" + formato.format(fecha)+"\n");
                fwriter.close();
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }   
}
