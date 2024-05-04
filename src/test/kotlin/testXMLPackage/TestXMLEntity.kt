package testXMLPackage

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import xmlPackage.XMLAttribute
import xmlPackage.XMLDocument
import xmlPackage.XMLEntity

class TestXMLEntity {

    @Test
    fun getName(){
        val xmlEntity = XMLEntity(xmlEntityName = "XMLEntityName")
        assertEquals("XMLEntityName",xmlEntity.getName)
    }
    @Test
    fun getParent(){
        val xmlDocument = XMLDocument(xmlDocumentName = "XMLDocumentName")
        val xmlEntity = XMLEntity(xmlEntityName = "XMLEntityName", parentXMLDocument = xmlDocument)
        val xmlEntity1 = XMLEntity(xmlEntityName = "XMLEntity1Name", parentXMLEntity = xmlEntity)
        val xmlEntity2 = XMLEntity(xmlEntityName = "XMLEntity2Name")
        assertEquals(xmlDocument, xmlEntity.getParent)
        assertEquals(xmlEntity, xmlEntity1.getParent)
        assertEquals(null, xmlEntity2.getParent)
    }
    @Test
    fun getChildEntities(){
        val xmlDocument = XMLDocument(xmlDocumentName = "XMLDocumentName")
        val xmlEntity = XMLEntity(xmlEntityName = "XMLEntityName", parentXMLDocument = xmlDocument)
        val xmlEntity1 = XMLEntity(xmlEntityName = "XMLEntity1Name", parentXMLEntity = xmlEntity)
        val xmlEntity2 = XMLEntity(xmlEntityName = "XMLEntity2Name", parentXMLEntity = xmlEntity)
        assertEquals(mutableListOf(xmlEntity1,xmlEntity2),xmlEntity.getChildEntities)
        assertEquals(mutableListOf<XMLEntity>(),xmlEntity1.getChildEntities)
    }
    @Test
    fun getAttributes(){
        val xmlEntity = XMLEntity(xmlEntityName = "XMLEntityName")
        val xmlAttibute = XMLAttribute(name = "AttributeName", value = "AttributeValue")
        xmlEntity.add(xmlAttibute)
        val xmlEntity0 = XMLEntity(xmlEntityName = "XMLEntity0Name")
        assertEquals(xmlAttibute,xmlEntity.getAttributes[0])
        assertEquals(0,xmlEntity0.getAttributes.size)
    }
    @Test
    fun getText(){
        val xmlEntity = XMLEntity(xmlEntityName = "XMLEntityName")
        xmlEntity.setText(newXMLEntityText = "new_text")
        val xmlEntity0 = XMLEntity(xmlEntityName = "XMLEntity0Name")
        assertEquals("new_text",xmlEntity.getText)
        assertEquals("", xmlEntity0.getText)
    }
    @Test
    fun hasChildren(){
        val xmlEntity = XMLEntity(xmlEntityName = "XMLEntityName")
        val xmlEntity0 = XMLEntity(xmlEntityName = "XMLEntity0Name",xmlEntity)
        assertEquals(true,xmlEntity.hasChildren)
        assertEquals(false, xmlEntity0.hasChildren)
    }
    @Test
    fun hasText(){
        val xmlEntity = XMLEntity(xmlEntityName = "XMLEntityName")
        xmlEntity.setText(newXMLEntityText = "new_text")
        val xmlEntity0 = XMLEntity(xmlEntityName = "XMLEntity0Name")
        assertEquals(true,xmlEntity.hasText)
        assertEquals(false, xmlEntity0.hasText)
    }

    @Test
    fun addXMLEntityChild(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val entidade1= XMLEntity(xmlEntityName = "curso")
        entidade0.add(xmlEntityToAdd = entidade1)
        assertEquals(mutableListOf(entidade1),entidade0.getChildEntities)
        assertEquals(mutableListOf<XMLEntity>(), entidade1.getChildEntities)
    }

    @Test
    fun addAllXMLEntitiesChildren(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val entidade1= XMLEntity(xmlEntityName = "curso")
        val entidade2= XMLEntity(xmlEntityName = "curso")
        entidade0.addAll(xmlElementsToAdd = mutableListOf(entidade1,entidade2))
        assertEquals(mutableListOf(entidade1,entidade2),entidade0.getChildEntities)
        assertEquals(mutableListOf<XMLEntity>(), entidade1.getChildEntities)
    }


