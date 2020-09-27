package Project6461;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class ComputerMain extends JFrame {

	private JPanel contentPane;
	protected JTextField gpr0field;
	protected JTextField gpr1field;
	protected JTextField gpr2field;
	protected JTextField gpr3field;
	protected JTextField ixr1field;
	protected JTextField ixr2field;
	protected JTextField ixr3field;
	protected JTextField pcfield;
	protected JTextField mbrfield;
	protected JTextField irfield;
	protected JTextField marfield;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ComputerMain gui = new ComputerMain();
					gui.setVisible(true);
					CPU cpu = new CPU(gui);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ComputerMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 950, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JLabel lblGpr_0 = new JLabel("GPR 0");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblGpr_0, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblGpr_0, 10, SpringLayout.WEST, contentPane);
		contentPane.add(lblGpr_0);
		
		JLabel lblGpr_1 = new JLabel("GPR 1");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblGpr_1, 20, SpringLayout.SOUTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblGpr_1, 0, SpringLayout.EAST, lblGpr_0);
		contentPane.add(lblGpr_1);
		
		JLabel lblGpr_2 = new JLabel("GPR 2");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblGpr_2, 18, SpringLayout.SOUTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblGpr_2, 0, SpringLayout.WEST, lblGpr_0);
		contentPane.add(lblGpr_2);
		
		JLabel lblGpr_3 = new JLabel("GPR 3");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblGpr_3, 16, SpringLayout.SOUTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblGpr_3, 0, SpringLayout.WEST, lblGpr_0);
		contentPane.add(lblGpr_3);
		
		JLabel lblixr_1 = new JLabel("IXR 1");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblixr_1, 46, SpringLayout.SOUTH, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblixr_1, 0, SpringLayout.WEST, lblGpr_0);
		contentPane.add(lblixr_1);
		
		JLabel lblixr_2 = new JLabel("IXR 2");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblixr_2, 26, SpringLayout.SOUTH, lblixr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblixr_2, 0, SpringLayout.WEST, lblGpr_0);
		contentPane.add(lblixr_2);
		
		JLabel lblixr_3 = new JLabel("IXR 3");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblixr_3, 25, SpringLayout.SOUTH, lblixr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblixr_3, 0, SpringLayout.WEST, lblGpr_0);
		contentPane.add(lblixr_3);
		
		gpr0field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr0field, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr0field, 6, SpringLayout.EAST, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.EAST, gpr0field, 206, SpringLayout.EAST, lblGpr_0);
		contentPane.add(gpr0field);
		gpr0field.setColumns(10);
		
		gpr1field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr1field, -3, SpringLayout.NORTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr1field, 6, SpringLayout.EAST, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.EAST, gpr1field, 206, SpringLayout.EAST, lblGpr_1);
		gpr1field.setColumns(10);
		contentPane.add(gpr1field);
		
		gpr2field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr2field, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr2field, -200, SpringLayout.EAST, gpr0field);
		sl_contentPane.putConstraint(SpringLayout.EAST, gpr2field, 0, SpringLayout.EAST, gpr0field);
		gpr2field.setColumns(10);
		contentPane.add(gpr2field);
		
		gpr3field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr3field, 0, SpringLayout.NORTH, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr3field, 6, SpringLayout.EAST, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.EAST, gpr3field, 0, SpringLayout.EAST, gpr0field);
		gpr3field.setColumns(10);
		contentPane.add(gpr3field);
		
		ixr1field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr1field, 0, SpringLayout.NORTH, lblixr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr1field, 0, SpringLayout.WEST, gpr0field);
		sl_contentPane.putConstraint(SpringLayout.EAST, ixr1field, 200, SpringLayout.WEST, gpr0field);
		ixr1field.setColumns(10);
		contentPane.add(ixr1field);
		
		ixr2field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr2field, 0, SpringLayout.NORTH, lblixr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr2field, 0, SpringLayout.WEST, gpr0field);
		sl_contentPane.putConstraint(SpringLayout.EAST, ixr2field, 209, SpringLayout.EAST, lblixr_2);
		ixr2field.setColumns(10);
		contentPane.add(ixr2field);
		
		ixr3field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr3field, 0, SpringLayout.NORTH, lblixr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr3field, -200, SpringLayout.EAST, gpr0field);
		sl_contentPane.putConstraint(SpringLayout.EAST, ixr3field, 0, SpringLayout.EAST, gpr0field);
		ixr3field.setColumns(10);
		contentPane.add(ixr3field);
		
		JButton gpr0load = new JButton("Load");
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr0load, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr0load, 6, SpringLayout.EAST, gpr0field);
		contentPane.add(gpr0load);
		
		JButton gpr1load = new JButton("Load");
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr1load, 0, SpringLayout.NORTH, gpr1field);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr1load, 6, SpringLayout.EAST, gpr1field);
		contentPane.add(gpr1load);
		
		JButton gpr2load = new JButton("Load");
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr2load, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr2load, 6, SpringLayout.EAST, gpr2field);
		contentPane.add(gpr2load);
		
		JButton gpr3load = new JButton("Load");
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr3load, 0, SpringLayout.NORTH, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr3load, 6, SpringLayout.EAST, gpr3field);
		contentPane.add(gpr3load);
		
		JButton ixr1load = new JButton("Load");
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr1load, 0, SpringLayout.NORTH, lblixr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr1load, 6, SpringLayout.EAST, ixr1field);
		contentPane.add(ixr1load);
		
		JButton ixr2load = new JButton("Load");
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr2load, 0, SpringLayout.NORTH, lblixr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr2load, 6, SpringLayout.EAST, ixr2field);
		contentPane.add(ixr2load);
		
		JButton ixr3load = new JButton("Load");
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr3load, 0, SpringLayout.NORTH, lblixr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr3load, 6, SpringLayout.EAST, ixr3field);
		contentPane.add(ixr3load);
		
		JLabel lblPC = new JLabel("PC");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblPC, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblPC, 90, SpringLayout.EAST, gpr0load);
		contentPane.add(lblPC);
		
		JLabel lblMAR = new JLabel("MAR");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblMAR, 0, SpringLayout.NORTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblMAR, 0, SpringLayout.WEST, lblPC);
		contentPane.add(lblMAR);
		
		JLabel lblMBR = new JLabel("MBR");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblMBR, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblMBR, 0, SpringLayout.WEST, lblPC);
		contentPane.add(lblMBR);
		
		JLabel lblIR = new JLabel("IR");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblIR, 0, SpringLayout.NORTH, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblIR, 0, SpringLayout.WEST, lblPC);
		contentPane.add(lblIR);
		
		pcfield = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, pcfield, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, pcfield, 17, SpringLayout.EAST, lblPC);
		sl_contentPane.putConstraint(SpringLayout.EAST, pcfield, 217, SpringLayout.EAST, lblPC);
		pcfield.setColumns(10);
		contentPane.add(pcfield);
		
		mbrfield = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, mbrfield, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, mbrfield, 0, SpringLayout.WEST, pcfield);
		sl_contentPane.putConstraint(SpringLayout.EAST, mbrfield, 200, SpringLayout.WEST, pcfield);
		mbrfield.setColumns(10);
		contentPane.add(mbrfield);
		
		irfield = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, irfield, 0, SpringLayout.NORTH, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, irfield, 0, SpringLayout.WEST, pcfield);
		sl_contentPane.putConstraint(SpringLayout.EAST, irfield, 0, SpringLayout.EAST, pcfield);
		irfield.setColumns(10);
		contentPane.add(irfield);
		
		marfield = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, marfield, 0, SpringLayout.NORTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, marfield, 0, SpringLayout.WEST, pcfield);
		sl_contentPane.putConstraint(SpringLayout.EAST, marfield, 0, SpringLayout.EAST, pcfield);
		marfield.setColumns(10);
		contentPane.add(marfield);
		
		JButton pcload = new JButton("Load");
		sl_contentPane.putConstraint(SpringLayout.NORTH, pcload, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, pcload, 6, SpringLayout.EAST, pcfield);
		contentPane.add(pcload);
		
		JButton marload = new JButton("Load");
		sl_contentPane.putConstraint(SpringLayout.NORTH, marload, 0, SpringLayout.NORTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, marload, 6, SpringLayout.EAST, marfield);
		contentPane.add(marload);
		
		JButton mbrload = new JButton("Load");
		sl_contentPane.putConstraint(SpringLayout.NORTH, mbrload, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, mbrload, 6, SpringLayout.EAST, mbrfield);
		contentPane.add(mbrload);
	}
}
