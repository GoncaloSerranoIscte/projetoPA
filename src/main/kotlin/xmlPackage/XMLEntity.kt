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
        getDocumentParent?.add(this)
        getEntityParent?.add(this)
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
     * @return this Instance
     */
    fun add(xmlEntityToAdd: XMLEntity): XMLEntity{
        if (!this.hasText) {
            xmlEntityToAdd.addParent(this)
            xmlEntitiesChildren.add(xmlEntityToAdd)
        }
        return this
    }



    /**
     * Adds a parent to this XMLEntity, removing the previous one if there is any
     * @param newXMLParent instance of XMLDocument or XMLEntity to be defined as parent of this instance
     * @return this Instance
     */
    internal fun addParent(newXMLParent: Any):XMLEntity{
        if(!(newXMLParent is XMLDocument || newXMLParent is XMLEntity)) {
            return this
        }
        if(newXMLParent is XMLDocument){
            if(newXMLParent.hasEntityChild){
                return this
            }
            this.removeParent()
            this.parentXMLDocument=newXMLParent
        }
        if (newXMLParent is XMLEntity){
            this.removeParent()
            this.parentXMLEntity=newXMLParent
        }
        return this
    }

    /**
     * Removes an XMLEntity instance from this instance
     * @param xmlEntityToRemove instance of XMLEntity to remove
     * @return this Instance
     */
    fun remove(xmlEntityToRemove: XMLEntity): XMLEntity {
        if (xmlEntitiesChildren.contains(xmlEntityToRemove)) {
            xmlEntitiesChildren.remove(xmlEntityToRemove)
            xmlEntityToRemove.removeParent()
        }
        return this
    }



    /**
     * Replaces the name of this instance
     * @param newXMLEntityName the new name
     * @return this Instance
     */
    fun setName(newXMLEntityName: String):XMLEntity{
        this.name=newXMLEntityName
        return this
    }

    /**
     * Defines the text of this instance
     * @param newXMLEntityText text to be defined in this instance
     * @return this Instance
     */
    fun setText(newXMLEntityText: String): XMLEntity{
        if (!hasChildren) {
            this.text = newXMLEntityText
        }
        return this
    }

    /**
     * Removes the text of this instance
     * @return this Instance
     */
    fun removeText():XMLEntity{
        text=""
        return this
    }

    /**
     * Removes the connection between this instance and it's parent
     * @return this Instance
     */
    fun removeParent() :XMLEntity{
        if (parentXMLEntity != null){
            parentXMLEntity!!.remove(this)
            parentXMLEntity = null
        }
        if(parentXMLDocument != null){
            parentXMLDocument!!.remove(this)
            parentXMLDocument = null
        }
        return this
    }

    /**
     * Adds an XMLAttribute to this instance or uptades the attribute defined with the same name
     * @param xmlAttributeToAdd XMLAttribute instance to add to this instance
     * @return this Instance
     */
    fun add(xmlAttributeToAdd: XMLAttribute): XMLEntity {
        if(getAttributes.any { it.getName == xmlAttributeToAdd.getName }){
            xmlAttributes.remove(getAttributes.first{it.getName == xmlAttributeToAdd.getName})
            
        }
        xmlAttributes.add(xmlAttributeToAdd)
        return this

    }

    /**
     * Adds an XMLAttribute to this instance
     * @param xmlAttributeNameToAdd name of the new XMLAttribute to add
     * @param xmlAttributeValueToAdd value of the new XMLAttribute to add
     * @return this Instance
     */
    fun add(xmlAttributeNameToAdd: String, xmlAttributeValueToAdd:String): XMLEntity {
        val xmlAttribute = XMLAttribute(xmlAttributeNameToAdd,xmlAttributeValueToAdd)
        this.add(xmlAttribute)
        return this
    }

    /**
     * Adds XMLEntities and XLMAttributes to the List of XMLEntities that are directly below this instance
     * @param xmlElementsToAdd List of the XMLEntities instances to add
     * @return this Instance
     */
    fun addAll(xmlElementsToAdd: List<Any>): XMLEntity{
        xmlElementsToAdd.forEach {
            if (it is XMLEntity){
                this.add(xmlEntityToAdd = it)
            }
            if (it is XMLAttribute){
                this.add(xmlAttributeToAdd = it)
            }
        }
        return this
    }

    /**
     * Removes an XMLAttribute to this instance
     * @param xmlAttributeToRemove XMLAttribute instance to remove from this instance
     * @return this Instance
     */
    fun remove(xmlAttributeToRemove: XMLAttribute): XMLEntity {
        xmlAttributes.remove(xmlAttributeToRemove)
        return this
    }



    /**
     * Removes XLMEntities and XMLAttributes instances from this instance
     * @param xmlElementsToRemove List of instances of XMLEntities to remove
     * @return this Instance
     */
    fun removeAll(xmlElementsToRemove: List<Any>): XMLEntity{
        xmlElementsToRemove.forEach { it ->
            if (it is XMLEntity){
                this.remove(xmlEntityToRemove = it)
            }
            if(it is XMLAttribute) {
                this.remove(xmlAttributeToRemove = it)
            }
        }
        return this
    }

    /**
     * Removes XMLAttributes to this instance
     * @param xmlAttributeNameToRemove Name of the XMLAttributes instances to remove from this instance
     * @return this Instance
     */
    fun remove(xmlAttributeNameToRemove:String): XMLEntity {
        this.removeAll(xmlElementsToRemove = xmlAttributes.filter { it.getName == xmlAttributeNameToRemove })
        return this
    }

    /**
     * Replaces the name of XMLAttributes in this instance
     * @param oldXMLAttributeName Name of the XMLAttributes instances to be renamed
     * @param newXMLAttributeName new Name for the XMLAttributes to be renamed
     * @return this Instance
     */
    fun changeXMLAttributeName(oldXMLAttributeName:String, newXMLAttributeName:String):XMLEntity{
        xmlAttributes.filter { it.getName == oldXMLAttributeName }.forEach {
                it.changeXMLAttributeName(newXMLAttributeName)
        }
        return this
    }

    /**
     * Replaces the value of XMLAttributes in this instance
     * @param xmlAttributeName Name of the XMLAttributes instances to change
     * @param newXMLAttributeValue new value for the XMLAttributes
     * @return this Instance
     */
    fun changeXMLAttributeValue(xmlAttributeName:String, newXMLAttributeValue:String):XMLEntity{
        xmlAttributes.filter { it.getName == xmlAttributeName }.forEach {
                it.changeXMLAttributeValue(newXMLAttributeValue)
        }
        return this
    }

    /**
     * Replaces XMLAttribute in this instance
     * @param oldXMLAttribute XMLAttribute to be replaced
     * @param newXMLAttribute XMLAttribute to replace with
     * @return this Instance
     */
    fun replaceXMLAttribute(oldXMLAttribute: XMLAttribute, newXMLAttribute: XMLAttribute): XMLEntity{
        if (xmlAttributes.contains(oldXMLAttribute)){
            this.remove(oldXMLAttribute)
            this.add(newXMLAttribute)
        }
        return this
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