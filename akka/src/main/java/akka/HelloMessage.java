package akka;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class HelloMessage.
 */
public class HelloMessage implements Serializable
{
    
    /** The message. */
    private String message;

    /**
     * Instantiates a new hello message.
     *
     * @param message the message
     */
    public HelloMessage(String message)
    {
        this.message = message;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message the new message
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
}