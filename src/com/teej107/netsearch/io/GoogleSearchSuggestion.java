package com.teej107.netsearch.io;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by teej107 on 7/31/2017.
 */
public class GoogleSearchSuggestion
{
	private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

	public static List<String> getResults(String result) throws IOException
	{
		List<String> list = new ArrayList<>();
		URL url = new URL("http://suggestqueries.google.com/complete/search?output=toolbar&hl=en&q=" + result);
		try
		{
			DocumentBuilder db = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
			InputStream is = url.openStream();
			Document document = db.parse(is);
			is.close();
			document.getDocumentElement().normalize();
			NodeList nodes = document.getElementsByTagName("suggestion");
			for(int i = 0; i < nodes.getLength(); i++)
			{
				Node node = nodes.item(i);
				if(node instanceof Element)
				{
					list.add(((Element) node).getAttribute("data"));
				}
			}

		}
		catch (ParserConfigurationException | SAXException e)
		{
			e.printStackTrace();
		}
		return list;
	}
}
