package com.akka.prime.message;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

// TODO: Auto-generated Javadoc
/**
 * The Class PrimeMaster.
 */
public class PrimeMaster extends UntypedActor
{
    
    /** The worker router. */
    private final ActorRef workerRouter;
    
    /** The listener. */
    private final ActorRef listener;

    /** The number of workers. */
    private final int numberOfWorkers;
    
    /** The number of results. */
    private int numberOfResults = 0;

    /** The final results. */
    private Result finalResults = new Result();

    /**
     * Instantiates a new prime master.
     *
     * @param numberOfWorkers the number of workers
     * @param listener the listener
     */
    @SuppressWarnings("deprecation")
    public PrimeMaster( final int numberOfWorkers, ActorRef listener )
    {
        // Save our parameters locally
        this.numberOfWorkers = numberOfWorkers;
        this.listener = listener;

        // Create a new router to distribute messages out to 10 workers
        final Props props = Props.create(PrimeWorker.class);
        workerRouter = this.getContext()
                .actorOf( props
                        .withRouter( new RoundRobinRouter( numberOfWorkers )), "workerRouter" );
    }

    /* (non-Javadoc)
     * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
     */
    @Override
    public void onReceive( Object message )
    {
        if( message instanceof NumberRangeMessage )
        {
            // We have a new set of work to perform
            NumberRangeMessage numberRangeMessage = ( NumberRangeMessage )message;

            // Just as a demo: break the work up into 10 chunks of numbers
            long numberOfNumbers = numberRangeMessage.getEndNumber() - numberRangeMessage.getStartNumber();
            long segmentLength = numberOfNumbers / 10;

            for( int i=0; i<numberOfWorkers; i++ )
            {
                // Compute the start and end numbers for this worker
                long startNumber = numberRangeMessage.getStartNumber() + ( i * segmentLength );
                long endNumber = startNumber + segmentLength - 1;

                // Handle any remainder
                if( i == numberOfWorkers - 1 )
                {
                    // Make sure we get the rest of the list
                    endNumber = numberRangeMessage.getEndNumber();
                }

                // Send a new message to the work router for this subset of numbers
                workerRouter.tell( new NumberRangeMessage( startNumber, endNumber ), getSelf() );
            }
        }
        else if( message instanceof Result )
        {
            // We received results from our worker: add its results to our final results
            Result result = ( Result )message;
            finalResults.getResults().addAll( result.getResults() );

            if( ++numberOfResults >= 10 )
            {
                // Notify our listener
                listener.tell( finalResults, getSelf() );

                // Stop our actor hierarchy
                getContext().stop( getSelf() );
            }

        }
        else
        {
            unhandled( message );
        }
    }
}
