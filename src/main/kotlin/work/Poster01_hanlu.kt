package work

import FilmGrain
import archives.LoadedArticle
import archives.localArchive
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.rgb
import org.openrndr.draw.loadFont
import org.openrndr.events.Event
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.blend.ColorDodge
import org.openrndr.extra.fx.blend.Multiply
import org.openrndr.extra.fx.blur.ApproximateGaussianBlur
import org.openrndr.extra.fx.color.Duotone
import org.openrndr.extra.fx.distort.Perturb
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
                    if (article.images.isNotEmpty()) {
                        drawer.imageFit(article.images[0], 0.0, 0.0, width * 1.0, height * 1.0)
                    }
                }

            }

            layer {
                draw {
                    if (article.images.isNotEmpty()) {
                        drawer.imageFit(article.images[0], 0.0, 0.0, width * 1.0, height * 1.0)
                    }
                }

                post(Perturb()).addTo(gui)
                mask {
                    drawer.imageFit(article.images[1], 0.0, 0.0, width * 1.0, height * 1.0)
                }
            }
//            post(Duotone()) {
//                this.backgroundColor = ColorRGBa.BLACK
//                this.foregroundColor = ColorRGBa.WHITE
//            }
            post(FilmGrain())
//            layer {
//                val font = loadFont("data/fonts/Jellyka CuttyCupcakes.ttf", 100.0)
//                draw {
//                    if (article.texts.isNotEmpty()) {
//                        val stats = article.imageStatistics[0]
//                        drawer.fill = ColorRGBa.GRAY
//                        drawer.fontMap = font
//                        writer {
//                            box = Rectangle(40.0, 40.0, width - 80.0, height - 80.0)
//                            gaplessNewLine()
//                            text(article.texts[0])
//                        }
//                    }
//                }
//                blend(ColorDodge())
//            }

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