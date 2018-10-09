import java.io.BufferedReader;
import java.io.IOException;
import java.lang.UnsupportedOperationException;

public class Main 
{
	public static void main(String[] args)
    {
    	boolean exit = false;
    	while (!exit)
		{
			exit = false;
			switch (getMenuChoice())
			{
				case 1:
					nameFromUsername();
					break;
				case 2:
					anagrams();
					break;
				case 0:
					exit = true;
					break;
				default:
					break;
			}

			System.out.println("\nPress [Enter] to continue...");
			try { System.in.read(); } catch (IOException e) {} // Wait for enter press

		}
    }

	/**
	 * Gets and outputs possible anagrams for a string gotten from the user
	 */
	private static void anagrams()
	{
		String word = PublicMethods.getString("Word to find anagrams of: ");
		word = word.replace(' ', '+');
		String url = "https://new.wordsmith.org/anagram/anagram.cgi?anagram=" + word;
		HtmlPage page = null;
		try
		{
			page = new HtmlPage(url);
		}
		catch (HtmlPageException e)
		{
			System.out.println(e.getMessage());
			return;
		}

		//TODO catch IllegalArgumentException from findRegex()

		try
		{
			String numberLine = page.getSourceCode(page.findRegex("\\d* found."));
			int startIndex = numberLine.indexOf("found") - 2;
			int endIndex = numberLine.indexOf(' ', startIndex);
			int numberOfAnagrams = Integer.parseInt(numberLine.substring(startIndex, endIndex));

			System.out.println(numberOfAnagrams + " anagrams found...\n");

			int startLine = page.findRegex("Displaying all:") + 1;

			for (int i = 0; i < numberOfAnagrams; i++)
				System.out.println(HtmlPage.stripTags(page.getSourceCode(startLine + i)));

		}
		catch (IllegalArgumentException e)
		{
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Gets an ECS username from the user, converts it to a name, and prints the name to the stdio
	 */
	private static void nameFromUsername()
	{
		boolean validInput = false;
		String username = PublicMethods.getString("Username: ");

		String address = "https://www.ecs.soton.ac.uk/people/" + username;
		HtmlPage page;

		try
		{
			page = new HtmlPage(address);
		}
		catch (HtmlPageException e) // Catch error with opening the web page
		{
			System.out.println(e.getMessage());
			return;
		}

		try
		{
			System.out.println(getName(page));
		}
		catch (IllegalArgumentException e)
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Searches a HtmlPage for the name of the person whom the page is about
	 * @param page the HtmlPage to get the name from
	 * @return the name of the person
	 * @throws IllegalArgumentException
	 */
	private static String getName(HtmlPage page) throws IllegalArgumentException
	{
		int lineNo;
		try
		{
			lineNo = page.findRegex("property=\"name\">");
		}
		catch (IllegalArgumentException e) // Catch error with not being able to find "property="name">"
		{
			throw new IllegalArgumentException("Cannot determine name from source code - username may be incorrect, \n" +
												"or the details may not be publicly available", e);
		}

		try
		{
			String line = page.getSourceCode(lineNo);
			// Finds the first occurrence of "property="name">" in the line, then adds 16 to find start index of name
			int startIndex = line.indexOf("property=\"name\">") + 16;
			// Finds the first occurrence of "<" after startIndex
			int endIndex = line.indexOf('<', startIndex);
			return line.substring(startIndex, endIndex);
		}
		catch (IllegalArgumentException e) // Catch error with invalid line number in getSourceCode.
		{
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Displays the main menu for the application
	 */
	private static void displayMenu()
	{
		System.out.println("--------Main Menu--------");
		System.out.println("    1. ECS Username to Name");
		System.out.println("    2. Anagram Generator");
		System.out.println("    ");
		System.out.println("    0. Exit");
		System.out.println();
	}

	/**
	 * Gets the user's choice for the main menu
	 */
	private static int getMenuChoice()
	{
		boolean validInput;
		do {
			validInput = true;
			displayMenu();
			System.out.print("Enter choice number: ");
			try (BufferedReader inputReader = new BufferedReader(new StreamReader(System.in))) {
				return Integer.parseInt(inputReader.readLine());
			} catch (IOException | NumberFormatException e) {
				System.out.println("Invalid Input \n");
				validInput = false;
			}
			finally
			{
				System.out.println();
			}

		} while(!validInput);

		// Should never be reached
		throw new UnsupportedOperationException();
	}
}
