package com.example.support.configuration;

import com.example.support.component.UserAccountDetails;
import java.lang.reflect.Method;
import javax.cache.management.CacheStatisticsMXBean;
import org.springframework.aot.hint.ExecutableMode;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.util.ReflectionUtils;

@Configuration
@ImportRuntimeHints(SupportRuntimeHints.class)
@RegisterReflectionForBinding(UserAccountDetails.class)
public class SupportRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#native-image.advanced.custom-hints
        // Register method for reflection
//        Method method = ReflectionUtils.findMethod(CacheStatisticsMXBean.class, "getCacheMisses");
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(CacheStatisticsMXBean.class);
        for (Method m : methods) {
            hints.reflection().registerMethod(m, ExecutableMode.INVOKE);
        }
//
        // Register resources
        hints.resources().registerPattern("*.yml");

        // Register serialization
        hints.serialization().registerType(org.springframework.security.core.userdetails.User.class);
        hints.serialization().registerType(org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal.class);
        hints.serialization().registerType(net.minidev.json.JSONObject.class);
        hints.serialization().registerType(java.util.HashMap.class);
        hints.serialization().registerType(java.time.Instant.class);
        hints.serialization().registerType(java.util.TreeSet.class);
        hints.serialization().registerType(org.springframework.security.core.authority.SimpleGrantedAuthority.class);

        // Register proxy
//        hints.proxies().registerJdkProxy(MyInterface.class);

    }
}
