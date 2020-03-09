//<editor-fold desc="Cabecera">
/**
 * Clase contenedor de los registros de la             
 *       tabla m_averias   
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
package EBuetas.TFM.BBDD;

import EBuetas.TFM.EscribeLog;

/**
 * Clase para contener los datos de las averias, sacados de la tabla m_averias
 * @author Eduardo Buetas 
 */
public class Datos_m_averias {
    //<editor-fold desc="variables">
    private long id_averia=0;
    private String area="";
    private String subarea="";
    private String sistema="";
    private String zona="";
    private String elemento="";
    private int prioridad=0;
    private String  nombre ="";
    private String mensaje="";
    private String descripcion="";
    private String actuacion="";
    private long id_tipo_averia=0;
    //</editor-fold>
    //<editor-fold desc="constructores">
    /**
     * Constructor de la clase sin parametros, para rellenar posteriormente a su creacion
     */
    public Datos_m_averias(){
        
    }
    /**
     * Constructor de la clase con parametros para rellenar los datos de identificacion de 
     * la averia en el propio constructor
     * @param _area valor String con el area de la averia
     * @param _subarea valor String con el subarea de la averia
     * @param _sistema valor String con el sistema de la averia
     * @param _zona valor String con la zona de la averia
     * @param _elemento valor String con el elemento de la averia
     * @param _nombre  valor String con el nombre de la averia
     */
    public Datos_m_averias(String _area, String _subarea, String _sistema, String _zona, String _elemento, String _nombre){
        area=_area;
        subarea=_subarea;
        sistema=_sistema;
        zona=_zona;
        elemento=_elemento;
        nombre=_nombre;        
    }
    //</editor-fold>
    //<editor-fold desc="GET">
    /**
     * Funcion que devuelve el valor de id_averia
     * @return valor long de id_averia
     */
    public long getId_averia(){
        return id_averia;
    }
    /**
     * Funcion que devuelve el valor de area
     * @return valor String de area
     */
    public String getArea(){
        return area;
    }
    /**
     * Funcion que devuelve el valor de subarea
     * @return valor String de subarea
     */
     public String getSubarea(){
        return subarea;
    }
     /**
     * Funcion que devuelve el valor de sistema
     * @return valor String de sistema
     */
    public String getSistema(){
        return sistema;
    }
    /**
     * Funcion que devuelve el valor de zona
     * @return valor String de zona
     */
    public String getZona(){
        return zona;
    }
    /**
     * Funcion que devuelve el valor de elemento
     * @return valor String de elemento
     */
    public String getElemento(){
        return elemento;
    }
    /**
     * Funcion que devuelve el valor de prioridad
     * @return valor Int de prioridad
     */
    public int getPrioridad(){
        return prioridad;
    }
    /**
     * Funcion que devuelve el valor de nombre
     * @return valor String de nombre
     */
    public String getNombre(){
        return nombre;
    }
    /**
     * Funcion que devuelve el valor de mensaje
     * @return valor String de mensaje
     */
    public String getMensaje(){
        return mensaje;
    }
    /**
     * Funcion que devuelve el valor de descripcion
     * @return valor String de descripcion
     */
    public String getDescripcion(){
        return descripcion;
    }
    /**
     * Funcion que devuelve el valor de actuacion
     * @return valor String de actuacion
     */
    public String getActuacion(){
        return actuacion;
    }
    /**
     * Funcion que devuelve el valor de id_tipo_averia
     * @return valor long de id_tipo_averia
     */
    public long getId_tipo_averia(){
        return id_tipo_averia;
    }
    //</editor-fold>
    //<editor-fold desc="SET">
    /**
     * Metodo que modifica el valor de area
     * @param _area valor String para modifciar el valor de area
     */
    public void setArea(String _area){
        area=_area;
    }
    /**
     * Metodo que modifica el valor de subarea
     * @param _subarea valor String para modifciar el valor de subarea
     */
    public void setSubarea(String _subarea){
        subarea=_subarea;
    }
    /**
     * Metodo que modifica el valor de sistema
     * @param _sistema valor String para modifciar el valor de sistema
     */
    public void setSistema(String _sistema){
        sistema=_sistema;
    }
    /**
     * Metodo que modifica el valor de zona
     * @param _zona valor String para modifciar el valor de zona
     */
    public void setZona(String _zona){
        zona=_zona;
    }
    /**
     * Metodo que modifica el valor de elemento
     * @param _elemento valor String para modifciar el valor de elemento
     */
    public void setElemento(String _elemento){
        elemento=_elemento;
    }
    /**
     * Metodo que modifica el valor de nombre
     * @param _nombre valor String para modifciar el valor de nombre
     */
    public void setNombre(String _nombre){
        nombre=_nombre;
    }
    /**
     * Metodo que modifica el valor de id_averia
     * @param _id_averia valor Long para modifciar el valor de id_averia
     */
    public void setId_averia(long _id_averia){
        id_averia=_id_averia;
    }
    /**
     * Metodo que modifica el valor de prioridad
     * @param _prioridad valor Int para modifciar el valor de prioridad
     */
    public void setPrioridad(int _prioridad){
        prioridad=_prioridad;
    }
    /**
     * Metodo que modifica el valor de mensaje
     * @param _mensaje valor String para modifciar el valor de mensaje
     */
    public void setMensaje(String _mensaje){
        mensaje=_mensaje;
    }
    /**
     * Metodo que modifica el valor de descripcion
     * @param _descripcion valor String para modifciar el valor de descripcion
     */
    public void setDescripcion(String _descripcion){
        descripcion=_descripcion;
    }
    /**
     * Metodo que modifica el valor de actuacion
     * @param _actuacion valor String para modifciar el valor de actuacion
     */
    public void setActuacion(String _actuacion){
        actuacion=_actuacion;
    }
    /**
     * Metodo que modifica el valor de id_tipo_averia
     * @param _id_tipo_averia valor String para modifciar el valor de id_tipo_averia
     */
    public void setId_tipo_averia(long _id_tipo_averia){
        id_tipo_averia=_id_tipo_averia;
    }
    //</editor-fold>
    /**
     * Este metodo lee los datos de la averia a partir de los datos obtenidos 
     * del topico de un mensaje MQTT, rellenando el resto de datos en el propio objeto 
     * de la clase
     * @return Valor Booleano que nos devuelve true si el metodo ha tenido exito 
     *          y false si no lo ha tenido
     */
    public boolean LeeDatos()
    {
        boolean respuesta = false;
        BBDD_Averias base_datos=null;
        try{
            base_datos=new BBDD_Averias();
            if (base_datos.Conectar()){
                if (base_datos.DameAveria(this)){
                    respuesta=true;
                }
            }
        }catch (Exception ex){
           EscribeLog.EscribeError(ex.getMessage(), 200);
        }finally{
            if (base_datos!=null){
                base_datos.Desconectar();
            }            
        }
        return respuesta;
    }
}