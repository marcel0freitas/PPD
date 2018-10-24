package Servidor;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.border.LineBorder;

import Cliente.ClienteGUI;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class ServidorGUI extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane, panel;
	private JTextField txtServidor, txtPorta, txtNome;
	private JTextArea txtEventos;
	private JButton btnOnOff;
	private JLabel lblServidor, lblPorta, lblStatus, lblTimeout;
	public Servidor servidor;
	Boolean servidorStatus = false;
	public String pecas = "1:2:0:1:2";
	public String nomeSv;
	Boolean live = false;
	private ImageIcon iconRed = new ImageIcon(ClienteGUI.class.getResource("/img/red.png")),
			  iconBlue = new ImageIcon(ClienteGUI.class.getResource("/img/blue.png")),
			  iconEmpty = new ImageIcon(ClienteGUI.class.getResource("/img/empty.png")),
			  iconEsqueleto = new ImageIcon(ClienteGUI.class.getResource("/img/esqueleto.png"));
	Registry registry;
	private JLabel lblLive;
	private JButton btnLive;
	private JButton btnA;
	private JButton btnB;
	private JButton btnD;
	private JButton btnE;
	private JButton btnC;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws RemoteException, MalformedURLException{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServidorGUI frame = new ServidorGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServidorGUI() {
		super("PONG-HAU-KI 1.6.1");
		servidor = null;
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 301, 381);
		setResizable(false);
		this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 690, 352);
		contentPane.add(panel);
		panel.setLayout(null);
		
		txtEventos = new JTextArea();
		txtEventos.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtEventos.setText("Clique no bot\u00E3o para iniciar...");
		txtEventos.setEditable(false);
		txtEventos.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtEventos.setBounds(10, 84, 278, 214);
		txtEventos.setLineWrap(true); 
		panel.add(txtEventos);
		
		JScrollPane scroll1 = new JScrollPane(txtEventos,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll1.setBounds(10, 84, 278, 214);
		panel.add(scroll1);
		
		lblServidor = new JLabel("Servidor :");
		lblServidor.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServidor.setBounds(0, 12, 62, 14);
		panel.add(lblServidor);
		
		txtServidor = new JTextField();
		txtServidor.setText("localhost");
		txtServidor.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtServidor.setColumns(10);
		txtServidor.setBounds(65, 11, 137, 16);
		panel.add(txtServidor);
		
		btnOnOff = new JButton("");
		btnOnOff.setBorder(null);
		btnOnOff.addActionListener(this);
		btnOnOff.setBackground(Color.WHITE);
		btnOnOff.setIcon(new ImageIcon(ServidorGUI.class.getResource("/img/on-off.png")));
		btnOnOff.setBounds(236, 10, 40, 40);
		panel.add(btnOnOff);
		
		lblPorta = new JLabel("Porta :");
		lblPorta.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPorta.setBounds(0, 36, 62, 14);
		panel.add(lblPorta);
		
		txtPorta = new JTextField();
		txtPorta.setText("1099");
		txtPorta.setColumns(10);
		txtPorta.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtPorta.setBounds(65, 35, 62, 16);
		panel.add(txtPorta);
		
		lblStatus = new JLabel("OFFLINE");
		lblStatus.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStatus.setForeground(Color.RED);
		lblStatus.setBounds(210, 61, 66, 14);
		panel.add(lblStatus);
		
		txtNome = new JTextField();
		txtNome.setHorizontalAlignment(SwingConstants.LEFT);
		txtNome.setText("PHKrmi");
		txtNome.setColumns(10);
		txtNome.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtNome.setBounds(65, 59, 137, 16);
		panel.add(txtNome);
		
		lblTimeout = new JLabel("Nome :");
		lblTimeout.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTimeout.setBounds(0, 59, 62, 14);
		panel.add(lblTimeout);

		
		btnLive = new JButton("Ao vivo");
		btnLive.setBounds(92, 309, 106, 32);
		btnLive.addActionListener(this);
		panel.add(btnLive);
		
		btnA = new JButton("", iconRed);
		btnA.setBorder(null);
		btnA.setBounds(337, 13, 50, 50);
		panel.add(btnA);
		
		btnB = new JButton("", iconBlue);
		btnB.setBorder(null);
		btnB.setBounds(337, 286, 50, 50);
		panel.add(btnB);
		
		btnD = new JButton("", iconRed);
		btnD.setBorder(null);
		btnD.setBounds(609, 13, 50, 50);
		panel.add(btnD);
		
		btnE = new JButton("", iconBlue);
		btnE.setBorder(null);
		btnE.setBounds(609, 286, 50, 50);
		panel.add(btnE);
		
		btnC = new JButton("", iconEmpty);
		btnC.setBorder(null);
		btnC.setBounds(470, 141, 50, 50);
		panel.add(btnC);
		
		lblLive = new JLabel("");
		lblLive.setIcon(iconEsqueleto);
		lblLive.setBounds(323, 0, 367, 350);
		panel.add(lblLive);
	}
	
	public void alterarStatus(int tipo) {
		if(tipo == 0) {
			lblStatus.setForeground(Color.GREEN);
			lblStatus.setText("ONLINE");
		}
		else {
			lblStatus.setForeground(Color.red);
			lblStatus.setText("OFFLINE");
		}
	}
	
	public void escreverEvento(String str, boolean inicio) {
		if(inicio) {
			if (servidorStatus) {
				txtEventos.setText("Servidor " + nomeSv + " iniciado em " + str + ".\n");
			}
			else {
				txtEventos.setText("Servidor " + nomeSv + " finalizado.\n");
			}
			txtEventos.setCaretPosition(txtEventos.getText().length() - 1);
		}
		else {
			txtEventos.append("Servidor: " + str + "\n");
			txtEventos.setCaretPosition(txtEventos.getText().length() - 1);
		}
	}
	
	private void registrar(String servidor, int porta, String nome) throws RemoteException, MalformedURLException{
		try {
			this.servidor = new Servidor(this);
			String nomeServidor = "//" + servidor + "/" + nome;
			registry = LocateRegistry.createRegistry(porta);
			Naming.rebind(nomeServidor, this.servidor);
			//registry.rebind("PHKrmi", this.servidor);
			nomeSv = nome;
            System.out.println("Servidor Registrado! (" + nomeServidor + ")");
        }catch(Exception e){
            System.out.println("Erro de porta: " + e.getMessage());
        } 
	}
	
	private void desregistrar(String servidor, int porta, String nome) throws RemoteException{
		try {
			String nomeServidor = "//" + servidor + "/" + nome;
			Naming.unbind(nomeServidor);
			//registry.unbind("PHKrmi");
			escreverEvento(nome + " desregistrado.", false);
			System.out.println("Servidor Desregistrado!");
		} catch (Exception e) {
			System.out.println("Erro ao desregistrar: " + e.getMessage());
		}
	}
	
	public void montarPecas(String pecasDist) {
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
	}
	
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == btnOnOff) {
			int porta = Integer.parseInt(txtPorta.getText());
			String servidor = txtServidor.getText();
			String nome = txtNome.getText();
			if (!servidorStatus) {
				try {
					registrar(servidor, porta, nome);
					this.servidor.iniciarServidor();
				} catch (RemoteException | MalformedURLException e1) {
					System.out.println("Erro ao desregistrar: " + e1.getMessage());
				}
				servidorStatus = true;
				escreverEvento(servidor, true);
			} else {
				try {
					desregistrar(servidor, porta, nome);
					this.servidor.finalizarServidor();
				} catch (RemoteException e1) {
					System.out.println("Erro ao desregistrar: " + e1.getMessage());
				}
				servidorStatus = false;
				escreverEvento(servidor, true);
			} 
		}
		if (o == btnLive) {
			if (!live) {
				setBounds(100, 100, 696, 381);
			} 
			else {
				setBounds(100, 100, 301, 381);
			}
			live = !live;
		}
	}
}
