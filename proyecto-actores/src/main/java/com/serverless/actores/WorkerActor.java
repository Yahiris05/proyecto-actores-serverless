package com.serverless.actores;

import akka.actor.AbstractActor;

public class WorkerActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(String.class, mensaje -> {
                if (mensaje.equals("ERROR")) {
                    // Simulamos un fallo para que el supervisor actúe
                    System.out.println("Worker: ¡Fallo intencional detectado!");
                    throw new RuntimeException("Fallo simulado en el Worker");
                }
                // Procesamos la tarea (convertir a mayúsculas)
                String resultado = "PROCESADO: " + mensaje.toUpperCase();
                System.out.println("Worker: Tarea completada -> " + resultado);
                getSender().tell(resultado, getSelf());
            })
            .build();
    }
}