    @Test
    fun removeXMLEntityChild(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val entidade1= XMLEntity(xmlEntityName = "curso", parentXMLEntity = entidade0)
        val entidade2= XMLEntity(xmlEntityName = "name", parentXMLEntity = entidade1)
        assertEquals(mutableListOf(entidade1),entidade0.getChildEntities)
        assertEquals(entidade0,entidade1.getEntityParent)
        entidade0.remove(xmlEntityToRemove = entidade1)
        assertEquals(mutableListOf<XMLEntity>(),entidade0.getChildEntities)
        assertEquals(mutableListOf(entidade2),entidade1.getChildEntities)
        assertEquals(null,entidade1.getEntityParent)
        assertEquals(entidade1,entidade2.getEntityParent)
    }
    @Test
    fun removeAllXMLEntitiesChildren(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val entidade1= XMLEntity(xmlEntityName = "curso", parentXMLEntity = entidade0)
        val entidade2= XMLEntity(xmlEntityName = "name", parentXMLEntity = entidade0)
        assertEquals(mutableListOf(entidade1,entidade2),entidade0.getChildEntities)
        assertEquals(entidade0,entidade1.getEntityParent)
        assertEquals(entidade0,entidade2.getEntityParent)
        entidade0.removeAll(xmlElementsToRemove = mutableListOf(entidade1,entidade2))
        assertEquals(mutableListOf<XMLEntity>(),entidade0.getChildEntities)
        assertEquals(null,entidade1.getEntityParent)
        assertEquals(null,entidade2.getEntityParent)
    }

    @Test
    fun setXMLEntityName(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        assertEquals("plano",entidade0.getName)
        entidade0.setName(newXMLEntityName = "plano2")
        assertEquals("plano2",entidade0.getName)
    }

    @Test
    fun setXMLEntityText(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val entidade1= XMLEntity(xmlEntityName = "plano1", parentXMLEntity = entidade0)
        entidade0.setText(newXMLEntityText = "texto")
        entidade1.setText(newXMLEntityText = "texto1")
        assertEquals("texto1", entidade1.getText)
        assertEquals("", entidade0.getText)
    }

    @Test
    fun removeXMLEntityText(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        entidade0.setText(newXMLEntityText = "texto")
        assertEquals("texto", entidade0.getText)
        entidade0.removeText()
        assertEquals("", entidade0.getText)
    }

    @Test
    fun removeXMLParent(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val entidade1= XMLEntity(xmlEntityName = "plano1", parentXMLEntity = entidade0)
        assertEquals(entidade0,entidade1.getParent)
        assertEquals(mutableListOf(entidade1),entidade0.getChildEntities)
        entidade1.removeParent()
        assertEquals(null,entidade1.getParent)
        assertEquals(mutableListOf<XMLEntity>(),entidade0.getChildEntities)
    }

    @Test
    fun addXMLAttribute(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val atributo0= XMLAttribute(name = "dia", value = "Sabado")
        entidade0.add(xmlAttributeToAdd = atributo0)
        entidade0.add(xmlAttributeNameToAdd = "mes", xmlAttributeValueToAdd = "janeiro")
        val atributo2= XMLAttribute(name = "dia", value = "Domingo")
        entidade0.add(xmlAttributeNameToAdd = "mes", xmlAttributeValueToAdd = "fevereiro")
        assertEquals(false,entidade0.getAttributes.contains(atributo2))
        assertEquals(2, entidade0.getAttributes.size)
        //assertNull(atributo3)
    }

    @Test
    fun addAllXMLAttribute(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val atributo0= XMLAttribute(name = "dia", value = "Sabado")
        val atributo1= XMLAttribute(name = "mes", value = "janeiro")
        entidade0.addAll(xmlElementsToAdd = mutableListOf(atributo0,atributo1))
        assertEquals(mutableListOf(atributo0,atributo1), entidade0.getAttributes)
    }

    @Test
    fun removeXMLAttribute(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val atributo0= XMLAttribute(name = "dia", value = "Sabado")
        val atributo1= XMLAttribute(name = "mes", value = "janeiro")
        entidade0.addAll(xmlElementsToAdd = mutableListOf(atributo0,atributo1))
        assertEquals(mutableListOf(atributo0,atributo1), entidade0.getAttributes)
        entidade0.remove(xmlAttributeToRemove = atributo0)
        assertEquals(mutableListOf(atributo1), entidade0.getAttributes)
        entidade0.remove(xmlAttributeNameToRemove = "mes")
        assertEquals(mutableListOf<XMLAttribute>(), entidade0.getAttributes)
    }
    @Test
    fun removeAllXMLAttributes(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val atributo0= XMLAttribute(name = "dia", value = "Sabado")
        val atributo1= XMLAttribute(name = "mes", value = "janeiro")
        val atributo2= XMLAttribute(name = "ano", value = "2015")
        entidade0.addAll(xmlElementsToAdd = mutableListOf(atributo0,atributo1,atributo2))
        assertEquals(mutableListOf(atributo0,atributo1,atributo2), entidade0.getAttributes)
        entidade0.removeAll(xmlElementsToRemove = mutableListOf(atributo0,atributo1))
        assertEquals(mutableListOf(atributo2), entidade0.getAttributes)
    }

