package testXMLPackage

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import xmlPackage.*

class TestDSL {

    @Test
    fun stuff(){
        val doc =
            document("GOD", version = "3.4") {
                entity("father") {
                    entity("firstChild"){
                        text("I Have a Text")
                    }
                    entity("secondChild") {
                        entity("firstGrandChild"){
                            attribute("firstBorn", "True")
                        }
                        text("I Dont Have a Text")
                    }
                }
                entity("orphan")
            }
        doc.getEntityChild!!["secondChild"] += attribute("firstBorn", "False")
        doc.getEntityChild!!["secondChild"] += "who_am_I"
        doc.getEntityChild!!["secondChild"] += entity("i_dont_know"){
            text("lol")
            attribute("do_i_Know","no")
        }
        assertEquals("<?xml version=\"3.4\" encoding=\"UTF-8\"?>\n" +
                "<father>\n" +
                "\t<firstChild>I Have a Text</firstChild>\n" +
                "\t<secondChild firstBorn=\"False\">\n" +
                "\t\t<firstGrandChild firstBorn=\"True\"/>\n" +
                "\t\t<who_am_I/>\n" +
                "\t\t<i_dont_know do_i_Know=\"no\">lol</i_dont_know>\n" +
                "\t</secondChild>\n" +
                "</father>",doc.prettyPrint)
        assertEquals(doc.getEntityChild!!["secondChild"]["firstGrandChild"]("firstBorn").getValue, "True")
    }
}