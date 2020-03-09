//<editor-fold desc="Cabecera">
/**
 * Clase para crear un suscriptor MQTT para recibir    
 *       todas las publicaciones del sistema de averías.
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
package EBuetas.TFM.MQTT;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import EBuetas.TFM.BBDD.*;
import EBuetas.TFM.EscribeLog;
/**
 * Clase para crear un suscriptor MQTT para recibir    
 *   todas las publicaciones del sistema de averías.
 * @author Eduardo Buetas
 */
public class MQTTSus_Pub implements MqttCallback {
    MqttClient clienteSus=null;
    MqttClient clientePub=null;
    private String ip="";
    private int puerto=0;
    private String usuario="";
    private String password="";
    /**
     * Esta variable publica informa si la conexion con el broker esta OK
     */
    public boolean conexionOK=false;
    /**
     * Constructor de la clase para crear de manejo MQTT
     * @param _ip valor String ip del broker MQTT
     * @param _puerto valor Int del puerto del broker MQTT
     * @param _usuario valor String del usuario del broker MQTT
     * @param _password valor String del passwor del broker MQTT
     */
    public MQTTSus_Pub (String _ip,int _puerto, String _usuario, String _password){
        ip=_ip;
        puerto=_puerto;
        usuario=_usuario;
        password=_password;
    }
    /**
     * Metodo que activa el suscriptor MQTT de todas los topicos cuya raiz sea
     * Averias/
     */
    public void ActivarSuscriptor ()
    {
        try{
            String cadenaConexion="tcp://"+ip+":"+String.valueOf(puerto);
            MqttConnectOptions opciones=new MqttConnectOptions();
            opciones.setUserName(usuario);
            opciones.setPassword(password.toCharArray());            
            clienteSus=new MqttClient (cadenaConexion,"suscriptor_server");
            clienteSus.connect(opciones);            
            clienteSus.subscribe("Averias/#",2);
            clienteSus.setCallback(this);
            conexionOK=true;
        }catch(MqttException ex){
            EscribeLog.EscribeError(ex.getMessage(), 300);
        }catch(Exception ex){
            EscribeLog.EscribeError(ex.getMessage(), 301);
        }
    }
     /**
     * Metodo que activa el publicador MQTT 
     */
    public void ActivarPublicador ()
    {
        try{
            if (conexionOK){
                conexionOK=false;
                String cadenaConexion="tcp://"+ip+":"+String.valueOf(puerto);
                MqttConnectOptions opciones=new MqttConnectOptions();
                opciones.setUserName(usuario);
                opciones.setPassword(password.toCharArray());
                clientePub=new MqttClient (cadenaConexion,"publicador_server");
                clientePub.connect(opciones);
                conexionOK=true;                
            }
        }catch(MqttException ex){
            EscribeLog.EscribeError(ex.getMessage(), 350);
        }catch(Exception ex){
            EscribeLog.EscribeError(ex.getMessage(), 351);
        }
    }
    /**
     * Metodo que se lanza ante una perdida de conexion con el broker MQTT
     * @param cause valor Throwable con el contenido de la causa de la desconexion
     */
    @Override
    public void connectionLost(Throwable cause) { 
        EscribeLog.EscribeError(cause.getLocalizedMessage()+"--"+cause.getMessage(),310);
        conexionOK=false;

    }
    /**
     * Metodo que se lanza ante la llegada de un mensaje MQTT
     * @param topic valor String que contiene el topico del mensaje recibido
     * @param message valor MqttMessage con el contenido del mensaje recibido
     * @throws Exception Esta funcion esta controlada por excepciones de tipo Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
            String area="";
            String subarea="";
            String sistema="";
            String zona="";
            String elemento="";
            String averia="";
            String valor="";
            Datos_m_averias averia_obj=null;
            String[] partes_topic=topic.split("/");            
            if (partes_topic.length==7)
            {
                area=partes_topic[1];
                subarea=partes_topic[2];
                sistema=partes_topic[3];
                zona=partes_topic[4];
                elemento=partes_topic[5]; 
                averia=partes_topic[6];
                valor=message.toString().substring(0,1);
                averia_obj=new Datos_m_averias(area,subarea,sistema,zona,elemento,averia);
                if (averia_obj.LeeDatos())
                {
                    BBDD_Averias base_datos=new BBDD_Averias();
                    switch (valor){

                        case "0": //fin averia
                            base_datos.Conectar();
                            if (!base_datos.CierraAveria(averia_obj)){
                                EscribeLog.EscribeError("Fin de averia no valida:\n"+topic+" "+message.toString(), 322);
                            }
                            base_datos.Desconectar();
                            break;
                        case "1": //inicio averia                        
                            base_datos.Conectar();
                            if (!base_datos.InicioAveria(averia_obj)){
                                EscribeLog.EscribeError("Insercion de averia no valida:\n"+topic+" "+message.toString(), 323);
                            }
                            base_datos.Desconectar();
                            break;
                        case "2": //averia acusada
                            if (message.toString().length()>3){
                                String equipo=message.toString().substring(2);
                                base_datos.Conectar();
                                if (!base_datos.AcuseAveria(averia_obj,equipo)){
                                    EscribeLog.EscribeError("Acuse de averia no valida:\n"+topic+" "+message.toString(), 324);
                                }
                                base_datos.Desconectar();
                               // PublicaMensaje(topic,"2");
                            }else{
                                EscribeLog.EscribeError("Valor acusador de averia no valido:\n"+topic+" "+message.toString(), 325);
                            }
                            break;
                        case "3": //peticion informacion        
                            if (message.toString().length()>3){
                                String[] datos_peticion=message.toString().substring(2).split("-");
                                if (datos_peticion.length==2){
                                    String peticionario=datos_peticion[0];
                                    String info =datos_peticion[1];
                                    switch (info){
                                        case "mensaje":
                                            if (!PublicaMensaje("Informacion/"+peticionario+"/mensaje",averia_obj.getMensaje())){
                                                EscribeLog.EscribeError("Publicacion mensaje fallida", 328);  
                                            }
                                            break;
                                        case "descripcion":
                                            if (!PublicaMensaje("Informacion/"+peticionario+"/descripcion",averia_obj.getDescripcion())){
                                                EscribeLog.EscribeError("Publicacion descripcion fallida", 329);  
                                            }
                                            break;
                                        case "actuacion":
                                            if (!PublicaMensaje("Informacion/"+peticionario+"/actuacion",averia_obj.getActuacion())){
                                                EscribeLog.EscribeError("Publicacion actuacion fallida", 330);  
                                            }
                                            break;
                                        case "prioridad":
                                            if (!PublicaMensaje("Informacion/"+peticionario+"/prioridad",String.valueOf(averia_obj.getPrioridad()))){
                                                EscribeLog.EscribeError("Publicacion prioridad fallida", 331);  
                                            }
                                            break;
                                        case "todo":
                                            if (!PublicaMensaje("Informacion/"+peticionario+"/mensaje",averia_obj.getMensaje())){
                                                EscribeLog.EscribeError("Publicacion mensaje fallida", 332); 
                                            }
                                            if (!PublicaMensaje("Informacion/"+peticionario+"/descripcion",averia_obj.getDescripcion())){
                                                 EscribeLog.EscribeError("Publicacion descripcion fallida", 333);  
                                            }
                                            if (!PublicaMensaje("Informacion/"+peticionario+"/actuacion",averia_obj.getActuacion())){
                                                EscribeLog.EscribeError("Publicacion actuacion fallida", 334);  
                                            }
                                            if (!PublicaMensaje("Informacion/"+peticionario+"/prioridad",String.valueOf(averia_obj.getPrioridad()))){
                                                EscribeLog.EscribeError("Publicacion prioridad fallida", 335);  
                                            }
                                            break;
                                        default:
                                            EscribeLog.EscribeError("Peticion de informacion no valida:\n"+topic+" "+message.toString(), 327);
                                            break;
                                    }  

                                }else{
                                    EscribeLog.EscribeError("Peticion de informacion no valida:\n"+topic+" "+message.toString(), 326);
                                } 
                            }else{
                                EscribeLog.EscribeError("Peticion de informacion no valida:\n"+topic+" "+message.toString(), 337); 
                            }
                        default:
                            EscribeLog.EscribeError("Valor Mensaje no valido:\n"+topic+"--"+message.toString(), 321);
                            break;
                    }
                }else{
                     EscribeLog.EscribeError("Averia no valida:\n"+topic, 336);
                }

            }else{
                EscribeLog.EscribeError("Topico no debidamente formateado:\n"+topic, 320);
            }

    }   
    /**
     * Metodo que se lanza en el momento de terminar el envio de un mensaje 
     * @param token valor IMqttDeliveryToken que contiene los datos de la entrega del mensaje
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
             

    }
    /**
     * Metodo para publicar mensajes de informacion pedida por un receptor de averias
     * @param Topic valor String que contiene el topico del mensaje a enviar
     * @param Mensaje valor String que contiene el mensaje a enviar
     * @return Valor Booleano que nos devuelve true si el metodo ha tenido exito 
     *          y false si no lo ha tenido
     */
    private boolean PublicaMensaje(String Topic, String Mensaje)
    {
        boolean respuesta=false;
        try{
          
            MqttMessage message = new MqttMessage(); //creamos el objeto mensaje
            message.setQos(2); //configuramos el Qos 2, el broker recibira el mensaje al menos una y solamente una vez
            message.setRetained(false); //configuramos el retener, si algun suscriptor no esta conectado cuando se conecte recibira el mensaje 
            message.setPayload(Mensaje.getBytes()); //rellenamos el mensaje
            
            clientePub.publish(Topic, message); //publicamos el mensje en el topic correspondiente
            
            respuesta=true;
        }catch(MqttException ex){
            EscribeLog.EscribeError(ex.getMessage(), 340);  
            if (!clientePub.isConnected()){
                ActivarPublicador();
            }
        }catch(Exception ex){
            EscribeLog.EscribeError(ex.getMessage(), 341);  
        }
        return respuesta;
    }
}
