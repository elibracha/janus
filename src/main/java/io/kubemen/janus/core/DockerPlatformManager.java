package io.kubemen.janus.core;

import com.github.dockerjava.api.DockerClient;
import io.kubemen.janus.domain.CommendConfig;
import io.kubemen.janus.exceptions.ImageNameMissingException;
import io.kubemen.janus.exceptions.PlatformFailedException;
import io.kubemen.janus.exceptions.PlatformFailedPullImageException;

import java.util.ArrayList;
import java.util.List;

public class DockerPlatformManager implements PlatformManager {

    private final CommendRunner commendRunner;
    private final List<String> runningContainers;

    public DockerPlatformManager(DockerClient dockerClient) {
        this.commendRunner = new DockerCommendRunner(dockerClient);
        this.runningContainers = new ArrayList<>();
    }

    public void run(CommendConfig config)
            throws ImageNameMissingException, PlatformFailedException, PlatformFailedPullImageException {
        String id = commendRunner.run(config);
        runningContainers.add(id);
    }

    public void stop() {
        runningContainers.forEach(commendRunner::stop);
    }
}
