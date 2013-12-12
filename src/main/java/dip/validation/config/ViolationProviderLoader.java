package dip.validation.config;

import java.util.Collection;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AssignableTypeFilter;

import dip.validation.violation.BusinessViolationProvider;

public class ViolationProviderLoader {
    private final Class<? extends BusinessViolationProvider> violationProviderType;
    private final String violationBasePackage;
    private ListableBeanFactory context;

    public ViolationProviderLoader(Class<? extends BusinessViolationProvider> violationProviderType,
            String violationBasePackage) {
        this.violationProviderType = violationProviderType;
        this.violationBasePackage = violationBasePackage;
    }

    private void loadContext() {
        if (context == null) {
            // Create the annotation-based context
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

            // Scan for classes annotated with @Violation, do not include standard Spring annotations in scan
            ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context, false);
            scanner.addIncludeFilter(new AssignableTypeFilter(violationProviderType));

            scanner.scan(violationBasePackage);
            context.refresh();

            this.context = context;
        }
    }

    public <T extends BusinessViolationProvider> Collection<T> getViolations(Class<T> extensionPointType) {
        loadContext();
        return getViolationDescriptors(context, extensionPointType);
    }

    private static <T> Collection<T> getViolationDescriptors(ListableBeanFactory context,
            Class<T> violationDescriptorType) {
        return context.getBeansOfType(violationDescriptorType).values();
    }
}
