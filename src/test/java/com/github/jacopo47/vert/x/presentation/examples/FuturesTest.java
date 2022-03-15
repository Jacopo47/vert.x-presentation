package com.github.jacopo47.vert.x.presentation.examples;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import java.io.File;
import org.junit.Test;

public class FuturesTest {

  @Test
  public void futureSimpleTest() {
    FileSystem fs = Vertx.vertx().fileSystem();

    Future<FileProps> future = fs.props("./");
    assertFalse(future.isComplete());

    future
        .compose(Future::succeededFuture)
        .compose(e -> {
          System.out.println("Second step");
          return Future.failedFuture("Props corrupted");
        })
        .transform(f -> {
          assertFalse(f.succeeded());
          return Future.failedFuture(f.cause());
        })
        .recover(t -> {
          System.out.println("I run if someone before me is failing!");
          return Future.succeededFuture(fs.propsBlocking("./"));
        })
        .onComplete(e -> {
          assertTrue(e.succeeded());
        });
  }

  @Test
  public void futureJoin() {

    Future<Void> f1 = Vertx.vertx().executeBlocking(event -> {
      try {
        System.out.println("App");
        Thread.sleep(10000);
        event.complete();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    Future<FileProps> f2 = Vertx.vertx().fileSystem().props("./");
    Future<FileProps> f3 = Vertx.vertx().fileSystem().props("./thisfiledoesntexists.txt");

    CompositeFuture.join(f1, f2, f3)
        .onComplete(e -> assertTrue(e.failed()));

    CompositeFuture.join(f1, f2)
        .onComplete(e -> assertTrue(e.succeeded()));
  }
}
