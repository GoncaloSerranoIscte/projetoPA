package XMLPackage

class XMLAttribute(
    private var name:String,
    private var value:String
) {
    val getName:String
        get() = name
    val getValue:String
        get() = value

    fun changeXMLAttributeName(newXMLAttributeName: String): Boolean{
        name=newXMLAttributeName
        return true
    }
    fun changeXMLAttributeValue(newXMLAttributeValue: String): Boolean{
        value=newXMLAttributeValue
        return true
    }
}