    @Test
    fun changeXMLAttributeName(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val atributo0= XMLAttribute(name = "dia", value = "Sabado")
        val atributo1= XMLAttribute(name = "mes", value = "Abril")
        entidade0.addAll(xmlElementsToAdd = mutableListOf(atributo0,atributo1))
        entidade0.changeXMLAttributeName(oldXMLAttributeName = "dia", newXMLAttributeName = "dia da semana")
        assertEquals("dia_da_semana",entidade0.getAttributes[0].getName)
        assertEquals("mes",entidade0.getAttributes[1].getName)
    }

    @Test
    fun changeXMLAttributeValue(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val atributo0= XMLAttribute(name = "dia", value = "Sabado")
        val atributo1= XMLAttribute(name = "mes", value = "Abril")
        entidade0.addAll(xmlElementsToAdd = mutableListOf(atributo0,atributo1))
        entidade0.changeXMLAttributeValue(xmlAttributeName =  "dia", newXMLAttributeValue = "Domingo")
        assertEquals("Domingo",entidade0.getAttributes[0].getValue)
        assertEquals("Abril",entidade0.getAttributes[1].getValue)
    }
    @Test
    fun replaceXMLAttribute(){
        val entidade0= XMLEntity(xmlEntityName = "plano")
        val atributo0= XMLAttribute(name = "dia", value = "Sabado")
        val atributo1= XMLAttribute(name = "mes", value = "Abril")
        entidade0.add(xmlAttributeToAdd = atributo0)
        assertEquals(atributo0,entidade0.getAttributes[0])
        entidade0.replaceXMLAttribute(oldXMLAttribute = atributo0, newXMLAttribute = atributo1)
        assertEquals(atributo1,entidade0.getAttributes[0])
    }


    @Test
    fun prettyPrint(){
        val entidade0= XMLEntity(xmlEntityName = "entidade0")
        val atributo0= XMLAttribute(name = "atributo0", value = "valor0")
        entidade0.add(atributo0)

        assertEquals("<entidade0 atributo0=\"valor0\"/>", entidade0.prettyPrint)
        entidade0.setText(newXMLEntityText = "texto0")
        assertEquals("<entidade0 atributo0=\"valor0\">texto0</entidade0>", entidade0.prettyPrint)

        val entidade1= XMLEntity(xmlEntityName = "entidade1")
        val entidade2= XMLEntity(xmlEntityName = "entidade2",entidade1)
        val entidade3= XMLEntity(xmlEntityName = "entidade3",entidade1)
        val entidade4= XMLEntity(xmlEntityName = "entidade4",entidade3)
        val atributo1= XMLAttribute(name = "atributo1", value = "valor1")
        entidade2.setText(newXMLEntityText = "texto2")
        entidade2.add(xmlAttributeToAdd = atributo1)
        entidade4.setText(newXMLEntityText = "texto4")
        assertEquals(
            "<entidade1>\n" +
                    "\t<entidade2 atributo1=\"valor1\">texto2</entidade2>\n" +
                    "\t<entidade3>\n" +
                    "\t\t<entidade4>texto4</entidade4>\n" +
                    "\t</entidade3>\n" +
                    "</entidade1>", entidade1.prettyPrint
        )
    }

    @Test
    fun testgetPath(){
        val entidadeSup= XMLEntity(xmlEntityName = "entidadeSup")
        val entidade1= XMLEntity(xmlEntityName = "entidade1",entidadeSup)
        val entidade3= XMLEntity(xmlEntityName = "entidade4",entidade1)
        val entidade5= XMLEntity(xmlEntityName = "entidade1",entidade3)
        assertEquals("entidadeSup",entidadeSup.getPath)
        assertEquals("entidadeSup/entidade1",entidade1.getPath)
        assertEquals("entidadeSup/entidade1/entidade4",entidade3.getPath)
        assertEquals("entidadeSup/entidade1/entidade4/entidade1",entidade5.getPath)
    }

}