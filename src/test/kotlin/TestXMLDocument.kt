import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File


class TestXMLDocument{

    @Test
    fun test(){
        assertTrue(true)
    }


    @Test
    fun testgetPrettyPrint(){
        val documento:XMLDocument=XMLDocument("documento")
        val entidadeSup:XMLEntity=XMLEntity("entidadeSup",documento)
        val entidade0:XMLEntity=XMLEntity("entidade0",entidadeSup)
        val entidade1:XMLEntity=XMLEntity("entidade1",entidadeSup)
        val entidade2:XMLEntity=XMLEntity("entidade2",entidade1)
        val entidade3:XMLEntity=XMLEntity("entidade3",entidade1)
        val entidade4:XMLEntity=XMLEntity("entidade4",entidade3)
        val entidade5:XMLEntity=XMLEntity("entidade5",entidade3)
        val atributo0:XMLAttribute=XMLAttribute("atributo0","valor0")
        val atributo1:XMLAttribute=XMLAttribute("atributo1","valor1")
        entidade0.addXMLAttribute(atributo0)
        entidade1.addXMLAttribute(atributo0)
        entidade0.setXMLEntityText("texto0")
        entidade2.setXMLEntityText("texto2")
        entidade2.addXMLAttribute(atributo1)
        entidade5.addXMLAttribute(atributo1)
        entidade4.setXMLEntityText("texto4")
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
        val documento:XMLDocument=XMLDocument("documento")
        val entidadeSup:XMLEntity=XMLEntity("entidadeSup",documento)
        val entidade0:XMLEntity=XMLEntity("entidade0",entidadeSup)
        val entidade1:XMLEntity=XMLEntity("entidade1",entidadeSup)
        val entidade2:XMLEntity=XMLEntity("entidade2",entidade1)
        val entidade3:XMLEntity=XMLEntity("entidade3",entidade1)
        val entidade4:XMLEntity=XMLEntity("entidade4",entidade3)
        val entidade5:XMLEntity=XMLEntity("entidade5",entidade3)
        val atributo0:XMLAttribute=XMLAttribute("atributo0","valor0")
        val atributo1:XMLAttribute=XMLAttribute("atributo1","valor1")
        entidade0.addXMLAttribute(atributo0)
        entidade1.addXMLAttribute(atributo0)
        entidade0.setXMLEntityText("texto0")
        entidade2.setXMLEntityText("texto2")
        entidade2.addXMLAttribute(atributo1)
        entidade5.addXMLAttribute(atributo1)
        entidade4.setXMLEntityText("texto4")
        val file_path="src/test/kotlin/file0.xml"
        val test_file_path="src/test/Testfiles/test_file0.xml"
        documento.writeToFile(file_path)
        val file = File(file_path)
        val test_file = File(test_file_path)
        assertEquals(file.readLines(),test_file.readLines())
    }

    @Test
    fun testaddAtributoGlobally(){
        val documento:XMLDocument=XMLDocument("documento")
        val entidadeSup:XMLEntity=XMLEntity("entidadeSup",documento)
        val entidade0:XMLEntity=XMLEntity("entidade0",entidadeSup)
        val entidade1:XMLEntity=XMLEntity("entidade1",entidadeSup)
        val entidade2:XMLEntity=XMLEntity("entidade1",entidade1)
        val entidade3:XMLEntity=XMLEntity("entidade4",entidade1)
        val entidade4:XMLEntity=XMLEntity("entidade1",entidade3)
        val entidade5:XMLEntity=XMLEntity("entidade1",entidade3)
        documento.addXMLAttributeGlobally("entidade1","atributo0","valor0")
        assertEquals(entidade1.getAttributes[0].getName,"atributo0")
        assertEquals(entidade1.getAttributes[0].getValue,"valor0")
        assertEquals(entidade2.getAttributes[0].getName,"atributo0")
        assertEquals(entidade2.getAttributes[0].getValue,"valor0")
        assertEquals(entidade4.getAttributes[0].getName,"atributo0")
        assertEquals(entidade4.getAttributes[0].getValue,"valor0")
        assertEquals(entidade5.getAttributes[0].getName,"atributo0")
        assertEquals(entidade5.getAttributes[0].getValue,"valor0")
        assertEquals(entidadeSup.getAttributes.size,0)
        assertEquals(entidadeSup.getAttributes.size,0)
        assertEquals(entidade0.getAttributes.size,0)
        assertEquals(entidade0.getAttributes.size,0)
        assertEquals(entidade3.getAttributes.size,0)
        assertEquals(entidade3.getAttributes.size,0)
    }

    @Test
    fun testreplaceEntidadeName(){
        val documento:XMLDocument=XMLDocument("documento")
        val entidadeSup:XMLEntity=XMLEntity("entidadeSup",documento)
        val entidade0:XMLEntity=XMLEntity("entidade0",entidadeSup)
        val entidade1:XMLEntity=XMLEntity("entidade1",entidadeSup)
        val entidade2:XMLEntity=XMLEntity("entidade1",entidade1)
        val entidade3:XMLEntity=XMLEntity("entidade4",entidade1)
        val entidade4:XMLEntity=XMLEntity("entidade1",entidade3)
        val entidade5:XMLEntity=XMLEntity("entidade1",entidade3)
        documento.replaceXMLEntityNameGlobally("entidade1","novo_nome")
        assertNotEquals(entidadeSup.getName,"novo_nome")
        assertNotEquals(entidade0.getName,"novo_nome")
        assertEquals(entidade1.getName,"novo_nome")
        assertEquals(entidade2.getName,"novo_nome")
        assertNotEquals(entidade3.getName,"novo_nome")
        assertEquals(entidade4.getName,"novo_nome")
        assertEquals(entidade5.getName,"novo_nome")
    }

    @Test
    fun testmicro_XPath(){
        val documento:XMLDocument=XMLDocument("documento")
        val entidadeSup:XMLEntity=XMLEntity("entidadeSup",documento)
        val entidade0:XMLEntity=XMLEntity("entidade0",entidadeSup)
        val entidade1:XMLEntity=XMLEntity("entidade1",entidadeSup)
        val entidade2:XMLEntity=XMLEntity("entidade1",entidade1)
        val entidade3:XMLEntity=XMLEntity("entidade4",entidade1)
        val entidade4:XMLEntity=XMLEntity("entidade1",entidade3)
        val entidade5:XMLEntity=XMLEntity("entidade1",entidade3)
        assertEquals(mutableListOf<XMLEntity>(),documento.micro_XPath("nenhumaEntidade"))
        assertEquals(mutableListOf<XMLEntity>(entidade4, entidade5),documento.micro_XPath("entidade4"))
        assertEquals(mutableListOf<XMLEntity>(entidadeSup,entidade0,entidade1,entidade2,entidade3,entidade4, entidade5),documento.micro_XPath(""))

    }




}

