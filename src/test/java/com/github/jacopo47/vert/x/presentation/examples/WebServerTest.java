package com.github.jacopo47.vert.x.presentation.examples;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(VertxUnitRunner.class)
public class WebServerTest {

  private final static Logger log = LoggerFactory.getLogger("this");

  @Test
  public void webServer(final TestContext testContext) {
    final Vertx vertx = Vertx.vertx();
    final HttpServer server = vertx.createHttpServer();
    final Router router = Router.router(vertx);

    router.get("/test").handler(ctx -> {
      ctx.put("foo", "bar");
      ctx.next();
    }).handler(ctx -> {
      ctx.response().end(ctx.get("foo", "not found"));
    });

    final Async async = testContext.async();
    server.requestHandler(router).listen(9000);
    WebClient.create(vertx).request(HttpMethod.GET, 9000, "localhost", "/test")
        .send()
        .onSuccess(res -> {
          testContext.assertEquals("bar", res.bodyAsString());
          async.complete();
        })
        .onFailure(testContext::fail);

    async.await(10000);
  }

}
