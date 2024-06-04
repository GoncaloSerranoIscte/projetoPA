package xmlPackage


/**
 * Implement this Interface to be able to use the XmlAdapter Annotation
 */
interface XMLAdapterInterface {
    /**
     * Implement this function to be able to use the XmlAdapter Annotation
     * @param xmlEntity XMLEntity to adapt
     */
    fun adaptXMLEntity(xmlEntity: XMLEntity)
}

/**
 * Implement this Interface to be able to use the XmlString Annotation
 */
interface StringAdapterInterface {
    /**
     * Implement this function to be able to use the XmlString Annotation
     * @param stringToAdapt String to adapt
     * @return adapted String
     */
    fun adaptString(stringToAdapt: String):String
}