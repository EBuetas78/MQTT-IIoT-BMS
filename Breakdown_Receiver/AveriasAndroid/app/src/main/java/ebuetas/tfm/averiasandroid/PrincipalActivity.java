//<editor-fold desc="Cabecera">
/**
 * En esta clase, heredada de AppCompactActivity, se crea la actividad principal de la aplicacion,
 * el layout de esta aplicacion esta en el fichero activity_principal.xml del directorio res/layout
 * de la aplicacion.
 * Esta actividad es la que muestra la ListView principal de la aplicacion donde se muestran
 * las alarmas y desde la que se llama a las otras actividades de la aplicacion,
 * la actividad de configuracion y la actividad de acuse y peticion de detalles
 * de cada averia.
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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.AdapterView;
import android.view.View;
import android.content.BroadcastReceiver;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


import ebuetas.tfm.averiasandroid.mqtt.ServicioMQTT;

/**
 * En esta clase, heredada de AppCompactActivity, se crea la actividad principal de la aplicacion,
 * el layout de esta aplicacion esta en el fichero activity_principal.xml del directorio res/layout
 * de la aplicacion.
 * Esta actividad es la que muestra la ListView principal de la aplicacion donde se muestran
 * las alarmas y desde la que se llama a las otras actividades de la aplicacion,
 * la actividad de configuracion y la actividad de acuse y peticion de detalles
 * de cada averia.
 */
public class PrincipalActivity extends AppCompatActivity {
    //<editor-fold desc="Variables globales de la clase">
    private ListView tablaAverias;
    private ArrayList<Averias> averias = new ArrayList<Averias>();
    private AveriasAdapter adaptador_averias = new AveriasAdapter(this,averias);
    private Button btnConectar;
    private Button btnBorrarResueltas;
    private Button btnDesconectar;
    private TextView lblConectado;
    private Intent msgIntent;
    private final String tag="Main";
    private boolean Mostrar_Notificaciones=false;
    private AppCompatActivity principalActividad;
    private ServiceReceiver rcv;
    private Configuracion configuracion=null;
    //</editor-fold>
    //<editor-fold desc="Sobreescrituras de la clase base">
    /**
     * Esta funcion sobre escribe la funcion onCreate de la clase de la que heredamos
     * Desde aqui llamamos a la funcion de asignar elementos, asignar eventos y crear el canal de notificaciones
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        AsignarElementos();
        AsignarEventos();
        FuncAux.createNotificationChannel(this);
        principalActividad=this;
    }

    /**
     * Sobreescribimos la funcion onResume con el unico objetivo de que cuando estemos con la app
     * en primer plano no salgan las notificaciones
     */
    @Override
    protected void onResume(){
        Mostrar_Notificaciones=false;
        FuncAux.BorrarNotificaciones(this);
        configuracion=new Configuracion();
        configuracion.LeeConfiguracion(this);
        if (configuracion.getVerOks()){
            btnBorrarResueltas.setVisibility(View.VISIBLE);
            btnBorrarResueltas.setEnabled(true);
        }else{
            btnBorrarResueltas.setVisibility(View.INVISIBLE);
            btnBorrarResueltas.setEnabled(false);
        }
        super.onResume();
    }

    /**
     * Sobreescribimos la funcion onPause con el unico objetivo que cuando estemos con la app no en
     * primer plano salgan las notificaciones
     */
    @Override
    protected void onPause(){
        Mostrar_Notificaciones=true;
        super.onPause();
    }

