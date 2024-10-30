package org.communication.Domain.headers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ServerResponseHandler {
    // High Intensity Colour
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String RESET = "\033[0m";
    /**
     * Displays the server response parsed from JSON format.
     *
     * @param serverResponse The server response in JSON format.
     */
    public static void displayServerResponse(String serverResponse) {
        if (serverResponse == null || serverResponse.trim().isEmpty()) {
            System.out.println("Received an empty or null JSON response.");
            return;
        }

        try {
            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(serverResponse, JsonElement.class);

            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                // Unpack and print data with safety checks
                if (jsonObject.has("result")) {
                    String result = jsonObject.get("result").getAsString();
                    System.out.println(RED_BRIGHT + "Result: " + RESET + YELLOW_BRIGHT + result + RESET);
                }

                JsonObject data = jsonObject.has("data") ? jsonObject.getAsJsonObject("data") : null;
                JsonObject state = jsonObject.has("state") ? jsonObject.getAsJsonObject("state") : null;

                if (data != null) {
                    System.out.println(RED_BRIGHT + "Data:" + RESET);
                    if (data.has("message")) {
                        System.out.println(BLUE_BRIGHT + "  Message    :  " + RESET + YELLOW_BRIGHT +  data.get("message").getAsString() + RESET);
                    }
                    if (data.has("object")) {
                        JsonArray objects = data.getAsJsonArray("object");
                        System.out.println(RED_BRIGHT + "  Objects:" + RESET);
                        for (JsonElement objElement : objects) {
                            JsonObject obj = objElement.getAsJsonObject();
                            String direction = obj.has("direction") ? obj.get("direction").getAsString() : "N/A";
                            String type = obj.has("type") ? obj.get("type").getAsString() : "N/A";
                            int distance = obj.has("distance") ? obj.get("distance").getAsInt() : -1;
                            System.out.println(BLUE_BRIGHT + "    Direction: " + RESET + YELLOW_BRIGHT + direction + BLUE_BRIGHT + ", Type: " + RESET + YELLOW_BRIGHT+ type + BLUE_BRIGHT +", Distance: "+ RESET + YELLOW_BRIGHT + distance + RESET);
                        }
                    }
                    // Other data fields you might want to display
                    if (data.has("repair")) {
                        System.out.println(BLUE_BRIGHT + "  Repair     :  " + RESET + YELLOW_BRIGHT + data.get("repair").getAsString() + RESET);
                    }
                    if (data.has("shields")) {
                        System.out.println(BLUE_BRIGHT +"  Shields    :  "+ RESET + YELLOW_BRIGHT + data.get("shields").getAsInt()+ RESET);
                    }
                    if (data.has("reload")) {
                        System.out.println(BLUE_BRIGHT +"  Reload     :  " + RESET + YELLOW_BRIGHT+ data.get("reload").getAsString()+ RESET);
                    }
                    if (data.has("visibility")) {
                        System.out.println(BLUE_BRIGHT +"  Visibility :  "+ RESET + YELLOW_BRIGHT + data.get("visibility").getAsString()+ RESET);
                    }
                    if (data.has("direction")){
                        System.out.println(BLUE_BRIGHT +"  Direction   :  "+ RESET + YELLOW_BRIGHT + data.get("direction").getAsString()+ RESET);
                    }
                    if (data.has("position")) {
                        System.out.println(BLUE_BRIGHT +"  Position   :  "+ RESET + YELLOW_BRIGHT + data.get("position").getAsString()+ RESET);
                    }
                    if (data.has("distance")){
                        System.out.println(BLUE_BRIGHT +"  Distance   :  "+ RESET + YELLOW_BRIGHT + data.get("distance").getAsString()+ RESET);
                    }
                    if (data.has("robot")){
                        System.out.println(BLUE_BRIGHT + "  Name       :  " + RESET + YELLOW_BRIGHT + data.get("robot").getAsString() + RESET);
                    }
                    if (data.has("state")) {
                        System.out.println(RED_BRIGHT + "  State:" + RESET);
                        JsonObject dataState = data.getAsJsonObject("state");
                        if (dataState.has("position")) {
                            System.out.println(BLUE_BRIGHT +"    Position   :  "+ RESET + YELLOW_BRIGHT + dataState.get("position").getAsString()+ RESET);
                        }
                        if (dataState.has("direction")) {
                            System.out.println(BLUE_BRIGHT +"    Direction  :  "+ RESET + YELLOW_BRIGHT + dataState.get("direction").getAsString()+ RESET);
                        }
                        if (dataState.has("shields")) {
                            System.out.println(BLUE_BRIGHT +"    Shields    :  "+ RESET + YELLOW_BRIGHT + dataState.get("shields").getAsInt()+ RESET);
                        }
                        if (dataState.has("shots")) {
                            System.out.println(BLUE_BRIGHT +"    Shots      :  "+ RESET + YELLOW_BRIGHT + dataState.get("shots").getAsInt()+ RESET);
                        }
                        if (dataState.has("status")) {
                            System.out.println(BLUE_BRIGHT +"    Status     :  "+ RESET + YELLOW_BRIGHT + dataState.get("status").getAsString()+ RESET);
                        }
                    }


                }

                if (state != null) {
                    System.out.println(RED_BRIGHT +"State:" + RESET);
                    if (state.has("position")) {
                        System.out.println(BLUE_BRIGHT +"  Position   :  "+ RESET + YELLOW_BRIGHT + state.get("position").getAsString()+ RESET);
                    }
                    if (state.has("direction")) {
                        System.out.println(BLUE_BRIGHT +"  Direction  :  "+ RESET + YELLOW_BRIGHT + state.get("direction").getAsString()+ RESET);
                    }
                    if (state.has("shields")) {
                        if (state.get("shields").getAsInt() != 0) {
                            System.out.println(BLUE_BRIGHT +"  Shields    :  "+ RESET + YELLOW_BRIGHT + state.get("shields").getAsInt()+ RESET);
                        }
                    }
                    if (state.has("shots")) {
                        System.out.println(BLUE_BRIGHT +"  Shots      :  "+ RESET + YELLOW_BRIGHT + state.get("shots").getAsInt()+ RESET);
                    }
                    if (state.has("status")) {
                        System.out.println(BLUE_BRIGHT +"  Status     :  "+ RESET + YELLOW_BRIGHT + state.get("status").getAsString()+ RESET);
                    }
                }
            } else {
                System.out.println(serverResponse);
            }
        } catch (Exception e) {
            System.out.println("Failed to parse JSON: " + e.getMessage());
        }
    }
}
