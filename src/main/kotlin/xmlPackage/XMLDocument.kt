package xmlPackage

import java.io.File

/**
 * @constructor creates an XMLDocument instance
 * @param xmlDocumentName the name of the new XMLDocument
 * @param version the version of the new XMLDocument, default value defined as "1.0"
 * @param enconding the enconding of the new XMLDocument, default value defined as "UTF-8"
 * @return the new XMLDocument instance
 */
class XMLDocument(
    private var xmlDocumentName:String,
    private val version: String = "1.0",
    private val enconding: String = "UTF-8"
): HasVisitor {
    private var xmlEntityChild: XMLEntity? = null

    /**
     * Gets the name of this instance
     * @return the name of this instance
     */
    val getName:String
        get() = xmlDocumentName

    /**
     * Gets the version of this instance
     * @return the version of this instance
     */
    val getVersion:String
        get() = version

    /**
     * Gets the enconding of this instance
     * @return the enconding of this instance
     */
    val getEncoding:String
        get() = enconding

    /**
     * Gets the XMLEntity defined in this instance
     * @return the XMLEntity defined in this instance
     */
    val getEntityChild: XMLEntity?
        get() = xmlEntityChild

    /**
     * Checks if this instance has any XMLEntity defined
     * @return true if this instance has any XMLEntity defined and false otherwise
     */
    val hasEntityChild: Boolean
        get() = getEntityChild != null


    override fun accept(visitor: (HasVisitor) -> Boolean) {
        if(visitor(this)) {
            getEntityChild?.accept(visitor)
        }
    }

    /**
     * Replaces the name of this instance
     * @param newXMLDocumentName the new name
     */
    fun changeName(newXMLDocumentName: String){
        xmlDocumentName = newXMLDocumentName
    }

    /**
     * Adds a XMLEntity to this instance
     * @param xmlEntityToAdd the XMLEntity instance to add
     * @return true if this instance can have a XMLEntity defined, and false otherwise
     */
    fun addXMLEntity(xmlEntityToAdd: XMLEntity): Boolean{
        if(hasEntityChild){
            return false
        }
        xmlEntityToAdd.addXMLParent(this)
        xmlEntityChild = xmlEntityToAdd
        return true
    }

    /**
     * Removes a XMLEntity to this instance
     * @param xmlEntityToRemove the XMLEntity instance to remove
     * @return true if the XMLEntity can be removed from this instance
     */
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

    /**
     * Gets the representation of this instance in prettyPrint format
     * @return the representation of this instance in prettyPrint format
     */
    val prettyPrint:String
        get() = toPrettyPrint()

    /**
     * Writes the representation of this instance in prettyPrint format inside a File
     * @param file_path the path to the File that is going to be written
     * @return the path of the File that was written
     */
    fun writeToFile(file_path: String):String{
        var filePathAux = file_path
        if (! filePathAux.endsWith(".xml") ){
            if(!filePathAux.endsWith(File.separatorChar)) {
                filePathAux += File.separatorChar
            }
            filePathAux += "${ getName }.xml"
        }
        val file = File(filePathAux)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(prettyPrint)
        return file.absolutePath
    }

    /**
     * Writes the representation of this instance in prettyPrint format inside a File
     * @param file File that is going to be written
     * @return the path of the File that was written
     */
    fun writeToFile(file: File):String{
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(prettyPrint)
        return file.absolutePath
    }

    /**
     * Adds XMLAttributes globally to every XMLEntity with a given Name defined in this instance
     * @param xmlEntityName name of the XMLEntities that are going to be added a new XMLAttribute
     * @param xmlAttributeNameToAdd name of the new XMLAttribute to add
     * @param xmlAttributeValueToAdd value of the new XMLAttribute to add
     */
    fun addXMLAttributeGlobally(xmlEntityName:String, xmlAttributeNameToAdd:String, xmlAttributeValueToAdd:String){
        this.accept {
            if (it is XMLEntity && it.getName == xmlEntityName) {
                it.addXMLAttribute(xmlAttributeNameToAdd, xmlAttributeValueToAdd)
            }
            true
        }
    }

    /**
     * Adds XMLAttributes globally to every XMLEntity with a given Name defined in this instance
     * @param xmlEntityName name of the XMLEntities that are going to be added a new XMLAttribute
     * @param xmlAttributeNameToAdd name of the new XMLAttribute to add
     * @param xmlAttributeValueToAdd value of the new XMLAttribute to add
     */
    fun replaceXMLEntityNameGlobally(oldXMLEntityName: String, newXMLEntityName:String){
        this.accept {
            if (it is XMLEntity && it.getName == oldXMLEntityName) {
                it.setXMLEntityName(newXMLEntityName)
            }
            true
        }
    }

    /**
     * Replaces the name of XMLAttributes globally to every XMLEntity with a given Name defined in this instance
     * @param xmlEntityName name of the XMLEntities that are going to rename a XMLAttribute
     * @param oldXMLAttributeName Name of the XMLAttributes instances to be renamed
     * @param newXMLAttributeName new Name for the XMLAttributes to be renamed
     */
    fun replaceXMLAttributeNameGlobally(xmlEntityName: String, oldXMLAttributeName: String, newXMLAttributeName: String ){
        this.accept {
            if (it is XMLEntity && it.getName == xmlEntityName) {
                it.changeXMLAttributeName(oldXMLAttributeName,newXMLAttributeName)
            }
            true
        }
    }

    /**
     * Removes XMLAttributes globally to every XMLEntity with a given Name defined in this instance
     * @param xmlEntityName name of the XMLEntities that are going to remove a XMLAttribute
     * @param xmlAttributeNameToRemove XMLAttribute instance to remove
     */
    fun removeXMLAttributeGlobally(xmlEntityName: String, xmlAttributeNameToRemove: String){
        this.accept {
            if (it is XMLEntity && it.getName == xmlEntityName)
                it.removeXMLAttribute(xmlAttributeNameToRemove)
            true
        }
    }

    /**
     * Gets all the XMLEntities defined in this document that have a path corresponding with the given path
     * @param path given path
     * @return List of XMLEntities whose path ends with the path passed as a parameter
     */
    fun microXPath(path: String): List<XMLEntity>{
        val xmlEntitiesWithPath:MutableList<XMLEntity> = mutableListOf()
        this.accept {
            if(it is XMLEntity){
                if(it.getPath.endsWith(path))
                    xmlEntitiesWithPath.add(it)
            }
            true
        }
        return xmlEntitiesWithPath
    }
}