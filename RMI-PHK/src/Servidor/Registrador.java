package Servidor;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;

public class Registrador {
	public static void main(String[] args) throws RemoteException, MalformedURLException{
		int portaServidor = 9090;
		String nomeServidor = "localhost";
		try{
            String porta = JOptionPane.showInputDialog("Porta: ");
            portaServidor = Integer.parseInt(porta);
            Registry registry = LocateRegistry.createRegistry(portaServidor);
            ServidorGUI sg = new ServidorGUI();
            Servidor sv = new Servidor(sg);
            String servidor = JOptionPane.showInputDialog("Servidor: ");
            nomeServidor = servidor;
            sg.setVisible(true);
            registry.rebind("//" + nomeServidor + ":" + portaServidor + "/PHKrmi", sv);
            System.out.println("Servidor Registrado!");
        }catch(Exception e){
            System.out.println("Erro de porta: " + e.getMessage());
        } 
	}
}
