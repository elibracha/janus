package io.kubemen.janus.extensions;

import io.kubemen.janus.annotations.Kubemen;
import io.kubemen.janus.annotations.Provide;
import io.kubemen.janus.core.DockerConnector;
import io.kubemen.janus.core.DockerPlatformManager;
import io.kubemen.janus.core.PlatformManager;
import io.kubemen.janus.domain.CommendConfig;
import io.kubemen.janus.domain.KubemenScope;
import io.kubemen.janus.exceptions.ImageNameMissingException;
import io.kubemen.janus.exceptions.PlatformFailedException;
import io.kubemen.janus.exceptions.PlatformFailedPullImageException;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;
import java.util.List;

public class KubemenExtension implements AfterAllCallback, TestInstancePostProcessor,
        BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static PlatformManager platformManager;

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        if (KubemenExtension.platformManager == null)
            KubemenExtension.platformManager = new DockerPlatformManager(new DockerConnector().connect());

        processKubemen(extensionContext, KubemenScope.METHOD);

    }

    @Override
    public void postProcessTestInstance(Object o, ExtensionContext extensionContext) {
        KubemenExtension.platformManager = new DockerPlatformManager(new DockerConnector().connect());
        processKubemen(extensionContext, KubemenScope.CLASS);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        killKubemen(KubemenScope.METHOD);
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        killKubemen(KubemenScope.CLASS);
    }

    private void killKubemen(KubemenScope method){
        platformManager.stop(method);
    }

    private void processKubemen(ExtensionContext extensionContext, KubemenScope method){
        extensionContext.getElement().ifPresent(e -> {
            boolean isProviderPresent = e.isAnnotationPresent(Provide.class);
            boolean isProvidersPresent = e.isAnnotationPresent(Kubemen.class);
            if (isProvidersPresent) {
                Provide[] providers = e.getAnnotation(Kubemen.class).provides();
                Arrays.asList(providers).forEach(p -> processProvide(
                        p.image(),
                        p.tag(),
                        Arrays.asList(p.portForwarding()),
                        Arrays.asList(p.env()),
                        method
                ));
            } if (isProviderPresent) {
                processProvide(
                        e.getAnnotation(Provide.class).image(),
                        e.getAnnotation(Provide.class).tag(),
                        Arrays.asList(e.getAnnotation(Provide.class).portForwarding()),
                        Arrays.asList(e.getAnnotation(Provide.class).env()),
                        method
                );
            }
        });
    }

    private void processProvide(String image, String tag,
                                List<String> portForwarding, List<String> envs, KubemenScope method) {

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
            platformManager.run(commendConfig, method);
        } catch (ImageNameMissingException | PlatformFailedException |
                PlatformFailedPullImageException ex) {
            ex.printStackTrace();
        }
    }
}
