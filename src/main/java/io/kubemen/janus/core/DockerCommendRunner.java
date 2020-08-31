package io.kubemen.janus.core;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.command.PullImageResultCallback;
import io.kubemen.janus.domain.CommendConfig;
import io.kubemen.janus.domain.PlatformCommend;
import io.kubemen.janus.exceptions.ImageNameMissingException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class DockerCommendRunner implements CommendRunner {

    private static final int DEFAULT_TIMEOUT = 60;

    private final DockerClient dockerClient;

    public DockerCommendRunner(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public boolean execute(PlatformCommend commend, CommendConfig config) throws ImageNameMissingException {
        String image = Optional.ofNullable(config.getImage()).orElseThrow(ImageNameMissingException::new);

        switch (commend) {
            case PULL_IMAGE:
                return pull(image, config);
            case RUN_IMAGE:
                return run(image, config);
             default:
                 return false;
        }
    }

    private boolean pull(String image, CommendConfig config){
        try {
            PullImageCmd cmd = dockerClient.pullImageCmd(image);
            String tag = Optional.ofNullable(config.getTag()).orElse("latest");

            cmd.withTag(tag);
            cmd.exec(new PullImageResultCallback()).awaitCompletion(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean run(String image, CommendConfig config){
        StringBuilder fullImageName = new StringBuilder();
        String tag = Optional.ofNullable(config.getTag()).orElse("latest");

        fullImageName.append(image).append(":").append(tag);

        try {
            CreateContainerCmd cmd = dockerClient.createContainerCmd(fullImageName.toString());

            Optional.of(config.getPortForwarding())
                    .ifPresent(ports ->
                            ports.forEach(port -> cmd.withPortBindings(PortBinding.parse(port)))
                    );

            cmd.exec();
            return true;
        } catch (NotFoundException | ConflictException e){
            e.printStackTrace();
            return false;
        }
    }
}
