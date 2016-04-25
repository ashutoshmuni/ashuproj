package akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

// TODO: Auto-generated Javadoc
/**
 * The Class HelloActor.
 */
public class HelloActor extends UntypedActor {
    
    /* (non-Javadoc)
     * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
     */
    public void onReceive(Object message) {
        if (message instanceof HelloMessage) {
            System.out.println("My message is: " + ((HelloMessage) message).getMessage());
        }
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("MySystem");
        final Props props = Props.create(HelloActor.class);
        /**
         * Actor system will create a actor to refer HelloActor  
         */
        ActorRef actorRef = actorSystem.actorOf(props);
        actorRef.tell(new HelloMessage("Hello, Akka!"), actorRef);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        actorSystem.stop(actorRef);
        actorSystem.shutdown();

    }
}