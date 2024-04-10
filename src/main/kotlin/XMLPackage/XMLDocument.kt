package XMLPackage

import java.io.File

class XMLDocument(
    private var xmlDocumentName:String,
    private val version: String = "1.0",
    private val enconding: String = "UTF-8"
): HasVisitor {
    private var xmlEntityChild: XMLEntity? = null

    val getName:String
        get() = xmlDocumentName
    val getVersion:String
        get() = version
    val getEncoding:String
        get() = enconding
    val getEntityChild: XMLEntity?
        get() = xmlEntityChild
    val hasEntityChild: Boolean
        get() = getEntityChild != null


    override fun accept(visitor: (HasVisitor) -> Boolean) {
        if(visitor(this)) {
            getEntityChild?.accept(visitor)
        }
    }

    fun changeName(newXMLDocumentName: String){
        xmlDocumentName = newXMLDocumentName
    }

    fun addXMLEntity(xmlEntityToAdd: XMLEntity): Boolean{
        if(hasEntityChild){
            return false
        }
        xmlEntityToAdd.addXMLParent(this)
        xmlEntityChild = xmlEntityToAdd
        return true
    }

    fun removeXMLEntity(xmlEntityToRemove: XMLEntity): Boolean {
        if (hasEntityChild && xmlEntityChild!! == xmlEntityToRemove) {
            xmlEntityChild = null
            if (xmlEntityToRemove.getDocumentParent != null) {
                xmlEntityToRemove.removeXMLParent()
            }
            return true
        }
        return false
    }

    private fun toPrettyPrint(): String{
        var str = "<?xml version=\"${ this.version }\" encoding=\"${this.enconding}\"?>"
        if (hasEntityChild){
            str += "\n${xmlEntityChild!!.prettyPrint}"
        }
        return str
    }

    val prettyPrint:String
        get() = toPrettyPrint()

    fun writeToFile(file_path: String):String{
        var file_path_aux = file_path
        if (! file_path_aux.endsWith(".xml") ){
            if(!file_path_aux.endsWith(File.separatorChar)) {
                file_path_aux += File.separatorChar
            }
            file_path_aux += "${ getName }.xml"
        }
        val file = File(file_path_aux)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(prettyPrint)
        return file.absolutePath
    }

    fun writeToFile(file: File):String{
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(prettyPrint)
        return file.absolutePath
    }

    fun addXMLAttributeGlobally(xmlEntityName:String, xmlAttributeNameToAdd:String, xmlAttributeValueToAdd:String){
        this.accept {
            if (it is XMLEntity && it.getName == xmlEntityName) {
                it.addXMLAttribute(xmlAttributeNameToAdd, xmlAttributeValueToAdd)
            }
            true
        }
    }

    fun replaceXMLEntityNameGlobally(oldXMLEntityName: String, newXMLEntityName:String){
        this.accept {
            if (it is XMLEntity && it.getName == oldXMLEntityName) {
                it.setXMLEntityName(newXMLEntityName)
            }
            true
        }
    }

    fun replaceXMLAttributeNameGlobally(xmlEntityName: String, oldXMLAttributeName: String, newXMLAttributeName: String ){
        this.accept {
            if (it is XMLEntity && it.getName == xmlEntityName) {
                it.changeXMLAttributeName(oldXMLAttributeName,newXMLAttributeName)
            }
            true
        }
    }

    fun removeXMLAttributeGlobally(xmlEntityName: String, xmlAttributeNameToRemove: String){
        this.accept {
            if (it is XMLEntity && it.getName == xmlEntityName)
                it.removeXMLAttribute(xmlAttributeNameToRemove)
            true
        }
    }

    fun micro_XPath(path: String): List<XMLEntity>{
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