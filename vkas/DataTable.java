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

public class DataTable extends DefaultHandler 
{
	private String m_dataSetName = null;
	private char[] m_inputVariables;
	private int m_dataSetCount = 0;
	private double[][] m_independentData;
	private double[] m_dependentData;
	
	private String m_currentlyReading = null;
	private ArrayList m_tempList = null;
	private ArrayList m_tempList2 = null;

	protected DataTable(String fileName)
	{
		XMLParser parser = new XMLParser(this);
		parser.parse(fileName);
	}
	protected char[] getInputVariables()
	{
		return m_inputVariables;
	}

	protected char[] getInputVariablesAndFuntions(ConfigData cfg)
	{
		String op = new String(cfg.getOperators());
		String inp = new String(getInputVariables());
		String inpfn = op+inp;
		return inpfn.toCharArray();
	}
	protected int getDataSetCount()
	{
		return m_dataSetCount;
	}

	protected  double[] getDependentData() 
	{
		return m_dependentData;
	}

	protected double[][] getIndependentData() 
	{
		return m_independentData;
	}
	
	protected double getFitness (Chromosome c) 
	throws VkasException
	{
		double[] fitness=new double[m_dependentData.length];
		for (int n=0;n<fitness.length;n++)
		{
			double val = Decoder.compute(c,m_inputVariables,m_independentData[n]);
			double percentDiff = 100*(Math.abs(m_dependentData[n]-val)/m_dependentData[n]);
			fitness[n] = 100-percentDiff;
			if (fitness[n]<0)
			{
				fitness[n]=0;
			}
		}
		double totalFitness=0.0;
		for (int n=0;n<fitness.length;n++)
		{		
			totalFitness+=fitness[n];
		}
		return totalFitness/fitness.length;
	}
	
	protected String getDataSetName()
	{
		return m_dataSetName;
	}
	
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
		throws SAXException 
	{
		//System.out.println("startElement =" + localName);
		m_currentlyReading = localName;
		if ((m_currentlyReading.equals("Functions"))
		||(m_currentlyReading.equals("InputVars")))
		{
			m_tempList = new ArrayList();
		}
		if (m_currentlyReading.equals("DataValues"))
		{
			m_tempList = new ArrayList();
			m_tempList2 = new ArrayList();
		}
		if (m_currentlyReading.equals("ValueSet"))
		{
			m_dataSetCount++;
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
		if (m_currentlyReading.equals("DataSetName"))
		{
			m_dataSetName = strValue;
			return;
		}
		if ((m_currentlyReading.equals("InputVar"))
		||(m_currentlyReading.equals("IndVar")))
		{
			m_tempList.add(strValue);
			return;
		}
		if (m_currentlyReading.equals("DependentVar"))
		{
			m_tempList2.add(strValue);
			return;
		}
	}

	public void endElement(String namespaceURI, String localName, String qName)
		throws SAXException
	{
		m_currentlyReading = null;
		if (localName.equals("InputVars"))
		{
			m_inputVariables = new char[m_tempList.size()];
			for (int n=0; n<m_tempList.size();n++)
			{
				//System.out.println("endElement ="+(String)m_tempList.get(n));
				//we have only one char in this string
				m_inputVariables[n] = ((String)m_tempList.get(n)).charAt(0);
			}
			m_tempList = null;
		}
		if (localName.equals("DataValues"))
		{
			if (m_tempList2.size()!=m_dataSetCount)
			{
				throw new SAXException("Data set count does not matches with provided data.");
			} 
			int indDataDim = m_tempList.size()/m_dataSetCount;
			m_independentData = new double[m_dataSetCount][indDataDim];
			m_dependentData = new double[m_dataSetCount];
			for (int n=0; n<m_tempList2.size();n++)
			{
				m_dependentData[n] = Double.parseDouble((String)m_tempList2.get(n));
				for (int cnt=0; cnt<indDataDim; cnt++)
				{
					m_independentData[n][cnt] = Double.parseDouble((String)m_tempList.get(n*indDataDim+cnt));
				}
			}
			m_tempList = null;
			m_tempList2 = null;
		}
		if (localName.equals("ValueSet"))
		{
			if (m_tempList2.size()!=m_dataSetCount)
			{
				throw new SAXException("Current data set count does not matches with provided data.");
			} 
		}
	}
}