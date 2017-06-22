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
            var account = storage.accounts.find { it.account_id.toString().equals(account_id) }
            if(account==null) return ctx.response.status(404).send("account is not found");
            when(operation) {
                "rename" -> renameAccount(ctx,account)
                "delete" -> deleteAccount(ctx,account.account_id)
                else -> ctx.response.status(400).send("Operation is not supported");
            }
        }
    }
    fun renameAccount(ctx: Context,account: AccountData) {
        val form = ctx.parse(Form::class.java)
        form.then {
            val newname = it["name"]
            if(newname.isNullOrEmpty())  ctx.response.status(400).send("Invalid parameters")
            else  {
                account.title=newname!!
                account.title_small=newname!!
                ctx.render(json(account))
            };
        }
    }

    fun deleteAccount(ctx: Context,accountId:Int) {
        val storage:CardsStorage = ctx.get(CardsStorage::class.java)
        if(storage.deleteAccount(accountId))
            ctx.response.status(204).send("account deleted")
        else ctx.response.status(404).send("account not deleted")
    }
}