package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;

public interface ServidorInterface extends Remote{
	void addCliente(String nome, String servidor, int porta) throws RemoteException;
	void distribuirMsgs(int id, String msg) throws RemoteException;
	void jogadaPecas(int id, String pecas) throws RemoteException;
	void desistencia(int id, int tipo) throws RemoteException;
	void reinicio(int id) throws RemoteException;
	void passIDentity(RemoteRef ref) throws RemoteException;
	void okRR(int id) throws RemoteException;
}
