import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextAnalyzerGUI {

	/*
	 * Declare the Variables
	 */
	private JFrame frmTextAnalyzer;
	private JComboBox selectBook;
	private JComboBox selectChapter;
	private JLabel lblSelectChapter;
	private List<String> bookList;
	private JTextArea outputConsole;
	private String[] stopWords;
	private JRadioButton rdbtnWords, rdbtnUnique, rdbtnCommon, rdbtnAppearance;
	private MyCanvas canvas;
	private int chapterCount, option;
	private int[] wordCount;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TextAnalyzerGUI window = new TextAnalyzerGUI();
					window.frmTextAnalyzer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TextAnalyzerGUI() {
		initialize();
		getStopWords();
		getBookDetails();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTextAnalyzer = new JFrame();
		frmTextAnalyzer.setResizable(false);
		frmTextAnalyzer.setTitle("Text Analyzer");
		frmTextAnalyzer.setBounds(100, 100, 1053, 389);
		frmTextAnalyzer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTextAnalyzer.getContentPane().setLayout(null);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * GUI Components
		 */
		JLabel lblNewLabel = new JLabel("Select a Book");
		lblNewLabel.setBounds(37, 46, 98, 14);
		frmTextAnalyzer.getContentPane().add(lblNewLabel);

		/*
		 * ComboBox To select a book
		 */
		selectBook = new JComboBox();
		/*
		 * Updates the chapter List Once the book is Selected
		 */
		selectBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (selectBook.getSelectedItem() != "") {

					updateChaptersList();
				}
			}
		});
		selectBook.setBounds(147, 43, 171, 20);
		frmTextAnalyzer.getContentPane().add(selectBook);

		/*
		 * Setting up the Canvas to Plot the Graphs!
		 */
		canvas = new MyCanvas();
		canvas.setBackground(Color.WHITE);
		canvas.setBounds(395, 10, 650, 330);
		frmTextAnalyzer.getContentPane().add(canvas);

		/*
		 * JcomboBox to Select the Chapter!
		 */
		selectChapter = new JComboBox();
		selectChapter.setBounds(147, 95, 171, 20);
		selectChapter.setModel(new DefaultComboBoxModel(new String[] { "Select a chapter" }));
		frmTextAnalyzer.getContentPane().add(selectChapter);

		lblSelectChapter = new JLabel("Select Chapter");
		lblSelectChapter.setBounds(37, 98, 98, 14);
		frmTextAnalyzer.getContentPane().add(lblSelectChapter);

		/*
		 * Radio Buttons For the Analysis!
		 * 
		 */

		/*
		 * Numbers of Words!
		 */
		rdbtnWords = new JRadioButton("No. of Words");
		rdbtnWords.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				rdbtnUnique.setSelected(false);
				rdbtnAppearance.setSelected(false);
				rdbtnCommon.setSelected(false);
			}
		});
		rdbtnWords.setBounds(37, 134, 109, 23);
		frmTextAnalyzer.getContentPane().add(rdbtnWords);

		/*
		 * Unique Word Count
		 */
		rdbtnUnique = new JRadioButton("Unique Word Count");
		rdbtnUnique.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				rdbtnWords.setSelected(false);
				rdbtnAppearance.setSelected(false);
				rdbtnCommon.setSelected(false);
			}
		});
		rdbtnUnique.setBounds(209, 134, 158, 23);
		frmTextAnalyzer.getContentPane().add(rdbtnUnique);

		/*
		 * Appearance of Words(Frequency)
		 */
		rdbtnAppearance = new JRadioButton("Appearance of Words");
		rdbtnAppearance.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnWords.setSelected(false);
				rdbtnUnique.setSelected(false);
				rdbtnCommon.setSelected(false);
			}
		});
		rdbtnAppearance.setBounds(37, 173, 170, 23);
		frmTextAnalyzer.getContentPane().add(rdbtnAppearance);

		/*
		 * Most common Words
		 */
		rdbtnCommon = new JRadioButton("Most Common Words");
		rdbtnCommon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rdbtnWords.setSelected(false);
				rdbtnUnique.setSelected(false);
				rdbtnAppearance.setSelected(false);
			}
		});
		rdbtnCommon.setBounds(209, 173, 158, 23);
		frmTextAnalyzer.getContentPane().add(rdbtnCommon);

		/*
		 * Button To Analyze the Results!
		 */
		JButton btnAnalyze = new JButton("Analyze");
		btnAnalyze.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectBook.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(new JFrame(), "Please choose a book.", "Warning",
							JOptionPane.WARNING_MESSAGE);

				} else if (selectChapter.getSelectedIndex() == 0) {

					JOptionPane.showMessageDialog(new JFrame(), "Please choose a chapter.", "Warning",
							JOptionPane.WARNING_MESSAGE);

				} else if (rdbtnWords.isSelected()) {

					outputConsole.setText("Total Number of Words in \n" + selectChapter.getSelectedItem() + " " + "is "
							+ getNumberOfWords(selectBook.getSelectedItem().toString(),
									selectChapter.getSelectedItem().toString())
							+ "");
				} else if (rdbtnUnique.isSelected()) {

					outputConsole.setText("Total Number of Unique Words in \n" + selectChapter.getSelectedItem() + " "
							+ "is " + getUniqueWordCount(selectBook.getSelectedItem().toString(),
									selectChapter.getSelectedItem().toString())
							+ "");
				} else if (rdbtnAppearance.isSelected()) {

					outputConsole.setText(getWordAppearance(selectBook.getSelectedItem().toString(),
							selectChapter.getSelectedItem().toString()));

				} else if (rdbtnCommon.isSelected()) {

					getMostCommon();
				} else {
					JOptionPane.showMessageDialog(new JFrame(), "Please choose an option.", "Warning",
							JOptionPane.WARNING_MESSAGE);

				}
			}
		});
		btnAnalyze.setBounds(37, 216, 89, 23);
		frmTextAnalyzer.getContentPane().add(btnAnalyze);

		/*
		 * Button To Plot Graphs!
		 */
		JButton btnShowGraph = new JButton("Show Graph");
		/*
		 * "Show Graphs" button plots Different Graphs when Different Radio
		 * Buttons are Selected
		 */
		btnShowGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (selectBook.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(new JFrame(), "Please choose a book.", "Warning",
							JOptionPane.WARNING_MESSAGE);

				} else if (selectChapter.getSelectedIndex() == 0) {

					JOptionPane.showMessageDialog(new JFrame(), "Please choose a chapter.", "Warning",
							JOptionPane.WARNING_MESSAGE);

				} else if (rdbtnWords.isSelected()) {
					wordCount = getWordCount();
					option = 1;
					canvas.repaint();
				} else if (rdbtnUnique.isSelected()) {
					wordCount = getUniqWordCount();
					option = 2;
					canvas.repaint();

				} else if (rdbtnAppearance.isSelected()) {
				} else if (rdbtnCommon.isSelected()) {

				} else {
					JOptionPane.showMessageDialog(new JFrame(), "Please choose an option.", "Warning",
							JOptionPane.WARNING_MESSAGE);

				}
			}
		});
		btnShowGraph.setBounds(209, 216, 109, 23);
		frmTextAnalyzer.getContentPane().add(btnShowGraph);

		/*
		 * ScrollPane to scroll through the Result!
		 */
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(37, 250, 281, 89);
		frmTextAnalyzer.getContentPane().add(scrollPane);

		/*
		 * Output Console(TextArea) to Print the Results
		 */
		outputConsole = new JTextArea();
		scrollPane.setViewportView(outputConsole);

	}

	/*
	 * Array of StopWords!
	 */
	public void getStopWords() {
		String words[] = { "a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all",
				"almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst",
				"amoungst", "amount", "an", "and", "another", "any", "anyhow", "anyone", "anything", "anyway",
				"anywhere", "are", "around", "as", "at", "back", "be", "became", "because", "become", "becomes",
				"becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between",
				"beyond", "bill", "both", "bottom", "but", "by", "call", "can", "cannot", "cant", "co", "con", "could",
				"couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg",
				"eight", "either", "eleven", "else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every",
				"everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire",
				"first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full",
				"further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here",
				"hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how",
				"however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its",
				"itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me",
				"meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must",
				"my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody",
				"none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one",
				"only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own",
				"part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming",
				"seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty",
				"so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such",
				"system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence",
				"there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv",
				"thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to",
				"together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up",
				"upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence",
				"whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether",
				"which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with",
				"within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the" };
		stopWords = words;
	}

	/*
	 * This method Gets the Details of the .txt files that are placed in the
	 * root folder(C:/Users/yoursystemusername/workspace/Textanalyzer) and
	 * updaes the combobox
	 */
	public void getBookDetails() {
		bookList = new ArrayList();
		File folder = new File(".");
		bookList.add("");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().contains(".txt")) {
				System.out.println("File " + listOfFiles[i].getName());
				bookList.add(listOfFiles[i].getName());
			}
		}
		String[] books = new String[bookList.size()];

		for (int i = 0; i < books.length; i++) {

			books[i] = bookList.get(i).toString();
		}

		selectBook.setModel(new DefaultComboBoxModel(books));
	}

	/*
	 * This Method Gets the Chapters from the text file selected and updates the
	 * combobox
	 */
	public void updateChaptersList() {

		String bookName = selectBook.getSelectedItem().toString();
		List names = new ArrayList();
		names.add("");
		String line;

		try {
			InputStream fis = new FileInputStream(bookName);
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);

			while ((line = br.readLine()) != null) {

				if (line.contains("CHAPTER")) {
					names.add(line);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		String chapters[] = new String[names.size()];
		for (int i = 0; i < names.size(); i++) {
			chapters[i] = names.get(i).toString();
		}
		chapterCount = chapters.length;
		selectChapter.setModel(new DefaultComboBoxModel(chapters));
	}

	/*
	 * Method to calculate the total number of words for the chapter selected
	 */
	public int getNumberOfWords(String bookName, String chapter) {
		int numberOfWords = 0;
		String line;
		String savedline = "";
		int count = 0;
		try {
			InputStream fis = new FileInputStream(bookName);
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);

			while ((line = br.readLine()) != null) {

				if (line.contains(chapter)) {
					count++;
					continue;
				}
				if (count == 1) {
					savedline += line;
				}

				if (count == 1 && line.contains("CHAPTER")) {
					break;
				}
			}
			String[] words = savedline.split("\\s+");

			for (int i = 0; i < words.length; i++) {

				words[i] = words[i].replaceAll("[^\\w]", "");
				if (!Arrays.asList(stopWords).contains(words[i])) {
					numberOfWords++;
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return numberOfWords;
	}

	/*
	 * Method to calculate the Unique Words for the chapter Selected
	 */

	public int getUniqueWordCount(String bookName, String chapter) {

		int unique = 0;
		String line;
		String savedline = "";
		int count = 0;
		try {
			InputStream fis = new FileInputStream(bookName);
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);

			while ((line = br.readLine()) != null) {

				if (line.contains(chapter)) {
					count++;
					continue;
				}
				if (count == 1) {

					savedline += line;
				}

				if (count == 1 && line.contains("CHAPTER")) {

					break;

				}

			}

			String[] words = savedline.split("\\s+");
			List actualWords = new ArrayList();
			for (int i = 0; i < words.length; i++) {

				words[i] = words[i].replaceAll("[^\\w]", "");
				if (!Arrays.asList(stopWords).contains(words[i])) {
					actualWords.add(words[i]);

				}

			}

			int c = 0;
			for (int i = 0; i < actualWords.size(); i++) {
				c = 0;
				String w = actualWords.get(i).toString();

				for (int k = 0; k < actualWords.size(); k++) {

					if (w.equals(actualWords.get(k).toString())) {
						c++;
					}

				}
				if (c == 1) {
					unique++;
				}

				// System.out.println(w+ " "+c);

			}

			// outputConsole.setText("Total Number of Unique Words in\n
			// "+selectChapter.getSelectedItem()+" "+"is "+unique);

		}

		catch (Exception e) {

			e.printStackTrace();
		}
		return unique;

	}

	/*
	 * Method to List out the most common words in a chapter
	 */
	public void getMostCommon() {
		String bookName = selectBook.getSelectedItem().toString();
		String chapter = selectChapter.getSelectedItem().toString();
		String output = "";
		String line;
		String savedline = "";
		int count = 0;
		try {
			InputStream fis = new FileInputStream(bookName);
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);

			while ((line = br.readLine()) != null) {

				if (line.contains(chapter)) {
					count++;
					continue;

				}
				if (count == 1) {

					savedline += line;
				}

				if (count == 1 && line.contains("CHAPTER")) {

					break;

				}

			}

			String[] words = savedline.split("\\s+");
			List actualWords = new ArrayList();
			for (int i = 0; i < words.length; i++) {

				words[i] = words[i].replaceAll("[^\\w]", "");
				if (!Arrays.asList(stopWords).contains(words[i])) {
					actualWords.add(words[i]);

				}

			}

			String word[] = new String[actualWords.size()];
			int[] wordCount = new int[actualWords.size()];
			int c = 0;

			for (int i = 0; i < actualWords.size(); i++) {
				c = 0;
				String w = actualWords.get(i).toString();

				for (int k = 0; k < actualWords.size(); k++) {

					if (w.equals(actualWords.get(k).toString())) {
						c++;
					}

				}
				word[i] = w;
				wordCount[i] = c;
				System.out.println(word[i] + " " + wordCount[i]);
				output += word[i] + " " + wordCount[i] + "\n";
			}

			outputConsole.setText(output);

			for (int i = actualWords.size() - 1; i >= 50; i--) {
				for (int j = 0; j < actualWords.size(); j++) {
					if (wordCount[j] == i) {

					}
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/*
	 * Method to get the number of times a word appears in a chapter
	 */
	public String getWordAppearance(String bookName, String chapter) {

		String output = "";
		String line;
		String savedline = "";
		int count = 0;
		try {

			InputStream fis = new FileInputStream(bookName);
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);

			while ((line = br.readLine()) != null) {

				if (line.contains(chapter)) {
					count++;
					continue;

				}
				if (count == 1) {

					savedline += line;
				}

				if (count == 1 && line.contains("CHAPTER")) {

					break;

				}

			}

			String[] words = savedline.split("\\s+");
			List actualWords = new ArrayList();
			for (int i = 0; i < words.length; i++) {

				words[i] = words[i].replaceAll("[^\\w]", "");
				if (!Arrays.asList(stopWords).contains(words[i])) {
					actualWords.add(words[i]);

				}

			}

			String word[] = new String[actualWords.size()];
			int[] wordCount = new int[actualWords.size()];
			int c = 0;

			for (int i = 0; i < actualWords.size(); i++) {
				c = 0;
				String w = actualWords.get(i).toString();

				for (int k = 0; k < actualWords.size(); k++) {

					if (w.equals(actualWords.get(k).toString())) {
						c++;
					}

				}
				word[i] = w;
				wordCount[i] = c;
				output += word[i] + " \t" + wordCount[i] + "\n";
			}

			outputConsole.setText(output);
		}

		catch (Exception e) {

			e.printStackTrace();
		}
		return output;
	}

	public int[] getWordCount() {

		int[] chapterWords = new int[chapterCount];

		for (int i = 0; i < chapterCount - 1; i++) {

			chapterWords[i] = getNumberOfWords(selectBook.getSelectedItem().toString(),
					selectChapter.getItemAt(i + 1).toString());
			System.out.println(chapterWords[i]);
		}

		return chapterWords;
	}

	public int[] getUniqWordCount() {

		int[] chapterWords = new int[chapterCount];

		for (int i = 0; i < chapterCount - 1; i++) {

			chapterWords[i] = getUniqueWordCount(selectBook.getSelectedItem().toString(),
					selectChapter.getItemAt(i + 1).toString());
			System.out.println(chapterWords[i]);
		}

		return chapterWords;
	}

	private class MyCanvas extends Canvas {
		@Override
		public void paint(Graphics g) {

		}

		@Override
		public void update(Graphics g) {
			if (option == 1) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.BLACK);
				g.drawLine(40, 10, 40, 400);
				g.drawLine(10, 285, 650, 285);
				g.drawString(selectBook.getSelectedItem().toString(), getWidth() / 2 - 40, 10);
				int distance = 30;

				for (int i = 0; i < wordCount.length; i++) {

					g.drawString("" + (i + 1), distance + (i + 1) * 30, 300);
				}
				g.drawString("Chapters", 100, getHeight() - 10);

				Graphics2D g2 = (Graphics2D) g;
				for (int i = 0; i < wordCount.length; i++) {
					int lineheight = (wordCount[i] / 50);
					g2.setStroke(new BasicStroke(5));
					g2.setColor(Color.GREEN);
					if (wordCount[i] > 2500) {
						g2.setColor(Color.RED);
					} else if (wordCount[i] < 1500) {
						g2.setColor(Color.BLUE);
					}
					g2.drawLine(distance + (i + 1) * 30 + 2, 280 - lineheight, distance + (i + 1) * 30 + 2, 280);
					g2.drawString("" + wordCount[i], distance + (i + 1) * 30 - 5, 280 - lineheight - 5);
				}
				g.setColor(Color.GREEN);
				g.fillRect(200, 310, 10, 10);
				g.drawString("1500-2500", 220, 320);
				g.setColor(Color.BLUE);
				g.fillRect(290, 310, 10, 10);
				g.drawString("<1500", 300, 320);
				g.setColor(Color.RED);
				g.fillRect(340, 310, 10, 10);
				g.drawString(">2500", 360, 320);
			}
			if (option == 2) {
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.BLACK);
				g.drawLine(40, 10, 40, 400);
				g.drawLine(10, 285, 650, 285);
				g.drawString(selectBook.getSelectedItem().toString(), getWidth() / 2 - 40, 10);
				int distance = 30;

				for (int i = 0; i < wordCount.length; i++) {

					g.drawString("" + (i + 1), distance + (i + 1) * 30, 300);
				}
				g.drawString("Chapters", 100, getHeight() - 10);

				Graphics2D g2 = (Graphics2D) g;
				for (int i = 0; i < wordCount.length; i++) {
					int lineheight = (wordCount[i] / 50);
					g2.setStroke(new BasicStroke(5));
					g2.setColor(Color.GREEN);
					if (wordCount[i] > 2500) {
						g2.setColor(Color.RED);
					} else if (wordCount[i] < 1500) {
						g2.setColor(Color.BLUE);
					}
					g2.drawLine(distance + (i + 1) * 30 + 2, 280 - lineheight, distance + (i + 1) * 30 + 2, 280);
					g2.drawString("" + wordCount[i], distance + (i + 1) * 30 - 5, 280 - lineheight - 5);
				}
				g.setColor(Color.GREEN);
				g.fillRect(200, 310, 10, 10);
				g.drawString("1500-2500", 220, 320);
				g.setColor(Color.BLUE);
				g.fillRect(290, 310, 10, 10);
				g.drawString("<1500", 300, 320);
				g.setColor(Color.RED);
				g.fillRect(340, 310, 10, 10);
				g.drawString(">2500", 360, 320);

			}

		}

		public void updateGraph(Graphics g) {

		}

		public void showWordcount(Graphics g, int[] chapterWords) {

			updateGraph(g);
		}
	}
}
