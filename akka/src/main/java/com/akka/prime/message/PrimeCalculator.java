package com.akka.prime.message;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

// TODO: Auto-generated Javadoc
/**
 * The Class PrimeCalculator.
 */
public class PrimeCalculator
{
    
    /**
     * Calculate.
     *
     * @param startNumber the start number
     * @param endNumber the end number
     */
    public void calculate( long startNumber, long endNumber )
    {
        // Create our ActorSystem, which owns and configures the classes
        ActorSystem actorSystem = ActorSystem.create( "primeCalculator" );

        // Create our listener
        final ActorRef primeListener = actorSystem.actorOf( Props.create( PrimeListener.class ), "primeListener" );

        // Create the PrimeMaster: we need to define an UntypedActorFactory so that we can control
        // how PrimeMaster instances are created (pass in the number of workers and listener reference
        final Props props = Props.create(PrimeMaster.class,10,primeListener);
/*        final Props props = Props.create( new Creator<PrimeMaster>() {
            *//**
             * 
             *//*
            private static final long serialVersionUID = 1L;

            public PrimeMaster create() {
                return new PrimeMaster( 10, primeListener );
            }
        });
*/        
        ActorRef primeMaster = actorSystem.actorOf( props, "primeMaster" );

        // Start the calculation
        primeMaster.tell( new NumberRangeMessage( startNumber, endNumber ),primeMaster );
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main( String[] args )
    {
        /*if( args.length < 2 )
        {
            System.out.println( "Usage: java com.geekcap.akka.prime.PrimeCalculator <start-number> <end-number>" );
            System.exit( 0 );
        }*/

        long startNumber = Long.parseLong( "1" );
        long endNumber = Long.parseLong( "10" );

        PrimeCalculator primeCalculator = new PrimeCalculator();
        primeCalculator.calculate( startNumber, endNumber );
    }
}
class CreatorCreation{
    
}