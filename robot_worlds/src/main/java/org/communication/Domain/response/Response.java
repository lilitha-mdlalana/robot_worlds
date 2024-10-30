package org.communication.Domain.response;

import java.util.Map;

import org.communication.Infrastructure.robot.State;

public class Response {
    private String result;
    private Map<String, Object> data;
    private State state;

    /**
     * Returns the result of the response.
     *
     * @return The result of the response.
     */
    public String getResult() {
        return result;
    }

    /**
            * Sets the result of the response.
            *
            * @param result The result to set.
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Returns the data of the response.
     *
     * @return The data of the response.
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * Sets the data of the response.
     *
     * @param data The data to set.
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * Returns the state of the response.
     *
     * @return The state of the response.
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the state of the response.
     *
     * @param state The state to set.
     */
    public void setState(State state) {
        this.state = state;
    }


}
