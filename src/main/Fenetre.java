package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import wikipediapckg.pageRank.arraysRanker.ArrayPageRank;

import java.util.Collections;
import java.util.ListIterator;
import java.util.Map;

public class Fenetre extends JFrame {
	private static final long serialVersionUID = 1L;

	public JPanel container = new JPanel();
	public JTextField jtf = new JTextField("");
	public JLabel label = new JLabel("");
	public JPanel bottom = new JPanel( new GridLayout(0,1,0,10) );
	private ArrayPageRank apr;
	private Map<Integer,String> titleById;

	//la fonction à activer qunad on click sur bouton
	public void result(JPanel bottom,String query){
		bottom.removeAll();
		bottom.revalidate();
		bottom.repaint();
		
		// si pas de recherche, on affiche les 30 premiers r�sultats
		/*if(query.isEmpty()) {
			final int NUM_PAGES = 20;
			double[] sorted = apr.pageranks.clone();
			Arrays.sort(sorted);
			for (int i = 0; i < NUM_PAGES; i++) {
				for (int j = 0; j < sorted.length; j++) {
					if (apr.pageranks[j] == sorted[sorted.length - 1 - i]) {
						System.out.printf("  %.3f  %s%n", Math.log10(apr.pageranks[j]), titleById.get(j));
						add_panel(bottom, titleById.get(j), new ArrayList<String>(), String.valueOf(apr.pageranks[j]));
						break;
					}
				}
			}
		}*/
		
		// on va rechercher les liens
		ArrayList<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");
		list.add("e");
		list.add("g");
		list.add("h");
		list.add("i");
		list.add("j");
		if( query.equals("France") ){
			add_panel(bottom,"D.org",list.subList(1, 5),"8");
			add_panel(bottom,"Etats-Unis.org",list.subList(2, 5),"9");
			add_panel(bottom,"France.org",list,"10");
		}
		else if( query.equals("a") ){
			add_panel(bottom,"Paris",list,"10");
			add_panel(bottom,"Londres",list,"8");			
			add_panel(bottom,"France",list.subList(2, 5),"8");
			add_panel(bottom,"Etats-Unis",list.subList(1, 5),"7");
			add_panel(bottom,"D-Day",list,"5");
		}		
		else{}
	}
	
	//la fonction ajouter paneau à bottom, ie , au panneau du dessous
	public void add_panel(JPanel bottom,final String nom,final java.util.List<String> list,String pagerank){
		if( nom.length()>0 ){
			int n = list.size() ;
			JLabel labelsource = new JLabel("<html>Nom:"+nom+"<br>Nombre de liens:"+n+"<br>pagerank:"+pagerank+"</html>");
			labelsource.setBorder(BorderFactory.createLineBorder(Color.black));
			labelsource.setPreferredSize(new Dimension(600,70));
			labelsource.setHorizontalAlignment(JLabel.LEFT);
			labelsource.addMouseListener(new MouseAdapter()  
			{  
			    public void mouseClicked(MouseEvent e)
			    {  
			        JFrame jf=new JFrame(nom);
			        jf.setBackground(Color.BLACK);
			        jf.setSize(new Dimension(400,100));
				    jf.setBackground(Color.LIGHT_GRAY);
			        final JTextArea txtArea = new JTextArea();
			        final JScrollPane scrollPane = new JScrollPane(txtArea);
				    txtArea.setBackground(Color.LIGHT_GRAY);
			        jf.add(scrollPane);
                    txtArea.setText( "Link present in the page "+nom+":\n" );
	                for(int i=0;i<list.size();i++)
	                {
	                    txtArea.setText( txtArea.getText()+" \n "+list.get(i) );
	                }
			        jf.setVisible(true);
			        jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			    }  
			});
			bottom.add(labelsource,0);
		}
	}
	
	//la classe fenetre
	public Fenetre(){
		//création du panneau des options
		this.creerPanneau();
		this.creerres();
	}
	
	public Fenetre(ArrayPageRank apr, Map<Integer,String> titleById){
		//création du panneau des options
		this.creerPanneau();
		this.creerres();
		this.apr = apr;
		this.titleById = titleById;
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
		bouton.setPreferredSize(new Dimension(100, 33));
		bouton.setBackground(Color.WHITE);
		bouton.setMargin(new Insets(0,0,0,0));
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

        bottom.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		bottom.setBackground(Color.LIGHT_GRAY);
        
		container.add(scrollPane);
	    this.setVisible(true);            
	}
	
	//la fonction main
	public static void main(String[] args){
		Fenetre fen = new Fenetre();
	}
}