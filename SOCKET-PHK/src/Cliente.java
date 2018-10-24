import java.net.*;
import java.io.*;

public class Cliente {
	public boolean vivo = false;
	private DataOutputStream ostream;
    private DataInputStream istream;
    private Socket socket;
    private ClienteGUI cg;
    private String msgUser = "";
    private String servidor;
    private String usuario;
    public int uId;
    private int porta;
    public boolean turno;
    public boolean doisJogadores = false;

    public Cliente(String servidor, int porta, String usuario, ClienteGUI clienteGUI) {
        this.servidor = servidor;
        this.porta = porta;
        this.usuario = usuario;
        this.cg = clienteGUI;
    }
    
    public String getUsuario() {
    	return usuario;
    }
    
    // Iniciar cliente ao servidor
    public boolean iniciarCliente() {
	    vivo = true;
    	try {
			socket = new Socket(servidor, porta);
			ostream = new DataOutputStream(socket.getOutputStream());
		    istream = new DataInputStream(socket.getInputStream());
		} catch(Exception e) {
			cg.escreverMsg(" erro ao tentar iniciar cliente: " + e, 0, false);
			vivo = false;
			cg.servidorOff(0);
			return false;
		}
    	//cg.escreverMsg("",0 , true);
    	new SrvThread().start();
    	//Enviar msg ao servidor pedindo um id
	    enviarMsg("I:0", true);
    	return true;
    }
    
    // Enviar msg ao servidor
    public boolean enviarMsg(String msg, boolean primeiraMsg) {
    	try {
    		if(msg.equals("I:0")) {
    			primeiraMsg = true;
    		}
        	if(primeiraMsg) {
        		ostream.writeUTF(msg);
        		ostream.flush();
        	}
        	if(!primeiraMsg && doisJogadores) {
        		vivo = true;
        		ostream.writeUTF(msg);
        		ostream.flush();
        	}
        	if(!primeiraMsg && !doisJogadores) {
        		cg.pecas = cg.pecasInicio;
        		cg.msgAlerta("Aguarde algum adversário se conectar ao servidor.");
        		return false;
        	}
    	}catch(IOException ioe) {
    		cg.servidorOff(1);
    		vivo = false;
    		return false;
    	}
    	return true;
    }
    
    /*
     * Jogadas possíveis:
     * 0-1 / 0-2 / 1-0 / 1-2 / 1-4 / 2-0 / 2-1 / 2-3 / 2-4 / 3-2 / 3-4 / 4-1 / 4-2 / 4-3
     * */
    public String movimentarPeca(String tipo, int origem) {
    	// Ex: movimentarPeca(1,0)
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
		//msgUser = retorno;
		return retorno;
    }

    // Desistência: na GUI 
    // @Deprecated
    public void desistir() {
    	
    }
    
    // Reiniciar partida: na GUI
    // @Deprecated
    public void reiniciar() {
    	
    }
    
    private void disconectarJogador() {
    	try {
			if(istream != null) {
				istream.close();
			}
			if(ostream != null) {
				ostream.close();
			}
			if(socket != null) {
				socket.close();
			}
			vivo = false;
		} catch (IOException e0) {
			cg.escreverMsg(" servidor está inacessível: " + e0, 0, false);
			System.exit(0);
		}
    }
    
    class SrvThread extends Thread {

		public void run() {
			while(vivo) {
				try {
					msgUser = istream.readUTF();
					String[] opcao = msgUser.split(":");
					//Tipos de msg para o cliente, recebidas do servidor:
					if(opcao[0].equals("I")) {
						uId = Integer.parseInt(opcao[1]);
						turno = uId == 1 ? true : false;
						cg.atualizarTela();
					}
					if(opcao[0].equals("X")) {
						doisJogadores = true;
					}
					if(opcao[0].equals("M")) {
						if(uId == 1) {
							cg.escreverMsg(msgUser.substring(2), 2, false);
						}
						else {
							cg.escreverMsg(msgUser.substring(2), 1, false);
						}
					}
					if(opcao[0].equals("J")) {
						cg.pecas = msgUser.substring(2);
						turno = !turno;
						cg.montarPecas(cg.pecas);
						cg.atualizarTela();
					}
					if(opcao[0].equals("R")) {
						cg.advReiniciar();
					}
					if(opcao[0].equals("N")) {
						cg.novoJogo = true;
						cg.reiniciar();
					}
					if(opcao[0].equals("D")) {
						cg.voceVenceu(0);
					}
					if(opcao[0].equals("Q")) {
						disconectarJogador();
					}
					if(opcao[0].equals("S")) {
						doisJogadores = false;
						cg.voceVenceu(1);
					}
				}
				catch(IOException e1) {
					cg.escreverMsg(" servidor inacessível: " + e1, uId, false);
					vivo = false;
				}
			}
		}
	}
}
