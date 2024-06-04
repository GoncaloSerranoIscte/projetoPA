package testXMLPackage

import ComponenteAvaliacao
import FUC
import Laranja
import Maca
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import xmlPackage.XMLEntity
import xmlPackage.XMLTranslator

class TestXMLTranslator {

    @Test
    fun translateClassWithNothing(){
        val tradutor = XMLTranslator()
        val maca = Maca()
        val laranja = Laranja()
        val entityMaca:XMLEntity = tradutor.toXMLEntity(objectToTranslate = maca)
        val entityLaranja:XMLEntity = tradutor.toXMLEntity(objectToTranslate = laranja)
        assertEquals("Maça",entityMaca.getName)
        assertEquals("Laranja",entityLaranja.getName)
        val stringTest:XMLEntity = tradutor.toXMLEntity(objectToTranslate = "texto")
        assertEquals("<String/>", stringTest.prettyPrint)
        val stringInt:XMLEntity = tradutor.toXMLEntity(objectToTranslate = 3)
        assertEquals("<Int/>", stringInt.prettyPrint)
    }

    @Test
    fun addAttributes(){
        val quizzComponent = ComponenteAvaliacao(nome = "quizz", peso = 2, ano = 2002)
        val entityquizzComponent:XMLEntity = XMLTranslator().toXMLEntity(objectToTranslate = quizzComponent)
        assertEquals("componente",entityquizzComponent.getName)
        assertEquals(2,entityquizzComponent.getAttributes.size)
        assertEquals("<componente nome=\"quizz\" peso=\"2%\"/>", entityquizzComponent.prettyPrint)
    }

    @Test
    fun addEntities(){
        val quizzComponent = ComponenteAvaliacao(nome = "quizz", peso = 2, ano = 2002)
        val quizzComponent1 = ComponenteAvaliacao(nome = "Exame", peso = 100, ano = 2002)
        val quizzComponent2 = ComponenteAvaliacao(nome = "teste", peso = 4, ano = 2002)
        val fuc = FUC(codigo = "0102", nome = "Calculo", ects = 6.0, observacoes = "Nao te inscrevas", mutableListOf(quizzComponent,quizzComponent2),quizzComponent1)
        val entityFuc:XMLEntity = XMLTranslator().toXMLEntity(objectToTranslate = fuc)
        assertEquals("<fuc codigo=\"0102\">\n" +
                "\t<avaliacao>\n" +
                "\t\t<componente nome=\"quizz\" peso=\"2%\"/>\n" +
                "\t\t<componente nome=\"teste\" peso=\"4%\"/>\n" +
                "\t</avaliacao>\n" +
                "\t<Componente Principal nome=\"Exame\" peso=\"100%\"/>\n" +
                "\t<ects>6.0</ects>\n" +
                "\t<nome>Calculo</nome>\n" +
                "</fuc>",entityFuc.prettyPrint)
    }
}