//Mine sweeper front end class
//Wenkan Zhu and Anas

import java.awt.event.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

public class Minesweeper extends JFrame implements ActionListener
{
	private JMenuBar menubar;							//Menu bar in top of window
	private JMenu Game;									//Game menu
	private JMenu Help;									//Help menu
	private JMenuItem Reset;							//Reset menu item
	private JMenuItem Topten;							//Top ten menu item
	private JMenuItem Clearrecords;						//Clear records menu item
	private JMenuItem eXit;								//exit menu item
	private JMenuItem heLp;								//help menu item
	private JMenuItem About;							//about menu item
	
	private JPanel head;								//Place for head button and text fields
	private JPanel panel;								//Place for mine blocks
	private GridLayout layout;							//Make the blocks align
	private JLabel countText;							//Text notice for count
	private JLabel count;								//Show not marked mines count
	private JLabel timeText;							//Text notice for time
	private JLabel time;								//Show elapsed game time
	private JButton reset;								//Reset button
	private JButton [][] squares = new JButton[10][10];	//Block buttons
	
	private ImageIcon normal = new ImageIcon("icon/button_normal.gif");		//Not cleared button view
	private ImageIcon marked = new ImageIcon("icon/button_flag.gif");		//Marked as mined button view
	private ImageIcon question = new ImageIcon("icon/button_question.gif");	//Marked as question button view
	private ImageIcon bomb = new ImageIcon("icon/button_bomb_pressed.gif");	//Exploded mine view
	
	private boolean gaming = true;						//Toggle if the game operation is allowed
	private boolean started = false;					//Toggle if game started to show time
	private long startTime;								//Time started game action
	private int score = 0;								//Store the final score of this round
	
	private static File record = new File("record.txt");//File record top 10 scores
	
	private static LinkedList<String> scores = new LinkedList<String>();	//top players in memory
	
	private static Game game;							//The game back end object
	
