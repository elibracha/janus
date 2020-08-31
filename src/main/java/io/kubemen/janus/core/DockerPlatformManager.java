package io.kubemen.janus.core;

import com.github.dockerjava.api.DockerClient;
import io.kubemen.janus.domain.CommendConfig;
import io.kubemen.janus.domain.PlatformCommend;
import io.kubemen.janus.exceptions.ImageNameMissingException;

import java.util.ArrayList;
import java.util.List;

public class DockerPlatformManager implements PlatformManager {

    private CommendRunner commendRunner;
    private List<String> runningContainers;
    private List<String> pulledImages;

    public DockerPlatformManager() {
    }

    public DockerPlatformManager(DockerClient dockerClient) {
        this.commendRunner = new DockerCommendRunner(dockerClient);
        this.runningContainers = new ArrayList<>();
        this.pulledImages = new ArrayList<>();
    }

    public boolean run(CommendConfig config) throws ImageNameMissingException {
        boolean pulled = commendRunner.execute(PlatformCommend.PULL_IMAGE, config);
        boolean ran = commendRunner.execute(PlatformCommend.RUN_IMAGE, config);
        return pulled && ran;
    }

}
