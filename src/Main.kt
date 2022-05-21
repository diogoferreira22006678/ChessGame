fun makeMenu(): String = "\nWelcome to DEISI Minesweeper\n\n" + "1 - Start New Game\n" + "0 - Exit Game\n"

fun game() {//para onde vai o código depois de o jogador introduzir o numero 1
    var contadorDelegenda = false;var mostrarLegenda = false ;var columns: Int? = 0 ;var lines: Int? = 0
    var mines: Int? = 0 ;val mostrarCor = true
    val tabuleiro: Array<Array<Pair<String, Boolean>>>
    askName()
    while (!contadorDelegenda) {
        println("Show legend (y/n)?")
        val legend = readLine() ?: ""
        if (mostraLegenda(legend) == true) {
            contadorDelegenda = true
            mostrarLegenda = true
        } else if (legend == "n" || legend == "N") {
            contadorDelegenda = true
            mostrarLegenda = false
        } else invalid()
    }
    var contadorDeLinhas = false
    while (!contadorDeLinhas) {
        println("How many lines?")
        lines = readLine()?.toIntOrNull()
        if (lines != null) {
            if (isNumberValid(lines.toString())) {
                if (lines in 4..9) {
                    contadorDeLinhas = true
                } else invalid()
            } else {
                invalid()
            }
        } else invalid()
    }
    var contadorDecolunas = false
    while (!contadorDecolunas) {
        println("How many columns?")
        columns = readLine()!!.toIntOrNull()
        if (columns != null) {
            if (columns in 4..9) {
                if (isNumberValid(columns.toString())) {
                    contadorDecolunas = true
                } else invalid()
            } else invalid()
        } else invalid()
    }
    var minesCorreta = false
    while (!minesCorreta) {
        println("How many mines (press enter for default value)?")
        mines = readLine()?.toIntOrNull()
        if (mines != null) {
            if (columns != null && lines != null) {
                if (isValidGameMinesConfiguration(columns, lines, mines)) {
                    minesCorreta = true
                }
            } else {
                invalid()
            }
        } else {
            mines = calculateNumMinesForGameConfiguration(lines!!, columns!!)
            minesCorreta = true
        }
    }
    if (columns != null && lines != null && mines != null) {
        tabuleiro = (createMatrixTerrain(lines, columns, mines, false))
       println(makeTerrain(tabuleiro,mostrarLegenda,false,false))

    mineswepper(lines, columns, mostrarLegenda, tabuleiro)
    }
}

fun mineswepper(lines: Int, columns: Int, mostrarLegenda: Boolean, tabuleiro: Array<Array<Pair<String, Boolean>>>){
    var inicialCord = Pair(0,0)
    do {

        var contadorDeCordenadas = false
        while (!contadorDeCordenadas) {
            println("Choose the Target cell (e.g 2D)")
            val cordenadas = readLine() ?: ""
            val cordenadasInt = getCoordinates(cordenadas)
            if (cordenadasInt != null) {

                if (isCoordinateInsideTerrain(cordenadasInt, columns, lines) && isMovementPValid(inicialCord, cordenadasInt)) {
                    revealMatrix(tabuleiro,cordenadasInt.first,cordenadasInt.second)
                    tabuleiro[cordenadasInt.first][cordenadasInt.second] = Pair("P",true)
                    tabuleiro[inicialCord.first][inicialCord.second] = Pair(" ",false)
                    contadorDeCordenadas = true
                    inicialCord = cordenadasInt
                    println(makeTerrain(tabuleiro, mostrarLegenda, false, true))
                } else invalid()
            } else invalid()
        }
        if(inicialCord == Pair(lines - 1,columns - 1)) {
            println(makeTerrain(tabuleiro, mostrarLegenda, false, true))
            println("You win the game!")
        }
    }while(inicialCord != Pair(lines,columns))
}

fun invalid() {
    print("Invalid response.\n\n")
}

fun askName() {
    var nomeCorreto = false
    while (!nomeCorreto) {
        println("Enter player name?")
        val name = readLine() ?: ""
        if (isNameValid(name)) {
            nomeCorreto = true
        } else {
            invalid()
        }
    }
}

