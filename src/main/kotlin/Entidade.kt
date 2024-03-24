import java.util.StringJoiner

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
    fun removeAtributo(atributo:Atributo): Boolean{
        if (atributos.contains(atributo)){
            atributos.remove(atributo)
            return true
        }
        return false
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
}