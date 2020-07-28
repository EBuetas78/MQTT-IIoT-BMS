//<editor-fold desc="Cabecera">
/**
 * En un objeto de esta clase se almacenara los datos de configuracion de la aplicacion,
 * estos datos de configuracion se almacenaran como los datos de preferencias compartidas de esta
 * aplicacion, utilizando un objeto de la clase de Android SharedPreferences.
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
package ebuetas.tfm.averiasandroid;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;


/**
 * En un objeto de esta clase se almacenara los datos de configuracion de la aplicacion,
 * estos datos de configuracion se almacenaran como los datos de preferencias compartidas de esta
 * aplicacion, utilizando un objeto de la clase de Android SharedPreferences.
 */
public class Configuracion {
    /**
     * Creamos las variables privadas para contener los datos de la configuracion
     */
    private final String tag="Configuracion";
    private String ip_servidor="";
    private int puerto=1883;
    private String user_servidor="";
    private String pass_servidor="";
    private String nombre_equipo="";
    private String topico_suscripcion="";
    private boolean ver_oks=false;

    /**
     * Funcion para la devolucion de la variable puerto
     * @return int variable puerto del objeto
     */
    public int getPuerto() {   return puerto;  }

    /**
     * Funcion para la devolucion de la variable ip_servidor
     * @return String variable ip_servidor del objeto
     */
    public String getIp_servidor(){ return ip_servidor;  }
    /**
     * Funcion para la devolucion de la variable user_servidor
     * @return String variable user_servidor del objeto
     */
    public String getUser_servidor(){ return user_servidor; }
    /**
     * Funcion para la devolucion de la variable pass_servidor
     * @return String variable pass_servidor del objeto
     */
    public String getPass_servidor(){ return pass_servidor; }
    /**
     * Funcion para la devolucion de la variable nombre_equipo
     * @return String variable nombre_equipo del objeto
     */
    public String getNombre_equipo(){ return nombre_equipo; }
    /**
     * Funcion para la devolucion de la variable topico_suscripcion
     * @return String variable topico_suscripcion del objeto
     */
    public String getTopico_suscripcion() { return topico_suscripcion; }
    /**
     * Funcion para la devolucion de la variable ver_oks
     * @return boolean variable ver_oks del objeto
     */
    public boolean getVerOks(){ return ver_oks;}

    /**
     * Funcion para escribir la variable ip_servidor del objeto
     * @param _ip_servidor String valor a escribir en la variable ip_servidor del objeto
     */
    public void setIp_servidor(String _ip_servidor) { ip_servidor=_ip_servidor; }
    /**
     * Funcion para escribir la variable puerto del objeto
     * @param _puerto int valor a escribir en la variable puerto del objeto
     */
    public void setPuerto(int _puerto){ puerto=_puerto; }
    /**
     * Funcion para escribir la variable user_servidor del objeto
     * @param _user_servidor String valor a escribir en la variable user_servidor del objeto
     */
    public void setUser_servidor(String _user_servidor){ user_servidor=_user_servidor; }
    /**
     * Funcion para escribir la variable pass_servidor del objeto
     * @param _pass_servidor String valor a escribir en la variable pass_servidor del objeto
     */
    public void setPass_servidor(String _pass_servidor){ pass_servidor=_pass_servidor; }
    /**
     * Funcion para escribir la variable nombre_equipo del objeto
     * @param _nombre_equipo String valor a escribir en la variable nombre_equipo del objeto
     */
    public void setNombre_equipo(String _nombre_equipo){ nombre_equipo=_nombre_equipo; }
    /**
     * Funcion para escribir la variable topico_suscripcion del objeto
     * @param _topico_suscripcion String valor a escribir en la variable topico_suscripcion del objeto
     */
    public void setTopico_suscripcion(String _topico_suscripcion) { topico_suscripcion=_topico_suscripcion; }
    /**
     * Funcion para escribir la variable ver_oks del objeto
     * @param _ver_oks boolean valor a escribir en la variable ver_oks del objeto
     */
    public void setVer_oks(boolean _ver_oks) { ver_oks=_ver_oks; }


    /**
     * Esta funcion escribe los valores del objeto de esta clase en las preferencias de la aplicacion
     * @param activity aqui hay que pasar la activity en la cual queremos escribir las preferencias
     * @return devuelve true si todo ha ido bien o false si no ha sido asi y ha habido alguna excepcion
     */
    public boolean EscribeConfiguracion(Activity activity){
        boolean respuesta=false;
        try{
            SharedPreferences prefs =
                    activity.getSharedPreferences("AveriasI40", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString("ip",ip_servidor);
            editor.putInt("puerto",puerto);
            editor.putString("usuario",user_servidor);
            editor.putString("password",pass_servidor);
            editor.putString("nombre_equipo",nombre_equipo);
            editor.putString("topico",topico_suscripcion);
            editor.putBoolean("ver_oks",ver_oks);
            editor.commit();
            respuesta=true;
        }catch (Exception ex){
            Log.e(tag, ex.getMessage());
        }
        return respuesta;
    }

    /**
     * Esta funcion lee los valores de las preferencias y las guarda en las variables del objeto
     * @param activity aqui hay que pasar la activity de la cual queremos leer sus preferencias
     * @return devuelve true si todo ha ido bien o false si no ha sido asi y ha habido alguna excepcion
     */
    public boolean LeeConfiguracion(Activity activity){
        boolean respuesta=false;
        try{
            SharedPreferences prefs =
                    activity.getSharedPreferences("AveriasI40", Context.MODE_PRIVATE);
            ip_servidor=prefs.getString("ip","192.168.1.30");
            puerto=prefs.getInt("puerto",1883);
            user_servidor=prefs.getString("usuario","eduardo");
            pass_servidor=prefs.getString("password", "asdf1234");
            nombre_equipo=prefs.getString("nombre_equipo", "Smartphone 1");
            topico_suscripcion=prefs.getString("topico", "Nave Pintura");
            ver_oks=prefs.getBoolean("ver_oks",true);
            respuesta=true;
        }catch(Exception ex){
            Log.e(tag, ex.getMessage());
        }
        return respuesta;
    }
}
