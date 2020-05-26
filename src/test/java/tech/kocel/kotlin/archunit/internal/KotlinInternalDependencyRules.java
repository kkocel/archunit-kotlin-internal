package tech.kocel.kotlin.archunit.internal;

import com.tngtech.archunit.PublicAPI;
import com.tngtech.archunit.core.domain.JavaAccess;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import static tech.kocel.kotlin.archunit.internal.InternalPredicates.isInternal;
import static com.tngtech.archunit.PublicAPI.Usage.ACCESS;

public class KotlinInternalDependencyRules {

    @PublicAPI(usage = ACCESS)
    public static ArchCondition<JavaClass> accessClassesThatResideInAnOuterPackage() {
        return new AccessClassesThatResideInAnOuterPackageCondition();
    }

    @PublicAPI(usage = ACCESS)
    public static ArchCondition<JavaClass> accessClassesThatResideInASubpackage() {
        return new AccessClassesThatResideInASubpackageCondition();
    }

    private static class AccessClassesThatResideInAnOuterPackageCondition extends ArchCondition<JavaClass> {
        AccessClassesThatResideInAnOuterPackageCondition() {
            super("access classes that reside in an upper package");
        }

        @Override
        public void check(final JavaClass clazz, final ConditionEvents events) {
            for (JavaAccess<?> access : clazz.getAccessesFromSelf()) {
                boolean callToSuperPackage = isCallToSuperPackage(access.getOriginOwner(), access.getTargetOwner());
                events.add(new SimpleConditionEvent(access, callToSuperPackage, access.getDescription()));
            }
        }

        private boolean isCallToSuperPackage(final JavaClass origin, final JavaClass target) {
            final String originPackageName = getOutermostEnclosingClass(origin).getPackageName();
            final String targetSubPackagePrefix = getOutermostEnclosingClass(target).getPackageName() + ".";
            return originPackageName.startsWith(targetSubPackagePrefix);
        }
    }

    private static class AccessClassesThatResideInASubpackageCondition extends ArchCondition<JavaClass> {
        AccessClassesThatResideInASubpackageCondition() {
            super("access classes that reside in a lower package");
        }

        @Override
        public void check(final JavaClass clazz, final ConditionEvents events) {
            for (JavaAccess<?> access : clazz.getAccessesFromSelf()) {
                final boolean callToLowerPackage = isCallToLowerPackage(access.getOriginOwner(), access.getTargetOwner());
                events.add(new SimpleConditionEvent(access, callToLowerPackage, access.getDescription()));
            }
        }

        private boolean isCallToLowerPackage(final JavaClass origin, final JavaClass target) {

            if (!isInternal(target.reflect())) {
                return false;
            }

            final String originPackageName = getOutermostEnclosingClass(origin).getPackageName();
            final String targetSubPackagePrefix = getOutermostEnclosingClass(target).getPackageName();
            return targetSubPackagePrefix.startsWith(originPackageName) && !targetSubPackagePrefix.equals(originPackageName);
        }

    }

    private static JavaClass getOutermostEnclosingClass(JavaClass javaClass) {
        while (javaClass.getEnclosingClass().isPresent()) {
            javaClass = javaClass.getEnclosingClass().get();
        }
        return javaClass;
    }
}