import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.http.Status
import ratpack.jackson.Jackson.json

/**
 * Created by vk on 27.09.16.
 */
class AccGetHandler : Handler {
    override fun handle(ctx: Context?) {
        if(ctx!=null) {
            val storage:CardsStorage = ctx.get(CardsStorage::class.java)
            val account_id = ctx.pathTokens["account"];
            if(account_id.isNullOrEmpty()) {
                ctx.render(json(storage.accounts))
            } else {
                val account = storage.accounts.filter { it.account_id.toString().equals(account_id) }.firstOrNull();
                if(account == null) 
                    ctx.response.status(404).send("account not found");
                else ctx.render(json(account))
            }
        }
    }
}