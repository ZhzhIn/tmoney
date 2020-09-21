package com.tengmoney.autoframework;

import org.junit.jupiter.api.Test;

class BasePageFactoryTest {

    @Test
    void create() {
        PageObjectModel web= BasePageFactory.create("web")
                .loadPage("src/test/resources/test_framework/contact_page.yaml");
    }
}