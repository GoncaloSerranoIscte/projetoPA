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
}