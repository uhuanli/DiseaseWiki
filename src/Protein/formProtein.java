package Protein;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class formProtein {
	
	public static String[] ProteinFiles;
	static BufferedReader[] brProFiles;
	
	static final int iAlternativeProducts = 0;
	static final int iBinaryInteractions = 1;
	static final int iCrossReferences = 2;
	static final int iEntryInformation = 3;
	static final int iGeneralAnnotation = 4;
	static final int iGeneralOntology = 5;
	static final int iName_Origin = 6;
	static final int iProtein_Attributes = 7;
	static final int iReferences = 8;
	static final int iSequenceAnnotation = 9;
	static final int iSequences = 10;
	
	public static String[][][] ProteinData = new String[11][][];
	
	public static void  fpInitial() throws IOException
	{
		BufferedWriter bwPro = new BufferedWriter(new FileWriter("ProteinData"));
		String formPaths = "Protein\\";
		File _file = new File(formPaths);
		ProteinFiles = _file.list();
		{
			if(ProteinFiles.length != 11)
			{
				System.out.print("bug len in p files\n");
			}
		}
		brProFiles = new BufferedReader[ProteinFiles.length];
		for(int i = 0; i < ProteinFiles.length; i ++)
		{
			brProFiles[i] = new BufferedReader(new FileReader("Protein\\" + ProteinFiles[i]));
		}
		
	}
	
	public static void LoadProteinFiles() throws IOException
	{
		String[] rLine = new String[11];
		
		{
			for(int i = 0; i < 11; i ++)
			{
				rLine[i] = brProFiles[i].readLine();
			}
		}
		String _cont;
		for(int i = 0; i < 11; i ++)
		{
			if(rLine[i].equals("=========="))
			{
				_cont = "";
				{
					System.out.print(brProFiles[i].readLine() + "++\n");
				}
				while(true)
				{
					rLine[i] = brProFiles[i].readLine();
					if(rLine[i] == null || rLine[i].equals("=========="))
						break;
					_cont += rLine[i] + "\n";
				}
				String[] _sp_cont = _cont.split("\n-----\n");
				ProteinData[i] = new String[_sp_cont.length][];
				for(int j = 0; j < _sp_cont.length; j ++)
				{
					ProteinData[i][j] = _sp_cont[j].split("#@!");
				}
			}
		}
		{
			for(int i = 0; i < 11; i ++)
				System.out.print(rLine[i] + "**\n");
		}
		
		{
			for(int i = 0; i < ProteinData.length; i ++)
			{
				System.out.print("\n++++++++++++++++++++" + ProteinFiles[i] + "\n");
				for(int j = 0; j < ProteinData[i].length; j ++)
				{
					System.out.print("\n****************" + j + "\n");
					for(int k = 0; k < ProteinData[i][j].length; k ++)
					{
						System.out.print("\n----------" + k + "\n");
						System.out.print(ProteinData[i][j][k] + "\n");
					}
				}
			}
		}
		
		
	}
	
	public static void fpClose() throws IOException
	{
		for(int i  = 0; i < ProteinFiles.length; i ++)
		{
			brProFiles[i].close();
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		fpInitial();
		LoadProteinFiles();
		fpClose();
	}
}


/*
AlternativeProducts
BinaryInteractions
CrossReferences
EntryInformation
GeneralAnnotation
GeneralOntology
Name_Origin
Protein_Attributes
References
SequenceAnnotation
Sequences
*/