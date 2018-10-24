package Servidor;

import Interfaces.ClienteInterface;

public class Jogador {
	public String nomeJogador;
	public int idJogador;
	public ClienteInterface ci;
	
	public Jogador(String nomeJogador, int idJogador, ClienteInterface ci) {
		this.nomeJogador = nomeJogador;
		this.idJogador = idJogador;
		this.ci = ci;
	}
	
	public String getNome() {
		return nomeJogador;
	}
	
	public int getId() {
		return idJogador;
	}

	public ClienteInterface getCliente() {
		return ci;
	}
}
