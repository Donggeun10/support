package com.example.support.configuration;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(SupportRuntimeHints.class)
public class SupportRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#native-image.advanced.custom-hints
        // Register method for reflection
//        Method method = ReflectionUtils.findMethod(MyClass.class, "sayHello", String.class);
//        hints.reflection().registerMethod(method, ExecutableMode.INVOKE);
//
//        // Register resources
        hints.resources().registerPattern("*.yml");
//
        // Register serialization
//        hints.serialization().registerType(org.springframework.cache.interceptor.SimpleKey.class);

//
//        // Register proxy
//        hints.proxies().registerJdkProxy(MyInterface.class);

    }
}
