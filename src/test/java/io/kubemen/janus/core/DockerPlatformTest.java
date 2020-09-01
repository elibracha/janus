package io.kubemen.janus.core;

import io.kubemen.janus.annotations.Kubemen;
import io.kubemen.janus.annotations.Provide;
import io.kubemen.janus.extensions.KubemenExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(KubemenExtension.class)
public class DockerPlatformTest {

    @Test
    @Kubemen(provides = {
            @Provide(image = "mongo", tag = "4.2.9", portForwarding = {"27017:27017"}),
            @Provide(image = "mysql", portForwarding = {"3306:3306"}, env = {"MYSQL_ROOT_PASSWORD=12345"})})
    public void DockerCreateImageTest() {
        System.out.println("this is a test!!!");
    }
}
