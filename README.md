<h1>Biblioteca de Manipulação de XML</h1>
<h2>Projeto de Programação Avançada 2023/2024</h2>
<h3>Docente: André Santos<br>
Alunos: Gonçalo Serrano e Nicole Nunes</h3>

<h3>Objetivo</h3>
O Objetivo desta biblioteca é a criação de um ficheiro XML através de objetos em memória, conseguindo manipular o seu conteúdo.
Tambem possuí a funcionalidade de transformar um objeto de qualquer tipo numa entidade XML usando o conceito de reflexão, sendo assim mais facil a criação de entidades.

<h3>Arquitetura</h3>
Para este projeto foram criadas três classes de objetos, 
XMLDocument, XMLEntity e XMLAttribute que podem estar ligados entre si da seguinte forma:
![img.png](img.png)
<ul><b>XMLDocument:</b> É contruído dando o nome do mesmo, e, pode tambem ser definida a versão e o encoding do XML, tento como default version="1.0" e encoding="UTF-8". Pode ter no máximo uma entitade diretamente definida no mesmo.</ul>
<ul><b>XMLEntity:</b> É contruída dando um nome, e pode tambem ser definida a instancia a que pertence(XMLDocument ou XMLEntity). Pode ter um texto ou uma lista de XMLEntitades dentro da mesma. Pode tambem ter vários XMLAttributes</ul>
<ul><b>XMLAttribute:</b> É construído dando um nome e um valor ao mesmo.</ul>

As classes XMLDocument e XMLEntity implementam a interface Visitor que facilita fazer um percurso pela estrutura do documento podendo assim fazer manipulações às entidades globalmente.

<h3>Exemplo Geral:</h3>
Isto é um exemplo daquilo que pode ser escrito no documento através desta biblioteca:
<br>
```kotlin
<?xml version="1.0" encoding="UTF-8"?>
<plano>
    <curso>Mestrado em Engenharia Informática</curso>
    <fuc codigo="M4310">
        <nome>Programação Avançada</nome>
        <ects>6.0</ects>
        <avaliacao>
            <componente nome="Quizzes" peso="20%"/>
            <componente nome="Projeto" peso="80%"/>
        </avaliacao>
    </fuc>
    <fuc codigo="03782">
        <nome>Dissertação</nome>
        <ects>42.0</ects>
        <avaliacao>
            <componente nome="Dissertação" peso="60%"/>
            <componente nome="Apresentação" peso="20%"/>
            <componente nome="Discussão" peso="20%"/>
        </avaliacao>
    </fuc>
</plano>
```

<h3>Tutorial</h3>
Para criar estas estruturas podemos seguir os seguintes exemplos:
<h4>Criar XMLDocument</h4>
Podemos criar apenas com o nome, ou se quisermos ser mais especificos com o resto dos parametros
```kotlin
val documentDefault = XMLDocument(xmlDocumentName = "ficheiro.xml")
val document = XMLDocument(xmlDocumentName = "ficheiro.xml", version = "1.0", encoding = "UTF-8")
```
<h4>Criar XMLEntity</h4>
```kotlin
val fuc = XMLEntity(name = "plano", xmlDocumentName = documentDefault)
val curso = XMLEntity(name = "curso", parentXMLEntity = entityWithParent)
val orphan = XMLEntity(name = "Clara")
```
<h4>Criar XMLAttribute</h4>
Podemos criar um atributo e adicionar à entidade, ou faze-lo diretamente na entitade
```kotlin
val codigo = XMLAttribute(name = "codigo", value = "M4310")
fuc.add(xmlAttributeToAdd = codigo)

componente.add(xmlAttributeNameToAdd = "peso", xmlAttributeValueToAdd = "20%")
```
<h4>PrettyPrint</h4>
A qualquer momento podemos chamar o previsualizar como o documento irá ficar apenas chamando este metodo
```kotlin
val pequenaPreview:String = componente.prettyPrint
val documentoPreview:String = document.prettyPrint
```
<h4>Write to file</h4>
A qualquer momento podemos escrever o ficheiro, passando o path desejado ou o File onde queremos escrever
```kotlin
documento.writeToFile(file = FicheiroInstanciado)
documento.writeToFile(file_path = "workspace/temp/dir")
```
<h4>Ver o Path</h4>
A qualquer momento podemos ver o path completo de uma dada entitdade
```kotlin
val path:String = componente.getPath
//neste caso iria devolver:
// plano/fuc/avaliacao/componente
```

<h4>microXPath</h4>
A qualquer momento podemos ver todas as entidades que partilham de um certo path
```kotlin
val path:String = componente.getPath
val sisters:List<XMLEntity> = documento.microXPath(path)
//neste caso iria devolver uma lista com todas as compenentes que partilham do mesmo path que componente.
```

<h4>Outras operações importantes</h4>
```kotlin
//ir buscar as entidades diretamente dentro de uma dada entidade
val filhos: MutableList<XMLEntity> = avaliacao.getChildEntities

//ir buscar os atributos diretamente dentro de uma dada entidade
val atributos: List<XMLAttribute> = componente.getAttributes

//ir buscar o texto definido dentro de uma dada entidade
val texto: String = curso.getText

//adicionar uma entidade dentro de outra (mesma forma para atributos)
father.add(orphan)

//remover uma entidade de dentro de outra
child.removeParent()
father.remove(orphan) //mesma forma para remover atributos

//remover ou adicionar varios de uma vez
father.addAll(mutableListOf<XMLEntity>(olderChild, middleChild,youngerChild))
father.removeAll(mutableListOf<XMLAttribute>(nome, idade))

//alterar atributos de uma entidade
pessoa.changeXMLAttributeName(oldXMLAttributeName = "idade", newXMLAttributeName = "age")
pessoa.changeXMLAttributeValue(xmlAttributeName = "age", newXMLAttributeValue = "18")
pessoa.replaceXMLAttribute(oldXMLAttribute = faculdade, newXMLAttribute = trabalho)

//alterações globais atraves do documento
documento.addXMLAttributeGlobally(xmlEntityName = "cabelo_escuro", xmlAttributeNameToAdd = "cor de cabelo", xmlAttributeValueToAdd = "escuro")
documento.replaceXMLEntityNameGlobally(oldXMLEntityName = "cabelo_escuro", newXMLEntityName = "morenos")
documento.replaceXMLAttributeNameGlobally(xmlEntityName = "morenos", oldXMLAttributeName = "cor de cabelo", newXMLAttributeName = "cabelo" )
documento.removeXMLAttributeGlobally(xmlEntityName = morenos, xmlAttributeNameToRemove = "cabelo")
```

<h3>DSL</h3>
Foi criada uma DSL para facilitar a criação de documentos
Pode ser criado uma pequena estrutura da seguinte forma:















