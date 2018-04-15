import java.net.DatagramSocket;
import java.net.InetAddress; 
import java.net.DatagramPacket;

/* Lista de IPs de diferentes servidores NTP. 
 *	0.europe.pool.ntp.org 149.202.97.123
 *	1.europe.pool.ntp.org 193.47.166.28
 *	hora.rediris.es       130.206.3.166  
 *	1.es.pool.ntp.org     147.156.7.50   
 */ 

/**
 * @author Carlos Garcia
 */

public class Practica2{

	public static void main(String [] args){

		try{
		/*Inicializacion de los arrays de bytes*/
		byte[] peticion = new byte[48];
		byte[] respuesta = new byte[48];
		/*Rellenamos el primer byte de la peticion*/
		peticion[0] = 0x23;	
		/*Creamos los DatagramPacket */ 
		DatagramPacket D_pet = new DatagramPacket(peticion,48,InetAddress.getByName("193.47.166.28"),123);
		DatagramPacket D_resp = new DatagramPacket(respuesta,48);
		/*Creamos el DatagramSocket */ 
		DatagramSocket socket = new DatagramSocket(2048);
		/* Enviamos y recibimos los mensajes NTP */
		socket.send(D_pet);
		socket.receive(D_resp);
		/* Obtenemos los 4 bytes correspondientes a la hora [40:43] */
		long byte40 = respuesta[40] & 0xFFL;
		long byte41 = respuesta[41] & 0xFFL;
		long byte42 = respuesta[42] & 0xFFL;
		long byte43 = respuesta[43] & 0xFFL;
		/*Ahora rellenamos un long con los cuatro anteriores 
		Desplazando cada uno los bits necesarios.
		*/
		long tiempo = byte40 << 24 | byte41 << 16 | byte42 << 8 | byte43;
		/* Obtenemos las horas,minutos y segundos del día actual
			segun el UTC */
		long horas = (tiempo%86400)/3600;
		long minutos = (tiempo%3600)/60;
		long segundos = tiempo%60;
		/* Representamos la hora actual en el formato UTC y UTC+2, zona
		horaria de españa en verano*/
		System.out.println("----------------");
		System.out.println("| "+horas+" : "+minutos+" : "+segundos+" |  UTC");
		System.out.println("----------------");
		System.out.println("");
		System.out.println("----------------");
		System.out.println("| "+(horas+2)+" : "+minutos+" : "+segundos+" |  UTC+2 (Verano)");
		System.out.println("----------------");
		/* Cerramos la conexion con el servidor*/
		socket.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
}
