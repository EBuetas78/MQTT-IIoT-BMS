/**
 * Sketch para Arduino como ejemplo de publicador de averias realizado 
 * con un microprocesador. 
 * Este controlador sera la simulacion del equipo "Laca 1" de la subzona "Maquinas"
 * de la zona general "Pintura"
 * Este controlador se conectara por Wifi al sistema. 
 * Su CienteId sera "publicadorArduino"
 * Publicara dos averias, la primera sera la publicacion de la averia estandar
 * de la conexion con el topico "Averias/Pintura/Maquinas/Laca 1/Mqtt/Mqtt/Conexion"
 * cuando se conecte enviara un mensaje 0 a este topico y se configurara este topico como 
 * willTopic y el willMessage sera 1 para que cuando el equipo se desconecte este mensaje 1 para 
 * el topico sea enviado
 * Por otro lado se publicara la averia con el topico "Averias/Pintura/Maquinas/Laca 1/Zona 1/ESTATICA1/Fallo Boquilla 5"
 * Esta averia sera controlada con un pulsador (PIN 2), con una pulsacion se activara el led de averia (pin 2) y 
 * se enviara el mensaje 1 al topico de la averia. 
 * Con la siguiente pulsacion se desactivara el led de averia y se enviara el mensaje 0 al topico de la averia
 * Destinatario: Trabajo Fin de Master                              
 *               Master Investigacion en Ingenieria de Software y   
 *               Sistemas Informaticos                              
 * Proyecto: Sistema de propagacion de averias en la Industria 4.0  
 * Fecha Creacion: 11/05/2018                                       
 * Fecha Revision: 11/05/2018                                       
 * @author: Eduardo Buetas
 * @version: 1.0
 */

#include <WiFi101.h>
#include <PubSubClient.h>

char ssid[] = "SSID_TFM";            //confiuracion de la conexion a la wifi
char pass[] = "s3168;6L";
IPAddress server(192,168,1,30);           //ip del broker mqtt
char clienteid[] = "publicadorArduino";   //Clinet ID del publicador de este equipo 
char topicname[] = "Averias/Pintura/Maquinas/Laca 1/Zona 1/ESTATICA1/Fallo Boquilla 5"; //nombres de los topicos
char willtopic[] = "Averias/Pintura/Maquinas/Laca 1/Mqtt/Mqtt/Conexion";
char user[] = "eduardo";    //usuario Mqtt
char password[]="asdf1234"; //password Mqtt
bool flanco_positivo=false; //Flancos para la gestion de la averia
bool flanco_negativo=false;
bool flanco_senal=false;
bool estado_averia=false;

const int buttonPin = 2;     // Pin de entrada de la senal del boton
const int ledPin =  5;      //  Pin de salida para el led
int buttonState = 0;         // Estado del boton

WiFiClient wificlient;          //objeto Wifi101 para la conexion wifi
PubSubClient client(wificlient); //objeto del cliente mqtt
/**
 * Funcion para conectar con el broker MQTT
 */
void conectarMQTT() {

//  Serial.print("Esperando para reconexion Mqtt...");
  // Intneto conexion
  if (client.connect(clienteid,user,password,willtopic,2,1,"1")) {
     //Conectamos con el mqtt y configuramos el mensaje will (para cuando se desconecte)
  //  Serial.println("Conectado"); //Sacamos la conexion por el puerto serie para debug
     delay(5000);           //Esperamos para darle tiempo al mqtt a conectarse
    // Enviamos el mensaje de conexion ok 
    //Serial.println("Enviando willtopic...");
    client.publish(willtopic,"0",true);
   // Serial.println("WillTopic enviado");
  } else {
    //Sacamos el fallo por el puerto serie para debug
  //  Serial.print("failed, rc=");
  //  Serial.print(client.state());
  //  Serial.println("Probaremos en 5 segundos...");
    delay(5000);
  }
}
/**
 * Funcion de setup se lanza cuando el micro arranca
 */
void setup() {    
 // Serial.begin(9600); //Arrancamos el puerto serie para ver los mensajes de debug
 // while (!Serial) { //Esperamos hasta que el puerto esta abierto
 //   delay(1000);
 // }
  WiFi.begin(ssid, pass); //Conectamos con la wifi
  delay(10000);           //Esperamos para darle tiempo al wifi a conectarse
  // Inicializamos el pin del led como salida
  pinMode(ledPin, OUTPUT);
  // Inicializamos el pin del boton como entrada
  pinMode(buttonPin, INPUT);
 // Metemos la configuracion del servidor en el objeto del clinte Mqtt
 client.setServer(server, 1883);
}
/**
 * Funcion que se ejecuta en bucle una vez terminado el setup y de manera infinita mientras el micro 
 * este activo
 */
void loop() {
  //lo primero en el bucle es mirar si esta conectada la wifi, si no lo esta volvemos a intentar que se conecte
   switch (WiFi.status()) {
    case WL_CONNECT_FAILED:
    case WL_CONNECTION_LOST:
    case WL_DISCONNECTED: WiFi.begin(ssid, pass);
  }
  // Si la wifi no esta conectada no hacemos nada, de lo contrario entramos en el if
  if (WiFi.status()==WL_CONNECTED){ 
    //Comprobamos si estamos conectados al Mqtt, si no lo estamos reconectamos
    if (!client.connected()) {
      conectarMQTT();
    }
    buttonState = digitalRead(buttonPin);
    //En el flanco positivo de la pulsacion del boton cambiamos el estado de la averia
    if (buttonState == HIGH && !flanco_senal) {      
        estado_averia=!estado_averia;
        flanco_senal=true;   
        Serial.println("Senal 1");     
    }
    if (buttonState == LOW) {           
       flanco_senal=false;      
       Serial.println("Senal 0");  
    }
    //en el flanco positivo de la averia enviamos un mensaje 1 al topico de la averia
    if (estado_averia && !flanco_positivo){
       flanco_positivo=true;
       client.publish(topicname,"1",true);
       digitalWrite(ledPin, HIGH);
       Serial.println("mando averia 1");
    }
    //en el flanco negativo de la averia enviamos un mensaje 1 al topico de la averia
    if (!estado_averia && flanco_positivo){
       flanco_positivo=false;
       client.publish(topicname,"0",true);
       digitalWrite(ledPin, LOW);
       Serial.println("mando averia 0");
    }        
    //Escribimos el estado de las variables por el puerto para depuracion
    char mensaje[1024];
    sprintf(mensaje,"Flanco Positivo: %d Flanco Senal: %d  Estado Averia: %d",flanco_positivo,flanco_senal,estado_averia);
    Serial.println(mensaje);     
  }else{
    //Escribimos el estado de la wifi para depuracion por el puerto serie
    Serial.println("Wifi no conectada");
  }
  //llamamos a la funcion loop del objeto mqtt, es necesario llamarla en el loop del sketch
  client.loop();
}
