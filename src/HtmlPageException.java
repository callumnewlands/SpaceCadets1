class HtmlPageException extends Exception
{
    HtmlPageException(String errorMessage) { super(errorMessage); }

    HtmlPageException(String errorMessage, Throwable e) { super(errorMessage, e); }
}
