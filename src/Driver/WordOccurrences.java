package Driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class WordOccurrences {

	public static void main(String[] args)
	{        
		Scanner sc = new Scanner(System.in);
		String input;
		String filePath = null;
		List<String> sentenceList=new ArrayList<>();

		System.out.println("Would you like to upload a file for processing? (y/n)");
		while(true) {
			input = sc.next();
			if(input.equals("exit")) {
				System.exit(1);
			}
			try {
				if(input.toLowerCase().equals("y") || input.toLowerCase().equals("yes"))
				{
					System.out.println("Please provide the file name in .txt format with complete path");	
					filePath= sc.next();
					sentenceList = getSentencesFromFile (filePath);
				}
				else if(input.toLowerCase().equals("n") || input.toLowerCase().equals("no"))
				{
					System.out.println("Please input the text in command line (enter '~' to quit)");
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

					String str;		         
					do
					{
						str = br.readLine();
						if(!("~".equals(str)))
							sentenceList.add(str);
					} while(!str.equals("~"));
				}
				else
				{
					System.out.println("Please provide a valid input");
					System.exit(1);
				}
			}				
			catch (Exception e) {
				System.out.println(e.getCause());
			}

			if(!sentenceList.isEmpty())
			{
				getWordFrequency(sentenceList);
			}
		}

	}


	private static List<String> getSentencesFromFile(String filePath)
	{
		List<String> sentenceList= new ArrayList<>();
		try
		{
			sentenceList=StreamSupport.stream(
					Files.readAllLines(
							Paths.get(filePath))
					.spliterator(), true)
					.map(aLine -> aLine.toLowerCase()).collect(Collectors.toList());	
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		} catch (Exception e) {
			System.out.println(e.getCause());
		}

		return sentenceList;
	}


	private static void getWordFrequency(List<String> inputLines) {

		Predicate<String> isEmpty = String::isEmpty;
		Predicate<String> notEmpty = isEmpty.negate();

		System.out.println("Below are the word occurrences:");
		try {
			IntStream.range(0, inputLines.size())
			.mapToObj(line -> Arrays.stream(inputLines.get(line)
					.split("[^a-zA-Z0-9]+")).filter(notEmpty)
					.map(word -> new AbstractMap.SimpleEntry<>(word.toLowerCase(), line + 1)))
			.flatMap(Function.identity())
			.collect(Collectors.groupingBy(Entry::getKey, 
					Collectors.mapping(Entry::getValue, Collectors.toList())))
			.forEach((k, v) -> System.out.println((k + " : {"+ v.size()+": " + v.toString().replaceAll("(^\\[|\\]$)", "") + "}")));

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
