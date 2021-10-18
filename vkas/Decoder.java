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

public class Decoder 
{
	protected static double compute (Chromosome chrm, char paramNames [], double inputValues [])
	throws VkasException
	{
		Gene[] genes = chrm.getGenes();
		assert (genes.length!=0):"no genes in the chromosome !";
		if (genes.length==1)
		{
			return compute(genes[0],paramNames,inputValues);
		}
		double val1 = compute(genes[0],paramNames,inputValues);
		double val2 = compute(genes[1],paramNames,inputValues);
		double result = fnValue(chrm.getLinkingFn(),val1,val2);;
		for (int n=2;n<genes.length;n++)
		{
			double val = compute(genes[n],paramNames,inputValues);
			result = fnValue(chrm.getLinkingFn(),result,val);
		}
		return result;
	}
	protected static double compute (Gene g, char paramNames [], double inputValues []) 
	throws VkasException
	{
		//TODO how to ensure that both paramNames and inputValues are having
		//same length
		
		//first prepare an arraylist of strings for easy decoding
		ArrayList listOfLayers = new ArrayList();
		StringBuffer currLayer = new StringBuffer();
		int charToAppendInThisLayer = 1;
		int charToAppendInNextLayer = 0;
		for (int n=0; n<g.m_info.length; n++)
		{
			if (charToAppendInThisLayer!=0)
			{
				currLayer.append(g.m_info[n]);
				if (isFunction(g.m_info[n]))
				{
					//nextLayerRequired=true;
					charToAppendInNextLayer += 2;
				}
				charToAppendInThisLayer--;
				//if our loop exhausted
				if (n==(g.m_info.length-1))
				{
					//System.out.println("Layer = "+currLayer.toString());
					listOfLayers.add(currLayer.toString());
				}
			}
			else
			{
				//System.out.println("Layer = "+currLayer.toString());
				listOfLayers.add(currLayer.toString());
				charToAppendInThisLayer=charToAppendInNextLayer;
				charToAppendInNextLayer=0;
				n--;
				if (charToAppendInThisLayer!=0)
				{
					//make a new string buffer for next layer
					currLayer = new StringBuffer();
				}
				else
				{
					break;
				}
			}
		}
		
//		for(int n=0; n<listOfLayers.size();n++)
//		{
//			System.out.println((String)listOfLayers.get(n));
//		}
		
		if (listOfLayers.size()==1)
		{
			return getParamValue(((String)listOfLayers.get(0)).charAt(0),paramNames,inputValues);
		}
		//now compute
		//values at lowermost level
		String lowestString = (String)listOfLayers.get(listOfLayers.size()-1);
		double [] values = new double [lowestString.length()]; 
		for(int m=0;m<values.length;m++)
		{
			values[m]=getParamValue(lowestString.charAt(m),paramNames,inputValues);
			//System.out.println(values.length-m-1);
			//System.out.println(lowestString.charAt(m));
		}
		//System.out.println("Lowermost values");
//		for(int m=0;m<values.length;m++)
//		{
//			System.out.println(values[m]);
//		}
		double finalValue=0.0;
		for(int n=listOfLayers.size()-2;n>=0;n--)
		{
			//int prevValLoc = 0;
			String currString = (String)listOfLayers.get(n);
			double [] currValues = new double [currString.length()]; 
			int currIdx=0;
			int prevIdx=0;
			
			for(int m=0;m<currString.length();m++)
			{
				//System.out.println(currString.charAt(m));
				if (isFunction(currString.charAt(m)))
				{
					//assume the function takes two params
					currValues[currIdx]=fnValue(currString.charAt(m),values[prevIdx],values[prevIdx+1]);
					//System.out.println(currValues[currIdx]);
					currIdx++;
					prevIdx += 2;
				}
				else
				{
					currValues[currIdx]=getParamValue(currString.charAt(m),paramNames,inputValues);
					currIdx++;
				}
			}
			if (currIdx==1)
			{
				if (n!=0)
				{
					System.out.println("ERROR: Exited from computing loop before index reached 0 !!!");
				}
				finalValue = currValues[0];
				break;
			}
			values = currValues;
		}
		
		//System.out.println("final value = "+finalValue);
		return finalValue;
	}
	
	protected static double getParamValue (char paramName, char paramNames [], double inputValues [])
	throws VkasException
	{
		for (int n=0; n<paramNames.length; n++)
		{
			if (paramNames[n]==paramName)
			{
				return inputValues[n];
			}
		}
		throw new VkasException("Paramter name not found");
	}
	
