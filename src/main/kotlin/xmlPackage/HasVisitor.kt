package xmlPackage

interface HasVisitor {
    fun accept(visitor: (HasVisitor) -> Boolean) {
        visitor(this)
    }


}