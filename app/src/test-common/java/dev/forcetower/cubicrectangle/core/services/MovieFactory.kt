package dev.forcetower.cubicrectangle.core.services

import dev.forcetower.cubicrectangle.model.database.Genre
import dev.forcetower.cubicrectangle.model.dto.MovieDetailed
import dev.forcetower.cubicrectangle.model.dto.MovieSimple
import dev.forcetower.cubicrectangle.model.dto.ReleaseCountry
import dev.forcetower.cubicrectangle.model.dto.ReleaseDate
import dev.forcetower.cubicrectangle.model.dto.ReleaseDates
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

object MovieFactory {
    private val counter = AtomicInteger(0)
    fun createMovieSimple(
        name: String = "movie",
        genres: String = "1,2,3"
    ): MovieSimple {
        return MovieSimple(
            Random.nextDouble(),
            Random.nextLong(),
            Random.nextBoolean(),
            null,
            counter.toLong(),
            Random.nextBoolean(),
            null,
            "pt-BR",
            "$name $counter",
            genres.split(",").map { it.toLong() },
            "$name",
            Random.nextDouble(),
            null,
            null
        )
    }

    fun createMovieDetailed(): MovieDetailed {
        return MovieDetailed(
            315635,
            false,
            "/vc8bCGjdVp0UbMNLzHnHSLRbBWQ.jpg",
            null,
            175000000,
            listOf(Genre(1L, "Ação"), Genre(2L, "Aventura")),
            "http://marvel.com/spidermanhomecoming",
            "tt2250912",
            "en",
            "Spider-Man: Homecoming",
            "Depois de atuar ao lado dos Vingadores, chegou a hora do pequeno Peter Parker (Tom Holland) voltar para casa e para a sua vida, já não mais tão normal. Lutando diariamente contra pequenos crimes nas redondezas, ele pensa ter encontrado a missão de sua vida quando o terrível vilão Abutre (Michael Keaton) surge amedrontando a cidade. O problema é que a tarefa não será tão fácil como ele imaginava.",
            31.337,
            "/9Fgs1ewIZiBBTto1XDHeBN0D8ug.jpg",
            null,
            null,
            LocalDate.parse("2017-07-05"),
            880166924,
            133,
            null,
            "Released",
            "O dever de casa pode esperar. A cidade não.",
            "Homem-Aranha: De Volta ao Lar",
            false,
            7.4,
            13356,
            ReleaseDates(listOf(
                ReleaseCountry(
                    "BR",
                    listOf(
                        ReleaseDate("18+", "pt", null, 4, ZonedDateTime.now())
                    )
                )
            ))
        )
    }
}