package io.kubemen.janus.core;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.command.PullImageResultCallback;
import io.kubemen.janus.domain.PlatformCommend;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DockerCommendRunner implements CommendRunner {

    private static final int DEFAULT_TIMEOUT = 60;

    private final DockerClient dockerClient;

    public DockerCommendRunner(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public boolean execute(PlatformCommend commend, String image, String tag) {
        switch (commend) {
            case PULL_IMAGE:
                try {
                    dockerClient.pullImageCmd(image)
                                .withTag(tag)
                                .exec(new PullImageResultCallback())
                                .awaitCompletion(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            case RUN_IMAGE:
                try {
                    String id = dockerClient.createContainerCmd(image + ":" + tag)
                                            .withName(UUID.randomUUID().toString())
                                            .withPortBindings(PortBinding.parse("27017:27017"))
                                            .exec()
                                            .getId();
                    System.out.println(id);
                    return true;
                } catch (NotFoundException | ConflictException e){
                    e.printStackTrace();
                    return false;
                }
             default:
                 return false;
        }
    }
}
