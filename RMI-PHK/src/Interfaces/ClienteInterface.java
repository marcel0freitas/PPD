package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClienteInterface extends Remote {
	void userID(int uId) throws RemoteException;
	void numJogadores(int num) throws RemoteException;
	void msgServidor(String nome, String msg) throws RemoteException;
	void jogadaServidor(String pecas) throws RemoteException;
	void ffServidor(int tipo) throws RemoteException;
	void rrServidor() throws RemoteException;
	void accServidor() throws RemoteException;
}
