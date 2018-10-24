import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class ServerGUI extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane, panel;
	private JTextField txtServidor, txtPorta, txtTimeout;
	private JTextArea txtEventos;
	private JButton btnOnOff;
	private JLabel lblServidor, lblPorta, lblStatus, lblSeg, lblTimeout;
	public Server server;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI frame = new ServerGUI();
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
	public ServerGUI() {
		super("PONG-HAU-KI 1.4.3");
		server = null;
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 304, 304);
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
		panel.setBounds(0, 0, 298, 276);
		contentPane.add(panel);
		panel.setLayout(null);
		
		txtEventos = new JTextArea();
		txtEventos.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtEventos.setText("Aguardando conex\u00F5es...\r\n");
		txtEventos.setEditable(false);
		txtEventos.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtEventos.setBounds(10, 84, 278, 181);
		txtEventos.setLineWrap(true); 
		panel.add(txtEventos);
		
		JScrollPane scroll1 = new JScrollPane(txtEventos,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll1.setBounds(10, 84, 278, 181);
		panel.add(scroll1);
		
		lblServidor = new JLabel("Servidor :");
		lblServidor.setHorizontalAlignment(SwingConstants.RIGHT);
		lblServidor.setBounds(0, 12, 62, 14);
		panel.add(lblServidor);
		
		txtServidor = new JTextField();
		txtServidor.setEnabled(false);
		txtServidor.setText("localhost");
		txtServidor.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtServidor.setColumns(10);
		txtServidor.setBounds(65, 11, 137, 16);
		panel.add(txtServidor);
		
		btnOnOff = new JButton("");
		btnOnOff.setBorder(null);
		btnOnOff.addActionListener(this);
		btnOnOff.setBackground(Color.WHITE);
		btnOnOff.setIcon(new ImageIcon(ServerGUI.class.getResource("/img/on-off.png")));
		btnOnOff.setBounds(236, 10, 40, 40);
		panel.add(btnOnOff);
		
		lblPorta = new JLabel("Porta :");
		lblPorta.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPorta.setBounds(0, 36, 62, 14);
		panel.add(lblPorta);
		
		txtPorta = new JTextField();
		txtPorta.setText("9090");
		txtPorta.setColumns(10);
		txtPorta.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtPorta.setBounds(65, 35, 62, 16);
		panel.add(txtPorta);
		
		lblStatus = new JLabel("OFFLINE");
		lblStatus.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStatus.setForeground(Color.RED);
		lblStatus.setBounds(210, 61, 66, 14);
		panel.add(lblStatus);
		
		txtTimeout = new JTextField();
		txtTimeout.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTimeout.setText("20");
		txtTimeout.setColumns(10);
		txtTimeout.setBorder(new LineBorder(new Color(0, 0, 0)));
		txtTimeout.setBounds(65, 59, 25, 16);
		panel.add(txtTimeout);
		
		lblTimeout = new JLabel("Timeout :");
		lblTimeout.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTimeout.setBounds(0, 59, 62, 14);
		panel.add(lblTimeout);
		
		lblSeg = new JLabel("seg");
		lblSeg.setBounds(92, 61, 35, 14);
		panel.add(lblSeg);
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
			txtEventos.append("Servidor iniciado em : " + str + ".\n");
			txtEventos.setCaretPosition(txtEventos.getText().length() - 1);
		}
		else {
			txtEventos.append("Servidor: " + str + "\n");
			txtEventos.setCaretPosition(txtEventos.getText().length() - 1);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(server == null) {
			try {
				int porta = Integer.parseInt(txtPorta.getText());
				String servidor = txtServidor.getText();
				server = new Server(servidor, porta, Integer.parseInt(txtTimeout.getText()), this);
			} catch (Exception e1) {
				escreverEvento(" porta inválida ou valores incorretos.", false);
			}
		}
		if(server != null) {
			if(!server.ativo) {
				alterarStatus(0);
				server.iniciarServidor();
			}
			else {
				alterarStatus(1);
				server.finalizarServidor();
			}
		}
	}
}
