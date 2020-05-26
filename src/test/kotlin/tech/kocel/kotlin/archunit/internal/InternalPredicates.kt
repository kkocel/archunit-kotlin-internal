@file:JvmName("InternalPredicates")

package tech.kocel.kotlin.archunit.internal

import com.tngtech.archunit.base.DescribedPredicate
import com.tngtech.archunit.core.domain.JavaClass
import kotlin.reflect.KVisibility

fun isKotlinInternal() = object : DescribedPredicate<JavaClass>("Kotlin internal class") {
    override fun apply(input: JavaClass) = input.reflect().isKotlinInternal()

    private fun Class<*>.isKotlinInternal() = isKotlinClass() && isInternal()
}

fun isKotlinNotInternal() = object : DescribedPredicate<JavaClass>("Kotlin not-internal class") {
    override fun apply(input: JavaClass) = input.reflect().isKotlinNotInternal()

    private fun Class<*>.isKotlinNotInternal() = isKotlinClass() && !isInternal()
}

fun Class<*>.isInternal() = this.kotlin.visibility == KVisibility.INTERNAL

private fun Class<*>.isKotlinClass() = this.declaredAnnotations.any {
    it.annotationClass.qualifiedName == "kotlin.Metadata"
}
