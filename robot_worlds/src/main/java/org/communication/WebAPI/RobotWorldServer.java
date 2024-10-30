package org.communication.WebAPI;

import io.javalin.Javalin;
import org.communication.Infrastructure.world.World;
import static io.javalin.apibuilder.ApiBuilder.*;

public class RobotWorldServer {

    private Javalin server;

    public RobotWorldServer() {
        server = Javalin.create();
        this.server.get("/world", RobotWorldApi::getCurrentWorld);
        this.server.get("/world/{world}",RobotWorldApi::restoreSavedWorld);
        this.server.post("/robot" , RobotWorldApi::addRobot);
    }


    public void start(int port) {
        this.server.start(port);
    }

    public void stop() {
        this.server.stop();
    }

    public static void main(String[] args) {
        RobotWorldServer server = new RobotWorldServer();
        server.start(8080);
    }

}