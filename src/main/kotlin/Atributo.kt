class Atributo(
    var name:String,
    var value:String
) {
    fun changeName(name: String): Boolean{
        this.name=name
        return true
    }
    fun changeValue(value: String): Boolean{
        this.value=value
        return true
    }
}