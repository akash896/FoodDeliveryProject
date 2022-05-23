package akka.chatroom;

import akka.actor.typed.*;
import akka.actor.typed.javadsl.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MyNode extends AbstractBehavior<MyCounter.NodeCommand> {
  int value = 0;
  String type = "+";
  ActorRef<MyCounter.CounterCommand> counterTell;

  public static Behavior<MyCounter.NodeCommand> create(ActorRef<MyCounter.CounterCommand> counter, String type) {
    return Behaviors.setup(context -> new MyNode(context, counter, type));
    // return Behaviors.setup(Gabbler::new); // Gabbler::new is a function of type
    // ActorContext<ChatRoom.SessionEvent> -> Gabbler
    // equivalent to "return new Gabbler();"
  }

  private MyNode(ActorContext<MyCounter.NodeCommand> context, ActorRef<MyCounter.CounterCommand> counter, String type) {
    super(context);
    this.counterTell = counter;
    this.type = type;
  }

  @Override
  public Receive<MyCounter.NodeCommand> createReceive() {
    ReceiveBuilder<MyCounter.NodeCommand> builder = newReceiveBuilder();
    return builder
      .onMessage(MyCounter.NodeDenied.class, this::onNodeDenied)
      .onMessage(MyCounter.NodeDo.class, this::onNodeDo)
      .build();
      // .onMessage(MyCounter.NodeGranted.class, this::onNodeGranted)
  }

  private Behavior<MyCounter.NodeCommand> onNodeDenied(MyCounter.NodeDenied message) {
    getContext().getLog().info("Gabbler was not granted chat room session due to: {}", message.reason);
    return Behaviors.stopped();
  }

  // private Behavior<MyCounter.NodeCommand> onNodeGranted(MyCounter.NodeGranted message) {
  //   counterTell = message.handle;
  //   // mySession.tell(new ChatRoom.PostMessage("Hello World " + messageCount + "!"));
  //   return this;
  // }

  private Behavior<MyCounter.NodeCommand> onNodeDo(MyCounter.NodeDo message) {
    if (this.type.equals(message.op)) {
      Integer old = this.value;
      if (this.type.equals("add")) {
        this.value += message.val;
      }
      if (this.type.equals("sub")) {
        this.value -= message.val;
      }
      if (this.type.equals("mul")) {
        this.value *= message.val;
      }
      if (this.type.equals("div")) {
        this.value /= message.val;
      }
      getContext().getLog().info("calculate '{}' '{}' '{}' = '{}'", old, this.type, message.val, this.value);
    }
    return this;
  }
}