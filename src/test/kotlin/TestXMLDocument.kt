import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File


class TestXMLDocument{

    @Test
    fun getName(){
        val documento = XMLDocument(xmlDocumentName = "documento")
        assertEquals("documento",documento.getName)
    }

    @Test
    fun getVersion(){
        val documento0 = XMLDocument(xmlDocumentName = "documento", version = "2.0")
        val documento1 = XMLDocument(xmlDocumentName = "documento")
        assertEquals("2.0",documento0.getVersion)
        assertEquals("1.0",documento1.getVersion)
    }

    @Test
    fun getEncoding(){
        val documento0 = XMLDocument(xmlDocumentName = "documento", enconding = "UTF-7")
        val documento1 = XMLDocument(xmlDocumentName = "documento")
        assertEquals("UTF-7",documento0.getEncoding)
        assertEquals("UTF-8",documento1.getEncoding)
    }

    @Test
    fun addXMLEntity(){
        val documento = XMLDocument(xmlDocumentName = "documento")
        val documento1 = XMLDocument(xmlDocumentName = "documento1")
        val entidade = XMLEntity(xmlEntityName = "entidade")
        documento.addXMLEntity(xmlEntityToAdd = entidade)
        assertEquals(mutableListOf(entidade),documento.getChildEntities)
        assertEquals(documento,entidade.getParent)
        assertEquals(mutableListOf<XMLEntity>(),documento1.getChildEntities)
        documento1.addXMLEntity(xmlEntityToAdd = entidade)
        assertEquals(mutableListOf<XMLEntity>(),documento.getChildEntities)
        assertEquals(documento1,entidade.getParent)
        assertEquals(mutableListOf(entidade),documento1.getChildEntities)
    }

    @Test
    fun removeXMLEntity(){
        val documento = XMLDocument(xmlDocumentName = "documento")
        val entidade = XMLEntity(xmlEntityName = "entidade")
        documento.addXMLEntity(xmlEntityToAdd = entidade)
        assertEquals(mutableListOf(entidade),documento.getChildEntities)
        assertEquals(documento,entidade.getParent)
        documento.removeXMLEntity(xmlEntityToRemove = entidade)
        assertEquals(mutableListOf<XMLEntity>(),documento.getChildEntities)
        assertEquals(null,entidade.getParent)
    }

    @Test
    fun prettyPrint(){
        val documento=XMLDocument(xmlDocumentName = "documento")
        val entidadeSup=XMLEntity(xmlEntityName = "entidadeSup", parentXMLDocument = documento)
        val entidade0=XMLEntity(xmlEntityName = "entidade0", parentXMLEntity = entidadeSup)
        val entidade1=XMLEntity(xmlEntityName = "entidade1", parentXMLEntity = entidadeSup)
        val entidade2=XMLEntity(xmlEntityName = "entidade2", parentXMLEntity = entidade1)
        val entidade3=XMLEntity(xmlEntityName = "entidade3", parentXMLEntity = entidade1)
        val entidade4=XMLEntity(xmlEntityName = "entidade4", parentXMLEntity = entidade3)
        val entidade5=XMLEntity(xmlEntityName = "entidade5", parentXMLEntity = entidade3)
        val atributo0=XMLAttribute(name = "atributo0", value = "valor0")
        val atributo1=XMLAttribute(name = "atributo1", value = "valor1")
        entidade0.addXMLAttribute(xmlAttributeToAdd = atributo0)
        entidade1.addXMLAttribute(xmlAttributeToAdd = atributo0)
        entidade0.setXMLEntityText(newXMLEntityText = "texto0")
        entidade2.setXMLEntityText(newXMLEntityText = "texto2")
        entidade2.addXMLAttribute(xmlAttributeToAdd = atributo1)
        entidade5.addXMLAttribute(xmlAttributeToAdd = atributo1)
        entidade4.setXMLEntityText(newXMLEntityText = "texto4")
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
                "</entidadeSup>",documento.prettyPrint)

    }

    @Test
    fun writeToFile(){
        val documento=XMLDocument(xmlDocumentName = "documento")
        val entidadeSup=XMLEntity(xmlEntityName = "entidadeSup",documento)
        val entidade0=XMLEntity(xmlEntityName = "entidade0",entidadeSup)
        val entidade1=XMLEntity(xmlEntityName = "entidade1",entidadeSup)
        val entidade2=XMLEntity(xmlEntityName = "entidade2",entidade1)
        val entidade3=XMLEntity(xmlEntityName = "entidade3",entidade1)
        val entidade4=XMLEntity(xmlEntityName = "entidade4",entidade3)
        val entidade5=XMLEntity(xmlEntityName = "entidade5",entidade3)
        val atributo0=XMLAttribute(name = "atributo0", value = "valor0")
        val atributo1=XMLAttribute(name = "atributo1", value = "valor1")
        entidade0.addXMLAttribute(xmlAttributeToAdd = atributo0)
        entidade1.addXMLAttribute(xmlAttributeToAdd = atributo0)
        entidade0.setXMLEntityText(newXMLEntityText = "texto0")
        entidade2.setXMLEntityText(newXMLEntityText = "texto2")
        entidade2.addXMLAttribute(xmlAttributeToAdd = atributo1)
        entidade5.addXMLAttribute(xmlAttributeToAdd = atributo1)
        entidade4.setXMLEntityText(newXMLEntityText = "texto4")
        val testFilePath="src${File.separatorChar}test${File.separatorChar}Testfiles${File.separatorChar}test_file0.xml"
        val testFile = File(testFilePath)

        val filePath0="src${File.separatorChar}test${File.separatorChar}Testfiles${File.separatorChar}file0.xml"
        val filePath1="src${File.separatorChar}test${File.separatorChar}Testfiles${File.separatorChar}file1.xml"
        val filePath2="src${File.separatorChar}test${File.separatorChar}Testfiles${File.separatorChar}"
        val filePath3="src${File.separatorChar}test${File.separatorChar}Testfiles"
        val file0 = File(filePath0)
        val file1 = File(filePath1)
        documento.writeToFile(file_path = filePath0)
        documento.writeToFile(file = file1)
        documento.changeName(newXMLDocumentName = "file2")
        val file2 = File(documento.writeToFile(file_path = filePath2))
        documento.changeName(newXMLDocumentName = "file3")
        val file3 = File(documento.writeToFile(file_path = filePath3))
        assertEquals(file0.readLines(),testFile.readLines())
        assertEquals(file1.readLines(),testFile.readLines())
        assertEquals(file2.readLines(),testFile.readLines())
        assertEquals(file3.readLines(),testFile.readLines())
    }

    @Test
    fun addXMLAttributeGlobally(){
        val documento=XMLDocument(xmlDocumentName = "documento")
        val entidadeSup=XMLEntity(xmlEntityName = "entidadeSup",documento)
        val entidade0=XMLEntity(xmlEntityName = "entidade0",entidadeSup)
        val entidade1=XMLEntity(xmlEntityName = "entidade1",entidadeSup)
        val entidade2=XMLEntity(xmlEntityName = "entidade1",entidade1)
        val entidade3=XMLEntity(xmlEntityName = "entidade3",entidade1)
        val entidade4=XMLEntity(xmlEntityName = "entidade1",entidade3)
        val entidade5=XMLEntity(xmlEntityName = "entidade1",entidade3)
        documento.addXMLAttributeGlobally(xmlEntityName = "entidade1", xmlAttributeNameToAdd = "atributo0", xmlAttributeValueToAdd = "valor0")
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


}

