import java.io.BufferedReader;
import java.io.IOException;

public class PublicMethods {

    /**
     * Gets a username from the user via stdin.
     * If invalid input is entered user is prompted to re-enter
     * @param request The string to prompt the user with e.g. "Username: "
     * @return the inputted username
     */
    public static String getString(String request)
    {
        // Try with resources statement:
        // Automatic resource management = inputReader is closed automatically
        while (true) {
            try (BufferedReader inputReader = new BufferedReader(new StreamReader(System.in))) {
                System.out.print(request);
                return inputReader.readLine();
            }
            catch (IOException e)
            {
                System.out.println("Invalid Input!");
            }
        }
    }
    // Overloads getString() to simulate optional request parameter
    private static String getString() { return getString(""); }

}


