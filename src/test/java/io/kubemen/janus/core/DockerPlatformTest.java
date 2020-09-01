package io.kubemen.janus.core;

import io.kubemen.janus.annotations.Provide;
import io.kubemen.janus.extensions.ContainerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ContainerExtension.class)
public class DockerPlatformTest {

    @Test
    @Provide(image = "mongo", tag = "4.2.9")
    public void DockerCreateImageTest() {
        System.out.println("this is a test!!!");
    }
}
