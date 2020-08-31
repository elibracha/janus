package io.kubemen.janus.core;

import io.kubemen.janus.domain.CommendConfig;
import io.kubemen.janus.exceptions.ImageNameMissingException;

public interface PlatformManager {

    boolean run(CommendConfig config) throws ImageNameMissingException;
}