    /**
     * Sobreescribimos onDestroy para finalizar la app cuando la destruyamos
     */
    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(rcv);
        finish();
    }

    /**
     * Sobreescribimos onKeyDown con el objetivo que cuando pulsemos la tecla atras no destruyamos
     * la aplicacion
     * @param keyCode codigo de la tecla pulsada
     * @param event objeto del tipo KeyEvent asociado al evento
     * @return Devolveremos un false si hemos pulsado la tebla atras y si no lo que nos devuelva
     *              la funcion de la clase de la que heredamos
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean respuesta=false;
        if (keyCode==4){
            FuncAux.MostrarAlert(this,"No debe cerrar esta aplicacion de lo contrario dejara de recibir las alarmas");
        }else{
            respuesta=super.onKeyDown(keyCode,event);
        }
        return respuesta;
    }
    /**
     * Esta funcion se encarga de sacar el menu de la activity, es un override de la funcion
     * de crear el menu
     * @param menu este parametro es el menu que se le pasa a la funcion
     * @return siempre devuelve true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }
    /**
     * Esta funcion controla las acciones de las pulsaciones en los items del menu
     * es un override de la funcion de la clase base
     * @param item el item del menu que ha sido pulsado
     * @return despues de la ejecucion llama a la funcion que sobreescribe y su devolucion es la devolucion de esta funcion
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if (id==R.id.acerca){
            FuncAux.MostrarAlert(this,"Este es un programa creado por Eduardo Buetas para el TFM del master en Investigacion para el Desarrollo de Software y sistemas informaticos de la UNED");
        }else if (id==R.id.configuracion){
            Intent intent=new Intent(this,ConfigActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    //</editor-fold>
    //<editor-fold desc="eventos elementos layout">
    /**
     * funcion del evento onclick del boton conectar, ejecuta la funcion ArrancarServicio
     */
    private void btnConectarOnClick() {
        ArrancarServicio();
    }

    /**
     * funcion del evento onclick del boton desconectar, lanza una ventana con una pregunta
     * para asegurarnos que el usuario quiere desconectar y si pulsa si desconectamos el servicio
     */
    private void btnDesconectarOnClick(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Desconexion");
        builder.setMessage("¿Esta seguro de desconectar el cliente MQTT?\n Si pulsa si dejara de recibir las Averias hasta una nueva conexion");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (msgIntent!=null){
                    stopService(msgIntent);
                    msgIntent=null;
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    /**
     * funcion del evento onclick del boton borrarresuelstas
     * cuando le demos a este boton todas las averias que esten en estado 0 (Averia finalizada)
     * desapareceran de la lista
     */
    private void btnBorrarResueltasOnClick() {
        Iterator<Averias> iter = averias.iterator(); //recorremos con un iterador para borrar las averias con estado 0
        while (iter.hasNext()) {
            Averias averia= iter.next();
            if (averia.getEstado()==0){
                iter.remove();
            }
        }
        adaptador_averias.notifyDataSetChanged();   //actualizamos la lista
    }

    //</editor-fold>
    //<editor-fold desc="funciones auxiliares">
    /**
     * funcion para arrancar el servicio de nuestro programa con el MQTT
     */
    private void ArrancarServicio(){
        try {
            msgIntent = new Intent(PrincipalActivity.this, ServicioMQTT.class);
            msgIntent.putExtra("ip",configuracion.getIp_servidor() );
            msgIntent.putExtra("puerto",configuracion.getPuerto());
            msgIntent.putExtra("usuario",configuracion.getUser_servidor());
            msgIntent.putExtra("password",configuracion.getPass_servidor());
            msgIntent.putExtra("topic",configuracion.getTopico_suscripcion());
            msgIntent.putExtra("nombre_equipo",configuracion.getNombre_equipo());
            startService(msgIntent);
        }
        catch (android.os.NetworkOnMainThreadException ex)
        {
            Log.e(tag, ex.getMessage());
        }
        catch (Exception ex){
            Log.e(tag, ex.getMessage());
        }
    }

    /**
     * Esta funcion asigna los eventos del servicio MQTT a esta activity
     */
    private void AsignarEventos(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(ServicioMQTT.AVERIA_RECIBIDA);
        filter.addAction(ServicioMQTT.CONEXION_FALLIDA);
        filter.addAction(ServicioMQTT.CONEXION_OK);
        filter.addAction(ServicioMQTT.DESCONEXION);
        rcv = new ServiceReceiver();
        rcv.setActividad(this);
        registerReceiver(rcv, filter);
        btnConectar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnConectarOnClick();
            }
        });
        btnBorrarResueltas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {  btnBorrarResueltasOnClick(); } });
        btnDesconectar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {  btnDesconectarOnClick(); } });
        tablaAverias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(principalActividad,DetalleActivity.class);
                intent.putExtra("averia", averias.get(position).toString());
                startActivityForResult(intent,1);
            }
        });
    }

    /**
     * Esta funcion asigna los elementos del layout a los objetos con los que los trataremos en el codigo
     */
    private void AsignarElementos(){
        tablaAverias=findViewById(R.id.Lista_Averias);
        tablaAverias.setAdapter(adaptador_averias);
        btnConectar=findViewById(R.id.btnConectar);
        lblConectado=findViewById(R.id.lblConectado);
        btnBorrarResueltas=findViewById(R.id.btnBorrarResueltas);
        btnDesconectar=findViewById(R.id.btnDesconectar);
        btnDesconectar.setEnabled(false);

    }
    //</editor-fold>
    //<editor-fold desc="Clase para recibir los eventos del servicio">
    /**
     * Esta clase que hereda de BroadcastReceiver sirve para recibir los eventos del servicio
     */
    public class ServiceReceiver extends BroadcastReceiver {
        private Activity actividad;
        /**
         * Esta funcion se ejecuta cuando uno de los eventos registrados en esta activity es lanzado
         * @param context contexto desde el que se lanza el evento
         * @param intent evento que ha sido lanzado
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ServicioMQTT.AVERIA_RECIBIDA)) {
                FuncAux.success_wav(actividad);

                Averias averia=new Averias();
                averia.setArea(intent.getStringExtra("Area"));
                averia.setSubarea(intent.getStringExtra("Subarea"));
                averia.setSistema(intent.getStringExtra("Sistema"));
                averia.setZona(intent.getStringExtra("Zona"));
                averia.setElemento(intent.getStringExtra("Elemento"));
                averia.setNombre(intent.getStringExtra("Nombre"));
                averia.setEstado(Integer.parseInt(intent.getStringExtra("Estado")));
                SimpleDateFormat formato=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date fecha=new Date();
                averia.setHora(formato.format(fecha));
                for (int i=0;i<averias.size();i++)   //Si la averia ya existe la borramos para que aparezca unicamente el nuevo estado en la lista
                {
                    if (averias.get(i).equals(averia)){
                        averias.remove(i);
                        break;
                    }
                }
                if ((averia.getEstado()!=0) || (configuracion.getVerOks())) {
                    averias.add(0, averia); //ponemos la nueva averia en el index 0 de esta manera siempre estara la primera la ultima en llegar
                }
                adaptador_averias.notifyDataSetChanged();   //actualizamos la lista
                if (Mostrar_Notificaciones) {   //Si estamos con la activity oculta lanzamos la notificacion
                    FuncAux.notifcation(actividad, averia.toString(), "Nuevo Evento Averias Industria 4.0");
                }
            }else if(intent.getAction().equals(ServicioMQTT.CONEXION_OK)){
                lblConectado.setText("Servidor MQTT Conectado");
                btnConectar.setEnabled(false);
                btnDesconectar.setEnabled(true);
                FuncAux.beep_wav(actividad);
            }else if(intent.getAction().equals(ServicioMQTT.CONEXION_FALLIDA)){
                lblConectado.setText("Intento de conexion MQTT fallida, espere unos segundos y vuelva a intentarlo");
                btnConectar.setEnabled(true);
                btnDesconectar.setEnabled(false);
            }else if(intent.getAction().equals(ServicioMQTT.DESCONEXION)){
                if (msgIntent!=null) {
                    lblConectado.setText("Atencion desconexion MQTT, espere unos segundos e intente la conexion");
                }else{
                    lblConectado.setText(R.string.conectado);
                }
                btnConectar.setEnabled(true);
                btnDesconectar.setEnabled(false);
                FuncAux.fail_wav(actividad);

            }
        }

        /**
         * Esta funcion sirve para cargar la actividad para llamar a las funciones auxiliares
         * @param _actividad actividad a cargar
         */
        public void setActividad(Activity _actividad){
            actividad=_actividad;
        }
    }
    //</editor-fold>
}
