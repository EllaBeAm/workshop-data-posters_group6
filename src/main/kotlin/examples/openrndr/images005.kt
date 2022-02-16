package examples

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolated
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.extras.imageFit.FitMethod
import org.openrndr.extras.imageFit.imageFit
import org.openrndr.shape.Rectangle
import org.openrndr.writer
import kotlin.math.cos

/**
 * This demonstrates imageFit, a much easier way for placing images on the screen.
 * In this example we make an image composition but manually placing images in the window
 */

fun main() = application {
    configure {
        width = 400
        height = 500
    }
    program {
        val image = loadImage("data/images/image.jpg")



        extend {



            val shiftX = (mouse.position.x / width) * 2.0 - 1.0
            val shiftY = (mouse.position.y / height) * 2.0 - 1.0

            drawer.imageFit(image, 00.0, 0.0, 400.0, 500.0, 100.0, shiftY)
            drawer.imageFit(image, 0.0, 0.0, 368.0, 500.0, shiftX, shiftY)
            drawer.imageFit(image, 0.0, seconds * 5.0, 320.0, 500.0, shiftX, shiftY)
            drawer.imageFit(image, 0.0, 100.0, 280.0, 350.0, shiftX, shiftY)
            drawer.imageFit(image, 0.0, 0.0, 100.0, 100.0, shiftX, shiftY)

            drawer.imageFit(image, seconds * 12.00, 0.0, 200.0, 100.0, seconds * 2.0, shiftY)
            drawer.imageFit(image, 200.00, seconds * 5.0, 100.0, 300.0, shiftX, shiftY)
            drawer.imageFit(image, seconds * 10.0, 200.0, seconds * 10.0, 100.0, 200.00, shiftY)


            drawer.imageFit(image, 100.00, 200.0, 100.0, 100.0, 200.00, shiftY)
        }




                }
            }
