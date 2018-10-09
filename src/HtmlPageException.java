public class HtmlPageException extends Exception
{
    public HtmlPageException(String errorMessage) { super(errorMessage); }

    public HtmlPageException(String errorMessage, Throwable e) { super(errorMessage, e); }
}
