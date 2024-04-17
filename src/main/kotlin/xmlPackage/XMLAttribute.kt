package xmlPackage

/**
 * @constructor creates an XMLAttribute instance
 * @param name the name of the new XMLAttribute if the name given has a " "(space) replaces it with "_"(underscore)
 * @param value the value of the new XMLAttribute
 * @return the new XMLAttribute instance with the defined name and value
 */
class XMLAttribute(
    private var name:String,
    private var value:String
) {
    init{
        changeXMLAttributeName(getName)
    }
    /**
     * Gets the name of this instance
     * @return the name of this instance
     */
    val getName:String
        get() = name

    /**
     * Gets the value of this instance
     * @return the value of this instance
     */
    val getValue:String
        get() = value

    /**
     * Changed the name of this instance if the name given has a " "(space) replaces it with "_"(underscore)
     * @param newXMLAttributeName The new name to be defined
     */
    fun changeXMLAttributeName(newXMLAttributeName: String){
        name=newXMLAttributeName.replace(" ", "_")
    }

    /**
     * Changed the value of this instance
     * @param newXMLAttributeValue The new value to be defined
     */
    fun changeXMLAttributeValue(newXMLAttributeValue: String){
        value=newXMLAttributeValue
    }
}