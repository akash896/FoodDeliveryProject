package akka.chatroom;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import akka.http.javadsl.marshallers.jackson.Jackson;

import static akka.http.javadsl.server.Directives.*;

import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;

import static akka.http.javadsl.server.Directives.complete;
import static akka.http.javadsl.server.Directives.path;
import static akka.http.javadsl.server.Directives.pathEnd;
import static akka.http.javadsl.server.Directives.pathPrefix;
import static akka.http.javadsl.server.Directives.pathSingleSlash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeRoutes {
    private final static Logger log = LoggerFactory.getLogger(NodeRoutes.class);
    private final ActorRef<MyCounter.CounterCommand> counter;
    private final Duration askTimeout;
    private final Scheduler scheduler;

    public NodeRoutes(ActorSystem<?> system, ActorRef<MyCounter.CounterCommand> counter) {
      this.counter = counter;
      scheduler = system.scheduler();
      askTimeout = system.settings().config().getDuration("my-app.routes.ask-timeout");
    }
  
    private CompletionStage<MyCounter.CalculatePerformed> calculate(String type, Integer value) {
      return AskPattern.ask(counter, ref -> 
            new MyCounter.CalculateWeb(type, value, ref), askTimeout, scheduler
        );
    }
  
    // private CompletionStage<UserRegistry.ActionPerformed> deleteUser(String name) {
    //   return AskPattern.ask(userRegistryActor, ref -> new UserRegistry.DeleteUser(name, ref), askTimeout, scheduler);
    // }
  
    // private CompletionStage<UserRegistry.Users> getUsers() {
    //   return AskPattern.ask(userRegistryActor, UserRegistry.GetUsers::new, askTimeout, scheduler);
    // }
  
    // private CompletionStage<UserRegistry.ActionPerformed> createUser(User user) {
    //   return AskPattern.ask(userRegistryActor, ref -> new UserRegistry.CreateUser(user, ref), askTimeout, scheduler);
    // }
  
    /**
     * This method creates one route (of possibly many more that will be part of your Web App)
     */
    //#all-routes


    // public Route userRoutes() {
    //   return pathPrefix("calculate", () ->
    //       concat(
    //           path(PathMatchers.segment(), (String type) ->
    //               concat(
    //                 path(PathMatchers.segment(), (String value) ->
    //                     concat(
    //                         get(() ->
    //                                 rejectEmptyResponse(() ->
    //                                     onSuccess(calculate(type, Integer.parseInt(value)), performed -> complete(StatusCodes.OK, performed, Jackson.marshaller()))
    //                                 )
    //                         )
    //                     )
    //                 )
    //               )
    //           )
    //       )
    //   );
    // }


    // public Route userRoutes() {
    //     return pathPrefix("calculate", () ->
    //         concat(
    //             get(() ->
    //                 rejectEmptyResponse(() ->
    //                     onSuccess(calculate("add", 12), performed -> complete(StatusCodes.OK, performed, Jackson.marshaller()))
    //                 )
    //             )
    //         )
    //     );
    //   }


    public Route userRoutes() {
      return pathPrefix("calculate", () ->
              path(PathMatchers.segment(), (String type) -> route(
                    pathEndOrSingleSlash(() -> route(
                            get(() ->
                                    rejectEmptyResponse(() ->
                                        onSuccess(calculate(type, 12), performed -> complete(StatusCodes.OK, performed, Jackson.marshaller()))
                                    )
                            )
                        )
                    ),
                    path(PathMatchers.integerSegment(), (Integer value) -> route(
                            get(() ->
                                    rejectEmptyResponse(() ->
                                        onSuccess(calculate(type, value), performed -> complete(StatusCodes.OK, performed, Jackson.marshaller()))
                                    )
                            )
                        )
                    )
                )
            )
        );
    }
}