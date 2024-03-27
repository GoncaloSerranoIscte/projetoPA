import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File


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

    @Test
    fun testgetString(){
        val documento:Document=Document("documento")
        val entidade0:Entidade=Entidade("entidade0",documento)
        val atributo0:Atributo=Atributo("atributo0","valor0")
        entidade0.addAtributo(atributo0)

        assertEquals("<entidade0 atributo0=\"valor0\"/>", entidade0.getString())
        entidade0.setTexto("texto0")
        assertEquals("<entidade0 atributo0=\"valor0\">texto0</entidade0>", entidade0.getString())
        assertEquals("\t<entidade0 atributo0=\"valor0\">texto0</entidade0>", entidade0.getString(1))

        val entidade1:Entidade=Entidade("entidade1",documento)
        val entidade2:Entidade=Entidade("entidade2",entidade1)
        val entidade3:Entidade=Entidade("entidade3",entidade1)
        val entidade4:Entidade=Entidade("entidade4",entidade3)
        val atributo1:Atributo=Atributo("atributo1","valor1")
        entidade2.setTexto("texto2")
        entidade2.addAtributo(atributo1)
        entidade4.setTexto("texto4")
        assertEquals("<entidade1>\n" +
                "\t<entidade2 atributo1=\"valor1\">texto2</entidade2>\n" +
                "\t<entidade3>\n" +
                "\t\t<entidade4>texto4</entidade4>\n" +
                "\t</entidade3>\n" +
                "</entidade1>", entidade1.getString())
    }

    @Test
    fun testgetPrettyPrint(){
        val documento:Document=Document("documento")
        val entidadeSup:Entidade=Entidade("entidadeSup",documento)
        val entidade0:Entidade=Entidade("entidade0",entidadeSup)
        val entidade1:Entidade=Entidade("entidade1",entidadeSup)
        val entidade2:Entidade=Entidade("entidade2",entidade1)
        val entidade3:Entidade=Entidade("entidade3",entidade1)
        val entidade4:Entidade=Entidade("entidade4",entidade3)
        val entidade5:Entidade=Entidade("entidade5",entidade3)
        val atributo0:Atributo=Atributo("atributo0","valor0")
        val atributo1:Atributo=Atributo("atributo1","valor1")
        entidade0.addAtributo(atributo0)
        entidade1.addAtributo(atributo0)
        entidade0.setTexto("texto0")
        entidade2.setTexto("texto2")
        entidade2.addAtributo(atributo1)
        entidade5.addAtributo(atributo1)
        entidade4.setTexto("texto4")
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<entidadeSup>\n" +
                "\t<entidade0 atributo0=\"valor0\">texto0</entidade0>\n" +
                "\t<entidade1 atributo0=\"valor0\">\n" +
                "\t\t<entidade2 atributo1=\"valor1\">texto2</entidade2>\n" +
                "\t\t<entidade3>\n" +
                "\t\t\t<entidade4>texto4</entidade4>\n" +
                "\t\t\t<entidade5 atributo1=\"valor1\"/>\n" +
                "\t\t</entidade3>\n" +
                "\t</entidade1>\n" +
                "</entidadeSup>",documento.getPrettyPrint())
    }

    @Test
    fun testwriteToFile(){
        val documento:Document=Document("documento")
        val entidadeSup:Entidade=Entidade("entidadeSup",documento)
        val entidade0:Entidade=Entidade("entidade0",entidadeSup)
        val entidade1:Entidade=Entidade("entidade1",entidadeSup)
        val entidade2:Entidade=Entidade("entidade2",entidade1)
        val entidade3:Entidade=Entidade("entidade3",entidade1)
        val entidade4:Entidade=Entidade("entidade4",entidade3)
        val entidade5:Entidade=Entidade("entidade5",entidade3)
        val atributo0:Atributo=Atributo("atributo0","valor0")
        val atributo1:Atributo=Atributo("atributo1","valor1")
        entidade0.addAtributo(atributo0)
        entidade1.addAtributo(atributo0)
        entidade0.setTexto("texto0")
        entidade2.setTexto("texto2")
        entidade2.addAtributo(atributo1)
        entidade5.addAtributo(atributo1)
        entidade4.setTexto("texto4")
        val file_path="src/test/kotlin/file0.xml"
        val test_file_path="src/test/Testfiles/test_file0.xml"
        documento.writeToFile(file_path)
        val file = File(file_path)
        val test_file = File(test_file_path)
        assertEquals(file.readLines(),test_file.readLines())
    }

    @Test
    fun testaddAtributoGlobally(){
        val documento:Document=Document("documento")
        val entidadeSup:Entidade=Entidade("entidadeSup",documento)
        val entidade0:Entidade=Entidade("entidade0",entidadeSup)
        val entidade1:Entidade=Entidade("entidade1",entidadeSup)
        val entidade2:Entidade=Entidade("entidade1",entidade1)
        val entidade3:Entidade=Entidade("entidade4",entidade1)
        val entidade4:Entidade=Entidade("entidade1",entidade3)
        val entidade5:Entidade=Entidade("entidade1",entidade3)
        documento.addAtributoGlobally("entidade1","atributo0","valor0")
        assertEquals(entidade1.atributos[0].name,"atributo0")
        assertEquals(entidade1.atributos[0].value,"valor0")
        assertEquals(entidade2.atributos[0].name,"atributo0")
        assertEquals(entidade2.atributos[0].value,"valor0")
        assertEquals(entidade4.atributos[0].name,"atributo0")
        assertEquals(entidade4.atributos[0].value,"valor0")
        assertEquals(entidade5.atributos[0].name,"atributo0")
        assertEquals(entidade5.atributos[0].value,"valor0")
        assertNotEquals(entidadeSup.atributos.size,0)
        assertNotEquals(entidadeSup.atributos.size,0)
        assertNotEquals(entidade0.atributos.size,0)
        assertNotEquals(entidade0.atributos.size,0)
        assertNotEquals(entidade3.atributos.size,0)
        assertNotEquals(entidade3.atributos.size,0)
    }

    @Test
    fun testreplaceEntidadeName(){
        val documento:Document=Document("documento")
        val entidadeSup:Entidade=Entidade("entidadeSup",documento)
        val entidade0:Entidade=Entidade("entidade0",entidadeSup)
        val entidade1:Entidade=Entidade("entidade1",entidadeSup)
        val entidade2:Entidade=Entidade("entidade1",entidade1)
        val entidade3:Entidade=Entidade("entidade4",entidade1)
        val entidade4:Entidade=Entidade("entidade1",entidade3)
        val entidade5:Entidade=Entidade("entidade1",entidade3)
        documento.replaceEntidadeName("entidade1","novo_nome")
        assertNotEquals(entidadeSup.name,"novo_nome")
        assertNotEquals(entidade0.name,"novo_nome")
        assertEquals(entidade1.name,"novo_nome")
        assertEquals(entidade2.name,"novo_nome")
        assertNotEquals(entidade3.name,"novo_nome")
        assertEquals(entidade4.name,"novo_nome")
        assertEquals(entidade5.name,"novo_nome")
    }
}