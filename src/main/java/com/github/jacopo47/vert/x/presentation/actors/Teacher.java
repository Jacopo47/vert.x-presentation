package com.github.jacopo47.vert.x.presentation.actors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Teacher extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(Teacher.class);

  private final String schoolClass;
  private EventBus eventBus;


  public Teacher(final String schoolClass) {
    this.schoolClass = schoolClass;
  }

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    this.eventBus = this.vertx.eventBus();
    startPromise.complete();
  }

  public void sayGoodMorning() {
    log.info("Good morning class!");
    this.getEventBus().ifPresent(eb -> eb.publish("school.class." + schoolClass + ".goodmorning",
        "Good morning class!! Let's start with our lesson"));
  }

  public Future<Message<Object>> makeQuestion(final String question) {
    log.info(question);
    return this.getEventBus()
        .map(eb -> eb.request("school.class." + schoolClass + ".question", question))
        .orElseGet(() -> Future.failedFuture("internal error"))
        .onFailure(res -> log.warn("So sad.."));
  }

  public Optional<EventBus> getEventBus() {
    return Optional.ofNullable(eventBus);
  }
}
