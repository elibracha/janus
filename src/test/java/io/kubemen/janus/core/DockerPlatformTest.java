package io.kubemen.janus.core;

import io.kubemen.janus.annotations.Kubemen;
import io.kubemen.janus.annotations.Provide;
import io.kubemen.janus.extensions.ContainerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ContainerExtension.class)
public class DockerPlatformTest {

    @Test
    @Kubemen(providers = {
            @Provide(image = "mongo", tag = "4.2.9", portForwarding = {"27017:27017"}),
            @Provide(image = "mysql", portForwarding = {"3306:3306"}, env = {"MYSQL_ROOT_PASSWORD=12345"})})
    public void DockerCreateImageTest() {
        System.out.println("this is a test!!!");
    }
}
