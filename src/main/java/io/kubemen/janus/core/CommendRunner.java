package io.kubemen.janus.core;

import io.kubemen.janus.domain.CommendConfig;
import io.kubemen.janus.domain.PlatformCommend;
import io.kubemen.janus.exceptions.ImageNameMissingException;

public interface CommendRunner {

    boolean execute(PlatformCommend commend, CommendConfig config) throws ImageNameMissingException;
}
