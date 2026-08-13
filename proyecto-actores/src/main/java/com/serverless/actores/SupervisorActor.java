package com.serverless.actores;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import scala.concurrent.duration.Duration;

public class SupervisorActor extends AbstractActor {
    
    // Estrategia: Si hay un error, reiniciamos al worker
    private static SupervisorStrategy strategy =
        new OneForOneStrategy(10, Duration.create("1 minute"), DeciderBuilder
            .match(RuntimeException.class, e -> SupervisorStrategy.restart())
            .build());

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    // Creamos al worker que estará bajo nuestra supervisión
    private ActorRef worker = getContext().actorOf(Props.create(WorkerActor.class), "worker1");

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, mensaje -> {
                // Pasamos el trabajo al worker
                worker.forward(mensaje, getContext());
            })
            .build();
    }
}