import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//Built using http://download.eclipse.org/windowbuilder/WB/release/R201209281200/4.2/
public class Main_GUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtGuess;
	public static int imgPointer = 0;

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
	
	public static int getImgPointer(){
		return imgPointer;
	}
	public static void setImgPointer(int i){
		imgPointer=i;
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
		
		final JPanel panelHangman = new JPanel();
		panelHangman.setToolTipText("Hangman");
		panelHangman.setBounds(99, 11, 200, 200);
		

		
		final ImageIcon img[] = new ImageIcon[7];
		img[0] = new ImageIcon("src/images/Hangman_1.png");
		img[1] = new ImageIcon("src/images/Hangman_2.png");
		img[2] = new ImageIcon("src/images/Hangman_3.png");
		img[3] = new ImageIcon("src/images/Hangman_4.png");
		img[4] = new ImageIcon("src/images/Hangman_5.png");
		img[5] = new ImageIcon("src/images/Hangman_6.png");
		img[6] = new ImageIcon("src/images/Hangman_7.png");
		
		final JLabel label = new JLabel("", img[imgPointer], JLabel.CENTER);
		panelHangman.add( label, BorderLayout.CENTER );
		
		
		contentPane.add(panelHangman);
		
		
		JButton btnGuess = new JButton("Guess");
		btnGuess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelHangman.removeAll();
	            panelHangman.revalidate();
				imgPointer++;
				
				label.setIcon(img[imgPointer]);
				panelHangman.add( label, BorderLayout.CENTER );
				
				panelHangman.repaint();
			}
		});
		btnGuess.setBounds(335, 230, 89, 23);
		contentPane.add(btnGuess);
	}
}
