package org.code13k.perri.service.api;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import org.code13k.perri.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiHttpServer extends AbstractVerticle {
    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(ApiHttpServer.class);

    // Port
    public static final int PORT = AppConfig.getInstance().getPort().getApiHttp();

    /**
     * start()
     */
    @Override
    public void start() throws Exception {
        super.start();
        mLogger.trace("start()");

        // Init HTTP APIHttpServer
        HttpServerOptions httpServerOptions = new HttpServerOptions();
        httpServerOptions.setCompressionSupported(true);
        httpServerOptions.setPort(PORT);
        httpServerOptions.setIdleTimeout(10); // seconds
        HttpServer httpServer = vertx.createHttpServer(httpServerOptions);

        // Routing
        Router router = Router.router(vertx);
        setRouter(router);

        // Listen
        httpServer.requestHandler(router::accept).listen();
    }

    /**
     * Set router
     */
    private void setRouter(Router router) {
        // GET /ping
        router.route().method(HttpMethod.GET).path("/ping").handler(routingContext -> {
            mLogger.debug("Request host : " + routingContext.request().localAddress().host());
            routingContext.request().endHandler(new Handler<Void>() {
                @Override
                public void handle(Void event) {
                    routingContext.response().setStatusCode(200).setStatusMessage("pong").end("pong");
                }
            });
        });
    }
}