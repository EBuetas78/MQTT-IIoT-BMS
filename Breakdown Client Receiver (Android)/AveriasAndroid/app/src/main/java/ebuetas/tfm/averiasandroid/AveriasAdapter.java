//<editor-fold desc="Cabecera">
/**
 * Clase que hereda de BaseAdapter y que implementa el adaptador
 * para cada registro mostrado en el ListView de la actividad principal
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
package ebuetas.tfm.averiasandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
/**
 * Clase para hereda de BaseAdapter y que implementa el adaptador
 * para cada registro mostrado en el ListView de la actividad principal
 * @author Eduardo Buetas
 */
class AveriasAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Averias> items;
    private float x_inicial=0;
    private float x_final=0;

    /**
     * Constructor de la clase
     * @param activity Actividad desde la que se genera el adaptador
     * @param items items para construir el adaptador
     */
    public AveriasAdapter (Activity activity, ArrayList<Averias> items) {
        this.activity = activity;
        this.items = items;
    }

    /**
     * Sobreescribe la funcion getCount
     * @return int devuelve el numero de items que rellenan el adaptador
     */
    @Override
    public int getCount() {
        return items.size();
    }

    /**
     * Sobreescribe la funcion getItem que nos devuelve un item según el indice que le pasamos
     * @param arg0 indice del item que queremos que nos devuelva
     * @return Object es el objeto con el indice dado
     */
    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    /**
     * Sobreescribe la funcion getItemId, no la utilizamos y devuelve el parametro pasado
     * @param position posicion del item del que queremos que nos devuelva el id
     * @return no implementamos esta funcion asi que devolvemos directamente el parametro pasado
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Devuleve la vista del adaptador segun el layout codificado en R.layout.item_aveiras
     * @param position posicion en el array de items
     * @param convertView Vista donde se va ha mostrar el item
     * @param parent Vista padre de la vista donde se va ha mostrar el item (en nuestro caso no se usa)
     * @return devuelve la vista rellenada con el item correpondiente
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_averias, null);
        }

        Averias dir = items.get(position);

        RelativeLayout layout=(RelativeLayout) v.findViewById(R.id.rlItemsAverias);

        TextView lbllocalizacion = (TextView) v.findViewById(R.id.lblLocalizacion);
        lbllocalizacion.setText("Area/Subarea: "+dir.getArea()+"/"+dir.getSubarea()+" Sistema:"+dir.getSistema());

        TextView lblaveria= (TextView) v.findViewById(R.id.lblAveria);
        lblaveria.setText(" Zona/Elemento:"+dir.getZona()+"/"+dir.getElemento()+" Averia:"+dir.getNombre());

        TextView lblhora= (TextView) v.findViewById(R.id.lblHora);
        lblhora.setText(dir.getHora());

        switch (dir.getEstado()){
            case 0: layout.setBackgroundColor(Color.parseColor("#00FF00")); //verde averia resuelta
                    break;
            case 1: layout.setBackgroundColor(Color.parseColor("#FF0000")); //rojo averia iniciada
                    break;
            case 2: layout.setBackgroundColor(Color.parseColor("#FFFF00")); //amarillo averia acusada
                    break;
            default: layout.setBackgroundColor(Color.parseColor("#FF0000")); //rojo cualquier otro valor
                     break;

        }

        return v;
    }
}