	protected static boolean isFunction(char c)
	{
		if ((c=='+') || (c=='-')||(c=='*') || (c=='/'))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	protected static double fnValue (char fn, double value1, double value2)
	{
		//TODO handle case of '^' (power)
		if (fn=='+')
		{
			return value1+value2;
		}
		else if (fn=='-')
		{
			return value1-value2;
		}
		else if (fn=='*')
		{
			return value1*value2;
		}
		else if (fn=='/')
		{
			return value1/value2;
		}
		else
		{
			System.out.println("Error in fnValue()!!!");
			return 0.0;
		}
	}
	
	protected static boolean isSupportedFn(char fn)
	{
		if ((fn=='+')||(fn=='-')||(fn=='*')||(fn=='/'))
		{
			return true;
		}
		else
		{
			return false;
		}		
	}
	
	protected static int getFunctionArgs(char fn)
	{
		//TODO handle case of '^' (power)
		if ((fn=='+')||(fn=='-')||(fn=='*')||(fn=='/'))
		{
			return 2;
		}
		else
		{
			System.out.println("Error in getFunctionArgs()!!!");
			return 0;
		}
	}
	protected static String getExpression (Gene g)
	{
		//first prepare an arraylist of strings for easy decoding
		ArrayList listOfLayers = new ArrayList();
		StringBuffer currLayer = new StringBuffer();
		int charToAppendInThisLayer = 1;
		int charToAppendInNextLayer = 0;
		for (int n=0; n<g.m_info.length; n++)
		{
			if (charToAppendInThisLayer!=0)
			{
				currLayer.append(g.m_info[n]);
				if (isFunction(g.m_info[n]))
				{
					//nextLayerRequired=true;
					charToAppendInNextLayer += 2;
				}
				charToAppendInThisLayer--;
				//if our loop exhausted
				if (n==(g.m_info.length-1))
				{
					//System.out.println("Layer = "+currLayer.toString());
					listOfLayers.add(currLayer.toString());
				}
			}
			else
			{
				//System.out.println("Layer = "+currLayer.toString());
				listOfLayers.add(currLayer.toString());
				charToAppendInThisLayer=charToAppendInNextLayer;
				charToAppendInNextLayer=0;
				n--;
				if (charToAppendInThisLayer!=0)
				{
					//make a new string buffer for next layer
					currLayer = new StringBuffer();
				}
				else
				{
					break;
				}
			}
		}
		
		if (listOfLayers.size()==1)
		{
			return (String)listOfLayers.get(0);
		}
		
		ArrayList arrayListOfStringArrays = new ArrayList();
		for (int n =0; n <listOfLayers.size(); n++)
		{
			String thisLayerSingleString = (String)listOfLayers.get(n);
			String[] strArray = new String[thisLayerSingleString.length()]; 
			for (int m=0; m<thisLayerSingleString.length(); m++)
			{
				strArray[m]=new String(new char[]{(thisLayerSingleString.toCharArray()[m])});
			}
			arrayListOfStringArrays.add(strArray);
		}
		
		for (int n=arrayListOfStringArrays.size()-2;n>=0;n--)
		{
			String[] upper = (String[])arrayListOfStringArrays.get(n);
			String[] lower = (String[])arrayListOfStringArrays.get(n+1);
			int k=0;
			for (int m=0; m<upper.length; m++)
			{
				if (isFunction(upper[m].toCharArray()[0]))
				{
					upper[m]="("+lower[k]+upper[m]+lower[k+1]+")";
					k+=2;
				}
			}
		}
		String[] decodedStrArr = (String[])arrayListOfStringArrays.get(0);
		String decodedStr = new String();
		//TODO (Minor) use string builder 
		for (int n=0; n<decodedStrArr.length;n++)
		{
			decodedStr+=decodedStrArr[n];
		}
		return decodedStr;
	}
	protected static String getExpression (Chromosome c)
	{
		String decodedStr = "{"+getExpression(c.m_genes[0])+"}";
		//TODO (Minor) use string builder 
		for (int n=1; n<c.m_genes.length;n++)
		{
			decodedStr+= (c.getLinkingFn() + "{"+ getExpression(c.m_genes[n]) +"}");
		}
		return decodedStr;
	}
}
