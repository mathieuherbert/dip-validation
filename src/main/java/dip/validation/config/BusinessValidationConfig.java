package dip.validation.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import dip.validation.BusinessViolation;
import dip.validation.violation.BusinessViolationExceptionBuilder;
import dip.validation.violation.BusinessViolationProvider;
import dip.validation.violation.BusinessViolationRepository;

@Configuration
public class BusinessValidationConfig {
    public static final String BASE_PACKAGE = "dip.*.integration.regle";

    @Autowired
    Environment env;

    @Bean
    public BusinessViolationRepository violationRepository() {
        String basePackage = env.getProperty("violation.base.dir");
        if ((basePackage == null) || basePackage.isEmpty()) {
            basePackage = BASE_PACKAGE;
        }

        ViolationProviderLoader violationLoader = new ViolationProviderLoader(BusinessViolationProvider.class,
                basePackage);
        Collection<BusinessViolationProvider> providers = violationLoader
                .getViolations(BusinessViolationProvider.class);
        return new BusinessViolationRepositoryImpl(providers);
    }

    @Bean
    public BusinessViolationExceptionBuilder violationExceptionBuilder() {
        return new BusinessViolationExceptionBuilder(violationRepository());
    }

    public static class BusinessViolationRepositoryImpl implements BusinessViolationRepository {
        private Logger logger = LoggerFactory.getLogger(getClass());

        private Map<String, BusinessViolation> violations = new HashMap<>();

        public BusinessViolationRepositoryImpl(Collection<BusinessViolationProvider> providers) {
            for (BusinessViolationProvider provider : providers) {
                for (BusinessViolation each : provider.getViolations()) {
                    BusinessViolation v = violations.put(each.getCode(), each);
                    if (v != null) {
                        logger.warn("A BusinessViolation with same code already exists: " + v.getCode());
                    }
                }
            }
        }

        @Override
        public BusinessViolation getViolation(String code) {
            return violations.get(code);
        }
    }
}
