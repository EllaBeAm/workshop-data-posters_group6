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
import org.openrndr.extra.shapes.grid
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

        var xOptions = listOf(0.00, 142.50, 142.50*2.0)
        var yOptions = listOf(0.00, 192.50, 192.50*2.0)

        //position for the large image
        var xx = 0.0
        var yy = 0.0
        //position for the large text block
        var xl = 0.0
        var yl = 0.0
        //position for the small text block
        var xs = 0.0
        var ys = 0.0

        val composite = compose {
            var background = ColorRGBa.WHITE
            onNewArticle.listen {
                background = ColorRGBa.WHITE
            }

            //small images matrix
            layer {

                var xs = 2
                var ys = 2

                onNewArticle.listen {
                    xs = Int.uniform(2, 100)
                    ys = Int.uniform(2, 100)
                }

                draw {
                    if (article.images.isNotEmpty()) {
                        val g = drawer.bounds.grid(4, 4,15.0, 15.0).flatten()
                        for (r in g) {
                            drawer.imageFit(article.images[0], r)
                        }
                    }
                }
            }

            //distortion masks - for small images
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

                }.addTo(gui)

                mask {

                    val g = drawer.bounds.grid(4, 4).flatten()

                    for (r in g) {
                        drawer.imageFit(article.images[1], r)
                    }


                }
                //blend(Multiply())
            }

            //large image
            layer{

                xx = xOptions.random()
                yy = yOptions.random()

                draw {
                    if (article.images.isNotEmpty()) {
                        drawer.imageFit(article.images[0], xx+15.0, yy+15.0, 142.5*2.0, 192.5*2.0)
                    }
                }
            }

            //distortion masks- for large image
            layer {

                var xs = 2
                var ys = 2

                onNewArticle.listen {
                    xs = Int.uniform(2, 100)
                    ys = Int.uniform(2, 100)
                }

                draw {
                    if (article.images.isNotEmpty()) {
                        drawer.imageFit(article.images[0], xx+15.0, yy+15.0, 142.5*2.0, 192.5*2.0)
                    }
                }

                post(Perturb()) {
                    phase = seconds * 0.1
                    xSegments = xs
                    ySegments = ys

                }.addTo(gui)

                mask {
                    drawer.imageFit(article.images[1], xx+15.0, yy+15.0, 142.5*2.0, 192.5*2.0)


                }
                //blend(Multiply())
            }

            //large text block
            layer{

                xl = xOptions.random()
                yl = yOptions.random()

                val font = loadFont("data/fonts/GT-America-Medium.otf", 24.0)


                draw {
                    drawer.rectangle(xl + 15.0, yl + 15.0, 142.5 * 2.0, 192.5)
                    drawer.fill = ColorRGBa.WHITE

                    if (article.texts.isNotEmpty()) {
                        val stats = article.imageStatistics[0]
                        drawer.fill = ColorRGBa.BLACK
                        drawer.fontMap = font
                        writer {
                            leading = 0.0
                            box = Rectangle(xl + 27.0, yl + 40.0, 142.5 * 2.0 - 20.0, 192.5 - 15.0)
                            text(article.texts[0])
                        }
                    }
                }
            }

            //small text block
            layer{

                xs = xOptions.random()
                ys = yOptions.random()

                val font = loadFont("data/fonts/GT-America-Medium.otf", 11.0)


                draw {
                    drawer.rectangle(xs + 15.0, ys + 15.0, 142.5, 192.5)
                    drawer.fill = ColorRGBa.WHITE

                    if (article.texts.isNotEmpty()) {
                        val stats = article.imageStatistics[0]
                        drawer.fill = ColorRGBa.BLACK
                        drawer.fontMap = font
                        writer {
                            leading = 0.0
                            box = Rectangle(xs + 27.0, ys + 27.0, 142.5 - 20.0, 192.5 - 15.0)
                            text("Prosopagnosia")
                        }
                    }
                }
            }

            post(Duotone()) {
                this.backgroundColor = ColorRGBa.BLACK
                this.foregroundColor = ColorRGBa.WHITE
            }

//          post(FilmGrain())




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