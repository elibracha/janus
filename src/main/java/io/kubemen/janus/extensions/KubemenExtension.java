package io.kubemen.janus.extensions;

import io.kubemen.janus.annotations.Kubemen;
import io.kubemen.janus.annotations.Provide;
import io.kubemen.janus.core.DockerConnector;
import io.kubemen.janus.core.DockerPlatformManager;
import io.kubemen.janus.core.PlatformManager;
import io.kubemen.janus.domain.CommendConfig;
import io.kubemen.janus.exceptions.ImageNameMissingException;
import io.kubemen.janus.exceptions.PlatformFailedException;
import io.kubemen.janus.exceptions.PlatformFailedPullImageException;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Arrays;
import java.util.List;

public class KubemenExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private PlatformManager platformManager;

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        platformManager.stop();
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        this.platformManager = new DockerPlatformManager(new DockerConnector().connect());

        extensionContext.getElement().ifPresent(e -> {
            boolean isProviderPresent = e.isAnnotationPresent(Provide.class);
            boolean isProvidersPresent = e.isAnnotationPresent(Kubemen.class);
            if (isProvidersPresent) {
                Provide[] providers = e.getAnnotation(Kubemen.class).provides();
                Arrays.asList(providers).forEach(p -> processProvide(
                        p.image(),
                        p.tag(),
                        Arrays.asList(p.portForwarding()),
                        Arrays.asList(p.env())
                ));
            } else if (isProviderPresent) {
                processProvide(
                        e.getAnnotation(Provide.class).image(),
                        e.getAnnotation(Provide.class).tag(),
                        Arrays.asList(e.getAnnotation(Provide.class).portForwarding()),
                        Arrays.asList(e.getAnnotation(Provide.class).env())
                );
            }
        });
    }

    private void processProvide(String image,
                                String tag,
                                List<String> portForwarding,
                                List<String> envs) {

        CommendConfig commendConfig = new CommendConfig();
        if (!image.isBlank())
            commendConfig.setImage(image);
        if (!tag.isBlank())
            commendConfig.setTag(tag);
        if (!portForwarding.isEmpty())
            commendConfig.setPortForwarding(portForwarding);
        if (!envs.isEmpty())
            commendConfig.setEnv(envs);

        try {
            platformManager.run(commendConfig);
        } catch (ImageNameMissingException | PlatformFailedException |
                PlatformFailedPullImageException ex) {
            ex.printStackTrace();
        }
    }
}
