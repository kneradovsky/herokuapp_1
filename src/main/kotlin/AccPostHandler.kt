import ratpack.form.Form
import ratpack.handling.Context
import ratpack.handling.Handler
import ratpack.jackson.Jackson.json


/**
 * Created by vk on 27.09.16.
 */
class AccPostHandler : Handler {
    override fun handle(ctx: Context?) {
        if(ctx!=null) {
            val storage:CardsStorage = ctx.get(CardsStorage::class.java)
            val account_id = ctx.pathTokens["account"];
            val operation=ctx.pathTokens["operation"];
            if(account_id.isNullOrEmpty()) {
                return ctx.response.status(500).send("Invalid account")
            }
            if(operation.isNullOrEmpty()) return ctx.response.status(500).send("Operation is not set");
            if(!operation.equals("rename")) return ctx.response.status(400).send("Operation is not supported");
            var account = storage.accounts.find { it.account_id.toString().equals(account_id) }
            if(account==null) return ctx.response.status(404).send("account is not found");
            val form = ctx.parse(Form::class.java)
            form.then {
                val newname = it["name"]
                if(newname.isNullOrEmpty())  ctx.response.status(400).send("Invalid parameters")
                else  {
                    account.title=newname!!
                    ctx.render(json(account))
                };

            }
        }
    }
}