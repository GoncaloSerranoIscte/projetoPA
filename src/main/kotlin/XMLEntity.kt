class XMLEntity private constructor(
    private var name:String,
    private var parentXMLDocument: XMLDocument?=null,
    private var parentXMLEntity: XMLEntity?=null
): HasVisitor {
    private val xmlEntitiesChildren: MutableList<XMLEntity> = mutableListOf()
    private val xmlAttributes: MutableList<XMLAttribute> = mutableListOf()
    private var text: String = ""

    val getName:String
        get()=this.name

    val getParent: Any?
        get() = this.getXMLParent()
    
    val getEntityParent: XMLEntity?
        get() = this.parentXMLEntity
    
    val getDocumentParent: XMLDocument?
        get() = this.parentXMLDocument

    val getChildEntities: MutableList<XMLEntity>
        get() = this.xmlEntitiesChildren

    val getAttributes: MutableList<XMLAttribute>
        get() = this.xmlAttributes

    val getText: String
        get() = this.text

    val hasChildren: Boolean
        get() = this.getChildEntities.size>0

    val hasText: Boolean
        get() = this.text != ""

    constructor(xmlEntityName: String, parentXMLDocument: XMLDocument) : this(xmlEntityName, parentXMLDocument, null)
    constructor(xmlEntityName: String, parentXMLEntity: XMLEntity) : this(xmlEntityName, null, parentXMLEntity)
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


    fun addXMLEntityChild(xmlEntityToAdd: XMLEntity): Boolean{
        if (!this.hasText) {
            xmlEntityToAdd.addXMLParent(this)
            xmlEntitiesChildren.add(xmlEntityToAdd)
            return true
        }
        return false
    }

    fun addAllXMLEntitiesChildren(xmlEntitiesToAdd: List<XMLEntity>): Boolean{
        xmlEntitiesToAdd.forEach {
            if(!this.addXMLEntityChild(it)){
                return false
            }
        }
        return true
    }

    //todo internal is not internaling ASK stor
    internal fun addXMLParent(newXMLParent: Any):Boolean{
        if(!(newXMLParent is XMLDocument || newXMLParent is XMLEntity)) {
            return false
        }
        this.removeXMLParent()
        if(newXMLParent is XMLDocument){
            this.parentXMLDocument=newXMLParent
        }
        if (newXMLParent is XMLEntity){
            this.parentXMLEntity=newXMLParent
        }
        return true
    }

    fun removeXMLEntityChild(xmlEntityToRemove:XMLEntity): XMLEntity{
        if (xmlEntitiesChildren.contains(xmlEntityToRemove)) {
            xmlEntitiesChildren.remove(xmlEntityToRemove)
            xmlEntityToRemove.removeXMLParent()
            return xmlEntityToRemove
        }
        return xmlEntityToRemove
    }

    fun removeAllXMLEntitiesChildren(xmlEntitiesToRemove: List<XMLEntity>): List<XMLEntity>{
        xmlEntitiesToRemove.forEach {
            this.removeXMLEntityChild(it)
        }
        return xmlEntitiesToRemove
    }

    fun setXMLEntityName(newXMLEntityName: String): Boolean{
        this.name=newXMLEntityName
        return true
    }

    fun setXMLEntityText(newXMLEntityText: String): Boolean{
        if (!hasChildren) {
            this.text = newXMLEntityText
            return true
        }
        return false
    }

    fun removeXMLEntityText(): Boolean{
        text=""
        return true
    }

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

    fun addXMLAttribute(xmlAttributeToAdd: XMLAttribute): XMLAttribute{
        xmlAttributes.add(xmlAttributeToAdd)
        return xmlAttributeToAdd
    }

    fun addXMLAttribute(xmlAttributeNameToAdd: String, xmlAttributeValueToAdd:String): XMLAttribute{
        var xmlAttribute = XMLAttribute(xmlAttributeNameToAdd,xmlAttributeValueToAdd)
        return this.addXMLAttribute(xmlAttribute)
    }
    fun addAllXMLAttribute(xmlAttributesToAdd: List<XMLAttribute>): Boolean{
        xmlAttributesToAdd.forEach {
            this.addXMLAttribute(it)
        }
        return true
    }
    fun removeXMLAttribute(xmlAttributeToRemove:XMLAttribute): XMLAttribute{
        xmlAttributes.remove(xmlAttributeToRemove)
        return xmlAttributeToRemove
    }

    fun removeAllXMLAttributes(xmlAttributesToRemove:List<XMLAttribute>): List<XMLAttribute>{
        xmlAttributesToRemove.forEach {
            this.removeXMLAttribute(it)
        }
        return xmlAttributesToRemove
    }

    fun removeXMLAttribute(xmlAttributeNameToRemove:String): List<XMLAttribute>{
        val xmlAttributesToRemove:MutableList<XMLAttribute> = mutableListOf()
        xmlAttributes.forEach {
            if(it.getName == xmlAttributeNameToRemove)
                xmlAttributesToRemove.add(it)
        }
        return this.removeAllXMLAttributes(xmlAttributesToRemove)
    }

    fun changeXMLAttributeName(oldXMLAttributeName:String, newXMLAttributeName:String):Boolean{
        xmlAttributes.forEach {
            if(it.getName == oldXMLAttributeName)
                it.changeXMLAttributeName(newXMLAttributeName)
        }
        return true
    }

    fun changeXMLAttributeValue(xmlAttributeName:String, newXMLAttributeValue:String):Boolean{
        xmlAttributes.forEach {
            if(it.getName == xmlAttributeName)
                it.changeXMLAttributeValue(newXMLAttributeValue)
        }
        return true
    }

    fun replaceXMLAttribute(oldXMLAttribute: XMLAttribute, newXMLAttribute: XMLAttribute): Boolean{
        if (xmlAttributes.contains(oldXMLAttribute)){
            this.removeXMLAttribute(oldXMLAttribute)
            this.addXMLAttribute(newXMLAttribute)
            return true
        }
        return false
    }

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
        return path.removeSuffix("/")
    }

    val getPath:String
        get() = this.getPath()

}