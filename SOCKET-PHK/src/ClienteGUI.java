import java.awt.EventQueue;
//import java.awt.TrayIcon.MessageType;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
public class ClienteGUI extends JFrame implements ActionListener{


	// Interface:
	
	private static final long serialVersionUID = 1L;
	private JTextArea txtChat, txtMsg;
	private JButton btnEnviar;

	// Comunicação :
	
	ServerSocket ss = null;
	DataInputStream in = null; 
    DataOutputStream out = null;	
    Socket soc = null;
    String msg = "";
    
    Cliente cliente = null;
    public String pecasAux = "1:2:0:1:2", pecas = "1:2:0:1:2", pecasInicio = "1:2:0:1:2";
    public boolean novoJogo = false, pedidoAdv = false;
    private JButton btnA, btnB, btnC, btnD, btnE, btnReiniciar, btnDesistir, btnConectar;
    private ImageIcon iconRed = new ImageIcon(ClienteGUI.class.getResource("/img/red.png")),
    				  iconBlue = new ImageIcon(ClienteGUI.class.getResource("/img/blue.png")),
    				  iconEmpty = new ImageIcon(ClienteGUI.class.getResource("/img/empty.png")),
    				  iconEsqueleto = new ImageIcon(ClienteGUI.class.getResource("/img/esqueleto.png")),
    				  iconLogo = new ImageIcon(ClienteGUI.class.getResource("/img/logomarca.png"));
    private JLabel lblEsqueleto;
    private JTextField txtServer, txtPorta;
    private JPanel panel, contentPane, panelInicio, panelConectado;
    private JLabel label, lblVez, lblTurno, lblJogador, lblNome, lblJogando, lblCor;
    private JPanel panelTurno, panelInformacoes, panelOpcoes2, panelOpcoes1;
    private JLabel lblStatus;
   
    // Main thread
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClienteGUI frame = new ClienteGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * @wbp.parser.constructor
	 */

