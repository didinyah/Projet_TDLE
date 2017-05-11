import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Fenetre extends JFrame {
	private static final long serialVersionUID = 1L;

	public JPanel container = new JPanel();
	public JTextField jtf = new JTextField("");
	public JLabel label = new JLabel("");
	public JPanel bottom = new JPanel( new FlowLayout(FlowLayout.LEFT) );

	public void result(JPanel bottom,String query){
		bottom.removeAll();
		bottom.revalidate();
		bottom.repaint();
		if( query.equals("France") ){
			add_panel(bottom,"France.org","France","10");
			add_panel(bottom,"Etats-Unis.org","Etats-unis","9");
			add_panel(bottom,"D.org","D","8");
		}
		else if( query.equals("Paris") ){
			add_panel(bottom,"Paris.org","Paris","8");
			add_panel(bottom,"Londres.org","Londres","8");			
		}
		else{}
	}
	
	public void add_panel(JPanel bottom,String source,String mot,String pagerank){
		if( mot.length()>0 ){
			JLabel labelsource = new JLabel("<html>"+source+"<br>"+pagerank+"<br>"+mot+"</html>");
			labelsource.setBorder(BorderFactory.createLineBorder(Color.black));
			labelsource.setPreferredSize(new Dimension(600,70));
			labelsource.setHorizontalAlignment(JLabel.LEFT);
			bottom.add(labelsource);
		}
	}
	
	public Fenetre(){
		//création du panneau des options
		this.creerPanneau();
		this.creerres();
	}
		
	public void creerPanneau(){
		this.setTitle("Search wiki");
	    this.setSize(900, 600);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setLocationRelativeTo(null);
	    container.setBackground(Color.white);
	    container.setLayout(new BorderLayout());
	    //jpanel top
	    JPanel top = new JPanel();
	    top.setBackground(Color.DARK_GRAY);
	    Font police = new Font("Arial", Font.BOLD, 14);
	    jtf.setFont(police);
	    jtf.setPreferredSize(new Dimension(300, 33));
	    jtf.setForeground(Color.BLUE);
	    top.add(label);
	    top.add(jtf);
	    container.add(top, BorderLayout.NORTH);
		
	    this.setContentPane(container);
	    //bouton
		JButton bouton = new JButton("search");
		bouton.setPreferredSize(new Dimension(100, 30));
		top.add(bouton);
		bouton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//update res
				bottom.removeAll();
				String query = jtf.getText();
				result(bottom,query);
				creerres();
	         }
		});
		
	    this.setVisible(true);            
	}

	public void creerres(){
	    //jpanel bottom
		bottom.setBackground(Color.LIGHT_GRAY);
		container.add(bottom);
	    this.setVisible(true);            
	}
	
	public static void main(String[] args){
		Fenetre fen = new Fenetre();
	}
}
