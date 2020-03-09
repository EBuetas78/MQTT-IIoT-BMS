//<editor-fold desc="Cabecera">
/**
 * Clase para el acceso a la BBDD del sistema       
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
import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat; 
/**
 * Clase que maneja los accesos a la base de datos del programa
 * @author Eduardo Buetas Sanjuan
 */
public class BBDD_Averias {
    private String driver="org.postgresql.Driver";
    private String connectString="";
    private String user="";
    private String password ="";
    private Connection con=null;
    /**
     * Constructor de la clase de manejo de las conexiones a la BBDD sin parametros
     * con lo que se construlle la clase con los parametros por defecto 
    */
    public BBDD_Averias()
    {
        connectString = "jdbc:postgresql://localhost:5432/averias_tfm";
        user = "ebuetas";
        password = "EBuetas78";
    }
    /**
     * Constructor de la clase con los datos de conexion como parametro
     * @param _ip valor String con la ip del servidor PostgreSQL
     * @param _puerto valor Int con el puerto de conexion al servidor PostgreSQL
     * @param _BBDD valor String con el nombre de la base de datos a conectarnos
     * @param _user valor String con el usuario de conexion al PostgreSQL
     * @param _password  valor String con el password de conexion al PostgreSQL
     */
    public BBDD_Averias(String _ip, int _puerto,String _BBDD,String _user,String _password)
    {
        connectString = "jdbc:postgresql://"+_ip+":"+String.valueOf(_puerto)+"/"+_BBDD;
        user = _user;
        password = _password;
    }
    /**
     * Metodo que conecta con la base de datos para posteriormente realizar los
     * accesos a la base de datos
     * @return Valor Booleano que nos devuelve true si la conexion ha tenido exito 
     *          y false si no lo ha tenido 
     */
    public Boolean Conectar()
    {        
        Boolean respuesta=false;        
        try{
            Class.forName(driver); 
            con = DriverManager.getConnection(connectString, user , password);
            respuesta=true;
        }catch(SQLException ex){
            EscribeLog.EscribeError(ex.getMessage(), 100);             
        }catch(Exception ex){
            EscribeLog.EscribeError(ex.getMessage(), 101);             
        }
        return respuesta;
    }
    /**
     * Metodo que desconecta la base de datos 
     * @return Valor Booleano que nos devuelve true si la desconexion ha tenido exito 
     *          y false si no lo ha tenido 
     */
    public Boolean Desconectar()
    {
        Boolean respuesta=false;
        try
        {
            if (!con.isClosed())
            {
                con.close();
            }
            respuesta=true;
        }catch(SQLException ex){
            EscribeLog.EscribeError(ex.getMessage(), 110);             
        }catch(Exception ex){
            EscribeLog.EscribeError(ex.getMessage(), 111);             
        }
        return respuesta;
        
    }
    /**
     * Metodo que rellena los datos de la averia a partir de los datos obtenidos 
     * a traves del topic de un mensaje dado. A partir del topic tenemos los datos 
     * Area, Subarea, Sistema, Zona, Elemento y Nombre de la averia los cuales 
     * identifican una averia de manera univoca, a partir de esta identificacion 
     * se leen de la base de datos los datos restantes de la averia, como son id_averia,
     * prioridad, mensaje, descripcion, actuacion, id_tipo_averia.
     * @param dato objeto del tipo Datos_m_averias, que debe contener los datos para 
     *              identificar la averia de manera unica y en el que se rellenaran los 
     *              demas datos de la averia. 
     * @return Valor Booleano que nos devuelve true si el metodo ha tenido exito 
     *          y false si no lo ha tenido 
     */
    public boolean DameAveria(Datos_m_averias dato){
        boolean respuesta=false;
        PreparedStatement  select_Averias=null;     
        try{                   
            String cadenasql="";        
            cadenasql="SELECT id_averia,prioridad,mensaje,descripcion,actuacion,id_tipo_averia from m_averias where area=? and subarea=? and "+
                  "sistema=? and zona=? and elemento=? and nombre=?;";            
            select_Averias=con.prepareStatement(cadenasql);
            select_Averias.setString(1,dato.getArea());
            select_Averias.setString(2,dato.getSubarea());
            select_Averias.setString(3,dato.getSistema());
            select_Averias.setString(4,dato.getZona());
            select_Averias.setString(5,dato.getElemento());
            select_Averias.setString(6,dato.getNombre());
            ResultSet rs=select_Averias.executeQuery();
            if (rs.next()==true)
            {
               dato.setId_averia(rs.getLong("id_averia"));
               dato.setPrioridad(rs.getInt("prioridad"));
               dato.setMensaje(rs.getString("mensaje"));
               dato.setDescripcion(rs.getString("descripcion"));
               dato.setActuacion(rs.getString("actuacion"));
               dato.setId_tipo_averia(rs.getLong("id_tipo_averia"));
               respuesta=true;
            }            
           
            rs.close();
        }catch(SQLException ex){
            EscribeLog.EscribeError(ex.getMessage(), 121);               
        }catch(Exception ex){
            EscribeLog.EscribeError(ex.getMessage(), 122);             
        
        }finally{
            if (select_Averias!=null){                
                try{
                    if (!select_Averias.isClosed()){
                        select_Averias.close();
                    }
                }catch(SQLException ex){
                    EscribeLog.EscribeError(ex.getMessage(), 123);             
                }
            }
        }
        
        return respuesta;
    }
    /**
     * Funcion que crea un inicio de una averia, creando un registro en la tabla averias_abiertas
     * con el id_averia correspondiente, automaticamente por medio de una funcion de trigger en el insert 
     * de la tabla rellenara el sello de tiempo del inicio de la averia. 
     * Si la averia ya esta iniciada y no finalizada (existe un registro para esta averia en averias_abiertas
     * la funcion no realiza ninguna accion)
     * @param dato objeto de la clase Datos_m_averias con los datos de la averia a iniciar.
     * @return Valor Booleano que nos devuelve true si el metodo ha tenido exito 
     *          y false si no lo ha tenido 
     */
    public boolean InicioAveria (Datos_m_averias dato){
        boolean respuesta=false;
        boolean encontrada=false;
        PreparedStatement  Insert_Averias_Abiertas=null;   
        PreparedStatement  Select_Averias_Abiertas=null;
        try{                   
            String cadenasql="";        
            cadenasql="SELECT count(*) as contador from averias_abiertas where id_averia=?";
            Select_Averias_Abiertas=con.prepareStatement(cadenasql);
            Select_Averias_Abiertas.setLong(1,dato.getId_averia());            
            ResultSet rs=Select_Averias_Abiertas.executeQuery();
            if (rs.next()==true){
                if (rs.getLong("contador")>0){
                    encontrada=true;
                }
            }
            if (!encontrada)
            {
                cadenasql="INSERT INTO averias_abiertas (id_averia) values (?)";            
                Insert_Averias_Abiertas=con.prepareStatement(cadenasql);
                Insert_Averias_Abiertas.setLong(1,dato.getId_averia());            
                Insert_Averias_Abiertas.execute();                        
                respuesta=true;
            }
        }catch(SQLException ex){
            EscribeLog.EscribeError(ex.getMessage(), 131);             
        }catch(Exception ex){
            EscribeLog.EscribeError(ex.getMessage(), 132);             
        
        }finally{
            if (Insert_Averias_Abiertas!=null){                
                try{
                    if (!Insert_Averias_Abiertas.isClosed()){
                        Insert_Averias_Abiertas.close();
                    }
                }catch(SQLException ex){
                    EscribeLog.EscribeError(ex.getMessage(), 133);             
                }
            }
            if (Select_Averias_Abiertas!=null){                
                try{
                    if (!Select_Averias_Abiertas.isClosed()){
                        Select_Averias_Abiertas.close();
                    }
                }catch(SQLException ex){
                    EscribeLog.EscribeError(ex.getMessage(), 134);             
                }
            }
        }
        
        return respuesta;
    }
    /**
     * Funcion que rellena el momento del acuse de recibo por parte de un tecnico y el id que 
     * identifica a dicho tecnico en la tabla de averias abiertas
     * @param dato objeto de la clase Datos_m_averias con los datos de la averia a acusar
     * @param equipo nombre del equipo que envia el acuse, a partir de este equipo se conocera el tecnico 
     *               que acusa la averia
     * @return Valor Booleano que nos devuelve true si el metodo ha tenido exito 
     *          y false si no lo ha tenido
     */
    public boolean AcuseAveria (Datos_m_averias dato,String equipo){
        boolean respuesta=false;
        PreparedStatement  Update_Averias_Abiertas=null;     
        try{                   
            String cadenasql="";    
           
            cadenasql="UPDATE averias_abiertas SET ts_acuse=?,id_tecnico_acusa=(select id_tecnico from m_receptores_averias where topic_receptor=?) where id_averia=?";
                                                                                 
            Update_Averias_Abiertas=con.prepareStatement(cadenasql);
            Update_Averias_Abiertas.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            Update_Averias_Abiertas.setString(2, equipo);
            Update_Averias_Abiertas.setLong(3, dato.getId_averia());            
            Update_Averias_Abiertas.execute();                      
            respuesta=true;
        }catch(SQLException ex){
            EscribeLog.EscribeError(ex.getMessage(), 141);             
        }catch(Exception ex){
            EscribeLog.EscribeError(ex.getMessage(), 142);             
        
        }finally{
            if (Update_Averias_Abiertas!=null){                
                try{
                    if (!Update_Averias_Abiertas.isClosed()){
                        Update_Averias_Abiertas.close();
                    }
                }catch(SQLException ex){
                    EscribeLog.EscribeError(ex.getMessage(), 143);             
                }
            }
        }
        
        return respuesta;
    }       
    /**
     * Esta funcion cierra la averia, esto quiere decir que crea un nuevo registro con los datos 
     * de la averia abierta (sello de tiempo de inicio, acuse, tecnico que acusa) y anade el sello de tiempo
     * del final de la averia por medio de un trigger en el insert, en la tabla de averias_cerradas y borra 
     * la correspondiente averia de la tabla averias_abiertas
     * @param dato objeto de la clase Datos_m_averias que contiene los datos de la averia a cerrar
     * @return Valor Booleano que nos devuelve true si el metodo ha tenido exito 
     *          y false si no lo ha tenido
     */
    public boolean CierraAveria (Datos_m_averias dato){
        boolean respuesta=false;
        PreparedStatement  Insert_Averias_Cerradas=null;    
        PreparedStatement  Delete_Averias_Abiertas=null;   
        try{                   
            String cadenasql="";    
           
            cadenasql="INSERT INTO averias_cerradas (id_averia,ts_inicio,ts_acuse,id_tecnico_acusa) values (?,"+
                      "(SELECT ts_inicio FROM averias_abiertas where id_averia=?),"+
                      "(SELECT ts_acuse FROM averias_abiertas where id_averia=?),"+
                      "(SELECT id_tecnico_acusa FROM averias_abiertas where id_averia=?))";
                                                                                 
            Insert_Averias_Cerradas=con.prepareStatement(cadenasql);
            Insert_Averias_Cerradas.setLong(1, dato.getId_averia());
            Insert_Averias_Cerradas.setLong(2, dato.getId_averia());
            Insert_Averias_Cerradas.setLong(3, dato.getId_averia());
            Insert_Averias_Cerradas.setLong(4, dato.getId_averia());                                    
            Insert_Averias_Cerradas.execute();                        
            cadenasql="DELETE FROM averias_abiertas where id_averia=?";
            Delete_Averias_Abiertas=con.prepareStatement(cadenasql);
            Delete_Averias_Abiertas.setLong(1, dato.getId_averia());
            Delete_Averias_Abiertas.execute();
            respuesta=true;
        }catch(SQLException ex){
            EscribeLog.EscribeError(ex.getMessage(), 151);                 
        }catch(Exception ex){
            EscribeLog.EscribeError(ex.getMessage(), 152);
        
        }finally{
            if (Insert_Averias_Cerradas!=null){                
                try{
                    if (!Insert_Averias_Cerradas.isClosed()){
                        Insert_Averias_Cerradas.close();
                    }
                }catch(SQLException ex){
                    EscribeLog.EscribeError(ex.getMessage(), 153);
                }
            }
            if (Delete_Averias_Abiertas!=null){                
                try{
                    if (!Delete_Averias_Abiertas.isClosed()){
                        Delete_Averias_Abiertas.close();
                    }
                }catch(SQLException ex){
                    EscribeLog.EscribeError(ex.getMessage(), 154);
                }
            }            
        }
        
        return respuesta;
    }          
}
