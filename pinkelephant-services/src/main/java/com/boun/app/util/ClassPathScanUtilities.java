package com.boun.app.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClassPathScanUtilities
 {

    private ClassPathScanUtilities() {}

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> Class<T>[] getAnnotatedClasses(Class<T> clazz, String basePackage) {
        List<Class<T>> classes = new ArrayList<>();

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter( new AnnotationTypeFilter(clazz) );
        Set<BeanDefinition> beans = scanner.findCandidateComponents(basePackage);
        Class<T> messageClass;
        try {
            for (BeanDefinition bd : beans) {
                messageClass = (Class<T>) Class.forName(bd.getBeanClassName());
                classes.add(messageClass);
            }
        } catch (ClassNotFoundException e) {
            // Unexpected, Do Nothing
        }

        return classes.toArray(new Class[classes.size()]);
    }
}
