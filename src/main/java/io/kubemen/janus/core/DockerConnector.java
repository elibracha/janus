package io.kubemen.janus.core;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;

public class DockerConnector implements Connector{

    public DockerConnector configure(){
        return this;
    }

    public DockerClient connect(){
        DefaultDockerClientConfig.Builder config
                = DefaultDockerClientConfig.createDefaultConfigBuilder();

        return DockerClientBuilder
                .getInstance(config)
                .build();
    }
}
