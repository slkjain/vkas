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

public class Chromosome 
{
	char m_linkingFn;
	Gene m_genes [];
	
	protected Chromosome(char linkingFn, Gene genes [] )
	{
		m_linkingFn = linkingFn;
		m_genes = genes;
	}
	
	protected char getLinkingFn()
	{
		return m_linkingFn;
	}
	
	protected Gene[] getGenes()
	{
		return m_genes;
	}
	
	public String toString ()
	{
		String chrStr = new String();
		for(int n=0; n<m_genes.length;n++)
		{
			chrStr+=m_genes[n].toString();
		}
		return chrStr;
	}
	protected static Chromosome createChromosome(DataTable dataTable, ConfigData cfg)
	{
		Gene[] genes = new Gene[cfg.getGenePerChromosome()];
		for(int n=0; n<genes.length; n++)
		{
			genes[n]=Gene.createGene(dataTable,cfg);
		}
		return new Chromosome(cfg.getLinkingFn(),genes);
	}
	
	public Object clone()
	{
		return new Chromosome(m_linkingFn,m_genes);
	}
	protected int size()
	{
		return (m_genes.length) * (m_genes[0].size());
	}
	
	protected void PerformISTransposition(int transposonMaxLength, SecureRandom rgen)
	{
		int srcGeneIdx = rgen.nextInt(m_genes.length-1);
		int destGeneIdx = rgen.nextInt(m_genes.length-1);
		int srcGenePos = rgen.nextInt(m_genes[0].headSz()-1);
		int availableCodeLengthForTransposition = m_genes[0].headSz()-srcGenePos;
		if (availableCodeLengthForTransposition<transposonMaxLength)
		{
			transposonMaxLength=availableCodeLengthForTransposition;
		}
		if (transposonMaxLength==0)
		{
			System.out.println("IS transposition declined in this chromosome.");
		}
		int transposonLen=rgen.nextInt(transposonMaxLength);
		if (transposonLen==0)
		{
			transposonLen=1;
		}
		int destGenePos = rgen.nextInt(m_genes[0].headSz()-transposonLen);
		char[] transposon = new char [transposonLen];
		for (int n=0; n<transposonLen; n++)
		{
			transposon[n]=m_genes[srcGeneIdx].m_info[n+srcGenePos];
		}
		//now copy the head of dest gene to a char array
		//then we will manipulate it and copy back to the gene
		char[] destHead = new char[m_genes[0].headSz()];
		for (int n=0; n<destHead.length; n++)
		{
			destHead[n]=m_genes[destGeneIdx].m_info[n];
		}
		//shift head from backward to create a gap to insert the 'is' element
		for (int n=destHead.length-1;n>destGenePos+transposonLen-1;n--)
		{
			destHead[n]=destHead[n-transposonLen];
		}
		for (int n=0; n<transposonLen; n++)
		{
			destHead[n+destGenePos]=transposon[n];
		}
		//copy back to gene
		for (int n=0; n<destHead.length; n++)
		{
			m_genes[destGeneIdx].m_info[n]=destHead[n];
		}
	}
	protected boolean PerformRISTransposition(int transposonMaxLength, SecureRandom rgen, DataTable dtbl, ConfigData cfg)
	{
		int srcGeneIdx = rgen.nextInt(m_genes.length-1);
		int srcPos = rgen.nextInt(m_genes[srcGeneIdx].headSz()-1);
		for(int n=srcPos; n<m_genes[srcGeneIdx].headSz(); n++)
		{
			if (cfg.isOperator(m_genes[srcGeneIdx].m_info[n]))
			{
				srcPos=n;
				break;
			}
		}
		if (srcPos==m_genes[srcGeneIdx].headSz()-1)
		{
			System.out.println("RIS transposition declined in this chromosome.");
			return false;
		}
		if ((m_genes[srcGeneIdx].headSz()-srcPos)<transposonMaxLength)
		{
			transposonMaxLength=m_genes[srcGeneIdx].headSz()-srcPos;
		}
		int trnsposonLen = rgen.nextInt(transposonMaxLength);
		if (trnsposonLen==0)
		{
			System.out.println("RIS transposition declined in this chromosome (Len==0).");
			return false;
		}
		char[] transposon = new char[trnsposonLen];
		for (int n=0; n<trnsposonLen; n++)
		{
			transposon[n] = m_genes[srcGeneIdx].m_info[n+srcPos];
		}
		String newBigHead = new String(transposon);
		String gene = m_genes[srcGeneIdx].toString();
		newBigHead = newBigHead.concat(gene);
		char[] newBigHeadChars = newBigHead.toCharArray();
		for (int n=0; n<m_genes[srcGeneIdx].headSz(); n++)
		{
			m_genes[srcGeneIdx].m_info[n] = newBigHeadChars[n];
		}
		return true;
	}
	protected boolean PerformGeneTransposition(SecureRandom rgen)
	{
		int srcGeneIdx = rgen.nextInt(m_genes.length-1);
		if (srcGeneIdx==0)
		{
			System.out.println("GeneTransposition is not performed.");
			return false;
		}
		Gene prevSrcGene = m_genes[0];
		m_genes[0]=m_genes[srcGeneIdx];
		m_genes[srcGeneIdx]=prevSrcGene;
		return true;
	}
}
