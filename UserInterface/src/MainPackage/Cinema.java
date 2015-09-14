package MainPackage;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class Cinema extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	client Client;
	JButton adminButton;
	JTextField adminField;
	ArrayList<String[]> cinemas;
	ArrayList<String> cinemaNames;
	int currentBox;
	JScrollPane cinemaScroll;
	JScrollPane filmScroll;
	JScrollPane commentScroll;
	JFrame thisFrame;
	JFrame filmFrame = null;
	JFrame commentFrame = null;
	
	Cinema(final client Client, boolean isAdmin) throws IOException
	{
		super("Cinema");
		this.Client = Client;
		thisFrame = this;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		cinemas = Client.countCinema();
		
		
		adminButton = new JButton("Add data");
		adminButton.setVisible(isAdmin);
		adminButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				String path = adminField.getText() ;
				if (!path.endsWith(".xml"))
				{
					JOptionPane.showMessageDialog(new JFrame(), "It's not xml file", "Error", JOptionPane.ERROR_MESSAGE);
				} else 
				{
					if (new File(path).exists())
					{
						parseXML xml = new parseXML(path);
						try {
							Client.addFilms(xml.cinema, xml.cinemaName, xml.filmNames, xml.films, xml.countFilms, xml.actors, xml.actorCount);
						} catch (IOException | InterruptedException e) {
							e.printStackTrace();
						}
						adminField.setText("");
					} else
					{
						JOptionPane.showMessageDialog(new JFrame(), "No such file", "Error", JOptionPane.ERROR_MESSAGE);						
					}
				}
			}
		});
		
		adminField = new JTextField(15);
		adminField.setVisible(isAdmin);
		
		JPanel pane = new JPanel(new FlowLayout());
		pane.add(adminField);
		pane.add(adminButton);
		
		JPanel mainPanel = new JPanel(new GridLayout(0,1));
		cinemaScroll = new JScrollPane(mainPanel);
		cinemaScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		Box cinemaLabel = Box.createVerticalBox();
		
		mainPanel.add(pane);
		
		Font font = new Font("Times New Roman", Font.PLAIN, 30);
		JLabel label = new JLabel("Welcome!");
		label.setAlignmentX(CENTER_ALIGNMENT);
		label.setAlignmentY(CENTER_ALIGNMENT);
		label.setFont(font);
		label.setForeground(Color.GREEN);
		
		cinemaLabel.add(label);
		
		mainPanel.add(cinemaLabel);
		
		
		
		for (int i = 0; i < cinemas.size(); ++i)
		{
			String[] lst = cinemas.get(i);
			JLabel name = new JLabel("Cinema: " + lst[1]);
			JLabel address = new JLabel("Address: " + lst[2]);
			name.setAlignmentX(CENTER_ALIGNMENT);
			address.setAlignmentX(CENTER_ALIGNMENT);
			
			
			Box cinema = Box.createVerticalBox();
			cinema.add(name);
			
			cinema.add(Box.createVerticalStrut(10));
			cinema.add(address);
			
			cinema.add(Box.createVerticalStrut(10));
			JButton seeFilms = new JButton("Films");
			seeFilms.setAlignmentX(CENTER_ALIGNMENT);
			
			seeFilms.addActionListener(new MyActionListener(lst[1]));
			
			Border black = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
			cinema.setBorder(black);
			
			cinema.add(seeFilms);
			
			cinema.setAlignmentX(CENTER_ALIGNMENT);
			mainPanel.add(cinema);
		}
		setContentPane(cinemaScroll);
		pack();
		setResizable(false);
		this.setSize(400, 400);
		this.setVisible(true);
	}
	class MyActionListener implements ActionListener
	{
		ArrayList<String[]> films;
		String name;
		MyActionListener(String name)
		{
			this.name = name;
		}
		public void actionPerformed(ActionEvent ae)
		{
			JPanel mainPanel = new JPanel(new GridLayout(0,1));
			if (filmFrame == null) filmFrame = new JFrame("Films");
			try {
				films = Client.getFilms(name);
				filmScroll = new JScrollPane(mainPanel);
				filmScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				for (int i = 0; i < films.size(); ++i)
				{
					mainPanel.add(createFilmBox(films.get(i)));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			JButton backButton = new JButton("Back");
			backButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae)
				{
					thisFrame.setVisible(true);
					filmFrame.setVisible(false);
				}
			});
			
			JPanel pane = new JPanel(new FlowLayout());
			pane.add(backButton);
			
			
			mainPanel.add(pane);
			
			filmFrame.setContentPane(filmScroll);
			filmFrame.setVisible(true);
			filmFrame.setSize(400, 500);
			thisFrame.setVisible(false);
			filmFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
	}
	private Box createFilmBox(String[] filmInfo)
	{
		Box film = Box.createVerticalBox();
		ArrayList<String> actors = null;
		try {
			actors = Client.getActorsFilm(filmInfo[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JLabel label = new JLabel("Name: " + filmInfo[0]);
		//label.setAlignmentX(CENTER_ALIGNMENT);
		
		film.add(label);
		film.add(Box.createVerticalStrut(10));
		
		film.add(new JLabel("Rating: " + filmInfo[1]));
		film.add(Box.createVerticalStrut(10));
		
		film.add(new JLabel("Description: " + filmInfo[2]));
		film.add(Box.createVerticalStrut(10));
		
		film.add(new JLabel("Genre: " + filmInfo[3]));
		film.add(Box.createVerticalStrut(10));
		
		film.add(new JLabel("Release Date: " + filmInfo[4]));
		film.add(Box.createVerticalStrut(10));
		
		String actorString = "";
		for (int i = 0; i < actors.size() - 1; ++i)
		{
			actorString += actors.get(i) + ", ";
		}
		actorString += actors.get(actors.size() - 1);
		film.add(new JLabel("Actors: " + actorString));
		Border black = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		
		JButton commentButton = new JButton("Go to comments >>");
		commentButton.addActionListener(new commentsActionListener(filmInfo[0]));
		film.add(commentButton);
		film.setBorder(black);
		return film;
	}
	class commentsActionListener implements ActionListener
	{
		String name;
		ArrayList<String[]> comments;
		JButton back, add;
		commentsActionListener(String filmName)
		{
			this.name = filmName;
		}
		public void actionPerformed(ActionEvent ae)
		{
			back = new JButton("Back");
			add = new JButton("Add");
			final JPanel mainPanel = new JPanel(new GridLayout(0,1));
			JPanel butPanel = new JPanel(new FlowLayout());
			back.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae)
				{
					commentFrame.setVisible(false);
					filmFrame.setVisible(true);
				}
			});
			add.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent ae)
				{
					final JFrame frame = new JFrame("Add comment");
					JPanel pane = new JPanel(new FlowLayout());
					
					String[] items = 
						{
							"1",
							"2",
							"3",
							"4",
							"5",
							"6",
							"7",
							"8",
							"9",
							"10"
						};
					final JComboBox<String> rating = new JComboBox<String>(items);
					
					final JTextArea comment = new JTextArea(10, 20);
					comment.setLineWrap(true);
					final JButton submit = new JButton("Submit");
					submit.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent ae)
						{
							if (comment.getText().length() >= 139)
							{
								JOptionPane.showMessageDialog(new JFrame(), "Your comment must be less than 140 symbols", "Warning", JOptionPane.WARNING_MESSAGE);
							} else
								try {
									Client.addComment(name, comment.getText(), rating.getSelectedItem().toString() );
									String [] newComment = new String[7];
									newComment[0] = "" + Client.UserID;
									newComment[1] = comment.getText();
									newComment[2] = rating.getSelectedItem().toString();
									newComment[3] = newComment[4] = "0";
									newComment[6] = Client.clientName;
									newComment[5] = "0";
									mainPanel.add(createCommentBox(newComment));
									commentFrame.repaint();
									frame.dispose();
									submit.setEnabled(false);
								} catch (IOException e) {
									e.printStackTrace();
								}
						}
					});
					Border black = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
					comment.setBorder(black);
					
					pane.add(comment);
					pane.add(rating);
					pane.add(submit);
					frame.setContentPane(pane);
					frame.setSize(250,250);
					frame.setVisible(true);
					frame.setResizable(false);
				}
			});
			butPanel.add(back);
			butPanel.add(add);
			butPanel.setSize(100, 400);
			
			mainPanel.add(butPanel);
			if (commentFrame == null) commentFrame = new JFrame("Comments");
			try
			{
				comments = Client.getComments(name);
				commentScroll = new JScrollPane(mainPanel);
				commentScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				for (int i = 0; i < comments.size(); ++i)
				{
					Box box = createCommentBox(comments.get(i));
					mainPanel.add(box);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			commentFrame.setContentPane(commentScroll);
			commentFrame.setSize(400, 400);
			commentFrame.setVisible(true);
			filmFrame.setVisible(false);
			commentFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
	}
	private Box createCommentBox(final String[] comment) throws IOException
	{
		Box general = Box.createHorizontalBox();
		
		Box commentBox = Box.createVerticalBox();
		
		JLabel label = new JLabel("Autor: " + comment[6]);
		label.setAlignmentX(0);
		commentBox.add(label);
		commentBox.add(Box.createVerticalStrut(10));
		
		JLabel label2 = new JLabel("Comment: " + Analyze(comment[1]));
		commentBox.add(label2);
		commentBox.add(Box.createVerticalStrut(10));
		
		commentBox.add(new JLabel("Rating: " + comment[2]));
		commentBox.add(Box.createVerticalStrut(10));
		
		
		Box hor = Box.createHorizontalBox();
		
		boolean visible = Client.isVisible(comment[1], comment[0]);
		
		final JButton like = new JButton(comment[3]);
		final JButton dislike = new JButton(comment[4]);
		like.setForeground(Color.green);
		like.setEnabled(visible);
		
		like.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				try {
					System.out.println(comment[5]);
					Client.addCommentIntoDB(comment[5], 1);
					like.setEnabled(false);
					dislike.setEnabled(false);
					int likes = Integer.parseInt(like.getText());
					likes++;
					like.setText("" + likes);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		dislike.setForeground(Color.red);
		dislike.setEnabled(visible);
		
		dislike.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				try {
					Client.addCommentIntoDB(comment[5], 0);
					like.setEnabled(false);
					dislike.setEnabled(false);
					int dislikes = Integer.parseInt(dislike.getText());
					dislikes++;
					dislike.setText("" + dislikes);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		hor.add(like);
		hor.add(Box.createHorizontalStrut(10));
		hor.add(dislike);
		
		general.add(commentBox);
		general.add(hor);
		Border black = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		general.setBorder(black);
		return general;
		
	}
	private String Analyze(String comment)
	{
		String result = "";
		int j = 0;
		for(int i = 0; i < comment.length(); ++i)
		{
			result += comment.charAt(i);
			++j;
			if (j == 50)
			{
				result += "\n";
				j = 0;
			}
		}
		return result;
	}
}
