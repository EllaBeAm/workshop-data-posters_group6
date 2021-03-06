package examples

import org.openrndr.application
import org.openrndr.draw.loadImage

/**
 * This demonstrates the most basic way of drawing images
 */

fun main() = application {

    configure {
        width = 400
        height = 100
    }
    program {
         val image = loadImage("data/images/pm5544.png")

        extend {
            drawer.image(image)
        }
    }
}