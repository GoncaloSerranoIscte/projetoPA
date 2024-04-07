import java.io.File

data class XMLDocument(
    val xmlDocumentName:String,
    val version: String? = "1.0",
    val enconding: String? = "UTF-8"
): HasVisitor {
    val xmlEntitiesChildren: MutableList<XMLEntity> = mutableListOf<XMLEntity>()
    override fun accept(visitor: (HasVisitor) -> Boolean) {
        if(visitor(this))
            xmlEntitiesChildren.forEach {
                it.accept(visitor)
            }
    }

    fun addXMLEntity(xmlEntityToAdd:XMLEntity): Boolean{
        xmlEntitiesChildren.add(xmlEntityToAdd)
        xmlEntityToAdd.addXMLParent(this)
        return true
    }
    fun removeXMLEntity(xmlEntityToRemove:XMLEntity): Boolean{
        if (xmlEntitiesChildren.contains(xmlEntityToRemove)) {
            xmlEntitiesChildren.remove(xmlEntityToRemove)
            return true
        }
        return false
    }

    fun getXMLEntities():List<XMLEntity>{
        return xmlEntitiesChildren
    }

    fun getPrettyPrint(): String{
        var str = "<?xml version=\"${ this.version }\" encoding=\"${this.enconding}\"?>"
        xmlEntitiesChildren.forEach {
            str += "\n${it.toString()}"
        }
        return str
    }

    fun writeToFile(file_path: String){
        val file = File(file_path)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(getPrettyPrint())
    }

    //todo tests
    fun writeToFile(file: File){
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(getPrettyPrint())
    }

    fun addXMLAttributeGlobally(xmlEntityName:String, xmlAttributeNameToAdd:String, xmlAttributeValueToAdd:String){
        val xmlAttribute=XMLAttribute(xmlAttributeNameToAdd,xmlAttributeValueToAdd)
        this.accept {
            if (it is XMLEntity && it.getName == xmlEntityName) it.addXMLAttribute(xmlAttribute)
            true
        }
    }

    fun replaceXMLEntityNameGlobally(oldXMLEntityName: String, newXMLEntityName:String){
        this.accept {
            if (it is XMLEntity && it.getName == oldXMLEntityName) it.setXMLEntityName(newXMLEntityName)
            true
        }
    }

    //todo testes
    fun replaceXMLAttributeNameGlobally(xmlEntityName: String, oldXMLAttributeName: String, newXMLAttributeName: String ){
        this.accept {
            if (it is XMLEntity && it.getName == xmlEntityName) it.changeXMLAttributeName(oldXMLAttributeName,newXMLAttributeName)
            true
        }
    }

    //todo testes
    fun removeXMLAttributeGlobally(xmlEntityName: String, xmlAttributeNameToRemove: String){
        this.accept {
            if (it is XMLEntity && it.getName == xmlEntityName) it.removeXMLAttribute(xmlAttributeNameToRemove)
            true
        }
    }

    fun micro_XPath(path: String):MutableList<XMLEntity>{
        val xmlEntityWithPath:MutableList<XMLEntity> = mutableListOf()
        this.accept {
            if(it is XMLEntity){
                if(it.getPath.endsWith(path))
                    xmlEntityWithPath.add(it)
            }
            true
        }
        return xmlEntityWithPath
    }
}