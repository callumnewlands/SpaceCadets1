import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class HtmlPage {

	private ArrayList<String> _sourceCode = new ArrayList<>();

	HtmlPage(String address) throws HtmlPageException
	{
		URL page;
		try
		{
			page = new URL(address);
		}
		catch (MalformedURLException e)
		{
			throw new HtmlPageException("Cannot open URL: " + address, e);  // Including e in constructor ensures stack trace is not lost
		}

		try (BufferedReader pageReader = new BufferedReader(new InputStreamReader(page.openStream())))
		{
			String line;
			while ((line = pageReader.readLine()) != null)
				_sourceCode.add(line);
		}
		catch (IOException e)
		{
			throw new HtmlPageException("Cannot open URL: " + address, e);  // Including e in constructor ensures stack trace is not lost
		}

	}

	/**
	 * Removes any html tags from a line of code and returns the contents of the tags
	 * @param code the line of code to strip tags from
	 * @return the code without tags
	 */
	static String stripTags(String code)
	{
		Pattern tagPattern = Pattern.compile("<\\/?\\s*[^>]*>");
		Matcher nameMatcher = tagPattern.matcher(code);
		return nameMatcher.replaceAll("");
	}

	/**
	 * Gets a String containing the full source code of the HtmlPage, including new-line characters
	 * @return the full source code as a String
	 */
	String getSourceCode()
	{
		String str = "";
		for (int i = 0; i < _sourceCode.size(); i++)
		{
			str += _sourceCode.get(i) + System.lineSeparator();
		}
		return str;
	}

	/**
	 * Returns a particular line of the page's source code
	 * @param lineNumber The 1-based index line number to return
	 * @return A String containing the requested line
	 * @throws IllegalArgumentException if given line number is outside bounds of array
	 */
	String getSourceCode(int lineNumber) throws IllegalArgumentException
	{
		if (lineNumber <= 0 || lineNumber > _sourceCode.size())
			throw new IllegalArgumentException("Invalid Line Number");
		
		return _sourceCode.get(lineNumber - 1);
	}

	/**
	 * Returns the line number of the first match with the given regular expression
	 * @param regex The expression to match
	 * @return The line number of the first match
	 * @throws IllegalArgumentException if the expression cannot be found in the source code
	 */
	int findRegex(String regex) throws IllegalArgumentException
	{
		Pattern nameSearch = Pattern.compile(regex);
		int i = 0;
		boolean found = false;
		
		while (i < _sourceCode.size() && !found)
		{
			Matcher nameMatcher = nameSearch.matcher(_sourceCode.get(i));	
			found = nameMatcher.find();
			i++;
		}
		if (!found)
			throw new IllegalArgumentException("String not found in source code");
		
		return i;
	}

	
}
