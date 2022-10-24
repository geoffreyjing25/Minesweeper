package hu.ait.minesweeper.model

import kotlin.random.Random.Default.nextInt

object MinesweeperModel {
    const val EMPTY: Short = 0
    const val FLAG: Short = 1
    const val BOMB: Short = 2
    const val ROWS = 8
    const val COLS = 8
    var flagOn: Boolean = false
    private const val bombCount = 5
    private var bombsLeft = bombCount

    private val model = arrayOf(
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY))

    private val display = arrayOf(
        intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1),
        intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1),
        intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1),
        intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1),
        intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1),
        intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1),
        intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1),
        intArrayOf(-1, -1, -1, -1, -1, -1, -1, -1))

    init {
        initializeBombs()
    }

    fun setFlag(flagOn: Boolean) {
        this.flagOn = flagOn
    }

    private fun initializeBombs() {
        val randomIndices = getRandomCells()
        for (idx in randomIndices) {
            val pair = getCoordinateFromIndex(idx)
            model[pair.first][pair.second] = BOMB
        }
        bombsLeft = bombCount
    }

    private fun getRandomCells() : MutableSet<Int> {
        val set = mutableSetOf<Int>()
        while (set.size < bombCount) {
            set.add(nextInt(0, 63))
        }
        return set
    }

    private fun getCoordinateFromIndex(i: Int) : Pair<Int, Int> {
        val col = (i % COLS)
        val row = Math.floorDiv(i, ROWS)
        return Pair(row, col)
    }

    fun getBombsLeft() : Int {
        return bombsLeft
    }

    fun decrementBombCount() {
        bombsLeft -= 1
    }

    fun resetModel() {
        for (i in 0 until ROWS) {
            for (j in 0 until COLS) {
                model[j][i] = EMPTY
                display[j][i] = -1
            }
        }
        initializeBombs()
    }

    fun getFieldContent(x: Int, y: Int) = model[y][x]

    fun setFlag(x: Int, y: Int) {
        model[y][x] = FLAG
    }

    fun getDisplay(x: Int, y: Int) : Int {
        return display[y][x]
    }

    fun setDisplay(x: Int, y: Int, count: Int) {
        display[y][x] = count
    }

    fun getNeighboringValue(x: Int, y: Int) : Int {
        var count : Int = 0
        for (i in x-1..x+1) {
            for (j in y-1..y+1) {
                if (i in 0 until COLS && j in 0 until ROWS && (model[j][i] == FLAG || model[j][i] == BOMB)) {
                    count += 1
                }
            }
        }
        return count
    }

     fun expand(x: Int, y: Int) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8 || display[y][x] != -1 || model[y][x] != EMPTY) return
        val bombCount = getNeighboringValue(x, y)
        display[y][x] = bombCount
        if (bombCount == 0) {
            expand(x+1, y)
            expand(x-1, y)
            expand(x, y+1)
            expand(x, y-1)
            expand(x-1, y-1)
            expand(x+1, y+1)
            expand(x+1, y-1)
            expand(x-1, y+1)
        }
    }

    fun displayAll() {
        for (i in 0 until ROWS) {
            for (j in 0 until COLS) {
                if (display[j][i] == -1) {
                    display[j][i] = 0
                }
            }
        }
    }

}