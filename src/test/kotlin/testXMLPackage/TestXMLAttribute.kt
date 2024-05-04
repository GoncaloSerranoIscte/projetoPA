package testXMLPackage

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import xmlPackage.XMLAttribute
import xmlPackage.XMLDocument
import xmlPackage.XMLEntity

class TestXMLAttribute {

    @Test
    fun testchangeName(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val atributo0= XMLAttribute(name = "nome0", value = "valor0")
        entidade0.add(xmlAttributeToAdd = atributo0)
        atributo0.changeXMLAttributeName(newXMLAttributeName = "nome1")
        val atributo1= XMLAttribute(name = "nome com ' '", value = "ola")
        assertEquals("nome1", atributo0.getName)
        assertEquals("nome1", entidade0.getAttributes[0].getName)
        assertEquals("nome_com_'_'", atributo1.getName)
    }

    @Test
    fun testchangeValue(){
        val documento= XMLDocument(xmlDocumentName = "documento")
        val entidade0= XMLEntity(xmlEntityName = "plano",documento)
        val atributo0= XMLAttribute(name = "nome", value = "valor0")
        entidade0.add(xmlAttributeToAdd = atributo0)
        atributo0.changeXMLAttributeValue(newXMLAttributeValue = "valor1")
        assertEquals("valor1", atributo0.getValue)
        assertEquals("valor1", entidade0.getAttributes[0].getValue)
    }
}