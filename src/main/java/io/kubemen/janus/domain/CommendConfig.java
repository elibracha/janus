package io.kubemen.janus.domain;

import java.util.List;

public class CommendConfig {
    private List<String> portForwarding;
    private String tag;
    private String image;
    private List<String> env;

    public CommendConfig() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getPortForwarding() {
        return portForwarding;
    }

    public void setPortForwarding(List<String> portForwarding) {
        this.portForwarding = portForwarding;
    }

    public List<String> getEnv() {
        return env;
    }

    public void setEnv(List<String> env) {
        this.env = env;
    }
}