fun isNameValid(name: String?, minLength: Int = 3): Boolean {
    if (name != null) {
        if (name.length >= minLength) {
            if (name[0].toUpperCase() == name[0]) {// Vê primeira maiuscula
                if (' ' in name) {
                    var count = 1
                    var nomeCorreto = false
                    while (name[count + 1] != ' ') {// vê minusculas do primeiro nome até ao caracter antes do espaço
                        if (name[count].toUpperCase() == name[count]) {
                            return false
                        }
                        count++
                    }
                    if (name.length > count) {
                        if (name[count + 2].toUpperCase() == name[count + 2]) {// Vê segunda maiscula
                            count += 3
                            do {
                                if (name[count].toUpperCase() != name[count]) { //vê minusculas do segundo nome ate ao fim
                                    nomeCorreto = true
                                } else {
                                    return false
                                }
                                count++
                            } while (count != name.length)
                        }
                    }
                    if (nomeCorreto) {
                        return true
                    }
                }
            }
            return false
        }
        return false
    } else {
        return false
    }
}

fun terrainLegenda(matrixTerrain: Array<Array<Pair<String, Boolean>>>) : String{
    var tabuleiro = ""
    for (linha in 0 until matrixTerrain.size) {
        if(linha == 0){
            tabuleiro += "    ${createLegend(matrixTerrain[linha].size)}    \n"
        }
        if(linha > 0){
            var countLinhas = 0
            tabuleiro += "   "
            do {
                tabuleiro += if (countLinhas != matrixTerrain[linha].size - 1){
                    "---+"
                }else{
                    "---"
                }
                countLinhas ++
            }while(countLinhas != matrixTerrain[linha].size)
            tabuleiro += "   \n"
        }
        tabuleiro +=  " ${linha + 1} "
        for (coluna in 0 until matrixTerrain[linha].size) {
            tabuleiro += if(matrixTerrain[linha][coluna].second){
                " ${matrixTerrain[linha][coluna].first} "
            }else{
                "   "
            }
            if (coluna != matrixTerrain[linha].size - 1){
                tabuleiro += "|"
            }
        }
        tabuleiro += "   \n"
    }
    var countUltimaLinha = 0
    tabuleiro += "      "
    do {
        tabuleiro += if(countUltimaLinha == matrixTerrain[0].size - 1){
            "   "
        }else{ "    " }
        countUltimaLinha++
    }while(countUltimaLinha != matrixTerrain[0].size)

    return tabuleiro
}

fun terrainSemLegenda(matrixTerrain: Array<Array<Pair<String, Boolean>>>) : String{
    var tabuleiro = ""
    for (linha in 0 until matrixTerrain.size) {
        if(linha > 0){
            var countLinhas = 0
            do {
                tabuleiro += if (countLinhas != matrixTerrain[linha].size - 1){
                    "---+"
                }else{ "---" }
                countLinhas ++
            }while(countLinhas != matrixTerrain[linha].size)
            tabuleiro += "\n"
        }
        for (coluna in 0 until matrixTerrain[linha].size) {
            tabuleiro += if(matrixTerrain[linha][coluna].second){
                " ${matrixTerrain[linha][coluna].first} "
            }else{
                "   "
            }
            if (coluna != matrixTerrain[linha].size - 1){
                tabuleiro += "|"
            }
        }
        if(linha != matrixTerrain.size - 1){
            tabuleiro += "\n"
        }
    }
    return tabuleiro
}

fun makeTerrain(matrixTerrain: Array<Array<Pair<String, Boolean>>>, showLegend: Boolean, withColor: Boolean, showEverything: Boolean ): String {
    val tabuleiro: String
    if (showLegend){
       tabuleiro = terrainLegenda(matrixTerrain)
    }else{
       tabuleiro = terrainSemLegenda(matrixTerrain)
    }

    return tabuleiro
}

fun isNumberValid(number: String): Boolean {
    return number in "0".."9"
}

fun createLegend(numColumns: Int): String {
    var count = 0
    val alfabeto =
        "A   B   C   D   E   F   G   H   I   J   K   L   M   N   O   P   Q   R   S   T   U   V   W   X   Y   Z"
    var legenda = ""
    do {
        legenda += alfabeto[count]
        count++
    } while (count != (numColumns * 4) - 3)
    return legenda
}

