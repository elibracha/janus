package io.kubemen.janus.core;

import com.github.dockerjava.api.DockerClient;
import io.kubemen.janus.domain.CommendConfig;
import io.kubemen.janus.exceptions.ImageNameMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DockerPlatformTest {

    @Test
    public void DockerConnectionTest(){
        DockerConnector connector = new DockerConnector();
        DockerClient client = connector.connect();

        Assertions.assertNotNull(client);
    }

    @Test
    public void DockerCreateImageTest() throws ImageNameMissingException {
        DockerConnector connector = new DockerConnector();
        DockerClient client = connector.connect();

        PlatformManager manager = new DockerPlatformManager(client);

        CommendConfig config = new CommendConfig();
        config.setPortForwarding(List.of("27017:27017"));
        config.setImage("mongo");
        config.setTag("4.2.9");

        boolean isOk = manager.run(config);
        Assertions.assertTrue(isOk);
    }
}
