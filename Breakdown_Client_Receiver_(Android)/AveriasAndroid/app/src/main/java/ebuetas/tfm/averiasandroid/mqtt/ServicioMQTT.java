package ebuetas.tfm.averiasandroid.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ServicioMQTT extends Service {

    private String ip="";
    private int puerto=0;
    private String usuario="";
    private String password="";
    private String topico="";
    private String nombre_equipo="";
    public static final String AVERIA_RECIBIDA="AveriaRecibida";
    public static final String CONEXION_OK="ConexionOK";
    public static final String CONEXION_FALLIDA="ConexionFallida";
    public static final String DESCONEXION="Desconexion";
    //private ClienteMQTT clienteMQTT;
    private MqttAndroidClient mqttAndroidClient;
    // private ProgressReceiverService rcv;
    String tag="MqttCliente";
    @Override
    public void onCreate(){
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        try{
            ip=intent.getStringExtra("ip");
            puerto=intent.getIntExtra("puerto",1883);
            usuario=intent.getStringExtra("usuario");
            password=intent.getStringExtra("password");
            topico=intent.getStringExtra("topic");
            nombre_equipo=intent.getStringExtra("nombre_equipo");
            //clienteMQTT = new ClienteMQTT();
            String uriServidor="tcp://"+ip+":"+puerto;
            //mqttAndroidClient = clienteMQTT.getMqttClient(getApplicationContext(), uriServidor, nombre_equipo,usuario,password);
            mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), uriServidor, nombre_equipo);
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(false);
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setUserName(usuario);
            mqttConnectOptions.setPassword(password.toCharArray());
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

                       // clienteMQTT.subscribe(mqttAndroidClient,"Averias/"+topico+"/#",2);
                        mqttAndroidClient.subscribe("Averias/"+topico+"/#",2);
                        Intent bcIntent = new Intent();
                        bcIntent.setAction(CONEXION_OK);
                        sendBroadcast(bcIntent);
                    }catch(MqttException ex){
                        Log.e(tag, "Fallo Suscripcion" + topico);
                        Intent bcIntent = new Intent();
                        bcIntent.setAction(CONEXION_FALLIDA);
                        sendBroadcast(bcIntent);
                    }
                }
                @Override
                public void connectionLost(Throwable throwable) {
                    Intent bcIntent = new Intent();
                    bcIntent.setAction(DESCONEXION);
                    sendBroadcast(bcIntent);
                }

                /**
                 * Metodo que se lanza ante la llegada de un mensaje MQTT
                 * @param s valor String que contiene el topico del mensaje recibido
                 * @param mqttMessage valor MqttMessage con el contenido del mensaje recibido
                 * @throws Exception Esta funcion debe estar controlada por excepciones de tipo Exception
                 */
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

                    try {
                        if ((mqttMessage.toString().substring(0,1).equals("0")) || (mqttMessage.toString().substring(0,1).equals("1")) || (mqttMessage.toString().substring(0,1).equals("2"))) {
                            String[] partes_topic = s.split("/");
                            if (partes_topic.length == 7) {
                                Intent bcIntent = new Intent();
                                bcIntent.putExtra("Area", partes_topic[1]);
                                bcIntent.putExtra("Subarea", partes_topic[2]);
                                bcIntent.putExtra("Sistema", partes_topic[3]);
                                bcIntent.putExtra("Zona", partes_topic[4]);
                                bcIntent.putExtra("Elemento", partes_topic[5]);
                                bcIntent.putExtra("Nombre", partes_topic[6]);
                                bcIntent.putExtra("Estado", mqttMessage.toString().substring(0,1));
                                bcIntent.setAction(AVERIA_RECIBIDA);
                                sendBroadcast(bcIntent);
                            } else {
                                Log.e(tag, "Topico no debidamente formateado:\n" + s);
                            }
                        }
                    }catch(Exception ex) {
                        Log.e(tag,ex.getMessage());
                    }
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });

        }catch(Exception ex){
            Log.e(tag,ex.getMessage());
        }
        return START_STICKY;
    }
    @Override
    public void onDestroy(){
        try{
            mqttAndroidClient.disconnect();
        }catch (MqttException ex){
            Log.e(tag, ex.getMessage());
        }

        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
