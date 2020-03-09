//<editor-fold desc="Cabecera">
/**
 * En esta clase se codificaran las diferentes funciones auxiliares que se van a utilizar
 * a lo largo de la aplicacion, emision de sonidos y vibraciones
 * (success_wav, fail_wav y beep_wav), gestion de notificaciones
 * (notification, createNotificationChanel y BorrarNotificaciones) y una funcion mas para
 * mostrar mensajes en la aplicacion MostrarAlert.
 * Todas estas funciones estan definidas como estaticas en la clase para poder ser utilizadas
 * directamente sin necesidad de crear un objeto de la clase para su utilizacion.
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
import android.app.NotificationChannel;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.util.Log;
import android.app.NotificationManager;
import android.content.Intent;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;

import android.os.Build;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

/**
 * En esta clase se codificaran las diferentes funciones auxiliares que se van a utilizar
 * a lo largo de la aplicacion, emision de sonidos y vibraciones
 * (success_wav, fail_wav y beep_wav), gestion de notificaciones
 * (notification, createNotificationChanel y BorrarNotificaciones) y una funcion mas para
 * mostrar mensajes en la aplicacion MostrarAlert.
 * Todas estas funciones estan definidas como estaticas en la clase para poder ser utilizadas
 * directamente sin necesidad de crear un objeto de la clase para su utilizacion.
 */
public class FuncAux {
    public static final String tag="FuncAux";
    private static int MessageID = 0;

    /**
     * Funcion que reproduce el sonido success.wav y realiza una vibracion de 200 ms
     * @param activity actividad desde donde se lanza la funcion
     */
    public static void success_wav(Activity activity){
        try{
            Vibrator mVibrator;
            SoundPool mSoundPool=new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            int mSuccessId= mSoundPool.load(activity, R.raw.success, 1);
            Thread.sleep(200);
            mSoundPool.play(mSuccessId, 1, 1, 0, 0, 1);
            mVibrator = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);
            mVibrator.vibrate(200);
        }catch(Exception ex){
            Log.e(tag, "Exception beep "+ex.getMessage());
        }
    }
    /**
     * Funcion que reproduce el sonido fail.wav y realiza una vibracion de 500 ms
     * @param activity actividad desde donde se lanza la funcion
     */
    public static void fail_wav(Activity activity){
        try{
            Vibrator mVibrator;
            SoundPool mSoundPool=new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            int mSuccessId= mSoundPool.load(activity, R.raw.fail, 1);
            Thread.sleep(200);
            mSoundPool.play(mSuccessId, 1, 1, 0, 0, 1);
            mVibrator = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);
            mVibrator.vibrate(500);
        }catch(Exception ex){
            Log.e(tag, "Exception beep "+ex.getMessage());
        }
    }
    /**
     * Funcion que reproduce el sonido beep.wav y realiza una vibracion de 200 ms
     * @param activity actividad desde donde se lanza la funcion
     */
    public static void beep_wav(Activity activity){
        try{
            Vibrator mVibrator;
            SoundPool mSoundPool=new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            int mSuccessId= mSoundPool.load(activity, R.raw.beep, 1);
            Thread.sleep(200);
            mSoundPool.play(mSuccessId, 1, 1, 0, 0, 1);
            mVibrator = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);
            mVibrator.vibrate(200);
        }catch(Exception ex){
            Log.e(tag, "Exception beep "+ex.getMessage());
        }
    }
    /**
     * Muestra una notificacion en el area de notificaciones
     * @param activity Activity que lanza la notificacion
     * @param texto_notificacion Texto de la notificacion
     * @param titulo Titulo de la notificacion
     */
    static void notifcation(Activity activity, String texto_notificacion, String titulo) {

        NotificationCompat.Builder mBuilder;
        NotificationManager mNotifyMgr =(NotificationManager) activity.getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        int icono = R.mipmap.ic_launcher;
        Intent i=new Intent(activity, PrincipalActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, i, 0);

        mBuilder =new NotificationCompat.Builder(activity.getApplicationContext(),"CanalAveriasI40Id")
                .setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setContentTitle(titulo)
                .setContentText(texto_notificacion)
                .setVibrate(new long[] {100, 250, 100, 500})
                .setAutoCancel(true);



        mNotifyMgr.notify(MessageID, mBuilder.build());
        MessageID++;

    }

    /**
     * Funcion que crea el canal de notificaciones para enviar las notificaciones a la zona
     * de notificaciones
     * @param activity actividad desde donde se crea el canal de notificacion
     */
    public static void createNotificationChannel(Activity activity) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "AveriasI40";
            String description = "Canal de notificaciones AveriasI40";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CanalAveriasI40Id", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Funcion para mostrar una ventana con una alerta
     * @param contexto actividad desde donde se lanza la alerta
     * @param texto texto de la alerta
     */
    public static void MostrarAlert(Context contexto,String texto)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setMessage(texto);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();

    }

    /**
     * Borra las notificaciones, cuando ponemos en primer plano la app borramos las notificaciones
     * @param activity actividad desde donde se lanza el borrado de las notificaciones
     */
    public static void BorrarNotificaciones(Activity activity){
        NotificationManager mNotifyMgr =(NotificationManager) activity.getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        for (int i=0;i<MessageID;i++){
            mNotifyMgr.cancel(i);
        }
        MessageID=0;
    }

}
