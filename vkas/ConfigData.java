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
import java.util.ArrayList;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

public class ConfigData extends DefaultHandler 
{
	private ArrayList m_tempList = null;
	private String m_currentlyReading = null;

	private String m_configName;
	private String m_version;
	private int m_generations;
	private double m_desiredFitness;
	private int m_populationSize;
	private char [] m_operators;
	private char m_linkingFn;
	private int m_geneHeadLen;
	private int m_genePerChromosome;
	private float m_mutationRate;
	private float m_onePtRecombRate;
	private float m_twoPtRecombRate;
	private float m_geneRecombRate;
	private float m_ISTranspositionRate;
	private int m_ISTransposonMaxLength;
	private float m_RISTranspositionRate;
	private int m_RISTransposonMaxLength;
	private float m_geneTranspositionRate;

	protected String getConfigName()
	{
		return m_configName;
	}
	protected  String getVersion()
	{
		return m_version;
	}
	protected  int getGenerations()
	{
		return m_generations;
	}
	protected double getDesiredFitness()
	{
		return m_desiredFitness;
	}
	protected int getPopulationSize()
	{
		return m_populationSize;
	}
	protected char[] getOperators()
	{
		return m_operators;
	}
	protected char getLinkingFn()
	{
		return m_linkingFn;
	}
	protected int getGeneHeadLen()
	{
		return m_geneHeadLen;
	}
	protected int getGenePerChromosome()
	{
		return m_genePerChromosome;
	}
	protected float getMutationRate()
	{
		return m_mutationRate;
	}
	protected float getOnePtRecombRate()
	{
		return m_onePtRecombRate;
	}
	protected float getTwoPtRecombRate()
	{
		return m_twoPtRecombRate;
	}
	protected float getGeneRecombRate()
	{
		return m_geneRecombRate;
	}
	protected  float getISTranspositionRate()
	{
		return m_ISTranspositionRate;
	}
	protected  int getISTransposonMaxLength()
	{
		return m_ISTransposonMaxLength;
	}
	protected  float getRISTranspositionRate()
	{
		return m_RISTranspositionRate;
	}
	protected  int getRISTransposonMaxLength()
	{
		return m_RISTransposonMaxLength;
	}
	protected  float getGeneTranspositionRate()
	{
		return m_geneTranspositionRate;
	}

	ConfigData(String fileName)
	{
		XMLParser parser = new XMLParser(this);
		parser.parse(fileName);
	}

	boolean isOperator (char op)
	{
		for (int n=0; n<m_operators.length; n++)
		{
			if (op==m_operators[n])
			{
				return true;
			}
		}
		return false;
	}
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
		throws SAXException 
	{
		//System.out.println("startElement =" + localName);
		m_currentlyReading = localName;
		if (m_currentlyReading.equals("Operators"))
		{
			m_tempList = new ArrayList();
		}
	}

	public void characters(char[] ch, int start, int length)
		throws SAXException 
	{
		if (m_currentlyReading==null)
		{
			return;
		}
		String strValue = new String(ch, start, length);
		//System.out.println("characters =" + strValue);
		if (m_currentlyReading.equals("ConfigName"))
		{
			m_configName = strValue;
			return;
		}
		if (m_currentlyReading.equals("Version"))
		{
			m_version = strValue;
			return;
		}
		if (m_currentlyReading.equals("MaxGenerations"))
		{
			m_generations = Integer.parseInt(strValue);
			return;
		}
		if (m_currentlyReading.equals("PopulationSize"))
		{
			m_populationSize = Integer.parseInt(strValue);
			return;
		}
		if (m_currentlyReading.equals("DesiredFitness"))
		{
			m_desiredFitness = Double.parseDouble(strValue);
			return;
		}
		if (m_currentlyReading.equals("Op"))
		{
			m_tempList.add(strValue);
			return;
		}
		if (m_currentlyReading.equals("LinkingOp"))
		{
			m_linkingFn = strValue.toCharArray()[0];
			return;
		}
		if (m_currentlyReading.equals("GeneHeadLen"))
		{
			m_geneHeadLen = Integer.parseInt(strValue);
			return;
		}
		if (m_currentlyReading.equals("GenesPerChromosome"))
		{
			m_genePerChromosome = Integer.parseInt(strValue);
			return;
		}
		if (m_currentlyReading.equals("MutationRate"))
		{
			m_mutationRate = Float.parseFloat(strValue);
			return;
		}
		if (m_currentlyReading.equals("OnePointRecombinationRate"))
		{
			m_onePtRecombRate = Float.parseFloat(strValue);
			return;
		}
		if (m_currentlyReading.equals("TwoPointRecombinationRate"))
		{
			m_twoPtRecombRate = Float.parseFloat(strValue);
			return;
		}
		if (m_currentlyReading.equals("GeneRecombinationRate"))
		{
			m_geneRecombRate = Float.parseFloat(strValue);
			return;
		}
		if (m_currentlyReading.equals("ISTranspositionRate"))
		{
			m_ISTranspositionRate = Float.parseFloat(strValue);
			return;
		}
		if (m_currentlyReading.equals("ISTransposonMaxLength"))
		{
			m_ISTransposonMaxLength = Integer.parseInt(strValue);
			return;
		}
		if (m_currentlyReading.equals("RISTranspositionRate"))
		{
			m_RISTranspositionRate = Float.parseFloat(strValue);
			return;
		}
		if (m_currentlyReading.equals("RISTransposonMaxLength"))
		{
			m_RISTransposonMaxLength = Integer.parseInt(strValue);
			return;
		}
		if (m_currentlyReading.equals("GeneTranspositionRate"))
		{
			m_geneTranspositionRate = Float.parseFloat(strValue);
			return;
		}
	}

	public void endElement(String namespaceURI, String localName, String qName)
		throws SAXException 
	{
		//System.out.println("endElement =" + localName);
		m_currentlyReading = null;
		if (localName.equals("Operators"))
		{
			m_operators = new char[m_tempList.size()];
			for (int n=0; n<m_tempList.size();n++)
			{
				//we have only one char in this string
				m_operators[n] = ((String)m_tempList.get(n)).charAt(0);
			}
			m_tempList = null;
		}
	}
}
