package Cliente;

//import java.io.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import Interfaces.ClienteInterface;
import Interfaces.ServidorInterface;

public class Cliente extends UnicastRemoteObject implements ClienteInterface{

	private static final long serialVersionUID = 1L;
	private ClienteGUI cg;
	public boolean vivo = false;
	public boolean servidorOn = false;
    //private String msgUser = "";
    private String servidor;
    public String nomeJogador;
    public int uId;
    private int porta;
    public boolean turno;
    public boolean doisJogadores = false;
    ServidorInterface servidorObj;

    public Cliente() throws RemoteException{
    	//super();
    }
    
	public Cliente(String servidor, int porta, String nomeJogador, ClienteGUI cg) throws RemoteException {
		//super();
		this.cg = cg;
		this.servidor = servidor;
        this.porta = porta;
        this.nomeJogador = nomeJogador;
	}
	
	public boolean logar(String nomeSv) throws RemoteException{
		try {
        	String nomeServidor = "//" + servidor + ":" + porta + "/" + nomeSv;
        	//String nomeCliente = "rmi://" + servidor + ":" + porta + "/" + nomeJogador;
        	//Naming.rebind(nomeCliente, this);
        	servidorObj = (ServidorInterface)Naming.lookup(nomeServidor);
        	servidorOn = true;
		} catch (MalformedURLException e) {
			servidorOn = false;
			e.printStackTrace();
		} catch (NotBoundException e) {
			servidorOn = false;
			e.printStackTrace();
		}
		try {
			novoCliente();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		if(vivo) {
			return servidorOn;			
		}
		return false;
	}
	
	public void novoCliente() throws RemoteException {
		try {
			servidorObj.passIDentity(this.ref);
			servidorObj.addCliente(nomeJogador, this.servidor, this.porta);
			vivo = true;
		} catch (Exception e) {
			vivo = false;
			e.printStackTrace();
		}
	}
	
	public void userID(int uId) throws RemoteException{
		this.uId = uId;
		if(uId == 1) {
			turno = true;
		}
		else {
			//doisJogadores = true;
		}
		cg.atualizarTela();
	}
	
	//Numero de jogadores
	public void numJogadores(int num) throws RemoteException {
		doisJogadores = num == 2 ? true : false;
	}
	
	//MSG recebida do servidor
	public void msgServidor(String nome, String msg) throws RemoteException{
		this.cg.escreverMsg(msg, nome, false);
	}
	
	//MSG enviada ao servidor
	public void enviarMsg(String msg) throws RemoteException{
		servidorObj.distribuirMsgs(uId,msg);
	}
	
	//JOGADA recebida do servidor
	public void jogadaServidor(String pecas) throws RemoteException{
		cg.pecas = pecas;
		turno = !turno;
		cg.montarPecas(pecas);
		cg.atualizarTela();
	}
	
	//JOGADA enviada ao servidor
	public void moverPeca(String pecas) throws RemoteException{
		servidorObj.jogadaPecas(uId, pecas);
	}
	
	public String movimentarPeca(String tipo, int origem) throws RemoteException {
		String retorno = "";
    	int tipoUser = Integer.parseInt(tipo);
    	// No inicio teremos posicoes[] = 1,2,0,1,2
    	String[] posicoes = cg.pecas.split(":");
    	//
    	if(tipoUser != uId) {
    		return cg.pecas;
    	}
    	if(origem == 0) {
    		if(posicoes[1].equals("0") || posicoes[2].equals("0")) {
    			//jogada possivel
    			if(posicoes[1].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[1] = tipo;
    			}
    			if(posicoes[2].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[2] = tipo;
    			}
    			turno = !turno;
    		}
    		else {
    			//jogada impossivel
    			return cg.pecas;
    		}
    	}
    	if(origem == 1) {
    		if(posicoes[0].equals("0") || posicoes[2].equals("0") || posicoes[4].equals("0")) {
    			//jogada possivel
    			if(posicoes[0].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[0] = tipo;
    			}
    			if(posicoes[2].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[2] = tipo;
    			}
    			if(posicoes[4].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[4] = tipo;
    			}
    			turno = !turno;
    		}
    		else {
    			//jogada impossivel
    			return cg.pecas;
    		}
    	}
    	if(origem == 2) {
    		if(posicoes[0].equals("0") || posicoes[1].equals("0") || posicoes[3].equals("0") || posicoes[4].equals("0")) {
    			//jogada possivel
    			if(posicoes[0].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[0] = tipo;
    			}
    			if(posicoes[1].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[1] = tipo;
    			}
    			if(posicoes[3].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[3] = tipo;
    			}
    			if(posicoes[4].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[4] = tipo;
    			}
    			turno = !turno;
    		}
    		else {
    			//jogada impossivel
    			return cg.pecas;
    		}
    	}
    	if(origem == 3) {
    		if(posicoes[2].equals("0") || posicoes[4].equals("0")) {
    			//jogada possivel
    			if(posicoes[2].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[2] = tipo;
    			}
    			if(posicoes[4].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[4] = tipo;
    			}
    			turno = !turno;
    		}
    		else {
    			//jogada impossivel
    			return cg.pecas;
    		}
    	}
    	if(origem == 4) {
    		if(posicoes[1].equals("0") || posicoes[2].equals("0") || posicoes[3].equals("0")) {
    			//jogada possivel
    			if(posicoes[1].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[1] = tipo;
    			}
    			if(posicoes[2].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[2] = tipo;
    			}
    			if(posicoes[3].equals("0")) {
    				posicoes[origem] = "0";
    				posicoes[3] = tipo;
    			}
    			turno = !turno;
    		}
    		else {
    			//jogada impossivel
    			return cg.pecas;
    		}
    	}
    	
		for(int i=0;i<4;i++) {
			retorno += posicoes[i] + ":";
		}
		retorno += posicoes[4];
		moverPeca(retorno);
		return retorno;
	}
	
	//FF recebido do servidor (0 : adv pediu ff ; 1: adv quitou)
	public void ffServidor(int tipo) throws RemoteException{
		if (tipo == 0) {
			cg.voceVenceu(0);
		} else {
			cg.voceVenceu(1);
			doisJogadores = false;
		}
	}
	
	//FF enviado ao servidor
	public void desistir(int tipo) throws RemoteException{
		if (doisJogadores) {
			servidorObj.desistencia(uId, tipo);
		}
	}

	//RR recebido do servidor, o outro jogador pediu
	public void rrServidor() throws RemoteException{
		cg.advReiniciar();
	}
	
	//RR enviado ao servidor, pedido de reinicio
	public void pedirReinicio() throws RemoteException{
		if (doisJogadores) {
			servidorObj.reinicio(uId);
		}
	}

	//RR recebido do servidor, o outro jogador aceitou
	public void accServidor() throws RemoteException{
		cg.novoJogo = true;
		cg.reiniciar();
	}
	
	//RR enviado ao servidor, aceitar o reinicio
	public void accReinicio() throws RemoteException{
		if (doisJogadores) {
			servidorObj.okRR(uId);
		}
	}	
}
