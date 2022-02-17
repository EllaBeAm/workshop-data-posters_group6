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
import org.openrndr.extensions.Screenshots
import org.openrndr.extra.compositor.*
import org.openrndr.extra.fx.blend.Multiply
import org.openrndr.extra.fx.blur.ApproximateGaussianBlur
import org.openrndr.extra.fx.color.Duotone
import org.openrndr.extra.fx.distort.Perturb
import org.openrndr.extra.fx.patterns.Checkers
import org.openrndr.extra.fx.shadow.DropShadow
import org.openrndr.extra.gui.GUI
import org.openrndr.extra.gui.addTo
import org.openrndr.extra.noise.uniform
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
            var background = ColorRGBa.WHITE
            onNewArticle.listen {
                background = ColorRGBa.WHITE
            }


            //original photo
            layer {

                // here we create variables that we will use to randomize the settings of StackRepeat
                var xo = 0.0
                var yo = 0.0
                var xOptions = listOf(30.00, 291.66)
                var yOptions = listOf(30.00, 276.66, 523.32)
                var xx = 0.0
                var yy = 0.0


                // listen for a new article event and randomize
                onNewArticle.listen {
                    xo = Double.uniform(-0.25, 0.25)
                    yo = Double.uniform(-0.25, 0.25)
                    xx = xOptions.random()
                    yy = yOptions.random()
                }

                draw {

                    drawer.imageFit(article.images[0], xx, yy, 246.66, 246.66)
                }
            }

            //text
            layer {
                val font = loadFont("data/fonts/GT-America-Medium.otf", 48.0)
                draw {
                    if (article.texts.isNotEmpty()) {
                        val stats = article.imageStatistics[0]
                        drawer.fill = ColorRGBa.BLACK
                        drawer.fontMap = font
                        writer {
                            box = Rectangle(31.0, 30.0, 271.0, height - 80.0)
                            gaplessNewLine()
                            text(article.texts[0])
                        }
                    }
                }
            }

            //pixel miracle
            layer {

                var xs = 2
                var ys = 2

                onNewArticle.listen {
                    xs = Int.uniform(2, 100)
                    ys = Int.uniform(2, 100)
                }

                draw {
                    if (article.images.isNotEmpty()) {
                        drawer.imageFit(article.images[0], 0.0, 0.0, width * 1.0, height * 1.0)
                    }
                }

                post(Perturb()) {
                    phase = seconds * 0.1
                    xSegments = xs
                    ySegments = ys

                } .addTo(gui)

//                mask {
//                    drawer.imageFit(article.images[1], 30.0, 50.0, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 167.5,50.0, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 305.0, 50.0, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 442.5, 50.0, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 30.0, 232.5, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 167.5, 232.5, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 305.0, 232.5, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 442.5, 232.5, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 30.0, 415.0, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 167.5, 415.0, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 305.0, 415.0, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 442.5, 415.0, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 30.0, 597.5, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 167.5, 597.5, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 305.0, 597.5, 127.5, 172.5)
//                    drawer.imageFit(article.images[1], 442.5, 597.5, 127.5, 172.5)
//
//                }
                //blend(Multiply())
            }
            post(Duotone()) {
                this.backgroundColor = ColorRGBa.BLACK
                this.foregroundColor = ColorRGBa.WHITE
            }

        }


        onNewArticle.trigger(article)

        gui.doubleBind = true
        gui.add(settings)
        extend(gui)
        extend(Screenshots()).apply {
            this.key = "s"
        }
        extend {
            gui.visible = mouse.position.x < 200
            drawer.clear(ColorRGBa.WHITE)
            composite.draw(drawer)


        }

    }
}