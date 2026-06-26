package io.navivani.variables

/**
 * Project: Variables
 * From: io.navivani.variables
 * Created by: navivani
 * On: 6/25/26
 * All rights reserved: 2026
 */

// no tengo idea si vimos data classes
// falte un dia y no se que tanto se vio en clase
// voy a asumir que si se vio para que sea muchisimo mas facil hacer lo de participantes :)
data class ParticipanteTorneo(
    var nombre: String,
    var puntos:Int = 0,
    var categoria:String = CAT_NOVATO
)
val CAT_LEYENDA = "Leyenda"
val CAT_EXPERTO = "Experto"
val CAT_COMPETIDOR = "Competidor"
val CAT_NOVATO = "Novato"

fun main() {
    val participantesTorneito = mutableListOf<ParticipanteTorneo>()
    while(true) {
        menuInicial()
        val opcion = readln()
        when (opcion) {
            "1" -> registrarParticipante(participantesTorneito)
            "2" -> registrarPuntos(participantesTorneito)
            "3" -> checarParticipante(participantesTorneito)
            "4" -> mostrarEstadisticas(participantesTorneito)
            "5" -> { if (cerrarApp()) return}
            else -> println("opcion no valida, ingresa un numero :<")
        }
    }
}

fun menuInicial() {
    println("""
        TORNEO DE JUEGOS 2026 TWITCH:
        1 - Registrar participante
        2 - Registrar puntos
        3 - Consultar participante
        4 - Ver estadisticas
        5 - Cerrar
        que quieres hacer?? :
    """.trimIndent())
}

fun registrarParticipante(participantes:MutableList<ParticipanteTorneo>) {
    while (true ){
        println("ingresa el nombre del nuevo participante : ")
        var participanteNuevo = readln()
        if (participanteNuevo.isEmpty()) {
            println("ingresa un nombre valido!")
        } else if (existeParticipante(participanteNuevo, participantes)) {
            println("ese participante ya existe! ingresa uno nuevo")
        } else {
            participantes.add(ParticipanteTorneo(participanteNuevo))
            println("participante registrado!")
            return
        }
    }
}

fun registrarPuntos(participantes:MutableList<ParticipanteTorneo>) {
    if (participantes.isEmpty()) {
        println("registra al menos 1 participante!!!")
        return
    }
    var participante = buscarParticipante(participantes)

    var puntosInvalidos = true
    var puntos = 0
    do {
        println("ingresa el puntaje a añadir : ")
        var pt = readln().toIntOrNull()
        if (pt == null) {
            println("puntaje no valido!!")
        } else if (pt <= 0) {
            println("los puntos tienen que ser mayores a 0")
        } else {
            puntosInvalidos = false
            puntos = pt
        }
    } while (puntosInvalidos)

    participante.puntos += puntos
    participante.categoria = obtenerCategoria(participante.puntos)
    println("se añadieron $puntos puntos al participante $participante.nombre")
}


fun checarParticipante(participantes:MutableList<ParticipanteTorneo>) {
    if (participantes.isEmpty()) {
        println("registra al menos 1 participante!!!")
        return
    }
    val participante = buscarParticipante(participantes)
    println("informacion del participante!")
    println("nombre: $participante.nombre")
    println("puntos: $participante.puntos")
    println("categoria: ${participante.categoria}")
    println("presiona enter para continuar...")
    readln()
}

fun mostrarEstadisticas(participantes: MutableList<ParticipanteTorneo>) {
    if (participantes.isEmpty()) {
        println("no hay participantes registrados todavia!!!")
        return
    }

    val totalPuntos = participantes.sumOf { it.puntos }
    val promedio = totalPuntos.toDouble() / participantes.size
    val lider = participantes.maxBy { it.puntos }
    val ultimo = participantes.minBy { it.puntos }

    val porCategoria = participantes.groupBy { it.categoria }

    println("""
        === ESTADISTICAS DEL TORNEO ===
        participantes totales : ${participantes.size}
        puntos totales        : $totalPuntos
        promedio de puntos    : ${"%.2f".format(promedio)}
        lider del torneo      : ${lider.nombre} (${lider.puntos} pts)
        participante base     : ${ultimo.nombre} (${ultimo.puntos} pts)
        
        === PARTICIPANTES POR CATEGORIA ===
        $CAT_LEYENDA   : ${porCategoria[CAT_LEYENDA]?.size ?: 0}
        $CAT_EXPERTO   : ${porCategoria[CAT_EXPERTO]?.size ?: 0}
        $CAT_COMPETIDOR: ${porCategoria[CAT_COMPETIDOR]?.size ?: 0}
        $CAT_NOVATO    : ${porCategoria[CAT_NOVATO]?.size ?: 0}
    """.trimIndent())

    println("presiona enter para continuar...")
    readln()
}

fun buscarParticipante(listaParticipantes: MutableList<ParticipanteTorneo>): ParticipanteTorneo {
    while(true) {
        println("ingresa el nombre del participante: ")
        val participanteNombre = readln()
        val participante = listaParticipantes.find {it.nombre == participanteNombre}

        if (participanteNombre.isEmpty()) {
            println("ingresa un nombre valido!!!")
        } else if (participante == null) {
            println("el participante no existe!!")
        } else return participante
    }
}

fun cerrarApp():Boolean {
    while (true) {
        println("de verdad quieres salir? :< \n S/N:")
        val res = readln().uppercase()
        if (res == "N") {
            println("okay :>")
            return false
        } else if (res == "Y") {
            println("baii :(")
            return true
        } else {
            println("siono... \n S/N:")
        }
    }
}


// helpers
fun existeParticipante(participante: String, listaParticipantes:MutableList<ParticipanteTorneo>):Boolean {
    return listaParticipantes.find { it.nombre == participante } != null
}

fun obtenerCategoria(puntaje:Int): String {
    if (puntaje >= 1000) return CAT_LEYENDA
    else if (puntaje >= 500) return CAT_EXPERTO
    else if (puntaje >=  200) return CAT_COMPETIDOR
    else return CAT_NOVATO
}