package hu.ait.minesweeper.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import hu.ait.minesweeper.R
import android.view.View
import hu.ait.minesweeper.MainActivity
import hu.ait.minesweeper.model.MinesweeperModel

class MineSweeperView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var paintBackGround = Paint()
    var paintLine = Paint()
    var paintBombs = Paint()
    var paintCell = Paint()


    var bombBitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.bomb)

    var bombExplodeBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.explodedbomb)

    var hiddenBitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.hidden)

    var numOneBitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.one)

    var numTwoBitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.two)

    var numThreeBitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.three)

    var numFourBitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.four)

    var numFiveBitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.five)

    var flagBitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.flag)

    var emptyBitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.empty)

    init {
        paintBackGround.color = Color.GRAY
        paintBackGround.style = Paint.Style.FILL

        paintCell.color = Color.GRAY
        paintCell.style = Paint.Style.FILL

        paintLine.color = Color.WHITE
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 3f

        paintBombs.textSize = 100f
        paintBombs.color = Color.WHITE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setBitmaps()
    }

    private fun setBitmaps() {
        val scaleWidth = width/MinesweeperModel.COLS
        val scaleHeight = height/MinesweeperModel.ROWS
        setBombsBitmap(scaleWidth, scaleHeight)
        setNumbersBitmap(scaleWidth, scaleHeight)
        setOtherBitmap(scaleWidth, scaleHeight)
    }

    private fun setBombsBitmap(scaleWidth : Int, scaleHeight : Int) {
        bombBitmap = Bitmap.createScaledBitmap(bombBitmap, scaleWidth, scaleHeight, false)
        bombExplodeBitmap = Bitmap.createScaledBitmap(bombExplodeBitmap, scaleWidth, scaleHeight, false)
    }

    private fun setNumbersBitmap(scaleWidth : Int, scaleHeight : Int) {
        numOneBitmap = Bitmap.createScaledBitmap(numOneBitmap, scaleWidth, scaleHeight, false)
        numTwoBitmap = Bitmap.createScaledBitmap(numTwoBitmap, scaleWidth, scaleHeight, false)
        numThreeBitmap = Bitmap.createScaledBitmap(numThreeBitmap, scaleWidth, scaleHeight,false)
    }

    private fun setOtherBitmap(scaleWidth : Int, scaleHeight : Int) {
        hiddenBitmap = Bitmap.createScaledBitmap(hiddenBitmap, scaleWidth, scaleHeight, false)
        emptyBitmap = Bitmap.createScaledBitmap(emptyBitmap, scaleWidth, scaleHeight, false)
        flagBitmap = Bitmap.createScaledBitmap(flagBitmap, scaleWidth, scaleHeight, false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBackGround)
        drawGame(canvas)
        drawCurrentState(canvas)
    }

    private fun drawGame(canvas: Canvas) {
        drawBorder(canvas)
        drawRows(canvas)
        drawCols(canvas)
        drawHiddenCells(canvas)
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)
    }

    private fun drawRows(canvas: Canvas) {
        for (i in 1 until MinesweeperModel.ROWS) {
            canvas.drawLine(
                0f,
                (i * height / MinesweeperModel.ROWS).toFloat(),
                width.toFloat(),
                (i * height / MinesweeperModel.COLS).toFloat(),
                paintLine
            )
        }
    }
    private fun drawCols(canvas: Canvas) {
        for (i in 1 until MinesweeperModel.COLS) {
            canvas.drawLine(
                (i * width / MinesweeperModel.COLS).toFloat(),
                0f,
                (i * width / MinesweeperModel.COLS).toFloat(),
                height.toFloat(),
                paintLine
            )
        }
    }

    private fun drawHiddenCells(canvas: Canvas) {
        for (i in 0 until MinesweeperModel.COLS) {
            for (j in 0 until MinesweeperModel.ROWS) {
                canvas?.drawBitmap(hiddenBitmap, (i * width / MinesweeperModel.COLS).toFloat(), (j * height / MinesweeperModel.ROWS).toFloat(), null)
            }
        }
    }

    private fun drawCurrentState(canvas: Canvas) {
        for (x in 0 until MinesweeperModel.COLS) {
            for (y in 0 until MinesweeperModel.ROWS) {
                if (MinesweeperModel.getDisplay(x, y) != -1) {
                    drawExposedCell(x, y, canvas)
                } else {
                    canvas?.drawBitmap(hiddenBitmap, (x * width / MinesweeperModel.COLS).toFloat(), (y * height / MinesweeperModel.ROWS).toFloat(), null)
                }
            }
        }
    }

    private fun drawExposedCell(x: Int, y: Int, canvas: Canvas) {
        val content = MinesweeperModel.getFieldContent(x, y)
        if (content == MinesweeperModel.FLAG) {
            canvas?.drawBitmap(flagBitmap, (x * width / MinesweeperModel.COLS).toFloat(), (y * height / MinesweeperModel.ROWS).toFloat(), null)
        } else if (content == MinesweeperModel.BOMB) {
            if (MinesweeperModel.getDisplay(x, y) != -2) canvas?.drawBitmap(bombBitmap, (x * width / MinesweeperModel.COLS).toFloat(), (y * height / MinesweeperModel.ROWS).toFloat(), null)
            else canvas?.drawBitmap(bombExplodeBitmap, (x * width / MinesweeperModel.COLS).toFloat(), (y * height / MinesweeperModel.ROWS).toFloat(), null)
        } else {
            val bombCount = MinesweeperModel.getDisplay(x, y)
            drawBombCount(canvas, x, y, bombCount)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = View.MeasureSpec.getSize(widthMeasureSpec)
        val h = View.MeasureSpec.getSize(heightMeasureSpec)
        val d = if (w == 0) h else if (h == 0) w else if (w < h) w else h
        setMeasuredDimension(d, d)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_DOWN) {
            val tX = event.x.toInt() / (width / MinesweeperModel.COLS)
            val tY = event.y.toInt() / (height / MinesweeperModel.ROWS)
            if (tX < MinesweeperModel.COLS && tY < MinesweeperModel.ROWS) {
                if (MinesweeperModel.getDisplay(tX, tY) == -1) {
                    handleClick(tX, tY)
                }
            }
        }
        return true
    }

    private fun handleClick(tX: Int, tY: Int) {
        if (MinesweeperModel.flagOn) {
            handleFlag(tX, tY)
        } else {
            if (MinesweeperModel.getFieldContent(tX, tY) == MinesweeperModel.BOMB) {
                handleBomb(tX, tY)
            } else {
                handleEmptyClick(tX, tY)
            }
        }
        invalidate()
    }

    private fun handleBomb(tX: Int, tY: Int) {
        MinesweeperModel.setDisplay(tX, tY, -2)
        endGame(false)
    }

    private fun handleEmptyClick(tX: Int, tY: Int) {
        val neighborBombCount = MinesweeperModel.getNeighboringValue(tX, tY)
        if (neighborBombCount == 0) {
            MinesweeperModel.expand(tX, tY)
        } else {
            MinesweeperModel.setDisplay(tX, tY, neighborBombCount)
        }
    }

    private fun handleFlag(tX: Int, tY: Int) {
        if (MinesweeperModel.getFieldContent(tX, tY) == MinesweeperModel.EMPTY) {
            endGame(false)
        } else if (MinesweeperModel.getFieldContent(tX, tY) == MinesweeperModel.BOMB) {
            MinesweeperModel.setFlag(tX, tY)
            MinesweeperModel.setDisplay(tX, tY, 0)
            MinesweeperModel.decrementBombCount()
            if (MinesweeperModel.getBombsLeft() == 0) {
                endGame(true)
            }
        }
    }

    fun setFlag(flagOn: Boolean) {
        MinesweeperModel.setFlag(flagOn)
    }

    private fun drawBombCount(canvas: Canvas, x: Int, y: Int, count: Int) {
        if (count == 0) {
            canvas?.drawBitmap(emptyBitmap, (x * width / MinesweeperModel.COLS).toFloat(), (y * height / MinesweeperModel.ROWS).toFloat(), null)
        } else if (count == 1) {
            canvas?.drawBitmap(numOneBitmap, (x * width / MinesweeperModel.COLS).toFloat(), (y * height / MinesweeperModel.ROWS).toFloat(), null)
        } else if (count == 2) {
            canvas?.drawBitmap(numTwoBitmap, (x * width / MinesweeperModel.COLS).toFloat(), (y * height / MinesweeperModel.ROWS).toFloat(), null)
        } else if (count == 3) {
        canvas?.drawBitmap(numThreeBitmap, (x * width / MinesweeperModel.COLS).toFloat(), (y * height / MinesweeperModel.ROWS).toFloat(), null)
        } else if (count == 4) {
            canvas?.drawBitmap(numFourBitmap, (x * width / MinesweeperModel.COLS).toFloat(), (y * height / MinesweeperModel.ROWS).toFloat(), null)
        } else {
            canvas?.drawBitmap(numFiveBitmap, (x * width / MinesweeperModel.COLS).toFloat(), (y * height / MinesweeperModel.ROWS).toFloat(), null)
        }
    }

    private fun endGame(win: Boolean) {
        (context as MainActivity).isWon(win)
        MinesweeperModel.displayAll()
    }

    fun resetGame() {
        MinesweeperModel.resetModel()
        invalidate()
    }
}