	public Minesweeper()
	{
		super("Minesweeper");
		
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		
		//Set up menu bar
		menubar = new JMenuBar();
		c.add(menubar);
		
		Game = new JMenu("Game");
		Game.setMnemonic('G');
		menubar.add(Game);
		
		Help = new JMenu("Help");
		Help.setMnemonic('H');
		menubar.add(Help);
		
		Reset = new JMenuItem("Reset");
		KeyStroke ctrlR = KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		Reset.setAccelerator(ctrlR);
		Reset.addActionListener(this);
		Game.add(Reset);
		
		Topten = new JMenuItem("Top ten");
		KeyStroke ctrlT = KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		Topten.setAccelerator(ctrlT);
		Topten.addActionListener(this);
		Game.add(Topten);
		
		Clearrecords = new JMenuItem("Clear records");
		KeyStroke ctrlC = KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		Clearrecords.setAccelerator(ctrlC);
		Clearrecords.addActionListener(this);
		Game.add(Clearrecords);
		
		eXit = new JMenuItem("eXit");
		KeyStroke ctrlX = KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		eXit.setAccelerator(ctrlX);
		eXit.addActionListener(this);
		Game.add(eXit);
		
		heLp = new JMenuItem("heLp");
		KeyStroke ctrlL = KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		heLp.setAccelerator(ctrlL);
		heLp.addActionListener(this);
		Help.add(heLp);
		
		About = new JMenuItem("About");
		KeyStroke ctrlA = KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		About.setAccelerator(ctrlA);
		About.addActionListener(this);
		Help.add(About);
		
		setJMenuBar(menubar);
		
		//Set up info display and reset button
		head = new JPanel();
		c.add(head);
		
		countText = new JLabel("Mines:");
		head.add(countText);
		
		count = new JLabel("10");
		head.add(count);
		
		reset = new JButton("Reset");
		reset.addActionListener(this);
		head.add(reset);
		
		timeText = new JLabel("Time:");
		head.add(timeText);
		
		time = new JLabel("0");
		head.add(time);
		
		//The main game area
		layout = new GridLayout(10,10);
		panel = new JPanel();
		panel.setLayout(layout);
		c.add(panel);
		
		//Blocks added
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				squares[i][j] = new JButton();
				squares[i][j].setPreferredSize(new Dimension(20, 20));
				squares[i][j].addMouseListener(new MouseClickHandler());
				squares[i][j].setIcon(normal);
				panel.add(squares[i][j]);
			}
		}
		
		setSize(230,300);
		show();
		
		//Set timer to refresh time count
		Timer gameTimer = new Timer(500, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				time.setText(""+getScore());
			}
		});
		
		gameTimer.start();
	}
	
	public void actionPerformed(ActionEvent e)
	//All the actions besides game play
	{
		if(e.getSource()==reset || e.getSource()==Reset)
			resetGame();
		if(e.getSource()==eXit)
			System.exit(0);
		if(e.getSource()==Topten)							
		{
			String display = "Top 10 records:\n(name;time)\n";
			
			for(int i=0; i<10 && i<scores.size();i++)
			{
				display += scores.get(i)+"\n";
			}
			
			JOptionPane.showMessageDialog(null,display);
			
		}
		if(e.getSource()==Clearrecords)
		{
			scores = new LinkedList<String>();
			record.delete();
		}
		if(e.getSource()==heLp)
			JOptionPane.showMessageDialog(null, "The rules in Minesweeper are simple:\n"
					+ "Uncover a mine, and the game ends.\n"
					+ "Uncover an empty square, and you keep playing.\n"
					+ "Uncover a number, and it tells you how many mines lay hidden in surrounding squares.");
		if(e.getSource()==About)
			JOptionPane.showMessageDialog(null, "Programmed by Wenkan and Anas, 2016.02.22");
	}
	
	public static void main(String[] args)
	{
		
		Minesweeper app = new Minesweeper();
		
		game = new Game();
		
		if(record.exists())	//load score records from file
		{
			try
			{
				Scanner reader = new Scanner(new FileInputStream(record));
				while (reader.hasNextLine())
				{
					scores.add(reader.nextLine());
				}
			}
			catch(Exception e)
			{
				
			}
		}
		
		app.addWindowListener(new WindowAdapter()
		{
			public void windowClosing (WindowEvent e)
			{
				System.exit(0);
			}
		});
		
	}
	
	public void resetGame()
	//Restart a new game
	{
		game = new Game();
		gaming = true;
		started = false;
		score = 0;
		//reset.setText("Reset");
		refreshGame();
	}
	
	public void refreshGame()
	//Refresh every botton's display
	{
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				String icon = game.getDisplay(i,j);
				
				if(icon.equals("M"))
					squares[i][j].setIcon(marked);
				else if(icon.equals("?"))
					squares[i][j].setIcon(question);
				else if(icon.equals("0"))
				{
					squares[i][j].setIcon(null);
					squares[i][j].setText("");
				}
				else if(icon.equals(""))
				{
					squares[i][j].setIcon(normal);
					squares[i][j].setText("");
				}
				else
				{
					squares[i][j].setIcon(null);
					squares[i][j].setText(icon);
				}
			}
		}
		
		count.setText(""+game.remainingMines());
		
		repaint();
	}
	
	public void showMines()
	//Show all mines when game lose
	{
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				if(game.getBlock(i, j).checkMined())
				{
					//squares[i][j].setText("");
					squares[i][j].setIcon(bomb);
				}
			}
		}
		
		repaint();
	}
	
	public void startTimer()
	//Start the game scoring if not started
	{
		if(gaming && !started)
		{
			started = true;
			startTime = System.currentTimeMillis();
		}
	}
	
	public int getScore()
	//Return the score number in any case
	{
		if(started && gaming)
		//Game in progress
			return (int)((System.currentTimeMillis()-startTime)/1000);
		else if(started && !gaming)
		//Game won, stop refresh
			return score;
		else
			return 0;
	}
	
	public void ifWin()
	//Do everything needed after user win the game here
	{
		if(game.getUncleared()==10)
		//Win the game
		{
			score = getScore();	//fix the score
			gaming = false;
			count.setText("0");
			//reset.setText("Win!");
			//JOptionPane.showMessageDialog(null, "You win!\nYour score is "+score);
			
			//Update the score records in memory
			if(scores.size()==0)
			//First time win
			{
				String name = JOptionPane.showInputDialog("You are in the top 10 scores!\nInput your name:");
				String entry = name + ";" + score;
				scores.add(entry);
			}
			else if(scores.size()>0)
			{
				int i=0;
				
				while(i<scores.size())
				//Go through the records to find if qualify for top 10
				{
					String[] info = scores.get(i).split(";");
					int value = Integer.parseInt(info[1]);
					
					if(score <= value && i<10)
					{
						String name = JOptionPane.showInputDialog("You are in the top 10 scores!\nInput your name:");
						String entry = name + ";" + score;
						
						scores.add(i,entry);
						
						i = 11;
						
						break;
					}
					
					i+=1;
				}
				
				if(i<10)
				//For records shorter than 10, append at last
				{
					String name = JOptionPane.showInputDialog("You are in the top 10 scores!\nInput your name:");
					String entry = name + ";" + score;
					
					scores.addLast(entry);
				}
				
			}
			
			//Save top 10 records, overwrite everytime
			try
			{
				FileWriter saver = new FileWriter(record, false);
				BufferedWriter writer = new BufferedWriter(saver);
				
				for(int i=0; i<10 && i<scores.size();i++)
				{
					writer.write(scores.get(i));
					writer.newLine();
				}
				
				writer.close();
				
			}
			catch(Exception e)
			{
				
			}
			
			
		}
	}

private class MouseClickHandler extends MouseAdapter
{
	public void mouseClicked(MouseEvent e)
	{
		
		if(gaming && SwingUtilities.isLeftMouseButton(e))
		//Left click
		{
			for(int i=0;i<10;i++)
			{
				for(int j=0;j<10;j++)
				{
					if(e.getSource() == squares[i][j])
					//Find the button clicked
					{
						startTimer();
						if(game.leftClick(i,j))
						//Do left click, if exploded, do:
						{
							gaming = false;
							showMines();
							//reset.setText("Boom!");
							//JOptionPane.showMessageDialog(null, "You lose");
						}
					}
				}
			}
			
			if(gaming)
				refreshGame();
			
			ifWin();
		}
		
		if(gaming && SwingUtilities.isRightMouseButton(e))
		//Right click
		{
			for(int i=0;i<10;i++)
			{
				for(int j=0;j<10;j++)
				{
					if(e.getSource() == squares[i][j])
					{
						game.rightClick(i, j);
					}
				}
			}
			
			refreshGame();
		}
		
	}
}
	
	
}
