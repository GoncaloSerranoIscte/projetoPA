package xmlPackage


class XMLEntity private constructor(
    private var name:String,
    private var parentXMLDocument: XMLDocument?=null,
    private var parentXMLEntity: XMLEntity?=null
): HasVisitor {
    private val xmlEntitiesChildren: MutableList<XMLEntity> = mutableListOf()
    private val xmlAttributes: MutableList<XMLAttribute> = mutableListOf()
    private var text: String = ""

    /**
     * Gets the name of this instance
     * @return the name of this instance
     */
    val getName:String
        get()=this.name

    /**
     * Gets the parent of this instance
     * @return the instance(XMLDocument or XMLEntity) that is parent of this instance, or null if this instance has no parent
     */
    val getParent: Any?
        get() = this.getXMLParent()

    /**
     * Gets the XMLEntity parent of the instance
     * @return the XMLEntity instance that is parent of this instance, or null if this instance has no XMLEntity parent
     */
    val getEntityParent: XMLEntity?
        get() = this.parentXMLEntity

    /**
     * Gets the XMLDocument parent of the instance
     * @return the XMLDocument instance that is parent of this instance, or null in case this instance has no XMLDocument parent
     */
    val getDocumentParent: XMLDocument?
        get() = this.parentXMLDocument

    /**
     * Gets all the XMLEntities that are in this instance
     * @return the List of all the XMLEntities that are directly below this instance
     */
    val getChildEntities: MutableList<XMLEntity>
        get() = this.xmlEntitiesChildren

    /**
     * Gets all the XMLAttributes that this instance has
     * @return the List of all XMLAttributes defined in the entity
     */
    val getAttributes: List<XMLAttribute>
        get() = this.xmlAttributes

    /**
     * Gets the value of this instance's text
     * @return this instance's text, returns an empty String in case there isn't any
     */
    val getText: String
        get() = this.text

    /**
     * Check if this instance has any XMLEntities defined
     * @return true if this instance has any XMLEntities inside it and false otherwise
     */
    val hasChildren:Boolean
        get() = this.getChildEntities.size>0

    /**
     * @return true if this instance as any text defined and false otherwise
     */
    val hasText: Boolean
        get() = this.text != ""

    /**
     * @constructor creates an XMLEntity instance
     * @param xmlEntityName the name of the new XMLEntity
     * @param parentXMLDocument the XMLDocument instance that is parent of the new instance
     * @return the new XMLEntity instance with the defined name and parent
     */
    constructor(xmlEntityName: String, parentXMLDocument: XMLDocument) : this(xmlEntityName, parentXMLDocument, null)

    /**
     * @constructor creates an XMLEntity instance
     * @param xmlEntityName the name of the new XMLEntity
     * @param parentXMLEntity the XMLEntity instance that is parent of the new instance
     * @return the new XMLEntity instance with the defined name and parent
     */
    constructor(xmlEntityName: String, parentXMLEntity: XMLEntity) : this(xmlEntityName, null, parentXMLEntity)

    /**
     * @constructor creates an XMLEntity instance
     * @param xmlEntityName the name of the new XMLEntity
     * @return the new XMLEntity instance with the defined name and with no defined parent
     */
    constructor(xmlEntityName: String) : this(xmlEntityName, null, null)


    init {
        getDocumentParent?.addXMLEntity(this)
        getEntityParent?.addXMLEntityChild(this)
    }

    override fun accept(visitor: (HasVisitor) -> Boolean) {
        if(visitor(this))
            getChildEntities.forEach {
                it.accept(visitor)
            }
    }

    /**
     * Adds an XMLEntity to the List of XMLEntities that are directly below this instance
     * @param xmlEntityToAdd the XMLEntity instance to add
     * @return true if this instance can have XMLEntities defined, and false otherwise
     */
    fun addXMLEntityChild(xmlEntityToAdd: XMLEntity): Boolean{
        if (!this.hasText) {
            xmlEntityToAdd.addXMLParent(this)
            xmlEntitiesChildren.add(xmlEntityToAdd)
            return true
        }
        return false
    }

    /**
     * Adds XMLEntities to the List of XMLEntities that are directly below this instance
     * @param xmlEntitiesToAdd List of the XMLEntities instances to add
     * @return true if this instance can have XMLEntities defined, and false otherwise
     */
    fun addAllXMLEntitiesChildren(xmlEntitiesToAdd: List<XMLEntity>): Boolean{
        xmlEntitiesToAdd.forEach {
            if(!this.addXMLEntityChild(it)){
                return false
            }
        }
        return true
    }

    /**
     * Adds a parent to this XMLEntity, removing the previous one if there is any
     * @param newXMLParent instance of XMLDocument or XMLEntity to be defined as parent of this instance
     * @return true if the instance given can be added as parent and false otherwise
     */
    internal fun addXMLParent(newXMLParent: Any):Boolean{
        if(!(newXMLParent is XMLDocument || newXMLParent is XMLEntity)) {
            return false
        }
        if(newXMLParent is XMLDocument){
            if(newXMLParent.hasEntityChild){
                return false
            }
            this.removeXMLParent()
            this.parentXMLDocument=newXMLParent
        }
        if (newXMLParent is XMLEntity){
            this.removeXMLParent()
            this.parentXMLEntity=newXMLParent
        }
        return true
    }

    /**
     * Removes an XMLEntity instance from this instance
     * @param xmlEntityToRemove instance of XMLEntity to remove
     * @return the removed XMLEntity instance
     */
    fun removeXMLEntityChild(xmlEntityToRemove: XMLEntity): XMLEntity {
        if (xmlEntitiesChildren.contains(xmlEntityToRemove)) {
            xmlEntitiesChildren.remove(xmlEntityToRemove)
            xmlEntityToRemove.removeXMLParent()
            return xmlEntityToRemove
        }
        return xmlEntityToRemove
    }

    /**
     * Removes XMLEntities instances from this instance
     * @param xmlEntitiesToRemove List of instances of XMLEntities to remove
     * @return List of the removed XMLEntities instances
     */
    fun removeAllXMLEntitiesChildren(xmlEntitiesToRemove: List<XMLEntity>): List<XMLEntity>{
        xmlEntitiesToRemove.forEach {
            this.removeXMLEntityChild(it)
        }
        return xmlEntitiesToRemove
    }

    /**
     * Replaces the name of this instance
     * @param newXMLEntityName the new name
     */
    fun setXMLEntityName(newXMLEntityName: String){
        this.name=newXMLEntityName
    }

    /**
     * Defines the text of this instance
     * @param newXMLEntityText text to be defined in this instance
     * @return true if this instance can have a text defined, and false otherwise
     */
    fun setXMLEntityText(newXMLEntityText: String): Boolean{
        if (!hasChildren) {
            this.text = newXMLEntityText
            return true
        }
        return false
    }

    /**
     * Removes the text of this instance
     */
    fun removeXMLEntityText(){
        text=""
    }

    /**
     * Removes the connection between this instance and it's parent
     */
    fun removeXMLParent() {
        if (parentXMLEntity != null){
            parentXMLEntity!!.removeXMLEntityChild(this)
            parentXMLEntity = null
        }
        if(parentXMLDocument != null){
            parentXMLDocument!!.removeXMLEntity(this)
            parentXMLDocument = null
        }
    }

    /**
     * Adds an XMLAttribute to this instance
     * @param xmlAttributeToAdd XMLAttribute instance to add to this instance
     * @return the XMLAttribute added
     */
    fun addXMLAttribute(xmlAttributeToAdd: XMLAttribute): XMLAttribute? { 
        if(getAttributes.any { it.getName == xmlAttributeToAdd.getName }){
            return null
        }
        xmlAttributes.add(xmlAttributeToAdd)
        return xmlAttributeToAdd

    }

    /**
     * Adds an XMLAttribute to this instance
     * @param xmlAttributeNameToAdd name of the new XMLAttribute to add
     * @param xmlAttributeValueToAdd value of the new XMLAttribute to add
     * @return the XMLAttribute added
     */
    fun addXMLAttribute(xmlAttributeNameToAdd: String, xmlAttributeValueToAdd:String): XMLAttribute? {
        var xmlAttribute = XMLAttribute(xmlAttributeNameToAdd,xmlAttributeValueToAdd)
        return this.addXMLAttribute(xmlAttribute)
    }

    /**
     * Adds XMLAttributes to this instance
     * @param xmlAttributesToAdd List of XMLAttributes instances to add to this instance
     */
    fun addAllXMLAttribute(xmlAttributesToAdd: List<XMLAttribute>){
        xmlAttributesToAdd.forEach {
            this.addXMLAttribute(it)
        }
    }

    /**
     * Removes an XMLAttribute to this instance
     * @param xmlAttributeToRemove XMLAttribute instance to remove from this instance
     * @return the XMLAttribute removed
     */
    fun removeXMLAttribute(xmlAttributeToRemove: XMLAttribute): XMLAttribute {
        xmlAttributes.remove(xmlAttributeToRemove)
        return xmlAttributeToRemove
    }

    /**
     * Removes XMLAttributes to this instance
     * @param xmlAttributesToRemove List of XMLAttributes instances to remove from this instance
     * @return the List of the XMLAttributes that were removed from this instance
     */
    fun removeAllXMLAttributes(xmlAttributesToRemove:List<XMLAttribute>): List<XMLAttribute>{
        var xmlAttributesRemoved = mutableListOf<XMLAttribute>()
        xmlAttributesToRemove.forEach {
            if (this.xmlAttributes.contains(it)){
                this.removeXMLAttribute(it)
                xmlAttributesRemoved.add(it)
            }
        }
        return xmlAttributesRemoved
    }

    /**
     * Removes XMLAttributes to this instance
     * @param xmlAttributeNameToRemove Name of the XMLAttributes instances to remove from this instance
     * @return the List of the XMLAttributes that were removed from this instance
     */
    fun removeXMLAttribute(xmlAttributeNameToRemove:String): List<XMLAttribute>{
        return this.removeAllXMLAttributes(xmlAttributes.filter { it.getName == xmlAttributeNameToRemove })
    }

    /**
     * Replaces the name of XMLAttributes in this instance
     * @param oldXMLAttributeName Name of the XMLAttributes instances to be renamed
     * @param newXMLAttributeName new Name for the XMLAttributes to be renamed
     */
    fun changeXMLAttributeName(oldXMLAttributeName:String, newXMLAttributeName:String){
        xmlAttributes.filter { it.getName == oldXMLAttributeName }.forEach {
                it.changeXMLAttributeName(newXMLAttributeName)
        }
    }

    /**
     * Replaces the value of XMLAttributes in this instance
     * @param xmlAttributeName Name of the XMLAttributes instances to change
     * @param newXMLAttributeValue new value for the XMLAttributes
     */
    fun changeXMLAttributeValue(xmlAttributeName:String, newXMLAttributeValue:String){
        xmlAttributes.filter { it.getName == xmlAttributeName }.forEach {
                it.changeXMLAttributeValue(newXMLAttributeValue)
        }
    }

    /**
     * Replaces XMLAttribute in this instance
     * @param oldXMLAttribute XMLAttribute to be replaced
     * @param newXMLAttribute XMLAttribute to replace with
     * @return true if the XMLAttribute to be replaced was defined in this instance, and false otherwise
     */
    fun replaceXMLAttribute(oldXMLAttribute: XMLAttribute, newXMLAttribute: XMLAttribute): Boolean{
        if (xmlAttributes.contains(oldXMLAttribute)){
            this.removeXMLAttribute(oldXMLAttribute)
            this.addXMLAttribute(newXMLAttribute)
            return true
        }
        return false
    }

    /**
     * Gets the parent of the entity
     * @return the instance(XMLDocument or XMLEntity) that is parent of this instance, or null if this instance has no parent
     */
    private fun getXMLParent(): Any?{
        if (parentXMLEntity != null) return parentXMLEntity
        if (parentXMLDocument != null) return parentXMLDocument
        return null
    }

    private fun toString(depth: Int = 0):String{
        var str = ""
        str += "\t".repeat(depth) + "<${ this.name }"
        xmlAttributes.forEach{
            str += " ${it.getName}=\"${it.getValue}\""
        }
        if(text!=""){
            str += ">${this.text}</${this.name}>"
        }else if (xmlEntitiesChildren.size>0){
            str += ">"
            xmlEntitiesChildren.forEach {
                str += "\n${it.toString(depth + 1)}"
            }
            str += "\n" + "\t".repeat(depth) + "</${this.name}>"
        }else{
            str += "/>"
        }
        return str
    }

    /**
     * Gets the representation of this instance in prettyPrint format
     * @return the representation of this instance in prettyPrint format
     */
    val prettyPrint:String
        get() = toString(0)

    private fun getPath():String{
        var path = ""
        fun getPathAux(xmlEntity: XMLEntity) {
            if (xmlEntity.getEntityParent == null){
                return
            }
            else {
                path = "${xmlEntity.getEntityParent?.getName}/${path}"
                getPathAux(xmlEntity.getEntityParent!!)
            }
        }
        getPathAux(this)
        return path + this.getName
    }

    /**
     * Gets the path of this instance
     * @return the path of this instance
     */
    val getPath:String
        get() = this.getPath()

}