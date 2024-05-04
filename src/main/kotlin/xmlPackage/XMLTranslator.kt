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

    private val defaultEntitiesTypes: MutableList<Any>
        get() = mutableListOf(1::class,2.0::class,false::class, ""::class)

    private val clazz:KClass<*>
        get() = objectToTranslate::class

    private fun toXMLEntity():XMLEntity{
        var entity = XMLEntity(xmlEntityName = getName())
        entity.addAll(xmlElementsToAdd = getXMLAttributes())
        entity.addAll(xmlElementsToAdd = getXMLEntities())
        if (clazz.hasAnnotation<XmlAdapter>()){
            try {
                entity = clazz.findAnnotation<XmlString>()!!.stringRefactor.constructors.filter { it -> it.parameters.size == 1 }
                    .random().call(entity) as XMLEntity
            }catch (e: Exception){
                print("Não entendo como fazer nem oque fazer ao certo")
            }
        }
        return entity
    }

    private fun getName():String{
        val clazz: KClass<*> = objectToTranslate::class
        return "${clazz.findAnnotation<OverrideName>()?.name ?: clazz.simpleName}"
    }

    //todo validar que o construtor recebe um String, dar "rollback" caso esta merda dÊ nulll
    private fun getXMLAttributes():List<XMLAttribute>{
        val xmlAttributesToAdd:MutableList<XMLAttribute> = mutableListOf()
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

    private fun getXMLEntities():List<XMLEntity>{
        val xmlEntitiesToAdd:MutableList<XMLEntity> = mutableListOf()
        clazz.declaredMemberProperties.filter { it.hasAnnotation<IsEntity>() && !it.hasAnnotation<Ignore>()}.forEach{
            val nameAux = it.findAnnotation<OverrideName>()?.name ?: it.name
            if (( !defaultEntitiesTypes.contains(it.call(objectToTranslate)!!::class) && it.call(objectToTranslate)!! !is Iterable<*>)){
                val xmlEntityToAdd:XMLEntity = XMLTranslator(it.call(objectToTranslate)!!).entity
                xmlEntityToAdd.setName(nameAux)
                xmlEntitiesToAdd.add(xmlEntityToAdd)
            }else {
                val xmlEntityToAdd = XMLEntity(nameAux)

                if (it.call(objectToTranslate) is Iterable<*>) {
                    (it.call(objectToTranslate) as Iterable<*>).forEach { it2: Any? ->
                        if (it2 != null) xmlEntityToAdd.add(XMLTranslator(it2).entity)
                    }
                }
                val text: String = try {
                    it.findAnnotation<XmlString>()!!.stringRefactor.constructors.filter { it2 -> it2.parameters.size == 1 }
                        .random().call(it.call(objectToTranslate).toString()).toString()
                } catch (e: Exception) {
                    it.call(objectToTranslate).toString()
                }
                xmlEntityToAdd.setText(text)

                xmlEntitiesToAdd.add(xmlEntityToAdd)
            }
        }
        return xmlEntitiesToAdd
    }

}
