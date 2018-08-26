package org.code13k.perri.service.api;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.*;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.code13k.perri.config.AppConfig;
import org.code13k.perri.service.api.controller.AppAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiHttpServer extends AbstractVerticle {
    // Logger
    private static final Logger mLogger = LoggerFactory.getLogger(ApiHttpServer.class);

    // Const
    public static final int PORT = AppConfig.getInstance().getPort().getApiHttp();

    // Data
    private AppAPI mAppAPI = new AppAPI();

    /**
     * start()
     */
    @Override
    public void start() throws Exception {
        super.start();
        mLogger.trace("start()");

        // Init
        HttpServerOptions httpServerOptions = new HttpServerOptions();
        httpServerOptions.setCompressionSupported(true);
        httpServerOptions.setPort(PORT);
        httpServerOptions.setIdleTimeout(5); // seconds
        HttpServer httpServer = vertx.createHttpServer(httpServerOptions);

        // Routing
        Router router = Router.router(vertx);
        setStatusRouter(router);

        // Listen
        httpServer.requestHandler(router::accept).listen();

        // End
        logging(httpServerOptions, router);
    }

    /**
     * Logging
     */
    private void logging(HttpServerOptions httpServerOptions, Router router) {
        // Begin
        mLogger.info("------------------------------------------------------------------------");
        mLogger.info("API HTTP Server");
        mLogger.info("------------------------------------------------------------------------");

        // Http Server Options
        mLogger.info("Port = " + httpServerOptions.getPort());
        mLogger.info("Idle timeout (second) = " + httpServerOptions.getIdleTimeout());
        mLogger.info("Compression supported = " + httpServerOptions.isCompressionSupported());
        mLogger.info("Compression level = " + httpServerOptions.getCompressionLevel());

        // Route
        router.getRoutes().forEach(r -> {
            mLogger.info("Routing path = " + r.getPath());
        });

        // End
        mLogger.info("------------------------------------------------------------------------");
    }


    /**
     * Set status router
     */
    private void setStatusRouter(Router router) {
        // GET /info
        router.route().method(HttpMethod.GET).path("/app/status").handler(routingContext -> {
            routingContext.request().endHandler(new Handler<Void>() {
                @Override
                public void handle(Void event) {
                    responseHttpOK(routingContext, mAppAPI.info());
                }
            });
        });
        // GET /hello
        router.route().method(HttpMethod.GET).path("/app/hello").handler(routingContext -> {
            routingContext.request().endHandler(new Handler<Void>() {
                @Override
                public void handle(Void event) {
                    responseHttpOK(routingContext, mAppAPI.hello());
                }
            });
        });
        // GET /ping
        router.route().method(HttpMethod.GET).path("/app/ping").handler(routingContext -> {
            routingContext.request().endHandler(new Handler<Void>() {
                @Override
                public void handle(Void event) {
                    responseHttpOK(routingContext, mAppAPI.ping());
                }
            });
        });
    }

    /**
     * Response HTTP 200 OK
     */
    private void responseHttpOK(RoutingContext routingContext, String message) {
        HttpServerResponse response = routingContext.response();
        response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.setStatusCode(200);
        response.setStatusMessage("OK");
        response.end(message);
        response.close();
    }

    /**
     * Response HTTP error status
     */
    private void responseHttpError(RoutingContext routingContext, int statusCode, String message) {
        HttpServerResponse response = routingContext.response();
        response.putHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
        response.setStatusCode(statusCode);
        response.setStatusMessage(message);
        response.end(message);
        response.close();
    }
}