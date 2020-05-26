package tech.kocel.kotlin.archunit.internal

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.Test
import tech.kocel.kotlin.archunit.internal.KotlinInternalDependencyRules.accessClassesThatResideInALowerPackage
import tech.kocel.kotlin.archunit.internal.KotlinInternalDependencyRules.accessClassesThatResideInAnUpperPackage

class InternalTest {

    @Test
    fun `limit internal scope`() {
        val nonInternalKotlinClasses = ClassFileImporter()
            .importPackages("com.acme")
            .that(isKotlinNotInternal())

        noClasses().should(accessClassesThatResideInALowerPackage()).check(nonInternalKotlinClasses)

        val internalKotlinClasses = ClassFileImporter()
            .importPackages("com.acme")
            .that(isKotlinInternal())

        noClasses().should(accessClassesThatResideInAnUpperPackage()).check(internalKotlinClasses)
    }
}
