package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
	public JPanel bottom = new JPanel( new GridLayout(0,1,0,10) );

	//la fonction à activer qunad on click sur bouton
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
		else if( query.equals("a") ){
			add_panel(bottom,"Paris.org","Paris","8");
			add_panel(bottom,"Londres.org","Londres","8");			
			add_panel(bottom,"France.org","France","10");
			add_panel(bottom,"Etats-Unis.org","Etats-unis","9");
			add_panel(bottom,"D.org","D","8");
			add_panel(bottom,"France.org","France","10");
			add_panel(bottom,"Etats-Unis.org","Etats-unis","9");
			add_panel(bottom,"D.org","D","8");
			add_panel(bottom,"Paris.org","Paris","8");
			add_panel(bottom,"Londres.org","Londres","8");			
			add_panel(bottom,"France.org","France","10");
			add_panel(bottom,"Etats-Unis.org","Etats-unis","9");
			add_panel(bottom,"D.org","D","8");
			add_panel(bottom,"France.org","France","10");
			add_panel(bottom,"Etats-Unis.org","Etats-unis","9");
			add_panel(bottom,"D.org","D","8");
		}		
		else{}
	}
	
	//la fonction ajouter paneau à bottom, ie , au panneau du dessous
	public void add_panel(JPanel bottom,String source,String mot,String pagerank){
		if( mot.length()>0 ){
			JLabel labelsource = new JLabel("<html>"+source+"<br>"+pagerank+"<br>"+mot+"</html>");
			labelsource.setBorder(BorderFactory.createLineBorder(Color.black));
			labelsource.setPreferredSize(new Dimension(600,70));
			labelsource.setHorizontalAlignment(JLabel.LEFT);
			bottom.add(labelsource,0);
		}
	}
	
	//la classe fenetre
	public Fenetre(){
		//création du panneau des options
		this.creerPanneau();
		this.creerres();
	}

	//le panneau du haut avec la barre de recherche et le bouton	
	public void creerPanneau(){
		//le panneau du haut avec la barre de recherche et le bouton
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
				//fonction bouton
				//update res
				//recuperer le contenu de la barre de recherche
				String query = jtf.getText();
				//effectuer la recherche
				result(bottom,query);
	         }
		});
		
	    this.setVisible(true);            
	}

	//le panneau du bas avec les resultats
	public void creerres(){
		//le panneau du bas avec les resultats
	    //jpanel bottom
		
		JScrollPane scrollPane = new JScrollPane(bottom);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(50, 30, 300, 50);

        bottom.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		bottom.setBackground(Color.LIGHT_GRAY);
        
		container.add(scrollPane);
	    this.setVisible(true);            
	}
	
	//la fonction main
	public static void main(String[] args){
		Fenetre fen = new Fenetre();
	}
}
