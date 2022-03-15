package com.github.jacopo47.vert.x.presentation.actors;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Student extends AbstractVerticle {
  private static final Logger log = LoggerFactory.getLogger(Student.class);

  private final String schoolClass;
  private final String name;


  public Student(final String schoolClass, final String name) {
    this.schoolClass = schoolClass;
    this.name = name;
  }

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    final EventBus eventBus = this.vertx.eventBus();

    eventBus.consumer("school.class." + schoolClass + ".goodmorning", message -> {
      log.info("{}: Good morning teacher! I'm ready for this lesson.", this.name);
    });

    eventBus.consumer("school.class." + schoolClass + ".question", question -> {
      log.info("{}: Teacher I had to be honest I dunno how to answer to your question: {}", this.name, question.body());
      question.fail(1, "KO");
    });

    startPromise.complete();
  }
}
