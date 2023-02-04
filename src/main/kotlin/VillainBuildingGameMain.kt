import mu.KotlinLogging

val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        logger.info { "Program arguments: ${args.joinToString()}" }
    } else {
        logger.info { "No program arguments." }
    }

    Game().run()
}