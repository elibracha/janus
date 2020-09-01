package io.kubemen.janus.core;

import com.github.dockerjava.api.DockerClient;
import io.kubemen.janus.domain.CommendConfig;
import io.kubemen.janus.domain.KubemenScope;
import io.kubemen.janus.exceptions.ImageNameMissingException;
import io.kubemen.janus.exceptions.PlatformFailedException;
import io.kubemen.janus.exceptions.PlatformFailedPullImageException;

import java.util.ArrayList;
import java.util.List;

public class DockerPlatformManager implements PlatformManager {

    private final CommendRunner commendRunner;
    private final List<String> instanceLevelRunningContainers;
    private final List<String> methodLevelRunningContainers;

    public DockerPlatformManager(DockerClient dockerClient) {
        this.commendRunner = new DockerCommendRunner(dockerClient);
        this.instanceLevelRunningContainers = new ArrayList<>();
        this.methodLevelRunningContainers = new ArrayList<>();
    }

    public void run(CommendConfig config, KubemenScope method)
            throws ImageNameMissingException, PlatformFailedException, PlatformFailedPullImageException {
        String id = commendRunner.run(config);
        switch (method){
            case CLASS:
                instanceLevelRunningContainers.add(id);
                break;
            case METHOD:
                methodLevelRunningContainers.add(id);
        }
    }

    public void stop(KubemenScope method) {
        switch (method){
            case CLASS:
                instanceLevelRunningContainers.forEach(commendRunner::stop);
                break;
            case METHOD:
                methodLevelRunningContainers.forEach(commendRunner::stop);
        }
    }
}
