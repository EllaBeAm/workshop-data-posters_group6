package work

import archives.LoadedArticle
import archives.localArchive
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.rgb
import org.openrndr.draw.loadFont
import org.openrndr.events.Event
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.blur.ApproximateGaussianBlur
import org.openrndr.extra.fx.color.Duotone
import org.openrndr.extra.fx.shadow.DropShadow
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.gui.addTo
import org.openrndr.extra.parameters.ActionParameter
import org.openrndr.extra.parameters.Description
import org.openrndr.extras.imageFit.imageFit
import org.openrndr.shape.Rectangle
import org.openrndr.writer

fun main() = application {
    configure {
        width = 600
        height = 800
    }
    program {
        val archive = localArchive("archives/faces").iterator()
        var article = archive.next()
        val gui = GUI()

        val onNewArticle = Event<LoadedArticle>()
        val settings = @Description("Settings") object {
            @ActionParameter("Next article")
            fun nextArticle() {
                article = archive.next()
                onNewArticle.trigger(article)
            }
        }

        val composite = compose {
            var background = ColorRGBa.PINK
            onNewArticle.listen {
                background = rgb(Math.random(), Math.random(), Math.random())
            }


            // -- image layer
            layer {
                draw {

                        drawer.imageFit(article.images[0], 0.0, 0.0, width * 1.0, height * 1.0)

                }
            }

            layer {
                draw {

                    drawer.imageFit(article.images[0], 0.0, 0.0, width * 1.0, height * 1.0)

                }
                post(ApproximateGaussianBlur()).addTo(gui)
                mask {
                    drawer.imageFit(article.images[1], 0.0, 0.0, width * 1.0, height * 1.0)

                }

            }

        }
        onNewArticle.trigger(article)

        gui.add(settings)
        extend(gui)
        extend {
            gui.visible = mouse.position.x < 200.0
            composite.draw(drawer)
        }
    }
}