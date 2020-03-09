//<editor-fold desc="Cabecera">
/**
 * Clase contenedor de los registros de la tabla m_averias
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

/**
 * Clase contenedor de los registros de la tabla m_averias
 */
public class Averias {
    //<editor-fold desc="variables">
    /**
     * Variables globales de la clase
     */
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
    private int estado=0;
    private String hora="";
    //</editor-fold>
    //<editor-fold desc="constructores">
    /**
     * Constructor de la clase sin parametros, para rellenar posteriormente a su creacion
     */
    public Averias(){

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
     * @param _estado valor entero del estado en el que se encuentra la averia 0-Fin Averia 1-Inicio Averia 2-Averia Acusada
     */
    public Averias(String _area, String _subarea, String _sistema, String _zona, String _elemento, String _nombre,int _estado, String _hora){
        area=_area;
        subarea=_subarea;
        sistema=_sistema;
        zona=_zona;
        elemento=_elemento;
        nombre=_nombre;
        estado=_estado;
        hora=_hora;
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

    /**
     * Funcion que devuelve el estado de la averia
     * @return valor int de estado
     */
    public int getEstado(){return estado; }
    /**
     * Funcion que devuelve la hora de la averia
     * @return valor int de hora
     */
    public String getHora(){return hora; }

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

    /**
     * Metodo que modifica el valor de estado
     * @param _estado valor entero para modificar el valor de estado
     */
    public void setEstado(int _estado) {  estado = _estado;    }

    /**
     * Metodo que modifica el valor de hora
     * @param _hora valor String para modificar el valor de hora
     */
    public void setHora(String _hora) {  hora = _hora;    }
    //</editor-fold>

    /**
     * Sobreescribimos el metodo equals para comparar si una averia es igual a otra, unicamente comparando
     * los elementos: Area, subarea, sistema, zona, elemento y nombre para saber si dos objetos averia se refieren
     * a la misma averia
     * @param obj objeto del tipo Averias para la comparacion con esta averia
     * @return devolvera true si se refieren a la misma averia y false de lo contrario
     */
    @Override
    public boolean equals(Object obj) {
        boolean respuesta=false;
        Averias averia_cmp=(Averias) obj;
        if (this.area.equals(averia_cmp.getArea()) && this.subarea.equals(averia_cmp.getSubarea()) &&
             this.sistema.equals(averia_cmp.getSistema()) && this.zona.equals(averia_cmp.getZona()) &&
             this.elemento.equals(averia_cmp.getElemento()) && this.nombre.equals(averia_cmp.getNombre())){
            respuesta=true;
        }

        return respuesta;
    }

    /**
     * Sobreescribimos el metodo toString para sacar la cadena con los datos de la averia en el
     * formato Averia nombre_averia en area/subarea/sistema/zona/elemento Estado: estado_averia
     * @return devuelve el string con los datos de la averia
     */
    @Override
    public String toString(){
        String estadostr="";
        switch (estado){
            case 0:
                estadostr="Finalizada";
                break;
            case 1:
                estadostr="Iniciada";
                break;
            case 2:
                estadostr="Acusada";
                break;
            default:
                estadostr="????";
                break;
        }
        return "Averia "+nombre +" en " + area+"/"+subarea+"/"+sistema+"/"+zona+"/"+elemento+" Estado:"+estadostr;

    }
}
