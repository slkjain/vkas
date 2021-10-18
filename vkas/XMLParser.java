//vkas - a genetic funcion finder
//Copyright (C) 2005 - Shailendra Jain (SLKJain@Yahoo.com) 
//
//This library is free software; you can redistribute it and/or modify it 
//under the terms of the GNU Lesser General Public License as published by 
//the Free Software Foundation; either version 2.1 of the License, or 
//(at your option) any later version.
//
//This library is distributed in the hope that it will be useful, but 
//WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
//or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
//License for more details.
//
//You should have received a copy of the GNU Lesser General Public License 
//along with this library; if not, write to the Free Software Foundation, 
//Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 

package vkas;

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser 
{
	private DefaultHandler m_defaultHandler;
	private SAXParser m_parser;

	protected XMLParser(DefaultHandler handler) {
	  try 
	  {
		m_defaultHandler = handler;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		m_parser = factory.newSAXParser();
	  } 
	  catch (Throwable t) 
	  {
		t.printStackTrace();
	  }
	}

	protected void parse(String fileName)
	{
	  File f = new File(fileName);			
	  try
	  {
		m_parser.parse(f,m_defaultHandler);
	  } 
	  catch (Throwable t) 
	  {
		t.printStackTrace();
	  }
	}
}
