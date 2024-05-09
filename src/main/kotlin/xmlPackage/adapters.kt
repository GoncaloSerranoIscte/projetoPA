package xmlPackage

interface XMLAdapterInterface {
    fun adaptXMLEntity(xmlEntity: XMLEntity)
}

interface StringAdapterInterface {
    fun adaptString(stringToAdapt: String):String
}