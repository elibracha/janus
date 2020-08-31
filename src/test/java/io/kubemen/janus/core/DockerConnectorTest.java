package io.kubemen.janus.core;

import com.github.dockerjava.api.DockerClient;
import io.kubemen.janus.domain.PlatformCommend;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DockerConnectorTest {

    @Test
    public void DockerConnectionTest(){
        DockerConnector connector = new DockerConnector();
        DockerClient client = connector.connect();

        Assertions.assertNotNull(client);
    }

    @Test
    public void DockerCreateImageTest() throws InterruptedException {
        DockerConnector connector = new DockerConnector();
        DockerClient client = connector.connect();

        DockerCommendRunner runner = new DockerCommendRunner(client);
        boolean isPullOk = runner.execute(PlatformCommend.PULL_IMAGE, "mongo", "4.2.9");
        boolean isRunOk = runner.execute(PlatformCommend.RUN_IMAGE, "mongo", "4.2.9");

        Assertions.assertTrue(isPullOk);
        Assertions.assertTrue(isRunOk);
    }
}
