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
}