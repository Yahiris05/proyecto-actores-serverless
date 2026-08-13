package com.serverless.actores;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import java.util.Map;

public class LambdaHandler implements RequestHandler<Map<String, String>, String> {
    
    // El ActorSystem se crea una sola vez para ahorrar recursos en Lambda
    private static final ActorSystem system = ActorSystem.create("ServerlessSystem");
    private static final ActorRef supervisor = system.actorOf(Props.create(SupervisorActor.class), "supervisor");

    @Override
    public String handleRequest(Map<String, String> event, Context context) {
        String input = event.getOrDefault("texto", "vacio");
        context.getLogger().log("Recibido en Lambda: " + input);

        try {
            // SOLUCIÓN: Usamos java.time.Duration explícitamente aquí
            Timeout timeout = Timeout.create(java.time.Duration.ofSeconds(5));
            Future<Object> future = Patterns.ask(supervisor, input, timeout);
            
            String resultado = (String) Await.result(future, timeout.duration());
            return "{ \"status\": \"success\", \"data\": \"" + resultado + "\" }";
            
        } catch (Exception e) {
            context.getLogger().log("Error capturado: " + e.getMessage());
            return "{ \"status\": \"error\", \"message\": \"El worker falló y fue reiniciado por el supervisor.\" }";
        }
    }
}
