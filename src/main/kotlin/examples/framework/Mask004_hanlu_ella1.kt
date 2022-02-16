package examples.framework

import FilmGrain
import archives.LoadedArticle
import archives.localArchive

import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.color.rgb
import org.openrndr.draw.loadFont
import org.openrndr.draw.tint
import org.openrndr.events.Event
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.blend.Multiply
import org.openrndr.extra.fx.blur.ApproximateGaussianBlur
import org.openrndr.extra.fx.color.Duotone
import org.openrndr.extra.fx.distort.Perturb
import org.openrndr.extra.fx.patterns.Checkers
import org.openrndr.extra.fx.shadow.DropShadow
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.gui.addTo
import org.openrndr.extra.parameters.ActionParameter
import org.openrndr.extra.parameters.Description
import org.openrndr.extras.imageFit.imageFit
import org.openrndr.shape.Rectangle
import org.openrndr.writer

// This demonstrates layer masking

fun main() = application {
    configure {
        width = 600
        height = 800



    }

    program {
        val archive = localArchive("archives/example-poetry").iterator()
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
            var background = ColorRGBa.WHITE
            onNewArticle.listen {
                background = ColorRGBa.WHITE
            }



            layer {
                draw {
                    if (article.images.isNotEmpty()) {
                        drawer.imageFit(article.images[0], 30.0, 50.0, width-60.0 * 1.0, height * 1.0-150.0)
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
                    drawer.circle(width/2.0, height/2.0, 220.0)
                }
                blend(Multiply())
            }
            post(Duotone()) {
                this.backgroundColor = ColorRGBa.BLACK
                this.foregroundColor = ColorRGBa.WHITE
            }

        }


        onNewArticle.trigger(article)

        gui.add(settings)
        extend(gui)
        extend {
            drawer.clear(ColorRGBa.WHITE)
            composite.draw(drawer)


        }

    }
}