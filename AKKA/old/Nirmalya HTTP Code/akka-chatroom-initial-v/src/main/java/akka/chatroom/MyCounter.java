package akka.chatroom;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MyCounter {
  // node command
  interface NodeCommand { }

  public static final class NodeDenied implements NodeCommand {

    public final String reason;

    public NodeDenied(String reason) {
      this.reason = reason;
    }
  }

  public static final class NodeDo implements NodeCommand {

    public final String op;
    public final Integer val;

    public NodeDo(String op, Integer val) {
      this.op = op;
      this.val = val;
    }
  }



  // counter command

  static interface CounterCommand { }

  public static final class GetNode implements CounterCommand {

    public final String type;
    public final ActorRef<CounterCommand> counterAddr;

    public GetNode(String type, ActorRef<CounterCommand> counterAddr) {
      this.type = type;
      this.counterAddr = counterAddr;
    }
  }

  public static final class Calculate implements CounterCommand {

    public final Integer value;
    public final String type;

    public Calculate(String type, Integer value) {
      this.type = type;
      this.value = value;
    }
  }

  public static final class CalculateWeb implements CounterCommand {
    public final Integer value;
    public final String type;
    public final ActorRef<CalculatePerformed> replyTo;

    public CalculateWeb(String type, Integer value, ActorRef<CalculatePerformed> replyTo) {
      this.type = type;
      this.value = value;
      this.replyTo = replyTo;
    }
  }

  public static final class CalculatePerformed implements CounterCommand {
    public final String description;
    public CalculatePerformed(String description) {
      this.description = description;
    }
  }



  public static Behavior<CounterCommand> create() {
    return Behaviors.setup(MyCounterBehaviour::new);
  }

  public static class MyCounterBehaviour extends AbstractBehavior<CounterCommand> {

    final HashMap<String, ActorRef<NodeCommand>> nodes = new HashMap<String, ActorRef<NodeCommand>>();

    private MyCounterBehaviour(ActorContext<CounterCommand> context) {
      super(context);
    }

    @Override
    public Receive<CounterCommand> createReceive() {
      ReceiveBuilder<CounterCommand> builder = newReceiveBuilder();

      builder.onMessage(GetNode.class, this::onGetNode);
      builder.onMessage(Calculate.class, this::onCalculate);
      builder.onMessage(CalculateWeb.class, this::onCalculateWeb);

      return builder.build();
    }

    private Behavior<CounterCommand> onGetNode(GetNode getNode) throws UnsupportedEncodingException {
      // ActorRef<CounterCommand> client = getNode.counterAddr;
      // ActorRef<MyCounter.NodeEvent> node1 = getContext().spawn(MyNode.create(), "node1");

      // getContext().getSelf()
      ActorRef<NodeCommand> nod = getContext().spawn(MyNode.create(getNode.counterAddr, getNode.type), // 
          URLEncoder.encode(getNode.type, StandardCharsets.UTF_8.name()));
      // narrow to only expose PostMessage
      // nod.tell(new NodeGranted(nod.narrow()));
      nodes.put(getNode.type, nod);
      return this;
    }

    private Behavior<CounterCommand> onCalculate(Calculate pub) {
      // NotifyClient notification = new NotifyClient((new MessagePosted(pub.screenName, pub.message)));
      // NotifyClient notification = new NotifyClient(new NodeDo(pub.type, pub.value));
      ActorRef<NodeCommand> s = nodes.get(pub.type);
      s.tell(new NodeDo(pub.type, pub.value));
      return this;
    }

    private Behavior<CounterCommand> onCalculateWeb(CalculateWeb pub) {
      // NotifyClient notification = new NotifyClient((new MessagePosted(pub.screenName, pub.message)));
      // NotifyClient notification = new NotifyClient(new NodeDo(pub.type, pub.value));
      ActorRef<NodeCommand> s = nodes.get(pub.type);
      s.tell(new NodeDo(pub.type, pub.value));

      pub.replyTo.tell(new CalculatePerformed(String.format("calculate done '{}' with '{}'", pub.type, pub.value)));
      // command.replyTo.tell(new Users(Collections.unmodifiableList(new ArrayList<>(users))));
      return this;
    }
  }
}