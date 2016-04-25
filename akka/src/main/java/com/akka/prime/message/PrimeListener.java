package com.akka.prime.message;

import akka.actor.UntypedActor;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving prime events.
 * The class that is interested in processing a prime
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addPrimeListener<code> method. When
 * the prime event occurs, that object's appropriate
 * method is invoked.
 *
 * @see PrimeEvent
 */
public class PrimeListener extends UntypedActor
{
    
    /* (non-Javadoc)
     * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
     */
    @Override
    public void onReceive( Object message ) throws Exception
    {
        if( message instanceof Result )
        {
            Result result = ( Result )message;

            System.out.println( "Results: " );
            for( Long value : result.getResults() )
            {
                System.out.print( value + ", " );
            }
            System.out.println();

            // Exit
            getContext().system().shutdown();
        }
        else
        {
            unhandled( message );
        }
    }
}