import java.io.File

public class Entidade private constructor(
    var name:String,
    var document: Document?=null,
    var parentEntidade: Entidade?=null
): HasVisitor {
    val children: MutableList<Entidade> = mutableListOf<Entidade>()
    val atributos: MutableList<Atributo> = mutableListOf<Atributo>()
    var texto: String = ""

    constructor(name: String, document: Document) : this(name, document, null)

    constructor(name: String, parentEntidade: Entidade) : this(name, null, parentEntidade)

    init {
        document?.addEntidade(this)
        parentEntidade?.addEntidade(this)
    }

    override fun accept(visitor: (HasVisitor) -> Boolean) {
        if(visitor(this))
            children.forEach {
                it.accept(visitor)
            }
    }

    fun getNameEntidade():String{
        return name
    }
    fun addEntidade(entidade: Entidade): Boolean{
        if (texto.equals("")) {
            children.add(entidade)
            return true
        }
        return false
    }
    fun removeEntidade(entidade:Entidade): Boolean{
        if (children.contains(entidade)) {
            children.remove(entidade)
            entidade.removeParent()
            return true
        }
        return false
    }

    fun setName(name: String): Boolean{
        this.name=name
        return true
    }

    fun setTexto(texto: String): Boolean{
        if (children.size==0) {
            this.texto = texto
            return true
        }
        return false
    }

    fun removeTexto(): Boolean{
        if(texto!=""){
            texto=""
            return true
        }
        return false
    }

    fun removeParent() {
        if(parentEntidade!= null) parentEntidade = null
        if(document!= null) document = null
    }

    fun addAtributo(atributo: Atributo): Boolean{
        atributos.add(atributo)
        return true
    }

    //todo testes
    fun addAtributo(name: String, value:String): Boolean{
        atributos.add(Atributo(name,value))
        return true
    }
    fun removeAtributo(atributo:Atributo): Boolean{
        if (atributos.contains(atributo)){
            atributos.remove(atributo)
            return true
        }
        return false
    }

    //todo testes
    fun removeAtributo(name:String): Boolean{
        var atributos_to_remove:MutableList<Atributo> = mutableListOf()
        atributos.forEach {
            if(it.name.equals(name))
                atributos_to_remove.add(it)
        }
        if (atributos_to_remove.size>0){
            atributos_to_remove.forEach {
                atributos.remove(it)
            }
            return true
        }
        return false
    }

    //todo testes
    fun changeAtributoName(nome_antigo:String,nome_novo:String):Boolean{
        atributos.forEach {
            if(it.name.equals(nome_antigo))
                it.changeName(nome_novo)
        }
        return true
    }

    //todo testes
    fun changeAtributoValue(nome:String,valor:String):Boolean{
        atributos.forEach {
            if(it.name.equals(nome))
                it.changeValue(valor)
        }
        return true
    }

    fun replaceAtributo(atributo_atual: Atributo, atributo_novo: Atributo): Boolean{
        if (atributos.contains(atributo_atual)){
            atributos.remove(atributo_atual)
            atributos.add(atributo_novo)
            return true
        }
        return false
    }
    fun getEntidades():List<Entidade>{
        return children
    }
    fun getEntidadeParent(): Entidade? {
        return parentEntidade
    }

    fun getString(depth: Int = 0):String{
        var str = ""
        str += "\t".repeat(depth) + "<"+this.name
        atributos.forEach{
            str += " ${it.name}=\"${it.value}\""
        }
        if(texto!=""){
            str += ">${this.texto}</${this.name}>"
        }else if (children.size>0){
            str += ">"
            children.forEach {
                str += "\n${it.getString(depth + 1)}"
            }
            str += "\n" + "\t".repeat(depth) + "</${this.name}>"
        }else{
            str += "/>"
        }
        return str
    }

    fun getPath():String{
        var path:String = ""
        fun getPathAux(entidade: Entidade) {
            if (entidade.getEntidadeParent() == null){
                return
            }
            else {
                path = "${entidade.getEntidadeParent()?.name}/" + path
                getPathAux(entidade.getEntidadeParent()!!)
            }
        }
        getPathAux(this)

        return path.removeSuffix("/")
    }

}