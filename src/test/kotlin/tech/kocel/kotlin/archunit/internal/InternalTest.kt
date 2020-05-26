package tech.kocel.kotlin.archunit.internal

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.Test
import tech.kocel.kotlin.archunit.internal.KotlinInternalDependencyRules.accessClassesThatResideInASubpackage
import tech.kocel.kotlin.archunit.internal.KotlinInternalDependencyRules.accessClassesThatResideInAnOuterPackage

class InternalTest {

    @Test
    fun `limit internal scope`() {
        val nonInternalKotlinClasses = ClassFileImporter()
            .importPackages("com.acme")
            .that(isKotlinNotInternal())

        noClasses().should(accessClassesThatResideInASubpackage()).check(nonInternalKotlinClasses)

        val internalKotlinClasses = ClassFileImporter()
            .importPackages("com.acme")
            .that(isKotlinInternal())

        noClasses().should(accessClassesThatResideInAnOuterPackage()).check(internalKotlinClasses)
    }
}
