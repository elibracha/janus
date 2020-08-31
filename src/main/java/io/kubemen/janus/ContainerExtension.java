package io.kubemen.janus;


import org.junit.jupiter.api.extension.*;

public class ContainerExtension implements
        BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        System.out.println("asdasda");
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        System.out.println("asdasda");
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        System.out.println("asdasda");
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        System.out.println("asdasda");
    }
}
