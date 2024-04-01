import java.io.File

class Document(
    val name:String,
    val version: String? = "1.0",
    val enconding: String? = "UTF-8"
): HasVisitor {
    val children: MutableList<Entidade> = mutableListOf<Entidade>()
    override fun accept(visitor: (HasVisitor) -> Boolean) {
        if(visitor(this))
            children.forEach {
                it.accept(visitor)
            }
    }

    fun addEntidade(entidade:Entidade): Boolean{
        children.add(entidade)
        return true
    }
    fun removeEntidade(entidade:Entidade): Boolean{
        if (children.contains(entidade)) {
            children.remove(entidade)
            return true
        }
        return false
    }

    fun getEntidades():List<Entidade>{
        return children
    }

    fun getPrettyPrint(): String{
        var str = "<?xml version=\"" + this.version + "\" encoding=\"" + this.enconding + "\"?>"
        children.forEach {
            str += "\n" + it.getString()
        }
        return str
    }

    fun writeToFile(file_path: String){
        val file = File(file_path)
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(getPrettyPrint())
    }

    fun addAtributoGlobally(nome_entidade:String, nome_atributo:String, valor_atributo:String){
        var atributo:Atributo=Atributo(nome_atributo,valor_atributo)
        this.accept {
            if (it is Entidade) it.addAtributo(atributo)
            true
        }
    }

    fun replaceEntidadeNameGlobal(nome_entidade: String,novo_nome_entidade:String){
        this.accept {
            if (it is Entidade && it.getNameEntidade().equals(nome_entidade)) it.setName(novo_nome_entidade)
            true
        }
    }

    //todo testes
    fun replaceAtributoNameGlobal(nome_entidade: String,nome_atributo_antigo: String,nome_atributo_novo: String ){
        this.accept {
            if (it is Entidade && it.getNameEntidade().equals(nome_entidade)) it.changeAtributoName(nome_atributo_antigo,nome_atributo_novo)
            true
        }
    }

    //todo testes
    fun removeAtributoGlobal(nome_entidade: String,nome_atributo: String){
        this.accept {
            if (it is Entidade && it.getNameEntidade().equals(nome_entidade)) it.removeAtributo(nome_atributo)
            true
        }
    }

    fun micro_XPath(path: String):MutableList<Entidade>{
        var entidadeWithPath:MutableList<Entidade> = mutableListOf()
        this.accept {
            if(it is Entidade){
                if(it.getPath().endsWith("path"))
                    entidadeWithPath.add(it)
            }
            true
        }
        return entidadeWithPath
    }
}