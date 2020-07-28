//<editor-fold desc="Cabecera">
/**
 * En esta clase, heredada de AppCompactActivity, se crea la actividad que permite al usuario
 * configurar la aplicacion, es lanzada al pulsar sobre el menu configuracion de la actividad
 * principal de la aplicacion, el layout de esta aplicación esta en el fichero
 * activity_config.xml del directorio res/layout de la aplicacion
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

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;


/**
 * En esta clase, heredada de AppCompactActivity, se crea la actividad que permite al usuario
 * configurar la aplicacion, es lanzada al pulsar sobre el menu configuracion de la actividad
 * principal de la aplicacion, el layout de esta aplicación esta en el fichero
 * activity_config.xml del directorio res/layout de la aplicacion
 * @author: Eduardo Buetas Sanjuan
 */
public class ConfigActivity extends AppCompatActivity {
    /**
     * Creo las variables globales en la clase para los objetos de la vista
     */
    private EditText txtip_server;
    private EditText txtpuerto_server;
    private EditText txtusuario_server;
    private EditText txtpass_server;
    private EditText txtnombre_equipo;
    private EditText txttopico_suscripcion;
    private Button btnOK;
    private Button btnCancel;
    private Switch swtVerOks;
    String tag="configuracion";
    /**
     * Funcion sobreescrita del onCreate, se ejecuta cada vez que se crea la activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        asignar_elementos();
        asignar_eventos();
        leer_configuracion();
    }

    /**
     * funcion para asignar los elementos del layout a los objetos en la clase de la activity
     */
    private void asignar_elementos(){
        txtip_server=findViewById(R.id.txtIPServer);
        txtpuerto_server=findViewById(R.id.txtPuertoServer);
        txtusuario_server=findViewById(R.id.txtUsuarioServer);
        txtpass_server=findViewById(R.id.txtPassServer);
        txtnombre_equipo=findViewById(R.id.txtNombre_Equipo);
        txttopico_suscripcion=findViewById(R.id.txtTopico);
        btnOK=findViewById(R.id.btnOK_Config);
        btnCancel=findViewById(R.id.btnCancel_Config);
        swtVerOks=findViewById(R.id.swtVerOks);
    }

    /**
     * Funcion para asignar los eventos de los objetos de la vista
     */
    private void asignar_eventos(){
        try{
            btnOK.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    btnOKOnClick();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    btnCancelOnClick();
                }
            });
        }catch(Exception ex){
            Log.e(tag, ex.getMessage());
        }
    }
    //<editor-fold desc="eventos">

    /**
     * Funcion del evento OnClick del boton OK
     */
    private void btnOKOnClick(){
        try{
            guardar_configuracion();
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Configuracion cambiada");
            dlgAlert.setTitle("Configuracion");
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }catch (Exception ex){
            Log.e(tag,ex.getMessage());
        }
    }
    /**
     * funcion del evento OnClick del boton cancelar
     */
    private void btnCancelOnClick(){
        try{
            finish();
        }catch (Exception ex){
            Log.e(tag,ex.getMessage());
        }
    }

    //</editor-fold>
    //<editor-fold desc="funciones auxiliares">

    /**
     * funcion para guardar las configuraciones designadas en las preferencias de la aplicacion
     */
    private void guardar_configuracion(){
        try{
            Configuracion configuracion = new Configuracion();
            configuracion.setIp_servidor(txtip_server.getText().toString());
            configuracion.setPuerto(Integer.parseInt(txtpuerto_server.getText().toString()));
            configuracion.setUser_servidor(txtusuario_server.getText().toString());
            configuracion.setPass_servidor(txtpass_server.getText().toString());
            configuracion.setNombre_equipo(txtnombre_equipo.getText().toString());
            configuracion.setTopico_suscripcion(txttopico_suscripcion.getText().toString());
            configuracion.setVer_oks(swtVerOks.isChecked());
            configuracion.EscribeConfiguracion(this);
            setResult(RESULT_OK);
            finish();
        }catch (Exception ex){
            Log.e(tag, ex.getMessage());
        }
    }

    /**
     * funcion para leer las configuraciones de la aplicacion de las preferencias
     */
    private void leer_configuracion(){
        try{
            Configuracion configuracion= new Configuracion();
            configuracion.LeeConfiguracion(this);
            txtip_server.setText(configuracion.getIp_servidor());
            txtpuerto_server.setText(String.valueOf(configuracion.getPuerto()));
            txtusuario_server.setText(configuracion.getUser_servidor());
            txtpass_server.setText(configuracion.getPass_servidor());
            txtnombre_equipo.setText(configuracion.getNombre_equipo());
            txttopico_suscripcion.setText(configuracion.getTopico_suscripcion());
            swtVerOks.setChecked(configuracion.getVerOks());
        }catch(Exception ex){
            Log.e(tag, ex.getMessage());
        }
    }
    //</editor-fold>
}
