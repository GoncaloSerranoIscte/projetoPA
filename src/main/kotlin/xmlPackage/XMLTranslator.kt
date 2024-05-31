package xmlPackage

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf


class XMLTranslator{
    //private val defaultEntitiesTypes: MutableList<Any>
    //    get() = mutableListOf(1::class,2.0::class,false::class, ""::class)

    private lateinit var objectToTranslate: Any
    private lateinit var defaultEntitiesTypes: List<KClass<*>>
    private val clazz:KClass<*>
        get() = objectToTranslate::class

    /**
     * Translates the given object into a XMLEntity instance
     * @param objectToTranslate Object to Translate
     * @param defaultEntitiesTypes List of KClass to not translate, default= Int, Double, Boolean and String
     * @return the Translated instance
     */
    fun toXMLEntity(objectToTranslate: Any, defaultEntitiesTypes: List<KClass<*>> =  mutableListOf(1::class,2.0::class,false::class, ""::class)):XMLEntity{
        this.objectToTranslate = objectToTranslate
        this.defaultEntitiesTypes = defaultEntitiesTypes
        val entity = XMLEntity(xmlEntityName = getName())
        entity.addAll(xmlElementsToAdd = getXMLAttributes())
        entity.addAll(xmlElementsToAdd = getXMLEntities())
        adaptEntity(entity)
        return entity
    }


    private fun adaptEntity(entity: XMLEntity) {
        if (clazz.hasAnnotation<XmlAdapter>()){
            val annotation = clazz.findAnnotation<XmlAdapter>()
            if (annotation != null) {
                val xmlRefactor = annotation.xmlRefactor
                if (xmlRefactor.isSubclassOf(XMLAdapterInterface::class)) {
                    val adapterInstance = xmlRefactor.java.getDeclaredConstructor().newInstance() as XMLAdapterInterface
                    val adaptEntityMethod = xmlRefactor.java.getDeclaredMethod("adaptXMLEntity", XMLEntity::class.java)
                    adaptEntityMethod.invoke(adapterInstance, entity)
                } else {
                    throw IllegalArgumentException("A classe deve implementar XMLAdapterInterface")
                }
            } else {
                throw IllegalArgumentException("A propriedade deve estar anotada com uma XMLAdapterInterface")
            }

        }
    }

    private fun getName():String{
        val clazz: KClass<*> = objectToTranslate::class
        return "${clazz.findAnnotation<OverrideName>()?.name ?: clazz.simpleName}"
    }


    private fun getString(kProperty: KProperty<*>, objectToTranslate: Any): String {
        return if (kProperty.hasAnnotation<XmlString>()) {
            val annotation = kProperty.findAnnotation<XmlString>()
            if (annotation != null) {
                val stringRefactor = annotation.stringRefactor
                if (stringRefactor.isSubclassOf(StringAdapterInterface::class)) {
                    val adapterInstance = stringRefactor.java.getDeclaredConstructor().newInstance() as StringAdapterInterface
                    val adaptStringMethod = stringRefactor.java.getDeclaredMethod("adaptString", String::class.java)
                    return adaptStringMethod.invoke(adapterInstance, kProperty.call(objectToTranslate).toString()) as String
                } else {
                    throw IllegalArgumentException("A classe deve implementar StringAdapterInterface")
                }
            } else {
                throw IllegalArgumentException("A propriedade deve estar anotada com uma StringAdapterInterface")
            }
        } else {
            kProperty.call(objectToTranslate).toString()
        }
    }


    private fun getXMLAttributes():List<XMLAttribute>{
        val xmlAttributesToAdd:MutableList<XMLAttribute> = mutableListOf()
        clazz.declaredMemberProperties.filter { it.hasAnnotation<IsAttribute>() && !it.hasAnnotation<Ignore>()}.forEach{
            val nameAt:String = it.findAnnotation<OverrideName>()?.name ?: it.name
            val valueAt: String = getString(it, objectToTranslate)
            xmlAttributesToAdd.add(XMLAttribute(name = nameAt, value = valueAt))
        }
        return xmlAttributesToAdd
    }

    private fun getXMLEntities():List<XMLEntity>{
        val xmlEntitiesToAdd:MutableList<XMLEntity> = mutableListOf()
        clazz.declaredMemberProperties.filter { it.hasAnnotation<IsEntity>() && !it.hasAnnotation<Ignore>()}.forEach{
            val nameAux = it.findAnnotation<OverrideName>()?.name ?: it.name
            if (( !defaultEntitiesTypes.contains(it.call(objectToTranslate)!!::class) && it.call(objectToTranslate)!! !is Iterable<*>)){
                val xmlEntityToAdd:XMLEntity = XMLTranslator().toXMLEntity(it.call(objectToTranslate)!!)
                xmlEntityToAdd.setName(nameAux)
                xmlEntitiesToAdd.add(xmlEntityToAdd)
            }else {
                val xmlEntityToAdd = XMLEntity(nameAux)
                if (it.call(objectToTranslate) is Iterable<*>) {
                    (it.call(objectToTranslate) as Iterable<*>).forEach { it2: Any? ->
                        if (it2 != null) xmlEntityToAdd.add(XMLTranslator().toXMLEntity(it2))
                    }
                }
                val text: String = getString(it, objectToTranslate)
                xmlEntityToAdd.setText(text)

                xmlEntitiesToAdd.add(xmlEntityToAdd)
            }
        }
        return xmlEntitiesToAdd
    }

}
