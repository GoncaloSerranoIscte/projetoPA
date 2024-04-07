import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TestXMLAttribute {

    @Test
    fun testchangeName(){
        val documento:XMLDocument=XMLDocument("documento")
        val entidade0:XMLEntity=XMLEntity("plano",documento)
        val atributo0:XMLAttribute=XMLAttribute("nome0","valor0")
        entidade0.addXMLAttribute(atributo0)
        atributo0.changeXMLAttributeName("nome1")
        assertEquals("nome1", atributo0.getName)
        assertEquals("nome1", entidade0.getAttributes[0].getName)
    }

    @Test
    fun testchangeValue(){
        val documento:XMLDocument=XMLDocument("documento")
        val entidade0:XMLEntity=XMLEntity("plano",documento)
        val atributo0:XMLAttribute=XMLAttribute("nome","valor0")
        entidade0.addXMLAttribute(atributo0)
        atributo0.changeXMLAttributeValue("valor1")
        assertEquals("valor1", atributo0.getValue)
        assertEquals("valor1", entidade0.getAttributes[0].getValue)
    }
}