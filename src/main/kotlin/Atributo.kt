class Atributo(
    val name:String,
    val value:String
): HasVisitor {
    override fun accept(visitor: (HasVisitor) -> Boolean) {
        visitor(this)
    }
}