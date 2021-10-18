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

public class Population 
{
	private Chromosome[] m_Chromosomes;
	private DataTable m_DataTable;
	private ConfigData m_cfg;
	SecureRandom m_rgen;
	
	protected Population (ConfigData cfg, DataTable dataTable)
	{ 
		m_Chromosomes=new Chromosome[cfg.getPopulationSize()];
		m_DataTable = dataTable;
		m_cfg = cfg;
		for(int n=0;n<m_Chromosomes.length;n++)
		{
			m_Chromosomes[n]=Chromosome.createChromosome(dataTable,cfg);
		}
		m_rgen = new SecureRandom();
		m_rgen.setSeed(m_rgen.generateSeed(16));
	}
	
	protected void performReplication ()
	throws VkasException
	{
		//create chromosomes for next generation
		Chromosome[] nextGenChromosomes=new Chromosome[m_Chromosomes.length];
		//also the start roulette position for each chromosome
		double fitness[] = new double [m_Chromosomes.length];
		
		//find out best individual of current generation
		//and copy it to the next generation
		//this is called elitism
		double bestFitness = 0.0;
		int bestFitnessChromosomeIndex = 0;
		double leastNonZeroFitness=0.0;
		double totalFitnessOfAllChr = 0.0;
		for (int n=0;n<m_Chromosomes.length;n++)
		{
			fitness[n]=m_DataTable.getFitness(m_Chromosomes[n]);
			totalFitnessOfAllChr += fitness[n];
			if (fitness[n]<0.0)
			{
				System.out.println("fitness is negative !");
				throw new ArithmeticException("fitness is negative !");
			}
			if (bestFitness<fitness[n])
			{
				bestFitness = fitness[n];
				bestFitnessChromosomeIndex=n;
			}
			if (fitness[n]!=0.0)
			{
				if ((leastNonZeroFitness==0.0)||(leastNonZeroFitness>fitness[n]))
				{
					leastNonZeroFitness=fitness[n];
				}
			}
		}
		if (totalFitnessOfAllChr<0.0)
		{
			System.out.println("total fitness of population is negative !");
			throw new ArithmeticException("total fitness of population is negative !");
		}
		//there could be some 0 fitness chromosome
		//to give them fair chance
		//we will assign them with some roulette position
		//we will give them one tenth of least non-zero fitness
		if (leastNonZeroFitness!=0.0)
		{
			for(int n=0; n<fitness.length;n++)
			{
				if (fitness[n]==0.0)
				{
					fitness[n]=leastNonZeroFitness/10;
				}
			}
		}

		//assign roulette positions
		double [] startRoulettePos = new double[fitness.length+1];
		startRoulettePos[0]=0.0;
		
		for (int n=0;n<fitness.length;n++)
		{
			startRoulettePos[n+1]=startRoulettePos[n]+fitness[n];
		}
		//copy the best chromosome to the next generation
		nextGenChromosomes[0] =(Chromosome) m_Chromosomes[bestFitnessChromosomeIndex].clone();
		
		
		//now perform roulette selection
		//where probability of an individual getting selected is in
		//proportion of its fitness
		//more the fitness, more the probability
		//we want a double type multiplier because it may be less than 1.0
		double multiplier = (Integer.MAX_VALUE/totalFitnessOfAllChr);
		for(int n=1;n<m_Chromosomes.length;n++)
		{
			int range = (int)(totalFitnessOfAllChr*multiplier);
			if (range==0)
			{
				range++;
			}
			int rouletteOutcome = m_rgen.nextInt();
			double rOutcome = rouletteOutcome/multiplier;
			boolean copied=false;
			for (int m=0;m<m_Chromosomes.length;m++)
			{
				if ((rOutcome>=startRoulettePos[m])&&(rOutcome<startRoulettePos[m+1]))
				{
					nextGenChromosomes[n] =
					(Chromosome) m_Chromosomes[m].clone();
					copied=true;
				}
			}
			if (!copied)
			{
				//copy the last chromosome
				nextGenChromosomes[n] =
				(Chromosome) m_Chromosomes[m_Chromosomes.length-1].clone();
			}
		}
		//now copy the new chromosomes over the existing ones
		m_Chromosomes=nextGenChromosomes;
		nextGenChromosomes=null;
	}
	protected void performMutation ()
	{
		float mutationRate = m_cfg.getMutationRate();
		int chrSz = m_Chromosomes[0].size();
		int mutationsToBePerformed = (int)(mutationRate*m_Chromosomes.length*chrSz);
		int genePerChromosome = m_Chromosomes[0].m_genes.length;
		int geneHeadSz = m_Chromosomes[0].m_genes[0].headSz();
		char[] codesVarsAndFn = m_DataTable.getInputVariablesAndFuntions(m_cfg);
		int mutationsDeclined = 0;
		for (int n=0; n<mutationsToBePerformed; n++)
		{
			int chrIdx = m_rgen.nextInt(m_Chromosomes.length-1);
			int geneIdx = m_rgen.nextInt(genePerChromosome-1);
			int codeIdx = m_rgen.nextInt(geneHeadSz-1);
			int newCodePos = m_rgen.nextInt(codesVarsAndFn.length-1);
			char newCode = codesVarsAndFn[newCodePos];
			if ((codeIdx==0)
				&& (!(m_cfg.isOperator(newCode))))
			{
				System.out.println("One mutation declined, codeIdx="+codeIdx+"; newCode="+newCode);
				mutationsDeclined++;
			}
			else
			{
				m_Chromosomes[chrIdx].m_genes[geneIdx].m_info[codeIdx] = newCode;
				//System.out.println("One mutation performed, chrIdx="+chrIdx+"; geneIdx="+geneIdx+ "; codeIdx="+codeIdx+"; newCode="+newCode);
			}
		}
		if (mutationsToBePerformed==mutationsDeclined)
		{
			System.out.println("Severe: All mutation declined, mutations attempted = "+mutationsToBePerformed);
		}
	}
	protected void performISTransposition ()
	{
		float rate = m_cfg.getISTranspositionRate();
		int trnsposMaxLen = m_cfg.getISTransposonMaxLength();
		int trnsposToBePerformed = (int)(m_Chromosomes.length*rate);
		for (int n=0; n<trnsposToBePerformed; n++)
		{
			int chrIdx = m_rgen.nextInt(m_Chromosomes.length-1);
			m_Chromosomes[chrIdx].PerformISTransposition(trnsposMaxLen,m_rgen);
		}
	}
	protected boolean performRISTransposition ()
	{
		float rate = m_cfg.getRISTranspositionRate();
		int trnsposMaxLen = m_cfg.getRISTransposonMaxLength();
		int trnsposToBePerformed = (int)(m_Chromosomes.length*rate);
		boolean passed=false;
		for (int n=0; n<trnsposToBePerformed; n++)
		{
			int chrIdx = m_rgen.nextInt(m_Chromosomes.length-1);
			boolean result = m_Chromosomes[chrIdx].PerformRISTransposition(trnsposMaxLen,m_rgen,m_DataTable,m_cfg);
			if (result)
			{
				passed=true;
			}
		}
		return passed;
	}
	protected boolean performGeneTransposition ()
	{
		float rate = m_cfg.getGeneTranspositionRate();
		int trnsposToBePerformed = (int)(m_Chromosomes.length*rate);
		boolean passed=false;
		for (int n=0; n<trnsposToBePerformed; n++)
		{
			int chrIdx = m_rgen.nextInt(m_Chromosomes.length-1);
			boolean result = m_Chromosomes[chrIdx].PerformGeneTransposition(m_rgen);
			if (result)
			{
				passed=true;
			}
		}
		return passed;
	}
	protected void performOnePtRecombination ()
	{
		float rate = m_cfg.getOnePtRecombRate();
		int opsToBePerformed = (int)(m_Chromosomes.length*rate);
		for (int n=0; n<opsToBePerformed; n++)
		{
			int chrIdx1 = m_rgen.nextInt(m_Chromosomes.length-1);
			int chrIdx2 = m_rgen.nextInt(m_Chromosomes.length-1);
			String chrStr1 = m_Chromosomes[chrIdx1].toString();
			String chrStr2 = m_Chromosomes[chrIdx2].toString();
			int recombPoint = m_rgen.nextInt(chrStr1.length()-1);
			String chrStr1Part1 = chrStr1.substring(0,recombPoint);
			String chrStr1Part2 = chrStr1.substring(recombPoint);
			String chrStr2Part1 = chrStr2.substring(0,recombPoint);
			String chrStr2Part2 = chrStr2.substring(recombPoint);
			chrStr1 = chrStr1Part1+chrStr2Part2;
			chrStr2 = chrStr2Part1+chrStr1Part2;
			char[] chrArr1 = chrStr1.toCharArray();
			char[] chrArr2 = chrStr2.toCharArray();
			int lengthOfOneGene = m_Chromosomes[chrIdx1].m_genes[0].size();
			int genesPerChromosome = m_Chromosomes[chrIdx1].m_genes.length;
			for (int m=0; m<genesPerChromosome; m++)
			{
				for (int p=0; p<lengthOfOneGene; p++)
				{
					m_Chromosomes[chrIdx1].m_genes[m].m_info[p]=chrArr1[m*lengthOfOneGene+p];
					m_Chromosomes[chrIdx2].m_genes[m].m_info[p]=chrArr2[m*lengthOfOneGene+p];
				}
			}
		}
	}
	protected void performTwoPtRecombination ()
	{
		float rate = m_cfg.getTwoPtRecombRate();
		int opsToBePerformed = (int)(m_Chromosomes.length*rate);
		for (int n=0; n<opsToBePerformed; n++)
		{
			int chrIdx1 = m_rgen.nextInt(m_Chromosomes.length-1);
			int chrIdx2 = m_rgen.nextInt(m_Chromosomes.length-1);
			String chrStr1 = m_Chromosomes[chrIdx1].toString();
			String chrStr2 = m_Chromosomes[chrIdx2].toString();
			int recombPoint1 = m_rgen.nextInt(chrStr1.length()-1);
			int recombPoint2 = m_rgen.nextInt(chrStr1.length()-1);
			if (recombPoint1>recombPoint2)
			{
				int tmp = recombPoint1;
				recombPoint1 = recombPoint2;
				recombPoint2 = tmp;
			}
			String chrStr1Part1 = chrStr1.substring(0,recombPoint1);
			String chrStr1Part2 = chrStr1.substring(recombPoint1,recombPoint2);
			String chrStr1Part3 = chrStr1.substring(recombPoint2);
			String chrStr2Part1 = chrStr2.substring(0,recombPoint1);
			String chrStr2Part2 = chrStr2.substring(recombPoint1,recombPoint2);
			String chrStr2Part3 = chrStr2.substring(recombPoint2);
			chrStr1 = chrStr1Part1+chrStr2Part2+chrStr1Part3;
			chrStr2 = chrStr2Part1+chrStr1Part2+chrStr2Part3;
			char[] chrArr1 = chrStr1.toCharArray();
			char[] chrArr2 = chrStr2.toCharArray();
			int lengthOfOneGene = m_Chromosomes[chrIdx1].m_genes[0].size();
			int genesPerChromosome = m_Chromosomes[chrIdx1].m_genes.length;
			for (int m=0; m<genesPerChromosome; m++)
			{
				for (int p=0; p<lengthOfOneGene; p++)
				{
					m_Chromosomes[chrIdx1].m_genes[m].m_info[p]=chrArr1[m*lengthOfOneGene+p];
					m_Chromosomes[chrIdx2].m_genes[m].m_info[p]=chrArr2[m*lengthOfOneGene+p];
				}
			}
		}
	}
	protected void performGeneRecombination ()
	{
		float rate = m_cfg.getGeneRecombRate();
		int opsToBePerformed = (int)(m_Chromosomes.length*rate);
		for (int n=0; n<opsToBePerformed; n++)
		{
			int chrIdx1 = m_rgen.nextInt(m_Chromosomes.length-1);
			int chrIdx2 = m_rgen.nextInt(m_Chromosomes.length-1);
			int recombGeneIdx = m_rgen.nextInt(m_Chromosomes[0].m_genes.length-1);
			Gene tmp = m_Chromosomes[chrIdx1].m_genes[recombGeneIdx];
			m_Chromosomes[chrIdx1].m_genes[recombGeneIdx]
				=	m_Chromosomes[chrIdx2].m_genes[recombGeneIdx];
			m_Chromosomes[chrIdx2].m_genes[recombGeneIdx] = tmp;
		}
	}
	public String toString()
	{
		StringBuffer sbuff=new StringBuffer();
		for (int n=0; n<m_Chromosomes.length; n++)
		{
			sbuff.append(m_Chromosomes[n].toString()).append(";");
		}
		return sbuff.toString();
	}
	protected Chromosome getBestChromosome()
	throws VkasException
	{
		double bestFitness = 0.0;
		int bestFitnessChromosomeIndex = 0;
		for (int n=0;n<m_Chromosomes.length;n++)
		{
			double fitness=m_DataTable.getFitness(m_Chromosomes[n]);
			if (fitness<0.0)
			{
				System.out.println("fitness is negative !");
				throw new ArithmeticException("fitness is negative !");
			}
			if (bestFitness<fitness)
			{
				bestFitness = fitness;
				bestFitnessChromosomeIndex=n;
			}
		}
		return m_Chromosomes[bestFitnessChromosomeIndex];
	}  
}
