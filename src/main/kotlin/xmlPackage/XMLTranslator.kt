package xmlPackage

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation


class XMLTranslator(
    val objectToTranslate: Any
) {
    val entity:XMLEntity
        get() = toXMLEntity()

    private fun toXMLEntity():XMLEntity{
        val entity = XMLEntity(xmlEntityName = getName())
        entity.addAllXMLAttribute(xmlAttributesToAdd = getXMLAttributes())
        println()
        return entity
    }

    private fun getName():String{
        val clazz: KClass<*> = objectToTranslate::class
        return "${clazz.findAnnotation<OverrideName>()?.name ?: clazz.simpleName}"
    }

    private fun getXMLAttributes():List<XMLAttribute>{
        val xmlAttributesToAdd:MutableList<XMLAttribute> = mutableListOf<XMLAttribute>()
        val clazz: KClass<*> = objectToTranslate::class
        clazz.declaredMemberProperties.filter { it.hasAnnotation<IsAttribute>() && !it.hasAnnotation<Ignore>()}.forEach{
            xmlAttributesToAdd.add(XMLAttribute(name = it.findAnnotation<OverrideName>()?.name ?: it.name, value = it.call(objectToTranslate).toString()))
        }
        return xmlAttributesToAdd
    }
}