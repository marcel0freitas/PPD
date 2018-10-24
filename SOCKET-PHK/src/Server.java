import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//import java.util.ArrayList;

public class Server{

	//Manter string pecas caso queira deixar o servidor ciente da posição das peças e
	//até mesmo replicar na GUI do servidor como está a partida: "transmissão ao vivo"
	//private String pecas = "1:2:0:1:2";
	private ServerGUI sg;
	private int idUsers = 1;
	private ArrayList<CliThread> listUsers;
	private ServerSocket ss;
	private int numUsers = 0;
	private int porta;
	private String servidor;
	private int timeout;
	public boolean ativo = false;
	
	public Server(String servidor, int porta, int timeout, ServerGUI serverGUI) {
		this.servidor = servidor;
		this.porta = porta;
		this.timeout = timeout;
		this.sg = serverGUI;
		listUsers = new ArrayList<CliThread>();
	}

	public void iniciarServidor() {
		ativo = true;
		//sg.escreverEvento(" aguardando conexões...", false);
		System.out.println("Servidor iniciado na porta " + porta + ". Aguardando conexoes...");
		try {
			ss = new ServerSocket(porta);
			ss.setSoTimeout(timeout*1000);
			if (!ss.isBound()){
				ss.bind(new InetSocketAddress(servidor, porta));
			}
			InetAddress inet = ss.getInetAddress();
			System.out.println("Endereço do servidor : "+ inet.getHostAddress());
			System.out.println("Nome do servidor : "+ inet.getHostName());
			while(ativo){
				//
				if(numUsers < 2) {
					Socket s = ss.accept();
					System.out.println("Jogador " + idUsers + " conectado (IP: "+ s.getInetAddress().getHostAddress() + ")");
					CliThread ct = new CliThread(s);
					listUsers.add(ct);
					if(!ct.isAlive()) {
						ct.start();
					}
				}
				if(numUsers == 2) {
					break;
				}
				//
				if(!ativo) {
					break;
				}
			}
		}catch(Exception e3) {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			sg.escreverEvento(" tempo de espera (timeout) atingido. Servidor OFFLINE.",false);
			sg.alterarStatus(1);
			ativo = false;
			//sg.dispose();
			System.out.println("Exceção E3: " + e3.getMessage());
		}
	}
	
	public void finalizarServidor() {
		ativo = false;
		System.out.println("Servidor finalizado.");
		if(!ativo) {
			try {
				distribuirMsgs("Q:1");
				distribuirMsgs("Q:2");
				ss.close();
				for (int i = 0; i < listUsers.size(); i++) {
					CliThread ct = listUsers.get(i);
					try {
						ct.istream.close();
						ct.ostream.close();
						ct.s.close();
					} catch (Exception e1) {
						sg.escreverEvento(" " + e1.getMessage(), false);
						System.out.println("Erro E1: " + e1.getMessage());
					}
				}
			} catch (Exception e2) {
				sg.escreverEvento(" " + e2.getMessage(), false);
				System.out.println("Erro E2: " + e2.getMessage());
			} 
		}
	}
	
	//Servidor trata pra quem deve ser enviada a msg
	private void distribuirMsgs(String msg) {
		int userOrig;
		try {
			String[] opcao = msg.split(":");
			CliThread ct1 = null, ct2 = null;
			if(listUsers.size() == 1) {
				ct1 = listUsers.get(0);
			}
			if(listUsers.size() == 2) {
				ct1 = listUsers.get(0);
				ct2 = listUsers.get(1);
			}
			if(opcao[0].equals("I")) {
				if(idUsers == 2) {
					ct1.enviarMsg("I:1");
				}
				if(idUsers == 3) {
					ct2.enviarMsg("I:2");
					ct1.enviarMsg("X:1");
					ct2.enviarMsg("X:2");
				}
			}
			else {
				userOrig = Integer.parseInt(opcao[1]);
				//Enviar conteudo para user 2
				if(userOrig == 1) {
					ct2.enviarMsg(msg);
				}
				//Enviar conteudo para user 1
				else {
					ct1.enviarMsg(msg);
				}
			}
		}catch(Exception e4) {
			//Quando um jogador envia msg sem nunca ter conectado outro jogador
			sg.escreverEvento(" " + e4.getMessage(),false);
			System.out.println("Erro E4: " + e4.getMessage());
		}
	}
	
	private void desistencia(String cliente) {
		sg.escreverEvento(" jogador " + cliente + " desistiu.",false);
		if(cliente.equals("1")) {
			
		}
		else {
			
		}
	}
	