	public ClienteGUI() {
		super("PONG-HAU-KI 1.4.3");
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 387, 103);
		setResizable(false);
		//this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				if(JOptionPane.showConfirmDialog(null,"Deseja sair do jogo?", "Sair",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
					if(cliente != null) {
						if(cliente.vivo) {
							//cliente.doisJogadores = false;
							if(cliente.doisJogadores) {
								cliente.enviarMsg("S:" + Integer.toString(cliente.uId), false);
							}
							System.exit(0);
						}
						else {
							System.exit(0);
						}
					}
					else {
						System.exit(0);
					}
				}
				else {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
		
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 76, 362, 300);
		contentPane.add(panel);
		panel.setLayout(null);
		
		txtChat = new JTextArea();
		txtChat.setEditable(false);
		txtChat.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtChat.setBounds(10, 11, 344, 207);
		txtChat.setLineWrap(true); 
		panel.add(txtChat, BorderLayout.CENTER);
		
		btnEnviar = new JButton("");
		btnEnviar.setForeground(Color.WHITE);
		btnEnviar.setBackground(Color.WHITE);
		btnEnviar.setToolTipText("ENVIAR");
		btnEnviar.setBorder(null);
		btnEnviar.setIcon(new ImageIcon(ClienteGUI.class.getResource("/img/btnEnviar.png")));
		btnEnviar.addActionListener(this);
		btnEnviar.setBounds(294, 229, 60, 60);
		panel.add(btnEnviar);
		
		txtMsg = new JTextArea();
		txtMsg.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtMsg.setBounds(10, 229, 274, 60);
		txtMsg.setColumns(10);
		txtMsg.setLineWrap(true);
		txtMsg.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) { 
					//escreverMsg(txtMsg.getText().trim(), cliente.uId, false);
					btnEnviar.doClick();
					txtMsg.getDocument().putProperty("filterNewlines", Boolean.TRUE);
					txtMsg.setText(txtMsg.getText().replaceAll(" ", ""));
					txtMsg.selectAll();
					txtMsg.replaceSelection("");
					txtMsg.setCaretPosition(0);
				}
			}
		});
		panel.add(txtMsg);

		JScrollPane scroll1 = new JScrollPane(txtChat,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll1.setBounds(10, 11, 344, 207);
		JScrollPane scroll2 = new JScrollPane(txtMsg,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll2.setBounds(10, 229, 274, 60);
		panel.add(scroll1);
		panel.add(scroll2);
				
		btnA = new JButton("", iconRed);
		btnA.setBorder(null);
		btnA.setBounds(526, 24, 50, 50);
		btnA.addActionListener(this);
		contentPane.add(btnA);
		
		btnB = new JButton("", iconBlue);
		btnB.setBorder(null);
		btnB.setBounds(526, 298, 50, 50);
		btnB.addActionListener(this);
		contentPane.add(btnB);
		
		btnD = new JButton("", iconRed);
		btnD.setBorder(null);
		btnD.setBounds(800, 24, 50, 50);
		btnD.addActionListener(this);
		contentPane.add(btnD);
		
		btnE = new JButton("", iconBlue);
		btnE.setBorder(null);
		btnE.setBounds(800, 298, 50, 50);
		btnE.addActionListener(this);
		contentPane.add(btnE);
		
		btnC = new JButton("", iconEmpty);
		btnC.setBorder(null);
		btnC.setBounds(661, 153, 50, 50);
		btnC.addActionListener(this);
		contentPane.add(btnC);
		
		lblEsqueleto = new JLabel("");
		lblEsqueleto.setBounds(513, 11, 350, 350);
		lblEsqueleto.setIcon(iconEsqueleto);
		contentPane.add(lblEsqueleto);
		
		panelInicio = new JPanel();
		panelInicio.setBackground(Color.WHITE);
		panelInicio.setBounds(10, 11, 362, 64);
		contentPane.add(panelInicio);
		panelInicio.setLayout(null);
		
		txtServer = new JTextField();
		txtServer.setText("localhost");
		txtServer.setBorder(new LineBorder(new Color(171, 173, 179)));
		txtServer.setBounds(76, 0, 148, 20);
		panelInicio.add(txtServer);
		txtServer.setColumns(10);
		
		JLabel lblServer = new JLabel("Servidor :");
		lblServer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServer.setBounds(10, 3, 59, 14);
		panelInicio.add(lblServer);
		
		JLabel lblPorta = new JLabel("Porta :");
		lblPorta.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPorta.setBounds(10, 36, 59, 14);
		panelInicio.add(lblPorta);
		
		txtPorta = new JTextField();
		txtPorta.setText("9090");
		txtPorta.setColumns(10);
		txtPorta.setBorder(new LineBorder(new Color(171, 173, 179)));
		txtPorta.setBounds(76, 33, 82, 20);
		panelInicio.add(txtPorta);
		
		btnConectar = new JButton("Entrar");
		btnConectar.setBounds(270, 21, 82, 29);
		btnConectar.addActionListener(this);
		panelInicio.add(btnConectar);
		
		lblStatus = new JLabel("Servidor offline. Tente novamente.\r\n");
		lblStatus.setVisible(false);
		lblStatus.setForeground(Color.RED);
		lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblStatus.setVerticalAlignment(SwingConstants.TOP);
		lblStatus.setBounds(195, 50, 162, 14);
		panelInicio.add(lblStatus);
		
		panelConectado = new JPanel();
		panelConectado.setBackground(Color.WHITE);
		panelConectado.setBounds(10, 11, 362, 63);
		contentPane.add(panelConectado);
		panelConectado.setVisible(false);
		panelConectado.setLayout(null);
		
		label = new JLabel("");
		label.setBackground(Color.WHITE);
		label.setIcon(iconLogo);
		label.setBounds(0, 0, 342, 53);
		panelConectado.add(label);
		
		panelTurno = new JPanel();
		panelTurno.setBackground(Color.WHITE);
		panelTurno.setForeground(Color.WHITE);
		panelTurno.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelTurno.setLayout(null);
		panelTurno.setBounds(382, 11, 112, 64);
		contentPane.add(panelTurno);
		
		lblVez = new JLabel("Vez de :");
		lblVez.setHorizontalAlignment(SwingConstants.CENTER);
		lblVez.setBounds(0, 11, 112, 14);
		panelTurno.add(lblVez);
		
		lblTurno = new JLabel("<html>Sem cor</html>");
		lblTurno.setVerticalAlignment(SwingConstants.TOP);
		lblTurno.setForeground(Color.BLACK);
		lblTurno.setHorizontalAlignment(SwingConstants.CENTER);
		lblTurno.setBounds(0, 36, 112, 28);
		panelTurno.add(lblTurno);
		
		panelOpcoes1 = new JPanel();
		panelOpcoes1.setBackground(Color.WHITE);
		panelOpcoes1.setForeground(Color.WHITE);
		panelOpcoes1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelOpcoes1.setLayout(null);
		panelOpcoes1.setBounds(382, 209, 112, 83);
		contentPane.add(panelOpcoes1);
		
		btnReiniciar = new JButton("");
		btnReiniciar.setBorder(null);
		btnReiniciar.setToolTipText("REINICIAR");
		btnReiniciar.setBackground(Color.WHITE);
		btnReiniciar.setIcon(new ImageIcon(ClienteGUI.class.getResource("/img/btnReiniciar.png")));
		btnReiniciar.setBounds(26, 11, 60, 60);
		btnReiniciar.addActionListener(this);
		panelOpcoes1.add(btnReiniciar);
		
		panelOpcoes2 = new JPanel();
		panelOpcoes2.setBackground(Color.WHITE);
		panelOpcoes2.setForeground(Color.WHITE);
		panelOpcoes2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelOpcoes2.setBounds(382, 293, 112, 83);
		contentPane.add(panelOpcoes2);
		panelOpcoes2.setLayout(null);
		
		btnDesistir = new JButton("");
		btnDesistir.setToolTipText("DESISTIR");
		btnDesistir.setBorder(null);
		btnDesistir.setBackground(Color.WHITE);
		btnDesistir.setIcon(new ImageIcon(ClienteGUI.class.getResource("/img/btnDesistir.png")));
		btnDesistir.setBounds(26, 11, 60, 60);
		btnDesistir.addActionListener(this);
		panelOpcoes2.add(btnDesistir);
		
		panelInformacoes = new JPanel();
		panelInformacoes.setLayout(null);
		panelInformacoes.setForeground(Color.WHITE);
		panelInformacoes.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelInformacoes.setBackground(Color.WHITE);
		panelInformacoes.setBounds(382, 76, 112, 132);
		contentPane.add(panelInformacoes);
		
		lblJogador = new JLabel("Jogador :");
		lblJogador.setHorizontalAlignment(SwingConstants.CENTER);
		lblJogador.setBounds(0, 11, 112, 14);
		panelInformacoes.add(lblJogador);
		
		lblNome = new JLabel("Jogador");
		lblNome.setHorizontalAlignment(SwingConstants.CENTER);
		lblNome.setForeground(Color.BLACK);
		lblNome.setBounds(0, 36, 112, 14);
		panelInformacoes.add(lblNome);
		
		lblJogando = new JLabel("Jogando com :");
		lblJogando.setHorizontalAlignment(SwingConstants.CENTER);
		lblJogando.setBounds(0, 77, 112, 14);
		panelInformacoes.add(lblJogando);
		
		lblCor = new JLabel("Sem cor");
		lblCor.setHorizontalAlignment(SwingConstants.CENTER);
		lblCor.setForeground(Color.BLACK);
		lblCor.setBounds(0, 102, 112, 14);
		panelInformacoes.add(lblCor);
		
		centerFrame();
	}
	
	private void centerFrame() {

        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();

        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;    
        setLocation(dx, dy);
	}

	
	public void servidorOff(int tipo) {
		if(tipo == 0) {
			panelConectado.setVisible(false);
			panelInicio.setVisible(true);
			setBounds(100, 100, 387, 103);
			txtChat.setText("");
			lblStatus.setVisible(true);
		}
		else {
			//txtChat.append("O servidor está OFFLINE.\n");
			//msgAlerta("O servidor está OFFLINE!!!");
			txtChat.setCaretPosition(txtChat.getDocument().getLength() - 1);
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(this, "O servidor está OFFLINE, tente reconectar.", "Reconectar ao servidor", dialogButton);
			if(dialogResult == 0) {
				panelConectado.setVisible(false);
				panelInicio.setVisible(true);
				setBounds(100, 100, 387, 103);
				txtChat.setText("");
			}
			else {
				this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		}		
	}
	
	public void escreverMsg(String msg, int userID, Boolean inicio) {
		if(inicio) {
			txtChat.append("Conectado ao servidor " + msg + ".\n");
			txtChat.setCaretPosition(txtChat.getDocument().getLength() - 1);
		}
		else {
			txtChat.append("Jogador " + Integer.toString(userID) + ": " + msg + "\n");
			txtChat.setCaretPosition(txtChat.getDocument().getLength() - 1);
		}
	}
	
	public void atualizarTela() {
		if(!novoJogo) {
			lblNome.setText("Jogador " + cliente.uId);
			boolean turno = cliente.turno;
			if(turno) {
				if(cliente.uId == 1) {
					lblTurno.setForeground(Color.RED);
					lblTurno.setText("Vermelho");
				}
				else {
					lblTurno.setForeground(Color.BLUE);
					lblTurno.setText("Azul");
				}
			}
			else {
				if(cliente.uId == 1) {
					lblTurno.setForeground(Color.BLUE);
					lblTurno.setText("Azul");
				}
				else {
					lblTurno.setForeground(Color.RED);
					lblTurno.setText("Vermelho");
				}
			}
			if(cliente.uId == 1) {
				lblCor.setText("Vermelho");
				lblCor.setForeground(Color.RED);
			}
			else {
				lblCor.setText("Azul");
				lblCor.setForeground(Color.BLUE);
			}
		}
	}
	
	private void resultadoJogo() {
		if(cliente.uId == 1) {
			if(pecas.equals("0:1:1:2:2") || pecas.equals("2:2:1:0:1")) {
				voceVenceu(0);
			}
			if(pecas.equals("0:2:2:1:1") || pecas.equals("1:1:2:0:2")) {
				vocePerdeu();
			}
		}
		else {
			if(pecas.equals("0:1:1:2:2") || pecas.equals("2:2:1:0:1")) {
				vocePerdeu();
			}
			if(pecas.equals("0:2:2:1:1") || pecas.equals("1:1:2:0:2")) {
				voceVenceu(0);
			}
		}
	}
	
	public void advReiniciar() {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(this, "Jogador " + cliente.uId + ", o adversário quer reiniciar a partida, aceita?", "Reiniciar?", dialogButton);
		if(dialogResult == 0) {
			pedidoAdv = true;
			//txtChat.append("PARTIDA REINICIADA!!!\n");
			msgAlerta("VOCÊ ACEITOU O REINICIO DA PARTIDA!!!");
			cliente.enviarMsg("N:" + Integer.toString(cliente.uId), false);
			reiniciar();
		} else {
			pedidoAdv = false;
		} 
	}
	
	public void reiniciar() {
		if(novoJogo) {
			pecas = pecasInicio;
			if(cliente.uId == 1) {
				cliente.turno = true;
			}
			else {
				cliente.turno = false;
			}
			montarPecas(pecas);
			novoJogo = false;
			//txtChat.append("PARTIDA REINICIADA!!!\n");
			msgAlerta("PARTIDA REINICIADA!!!");
		}
		if(pedidoAdv) {
			pecas = pecasInicio;
			if(cliente.uId == 1) {
				cliente.turno = true;
			}
			else {
				cliente.turno = false;
			}
			montarPecas(pecas);
			pedidoAdv = false;
		}
	}
	
	public void voceVenceu(int tipo) {
		if (tipo == 0) {
			//txtChat.append("PARABÉNS, VOCÊ VENCEU!!!\n");
			msgAlerta("PARABÉNS, VOCÊ VENCEU!!!");
			txtChat.setCaretPosition(txtChat.getText().length() - 1);
			cliente.turno = false;
			lblTurno.setForeground(Color.BLACK);
			lblTurno.setText("Reinicie a partida");
			novoJogo = true;
			//reiniciar();
		}
		else {
			msgAlerta("O ADVERSÁRIO SAIU, VOCÊ VENCEU!!!");
			txtChat.setCaretPosition(txtChat.getText().length() - 1);
			//cliente.turno = false;
			lblTurno.setForeground(Color.BLACK);
			lblTurno.setText("Aguardando novo adversário.");
			//novoJogo = true;
		}
	}
	
	private void vocePerdeu() {
		//txtChat.append("NÃO DEU, VOCÊ PERDEU!!!\n");
		msgAlerta("NÃO DEU, VOCÊ PERDEU!!!");
		txtChat.setCaretPosition(txtChat.getText().length() - 1);
		cliente.turno = false;
		lblTurno.setForeground(Color.BLACK);
		lblTurno.setText("Reinicie a partida");
		novoJogo = true;
		//reiniciar();
	}
	
	public void msgAlerta(String msg) {
		JOptionPane.showMessageDialog(this, msg, "PONG-HAU-KI", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		// Ações dos botões
		if(o == btnConectar && !txtServer.getText().trim().equals("") && !txtPorta.getText().trim().equals("")) {
			//Iniciar cliente
			cliente = new Cliente(txtServer.getText().trim(), Integer.parseInt(txtPorta.getText().trim()), "Nome do jogador", this);
			if(!cliente.iniciarCliente()) {
				escreverMsg(" não conectado.",0, false);
			}
			else {
				panelConectado.setVisible(true);
				panelInicio.setVisible(false);
				setBounds(100, 100, 900, 415);
				centerFrame();
				lblStatus.setVisible(false);
				txtChat.setText("Conectado ao servidor " + txtServer.getText().trim() + ".\n");
			}
		}
		
		if(o == btnEnviar && !(txtMsg.getText().trim().length() == 0)) {
			if(cliente.enviarMsg("M:" + Integer.toString(cliente.uId) + ":" + txtMsg.getText().trim(), false)) {
				escreverMsg(txtMsg.getText().trim(), cliente.uId, false);
			}
			txtMsg.setText("");
		}

		if(o == btnReiniciar) {
			if(novoJogo || pedidoAdv) {
				reiniciar();
			}
			else {
				cliente.enviarMsg("R:" + Integer.toString(cliente.uId), false);
			}
		}
		
		if(o == btnDesistir) {
			if (cliente.doisJogadores) {
				vocePerdeu();
			}
			cliente.enviarMsg("D:" + Integer.toString(cliente.uId), false);
		}
		
		String[] val = pecas.split(":");
		
		if(cliente.vivo && !cliente.turno && (o == btnA || o == btnB || o == btnC || o == btnD || o == btnD || o == btnE)) {
			if(novoJogo) {
				msgAlerta("A partida acabou, reinicie para jogar novamente.");
			}
			else {
				msgAlerta("Aguarde seu turno.");				
			}
		}
		
		if(cliente.vivo && cliente.doisJogadores && cliente.turno && (o == btnA || o == btnB || o == btnC || o == btnD || o == btnD || o == btnE)) {
			if(o == btnA) {
				pecasAux = pecas;
				pecas = cliente.movimentarPeca(val[0], 0);		
				if(pecas.equals(pecasAux) && val[0].equals(Integer.toString(cliente.uId))) {
					msgAlerta("Sem movimentos possíveis para essa peça.");
				}
				if(pecas.equals(pecasAux) && !val[0].equals(Integer.toString(cliente.uId))) {
					msgAlerta("Movimente somente suas peças.");
				}
				if(!pecas.equals(pecasAux)) {
					cliente.enviarMsg("J:" + Integer.toString(cliente.uId) + ":" + pecas, false);
					montarPecas(pecas);
				}
			}
			if(o == btnB) {
				pecasAux = pecas;
				pecas = cliente.movimentarPeca(val[1], 1);
				if(pecas.equals(pecasAux) && val[1].equals(Integer.toString(cliente.uId))) {
					msgAlerta("Sem movimentos possíveis para essa peça.");
				}
				if(pecas.equals(pecasAux) && !val[1].equals(Integer.toString(cliente.uId))) {
					msgAlerta("Movimente somente suas peças.");
				}
				if(!pecas.equals(pecasAux)) {
					cliente.enviarMsg("J:" + Integer.toString(cliente.uId) + ":" + pecas, false);
					montarPecas(pecas);
				}
			}
			if(o == btnC) {
				pecasAux = pecas;
				pecas = cliente.movimentarPeca(val[2], 2);
				if(pecas.equals(pecasAux) && val[2].equals(Integer.toString(cliente.uId))) {
					msgAlerta("Sem movimentos possíveis para essa peça.");
				}
				if(pecas.equals(pecasAux) && !val[2].equals(Integer.toString(cliente.uId))) {
					msgAlerta("Movimente somente suas peças.");
				}
				if(!pecas.equals(pecasAux)) {
					cliente.enviarMsg("J:" + Integer.toString(cliente.uId) + ":" + pecas, false);
					montarPecas(pecas);
				}
			}
			if(o == btnD) {
				pecasAux = pecas;
				pecas = cliente.movimentarPeca(val[3], 3);
				if(pecas.equals(pecasAux) && val[3].equals(Integer.toString(cliente.uId))) {
					msgAlerta("Sem movimentos possíveis para essa peça.");
				}
				if(pecas.equals(pecasAux) && !val[3].equals(Integer.toString(cliente.uId))) {
					msgAlerta("Movimente somente suas peças.");
				}
				if(!pecas.equals(pecasAux)) {
					cliente.enviarMsg("J:" + Integer.toString(cliente.uId) + ":" + pecas, false);
					montarPecas(pecas);
				}
			}
			if(o == btnE) {
				pecasAux = pecas;
				pecas = cliente.movimentarPeca(val[4], 4);
				if(pecas.equals(pecasAux) && val[4].equals(Integer.toString(cliente.uId))) {
					msgAlerta("Sem movimentos possíveis para essa peça.");
				}
				if(pecas.equals(pecasAux) && !val[4].equals(Integer.toString(cliente.uId))) {
					msgAlerta("Movimente somente suas peças.");
				}
				if(!pecas.equals(pecasAux)) {
					cliente.enviarMsg("J:" + Integer.toString(cliente.uId) + ":" + pecas, false);
					montarPecas(pecas);
				}
			}
		}
	}
	
	public void montarPecas(String pecasDist) {		
		if(cliente.turno) {
			if(cliente.uId == 1) {
				lblTurno.setForeground(Color.RED);
				lblTurno.setText("Vermelho");
			}
			else {
				lblTurno.setForeground(Color.BLUE);
				lblTurno.setText("Azul");
			}
		}
		else {
			if(cliente.uId == 1) {
				lblTurno.setForeground(Color.BLUE);
				lblTurno.setText("Azul");
			}
			else {
				lblTurno.setForeground(Color.RED);
				lblTurno.setText("Vermelho");
			}
		}
		String[] val = pecasDist.split(":");
		if(val[0].equals("0")) {
			btnA.setIcon(iconEmpty);
		}
		if(val[0].equals("1")) {
			btnA.setIcon(iconRed);
		}
		if(val[0].equals("2")) {
			btnA.setIcon(iconBlue);
		}
		if(val[1].equals("0")) {
			btnB.setIcon(iconEmpty);
		}
		if(val[1].equals("1")) {
			btnB.setIcon(iconRed);
		}
		if(val[1].equals("2")) {
			btnB.setIcon(iconBlue);
		}
		if(val[2].equals("0")) {
			btnC.setIcon(iconEmpty);
		}
		if(val[2].equals("1")) {
			btnC.setIcon(iconRed);
		}
		if(val[2].equals("2")) {
			btnC.setIcon(iconBlue);
		}
		if(val[3].equals("0")) {
			btnD.setIcon(iconEmpty);
		}
		if(val[3].equals("1")) {
			btnD.setIcon(iconRed);
		}
		if(val[3].equals("2")) {
			btnD.setIcon(iconBlue);
		}
		if(val[4].equals("0")) {
			btnE.setIcon(iconEmpty);
		}
		if(val[4].equals("1")) {
			btnE.setIcon(iconRed);
		}
		if(val[4].equals("2")) {
			btnE.setIcon(iconBlue);
		}
		resultadoJogo();
	}
}
