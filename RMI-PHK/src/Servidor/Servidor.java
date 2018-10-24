package Servidor;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
//import java.util.ArrayList;
import java.util.Vector;

import Interfaces.ClienteInterface;
import Interfaces.ServidorInterface;


public class Servidor extends UnicastRemoteObject implements ServidorInterface{

	private ServidorGUI sg;
	private int idUsers = 1;
	//private ArrayList<CliThread> listUsers;
	private int numUsers = 0;
	//private int porta;
	//private String servidor;
	//private int timeout;
	public boolean ativo = false;
	private static final long serialVersionUID = 1L;
	//private ArrayList<ClienteInterface> clInt;
	private Vector<Jogador> jogadores;
	ClienteInterface clienteObj;

//	public Servidor() throws RemoteException{
//		//super();
//		System.out.println("ok google");
//	};
	
	public Servidor(ServidorGUI servidorGUI) throws RemoteException{
		super();
	    sg = servidorGUI;
	    //clInt = new ArrayList<ClienteInterface>();
	    jogadores = new Vector<Jogador>(2,1);
	}
	
	public void iniciarServidor(){
		ativo = true;
		sg.alterarStatus(0);
		System.out.println("Iniciando servidor...");
	}
	
	public void finalizarServidor(){
		ativo = false;
		sg.alterarStatus(1);
		System.out.println("Finalizando servidor...");
	}
	
	public void passIDentity(RemoteRef ref) throws RemoteException {	
		//System.out.println("\n" + ref.remoteToString() + "\n");
		try{
			System.out.println("Cliente " + idUsers + ": " + ref.toString());
		}catch(Exception e){
		}
	}//end passIDentity
	
	public void addCliente(String nome, String servidor, int porta) throws RemoteException {
        try {
        	String nomeCliente = "rmi://"+ servidor + ":" + porta + "/" + nome;
            clienteObj = (ClienteInterface) Naming.lookup(nomeCliente);
            jogadores.add(new Jogador(nome, idUsers,clienteObj));
        } catch (MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }
        jogadores.get(numUsers).ci.userID(idUsers);
        //clInt.add(clienteObj);
        numUsers++;
        idUsers++;
        if (numUsers == 2) {
			jogadores.get(0).ci.numJogadores(2);
			jogadores.get(1).ci.numJogadores(2);
		}
    }
	
	public void distribuirMsgs(int id, String msg) throws RemoteException{
		if (id == 2) {
			String nome = jogadores.get(1).getNome();
			jogadores.get(0).ci.msgServidor(nome, msg);
		} else {
			String nome = jogadores.get(0).getNome();
			jogadores.get(1).ci.msgServidor(nome, msg);
		}
	}
	
	public void jogadaPecas(int id, String pecas) throws RemoteException{
		if (id == 2) {
			jogadores.get(0).ci.jogadaServidor(pecas);
		} else {
			jogadores.get(1).ci.jogadaServidor(pecas);
		}
		sg.montarPecas(pecas);
	}
	
	public void desistencia(int id, int tipo) throws RemoteException{
		if (id == 2) {
			jogadores.get(0).ci.ffServidor(tipo);
		} else {
			jogadores.get(1).ci.ffServidor(tipo);
		}
		if (tipo == 0) {
			sg.escreverEvento(jogadores.get(id - 1).getNome() + " desistiu da partida.", false);
		} else {
			sg.escreverEvento(jogadores.get(id - 1).getNome() + " saiu do jogo.", false);
		}
	}
	
	public void reinicio(int id) throws RemoteException{
		if (id == 2) {
			jogadores.get(0).ci.rrServidor();
		} else {
			jogadores.get(1).ci.rrServidor();
		}
	}

	public void okRR(int id) throws RemoteException {
		if (id == 2) {
			jogadores.get(0).ci.accServidor();
		} else {
			jogadores.get(1).ci.accServidor();
		}
		sg.escreverEvento(jogadores.get(id - 1).getNome() + " reiniciou a partida.", false);
	}
	
}