	private void reinicio(String cliente) {
		sg.escreverEvento(" jogador " + cliente + " pediu reinicio de partida.",false);
		if(cliente.equals("1")) {
			
		}
		else {
			
		}
	}	
	
	// THREAD dos clientes :
	
	class CliThread extends Thread {
		DataOutputStream ostream;
		DataInputStream istream;
		Socket s;
		String msg;
		int uId;

		public CliThread(Socket s) {
			uId = idUsers;
			idUsers++;
			numUsers++;
			this.s = s;
			try
			{
				ostream = new DataOutputStream(s.getOutputStream());
				istream  = new DataInputStream(s.getInputStream());
				sg.escreverEvento(" jogador " + uId + " conectado.", false);
				System.out.println("Jogador " + uId + " conectado.");
			}
			catch (IOException ioe1) {
				sg.escreverEvento(" não foi possível criar as streams do cliente " + uId + ": " + ioe1, false);
				System.out.println("Erro IOE1: " + ioe1.getMessage());
				return;
			}
		}
		
		public void run() {
			boolean ativo = true;
			while(ativo) {
				try {
					msg = istream.readUTF();
					// Tratando o conteúdo das msgs:
					// I:0: quer dizer que um jogador solicitou id
					// M:1:msg quer dizer que cl.1 mandou msg via chat
					// J:2:1:2:2:0:1 quer dizer que cl.2 fez alguma jogada
					// D:1 quer dizer que cl.1 desistiu
					// R:2 quer dizer que cl.2 quer reiniciar
					// N:1 quer dizer que o jogador 1 autorizou novo jogo
					String[] opcao = msg.split(":");
					if(opcao[0].equals("I")) {
						distribuirMsgs(msg);
					}
					if(opcao[0].equals("M")) {
						distribuirMsgs(msg);
					}
					if(opcao[0].equals("J")) {
						distribuirMsgs(msg);
					}
					if(opcao[0].equals("D")) {
						desistencia(opcao[1]);
						distribuirMsgs(msg);
					}
					if(opcao[0].equals("R")) {
						reinicio(opcao[1]);
						distribuirMsgs(msg);
					}
					if(opcao[0].equals("N")) {
						distribuirMsgs(msg);
					}
					if(opcao[0].equals("S")) {
						distribuirMsgs(msg);
					}
				}catch (Exception me1) {
					//Quando um jogador fecha o jogo
					//sg.escreverEvento(" " + me1.getMessage(), false);
					System.out.println("Erro ME1: " + me1.getMessage());
					break;
				}
			}
		}
		
		//Server -> Cliente
		private void enviarMsg(String msg) {
			if(!s.isConnected()) {
				desconectarCliente();
			}
			try {
				String[] opcao = msg.split(":");
				String msgEnviada = "";
				if(opcao[0].equals("M")) {
					msgEnviada = "M:" + msg.substring(4);
				}
				if(opcao[0].equals("I")) {
					msgEnviada = msg;
				}
				if(opcao[0].equals("X")) {
					msgEnviada = msg;
				}
				if(opcao[0].equals("J")) {
					msgEnviada = "J:" + msg.substring(4);
					//Atualizar as peças no servidor
					//pecas = msg.substring(4);
				}
				if(opcao[0].equals("D")) {
					msgEnviada = msg;
					System.out.println("Jogador " + opcao[1] + " desistiu da partida.");
				}
				if(opcao[0].equals("R")) {
					msgEnviada = msg;
					System.out.println("Jogador " + opcao[1] + " pediu reinicio de partida.");
				}
				if(opcao[0].equals("N")) {
					msgEnviada = msg;
					System.out.println("Jogador " + opcao[1] + " aceitou reinicio de partida.");
					sg.escreverEvento(" jogador " + opcao[1] + " aceitou reinicio de partida.",false);
				}
				if(opcao[0].equals("Q")) {
					msgEnviada = msg;
				}
				if(opcao[0].equals("S")) {
					msgEnviada = msg;
					sg.escreverEvento(" jogador " + opcao[1] + " saiu do servidor.",false);
				}
				ostream.writeUTF(msgEnviada);
				ostream.flush();
			}catch(Exception me2) {
				//sg.escreverEvento(" " + me2.getMessage(), false);
				System.out.println("Erro ME2: " + me2.getMessage());
			}
		}
		
		private void desconectarCliente() {
			try {
				idUsers--;
				numUsers--;
				if(s != null) {
					s.close();
				}
				if(ostream != null) {
					ostream.close();
				}
				if(istream != null) {
					istream.close();
				}
			}
			catch(Exception dc) {
				System.out.println("Erro DC: " + dc.getMessage());
			}
		}
	}
	
}
