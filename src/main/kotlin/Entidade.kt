class Entidade (
    val name:String,
    val document: Document?,
    val parentEntidade: Entidade
): HasVisitor {
    val children: MutableList<Entidade> = mutableListOf<Entidade>()
    val atributos: MutableList<Atributo> = mutableListOf<Atributo>()
    val texto: String = ""
    override fun accept(visitor: (HasVisitor) -> Boolean) {
        if(visitor(this))
            children.forEach {
                it.accept(visitor)
            }
    }
}