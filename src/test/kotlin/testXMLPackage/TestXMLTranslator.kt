package testXMLPackage

import ComponenteAvaliacao
import Laranja
import Maca
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import xmlPackage.XMLEntity
import xmlPackage.XMLTranslator

class TestXMLTranslator {

    @Test
    fun translateClassWithNothing(){
        val maca = Maca()
        val laranja = Laranja()
        val entityMaca:XMLEntity = XMLTranslator(maca).entity
        val entityLaranja:XMLEntity = XMLTranslator(laranja).entity
        assertEquals("Maça",entityMaca.getName)
        assertEquals("Laranja",entityLaranja.getName)
    }

    @Test
    fun addAttributes(){
        val quizzComponent = ComponenteAvaliacao(nome = "quizz", peso = 2)
        val entityquizzComponent:XMLEntity = XMLTranslator(quizzComponent).entity
        assertEquals("componente",entityquizzComponent.getName)
        assertEquals(2,entityquizzComponent.getAttributes.size)
        assertEquals("<componente nome=\"quizz\" peso=\"2\"/>", entityquizzComponent.prettyPrint)
    }
}