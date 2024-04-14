package TestXMLPackage

import xmlPackage.XMLAttribute
import xmlPackage.XMLDocument
import xmlPackage.XMLEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestXMLAttribute {

    @Test
    fun testchangeName(){
        val documento= XMLDocument(xmlDocumentName = "documento")
        val entidade0= XMLEntity(xmlEntityName = "plano", parentXMLDocument = documento)
        val atributo0= XMLAttribute(name = "nome0", value = "valor0")
        entidade0.addXMLAttribute(xmlAttributeToAdd = atributo0)
        atributo0.changeXMLAttributeName(newXMLAttributeName = "nome1")
        assertEquals("nome1", atributo0.getName)
        assertEquals("nome1", entidade0.getAttributes[0].getName)
    }

    @Test
    fun testchangeValue(){
        val documento= XMLDocument(xmlDocumentName = "documento")
        val entidade0= XMLEntity(xmlEntityName = "plano",documento)
        val atributo0= XMLAttribute(name = "nome", value = "valor0")
        entidade0.addXMLAttribute(xmlAttributeToAdd = atributo0)
        atributo0.changeXMLAttributeValue(newXMLAttributeValue = "valor1")
        assertEquals("valor1", atributo0.getValue)
        assertEquals("valor1", entidade0.getAttributes[0].getValue)
    }
}