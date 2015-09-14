package MainPackage;

	import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

	import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class parseXML {
		String cinema = "(";
		String cinemaName = "";
		String[] filmNames = new String[10];
		String[] films = new String[10];
		int countFilms = 0;
		String[][] actors = new String[10][20];
		int[] actorCount = new int[20]; 
		private void stepThroughForCinema (Node start)
		{
			if (start.getNodeName().equals("cinema") )
			{
				int cinemaAttributeCount = 0;
				NodeList list = start.getChildNodes();
				System.out.println(list.getLength());
				for (Node child = start.getFirstChild(); child != null; child = child.getNextSibling())
				{
					if (child.getFirstChild() != null && child.getNodeName() != "films")
					{
						++cinemaAttributeCount;
						if (cinemaAttributeCount == 1)
						{
							cinemaName += child.getFirstChild().getNodeValue();
						}
						cinema += "\"" + child.getFirstChild().getNodeValue() + "\"";
						if (cinemaAttributeCount < 2)
							cinema += ", ";
					}

				}
				cinema += ")";
			}
		    
		    for (Node child = start.getFirstChild();
		          child != null;
		          child = child.getNextSibling())
		    {
		      stepThroughForCinema(child); 
		    }
		}
		private void stepThroughForFilm (Node start)
		{
			if (start.getNodeName().equals("film") )
			{
				films[countFilms] += "(";
				int filmAttributeCount = 0;
				NodeList list = start.getChildNodes();
				System.out.println(list.getLength());
				for (Node child = start.getFirstChild(); child != null; child = child.getNextSibling())
				{
					if (child.getFirstChild() != null)
					{
						if (child.getNodeName() != "actors")
						{
							++filmAttributeCount;
							if (filmAttributeCount == 1)
							{
								filmNames[countFilms] += child.getFirstChild().getNodeValue();
							}
							films[countFilms] += "\"" + child.getFirstChild().getNodeValue() + "\"";
							if (filmAttributeCount < 5)
								films[countFilms] += ", ";
						}
						else
						{
							int i = 0;
							for (Node node = child.getFirstChild(); node != null; node = node.getNextSibling())
							{
								if (node.getFirstChild() != null)
								{
									actors[countFilms][i] += node.getFirstChild().getNodeValue();
									++i;
								}
							}
							actorCount[countFilms] = i;
						}
					}

				}
				films[countFilms] += ")";
				++countFilms;
			}
		    
		    for (Node child = start.getFirstChild();
		          child != null;
		          child = child.getNextSibling())
		    {
		      stepThroughForFilm(child); 
		    }
		}
		parseXML(String path)
		{
			
			
			Document doc = null;
			try
			{
				File xmlFile = new File(path);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = factory.newDocumentBuilder();
				doc = db.parse(xmlFile);
			} catch(Exception e)
			{
				e.printStackTrace();
			}
			Element root = doc.getDocumentElement();
			for (int i = 0; i < 10; ++i)
			{
				filmNames[i] = "";
				films[i] = "";
				for (int j = 0; j < 20; ++j)
				{
					actors[i][j] = "";
					actorCount[i] = 0;
				}
			}
			stepThroughForFilm(root);
			stepThroughForCinema(root);
		}
}
