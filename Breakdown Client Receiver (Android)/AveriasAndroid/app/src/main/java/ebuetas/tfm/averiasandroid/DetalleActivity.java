//<editor-fold desc="Cabecera">
/**
 * En esta clase, heredada de AppCompactActivity, se crea la actividad que permite al usuario
 * acusar una averia o pedir las informaciones adicionales de una averia, es lanzada al pulsar
 * sobre una averia de la actividad principal de la aplicacion, el layout de esta aplicacion esta
 * en el fichero activity_detalles.xml del directorio res/layout de la aplicacion
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


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * En esta clase, heredada de AppCompactActivity, se crea la actividad que permite al usuario
 * acusar una averia o pedir las informaciones adicionales de una averia, es lanzada al pulsar
 * sobre una averia de la actividad principal de la aplicacion, el layout de esta aplicacion esta
 * en el fichero activity_detalles.xml del directorio res/layout de la aplicacion
 */
public class DetalleActivity extends AppCompatActivity {
    //<editor-fold desc="Variables Globales">
    /**
     * Creamos las variables utilizadas en la clase
     */
    Spinner spnnPedirInfo;
    int posicion_spinner;
    TextView lblAveria;
    TextView lblMensaje;
    TextView lblDescripcion;
    TextView lblActuacion;
    TextView lblPrioridad;
    Button btnAcusar;
    Button btnPedirInfo;
    Button btnAtras;
    AppCompatActivity actividad_Detalle;
    public static final String ACUSAR_AVERIA="Acusar_averia";
    static final String tag="DetalleActivity";
   // private ClienteMQTT clienteMQTT;
    private MqttAndroidClient mqttAndroidClient;
    private  Configuracion configuracion=null;
    //</editor-fold>
    /**
     * Funcion sobreescrita del onCreate, se ejecuta cada vez que se crea la activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        AsignarElementos();
        AsignarEventos();
        lblAveria.setText(getIntent().getStringExtra("averia"));
        actividad_Detalle=this;
        btnPedirInfo.setEnabled(false);
        btnAcusar.setEnabled(false);
        CrearConexionMQTT();
    }
    /**
     * Funcion sobreescrita del onDestroy, se ejecuta cada vez que se destruye la activity
     */
    @Override
    protected void onDestroy(){
        try{
            mqttAndroidClient.disconnect();
        }catch(MqttException ex){
            Log.e(tag,ex.getMessage());
        }catch(Exception ex){
            Log.e(tag,ex.getMessage());
        }

        super.onDestroy();
    }
    //<editor-fold desc="eventos elementos layout">
    /**
     * Funcion del evento OnClick del boton Acusar
     */
    public void btnAcusarOnClick(){
        String mensaje="";
        String topico="";
        try{
            Pattern patron = Pattern.compile("Averia (.*?) en (.*?) Estado:.*");
            Matcher matcher = patron.matcher(lblAveria.getText());

            if (matcher.matches()){
                topico = "Averias/"+matcher.group(2)+"/"+matcher.group(1);
                mensaje="2-"+configuracion.getNombre_equipo();
                //clienteMQTT.publishMessage(mqttAndroidClient,mensaje,2,topico);
                try{
                    byte[] encodedPayload = new byte[0];
                    encodedPayload = mensaje.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    message.setRetained(false);
                    message.setQos(2);
                    mqttAndroidClient.publish(topico, message);

                }catch (MqttException ex){
                    Log.e (tag,ex.getMessage());
                }
                finish();
            }else{
                FuncAux.MostrarAlert(this,"Problemas al intentar acusar la averia");
                Log.e(tag,"Expresion regular no localiza el topico y la averia");
            }


        }catch(Exception ex){
            Log.e(tag,ex.getMessage());
        }
    }
    /**
     * Funcion del evento OnClick del boton Pedir Info
     */
    public void btnPedirInfoOnClick(){
        String mensaje="";
        String topico="";
        switch (posicion_spinner){
            case 0: mensaje="3-"+configuracion.getNombre_equipo()+"-todo";
                    break;
            case 1: mensaje="3-"+configuracion.getNombre_equipo()+"-mensaje";
                break;
            case 2: mensaje="3-"+configuracion.getNombre_equipo()+"-descripcion";
                break;
            case 3: mensaje="3-"+configuracion.getNombre_equipo()+"-actuacion";
                break;
            case 4: mensaje="3-"+configuracion.getNombre_equipo()+"-prioridad";
                break;

        }

        try{
            Pattern patron = Pattern.compile("Averia (.*?) en (.*?) Estado:.*");
            Matcher matcher = patron.matcher(lblAveria.getText());

            if (matcher.matches()){
                topico = "Averias/"+matcher.group(2)+"/"+matcher.group(1);
                //clienteMQTT.publishMessage(mqttAndroidClient,mensaje,2,topico);
                try{
                    byte[] encodedPayload = new byte[0];
                    encodedPayload = mensaje.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    message.setRetained(false);
                    message.setQos(2);
                    mqttAndroidClient.publish(topico, message);

                }catch (MqttException ex){
                    Log.e (tag,ex.getMessage());
                }


            }else{
                FuncAux.MostrarAlert(this,"Problemas al intentar acusar la averia");
                Log.e(tag,"Expresion regular no localiza el topico y la averia");
            }


        }catch(Exception ex){
            Log.e(tag,ex.getMessage());
        }
    }
    public void btnAtrasOnClick(){
        try{
            Intent data =new Intent();
            setResult(RESULT_OK,data);
            finish();
        }catch (Exception ex){
            Log.e(tag,ex.getMessage());
        }
    }
    //</editor-fold>
    //<editor-fold desc="funciones auxiliares">
    /**
     * Esta funcion asigna los eventos del servicio MQTT a esta activity
     */
    public void AsignarEventos() {
        btnAcusar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnAcusarOnClick();
            }
        });
        btnPedirInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnPedirInfoOnClick();
            }
        });
        btnAtras.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnAtrasOnClick();
            }
        });
        spnnPedirInfo.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View vies,
                                       int position, long id) {
             //  FuncAux.MostrarAlert(actividad_Detalle,"seleccionado--"+adapter.getItemAtPosition(position));
                posicion_spinner=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
               // FuncAux.MostrarAlert(actividad_Detalle,"No seleccionado");
            }
        });
    }
    /**
     * Esta funcion asigna los elementos del layout a los objetos con los que los trataremos en el codigo
     */
    public void AsignarElementos(){
        spnnPedirInfo=findViewById(R.id.spnnPedirInfo);
        ArrayAdapter<CharSequence> pedirInfoAdapter=ArrayAdapter.createFromResource(this,
                R.array.detalles_array,android.R.layout.simple_spinner_item);
        pedirInfoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnnPedirInfo.setAdapter(pedirInfoAdapter);
        lblAveria=findViewById(R.id.lblAveria);
        lblMensaje=findViewById(R.id.lblMensaje);
        lblDescripcion=findViewById(R.id.lblDescripcion);
        lblActuacion=findViewById(R.id.lblActuacion);
        lblPrioridad=findViewById(R.id.lblprioridad);
        btnAcusar=findViewById(R.id.btnAcusar);
        btnPedirInfo=findViewById(R.id.btnPedirInfo);
        btnAtras=findViewById(R.id.btnAtras);

    }
    //</editor-fold>

    /**
     * Esta funcion crea la conexion MQTT para recibir los detalles pedidos por parte del usuario
     */
    public void CrearConexionMQTT(){
        configuracion=new Configuracion();
        configuracion.LeeConfiguracion(this);
        //clienteMQTT = new ClienteMQTT();
        String uriServidor="tcp://"+configuracion.getIp_servidor()+":"+configuracion.getPuerto();
        try{
            mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), uriServidor, configuracion.getNombre_equipo()+"_publicador");
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(false);
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setUserName(configuracion.getUser_servidor());
            mqttConnectOptions.setPassword(configuracion.getPass_servidor().toCharArray());
            mqttAndroidClient.connect(mqttConnectOptions);
            mqttAndroidClient.setCallback(new MqttCallbackExtended() {
                /**
                 * Esta funcion es lanzada cuando la conexion MQTT es terminada con exito
                 * @param b es un booleano, si es true es que la conexion es resultado de una reconexion
                 * @param s es un String que representa la direccion uri del servidor
                 */
                @Override
                public void connectComplete(boolean b, String s) {
                    try{
                        btnPedirInfo.setEnabled(true);
                        btnAcusar.setEnabled(true);
                       // clienteMQTT.subscribe(mqttAndroidClient,"Informacion/"+configuracion.getNombre_equipo()+"/#",2);
                        mqttAndroidClient.subscribe("Informacion/"+configuracion.getNombre_equipo()+"/#",2);
                    }catch(Exception ex){
                        Log.e(tag, ex.getMessage());

                    }

                }

                @Override
                public void connectionLost(Throwable throwable) {

                }

                /**
                 * Metodo que se lanza ante la llegada de un mensaje MQTT
                 * @param s valor String que contiene el topico del mensaje recibido
                 * @param mqttMessage valor MqttMessage con el contenido del mensaje recibido
                 * @throws Exception Esta funcion debe estar controlada por excepciones de tipo Exception
                 */
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

                    if (s.equals("Informacion/"+configuracion.getNombre_equipo()+"/mensaje")){
                        lblMensaje.setText("Mensaje: "+mqttMessage.toString());
                    }else if(s.equals("Informacion/"+configuracion.getNombre_equipo()+"/descripcion")){
                        lblDescripcion.setText("Descripcion: "+mqttMessage.toString());
                    }else if(s.equals("Informacion/"+configuracion.getNombre_equipo()+"/actuacion")){
                        lblActuacion.setText("Actuacion: "+mqttMessage.toString());
                    }else if(s.equals("Informacion/"+configuracion.getNombre_equipo()+"/prioridad")) {
                        lblPrioridad.setText("Prioridad: "+mqttMessage.toString());
                    }
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
        }catch (MqttException ex){
            Log.d (tag,ex.getMessage());
        }
    }
}
