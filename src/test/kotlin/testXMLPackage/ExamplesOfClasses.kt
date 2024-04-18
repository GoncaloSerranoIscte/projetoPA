import xmlPackage.Ignore
import xmlPackage.IsAttribute
import xmlPackage.IsEntity
import xmlPackage.OverrideName

@OverrideName(name = "Maça")
class Maca()

class Laranja()

@OverrideName(name = "componente")
class ComponenteAvaliacao (
    @IsAttribute
    val nome: String,
    @IsAttribute
    val peso: Int
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
