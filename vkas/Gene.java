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

import java.security.SecureRandom;

public class Gene 
{
	static int m_geneCount = 0;
	int m_geneHeadSz = 0;
	protected char m_info [];
	
	//only for testing
	protected Gene (String str)
	{
		m_geneCount++;
		m_info = new char[str.length()];
		for(int n=0; n<str.length(); n++)
		{
			m_info[n]=str.charAt(n);
		}
	}
	private Gene (int geneHeadSize, int maxFuncArgs)
	{
		int geneTailSize = geneHeadSize*(maxFuncArgs-1)+1;
		int geneSize = geneHeadSize + geneTailSize;
		m_geneHeadSz = geneHeadSize;
		m_info = new char[geneSize];
	}
	
	protected int headSz()
	{
		return m_geneHeadSz;
	}
	
	protected static Gene createGene(DataTable dataTable, ConfigData cfg)
	{
		//get functions to be used
		char[] reqOperators = cfg.getOperators();
		//get input variables to be used (in gene tail)
		char[] reqTerminals = dataTable.getInputVariables();
		//mix functions and terminals to be put in gene head
		char[] fnAndTerminals=new char[reqOperators.length+reqTerminals.length];
		//fill fnAndTerminals array
		for (int n=0; n < fnAndTerminals.length; n++)
		{
			if (n<reqOperators.length)
			{
				fnAndTerminals[n]=reqOperators[n];
			}
			else
			{
				fnAndTerminals[n]=reqTerminals[n-reqOperators.length];
			}
		}
		
		
		//check with decoder whether these functions are supported
		//and what is max number of arguments possible to a function
		int maxFuncArgs=0;
		for(int n=0; n<reqOperators.length;n++)
		{
			if (Decoder.isSupportedFn(reqOperators[n])==false)
			{
				throw new IllegalArgumentException("Datatable has at least one function which is not supported.");
			}
			int args = Decoder.getFunctionArgs(reqOperators[n]);
			if (args>maxFuncArgs)
			{
				maxFuncArgs=args;
			}
		}
		//create a new gene
		Gene newGene = new Gene(cfg.getGeneHeadLen(),maxFuncArgs);
		SecureRandom rgen = new SecureRandom();
		rgen.setSeed(rgen.generateSeed(16));
		//populate randomly with functions and terminals
		for (int n=0; n < newGene.m_info.length; n++)
		{
			//Random rgen = new Random(n*System.currentTimeMillis()+m_geneCount);
			if (n==0)
			{
				//fill header first position 
				//randomly with function
				int i = rgen.nextInt(reqOperators.length);
				//clamp i if rgen produces something out of range
				if (i>=reqOperators.length)
				{
					i=reqOperators.length-1;
				}
				newGene.m_info[n]=reqOperators[i];
			}
			else if (n<cfg.getGeneHeadLen())
			{
				//fill header randomly with functions and terminals
				int i = rgen.nextInt(fnAndTerminals.length);
				//clamp i if rgen produces something out of range
				if (i>=fnAndTerminals.length)
				{
					i=fnAndTerminals.length-1;
				}
				newGene.m_info[n]=fnAndTerminals[i];
			}
			else
			{
				//fill tail randomly with functions and terminals
				int i = rgen.nextInt(reqTerminals.length);
				//clamp i if rgen produces something out of range
				if (i>=reqTerminals.length)
				{
					i=reqTerminals.length-1;
				}
				newGene.m_info[n]=reqTerminals[i];
			}
		}
		return newGene;
	}
	
	public String toString ()
	{
		return new String(m_info);
	}
	
	protected int size()
	{
		return m_info.length;
	}
}
