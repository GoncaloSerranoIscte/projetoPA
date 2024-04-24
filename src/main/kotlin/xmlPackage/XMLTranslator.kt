package xmlPackage

import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation


class XMLTranslator(
    private val objectToTranslate: Any
) {
    val entity:XMLEntity
        get() = toXMLEntity()

    private fun toXMLEntity():XMLEntity{
        val entity = XMLEntity(xmlEntityName = getName())
        entity.addAllXMLAttribute(xmlAttributesToAdd = getXMLAttributes())
        return entity
    }

    private fun getName():String{
        val clazz: KClass<*> = objectToTranslate::class
        return "${clazz.findAnnotation<OverrideName>()?.name ?: clazz.simpleName}"
    }

    //todo validar que o construtor recebe um String, dar "rollback" caso esta merda dÊ nulll
    private fun getXMLAttributes():List<XMLAttribute>{
        val xmlAttributesToAdd:MutableList<XMLAttribute> = mutableListOf()
        val clazz: KClass<*> = objectToTranslate::class
        clazz.declaredMemberProperties.filter { it.hasAnnotation<IsAttribute>() && !it.hasAnnotation<Ignore>()}.forEach{
            val nameAt:String = it.findAnnotation<OverrideName>()?.name ?: it.name
            val valueAt:String = try {
                    it.findAnnotation<XmlString>()!!.stringRefactor.constructors.filter { it2 -> it2.parameters.size == 1 }
                        .random().call(it.call(objectToTranslate).toString()).toString()
                }catch (e: Exception){
                    it.call(objectToTranslate).toString()
            }

            xmlAttributesToAdd.add(XMLAttribute(name = nameAt, value = valueAt))
        }
        return xmlAttributesToAdd
    }
/*
    private fun getXMLEntities():List<XMLEntity>{
        val xmlAttributesToAdd:MutableList<XMLEntity> = mutableListOf()
        val clazz: KClass<*> = objectToTranslate::class
        clazz.declaredMemberProperties.filter { it.hasAnnotation<IsAttribute>() && !it.hasAnnotation<Ignore>()}.forEach{
            
            //xmlAttributesToAdd.add(XMLAttribute(name = it.findAnnotation<OverrideName>()?.name ?: it.name, value = it.call(objectToTranslate).toString()))
        }
        return xmlAttributesToAdd
    }
 */
}