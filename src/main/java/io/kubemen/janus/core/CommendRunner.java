package io.kubemen.janus.core;

import io.kubemen.janus.domain.CommendConfig;
import io.kubemen.janus.exceptions.ImageNameMissingException;
import io.kubemen.janus.exceptions.PlatformFailedException;
import io.kubemen.janus.exceptions.PlatformFailedPullImageException;

public interface CommendRunner {

    String run(CommendConfig config) throws ImageNameMissingException, PlatformFailedException, PlatformFailedPullImageException;

    boolean stop(String id);
}
