
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

@XmlAdapter(FUCAdapter::class)
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
    @OverrideName("Componente Principal")
    @IsEntity
    val compenenteAuxiliar: ComponenteAvaliacao
)

class AddPercentage : StringAdapterInterface {
    override fun adaptString(stringToAdapt: String): String {
        return "${stringToAdapt}%"
    }
}

class FUCAdapter (
    private val entity: XMLEntity
) {

}