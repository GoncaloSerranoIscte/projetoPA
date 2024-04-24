
import xmlPackage.*

@OverrideName(name = "Maça")
class Maca()

class Laranja()

@OverrideName(name = "componente")
class ComponenteAvaliacao (
    @IsAttribute
    val nome: String,
    @XmlString(AddPercentage::class)
    @IsAttribute
    val peso: Int,
    @Ignore
    val ano:Int
)

@OverrideName(name = "fuc")
class FUC (
    @IsAttribute
    val codigo: String,
    @IsEntity
    val nome: String,
    @IsEntity
    val ects: Double,
    @Ignore
    val observacoes: String,
    @IsEntity
    val avaliacao: List<ComponenteAvaliacao>,
    @IsEntity
    val EntidadeComVariosAtributos: List<ComponenteAvaliacao>
)

class AddPercentage(
    private val text:String
){
    override fun toString():String{
        return "$text%"
    }
}