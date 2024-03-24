import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class Tests{

    @Test
    fun test(){
        assertTrue(true)
    }

    @Test
    fun testaddEntidade(){
        val documento:Document=Document("documento")
        val entidade0:Entidade=Entidade("plano",documento)
        val entidade1:Entidade=Entidade("curso",entidade0)
        assertEquals(mutableListOf<Entidade>(entidade0),documento.getEntidades())
        assertEquals(mutableListOf<Entidade>(entidade1),entidade0.getEntidades())
        assertEquals(mutableListOf<Entidade>(), entidade1.getEntidades())


    }

    @Test
    fun testremoveEntidade(){
        val documento:Document=Document("documento")
        val entidade0:Entidade=Entidade("plano",documento)
        val entidade1:Entidade=Entidade("curso",entidade0)
        documento.removeEntidade(entidade0)
        assertEquals(mutableListOf<Entidade>(),documento.getEntidades())
        assertEquals(mutableListOf<Entidade>(entidade1),entidade0.getEntidades())
        entidade0.removeEntidade(entidade1)
        assertEquals(mutableListOf<Entidade>(),entidade0.getEntidades())
        assertEquals(null,entidade1.getEntidadeParent())
    }

    @Test
    fun testsetTexto(){
        val documento:Document=Document("documento")
        val entidade0:Entidade=Entidade("plano",documento)
        val entidade1:Entidade=Entidade("plano2",entidade0)

        entidade1.setTexto("texto")
        assertEquals("texto",entidade1.texto)
        entidade0.setTexto("texto")
        assertEquals("",entidade0.texto)
    }

    @Test
    fun testremoveTexto(){
        val documento:Document=Document("documento")
        val entidade0:Entidade=Entidade("plano",documento)
        entidade0.setTexto("texto")
        entidade0.removeTexto()
        assertEquals("",entidade0.texto)
    }


    @Test
    fun testaddAtributo(){
        val documento:Document=Document("documento")
        val entidade0:Entidade=Entidade("plano",documento)
        val atributo0:Atributo=Atributo("dia","Sabado")
        entidade0.addAtributo(atributo0)
        assertEquals(mutableListOf<Atributo>(atributo0), entidade0.atributos)
    }

    @Test
    fun testremoveAtributo(){
        val documento:Document=Document("documento")
        val entidade0:Entidade=Entidade("plano",documento)
        val atributo0:Atributo=Atributo("dia","Sabado")
        entidade0.addAtributo(atributo0)
        entidade0.removeAtributo(atributo0)
        assertEquals(mutableListOf<Atributo>(), entidade0.atributos)
    }

    @Test
    fun testreplaceAtributo(){
        val documento:Document=Document("documento")
        val entidade0:Entidade=Entidade("plano",documento)
        val atributo0:Atributo=Atributo("dia","Sabado")
        entidade0.addAtributo(atributo0)
        val atributo1:Atributo=Atributo("mes","Abril")
        entidade0.replaceAtributo(atributo0,atributo1)
        assertEquals(mutableListOf<Atributo>(atributo1), entidade0.atributos)
    }

    @Test
    fun testgetEntidadeParent(){
        val documento:Document=Document("documento")
        val entidade0:Entidade=Entidade("plano",documento)
        val entidade1:Entidade=Entidade("curso",entidade0)
        assertEquals(entidade0,entidade1.getEntidadeParent())
    }

    @Test
    fun testchangeName(){
        val documento:Document=Document("documento")
        val entidade0:Entidade=Entidade("plano",documento)
        val atributo0:Atributo=Atributo("nome0","valor0")
        entidade0.addAtributo(atributo0)
        atributo0.changeName("nome1")
        assertEquals("nome1",atributo0.name)
        assertEquals("nome1",entidade0.atributos[0].name)
    }

    @Test
    fun testchangeValue(){
        val documento:Document=Document("documento")
        val entidade0:Entidade=Entidade("plano",documento)
        val atributo0:Atributo=Atributo("nome","valor0")
        entidade0.addAtributo(atributo0)
        atributo0.changeValue("valor1")
        assertEquals("valor1",atributo0.value)
        assertEquals("valor1",entidade0.atributos[0].value)
    }

}