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

public class VkasApp {

	public static void Run(String[] args) 
	{
		ShowAbout();
		
		if (args.length<2)
		{
			System.out.println("Usage: ");
			System.out.println("java vkas VkasConfig.xml VkasData.xml");
			return;
		}
		try
		{
			RunApp(args[0],args[1]);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	static void ShowAbout ()
	{
		System.out.println("***************************************************************************");
		System.out.println("                          vkas (a genetic function finder)");
		System.out.println("                          --------------------------------");
		System.out.println("    version              : 1.0");
		System.out.println("    webpage              : https://sourceforge.net/projects/vikasg/");
		System.out.println("    copyright            : (C) 2005 by Shailendra Jain");
		System.out.println("    email                : SLKJain@Yahoo.Com");
		System.out.println("***************************************************************************");
		System.out.println("***************************************************************************");
		System.out.println("*                                                                         *");
		System.out.println("*   This program is free software; you can redistribute it and/or modify  *");
		System.out.println("*   it under the terms of the GNU Lesser General Public License as        *");
		System.out.println("*   published by the Free Software Foundation; either version 2.1 of the  *");
		System.out.println("*   License, or (at your option) any later version.                       *");
		System.out.println("*                                                                         *");
		System.out.println("***************************************************************************");
		System.out.println("");
	}
	static void RunApp(String configFile, String dataFile)
	throws VkasException
	{
		ConfigData cfg = new ConfigData(configFile);
		DataTable dtbl = new DataTable(dataFile);
		Population p = new Population(cfg,dtbl);
		double desiredFitness = cfg.getDesiredFitness();
		Chromosome bestChr = p.getBestChromosome();
		//TODO avoid following call as it requires computation of fitness again
		double bestFitness=dtbl.getFitness(bestChr);
		if (bestFitness>desiredFitness)
		{
			System.out.println("Final Population 0");
			System.out.println("Fitness = "+bestFitness);
			System.out.println(bestChr.toString());
			System.out.println("Resultant expression = "+Decoder.getExpression(bestChr));
			return;//we already got it
		}
		int generations = cfg.getGenerations();
		int gen=1;
		for ( ; gen<=generations; gen++)
		{
			System.out.println("Checking Population "+gen);
			p.performMutation();
			p.performISTransposition();
			p.performRISTransposition();
			p.performGeneTransposition();
			p.performOnePtRecombination();
			p.performTwoPtRecombination();
			p.performGeneRecombination();
			p.performReplication();
			Chromosome bestChr2 = p.getBestChromosome();
			//TODO avoid following call as it requires computation of fitness again
			double bestFitness2=dtbl.getFitness(bestChr2);
			if (bestFitness2>desiredFitness)
			{
				CheckChromosomeValue(bestChr2,dtbl);
				System.out.println("Final Population "+gen);
				System.out.println("Fitness = "+bestFitness2);
				System.out.println(bestChr2.toString());
				System.out.println("Resultant expression = "+Decoder.getExpression(bestChr2));
				break;
			}
			if (bestFitness2>bestFitness)
			{
				System.out.println("Better Population "+gen);
				System.out.println("Fitness = "+bestFitness2);
				System.out.println(bestChr2.toString());
				System.out.println("Resultant expression = "+Decoder.getExpression(bestChr2));
				bestFitness = bestFitness2;
			}
		}
		if (gen>generations)
		{
			System.out.println("No results...");
		}
	}
	private static void CheckChromosomeValue (Chromosome chr, DataTable dtbl) 
	throws VkasException
	{
		System.out.println("Checking chromosome against the data table...");
		double[][] ind = dtbl.getIndependentData();
		double[] dep = dtbl.getDependentData();
		char[] inpvars = dtbl.getInputVariables();
		for (int n=0; n<dtbl.getDataSetCount(); n++)
		{
			for (int m=0; m<inpvars.length; m++)
			{
				System.out.print(inpvars[m] + " = " + ind[n][m]+"; ");
			}
			System.out.print("Expected = "+dep[n]+"; ");
			double chrVal= Decoder.compute(chr,inpvars,ind[n]);
			System.out.println("Computed = " + chrVal); 
		}
	}
}
