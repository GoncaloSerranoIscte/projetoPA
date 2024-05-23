package xmlPackage

import kotlin.reflect.KClass

/**
 *  Change the name of the Property or Class to the given name
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class OverrideName(val name:String)

/**
 * Ignores this Property when translating to XML
 */
@Target(AnnotationTarget.PROPERTY)
annotation class Ignore

/**
 * Defines this Property as XMLAttribute
 */
@Target(AnnotationTarget.PROPERTY)
annotation class IsAttribute

/**
 * Defines this Property as XMLEntity
 */
@Target(AnnotationTarget.PROPERTY)
annotation class IsEntity

@Target(AnnotationTarget.PROPERTY)
annotation class IsEntityToTranslate

@Target(AnnotationTarget.PROPERTY)
annotation class IsEntityWithChildren

/**
 *
 */
//todo
@Target(AnnotationTarget.PROPERTY)
annotation class XmlString(val stringRefactor:KClass<out StringAdapterInterface>)

@Target(AnnotationTarget.CLASS)
annotation class XmlAdapter(val xmlRefactor:KClass<out XMLAdapterInterface>)