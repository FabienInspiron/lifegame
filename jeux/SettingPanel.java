package jeux;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class SettingPanel extends JFrame {
	private JTextField txflRowNumber;
	private JTextField txflLineNumber;
	private JTextField txflStepNumber;
	
	
	/**
	 * Create the option panel.
	 */
	public SettingPanel() {
		super();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    setSize(300, 200);
		setTitle("Jeu de la vie");
		JLabel lblNombreDeColonnes = new JLabel("Nombre de colonnes");
		JLabel lblNombreDeLignes = new JLabel("Nombre de lignes");
		JLabel lblNombreDtape = new JLabel("Nombre d'étape");
		
		txflRowNumber = new JTextField();
		txflRowNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txflRowNumber.setText("50");
		txflRowNumber.setColumns(10);
		
		txflLineNumber = new JTextField();
		txflLineNumber.setText("50");
		txflLineNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txflLineNumber.setColumns(10);
		
		txflStepNumber = new JTextField();
		txflStepNumber.setText("50");
		txflStepNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txflStepNumber.setColumns(10);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Parse row number
				int rowNumber = Integer.parseInt(txflRowNumber.getText());
				int lineNumber = Integer.parseInt(txflLineNumber.getText());
				int stepNumber = Integer.parseInt(txflStepNumber.getText());
				
				Simulator sim = new SimulatorLinear(rowNumber, lineNumber, stepNumber);
			}
		});

		JPanel jContentPane = new JPanel();     
		
		GroupLayout groupLayout = new GroupLayout(jContentPane);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(102)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNombreDeColonnes)
						.addComponent(lblNombreDeLignes)
						.addComponent(lblNombreDtape))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(txflStepNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txflLineNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txflRowNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(77, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap(204, Short.MAX_VALUE)
					.addComponent(btnOk)
					.addGap(192))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(55)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNombreDeColonnes)
						.addComponent(txflRowNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNombreDeLignes)
						.addComponent(txflLineNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNombreDtape)
						.addComponent(txflStepNumber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(41)
					.addComponent(btnOk)
					.addContainerGap(86, Short.MAX_VALUE))
		);
		//setLayout(groupLayout);
		
		 
		jContentPane.setLayout(groupLayout);
		setContentPane(jContentPane);
		
		pack();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	        	 SettingPanel thisClass = new SettingPanel();
	               thisClass.setVisible(true);
	           }
	      });
	}
}
