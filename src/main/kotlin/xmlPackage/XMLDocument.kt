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
     * @return this Instance
     */
    fun changeName(newXMLDocumentName: String):XMLDocument{
        xmlDocumentName = newXMLDocumentName
        return this
    }

    /**
     * Adds a XMLEntity to this instance
     * @param xmlEntityToAdd the XMLEntity instance to add
     * @return this Instance
     */
    fun add(xmlEntityToAdd: XMLEntity): XMLDocument{
        if(hasEntityChild){
            return this
        }
        xmlEntityToAdd.addParent(this)
        xmlEntityChild = xmlEntityToAdd
        return this
    }

    /**
     * Removes a XMLEntity to this instance
     * @param xmlEntityToRemove the XMLEntity instance to remove
     * @return this Instance
     */
    fun remove(xmlEntityToRemove: XMLEntity): XMLDocument {
        if (hasEntityChild && xmlEntityChild!! == xmlEntityToRemove) {
            xmlEntityChild = null
            if (xmlEntityToRemove.getDocumentParent != null) {
                xmlEntityToRemove.removeParent()
            }
        }
        return this
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
     * @return this Instance
     */
    fun addXMLAttributeGlobally(xmlEntityName:String, xmlAttributeNameToAdd:String, xmlAttributeValueToAdd:String):XMLDocument{
        this.accept {
            if (it is XMLEntity && it.getName == xmlEntityName) {
                it.add(xmlAttributeNameToAdd, xmlAttributeValueToAdd)
            }
            true
        }
        return this
    }

    /**
     * Renames XMLEntities globally to every XMLEntity with a given Name defined in this instance
     * @param oldXMLEntityName name of the XMLEntities that are going to be renamed
     * @param newXMLEntityName name that is going to be applied
     * @return this Instance
     */
    fun replaceXMLEntityNameGlobally(oldXMLEntityName: String, newXMLEntityName:String):XMLDocument{
        this.accept {
            if (it is XMLEntity && it.getName == oldXMLEntityName) {
                it.setName(newXMLEntityName)
            }
            true
        }
        return this
    }

    /**
     * Replaces the name of XMLAttributes globally to every XMLEntity with a given Name defined in this instance
     * @param xmlEntityName name of the XMLEntities that are going to rename a XMLAttribute
     * @param oldXMLAttributeName Name of the XMLAttributes instances to be renamed
     * @param newXMLAttributeName new Name for the XMLAttributes to be renamed
     * @return this Instance
     */
    fun replaceXMLAttributeNameGlobally(xmlEntityName: String, oldXMLAttributeName: String, newXMLAttributeName: String ):XMLDocument{
        this.accept {
            if (it is XMLEntity && it.getName == xmlEntityName) {
                it.changeXMLAttributeName(oldXMLAttributeName,newXMLAttributeName)
            }
            true
        }
        return this
    }

    /**
     * Removes XMLAttributes globally to every XMLEntity with a given Name defined in this instance
     * @param xmlEntityName name of the XMLEntities that are going to remove a XMLAttribute
     * @param xmlAttributeNameToRemove XMLAttribute instance to remove
     * @return this Instance
     */
    fun removeXMLAttributeGlobally(xmlEntityName: String, xmlAttributeNameToRemove: String):XMLDocument{
        this.accept {
            if (it is XMLEntity && it.getName == xmlEntityName)
                it.remove(xmlAttributeNameToRemove)
            true
        }
        return this
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