import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Canvas;
import java.awt.Color;

//Built using http://download.eclipse.org/windowbuilder/WB/release/R201209281200/4.2/
public class Main_GUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtGuess;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_GUI frame = new Main_GUI();
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
	public Main_GUI() {
		setTitle("Hangman");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtGuess = new JTextField();
		txtGuess.setText("Guess");
		txtGuess.setBounds(125, 231, 150, 20);
		contentPane.add(txtGuess);
		txtGuess.setColumns(10);
		
		JButton btnGuess = new JButton("Guess");
		btnGuess.setBounds(335, 230, 89, 23);
		contentPane.add(btnGuess);
		
		Canvas canvasHangman = new Canvas();
		canvasHangman.setBackground(Color.WHITE);
		canvasHangman.setForeground(new Color(255, 255, 255));
		canvasHangman.setBounds(125, 10, 200, 210);
		contentPane.add(canvasHangman);
	}
}