fun mostraLegenda(message: String): Boolean? {
    return if (message == "y" || message == "Y") {
        true
    } else if (message == "n" || message == "N") {
        false
    } else {
        null
    }
}

fun calculateNumMinesForGameConfiguration(numLines: Int, numColumns: Int): Int? {
    return when (numLines * numColumns - 2) {
        in 14..20 -> {
            6
        }
        in 21..40 -> {
            9
        }
        in 41..60 -> {
            12
        }
        in 61..79 -> {
            19
        }
        else -> null
    }
}

fun isValidGameMinesConfiguration(numLines: Int, numColumns: Int, numMines: Int): Boolean {
    val casaVazias: Int = numLines * numColumns - 2
    return if (numMines <= 0) {
        false
    } else casaVazias >= numMines
}

fun getCoordinates(readText: String?): Pair<Int, Int>? {
    var coluna = 0
    var linha = 0
    if (readText?.length == 2) {
        if (readText[0] in '0'..'9' && (readText[1] in 'A'..'I' || readText[1] in 'a'..'i')) {
            when (readText[0]) {
                '1' -> linha = 0
                '2' -> linha = 1
                '3' -> linha = 2
                '4' -> linha = 3
                '5' -> linha = 4
                '6' -> linha = 5
                '7' -> linha = 6
                '8' -> linha = 7
                '9' -> linha = 8
            }
            when (readText[1]) {

                'A' -> coluna = 0
                'a' -> coluna = 0
                'B' -> coluna = 1
                'b' -> coluna = 1
                'C' -> coluna = 2
                'c' -> coluna = 2
                'D' -> coluna = 3
                'd' -> coluna = 3
                'E' -> coluna = 4
                'e' -> coluna = 4
                'F' -> coluna = 5
                'f' -> coluna = 5
                'G' -> coluna = 6
                'g' -> coluna = 6
                'H' -> coluna = 7
                'h' -> coluna = 7
                'i' -> coluna = 8
                'I' -> coluna = 8
            }
            return Pair(linha, coluna)
        } else return null
    } else return null

}

fun isCoordinateInsideTerrain(coord: Pair<Int, Int>, numColumns: Int, numLines: Int): Boolean {
    return coord.first in 0..numLines - 1 && coord.second in 0..numColumns - 1 && coord != Pair(numLines, numColumns)
}

fun isMovementPValid(currentCoord: Pair<Int, Int>, targetCoord: Pair<Int, Int>): Boolean {

    return when {
        targetCoord.first == currentCoord.first + 1 && targetCoord.second == currentCoord.second + 1 -> true
        targetCoord.first == currentCoord.first - 1 && targetCoord.second == currentCoord.second + 1 -> true
        targetCoord.first == currentCoord.first + 1 && targetCoord.second == currentCoord.second - 1 -> true
        targetCoord.first == currentCoord.first + 1 && targetCoord.second == currentCoord.second -> true
        targetCoord.first == currentCoord.first && targetCoord.second == currentCoord.second + 1 -> true
        targetCoord.first == currentCoord.first && targetCoord.second == currentCoord.second - 1 -> true
        targetCoord.first == currentCoord.first - 1 && targetCoord.second == currentCoord.second -> true
        targetCoord.first == currentCoord.first - 1 && targetCoord.second == currentCoord.second - 1 -> true
        else -> false
    }
}

fun getSquareAroundPoint(linha: Int, coluna: Int, numLines: Int, numColumns: Int ): Pair<Pair<Int, Int>, Pair<Int, Int>> {

    val yLeft = Math.max(linha -1 , 0)
    val yRight = Math.min(linha + 1 , numLines - 1)
    val xLeft = Math.max(0 , coluna - 1)
    val xRight = Math.min(coluna + 1,numColumns - 1)

    return Pair(Pair(yLeft, xLeft), Pair(yRight, xRight))
}

fun isEmptyAround(matrixTerrain: Array<Array<Pair<String, Boolean>>>, centerY: Int, centerX: Int, yl: Int, xl: Int, yr: Int, xr: Int ): Boolean {
    for (linha in yl..yr) {
        for (coluna in xl..xr) {
            if(matrixTerrain[linha][coluna].first != " " && matrixTerrain[linha][coluna] != matrixTerrain[centerY][centerX]){
                return false
            }
        }
    }
    return true
}

