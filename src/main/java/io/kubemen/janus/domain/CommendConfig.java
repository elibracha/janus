package io.kubemen.janus.domain;

import java.util.List;

public class CommendConfig {
    private List<String> portForwarding;
    private String tag;
    private String image;

    public CommendConfig() {
    }

    public CommendConfig(List<String> portForwarding, String tag, String image) {
        this.portForwarding = portForwarding;
        this.tag = tag;
        this.image = image;
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

    public List<String>  getPortForwarding() {
        return portForwarding;
    }

    public void setPortForwarding(List<String>  portForwarding) {
        this.portForwarding = portForwarding;
    }
}
