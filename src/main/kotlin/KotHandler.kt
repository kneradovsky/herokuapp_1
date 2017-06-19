import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ratpack.handling.Handler;
import ratpack.handling.Context;

class KotHandler : Handler {
	val log: Logger = LoggerFactory.getLogger(KotHandler::class.java)
	override fun handle(ctx: Context?) {
		if (ctx != null) {
			val storage:CardsStorage = ctx.get(CardsStorage::class.java)
			var oper = ctx.pathTokens["operation"]
			when(oper) {
				"resetstorage" -> {
					storage.resetStorage()
					ctx.response.send("Data reset")
				}
				null -> {
					log.debug("null operation")
					ctx.getResponse().send("Kotlin Hello")
				}
				else -> {
					log.debug("unknown operation")
					ctx.getResponse().send("Unknown operation")

				}

			}
		}
	}

 }