fun revealMatrix(matrixTerrain: Array<Array<Pair<String, Boolean>>>, coordY: Int, coordX: Int, endGame: Boolean = false) {
    val pontos = getSquareAroundPoint(coordY,coordX,matrixTerrain.size,matrixTerrain[0].size).first
    val pontos2 = getSquareAroundPoint(coordY,coordX,matrixTerrain.size,matrixTerrain[0].size).second
    if(!endGame) {
        for (linha in pontos.first..pontos2.first) {
            for (coluna in pontos.second..pontos2.second) {
                if (matrixTerrain[linha][coluna].first != "*") {
                    matrixTerrain[linha][coluna] = Pair(matrixTerrain[linha][coluna].first, true)
                }
            }
    }
    }else{
        for (linha in pontos.first..pontos2.first) {
            for (coluna in pontos.second..pontos2.second) {
                matrixTerrain[linha][coluna] =
                    Pair(matrixTerrain[linha][coluna].first, true)
            }
            }
}
}

fun countNumberOfMinesCloseToCurrentCell(matrixTerrain: Array<Array<Pair<String, Boolean>>>, centerY: Int, centerX: Int ): Int {
        val pontos = getSquareAroundPoint(centerY,centerX,matrixTerrain.size,matrixTerrain[centerY].size).first
        val pontos2 = getSquareAroundPoint(centerY,centerX,matrixTerrain.size,matrixTerrain[centerY].size).second
        var minasvizinhas = 0
        for (linha in pontos.first..pontos2.first) {
            for (coluna in pontos.second..pontos2.second) {
                val celula = matrixTerrain[linha][coluna]
                if (celula.first == "*") {
                    minasvizinhas += 1
                }
            }
            if(matrixTerrain[3][0].first == "*"){
                minasvizinhas += 1
            }
        }
        return minasvizinhas
    }

fun fillNumberOfMines(matrixTerrain: Array<Array<Pair<String, Boolean>>>) {
    for (linha in 0 until matrixTerrain.size) {
        for (coluna in 0 until matrixTerrain[0].size) {
            if (matrixTerrain[linha][coluna].first == " " &&
                countNumberOfMinesCloseToCurrentCell(matrixTerrain,linha,coluna) > 0) {
                matrixTerrain[linha][coluna] =
                    Pair(countNumberOfMinesCloseToCurrentCell(matrixTerrain, linha, coluna).toString(),false)
            }
        }
    }

}

fun createMatrixTerrain(numLines: Int, numColumns: Int, numMines: Int, ensurePathToWin: Boolean): Array<Array<Pair<String,Boolean>>> {
    val tabuleiro = Array(numLines) { Array(numColumns) { Pair(" ", false) } }
    var count = 0
    tabuleiro[0][0] = Pair("P", true)
    tabuleiro[numLines - 1][numColumns - 1] = Pair("f",true)
    if (!ensurePathToWin){
        do {
            val linha = (0 until numLines).random()
            val coluna = (0 until numColumns).random()

            if(tabuleiro[linha][coluna].first == " "){
                tabuleiro[linha][coluna] = Pair("*", false)
                count++
            }
        }while(count < numMines)
    }else{
        do {
            val linha = (0 until numLines).random()
            val coluna = (0 until numColumns).random()
            val superiorEsquerdo = getSquareAroundPoint(linha,coluna,tabuleiro.size,tabuleiro[0].size).first
            val inferiorDireito = getSquareAroundPoint(linha,coluna,tabuleiro.size,tabuleiro[0].size).second
            if(tabuleiro[linha][coluna].first == " " ){
                    if(isEmptyAround(tabuleiro,linha,coluna,
                            superiorEsquerdo.first,superiorEsquerdo.second,inferiorDireito.first,inferiorDireito.second)){
                tabuleiro[linha][coluna] = Pair("*", false)
                count++
                    }
            }
        }while(count < numMines)
    }
    return tabuleiro
}

fun main() {
    do {
        println(makeMenu())
        val opcao = readLine()
        when (opcao) {
            "1" -> {
                game()
                return
            }
            "0" -> return
            else -> invalid()
        }
    } while (opcao != "1" || opcao != "0")
}









