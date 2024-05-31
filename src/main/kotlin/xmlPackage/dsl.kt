package xmlPackage

/**
 * Creates an entity inside the given entity
 * @param entityName name of the new XMLEntity
 * @param build applies this to the new entity
 * @return the new XMLEntity
 */
fun XMLEntity.entity(entityName:String, build: XMLEntity.() -> Unit = {}): XMLEntity=
    XMLEntity(xmlEntityName = entityName, parentXMLEntity = this).apply(build)

/**
 * Creates an entity
 * @param entityName name of the new XMLEntity
 * @param build applies this to the new entity
 * @return the new XMLEntity
 */
fun entity(entityName:String, build: XMLEntity.() -> Unit = {}): XMLEntity=
    XMLEntity(xmlEntityName = entityName).apply(build)

/**
 * Creates a new document
 * @param documentName name of the new XMLDocument
 * @param version version of the new XMLDocument
 * @param encoding encoding of the new XMLDocument
 * @param build applies this to the new document
 * @return the new XMLDocument
 */
fun document(documentName:String, version: String = "1.0", encoding: String = "UTF-8", build: XMLDocument.() -> Unit = {}):XMLDocument =
    XMLDocument(xmlDocumentName = documentName, version = version, encoding = encoding).apply(build)

/**
 * Creates an entity inside the given Document
 * @param entityName name of the new XMLEntity
 * @param build applies this to the new entity
 * @return the new XMLEntity
 */
fun XMLDocument.entity(entityName:String, build: XMLEntity.() -> Unit = {}): XMLEntity=
    XMLEntity(xmlEntityName = entityName, parentXMLDocument = this).apply(build)

/**
 * Creates an attribute
 * @param attributeName name of the new XMLAttribute
 * @param attributeValue value of the new XMLAttribute
 * @return the new XMLAttribute
 */
fun attribute(attributeName:String,attributeValue: String):XMLAttribute =
    XMLAttribute(attributeName,attributeValue)

/**
 * Creates an attribute inside the given Entity
 * @param attributeName name of the new XMLAttribute
 * @param attributeValue value of the new XMLAttribute
 * @return the new XMLAttribute
 */
fun XMLEntity.attribute(attributeName:String,attributeValue: String):XMLAttribute{
    this.add(xmlAttributeNameToAdd = attributeName, xmlAttributeValueToAdd = attributeValue)
    return this.getAttributes.first { it.getName == attributeName }
}

/**
 * Sets the text of the given Entity
 * @param text String to be defined as this entity's Text
 */
fun XMLEntity.text(text:String){
    this.setText(text)
}

/**
 * gets the entity with the given name
 * @param nameOfEntity name of the entity to find
 * @return the XMLEntity inside the called one with the given name
 */
operator fun XMLEntity.get(nameOfEntity: String): XMLEntity =
    this.getChildEntities.first { it.getName == nameOfEntity }

/**
 * gets the attribute with the given name
 * @param nameOfAttribute name of the Attribute to find
 * @return the XMLAttribute inside the called one with the given name
 */
operator fun XMLEntity.invoke(nameOfAttribute:  String): XMLAttribute =
    this.getAttributes.first { it.getName == nameOfAttribute }

/**
 * Creates an entity inside the called one
 * @param entityName name of the new entity
 */
operator fun XMLEntity.plusAssign(entityName: String){
    XMLEntity(xmlEntityName = entityName, parentXMLEntity = this)
}
/**
 * Creates an entity inside the called one
 * @param entity the new entity
 */
operator fun XMLEntity.plusAssign(entity: XMLEntity){
    this.add(entity)
}

/**
 * Creates an attribute inside the called one
 * @param attribute the new entity
 */
operator fun XMLEntity.plusAssign(attribute: XMLAttribute){
    this.add(attribute)
}