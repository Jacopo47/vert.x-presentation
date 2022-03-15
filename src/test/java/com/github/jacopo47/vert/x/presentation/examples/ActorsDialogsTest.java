package com.github.jacopo47.vert.x.presentation.examples;

import com.github.jacopo47.vert.x.presentation.actors.Student;
import com.github.jacopo47.vert.x.presentation.actors.Teacher;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ActorsDialogsTest {

  private final Teacher teacher = new Teacher("A");
  private final Vertx vertx = Vertx.vertx();

  @Before
  public void setup() {
    vertx.deployVerticle(new Student("A", "jacopo"));
    vertx.deployVerticle(new Student("A", "jack"));
    vertx.deployVerticle(new Student("B", "matt"));
    vertx.deployVerticle(teacher);

  }

  @Test
  public void goodMorning(TestContext ctx) {
    final Async async = ctx.async();
    vertx.eventBus().consumer("school.class.A.goodmorning", m -> async.complete());

    teacher.sayGoodMorning();
    async.await(10000);
  }


  @Test
  public void questionAndAnswer(TestContext ctx) {
    final Async async = ctx.async();
    vertx.eventBus().consumer("school.class.A.goodmorning", m -> async.complete());

    teacher.makeQuestion("Is tomato a fruit?")
        .onComplete(res -> {
          async.complete();
        });
    async.await(10000);
  }

}
