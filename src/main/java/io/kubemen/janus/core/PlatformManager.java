package io.kubemen.janus.core;

import io.kubemen.janus.domain.CommendConfig;
import io.kubemen.janus.domain.KubemenScope;
import io.kubemen.janus.exceptions.ImageNameMissingException;
import io.kubemen.janus.exceptions.PlatformFailedException;
import io.kubemen.janus.exceptions.PlatformFailedPullImageException;

public interface PlatformManager {

    void run(CommendConfig config, KubemenScope method) throws ImageNameMissingException, PlatformFailedException, PlatformFailedPullImageException;

    void stop(KubemenScope method);
}
