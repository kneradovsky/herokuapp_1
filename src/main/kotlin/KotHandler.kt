import ratpack.handling.Handler;
import ratpack.handling.Context;

class KotHandler : Handler {
	override fun handle(ctx: Context?) {
        if(ctx!=null)
		    ctx.getResponse().send("Kotlin Hello")
	}


 }