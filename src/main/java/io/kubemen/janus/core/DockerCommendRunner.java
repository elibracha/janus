package io.kubemen.janus.core;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.command.PullImageResultCallback;
import io.kubemen.janus.domain.CommendConfig;
import io.kubemen.janus.exceptions.ImageNameMissingException;
import io.kubemen.janus.exceptions.PlatformFailedException;
import io.kubemen.janus.exceptions.PlatformFailedPullImageException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DockerCommendRunner implements CommendRunner {

    private static final int DEFAULT_TIMEOUT = 60;
    private static final String DEFAULT_TAG = "latest";

    private final DockerClient dockerClient;

    public DockerCommendRunner(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public String run(CommendConfig config) throws
            ImageNameMissingException, PlatformFailedException, PlatformFailedPullImageException {
        String image = Optional.ofNullable(config.getImage()).orElseThrow(ImageNameMissingException::new);

        boolean pulled = pull(image, config);

        if (!pulled)
            throw new PlatformFailedPullImageException();

        String created = run(image, config);
        return Optional.ofNullable(created).orElseThrow(PlatformFailedException::new);
    }

    public boolean stop(String id) {
        dockerClient.killContainerCmd(id).exec();
        return true;
    }

    private boolean pull(String image, CommendConfig config) {
        try {
            PullImageCmd cmd = dockerClient.pullImageCmd(image);
            String tag = Optional.ofNullable(config.getTag()).orElse(DEFAULT_TAG);

            cmd.withTag(tag);
            cmd.exec(new PullImageResultCallback()).awaitCompletion(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String run(String image, CommendConfig config) {
        StringBuilder fullImageName = new StringBuilder();
        String tag = Optional.ofNullable(config.getTag()).orElse(DEFAULT_TAG);

        fullImageName.append(image).append(":").append(tag);

        try {
            CreateContainerCmd cmd = dockerClient.createContainerCmd(fullImageName.toString());

            Optional.ofNullable(config.getPortForwarding())
                    .ifPresent(ports -> {
                        List<PortBinding> portsBindings = ports.stream()
                                .map(PortBinding::parse)
                                .collect(Collectors.toList());
                        cmd.withPortBindings(portsBindings);
                    });

            Optional.ofNullable(config.getEnv()).ifPresent(cmd::withEnv);

            String id = cmd.exec().getId();
            dockerClient.startContainerCmd(id).exec();
            return id;
        } catch (NotFoundException | ConflictException e) {
            e.printStackTrace();
            return null;
        }
    }
}
