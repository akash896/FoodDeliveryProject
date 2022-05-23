package akka.chatroom;

// import akka.actor.typed.*;
// import akka.actor.typed.javadsl.*;



import akka.NotUsed;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.Route;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.actor.typed.javadsl.Adapter;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.ActorSystem;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletionStage;

public class Main {
	static void startHttpServer(Route route, ActorSystem<?> system) {
		CompletionStage<ServerBinding> futureBinding = Http.get(system).newServerAt("localhost", 8080).bind(route);

		futureBinding.whenComplete((binding, exception) -> {
			if (binding != null) {
				InetSocketAddress address = binding.localAddress();
				system.log().info("Server online at http://{}:{}/", address.getHostString(), address.getPort());
			} else {
				system.log().error("Failed to bind HTTP endpoint, terminating system", exception);
				system.terminate();
			}
		});
	}

	public static Behavior<Void> create() {
	    return Behaviors.setup(
	        context -> {
	          ActorRef<MyCounter.CounterCommand> counter = context.spawn(MyCounter.create(), "counter");
			  counter.tell(new MyCounter.GetNode("add", counter));
			  counter.tell(new MyCounter.GetNode("sub", counter));
	        //   ActorRef<MyCounter.NodeEvent> node1 = context.spawn(MyNode.create(), "node1");
	        //   ActorRef<MyCounter.NodeEvent> node2 = context.spawn(MyNode.create(), "node1");

	          //context.watch(gabbler1);
	          //context.watch(gabbler2);

	          counter.tell(new MyCounter.Calculate("add", 20));
	          counter.tell(new MyCounter.Calculate("sub", 10));
	          counter.tell(new MyCounter.Calculate("add", 10));
	          counter.tell(new MyCounter.Calculate("sub", 30));

			  NodeRoutes userRoutes = new NodeRoutes(context.getSystem(), counter);
			  startHttpServer(userRoutes.userRoutes(), context.getSystem());

	          return Behaviors.empty(); // don't want to receive any more messages
	        });
	  }

	  public static void main(String[] args) {
	    ActorSystem.create(Main.create(), "MyCounterDemo"); // spawn "user guardian" actor
	  }
}